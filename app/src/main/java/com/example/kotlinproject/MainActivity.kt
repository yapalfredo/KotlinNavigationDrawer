package com.example.kotlinproject

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.kotlinproject.ui.AboutMeFragment
import com.example.kotlinproject.ui.HomeFragment
import com.example.kotlinproject.util.*
import com.google.android.material.internal.NavigationMenuView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_about_me.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TransactionCallback {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid
    private lateinit var userDatabase: DatabaseReference

    private var aboutMeFrag: AboutMeFragment? = null
    private var homeFrag: HomeFragment? = null

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (userId.isNullOrEmpty()) {
            onSignout()
        }
        userDatabase = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        //handling the Toolbar in the content_main.xml
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //handling the DrawerLayout in the activity_main.xml
        drawerLayout = findViewById(R.id.drawer_layout)
        //handling the NavigationView in the activity_main.xml
        navView = findViewById(R.id.navigationView)

        /*
        * ActionBarDrawerToggle is for displaying an drawer indicator in the appbar which needs 5 arguments.
            1) Current Activity context
            2) DrawerLayout variable
            3) Toolbar variable
            4) Drawer open description message via Resource string
            5) Drawer close description message via Resource string
        * */
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        /*
        * added the ActionBarDrawerToggle into our DrawerLayout view and synced it.
        * Now it will display a drawer indicator in our app bar.
        * */
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        getGenderForImageView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        getGenderForImageView()
    }

    private fun getGenderForImageView() {
        var db = userDatabase.child(userId!!)

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user?.gender == GENDER_MALE) {
                    imageView.setImageResource(R.drawable.profile_man)
                }
                if (user?.gender == GENDER_FEMALE) {
                    imageView.setImageResource(R.drawable.profile_woman)
                }
            }
        })
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                if (homeFrag == null) {
                    homeFrag = HomeFragment()
                    homeFrag!!.setCallback(this@MainActivity)
                }
                replaceFragment(homeFrag!!)
            }
            R.id.nav_aboutme -> {
                if (aboutMeFrag == null) {
                    aboutMeFrag = AboutMeFragment()
                    aboutMeFrag!!.setCallback(this@MainActivity)
                }
                replaceFragment(aboutMeFrag!!)
            }

            R.id.nav_logout -> {
                Toast.makeText(this, "Logout Successful!", Toast.LENGTH_SHORT).show()
                onSignout()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.relative_layout, fragment).commit()
    }

    override fun onSignout() {
        firebaseAuth.signOut()
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    override fun getUserId(): String = userId!!
    override fun getUserDatabase(): DatabaseReference = userDatabase


    //Static function in Kotlin
    companion object {
        fun newIntent(context: Context?) = Intent(context, MainActivity::class.java)
    }
}
