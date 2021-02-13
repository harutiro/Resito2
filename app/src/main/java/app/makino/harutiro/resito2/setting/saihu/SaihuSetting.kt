package app.makino.harutiro.resito2.setting.saihu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.makino.harutiro.resito2.OkaneListDateSaveRealm
import app.makino.harutiro.resito2.R
import app.makino.harutiro.resito2.RecyclerViewAdapter
import io.realm.Realm
import io.realm.RealmResults

class SaihuSetting : AppCompatActivity() {

    //配列の保存場所
    var saihuDateView:MutableList<SaihuSettingListDateSaveRealm> = mutableListOf()

    var adapter:SettingRecycleViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saihu_setting)

        val recycleView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.saihuSettingRecycleView)

        adapter = SettingRecycleViewAdapter(this)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

        hyouzi()


    }

    fun hyouzi(){

        saihuDateView.clear()

        saihuDateView = mutableListOf(
            SaihuSettingListDateSaveRealm("aaaa","aaaa"),
            SaihuSettingListDateSaveRealm("aaaa","aaaa"),
            SaihuSettingListDateSaveRealm("aaaa","aaaa"),
            SaihuSettingListDateSaveRealm("aaaa","aaaa")

        )

        val realm: Realm = Realm.getDefaultInstance()
        val persons:RealmResults<SaihuSettingListDateSaveRealm>

        persons = realm.where(SaihuSettingListDateSaveRealm::class.java).findAll()

        for(person in persons){
            val name = person.name
            val icon = person.image

            saihuDateView.add(SaihuSettingListDateSaveRealm(name,icon))
        }

        adapter?.setList(saihuDateView)

    }
}