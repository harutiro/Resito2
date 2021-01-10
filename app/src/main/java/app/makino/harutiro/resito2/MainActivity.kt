package app.makino.harutiro.resito2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    //値段データ配列の保存場所
    companion object{
        var nedanDateView:MutableList<OkaneListDateResycle> = mutableListOf()
    }
    //アダプターインスタンスの保存
    var adapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findviewByIdの保存場所
        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.resycleView)
        val inputButton = findViewById<Button>(R.id.inputButtonId)
        val kousinButton = findViewById<Button>(R.id.kousinButton)


        inputButton.setOnClickListener {
            val inputPage = Intent(this,testInput::class.java)
            startActivityForResult(inputPage,1)
            finish()
        }

        nedanDateView = mutableListOf(
            OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,"サイフ"),
            OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,"サイフ"),
            OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,"サイフ")

        )


        adapter = RecyclerViewAdapter(this)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

        //リサイクラービューアダプターで宣言したaddAllメソッドを呼んであげてデータも渡している
        adapter!!.addAll(nedanDateView)







    }

    override fun onResume() {
        super.onResume()

        val hizukeNaiyou = intent?.getStringExtra("hizuke") ?:"何も入ってない"
        val nedanNaiyou = intent?.getStringExtra("nedan")?.toInt() ?:0
        val saihuNaiyou = intent?.getStringExtra("sihu") ?:"何も入ってない"

        nedanDateView.add(0,OkaneListDateResycle(hizukeNaiyou,nedanNaiyou,R.drawable.image1,R.drawable.image1,saihuNaiyou))

        adapter?.setList(nedanDateView)

        println("出力！！")
        println(nedanDateView.size)

    }
}