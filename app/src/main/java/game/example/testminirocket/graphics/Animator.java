package game.example.testminirocket.graphics;

import android.graphics.Canvas;
import android.util.Log;

import game.example.testminirocket.GameObjects.Asteroid;
import game.example.testminirocket.GameObjects.Planet;

public class Animator {

    private static final int MAX_UPDATE_BEFORE_NEXT_MOVE_FRAME = 5;
    private Sprite[] spriteArray;
    private int idxNotMovingFrame = 0;
    private int idxMovingFrame = 1;
    private int updateBeforeNextMoveFrame = 5;

    public Animator(Sprite[] spriteArray){
        this.spriteArray = spriteArray;
    }

    public void drawPlanet(Canvas canvas, Planet planet) {
        switch (planet.getPlanetState().getState()){
            case NOT_MOVING:
                drawFramePlanet(canvas, planet, spriteArray[idxNotMovingFrame]);
                break;
            case IS_MOVING:
                updateBeforeNextMoveFrame--;
                if (updateBeforeNextMoveFrame == 0){
                    updateBeforeNextMoveFrame = MAX_UPDATE_BEFORE_NEXT_MOVE_FRAME;
                    toggleIdxNotMovingFrame();
                }
                drawFramePlanet(canvas, planet, spriteArray[idxMovingFrame]);
                break;
            default:
                break;
        }
    }

    public void drawAsteroid(Canvas canvas, Asteroid asteroid) {
        switch (asteroid.getAsteroidState().getState()){
            case NOT_MOVING:
                drawFrameAsteroid(canvas, asteroid, spriteArray[idxNotMovingFrame]);
                break;
            case IS_MOVING:
                updateBeforeNextMoveFrame--;
                if (updateBeforeNextMoveFrame == 0){
                    updateBeforeNextMoveFrame = MAX_UPDATE_BEFORE_NEXT_MOVE_FRAME;
                    toggleIdxNotMovingFrame();
                }
                drawFrameAsteroid(canvas, asteroid, spriteArray[idxMovingFrame]);
                break;
            default:
                break;
        }
    }

    private void toggleIdxNotMovingFrame() {
        if (idxMovingFrame == 49)
            idxMovingFrame = 0;
        else
            idxMovingFrame++;
    }

    public void drawFramePlanet(Canvas canvas, Planet planet, Sprite sprite){
        sprite.draw(canvas, (int)planet.getPositionX() - sprite.getWidth() / 2, (int)planet.getPositionY() - sprite.getHeight() / 2);
    }
    public void drawFrameAsteroid(Canvas canvas, Asteroid asteroid, Sprite sprite){
        sprite.draw(canvas, (int)asteroid.getPositionX() - sprite.getWidth() / 2, (int)asteroid.getPositionY() - sprite.getHeight() / 2);
    }
}
