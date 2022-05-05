package game.example.testminirocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

import java.util.ArrayList;

public class Planet extends GameObject{
    public int id; // id unique de la planète
    public double radius; // Rayon de la planète
    private Context context;
    private String infos; // infos sur la planète
    private Paint paint;

    private ArrayList<Traveller> list_travellers = new ArrayList<Traveller>(); // Liste des voyageurs sur la planète
    private ArrayList<Planet> list_of_arr_planets = new ArrayList<Planet>(); // Liste des voyageurs sur la planète


    public Trajectory my_trajectory; // La trajectoire associée à cette planète
    public Planet linkedPlanet = null;// La planète d'arr associée à cette planète

    // Constructeur
    public Planet(Context context, int id, double coordX, double coordY, double radius, String infos, Trajectory my_trajectory, ArrayList<Traveller> list_travellers){
        super(coordX, coordY);
        this.id = id;
        this.coordX = coordX;
        this.coordY = coordY;
        this.radius = radius;
        this.my_trajectory = my_trajectory;
        this.context = context;
        this.infos = infos;
        this.paint = paint;

        this.list_travellers = list_travellers;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.planet_color);
        paint.setColor(color);

    }
     // Affichage
    public void draw(Canvas canvas) {
        canvas.drawCircle((float)coordX, (float)coordY, (float)radius, paint);
        this.my_trajectory.draw(canvas);
    }
    // pour changer la planète de position
    public void changePosition(double positionX, double positionY){
        int color = ContextCompat.getColor(context, R.color.purple_200);
        this.paint.setColor(color);
        this.coordX = positionX;
        this.coordY = positionY;
    }
    // Pour set la trajectoire de sortie de cette planète
    public void setMyTrajectory(Trajectory my_trajectory){
        this.my_trajectory = my_trajectory;
    }
    // Pour unset la trajectoire de sortie de cette planète
    public void unsetMyTrajectory(){
        this.my_trajectory = new Trajectory(-1, 0,0,0,0, null, null);
    }
    // Pour set la planète associée de cette planète
    public void setLinkedPlanet(Planet linkedPlanet){
        this.linkedPlanet = linkedPlanet;
    }
    // Pour unset la planète associée de cette planète
    public void unsetLinkedPlanet(){
        this.linkedPlanet = null;
    }
    // Pour savoir si cette planète est associée à une planète
    public boolean isLinkedWithPlanet(){
        return this.linkedPlanet != null;
    }
    public ArrayList<Planet> getListOfArrPlanets(){
        return this.list_of_arr_planets;
    }
    //update
    public void update() {
        //my_trajectory.update();
    }

}
