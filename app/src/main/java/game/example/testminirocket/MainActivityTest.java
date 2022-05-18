package game.example.testminirocket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import game.example.testminirocket.GameObjects.Game;

/**
 * MainActivity is the entry point to our application.
 */
public class MainActivityTest extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivityTest.java", "onCreate()");

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        super.onCreate(savedInstanceState);

        Bundle parameter = getIntent().getExtras();
        int nbPlanets = -1;
        int distance = -1;
        if(parameter != null){
            nbPlanets = parameter.getInt("nbPlanets");
            distance = parameter.getInt("distance");
        }

        // Set content view to game, so that objects in the Game class can be rendered to the screen
        game = new Game(this, nbPlanets, distance);
        setContentView(game);
    }

    @Override
    protected void onStart() {
        Log.d("MainActivityTest.java", "onStart()");
        super.onStart();

    }

    @Override
    protected void onResume() {
        Log.d("MainActivityTest.java", "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("MainActivityTest.java", "onPause()");
        game.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("MainActivityTest.java", "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivityTest.java", "onDestroy()");
        super.onDestroy();
        finish();
    }
}