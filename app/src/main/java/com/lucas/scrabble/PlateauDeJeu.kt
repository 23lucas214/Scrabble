package com.lucas.scrabble
import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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
                textView.width = 100 // Taille d'une case en pixels
                textView.height = 100
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
        return if ((row == 0 || row == 7 || row == 14) && (col == 0 || col == 7 || col == 14))
            Color.LTGRAY
        else
            Color.WHITE
    }
}