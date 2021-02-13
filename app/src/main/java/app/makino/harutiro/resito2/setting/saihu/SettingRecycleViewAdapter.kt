package app.makino.harutiro.resito2.setting.saihu

import android.content.Context
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import app.makino.harutiro.resito2.OkaneListDateSaveRealm
import app.makino.harutiro.resito2.R
import app.makino.harutiro.resito2.RecyclerViewAdapter

class SettingRecycleViewAdapter (private val context: Context,private val listener: OnItemClickListner):
    RecyclerView.Adapter<SettingRecycleViewAdapter.ViewHolder>() {

    //リサイクラービューに表示するリストを宣言する
    val items: MutableList<SaihuSettingListDateSaveRealm> = mutableListOf()

    //データをcourseDateと結びつける？？
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val saihuIconImage: ImageView = view.findViewById(R.id.iconImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val container2:androidx.constraintlayout.widget.ConstraintLayout =view.findViewById(R.id.container2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_saihu_setting_date_call,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.saihuIconImage.setImageURI(item.image.toUri())
        holder.nameTextView.text = item.name
        holder.container2.setOnClickListener { listener.onItemClick(item) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //引数にとったリストをadapterに追加するメソッド
    fun addAll(items: List<SaihuSettingListDateSaveRealm>){
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    //編集したデータの受け取り。
    fun setList(list: MutableList<SaihuSettingListDateSaveRealm>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    // RecyclerViewの要素をタップするためのもの
    interface OnItemClickListner{
        fun onItemClick(item: SaihuSettingListDateSaveRealm)
    }

}