package game.example.testminirocket.GameObjects;

import android.content.Context;
import android.graphics.Canvas;

import game.example.testminirocket.graphics.Animator;
import game.example.testminirocket.graphics.Sprite;

public class Asteroid extends GameObject{
    private Trajectory target_trajectory;
    private float level;
    private Animator animator;

    public double velocityX; // vitesse en x
    public double velocityY; // vitesse en y

    public Asteroid(Context context, double coordX, double coordY, Trajectory target_trajectory, float level, Animator animator) {
        super(coordX, coordY);
        this.target_trajectory = target_trajectory;
        this.level = level;
        this.animator = animator;
    }

    public void update() {
    }

    public void draw(Canvas canvas) {
        //sprite.draw(canvas, (int)getPositionX() - sprite.getWidth() / 2, (int)getPositionY() - sprite.getHeight() / 2);
    }
}
