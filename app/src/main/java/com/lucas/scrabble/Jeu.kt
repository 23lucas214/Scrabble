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

    fun majBDON(compte : Compte, joueur : Inventory, id : Int){ // a faire a la fin de chaque tour de chaque joueur
        compte.connect()
        compte.initProperties()
        var connect = DriverManager.getConnection(compte.url, compte.loginDB, compte.passwordDB)
        var request = "UPDATE compte SET points=?,scorePartie=?  WHERE pseudo=? AND id_partie=?" //compte
        var stmt = connect.prepareStatement(request)
        stmt.setInt(1, joueur.getPoints())
        stmt.setInt(2,joueur.getScore())
        stmt.setString(3, joueur.getPseudo())
        stmt.setInt(4,id)
        stmt.executeUpdate()

        request = "UPDATE piece SET id_piece=?,lettre=?,X=?,Y=? WHERE id_partie=?" //pièce
        stmt = connect.prepareStatement(request)
        stmt.setInt(1, ) //????
        stmt.setInt(2,points+joueur.getscorePartie()) //??
        stmt.setString(3, )
        stmt.setInt(5,id)
        stmt.executeUpdate()

        request = "UPDATE pioche SET lettre=?,nbLettre=? WHERE id_partie=?" //pioche
        for(i in 1..pioche.size) {
            stmt = connect.prepareStatement(request)
            stmt.setString(1, pioche.get(i))
            stmt.setInt(2, compte(pioche.get(i),pioche))
            stmt.setInt(3, id)
            stmt.executeUpdate()
        }

        compte.disconnect()
    }

    fun compte(lettre : String, list : MutableList<String>): Int{
        var s = 0
        for(i in 1..list.size){
            if(list.get(i)==lettre){
                s++
            }
        }
        return s
    }

    fun majJoueurs(compte : Compte){ // a faire dès qu'on trouve une modif dans la base de données

    }
    fun tourDejeu(compte : Compte, joueur : Inventory, pseudo: String, nextPseudo: String, plateauDebutTour: Plateau, plateauFinTour: Plateau, firstTurn: Boolean = false){
        var request = ""
        var resultat = ""
        var res1 = ""
        var finDeTour = 0
        var rs : ResultSet
        var stmt : PreparedStatement
        var connect = DriverManager.getConnection(compte.url, compte.loginDB, compte.passwordDB)
        request = "SELECT tour FROM partie" //vérifie à qui c'est le tour
        stmt = connect.prepareStatement(request)
        rs = stmt.executeQuery()
        rs.next()
        resultat = rs.getString(1)
        res1 = resultat
        if(pseudo==resultat) {
            //TOUR DE PSEUDO
            while(!plateauFinTour.verifierAjoutPlateau(plateauDebutTour, this, firstTurn)){
                // REMPLIR ICI
            }
            // fin du tour :
            joueur.setPoints(joueur.calculPointsMot(plateauDebutTour,plateauFinTour, this))
            joueur.setScore(joueur.getScore()+joueur.getPoints())
            majBDON(compte, joueur) //maj des données
            request = "UPDATE partie SET tour=?" //maj du pseudo pour qui c'est le tour
            stmt = connect.prepareStatement(request)
            stmt.setString(1, nextPseudo)
            stmt.executeUpdate()
        }
        else {
            do {
                Thread.sleep(3000) // on attend 3 secondes
                request = "SELECT tour FROM partie" //vérifie à qui c'est le tour
                stmt = connect.prepareStatement(request)
                rs = stmt.executeQuery()
                rs.next()
                resultat = rs.getString(1)
            }while(resultat!=res1) // si on a trouvé un changement, on a fini le tour
            majJoueurs(compte) //on met a jour ses données
        }

    }

    fun rejoindre(pseudo: String, id : Int){
        //Gestion BDONN
        var compte = Compte()
        compte.initProperties()
        compte.connect()

        compte.joinGame(id, pseudo)
        compte.disconnect()
    }

    fun getRowCount(resultSet: ResultSet): Int {
        var rowCount = 0
        while (resultSet.next()) {
            rowCount++
        }
        return rowCount
    }

    fun jeu(nbjoueurs : Int, id : Int){

        //Gestion BDONN
        var compte = Compte()
        compte.initProperties()
        compte.connect()

        //Création de la partie
        compte.createNewGame(id,nbjoueurs,pioche)

        //Createur rejoint la partie
        rejoindre(compte.pseudo,id)

        //Créer la liste des joueurs:
        var listInventories = mutableListOf<Inventory>()


        //Attente de la connexion des joueurs : ECRAN ATTENTE
        var request = ""
        var stmt : PreparedStatement
        var rs : ResultSet
        var connect = DriverManager.getConnection(compte.url, compte.loginDB, compte.passwordDB)

        do {
            var nbJoueurs = 0
            Thread.sleep(3000) // on attend 3 secondes
            request = "SELECT pseudo FROM compte WHERE id_partie=?"
            stmt = connect.prepareStatement(request)
            stmt.setInt(1,id)
            rs = stmt.executeQuery()
            rs.next()
            for(i in 1..getRowCount(rs)) {
                nbJoueurs ++
            }
        }while(nbjoueurs==nbJoueurs)

        //Remplit la liste
        request = "SELECT pseudo FROM compte WHERE id_partie=?"
        stmt = connect.prepareStatement(request)
        stmt.setInt(1,id)
        rs = stmt.executeQuery()
        rs.next()
        for(i in 1..nbjoueurs) {
            listInventories.get(i).setPseudo(rs.getString(i))
        }

        //Tirer au sort l'ordre de jeu
        var alea : Int
        var listJoueurs = mutableListOf<Inventory>()
        while (listInventories.isNotEmpty()){
            alea = Random.nextInt(listInventories.size)
            listJoueurs.add(listInventories.get(alea))
            listInventories.removeAt(alea)
        }

        //Initialisation du jeu
        initPioche()
        initDictionnaire("") //lien fichier text

        //Remplissage des inventaires
        for (x in listInventories){
            x.tirerNLettres(7, pioche)
        }

        //Creation des plateaux
        var plateauDebutTour = Plateau()
        var plateauFinTour = Plateau()
        var running = true
        var turn = 0

        //Initialisation de la pioche
        initPioche()

        //Turn 0
        tourDejeu(compte, listInventories.get(0), listJoueurs.get(0).getPseudo(), listJoueurs.get(1).getPseudo(), plateauFinTour, plateauDebutTour, true)

        //Main loop
        while (running){
            turn++
            tourDejeu(compte, listInventories.get(turn%listInventories.size), listInventories.get(turn%listInventories.size).getPseudo(), nextInList(listInventories, turn%listInventories.size), plateauFinTour, plateauDebutTour)
            plateauDebutTour.copier(plateauFinTour)
        }
        compte.disconnect()
    }

    fun nextInList(list : MutableList<Inventory>, indice : Int): String{ // renvoie l'indice du prochain joueur à jouer
        var taille = list.size
        if(indice==taille){
            return list.get(0).getPseudo()
        }
        else { return list.get(indice+1).getPseudo() }
    }
}