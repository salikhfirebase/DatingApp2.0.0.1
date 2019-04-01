package com.datingonline.meet

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.datingonline.meet.db.AppDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TakePictureActivity : AppCompatActivity() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    lateinit var takePicButt: Button
    lateinit var imgHolder: ImageView
    lateinit var nextButton: Button
    val PERMISSION_CODE = 1000
    var IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    lateinit var db: AppDatabase
    var userId = 0
    lateinit var intent1:Intent
    val PICK_IMAGE = 1
    var selectedImg: Uri? = null
    lateinit var selectImgButton: Button


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_picture)

        imgHolder = findViewById(R.id.got_picture)
        takePicButt = findViewById(R.id.take_pic_button)
        selectImgButton = findViewById(R.id.select_from_button)
        nextButton = findViewById(R.id.last_next_button)
        db = AppDatabase.getInstance(this) as AppDatabase

        Observable.fromCallable { db.userDao().getLastId() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userId = it
            }, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })

        selectImgButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    var permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    getImgFormGallery()
                }
            } else {
                getImgFormGallery()
            }
        }

        takePicButt.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    var permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
        }

        nextButton.setOnClickListener {
            intent1 = Intent(this, UserProfileActivity::class.java)
            intent1.putExtra("log_in", "")
            intent1.putExtra("reg", userId)
            startActivity(intent1)
        }
    }

    private fun openCamera() {

        var values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        image_uri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }

    fun getImgFormGallery() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        getIntent.type = "image/*"
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Вы не дали доступ", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    @SuppressLint("CheckResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_CAPTURE_CODE) {
            imgHolder.setImageURI(image_uri)

            Completable.fromAction { db.userDao().updateUserPic(image_uri.toString(), userId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
                })
        }
        if (requestCode == PICK_IMAGE) {
            selectedImg = data?.data
            imgHolder.setImageURI(selectedImg)

            Completable.fromAction { db.userDao().updateUserPic(selectedImg.toString(), userId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
                })
        }

    }

}
