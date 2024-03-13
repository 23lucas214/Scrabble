package com.lucas.scrabble

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.widget.GridLayout



class PlateauDeJeuActivity : AppCompatActivity() {

    private lateinit var plateauDeJeu: PlateauDeJeu
    private lateinit var selectedLetter: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plateau)

        // Instancie le plateau de jeu
        plateauDeJeu = PlateauDeJeu()

        // Inclut le plateau de jeu
        val plateauDeJeuLayout: RelativeLayout = findViewById(R.id.plateauDeJeuLayout)
        val plateauDeJeuView = LayoutInflater.from(this).inflate(R.layout.plateau_de_jeu, null)
        plateauDeJeuLayout.addView(plateauDeJeuView)

        // Inclut la main du joueur
        val playerHandLayout: LinearLayout = findViewById(R.id.playerHandLayout)
        val playerHandView = LayoutInflater.from(this).inflate(R.layout.player_hand, null)
        playerHandLayout.addView(playerHandView)

        // Ajout de lettres à la main du joueur
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

                // Récupère la lettre du TextView cliqué
                selectedLetter = (it as TextView).text.toString()

                // Change la couleur de fond pour indiquer que la lettre a été sélectionnée
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.lightbrown))

                // Ajoute un écouteur de clic à chaque case du plateau
                val plateauDeJeu = findViewById<GridLayout>(R.id.boardGridLayout)
                for (i in 0 until plateauDeJeu.childCount) {
                    val case = plateauDeJeu.getChildAt(i)
                    case.setOnClickListener {
                        // Placer la lettre sélectionnée dans la case du plateau
                        (it as TextView).text = selectedLetter
                    }
                }
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }

            playerHandLayout.addView(textView)
        }
    }
}
