package com.example.myquiz

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myquiz.databinding.ActivityQuizBinding
import com.example.myquiz.databinding.ScoreDialogueBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time : String=""
    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0;
    var selectedAnswer = ""
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)


        }
        loaQuestion()
        startTimer()

    }
    private fun startTimer(){
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object  : CountDownTimer(totalTimeInMillis, 1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished/1000
                val minutes = seconds/60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }

        }.start()
    }

    private fun loaQuestion(){
        selectedAnswer = ""
        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex+1}/ ${questionModelList.size}"
            questionProgressIndicator.progress =
                ( currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100 ).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]

        }
    }

    override fun onClick(view : View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.grey))
            btn1.setBackgroundColor(getColor(R.color.grey))
            btn2.setBackgroundColor(getColor(R.color.grey))
            btn3.setBackgroundColor(getColor(R.color.grey))

        }

        val clickedBtn =  view as Button
        if (clickedBtn.id == R.id.next_btn){
            //next button is clicked
            if (selectedAnswer.isEmpty()){
                Toast.makeText(applicationContext, "Please select answer to continue", Toast.LENGTH_SHORT).show()
                return;
            }
            if (selectedAnswer == questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Score of Quiz", score.toString())
            }
            currentQuestionIndex++
            loaQuestion()
        }else{
            //option is clicked
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }
    private fun finishQuiz(){
        val totalQuestion = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestion.toFloat() ) *100).toInt()

        val dialogueBinding = ScoreDialogueBinding.inflate((layoutInflater))
        dialogueBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage>60){
                scoreTitle.text= "Congrats! You have Passed!"
                scoreTitle.setTextColor(Color.BLUE)
            }else{
                scoreTitle.text= "Oppss! You have Failed!"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestion are correct"
            finishBtn.setOnClickListener{
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogueBinding.root)
            .setCancelable(false)
            .show()

    }
}