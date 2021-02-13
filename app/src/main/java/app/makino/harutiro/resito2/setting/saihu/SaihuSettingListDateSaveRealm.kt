package app.makino.harutiro.resito2.setting.saihu

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class SaihuSettingListDateSaveRealm(

    open var name:String = "",
    open var image:String ="",
    @PrimaryKey open var Id: String? = UUID.randomUUID().toString()

    ):RealmObject()