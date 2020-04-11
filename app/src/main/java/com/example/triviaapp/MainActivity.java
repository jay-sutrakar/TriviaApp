package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp.data.AnswerListAsyncResponse;
import com.example.triviaapp.data.QuestionBank;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionText;
    private TextView questionCounterTextView;
    private int currentQuestionIndex=0;
    private List<Question> questionList;
    private int score=0;
    Vibrator vibrator;
    private TextView scoreView;
    private Prefs prefs;
    private TextView highScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator=(Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);
        trueButton=findViewById(R.id.trueButtonid);
        falseButton=findViewById(R.id.falseButtonid);
        nextButton=findViewById(R.id.nextButton);
        prevButton=findViewById(R.id.prevButton);
        questionText=findViewById(R.id.question);
        questionCounterTextView=findViewById(R.id.counter);
        scoreView=findViewById(R.id.scoreView);
        highScore=findViewById(R.id.bestScore);
        cardView=findViewById(R.id.cardView);
        cardView.setClickable(false);
        cardView.setEnabled(false);
        questionText.setClickable(false);
        //questionText.setEnabled(false);

        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        questionText.setOnClickListener(this);
        questionCounterTextView.setOnClickListener(this);

        prefs=new Prefs(MainActivity.this);
        highScore.setText("Best Score: "+prefs.getHighScore());
        Log.d("High Score ", "onCreate: "+prefs.getHighScore());
        questionList=new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
                public void processFinished(ArrayList<Question> questionArrayList) {
                questionText.setText(questionArrayList.get(currentQuestionIndex).getAnswer() );
                questionCounterTextView.setText(currentQuestionIndex+1+ " / "+questionArrayList.size());
                Log.d( "insdide :", "processFinished: "+ questionArrayList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.trueButtonid:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.nextButton:
                currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
                questionCounterTextView.setText(currentQuestionIndex+1 +" / "+questionList.size());
                updateQuestion();
                break;
            case R.id.falseButtonid:
                 checkAnswer(false);
                 updateQuestion();
                break;

            case R.id.prevButton:
                currentQuestionIndex=(currentQuestionIndex-1);
                if(currentQuestionIndex<0)
                    currentQuestionIndex=questionList.size()-1;
                updateQuestion();
                questionCounterTextView.setText(currentQuestionIndex+1 +" / "+questionList.size());

                break;
        }
    }

    private void checkAnswer(boolean userAnswer) {
        boolean correctAnswer=questionList.get(currentQuestionIndex).isAnswerTrue();
        if(userAnswer == correctAnswer) {
            fadeAnimation();
            increaseScore();
            Toast.makeText(MainActivity.this,"Correct",Toast.LENGTH_SHORT).show();
            currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
            questionCounterTextView.setText(currentQuestionIndex+1 +" / "+questionList.size());
            updateQuestion();
        } else {
            shakeAnimation();
            decreaseScore();
            vibrator.vibrate(300);
            Toast.makeText(MainActivity.this,"Wrong",Toast.LENGTH_SHORT).show();
       }
    }

    public void increaseScore(){
        score+=100;
        scoreView.setText("Score :"+score);
    }
    public void decreaseScore(){
        if(score!=0) {
            score -= 100;
            scoreView.setText("Score :"+score);
        }
    }
    private void updateQuestion() {
        questionText.setText(questionList.get(currentQuestionIndex).getAnswer());
    }
    private void fadeAnimation(){
        final CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void shakeAnimation(){
        Animation shake= AnimationUtils.loadAnimation(this,R.anim.shake);
        final CardView cardView=findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score);
        super.onPause();

    }
}
