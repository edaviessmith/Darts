package com.ertyguy.darts;

//import java.io.IOException;
//import java.io.InputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.opengl.GLUtils;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;


public class DartPiece {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private int[] textures = new int[1];
	
	//Dart box coordinates
	private float xl = -0.25f;
	private float xr = 0.25f;
	private float zf = 0.5f;
	private float zb = -0.5f;
	private float yt = 0.5f;
	private float yb = 0.0f;
	
	private float vertices[] = {
		
		xl, yb, zf, 
		xr, yb, zf,
		xl, yb, zb,  //Bottom
		xr, yb, zb, 
		
		
		xl, yb, zb, 
		xr, yb, zb, 
		xl, yt, zb,  //Front
		xr, yt, zb,		
		
		
		xl, yb, zb, 
		xl, yb, zf, 
		xl, yt, zb, //Left
		xl, yt, zf,
		
		
		xr, yb, zf, 
		xr, yb, zb, 
		xr, yt, zf, //Right
		xr, yt, zb,
		
		
		xl, yb, zf, 
		xr, yb, zf, 
		xl, yt, zf,  //Back
		xr, yt, zf,
		
		
		xl, yt, zf, 
		xr, yt, zf, 
		xl, yt, zb, //Top
		xr, yt, zb,
		
	};
	
	/*
	float color1[] = { 255,   0,   0 };
    float color2[] = {   0, 255,   0 };
    float color3[] = {   0,   0, 255 };
    float color4[] = { 255, 255,   0 };
    float color5[] = {   0, 255, 255 };
    float color6[] = { 255, 255, 255 };

    float color[] = {
        color1[0], color1[1], color1[2],
        color1[0], color1[1], color1[2],
        color1[0], color1[1], color1[2],
        color1[0], color1[1], color1[2],
        //color1[0], color1[1], color1[2],
        //color1[0], color1[1], color1[2],

        color2[0], color2[1], color2[2],
        color2[0], color2[1], color2[2],
        color2[0], color2[1], color2[2],
        color2[0], color2[1], color2[2],
        //color2[0], color2[1], color2[2],
        //color2[0], color2[1], color2[2],

        color3[0], color3[1], color3[2],
        color3[0], color3[1], color3[2],
        color3[0], color3[1], color3[2],
        color3[0], color3[1], color3[2],
        //color3[0], color3[1], color3[2],
        //color3[0], color3[1], color3[2],

        color4[0], color4[1], color4[2],
        color4[0], color4[1], color4[2],
        color4[0], color4[1], color4[2],
        color4[0], color4[1], color4[2],
        //color4[0], color4[1], color4[2],
        //color4[0], color4[1], color4[2],

        color5[0], color5[1], color5[2],
        color5[0], color5[1], color5[2],
        color5[0], color5[1], color5[2],
        color5[0], color5[1], color5[2],
        //color5[0], color5[1], color5[2],
        //color5[0], color5[1], color5[2],

        color6[0], color6[1], color6[2],
        color6[0], color6[1], color6[2],
        color6[0], color6[1], color6[2],
        color6[0], color6[1], color6[2],
        //color6[0], color6[1], color6[2],
        //color6[0], color6[1], color6[2],
    };
	*/
	private float texture[] = {
			0.0f, 0.0f,
			1.0f, 0f,
			0f, 1f,
			1f, 1.0f,
			/*
			0.0f, 0.0f,
			1.0f, 0f,
			0f, 1f,
			1f, 1.0f,

			0.0f, 0.0f,
			1.0f, 0f,
			0f, 1f,
			1f, 1.0f,
			
			0.0f, 0.0f,
			1.0f, 0f,
			0f, 1f,
			1f, 1.0f,
			
			0.0f, 0.0f,
			1.0f, 0f,
			0f, 1f,
			1f, 1.0f,
			
			0.0f, 0.0f,
			1.0f, 0f,
			0f, 1f,
			1f, 1.0f,*/
		};
	
	
	public DartPiece() {
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		/*
		byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);*/
	}
	
	public void draw(GL10 gl) {
		//gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		 //gl.glFrontFace(GL10.GL_CCW);
		 //gl.glEnable(GL10.GL_CULL_FACE);
		 //gl.glCullFace(GL10.GL_BACK);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		//gl.glColorPointer(3, GL10.GL_FLOAT, 0, colorBuffer);
		
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glColor4f(0.0f,0.0f,1.0f,1f);  
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0,4);
		gl.glColor4f(0.0f,0.0f,0.5f,1f);  
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4,4);
		gl.glColor4f(1.0f,0.5f,1.0f,1f);  
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8,4);
		gl.glColor4f(0.5f,1.0f,1.0f,1f);  
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12,4);
		gl.glColor4f(1.0f,0.5f,0.5f,1f);  
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16,4);
		gl.glColor4f(0.5f,0.5f,1.0f,1f);  
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20,4);
		
		//gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		 //gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	public void loadTexture(GL10 gl,int texture, Context context) {
		InputStream imagestream = context.getResources().openRawResource(texture);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(imagestream);
		}catch(Exception e){
		}finally {
			try {
				imagestream.close();
				imagestream = null;
			} catch (IOException e) {
			}
		}
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}
}

