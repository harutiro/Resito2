package app.makino.harutiro.resito2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import app.makino.harutiro.resito2.input.inputPageNedan
import app.makino.harutiro.resito2.input.testInput
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class MainActivity : AppCompatActivity() {
    //値段データ配列の保存場所
    var nedanDateView:MutableList<OkaneListDateResycle> = mutableListOf()

    //アダプターインスタンスの保存
    var adapter: RecyclerViewAdapter? = null

    //Allモードにするか保存
    var allViewMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findviewByIdの保存場所
        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.resycleView)
        val inputButton = findViewById<Button>(R.id.inputButtonId)
        val AllView = findViewById<Switch>(R.id.AllSwitchId)


        //表示の状態管理
        AllView.setOnCheckedChangeListener { buttonView, isChecked ->
            allViewMode = isChecked
            onResume()

        }

        //テスト入力画面にintent
        inputButton.setOnClickListener {
            val inputPage = Intent(this,inputPageNedan::class.java)
            startActivityForResult(inputPage,1)
        }


        //アダプターのインスタンス作成？
        adapter = RecyclerViewAdapter(this,object: RecyclerViewAdapter.OnItemClickListner{
            override fun onItemClick(item: OkaneListDateResycle) {
                // SecondActivityに遷移するためのIntent
                val intent = Intent(applicationContext, testInput::class.java)
                // RecyclerViewの要素をタップするとintentによりSecondActivityに遷移する
                // また，要素のidをSecondActivityに渡す
                intent.putExtra("id", item.Id)
                startActivity(intent)
            }
        })


        //リサイクラービューの設定
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

        //リサイクラービューアダプターで宣言したaddAllメソッドを呼んであげてデータも渡している
        adapter!!.addAll(nedanDateView)







    }


    /*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝表示する中身の配列作り＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝*/
    override fun onResume() {
        super.onResume()

        nedanDateView.clear()

        //初期データのところ
//        nedanDateView = mutableListOf(
//                OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,"サイフ",false),
//                OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,"サイフ",false),
//                OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,"サイフ",false)
//
//        )


        //realmのインスタンス
        val realm: Realm = Realm.getDefaultInstance()

        //realm.where(DBのクラス::class.java).findAll().sort(フィールド名)
        //.findAll()は全検索。
        //.sort(フィールド名)はソート。

        val persons: RealmResults<OkaneListDateSaveRealm>

        if(allViewMode){
            persons = realm.where(OkaneListDateSaveRealm::class.java).sort("hizuke", Sort.DESCENDING).findAll()
        }else{
            persons = realm.where(OkaneListDateSaveRealm::class.java).sort("hizuke",Sort.DESCENDING).equalTo("akaibu",allViewMode).findAll()
        }


        for (person in persons) {
            val hizukeRealm: String = person?.hizuke ?: "eror"
            val nedanRealm = person?.nedan ?: 0
            val saihuRealm = person?.saihu ?: "eror"
            val akaibuRealm = person?.akaibu ?:false

            val Id = person?.Id ?:""



            //送られてきたデータを配列に梱包するところ。
            nedanDateView.add(OkaneListDateResycle(hizukeRealm,nedanRealm,R.drawable.image1,R.drawable.image1,saihuRealm,akaibuRealm,Id))
        }
        println("===============================")
        println()



        //詰めたデータをアダプターに発送するところ
        adapter?.setList(nedanDateView)
        //デバッグ用
        println("出力！！")
        println(nedanDateView.size)

    }

    // Activity終了時にRealmを終了すること
    override fun onDestroy() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.close()
        super.onDestroy()
    }
}