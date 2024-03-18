package com.lucas.scrabble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_home)
        val btnLogin = findViewById<Button>(R.id.btnLogin);
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val compte = Compte()
        compte.initProperties()
        compte.connect()

        btnLogin.setOnClickListener{
            setContentView(R.layout.c_connexion)
            val btnOk = findViewById<Button>(R.id.btnOk)
            btnOk.setOnClickListener {
                val etLogin = findViewById<EditText>(R.id.etUsername1)
                val login = etLogin.text.toString()
                val etmdp = findViewById<EditText>(R.id.etPassword1)
                val mdp = etmdp.text.toString()
                if (compte.authentification(login,mdp)) {
                    setContentView(R.layout.d_accueil)
                    val btnJoinGame = findViewById<Button>(R.id.btnJoinGame)
                    val btnNewGame = findViewById<Button>(R.id.btnNewGame)
                    btnNewGame.setOnClickListener {
                        setContentView(R.layout.e_nouvelle_partie)
                        val etGameIdentifier = findViewById<EditText>(R.id.etGameIdentifier)
                        val idpartie = etGameIdentifier.text.toString().toInt()
                        val etNbJoueurs = findViewById<EditText>(R.id.etNombreJoueurs)
                        val nbjoueurs = etNbJoueurs.toString().toInt()
                        val btnConfirmNewGame = findViewById<Button>(R.id.btnConfirmNewGame)
                        btnConfirmNewGame.setOnClickListener {
                            setContentView(R.layout.i_chargement)
                            var request = ""
                            var stmt : PreparedStatement
                            var rs : ResultSet
                            var connect = DriverManager.getConnection(compte.url, compte.loginDB, compte.passwordDB)
                            do {
                                var nbJoueurs = 0 //Nombre de joueurs connectés
                                Thread.sleep(3000) // on attend 3 secondes
                                request = "SELECT pseudo FROM compte WHERE id_partie=?"
                                stmt = connect.prepareStatement(request)
                                stmt.setInt(1,idpartie)
                                rs = stmt.executeQuery()
                                rs.next()
                                for(i in 1..jeu.getRowCount(rs)) { //pas connu, clément cherche
                                    nbJoueurs ++
                                    rs.getString(i) //à mettre dans le textView
                                }
                            }while(nbjoueurs==nbJoueurs)
                            var jeu=Jeu()
                            jeu.jeu(nbjoueurs,idpartie)//nombre de joueurs, et identifiant de la partie
                            //Appel à plateauDeJeuActivité, et déroulement d'une partie
                        }
                    }
                    btnJoinGame.setOnClickListener {
                        setContentView(R.layout.f_rejoindre)
                        val btnJoinPublicGame = findViewById<Button>(R.id.btnJoinPublicGame)
                        val etGameId = findViewById<EditText>(R.id.etGameId)
                        val idpartie = etGameId.text.toString().toInt()
                        btnJoinPublicGame.setOnClickListener {
                            setContentView(R.layout.i_chargement)
                            jeu.rejoindre(compte.pseudo, idpartie)
                            //Appel à plateauDeJeuActivité, et déroulement d'une partie
                        }
                    }
                }
            }
        }
        btnRegister.setOnClickListener {
            setContentView(R.layout.b_inscription)
            val btnConfirm = findViewById<Button>(R.id.btnConfirm)
            val etLogin = findViewById<EditText>(R.id.etLogin)
            val loginText = etLogin.text.toString()
            val etPassword = findViewById<EditText>(R.id.etPassword)
            val passwordText = etPassword.text.toString()
            val etUsername = findViewById<EditText>(R.id.etUsername)
            val usernameText = etUsername.text.toString()
            compte.createAccount(loginText, passwordText, usernameText)
            btnConfirm.setOnClickListener {
                setContentView(R.layout.d_accueil)
                val btnJoinGame = findViewById<Button>(R.id.btnJoinGame)
                val btnNewGame = findViewById<Button>(R.id.btnNewGame)
                btnNewGame.setOnClickListener {
                    setContentView(R.layout.e_nouvelle_partie)
                    val etGameIdentifier = findViewById<EditText>(R.id.etGameIdentifier)
                    val idpartie = etGameIdentifier.text.toString().toInt()
                    val etNbJoueurs = findViewById<EditText>(R.id.etNombreJoueurs)
                    val nbjoueurs = etNbJoueurs.toString().toInt()
                    val btnConfirmNewGame = findViewById<Button>(R.id.btnConfirmNewGame)
                    btnConfirmNewGame.setOnClickListener {
                        setContentView(R.layout.i_chargement)
                        var request = ""
                        var stmt : PreparedStatement
                        var rs : ResultSet
                        var connect = DriverManager.getConnection(compte.url, compte.loginDB, compte.passwordDB)
                        do {
                            var nbJoueurs = 0 //Nombre de joueurs connectés
                            Thread.sleep(3000) // on attend 3 secondes
                            request = "SELECT pseudo FROM compte WHERE id_partie=?"
                            stmt = connect.prepareStatement(request)
                            stmt.setInt(1,idpartie)
                            rs = stmt.executeQuery()
                            rs.next()
                            for(i in 1..jeu.getRowCount(rs)) { //pas connu, clément cherche
                                nbJoueurs ++
                                rs.getString(i) //à mettre dans le textView
                            }
                        }while(nbjoueurs==nbJoueurs)
                        var jeu=Jeu()
                        jeu.jeu(nbjoueurs,idpartie)//nombre de joueurs, et identifiant de la partie
                        //Appel à plateauDeJeuActivité, et déroulement d'une partie
                    }
                }
                btnJoinGame.setOnClickListener {
                    setContentView(R.layout.f_rejoindre)
                    val btnJoinPublicGame = findViewById<Button>(R.id.btnJoinPublicGame)
                    val etGameId = findViewById<EditText>(R.id.etGameId)
                    val idpartie = etGameId.text.toString().toInt()
                    btnJoinPublicGame.setOnClickListener {
                        setContentView(R.layout.i_chargement)
                        jeu.rejoindre(compte.pseudo, idpartie)
                        //Appel à plateauDeJeuActivité, et déroulement d'une partie
                    }
                }
            }
        }
    }

}