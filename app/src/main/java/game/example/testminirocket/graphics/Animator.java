package game.example.testminirocket.graphics;

import android.graphics.Canvas;
import android.util.Log;

import game.example.testminirocket.GameObjects.Planet;

public class Animator {

    private static final int MAX_UPDATE_BEFORE_NEXT_MOVE_FRAME = 5;
    private Sprite[] playerSpriteArray;
    private int idxNotMovingFrame = 0;
    private int idxMovingFrame = 1;
    private int updateBeforeNextMoveFrame = 5;

    public Animator(Sprite[] playerSpriteArray){
        this.playerSpriteArray = playerSpriteArray;
    }

    public void draw(Canvas canvas, Planet planet) {
        switch (planet.getPlanetState().getState()){
            case NOT_MOVING:
                drawFrame(canvas, planet, playerSpriteArray[idxNotMovingFrame]);
                break;
            case IS_MOVING:
                updateBeforeNextMoveFrame--;
                if (updateBeforeNextMoveFrame == 0){
                    updateBeforeNextMoveFrame = MAX_UPDATE_BEFORE_NEXT_MOVE_FRAME;
                    toggleIdxNotMovingFrame();
                }
                drawFrame(canvas, planet, playerSpriteArray[idxMovingFrame]);
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

    public void drawFrame(Canvas canvas, Planet planet, Sprite sprite){
        sprite.draw(canvas, (int)planet.getPositionX() - sprite.getWidth() / 2, (int)planet.getPositionY() - sprite.getHeight() / 2);
    }
}
