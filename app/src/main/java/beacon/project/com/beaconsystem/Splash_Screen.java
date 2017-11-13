package beacon.project.com.beaconsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Dell4050 on 11/13/2017.
 */

public class Splash_Screen extends AppCompatActivity{
    private Handler handler;
    private Runnable runnable;
    private long delay_time  = 3000;
    private long time = 3000L;
    private String TAG = "Splash screen";
    private ImageView img;
    private String uri = "https://www.ximedes.com/wp-content/uploads/2017/06/iBeacon-1.jpg";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        init();
    }

    private void init(){
        try{

            getSupportActionBar().hide();
            img = findViewById(R.id.img_splash_screen);

            Picasso.with(getApplicationContext()).load(uri).into(img);

            handler = new Handler();

            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            delay_time = time;
            handler.postDelayed(runnable,delay_time);
            time = System.currentTimeMillis();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            handler.removeCallbacks(runnable);
            time = delay_time - (System.currentTimeMillis() - time);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            handler.removeCallbacks(runnable);
            time = delay_time - (System.currentTimeMillis() - time);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }
}
