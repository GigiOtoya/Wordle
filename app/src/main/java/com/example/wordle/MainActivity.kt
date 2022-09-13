package com.example.wordle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*

class MainActivity : AppCompatActivity() {
    var wordToGuess = FourLetterWordList.getRandomFourLetterWord()
    var tries = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println(wordToGuess)

        val userInput = findViewById<EditText>(R.id.userInput)
        val submitBtn = findViewById<Button>(R.id.submitBtn)
        val tvIDs = arrayOf(
            arrayOf<Int>(R.id.r0c0,R.id.r0c1,R.id.r0c2,R.id.r0c3),
            arrayOf<Int>(R.id.r1c0,R.id.r1c1,R.id.r1c2,R.id.r1c3),
            arrayOf<Int>(R.id.r2c0,R.id.r2c1,R.id.r2c2,R.id.r2c3)
        )

        userInput.addTextChangedListener(object : TextWatcher {
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
            insertWord(word, tvIDs)
            if (word == wordToGuess){
                Toast.makeText(it.context, "Success", Toast.LENGTH_SHORT).show()
            }
            tries++
            println(tries)
            userInput.text = null
            if (tries == 3) {
                userInput.isEnabled = false
            }
        }
    }

    private fun insertWord(word: String, ids: Array<Array<Int>>) {
        for (i in 0..3){
            val tv = findViewById<TextView>(ids[tries][i])
            tv.text = word[i].toString()
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