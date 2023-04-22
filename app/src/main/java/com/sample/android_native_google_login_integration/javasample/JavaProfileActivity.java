package com.sample.android_native_google_login_integration.javasample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sample.android_native_google_login_integration.R;

public class JavaProfileActivity extends AppCompatActivity {

    ImageView ivUser;
    TextView tvUserValue;
    String googleData, googleProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_profile);

        initUI();

        Intent intent = getIntent();

        googleData = intent.getStringExtra("googleData");
        googleProfileImage = intent.getStringExtra("googleProfileImage");

        Glide.with(JavaProfileActivity.this)
                .load(googleProfileImage)
                .circleCrop()
                .into(ivUser);

        tvUserValue.setText(googleData);
    }

    /**
     * The function initializes the user interface by finding and assigning values to the ImageView and
     * TextView objects.
     */
    private void initUI() {
        ivUser = findViewById(R.id.ivUser);
        tvUserValue = findViewById(R.id.tvUserValue);
    }
}