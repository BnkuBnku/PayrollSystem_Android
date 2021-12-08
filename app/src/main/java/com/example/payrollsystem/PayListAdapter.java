package com.example.payrollsystem;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PayListAdapter extends RecyclerView.Adapter<PayListAdapter.ListViewHolder> {

    Context context;
    String[] EMPID;
    String[] FIRSTN;
    String[] LASTN;
    String[] EMPAGE;
    String[] POSTT;
    String[] BANKC;
    String[] DEDUCTT;
    String[] DEDUCTD;

    public PayListAdapter(Context context, String[] empid, String[] firstn, String[] lastn, String[] empage, String[] postt, String[] bankc, String[] deductt, String[] deductd) {
        this.context = context;
        EMPID = empid;
        FIRSTN = firstn;
        LASTN = lastn;
        EMPAGE = empage;
        POSTT = postt;
        BANKC = bankc;
        DEDUCTT = deductt;
        DEDUCTD = deductd;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        //eemployeeList Layout TextViews/TableLayout
        TextView tv_empidD, tv_firstn, tv_lastn, tv_empage ,tv_post, tv_bank, tv_deducttype, tv_deductdesc;
        TableLayout TableLayoutList;

        public ListViewHolder(@NonNull View itemView) {
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
                String bank = tv_bank.getText().toString();
                String deductt = tv_deducttype.getText().toString();

                showAddPayrollDialog(empid, empf, empl, bank, deductt);
                return false;
            }
        };
    }

    @Override
    public int getItemCount() {
        return EMPID.length;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_eemployee_list,parent, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.tv_empidD.setText(EMPID[position]);
        holder.tv_firstn.setText(FIRSTN[position]);
        holder.tv_lastn.setText(LASTN[position]);
        holder.tv_empage.setText(EMPAGE[position]);
        holder.tv_post.setText(POSTT[position]);
        holder.tv_bank.setText(BANKC[position]);
        holder.tv_deducttype.setText(DEDUCTT[position]);
        holder.tv_deductdesc.setText(DEDUCTD[position]);
    }

    //THE DEEPER IN ADAPTER//
    public void showAddPayrollDialog(String INID, String INFirst, String INLast,String INBank, String INDeductType){
        final Dialog dialog = new Dialog(context);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_addpayroll);
        //Initializing the views of the dialog.
        TextView EmploID = dialog.findViewById(R.id.tv_Employee_INDE);
        TextView Rollnum = dialog.findViewById(R.id.tv_Rollnumber);
        TextView FirstN = dialog.findViewById(R.id.tv_FirstNames);
        TextView LastN = dialog.findViewById(R.id.tv_LastNames);
        TextView TotalS = dialog.findViewById(R.id.tv_TotalSalarys);
        TextView BasicP = dialog.findViewById(R.id.tv_BasicPays);
        TextView SS = dialog.findViewById(R.id.tv_SSSS);
        TextView OtherTax = dialog.findViewById(R.id.tv_OtherTaxess);
        TextView TotalDeduct = dialog.findViewById(R.id.tv_TotalDeductionss);
        TextView NeTPay = dialog.findViewById(R.id.tv_NetPays);
        EditText WorkHours = dialog.findViewById(R.id.editTextWorkHourss);
        EditText Allowance = dialog.findViewById(R.id.editTextAllows);
        EditText Adjustments = dialog.findViewById(R.id.editTextAdjusts);
        RadioButton PayHand = dialog.findViewById(R.id.RadioPayHand);
        RadioButton PayBank = dialog.findViewById(R.id.RadioPayBank);
        Button submitButton = dialog.findViewById(R.id.SaveBtn);
        Button CancelButton = dialog.findViewById(R.id.CancelBtn);
        Button calcButton = dialog.findViewById(R.id.CalcBtn);

        //BANK SEARCHING IF NULL OR NOT
        String BankAccount = INBank;

        //Listener for PayBank RadioBtn
        PayBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayHand.setChecked(false);
            }
        });

        //Listener for Payhand RadioBtn
        PayHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayBank.setChecked(false);
            }
        });

        //Listener for CancelButton
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = Integer.parseInt(WorkHours.getText().toString());
                int allow = Integer.parseInt(Allowance.getText().toString());
                int adjust = Integer.parseInt(Adjustments.getText().toString());
                int Total = ((hours * 50) + allow) - adjust;

                TotalS.setText(String.valueOf(Total));

                int Deduct = Integer.parseInt(TotalDeduct.getText().toString());
                int Net = Total - Deduct;

                NeTPay.setText(String.valueOf(Net));
            }
        });

        //Insert to Dialog section
        //---------------------------------------------------------------------------------------//

            //----------------------------------------------------------------------------//
            //Get the RollNum on PayrollTable
                String url = "https://cyvinhenz.xyz/FetchRollNum.php";

                RequestQueue q = Volley.newRequestQueue(context);

                StringRequest r = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject parent = new JSONObject(response);

                                    JSONArray RollAr = parent.getJSONArray("RollNumber");

                                    JSONObject ah = RollAr.getJSONObject(0);

                                    String A1 = ah.getString("RollNum");

                                    //Display on Rollnum Textview
                                    Rollnum.setText(A1);
                                }
                                catch (Exception e){
                                    Toast.makeText(context,"Exception Error \n" + e.getMessage(), LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error \n" + error.getMessage(), LENGTH_SHORT).show();
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("EmpID",INID);
                        return param;
                    }
                };
                q.add(r);
             //----------------------------------------------------------------------------//


        EmploID.setText(INID);

        FirstN.setText(INFirst);
        LastN.setText(INLast);

        if (INDeductType == "SSS"){
            SS.setText("20");
            OtherTax.setText("0");
        }
        else {
            SS.setText("0");
            OtherTax.setText("15");
        }

        int SSS = Integer.parseInt(SS.getText().toString());
        int Other = Integer.parseInt(OtherTax.getText().toString());
        int TDeduct = SSS + Other;
        TotalDeduct.setText(String.valueOf(TDeduct));

        //--------------------------------------------------------------------------------------//


        //Listener for SubmitButton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmpID = EmploID.getText().toString();
                String Roll = Rollnum.getText().toString();
                String First = FirstN.getText().toString();
                String Last = LastN.getText().toString();
                String WorkH = WorkHours.getText().toString();
                String Basic = BasicP.getText().toString();
                String Salary = TotalS.getText().toString();
                String Allow = Allowance.getText().toString();
                String Adjust = Adjustments.getText().toString();
                String SSF = SS.getText().toString();
                String Other = OtherTax.getText().toString();
                String TDeduct = TotalDeduct.getText().toString();
                String NetPAY = NeTPay.getText().toString();
                String Bankis = "";
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat;
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(cal.getTime());




                if(TextUtils.isEmpty(WorkH) || TextUtils.isEmpty(Allow) || TextUtils.isEmpty(Adjust)){
                    Toast.makeText(context, "Please Fill The Form Completely", LENGTH_SHORT).show();
                }
                else{
                    if (PayBank.isChecked()){
                        if(BankAccount.isEmpty()){
                            Toast.makeText(context, "Employee has no Bank Account, Please Select Pay on Hand option! \n", LENGTH_SHORT).show();
                        }else{
                            Bankis = "Yes";
                            AddPayrollReport(EmpID,
                                    Roll,
                                    First,
                                    Last,
                                    WorkH,
                                    Basic,
                                    Salary,
                                    Allow,
                                    Adjust,
                                    SSF,
                                    Other,
                                    TDeduct,
                                    NetPAY,
                                    Bankis,
                                    date);
                            dialog.dismiss();
                        }

                    }
                    else if (PayHand.isChecked()){
                        Bankis = "No";
                        AddPayrollReport(EmpID,
                                Roll,
                                First,
                                Last,
                                WorkH,
                                Basic,
                                Salary,
                                Allow,
                                Adjust,
                                SSF,
                                Other,
                                TDeduct,
                                NetPAY,
                                Bankis,
                                date);
                        dialog.dismiss();
                    }
                    else {
                        Toast.makeText(context, "Please Select One of Payment Option", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }




    private void AddPayrollReport(String ID, String Roll, String EmpF, String EmpL, String Work, String Basic, String Salary, String Allow, String Adjust, String SSS, String Taxes, String Total, String EmpNet, String PayBank, String Time){
        // post our data.
        String url = "https://cyvinhenz.xyz/AddPayrollReport.php";

        // creating a new variable for our request queue.
        RequestQueue q = Volley.newRequestQueue(context);

        StringRequest r = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Successfully Added!", LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Saved Failed. \n\n" + error.getMessage(), LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("EmpID", ID);
                param.put("RollNum", Roll);
                param.put("EmpFirst", EmpF);
                param.put("EmpLast", EmpL);
                param.put("EmpWork", Work);
                param.put("EmpBasic", Basic);
                param.put("EmpSalary", Salary);
                param.put("EmpAllow", Allow);
                param.put("EmpAdjust", Adjust);
                param.put("EmpSSS", SSS);
                param.put("OtherTaxes", Taxes);
                param.put("Total", Total);
                param.put("EmpNet", EmpNet);
                param.put("PayBank", PayBank);
                param.put("Time", Time);
                return param;
            }
        };
        q.add(r);
    }



}
