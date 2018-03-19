package sen.sen.numericonsandroid.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sen.sen.numericonsandroid.CustomUI.BluetoothDevicesRecyclerViewAdaptor;
import sen.sen.numericonsandroid.R;

public class BluetoothConnectionActivity extends AppCompatActivity implements BluetoothDevicesRecyclerViewAdaptor.AdaptorDelegate{
  private static final int REQUEST_ENABLE_BT = 6969;
  private static final int REQUEST_ENABLE_LOCATION = 9696;

  private Button refreshButton;
  private RecyclerView recyclerView;

  private BluetoothAdapter bluetoothAdapter;
  private List<BluetoothDevice> bluetoothDeviceList;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bluetooth_connection);

    refreshButton = findViewById(R.id.refreshButton);
    recyclerView = findViewById(R.id.recyclerView);

    bluetoothDeviceList = new ArrayList<>();
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(new BluetoothDevicesRecyclerViewAdaptor(this, bluetoothDeviceList));

    if(initBluetooth()){
      startBluetooth();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);

    switch(requestCode){
      case REQUEST_ENABLE_BT:
        if(resultCode == RESULT_OK){
          startBluetooth();
        }
        break;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
    switch(requestCode){
      case REQUEST_ENABLE_LOCATION:{
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
          discoverDevices();
        }
        else{
          noBluetooth();
        }
        return;
      }
    }
  }

  public void refreshButtonPressed(View view){
    bluetoothDeviceList.clear();
    getPairedDevices();
  }

  private void startBluetooth(){
    getPairedDevices();
    discoverDevices();
  }

  private void discoverDevices(){
    if(checkLocationPermission()){
      //todo discover
    }
  }

  private boolean checkLocationPermission(){
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
      if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
//todo do i need?
        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

      }
      else{
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ENABLE_LOCATION);
      }
      return false;
    }
    else{
      return true;
    }
  }

  private boolean initBluetooth(){
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    if(bluetoothAdapter == null){
      noBluetooth();
      return false;
    }
    if(bluetoothAdapter.isEnabled()){
      return true;
    }
    else{
      Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
      return false;
    }
  }

  private void getPairedDevices(){
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    bluetoothDeviceList.addAll(pairedDevices);
    recyclerView.getAdapter().notifyDataSetChanged();
  }

  private void noBluetooth(){
    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(this);
    builder.setTitle("Bluetooth not enabled :(")
        .setPositiveButton("Back", new DialogInterface.OnClickListener(){
          public void onClick(DialogInterface dialog, int which){
            finish();
          }
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
  }

  @Override
  public void inviteDeviceAtPosition(int position){

  }
}
