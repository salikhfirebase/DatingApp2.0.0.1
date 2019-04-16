package com.datingonline.meet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.DataSnapshot
import com.datingonline.meet._core.BaseActivity
import com.datingonline.meet.ui.WebViewActivity
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

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
    val REFERRER_DATA = "REFERRER_DATA"
    var gclid: String? = null


    fun getPreferer(context: Context): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        if (!sp.contains(REFERRER_DATA)) {
            return "Didn't got any referrer follow instructions"
        }
        return sp.getString(REFERRER_DATA, null)
    }

    fun getGclid(){
        if (gclid != null) {
            if (gclid!!.contains("gclid")) {
                gclid = gclid?.substringAfter("gclid=")
                gclid = gclid?.substringBefore("&conv")
            } else {
                gclid = null
            }
        }
    }

    override fun setUI() {

        getValuesFromDatabase({
            dataSnapshot = it

            val taskUrl25 = dataSnapshot.child(TASK_URL_25).value as String
            val taskUrl2529 = dataSnapshot.child(TASK_URL_25_29).value as String
            val taskUrl30 = dataSnapshot.child(TASK_URL_30).value as String

            if (mSettings.contains(APP_REFERENCES_AGE)) {
                sp = mSettings.getString(APP_REFERENCES_AGE, "empty")
            }

            getGclid()

            when (sp) {
                "Меньше 25" -> {
                    if ((gclid != null) && (gclid != "")) {
                        intent1.putExtra(EXTRA_TASK_URL, "$taskUrl25?gclid=$gclid")
                    } else{
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                    }
                    startActivity(intent1)
                }
                "25-29" -> {
                    if ((gclid != null) && (gclid != "")) {
                        intent1.putExtra(EXTRA_TASK_URL, "$taskUrl2529?gclid=$gclid")
                    } else{
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                    }
                    startActivity(intent1)
                }
                "Больше 30" -> {
                    if ((gclid != null) && (gclid != "")) {
                        intent1.putExtra(EXTRA_TASK_URL, "$taskUrl30?gclid=$gclid")
                    } else{
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                    }
                    startActivity(intent1)
                }
            }

            toWebViewButton.setOnClickListener {
                radioButton = findViewById(radioGroup.checkedRadioButtonId)

                when (radioButton.text.toString()) {

                    "Меньше 25" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "Меньше 25")
                        mEditor.apply()
                        if ((gclid != null) && (gclid != "")) {
                            intent1.putExtra(EXTRA_TASK_URL, "$taskUrl25?gclid=$gclid")
                        } else{
                            intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                        }
                        startActivity(intent1)
                    }
                    "25-29" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "25-29")
                        mEditor.apply()
                        if ((gclid != null) && (gclid != "")) {
                            intent1.putExtra(EXTRA_TASK_URL, "$taskUrl2529?gclid=$gclid")
                        } else{
                            intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                        }
                        startActivity(intent1)
                    }
                    "Больше 30" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "Больше 30")
                        mEditor.apply()
                        if ((gclid != null) && (gclid != "")) {
                            intent1.putExtra(EXTRA_TASK_URL, "$taskUrl30?gclid=$gclid")
                        } else{
                            intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                        }
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
        val config = YandexMetricaConfig.newConfigBuilder("02c94e20-5d01-4b57-814a-aac3480ce940").build()
        YandexMetrica.activate(this, config)
        YandexMetrica.enableActivityAutoTracking(this.application)
        mSettings = getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE)
        mEditor = mSettings.edit()
        if (getPreferer(this) != "Didn't got any referrer follow instructions") {
           gclid = getPreferer(this)
        } else {
            gclid = null
        }
    }

    override fun getContentView(): Int = R.layout.activity_choose_age

}
