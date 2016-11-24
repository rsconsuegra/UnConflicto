package org.underpressureapps.unconflicto;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class MiHorarioViewAdapter extends RecyclerView.Adapter<MiHorarioViewAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private  List<Block> bloques = Collections.emptyList();
    private Schedule schedule = new Schedule( bloques);

    public MiHorarioViewAdapter(Context context, Schedule schedule){
        inflater = LayoutInflater.from(context);
        this.schedule = schedule;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_my_schedule,parent,true);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Block bloque = schedule.getBlock(position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
