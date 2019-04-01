package com.datingonline.meet.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.datingonline.meet.db.AppDatabase
import com.datingonline.meet.Model.User
import com.datingonline.meet.R
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
class MainInfoFragment : Fragment() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    private lateinit var nickEdit: EditText
    private lateinit var yearsSpinner: Spinner
    private lateinit var daysSpinner: Spinner
    private lateinit var monthsSpinner: Spinner
    private lateinit var raceSpinner: Spinner
    private lateinit var heightSeek: SeekBar
    private lateinit var weightSeek: SeekBar
    lateinit var heightText: TextView
    lateinit var weightText: TextView
    private lateinit var nextButton: Button
    lateinit var db: AppDatabase

    var user = User()
    private var birthDate = ""
    var userHeight = ""
    var userWeight = ""
    private var userId = 0


    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_info, container, false)

        nickEdit = view.findViewById(R.id.nick_edit_text)
        yearsSpinner = view.findViewById(R.id.years_spinner)
        daysSpinner = view.findViewById(R.id.days_spinner)
        monthsSpinner = view.findViewById(R.id.months_spinner)
        raceSpinner = view.findViewById(R.id.race_spinner)
        heightSeek = view.findViewById(R.id.height_seek_bar)
        weightSeek = view.findViewById(R.id.weight_seek_bar)
        heightText = view.findViewById(R.id.textView5)
        weightText = view.findViewById(R.id.textView8)
        nextButton = view.findViewById(R.id.first_next_button)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        Observable.fromCallable { db.userDao().getLastId() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userId = it
            }, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })



        weightSeek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                weightText.text = seekBar.progress.toString()
                userWeight = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                weightText.text = seekBar.progress.toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                weightText.text = seekBar.progress.toString()
            }

        })

        heightSeek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                heightText.text = (seekBar.progress + 140).toString()
                userHeight = (seekBar.progress + 140).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                heightText.text = (seekBar.progress + 140).toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                heightText.text = (seekBar.progress + 140).toString()
            }

        })

        nextButton.setOnClickListener {
            userSet()
            if (userId != 0) {
                saveToDb()
                fragmentMain = SingleOrCoupleFragment()
                setFragment(fragmentMain)
            } else {
                Toast.makeText(this.requireContext(), "Подождите", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    @SuppressLint("CheckResult")
    fun userSet() {
        birthDate = daysSpinner.selectedItem.toString() + "/" + monthsSpinner.selectedItem.toString() + "/" + yearsSpinner.selectedItem.toString()
        user.setNick(nickEdit.text.toString())
        user.setBirth(birthDate)
        user.setRace(raceSpinner.selectedItem.toString())
        user.setHeight(userHeight)
        user.setWeight(userWeight)

    }

    @SuppressLint("CheckResult")
    fun saveToDb() {

        Completable.fromAction { db.userDao().updateNick(user.getNick(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateBirth(user.getBirth(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateRace(user.getRace(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateHeight(user.getHeight(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateWeight(user.getWeight(), userId) }
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
