package sen.sen.numericonsandroid.Global;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

  public static String fingerprintPassword(String password){
    return SHA1(password);
  }

  public static String MD5Hash(String s){
    MessageDigest m = null;

    try{
      m = MessageDigest.getInstance("MD5");
    } catch(NoSuchAlgorithmException e){
      e.printStackTrace();
    }

    m.update(s.getBytes(), 0, s.length());
    String hash = new BigInteger(1, m.digest()).toString(16);
    return hash;
  }

  private static String convertToHex(byte[] data){
    StringBuilder buf = new StringBuilder();
    for(byte b : data){
      int halfbyte = (b >>> 4) & 0x0F;
      int two_halfs = 0;
      do{
        buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
        halfbyte = b & 0x0F;
      } while(two_halfs++ < 1);
    }
    return buf.toString();
  }

  public static String SHA1(String text){
    try{
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] textBytes = text.getBytes("UTF-8");
      md.update(textBytes, 0, textBytes.length);
      byte[] sha1hash = md.digest();
      return convertToHex(sha1hash);
    }
    catch(Exception e){
      e.printStackTrace();
      return text;
    }
  }
}
