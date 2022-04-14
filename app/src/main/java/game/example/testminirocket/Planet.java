package game.example.testminirocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

public class Planet {
    private double positionX;
    private double positionY;
    private double radius;
    private Context context;
    private String infos;
    private Paint paint;

    public Planet(Context context, double positionX, double positionY, double radius, String infos){
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.context = context;
        this.infos = infos;
        this.paint = paint;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.planet_color);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float)positionX, (float)positionY, (float)radius, paint);
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

    public void update() {
    }
}
