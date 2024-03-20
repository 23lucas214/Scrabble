package com.lucas.scrabble

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
        server = "localhost:5342"
        database = "SCRABBLE"
        url = "jdbc:postgresql://"+server+"/"+database
        var driver = DriverManager.getDriver(url)
    }

    /**
     * Get connection to the database
     */
    fun connect() {
        if (connection == null) {
            connection = DriverManager.getConnection(url, loginDB, passwordDB)
        }
    }

    /**
     * Disconnect from database
     */
    fun disconnect() {
        if (connection != null) {
            //connection.close()
            connection = null
        }
    }


    fun authentification(login : String, mdp : String) : Boolean{
        connect()
        var retour = false
        val hash1 = (login + mdp).toByteArray(Charsets.UTF_8).toString()
        var request = "SELECT mdpHash FROM compte WHERE loginHash=?"
        var connect = DriverManager.getConnection(url, loginDB, passwordDB)
        var stmt = connect.prepareStatement(request)
        stmt.setString(1,login.toByteArray(Charsets.UTF_8).toString())
        var rs = stmt.executeQuery()
        rs.next()
        val hash2 = rs.getString(1);
        disconnect()
        if(hash1 == hash2){
            retour = true
        }
        request = "SELECT pseudo FROM compte WHERE loginHash=?"
        stmt = connect.prepareStatement(request)
        stmt.setString(1,login.toByteArray(Charsets.UTF_8).toString())
        rs = stmt.executeQuery()
        rs.next()
        pseudo=rs.getString(1)
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
        val logmdp = (login + mdp).toByteArray(Charsets.UTF_8).toString()
        val log = (login).toByteArray(Charsets.UTF_8).toString()
        val request = "INSERT INTO compte(pseudo,mdpHash,points,pointsPartie,loginHash,id_partie) VALUES(?,?,'0','0',?,?)"
        var connect = DriverManager.getConnection(url, loginDB, passwordDB)
        var stmt = connect.prepareStatement(request)
        stmt.setString(1,pseudo)
        stmt.setString(2,logmdp)
        stmt.setString(3,log)
        stmt.setString(4,null)
        stmt.executeUpdate()
        disconnect()
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
            request = "INSERT INTO pioche(lettre,nb_lettre,id_partie) VALUES(?,?,?)"
            stmt = connect.prepareStatement(request)
            stmt.setInt(1, id)
            stmt.executeUpdate()
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