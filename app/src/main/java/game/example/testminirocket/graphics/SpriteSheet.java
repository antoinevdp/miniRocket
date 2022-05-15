package game.example.testminirocket.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.minirocket.game.R;


// Responsible for returning correct sprite
public class SpriteSheet {
    private Bitmap bitmap;
    private Context context;
    private int counter = 0;
    BitmapFactory.Options bitmapOptions;

    public SpriteSheet(Context context, int index, int[] spriteList){
        this.context = context;
        bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        Log.d("index", String.valueOf(index));
        for (int i = 0; i < spriteList.length; i++) {
            if (index == i){
                Log.d("found", String.valueOf(i));
                bitmap = BitmapFactory.decodeResource(this.context.getResources(), spriteList[i], bitmapOptions);
                return;
            }
        }
        bitmap = BitmapFactory.decodeResource(this.context.getResources(), spriteList[0], bitmapOptions);

    }

    public Sprite[] getPlanetSpriteArray(){
        Sprite[] spriteArray = new Sprite[50];
        for (int i = 0; i < spriteArray.length; i++) {
            spriteArray[i] = new Sprite(this, new Rect(i*100, 0, (i+1)*100, 100));
        }


        return spriteArray;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
