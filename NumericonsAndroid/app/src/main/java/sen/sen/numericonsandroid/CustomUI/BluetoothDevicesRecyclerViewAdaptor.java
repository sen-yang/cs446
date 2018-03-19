package sen.sen.numericonsandroid.CustomUI;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sen.sen.numericonsandroid.R;

public class BluetoothDevicesRecyclerViewAdaptor extends RecyclerView.Adapter<BluetoothDevicesRecyclerViewAdaptor.ViewHolder>{
  public interface AdaptorDelegate{
    void inviteDeviceAtPosition(int position);
  }

  private List<BluetoothDevice> devicesList;
  private AdaptorDelegate delegate;

  public BluetoothDevicesRecyclerViewAdaptor(AdaptorDelegate delegate, List<BluetoothDevice> devicesList){
    this.delegate = delegate;
    this.devicesList = devicesList;
  }

  @Override
  public BluetoothDevicesRecyclerViewAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bluetooth_device, parent, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position){
    holder.bind(position);
  }

  @Override
  public int getItemCount(){
    return devicesList.size();
  }


  public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView nameTextView;
    public TextView addressTextView;
    public Button messageButton;
    private int position;

    public ViewHolder(View itemView){
      super(itemView);

      nameTextView = itemView.findViewById(R.id.deviceNameTextView);
      addressTextView = itemView.findViewById(R.id.deviceAddressTextView);
      messageButton = itemView.findViewById(R.id.inviteButton);
      messageButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
          if(delegate != null){
            delegate.inviteDeviceAtPosition(position);
          }
        }
      });
    }

    void bind(int position){
      this.position = position;
      BluetoothDevice device = devicesList.get(position);
      nameTextView.setText(device.getName());
      addressTextView.setText(device.getAddress());
    }
  }

}
