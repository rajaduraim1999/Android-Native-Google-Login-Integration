package com.sample.android_native_google_login_integration.javasample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sample.android_native_google_login_integration.MainActivity;
import com.sample.android_native_google_login_integration.R;

public class JavaSampleActivity extends AppCompatActivity {

    CardView cvNativeGoogleSignIn, cvCustomGoogleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_sample);

        initUI();

        cvNativeGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JavaSampleActivity.this, NativeGoogleSignInButtonActivity.class);
                startActivity(intent);
            }
        });

        cvCustomGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JavaSampleActivity.this, CustomGoogleSignInButtonActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        cvNativeGoogleSignIn = findViewById(R.id.cvNativeGoogleSignIn);
        cvCustomGoogleSignIn = findViewById(R.id.cvCustomGoogleSignIn);
    }
}