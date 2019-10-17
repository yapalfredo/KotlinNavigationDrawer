package com.example.kotlinproject.ui


import android.os.Bundle
import android.view.ActionProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.example.kotlinproject.R
import com.example.kotlinproject.TransactionCallback
import com.example.kotlinproject.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_about_me.*

/**
 * A simple [Fragment] subclass.
 */
class AboutMeFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private var callback: TransactionCallback? = null

    fun setCallback(callback: TransactionCallback) {
        this.callback = callback
        userId = callback.getUserId()
        userDatabase = callback.getUserDatabase().child(userId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateInfo()
        update_button.setOnClickListener {
            onUpdate()
        }
    }

    private fun populateInfo() {
        userDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (isAdded) {
                    val user = p0.getValue(User::class.java)
                    about_me_name_edittext.setText(user?.name, TextView.BufferType.EDITABLE)
                    about_me_email_edittext.setText(user?.email, TextView.BufferType.EDITABLE)
                    about_me_age_edittext.setText(user?.age, TextView.BufferType.EDITABLE)
                    if (user?.gender == GENDER_MALE) {
                        male_radio.isChecked = true
                    }
                    if (user?.gender == GENDER_FEMALE) {
                        female_radio.isChecked = true
                    }
                }
            }
        })
    }

    fun onUpdate() {
        if (about_me_name_edittext.text.toString().isNullOrEmpty() ||
            about_me_email_edittext.text.toString().isNullOrEmpty() ||
            about_me_age_edittext.text.toString().isNullOrEmpty() ||
            about_me_radioGroup.checkedRadioButtonId == -1
        ) {
            Toast.makeText(context, "Please complete your profile", Toast.LENGTH_SHORT).show()
        } else {
            val name = about_me_name_edittext.text.toString()
            val email = about_me_email_edittext.text.toString()
            val age = about_me_age_edittext.text.toString()
            val gender =
                if (male_radio.isChecked) GENDER_MALE
                else GENDER_FEMALE

            userDatabase.child(DATA_NAME).setValue(name)
            userDatabase.child(DATA_EMAIL).setValue(email)
            userDatabase.child(DATA_AGE).setValue(age)
            userDatabase.child(DATA_GENDER).setValue(gender)

            Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()


        }
    }

}