package com.example.kotlinproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    //1
    private val firebaseAuth = FirebaseAuth.getInstance()

    //2
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        Toast.makeText(this,"Registration Successful!",Toast.LENGTH_SHORT).show()
        if (user != null) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }

    //3
    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    //4
    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    fun onSignup(v: View) {

        if (!username_edittext.text.toString().isNullOrEmpty() && !password_edittext.text.toString().isNullOrEmpty() && !confirmpassword_edittext.text.toString().isNullOrEmpty()) {
            if (password_edittext.text.toString().equals(confirmpassword_edittext.text.toString())) {
                firebaseAuth.createUserWithEmailAndPassword(
                    username_edittext.text.toString(),
                    password_edittext.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Signup error ${task.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Passwords Have To Match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "All Fields Are Required!", Toast.LENGTH_SHORT).show()
        }

    }

    //Static function in Kotlin
    companion object {
        fun newIntent(context: Context?) = Intent(context, SignupActivity::class.java)
    }

}
