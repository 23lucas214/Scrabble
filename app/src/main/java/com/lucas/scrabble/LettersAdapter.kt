package com.lucas.scrabble

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LettersAdapter(private val letters: List<Letter>) :
    RecyclerView.Adapter<LettersAdapter.LetterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.drawn_letter_layout, parent, false)
        return LetterViewHolder(view)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val letter = letters[position]
        holder.bind(letter)
    }

    override fun getItemCount(): Int {
        return letters.size
    }

    inner class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val letterTextView: TextView = itemView.findViewById(R.id.letterTextView)

        fun bind(letter: Letter) {
            letterTextView.text = letter.character.toString()
        }
    }
}