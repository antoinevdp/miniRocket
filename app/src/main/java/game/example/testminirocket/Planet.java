package game.example.testminirocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

import java.util.ArrayList;

public class Planet{
    private double positionX;
    private double positionY;
    public double radius;
    private Context context;
    private String infos; // infos sur la planète
    private Paint paint;

    private ArrayList<Traveller> list_travellers = new ArrayList<Traveller>(); // Liste des voyageurs sur la planète

    public Trajectory my_trajectory; // La trajectoire associée à cette planète
    public Planet linkedPlanet = null;// La planète d'arr associée à cette planète

    // Constructeur
    public Planet(Context context, double positionX, double positionY, double radius, String infos, Trajectory my_trajectory, ArrayList<Traveller> list_travellers){
        this.positionX = positionX;
        this.positionY = positionY;
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

    public void draw(Canvas canvas) {
        canvas.drawCircle((float)positionX, (float)positionY, (float)radius, paint);
        this.my_trajectory.draw(canvas);
    }
    // pour changer la planète de position
    public void changePosition(double positionX, double positionY){
        int color = ContextCompat.getColor(context, R.color.purple_200);
        this.paint.setColor(color);
        this.positionX = positionX;
        this.positionY = positionY;
    }
    // Pour set la trajectoire de sortie de cette planète
    public void setMyTrajectory(Trajectory my_trajectory){
        this.my_trajectory = my_trajectory;
    }
    // Pour unset la trajectoire de sortie de cette planète
    public void unsetMyTrajectory(){
        this.my_trajectory = new Trajectory(0,0,0,0);
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
    // Récuperer la position
    public float getPositionX(){
        return (float)(this.positionX);
    }
    public float getPositionY(){
        return (float)(this.positionY);
    }
    //update
    public void update() {
        //my_trajectory.update();
    }

}
