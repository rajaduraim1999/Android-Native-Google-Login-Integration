package com.sample.android_native_google_login_integration.kotlinsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sample.android_native_google_login_integration.R

class KotlinGoogleSignInActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "GoogleSignIn"

    var sibGoogleSignIn: SignInButton? = null
    var cvGoogleSignIn: CardView? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_google_sign_in)
        initUI()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)

        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sibGoogleSignIn)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener(this)
        cvGoogleSignIn!!.setOnClickListener(this)
    }

    /**
     * The function initializes UI elements for Google sign-in in a Kotlin program.
     */
    private fun initUI() {
        sibGoogleSignIn = findViewById(R.id.sibGoogleSignIn)
        cvGoogleSignIn = findViewById(R.id.cvGoogleSignIn)
    }

    /**
     * This function handles the click event for Google sign-in button and card view.
     *
     * @param v The parameter "v" is of type View, which represents the view that was clicked and
     * triggered the onClick() method.
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.sibGoogleSignIn, R.id.cvGoogleSignIn -> signIn()
        }
    }

    /**
     * This function initiates the Google sign-in process and starts an activity to handle the sign-in
     * intent.
     */
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * This function handles the result of a Google sign-in intent in Kotlin.
     *
     * @param requestCode An integer request code that identifies the request. This code is passed to
     * onActivityResult() when the activity exits and returns a result.
     * @param resultCode resultCode is an integer value that represents the result of the activity that
     * was launched. It can have different values such as RESULT_OK, RESULT_CANCELED, or any other
     * custom result code that was set by the activity. The value of resultCode is usually checked to
     * determine the outcome of the activity.
     * @param data The `data` parameter in the `onActivityResult` method is an `Intent` object that
     * contains the result data returned from the activity launched by the current activity. In this
     * case, it contains the result data returned from the Google Sign-In activity launched by the
     * `GoogleSignInClient.getSignInIntent(...
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    /**
     * The function handles the result of a Google sign-in attempt and updates the UI accordingly.
     *
     * @param completedTask The parameter `completedTask` is a `Task` object that represents the result
     * of a Google sign-in operation. It contains information about whether the sign-in was successful
     * or not, and if it was successful, it also contains the `GoogleSignInAccount` object representing
     * the signed-in user.
     */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    /**
     * The function updates the user interface with the Google account information and starts a new
     * activity with the data.
     *
     * @param account GoogleSignInAccount object that contains information about the signed-in user. It
     * includes the user's unique ID, display name, given name, family name, email address, ID token,
     * and profile photo URL.
     */
    private fun updateUI(account: GoogleSignInAccount?) {
        Log.e(TAG, "updateUI : account $account")
        if (account != null) {
            val personId = account.id
            val personName = account.displayName
            val personGivenName = account.givenName
            val personFamilyName = account.familyName
            val personEmail = account.email
            val personToken = account.idToken
            val personPhoto = account.photoUrl.toString()
            val googleData =
                " personId : $personId\n\n personName : $personName\n\n personGivenName : $personGivenName\n\n personFamilyName : $personFamilyName\n\n personEmail : $personEmail\n\n personToken : $personToken\n\n personPhoto : $personPhoto"
            Log.e(TAG, "updateUI : data : account : $googleData")
            val intent = Intent(this@KotlinGoogleSignInActivity, KotlinProfileActivity::class.java)
            intent.putExtra("googleData", googleData)
            intent.putExtra("googleProfileImage", personPhoto)
            startActivity(intent)
            signOut()
        } else {
            Log.e(TAG, "Login failed!")
        }
    }

    /**
     * The function signs out the user from Google Sign-In.
     */
    private fun signOut() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) { // Signed out successfully
                Log.e(TAG, "Google Sign out successfully")
            }
    }

    /**
     * The function revokes access to a Google account using the Google Sign-In API in Kotlin.
     */
    private fun revokeAccess() {
        mGoogleSignInClient!!.revokeAccess()
            .addOnCompleteListener(this) {
                Log.e(TAG, "Google Revoke access successfully")
            }
    }
}