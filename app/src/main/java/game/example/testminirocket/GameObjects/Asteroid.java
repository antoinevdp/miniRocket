package game.example.testminirocket.GameObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import game.example.testminirocket.graphics.Animator;
import game.example.testminirocket.graphics.Sprite;

public class Asteroid extends GameObject{
    private static final double SPEED_PIXELS_PER_SECOND = 100;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWN_PER_MINUTE = 0.1;
    private static final double SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0;
    private static final double UPDATE_PER_SPAWN = GameLoop.MAX_UPS / SPAWN_PER_SECOND;
    private static double updateUntilNextSpawn = UPDATE_PER_SPAWN;

    public boolean canAnimate;
    private Trajectory target_trajectory;
    private float level;
    private AsteroidState asteroidState;
    private Animator animator;

    public boolean hasArrived = false;

    public double velocityX; // vitesse en x
    public double velocityY; // vitesse en y

    double distanceToNextPlanetX;
    double distanceToNextPlanetY;
    double distanceToNextPlanet;
    double directionX;
    double directionY;

    public Asteroid(Context context, double coordX, double coordY, Trajectory target_trajectory, float level, Animator animator) {
        super(coordX, coordY);
        this.target_trajectory = target_trajectory;
        this.level = level;
        this.animator = animator;
        this.asteroidState = new AsteroidState(this);
        this.canAnimate = true;

        distanceToNextPlanetX = this.target_trajectory.getPosMidX() - this.coordX; // On calcule la distance x entre sa position et la planète
        distanceToNextPlanetY = this.target_trajectory.getPosMidY() - this.coordY; // On calcule la distance y entre sa position et la planète

        distanceToNextPlanet = GameObject.getDistanceBetweenObjects(this, this.target_trajectory); // on calcule la distance entre la planète target et le traveller

        directionX = distanceToNextPlanetX/distanceToNextPlanet; // on calcule la direction à prendre en x
        directionY = distanceToNextPlanetY/distanceToNextPlanet; // on calcule la direction à prendre en y
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

    public void update() {
        distanceToNextPlanet = GameObject.getDistanceBetweenObjects(this, this.target_trajectory); // on calcule la distance entre la planète target et le traveller
        if (distanceToNextPlanet > 10){ // Si la distance entre le traveller et la planète cible est > 10
            // on fait avancer dans la direction
            this.velocityX = directionX*MAX_SPEED;
            this.velocityY = directionY*MAX_SPEED;
            this.coordX += this.velocityX;
            this.coordY += this.velocityY;
        }else { // Sinon, on est arrivé à la planète cible
            this.velocityX = 0;
            this.velocityY = 0;
            this.hasArrived = true;
            //this.next_target_planet = this.listAllPlanets.get(this.path_to_take.get(counter)); // Sa planète cible est la planète suivante dans la liste des planètes du chemin à prendre
        }
        asteroidState.update();
    }

    public void draw(Canvas canvas) {
        animator.drawAsteroid(canvas, this);
    }

    public AsteroidState getAsteroidState() {
        return asteroidState;
    }

    public Trajectory getTargetTrajectory() {
        return this.target_trajectory;
    }
}
