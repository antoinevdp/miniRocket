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

    private double velocityX; // vitesse en x
    private double velocityY; // vitesse en y

    public boolean canGo; // Boolean pour avancer ou non
    public boolean canBeDestroyed;

    private Planet current_planet; // Planète actuelle
    private Planet target_planet; // Planète où l'on veut aller
    private double max_time; // temps de vie maximum
    private double time_remaining; // temps restant avant mort
    private final Paint paint;

    private int counter = 1; // Counter pour arriver jusqu'à la planète souhaitée

    private ArrayList<Planet> listAllPlanets; // Liste de toutes les planètes du niveau

    private ArrayList<Integer> path_to_take; // Chemin que doit prendre le traveller pour arriver jusqu'à sa planète cible
    //Constructor
    public Traveller(Context context, double coordX, double coordY, Planet current_planet, Planet target_planet, ArrayList<Planet> listAllPlanets) {
        super(coordX, coordY);
        this.current_planet = current_planet;
        this.target_planet = target_planet;
        this.listAllPlanets = listAllPlanets;
        this.max_time = max_time;
        this.canGo = false;
        this.canBeDestroyed = false;
        this.velocityX = 0;
        this.velocityY = 0;

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
        paint.setColor(this.target_planet.getRandomAndroidColor());
    }
    // Affichage
    public void draw(Canvas canvas) {
        canvas.drawCircle((float)this.coordX, (float)this.coordY, 10, paint);
    }
    // Update
    public void update(){
        if (this.path_to_take != null){ // Si un chemin a été trouvé
            if (this.path_to_take.size() == counter){ // Si le traveller est arrivé à destination
                this.canGo = false; // on n'avance plus
                this.canBeDestroyed = true;
                unsetPathToTake(); // Il n'y a plus de chemin à prendre

            }else { // Si le traveller n'est pas encore arrivé
                this.canGo = true; // Il peut voyager
                this.target_planet = this.listAllPlanets.get(this.path_to_take.get(counter)); // Sa planète cible est la planète suivante dans la liste des planètes du chemin à prendre
            }

        }
        if (this.canGo){ // Si il peut voyager
            double distanceToNextPlanetX = this.target_planet.getPositionX() - this.coordX; // On calcule la distance x entre sa position et la planète
            double distanceToNextPlanetY = this.target_planet.getPositionY() - this.coordY; // On calcule la distance y entre sa position et la planète

            double distanceToNextPlanet = GameObject.getDistanceBetweenObjects(this, this.target_planet); // on calcule la distance entre la planète target et le traveller

            double directionX = distanceToNextPlanetX/distanceToNextPlanet; // on calcule la direction à prendre en x
            double directionY = distanceToNextPlanetY/distanceToNextPlanet; // on calcule la direction à prendre en y

            if (distanceToNextPlanet > 10){ // Si la distance entre le traveller et la planète cible est > 10
                // on fait avancer dans la direction
                this.velocityX = directionX*MAX_SPEED;
                this.velocityY = directionY*MAX_SPEED;
                this.coordX += this.velocityX;
                this.coordY += this.velocityY;
            }else { // Sinon, on est arrivé à la planète cible
                this.velocityX = 0;
                this.velocityY = 0;
                counter++; // on incrémente le compteur de planète pour le chemin
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
