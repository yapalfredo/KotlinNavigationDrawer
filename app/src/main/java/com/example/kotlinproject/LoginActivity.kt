package com.example.kotlinproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    //1
    private val firebaseAuth = FirebaseAuth.getInstance()

    //2
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if (user != null) {
            startActivity(MainActivity.newIntent(this))
            Toast.makeText(this,"Login Successful!",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    //3
    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    //4
    override fun onDestroy() {
        super.onDestroy()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    fun startSignup(view: View) {
        startActivity(SignupActivity.newIntent(this))
    }

    fun onLogin(view: View) {
        if (!username_edittext.text.toString().isNullOrEmpty() && !password_edittext.text.toString().isNullOrEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(
                username_edittext.text.toString(),
                password_edittext.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Login Error ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    //Static function in Kotlin
    companion object {
        fun newIntent(context: Context?) = Intent(context, LoginActivity::class.java)
    }

}
