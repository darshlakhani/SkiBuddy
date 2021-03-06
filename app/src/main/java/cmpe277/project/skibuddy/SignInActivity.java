package cmpe277.project.skibuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONException;
import java.io.IOException;
import java.util.*;

import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private boolean signingOut = false;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_n);

        mStatusTextView = (TextView) findViewById(R.id.status);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .addApi(Plus.API)
                .build();

        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                if(signingOut)
                    signOut();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle b = getIntent().getExtras();
        if (b != null && b.getString(BundleKeys.SIGNOUT) != null){
            signingOut = true;
            connectAndSignOut();
        } else {
            // If we're not signing out, try to sign in with a cached account

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                try {
                    handleSignInResult(result);
                } catch (IOException | NoUserIdException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        try {
                            handleSignInResult(googleSignInResult);
                        } catch (IOException | NoUserIdException | JSONException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            try {
                handleSignInResult(result);
            } catch (IOException | NoUserIdException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Exception thrown when no user ID could be retrieved.
     */
    private static class NoUserIdException extends Exception {
    }

    private void handleSignInResult(GoogleSignInResult result) throws IOException, JSONException, NoUserIdException, InterruptedException {
        if (result.isSuccess()) {
            Log.d(TAG, "Google Sign in successful");
            final Server s = new ServerSingleton().getServerInstance(this);

            final String id = result.getSignInAccount().getId();
            final String name = result.getSignInAccount().getDisplayName();

            s.authenticateUser("G+" + id, new ServerCallback<User>() {
                @Override
                public void handleResult(User result) {
                    if (result == null) {

                        // We don't know this user yet, so we should create an account
                        // If the user has a Google+ account, fetch tagline and profile pic
                        // By default leave them blank though
                        String taglineStr = "";
                        String profilePictureUrl = "";
                        if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                            final Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                            if (currentPerson != null) {
                                String googleTagline = currentPerson.getTagline();
                                taglineStr = googleTagline != null ? googleTagline : "";
                                profilePictureUrl = currentPerson.getImage().getUrl();

                            }

                        }

                        User user = ServerSingleton.createUser();
                        user.setName(name);
                        user.setTagline(taglineStr);
                        user.setProfilePictureURL(profilePictureUrl);

                        s.storeUser("G+" + id, user);
                    }
                    // Launch new activity
                    Intent i = new Intent(SignInActivity.this, DashboardActivity.class);
                    startActivity(i);
                }
            });
        } else {
            Log.d(TAG, "Google Sign in failed");

            Toast t = Toast.makeText(this,
                    "Authentication with Google failed, please try again later",
                    Toast.LENGTH_LONG);
            t.show();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void connectAndSignOut(){
        if(mGoogleApiClient.isConnected()) {
            signOut();
        } else {
            mGoogleApiClient.connect();
        }
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast t = Toast.makeText(getBaseContext(),
                                "Signed out",
                                Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

}