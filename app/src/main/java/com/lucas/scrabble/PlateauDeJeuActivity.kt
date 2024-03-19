package com.lucas.scrabble

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView


class PlateauDeJeuActivity : AppCompatActivity() {

    private var selectedLetter: View? = null

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plateau)

        val boardGridLayout: GridLayout = findViewById(R.id.boardGridLayout)

        // Création du plateau de jeu
        for (row in 0 until 15) {
            for (col in 0 until 15) {
                val textView = TextView(this)
                val params = GridLayout.LayoutParams()
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                params.setMargins(6, 6, 6, 6)
                textView.layoutParams = params
                textView.width = 52 // Taille d'une case en pixels
                textView.height = 52
                textView.text = ""
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textView.setBackgroundColor(getCellColor(row, col))
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.setColor(getCellColor(row, col))
                shape.cornerRadius = 7f // Définir le rayon pour arrondir les coins
                textView.background = shape
                textView.setTextColor(Color.BLACK)
                boardGridLayout.addView(textView)
            }
        }

        // Inclut la main du joueur
        val playerHandLayout: LinearLayout = findViewById(R.id.playerHandLayout)
        //val playerHandView = LayoutInflater.from(this).inflate(R.layout.player_hand, null)
        //playerHandLayout.addView(playerHandView)

        // Inclut le plateau de jeu
        val plateauDeJeuLayout: RelativeLayout = findViewById(R.id.plateauDeJeuLayout)
        //val plateauDeJeuView = LayoutInflater.from(this).inflate(R.layout.plateau_de_jeu, null)
        //plateauDeJeuLayout.addView(plateauDeJeuView)

        // Ajout de lettres à la main du joueur
        /*var player = Inventory()
        for(joueur in listJoueurs){
            if(joueur.getPseudo()==compte.pseudo){
                player = joueur
            }
        }
        val playerHand = player.getMain()*/
        val playerHand = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G')

        // Crée une vue pour chaque lettre dans la main du joueur
        playerHand.forEach { letter ->
            val textView = TextView(this)
            textView.text = letter.toString()
            textView.textSize = 24f
            textView.gravity = Gravity.CENTER
            textView.setBackgroundColor(ContextCompat.getColor(this, R.color.brown))
            textView.setPadding(16, 16, 16, 16)

            // Ajoute une marge entre les lettres
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            textView.layoutParams = params

            // Ajoute un listener pour détecter les clics sur les lettres
            textView.setOnClickListener {
                // Code pour gérer le clic sur une lettre de la main du joueur
                if (selectedLetter == null&&(it.background as ColorDrawable).color != -1 ){
                    // Récupère la lettre du TextView cliqué
                    selectedLetter = it
                    it.setBackgroundColor(ContextCompat.getColor(this, R.color.lightbrown))
                }else if (selectedLetter == null){
                    selectedLetter =it
                }


                // Ajoute un écouteur de clic à chaque case du plateau
                val plateauDeJeu = findViewById<GridLayout>(R.id.boardGridLayout)
                for (i in 0 until plateauDeJeu.childCount) {
                    val case = plateauDeJeu.getChildAt(i)
                    case.setOnClickListener {
                        if (selectedLetter != null){
                            if ((selectedLetter!!.background as ColorDrawable).color == -1323865 ){
                                // Placer la lettre sélectionnée dans la case du plateau
                                selectedLetter!!.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                                (it as TextView).text = (selectedLetter as TextView).text
                                selectedLetter = null
                            }
                            else if((selectedLetter!!.background as ColorDrawable).color == -1 ){
                                if ((selectedLetter!! as TextView).text == (it as TextView).text ){
                                    selectedLetter!!.setBackgroundColor(ContextCompat.getColor(this, R.color.brown))
                                    (it as TextView).text = ""
                                    selectedLetter = null
                                }
                            }

                        }
                    }
                }
            }
            playerHandLayout.addView(textView)
        }
        var poubelle = findViewById<ImageView>(R.id.btn_poubelle)
        poubelle.setOnClickListener{
            selectedLetter?.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            selectedLetter = null
        }
    }


    //////////Coloration//////////


    private fun getCellColor(row: Int, col: Int): Int {
        // Logique pour déterminer la couleur de chaque case du plateau (par exemple, cases spéciales)
        return when {
            isSpecialCase(row, col) -> ContextCompat.getColor(this, R.color.yellow)
            isSpecialCase1(row, col) -> ContextCompat.getColor(this, R.color.red)
            isSpecialCase2(row, col) -> ContextCompat.getColor(this, R.color.blue)
            isSpecialCase3(row, col) -> ContextCompat.getColor(this, R.color.green)
            else -> ContextCompat.getColor(this,R.color.grey)
        }
    }

    private fun isSpecialCase(row: Int, col: Int): Boolean {
        // Logique pour vérifier si c'est une case de mot compte triple ou non
        // Retourne true si c'est une case spéciale, sinon false
        val specialCases = arrayOf(
            Pair(0, 0), Pair(0, 7), Pair(0, 14),
            Pair(7, 0), Pair(7, 14),
            Pair(14, 0), Pair(14, 7), Pair(14, 14)
        )
        return specialCases.contains(Pair(row, col))
    }

    private fun isSpecialCase1(row: Int, col: Int): Boolean {
        //mots compte double
        val specialCases = arrayOf(
            Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4, 4),
            Pair(10, 10), Pair(11, 11), Pair(12, 12), Pair(13, 13),
            Pair(1, 13), Pair(2, 12), Pair(3, 11), Pair(4, 10),
            Pair(10, 4), Pair(11, 3), Pair(12, 2), Pair(13, 1)
        )
        return specialCases.contains(Pair(row, col))
    }

    private fun isSpecialCase2(row: Int, col: Int): Boolean {
        //lettre compte double
        val specialCases = arrayOf(
            Pair(0, 3), Pair(0, 11),
            Pair(2, 6), Pair(2, 8),
            Pair(3, 0), Pair(3, 7), Pair(3, 14),
            Pair(6, 2), Pair(6, 6), Pair(6, 8), Pair(6, 12),
            Pair(7, 3), Pair(7, 11),
            Pair(8, 2), Pair(8, 6), Pair(8, 8), Pair(8, 12),
            Pair(11, 0), Pair(11, 7), Pair(11, 14),
            Pair(12, 6), Pair(12, 8),
            Pair(14, 3), Pair(14, 11)
        )
        return specialCases.contains(Pair(row, col))
    }

    private fun isSpecialCase3(row: Int, col: Int): Boolean {
        // mot compte triple ou non
        val specialCases = arrayOf(
            Pair(1, 5), Pair(1, 9),
            Pair(5, 1), Pair(5, 5), Pair(5, 9), Pair(5, 13),
            Pair(9, 1), Pair(9, 5), Pair(9, 9), Pair(9, 13),
            Pair(13, 5), Pair(13, 9)
        )
        return specialCases.contains(Pair(row, col))
    }
}
