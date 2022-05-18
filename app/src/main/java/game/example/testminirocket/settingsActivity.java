package game.example.testminirocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.minirocket.game.R;

/*
 * cette activité permet de configurer différentes options
 */

public class settingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //cela permet de mettre l'activité en plein écran
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ImageButton button = (ImageButton) findViewById(R.id.returnButton); //on initialise les buttons
        ImageButton trueFasle = (ImageButton) findViewById(R.id.trueFalse);
        trueFasle.setTag(R.drawable.ic_resource_true);


        //en cliauant sur ce button on retourne à l'activité précedente
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //cela permet changer l'icone pour la vibration
        trueFasle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Integer) trueFasle.getTag()) == R.drawable.ic_resource_true){
                    trueFasle.setTag(R.drawable.ic_false);
                    trueFasle.setImageResource(R.drawable.ic_false);
                }
                else if(((Integer) trueFasle.getTag()) == R.drawable.ic_false){
                    trueFasle.setTag(R.drawable.ic_resource_true);
                    trueFasle.setImageResource(R.drawable.ic_resource_true);
                }

            }
        });



    }

    //no transition between activity
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
