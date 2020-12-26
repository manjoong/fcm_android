package com.example.jeonse_alarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.jeonse_alarm.SelectedStationListRenewAdapter.MyViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-07.
 */

public class SelectedStationListRenewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    private OnItemClick mCallback;
    ArrayList<SelectedStationConstructRenew> SelectedStation;

    public SelectedStationListRenewAdapter(Context context, ArrayList<SelectedStationConstructRenew> list, OnItemClick listener) {
        super();
        this.context = context;
        this.SelectedStation = list;
        this.mCallback = listener;
    }

    public interface OnItemClick {
        void onClick (View view, Integer position, String type);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_selected_station_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewStnName.setText(SelectedStation.get(position).getStnName());
        holder.textViewStnId.setText(SelectedStation.get(position).getStn_id());
        Log.d("역의 ID가 왜 안나 ", holder.textViewStnId.getText().toString() + SelectedStation.get(position).getStn_id());

        if (SelectedStation.get(position).getOpst_check()){
            holder.LayOutOpst.setVisibility(View.VISIBLE);
        }
        //신형 for문 : keys값을 모두 String key변수에 반복하여 넣는다.

        if (SelectedStation.get(position).getApt_check()){
            holder.LayOutApt.setVisibility(View.VISIBLE);
        }
        if (SelectedStation.get(position).getOr_check()){
            holder.LayOutOr.setVisibility(View.VISIBLE);
        }

        holder.BtnOpst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,  SelectedStation.get(position).getStnName() + "오피스텔", Toast.LENGTH_SHORT).show();
                holder.LayOutOpst.setVisibility(View.GONE);
                mCallback.onClick(v, position, "OPST");
            }
        });
        holder.BtnApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,  SelectedStation.get(position).getStnName() + "아파트", Toast.LENGTH_SHORT).show();
                holder.LayOutApt.setVisibility(View.GONE);
                mCallback.onClick(v, position,"APT");
            }
        });
        holder.BtnOr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,  SelectedStation.get(position).getStnName() + "원룸", Toast.LENGTH_SHORT).show();
                holder.LayOutOr.setVisibility(View.GONE);
                mCallback.onClick(v, position, "OR");
            }
        });

    }

    @Override
    public int getItemCount() {
        return SelectedStation.size();
    }

    public class MyViewHolder extends ViewHolder {

        TextView textViewStnName;
        TextView textViewStnId;
        Button BtnOpst;
        Button BtnApt;
        Button BtnOr;
        FrameLayout LayOutOpst;
        FrameLayout LayOutApt;
        FrameLayout LayOutOr;
//        TextView textViewStnType;
//        TextView textViewLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewStnName = (TextView)itemView.findViewById(R.id.station_name);
            textViewStnId = (TextView)itemView.findViewById(R.id.station_id);
            BtnOpst = itemView.findViewById(R.id.opst_btn);
            BtnApt = itemView.findViewById(R.id.apt_btn);
            BtnOr = itemView.findViewById(R.id.or_btn);
            LayOutOpst = itemView.findViewById(R.id.opst_layout);
            LayOutApt = itemView.findViewById(R.id.apt_layout);
            LayOutOr = itemView.findViewById(R.id.or_layout);
//            textViewStnType = (TextView)itemView.findViewById(R.id.station_type);
//            textViewLine = (TextView)itemView.findViewById(R.id.label_line);

//            if (SelectedStation.get(position).getType() == "OPST"){
//
//            }


//            frameLayoutOpst.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition() ;
//                    if (pos != RecyclerView.NO_POSITION) {
//                        Toast.makeText(context, SelectedStation.get(pos).getStnName() + "오피스텔 ", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });



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
