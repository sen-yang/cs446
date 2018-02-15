package test.test1.prototype1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startGame(View view){
        Intent intent = new Intent(this, GamePlay.class);
        startActivity(intent);

    }
    public void quitGame(View view){
               // TODO Auto-generated method stub
                finish();
                System.exit(0);

    }
}
