package app.makino.harutiro.resito2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    var nedanDateView:MutableList<OkaneListDateResycle>? = null
    var adapter: RecyclerViewAdapter? = null

    var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.resycleView)

        val inputButton = findViewById<Button>(R.id.inputButtonId)

        inputButton.setOnClickListener {
            val inputPage = Intent(this,testInput::class.java)
            startActivity(inputPage)
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
        adapter!!.addAll(nedanDateView!!)



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        date = intent.getStringExtra("")

        val hizukeNaiyou = intent?.getStringExtra("hizuke") ?:"何も入ってない"
        val nedanNaiyou = intent?.getStringExtra("nedan")?.toInt() ?:0
        val saihuNaiyou = intent?.getStringExtra("sihu") ?:"何も入ってない"

        nedanDateView?.add(OkaneListDateResycle(hizukeNaiyou,nedanNaiyou,R.drawable.image1,R.drawable.image1,saihuNaiyou))



    }
}