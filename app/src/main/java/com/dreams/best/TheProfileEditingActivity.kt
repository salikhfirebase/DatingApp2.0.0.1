package com.dreams.best

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dreams.best.db.AppDatabase
import com.dreams.best.Model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TheProfileEditingActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    private var the_userId = 0
    private var the_userEmail = ""
    var the_user = User()
    private var the_otherData = ""
    private var the_houseChecked = ""
    private var the_carChecked = ""
    private var the_motoChecked = ""
    private lateinit var the_userNick: EditText
    private lateinit var the_userMakeClear: EditText
    private lateinit var the_yearsSpinner: Spinner
    private lateinit var the_daysSpinner: Spinner
    private lateinit var the_monthsSpinner: Spinner
    private lateinit var the_raceSpinner: Spinner
    private lateinit var the_heightSeek: SeekBar
    private lateinit var the_weightSeek: SeekBar
    lateinit var the_heightText: TextView
    lateinit var the_weightText: TextView
    private lateinit var the_radioGroup: RadioGroup
    private lateinit var the_radioButton: RadioButton
    private lateinit var the_checkedHaveHouse: TextView
    private lateinit var the_checkedHaveCar: TextView
    private lateinit var the_checkedHaveMoto: TextView
    private lateinit var the_saveButton: Button
    private lateinit var the_checkedRadioText: String
    private lateinit var the_intent1: Intent

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_the_profile_editing)

        the_userId = intent.getIntExtra("id", 0)
        the_userEmail = intent.getStringExtra("email")
        db = AppDatabase.getInstance(this) as AppDatabase
        the_userNick = findViewById(R.id.enter_nickname_edit_text)
        the_userMakeClear = findViewById(R.id.enter_want_make_clear_edit_text)
        the_yearsSpinner = findViewById(R.id.enter_years_date_spinner)
        the_daysSpinner = findViewById(R.id.enter_days_date_spinner)
        the_monthsSpinner = findViewById(R.id.enter_months_date_spinner)
        the_raceSpinner = findViewById(R.id.enter_race_spinner)
        the_heightSeek = findViewById(R.id.enter_height_bar)
        the_weightSeek = findViewById(R.id.enter_weight_bar)
        the_heightText = findViewById(R.id.enter_textView5_height)
        the_weightText = findViewById(R.id.enter_textView8_weight)
        the_saveButton = findViewById(R.id.edit_second_next_button)
        the_radioGroup = findViewById(R.id.edit_radioGroup)
        the_checkedHaveHouse = findViewById(R.id.enter_have_house_chooser)
        the_checkedHaveCar = findViewById(R.id.enter_have_car_view)
        the_checkedHaveMoto = findViewById(R.id.enter_have_motorcycle_text_view)

        if (the_userId != 0) {
            Observable.fromCallable{ db.userDao().getUserFromDbById(the_userId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    the_user = it
                    setViews()
                }
        }

        if (the_userEmail != "") {
            Observable.fromCallable{ db.userDao().getUserFromDb(the_userEmail) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    the_user = it
                    setViews()
                }
        }

        the_checkedHaveHouse.setOnClickListener {
            the_houseChecked = if (the_houseChecked == "") {
                it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                the_checkedHaveHouse.text.toString() + ";"
            } else {
                it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                ""
            }
        }
        the_checkedHaveCar.setOnClickListener {
            the_carChecked = if (the_carChecked == "") {
                the_checkedHaveCar.setBackgroundColor(Color.parseColor("#F5F5F5"))
                the_checkedHaveCar.text.toString() + ";"
            } else {
                the_checkedHaveCar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                ""
            }
        }

        the_checkedHaveMoto.setOnClickListener {
            the_motoChecked = if (the_motoChecked == "") {
                it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                the_checkedHaveMoto.text.toString() + ";"
            } else {
                it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                ""
            }
        }

        the_saveButton.setOnClickListener {

            setDb()

            Completable.fromAction { db.userDao().update(the_user) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this, "Информация обновлена!", Toast.LENGTH_SHORT).show()
                    the_intent1 = Intent(this, ProfileActivity::class.java)
                    the_intent1.putExtra("reg", the_user.getId())
                    the_intent1.putExtra("log_in", "")
                    startActivity(the_intent1)
                }, {
                    Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
                })
        }


    }

    private fun setDb() {
        the_user.setRace(the_raceSpinner.selectedItem.toString())
        the_user.setHeight(the_heightText.text.toString())
        the_user.setWeight(the_weightText.text.toString())
        the_user.setNick(the_userNick.text.toString())
        the_user.setMakeClear(the_userMakeClear.text.toString())
        val birthDate = the_daysSpinner.selectedItem.toString() + "/" + the_monthsSpinner.selectedItem.toString() + "/" + the_yearsSpinner.selectedItem.toString()
        the_user.setBirth(birthDate)
        the_radioButton = findViewById(the_radioGroup.checkedRadioButtonId)
        the_checkedRadioText = the_radioButton.text.toString()
        the_otherData = the_houseChecked + the_motoChecked + the_carChecked
        the_user.setOtherData(the_otherData)
        the_user.setStatus(the_checkedRadioText)
    }


    private fun setViews() {
        the_userNick.setText(the_user.getNick(), TextView.BufferType.EDITABLE)
        the_userMakeClear.setText(the_user.getMakeClear(), TextView.BufferType.EDITABLE)
        the_raceSpinner.setSelection(getIndex(the_raceSpinner, the_user.getRace()))
        the_weightText.text = the_user.getWeight()
        the_heightText.text = the_user.getHeight()
        if (the_user.getWeight() != "") {the_weightSeek.progress = the_user.getWeight().toInt()}
        if (the_user.getHeight() != "") {the_heightSeek.progress = the_user.getHeight().toInt()}
        the_weightSeek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                the_weightText.text = seekBar.progress.toString()
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
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                the_heightText.text = (seekBar.progress + 140).toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                the_heightText.text = (seekBar.progress + 140).toString()
            }

        })
    }

    private fun getIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value){
                return i
            }
        }
        return 0
    }

}
