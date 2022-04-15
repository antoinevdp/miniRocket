package game.example.testminirocket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

import java.util.ArrayList;
import java.util.List;

/*
This class manages everything that deals with the game
Responsible of rendering, updating, etc...

 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;

    private Planet planet;
    private Planet planet2;
    private Planet planet3;

    private boolean canDragLine = false;
    private boolean canLeaveLine = false;

    public ArrayList<Planet> list_planets = new ArrayList<Planet>();
    public ArrayList<Trajectory> list_trajectories = new ArrayList<Trajectory>();



    @SuppressLint("ClickableViewAccessibility")
    public Game(Context context) { // Constructor
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        planet = new Planet(getContext(), 500, 200, 50, "", list_planets);
        list_planets.add(planet);
        planet2 = new Planet(getContext(), 1500, 300, 50, "", list_planets);
        list_planets.add(planet2);
        planet3 = new Planet(getContext(), 720, 700, 50, "", list_planets);
        list_planets.add(planet3);

        list_trajectories.add(new Trajectory(0,0,0,0));
        list_trajectories.add(new Trajectory(0,0,0,0));
        list_trajectories.add(new Trajectory(0,0,0,0));

        Log.d("liste p", String.valueOf(list_planets.get(0).getPositionX()));
        Log.d("liste t", String.valueOf(list_trajectories));

        this.setOnTouchListener(planet);


        setFocusable(true);
    }

    @Override // Touch Event Handler
    public boolean onTouchEvent(MotionEvent event) {
        int index_traj = 0;
        //Touch events
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < list_planets.size(); i++) {
                    index_traj = i;
                    Planet t_planet = list_planets.get(i);
                    if(hasTouchedPlanet((float) event.getX(), (float) event.getY(), t_planet)){
                        list_trajectories.get(0).resetToCursor(event.getX(), (float) event.getY());
                        Log.d("a touche ? : ", String.valueOf(hasTouchedPlanet((float) event.getX(), (float) event.getY(), t_planet)));
                        list_trajectories.get(0).setStartPosition(t_planet.getPositionX(), t_planet.getPositionY());
                        canDragLine = true;
                        break;
                    }
                    else{
                        list_trajectories.get(0).reset();
                        canDragLine = false;
                    }
                }

                return true;

            case MotionEvent.ACTION_MOVE:
                if(canDragLine){
                    list_trajectories.get(0).setEndPosition((float) event.getX(), (float) event.getY());
                    invalidate();
                }
            case MotionEvent.ACTION_UP:
                if (true){
                    for (int i = 0; i < list_planets.size(); i++) {
                        index_traj = i;
                        Planet t_planet = list_planets.get(i);
                        if(hasTouchedPlanet((float) event.getX(), (float) event.getY(), t_planet)){
                            Log.d("a touche leve? : ", String.valueOf(hasTouchedPlanet((float) event.getX(), (float) event.getY(), t_planet)));
                            list_trajectories.get(0).setEndPosition(t_planet.getPositionX(), t_planet.getPositionY());
                            break;
                        }
                    }
                }

                //list_trajectories.get(0).reset();
                return true;

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
        holder.removeCallback(this);
        gameLoop.stopLoop();

    }

    @Override // Affichage
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawFPS(canvas);

        for (int i = 0; i < list_planets.size(); i++) {
            list_planets.get(i).draw(canvas);
        }
        for (int i = 0; i < list_trajectories.size(); i++) {
            list_trajectories.get(i).draw(canvas);
        }

    }

    public boolean hasTouchedPlanet(float cursorX, float cursorY, Planet t_planet){
        return ((cursorX >= t_planet.getPositionX() - t_planet.radius) && (cursorX <= (float) t_planet.getPositionX() + t_planet.radius)) && ((cursorY >= t_planet.getPositionY() - t_planet.radius) && (cursorY <= t_planet.getPositionY() + t_planet.radius));
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.purple_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS : " + averageFPS, 100, 100, paint);
    }

    public void update() {
        //update
        for (int i = 0; i < list_planets.size(); i++) {
            list_planets.get(i).update();
        }
        for (int i = 0; i < list_trajectories.size(); i++) {
            list_trajectories.get(i).update();
        }
        return;
    }
}
/*
for (int i=0; i <= list_planets.size(); i++){
                    index_traj = i;
                    Planet t_planet = list_planets.get(i);
                    if (hasTouchedPlanet((float) getX(), (float) getY(), t_planet)){
                        Log.d("ff", "ff");
                        list_trajectories.get(index_traj).setStartPosition((float)event.getX(), (float)event.getY());
                    }
                }
 */