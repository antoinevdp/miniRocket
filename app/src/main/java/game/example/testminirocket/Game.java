package game.example.testminirocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

/*
This class manages everything that deals with the game
Responsible of rendering, updating, etc...

 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private Planet planet;
    private Planet planet2;
    private Planet planet3;
    private Context context;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);

        planet = new Planet(getContext(), 500, 200, 50, "");
        planet2 = new Planet(getContext(), 1500, 300, 50, "");
        planet3 = new Planet(getContext(), 720, 700, 50, "");

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Touch events
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                planet.changePosition((double)event.getX(), (double) event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                planet.release();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawFPS(canvas);

        planet.draw(canvas);
        planet2.draw(canvas);
        planet3.draw(canvas);

    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.purple_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS : " + averageFPS, 100, 100, paint);
    }

    public void update() {
        //update
        planet.update();
        return;
    }
}
