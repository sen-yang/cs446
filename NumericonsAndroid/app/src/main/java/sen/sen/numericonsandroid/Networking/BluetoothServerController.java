package sen.sen.numericonsandroid.Networking;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameDroppedItemMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameStateMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.PlayerActionMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.WebsocketMessage;
import org.apache.commons.*;
import org.apache.commons.lang.SerializationUtils;


public class BluetoothServerController{
    public static final String TAG = "BluetoothController";
    private static BluetoothServerController staticBluetoothController;

    public interface BluetoothListener{
        void onConnected();

        void onClose(int code, String reason, boolean remote);

        void invitedToMatch(BluetoothDevice device);

        void matchConfirmed(BluetoothDevice device);

        void gameInitialized(GameState gameState);
    }

    private Gson gson;
    private List<WeakReference<BluetoothListener>> bluetoothListenerList;
    private List<WeakReference<GameListener>> gameListenerList;
    private BluetoothChatService bluetoothChatService;
    private boolean isConnected;
    private User user;
    private GameState gameState;
    private BluetoothDevice connectedDevice;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isHost;
    public static final int USE_POWERUP = 5;

    public static BluetoothServerController getInstance(){
        if(staticBluetoothController == null){
            staticBluetoothController = new BluetoothServerController();
            staticBluetoothController.initialize();
        }
        return staticBluetoothController;
    }

    public void addBluetoothListener(BluetoothListener bluetoothListener){
        boolean existsInList = false;
        for(WeakReference listenerReference : bluetoothListenerList){
            if(listenerReference.get() != null && listenerReference.get() == bluetoothListener){
                existsInList = true;
            }
        }
        if(existsInList == false){
            bluetoothListenerList.add(new WeakReference<BluetoothListener>(bluetoothListener));
        }
    }


    public void addGameListener(GameListener gameListener){
        boolean existsInList = false;
        for(WeakReference listenerReference : gameListenerList){
            if(listenerReference.get() != null && listenerReference.get() == gameListener){
                existsInList = true;
            }
        }
        if(existsInList == false){
            gameListenerList.add(new WeakReference<GameListener>(gameListener));
        }
    }


    public void inviteDeviceToGame(BluetoothDevice device){
        bluetoothChatService.connect(device, false);
    }



    public void ReplyPlayerAction(PlayerAction playerAction) {
        WebsocketMessage websocketMessage = new PlayerActionMessage(playerAction);
        try {
            byte[] datatobesend = convertToBytes(websocketMessage);
            bluetoothChatService.write(datatobesend);
        }catch(IOException e){
            Log.e("ERROR","DATA Cannot be send : "+ e);
        }
    }


    public User getUser(){
        return user;
    }

    private void initialize(){
        bluetoothChatService = new BluetoothChatService(new IncomingBluetoothMessageHandler());
        bluetoothListenerList = new ArrayList<>();
        gameListenerList = new ArrayList<>();
    }

    private class IncomingBluetoothMessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case Constants.MESSAGE_STATE_CHANGE:
                    switch(msg.arg1){
                        case BluetoothChatService.STATE_CONNECTED:
//              setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//              mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
//              setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
//              setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    WebsocketMessage message = null;
                    // construct a string from the valid bytes in the buffer
                    try {
                        message = convertFromBytes(readBuf);
                    }catch(IOException e){
                        Log.e("ERROR","DATA Cannot be send : "+ e);
                    }catch(ClassNotFoundException ce){
                        Log.e("ERROR","CLASS Cannot be found : "+ ce);
                    }
                    if(message!=null)
                        BluetoothServerController.this.handleMessage(message);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    BluetoothDevice otherDevice = msg.getData().getParcelable(Constants.DEVICE);
                    boolean isAccepting = msg.getData().getBoolean(Constants.IS_ACCEPTING);

                    if(isAccepting){
                        for(WeakReference<BluetoothListener> listenerWeakReference : bluetoothListenerList){
                            if(listenerWeakReference.get() != null){
                                listenerWeakReference.get().invitedToMatch(otherDevice);
                            }
                        }
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    Log.d(TAG, "toast: " + msg.getData().getString(Constants.TOAST));
                    break;
            }
        }
    }

    private void handleMessage(WebsocketMessage websocketMessage ){
        PlayerAction PA = null;
        switch(websocketMessage.getType()){
            case PLAYER_ACTION:
                PlayerActionMessage wsm =(PlayerActionMessage) websocketMessage;
                PA = wsm.getPlayerAction();
                break;


//            case USE_POWERUP:
            default:
                break;
        }

    }



    private byte[] convertToBytes(WebsocketMessage object) throws IOException {
        byte[] data = SerializationUtils.serialize(object);
        return data;
    }

    private WebsocketMessage convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        WebsocketMessage yourObject =(WebsocketMessage) SerializationUtils.deserialize(bytes);
        return yourObject;
    }

    public boolean isConnected(){
        return isConnected;
    }

    public GameState getGameState(){
        return gameState;
    }

}
