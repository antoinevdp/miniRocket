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

    private boolean canDragLine = false; // peut on tracer la ligne en maintenant le doigt
    private boolean canLeaveLine = false; // peut on relâcher la ligne pour a créée

    private Planet currentStartPlanet; // Planète où commence la ligne
    private Planet currentStopPlanet; // Planète où finie la ligne

    public ArrayList<Planet> list_planets = new ArrayList<Planet>(); // liste des planète du niveau
    public ArrayList<Trajectory> list_trajectories = new ArrayList<Trajectory>(); // liste des Trajets entre planètes

    private int trajectoryIndex; // index dans la liste des trajectoires de la trajectoire actuelle



    @SuppressLint("ClickableViewAccessibility")
    public Game(Context context) { // Constructor
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder); // loop du jeu

        //initialisationd es planètes et ajout dans la liste des planètes
        planet = new Planet(getContext(), 500, 200, 50, "", list_planets);
        list_planets.add(planet);
        planet2 = new Planet(getContext(), 1500, 300, 50, "", list_planets);
        list_planets.add(planet2);
        planet3 = new Planet(getContext(), 720, 700, 50, "", list_planets);
        list_planets.add(planet3);

        // initialisation des trajectoires et ajout dans la liste des trajectoires
        for (int i=0; i<list_planets.size(); i++) list_trajectories.add(new Trajectory(0,0,0,0));

        setFocusable(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override // Touch Event Handler
    public boolean onTouchEvent(MotionEvent event) {
        //Touch events
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: // Dès que l'utilisateur appui
                startLine(event.getX(), event.getY()); // On crée ou non la ligne en récupérant les coord de l'endroit cliqué
                return true; // Action terminée

            case MotionEvent.ACTION_MOVE: // Tant que l'utilisateur reste appuyé
                if(canDragLine) draggingLine(event.getX(), event.getY()); // on trace la ligne en fonction de l'endroit où l'ulisateur se déplace
                return true; // Action terminée

            case MotionEvent.ACTION_UP: // Dès que l'utilisateur relâche
                stopLine(); // on arrête la ligne sur une planète ou alors on l'efface

                return true; // Action terminée

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

    @Override // Affichage des elements à l'écran
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawFPS(canvas);

        // on affiche toutes les planètes et trajectoires
        for (int i = 0; i < list_planets.size(); i++) {
            list_planets.get(i).draw(canvas);
        }
        for (int i = 0; i < list_trajectories.size(); i++) {
            list_trajectories.get(i).draw(canvas);
        }

    }

    // Si l'utilisateur touche une planète
    public boolean hasTouchedPlanet(float cursorX, float cursorY, Planet t_planet){
        return ((cursorX >= t_planet.getPositionX() - t_planet.radius) && (cursorX <= (float) t_planet.getPositionX() + t_planet.radius)) && ((cursorY >= t_planet.getPositionY() - t_planet.radius) && (cursorY <= t_planet.getPositionY() + t_planet.radius));
    }
    // Trace la ligne
    public void startLine(float cursorX, float cursorY){
        for (int i = 0; i < list_planets.size(); i++) { // on parcourt la liste des planètes
            Planet t_planet = list_planets.get(i); // La planète actuelle
            if(hasTouchedPlanet(cursorX, cursorY, t_planet)){ // Si la planète actuelle est touché
                currentStartPlanet = t_planet; // on assigne la planète touchée à la variable qui stocke la planète de départ
                trajectoryIndex = list_planets.indexOf(currentStartPlanet); // on assigne son index à celui de la trajectoire actuelle
                list_trajectories.get(i).resetToCursor(cursorX,cursorY); // On place le départ de la ligne sur la position de la planète
                list_trajectories.get(i).setStartPosition(t_planet.getPositionX(), t_planet.getPositionY()); // On positionne le début de la trajectoire à la planète de départ
                canDragLine = true; // On peut créer une ligne depuis la planète de départ
                break; // Comme nous avons trouve la planète touchée, on arrête la boucle for
            }
            else{ // Si l'utilisateur n'appui pas sur une planète, on reset toutes les variables
                canDragLine = false;
                canLeaveLine = false;
                currentStartPlanet = null;
                currentStopPlanet = null;
                trajectoryIndex = -1;
            }
        }
    }
    public void draggingLine(float cursorX, float cursorY){ // Quand le joueur maintient
        if (trajectoryIndex != -1){
            list_trajectories.get(trajectoryIndex).setEndPosition(cursorX, cursorY); // On récupère la trajectoire actuelle
            for (int i = 0; i < list_planets.size(); i++) { // on parcourt la liste des planètes
                Planet t_planet = list_planets.get(i); // on recupere la planète actuelle
                if(hasTouchedPlanet(cursorX, cursorY, t_planet)){ // Si l'utilisateur passe sur une planète
                    canLeaveLine = true; // On peut relacher la ligne
                    currentStopPlanet = t_planet; // la planète d'arrivée est la planète actuelle
                    // On set la fin du segment à l'emplacement du doigt
                    list_trajectories.get(trajectoryIndex).setEndPosition(currentStopPlanet.getPositionX(), currentStopPlanet.getPositionY());
                    break; // On a trouve la planète donc on quitte la boucle
                }
                else{ // Si l'utilisateur ne passe pas au dessus d'une planète
                    canLeaveLine = false; // On ne peut pas relâcher (en créant une ligne)
                    currentStopPlanet = null; // il n'y a pas de planète d'arr
                }
            }
            invalidate(); // on call draw pour actualiser
        }

    }
    public void stopLine(){ // Quand on relâche le doigt
        if(trajectoryIndex != -1){
            // Si on peut relâcher sur une planète
            if(canLeaveLine){
                // On set la fin du segment à l'emplacement de la deuxieme planète
                list_trajectories.get(trajectoryIndex).setEndPosition(currentStopPlanet.getPositionX(), currentStopPlanet.getPositionY());
            }
            else list_trajectories.get(trajectoryIndex).reset(); // Si l'utilisateur ne relâche pas sur une planète, on efface la ligne
            //on reset toutes les variables
            canDragLine = false;
            canLeaveLine = false;
            currentStartPlanet = null;
            currentStopPlanet = null;
            trajectoryIndex = -1;
        }

    }

    public void drawFPS(Canvas canvas){ // FPS
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.purple_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS : " + averageFPS, 100, 100, paint);
    }

    public void update() { // Update
        //update
        for (int i = 0; i < list_planets.size(); i++) {
            list_planets.get(i).update();
        }
        for (int i = 0; i < list_trajectories.size(); i++) {
            list_trajectories.get(i).update();
        }
    }
}
