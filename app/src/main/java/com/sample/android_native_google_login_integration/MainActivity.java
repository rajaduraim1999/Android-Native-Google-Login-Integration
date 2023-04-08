package com.sample.android_native_google_login_integration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sample.android_native_google_login_integration.R;
import com.sample.android_native_google_login_integration.javasample.JavaSampleActivity;

public class MainActivity extends AppCompatActivity {

    CardView cvJava, cvKotlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        cvJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JavaSampleActivity.class);
                startActivity(intent);
            }
        });

        cvKotlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, JavaSampleActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void initUI() {
        cvJava = findViewById(R.id.cvJava);
        cvKotlin = findViewById(R.id.cvKotlin);
    }
}