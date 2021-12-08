package com.example.payrollsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeListViewHolder> {

    Context context;
    String[] Empid;
    String[] Firstn;
    String[] Lastn;
    String[] Empage;
    String[] Postt;
    String[] Bankc;
    String[] Deductt;
    String[] Deductd;

    public EmployeeListAdapter(Context context, String[] empid, String[] firstn, String[] lastn, String[] empage, String[] postt, String[] bankc, String[] deductt, String[] deductd) {
        this.context = context;
        Empid = empid;
        Firstn = firstn;
        Lastn = lastn;
        Empage = empage;
        Postt = postt;
        Bankc = bankc;
        Deductt = deductt;
        Deductd = deductd;
    }

    public static class EmployeeListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_empid, tv_firstn, tv_lastn, tv_empage ,tv_post, tv_bank, tv_deducttype, tv_deductdesc;

        TableLayout TableLayoutList;

        public EmployeeListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_empid = itemView.findViewById(R.id.In_EmplD);
            tv_firstn = itemView.findViewById(R.id.In_Employee_FirstN);
            tv_lastn = itemView.findViewById(R.id.In_Employee_LastN);
            tv_empage = itemView.findViewById(R.id.In_Employee_age);
            tv_post = itemView.findViewById(R.id.In_Position_title);
            tv_bank = itemView.findViewById(R.id.In_Bank_Acc_No);
            tv_deducttype = itemView.findViewById(R.id.In_Deduc_type);
            tv_deductdesc = itemView.findViewById(R.id.In_Deduc_descrip);
            TableLayoutList = itemView.findViewById(R.id.tablelayout);
        }
    }

    @Override
    public int getItemCount() {
        return Empid.length;
    }

    @NonNull
    @Override
    public EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_eemployee_list,parent, false);
        return new EmployeeListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListViewHolder holder, int position) {
        holder.tv_empid.setText(Empid[position]);
        holder.tv_firstn.setText(Firstn[position]);
        holder.tv_lastn.setText(Lastn[position]);
        holder.tv_empage.setText(Empage[position]);
        holder.tv_post.setText(Postt[position]);
        holder.tv_bank.setText(Bankc[position]);
        holder.tv_deducttype.setText(Deductt[position]);
        holder.tv_deductdesc.setText(Deductd[position]);
    }
}
