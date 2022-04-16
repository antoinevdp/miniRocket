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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
This class manages everything that deals with the game
Responsible of rendering, updating, etc...

 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;

    private boolean canDragLine = false; // peut on tracer la ligne en maintenant le doigt
    private boolean canLeaveLine = false; // peut on relâcher la ligne pour a créée
    private boolean overlapping = false; // Boolean permettant de savoir si la planète que l'on veut faire spawn touche une autre

    private Planet currentStartPlanet; // Planète où commence la ligne
    private Planet currentStopPlanet; // Planète où finie la ligne

    public ArrayList<Planet> list_planets = new ArrayList<Planet>(); // liste des planète du niveau
    public ArrayList<Trajectory> list_trajectories = new ArrayList<Trajectory>(); // liste des Trajets entre planètes
    public ArrayList<Traveller> list_travellers = new ArrayList<Traveller>(); // liste des Trajets entre planètes


    private int trajectoryIndex; // index dans la liste des trajectoires de la trajectoire actuelle

    @SuppressLint("ClickableViewAccessibility")
    public Game(Context context, int numberOfPlanets, double facteurDeDistance) { // Constructor
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder); // loop du jeu

        // initialisation des trajectoires et ajout dans la liste des trajectoires
        for (int i=0; i<numberOfPlanets; i++) list_trajectories.add(new Trajectory(0,0,0,0));

        //initialisationd es planètes et ajout dans la liste des planètes
        generatePlanets(numberOfPlanets, facteurDeDistance);


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
            boolean removeTrajectories = false;
            Planet t_planet = list_planets.get(i); // La planète actuelle
            if(hasTouchedPlanet(cursorX, cursorY, t_planet)){ // Si la planète actuelle est touché
                currentStartPlanet = t_planet; // on assigne la planète touchée à la variable qui stocke la planète de départ
                trajectoryIndex = list_planets.indexOf(currentStartPlanet); // on assigne son index à celui de la trajectoire actuelle
                Trajectory current_trajectory = list_trajectories.get(i);
                current_trajectory.resetToCursor(cursorX,cursorY); // On place le départ de la ligne sur la position de la planète
                current_trajectory.setStartPosition(t_planet.getPositionX(), t_planet.getPositionY()); // On positionne le début de la trajectoire à la planète de départ

                canDragLine = true; // On peut créer une ligne depuis la planète de départ
                break; // Comme nous avons trouve la planète touchée, on arrête la boucle for
            }
            else{ // Si l'utilisateur n'appui pas sur une planète, on reset toutes les variables
                resetVariables();
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
                currentStartPlanet.setLinkedPlanet(currentStopPlanet); // On link la planète d'arr à la planète de départ
                currentStartPlanet.setMyTrajectory(list_trajectories.get(trajectoryIndex)); // on link la trajectoire à la planète de départ
            }
            else { // Si on relâche dans la vide
                list_trajectories.get(trajectoryIndex).reset();//on efface la ligne
                currentStartPlanet.unsetLinkedPlanet();// On retire la planète link
                currentStartPlanet.unsetMyTrajectory();// on retire la trajectoire link
            }
            //on reset toutes les variables
            resetVariables();
        }

    }

    public void generatePlanets(int numberOfPlanets, double facteurDeDistance){
        // Coordonnées et rayon min et max
        int coordMinX = 100;
        int coordMinY = 100;
        int coordMaxX = 1800;
        int coordMaxY = 1000;
        int minRadius = 30;
        int maxRadius = 100;
        // Protection si des planètes n'arrive pas à spawn
        int protection = 0;

        // Tant que toute les planètes n'ont pas spawn
        while (list_planets.size() < numberOfPlanets) {
            protection++; // incrementation de la protection

            // Coord et rayon aleatoires de spawn
            int coordX_rand = (int)Math.floor(Math.random()*(coordMaxX-coordMinX+1)+coordMinX);
            int coordY_rand = (int)Math.floor(Math.random()*(coordMaxY-coordMinY+1)+coordMinY);
            int radius_rand = (int)Math.floor(Math.random()*(maxRadius-minRadius+1)+minRadius);


            if(list_planets.size()!=0){ // Si la liste contient deja des planètes
                for (int j = 0; j < list_planets.size(); j++) { // on parcourt l'ensemble des planètes déjà présente
                    Planet previousPlanet = list_planets.get(j); // planète actuelle
                    // Calcul de la distance entre les planètes:
                    // on recupere le max et min de chaque coord
                    int maxBtwCoordX = (int)Math.max(coordX_rand, previousPlanet.getPositionX());
                    int maxBtwCoordY = (int)Math.max(coordY_rand, previousPlanet.getPositionY());
                    int minBtwCoordX = (int)Math.min(coordX_rand, previousPlanet.getPositionX());
                    int minBtwCoordY = (int)Math.min(coordY_rand, previousPlanet.getPositionY());
                    // Pyhtagore pour déterminer la distance entre les 2 planètes
                    int distance = (int)Math.sqrt(Math.pow(maxBtwCoordX - minBtwCoordX, 2) + Math.pow(maxBtwCoordY - minBtwCoordY, 2));
                    // Si la distance est inferieur à la somme des rayons, les planètes se touchent
                    if (distance < radius_rand*facteurDeDistance + previousPlanet.radius*facteurDeDistance){
                        overlapping = true;
                        break; // on s'arrête car cette planète ne peut pas spawn
                    }
                }
            }

            if (!overlapping){ // Si la planète est spawnable
                // Instanciation de la planète
                Planet planet = new Planet(getContext(), coordX_rand, coordY_rand, radius_rand, "", list_trajectories.get(list_planets.size()), list_travellers);
                list_planets.add(planet);
            }
            overlapping = false; // on reset
            if (protection > 1000)break;// Si il y a eu plus de 1000 itérations, on arrête
        }
    }

    public void resetVariables(){
        // Reset toutes les variables de mouvements ligne et planète
        canDragLine = false;
        canLeaveLine = false;
        currentStartPlanet = null;
        currentStopPlanet = null;
        trajectoryIndex = -1;
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
