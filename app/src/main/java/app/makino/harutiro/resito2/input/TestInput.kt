package app.makino.harutiro.resito2.input

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.makino.harutiro.resito2.OkaneListDateSaveRealm
import app.makino.harutiro.resito2.R
import io.realm.Realm
import java.time.LocalDate
import java.util.*

class TestInput : AppCompatActivity() {


    // idをonCreate()とonDestroy()で利用するため
    var id: String? = null

    private val realm by lazy {
        Realm.getDefaultInstance()
    }

    //新規かどうか判断
    var sinki = false
    //アーカイブ状態
    var akaibu = false

    //findViewを外で使うところ
    var hizukeId:TextView? = null
    var nedanId:EditText? = null
    var sihuId:EditText? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_input)

        // MainActivityのRecyclerViewの要素をタップした場合はidが，fabをタップした場合は"空白"が入っているはず
        id = intent.getStringExtra("id")

        println(id)

        //findViewById
        hizukeId = findViewById<TextView>(R.id.dayTextView)
        nedanId = findViewById<EditText>(R.id.inNedanId)
        sihuId = findViewById<EditText>(R.id.inSihuId)
        val saveButtonId = findViewById<Button>(R.id.saveButton)
        val akaibuSwitch = findViewById<Switch>(R.id.akaibuSwichId)
        val dellButton = findViewById<Button>(R.id.delButton)


        akaibuSwitch.setOnCheckedChangeListener{ componundButton, isChecked ->

            akaibu = isChecked

        }

        dellButton.setOnClickListener {
            val persons = realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findAll()

            //読み込んだデータを一時配列に入れる
            var personArray = mutableListOf<OkaneListDateSaveRealm>()
            for(person in persons){
                personArray.add(person)
            }

            //配列に入ったデータを消す
            realm.executeTransaction {
                for (person in persons){
                    person.deleteFromRealm()
                }
            }

            finish()
        }


        /*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝データの挿入＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/

        if(id.isNullOrEmpty()){


            sinki = true

            // 新しい要素に重複しないIDを設定するため，ランダムなUUIDを生成
            id = UUID.randomUUID().toString()



            //インテントしてきたデータを受け取る場所
            val nedanItiziDate = intent.getIntExtra("nedanItiziDate", 0)
            println(nedanItiziDate)
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
                findViewById<TextView>(R.id.dayTextView).setText(item.hizuke)
                findViewById<EditText>(R.id.inNedanId).setText(item.nedan.toString())
                findViewById<EditText>(R.id.inSihuId).setText(item.saihu)
                findViewById<Switch>(R.id.akaibuSwichId).isChecked = item.akaibu

                println(item.akaibu)
            }

        }





        /*===============================realmに送る============================*/

        saveButtonId.setOnClickListener {

            datekanri()

            //intent開始
            finish()

        }

    }

    //レルムにデータを送る関数
    fun datekanri(){

        realm.executeTransaction {

            val new :OkaneListDateSaveRealm? = if(sinki){
                realm.createObject(OkaneListDateSaveRealm::class.java, UUID.randomUUID().toString())

            }else {
                realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findFirst()

            }


            new?.hizuke = hizukeId?.text.toString()

            new?.nedan = Integer.parseInt(nedanId?.text.toString())

            new?.saihu = sihuId?.text.toString()

            new?.akaibu = akaibu
        }
    }


    // Activity終了時にralmを終了
    override fun onDestroy() {
        realm.close()
        super.onDestroy()

        sinki = false

    }

    //戻るボタンの処理
    override fun onBackPressed() {
        // 行いたい処理
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("注意")
                .setMessage("入力したデータを保存しないでホームに戻りますか？")
                .setPositiveButton("OK") { dialog, which ->
                    //Yesが押された時の挙動
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Noが押された時の挙動
                }
                .setNeutralButton("保存") { dialog, which ->
                    //その他が押された時の挙動
                    datekanri()
                    finish()
                }
                .show()
    }
}