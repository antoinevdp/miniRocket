package game.example.testminirocket;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceView;
import android.view.View;

public class SelectLevel extends SurfaceView {

    Paint paint = new Paint();

    public SelectLevel(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10F);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startX = 20;
        float startY = 100;
        float stopX = 140;
        float stopY = 30;

        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }
}
