package sen.sen.numericonsandroid.Models;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
  public boolean isAlive = true;
  private Constants.ITEM_TYPE itemType;

  private Drawable numberImage;

  public DroppedItem(){
  }

  public DroppedItem(int number, float xPosition, float ySpeed){
    this.itemType = Constants.ITEM_TYPE.NUMBER;
    this.number = number;
    this.xPosition = xPosition;
    this.ySpeed = ySpeed;
    this.yPosition = 0;
  }

  public DroppedItem(Constants.ITEM_TYPE itemType, float xPosition, float ySpeed){
    this.itemType = itemType;
    this.xPosition = xPosition;
    this.ySpeed = ySpeed;
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

  public void fall(){
    long timePass = System.currentTimeMillis() - startTimer;
    yPosition = (ySpeed / SPEEDOFFSET) * (timePass / TIMEROFFSET);
  }

  public Constants.ITEM_TYPE getItemType(){
    return itemType;
  }
}
