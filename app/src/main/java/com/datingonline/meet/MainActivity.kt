package com.datingonline.meet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.datingonline.meet.db.AppDatabase
import com.datingonline.meet.Model.User
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var facebookLogin: LoginButton


    private lateinit var callbackManager: CallbackManager

    private lateinit var intent1:Intent
    private var user = User()
    private lateinit var db: AppDatabase
    var userEmail: String = "didn't get"
    var handler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        facebookLogin = findViewById(R.id.facebook_login_button)

        facebookLogin.setReadPermissions("email")
        val config = YandexMetricaConfig.newConfigBuilder("02c94e20-5d01-4b57-814a-aac3480ce940").build()
        YandexMetrica.activate(this, config)
        YandexMetrica.enableActivityAutoTracking(this.application)

        callbackManager = CallbackManager.Factory.create()
        db = AppDatabase.getInstance(this) as AppDatabase

        LoginManager.getInstance().logOut()

        LoginManager.getInstance().registerCallback(callbackManager, object: FacebookCallback<LoginResult>{

            override fun onError(error: FacebookException?) {

            }

            override fun onCancel() {
                Toast.makeText(this@MainActivity, "Авторизация отменена", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: LoginResult) {
                getUserEmail(AccessToken.getCurrentAccessToken())
                handler.postDelayed({ isUserInDb(userEmail) }, 1000)
            }

        })

        facebookLogin.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() == null) {
                //Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUserEmail(token: AccessToken) {

        val request: GraphRequest = GraphRequest.newMeRequest(
            token
        ) { `object`, response ->
            Log.v("MainActivity", response.toString())

            if (`object` != null) {
                userEmail = `object`.getString("email")
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "email")
        request.parameters = parameters
        request.executeAsync()
    }

    fun onCreateAccButtonClick() {

        intent1 = Intent(this, QuestionnaireActivity::class.java)
        intent1.putExtra("action", "registration")
        startActivity(intent1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("CheckResult")
    fun saveToDb(user: User) {

        Completable.fromAction { db.userDao().insert(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })

    }

    @SuppressLint("CheckResult")
    fun isUserInDb(email: String) {

        Observable.fromCallable{ db.userDao().isEmailInDb(email) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it>0) {
                    intent1 = Intent(this, UserProfileActivity::class.java)
                    intent1.putExtra("log_in", email)
                    intent1.putExtra("reg", 0)
                    startActivity(intent1)
                } else {
                    user.setEmail(email)
                    saveToDb(user)
                    intent1 = Intent(this, QuestionnaireActivity::class.java)
                    intent1.putExtra("action", "facebook_login")
                    startActivity(intent1)
                }
            }

    }

    fun onSignInButtonClick() {
        intent1 = Intent(this, QuestionnaireActivity::class.java)
        intent1.putExtra("action", "sign_in")
        startActivity(intent1)
    }

}
