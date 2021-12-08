package com.example.payrollsystem;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EmpListAdapter extends RecyclerView.Adapter<EmpListAdapter.EmpListViewHolder> {

    Context context;
    String[] EMPID;
    String[] FIRSTN;
    String[] LASTN;
    String[] EMPAGE;
    String[] POSTT;
    String[] BANKC;
    String[] DEDUCTT;
    String[] DEDUCTD;

    public EmpListAdapter(Context context, String[] EMPID, String[] FIRSTN, String[] LASTN, String[] EMPAGE, String[] POSTT, String[] BANKC, String[] DEDUCTT, String[] DEDUCTD) {
        this.context = context;
        this.EMPID = EMPID;
        this.FIRSTN = FIRSTN;
        this.LASTN = LASTN;
        this.EMPAGE = EMPAGE;
        this.POSTT = POSTT;
        this.BANKC = BANKC;
        this.DEDUCTT = DEDUCTT;
        this.DEDUCTD = DEDUCTD;
    }

    public class EmpListViewHolder extends RecyclerView.ViewHolder {
        //eemployeeList Layout TextViews/TableLayout
        TextView tv_empidD, tv_firstn, tv_lastn, tv_empage ,tv_post, tv_bank, tv_deducttype, tv_deductdesc;
        TableLayout TableLayoutList;

        public EmpListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_empidD = itemView.findViewById(R.id.In_Employee_id);
            tv_firstn = itemView.findViewById(R.id.In_Employee_FirstN);
            tv_lastn = itemView.findViewById(R.id.In_Employee_LastN);
            tv_empage = itemView.findViewById(R.id.In_Employee_age);
            tv_post = itemView.findViewById(R.id.In_Position_title);
            tv_bank = itemView.findViewById(R.id.In_Bank_Acc_No);
            tv_deducttype = itemView.findViewById(R.id.In_Deduc_type);
            tv_deductdesc = itemView.findViewById(R.id.In_Deduc_descrip);
            TableLayoutList = itemView.findViewById(R.id.tablelayout);
            TableLayoutList.setOnLongClickListener(OnTableList);
        }

        View.OnLongClickListener OnTableList = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String empid = tv_empidD.getText().toString();
                String empf = tv_firstn.getText().toString();
                String empl = tv_lastn.getText().toString();
                String empa = tv_empage.getText().toString();
                String empp = tv_post.getText().toString();
                String empb = tv_bank.getText().toString();
                String deductt = tv_deducttype.getText().toString();
                String empd = tv_deductdesc.getText().toString();

                showUpdateDialog(empid, empf, empl, empa, empp, empb, deductt, empd);
                return false;
            }
        };
    }

    @NonNull
    @Override
    public EmpListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_eemployee_list,parent, false);
        return new EmpListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpListViewHolder holder, int position) {
        holder.tv_empidD.setText(EMPID[position]);
        holder.tv_firstn.setText(FIRSTN[position]);
        holder.tv_lastn.setText(LASTN[position]);
        holder.tv_empage.setText(EMPAGE[position]);
        holder.tv_post.setText(POSTT[position]);
        holder.tv_bank.setText(BANKC[position]);
        holder.tv_deducttype.setText(DEDUCTT[position]);
        holder.tv_deductdesc.setText(DEDUCTD[position]);
    }

    @Override
    public int getItemCount() {
        return EMPID.length;
    }


    public void showUpdateDialog(String id, String fn, String ln, String age, String position, String bank, String type, String desc){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_updateemployee);

        //Initializing the editText of the dialog.
        TextView EmpIDDialog = dialog.findViewById(R.id.UpEmpID);
        EditText InEmpF = dialog.findViewById(R.id.Update_EmpFn);
        EditText InEmpL = dialog.findViewById(R.id.Update_EmpLn);
        EditText InAge = dialog.findViewById(R.id.Update_EmpAge);
        EditText InPos = dialog.findViewById(R.id.Update_EmpPos);
        EditText InBank = dialog.findViewById(R.id.Update_EmpBank);
        EditText InDType = dialog.findViewById(R.id.Update_EmpType);
        EditText InDesc = dialog.findViewById(R.id.Update_EmpDesc);

        //Initializing the Buttons of the dialog
        Button UpUpdate = dialog.findViewById(R.id.UpUpdateBtn);
        Button UpCancel = dialog.findViewById(R.id.UpCancelBtn);

        //Set all Edit Text
        EmpIDDialog.setText(id);
        InEmpF.setText(fn);
        InEmpL.setText(ln);
        InAge.setText(age);
        InPos.setText(position);
        InBank.setText(bank);
        InDType.setText(type);
        InDesc.setText(desc);

        UpUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid = EmpIDDialog.getText().toString();
                String empfn = InEmpF.getText().toString();
                String empln = InEmpL.getText().toString();
                String empage = InAge.getText().toString();
                String emppos = InPos.getText().toString();
                String empbank = InBank.getText().toString();
                String empT = InDType.getText().toString();
                String empD = InDesc.getText().toString();

                UpdateEmployee(empid,empfn,empln,empage,emppos,empbank,empT,empD);
                dialog.dismiss();
            }
        });

        UpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void UpdateEmployee(String id, String fn, String ln, String age, String pos, String bank, String type, String desc){
        // post our data.
        String url = "https://cyvinhenz.xyz/UpdateEmployee.php";

        // creating a new variable for our request queue.
        RequestQueue q = Volley.newRequestQueue(context);

        StringRequest r = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Employee Successfully Updated!", LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Failed to Search. \n\n" + error.getMessage(), LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("Id", id);
                param.put("First", fn);
                param.put("Last", ln);
                param.put("age", age);
                param.put("position", pos);
                param.put("bankAcc", bank);
                param.put("deductype", type);
                param.put("deducDesc", desc);

                return param;
            }
        };
        q.add(r);
    }
}
