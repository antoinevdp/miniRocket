package game.example.testminirocket.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import game.example.testminirocket.GameObjects.GameObject;

public class ColorSelect extends GameObject {
    public int id;
    public double radius;

    private Paint paint;
    private int randomAndroidColor;

    public ColorSelect(double coordX, double coordY, int id, double radius, int randomAndroidColor) {
        super(coordX, coordY);
        this.id = id;
        this.radius = radius;

        this.paint = new Paint();
        this.paint.setColor(Color.GREEN);
    }

    // Affichage
    public void draw(Canvas canvas) {
        canvas.drawCircle((float)coordX, (float)coordY, (float)radius, paint);
    }
}
