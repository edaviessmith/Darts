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


public class DartPiece {
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	//private int[] textures = new int[2];
	
	private float vertices[] = {
		
		-0.25f, 0.0f, 0.25f, 
		0.25f, 0.0f, 0.25f,
		-0.25f, 0.0f, -0.25f,  //Bottom
		0.25f, 0.0f, -0.25f, 
		
		
		-0.25f, 0.0f, -0.25f, 
		0.25f, 0.0f, -0.25f, 
		-0.25f, 1.0f, -0.25f,  //Front
		0.25f, 1.0f, -0.25f,		
		
		
		-0.25f, 0.0f, -0.25f, 
		-0.25f, 0.0f, 0.25f, 
		-0.25f, 1.0f, -0.25f, //Left
		-0.25f, 1.0f, 0.25f,
		
		
		0.25f, 0.0f, 0.25f, 
		0.25f, 0.0f, -0.25f, 
		0.25f, 1.0f, 0.25f, //Right
		0.25f, 1.0f, -0.25f,
		
		
		-0.25f, 0.0f, 0.25f, 
		0.25f, 0.0f, 0.25f, 
		-0.25f, 1.0f, 0.25f,  //Back
		0.25f, 1.0f, 0.25f,
		
		
		-0.25f, 1.0f, 0.25f, 
		0.25f, 1.0f, 0.25f, 
		-0.25f, 1.0f, -0.25f, //Top
		0.25f, 1.0f, -0.25f,
		
	};
	
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
	
	
	public DartPiece() {
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);
	}
	
	public void draw(GL10 gl) {
		//gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glFrontFace(GL10.GL_CCW);
		
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
		
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
}

