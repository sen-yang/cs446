package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

public class DroppedItem implements Serializable{
  private int number;
  private float xPosition;
  private float yPosition;
  private float ySpeed;
  private boolean alive;
  public DroppedItem(){
  }

  public DroppedItem(int number, float xPosition, float ySpeed){
    this.number = number;
    this.xPosition = xPosition;
    this.ySpeed = ySpeed;
    this.alive = true;
    this.yPosition = 0;
  }

  public float getyPosition(){
    return yPosition;
  }

  public void setyPosition(float yPosition){
    this.yPosition = yPosition;
  }

  public int getNumber(){
    return number;
  }

  public void setNumber(int number){
    this.number = number;
  }

  public float getxPosition(){
    return xPosition;
  }

  public void setxPosition(float xPosition){
    this.xPosition = xPosition;
  }

  public float getySpeed(){
    return ySpeed;
  }

  public void setySpeed(long ySpeed){
    this.ySpeed = ySpeed;
  }

  public boolean isAlive(){
    return alive;
  }
  public void setAlive(boolean alive){
    this.alive = alive;
  }

  public void fall(){
    yPosition += ySpeed / 2;
  }

}
