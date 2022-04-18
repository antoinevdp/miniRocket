package game.example.testminirocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

public class Traveller extends GameObject{
    private static final double SPEED_PIXELS_PER_SECOND = 400;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    private double coordX;
    private double coordY;
    private double velocityX;
    private double velocityY;

    public boolean canGo;

    private final Planet current_planet;
    private final Planet target_planet;
    private double max_time;
    private double time_remaining;
    private final Paint paint;

    public Traveller(Context context, double coordX, double coordY, Planet current_planet, Planet target_planet) {
        super(coordX, coordY);
        this.current_planet = current_planet;
        this.target_planet = target_planet;
        this.max_time = max_time;
        this.canGo = true;
        this.velocityX = 0;
        this.velocityY = 0;

        int planetX = (int)current_planet.getPositionX();
        int planetY = (int)current_planet.getPositionY();
        int spawnRadius = (int)current_planet.radius-20;
        double coordX_rand = Math.floor(Math.random()*((planetX+spawnRadius)-(planetX-spawnRadius)+1)+(planetX-spawnRadius));
        double coordY_rand = (int)Math.floor(Math.random()*((planetY+spawnRadius)-(planetY-spawnRadius)+1)+(planetY-spawnRadius));
        this.coordX = coordX_rand;
        this.coordY = coordY_rand;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.traveller_color);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float)this.coordX, (float)this.coordY, 10, paint);
    }

    public void update(){
        if (this.canGo){
            double distanceToNextPlanetX = this.target_planet.getPositionX() - this.coordX;
            double distanceToNextPlanetY = this.target_planet.getPositionY() - this.coordY;

            double distanceToNextPlanet = GameObject.getDistanceBetweenObjects(this, this.target_planet);

            double directionX = distanceToNextPlanetX/distanceToNextPlanet;
            double directionY = distanceToNextPlanetY/distanceToNextPlanet;

            if (distanceToNextPlanet > 0){
                this.velocityX = directionX*MAX_SPEED;
                this.velocityY = directionY*MAX_SPEED;
            }else {
                this.velocityX = 0;
                this.velocityY = 0;
            }

            this.coordX += this.velocityX;
            this.coordY += this.velocityY;

        }

    }
}
