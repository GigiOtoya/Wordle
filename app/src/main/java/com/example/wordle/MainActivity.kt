package com.example.wordle


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.core.view.isVisible
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var wordToGuess = FourLetterWordList.getRandomFourLetterWord()
    private var wordList = FourLetterWordList.getAllFourLetterWords().map { it.uppercase() }
    private var tries = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // For Debug
        println(wordToGuess)

        // Set tootip message and colors
        val teal = ContextCompat.getColor(this, R.color.Teal_A400)
        val yellow = ContextCompat.getColor(this, R.color.Lime_A200)
        val tipsText = SpannableStringBuilder()
            .color(teal, { append("Teal: ") })
            .append("letter in correct spot\n")
            .color(yellow, { append("Yellow: ")})
            .append("letter in wrong spot")
        findViewById<TextView>(R.id.tips).setText(tipsText)

        val userInput = findViewById<EditText>(R.id.userInput)
        val submitBtn = findViewById<Button>(R.id.submitBtn)
        val resetBtn = findViewById<Button>(R.id.resetBtn)
        val tryAgainMsg = findViewById<TextView>(R.id.tryAgainMsg)
        val tvIDs = arrayOf(
            arrayOf(R.id.r0c0,R.id.r0c1,R.id.r0c2,R.id.r0c3),
            arrayOf(R.id.r1c0,R.id.r1c1,R.id.r1c2,R.id.r1c3),
            arrayOf(R.id.r2c0,R.id.r2c1,R.id.r2c2,R.id.r2c3)
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
        // Submit Button
        submitBtn.setOnClickListener {
            closeKeyboard()
            val word = userInput.text.toString().uppercase()
            println(word)
            if (checkValid(word)) {
                insertWord(word, tvIDs)

                // On Correct Guess
                if (checkGuess(word)) {
                    userInput.isEnabled = false
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                    val party = Party(
                        speed = 0f,
                        maxSpeed = 30f, damping = 0.9f,
                        spread = 360,
                        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                        position = Position.Relative(0.5, 0.3)
                    )
                    val konfettiView = findViewById<KonfettiView>(R.id.konfettiView)
                    konfettiView.start(party)
                }
                tries++
                if (tries == 3) {
                    userInput.isEnabled = false
                    tryAgainMsg.isVisible = true
                }
            }
            else {
                Toast.makeText(it.context, "Not in Word List", Toast.LENGTH_SHORT).show()
            }
            userInput.text = null

        }

        // Reset Button
        resetBtn.setOnClickListener {
            val mIntent = intent
            finish()
            startActivity(mIntent)
//            tries = 0
//            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
//            userInput.isEnabled = true
//            for (i in 0..2){
//                for (j in 0..3){
//                    val tv = findViewById<TextView>(tvIDs[i][j])
//                    tv.setTextColor(ContextCompat.getColor(this, R.color.white))
//                    tv.text = ""
//                }
//            }
        }
    }
    // check if word is in list
    private fun checkValid(guess: String) :Boolean {
        return guess in wordList
    }
    // insert guess into table
    private fun insertWord(word: String, ids: Array<Array<Int>>) {
        // frequency map for letters in the word to guess
        val countMap = HashMap<Char, Int>()
        for (ch in wordToGuess) {
            if (countMap.containsKey(ch))
                countMap[ch] = countMap.getValue(ch)+1
            else
                countMap[ch] = 1
        }
        //println(countMap)
        for (i in 0..3) {
            val tv = findViewById<TextView>(ids[tries][i])
            tv.text = word[i].toString()
            if (word[i] == wordToGuess[i]) {
                tv.setTextColor(ContextCompat.getColor(this, R.color.Teal_A400))
                countMap[word[i]] = countMap.getValue(word[i])-1
            } else if ( (word[i] in wordToGuess) && (countMap.getValue(word[i]) > 0) ) {
                tv.setTextColor(ContextCompat.getColor(this, R.color.Lime_A200))
                countMap[word[i]] = countMap.getValue(word[i])-1
                //println(countMap)
            }
        }
    }
    // check if guess matches the word
    private fun checkGuess(guess: String) : Boolean {
        return guess == wordToGuess
    }
    // Close Keyboard
    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}