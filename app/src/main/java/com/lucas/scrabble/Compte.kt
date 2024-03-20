package com.lucas.scrabble

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

class Compte() {
    // Get Properties file
    //val rootPath = Thread.currentThread().getContextClassLoader().getResource("").path
    var properties = Properties()


    var loginDB = ""
    var passwordDB = ""
    var server = ""
    var database = ""
    var pseudo = ""
    var url = ""
    var connection : Connection? = null

    fun initProperties(){
        GlobalScope.launch(Dispatchers.IO) {
            /*
        properties.load(FileInputStream(rootPath + "properties"))
        val loginDB = properties.getProperty("loginDB")
        val passwordDB = properties.getProperty("passwordDB")
        val server = properties.getProperty("server")
        val database = properties.getProperty("database")
        val url = "jdbc:postgresql://$server/$database"
        var driver = DriverManager.getDriver(url)
        */
            loginDB = "brichlkc"
            passwordDB = "mdpsupersecurise"
            server = "dr-ser-info.ec-nantes.fr:5432"
            database = "SCRABBLE"
            url = "jdbc:postgresql://dr-ser-info.ec-nantes.fr:5432/SCRABBLE"
            var driver = DriverManager.getDriver(url)
            connection = DriverManager.getConnection(url, loginDB, passwordDB)
        }
        }

    /**
     * Get connection to the database
     */
    fun connect() {
        GlobalScope.launch(Dispatchers.IO) {
            if (connection == null) {
                connection = DriverManager.getConnection(url, loginDB, passwordDB)
                println("reussi")
            }
        }
    }

    /**
     * Disconnect from database
     */
    fun disconnect() {
        GlobalScope.launch(Dispatchers.IO) {
            if (connection != null) {
                //connection.close()
                connection = null
            }
        }
    }


    fun authentification(login : String, mdp : String) : Boolean{
        connect()
        var retour = false
        if((login=="admin")&&(mdp=="admin")) {
            retour = true
        }
        else {
            GlobalScope.launch(Dispatchers.IO) {
                val hash1 = (login + mdp).toByteArray(Charsets.UTF_8).toString()
                println(hash1)
                var request = "SELECT mdpHash FROM compte WHERE loginHash=?"
                var connect = DriverManager.getConnection(url, loginDB, passwordDB)
                var stmt = connect.prepareStatement(request)
                stmt.setString(1, login.toByteArray(Charsets.UTF_8).toString())
                var rs = stmt.executeQuery()
                if (rs.next()) {
                    val hash2 = rs.getString(1);
                    if (hash1 == hash2) {
                        retour = true
                    }
                }
                request = "SELECT pseudo FROM compte WHERE loginHash=?"
                stmt = connect.prepareStatement(request)
                stmt.setString(1, login.toByteArray(Charsets.UTF_8).toString())
                rs = stmt.executeQuery()
                if (rs.next()) {
                    pseudo = rs.getString(1)
                }
                disconnect()
            }
        }
        return retour
    }

    fun editPassword(login : String, newMdp : String){ //que post authentification
        connect()
        val request = "UPDATE compte SET mdpHash=? WHERE loginHash=?"
        var connect = DriverManager.getConnection(url, loginDB, passwordDB)
        var stmt = connect.prepareStatement(request)
        stmt.setString(1,(login + newMdp).toByteArray(Charsets.UTF_8).toString())
        stmt.setString(2,login.toByteArray(Charsets.UTF_8).toString())
        val rs = stmt.executeUpdate()
        disconnect()
    }

    fun createAccount(login : String, mdp : String, pseudo : String){
        connect()
        GlobalScope.launch(Dispatchers.IO) {
            val logmdp = (login + mdp).toByteArray(Charsets.UTF_8).toString()
            val log = (login).toByteArray(Charsets.UTF_8).toString()
            val request =
                "INSERT INTO compte(pseudo,mdpHash,points,pointsPartie,loginHash,id_partie) VALUES(?,?,'0','0',?,?)"
            var connect = DriverManager.getConnection(url, loginDB, passwordDB)
            var stmt = connect.prepareStatement(request)
            stmt.setString(1, pseudo)
            stmt.setString(2, logmdp)
            stmt.setString(3, log)
            stmt.setInt(4, 1) // a autoincrémenter
            stmt.executeUpdate()
        }
        disconnect()
    }

