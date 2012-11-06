package com.ertyguy.darts;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.ertyguy.darts.DartsGameRenderer;

public class DartsGameView extends GLSurfaceView{
	private DartsGameRenderer renderer;

	public DartsGameView(Context context) {
		super(context);
		renderer = new DartsGameRenderer();
		this.setRenderer(renderer);
	}
	
	 

}
