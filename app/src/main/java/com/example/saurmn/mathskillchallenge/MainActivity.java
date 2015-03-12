package com.example.saurmn.mathskillchallenge;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private List<String> multiplicationQList;
    private List<String> divisionQList;
    private List<String> additionQList;
    private List<String> subtractionQList;
    private List<String> quizQuestionsList;
    private String problemTypeSelected;
    private String correctAnswer;
    private int totalGuesses;
    private int correctAnswers;
    private int guessRows;
    private Random random;
    private Handler handler;
    private Animation shakeAnimation;

    private TextView answerTextView;
    private TextView questionNumberTextView;
    private TextView questionDisplayTextView;
    private TableLayout buttonTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        multiplicationQList = new ArrayList<String>();//multiplication question
        divisionQList= new ArrayList<String>();//division questions
        additionQList= new ArrayList<String>();//addition questions
        subtractionQList= new ArrayList<String>();//subtraction questions
        quizQuestionsList = new ArrayList<String>();//questions for the quiz
        problemTypeSelected = "All";// default to all problems being selected
        guessRows=1;// default to one row choices
        random = new Random();// initialize the random number generator
        handler = new Handler();// used to perform delayed operations

        // load the shake animation that's used for incorrect answers
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);

        shakeAnimation.setRepeatCount(3);

        //get array of problem types
        String[] problemTypes = getResources().getStringArray(R.array.problemTypes);

        //references to GUI components
        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        questionDisplayTextView = (TextView) findViewById(R.id.questionDisplayTextView);
        buttonTableLayout = (TableLayout) findViewById(R.id.buttonTableLayout);
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        questionNumberTextView.setText(
         getResources().getString(R.string.question)+" 1 "
                 + getResources().getString(R.string.of)+ " 10 "
        );

        resetQuiz();

    }

    //form a multiplication problem
    private String formMultiplicationProblem(){

        int firstValue = random.nextInt(25);
        int secondValue = random.nextInt(25);
        int ans = firstValue * secondValue;

        //use a delimiter to separate answer form question
        return (firstValue + " x " +secondValue+" = ?_"+ans);
    }

    //form a division problem
    private String formDivisionProblem(){

        int secondValue = random.nextInt(25);
        int ans = random.nextInt(25);
        int firstValue = secondValue *ans;

        //use a delimiter to separate answer form question
        return (firstValue + " / " +secondValue+" = ?_"+ans);
    }

    //form an addition problem
    private String formAdditionProblem(){

        int firstValue = random.nextInt(100);
        int secondValue = random.nextInt(100);
        int ans = firstValue + secondValue;

        //use a delimiter to separate answer form question
        return (firstValue + " + " +secondValue+" = ?_"+ans);

    }

        //form a subtraction problem
        private String formSubtractionProblem(){

        int firstValue = random.nextInt(100);
        int secondValue = random.nextInt(100);

        if(firstValue >= secondValue){

            int ans = firstValue - secondValue;
            //use a delimiter to separate answer form question
            return (firstValue + " - " +secondValue+" = ?_"+ans);
        }else{

            int ans = secondValue - firstValue;
            //use a delimiter to separate answer form question
            return (secondValue + " - " +firstValue+" = ?_"+ans);
        }
    }

    //set up and start a new quiz
    private void resetQuiz(){

        int randomInt = 0;

        //reset all values
        correctAnswers = 0;
        totalGuesses = 0;


        //clear old questions and generate new questions
         quizQuestionsList.clear();
         multiplicationQList.clear();
         divisionQList.clear();
         additionQList.clear();
         subtractionQList.clear();


        //add 10 random problems to the quizQuestionsList
        int counter = 0;

         if(problemTypeSelected.equalsIgnoreCase("all")){
             //generate 10 questions
             while(counter<10){

                 randomInt = random.nextInt(3);

                 if(randomInt == 0){
                     quizQuestionsList.add(formAdditionProblem());
                 }else if(randomInt == 1){
                     quizQuestionsList.add(formSubtractionProblem());
                 }else if(randomInt == 2){
                     quizQuestionsList.add(formMultiplicationProblem());
                 }else if(randomInt == 3){
                     quizQuestionsList.add(formDivisionProblem());
                 }

                 counter++;
             }


         }else if(problemTypeSelected.equalsIgnoreCase("addition")){
             //generate 10 questions
             while(counter<10) {
                 quizQuestionsList.add(formAdditionProblem());
                 counter++;
             }

         }else if(problemTypeSelected.equalsIgnoreCase("subtraction")){
             //generate 10 questions
             while(counter<10) {
                 quizQuestionsList.add(formSubtractionProblem());
                 counter++;
             }

         }else if(problemTypeSelected.equalsIgnoreCase("multiplication")){
             //generate 10 questions
             while(counter<10) {
                 quizQuestionsList.add(formMultiplicationProblem());
                 counter++;
             }
         }else if(problemTypeSelected.equalsIgnoreCase("division")){
             //generate 10 questions
             while(counter<=10) {
                 quizQuestionsList.add(formDivisionProblem());
                 counter++;
             }
         }

        //load the first problem
        loadNextProblem();
    }

    //after user guesses the right flag, load the next flag
    private void loadNextProblem(){

        int random1 = 0, random2 = 0;

        //clear the answer text
        answerTextView.setText("");

        // form the question number text
        questionNumberTextView.setText(getResources().getString(R.string.question)+" "+
                    (correctAnswers+1)+" "+getResources().getString(R.string.of)+ " 10 ");

        String[] questionAndAnswer = quizQuestionsList.remove(0).split("_");
        //quizQuestionsList.remove(questionAndAnswer);

        String question = questionAndAnswer[0];
        correctAnswer = questionAndAnswer[1];
        questionDisplayTextView.setText(question);

        for(int row = 0; row < buttonTableLayout.getChildCount(); row++){
            ((TableRow)buttonTableLayout.getChildAt(row)).removeAllViews();
        }

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //add 3,6 or 9 answer buttons based on value of guessRows
        for(int row = 0; row < guessRows ; row++)
        {
            TableRow currentTableRow = getTableRow(row);

            for(int column=0;column<3;column++)
            {
                //inflating guess button
                Button newGuessButton = (Button)layoutInflater.inflate(R.layout.guess_button, null);

                //assigning random values
                random1 = random.nextInt(20)+1;
                random2 = random.nextInt(30)+1;
                int guess = Integer.parseInt(correctAnswer) + column + random1*2 - random2;

                //set newGuessButton's text value
                newGuessButton.setText(guess+"");

                //assigning a listener
                newGuessButton.setOnClickListener(guessButtonListener);
                currentTableRow.addView(newGuessButton);
            }
        }

        //randomly replace one button with correct value
         int randomRow = random.nextInt(guessRows);
         int randomCol = random.nextInt(3);
         TableRow randomTableRow = getTableRow(randomRow);
        ((Button)randomTableRow.getChildAt(randomCol)).setText(correctAnswer);

    }

    //return the specified TableRow
    private TableRow getTableRow(int row){
     return (TableRow)buttonTableLayout.getChildAt(row);
    }


    //called when the user selects an answer
    private void submitGuess(Button guessButton){

    String guess = guessButton.getText().toString();
    totalGuesses++; //incrementing total number of guesses

        //if guess is correct
        if(guess.equals(correctAnswer))
        {
            correctAnswers++; //incrementing total correct Answers

            //display correct
            answerTextView.setText(correctAnswer + "!");
            answerTextView.setTextColor(getResources().getColor(R.color.correct));

            disableButtons();

            if(correctAnswers == 10){
                //if 10 flags are identified correctly

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.restart_quiz);

                //show quiz results
                builder.setMessage(String.format("%d %s, %.02f%% %s",totalGuesses,
                        getResources().getString(R.string.guesses),
                        (1000/(double)totalGuesses),getResources().getString(R.string.correct)));

                builder.setCancelable(false);

                //add restart quiz button
                builder.setPositiveButton(R.string.play_again,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetQuiz();
                            }
                        });

                AlertDialog resetDialog = builder.create();
                resetDialog.show();

            }else{
                //load next question after a second delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextProblem();
                    }
                },1000);


            }

        }else
        {
            //if guess is wrong
            questionDisplayTextView.startAnimation(shakeAnimation);

            answerTextView.setText(R.string.incorrect);
            answerTextView.setTextColor(getResources().getColor(R.color.incorrect));

            //disabling the incorrect guess
            guessButton.setEnabled(false);

        }


    }

    //method that disables all answer buttons
    private void disableButtons(){

        for(int row = 0;row < buttonTableLayout.getChildCount();row++){

            TableRow tableRow = (TableRow) buttonTableLayout.getChildAt(row);

            for(int j = 0; j < tableRow.getChildCount(); j++){

                tableRow.getChildAt(j).setEnabled(false);

            }
        }

    }

    //constants for the menu
    private final int CHOICES_MENU_ID = Menu.FIRST;
    private final int PROBLEM_TYPES_MENU_ID = Menu.FIRST + 1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        //add two options to the menu - "Choices" and "Problem Types"
        menu.add(Menu.NONE,CHOICES_MENU_ID, Menu.NONE, "Select the number of choices");
        menu.add(Menu.NONE, PROBLEM_TYPES_MENU_ID,Menu.NONE, "Select Problem Types");

        return true;
    }

    // called when the user selects an option from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case CHOICES_MENU_ID:

                final String[] possibleChoices =
                        getResources().getStringArray(R.array.guessesList);

            AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(this);
                choicesBuilder.setTitle(R.string.choices);

                choicesBuilder.setItems(R.array.guessesList,
             new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int item){
                    //update guessRows to match the user's choice
                    guessRows = Integer.parseInt(possibleChoices[item].toString())/3;
                    resetQuiz();
                }
            });

            AlertDialog choiceMenu = choicesBuilder.create();
            choiceMenu.show();//show the dialog

             return true;
                
            case PROBLEM_TYPES_MENU_ID:

             final String[] possibleProblemTypes =
                     getResources().getStringArray(R.array.problemTypes);

             final AlertDialog.Builder problemTypesBuilder = new AlertDialog.Builder(this);
             problemTypesBuilder.setTitle(R.string.problem_types);

             problemTypesBuilder.setItems(R.array.problemTypes,
                 new DialogInterface.OnClickListener()
                 {
                     @Override
                     public void onClick(DialogInterface dialog, int item) {

                        problemTypeSelected = possibleProblemTypes[item].toString();
                        resetQuiz();
                     }
                 });

             AlertDialog problemTypesDialog = problemTypesBuilder.create();
             problemTypesDialog.show();
             return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //called when a guess button is clicked
    private OnClickListener guessButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
            submitGuess((Button) v);
        }
    };

}
