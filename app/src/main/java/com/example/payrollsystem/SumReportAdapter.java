package com.example.payrollsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SumReportAdapter extends RecyclerView.Adapter<SumReportAdapter.SummaryViewHolder> {

    Context context;
    String[] SUMID;
    String[] PAYID;
    String[] ROLLNUM;
    String[] EMPID;
    String[] PAYTYPE;
    String[] PAYDESC;

    public SumReportAdapter(Context context, String[] SUMID, String[] PAYID, String[] ROLLNUM, String[] EMPID, String[] PAYTYPE, String[] PAYDESC) {
        this.context = context;
        this.SUMID = SUMID;
        this.PAYID = PAYID;
        this.ROLLNUM = ROLLNUM;
        this.EMPID = EMPID;
        this.PAYTYPE = PAYTYPE;
        this.PAYDESC = PAYDESC;
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder {
        //TextView
        TextView SumIDTV, PayIDTV, RollTV, EmpIDTV, PayTypeTV, PayDescTV;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initializing the Textview/s
            SumIDTV = itemView.findViewById(R.id.In_SummaryID);
            PayIDTV = itemView.findViewById(R.id.In_PayID);
            RollTV = itemView.findViewById(R.id.In_RollNum);
            EmpIDTV = itemView.findViewById(R.id.In_EmpID);
            PayTypeTV = itemView.findViewById(R.id.In_PayrollType);
            PayDescTV = itemView.findViewById(R.id.In_PayrollDescrip);
        }
    }

    @Override
    public int getItemCount() {
        return SUMID.length;
    }
    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_sumreport,parent, false);
        return new SummaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        holder.SumIDTV.setText(SUMID[position]);
        holder.PayIDTV.setText(PAYID[position]);
        holder.RollTV.setText(ROLLNUM[position]);
        holder.EmpIDTV.setText(EMPID[position]);
        holder.PayTypeTV.setText(PAYTYPE[position]);
        holder.PayDescTV.setText(PAYDESC[position]);
    }




}
