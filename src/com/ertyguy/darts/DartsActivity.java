package com.ertyguy.darts;

import java.util.Date;
import javax.microedition.khronos.opengles.GL;
import com.ertyguy.darts.DartsEngine;
import com.ertyguy.darts.DartsGameView;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class DartsActivity extends Activity {

	Date now = new Date();

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DartsEngine.display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	DartsEngine.display.getSize(DartsEngine.size);
    	
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
    
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	float x = event.getX();
    	float y = event.getY();
    	
    	y = (DartsEngine.size.y-y);
    	switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				DartsEngine.startx = x;
				DartsEngine.starty = y;
				DartsEngine.pressed = true;
				DartsEngine.x = x;
				DartsEngine.y = y;
				
				now = new Date();
				DartsEngine.distance = 1;
				if(DartsEngine.selecteddart+1 < DartsEngine.maxdarts )
					DartsEngine.selecteddart ++;
				else
					DartsEngine.selecteddart = 0;
				Dart dart = new Dart();
				dart.sta = Dart.state.inhand;
				DartsEngine.darts[DartsEngine.selecteddart] = dart;
				break;
				
			case MotionEvent.ACTION_MOVE:
				
				DartsEngine.x = x;
				DartsEngine.y = y;
				break;
				
			case MotionEvent.ACTION_UP:
				Date releasetime = new Date();
				
				DartsEngine.touchtime = (int) (releasetime.getTime() - now.getTime()) / 10 ;

				DartsEngine.pressed = false;
				break;
				
    	}
    	return false;
    }
}
