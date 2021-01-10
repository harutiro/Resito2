package app.makino.harutiro.resito2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val context: Context):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    //リサイクラービューに表示するリストを宣言する
    val items: MutableList<OkaneListDateResycle> = mutableListOf()

    //データをcourseDateと結びつける？？
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val saihuIconImage: ImageView = view.findViewById(R.id.saihuIconId)
        val zyanruIconImage: ImageView = view.findViewById(R.id.zyanruIconId)
        val hizukeText: TextView = view.findViewById(R.id.hizukeId)
        val saihuText: TextView = view.findViewById(R.id.saihuId)
        val nedanText: TextView = view.findViewById(R.id.nedanId)
    }

    //はめ込むものを指定
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_okanelist_date_call,parent,false)
        return ViewHolder(view)
    }

    //itemsのposition番目の要素をviewに表示するコード
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.saihuIconImage.setImageResource(item.saihuIcon)
        holder.zyanruIconImage.setImageResource(item.zyanruIcon)
        holder.hizukeText.text = item.hizuke
        holder.saihuText.text = item.saihu.toString()
        holder.nedanText.text = item.nedan.toString()


    }

    //引数にとったリストをadapterに追加するメソッド
    fun addAll(items: List<OkaneListDateResycle>){
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    //リストの要素数を返すメソッド
    override fun getItemCount(): Int {

        return items.size
    }
}