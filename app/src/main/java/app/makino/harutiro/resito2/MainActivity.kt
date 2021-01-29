package app.makino.harutiro.resito2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import app.makino.harutiro.resito2.input.inputPageNedan
import app.makino.harutiro.resito2.input.TestInput
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    //インテントの戻るいちを指定
    val REQUEST_PREVIEW = 1

    //値段データ配列の保存場所
    var nedanDateView:MutableList<OkaneListDateSaveRealm> = mutableListOf()

    //アダプターインスタンスの保存
    var adapter: RecyclerViewAdapter? = null

    //Allモードにするか保存
    var allViewMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findviewByIdの保存場所
        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.resycleView)
        val AllView = findViewById<Switch>(R.id.AllSwitchId)


        //カメラ写真入力
        findViewById<View>(R.id.fabCamera).setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, REQUEST_PREVIEW)
                }
            }

        }

        //表示の状態管理
        AllView.setOnCheckedChangeListener { buttonView, isChecked ->
            allViewMode = isChecked
            onResume()

        }



        //アダプターのインスタンス作成？
        adapter = RecyclerViewAdapter(this,object: RecyclerViewAdapter.OnItemClickListner{
            override fun onItemClick(item: OkaneListDateSaveRealm) {
                // SecondActivityに遷移するためのIntent
                val intent = Intent(applicationContext, TestInput::class.java)
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


        //データ作成Intent
        findViewById<View>(R.id.Fab).setOnClickListener {
            val inputPage = Intent(this,inputPageNedan::class.java)
            startActivityForResult(inputPage,1)
        }




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
            nedanDateView.add(OkaneListDateSaveRealm(hizukeRealm,nedanRealm,R.drawable.image1,R.drawable.image1,saihuRealm,akaibuRealm,Id))
        }
        println("===============================")
        println()



        //詰めたデータをアダプターに発送するところ
        adapter?.setList(nedanDateView)
        //デバッグ用
        println("出力！！")
        println(nedanDateView.size)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //データを受け取る
        if (requestCode == REQUEST_PREVIEW && data!=null && resultCode == RESULT_OK){
            //データの格納
            val imagebitmap = data?.extras?.get("data") as Bitmap

            //＝＝＝＝＝＝＝＝＝＝＝＝＝＝BASE６４＝＝＝＝＝＝＝＝＝＝＝＝＝＝
            //エンコード
            val immagex: Bitmap = imagebitmap
            val baos = ByteArrayOutputStream()
            immagex.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val output = Base64.encodeToString(b, Base64.NO_WRAP)



            //インテント
            val inputPage = Intent(this, TestInput::class.java)
            inputPage.putExtra("resitoImage",output)
            startActivity(inputPage)


        }
    }

    // Activity終了時にRealmを終了すること
    override fun onDestroy() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.close()
        super.onDestroy()
    }
}