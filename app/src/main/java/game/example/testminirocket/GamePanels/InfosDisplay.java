package game.example.testminirocket.GamePanels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import game.example.testminirocket.GameObjects.GameObject;

public class InfosDisplay extends GameObject {

    private Paint paint;

    private int nb_trajectories;
    private int amelioration_traj;
    private int score;


    public InfosDisplay(Context context, double coordX, double coordY) {
        super(coordX, coordY);
        this.coordX = coordX;
        this.coordY = coordY;
        this.nb_trajectories = 0;
        this.score = 0;
        this.amelioration_traj = 0;

        this.paint = new Paint();

        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(30);
    }

    public void setNb_trajectories(int nb_trajectories){
        this.nb_trajectories = nb_trajectories;
    }
    public void setScore(int score){
        this.score = score;
    }


    public void draw(Canvas canvas){


        canvas.drawText("Score : " + this.score, (float)this.coordX, (float)this.coordY, this.paint);
        canvas.drawText("Trajectoires : " + this.nb_trajectories, (float)this.coordX, (float)this.coordY + 50, this.paint);
        canvas.drawText("Ameliorations Traj: " + this.amelioration_traj, (float)this.coordX, (float)this.coordY + 100, this.paint);


    }

    public void update() {

    }

}
