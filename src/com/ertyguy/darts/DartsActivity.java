package com.ertyguy.darts;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.opengles.GL;

import com.ertyguy.darts.DartsEngine;
import com.ertyguy.darts.DartsGameView;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class DartsActivity extends Activity {
	//private DartsGameView gameView;
	//private float startx=0f,starty=0f;
	private MatrixGrabber mg = new MatrixGrabber();
	private int touchtime;
	//Calendar c = Calendar.getInstance(); 
	Date now = new Date();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DartsEngine.display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);   
        DartsEngine.gameView = new DartsGameView(this);
        setContentView(DartsEngine.gameView);
        
        DartsEngine.gameView.setGLWrapper(new GLSurfaceView.GLWrapper() //To use Matrix Grabber
	    {
	        public GL wrap(GL gl)
	        {
	            return new MatrixTrackingGL(gl);
	        }
	    });
        
        DartsEngine.context = this;
        
        //DartsEngine.gameView = gameView;
    }

    @Override
    protected void onResume() {
	    super.onResume();
	    DartsEngine.gameView.onResume();
    }
    @Override
    protected void onPause() {
	    super.onPause();
	    DartsEngine.gameView.onPause();
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	float x = event.getX();
    	float y = event.getY();
    	
    	
    	//x = (x-DartsEngine.display.getWidth() / 2);
    	y = (DartsEngine.display.getHeight()-y);
    	switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				DartsEngine.startx = x;
				DartsEngine.starty = y;
				DartsEngine.pressed = true;
				DartsEngine.x = (x);//-DartsEngine.display.getWidth() / 2;
				DartsEngine.y = (y);
				
				now = new Date();
				DartsEngine.distance = 1;
				break;
				
			case MotionEvent.ACTION_MOVE:
				
				DartsEngine.x = (x);//-DartsEngine.display.getWidth() / 2;
				DartsEngine.y = (y);
				break;
				
			case MotionEvent.ACTION_UP:
				Date releasetime = new Date();
				
				DartsEngine.touchtime = (int) (releasetime.getTime() - now.getTime()) / 10 ;//c.get(Calendar.SECOND) - touchtime;

				DartsEngine.pressed = false;
				//DartsEngine.throwx = (x);
				//DartsEngine.throwy = (- y);
				break;
				
    	}
    	return false;
    }
}
