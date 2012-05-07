package se.chalmers.labyrinth;

public class Wall {
    private float posX1;
    private float posY1;
    private float posX2;
    private float posY2;
    private int color;
    
    public Wall(float posX1, float posY1, float posX2, float posY2, int color) {
        //X1Y1 för översta vänstra hörnet och X2Y2 för det nedre högra
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
        this.color = color;
    }
    
    public float getPosX1() {
        return posX1;
    }
    
    public float getPosY1() {
        return posY1;
    }
    
    public void setPosX1(float posX1) {
        this.posX1 = posX1;
    }
    
    public void setPosY1(float posY1) {
        this.posY1 = posY1;
    }

    public float getPosX2() {
        return posX2;
    }
    
    public float getPosY2() {
        return posY2;
    }
    
    public void setPosX2(float posX2) {
        this.posX2 = posX2;
    }
    
    public void setPosY2(float posY2) {
        this.posY2 = posY2;
    }
    
    public int getColor() {
        return color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
}
