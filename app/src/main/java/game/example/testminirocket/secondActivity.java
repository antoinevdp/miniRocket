package game.example.testminirocket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.minirocket.game.R;

public class secondActivity extends Activity {

    Animation rotateAnimation;
    int selectLvl = 0;

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

        ImageView traj1 = (ImageView) findViewById(R.id.imageView4);
        ImageView traj2 = (ImageView) findViewById(R.id.imageView3);
        ImageView traj3 = (ImageView) findViewById(R.id.imageView5);
        ImageView traj4 = (ImageView) findViewById(R.id.imageView6);

        ImageView rocket = (ImageView) findViewById(R.id.rocketbutton);
        ImageButton cerclebutton = (ImageButton) findViewById(R.id.cercle);


        rotateAnimation(galaxie1,7000) ;
        rotateAnimation(galaxie2,5000) ;
        rotateAnimation(galaxie3,4000) ;
        rotateAnimation(galaxie4,6000) ;

        //line between planet and rocket

        galaxie1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traj1.setVisibility(View.VISIBLE);
                traj2.setVisibility(View.INVISIBLE);
                traj3.setVisibility(View.INVISIBLE);
                traj4.setVisibility(View.INVISIBLE);
                rocket.setImageResource(R.drawable.ic_round_rocket_green);
                cerclebutton.setImageResource(R.drawable.ic_ellipse_2_green);
                selectLvl = 1;
            }
        });
        galaxie2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traj2.setVisibility(View.VISIBLE);
                traj1.setVisibility(View.INVISIBLE);
                traj3.setVisibility(View.INVISIBLE);
                traj4.setVisibility(View.INVISIBLE);
                rocket.setImageResource(R.drawable.ic_round_rocket_green);
                cerclebutton.setImageResource(R.drawable.ic_ellipse_2_green);
                selectLvl = 2;
            }
        });
        galaxie3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traj3.setVisibility(View.VISIBLE);
                traj1.setVisibility(View.INVISIBLE);
                traj2.setVisibility(View.INVISIBLE);
                traj4.setVisibility(View.INVISIBLE);
                rocket.setImageResource(R.drawable.ic_round_rocket_green);
                cerclebutton.setImageResource(R.drawable.ic_ellipse_2_green);
                selectLvl = 3;
            }
        });
        galaxie4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                traj4.setVisibility(View.VISIBLE);
                traj1.setVisibility(View.INVISIBLE);
                traj2.setVisibility(View.INVISIBLE);
                traj3.setVisibility(View.INVISIBLE);
                rocket.setImageResource(R.drawable.ic_round_rocket_green);
                cerclebutton.setImageResource(R.drawable.ic_ellipse_2_green);
                selectLvl = 4;
            }
        });

        cerclebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivityTest.class);
                Bundle parameter = new Bundle();
                switch (selectLvl){
                    case  0:
                        Toast.makeText(secondActivity.this, "Choose a level", Toast.LENGTH_SHORT).show();
                        break;
                    case  1:
                        parameter.putInt("nbPlanets", 5);
                        parameter.putInt("distance", 3);
                        myIntent.putExtras(parameter);
                        startActivityForResult(myIntent, 0);
                        break;
                    case  2:
                        parameter.putInt("nbPlanets", 8);
                        parameter.putInt("distance", 2);
                        myIntent.putExtras(parameter);
                        startActivityForResult(myIntent, 0);
                        break;
                    case  3:
                        parameter.putInt("nbPlanets", 12);
                        parameter.putInt("distance", 2);
                        myIntent.putExtras(parameter);
                        startActivityForResult(myIntent, 0);
                        break;
                    case  4:
                        parameter.putInt("nbPlanets", 15);
                        parameter.putInt("distance", 2);
                        myIntent.putExtras(parameter);
                        startActivityForResult(myIntent, 0);
                        break;
                }
            }
        });
    }


    private void rotateAnimation(ImageButton imageButton, long duration){
        rotateAnimation= AnimationUtils.loadAnimation(this,R.anim.rotate);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(duration);
        imageButton.startAnimation(rotateAnimation);
    }

    //no transition between activity
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}