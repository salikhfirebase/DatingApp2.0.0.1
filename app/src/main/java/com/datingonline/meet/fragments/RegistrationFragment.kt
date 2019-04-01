package com.datingonline.meet.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.datingonline.meet.db.AppDatabase
import com.datingonline.meet.Model.User
import com.datingonline.meet.R
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationFragment : Fragment() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    private lateinit var db:AppDatabase
    private lateinit var emailEditText: EditText
    private lateinit var passEditText: EditText
    private var user = User()
    private lateinit var createAccButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        emailEditText = view.findViewById(R.id.reg_email_edit_text)
        passEditText = view.findViewById(R.id.reg_pass_edit_text)
        createAccButton = view.findViewById(R.id.reg_button)

        createAccButton.setOnClickListener {
            if ((passEditText.text.toString() == "") || (emailEditText.text.toString() == "")) {
                Toast.makeText(this.requireContext(), "Заполните поля", Toast.LENGTH_SHORT).show()
            } else {
                isUserInDb(emailEditText.text.toString(), passEditText.text.toString())
            }
        }

        return view
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
    fun isUserInDb(email: String, password: String) {

        Observable.fromCallable{ db.userDao().isEmailInDb(email) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it>0) {
                    Toast.makeText(this.requireContext(), "Адрес уже занят", Toast.LENGTH_SHORT).show()
                } else {
                    user.setEmail(email)
                    user.setPassword(password)
                    saveToDb(user)
                    fragmentMain = MainInfoFragment()
                    setFragment(fragmentMain)
                }
            }

    }

    private fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = this.requireActivity().supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.question_container, f)
        ft.commit()

    }


}
