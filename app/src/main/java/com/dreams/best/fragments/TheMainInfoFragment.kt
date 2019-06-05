package com.dreams.best.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.dreams.best.db.AppDatabase
import com.dreams.best.Model.User
import com.dreams.best.R
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class TheMainInfoFragment : Fragment() {

    private var the_fragmentMain = androidx.fragment.app.Fragment()
    private lateinit var the_nickEdit: EditText
    private lateinit var the_yearsSpinner: Spinner
    private lateinit var the_daysSpinner: Spinner
    private lateinit var the_monthsSpinner: Spinner
    private lateinit var the_raceSpinner: Spinner
    private lateinit var the_heightSeek: SeekBar
    private lateinit var the_weightSeek: SeekBar
    lateinit var the_heightText: TextView
    lateinit var the_weightText: TextView
    private lateinit var the_nextButton: Button
    lateinit var db: AppDatabase

    var the_user = User()
    private var the_birthDate = ""
    var the_userHeight = ""
    var the_userWeight = ""
    private var the_userId = 0


    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_the_main_info, container, false)

        the_nickEdit = view.findViewById(R.id.enter_nickname_edit_text)
        the_yearsSpinner = view.findViewById(R.id.enter_birth_years_spinner)
        the_daysSpinner = view.findViewById(R.id.enter_birth_days_spinner)
        the_monthsSpinner = view.findViewById(R.id.enter_birth_months_spinner)
        the_raceSpinner = view.findViewById(R.id.enter_race_spinner)
        the_heightSeek = view.findViewById(R.id.enter_height_sbar)
        the_weightSeek = view.findViewById(R.id.enter_weight_sbar)
        the_heightText = view.findViewById(R.id.enter_textView5_height)
        the_weightText = view.findViewById(R.id.enter_textView8_weight)
        the_nextButton = view.findViewById(R.id.enter_first_next_btn)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        Observable.fromCallable { db.userDao().getLastId() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                the_userId = it
            }, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })



        the_weightSeek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                the_weightText.text = seekBar.progress.toString()
                the_userWeight = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                the_weightText.text = seekBar.progress.toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                the_weightText.text = seekBar.progress.toString()
            }

        })

        the_heightSeek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                the_heightText.text = (seekBar.progress + 140).toString()
                the_userHeight = (seekBar.progress + 140).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                the_heightText.text = (seekBar.progress + 140).toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                the_heightText.text = (seekBar.progress + 140).toString()
            }

        })

        the_nextButton.setOnClickListener {
            userSet()
            if (the_userId != 0) {
                saveToDb()
                the_fragmentMain = CondishnsFragment()
                setFragment(the_fragmentMain)
            } else {
                Toast.makeText(this.requireContext(), "Подождите", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    @SuppressLint("CheckResult")
    fun userSet() {
        the_birthDate = the_daysSpinner.selectedItem.toString() + "/" + the_monthsSpinner.selectedItem.toString() + "/" + the_yearsSpinner.selectedItem.toString()
        the_user.setNick(the_nickEdit.text.toString())
        the_user.setBirth(the_birthDate)
        the_user.setRace(the_raceSpinner.selectedItem.toString())
        the_user.setHeight(the_userHeight)
        the_user.setWeight(the_userWeight)

    }

    @SuppressLint("CheckResult")
    fun saveToDb() {

        Completable.fromAction { db.userDao().updateNick(the_user.getNick(), the_userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateBirth(the_user.getBirth(), the_userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateRace(the_user.getRace(), the_userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateHeight(the_user.getHeight(), the_userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateWeight(the_user.getWeight(), the_userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
    }

    private fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = this.requireActivity().supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.question_container, f)
        ft.commit()

    }



}
