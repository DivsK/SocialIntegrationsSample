package com.kellton.socialintegrationssample;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

public class GmailActivity extends AppCompatActivity {

    private String TAG=GmailActivity.class.getSimpleName();

    private GoogleSignInAccount mGoogleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail);

        mGoogleSignInAccount= (GoogleSignInAccount) getIntent().getExtras().get("GoogleSignedInProfile");

        assert mGoogleSignInAccount != null;
        Log.v(TAG, String.valueOf(mGoogleSignInAccount.getGrantedScopes()));

        setUpToolBar();
        setProfileData();

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
            actionBar.setTitle("");
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.google_background,null));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setProfileData() {
        TextView tvProfileName=(TextView)findViewById(R.id.tv_profile_name);
        TextView tvProfileId=(TextView)findViewById(R.id.tv_profile_id);
        TextView tvProfileEmail=(TextView)findViewById(R.id.tv_email_user);
        ImageView ivProfileCoverPic=(ImageView)findViewById(R.id.iv_profile_cover_pic);

        tvProfileName.setText(mGoogleSignInAccount.getDisplayName());
        tvProfileId.setText(mGoogleSignInAccount.getId());
        tvProfileEmail.setText(mGoogleSignInAccount.getEmail());

        Picasso.with(this).load(mGoogleSignInAccount.getPhotoUrl()).into(ivProfileCoverPic);
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
