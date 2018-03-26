package sen.sen.numericonsandroid.Global;

public class Helpers{
  public static float randomFloat(String seed){
    //todo implement seed
    return (float) Math.random();
  }

  public static int randomIntInRange(int min, int max, String seed){
    return Math.round(randomFloat(seed) * (max - min)) + min;
  }

  public static float randomFloatInRange(float min, float max, String seed){
    return (randomFloat(seed) * (max - min)) + min;
  }
}
