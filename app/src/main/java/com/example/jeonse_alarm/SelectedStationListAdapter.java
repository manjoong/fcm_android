package com.example.jeonse_alarm;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeonse_alarm.SelectedStationListAdapter.MyViewHolder;
import com.example.jeonse_alarm.SelectedStationConstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Administrator on 2017-08-07.
 */

public class SelectedStationListAdapter extends RecyclerView.Adapter<MyViewHolder>{
//    public class SelectedStationListAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {

    Context context;
    Map<String, Integer> station_pointer = new HashMap<String, Integer>();

    ArrayList<SelectedStationConstruct> SelectedStation;

    public SelectedStationListAdapter(Context context, ArrayList<SelectedStationConstruct> list) {
        super();
        this.context = context;
        this.SelectedStation = list;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_selected_station_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String station_name;



        holder.textViewStnName.setText(SelectedStation.get(position).getStnName());
        if (SelectedStation.get(position).getType().equals("OPST")){
            holder.frameLayoutOpst.setVisibility(View.VISIBLE);
        }
        //신형 for문 : keys값을 모두 String key변수에 반복하여 넣는다.

        if (SelectedStation.get(position).getType().equals("APT")){
            holder.frameLayoutApt.setVisibility(View.VISIBLE);
        }
        if (SelectedStation.get(position).getType().equals("OR")){
            holder.frameLayoutOr.setVisibility(View.VISIBLE);
        }


        holder.opst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,  SelectedStation.get(position).getStnName() + "오피스텔", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return SelectedStation.size();
    }

//    @Override
//    public void onClick(View v) {
//        int pos = getAdapterPosition() ;
//        switch (v.getId()) {
//            case R.id.opst_btn:
//                Toast.makeText(context, SelectedStation..getStnName();
//                break;
//            case R.id.apt_btn:
//                Toast.makeText(context, data.getTitle(), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.or_btn:
//                Toast.makeText(context, data.getContent(), Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }



    public class MyViewHolder extends ViewHolder {

        TextView textViewStnName;
        FrameLayout frameLayoutOpst;
        Button opst_btn;
        FrameLayout frameLayoutApt;
        FrameLayout frameLayoutOr;
//        TextView textViewStnType;
//        TextView textViewLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewStnName = (TextView)itemView.findViewById(R.id.station_name);
            opst_btn = itemView.findViewById(R.id.opst_btn);
            frameLayoutApt = itemView.findViewById(R.id.apt_btn);
            frameLayoutOr = itemView.findViewById(R.id.or_btn);

//            if (SelectedStation.get(position).getType() == "OPST"){
//
//            }

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition() ;
//                    if (pos != RecyclerView.NO_POSITION) {
//                        Toast.makeText(context,  SelectedStation.get(pos).getStnName() + "역을 삭제 하시겠습니까?", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }



}
