package app.makino.harutiro.resito2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.makino.harutiro.resito2.input.inputPageNedan
import app.makino.harutiro.resito2.input.TestInput
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import app.makino.harutiro.resito2.RecyclerViewAdapter
import app.makino.harutiro.resito2.setting.saihu.SaihuSetting

class MainActivity : AppCompatActivity() {

    //インテントの戻るいちを指定
    val REQUEST_PREVIEW = 1

    //値段データ配列の保存場所
    var nedanDateView:MutableList<OkaneListDateSaveRealm> = mutableListOf()

    //アダプターインスタンスの保存
    var adapter: RecyclerViewAdapter? = null

    //Allモードにするか保存
    var allViewMode = false

    //写真関係
    val REQUEST_PICTURE = 2
    val REQUEST_EXTERNAL_STORAGE = 3
    var UriString = ""
    lateinit var currentPhotoUri : Uri

    //findViewById
    var fabCamera:View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findviewByIdの保存場所
        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.resycleView)
        val AllView = findViewById<Switch>(R.id.AllSwitchId)
        fabCamera = findViewById(R.id.fabCamera)



        //カメラ写真入力
        findViewById<View>(R.id.fabCamera).setOnClickListener {

            val context: Context = applicationContext

            // 保存先のフォルダー
            val cFolder: File? = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)

            //        *名前関係*       //
            //　フォーマット作成
            val fileDate: String = SimpleDateFormat("ddHHmmss", Locale.US).format(Date())
            //　名前作成
            val fileName: String = String.format("CameraIntent_%s.jpg", fileDate)

            //uriの前作成
            val cameraFile: File = File(cFolder, fileName)

            //uri最終作成
            currentPhotoUri = FileProvider.getUriForFile(this, context.packageName.toString() + ".fileprovider", cameraFile)


            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            startActivityForResult(intent, REQUEST_PICTURE)

            UriString = currentPhotoUri.toString()

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


        //============================権限関係======================================

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            storagePermission()
        }




    }

    //======================================権限関係関数=================================

    private fun storagePermission() {
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                fabCamera?.isEnabled = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
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
            persons = realm.where(OkaneListDateSaveRealm::class.java).findAll().sort("hizukeSystem", Sort.DESCENDING)
        }else{
            persons = realm.where(OkaneListDateSaveRealm::class.java).equalTo("akaibu",allViewMode).findAll().sort("hizukeSystem",Sort.DESCENDING)
        }


        for (person in persons) {
            Log.d("debag", person.hizukeSystem)

            val hizukeRealm: String = person?.hizuke ?: "eror"
            val nedanRealm = person?.nedan ?: 0
            val saihuRealm = person?.saihu ?: "eror"
            val akaibuRealm = person?.akaibu ?:false

            val resitoImage = person?.resitoImage ?:"eror"

            val Id = person?.Id ?:""
            val buyName = person?.buyName ?:""

            val saihuIcon = person?.saihuIcon ?:""


            //送られてきたデータを配列に梱包するところ。
            nedanDateView.add(OkaneListDateSaveRealm(hizukeRealm,nedanRealm,saihuIcon,R.drawable.image1,saihuRealm,akaibuRealm,Id,resitoImage,"",buyName))
        }



        //詰めたデータをアダプターに発送するところ
        adapter?.setList(nedanDateView)
        //デバッグ用
        println("出力！！")
        println(nedanDateView.size)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //データを受け取る
        if (requestCode == REQUEST_PICTURE) {

            when (resultCode) {
                RESULT_OK -> {

                    val inputPage = Intent(this, TestInput::class.java)
                    inputPage.putExtra("resitoImage",UriString)
                    startActivity(inputPage)


                }

                //正常じゃないとき　Cancelされたとき
                else -> {
                    //メディアプレイヤーに追加したデータを消去する
                    contentResolver.delete(currentPhotoUri, null, null)
                }

            }
        }

    }

    // Activity終了時にRealmを終了すること
    override fun onDestroy() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.close()
        super.onDestroy()
    }

    //オプションボタン関係
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.saihu_setting -> {
                val settingPage = Intent (this, SaihuSetting::class.java)
                startActivity(settingPage)

            }
            R.id.zyanru_setting -> {

            }
            R.id.kani_input_setting -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }


}