package com.example.standaloneapp1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mFirstName: String? = null
    private var mMiddleName: String? = null
    private var mLastName: String? = null

    private var mEtFirstName: EditText? = null
    private var mEtMiddleName: EditText? = null
    private var mEtLastName: EditText? = null

    private var mSubmitButton: Button? = null
    private var mPictureButton: Button? = null

    private var mProfilePic: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEtFirstName = findViewById(R.id.et_firstName)
        mEtMiddleName = findViewById(R.id.et_middleName)
        mEtLastName = findViewById(R.id.et_lastName)

        mSubmitButton = findViewById(R.id.submit_button)
        mPictureButton = findViewById(R.id.picture_button)

        mSubmitButton!!.setOnClickListener(this)
        mPictureButton!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.submit_button -> {
                mFirstName = mEtFirstName!!.text.toString()
                mMiddleName = mEtMiddleName!!.text.toString()
                mLastName = mEtLastName!!.text.toString()

                if (mFirstName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(this@MainActivity, "Enter a first name.", Toast.LENGTH_SHORT)
                        .show()
                } else if (mLastName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(this@MainActivity, "Enter a last name.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            R.id.picture_button -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraActivity.launch(cameraIntent)
                } catch (ex: ActivityNotFoundException) {
                    //Do error handling here
                }
            }
        }
    }
        private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    mProfilePic = findViewById<View>(R.id.profile_thumbnail) as ImageView
                    //val extras = result.data!!.extras
                    //val thumbnailImage = extras!!["data"] as Bitmap?

                    if (Build.VERSION.SDK_INT >= 33) {
                        val thumbnailImage =
                            result.data!!.getParcelableExtra("data", Bitmap::class.java)
                        mProfilePic!!.setImageBitmap(thumbnailImage)
                    } else {
                        val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                        mProfilePic!!.setImageBitmap(thumbnailImage)
                    }
                }
            }
}