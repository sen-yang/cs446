package sen.sen.numericonsandroid.Models;

import android.util.Log;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class DroppedItem implements Serializable{
  static final int TIMEROFFSET = 100;
  static final int SPEEDOFFSET = 2;

  private int number;
  private long startTimer = System.currentTimeMillis();
  private float xPosition;
  private float yPosition;
  private float ySpeed;
  private boolean alive;
  private int itemType;
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
    long timePass = System.currentTimeMillis() - startTimer;
    yPosition = (ySpeed / SPEEDOFFSET) * (timePass / TIMEROFFSET);
  }

}
