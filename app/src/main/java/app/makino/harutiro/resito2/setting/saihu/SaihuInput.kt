package app.makino.harutiro.resito2.setting.saihu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import app.makino.harutiro.resito2.OkaneListDateSaveRealm
import app.makino.harutiro.resito2.R
import io.realm.Realm
import java.util.*

class SaihuInput : AppCompatActivity() {

    val readRequestCode = 42

    var currentPhotoUri = ""

    var image:ImageView? = null

    private val realm by lazy {
        Realm.getDefaultInstance()
    }

    var sinki = false
    // MainActivityのRecyclerViewの要素をタップした場合はidが，fabをタップした場合は"空白"が入っているはず
    var id:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saihu_input2)

        // MainActivityのRecyclerViewの要素をタップした場合はidが，fabをタップした場合は"空白"が入っているはず
        id = intent.getStringExtra("id")

        image = findViewById<ImageView>(R.id.iconView)





        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.iconbar).setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent,readRequestCode)
        }

        //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝データはめ込み＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
        if(id.isNullOrEmpty()){
            sinki = true

            // 新しい要素に重複しないIDを設定するため，ランダムなUUIDを生成
            id = UUID.randomUUID().toString()

        }else{
            val item = realm.where(SaihuSettingListDateSaveRealm::class.java).equalTo("Id",id).findFirst()

            if(item != null){
                findViewById<ImageView>(R.id.iconView).setImageURI(item.image.toUri())
                findViewById<TextView>(R.id.nameText).setText(item.name)
            }

        }

        findViewById<Button>(R.id.save).setOnClickListener {
            datekanri()
            finish()
        }




    }



    fun datekanri(){
        realm.executeTransaction {

            //新しい要素が作られたかどうか判定
            val new : SaihuSettingListDateSaveRealm? = if(sinki){
                realm.createObject(SaihuSettingListDateSaveRealm::class.java, UUID.randomUUID().toString())

            }else {
                realm.where(SaihuSettingListDateSaveRealm::class.java).equalTo("Id", id).findFirst()

            }

            new?.image = currentPhotoUri
            new?.name = findViewById<EditText>(R.id.nameText).text.toString()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == readRequestCode && resultCode == Activity.RESULT_OK){
            data?.data.also { uri ->
                currentPhotoUri = uri.toString()
                image?.setImageURI(uri)

            }
        }
    }
}