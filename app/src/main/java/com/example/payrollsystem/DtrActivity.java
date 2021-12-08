package com.example.payrollsystem;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.*;

public class DtrActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Context context;

    private TextView FromDisplayDate;
    private TextView ToDisplayDate;
    private TextView DDate;
    private TextView Total;

    private DatePickerDialog.OnDateSetListener FromDateSetListener;
    private DatePickerDialog.OnDateSetListener ToDateSetListener;

    private Button RefreshBtn;
    private Button ExitBtn;
    private Button EnterBtn;

    private EditText EmpIdText;

    private RecyclerView recycler_v;

    private int SumofTotal = 0;
    private int ValueOfWork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtr);
        FromDisplayDate = findViewById(R.id.tv_fromDate);
        ToDisplayDate = findViewById(R.id.tv_toDate);
        DDate = findViewById(R.id.tv_date);
        RefreshBtn = findViewById(R.id.refresh_btn);
        ExitBtn = findViewById(R.id.btn_Exit);
        EnterBtn = findViewById(R.id.btn_enter);
        EmpIdText = findViewById(R.id.editTB_EmpID);
        recycler_v = findViewById(R.id.recycler_v);
        Total = findViewById(R.id.TV_Total);

        // Current Date Display
        Calendar cal = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        DDate.setText(currentDate);

        // Layout for RecyclerView
        recycler_v.setHasFixedSize(true);
        recycler_v.setLayoutManager(new LinearLayoutManager(this)); //Left to Right.

        // Listenerr for EnterButton
        EnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the Edit Text field empty or not.
                if(TextUtils.isEmpty(EmpIdText.getText().toString())){
                    Toast.makeText(DtrActivity.this, "Please Input ID First", Toast.LENGTH_LONG).show();
                }
                else
                {
                    getDTREmployees(EmpIdText.getText().toString(),
                            FromDisplayDate.getText().toString(),
                            ToDisplayDate.getText().toString());
                }
            }
        });

        //Listener for RefreshBtn
        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Total.setText("0");
                SumofTotal = 0;
                EmpIdText.setText("");
                FromDisplayDate.setText("Select Date");
                ToDisplayDate.setText("Select Date");
                Toast.makeText(DtrActivity.this,"Refreshed!", LENGTH_SHORT).show();
            }
        });

        //Listener for ExitBtn
        ExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DtrActivity.this, MainActivity.class);
                DtrActivity.this.startActivity(i);
            }
        });

        // Listener for From Date
        FromDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DtrActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        FromDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // Listener for To Date
        ToDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DtrActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        ToDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // Displays after click/tap FromDate TextView
        FromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateset: yyy/mm/dd: " + year + "/" + month + "/" + dayOfMonth);

                String date  = year + "-" + month + "-" + dayOfMonth;
                FromDisplayDate.setText(date);
                Toast.makeText(DtrActivity.this, FromDisplayDate.getText().toString(), Toast.LENGTH_SHORT).show(); // CHECK THE VALUE
            }
        };

        // Displays after click/tap ToDate TextView
        ToDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateset: yyy/mm/dd: " + year + "/" + month + "/" + dayOfMonth);

                String date  = year + "-" + month + "-" + dayOfMonth;
                ToDisplayDate.setText(date);
                Toast.makeText(DtrActivity.this, ToDisplayDate.getText().toString(), Toast.LENGTH_SHORT).show(); // CHECK THE VALUE
            }
        };
    }

    private void getDTREmployees(String Id, String from, String to){
        // post our data.
        String url = "https://cyvinhenz.xyz/SearchDTR.php";

        // creating a new variable for our request queue.
        RequestQueue q = Volley.newRequestQueue(DtrActivity.this);

        StringRequest r = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject parent = new JSONObject(response);

                            //get JSON Array Node
                            JSONArray dtrArray = parent.getJSONArray("DTREmployees");

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

                                A1[i] = ob.getString("DtrID");
                                A2[i] = ob.getString("EmplD");
                                A3[i] = ob.getString("WorkDate");
                                A4[i] = ob.getString("IN_Dtime");
                                A5[i] = ob.getString("OUT_Dtime");
                                A6[i] = ob.getString("TotalHours");

                                //Sum of All Total Hours.
                                ValueOfWork = Integer.parseInt(A6[i]);
                                SumofTotal+=ValueOfWork;
                                String sum = String.valueOf(SumofTotal);
                                Total.setText(sum);


                                GetDTR(size, A1, A2, A3, A4, A5, A6);
                            }



                        } catch (JSONException e) { //Display Error if any
                            Toast.makeText(DtrActivity.this,"ERROR \n\n" + e.getMessage(), LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DtrActivity.this, "Failed to Search. \n\n" + error.getMessage(), LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("EmpID", Id);
                param.put("FromDate", from);
                param.put("ToDate", to);
                return param;
            }
        };
        q.add(r);
    }

    private void GetDTR(int Count, String[] dtrid, String[] empid, String[] workdate, String[] timein, String[] timeout, String[] totalH){
        String[] DTRIDAr = new String[Count];
        String[] EMPIDAr = new String[Count];
        String[] WORKAr = new String[Count];
        String[] INAr = new String[Count];
        String[] OUTAr = new String[Count];
        String[] TOTALAr = new String[Count];

        int size = DTRIDAr.length;
        for(int i=0;i<size;i++){
            DTRIDAr[i] = dtrid[i];
            EMPIDAr[i] = empid[i];
            WORKAr[i] = workdate[i];
            INAr[i] = timein[i];
            OUTAr[i] = timeout[i];
            TOTALAr[i] = totalH[i];
        }

        DTRAdapter ada = new DTRAdapter(this,DTRIDAr,EMPIDAr,WORKAr,INAr,OUTAr,TOTALAr);
        recycler_v.setAdapter(ada);
    }
}