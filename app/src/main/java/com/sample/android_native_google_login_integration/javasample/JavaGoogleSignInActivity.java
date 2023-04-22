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

public class JavaGoogleSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleSignIn";

    SignInButton sibGoogleSignIn;
    CardView cvGoogleSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_google_sign_in);

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

    /**
     * The function initializes UI elements for Google sign-in.
     */
    private void initUI() {
        sibGoogleSignIn = findViewById(R.id.sibGoogleSignIn);
        cvGoogleSignIn = findViewById(R.id.cvGoogleSignIn);
    }

    /**
     * The function handles the click event for Google sign-in button and card view.
     *
     * @param v The parameter "v" is the View object that was clicked by the user, which triggered the
     * onClick() method to be called. In this case, the method is checking if the ID of the clicked
     * view matches either R.id.sibGoogleSignIn or R.id.cvGoogleSignIn, and if so,
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sibGoogleSignIn:
            case R.id.cvGoogleSignIn:
                signIn();
                break;
        }
    }

    /**
     * This function initiates the Google sign-in process and starts an activity to handle the sign-in
     * intent.
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * This function handles the result of a Google sign-in intent.
     *
     * @param requestCode An integer code that identifies the request. This code is used to match the
     * result to the correct request when receiving the result in the onActivityResult() method.
     * @param resultCode The result code returned by the activity that was launched by
     * startActivityForResult() method. It indicates whether the activity was successful or not. The
     * possible values are RESULT_OK, RESULT_CANCELED, or any custom result code set by the launched
     * activity.
     * @param data The Intent data parameter in the onActivityResult method is the data returned from
     * the activity launched by startActivityForResult. In this specific code snippet, it is the data
     * returned from the GoogleSignInClient.getSignInIntent() method. The data contains information
     * about the user's Google account, such as the user's email address and
     */
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

    /**
     * This function handles the result of a Google sign-in attempt and updates the UI accordingly.
     *
     * @param completedTask completedTask is a Task object that represents the result of a Google
     * sign-in operation. It contains information about whether the sign-in was successful or not, and
     * if it was successful, it also contains the user's Google account information.
     */
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

    /**
     * The function updates the user interface with the Google sign-in account information and starts a
     * new activity with the account data and profile image.
     *
     * @param account The GoogleSignInAccount object that contains information about the signed-in
     * user. It includes the user's ID, display name, given name, family name, email address, ID token,
     * and profile photo URL.
     */
    private void updateUI(GoogleSignInAccount account) {
        Log.e(TAG, "updateUI : account " + account);
        if (account != null) {

            String personId = account.getId();
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personToken = account.getIdToken();
            String personPhoto = account.getPhotoUrl().toString();

            String googleData =  " personId : " + personId  +"\n\n"+ " personName : " + personName  +"\n\n"+ " personGivenName : " + personGivenName  +"\n\n"+ " personFamilyName : " + personFamilyName  +"\n\n"+ " personEmail : " + personEmail  +"\n\n"+ " personToken : " + personToken  +"\n\n"+ " personPhoto : " + personPhoto;
            Log.e(TAG, "updateUI : data : account : " + googleData);

            Intent intent    =   new Intent(JavaGoogleSignInActivity.this, JavaProfileActivity.class);
            intent.putExtra("googleData",googleData);
            intent.putExtra("googleProfileImage", personPhoto);
            startActivity(intent);
            signOut();

        } else {
            Log.e(TAG, "Login failed!");
        }
    }

    /**
     * This function signs out the user from their Google account.
     */
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Signed out successfully
                        Log.e(TAG, "Google Sign out successfully");
                    }
                });
    }

    /**
     * This function revokes access to a Google account and logs a message upon successful completion.
     */
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Revoke access successfully
                        Log.e(TAG, "Google Revoke access successfully");
                    }
                });
    }
}