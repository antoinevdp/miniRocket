package game.example.testminirocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

import java.util.ArrayList;

public class Traveller extends GameObject{
    private static final double SPEED_PIXELS_PER_SECOND = 400;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    private double velocityX;
    private double velocityY;

    public boolean canGo;

    private Planet current_planet;
    private Planet target_planet;
    private double max_time;
    private double time_remaining;
    private final Paint paint;

    private int counter = 1;

    private ArrayList<Planet> listAllPlanets;

    private ArrayList<Integer> path_to_take;

    public Traveller(Context context, double coordX, double coordY, Planet current_planet, Planet target_planet, ArrayList<Planet> listAllPlanets) {
        super(coordX, coordY);
        this.current_planet = current_planet;
        this.target_planet = target_planet;
        this.listAllPlanets = listAllPlanets;
        this.max_time = max_time;
        this.canGo = false;
        this.velocityX = 0;
        this.velocityY = 0;

        this.path_to_take = null;

        int planetX = (int)current_planet.getPositionX();
        int planetY = (int)current_planet.getPositionY();
        int spawnRadius = (int)current_planet.radius-20;
        double coordX_rand = Math.floor(Math.random()*((planetX+spawnRadius)-(planetX-spawnRadius)+1)+(planetX-spawnRadius));
        double coordY_rand = (int)Math.floor(Math.random()*((planetY+spawnRadius)-(planetY-spawnRadius)+1)+(planetY-spawnRadius));
        this.coordX = coordX_rand;
        this.coordY = coordY_rand;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.traveller_color);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float)this.coordX, (float)this.coordY, 10, paint);
    }

    public void update(){
        if (this.path_to_take != null){
            if (this.path_to_take.size() == counter){
                this.canGo = false;
                this.path_to_take = null;
            }else {
                this.canGo = true;
                this.target_planet = this.listAllPlanets.get(this.path_to_take.get(counter));
            }

        }
        if (this.canGo){
            double distanceToNextPlanetX = this.target_planet.getPositionX() - this.coordX;
            double distanceToNextPlanetY = this.target_planet.getPositionY() - this.coordY;

            double distanceToNextPlanet = GameObject.getDistanceBetweenObjects(this, this.target_planet);
            System.out.println(distanceToNextPlanet);

            double directionX = distanceToNextPlanetX/distanceToNextPlanet;
            double directionY = distanceToNextPlanetY/distanceToNextPlanet;

            if (distanceToNextPlanet > 10){
                this.velocityX = directionX*MAX_SPEED;
                this.velocityY = directionY*MAX_SPEED;
                this.coordX += this.velocityX;
                this.coordY += this.velocityY;
            }else {
                this.velocityX = 0;
                this.velocityY = 0;
                counter++;
            }

        }

    }

    public void setPathToTake(ArrayList<Integer> path){
        this.path_to_take = path;
    }
    public void unsetPathToTake(){
        this.path_to_take = null;
    }
    public ArrayList<Integer> getPathToTake(){
        return this.path_to_take;
    }

    public void calculatePath(int nb_connections, ArrayList<ArrayList<Integer>> list_connections){
        System.out.println(BFS.calculateShortestPath(nb_connections, this.current_planet.id, this.target_planet.id, list_connections));
    }

    public Planet getCurrent_planet() {
        return current_planet;
    }

    public Planet getTarget_planet() {
        return target_planet;
    }
}
