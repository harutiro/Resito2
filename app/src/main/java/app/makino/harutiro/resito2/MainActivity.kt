package app.makino.harutiro.resito2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.resycleView)

        var nedanDateView: List <OkaneListDateResycle> = listOf(
            OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,1),
            OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,1),
            OkaneListDateResycle("20xx-aa-bb",1,R.drawable.image1,R.drawable.image2,1)

        )


        val adapter = RecyclerViewAdapter(this)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

        //リサイクラービューアダプターで宣言したaddAllメソッドを呼んであげてデータも渡している
        adapter.addAll(nedanDateView)



    }
}