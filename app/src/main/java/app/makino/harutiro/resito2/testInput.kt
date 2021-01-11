package app.makino.harutiro.resito2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.webkit.DateSorter
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import io.realm.Realm
import io.realm.RealmResults
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class testInput : AppCompatActivity() {

    //オンレジューム用
    var nedanId:EditText? = null
    var hizukeId:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_input)

        //findViewById
        hizukeId = findViewById<EditText>(R.id.inHizukeId)
        nedanId = findViewById<EditText>(R.id.inNedanId)
        val sihuId = findViewById<EditText>(R.id.inSihuId)
        val saveButtonId = findViewById<Button>(R.id.saveButton)

        saveButtonId.setOnClickListener {





            //realmに送る
            val realm = Realm.getDefaultInstance()
            val persons: RealmResults<OkaneListDateSaveRealm> = realm.where(OkaneListDateSaveRealm::class.java).findAll()

            realm.executeTransaction{
                //梱包するためのダンボールを作る（インスタンス作成）
                val new= it.createObject(OkaneListDateSaveRealm::class.java, UUID.randomUUID().toString())

                //null対策でコピーを作りぬるチェックを行う
                val hizukeCopy = hizukeId
                if(hizukeCopy != null) {
                    new.hizuke = hizukeCopy.text.toString()
                }

                //null対策でコピーを作りぬるチェックを行う
                val nedanCopy = nedanId
                if (nedanCopy != null){
                    new.nedan = Integer.parseInt(nedanCopy.text.toString())
                }

                new.saihu = sihuId.text.toString()
            }
            println("===============================")
            println("更新完了")

            //intent開始
            finish()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        //インスタンスを作る
        //ファイル操作のモード　Context.MODE_PRIVATE・Context.MODE_MULTI_PROCESS
        // getSharedPreferences(”設定データの名前”, ファイル操作のモード)
        var DateStore: SharedPreferences = getSharedPreferences("DateStore", MODE_PRIVATE)

        //　　　データ型（"ラベル名",代入するデータ）
        val nedanItiziDate: Int = DateStore.getInt("nedanItiziDate",0)
        //値段データセット
        nedanId?.setText(nedanItiziDate.toString())

        //時間取得
        val dateAndtime: LocalDate = LocalDate.now()
        //時間データセット
        hizukeId?.setText(dateAndtime.toString())



    }
}