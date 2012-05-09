package se.chalmers.labyrinth;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
/*
 * Vissa delar av den här koden är tagna från Android developers:
 * http://developer.android.com/resources/samples/AccelerometerPlay/
 * src/com/example/android/accelerometerplay/AccelerometerPlayActivity.html
 * 
 * Dessa är markerade med en kommentar innan.
 * 
 */

public class Game extends Activity {
	private GameView gameView;
    private SensorManager sensorManager;
    private WindowManager windowManager;
    private Display display;
    private Sensor accelerometer;
    private PowerManager powerManager;
    private WakeLock wakeLock;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // För att använda sensorerna (accelerometern i vårt fall)
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        
        // För att hålla skärmen igång konstant (Tagen från Android developers)
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        
        
        gameView = new GameView(this);
        setContentView(gameView);
    }
    
    
    // Den här funktionen är tagen från Android developers
    // och modifierad för att fungera med våran applikation.
    @Override
    protected void onResume() {
    	super.onResume();
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
    	wakeLock.acquire();
    	// Start the simulation
    	gameView.startGame();
    }
    
    // Den här funktionen är tagen från Android developers
    // och modifierad för att fungera med våran applikation.
    @Override
    protected void onPause() {
    	super.onPause();
        /*
         * When the activity is paused, we make sure to stop the simulation,
         * release our sensor resources and wake locks
         */

        // Stop the simulation
    	gameView.stopGame();
    	
    	// and release our wake-lock
    	wakeLock.release();
    }
    
    
    class GameView extends View implements SensorEventListener {
        private Ball ball;
        private Hole finalHole;
        private ArrayList<Wall> walls;
        private ArrayList<Hole> sinkHoles;
        private float sensorX;
        private float sensorY;
        
        
        public GameView(Context context) {
            super(context);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            
            // Initiera bollen, alla väggar samt finalHole
            ball = new Ball(60f, 60f, 20f, Color.RED);
            walls = new ArrayList<Wall>();
            finalHole = new Hole(30f, Color.BLUE,300f,600f);
            sinkHoles = new ArrayList<Hole>();
            initiateBoard();
        }
        
        // Den här funktionen är tagen från Android developers
        // och modifierad för att fungera med våran applikation.
        public void startGame() {
        	sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        
        //Den här funktionen är rakt tagen från Android developers.
        public void stopGame() {
        	sensorManager.unregisterListener(this);
        	Log.d("SENSOR_MANAGER", "Sensor shall now be turned off");	// Vår kod, till för debugging.
        }
        
        private void initiateBoard() {
            int wallColor = Color.GRAY;
            
            // Kanter
            walls.add(new Wall(20f, 400f, 220f, 420f, wallColor));
            walls.add(new Wall(220f, 100f, 240f, 420f, wallColor));
            
            // Yttre kanterna (för tillfället inställda efter Galaxy S & S2)
            walls.add(new Wall(0f, 780f, 480f, 800f, wallColor));
            walls.add(new Wall(0f, 0, 480f, 20, wallColor));
            walls.add(new Wall(460f, 0f, 480f, 800f, wallColor));
            walls.add(new Wall(0f, 0f, 20f, 800f, wallColor));
            
            //Alla sjunkhål
            sinkHoles.add(new Hole(30f,Color.BLACK,300f,150f));
            sinkHoles.add(new Hole(30f,Color.BLACK,150f,150f));
        }
       
        
        //Den här funktionen är rakt tagen från Android developers.
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
        }
        
        public void onAccuracyChanged(Sensor arg0, int arg1) {
            // TODO Auto-generated method stub
        }
        
        private void checkCollisions(float sensX, float sensY) {
            
        	float ballPosX = ball.getPosX();
            float ballPosY = ball.getPosY();
            float ballRadius = ball.getRadius();
            
            float ballOffsetTop = ballPosX + ballRadius;
            float ballOffsetDown = ballPosX - ballRadius;
            float ballOffsetLeft = ballPosY - ballRadius;
            float ballOffsetRight = ballPosY + ballRadius;
            
            float updPosX = sensX;
            float updPosY = sensY;
            
            //Beräkna för varje hål dess position samt om bollen ramlar ned
            for(Hole hole : sinkHoles){
            	float holePosX = hole.getPosX();
                float holePosY = hole.getPosY();
                float holeRadius = hole.getRadius();
                
                float holeOffsetTop = holePosX + holeRadius;
                float holeOffsetDown = holePosX - holeRadius;
                float holeOffsetLeft = holePosY - holeRadius;
                float holeOffsetRight = holePosY + holeRadius;
 
                //Kollar ifall bollen ramlar ned i sjunkhålet genom att matcha
                //bollens koordinater med hålens offset.
                //Om bollen är till hälften i hålet eller mer räknas den som inne.
                if (ballPosY <= holeOffsetRight && ballPosY >= holeOffsetLeft) {
                	if (ballPosX <= holeOffsetTop && ballPosX >= holeOffsetDown) {
                		//Om bollen ramlar ned; flytta bollen
                		ball.setPosX(400);
                		ball.setPosY(400);
                	}
                }
            }
            //Räkna ut offset för finalHole
            float finalHoleOffsetTop = finalHole.getPosX() + finalHole.getRadius();
            float finalHoleOffsetDown = finalHole.getPosX() - finalHole.getRadius();
            float finalHoleOffsetLeft = finalHole.getPosY() - finalHole.getRadius();
            float finalHoleOffsetRight = finalHole.getPosY() + finalHole.getRadius();
            
            //Kollar ifall bollen ramlar ned i finalHole
            if (ballPosY <= finalHoleOffsetRight && ballPosY >= finalHoleOffsetLeft) {
            	if (ballPosX <= finalHoleOffsetTop && ballPosX >= finalHoleOffsetDown) {
            		//Om bollen ramlar ned; flytta bollen
            		ball.setPosX(400);
            		ball.setPosY(400);
            	}
            }
            
            // Kolla kollisioner mot alla väggar
            for (Wall wall : walls) {
                float posX1 = wall.getPosX1();
                float posY1 = wall.getPosY1();
                float posX2 = wall.getPosX2();
                float posY2 = wall.getPosY2();
                
                // Kollar X-delen av väggen
                if (ballPosY >= posY1 && ballPosY <= posY2) {
                    // Om den träffar mot undersidan av bollen
                    if (ballOffsetDown > posX1 && ballOffsetDown < posX2) {
                        // Kolla om sensorn är åt motsatt håll jämfört med bollen.
                        // För att kunna "släppa den" från väggen.
                        if (sensX < 0) {
                            updPosX = sensX;
                        } else {
                            updPosX = 0;
                        }
                    // Om den träffar mot ovansidan av bollen
                    } else if (ballOffsetTop > posX1 && ballOffsetTop < posX2) {
                        if (sensX > 0) {
                            // Kolla om sensorn är åt motsatt håll jämfört med bollen.
                            // För att kunna "släppa den" från väggen.
                            updPosX = sensX;
                        } else {
                            updPosX = 0;
                        }
                    }
                }
                
                if (ballPosX > posX1 && ballPosX < posX2) {
                    // Om den träffar mot vänstersidan av bollen
                    if (ballOffsetLeft >= posY1 && ballOffsetLeft <= posY2) {
                        if (sensY > 0) {
                            // Kolla om sensorn är åt motsatt håll jämfört med bollen.
                            // För att kunna "släppa den" från väggen.
                            updPosY = sensY;
                        } else {
                            updPosY = 0;
                        }
                    // Om den träffar mot högersidan av bollen
                    } else if (ballOffsetRight >= posY1 && ballOffsetRight <= posY2) {
                        if (sensY < 0) {
                            // Kolla om sensorn är åt motsatt håll jämfört med bollen.
                            // För att kunna "släppa den" från väggen.
                            updPosY = sensY;
                        } else {
                            updPosY = 0;
                        }
                    }
                }
            }
            
            

            ball.updatePosition(updPosX, updPosY);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            final float sensX = sensorX;
            final float sensY = sensorY;
            Paint paint = new Paint();
            
            // Kolla kollisioner
            checkCollisions(sensX, sensY);
            
            // Sätt bakgrunden
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.DKGRAY);
            canvas.drawPaint(paint);
            
            // Rita upp alla väggar
            for(Wall wall : walls) {
                paint.setColor(wall.getColor());
                canvas.drawRect(wall.getPosX1(), wall.getPosY1(), wall.getPosX2(), wall.getPosY2(), paint);
            }
            
            //Rita upp hålen
            for(Hole hole : sinkHoles){
            	paint.setColor(hole.getColor());
            	paint.setAntiAlias(true);
            	canvas.drawCircle(hole.getPosX(), hole.getPosY(), hole.getRadius(), paint);
            	
            }
            
            //Rita ut finalHole
            paint.setColor(finalHole.getColor());
            paint.setAntiAlias(true);
            canvas.drawCircle(finalHole.getPosX(), finalHole.getPosY(), finalHole.getRadius(), paint);
            
            // Hämta värdena för bollen
            final float posX = ball.getPosX();
            final float posY = ball.getPosY();
            final float radius = ball.getRadius();
            final int color = ball.getColor();
            
            // Rita upp bollen
            paint.setColor(color);
            paint.setAntiAlias(true);
            canvas.drawCircle(posX, posY, radius, paint);
        
            // Rita om allt igen
            invalidate();
        }
    }
}
