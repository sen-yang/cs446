package test.test1.bluetoothannoyingshit;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ASUS on 17/3/2018.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private ArrayList<DeviceItem> allDevices = new ArrayList();
    private Context mContext;

    public DeviceAdapter(Context context, ArrayList<DeviceItem> dev) {
       allDevices.addAll(dev);
        mContext = context;
    }
    private Context getContext() {
        return mContext;
    }
    private void adddevice(){

    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView nameTextView1;
        public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.device_name);
            nameTextView1 = (TextView) itemView.findViewById(R.id.device_addr);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }


    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.device_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(DeviceAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        DeviceItem dev = allDevices.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        TextView textView1 = viewHolder.nameTextView1;
        textView.setText(dev.getDeviceName());
        textView1.setText(dev.getAddress());
        Button button = viewHolder.messageButton;
        button.setText("Connect");
        button.setEnabled(true);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return allDevices.size();
    }


}
