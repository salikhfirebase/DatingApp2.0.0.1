package com.datingonline.meet.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.datingonline.meet.db.AppDatabase
import com.datingonline.meet.R
import com.datingonline.meet.UserProfileActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class SigInFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var emailEditText: EditText
    private lateinit var passEditText: EditText
    private lateinit var logInButton: Button
    private lateinit var intent1: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sig_in, container, false)

        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        emailEditText = view.findViewById(R.id.sign_in_email_edit_text)
        passEditText = view.findViewById(R.id.sign_in_pass_edit_text)
        logInButton = view.findViewById(R.id.sign_in_button)

        logInButton.setOnClickListener {
            isUserInDb(emailEditText.text.toString(), passEditText.text.toString())
        }

        return view
    }

    @SuppressLint("CheckResult")
    fun isUserInDb(email: String, password: String) {

        Observable.fromCallable{ db.userDao().verifaicate(email,password) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it>0) {
                    intent1 = Intent(this.requireContext(), UserProfileActivity::class.java)
                    intent1.putExtra("log_in", email)
                    intent1.putExtra("reg", 0)
                    startActivity(intent1)
                } else {
                    Toast.makeText(this.requireContext(), "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }

    }




}
