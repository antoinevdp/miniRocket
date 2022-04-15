package game.example.testminirocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventListener;

public class Planet implements View.OnTouchListener{
    private double positionX;
    private double positionY;
    public double radius;
    private Context context;
    private String infos;
    private Paint paint;

    private ArrayList<Planet> list_planets = new ArrayList<Planet>();

    private Trajectory trajectory;

    public Planet(Context context, double positionX, double positionY, double radius, String infos, ArrayList<Planet> list_planets){
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.context = context;
        this.infos = infos;
        this.paint = paint;

        this.list_planets = list_planets;

        trajectory = new Trajectory(0,0,0,0);

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.planet_color);
        paint.setColor(color);

    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float)positionX, (float)positionY, (float)radius, paint);
        trajectory.draw(canvas);
    }

    public void changePosition(double positionX, double positionY){
        int color = ContextCompat.getColor(context, R.color.purple_200);
        this.paint.setColor(color);
        this.positionX = positionX;
        this.positionY = positionY;
    }
    public void release(){
        int color = ContextCompat.getColor(context, R.color.planet_color);
        this.paint.setColor(color);
    }

    public float getPositionX(){
        return (float)(this.positionX);
    }
    public float getPositionY(){
        return (float)(this.positionY);
    }

    public void update() {
        trajectory.update();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }
}
