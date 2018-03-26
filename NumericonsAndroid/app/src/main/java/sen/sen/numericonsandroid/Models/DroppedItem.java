package sen.sen.numericonsandroid.Models;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class DroppedItem implements Serializable{
  static final int TIMEROFFSET = 100;
  static final int SPEEDOFFSET = 2;
  static final int WIDTH = 90;
  static final int HEIGHT = 100;

  private int number;
  private long startTimer = System.currentTimeMillis();
  private float xPosition;
  private float yPosition;
  private float ySpeed;
  private boolean alive;
  private int itemType;
  private Drawable numberDrawable;
  private Rect numberBound  = new Rect((int)xPosition, (int)yPosition, (int)xPosition+100, (int)yPosition+100);

  private Drawable numberImage;

  public DroppedItem(){
  }
  public DroppedItem(int number, float xPosition, float ySpeed){
    this.number = number;
    this.xPosition = xPosition;
    this.ySpeed = ySpeed;
    this.alive = true;
    this.yPosition = 0;
  }

  public void setNumberDrawable(Drawable numberDrawable){
    this.numberDrawable = numberDrawable;
  }

  public Drawable getNumberDrawable(){
    return this.numberDrawable;
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
  public void setNumberBound(int left, int top){
    this.numberBound.set(left, top, left+WIDTH, top+HEIGHT);
    this.numberDrawable.setBounds(this.numberBound);

  }
  public Rect getNumberBound() {
    return this.numberBound;
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
