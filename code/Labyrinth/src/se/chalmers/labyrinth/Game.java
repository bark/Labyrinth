package se.chalmers.labyrinth;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class Game extends Activity {

	private SensorManager sensorManager;
	private WindowManager windowManager;
	private Display display;
	private Sensor accelerometer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // För att använda sensorerna (accelerometern i vårt fall)
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        
        
        GameView gv = new GameView(this);
        setContentView(gv);
    }
    
    
    class GameView extends View implements SensorEventListener {
    	private Bitmap bmBall;
    	private Ball ball;
    	private float sensorX;
    	private float sensorY;
    	
    	
	    public GameView(Context context) {
	        super(context);
	        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        ball = new Ball(0,0);
	        
	        
	        bmBall = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
	        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
	    }
	    
	    @Override
        protected void onDraw(Canvas canvas) {
	    	// Uppdatera bollens position med hjälp av sensorerna
	    	ball.updatePosition(sensorX, sensorY);
	    	
	    	// Hämta värdena
	    	final float posX = ball.getPosX();
	    	final float posY = ball.getPosY();
	    	
	    	// Rita ut bollen på nytt
	    	canvas.drawBitmap(bmBall, posX, posY, null);
	    
	    	// Rita om allt igen
	    	invalidate();
	    }
		
		/*
		 * Den här funktionen är rakt tagen från Android developers:
		 * http://developer.android.com/resources/samples/AccelerometerPlay/src/com/example/android/accelerometerplay/AccelerometerPlayActivity.html
		 */
		public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;
            /*
             * record the accelerometer data, the event's timestamp as well as
             * the current time. The latter is needed so we can calculate the
             * "present" time during rendering. In this application, we need to
             * take into account how the screen is rotated with respect to the
             * sensors (which always return data in a coordinate space aligned
             * to with the screen in its native orientation).
             */

            switch (display.getOrientation()) {
                case Surface.ROTATION_0:
                	sensorX = event.values[0];
                	sensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                	sensorX = -event.values[1];
                	sensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                	sensorX = -event.values[0];
                	sensorY = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                	sensorX = event.values[1];
                	sensorY = -event.values[0];
                    break;
            }
            
            Log.d("SENSOR_X", Float.toString(sensorX));
            Log.d("SENSOR_Y", Float.toString(sensorY));
		}
		
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
		}
    }
}