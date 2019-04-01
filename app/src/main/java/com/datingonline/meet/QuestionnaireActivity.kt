package com.datingonline.meet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.datingonline.meet.fragments.MainInfoFragment
import com.datingonline.meet.fragments.RegistrationFragment
import com.datingonline.meet.fragments.SigInFragment

class QuestionnaireActivity : AppCompatActivity() {


    private var fragmentMain = androidx.fragment.app.Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        if (intent.getStringExtra("action") == "registration") {
            fragmentMain = RegistrationFragment()
        }

        if (intent.getStringExtra("action") == "facebook_login"){
            fragmentMain = MainInfoFragment()
        }

        if (intent.getStringExtra("action") == "sign_in") {
            fragmentMain = SigInFragment()
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
