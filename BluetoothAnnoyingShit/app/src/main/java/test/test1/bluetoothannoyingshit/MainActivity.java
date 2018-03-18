package test.test1.bluetoothannoyingshit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    protected void joinButton(View view){
        Intent intent = new Intent(this, DisplayDevices.class);
        startActivity(intent);
    }

    protected void hostButton(View view){

    }
}
