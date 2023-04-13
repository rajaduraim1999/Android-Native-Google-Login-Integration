package com.sample.android_native_google_login_integration.javasample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sample.android_native_google_login_integration.R;

public class GoogleSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleSignIn";

    SignInButton sibGoogleSignIn;
    CardView cvGoogleSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        initUI();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sibGoogleSignIn);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(this);
        cvGoogleSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sibGoogleSignIn:
            case R.id.cvGoogleSignIn:
                signIn();
                break;
        }
    }

    private void initUI() {
        sibGoogleSignIn = findViewById(R.id.sibGoogleSignIn);
        cvGoogleSignIn = findViewById(R.id.cvGoogleSignIn);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.e("TAG", "updateUI : account " + account);
        if (account != null) {

            String personId = account.getId();
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personToken = account.getIdToken();
            String personPhoto = account.getPhotoUrl().toString();

            String googleData =  " personId : " + personId  +"\n\n"+ " personName : " + personName  +"\n\n"+ " personGivenName : " + personGivenName  +"\n\n"+ " personFamilyName : " + personFamilyName  +"\n\n"+ " personEmail : " + personEmail  +"\n\n"+ " personToken : " + personToken  +"\n\n"+ " personPhoto : " + personPhoto;
            Log.e("TAG", "updateUI : data : account : " + googleData);

            Intent intent    =   new Intent(GoogleSignInActivity.this,ProfileActivity.class);
            intent.putExtra("googleData",googleData);
            intent.putExtra("googleProfileImage", personPhoto);
            startActivity(intent);
            disconnectFromGoogle();

        } else {
            Log.e("TAG", "Login failed!");
        }
    }

    private void disconnectFromGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Signed out successfully
                        Log.e(TAG, "Google Sign out successfully");
                    }
                });
    }
}