package game.example.testminirocket.GamePanels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

import game.example.testminirocket.GameObjects.Traveller;

public class TimeBar {

    private Traveller traveller;
    private int width, height, margin;
    private Paint borderPaint, timePaint;

    public TimeBar(Context context, Traveller traveller) {
        this.traveller = traveller;
        this.width = 50;
        this.height = 10;
        this.margin = 2;

        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.timeBarColor);
        borderPaint.setColor(borderColor);

        this.timePaint = new Paint();
        int timeColor = ContextCompat.getColor(context, R.color.timeBarColorP);
        timePaint.setColor(timeColor);
    }

    public void draw(Canvas canvas){
        float x = (float) traveller.getPositionX();
        float y = (float) traveller.getPositionY();
        float distanceToTraveller = 15;
        float timePointPercentage = (float) (traveller.getTime_remaining() / traveller.getMax_time());

        float borderLeft, borderBottom;
        borderLeft = x - this.width / 2;
        borderBottom = y - distanceToTraveller;

        float timeLeft, timeTop, timeRight, timeBottom, timeWidth, timeHeight;
        timeWidth = this.width - 2*this.margin;
        timeHeight = this.height - 2*this.margin;
        timeLeft = borderLeft + this.margin;
        timeRight = timeLeft + timeWidth*timePointPercentage;
        timeBottom = borderBottom - this.margin;
        timeTop = timeBottom + timeHeight;

        canvas.drawRect(timeLeft, timeTop, timeRight, timeBottom, timePaint);
    }
}
