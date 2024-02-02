package com.lucas.scrabble

class Plateau {
    private var plateau = arrayOf(
        arrayOf("3M", "0", "0", "2L", "0", "0", "0", "2M", "0", "0", "0", "2L", "0", "0", "3M"),
        arrayOf("0", "2M", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "2M", "0"),
        arrayOf("0", "0", "2M", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2M", "0", "0"),
        arrayOf("2L", "0", "0", "3L", "0", "0", "0", "2L", "0", "0", "0", "2M", "0", "0", "2L"),
        arrayOf("0", "0", "0", "0", "2M", "0", "0", "0", "0", "0", "2M", "0", "0", "0", "0"),
        arrayOf("0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0"),
        arrayOf("0", "0", "2L", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2L", "0"),
        arrayOf("3M", "0", "0", "2L", "0", "0", "0", "CD", "0", "0", "0", "2L", "0", "0", "3M"),
        arrayOf("0", "0", "2L", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2L", "0", "0"),
        arrayOf("0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0"),
        arrayOf("0", "0", "0", "0", "2M", "0", "0", "0", "0", "0", "2M", "0", "0", "0", "0"),
        arrayOf("2L", "0", "0", "2M", "0", "0", "0", "2L", "0", "0", "0", "2M", "0", "0", "2L"),
        arrayOf("0", "0", "2M", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2M", "0", "0"),
        arrayOf("0", "2M", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "2M", "0"),
        arrayOf("3M", "0", "0", "2L", "0", "0", "0", "3M", "0", "0", "0", "2L", "0", "0", "3M"),
    )

    val collisionOk = listOf("0", "2L", "3L", "2M", "3M", "CD")

    fun verifCaseLibre(i: Int, j: Int) : Boolean{
        return plateau[i][j] in collisionOk
    }

    fun listeDeMots() : List<String>{
        var list = mutableListOf<String>()
        var mot : String = ""
        for (i in 0..14){
            for (j in 0..14){
                if (verifCaseLibre(i, j)){
                    if (mot.length>1){list.add(mot)}
                    mot = ""
                }
                else{
                    mot += plateau[i][j]
                }
            }
            if (mot.length>1){list.add(mot)}
        }

        mot = ""
        for (i in 0..14){
            for (j in 0..14){
                if (verifCaseLibre(j, i)){
                    if (mot.length>1){list.add(mot)}
                    mot = ""
                }
                else{
                    mot += plateau[j][i]
                }
            }
            if (mot.length>1){list.add(mot)}
        }
        return list
    }
}