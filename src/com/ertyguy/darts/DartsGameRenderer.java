package com.ertyguy.darts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ertyguy.darts.DartsEngine;

import android.opengl.GLES10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class DartsGameRenderer implements Renderer{
	
	//Vector Arrays
	private int X=0,Y=1,Z=2;
	
	
	private DartZoom dartzoom = new DartZoom();
	//private Arrow arrow = new Arrow();
	private Board board = new Board();

	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0 ;
	
	//private boolean justletgo = false;
	private float zBoard = 50;
	private float zReach = 15;

	private float maxvelocity = 10f;
	private float minvelocity = 4f;
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
		
		gl.glViewport(0, 0, DartsEngine.size.x, DartsEngine.size.y);
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
	    
		//gl.glFrustumf(-1f, 1f, -1f, 1.0f, 1f, 60f);
		
	    
	    
	    ///Get Touch Positions
		MatrixGrabber matrixGrabber = new MatrixGrabber();
        matrixGrabber.getCurrentModelView(gl);  
        matrixGrabber.getCurrentProjection(gl);
        //Map finger positions relative to screen
        boolean unprojectedNearStart = (GLU.gluUnProject(DartsEngine.startx, DartsEngine.starty, 0f, matrixGrabber.mModelView, 0, matrixGrabber.mProjection, 0, new int[]{DartsEngine.gameView.getTop(),DartsEngine.gameView.getLeft(),DartsEngine.gameView.getWidth(),DartsEngine.gameView.getHeight()}, 0, startvector, 0) == GLES10.GL_TRUE);
        boolean unprojectedNear = (GLU.gluUnProject(DartsEngine.x, DartsEngine.y, 0f, matrixGrabber.mModelView, 0, matrixGrabber.mProjection, 0, new int[]{DartsEngine.gameView.getTop(),DartsEngine.gameView.getLeft(),DartsEngine.gameView.getWidth(),DartsEngine.gameView.getHeight()}, 0, vector, 0) == GLES10.GL_TRUE);
        
        
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        
		if(unprojectedNear) {
			for(int i=0; i< DartsEngine.maxdarts && DartsEngine.selecteddart>=0;i++ ){
				if(i <= DartsEngine.selecteddart){
					if(i==DartsEngine.selecteddart){
						//Dart dart = new Dart();
						//DartsEngine.darts[i] = dart;
						System.out.println("{{{-------}}}"+" Position Z: "+DartsEngine.darts[i].position[2]+" Y: "+DartsEngine.darts[i].position[1]);
					}
					
					/*if(DartsEngine.darts[i].sta == Dart.state.hidden && DartsEngine.pressed && DartsEngine.distance < zReach){
						DartsEngine.darts[i].sta = Dart.state.inhand;
					}else*/ 
					if(DartsEngine.darts[i].sta == Dart.state.inhand && (!DartsEngine.pressed || throwdistmax < DartsEngine.darts[i].position[Z])){//DartsEngine.darts[i].zPosition < zBoard && DartsEngine.darts[i].yPosition > DartsEngine.bottomy){
						DartsEngine.darts[i].sta = Dart.state.inflight;
					}else if(DartsEngine.darts[i].sta == Dart.state.inflight && DartsEngine.darts[i].position[2] > zBoard){
						DartsEngine.darts[i].sta = Dart.state.landed;
					}
					
					drawDart(gl, DartsEngine.darts[i]);
					
				}else{
					if(DartsEngine.darts[i] != null){
						DartsEngine.darts[i].sta = Dart.state.hidden;
					}
				}
			}
		}
		gl.glColor4f(0.5f,0.5f,1.0f,0f);
		if(unprojectedNearStart) drawLine(gl,linepoints);
		
		drawBoard(gl);
		
		loopEnd = System.currentTimeMillis();
		loopRunTime = ((loopEnd - loopStart));
		
		
	}
	

	private void drawLine(GL10 gl, float[] points){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(points.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		linevertexBuffer = byteBuf.asFloatBuffer();
		linevertexBuffer.put(points);
		linevertexBuffer.position(0);
		
		gl.glColor4f(0.2f, 0.9f, 0.5f, 1.0f);
		gl.glPushMatrix();
		
		  gl.glFrontFace(GL10.GL_CCW);
          gl.glEnable(GL10.GL_CULL_FACE);
          gl.glCullFace(GL10.GL_BACK);
        
          gl.glTranslatef(0, 0, 0);
          gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

          gl.glVertexPointer(3, GL10.GL_FLOAT, 0, linevertexBuffer);

          gl.glLineWidth(3.0f);
          

          //Insert For loop when more than 2 points
          gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);

          gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
          gl.glDisable(GL10.GL_CULL_FACE);
        gl.glPopMatrix();
        
	}
	
	
	private void drawDart(GL10 gl, Dart dart){

		if(dart.sta == Dart.state.inhand){

			dart.position[Z] += throwspeed;
			dart.position[X] = vector[X]*dart.position[Z];
			dart.position[Y] = -vector[Y]*dart.position[Z];
			
			dart.velocity[X] = vector[X]-startvector[X];//Previous position
			dart.velocity[Y] = vector[Y]-startvector[Y];
			dart.velocity[Z] = throwdistmax-dart.position[Z];//+(dart.velocity[Y]);
			
			linepoints = new float[] { startvector[X],startvector[Y],-1f,  vector[X]*DartsEngine.distance,vector[Y]*DartsEngine.distance, -DartsEngine.distance, };
		}else if(dart.sta == Dart.state.inflight){
			
			System.out.println("[[[[[[[[[[[[[[[["+"]]]]]]]]]] velocity= "+dart.velocity[Z]+"  and touchtime= "+DartsEngine.touchtime);
			
			dart.position[X] += dart.velocity[X]*DartsEngine.vectorscale[X];
			dart.position[Y] -= (dart.velocity[Y])*DartsEngine.vectorscale[Y];
			dart.position[Z] += (dart.velocity[Z])*DartsEngine.vectorscale[Z];
			//dart.velocity *= 0.99; // Drag
			
			if(dart.velocity[Y] > -30)
				dart.velocity[Y] -= .08f;
			
		}
		
		if(dart.sta == Dart.state.landed){
			dart.velocity[Z] = 0;
			if(dart.position[Z] > zBoard+2) dart.position[Z] = zBoard+0.5f; //Move dart in front of board
 
			 
			if(dart.position[Z] > DartsEngine.bottomy){

			}
		} else {
				//get angle
				double angle =  Math.atan2((dart.prevposition[Y]-dart.position[Y]),(dart.prevposition[Z]-dart.position[Z]));//*100;
				//get degrees
				dart.rotate = (float) (angle * (180 / Math.PI));
				//System.out.println("Angle = y:"+(dart.prevposition[Y]-dart.position[Y])+" z:"+(dart.prevposition[Z]-dart.position[Z]));
				System.out.println("{{{-------}}}  Rotate = "+dart.rotate);
		}

		
		
		dart.prevposition = dart.position.clone();
		
		//Draw dart
		gl.glPushMatrix();		  
		  gl.glColor4f(1f, 1f, 1f, 1f);
	      gl.glTranslatef(dart.position[X], -dart.position[Y], -dart.position[Z]);
	      gl.glRotatef(-dart.rotate, 1.0f,0.0f, 0.0f);
	      dart.dartpiece.draw(gl);
	    gl.glPopMatrix();
	    
	    
	    //Draw Top Dart testing block for rotation
	    gl.glPushMatrix();		  
	      gl.glColor4f(1f, 1f, 1f, 1f);
	      gl.glTranslatef((dart.position[X]/-dart.position[Z])*3, 2f, -5f);
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
	
	/*
	 Pyramid pointer 
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
