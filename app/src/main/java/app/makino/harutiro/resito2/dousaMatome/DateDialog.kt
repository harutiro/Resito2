package app.makino.harutiro.resito2.dousaMatome

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DateDialog(val motoDate: String ,private val onSelected: (String) -> Unit) :
            DialogFragment(), DatePickerDialog.OnDateSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()


            //デフォルト設定の設定部分
            val year = Henkan(motoDate).yyyy().toInt()
            val month = Henkan(motoDate).mm().toInt()
            val date = Henkan(motoDate).dd().toInt()


            //デフォルト設定出力部分
            return DatePickerDialog(requireActivity(), this, year, month, date)
        }

        //帰ってきたデータを送り返すところ
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

            //データを書き換えるよう
            val month1 = month + 1

            //基本データ書き込み
            var monthString = month1.toString()
            var dayOfMonthString = dayOfMonth.toString()

            //データが一桁ならゼロ追加
            if(month1 < 10){
                monthString = "0" + month1.toShort()
            }
            if(dayOfMonth < 10){
                dayOfMonthString = "0" + dayOfMonth.toString()
            }


            onSelected("${year}年 ${monthString}月 ${dayOfMonthString}日")

        }
    }
