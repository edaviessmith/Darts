package com.ertyguy.darts;

import android.content.Context;
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
	
	public static final float gamespeed = .5f;//1f;
	
	public static final int BOARD = R.drawable.dartboard;
	public static final int CEMENT = R.drawable.cement;
	
	public static boolean pressed = false;
	public static final int PLAYER_FORWARD = 1;
	public static final int PLAYER_RIGHT = 2;
	public static final int PLAYER_LEFT = 3;
	public static final int PLAYER_BACK = 4;
	public static final float PLAYER_ROTATE_SPEED = 2f;
	public static final float PLAYER_WALK_SPEED =0.1f;
	public static int playerMovementAction = 0;
	
	/*Game Variables*/
	public static Context context;
	public static Display display;
	public static DartsGameView gameView;
}