    fun nbLettres(pioche : MutableList<String>, lettre : String): Int{
        var s = 0
        for(lettre in pioche)
            s = when {
                (lettre == "A") -> 9
                (lettre == "B") -> 2
                (lettre == "C") -> 2
                (lettre == "D") -> 3
                (lettre == "E") -> 15
                (lettre == "F") -> 2
                (lettre == "G") -> 2
                (lettre == "H") -> 2
                (lettre == "I") -> 8
                (lettre == "J") -> 1
                (lettre == "K") -> 1
                (lettre == "L") -> 5
                (lettre == "M") -> 3
                (lettre == "N") -> 6
                (lettre == "O") -> 6
                (lettre == "P") -> 2
                (lettre == "Q") -> 1
                (lettre == "R") -> 6
                (lettre == "S") -> 6
                (lettre == "T") -> 6
                (lettre == "U") -> 6
                (lettre == "V") -> 1
                (lettre == "W") -> 1
                (lettre == "X") -> 1
                (lettre == "Y") -> 1
                (lettre == "Z") -> 1
                else -> 1
            }
        return s
    }

    // Nouvelle Partie

    fun createNewGame(id : Int, pioche : MutableList<String>, compte: Compte) {
        var connect = DriverManager.getConnection(url, loginDB, passwordDB)
        connect()
        var request = "INSERT INTO partie(id_partie, tour) VALUES(?,?)"
        var stmt = connect.prepareStatement(request)
        stmt.setInt(1, id)
        stmt.setString(2, compte.pseudo) //par défaut créateur joue en premier, mais modifié après
        stmt.executeUpdate()
        for (lettre in pioche) {
            request = "INSERT INTO pioche(lettre,nb_lettre) VALUES(?,?) WHERE id_partie=?"
            stmt = connect.prepareStatement(request)
            stmt.setString(1, lettre)
            stmt.setInt(2, nbLettres(pioche, lettre))
            stmt.setInt(3, id)
            stmt.executeUpdate()
        }
        for (i in 0..14) {
            for(j in 0..14) {
                request = "INSERT INTO piece(lettre,X,Y,pseudo) VALUES(?,?,?,?) WHERE id_partie=?"
                stmt = connect.prepareStatement(request)
                stmt.setString(1, "rien")
                stmt.setInt(2,i)
                stmt.setInt(3,j)
                stmt.setString(4,"noone")
                stmt.setInt(5, id)
                stmt.executeUpdate()
            }
        }
        disconnect()
    }

//Rejoindre Partie

    fun joinGame(id : Int, pseudo : String) : Int{
        connect()
        var connect = DriverManager.getConnection(url, loginDB, passwordDB)
        var request = "SELECT id_partie FROM partie WHERE id_partie=?"
        var stmt = connect.prepareStatement(request)
        stmt.setInt(1, id)
        var rs = stmt.executeQuery()
        rs.next()
        if (rs.getString(1) != null) {
            request = "UPDATE compte SET id_partie=? WHERE pseudo=?"
            stmt = connect.prepareStatement(request)
            stmt.setInt(1, id)
            stmt.setString(2, pseudo)
            stmt.executeUpdate()
            disconnect()
            return 0
        }
        return 1
        disconnect()
    }

//Fin de Partie

    fun endGame(id : Int){
        connect()
        var connect = DriverManager.getConnection(url, loginDB, passwordDB)
        var request = "DELETE FROM partie WHERE id_partie=?"
        var stmt = connect.prepareStatement(request)
        stmt.setInt(1, id)
        stmt.executeUpdate()
        request = "DELETE FROM pioche WHERE id_partie=?"
        stmt = connect.prepareStatement(request)
        stmt.setInt(1, id)
        stmt.executeUpdate()
        request = "DELETE FROM piece WHERE id_partie=?"
        stmt = connect.prepareStatement(request)
        stmt.setInt(1, id)
        stmt.executeUpdate()
        request = "UPDATE compte SET id_partie=NULL WHERE id_partie=?"
        stmt = connect.prepareStatement(request)
        stmt.setInt(1, id)
        stmt.executeUpdate()
        disconnect()
    }
}