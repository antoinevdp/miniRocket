package game.example.testminirocket;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

public class Trajectory {
    private int id;
    private double startPosX;
    private double startPosY;
    private double endPosX;
    private double endPosY;
    private Planet startPlanet;
    private Planet endPlanet;
    private final Paint paint = new Paint();

    public Trajectory(int id, double startPosX, double startPosY, double endPosX, double endPosY, Planet startPlanet, Planet endPlanet) {
        this.id = id;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.endPosX = endPosX;
        this.endPosY = endPosY;
        this.startPlanet = startPlanet;
        this.endPlanet = endPlanet;

        paint.setColor(Color.GREEN);
    }

    public void draw(Canvas canvas) {
        canvas.drawLine((float)startPosX, (float)startPosY, (float)endPosX, (float)endPosY, paint);
    }

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
        this.endPlanet = endPlanet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
