package com.lucas.scrabble

import kotlin.random.Random

class Inventory {
    private var score : Int = 0
    private var points : Int = 0
    private var pseudo : String = ""

    /**
     * Main d'un joueur
     */
    private var main = mutableListOf<String>()

    /**
     * Tirer n lettres et les ajouter a la main
     */
    fun tirerNLettres(n: Int, L: MutableList<String>){
        for (i in 1..n){
            main.add(tirer(L))
        }
    }
    fun getMain(): MutableList<String>{
        return main
    }

    fun resetMain(){
        this.main = mutableListOf()
    }
    fun getScore(): Int{
        return score
    }
    fun setScore(score : Int){
        this.score = score
    }

    fun getPoints(): Int {
        return points
    }
    fun setPoints(points : Int){
        this.points = points
    }

    fun getPseudo(): String {
        return pseudo
    }
    fun setPseudo(pseudo : String){
        this.pseudo = pseudo
    }

    /**
     * Tirer une lettre au hasard dans la pioche
     */
    fun tirer(pioche: MutableList<String>) : String{
        val alea = Random.nextInt(pioche.size) + 1
        var lettre : String = pioche.removeAt(alea)
        return lettre
    }

    /**
     * Calcul les points generes a un tour
     */
    fun calculPointsMot(plateauDebutTour: Plateau, plateauFinTour: Plateau, jeu : Jeu): Int{
        var lettrePosees = 0
        for (i in 0..14){
            for (j in 0..14){
                if (plateauFinTour.getCase(Pair(i, j)) != plateauDebutTour.getCase(Pair(i, j))){
                    lettrePosees += 1
                }
            }
        }
        if(plateauFinTour.motsAjoutes(plateauDebutTour).isNotEmpty()) {
            val mot = plateauFinTour.motsAjoutes(plateauDebutTour).get(0)
            var scoreMot = 0
            var multiplicateurLettre = 1
            var multiplicateurMot = 1
            var contenu: String
            var position: Pair<Int, Int>
            for (k in 0..mot.second.size - 1) {
                position = mot.second.get(k)
                contenu = plateauDebutTour.getCase(position)
                if ((contenu != "2M") && (contenu != "3M")) {
                    multiplicateurLettre = when {
                        contenu == "2L" -> 2
                        contenu == "3L" -> 3
                        else -> 1
                    }
                } else if (contenu == "2M") {
                    multiplicateurMot = 2
                } else {
                    multiplicateurMot = 3
                }
                scoreMot += multiplicateurLettre * jeu.lettrePoints(mot.first[k].toString())
                multiplicateurLettre = 1
            }
            val scrabble = if (lettrePosees == 7) 1 else 0
            score += scoreMot * multiplicateurMot + scrabble * 50
            return scoreMot * multiplicateurMot + scrabble * 50
        }
        else { return 0}
    }

    /**
     * Pose une lettre de la main sur un plateau
     */
    fun poserLettre(index: Int, plateau: Plateau, position: Pair<Int,Int>){
        plateau.setCase(position, main.removeAt(index))
    }

    /**
     * Permet de recuperer une piece posee pendant le tour
     */
    fun recupererLettre(position: Pair<Int,Int>, plateauDebutTour: Plateau, plateauActu: Plateau){
        if(plateauActu.getCase(position) != plateauDebutTour.getCase(position)) {
            main.add(plateauActu.getCase(position))
            plateauActu.setCase(position, plateauDebutTour.getCase(position))
        }
    }

    /**
     *Echange deux lettres dans l'inventaire pour les ordonner
     */
    fun deplacerLettreInv(index1: Int, index2: Int){
        val l = main.get(index1)
        main.set(index1, main.get(index2))
        main.set(index2, l)
    }

    /**
     * Defausser une piece
     */
    fun echangeDeLettre(index: Int, pioche: MutableList<String>){
        val tempo = main.removeAt(index)
        tirerNLettres(1, pioche)
        pioche.add(tempo)
    }
}
