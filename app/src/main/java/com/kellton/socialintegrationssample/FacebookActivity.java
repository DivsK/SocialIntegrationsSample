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

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookActivity extends AppCompatActivity {

    private String TAG=FacebookActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        setUpToolBar();
        requestingProfileData();
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
            actionBar.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.fb_background,null));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setProfileData(JSONObject object) throws JSONException {
        TextView tvUserName=(TextView)findViewById(R.id.tv_user_name);
        TextView tvUserId=(TextView)findViewById(R.id.tv_user_id);
        TextView tvAboutUser=(TextView)findViewById(R.id.tv_about_user);
        TextView tvUserEmail=(TextView)findViewById(R.id.tv_email_user);
        ImageView ivUserCoverPic=(ImageView)findViewById(R.id.iv_user_cover_pic);

        tvUserName.setText(object.getString("name"));
        tvUserId.setText(object.getString("id"));
        tvUserEmail.setText(object.getString("email"));
        tvAboutUser.setText(String.format("GENDER -%s", object.getString("gender")));

        Picasso.with(this).load(object.optJSONObject("cover").getString("source")).into(ivUserCoverPic);
    }

    private void requestingProfileData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("AccessToken", String.valueOf(AccessToken.getCurrentAccessToken()));
                        Log.v("JsonObject", String.valueOf(object));
                        Log.v("GraphResponse", String.valueOf(response));
                        try {
                            setProfileData(object);
                        } catch (JSONException e) {
                            Log.v(TAG, String.valueOf(e));
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,about,cover,gender");
        request.setParameters(parameters);
        request.executeAsync();
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
