package com.lucas.scrabble

import kotlin.random.Random
import kotlin.random.nextInt

class Inventory {

    /**
     * Main d'un joueur
     */
    private val main = mutableListOf<String>()

    fun addNLetter(n: Int){
        for (i in 1..n){
            main.add(tirer())
        }
    }

    fun tirer() : String{
        val alea = Random.nextInt(101) + 1
        val lettre  = when {
            9 >= alea -> "A"
            11 >= alea -> "B"
            13 >= alea -> "C"
            16 >= alea -> "D"
            31 >= alea -> "E"
            33 >= alea -> "F"
            35 >= alea -> "G"
            37 >= alea -> "H"
            45 >= alea -> "I"
            46 >= alea -> "J"
            47 >= alea -> "K"
            52 >= alea -> "L"
            55 >= alea -> "M"
            61 >= alea -> "N"
            67 >= alea -> "O"
            69 >= alea -> "P"
            70 >= alea -> "Q"
            76 >= alea -> "R"
            82 >= alea -> "S"
            88 >= alea -> "T"
            94 >= alea -> "U"
            96 >= alea -> "V"
            97 >= alea -> "W"
            98 >= alea -> "X"
            99 >= alea -> "Y"
            100 >= alea -> "Z"
            102 >= alea -> " "
            else -> "err"
        }
        return lettre
    }
}