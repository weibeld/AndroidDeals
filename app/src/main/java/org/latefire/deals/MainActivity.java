package org.latefire.deals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.latefire.deals.managers.DatabaseManager;
import org.latefire.deals.models.Customer;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DatabaseManager mDatabaseManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;
    private Context mActivity;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // For signing-in to Firebase anonymously (which allows to read and write the database),
        // only the following line is needed (and all the Google sign in stuff is not needed)
        //mFirebaseAuth.signInAnonymously();

        // Test if there's an authenticated Firebase user, if not, redirect the user to sign in
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        getSupportActionBar().setSubtitle(String.format(getString(R.string.subtitle_main_activity), mFirebaseUser.getDisplayName()));

        // Needed for enabling sign out from Google account
        mGoogleApiClient = getGoogleApiClient();

        mDatabaseManager = DatabaseManager.getInstance();
        Customer customer = new Customer("danielmweibel@gmail.com", "+41798310140", "Daniel", "Weibel");
        String id = mDatabaseManager.saveCustomer(customer);
        ((TextView) findViewById(R.id.tvHello)).setText(id);
    }

    private GoogleApiClient getGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> {
                    Log.d(LOG_TAG, "Could not connect to Google for signing in");
                    Toast.makeText(mActivity, "Could not connect to Google for signing in", Toast.LENGTH_SHORT).show();
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Sign out from Firebase and from the Google account
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
