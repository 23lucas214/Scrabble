package com.lucas.scrabble

class Plateau {

    /**
     * Plateau de jeu initial
     * CD est la case sur laquelle poser le premier mot
     * 0 les cases classiques
     * 2 pour double, 3 pour triple
     * M si c'est le score du mot qui est multiplié
     * L si c'est celui de la lettre
     */
    private var plateau = arrayOf(
        arrayOf("3M", "0", "0", "2L", "0", "0", "0", "2M", "0", "0", "0", "2L", "0", "0", "3M"),
        arrayOf("0", "2M", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "2M", "0"),
        arrayOf("0", "0", "2M", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2M", "0", "0"),
        arrayOf("2L", "0", "0", "3L", "0", "0", "0", "2L", "0", "0", "0", "2M", "0", "0", "2L"),
        arrayOf("0", "0", "0", "0", "2M", "0", "0", "0", "0", "0", "2M", "0", "0", "0", "0"),
        arrayOf("0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0"),
        arrayOf("0", "0", "2L", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2L", "0", "0"),
        arrayOf("3M", "0", "0", "2L", "0", "0", "0", "CD", "0", "0", "0", "2L", "0", "0", "3M"),
        arrayOf("0", "0", "2L", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2L", "0", "0"),
        arrayOf("0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "3L", "0"),
        arrayOf("0", "0", "0", "0", "2M", "0", "0", "0", "0", "0", "2M", "0", "0", "0", "0"),
        arrayOf("2L", "0", "0", "2M", "0", "0", "0", "2L", "0", "0", "0", "2M", "0", "0", "2L"),
        arrayOf("0", "0", "2M", "0", "0", "0", "2L", "0", "2L", "0", "0", "0", "2M", "0", "0"),
        arrayOf("0", "2M", "0", "0", "0", "3L", "0", "0", "0", "3L", "0", "0", "0", "2M", "0"),
        arrayOf("3M", "0", "0", "2L", "0", "0", "0", "3M", "0", "0", "0", "2L", "0", "0", "3M"),
    )

    /**
     * Liste des cases sur lesquelles on peut ecrire
     */
    val collisionOk = listOf("0", "2L", "3L", "2M", "3M", "CD")

    fun copier(plateauACopier: Plateau){
        this.plateau = plateauACopier.plateau
    }

    /**
     * Verifier si une case est toujours disponible
     */
    fun verifCaseLibre(i: Int, j: Int) : Boolean{
        return plateau[i][j] in collisionOk
    }

    fun get(x:Int,y:Int):String{
        return plateau[x][y]
    }

    /**
     * Renvoie une liste contenant les mots ecris sur le plateau
     */
    fun listeDeMots() : MutableList<Pair<String, MutableList<Pair<Int, Int>>>>{
        var list = mutableListOf<Pair<String, MutableList<Pair<Int, Int>>>>()
        var mot : String = ""
        var coord = mutableListOf<Pair<Int, Int>>()
        for (i in 0..14){
            for (j in 0..14){
                if (verifCaseLibre(i, j)){
                    if (mot.length>1){list.add(Pair(mot, coord))}
                    mot = ""
                    coord = mutableListOf<Pair<Int, Int>>()
                }
                else{
                    mot += plateau[i][j]
                    coord.add(Pair(i, j))

                }
            }
            if (mot.length>1){list.add(Pair(mot, coord))}
        }

        mot = ""
        coord = mutableListOf<Pair<Int, Int>>()
        for (i in 0..14){
            for (j in 0..14){
                if (verifCaseLibre(j, i)){
                    if (mot.length>1){list.add(Pair(mot, coord))}
                    mot = ""
                    coord = mutableListOf<Pair<Int, Int>>()
                }
                else{
                    mot += plateau[j][i]
                    coord.add(Pair(i, j))
                }
            }
            if (mot.length>1){list.add(Pair(mot, coord))}
        }
        return list
    }

    /**
     * Renvoie la case correspondante aux coordonnées
     */
    fun getCase(coord: Pair<Int, Int>): String{
        return plateau[coord.first][coord.second]
    }

    /**
     * Change le contenu d'une case
     * En paramètres: coordonnées de la case et son nouveau contenu
     */
    fun setCase(coord: Pair<Int, Int>, case: String){
        plateau[coord.first][coord.second] = case
    }

    /**
     * Verifie que tous les mots formes sur le plateau sont acceptables
     * Attention le dictionnaire doit etre initialise
     */
    fun verifierPlateau(jeu: Jeu): Boolean{
        val mots = listeDeMots()
        var bool = true
        for (x in mots){
            if (!jeu.dictionnaire.contains(x.first)){
                bool = false
            }
        }
        return bool
    }

    /**
     * Fonction qui renvoie la liste des mots ajoutes durant un tour
     */
    fun motsAjoutes(plateauDebutTour: Plateau): MutableList<Pair<String, MutableList<Pair<Int, Int>>>>{
        var list = listeDeMots()
        list.removeAll(plateauDebutTour.listeDeMots())
        return list
    }

    fun verifierAjoutPlateau(plateauDebutTour: Plateau, jeu: Jeu, firstTurn: Boolean): Boolean{
        var mots = motsAjoutes(plateauDebutTour)
        if (mots.size != 1){return false}
        if (!jeu.dictionnaire.contains(mots.get(0).first)){return false}
        if (firstTurn){}
        return true
    }
}