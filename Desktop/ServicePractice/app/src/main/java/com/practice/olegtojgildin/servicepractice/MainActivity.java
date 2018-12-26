package com.practice.olegtojgildin.servicepractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button startService;
    Button startSecondActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
        initListener();
    }

    public void initviews() {
        startService = findViewById(R.id.startService);
        startSecondActivity = findViewById(R.id.startSecondActivity);
    }

    public void initListener() {
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(MyService.newIntent(MainActivity.this));
                Toast.makeText(MainActivity.this, "Service start", Toast.LENGTH_LONG).show();

            }
        });
        startSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SecondActivity.newIntent(MainActivity.this));
            }
        });
    }
}
