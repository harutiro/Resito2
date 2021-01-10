package app.makino.harutiro.resito2

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm

class RealmDousa {

    //Realmの変数を宣言
    val realm :Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //read()というメソッドを使ってすでに保存されているメモのデータを取得してmemoという変数に代入
        val memo:OkaneListDateSaveRealm?  = read()

        //データベースから取得したMemoをテキストに表示する処理
        //nullが保存されるとえらるので分岐処理をする
        if(memo != null){
            titleEditText.setText(memo.title)
            contentEditText.setText(memo.content)
        }

        saveButton.setOnClickListener {
            val title:String = titleEditText.text.toString()
            val content:String = contentEditText.text.toString()
            //保存ボタンを押したときに、titleTextとcontentEditTextに入力されたテキストを取得しsave() メソッドに値を渡す
            save(title,content)
        }


    }

    //画面終了時にRealmを閉じる
    //画面（Activity）が終了したときに実行される部分
    //パフォーマンスを良くするためにやる
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    //画面を起動したときにすでに保存されているMemoのデータを取得する関数
    //メソッド名（read）と返り値の型を指定（Memo? ）]
    //返り値の？はnullが帰ってくる可能性があるから
    fun read(): OkaneListDateSaveRealm? {

        //realmを使ってデータベース中のMemoリストから最初のデータを一つ取り出している

        //※findFirst()の他にfindAll()というリストを取得する機能もある
        return realm.where(OkaneListDateSaveRealm::class.java).findFirst()
    }

    fun save(title :String,content :String){

        //すでに保存されているメモを取得
        // すでに保存されたデータがあればそのデータの更新。なければ新しくデータを作成するようにする
        val memo :OkaneListDateSaveRealm? = read()

        //データベースへの書き込み（データの作成、更新、消去.etc）
        realm.executeTransaction{

            //データの存在によって、更新処理もしくは新規作成の処理をする
            if(memo != null) {
                //memoの更新をしている
                memo.title = title
                memo.content = content
            }else{
                //メモの新規作成
                //保存するデータの作成「it.createObuject(データ型::class.java)」
                //データベースに新しいオブジェクトを作って保存する

                //作ったオブジェクトをnewMemoという変数に代入してtitleとcontentを更新する
                //realm.executeTransaction{}ないだとitをrealmの変数として扱う→高階関数
                val newMemo :OkaneListDateSaveRealm = it.createObject(OkaneListDateSaveRealm::class.java)
                newMemo.title = title
                newMemo.content = content
            }

        }
    }
}