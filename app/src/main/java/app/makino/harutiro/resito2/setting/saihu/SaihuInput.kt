package app.makino.harutiro.resito2.setting.saihu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import app.makino.harutiro.resito2.R

class SaihuInput : AppCompatActivity() {

    val readRequestCode = 42

    var currentPhotoUri : Uri? = null

    var image:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saihu_input2)

        image = findViewById<ImageView>(R.id.iconView)

        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.iconbar).setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent,readRequestCode)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == readRequestCode && resultCode == Activity.RESULT_OK){
            data?.data.also { uri ->
                currentPhotoUri = uri
                image?.setImageURI(uri)

            }
        }
    }
}