package com.kellton.socialintegrationssample;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class TwitterActivity extends AppCompatActivity {

    private String TAG=GmailActivity.class.getSimpleName();

    private String mUserId;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        mUserId = String.valueOf(getIntent().getExtras().get("TwitterLogInUserId"));
        mUserName= String.valueOf(getIntent().getExtras().get("TwitterLogInUserName"));

        setUpToolBar();
        setProfileData();
    }

    private void setProfileData() {
        TextView tvProfileName=(TextView)findViewById(R.id.tv_profile_name);
        TextView tvProfileId=(TextView)findViewById(R.id.tv_profile_id);

        tvProfileName.setText(mUserName);
        tvProfileId.setText(mUserId);
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
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.twitter_background,null));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
