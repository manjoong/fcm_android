package com.example.jeonse_alarm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.MyViewHolder> {

    Context context;
    ArrayList<SaleConstruct> Sale;

    public SaleAdapter(Context context, ArrayList<SaleConstruct> list) {
        super();
        this.context = context;
        this.Sale = list;
    }
    @Override
    public SaleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main_detail, parent, false);

        return new SaleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SaleAdapter.MyViewHolder holder, int position) {

        if (Sale.get(position).getImgLink().length() != 0){
            Log.d("제대로 들어갔나요", "이미지가 있어서 그림을 넣었습니다.");
            holder.imageLayout.setVisibility(View.VISIBLE);
            holder.totalLayout.setGravity(Gravity.NO_GRAVITY);
            String imageUrl = "https://landthumb-phinf.pstatic.net"+ Sale.get(position).getImgLink();
            Glide.with(context).load(imageUrl).into(holder.ImageViewSaleImgLink);
        }else{
            holder.imageLayout.setVisibility(View.GONE);
            holder.totalLayout.setGravity(Gravity.CENTER);
            holder.ImageViewSaleImgLink.setImageResource(R.mipmap.no_img_foreground);
        }

        if(Sale.get(position).getRecentSale().equals("true") && position != 0){
            holder.recSaleLayout.setVisibility(View.VISIBLE);
        }else{
            holder.recSaleLayout.setVisibility(View.GONE);
        }
        Log.d("제대로 들어갔나요", "제대로 들어갔나요?" + Sale.get(position).getName());
        holder.textViewSaleName.setText(Sale.get(position).getName());
        if(Sale.get(position).getType().equals("OPST")){
            holder.textViewSaleType.setText("오피스텔");
        }else if(Sale.get(position).getType().equals("APT")){
            holder.textViewSaleType.setText("아파트");
        }else if(Sale.get(position).getType().equals("OR")){
            holder.textViewSaleType.setText("원룸");
        }
//        holder.textViewSaleType.setText(Sale.get(position).getType());

        Integer price_1=Integer.valueOf(Sale.get(position).getPrice())/10000;
        Integer price_2=(Integer.valueOf(Sale.get(position).getPrice())%10000)/1000;
        Integer price_3=((Integer.valueOf(Sale.get(position).getPrice())%10000)%1000)/100;
        Integer price_4=(((Integer.valueOf(Sale.get(position).getPrice())%10000)%1000)%100)/10;
        String han_price="";
        int[] price = {price_1, price_2, price_3, price_4};
        String[] han_money ={"억", "천", "백", "십"};
        for(int i=0; i<4;i++ ){
            if(price[i]!=0){
                han_price = han_price + String.valueOf(price[i])+han_money[i]+ " ";
            }
        }
        han_price = "전세 "+ han_price;
        holder.textViewSalePrc.setText(han_price);


        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat hanFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
        String created_at = Sale.get(position).getCreateDt();
//        String now_at = String.valueOf(dateNow);
        String now_time_format = String.valueOf(dateFormat.format(dateNow));

        String time_context="";
        try {

            Date dateCreated = dateFormat.parse(created_at); //String을 포맷에 맞게 변경
//            Date HanCreated = hanFormat.parse(created_at); //String을 포맷에 맞게 변경
            long duration = dateNow.getTime() - dateCreated.getTime(); // 글이 올라온시간,현재시간비교
            long hour = duration/3600000;
            long min = (duration%3600000)/60000;
            if (duration<86400000){
                if (duration<3600000){
                    time_context = String.valueOf(min) + "분 전";
                }else{
                    time_context = String.valueOf(hour) + "시간 " + String.valueOf(min)+"분 전";
                }
            }else{
                if (created_at.substring(0, 4).equals(now_time_format.substring(0,4))){
                    time_context = String.valueOf(created_at.substring(5));
                }else{
                    time_context = String.valueOf(Sale.get(position).getCreateDt());
                }
            }

            Log.d("현재 시간 ", String.valueOf(now_time_format));
            Log.d("두 시간의 차이 ", now_time_format.substring(0,4));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        holder.textViewSaleTime.setText(time_context);
        holder.textViewSaleStnName.setText(Sale.get(position).getStation());


//        holder.textViewLine.setText(filteredList.get(position).getLine());
    }

    @Override
    public int getItemCount() {
        return Sale.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImageViewSaleImgLink;
        TextView textViewSaleName;
        TextView textViewSaleType;
        TextView textViewSalePrc;
        TextView textViewSaleTime;
        TextView textViewSaleStnName;
        LinearLayout imageLayout;
        LinearLayout totalLayout;
        LinearLayout recSaleLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            totalLayout = (LinearLayout) itemView.findViewById(R.id.total_layout);
            imageLayout = (LinearLayout) itemView.findViewById(R.id.image_layout);
            recSaleLayout = (LinearLayout) itemView.findViewById(R.id.rec_sale_layout);
            ImageViewSaleImgLink  = (ImageView) itemView.findViewById(R.id.sale_img_link);
            textViewSaleName  = (TextView)itemView.findViewById(R.id.sale_name);
            textViewSaleType  = (TextView)itemView.findViewById(R.id.sale_type);
            textViewSalePrc  = (TextView)itemView.findViewById(R.id.sale_prc);
            textViewSaleTime  = (TextView)itemView.findViewById(R.id.sale_time);
            textViewSaleStnName  = (TextView)itemView.findViewById(R.id.sale_stn_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
//                        Toast.makeText(context,  Sale.get(pos).getNaverLink() + "로 이동하시겠습니까?", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context,  "이미지 링크: " + Sale.get(pos).getImgLink(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(Sale.get(pos).getNaverLink()));
                        context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        }
    }



}
