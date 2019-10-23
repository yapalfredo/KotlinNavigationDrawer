package com.example.kotlinproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlinproject.util.DATA_USERS
import com.example.kotlinproject.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    //1
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    private val firebaseAuth = FirebaseAuth.getInstance()

    //2
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val userId = firebaseAuth.currentUser
        if (userId != null) {
            Toast.makeText(this,"Registration Successful!",Toast.LENGTH_SHORT).show()
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
            if (password_edittext.text.toString() == confirmpassword_edittext.text.toString()) {
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
                        }else{
                            val email = username_edittext.text.toString()
                            //either take the value from the firebase or if it's null then take ""
                            val userId = firebaseAuth.currentUser?.uid ?: ""
                            val user = User(userId, email, "", "", "")
                            //programmatically creates the database
                            firebaseDatabase.child(DATA_USERS).child(userId).setValue(user)
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
