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


public class Board {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;
	private int[] textures = new int[1];
	
	private float vertices[] = {
		//-20f, 0.0f, 0f, 
		//20f, 0.0f, 0f, 
		//20f, 40.0f, 0f,  //Front with indeces
		//-20f, 40.0f, 0f,	
		
		-15f, -15.0f, 0f, 
		15f, -15.0f, 0f, 
		-15f, 15.0f, 0f,  //Front
		15f, 13.0f, 0f,		
	};
		
	private float texture[] = {
		0.0f, 0.0f,
		1.0f, 0f,
		0f, 1f,
		1f, 1.0f,
	};
	
	private byte indices[] = {
		0,1,2,
		0,2,3,
	};
	
	public Board() {
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
		
		//indexBuffer = ByteBuffer.allocateDirect(indices.length);
		//indexBuffer.put(indices);
		//indexBuffer.position(0);
	}
	
	public void draw(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0,4);
		//gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
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
