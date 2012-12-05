package com.ertyguy.darts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ertyguy.darts.DartsEngine;

import android.opengl.GLES10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.FloatMath;

public class DartsGameRenderer implements Renderer{
	

	//private Dart[] darts = new Dart[DartsEngine.maxdarts];
	
	
	
	private DartZoom dartzoom = new DartZoom();
	//private Arrow arrow = new Arrow();
	private Board board = new Board();
	/*
	private float zPosition = 10f;
	private float xPosition = 0f;
	private float yPosition = 0f;
	private float zPrev = 10f;
	private float xPrev = 0f;
	private float yPrev = 0f;
	
	private float rotate = 0f;
	*/
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0 ;
	
	private boolean justletgo = false;
	private float zBoard = 50;
	private float zReach = 15;
	//private float distance = 1;
	//private float velocity = 1;
	private float maxvelocity = 10f;
	private float longthrowvelocity = 4f;
	private float throwspeed = 0.5f;
	
	private float throwdist = 0f;
	private float throwdistmax = zReach/throwspeed;

	private float linepoints[] = {0f,0f,0f,0f,0f,0f};
	private FloatBuffer linevertexBuffer;
	
	float[] vector = new float[4];
	float[] startvector = new float[4];
    
	@Override
	public void onDrawFrame(GL10 gl) {
		loopStart = System.currentTimeMillis();
		try {
			if (loopRunTime < DartsEngine.GAME_THREAD_FPS_SLEEP){
				Thread.sleep(DartsEngine.GAME_THREAD_FPS_SLEEP - loopRunTime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		
		//gl.glLoadIdentity();
		
		gl.glViewport(0, 0, DartsEngine.display.getWidth(), DartsEngine.display.getHeight());
	    gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    //gl.glFrustumf(-0.5f, 0.5f, -0.5f, 0.5f, 1, 100);
	    
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		//GLU.gluLookAt(gl, 0f, 0f, 1f,
		//				  0f, 0f, 0f, 
		//				  0f, 1f, 0f);
	    
	    ///Set Screen Dimensions
	    double fovy=65,aspect=1;
	    float zNear=0.01f,zFar=100;
	    
	    float ymax = (float) (zNear * Math.tan( fovy * Math.PI / 360.0));
	    float ymin = -ymax;
	    float xmin = (float) (ymin * aspect);
	    float xmax = (float) (ymax * aspect);
	    gl.glFrustumf(xmin, xmax, ymin, ymax, zNear, zFar);
	    
		//gl.glFrustumf(0f, 1f, 0f, 1.0f, 1, 100);
		
	    
	    
	    ///Get Touch Positions
		MatrixGrabber matrixGrabber = new MatrixGrabber();
        matrixGrabber.getCurrentModelView(gl);  
        matrixGrabber.getCurrentProjection(gl);

        boolean unprojectedNearStart = (GLU.gluUnProject(DartsEngine.startx, DartsEngine.starty, 0f, matrixGrabber.mModelView, 0, matrixGrabber.mProjection, 0, new int[]{DartsEngine.gameView.getTop(),DartsEngine.gameView.getLeft(),DartsEngine.gameView.getWidth(),DartsEngine.gameView.getHeight()}, 0, startvector, 0) == GLES10.GL_TRUE);
        boolean unprojectedNear = (GLU.gluUnProject(DartsEngine.x, DartsEngine.y, 0f, matrixGrabber.mModelView, 0, matrixGrabber.mProjection, 0, new int[]{DartsEngine.gameView.getTop(),DartsEngine.gameView.getLeft(),DartsEngine.gameView.getWidth(),DartsEngine.gameView.getHeight()}, 0, vector, 0) == GLES10.GL_TRUE);
        
        //System.out.println("vec 0"+vector[0]+"  vec 1"+vector[1]+"  vec 2"+vector[2]+"  Vector 3 "+vector[3]+" --------  ------  ----  ------");
        
        
        ///Draw Line
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        System.out.println("Drawing things");
		if(unprojectedNear) {
			for(int i=0; i< DartsEngine.maxdarts && DartsEngine.selecteddart>=0;i++ ){
				if(i <= DartsEngine.selecteddart){
					System.out.println("Drawing line "+i);
					if(i==DartsEngine.selecteddart){
						//Dart dart = new Dart();
						//DartsEngine.darts[i] = dart;
					}
					System.out.println("Drawing dart # "+i);
					
					/*if(DartsEngine.darts[i].sta == Dart.state.hidden && DartsEngine.pressed && DartsEngine.distance < zReach){
						DartsEngine.darts[i].sta = Dart.state.inhand;
					}else*/ 
					if(DartsEngine.darts[i].sta == Dart.state.inhand && !DartsEngine.pressed){//DartsEngine.darts[i].zPosition < zBoard && DartsEngine.darts[i].yPosition > DartsEngine.bottomy){
						DartsEngine.darts[i].sta = Dart.state.inflight;
					}else if(DartsEngine.darts[i].sta == Dart.state.inflight && DartsEngine.darts[i].zPosition > zBoard){
						DartsEngine.darts[i].sta = Dart.state.landed;
					}
					
					drawDart(gl, DartsEngine.darts[i]);
					System.out.println("Finished drawing dart # "+i);
				}else{
					if(DartsEngine.darts[i] != null){
						DartsEngine.darts[i].sta = Dart.state.hidden;
					}
				}
			}
		}
	    
		if(unprojectedNearStart) drawLine(gl,linepoints);
		
		drawBoard(gl);
		System.out.println("Finished drawing things");
		
		loopEnd = System.currentTimeMillis();
		loopRunTime = ((loopEnd - loopStart));
		
		
	}
	

	private void drawLine(GL10 gl, float[] points){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(points.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		linevertexBuffer = byteBuf.asFloatBuffer();
		linevertexBuffer.put(points);
		linevertexBuffer.position(0);
		
		gl.glPushMatrix();
		
		  gl.glFrontFace(GL10.GL_CCW);
          gl.glEnable(GL10.GL_CULL_FACE);
          gl.glCullFace(GL10.GL_BACK);
        
          gl.glTranslatef(0, 0, 0);
        
          gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

          gl.glVertexPointer(3, GL10.GL_FLOAT, 0, linevertexBuffer);

          gl.glLineWidth(3.0f);
          gl.glColor4f(1.0f, 1.0f, 0.5f, 1.0f);

          //Insert For loop when more than 2 points
          gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);

          gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
          gl.glDisable(GL10.GL_CULL_FACE);
        gl.glPopMatrix();
        
	}
	
	
	private void drawDart(GL10 gl, Dart dart){

		//if(DartsEngine.pressed && DartsEngine.distance < zReach){
		if(dart.sta == Dart.state.inhand){
			//int w = DartsEngine.display.getWidth();
			//int h = DartsEngine.display.getHeight();
			//Draw colourful pyramid
			//gl.glPushMatrix();
			 // gl.glTranslatef(startvector[0], -startvector[1], -5f);
			  //gl.glRotatef( (FloatMath.sin((float)(w/(w-(DartsEngine.startx-DartsEngine.x)) ))* 360)-180, 0.0f,1.0f, 0.0f);
			  //gl.glRotatef(FloatMath.cos((float) ((DartsEngine.starty-DartsEngine.y)/h / 180 * Math.PI)), 1.0f,0.0f, 0.0f);
			  //System.out.println("moving triangle : "+((DartsEngine.startx-DartsEngine.x)/w));
			  //arrow.draw(gl);
			//gl.glPopMatrix();
			//System.out.println("rotate around X: "+(FloatMath.sin((float)(w/(w-(DartsEngine.startx-DartsEngine.x)) ))* 360));
			
			dart.xPosition = vector[0]*DartsEngine.distance;//DartsEngine.x;
			dart.yPosition = -vector[1]*DartsEngine.distance;//DartsEngine.y;
			dart.zPosition = DartsEngine.distance;
			
			dart.throwdist += throwspeed;
			
			
			
			DartsEngine.distance += throwspeed;
			gl.glColor4f(1.0f,1.0f,1.0f,1f);  
			
			dart.throwx = vector[0]-startvector[0];//Previous position
			dart.throwy = vector[1]-startvector[1];
			dart.velocity = throwdistmax-dart.throwdist+(dart.throwy);
			
			linepoints = new float[] { startvector[0],startvector[1],-1f,  vector[0]*DartsEngine.distance,vector[1]*DartsEngine.distance, -DartsEngine.distance, };
			justletgo=true;
		//}else if(dart.zPosition < zBoard && dart.yPosition > DartsEngine.bottomy){
		}else if(dart.sta == Dart.state.inflight){
			//once
			if(justletgo){
				dart.velocity = throwdistmax-dart.throwdist+(dart.throwy);
						 //DartsEngine.touchtime<maxvelocity ? maxvelocity-DartsEngine.touchtime: maxvelocity;
				justletgo=false;
				dart.throwdist = 0f;
			}
			
			System.out.println("[[[[[[[[[[[[[[[["+"]]]]]]]]]] velocity= "+dart.velocity+"  and touchtime= "+DartsEngine.touchtime);
			
		
			
			dart.xPosition += (dart.throwx)*DartsEngine.gamespeed;
			dart.yPosition -= (dart.throwy)*DartsEngine.gamespeed;
			dart.zPosition += dart.velocity/10;
			
			//dart.velocity *= 0.99; // Drag
			
			if(dart.throwy > -10)
				dart.throwy -= .08f;
			
		    DartsEngine.z += .05f;
		    
		    gl.glColor4f(0.8f, 1.0f, 0.5f, 1.0f);
		//}else if(dart.zPosition > zBoard){
		}else if(dart.sta == Dart.state.landed){
			dart.velocity = 0;
			if(dart.zPosition > zBoard+2) dart.zPosition = zBoard+0.5f; //Move dart in front of board
			 gl.glColor4f(1.0f,0.0f,0.0f,1f);  
			 
			if(dart.yPosition > DartsEngine.bottomy){
				gl.glColor4f(0.0f,1.0f,0.0f,1f);  
			}
		}
		
		
		// (velocity/maxvelocity)*(360/2) - 180
		dart.rotate = (float) Math.atan2((dart.yPrev-dart.yPosition),(dart.zPrev-dart.zPosition))*100;
		System.out.println("{{{-------}}}  Rotate = "+dart.rotate);
		///// TESTING BITCHES
		/*double launchAngle = Math.tan(dart.throwy/dart.throwdist);
		
		final double DEG2RAD = Math.PI/180;
		double ang = launchAngle * DEG2RAD;
		double v0x = dart.throwvelocity * Math.cos(ang); // initial velocity in x
		double v0y = dart.throwvelocity * Math.sin(ang); // initial velocity in y

		double x = (v0x * time);
		// double y = (v0y * time) + (0.5 * g * (float)Math.Pow(time, 2));
		double y = (0.5 * g * time + v0y) * time
				
				
	    double ang = Math.atan2(vy,vx); // don't forget, vy first!!!
		double deg = ang * RAD2DEG;*/
		///YAAAAA
		
		
		dart.zPrev = dart.zPosition;
		dart.yPrev = dart.yPosition;
		
		//Draw dart
		gl.glPushMatrix();		  
	    
	      gl.glTranslatef(dart.xPosition, -dart.yPosition, -dart.zPosition);
	      //gl.glRotatef(rotate, 1.0f,0.0f, 0.0f);
	      dart.dartpiece.draw(gl);
	    gl.glPopMatrix();
	    
	    gl.glColor4f(1f, 1f, 1f, 1f);
	    
	    
	    gl.glPushMatrix();		  
	      gl.glTranslatef(0f, 2f, -5f);
	      //if(dart.velocity != 0)
	      gl.glTranslatef((dart.xPosition/-dart.zPosition)*3, 0f, 0f);
	      gl.glRotatef(-dart.rotate, 1.0f,0.0f, 0.0f);
	      dartzoom.draw(gl);
	    gl.glPopMatrix();
	    
	  
	}
	
	private void drawBoard(GL10 gl){
		gl.glPushMatrix();
		  gl.glTranslatef(0f, 0f, -(zBoard+2.5f));
		  gl.glColor4f(1.0f,1.0f,1.0f,1f);  
		  board.draw(gl);
		gl.glPopMatrix();
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width,height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		GLU.gluPerspective(gl, 45.0f, (float) width / height, .1f, 100.f);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glDisable(GL10.GL_DITHER);
		
		board.loadTexture(gl, DartsEngine.BOARD, DartsEngine.context);
		dartzoom.loadTexture(gl, DartsEngine.CEMENT, DartsEngine.context);
		
	}
	
	//////Failed lines
	/*
	gl.glPushMatrix();
    float points[] = {-2f,0f,15f,  -2f,20f,15f,  5f,20f,10f,  5f,5f,10f,};
    ByteBuffer byteBuf = ByteBuffer.allocateDirect(points.length * 4);
	byteBuf.order(ByteOrder.nativeOrder());
	linevertexBuffer = byteBuf.asFloatBuffer();
	linevertexBuffer.put(points);
	linevertexBuffer.position(0);
	
	gl.glFrontFace(GL10.GL_CCW);
	gl.glColor4f(0.0f,1.0f,0.0f,1.0f);//Change the object colour
	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, linevertexBuffer);
	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 4);
	gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	gl.glDisable(GL10.GL_CULL_FACE);
	gl.glPopMatrix();
	*/
}
