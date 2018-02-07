package sen.sen.prototypesen;

public class Helpers{
  public static int randomIntInRangeInclusive(int min, int max){
    return (int)(Math.random() * ((max + 1) - min)) + min;
  }
}
