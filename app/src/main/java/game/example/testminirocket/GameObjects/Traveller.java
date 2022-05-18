package game.example.testminirocket.GameObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import game.example.testminirocket.BFS;
import game.example.testminirocket.GamePanels.TimeBar;

public class Traveller extends GameObject{
    private static final double SPEED_PIXELS_PER_SECOND = 100;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWN_PER_MINUTE = 20;
    private static final double SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0;
    private static final double UPDATE_PER_SPAWN = GameLoop.MAX_UPS / SPAWN_PER_SECOND;
    private static double updateUntilNextSpawn = UPDATE_PER_SPAWN;


    private TimeBar timeBar;

    private double velocityX; // vitesse en x
    private double velocityY; // vitesse en y

    public boolean canGo; // Boolean pour avancer ou non
    public boolean isTravelling;
    public boolean canBeDestroyed;
    public boolean hasArrived;

    private Planet current_planet; // Planète actuelle
    private Planet next_target_planet; // Planète où l'on veut aller
    private Planet final_target_planet; // Planète où l'on veut aller

    private double max_time; // temps de vie maximum
    private double time_remaining; // temps restant avant mort
    private final Paint paint;

    private int counter = 1; // Counter pour arriver jusqu'à la planète souhaitée

    private ArrayList<Planet> listAllPlanets; // Liste de toutes les planètes du niveau

    private SpaceShip current_spaceship;


    private ArrayList<Integer> path_to_take; // Chemin que doit prendre le traveller pour arriver jusqu'à sa planète cible
    //Constructor
    public Traveller(Context context, double coordX, double coordY, double max_time, Planet current_planet, Planet next_target_planet, ArrayList<Planet> listAllPlanets) {
        super(coordX, coordY);
        this.current_planet = current_planet;
        this.next_target_planet = next_target_planet;
        this.final_target_planet = next_target_planet;
        this.current_spaceship = null;

        this.timeBar = new TimeBar(context, this);

        this.listAllPlanets = listAllPlanets;

        this.max_time = max_time;
        this.time_remaining = this.max_time;
        this.canGo = false;
        this.canBeDestroyed = false;
        this.hasArrived = false;

        this.path_to_take = null;
        // Faire spawn le traveller dans le rayon de sa planète aléatoirement
        double planetX = current_planet.getPositionX();
        double planetY = current_planet.getPositionY();
        double spawnRadius = current_planet.radius-20;
        double coordX_rand = Math.floor(Math.random()*((planetX+spawnRadius)-(planetX-spawnRadius)+1)+(planetX-spawnRadius));
        double coordY_rand = (int)Math.floor(Math.random()*((planetY+spawnRadius)-(planetY-spawnRadius)+1)+(planetY-spawnRadius));
        this.coordX = coordX_rand; // Set les coordonées
        this.coordY = coordY_rand;

        this.paint = new Paint();
        paint.setColor(this.final_target_planet.getRandomAndroidColor());
    }

    // Check if readyToSpawn
    public static boolean readyToSpawn() {
        if(updateUntilNextSpawn <= 0){
            updateUntilNextSpawn += UPDATE_PER_SPAWN;
            return true;
        }else {
            updateUntilNextSpawn --;
            return false;
        }
    }

    // Affichage
    public void draw(Canvas canvas) {
        canvas.drawCircle((float)this.coordX, (float)this.coordY, 10, paint);
        timeBar.draw(canvas);
    }
    // Update
    public void update(){
        this.time_remaining -= 0.01f;
        if (this.time_remaining <= 0){
            this.canBeDestroyed = true;
        }

    }

    public void setPathToTake(ArrayList<Integer> path){
        this.path_to_take = path;
    }

    public Planet getInitial_planet() {
        return current_planet;
    }

    public Planet getNext_Target_planet() {
        return next_target_planet;
    }
    public Planet getFinal_Target_planet() {
        return final_target_planet;
    }

    public double getMax_time() {
        return max_time;
    }

    public void setMax_time(double max_time) {
        this.max_time = max_time;
    }

    public double getTime_remaining() {
        return time_remaining;
    }

    public void setTime_remaining(double time_remaining) {
        this.time_remaining = time_remaining;
    }

    public void setCurrent_spaceship(SpaceShip spaceShip){
        this.current_spaceship = spaceShip;
    }
    public SpaceShip getCurrent_spaceship(){
        return this.current_spaceship;
    }

    public void moveToCoord(double coordX, double coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }
}
