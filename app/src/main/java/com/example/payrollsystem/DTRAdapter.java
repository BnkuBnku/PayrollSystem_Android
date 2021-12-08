package com.example.payrollsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DTRAdapter extends RecyclerView.Adapter<DTRAdapter.DTRViewHolder> {

    Context context;
    String [] DTRID;
    String [] EMPID;
    String [] WORKD;
    String [] TIMEIN;
    String [] TIMEOUT;
    String [] TOTALHOURS;

    public DTRAdapter(Context context, String[] DTRID, String[] EMPID, String[] WORKD, String[] TIMEIN, String[] TIMEOUT, String[] TOTALHOURS) {
        this.context = context;
        this.DTRID = DTRID;
        this.EMPID = EMPID;
        this.WORKD = WORKD;
        this.TIMEIN = TIMEIN;
        this.TIMEOUT = TIMEOUT;
        this.TOTALHOURS = TOTALHOURS;
    }

    public class DTRViewHolder extends RecyclerView.ViewHolder {
        //variable for textView/s
        TextView tv_dtrid, tv_empid, tv_workd, tv_in, tv_out, tv_total, tot;

        public DTRViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dtrid = itemView.findViewById(R.id.In_DtrID);
            tv_empid = itemView.findViewById(R.id.In_EmplD);
            tv_workd = itemView.findViewById(R.id.In_WorkDate);
            tv_in = itemView.findViewById(R.id.In_IN_Dtime);
            tv_out = itemView.findViewById(R.id.In_OUT_Dtime);
            tv_total = itemView.findViewById(R.id.In_TotalHours);

        }
    }

    @Override
    public int getItemCount() {
        return DTRID.length;
    }

    @NonNull
    @Override
    public DTRAdapter.DTRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dtr,parent, false);
        return new DTRViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DTRAdapter.DTRViewHolder holder, int position) {
        holder.tv_dtrid.setText(DTRID[position]);
        holder.tv_empid.setText(EMPID[position]);
        holder.tv_workd.setText(WORKD[position]);
        holder.tv_in.setText(TIMEIN[position]);
        holder.tv_out.setText(TIMEOUT[position]);
        holder.tv_total.setText(TOTALHOURS[position]);
    }
}
