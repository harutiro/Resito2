package app.makino.harutiro.resito2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class testInput : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_input)

        //findViewById
        val hizukeId = findViewById<EditText>(R.id.inHizukeId)
        val nedanId = findViewById<EditText>(R.id.inNedanId)
        val sihuId = findViewById<EditText>(R.id.inSihuId)
        val saveButtonId = findViewById<Button>(R.id.saveButton)

        saveButtonId.setOnClickListener {
            //intentの作成
            val homePage = Intent(this,MainActivity::class.java)

            /*
            //データを送るものを作成
            homePage.putExtra("hizuke",hizukeId.text)
            homePage.putExtra("nedan",nedanId.text)
            homePage.putExtra("sihu",sihuId.text)

             */

            //intent開始
            startActivity(homePage)
            finish()



            //realmに送る
            val realm = Realm.getDefaultInstance()
            val persons: RealmResults<OkaneListDateSaveRealm> = realm.where(OkaneListDateSaveRealm::class.java).findAll()

            realm.executeTransaction{
                val new= it.createObject(OkaneListDateSaveRealm::class.java, UUID.randomUUID().toString())
                new.hizuke = hizukeId.text.toString()
                new.nedan = Integer.parseInt(nedanId.text.toString())
                new.saihu = sihuId.text.toString()
            }
            println("===============================")
            println("更新完了")

        }

    }
}