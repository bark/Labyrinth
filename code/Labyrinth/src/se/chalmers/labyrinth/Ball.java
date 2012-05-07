package se.chalmers.labyrinth;


public class Ball {
    private float posX;
    private float posY;
    private float radius;
    private int color;
    //private float lastPosX;
    //private float lastPosY;
    
    public Ball(float posX, float posY, float radius, int color) {
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.color = color;
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
}