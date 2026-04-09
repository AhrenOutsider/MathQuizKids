package com.example.mathforkids

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mathforkids.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var score = 0
    private var questionNumber = 0
    private val maxQuestions = 10
    private lateinit var currentQuestion: QuizQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener { checkAnswer() }
        binding.nextButton.setOnClickListener { loadNextQuestion() }
        binding.playAgainButton.setOnClickListener { startGame() }

        startGame()
    }

    private fun startGame() {
        score = 0
        questionNumber = 0
        binding.resultText.text = getString(R.string.result_placeholder)
        binding.resultText.setTextColor(getColorCompat(R.color.deep_blue))
        binding.scoreText.text = getString(R.string.score_text, score)
        binding.answerInput.text?.clear()
        binding.answerInputLayout.isErrorEnabled = false
        binding.answerInputLayout.error = null
        binding.gameCard.visibility = android.view.View.VISIBLE
        binding.summaryCard.visibility = android.view.View.GONE
        binding.nextButton.isEnabled = false
        loadNextQuestion()
    }

    private fun loadNextQuestion() {
        if (questionNumber >= maxQuestions) {
            showSummary()
            return
        }

        questionNumber += 1
        currentQuestion = generateQuestion()
        binding.questionCountText.text = getString(R.string.question_counter, questionNumber, maxQuestions)
        binding.questionText.text = currentQuestion.text
        binding.answerInput.text?.clear()
        binding.answerInputLayout.isErrorEnabled = false
        binding.answerInputLayout.error = null
        binding.resultText.text = getString(R.string.result_placeholder)
        binding.resultText.setTextColor(getColorCompat(R.color.deep_blue))
        binding.submitButton.isEnabled = true
        binding.nextButton.isEnabled = false
    }

    private fun checkAnswer() {
        val answerText = binding.answerInput.text?.toString()?.trim().orEmpty()
        if (answerText.isEmpty()) {
            binding.answerInputLayout.error = getString(R.string.enter_answer_error)
            return
        }

        val userAnswer = answerText.toIntOrNull()
        if (userAnswer == null) {
            binding.answerInputLayout.error = getString(R.string.number_only_error)
            return
        }

        binding.answerInputLayout.error = null
        val isCorrect = userAnswer == currentQuestion.answer

        if (isCorrect) {
            score += 10
            binding.resultText.text = getString(R.string.correct_message)
            binding.resultText.setTextColor(getColorCompat(R.color.success_green))
        } else {
            binding.resultText.text = getString(R.string.wrong_message, currentQuestion.answer)
            binding.resultText.setTextColor(Color.parseColor("#C62828"))
        }

        binding.scoreText.text = getString(R.string.score_text, score)
        binding.submitButton.isEnabled = false
        binding.nextButton.isEnabled = true
    }

    private fun showSummary() {
        binding.gameCard.visibility = android.view.View.GONE
        binding.summaryCard.visibility = android.view.View.VISIBLE
        binding.finalScoreText.text = getString(R.string.final_score_text, score)
        binding.starMessageText.text = when {
            score >= 90 -> getString(R.string.summary_amazing)
            score >= 60 -> getString(R.string.summary_great)
            else -> getString(R.string.summary_keep_practicing)
        }
    }

    private fun generateQuestion(): QuizQuestion {
        return when (Random.nextInt(3)) {
            0 -> additionQuestion()
            1 -> subtractionQuestion()
            else -> multiplicationQuestion()
        }
    }

    private fun additionQuestion(): QuizQuestion {
        val first = Random.nextInt(1, 11)
        val second = Random.nextInt(1, 11)
        return QuizQuestion("$first + $second = ?", first + second)
    }

    private fun subtractionQuestion(): QuizQuestion {
        val first = Random.nextInt(5, 16)
        val second = Random.nextInt(1, first + 1)
        return QuizQuestion("$first - $second = ?", first - second)
    }

    private fun multiplicationQuestion(): QuizQuestion {
        val first = Random.nextInt(1, 6)
        val second = Random.nextInt(1, 6)
        return QuizQuestion("$first x $second = ?", first * second)
    }

    private fun getColorCompat(colorRes: Int): Int {
        return androidx.core.content.ContextCompat.getColor(this, colorRes)
    }
}

data class QuizQuestion(
    val text: String,
    val answer: Int
)

