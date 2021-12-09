package com.example.payrollsystem;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Payroll_Activity extends AppCompatActivity {

    private Button RefreshBtn;
    private Button BackBtn;

    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);
        RefreshBtn = findViewById(R.id.RefreshBtn);
        BackBtn = findViewById(R.id.BackBtn);
        recycler = findViewById(R.id.recycler_ee);


        DisplayEmployees();

        // Layout for RecyclerView
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this)); //Left to Right.

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Payroll_Activity.this, MainActivity.class);
                Payroll_Activity.this.startActivity(i);
            }
        });
    }

    private void DisplayEmployees(){
        // post our data.
        String url = "https://cyvinhenz.xyz/DisplayEmployees.php";

        // creating a new variable for our request queue.
        RequestQueue q = Volley.newRequestQueue(Payroll_Activity.this);

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
                        Toast.makeText(Payroll_Activity.this, "Failed to Search. \n\n" + error.getMessage(), LENGTH_SHORT).show();
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

        PayListAdapter ada = new PayListAdapter(this,ChillID,ChillFN,ChillLN,CHILLAGE,CHILLTITLE,CHILLBANK,CHILLTYPE,CHILLDESC);
        recycler.setAdapter(ada);
    }
}