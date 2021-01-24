package app.makino.harutiro.resito2.input

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import app.makino.harutiro.resito2.OkaneListDateSaveRealm
import app.makino.harutiro.resito2.R
import app.makino.harutiro.resito2.testInput
import com.google.android.material.button.MaterialButtonToggleGroup
import io.realm.Realm

class inputPageNedan : AppCompatActivity() {

    var firstNumber = 0.0
    var secondNumber = 0.0
    var totalNumber = 0.0
    var operator = "empty"

    var plusButton :Button? = null
    var minusButton :Button? = null
    var multiplyButton :Button? = null
    var equalButton :Button? = null
    var waruButton :Button? = null

    var numberText :TextView? = null
    var numberButton0 :Button? = null
    var numberButton1 :Button? = null
    var numberButton2 :Button? = null
    var numberButton3 :Button? = null
    var numberButton4 :Button? = null
    var numberButton5 :Button? = null
    var numberButton6 :Button? = null
    var numberButton7 :Button? = null
    var numberButton8 :Button? = null
    var numberButton9 :Button? = null

    var clearBotton :Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_page_nedan)

        plusButton = findViewById<Button>(R.id.plusButton)
        minusButton = findViewById<Button>(R.id.minusButton)
        multiplyButton = findViewById<Button>(R.id.multiplyButton)
        equalButton = findViewById<Button>(R.id.equalButton)
        waruButton = findViewById<Button>(R.id.waruButton)

        numberText = findViewById<TextView>(R.id.numberText)
        numberButton0 = findViewById<Button>(R.id.numberButton0)
        numberButton1 = findViewById<Button>(R.id.numberButton1)
        numberButton2 = findViewById<Button>(R.id.numberButton2)
        numberButton3 = findViewById<Button>(R.id.numberButton3)
        numberButton4 = findViewById<Button>(R.id.numberButton4)
        numberButton5 = findViewById<Button>(R.id.numberButton5)
        numberButton6 = findViewById<Button>(R.id.numberButton6)
        numberButton7 = findViewById<Button>(R.id.numberButton7)
        numberButton8 = findViewById<Button>(R.id.numberButton8)
        numberButton9 = findViewById<Button>(R.id.numberButton9)

        clearBotton = findViewById(R.id.clearBotton)

        val intent = findViewById<Button>(R.id.intentButton)

        intent.setOnClickListener {
            //計算途中かどうか判断
            if(totalNumber == 0.0) {
                totalNumber += firstNumber
            }



            //インテント
            val inputPage = Intent(this,testInput::class.java)
            inputPage.putExtra("nedanItiziDate",totalNumber.toInt())
            startActivity(inputPage)
            finish()

            println(totalNumber)

        }




        plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
        minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
        multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
        waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
        equalButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))



        plusButton?.isEnabled = false
        minusButton?.isEnabled = false
        multiplyButton?.isEnabled = false
        equalButton?.isEnabled = false
        waruButton?.isEnabled = false


        numberText?.text = firstNumber.toString()


        //0ボタン
        numberButton0?.setOnClickListener {
            onNumber(10,0)
        }

        //1ボタン
        numberButton1?.setOnClickListener {
            onNumber(10,1)
        }

        //２ボタン
        numberButton2?.setOnClickListener {
            onNumber(10,2)

        }

        //3botton
        numberButton3?.setOnClickListener {
            onNumber(10,3)

        }

        //4botton
        numberButton4?.setOnClickListener {
            onNumber(10,4)
        }

        //5botton
        numberButton5?.setOnClickListener {
            onNumber(10,5)
        }

        //6botton
        numberButton6?.setOnClickListener {
            onNumber(10,6)
        }

        //7botton
        numberButton7?.setOnClickListener {
            onNumber(10,7)
        }

        //8botton
        numberButton8?.setOnClickListener {
            onNumber(10,8)

        }

        //9botton
        numberButton9?.setOnClickListener {
            onNumber(10,9)
        }

        //+botton
        plusButton?.setOnClickListener {
            plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(220,0,100))
            minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))

            operator = "plus"

            numberText?.text = secondNumber.toString()

        }

        //-botton
        minusButton?.setOnClickListener {
            minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(220,0,100))
            plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))

            operator = "minus"

            numberText?.text = secondNumber.toString()

        }

        //*botton
        multiplyButton?.setOnClickListener {
            multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(220,0,100))
            minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))

            operator = "multiply"

            numberText?.text = secondNumber.toString()

        }

        // /botton
        waruButton?.setOnClickListener {
            waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(220,0,100))
            plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))

            operator = "waru"

            numberText?.text = secondNumber.toString()

        }



        //=botton
        equalButton?.setOnClickListener {
            if (operator == "plus"){
                totalNumber = firstNumber + secondNumber
            }else if (operator == "minus"){
                totalNumber = firstNumber - secondNumber
            }else if (operator == "multiply"){
                totalNumber = firstNumber * secondNumber
            }else if (operator == "waru"){
                totalNumber = firstNumber / secondNumber
            }

            plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            equalButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))

            firstNumber = totalNumber
            secondNumber = 0.0
            operator = "empty"
            numberText?.text = totalNumber.toString()

            plusButton?.isEnabled = true
            minusButton?.isEnabled = true
            multiplyButton?.isEnabled = true
            equalButton?.isEnabled = true
            waruButton?.isEnabled = true

        }

        //C,botton
        clearBotton?.setOnClickListener {
            plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            equalButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))
            waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))


            firstNumber = 0.0
            secondNumber = 0.0
            totalNumber = 0.0

            operator = "empty"

            numberText?.text = firstNumber.toString()

            plusButton?.isEnabled = false
            minusButton?.isEnabled = false
            multiplyButton?.isEnabled = false
            equalButton?.isEnabled = false
            waruButton?.isEnabled = false


        }




    }




    fun onNumber(num1: Int,num2: Int) {

        if (operator == "empty") {
            firstNumber = firstNumber * num1 + num2

            numberText?.text = firstNumber.toString()

            plusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
            minusButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
            multiplyButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
            waruButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))
            equalButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150,150,150))


            plusButton?.isEnabled = true
            minusButton?.isEnabled = true
            multiplyButton?.isEnabled = true
            equalButton?.isEnabled = false
            waruButton?.isEnabled = true



        } else {
            secondNumber = secondNumber * num1 + num2

            numberText?.text = secondNumber.toString()

            equalButton?.backgroundTintList = ColorStateList.valueOf(Color.rgb(150, 150, 150))

            equalButton?.isEnabled = true

        }

    }

}