package se.chalmers.labyrinth;

public class Ball {
	private float radius;
	private float posX;
	private float posY;
	//private float lastPosX;
	//private float lastPosY;
	
	public Ball(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	public void updatePosition(float sensX, float sensY) {
		//lastPosX = posX;
		//lastPosY = posY;
		
		posX -= sensX;
		posY += sensY;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}
}