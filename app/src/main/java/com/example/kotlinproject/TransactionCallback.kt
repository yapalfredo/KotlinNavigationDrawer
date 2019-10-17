package com.example.kotlinproject

import com.google.firebase.database.DatabaseReference

interface TransactionCallback {

    fun onSignout()
    fun getUserId(): String
    fun getUserDatabase() : DatabaseReference

}