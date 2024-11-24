package com.example.worklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {
    Button getStartedButton;
    ProgressBar progressBar;
    int progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent=new Intent(getApplicationContext(),MainActivity.class);

        getStartedButton=(Button) findViewById(R.id.getStartedButtonId);

        progressBar=(ProgressBar) findViewById(R.id.progressBarId);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doWork();
                        startMainActivity();
                    }
                });
                thread.start();
            }
        });
    }
    public void doWork(){
        for(progress=10;progress<=100;progress=progress+20){
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void startMainActivity(){
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);

        startActivity(intent);
        finish();
    }
}