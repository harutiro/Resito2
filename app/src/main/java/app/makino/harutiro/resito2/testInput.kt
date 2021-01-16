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
import android.widget.ImageView
import android.widget.Switch
import androidx.annotation.RequiresApi
import io.realm.Realm
import io.realm.RealmResults
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class testInput : AppCompatActivity() {


    // idをonCreate()とonDestroy()で利用するため
    var id: String? = null

    private val realm by lazy {
        Realm.getDefaultInstance()
    }
    //新規かどうか判断
    var sinki = false
    //アーカイブ状態
    var akaibu = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_input)

        // MainActivityのRecyclerViewの要素をタップした場合はidが，fabをタップした場合は"空白"が入っているはず
        id = intent.getStringExtra("id")

        println(id)

        //findViewById
        val hizukeId = findViewById<EditText>(R.id.inHizukeId)
        val nedanId = findViewById<EditText>(R.id.inNedanId)
        val sihuId = findViewById<EditText>(R.id.inSihuId)
        val saveButtonId = findViewById<Button>(R.id.saveButton)
        val akaibuSwitch = findViewById<Switch>(R.id.akaibuSwichId)


        akaibuSwitch.setOnCheckedChangeListener{componundButton,isChecked ->

            akaibu = isChecked

        }


        /*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝データの挿入＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/

        if(id.isNullOrEmpty()){


            sinki = true

            // 新しい要素に重複しないIDを設定するため，ランダムなUUIDを生成
            id = UUID.randomUUID().toString()


            //インスタンスを作る
            //ファイル操作のモード　Context.MODE_PRIVATE・Context.MODE_MULTI_PROCESS
            // getSharedPreferences(”設定データの名前”, ファイル操作のモード)
            var DateStore: SharedPreferences = getSharedPreferences("DateStore", MODE_PRIVATE)

            //　　　データ型（"ラベル名",代入するデータ）
            val nedanItiziDate: Int = DateStore.getInt("nedanItiziDate", 0)
            //値段データセット
            nedanId?.setText(nedanItiziDate.toString())

            //時間取得
            val dateAndtime: LocalDate = LocalDate.now()
            //時間データセット
            hizukeId?.setText(dateAndtime.toString())



        }else{

            // MainActivityに渡されたidを元にデータを検索して取得
            val item = realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findFirst()

            //もしidが間違っていたりして取得に失敗したら以下の「取得したデータをViewに設定する」処理は行わない
            if(item != null) {
                findViewById<EditText>(R.id.inHizukeId).setText(item.hizuke)
                findViewById<EditText>(R.id.inNedanId).setText(item.nedan.toString())
                findViewById<EditText>(R.id.inSihuId).setText(item.saihu)
                findViewById<Switch>(R.id.akaibuSwichId).isChecked = item.akaibu

                println(item.akaibu)
            }

        }





        /*===============================realmに送る============================*/

        saveButtonId.setOnClickListener {





            if(sinki == true){

                realm.executeTransaction {
                    //梱包するためのダンボールを作る（インスタンス作成）
                    val new = it.createObject(OkaneListDateSaveRealm::class.java, UUID.randomUUID().toString())




                    new?.hizuke = hizukeId.text.toString()

                    new?.nedan = Integer.parseInt(nedanId.text.toString())

                    new?.saihu = sihuId.text.toString()

                    new?.akaibu = akaibu
                }

            }else {

                realm.executeTransaction {
                    //梱包するためのダンボールを作る（インスタンス作成）
                    val new = realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findFirst()


                    new?.hizuke = hizukeId.text.toString()

                    new?.nedan = Integer.parseInt(nedanId.text.toString())

                    new?.saihu = sihuId.text.toString()

                    new?.akaibu = akaibu

                    println(akaibu)


                }
            }


            //intent開始
            finish()

        }

    }


    // Activity終了時にralmを終了
    override fun onDestroy() {
        realm.close()
        super.onDestroy()

        sinki = false
    }
}