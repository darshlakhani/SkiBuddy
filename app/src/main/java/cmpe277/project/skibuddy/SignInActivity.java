package cmpe277.project.skibuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_n);

        mStatusTextView = (TextView) findViewById(R.id.status);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
//        findViewById(R.id.sign_out_button).setOnClickListener(this);

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

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(false);

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            try {
                handleSignInResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoUserIdException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoUserIdException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            try {
                handleSignInResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoUserIdException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            final Server s = new ServerSingleton().getServerInstance(this);
 //           Intent userProfileIntent = new Intent(SignInActivity.this, UserProfileActivity.class);
  //          ArrayList<String> arrStr = new ArrayList<String>();

            if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                final Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                if (currentPerson != null) {
                    final String name = currentPerson.getDisplayName();
                    final String taglineStr = currentPerson.getTagline();
                    //final String uriStr = currentPerson.getImage().getUrl();
                    final String id = currentPerson.getId();

                    s.authenticateUser("G+" + id, new ServerCallback<User>() {
                        @Override
                        public void handleResult(User result) {
                            if (result == null) {
                                User user = ServerSingleton.createUser();
                                user.setName(name);
                                user.setTagline(taglineStr);
                                //user.setProfilePictureURL(uriStr);

                                //s.storeUser(UID, user);
                                s.storeUser("G+" + id, user);
                            }
                            // Launch new activity
                            //Intent i = new Intent(SignInActivity.this, DashboardActivity.class);
                            Intent i = new Intent(SignInActivity.this, UserProfileActivity.class);
                            startActivity(i);
                        }
                    });

                } else {
                    final String UID = "qwes";
                    //final String UID = userIdGenerator(currentPerson.getDisplayName()); //UUID generator
                    s.authenticateUser(UID, new ServerCallback<User>() {
                        @Override
                        public void handleResult(User result) {
                            if (result == null) {
                                User user = ServerSingleton.createUser();
                                user.setName(currentPerson.getDisplayName());
                                user.setName("Ernst Haagsman");

                                user.setTagline("");

                                s.storeUser(UID, user);
                            }

                            // Launch new activity
                            //Intent i = new Intent(SignInActivity.this, DashboardActivity.class);
                            Intent i = new Intent(SignInActivity.this, UserProfileActivity.class);
                            startActivity(i);
                        }
                    });

                }//if

            }
        }
    }
    public static String userIdGenerator(String appended) throws InterruptedException {
        //generate a unique 14-char number
        String name = System.currentTimeMillis() + "";

        return appended + name.substring(7, 13);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
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

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
       //     case R.id.sign_out_button:
        //        signOut();
        //        break;
        }
    }

}