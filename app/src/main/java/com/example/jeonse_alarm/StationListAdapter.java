package com.example.jeonse_alarm;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeonse_alarm.StationListAdapter.MyViewHolder;
import com.example.jeonse_alarm.StationConstruct;

import java.util.ArrayList;
/**
 * Created by Administrator on 2017-08-07.
 */

public class StationListAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    Context context;
    ArrayList<StationConstruct> unFilteredlist;
    ArrayList<StationConstruct> filteredList;


    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
    private OnListItemSelectedInterface mListener;


    public StationListAdapter(Context context, ArrayList<StationConstruct> list, OnListItemSelectedInterface listener){
        super();
        this.context = context;
        this.unFilteredlist = list;
        this.filteredList = list;
        this.mListener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_select_station_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewName.setText(filteredList.get(position).getName());
        holder.idTextView.setText(filteredList.get(position).getStnId().toString());
//        holder.textViewLine.setText(filteredList.get(position).getLine());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class MyViewHolder extends ViewHolder {

        TextView textViewName;
        TextView idTextView;
//        TextView textViewLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.label_name);
            idTextView = (TextView)itemView.findViewById(R.id.id_text);
//            textViewLine = (TextView)itemView.findViewById(R.id.label_line);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
//                        Toast.makeText(context, "역이름: " + filteredList.get(pos).getName() + " 역 ID: " + filteredList.get(pos).getStnId() + " 호선: " + filteredList.get(pos).getLine(), Toast.LENGTH_SHORT).show();
                        mListener.onItemSelected(v, getAdapterPosition());
                    }
                }
            });
        }
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<StationConstruct> filteringList = new ArrayList<>();
                    for(StationConstruct name : unFilteredlist) {
                        if(name.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<StationConstruct>)results.values;
                notifyDataSetChanged();
            }
        };
    }

}

