package app.makino.harutiro.resito2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class OkaneListDateSaveRealm ( //メモのクラスを定義 //openを書くのはRealmを使う際に必要

    //保存するデータの要素となる変数を定義する
    @PrimaryKey open var Id: String = UUID.randomUUID().toString(),
    open var hizuke: String = "",
    open var nedan:Int = 0,
    open var saihuIcon:Int = 0,
    open var zyanruIcon:Int = 0,
    open var saihu:String = "",
    //open var nedanDateView:MutableList<OkaneListDateResycle> = mutableListOf()


): RealmObject() //RealmObjectという方を継承している部分 メモというクラスをRealmで保存できる型にすることができる