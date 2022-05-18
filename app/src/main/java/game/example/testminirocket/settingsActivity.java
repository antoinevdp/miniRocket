package game.example.testminirocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.minirocket.game.R;

public class settingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ImageButton button = (ImageButton) findViewById(R.id.returnButton);
        ImageButton trueFasle = (ImageButton) findViewById(R.id.trueFalse);
        trueFasle.setTag(R.drawable.ic_resource_true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        trueFasle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Integer) trueFasle.getTag()) == R.drawable.ic_resource_true){
                    trueFasle.setTag(R.drawable.ic_false);
                    trueFasle.setImageResource(R.drawable.ic_false);
                }
                else if(((Integer) trueFasle.getTag()) == R.drawable.ic_false){
                    trueFasle.setTag(R.drawable.ic_resource_true);
                    trueFasle.setImageResource(R.drawable.ic_resource_true);
                }

            }
        });



    }

    //no transition between activity
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
