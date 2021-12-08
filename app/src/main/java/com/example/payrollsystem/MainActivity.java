package com.example.payrollsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button EmpActivityButton;
    private Button PayrollActivityButton;
    private Button DTRActivityButton;
    private Button PayReportActivityButton;
    private Button BackLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomeadmin);
        EmpActivityButton = findViewById(R.id.EmpActBtn);
        PayrollActivityButton = findViewById(R.id.PayActBtn);
        DTRActivityButton = findViewById(R.id.DtrActBtn);
        PayReportActivityButton = findViewById(R.id.PayPortActBtn);
        BackLoginButton = findViewById(R.id.BackLoginBtn);

        DTRActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, DtrActivity.class);
                MainActivity.this.startActivity(a);
            }
        });

        PayrollActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, Payroll_Activity.class);
                MainActivity.this.startActivity(a);
            }
        });

        EmpActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this,Employee_Activity.class);
                MainActivity.this.startActivity(a);
            }
        });

        PayReportActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, PayReport_Activity.class);
                MainActivity.this.startActivity(a);
            }
        });

        BackLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(a);
            }
        });

    }
}