package com.dreams.best

import android.app.Application
import com.google.firebase.FirebaseApp


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/13/19
 */
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}