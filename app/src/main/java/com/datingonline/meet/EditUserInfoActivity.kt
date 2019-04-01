package com.datingonline.meet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.datingonline.meet.db.AppDatabase
import com.datingonline.meet.Model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditUserInfoActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    private var userId = 0
    private var userEmail = ""
    var user = User()
    private var otherData = ""
    private var houseChecked = ""
    private var carChecked = ""
    private var motoChecked = ""
    private lateinit var userNick: EditText
    private lateinit var userMakeClear: EditText
    private lateinit var yearsSpinner: Spinner
    private lateinit var daysSpinner: Spinner
    private lateinit var monthsSpinner: Spinner
    private lateinit var raceSpinner: Spinner
    private lateinit var heightSeek: SeekBar
    private lateinit var weightSeek: SeekBar
    lateinit var heightText: TextView
    lateinit var weightText: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton: RadioButton
    private lateinit var checkedHaveHouse: TextView
    private lateinit var checkedHaveCar: TextView
    private lateinit var checkedHaveMoto: TextView
    private lateinit var saveButton: Button
    private lateinit var checkedRadioText: String
    private lateinit var intent1: Intent

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_info)

        userId = intent.getIntExtra("id", 0)
        userEmail = intent.getStringExtra("email")
        db = AppDatabase.getInstance(this) as AppDatabase
        userNick = findViewById(R.id.edit_nick_edit_text)
        userMakeClear = findViewById(R.id.edit_make_clear_edit_text)
        yearsSpinner = findViewById(R.id.edit_years_spinner)
        daysSpinner = findViewById(R.id.edit_days_spinner)
        monthsSpinner = findViewById(R.id.edit_months_spinner)
        raceSpinner = findViewById(R.id.edit_race_spinner)
        heightSeek = findViewById(R.id.edit_height_seek_bar)
        weightSeek = findViewById(R.id.edit_weight_seek_bar)
        heightText = findViewById(R.id.edit_textView5)
        weightText = findViewById(R.id.edit_textView8)
        saveButton = findViewById(R.id.edit_second_next_button)
        radioGroup = findViewById(R.id.edit_radioGroup)
        checkedHaveHouse = findViewById(R.id.edit_have_house_text_view)
        checkedHaveCar = findViewById(R.id.edit_have_car_text_view)
        checkedHaveMoto = findViewById(R.id.edit_have_motorcycle_text_view)

        if (userId != 0) {
            Observable.fromCallable{ db.userDao().getUserFromDbById(userId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    user = it
                    setViews()
                }
        }

        if (userEmail != "") {
            Observable.fromCallable{ db.userDao().getUserFromDb(userEmail) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    user = it
                    setViews()
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
            motoChecked = if (motoChecked == "") {
                it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                checkedHaveMoto.text.toString() + ";"
            } else {
                it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                ""
            }
        }

        saveButton.setOnClickListener {

            setDb()

            Completable.fromAction { db.userDao().update(user) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this, "Информация обновлена!", Toast.LENGTH_SHORT).show()
                    intent1 = Intent(this, UserProfileActivity::class.java)
                    intent1.putExtra("reg", user.getId())
                    intent1.putExtra("log_in", "")
                    startActivity(intent1)
                }, {
                    Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
                })
        }


    }

    private fun setDb() {
        user.setRace(raceSpinner.selectedItem.toString())
        user.setHeight(heightText.text.toString())
        user.setWeight(weightText.text.toString())
        user.setNick(userNick.text.toString())
        user.setMakeClear(userMakeClear.text.toString())
        val birthDate = daysSpinner.selectedItem.toString() + "/" + monthsSpinner.selectedItem.toString() + "/" + yearsSpinner.selectedItem.toString()
        user.setBirth(birthDate)
        radioButton = findViewById(radioGroup.checkedRadioButtonId)
        checkedRadioText = radioButton.text.toString()
        otherData = houseChecked + motoChecked + carChecked
        user.setOtherData(otherData)
        user.setStatus(checkedRadioText)
    }


    private fun setViews() {
        userNick.setText(user.getNick(), TextView.BufferType.EDITABLE)
        userMakeClear.setText(user.getMakeClear(), TextView.BufferType.EDITABLE)
        raceSpinner.setSelection(getIndex(raceSpinner, user.getRace()))
        weightText.text = user.getWeight()
        heightText.text = user.getHeight()
        if (user.getWeight() != "") {weightSeek.progress = user.getWeight().toInt()}
        if (user.getHeight() != "") {heightSeek.progress = user.getHeight().toInt()}
        weightSeek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                weightText.text = seekBar.progress.toString()
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
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                heightText.text = (seekBar.progress + 140).toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                heightText.text = (seekBar.progress + 140).toString()
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
