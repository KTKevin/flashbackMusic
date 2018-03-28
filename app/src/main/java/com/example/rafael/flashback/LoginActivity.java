package com.example.rafael.flashback;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.util.Log;

import com.example.rafael.flashback.adapters.PeopleAdapter;
import com.example.rafael.flashback.handlers.PeopleHandler;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 3/14/2018.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener{

    private static final String TAG = "LoginActivity";

    SignInButton signInButton;
    GoogleApiClient mGoogleApiClient;
    final int RC_INTENT = 200;
    final int RC_API_CHECK = 100;
    PeopleAdapter adapter;
    String userId;
    String userName;
    String userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = (SignInButton) findViewById(R.id.main_googlesigninbtn);
        signInButton.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // The serverClientId is an OAuth 2.0 web client ID
                .requestServerAuthCode(getString(R.string.clientID))
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleScopes.CONTACTS_READONLY),
                        new Scope(PeopleScopes.USER_EMAILS_READ),
                        new Scope(PeopleScopes.USERINFO_EMAIL))
                .build();

        // To connect with Google Play Services and Sign In
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("connection", "msg: " + connectionResult.getErrorMessage());

        GoogleApiAvailability mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = mGoogleApiAvailability.getErrorDialog(this, connectionResult.getErrorCode(), RC_API_CHECK);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        signInButton.setEnabled(false);
        switch (v.getId()) {
            case R.id.main_googlesigninbtn:
                Log.d(TAG, "btn click");
                getIdToken();
                break;
        }
    }

    // Performed on Google Sign in click
    private void getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_INTENT:
                Log.d(TAG, "sign in result");
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if (result.isSuccess()) {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    Log.d(TAG, "onActivityResult:GET_TOKEN:success:" + result.getStatus().isSuccess());
                    // This is what we need to exchange with the server.
                    Log.d(TAG, "auth Code:" + acct.getServerAuthCode());
                    userId = acct.getId();
                    userName = acct.getDisplayName();
                    userEmail = acct.getEmail();

                    new PeoplesAsync().execute(acct.getServerAuthCode());

                } else {

                    Log.d(TAG, result.getStatus().toString() + "\nmsg: " + result.getStatus().getStatusMessage());
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    class PeoplesAsync extends AsyncTask<String, Void, List<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected List<String> doInBackground(String... params) {

            List<String> nameList = new ArrayList<>();
            List<String> emailList = new ArrayList<>();

            try {
                People peopleService = PeopleHandler.setUp(LoginActivity.this, params[0]);

                ListConnectionsResponse response = peopleService.people().connections()
                        .list("people/me")
                        .setRequestMaskIncludeField("person.names,person.emailAddresses")
                        .execute();
                List<Person> connections = response.getConnections();

                if (connections != null) {
                    for (Person person : connections) {
                        if (!person.isEmpty()) {
                            List<Name> names = person.getNames();
                            List<EmailAddress> emailAddresses = person.getEmailAddresses();

                            if (names != null) {
                                for (int i = 0; i < names.size(); i++) {
                                    nameList.add(names.get(i).getDisplayName());
                                }
                            }
                            if (emailAddresses != null) {
                                for (int i = 0; i < emailAddresses.size(); i++) {
                                    emailList.add(emailAddresses.get(i).getValue());
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter = new PeopleAdapter(nameList, emailList, userId, userName, userEmail);
            return nameList;
        }


        @Override
        protected void onPostExecute(List<String> nameList) {
            super.onPostExecute(nameList);
            updateUI();
        }
    }

    void updateUI() {
        signInButton.setVisibility(View.INVISIBLE);
        //Transition to BrowseActivity
        Intent intent = new Intent(this, BrowseActivity.class);
        intent.putExtra("user", adapter);
        startActivity(intent);
    }
}
