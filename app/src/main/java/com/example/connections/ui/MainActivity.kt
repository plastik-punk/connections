package com.example.connections.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.connections.R
import com.example.connections.dao.WordConnectionDao
import com.example.connections.database.DatabaseActions
import com.example.connections.database.WordConnectionDatabase
import com.example.connections.entity.Word
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var database: WordConnectionDatabase
    lateinit var dao: WordConnectionDao

    private val wordMap: HashMap<Pair<Int, Int>, Word> = HashMap()
    private lateinit var wordTextView: TextView

    private var round = 1

    private var mistakes = 4
    private lateinit var numberTextView: TextView

    private var gameOver = false
    private var gameWon = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        numberTextView = findViewById(R.id.mistakes_counter)
        numberTextView.text = mistakes.toString()

        database = WordConnectionDatabase.getDatabase(this)
        dao = database.wordConnectionDao()

        lifecycleScope.launch {
            DatabaseActions().initializeData(database)
            mapWords()
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun selectWord(word: String, view: View) {

    }

    fun shuffle(view: View) {
        val words = ArrayList<Word>()
        wordMap.forEach { pair ->
            val word = pair.value
            words.add(word)
        }
        words.shuffle()
        for (row in 0..3) {
            for (col in 0..3) {
                val word = words.removeAt(0)
                val pair = Pair(row, col)
                wordMap[pair] = word
            }
        }
        wordsToButtons()
    }

    fun deselect(view: View) {

    }

    fun submit(view: View) {
        mistakes--
        if (mistakes <= 0) {
            mistakes = 0
            numberTextView.text = mistakes.toString()
            showGameOver()
        } else {
            numberTextView.text = mistakes.toString()
        }
    }

    private fun showGameOver() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over")
        builder.setMessage("You ran out of mistakes!")
        builder.setPositiveButton("Show Solution") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private suspend fun mapWords() {
        val words = ArrayList<Word>()
        val connections = dao.getConnectionsByRound(round).forEach { connection ->
            dao.getWordsByConnection(connection.id).forEach { word ->
                words.add(word)
            }
        }
        words.shuffle()
        for (row in 0..3) {
            for (col in 0..3) {
                val word = words.removeAt(0)
                val pair = Pair(row, col)
                wordMap[pair] = word
            }
        }
        wordsToButtons()
    }

    private fun wordsToButtons() {
        wordTextView = findViewById(R.id.row_0_word_0)
        wordTextView.text = wordMap[Pair(0, 0)].toString()
        wordTextView = findViewById(R.id.row_0_word_1)
        wordTextView.text = wordMap[Pair(0, 1)].toString()
        wordTextView = findViewById(R.id.row_0_word_2)
        wordTextView.text = wordMap[Pair(0, 2)].toString()
        wordTextView = findViewById(R.id.row_0_word_3)
        wordTextView.text = wordMap[Pair(0, 3)].toString()

        wordTextView = findViewById(R.id.row_1_word_0)
        wordTextView.text = wordMap[Pair(1, 0)].toString()
        wordTextView = findViewById(R.id.row_1_word_1)
        wordTextView.text = wordMap[Pair(1, 1)].toString()
        wordTextView = findViewById(R.id.row_1_word_2)
        wordTextView.text = wordMap[Pair(1, 2)].toString()
        wordTextView = findViewById(R.id.row_1_word_3)
        wordTextView.text = wordMap[Pair(1, 3)].toString()

        wordTextView = findViewById(R.id.row_2_word_0)
        wordTextView.text = wordMap[Pair(2, 0)].toString()
        wordTextView = findViewById(R.id.row_2_word_1)
        wordTextView.text = wordMap[Pair(2, 1)].toString()
        wordTextView = findViewById(R.id.row_2_word_2)
        wordTextView.text = wordMap[Pair(2, 2)].toString()
        wordTextView = findViewById(R.id.row_2_word_3)
        wordTextView.text = wordMap[Pair(2, 3)].toString()

        wordTextView = findViewById(R.id.row_3_word_0)
        wordTextView.text = wordMap[Pair(3, 0)].toString()
        wordTextView = findViewById(R.id.row_3_word_1)
        wordTextView.text = wordMap[Pair(3, 1)].toString()
        wordTextView = findViewById(R.id.row_3_word_2)
        wordTextView.text = wordMap[Pair(3, 2)].toString()
        wordTextView = findViewById(R.id.row_3_word_3)
        wordTextView.text = wordMap[Pair(3, 3)].toString()
    }
}

