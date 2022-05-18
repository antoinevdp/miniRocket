package game.example.testminirocket.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

public class Trajectory extends GameObject{
    private int id; // Id unique de la trajectoire
    private double startPosX; // Position de départ X
    private double startPosY; // Position de départ y
    private double endPosX; // Position de fin X
    private double endPosY; // Position de fin Y
    private Planet startPlanet; // Planète de départ
    private Planet endPlanet; // Planète de fin
    private int familly;
    private final Paint paint = new Paint();

    public ArrayList<SpaceShip> list_spaceShips = new ArrayList<>(); // liste des Trajets entre planètes


    // Constructeur
    public Trajectory(int id, double startPosX, double startPosY, double endPosX, double endPosY, Planet startPlanet, Planet endPlanet, int familly) {
        super(startPosX, startPosY);
        this.id = id;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.endPosX = endPosX;
        this.endPosY = endPosY;
        this.startPlanet = startPlanet;
        this.endPlanet = endPlanet;
        this.list_spaceShips = null;
        this.familly = familly;

        paint.setColor(Color.GREEN);
    }
    // Affichage
    public void draw(Canvas canvas) {
        canvas.drawLine((float)startPosX, (float)startPosY, (float)endPosX, (float)endPosY, paint);
    }
    // Update
    public void update() {


    }
    public void setStartPosition(double startPosX, double startPosY) {
        this.startPosX = startPosX;
        this.startPosY = startPosY;
    }
    public void setEndPosition(double endPosX, double endPosY) {
        this.endPosX = endPosX;
        this.endPosY = endPosY;
    }

    public void reset() {
        this.startPosX = 0;
        this.startPosY = 0;
        this.endPosX = 0;
        this.endPosY = 0;
    }

    public void resetToCursor(float x, float y) {
        this.startPosX = x;
        this.startPosY = y;
        this.endPosX = x;
        this.endPosY = y;
    }

    public Planet getStartPlanet() {
        return startPlanet;
    }

    public void setStartPlanet(Planet startPlanet) {
        this.startPlanet = startPlanet;
    }

    public Planet getEndPlanet() {
        return endPlanet;
    }

    public void setEndPlanet(Planet endPlanet) {
        this.coordX = getPosMidX();
        this.coordY = getPosMidY();
        this.endPlanet = endPlanet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPosMidX(){
        return Math.min(startPosX, endPosX) + (Math.max(startPosX, endPosX) - Math.min(startPosX, endPosX)) / 2;
    }
    public double getPosMidY(){
        return Math.min(startPosY, endPosY) + (Math.max(startPosY, endPosY) - Math.min(startPosY, endPosY)) / 2;
    }

    public void addSpaceShip(SpaceShip spaceShip){
        this.list_spaceShips.add(spaceShip);
    }

    public int getFamilly() {
        return familly;
    }
    public void setFamilly(int familly) {
        Log.d("familly", String.valueOf(familly));
        this.familly = familly;
        if (this.familly == 1)
        paint.setColor(Color.GREEN);
        else paint.setColor(Color.RED);
        Log.d("familly", String.valueOf(familly));
    }
}
