package cmpe277.project.skibuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.auth.oauth2.Credential;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.accounts.Account;
import android.accounts.AccountManager;


import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.bigquery.BigqueryScopes;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.oauth2.Oauth2;
//import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.oauth2.Oauth2;
//import com.google.api.services.oauth2.model.Userinfoplus;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

import cmpe277.project.skibuddy.common.User;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private ImageView imageView;
    private TextView tagline;

    ///
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Drive API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart. */
//    private static final List<String> SCOPES =
//            Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

/*    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusTextView = (TextView) findViewById(R.id.status);

        ///
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(null);
        tagline = (TextView)findViewById(R.id.tagline);
        ///

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .addApi(Plus.API) ///
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();

/*            try {
                ;//handleSignInResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoUserIdException e) {
                e.printStackTrace();
            }*/
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoUserIdException e) {
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
            }
        }
    }

    /**
     * Exception thrown when no user ID could be retrieved.
     */
    private static class NoUserIdException extends Exception {
    }

    /**
     * Exception thrown when a code exchange has failed.
     */
    public static class CodeExchangeException extends GetCredentialsException {
        /**
         * Construct a CodeExchangeException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public CodeExchangeException(String authorizationUrl) {
            super(authorizationUrl);
        }
    }
  /*  *//**
     * Build an authorization flow and store it as a static class attribute.
     *
     * @return GoogleAuthorizationCodeFlow instance.
     * @throws IOException Unable to load client_secret.json.
     *//*
    static GoogleAuthorizationCodeFlow getFlow() throws IOException {
        if (flow == null) {
            InputStream in =
                    MyClass.class.getResourceAsStream(CLIENTSECRET_LOCATION);
            GoogleClientSecrets clientSecret =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecret, SCOPES)
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();
        }
        return flow;
    }
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(PlusSample.class.getResourceAsStream("/client_secrets.json")));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(
                dataStoreFactory).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Exchange an authorization code for OAuth 2.0 credentials.
     *
     * @param code to exchange for OAuth 2.0
     *     credentials.
     * @return OAuth 2.0 credentials.
     * @throws CodeExchangeException An error occurred.
     */
    /*
    static Credential exchangeCode(String authorizationCode)
            throws CodeExchangeException {
        try {
            GoogleAuthorizationCodeFlow flow = getFlow();
            GoogleTokenResponse response = flow
                    .newTokenRequest(authorizationCode)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();
            return flow.createAndStoreCredential(response, null);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
            throw new CodeExchangeException(null);
        }
    }*/

    private void handleSignInResult(GoogleSignInResult result) throws IOException, JSONException, NoUserIdException {
        imageView.setImageBitmap(null);
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            ///
            //UserProfileActivity userProfile = new UserProfileActivity(mGoogleApiClient);
            //Intent userProfileIntent = userProfile.getIntent();

            Intent userProfileIntent = new Intent(SignInActivity.this, UserProfileActivity.class);
            ArrayList<String> arrStr = new ArrayList<String>();

            if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String name = currentPerson.getDisplayName();
                String taglineStr = currentPerson.getTagline();
                String uriStr = currentPerson.getImage().getUrl();
                arrStr.add(name);       //index 0
                arrStr.add(taglineStr); //index 1
                arrStr.add(uriStr);     //index 2

            }
            userProfileIntent.putExtra("UserProfileActivity", arrStr);
            SignInActivity.this.startActivity(userProfileIntent);
            ///

 /*           // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

            //Tagline
            if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String taglineStr = currentPerson.getTagline();

                if (taglineStr != null) {
                    tagline.setText(getString(R.string.tagline, taglineStr));
                }
                else {
                    tagline.setText(getString(R.string.tagline_na));
                }

            }

            //Profile picture
            final int PROFILE_PIC_SIZE = 500;
            //Uri personPhotoUrl = acct.getPhotoUrl();
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String uriStr = currentPerson.getImage().getUrl();
            String shorter_url =
                    uriStr.substring(0,
                            uriStr.length() - 2)
                            + PROFILE_PIC_SIZE;
            new LoadProfileImage(imageView).execute(shorter_url);*/

            /*if (acct.getPhotoUrl() != null) {
                String personPhotoUrlString = personPhotoUrl.toString();
                String shorter_url =
                        personPhotoUrlString.substring(0,
                                personPhotoUrlString.length() - 2)
                        + PROFILE_PIC_SIZE;
                System.out.println("shorter_url: " + shorter_url);
                new LoadProfileImage(imageView).execute(shorter_url);
            }
            else imageView.setImageBitmap(null);*/

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
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
            imageView.setVisibility(View.VISIBLE);
            tagline.setVisibility(View.VISIBLE);

        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            imageView.setVisibility(View.INVISIBLE);
            tagline.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /**
     * Exception thrown when an error occurred while retrieving credentials.
     */
    public static class GetCredentialsException extends Exception {

        protected String authorizationUrl;

        /**
         * Construct a GetCredentialsException.
         *
         * @param authorizationUrl The authorization URL to redirect the user to.
         */
        public GetCredentialsException(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * Set the authorization URL.
         */
        public void setAuthorizationUrl(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * @return the authorizationUrl
         */
        public String getAuthorizationUrl() {
            return authorizationUrl;
        }
    }



}