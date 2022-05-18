package game.example.testminirocket.graphics;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Sprite {

    private final SpriteSheet spriteSheet;
    private final Rect rect;
    private Matrix matrix;
    private float angle;

    public Sprite(SpriteSheet spriteSheet, Rect rect, float angle) {
        this.spriteSheet = spriteSheet;
        this.rect = rect;
        matrix = new Matrix();
        this.angle = angle;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(
                spriteSheet.getBitmap(),
                rect,
                new Rect(x, y, x + 100, y + 100),
                null
        );
    }

    public int getWidth() {
        return rect.width();
    }
    public int getHeight() {
        return rect.height();
    }
}
