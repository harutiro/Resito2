package app.makino.harutiro.resito2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val context: Context,private val listener: OnItemClickListner):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    //リサイクラービューに表示するリストを宣言する
    val items: MutableList<OkaneListDateSaveRealm> = mutableListOf()

    //データをcourseDateと結びつける？？
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val container: ConstraintLayout = view.findViewById(R.id.container)

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

        // MainActivity側でタップしたときの動作を記述するため，n番目の要素を渡す
        holder.container.setOnClickListener { listener.onItemClick(item) }

        holder.saihuIconImage.setImageResource(item.saihuIcon)
        holder.zyanruIconImage.setImageResource(item.zyanruIcon)
        holder.hizukeText.text = item.hizuke
        holder.saihuText.text = item.saihu
        holder.nedanText.text = item.nedan.toString() + "￥"

        println(item.saihu)


    }

    //引数にとったリストをadapterに追加するメソッド
    fun addAll(items: List<OkaneListDateSaveRealm>){
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    //編集したデータの受け取り。
    fun setList(list: MutableList<OkaneListDateSaveRealm>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    //リストの要素数を返すメソッド
    override fun getItemCount(): Int {

        return items.size
    }

    // RecyclerViewの要素をタップするためのもの
    interface OnItemClickListner{
        fun onItemClick(item: OkaneListDateSaveRealm)
    }
}