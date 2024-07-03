package com.example.connections.ui

import android.os.Bundle
import android.util.Log
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

    private lateinit var database: WordConnectionDatabase
    private lateinit var dao: WordConnectionDao

    private val wordMap: HashMap<Pair<Int, Int>, Word> = HashMap()
    private lateinit var wordTextView: TextView

    private var selectedWords: ArrayList<Word> = ArrayList()

    private var round = 1

    private var mistakes = 4
    private lateinit var numberTextView: TextView

    private var foundConnections = 0

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

    private fun selectWord(word: Word?, view: View) {
        Log.d("MainActivity", "Selected word: $word")

        if (word == null) {
            return
        }

        if (selectedWords.contains(word)) {
            selectedWords.remove(word)
            //view.setBackgroundResource(R.drawable.button_background)
            Log.d("MainActivity", "Selected words: $selectedWords")
            return
        }

        if (selectedWords.size >= 4) {
            Log.d("MainActivity", "Selected words: $selectedWords")
            return
        }

        selectedWords.add(word)
        // view.setBackgroundResource(R.drawable.button_background_selected)

        Log.d("MainActivity", "Selected words: $selectedWords")
    }

    fun shuffle(view: View) {
        shuffleWordMap()
    }

    private fun shuffleWordMap() {
        val words = ArrayList<Word>()
        wordMap.forEach { pair ->
            val word = pair.value
            words.add(word)
        }
        wordMap.clear()
        words.shuffle()
        for (row in (foundConnections)..3) {
            for (col in 0..3) {
                if (words.isNotEmpty()) {
                    val word = words.removeFirst()
                    val pair = Pair(row, col)
                    wordMap[pair] = word
                } else {
                    Log.d("MainActivity", "No more words to place on the board.")
                }
            }
        }
        wordsToButtons()
    }

    fun deselect(view: View) {
        selectedWords.clear()
    }

    fun submit(view: View) {

        if (selectedWords.size != 4) {
            return
        }

        val connectionId = selectedWords[0].connectionId
        for (word in selectedWords) {
            if (word.connectionId != connectionId) {
                mistakes--
                if (mistakes <= 0) {
                    mistakes = 0
                    numberTextView.text = mistakes.toString()
                    showGameOver()
                } else {
                    numberTextView.text = mistakes.toString()
                }
                selectedWords.clear()
                return
            }
        }

        for (word in selectedWords) {
            wordMap.remove(wordMap.keys.find { wordMap[it] == word })
        }

        shuffleWordMap()
        wordsToButtons()

        foundConnections++

        if (foundConnections == 4) {
            showGameWon()
            foundConnections = 0
            return
        }
    }

    private fun showGameWon() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Won")
        builder.setMessage("You found all the connections!")
        builder.setPositiveButton("Play Again") { dialog, _ ->
            dialog.dismiss()
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
        dao.getConnectionsByRound(round).forEach { connection ->
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

    fun selectRow0Word0(view: View) {
        selectWord(wordMap[Pair(0, 0)], view)
    }

    fun selectRow0Word1(view: View) {
        selectWord(wordMap[Pair(0, 1)], view)
    }

    fun selectRow0Word2(view: View) {
        selectWord(wordMap[Pair(0, 2)], view)
    }

    fun selectRow0Word3(view: View) {
        selectWord(wordMap[Pair(0, 3)], view)
    }

    fun selectRow1Word0(view: View) {
        selectWord(wordMap[Pair(1, 0)], view)
    }

    fun selectRow1Word1(view: View) {
        selectWord(wordMap[Pair(1, 1)], view)
    }

    fun selectRow1Word2(view: View) {
        selectWord(wordMap[Pair(1, 2)], view)
    }

    fun selectRow1Word3(view: View) {
        selectWord(wordMap[Pair(1, 3)], view)
    }

    fun selectRow2Word0(view: View) {
        selectWord(wordMap[Pair(2, 0)], view)
    }

    fun selectRow2Word1(view: View) {
        selectWord(wordMap[Pair(2, 1)], view)
    }

    fun selectRow2Word2(view: View) {
        selectWord(wordMap[Pair(2, 2)], view)
    }

    fun selectRow2Word3(view: View) {
        selectWord(wordMap[Pair(2, 3)], view)
    }

    fun selectRow3Word0(view: View) {
        selectWord(wordMap[Pair(3, 0)], view)
    }

    fun selectRow3Word1(view: View) {
        selectWord(wordMap[Pair(3, 1)], view)
    }

    fun selectRow3Word2(view: View) {
        selectWord(wordMap[Pair(3, 2)], view)
    }

    fun selectRow3Word3(view: View) {
        selectWord(wordMap[Pair(3, 3)], view)
    }
}

