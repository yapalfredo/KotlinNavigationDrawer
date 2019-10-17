package com.example.kotlinproject.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.kotlinproject.R
import com.example.kotlinproject.TransactionCallback
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_about_me.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}
