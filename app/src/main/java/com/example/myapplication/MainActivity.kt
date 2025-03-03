package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var wordToGuess = FourLetterWordList.getRandomFourLetterWord()
    private var attempts = 0
    private val maxAttempts = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val guessInput = findViewById<EditText>(R.id.wordToGuess)
        val resultText = findViewById<TextView>(R.id.resultText)
        val answerText = findViewById<TextView>(R.id.answerText)

        submitButton.setOnClickListener {
            val guess = guessInput.text.toString().uppercase()

            if (guess.length != 4) {
                Toast.makeText(this, "Please enter a 4-letter word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val feedback = checkGuess(guess)
            attempts++

            resultText.append("\nAttempt $attempts: $guess ➝ $feedback")

            if (guess == wordToGuess || attempts >= maxAttempts) {
                submitButton.visibility = View.GONE
                resetButton.visibility = View.VISIBLE
                answerText.visibility = View.VISIBLE
                answerText.text = "Correct word: $wordToGuess"
            }

            guessInput.text.clear()
            hideKeyboard()
        }

        resetButton.setOnClickListener {
            resetGame()
        }
    }

    private fun checkGuess(guess: String): String {
        var result = ""
        for (i in guess.indices) {
            result += when {
                guess[i] == wordToGuess[i] -> "O"
                guess[i] in wordToGuess -> "+"
                else -> "X"
            }
        }
        return result
    }

    private fun resetGame() {
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        attempts = 0

        findViewById<TextView>(R.id.resultText).text = ""
        findViewById<TextView>(R.id.answerText).visibility = View.GONE
        findViewById<Button>(R.id.submitButton).visibility = View.VISIBLE
        findViewById<Button>(R.id.resetButton).visibility = View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}