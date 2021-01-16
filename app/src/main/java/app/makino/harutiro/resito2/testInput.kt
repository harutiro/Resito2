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

    // idをonCreate()とonDestroy()で利用するため
    var id: String? = null

    private val realm by lazy {
        Realm.getDefaultInstance()
    }

    var sinki = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_input)

        // MainActivityのRecyclerViewの要素をタップした場合はidが，fabをタップした場合は"空白"が入っているはず
        id = intent.getStringExtra("id")

        println(id)

        //findViewById
        hizukeId = findViewById<EditText>(R.id.inHizukeId)
        nedanId = findViewById<EditText>(R.id.inNedanId)
        val sihuId = findViewById<EditText>(R.id.inSihuId)
        val saveButtonId = findViewById<Button>(R.id.saveButton)

        if(id.isNullOrEmpty()){


            sinki = true

            // 新しい要素に重複しないIDを設定するため，ランダムなUUIDを生成
            id = UUID.randomUUID().toString()



        }else{

            // MainActivityに渡されたidを元にデータを検索して取得
            val item = realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findFirst()

            //もしidが間違っていたりして取得に失敗したら以下の「取得したデータをViewに設定する」処理は行わない
            if(item != null) {
                findViewById<EditText>(R.id.inHizukeId).setText(item.hizuke)
                findViewById<EditText>(R.id.inNedanId).setText(item.nedan.toString())
                findViewById<EditText>(R.id.inSihuId).setText(item.saihu)
            }

        }



        saveButtonId.setOnClickListener {


            /*===============================realmに送る============================*/


            if(sinki == true){

                realm.executeTransaction {
                    //梱包するためのダンボールを作る（インスタンス作成）
                    val new = it.createObject(OkaneListDateSaveRealm::class.java, UUID.randomUUID().toString())

                    //null対策でコピーを作りぬるチェックを行う
                    val hizukeCopy = hizukeId
                    if (hizukeCopy != null) {
                        new?.hizuke = hizukeCopy.text.toString()
                    }

                    //null対策でコピーを作りぬるチェックを行う
                    val nedanCopy = nedanId
                    if (nedanCopy != null) {
                        new?.nedan = Integer.parseInt(nedanCopy.text.toString())
                    }

                    new?.saihu = sihuId.text.toString()
                }

            }else {

                realm.executeTransaction {
                    //梱包するためのダンボールを作る（インスタンス作成）
                    val new = realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findFirst()

                    //null対策でコピーを作りぬるチェックを行う
                    val hizukeCopy = hizukeId
                    if (hizukeCopy != null) {
                        new?.hizuke = hizukeCopy.text.toString()
                    }

                    //null対策でコピーを作りぬるチェックを行う
                    val nedanCopy = nedanId
                    if (nedanCopy != null) {
                        new?.nedan = Integer.parseInt(nedanCopy.text.toString())
                    }

                    new?.saihu = sihuId.text.toString()
                }
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

        println(sinki)


        if(sinki == true) {

            println("来た！！！！")

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

            println(dateAndtime)
            println(nedanItiziDate)

        }



    }

    // Activity終了時にralmを終了
    override fun onDestroy() {
        realm.close()
        super.onDestroy()

        sinki = false
    }
}