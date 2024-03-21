package com.lucas.scrabble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        var textView : TextView
        var jeu=Jeu()
        btnLogin.setOnClickListener{
            setContentView(R.layout.c_connexion)
            val btnOk = findViewById<Button>(R.id.btnOk)
            btnOk.setOnClickListener {
                val etLogin = findViewById<EditText>(R.id.etUsername1)
                val login = etLogin.text.toString()
                val etmdp = findViewById<EditText>(R.id.etPassword1)
                val mdp = etmdp.text.toString()
                if ((login=="admin")&&(mdp=="admin")) {
                    setContentView(R.layout.d_accueil)
                    val btnJoinGame = findViewById<Button>(R.id.btnJoinGame)
                    val btnNewGame = findViewById<Button>(R.id.btnNewGame)
                    btnNewGame.setOnClickListener {
                        setContentView(R.layout.e_nouvelle_partie)
                        val etGameIdentifier = findViewById<EditText>(R.id.etGameIdentifier)
                        var idpa = etGameIdentifier.text.toString()
                        val etNbJoueurs = findViewById<EditText>(R.id.etNombreJoueurs)
                        val btnConfirmNewGame = findViewById<Button>(R.id.btnConfirmNewGame)
                        btnConfirmNewGame.setOnClickListener {
                            while(idpa==""){
                                idpa = etGameIdentifier.text.toString()
                                Thread.sleep(500)
                            }
                            var idpartie = idpa.toInt()
                            setContentView(R.layout.i_chargement)
                            val intent = Intent(this, PlateauDeJeuActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    btnJoinGame.setOnClickListener {
                        setContentView(R.layout.f_rejoindre)
                        val btnJoinPublicGame = findViewById<Button>(R.id.btnJoinPublicGame)
                        val etGameId = findViewById<EditText>(R.id.etGameId)
                        var idpa = etGameId.text.toString()
                        while(idpa==""){
                            idpa = etGameId.text.toString()
                            Thread.sleep(500)
                        }
                        var idpartie = idpa.toInt()
                        btnJoinPublicGame.setOnClickListener {
                            setContentView(R.layout.i_chargement)
                        }
                    }
                }
            }
        }
        btnRegister.setOnClickListener {
        }
    }

}