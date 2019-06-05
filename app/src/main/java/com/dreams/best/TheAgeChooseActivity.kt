package com.dreams.best

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.DataSnapshot
import com.dreams.best._core.BaseActivity
import com.dreams.best.ui.WebVActivity
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class TheAgeChooseActivity : BaseActivity() {

    lateinit var the_radioGroup: RadioGroup
    lateinit var the_radioButton: RadioButton
    lateinit var the_toWebViewButton: ImageView
    lateinit var the_intent1: Intent
    private lateinit var the_dataSnapshot: DataSnapshot
    val APP_REFERENCES = "mysettings"
    val APP_REFERENCES_AGE = "empty"
    lateinit var the_mSettings: SharedPreferences
    lateinit var the_mEditor: SharedPreferences.Editor
    var the_sp: String? = null
    val the_REFERRER_DATA = "REFERRER_DATA"
    var the_gclid: String? = null


    fun getPreferer(context: Context): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        if (!sp.contains(the_REFERRER_DATA)) {
            return "Didn't got any referrer follow instructions"
        }
        return sp.getString(the_REFERRER_DATA, null)
    }

    fun getGclid(){
        if (the_gclid != null) {
            if (the_gclid!!.contains("gclid")) {
                the_gclid = the_gclid?.substringAfter("gclid=")
                the_gclid = the_gclid?.substringBefore("&conv")
            } else {
                the_gclid = null
            }
        }
    }

    override fun setUI() {

        getValuesFromDatabase({
            the_dataSnapshot = it

            val taskUrl25 = the_dataSnapshot.child(TASK_URL_25).value as String
            val taskUrl2529 = the_dataSnapshot.child(TASK_URL_25_29).value as String
            val taskUrl30 = the_dataSnapshot.child(TASK_URL_30).value as String

            if (the_mSettings.contains(APP_REFERENCES_AGE)) {
                the_sp = the_mSettings.getString(APP_REFERENCES_AGE, "empty")
            }

            getGclid()

            when (the_sp) {
                "Меньше 25" -> {
                    if ((the_gclid != null) && (the_gclid != "")) {
                        the_intent1.putExtra(EXTRA_TASK_URL, "$taskUrl25?gclid=$the_gclid")
                    } else{
                        the_intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                    }
                    startActivity(the_intent1)
                }
                "25-29" -> {
                    if ((the_gclid != null) && (the_gclid != "")) {
                        the_intent1.putExtra(EXTRA_TASK_URL, "$taskUrl2529?gclid=$the_gclid")
                    } else{
                        the_intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                    }
                    startActivity(the_intent1)
                }
                "Больше 30" -> {
                    if ((the_gclid != null) && (the_gclid != "")) {
                        the_intent1.putExtra(EXTRA_TASK_URL, "$taskUrl30?gclid=$the_gclid")
                    } else{
                        the_intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                    }
                    startActivity(the_intent1)
                }
            }

            the_toWebViewButton.setOnClickListener {
                the_radioButton = findViewById(the_radioGroup.checkedRadioButtonId)

                when (the_radioButton.text.toString()) {

                    "Меньше 25" -> {
                        the_mEditor.putString(APP_REFERENCES_AGE, "Меньше 25")
                        the_mEditor.apply()
                        if ((the_gclid != null) && (the_gclid != "")) {
                            the_intent1.putExtra(EXTRA_TASK_URL, "$taskUrl25?gclid=$the_gclid")
                        } else{
                            the_intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                        }
                        startActivity(the_intent1)
                    }
                    "25-29" -> {
                        the_mEditor.putString(APP_REFERENCES_AGE, "25-29")
                        the_mEditor.apply()
                        if ((the_gclid != null) && (the_gclid != "")) {
                            the_intent1.putExtra(EXTRA_TASK_URL, "$taskUrl2529?gclid=$the_gclid")
                        } else{
                            the_intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                        }
                        startActivity(the_intent1)
                    }
                    "Больше 30" -> {
                        the_mEditor.putString(APP_REFERENCES_AGE, "Больше 30")
                        the_mEditor.apply()
                        if ((the_gclid != null) && (the_gclid != "")) {
                            the_intent1.putExtra(EXTRA_TASK_URL, "$taskUrl30?gclid=$the_gclid")
                        } else{
                            the_intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                        }
                        startActivity(the_intent1)
                    }

                }
            }
        })

    }

    @SuppressLint("CommitPrefEdits")
    override fun initUI() {
        the_radioGroup = findViewById(R.id.age_chooses_radio_group)
        the_toWebViewButton = findViewById(R.id.open_web_view_button_choose)
        the_intent1 = Intent(this, WebVActivity::class.java)
        val config = YandexMetricaConfig.newConfigBuilder("6185dd86-00ef-4317-aee8-5cf48416824f").build()
        YandexMetrica.activate(this, config)
        YandexMetrica.enableActivityAutoTracking(this.application)
        the_mSettings = getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE)
        the_mEditor = the_mSettings.edit()
        if (getPreferer(this) != "Didn't got any referrer follow instructions") {
            the_gclid = getPreferer(this)
        } else {
            the_gclid = null
        }
    }

    override fun getContentView(): Int = R.layout.activity_the_age_choose

}
