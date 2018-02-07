package sen.sen.prototypesen.Activities;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sen.sen.prototypesen.CustomUI.NumberListVIew;
import sen.sen.prototypesen.Helpers;
import sen.sen.prototypesen.R;

public class MainActivity extends AppCompatActivity{
  private static final int LIST_MIN_RANGE = 0;
  private static final int LIST_MAX_RANGE = 9;
  private static final int TARGET_MIN = 1;
  private static final int TARGET_MAX = 100;
  private static final int MAX_TIME = 60 * 1000;

  private TextView timeRemainingTextView;
  private TextView targetTextView;
  private TextView currentTextView;
  private NumberListVIew<Integer> numberListView;
  private ImageView addButton;
  private ImageView subtractButton;
  private ImageView multiplyButton;
  private ImageView divideButton;

  private CountDownTimer countDownTimer;
  private List<Integer> numberList;
  private int currentNumberListPosition;
  private int targetNumber;
  private int currentNumber;
  private long timeRemaining;
  private boolean gameStarted;
  private boolean gameOver;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    timeRemainingTextView = findViewById(R.id.timeRemainingTextView);
    targetTextView = findViewById(R.id.targetTextView);
    currentTextView = findViewById(R.id.currentTextView);
    numberListView = findViewById(R.id.numberListView);
    addButton = findViewById(R.id.addButton);
    subtractButton = findViewById(R.id.subtractButton);
    multiplyButton = findViewById(R.id.multiplyButton);
    divideButton = findViewById(R.id.divideButton);

    newGame();
  }

  public void addButtonPressed(View view){
    if(gameOver){
      return;
    }
    currentNumber += numberList.get(currentNumberListPosition);
    buttonAction();
  }

  public void subtractButtonPressed(View view){
    if(gameOver){
      return;
    }
    currentNumber -= numberList.get(currentNumberListPosition);
    buttonAction();
  }

  public void multiplyButtonPressed(View view){
    if(gameOver){
      return;
    }
    currentNumber *= numberList.get(currentNumberListPosition);
    buttonAction();
  }

  public void divideButtonPressed(View view){
    if(gameOver){
      return;
    }
    if(numberList.get(currentNumberListPosition) == 0){
      gameLost(true);
      return;
    }
    currentNumber /= numberList.get(currentNumberListPosition);
    buttonAction();
  }

  private void buttonAction(){
    currentTextView.setText(Integer.toString(currentNumber));

    if(!gameStarted){
      startGame();
    }
    if(!checkWin()){
      incrementNumberList();
    }
  }

  private void newGame(){
    numberList = new ArrayList<>();
    numberList.add(Helpers.randomIntInRangeInclusive(LIST_MIN_RANGE, LIST_MAX_RANGE));
    numberList.add(Helpers.randomIntInRangeInclusive(LIST_MIN_RANGE, LIST_MAX_RANGE));
    numberList.add(Helpers.randomIntInRangeInclusive(LIST_MIN_RANGE, LIST_MAX_RANGE));
    currentNumber = 0;
    targetNumber = Helpers.randomIntInRangeInclusive(TARGET_MIN, TARGET_MAX);

    while(targetNumber == 0){
      targetNumber = Helpers.randomIntInRangeInclusive(TARGET_MIN, TARGET_MAX);
    }
    currentNumberListPosition = 0;
    numberListView.updateListAndPosition(numberList, currentNumberListPosition);
    gameStarted = false;
    gameOver = false;
    targetTextView.setText(Integer.toString(targetNumber));
    currentTextView.setText(Integer.toString(currentNumber));
    countDownTimer = new CountDownTimer(MAX_TIME, 10){
      @Override
      public void onTick(long timeRemaining){
        MainActivity.this.timeRemaining = timeRemaining;
        timeRemainingTextView.setText(String.format("%ss",new SimpleDateFormat("ss.SS").format(new Date(timeRemaining))));
      }

      @Override
      public void onFinish(){
        if(!checkWin()){
          gameLost(false);
        }
      }
    };
  }

  private void startGame(){
    gameStarted = true;
    countDownTimer.start();
  }

  private boolean checkWin(){
    if(targetNumber == currentNumber){
      gameOver = true;
      countDownTimer.cancel();
      showGameEndDialog(getString(R.string.winner_title), String.format(getString(R.string.winner_message), currentNumberListPosition, new SimpleDateFormat("ss.SS").format(new Date(timeRemaining))));
      return true;
    }
    return false;
  }


  private void gameLost(boolean divideBy0){
    gameOver = true;
    countDownTimer.cancel();

    if(divideBy0){
      showGameEndDialog(getString(R.string.loser_title), getString(R.string.divide_by_0_message));
    }
    else{
      showGameEndDialog(getString(R.string.loser_title), String.format(getString(R.string.time_lose_message), Math.abs(targetNumber - currentNumber)));
    }
  }

  private void showGameEndDialog(String title, String message){
    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
    alertDialog.setTitle(title);
    alertDialog.setMessage(message);
    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "New Game",
        new DialogInterface.OnClickListener(){
          public void onClick(DialogInterface dialog, int which){
            newGame();
            dialog.dismiss();
          }
        });
    alertDialog.setCanceledOnTouchOutside(false);
    alertDialog.show();
  }

  private void incrementNumberList(){
    numberList.add(Helpers.randomIntInRangeInclusive(LIST_MIN_RANGE, LIST_MAX_RANGE));
    currentNumberListPosition++;
    numberListView.updateListAndPosition(numberList, currentNumberListPosition);
  }
}
