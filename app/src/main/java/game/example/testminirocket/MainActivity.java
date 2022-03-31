package game.example.testminirocket;

import android.app.Activity;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Button;


import com.minirocket.game.R;

public class MainActivity extends Activity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_LONG).show();

        button = (Button) findViewById(R.id.btn_play);


        
    }

}