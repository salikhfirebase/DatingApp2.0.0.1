package com.dreams.best

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dreams.best.fragments.TheMainInfoFragment
import com.dreams.best.fragments.TheSignUpFragment
import com.dreams.best.fragments.TheSignInFragment

class AnketaActivity : AppCompatActivity() {


    private var fragmentMain = androidx.fragment.app.Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anketa)

        if (intent.getStringExtra("action") == "registration") {
            fragmentMain = TheSignUpFragment()
        }

        if (intent.getStringExtra("action") == "facebook_login"){
            fragmentMain = TheMainInfoFragment()
        }

        if (intent.getStringExtra("action") == "sign_in") {
            fragmentMain = TheSignInFragment()
        }
        setFragment(fragmentMain)
    }

    private fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.question_container, f)
        ft.commit()

    }
}
