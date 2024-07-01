package com.example.connections

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

class MainActivity : AppCompatActivity() {

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

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true) val wordId: Int,
    var value: String,
    var groupId: Int
)

@Entity
data class Group(
    @PrimaryKey(autoGenerate = true) val groupId: Int,
    var round: Int,
    var info: String,
    var difficulty: Int
)

@Dao
interface WordGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Query("SELECT * FROM 'Group' WHERE round = :round")
    suspend fun getGroupsByRound(round: Int): List<Group>

    @Query("SELECT * FROM 'Word' WHERE groupId = :groupId")
    suspend fun getWordsByGroup(groupId: Int): List<Word>
}

@Database(entities = [Group::class, Word::class], version = 1)
abstract class WordGroupDatabase : RoomDatabase() {
    abstract fun wordGroupDao(): WordGroupDao
}