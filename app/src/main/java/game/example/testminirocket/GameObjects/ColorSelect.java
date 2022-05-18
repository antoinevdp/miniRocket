package game.example.testminirocket.GameObjects;

import android.graphics.Canvas;
import android.graphics.Paint;

public class ColorSelect extends GameObject{
    public int id;
    public double radius;

    private Paint paint;
    private int randomAndroidColor;

    private double coordX;
    private double coordY;

    public ColorSelect(double coordX, double coordY, int id, double radius, int randomAndroidColor) {
        super(coordX, coordY);
        this.id = id;
        this.radius = radius;

        this.paint = new Paint();
        this.paint.setColor(this.randomAndroidColor);
    }

    // Affichage
    public void draw(Canvas canvas) {
        canvas.drawCircle((float)coordX, (float)coordY, (float)radius, paint);
    }
}
