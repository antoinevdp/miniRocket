package game.example.testminirocket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import com.minirocket.game.R;

public class secondActivity extends Activity {

    Animation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_menu);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //button retour
        ImageButton button = (ImageButton) findViewById(R.id.returnButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton galaxie1 = (ImageButton) findViewById(R.id.galaxie1);
        ImageButton galaxie2 = (ImageButton) findViewById(R.id.galaxie2);
        ImageButton galaxie3 = (ImageButton) findViewById(R.id.galaxie3);
        ImageButton galaxie4 = (ImageButton) findViewById(R.id.galaxie4);


        rotateAnimation(galaxie1,7000) ;
        rotateAnimation(galaxie2,5000) ;
        rotateAnimation(galaxie3,4000) ;
        rotateAnimation(galaxie4,6000) ;

    }

    private void rotateAnimation(ImageButton imageButton, long duration){
        rotateAnimation= AnimationUtils.loadAnimation(this,R.anim.rotate);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(duration);
        imageButton.startAnimation(rotateAnimation);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}