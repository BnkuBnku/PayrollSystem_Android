package com.example.payrollsystem;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Employee_Activity extends AppCompatActivity {

    private static final String TAG = "Employee_Activity";

    private TableLayout Tavle;

    private Button EmpBack;
    private Button EmpRefresh;
    private Button EmpDelete;
    private Button EmpAdd;

    private RecyclerView recycler_emp;

    private TextView EmpID;

    private TextView EmpF;
    private TextView EmpL;
    private TextView EmpAge;
    private TextView EmpPost;
    private TextView EmpBank;
    private TextView EmpType;
    private TextView EmpDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        //Initializing the important components
        //RecyclerView
        recycler_emp = findViewById(R.id.recycler_emp);

        //Button
        EmpBack = findViewById(R.id.EmpBackBtn);
        EmpRefresh = findViewById(R.id.EmpRefreshBtn);
        EmpDelete = findViewById(R.id.empDeleteBtn);
        EmpAdd = findViewById(R.id.empAddBtn);

        //TextView
        EmpID = findViewById(R.id.In_Employee_id);
        EmpF = findViewById(R.id.In_Employee_FirstN);
        EmpL = findViewById(R.id.In_Employee_LastN);
        EmpAge = findViewById(R.id.In_Employee_age);
        EmpPost = findViewById(R.id.In_Position_title);
        EmpBank = findViewById(R.id.In_Bank_Acc_No);
        EmpType = findViewById(R.id.In_Deduc_type);
        EmpDesc = findViewById(R.id.In_Deduc_descrip);

        Tavle = findViewById(R.id.tablelayout);

        //Displays Employees
        DisplayEmployee();

        //Listener for BackBtn
        EmpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Employee_Activity.this, MainActivity.class);
                Employee_Activity.this.startActivity(i);
            }
        });

        //Listener for RefreshBtn
        EmpRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayEmployee();
            }
        });

        EmpDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        EmpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        // Layout for RecyclerView
        recycler_emp.setHasFixedSize(true);
        recycler_emp.setLayoutManager(new LinearLayoutManager(this)); //Left to Right.

    }

    //Add Dialog
    public void showAddDialog(){
        final Dialog dialog = new Dialog(Employee_Activity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_updateemployee);

        //Initializing the editText of the dialog.
        EditText InEmpF = dialog.findViewById(R.id.Update_EmpFn);
        EditText InEmpL = dialog.findViewById(R.id.Update_EmpLn);
        EditText InAge = dialog.findViewById(R.id.Update_EmpAge);
        EditText InPos = dialog.findViewById(R.id.Update_EmpPos);
        EditText InBank = dialog.findViewById(R.id.Update_EmpBank);
        EditText InDType = dialog.findViewById(R.id.Update_EmpType);
        EditText InDesc = dialog.findViewById(R.id.Update_EmpDesc);

        //Initializing the Button of the dialog.
        Button AddSave = dialog.findViewById(R.id.UpUpdateBtn);
        Button AddCancel = dialog.findViewById(R.id.UpCancelBtn);

        AddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = InEmpF.getText().toString();
                String ln = InEmpL.getText().toString();
                String age = InAge.getText().toString();
                String pos = InPos.getText().toString();
                String bnk = InBank.getText().toString();
                String dtyp = InDType.getText().toString();
                String desc = InDesc.getText().toString();
                InsertEmp(fn, ln, age, pos, bnk, dtyp, desc);
                DisplayEmployee();
                dialog.dismiss();
            }
        });

        AddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void showDeleteDialog(){
        final Dialog dialog = new Dialog(Employee_Activity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_deleteemployee);

        //Initializing EditText of the dialog.
        EditText empid = dialog.findViewById(R.id.editDeleteEmpID);

        //Initializing Button of the dialog.
        Button DeleteB = dialog.findViewById(R.id.DeleteDeleteBtn);
        Button CancelB = dialog.findViewById(R.id.DeleteCancelBtn);

        DeleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = empid.getText().toString();
               if(TextUtils.isEmpty(id)){
                   Toast.makeText(Employee_Activity.this, "The Number Field is Empty! \n Please input ID" , LENGTH_SHORT).show();
               }
               else{
                   DeleteEmp(id);
                   DisplayEmployee();
                   dialog.dismiss();
               }
            }
        });

        CancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    //Insert Employee
    private void InsertEmp(String fn, String ln, String age, String pos, String bank, String Dt, String Desc){
        String url = "https://cyvinhenz.xyz/InsertEmployee.php";

        RequestQueue q = Volley.newRequestQueue(Employee_Activity.this);

        StringRequest r = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Toast.makeText(Employee_Activity.this, "Added Employee Successfully" , LENGTH_SHORT).show();
                        }
                        catch (Exception e){
                            Toast.makeText(Employee_Activity.this, "Exception Error \n\n" + e.getMessage(), LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Employee_Activity.this, "Volley Error \n\n" + error.getMessage(), LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("Employee_FirstN",fn);
                param.put("Employee_LastN",ln);
                param.put("Employee_age",age);
                param.put("Position_title",pos);
                param.put("Bank_Acc_No",bank);
                param.put("Deduc_type", Dt);
                param.put("Deduc_descrip",Desc);
                return param;
            }
        };

        q.add(r);
    }

    //Delete Emp
    private void DeleteEmp(String ID){
        String url = "https://cyvinhenz.xyz/DeleteEmployees.php";

        RequestQueue q = Volley.newRequestQueue(Employee_Activity.this);

        StringRequest r = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Toast.makeText(Employee_Activity.this, "Employee Successfully Deleted!", LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(Employee_Activity.this, "Exception Error " + e.getMessage(), LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Employee_Activity.this, "Volley Error \n" + error.getMessage(), LENGTH_SHORT).show();
                }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("ID", ID);
                return param;
            }
        };
        q.add(r);
    }

    private void DisplayEmployee(){
        // post our data.
        String url = "https://cyvinhenz.xyz/DisplayEmployees.php";

        // creating a new variable for our request queue.
        RequestQueue q = Volley.newRequestQueue(Employee_Activity.this);

        StringRequest r = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject parent = new JSONObject(response);

                            //get JSON Array Node
                            JSONArray EmployeesArr = parent.getJSONArray("Employees");

                            int size = EmployeesArr.length();
                            //Create Array for each key value json object
                            String[] A1 = new String[size];
                            String[] A2 = new String[size];
                            String[] A3 = new String[size];
                            String[] A4 = new String[size];
                            String[] A5 = new String[size];
                            String[] A6 = new String[size];
                            String[] A7 = new String[size];
                            String[] A8 = new String[size];

                            for(int i=0; i<size; i++){
                                JSONObject ah = EmployeesArr.getJSONObject(i);

                                A1[i] = ah.optString("Employee_id");
                                A2[i] = ah.optString("Employee_FirstN");
                                A3[i] = ah.optString("Employee_LastN");
                                A4[i] = ah.optString("Employee_age");
                                A5[i] = ah.optString("Position_title");
                                A6[i] = ah.optString("Bank_Acc_No");
                                A7[i] = ah.optString("Deduc_type");
                                A8[i] = ah.optString("Deduc_descrip");


                                GetEmployees(size, A1, A2, A3, A4, A5, A6, A7, A8);
                            }



                        } catch (JSONException e) { //Display Error if any
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Employee_Activity.this, "Failed to Search. \n\n" + error.getMessage(), LENGTH_SHORT).show();
                    }
                });
        q.add(r);
    }

    private void GetEmployees(int Count, String[] A1, String[] A2, String[] A3, String[] A4, String[] A5, String[] A6, String[] A7, String[] A8){
        String[] ChillID = new String[Count];
        String[] ChillFN = new String[Count];
        String[] ChillLN = new String[Count];
        String[] CHILLAGE = new String[Count];
        String[] CHILLTITLE = new String[Count];
        String[] CHILLBANK = new String[Count];
        String[] CHILLTYPE = new String[Count];
        String[] CHILLDESC = new String[Count];

        int size = ChillID.length;

        for(int i=0;i<size;i++){
            ChillID[i] = A1[i];
            ChillFN[i] = A2[i];
            ChillLN[i] = A3[i];
            CHILLAGE[i] = A4[i];
            CHILLTITLE[i] = A5[i];
            CHILLBANK[i] = A6[i];
            CHILLTYPE[i] = A7[i];
            CHILLDESC[i] = A8[i];
        }

        EmpListAdapter ada = new EmpListAdapter(this,ChillID,ChillFN,ChillLN,CHILLAGE,CHILLTITLE,CHILLBANK,CHILLTYPE,CHILLDESC);
        recycler_emp.setAdapter(ada);
    }
}