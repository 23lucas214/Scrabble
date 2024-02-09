package com.lucas.scrabble

import kotlin.random.Random

class Inventory {
    private var score : Int = 0
    private var scrabble : Boolean = false //remettre false apres chaque tour de jeu

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

    /**
     * Tirer une lettre au hasard dans la pioche
     */
    fun tirer(pioche: MutableList<String>) : String{
        val alea = Random.nextInt(pioche.size) + 1
        var lettre : String = pioche.removeAt(alea)
        return lettre
    }

    fun calculPointsMot(mot: Pair<String,MutableList<Pair<Int,Int>>>, plateauDebutTour: Plateau, jeu : Jeu): Int{
        var scoreMot = 0
        var multiplicateurLettre = 1
        var multiplicateurMot = 1
        var contenu: String
        var position: Pair<Int,Int>
        for (k in 1..mot.second.size-1) {
            position = mot.second.get(k)
            contenu = plateauDebutTour.getCase(position)
            if ((contenu != "2M") && (contenu != "3M")) {
                multiplicateurLettre = when {
                    contenu == "2L" -> 2
                    contenu == "3L" -> 3
                    else -> 1
                }
            } else if (contenu == "2M") {
                multiplicateurMot= 2
            } else {
                multiplicateurMot = 3
            }
            scoreMot += multiplicateurLettre * jeu.lettrePoints(mot.first[k].toString())
            multiplicateurLettre = 1
        }
        score += scoreMot * multiplicateurMot
        return scoreMot * multiplicateurMot
        //Scrabble
    }

    /**
     * Fonction qui renvoie la liste des mots ajoutes durant un tour
     */
    fun motsAjoutes(plateauDebutTour: Plateau, plateauFinTour: Plateau): MutableList<Pair<String, MutableList<Pair<Int, Int>>>>{
        var list = plateauFinTour.listeDeMots()
        list.removeAll(plateauDebutTour.listeDeMots())
        return list
    }


    fun poserLettre(index: Int, plateau: Plateau, position: Pair<Int,Int>){
        plateau.setCase(position, main.removeAt(index))
    }

    fun recupererLettre(position: Pair<Int,Int>, plateauDebutTour: Plateau, plateauActu: Plateau){
        if(plateauActu.getCase(position) != plateauDebutTour.getCase(position)) {
            main.add(plateauActu.getCase(position))
            plateauActu.setCase(position, plateauDebutTour.getCase(position))
        }
    }

    fun deplacerLettreInv(index1: Int, index2: Int){
        val l = main.get(index1)
        main.set(index1, main.get(index2))
        main.set(index2, l)
    }
    fun echangeDeLettre(index: Int, pioche: MutableList<String>){
        val tempo = main.removeAt(index)
        tirerNLettres(1, pioche)
        pioche.add(tempo)
    }
}
