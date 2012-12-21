package com.ertyguy.darts;

import android.graphics.Point;

import android.content.Context;
//import android.graphics.Point;
import android.view.Display;

public class DartsEngine {

	public static final int GAME_THREAD_DELAY = 4000;
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);
	
	public static float x = 0;
	public static float y = 0;
	public static float z = 0;

	
	public static float throwx = 0;
	public static float throwy = 0;
	public static float startx;
	public static float starty;
	public static float bottomy = -20;
	
	public static int touchtime;
	public static float distance;
	public static int selecteddart = -1;
	public static int maxdarts = 5;
	
	public static Dart[] darts = new Dart[DartsEngine.maxdarts];
	public static final double[] vectorscale = new double[]{0.5,0.5,0.1};
	//public static final float gamespeed = .5f;//1f;
	
	public static final int BOARD = R.drawable.dartboard;
	public static final int CEMENT = R.drawable.cement;
	
	public static boolean pressed = false;
	
	/*Game Variables*/
	public static Context context;
	
	public static Display display;
	public static Point size = new Point();
	
	public static DartsGameView gameView;
}
