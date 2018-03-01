package sen.sen.numericonsandroid.Models;

public class DroppedItem{
  private int number;
  private float xPosition;
  private long dropTime;

  public DroppedItem(int number, float xPosition, long dropTime){
    this.number = number;
    this.xPosition = xPosition;
    this.dropTime = dropTime;
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

  public long getDropTime(){
    return dropTime;
  }

  public void setDropTime(long dropTime){
    this.dropTime = dropTime;
  }
}
