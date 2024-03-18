package com.lucas.scrabble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //quand on lance une partie :
        var jeu = Jeu()
        var id = 123 // A choisir par l'utilisateur
        var nbJoueurs = 3 // A choisir par l'utilisateur 2/3/4
        jeu.jeu(nbJoueurs,id)
    }
}