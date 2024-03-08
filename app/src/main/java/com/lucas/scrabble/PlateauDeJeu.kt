package com.lucas.scrabble
import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PlateauDeJeu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plateau_de_jeu)

        val boardGridLayout: GridLayout = findViewById(R.id.boardGridLayout)

        // Création du plateau de jeu
        for (row in 0 until 15) {
            for (col in 0 until 15) {
                val textView = TextView(this)
                val params = GridLayout.LayoutParams()
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                textView.layoutParams = params
                textView.width = 50 // Taille d'une case en pixels
                textView.height = 50
                textView.text = ""
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textView.setBackgroundColor(getCellColor(row, col))
                textView.setTextColor(Color.BLACK)
                boardGridLayout.addView(textView)
            }
        }
    }

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