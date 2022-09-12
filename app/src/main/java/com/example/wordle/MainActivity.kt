package com.example.wordle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var wordToGuess = FourLetterWordList.getRandomFourLetterWord()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println(wordToGuess)

        val userInput = findViewById<EditText>(R.id.userInput)
        val submitBtn = findViewById<Button>(R.id.submitBtn)

        userInput.addTextChangedListener(object :
        TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                submitBtn.isEnabled = (p0 != null) && (p0.length == 4)
            }
            override fun afterTextChanged(p0: Editable?) {
            }

        })

        submitBtn.setOnClickListener {
            val word = userInput.text.toString().uppercase()
            println(word)
            if (word == wordToGuess){
                Toast.makeText(it.context, "YEET", Toast.LENGTH_SHORT).show()
                userInput.text = null
            }
            else {
                userInput.text = null
            }
        }
    }
    private fun checkGuess(guess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "o"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "x"
            }
        }
        return result
    }
}