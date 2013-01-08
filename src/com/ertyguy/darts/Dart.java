package com.ertyguy.darts;


public class Dart {
	
	public DartPiece dartpiece;
	
	
	
	public float[] position = new float[]{0f,0f,1f};
	public float[] prevposition = position.clone();	
	public float[] velocity = new float[3];
	
	public float rotate = 0f;
	public state sta;
	
	public Dart(){
		dartpiece  = new DartPiece();
		sta = state.hidden;
	}
	
	public enum state{
		hidden,
		inhand,
		inflight,
		landed
	}
	
}

