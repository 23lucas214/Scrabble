package com.lucas.scrabble

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.random.Random

class Jeu {

    /**
     * Il s'agit de la pioche du jeu avec les lettres toujours piochables
     */
    private var pioche = mutableListOf<String>()

    /**
     * Le dictionnaire est un set qui comporte tous les mots admissibles dans le jeu
     */
    var dictionnaire = mutableSetOf<String>()

    /**
     * Map qui donne la valeur de chaque lettre
     */
    private val pointLettre = hashMapOf<String, Int>("A" to 1, "B" to 3, "C" to 3, "D" to 2,
        "E" to 1, "F" to 4, "G" to 2, "H" to 4, "I" to 1, "J" to 8, "K" to 10, "L" to 1, "M" to 2,
        "N" to 1, "O" to 1, "P" to 3, "Q" to 8, "R" to 1, "S" to 1, "T" to 1, "U" to 1, "V" to 4,
        "W" to 10, "X" to 10, "Y" to 10, "Z" to 10, " " to 0)

    /**
     * Attribut le nombre de points a une lettre
     */
    fun lettrePoints(l: String): Int{
        return pointLettre.getOrDefault(l, 0)
    }

    /**
     * Initialise la pioche en la remplissant des lettres du scrabble
     */
    fun initPioche() {
        for (i in 1..102) {
            val lettre = when {
                9 >= i -> "A"
                11 >= i -> "B"
                13 >= i -> "C"
                16 >= i -> "D"
                31 >= i -> "E"
                33 >= i -> "F"
                35 >= i -> "G"
                37 >= i -> "H"
                45 >= i -> "I"
                46 >= i -> "J"
                47 >= i -> "K"
                52 >= i -> "L"
                55 >= i -> "M"
                61 >= i -> "N"
                67 >= i -> "O"
                69 >= i -> "P"
                70 >= i -> "Q"
                76 >= i -> "R"
                82 >= i -> "S"
                88 >= i -> "T"
                94 >= i -> "U"
                96 >= i -> "V"
                97 >= i -> "W"
                98 >= i -> "X"
                99 >= i -> "Y"
                100 >= i -> "Z"
                else -> " "
            }
            pioche.add(lettre)
        }
    }

    /**
     * Initialise le dictionnaire en le remplissant de tous les mots admissibles a partir d'un fichier texte
     */
    fun initDictionnaire(fichier : String) {
        val file = File(fichier)
        try {
            File(fichier).forEachLine { dictionnaire.add(it) }
        }catch(e: Exception){print(e)}
    }

    fun majBDON(){ // a faire a la fin de chaque tour de chaque joueur

    }

    fun majJoueurs(){ // a faire dès qu'on trouve une modif dans la base de données

    }
    fun tourDejeu(compte : Compte, pseudo: String, nextPseudo: String, plateauDebutTour: Plateau, plateauFinTour: Plateau, firstTurn: Boolean = false){
        var request = ""
        var finDeTour = 0
        var rs : ResultSet
        var stmt : PreparedStatement
        var connect = DriverManager.getConnection(compte.url, compte.loginDB, compte.passwordDB)
        while (!plateauFinTour.verifierAjoutPlateau(plateauDebutTour, this, firstTurn)) {


            pseudo = rs.getString(1);


            //TOUR DE PSEUDO



            // fin du tour :
            majBDON()
            request = "UPDATE partie SET tour=?"
            stmt = connect.prepareStatement(request)
            stmt.setString(1, nextPseudo)
            stmt.executeUpdate()
            while (finDeTour == 1) {
                wait 3 seconds
                requete sql
                        if (response) {
                            majJoueurs()
                            finDeTour = 0;
                        }
            }
                //tour du joueur suivant
            }
        }
    }

    fun jeu(listPseudosTemp : MutableList<String>){

        //Tirer au sort l'ordre de jeu
        var alea : Int
        var listPseudos = mutableListOf<String>()
        while (listPseudosTemp.isNotEmpty()){
            alea = Random.nextInt(listPseudosTemp.size)
            listPseudos.add(listPseudosTemp.removeAt(alea))
        }

        //Initialisation du jeu
        initPioche()
        initDictionnaire("") //lien fichier text

        //Creation des inventaires
        var inventaires = mutableListOf<Inventory>()
        for (x in listPseudos){
            inventaires.add(Inventory())
            inventaires.get(inventaires.size).tirerNLettres(7, pioche)
        }

        //Creation des plateaux
        var plateauDebutTour = Plateau()
        var plateauFinTour = Plateau()
        var running = true
        var turn = 0

        //Gestion BDONN
        compte.connect()
        var compte = Compte()
        compte.initProperties()

        //Turn 0
        tourDejeu(compte, listPseudos.get(0), listPseudos.get(1), plateauFinTour, plateauDebutTour, true)

        //Main loop
        while (running){
            turn++
            tourDejeu(compte, listPseudos.get(turn%listPseudos.size), listPseudos.get(turn%listPseudos.size+1) plateauFinTour, plateauDebutTour) // Attention a size +1
            plateauDebutTour.copier(plateauFinTour)
        }
        compte.disconnect()
    }
}