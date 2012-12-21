package com.ertyguy.darts;


public class Dart {
	
	public DartPiece dartpiece;
	
	
	
	public float[] position = new float[]{0f,0f,1f};
	public float[] prevposition = position.clone();	
	public float[] velocity = new float[3];
	
	//public float vel = 0;
	public float rotate = 0f;
	//public float throwdist = 0f;
	//public float throwx = 0f;
	//public float throwy = 0f;
	//public float throwvelocity;
	
	
	public state sta = state.hidden;
	
	public Dart(){
		dartpiece  = new DartPiece();
	}
	
	public enum state{
		hidden,
		inhand,
		inflight,
		landed
	}
	
}

