package com.dreams.best

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.dreams.best.db.AppDatabase
import com.dreams.best.fragments.OtherDataFragment
import com.dreams.best.fragments.PrivatePolicyFragment
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.app_bar_user_profile.*

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var db: AppDatabase
    private var the_userId = 0
    private var the_userEmail = ""
    private lateinit var the_userName: TextView
    private lateinit var the_userPic: CircleImageView
    private lateinit var the_navigationView: NavigationView
    private var the_fragmentMain = androidx.fragment.app.Fragment()
    private lateinit var the_intent1:Intent
    private lateinit var the_menuIntent:Intent

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(user_toolbar)
        user_toolbar.title = "Найди настоящую любовь"

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, user_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        the_userId = intent.getIntExtra("reg", 0)
        the_userEmail = intent.getStringExtra("log_in")
        the_navigationView = findViewById(R.id.navigation_view)
        the_userName = the_navigationView.getHeaderView(0).findViewById(R.id.nav_header_name)
        the_userPic = the_navigationView.getHeaderView(0).findViewById(R.id.nav_header_pic)
        db = AppDatabase.getInstance(this) as AppDatabase
        the_intent1 = Intent(this, TheProfileEditingActivity::class.java)
        the_fragmentMain = OtherDataFragment()
        setFragment(the_fragmentMain)

        if (the_userId != 0) {
            Observable.fromCallable{ db.userDao().getUserFromDbById(the_userId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    the_userName.text = it.getNick()
                    the_userPic.setImageURI(Uri.parse(it.getUserPic()))
                    the_intent1.putExtra("id", the_userId)
                    the_intent1.putExtra("email", "")
                }
        }

        if (the_userEmail != "") {
            Observable.fromCallable{ db.userDao().getUserFromDb(the_userEmail) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    the_userName.text = it.getNick()
                    the_userPic.setImageURI(Uri.parse(it.getUserPic()))
                    the_intent1.putExtra("id", 0)
                    the_intent1.putExtra("email", the_userEmail)
                }
        }

    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_messages -> {
                the_fragmentMain = OtherDataFragment()
                setFragment(the_fragmentMain)
            }
            R.id.nav_profile -> {
                startActivity(the_intent1)
            }
            R.id.nav_chat -> {
                the_fragmentMain = OtherDataFragment()
                setFragment(the_fragmentMain)
            }
            R.id.nav_free_stars -> {
                the_fragmentMain = OtherDataFragment()
                setFragment(the_fragmentMain)
            }
            R.id.nav_feedback -> {
                the_fragmentMain = OtherDataFragment()
                setFragment(the_fragmentMain)
            }
            R.id.nav_help -> {
                the_fragmentMain = PrivatePolicyFragment()
                setFragment(the_fragmentMain)
            }

            R.id.nav_sign_out -> {
                the_menuIntent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, "Вы успешно вышли", Toast.LENGTH_SHORT).show()
                startActivity(the_menuIntent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.user_profile_container, f)
        ft.commit()

    }
}