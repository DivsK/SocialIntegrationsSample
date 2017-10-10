package com.kellton.socialintegrationssample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG=LoginActivity.class.getSimpleName();

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "cPXzgwxhsBb2BljOKSmeo01jJ";
    private static final String TWITTER_SECRET = "poP7HIspJoXVzQvODhnnX07eCrwAqLHrOiHXVJfm7jerMhsTgs";

    private CallbackManager mFacebookCallbackManager;
    private LoginButton mFacebookSignInButton;
    private Intent mIntent;
    private static final int GOOGLE_SIGN_IN = 9001;
    private static final int FACEBOOK_SIGN_IN = 9002;

    private GoogleApiClient mGoogleApiClient;
    private TwitterLoginButton mTwitterSignInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        setContentView(R.layout.activity_login);
        initUI();
        setUpToolBar();

    }

    private void initUI() {
        mIntent=new Intent(LoginActivity.this, FacebookActivity.class);

        mFacebookSignInButton = (LoginButton) findViewById(R.id.facebook_login_button);
        mFacebookSignInButton.setOnClickListener(this);
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(mIntent);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        SignInButton googleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_ICON_ONLY);
        googleSignInButton.setOnClickListener(this);

        mTwitterSignInButton = (TwitterLoginButton)findViewById(R.id.twitter_sign_in_button);
        mTwitterSignInButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(final Result<TwitterSession> result) {
                Intent intent=new Intent(LoginActivity.this,TwitterActivity.class);
                intent.putExtra("TwitterLogInUserId",result.data.getUserId());
                intent.putExtra("TwitterLogInUserName",result.data.getUserName());
                startActivity(intent);
            }

            @Override
            public void failure(TwitterException e) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                final GoogleApiClient client = mGoogleApiClient;
                Intent intent=new Intent(LoginActivity.this,GmailActivity.class);
                intent.putExtra("GoogleSignedInProfile",result.getSignInAccount());
                startActivity(intent);
            } else {
                Log.v(TAG,"Not able to Sign In");

            }
        }
        else if(TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE == requestCode) {
            mTwitterSignInButton.onActivityResult(requestCode, resultCode, data);
        }else{
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.facebook_login_button:
                mFacebookSignInButton.setReadPermissions("email","public_profile");
                break;
            case R.id.google_sign_in_button:
                signInWithGoogle();
                break;
            default:
                break;
        }
    }

    /**
     * Initialize Toolbar and set in the action bar
     */
    private void setUpToolBar() {
        Log.v(TAG, "setUpToolBar");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
            actionBar.setTitle(getResources().getString(R.string.login_social_media));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void signInWithGoogle() {
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //back arrow event
            case android.R.id.home:
                finish();
                return true;
            default:
                Log.e(TAG, getString(R.string.wrong_case_selection));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

