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

            //intent開始
            finish()

        }

    }
}