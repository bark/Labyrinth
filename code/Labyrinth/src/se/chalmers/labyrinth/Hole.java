package se.chalmers.labyrinth;

public class Hole {
	private float radius;
	private int color;
	private float posX;
	private float posY;
	
	public Hole(float radius, int color, float posX, float posY){
		this.radius = radius;
		this.color = color;
		this.posX = posX;
		this.posY = posY;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
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
