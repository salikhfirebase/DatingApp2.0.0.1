package com.datingonline.meet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.DataSnapshot
import com.datingonline.meet._core.BaseActivity
import com.datingonline.meet.ui.WebViewActivity

class ChooseAgeActivity : BaseActivity() {

    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var toWebViewButton: ImageView
    lateinit var intent1: Intent
    private lateinit var dataSnapshot: DataSnapshot
    val APP_REFERENCES = "mysettings"
    val APP_REFERENCES_AGE = "empty"
    lateinit var mSettings: SharedPreferences
    lateinit var mEditor: SharedPreferences.Editor
    var sp: String? = null


    override fun setUI() {

        getValuesFromDatabase({
            dataSnapshot = it

            val taskUrl25 = dataSnapshot.child(TASK_URL_25).value as String
            val taskUrl2529 = dataSnapshot.child(TASK_URL_25_29).value as String
            val taskUrl30 = dataSnapshot.child(TASK_URL_30).value as String

            if (mSettings.contains(APP_REFERENCES_AGE)) {
                sp = mSettings.getString(APP_REFERENCES_AGE, "empty")
            }

            when (sp) {
                "Меньше 25" -> {
                    intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                    startActivity(intent1)
                }
                "25-29" -> {
                    intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                    startActivity(intent1)
                }
                "Больше 30" -> {
                    intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                    startActivity(intent1)
                }
            }

            toWebViewButton.setOnClickListener {
                radioButton = findViewById(radioGroup.checkedRadioButtonId)

                when (radioButton.text.toString()) {

                    "Меньше 25" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "Меньше 25")
                        mEditor.apply()
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                        startActivity(intent1)
                    }
                    "25-29" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "25-29")
                        mEditor.apply()
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                        startActivity(intent1)
                    }
                    "Больше 30" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "Больше 30")
                        mEditor.apply()
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                        startActivity(intent1)
                    }

                }
            }
        })

    }

    @SuppressLint("CommitPrefEdits")
    override fun initUI() {
        radioGroup = findViewById(R.id.ages_radio_group)
        toWebViewButton = findViewById(R.id.to_web_view_button)
        intent1 = Intent(this, WebViewActivity::class.java)
        mSettings = getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE)
        mEditor = mSettings.edit()
    }

    override fun getContentView(): Int = R.layout.activity_choose_age

}
