package app.makino.harutiro.resito2.dousaMatome

//文字データを年、月、日にバラバラにするクラス
class Henkan(val motoDate:String){

    //アルス信号の応用！！

    fun yyyy(): String {
        var charAry = motoDate.toCharArray()
        var ukeire = ""

        for(ch in charAry){

            when(ch){

                ' '-> ukeire += ""
                '年'-> break
                else->ukeire += ch

            }

        }

        return ukeire
    }

    fun mm():String{
        var charAry = motoDate.toCharArray()
        var ukeire = ""
        var hantei = false

        for (ch in charAry){

            when(ch){

                ' '-> ukeire += ""
                '年'-> hantei = true
                '月'-> break
                else-> if(hantei){
                    ukeire += ch
                }

            }
        }

        return (ukeire.toInt()-1).toString()
    }

    fun mm2():String{
        var charAry = motoDate.toCharArray()
        var ukeire = ""
        var hantei = false

        for (ch in charAry){

            when(ch){

                ' '-> ukeire += ""
                '年'-> hantei = true
                '月'-> break
                else-> if(hantei){
                    ukeire += ch
                }

            }
        }

        return ukeire
    }

    fun dd():String{
        var charAry = motoDate.toCharArray()
        var ukeire = ""
        var hantei = false

        for (ch in charAry){

            when(ch){

                ' '-> ukeire += ""
                '月'-> hantei = true
                '日'-> break
                else-> if(hantei){
                    ukeire += ch
                }

            }
        }

        return ukeire
    }
}