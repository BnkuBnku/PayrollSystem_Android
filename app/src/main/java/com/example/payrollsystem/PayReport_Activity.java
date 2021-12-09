package com.example.payrollsystem;

import static android.widget.Toast.LENGTH_SHORT;

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

public class PayReport_Activity extends AppCompatActivity {

    private Button ReBackBtn;
    private Button ReRefreshBtn;

    private RecyclerView recycler_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_report);
        ReBackBtn = findViewById(R.id.ReportBackBtn);
        ReRefreshBtn = findViewById(R.id.reportRefreshBtn);
        recycler_r = findViewById(R.id.recycler_report);

        //Display Report List
        DisplayReport();

        ReBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayReport_Activity.this, MainActivity.class);
                PayReport_Activity.this.startActivity(i);
            }
        });

        ReRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayReport();
            }
        });

        // Layout for RecyclerView
        recycler_r.setHasFixedSize(true);
        recycler_r.setLayoutManager(new LinearLayoutManager(this)); //Left to Right.
    }

    private void DisplayReport(){
        String url = "https://cyvinhenz.xyz/DisplaySummary.php";

        RequestQueue q = Volley.newRequestQueue(PayReport_Activity.this);

        StringRequest r = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject parent = new JSONObject(response);

                            //get JSON Array Node
                            JSONArray dtrArray = parent.getJSONArray("SummaryReport");

                            int size = dtrArray.length();

                            //Create Array for each key value json object
                            String[] A1 = new String[size];
                            String[] A2 = new String[size];
                            String[] A3 = new String[size];
                            String[] A4 = new String[size];
                            String[] A5 = new String[size];
                            String[] A6 = new String[size];

                            for(int i=0; i<size; i++){
                                JSONObject ob = dtrArray.getJSONObject(i);

                                A1[i] = ob.getString("SummaryID");
                                A2[i] = ob.getString("PayReportID");
                                A3[i] = ob.getString("RollNum");
                                A4[i] = ob.getString("EmpID");
                                A5[i] = ob.getString("PayrollType");
                                A6[i] = ob.getString("PayrollDescrip");

                                GetTHEREPORT(size, A1, A2, A3, A4, A5, A6);
                            }



                        } catch (JSONException e) { //Display Error if any
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PayReport_Activity.this, "Failed to Search. \n\n" + error.getMessage(), LENGTH_SHORT).show();
            }
        });
        q.add(r);
    }

    private void GetTHEREPORT(int Count, String[] sumid, String[] payid, String[] roll, String[] Empid, String[] paytype, String[] PayDesc){
        String[] SUMAr = new String[Count];
        String[] PAYAr = new String[Count];
        String[] ROLLAr = new String[Count];
        String[] EMPAr = new String[Count];
        String[] TYPEAr = new String[Count];
        String[] DESCAr = new String[Count];

        int size = SUMAr.length;
        for(int i=0;i<size;i++){
            SUMAr[i] = sumid[i];
            PAYAr[i] = payid[i];
            ROLLAr[i] = roll[i];
            EMPAr[i] = Empid[i];
            TYPEAr[i] = paytype[i];
            DESCAr[i] = PayDesc[i];
        }

        SumReportAdapter ada = new SumReportAdapter(this,SUMAr,PAYAr,ROLLAr,EMPAr,TYPEAr,DESCAr);
        recycler_r.setAdapter(ada);
    }
}