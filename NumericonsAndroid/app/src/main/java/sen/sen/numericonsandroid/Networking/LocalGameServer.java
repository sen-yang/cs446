package sen.sen.numericonsandroid.Networking;


import android.bluetooth.BluetoothSocket;
import android.util.Log;

import sen.sen.numericonsandroid.Models.*;

import java.security.SecureRandom;
import java.util.ArrayList;


public class LocalGameServer{
    public static final int ADDITION= 1;
    public static final int SUBTRACTION = 2;
    public static final int MUTLIPLICATION = 3;
    public static final int DIVISION = 4;
    public static final int GET_NUMBER = 5;
    public static final int USE_POWERUP = 5;


    BluetoothController BC = new BluetoothController();
    private GameRoom gameroom1;
    BluetoothSocket bsocket;

    public LocalGameServer(BluetoothSocket bsocket){
    this.bsocket = bsocket;
    }

    public DroppedItem[] generatesDroppeditem(int amt){
        //generate the random item. will add generation of what type of item soon with skewed probability
        DroppedItem[] listofItem = null;
        byte[] longseed = null;
        int num;
        float x, yspeed;
        for(int i = 0; i< 20 ; i++) {
         longseed[i]  =(byte)System.currentTimeMillis();
        }
        SecureRandom rdm = new SecureRandom(longseed);
        for(int i = 0; i< amt ; i++){
        //need to know the range for x and yspeed
        num = rdm.nextInt(10);
        x = rdm.nextFloat();
        yspeed = rdm.nextFloat();
            listofItem[i] = new DroppedItem(num, x ,yspeed);
        }
        return listofItem;
    }

    public int generateGoalNumber(){
        byte[] longseed = null;
        for(int i = 0; i< 20 ; i++) {
            longseed[i]  =(byte)System.currentTimeMillis();
        }
        SecureRandom rdm = new SecureRandom(longseed);
        return rdm.nextInt(200);
    }

    public void useItem(int item){
        //TODO
    }

    public int calculateNextScore(int current, int next, int operation){
        switch(operation){
            case ADDITION:
                return current + next;
            case SUBTRACTION:
                return current - next;
            case MUTLIPLICATION:
                return current * next;
            case DIVISION:
                if(next==0)
                    //error ??? send
                    return 11111111;
                return current / next;
            default:
                return 0;
        }

    }

    public void initGame(ArrayList<User> userList){
        GameState gs = new GameState();

        int goalnumber = generateGoalNumber();
        ArrayList<Player> players =null;
        for(int i = 0 ; i< userList.size() ; i++) {
            players.add(new Player(goalnumber, userList.get(i).getUsername()));
        }
        gs.setPlayerList(players);
        gs.setTimeRemaining(60000);
        gs.setTargetNumber(goalnumber);
        gameroom1 = new GameRoom(gs, userList);
        //send both to the client
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        DroppedItem[]  listitem = generatesDroppeditem(4);
                        //sends to both clients
                    //sends to client
                    }
                },
                2000);



}
