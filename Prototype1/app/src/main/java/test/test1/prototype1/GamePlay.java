package test.test1.prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import Computation.*;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class GamePlay extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    MainController mc = new MainController();
    int currentdigit;
    int goaldigit;
    int nextdigit;
    long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        startGame();
    }

    public void startGame(){
        currentdigit = 0;
        goaldigit = (int) (Math.random() * ((5000 - 2000) + 1));
        goaldigit =50;
        nextdigit = (int) (Math.random() * ((9 - 0) + 1));
        TextView Goal = (TextView) findViewById(R.id.GoalNum);
        Goal.setText(Integer.toString(goaldigit));
        TextView current = (TextView) findViewById(R.id.CurrentNum);
        current.setText(Integer.toString(currentdigit));
        NextDigit();
        startTime = System.currentTimeMillis();

    }

    public void NextDigit(){
        nextdigit = (int) (Math.random() * ((9 - 0) + 1));
        TextView next = (TextView) findViewById(R.id.NextDigit);
        next.setText(Integer.toString(nextdigit));

    }

    public void CalculateNext(View view) {
        String arith;
        switch (view.getId()) {
            case R.id.add:
                arith = "+";
                break;
            case R.id.minus:
                arith = "-";
                break;
            case R.id.multiple:
                arith = "*";
                break;
            case R.id.divide:
                if(nextdigit!=0)
                arith = "/";
                else
                arith = "error";
                break;
            default:
                arith = "+";
                break;

        }

        currentdigit = mc.Calculate(currentdigit, nextdigit, arith);
        TextView current = (TextView) findViewById(R.id.CurrentNum);
        current.setText(Integer.toString(currentdigit));
        checkEndgame(currentdigit,goaldigit);
        NextDigit();


    }

    public void checkEndgame(int currentdigit, int goaldigit){
        if(goaldigit == currentdigit){
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            String test = Long.toString(totalTime);
            System.out.println(test);
            Intent endgame = new Intent(this, EndGame.class);
            endgame.putExtra(EXTRA_MESSAGE, test);
            startActivity(endgame);
        }

    }

}