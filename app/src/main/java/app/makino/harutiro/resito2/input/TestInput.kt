package app.makino.harutiro.resito2.input

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import app.makino.harutiro.resito2.OkaneListDateSaveRealm
import app.makino.harutiro.resito2.R
import io.realm.Realm
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TestInput : AppCompatActivity() {

    //インテントの戻るいちを指定
    val REQUEST_PICTURE = 2
    val REQUEST_EXTERNAL_STORAGE = 3
    
    //日にち保存
    object ukewatasi {
        var hizukeSystem = ""
    }

    // idをonCreate()とonDestroy()で利用するため
    var id: String? = null

    //レシート画像
    lateinit var resitoImage:Bitmap
    lateinit var currentPhotoUri : Uri
    var UriString = ""



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
    var resitoImageView:ImageView? = null

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_input)



        //====================================他の入力画面からの入力======================================

        // MainActivityのRecyclerViewの要素をタップした場合はidが，fabをタップした場合は"空白"が入っているはず
        id = intent.getStringExtra("id")
        UriString = intent.getStringExtra("resitoImage").toString()


        //======================================findViewById==========================================

        hizukeId = findViewById<TextView>(R.id.dayTextView)
        nedanId = findViewById<EditText>(R.id.inNedanId)
        sihuId = findViewById<EditText>(R.id.inSihuId)
        resitoImageView = findViewById(R.id.resitoImageView)
        val saveButtonId = findViewById<Button>(R.id.saveButton)
        val akaibuSwitch = findViewById<Switch>(R.id.akaibuSwichId)
        val dellButton = findViewById<Button>(R.id.delButton)




        //=====================================ボタン系の動作=======================================


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

        findViewById<ImageView>(R.id.resitoImageView).setOnTouchListener { view, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {

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

            }
            true
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
            val formatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日")
            val formatted = dateAndtime.format(formatter)
            //時間データセット
            hizukeId?.setText(formatted)

            resitoGazou()



        }else{

            // MainActivityに渡されたidを元にデータを検索して取得
            val item = realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findFirst()

            //もしidが間違っていたりして取得に失敗したら以下の「取得したデータをViewに設定する」処理は行わない
            if(item != null) {
                findViewById<TextView>(R.id.dayTextView).setText(item.hizuke)
                findViewById<EditText>(R.id.inNedanId).setText(item.nedan.toString())
                findViewById<EditText>(R.id.inSihuId).setText(item.saihu)
                findViewById<Switch>(R.id.akaibuSwichId).isChecked = item.akaibu

                val decodedByte: ByteArray = Base64.decode(item.resitoImage, 0)
                findViewById<ImageView>(R.id.resitoImageView).setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size))
                findViewById<ImageView>(R.id.resitoImageView).setVisibility(View.VISIBLE)
                UriString = item.resitoImage
                currentPhotoUri = UriString.toUri()

            }

        }

        resitoGazou()





        /*===============================realmに送る============================*/

        saveButtonId.setOnClickListener {

            //レルム入力関数
            datekanri()

            //intent開始
            finish()

        }

        /*===============================日にち変更動作=============================*/



        val dayTextView =  findViewById<TextView>(R.id.dayTextView)

        //テキストボックスをタッチ動作対応させる
        dayTextView.setClickable(true)
        dayTextView.setOnClickListener {

            //ダイアログの表示　このとき、テキストボックスの中に入ってるデータをクラスに送る
            DateDialog(dayTextView.text.toString()){ date ->

                //出力結果を反映
                dayTextView.setText(date)

            }.show(supportFragmentManager,"date_dialog")

        }

        //==========================権限関係===============================
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            storagePermission()
        }
    }

    /*=================================動作まとめ=======================================*/
    fun resitoGazou(){
        if(UriString != "null") {
            findViewById<ImageView>(R.id.resitoImageView).setImageURI(UriString.toUri())
        }else{
            findViewById<ImageView>(R.id.resitoImageView).setVisibility(View.GONE)
        }
    }

    //レルムにデータを送る関数
    fun datekanri(){

        realm.executeTransaction {

            //新しい要素が作られたかどうか判定
            val new :OkaneListDateSaveRealm? = if(sinki){
                realm.createObject(OkaneListDateSaveRealm::class.java, UUID.randomUUID().toString())

            }else {
                realm.where(OkaneListDateSaveRealm::class.java).equalTo("Id", id).findFirst()

            }

            new?.resitoImage = UriString

            new?.hizuke = hizukeId?.text.toString()

            new?.nedan = Integer.parseInt(nedanId?.text.toString())

            new?.saihu = sihuId?.text.toString()

            new?.akaibu = akaibu

            new?.hizukeSystem = ukewatasi.hizukeSystem
        }
    }

    //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝権限関係関数=========================================
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
                resitoImageView?.isEnabled = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
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

    //画像データ受け取り
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //データを受け取る
        if (requestCode == REQUEST_PICTURE) {

            when (resultCode) {
                RESULT_OK -> {

                    resitoImageView?.setImageURI(currentPhotoUri)
                    UriString = currentPhotoUri.toString()

                }

                //正常じゃないとき　Cancelされたとき
                else -> {
                    //メディアプレイヤーに追加したデータを消去する
                    contentResolver.delete(currentPhotoUri, null, null)
                }

            }
        }
    }



    //======================================ダイアログの出力部分======================================
    class DateDialog(val motoDate: String ,private val onSelected: (String) -> Unit) :
            DialogFragment(), DatePickerDialog.OnDateSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()


            //デフォルト設定の設定部分
            val year = henkan(motoDate).yyyy()
            val month = henkan(motoDate).mm()
            val date = henkan(motoDate).dd()


            //デフォルト設定出力部分
            return DatePickerDialog(requireActivity(), this, year, month, date)
        }

        //帰ってきたデータを送り返すところ
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

            //データを書き換えるよう
            val month1 = month + 1

            //基本データ書き込み
            var monthString = month1.toString()
            var dayOfMonthString = dayOfMonth.toString()

            //データが一桁ならゼロ追加
            if(month1 < 10){
                monthString = "0" + month1.toShort()
            }
            if(dayOfMonth < 10){
                dayOfMonthString = "0" + dayOfMonth.toString()
            }


            onSelected("${year}年 ${monthString}月 ${dayOfMonthString}日")
            
            ukewatasi.hizukeSystem = "${year}-${monthString}-${dayOfMonthString}"

        }
    }




}



/*===================================クラスまとめ=====================================*/




//文字データを年、月、日にバラバラにするクラス
class henkan(val motoDate:String){

    //アルス信号の応用！！

    fun yyyy(): Int {
        var charAry = motoDate.toCharArray()
        var ukeire = ""

        for(ch in charAry){

            when(ch){

                ' '-> ukeire += ""
                '年'-> break
                else->ukeire += ch

            }

        }

        return ukeire.toInt()
    }

    fun mm():Int{
        var charAry = motoDate.toCharArray()
        var ukeire = ""
        var hantei = false

        for (ch in charAry){

            when(ch){

                ' '-> ukeire += ""
                '年'-> hantei = true
                '月'-> break
                else-> if(hantei){
                    ukeire += ch
                }

            }
        }

        return ukeire.toInt()-1
    }

    fun dd():Int{
        var charAry = motoDate.toCharArray()
        var ukeire = ""
        var hantei = false

        for (ch in charAry){

            when(ch){

                ' '-> ukeire += ""
                '月'-> hantei = true
                '日'-> break
                else-> if(hantei){
                    ukeire += ch
                }

            }
        }

        return ukeire.toInt()
    }
}