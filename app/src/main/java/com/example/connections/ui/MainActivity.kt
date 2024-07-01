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
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var database: WordConnectionDatabase
    lateinit var dao: WordConnectionDao

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
}

