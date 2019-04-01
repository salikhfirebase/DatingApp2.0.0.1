package com.datingonline.meet.fragments


import android.annotation.SuppressLint
import android.graphics.Color
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

/**
 * A simple [Fragment] subclass.
 *
 */
class SingleOrCoupleFragment : Fragment() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton: RadioButton
    private lateinit var checkedHaveHouse: TextView
    private lateinit var checkedHaveCar: TextView
    private lateinit var checkedHaveMoto: TextView
    private lateinit var makeClear: EditText
    private lateinit var nextButton: Button
    private lateinit var checkedRadioText: String
    lateinit var db: AppDatabase
    private var otherData = ""
    private var houseChecked = ""
    private var carChecked = ""
    private var mohoChecked = ""
    private var userId = 0
    var user = User()

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_or_couple, container, false)

        radioGroup = view.findViewById(R.id.radioGroupLast)
        checkedHaveHouse = view.findViewById(R.id.have_house_text_view)
        checkedHaveCar = view.findViewById(R.id.have_car_text_view)
        checkedHaveMoto = view.findViewById(R.id.have_motorcycle_text_view)
        makeClear = view.findViewById(R.id.make_clear_edit_text)
        nextButton = view.findViewById(R.id.second_next_button)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase

        Observable.fromCallable { db.userDao().getLastId() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userId = it
            }, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })

        nextButton.setOnClickListener {
            radioButton = view.findViewById(radioGroup.checkedRadioButtonId)
            checkedRadioText = radioButton.text.toString()
            otherData = houseChecked + carChecked + mohoChecked
            userSet()
            if (userId != 0) {
                saveToDb()
                fragmentMain = LastRegFragment()
                setFragment(fragmentMain)
            } else {
                Toast.makeText(this.requireContext(), "Подождите", Toast.LENGTH_SHORT).show()
            }
        }

            checkedHaveHouse.setOnClickListener {
                houseChecked = if (houseChecked == "") {
                    it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveHouse.text.toString() + ";"
                } else {
                    it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }
            checkedHaveCar.setOnClickListener {
                carChecked = if (carChecked == "") {
                    checkedHaveCar.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveCar.text.toString() + ";"
                } else {
                    checkedHaveCar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }

            checkedHaveMoto.setOnClickListener {
                mohoChecked = if (mohoChecked == "") {
                    it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveMoto.text.toString() + ";"
                } else {
                    it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }


        return view
    }


    @SuppressLint("CheckResult")
    fun userSet() {
        user.setOtherData(otherData)
        user.setStatus(checkedRadioText)
        user.setMakeClear(makeClear.text.toString())
    }

    @SuppressLint("CheckResult")
    fun saveToDb() {
        Completable.fromAction { db.userDao().updateotherData(user.getOtherData(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateStatus(user.getStatus(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateMakeClear(user.getMakeClear(), userId) }
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
