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
import androidx.core.graphics.drawable.toBitmap
import java.util.*

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
    private var mThumbnailImage: Bitmap? = null

    private var mLoggedinIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEtFirstName = findViewById(R.id.et_firstName)
        mEtMiddleName = findViewById(R.id.et_middleName)
        mEtLastName = findViewById(R.id.et_lastName)

        mSubmitButton = findViewById(R.id.submit_button)
        mPictureButton = findViewById(R.id.picture_button)

        mProfilePic = findViewById<View>(R.id.profile_thumbnail) as ImageView

        mSubmitButton!!.setOnClickListener(this)
        mPictureButton!!.setOnClickListener(this)

        mLoggedinIntent = Intent(this, LoginActivity::class.java)

        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
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
                else{
                    mLoggedinIntent!!.putExtra("FN_DATA", mFirstName)
                    mLoggedinIntent!!.putExtra("LN_DATA", mLastName)
                    startActivity(mLoggedinIntent)
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
                    //val extras = result.data!!.extras
                    //val thumbnailImage = extras!!["data"] as Bitmap?

                    if (Build.VERSION.SDK_INT >= 33) {
                        mThumbnailImage =
                            result.data!!.getParcelableExtra("data", Bitmap::class.java)
                        mProfilePic!!.setImageBitmap(mThumbnailImage)

                    } else {
                        mThumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                        mProfilePic!!.setImageBitmap(mThumbnailImage)
                    }
                }
            }

    companion object {
        private const val FIRST_NAME_KEY = "FN_DATA"
        private const val MIDDLE_NAME_KEY = "MN_DATA"
        private const val LAST_NAME_KEY = "LN_DATA"
        private const val PICTURE_KEY = "P_DATA"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(FIRST_NAME_KEY, mFirstName)
        outState.putString(LAST_NAME_KEY, mLastName)
        outState.putString(MIDDLE_NAME_KEY, mMiddleName)
        if(mProfilePic!!.drawable != null)
            outState.putParcelable(PICTURE_KEY, mProfilePic!!.drawable.toBitmap())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mFirstName = savedInstanceState.getString(FIRST_NAME_KEY)
        mMiddleName = savedInstanceState.getString(MIDDLE_NAME_KEY)
        mLastName = savedInstanceState.getString(LAST_NAME_KEY)

        if (!savedInstanceState.containsKey(PICTURE_KEY))
            return

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= 33)
            mProfilePic!!.setImageBitmap(savedInstanceState.getParcelable(PICTURE_KEY, Bitmap::class.java))
        else
            mProfilePic!!.setImageBitmap(savedInstanceState.getParcelable(PICTURE_KEY))
    }
}