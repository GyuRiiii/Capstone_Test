package com.example.capstone_test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> deviceList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textName;

        public ViewHolder(View view){
            super(view);
            textName = view.findViewById(R.id.textItem);
        }
    }

    public DeviceListAdapter(Context context, List<Object> deviceList){
        this.context = context;
        this.deviceList = deviceList;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        // Get device name and device address
        DeviceInfoModel deviceInfoModel = (DeviceInfoModel) deviceList.get(position);
        String deviceName = deviceInfoModel.getDeviceName();
        String deviceAddress = deviceInfoModel.getDeviceHardwareAddress();

        // Assign device name to the recycler view
        ViewHolder itemHolder = (ViewHolder) holder;
        itemHolder.textName.setText(deviceName);

        // Return to Main Screen when a device name is selected
        itemHolder.textName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("deviceAddress", deviceAddress);
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        int dataCount = deviceList.size();
        return dataCount;
    }
}
