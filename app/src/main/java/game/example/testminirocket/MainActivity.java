package game.example.testminirocket;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Button;


import com.minirocket.game.R;

/*
This activity is the launch activity of the App
 */

public class MainActivity extends Activity {
    private Button button;
    private Button button_test;
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //cela permet de mettre l'activité en plein écran
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_LONG).show();

        button = (Button) findViewById(R.id.btn_play);
        settings = (Button) findViewById(R.id.btn_options);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), secondActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), settingsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    //cela permet d'enlever les transitions entre les activités
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}