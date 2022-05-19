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
    private Paint paintStroke;
    private int randomAndroidColor;

    public boolean colorIsSelected = false;
    public boolean isUsed = false;

    public ColorSelect(double coordX, double coordY, int id, double radius, int randomAndroidColor) {
        super(coordX, coordY);
        this.id = id;
        this.radius = radius;

        this.paintStroke = new Paint();
        this.paint = new Paint();
        this.paint.setColor(randomAndroidColor);
    }

    // Affichage
    public void draw(Canvas canvas) {
        if(!this.colorIsSelected){
            canvas.drawCircle((float)coordX, (float)coordY, (float)radius, paint);
        }else {
            this.paintStroke.setStyle(Paint.Style.STROKE);
            this.paintStroke.setStrokeWidth(3f);
            this.paintStroke.setColor(Color.WHITE);
            canvas.drawCircle((float)coordX, (float)coordY, (float)radius+5, this.paintStroke);
            canvas.drawCircle((float)coordX, (float)coordY, (float)radius, paint);
        }
    }


    public void IsSelected(boolean isSelected){
        this.colorIsSelected = isSelected;
    }
    public void setIsUsed(boolean isUsed){
        this.isUsed = isUsed;
    }
    public boolean getIsUsed(){
        return this.isUsed;
    }

    public int getColor() {
        return this.randomAndroidColor;
    }
}
