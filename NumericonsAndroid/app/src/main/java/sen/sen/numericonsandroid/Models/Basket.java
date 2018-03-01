package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

/**
 * Created by Jennifer on 2018-02-28.
 */

public class Basket implements Serializable{
  private float xPosition;
  private float yPosition;
  private float xScale;

  public Basket(float _xPosition, float _yPosition) {
    this.xPosition = _xPosition;
    this.yPosition = _yPosition;
    this.xScale = 1;
  }

  public float getxPosition(){
    return xPosition;
  }
  public float getyPosition(){
    return yPosition;
  }

  public void setxPosition(float xPosition){
    this.xPosition = xPosition;
  }

  public void setyPosition(float yPosition){
    this.yPosition = yPosition;
  }

  public boolean checkCollision() {
    //@TODO: Change this later...
    //https://code.tutsplus.com/tutorials/develop-your-first-game-in-canvas-from-start-to-finish--pre-39198
    return true;
  }


}
