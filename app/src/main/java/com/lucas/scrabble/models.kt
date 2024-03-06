package com.lucas.scrabble

data class Letter(
    val character: Char, // Le caractère de la lettre (A, B, C, etc.)
    val score: Int // Le score associé à la lettre
)

// Modèle de données pour représenter une case du plateau de jeu
data class Tile(
    val row: Int, // L'indice de la ligne de la case sur le plateau
    val column: Int, // L'indice de la colonne de la case sur le plateau
    val type: TileType // Le type de la case (double lettre, triple mot, etc.)
)

// Enumération pour représenter les différents types de cases du plateau de jeu
enum class TileType {
    REGULAR, // Case régulière
    DOUBLE_LETTER, // Double lettre
    TRIPLE_LETTER, // Triple lettre
    DOUBLE_WORD, // Double mot
    TRIPLE_WORD // Triple mot
}