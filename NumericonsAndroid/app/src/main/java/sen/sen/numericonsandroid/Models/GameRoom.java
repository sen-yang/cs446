package sen.sen.numericonsandroid.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ASUS on 20/3/2018.
 */

public class GameRoom implements Serializable{
    private GameState gs;
    private ArrayList<User> userList;



    public GameState getGs() {
        return gs;
    }

    public void setGs(GameState gs) {
        this.gs = gs;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }


    public GameRoom(GameState gs, ArrayList<User> userList) {
        this.gs = gs;
        this.userList = userList;
    }


}
