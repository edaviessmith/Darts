package com.ertyguy.darts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import javax.microedition.khronos.opengles.GL10;


public class DartZoom {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private int[] textures = new int[1];
	
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
	private float vertices[] = {
		
		-0.25f, 0.0f, 0.25f, 
		0.25f, 0.0f, 0.25f,
		-0.25f, 0.0f, -0.25f,  //Bottom
		0.25f, 0.0f, -0.25f, 
		
		
		-0.25f, 0.0f, -0.25f, 
		0.25f, 0.0f, -0.25f, 
		-0.25f, 0.5f, -0.25f,  //Front
		0.25f, 0.5f, -0.25f,		
		
		
		-0.25f, 0.0f, -0.25f, 
		-0.25f, 0.0f, 0.25f, 
		-0.25f, 0.5f, -0.25f, //Left
		-0.25f, 0.5f, 0.25f,
		
		
		0.25f, 0.0f, 0.25f, 
		0.25f, 0.0f, -0.25f, 
		0.25f, 0.5f, 0.25f, //Right
		0.25f, 0.5f, -0.25f,
		
		
		-0.25f, 0.0f, 0.25f, 
		0.25f, 0.0f, 0.25f, 
		-0.25f, 0.5f, 0.25f,  //Back
		0.25f, 0.5f, 0.25f,
		
		
		-0.25f, 0.5f, 0.25f, 
		0.25f, 0.5f, 0.25f, 
		-0.25f, 0.5f, -0.25f, //Top
		0.25f, 0.5f, -0.25f,
		
	};*/
	
	private float texture[] = {
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
			1f, 1.0f,
			
			0.0f, 0.0f,
			1.0f, 0f,
			0f, 1f,
			1f, 1.0f,
		};

	
	public DartZoom() {
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
	}
	
	public void draw(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		//gl.glFrontFace(GL10.GL_CCW);
		//gl.glEnable(GL10.GL_CULL_FACE);
		//gl.glCullFace(GL10.GL_BACK);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0,4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4,4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8,4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12,4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16,4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20,4);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		//gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_TEXTURE_2D);
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

