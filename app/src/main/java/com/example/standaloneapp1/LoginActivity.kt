package com.example.standaloneapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    var mTvFirstName: TextView? = null
    var mTvLastName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mTvFirstName = findViewById(R.id.tv_loggedInFirstName)
        mTvLastName = findViewById(R.id.tv_loggedInLastName)

        val recievedIntent = intent;

        mTvFirstName!!.text = recievedIntent.getStringExtra("FN_DATA") + " "
        mTvLastName!!.text = recievedIntent.getStringExtra("LN_DATA") + " "
    }
}