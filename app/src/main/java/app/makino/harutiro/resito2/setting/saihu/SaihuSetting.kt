package app.makino.harutiro.resito2.setting.saihu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import app.makino.harutiro.resito2.OkaneListDateSaveRealm
import app.makino.harutiro.resito2.R
import app.makino.harutiro.resito2.RecyclerViewAdapter
import app.makino.harutiro.resito2.dousaMatome.Henkan
import app.makino.harutiro.resito2.input.TestInput
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class SaihuSetting : AppCompatActivity() {

    //配列の保存場所
    var saihuDateView:MutableList<SaihuSettingListDateSaveRealm> = mutableListOf()

    var adapter:SettingRecycleViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saihu_setting)

        val go = intent.getBooleanExtra("go",false)




        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.saihuSettingRecycleView)

        adapter = SettingRecycleViewAdapter(this,object: SettingRecycleViewAdapter.OnItemClickListner{
            override fun onItemClick(item: SaihuSettingListDateSaveRealm) {
                var intent:Intent? = null
                if(go) {
                    val dataStore: SharedPreferences = getSharedPreferences("DateStore", Context.MODE_PRIVATE)
                    val editor = dataStore.edit()
                    editor.putString("idGo",item.Id)
                    editor.apply()
                    finish()

                }else{
                    // SecondActivityに遷移するためのIntent
                    intent = Intent(applicationContext, SaihuInput::class.java)
                    // RecyclerViewの要素をタップするとintentによりSecondActivityに遷移する
                    // また，要素のidをSecondActivityに渡す
                    intent.putExtra("id", item.Id)
                    startActivity(intent)
                }

            }
        })
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

        hyouzi()





    }



    fun hyouzi(){

        saihuDateView.clear()

        saihuDateView = mutableListOf(
            SaihuSettingListDateSaveRealm("add","aaaa",null),

        )

        val realm: Realm = Realm.getDefaultInstance()
        val persons:RealmResults<SaihuSettingListDateSaveRealm>

        persons = realm.where(SaihuSettingListDateSaveRealm::class.java).findAll()

        for(person in persons){
            val name = person.name
            val icon = person.image
            val Id = person.Id

            saihuDateView.add(SaihuSettingListDateSaveRealm(name,icon,Id))
        }

        adapter?.setList(saihuDateView)

    }

    override fun onRestart() {
        super.onRestart()

        hyouzi()
    }
}