package org.latefire.deals.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.latefire.deals.R;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.databinding.ActivitySampleShowCustomerBinding;

public class SampleShowCustomerActivity extends AppCompatActivity {

  ActivitySampleShowCustomerBinding b;
  DatabaseManager mDatabaseManager;
  ValueEventListener mListener;
  DatabaseReference mCustomerRef;
  String mCustomerId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setTitle("Sample: Show Customer");
    b = DataBindingUtil.setContentView(this, R.layout.activity_sample_show_customer);

    mCustomerId = getIntent().getStringExtra(getString(R.string.extra_customer_id));
    mDatabaseManager = DatabaseManager.getInstance();
    mListener = new ValueEventListener() {
      // Called (1) when assigning listener, (2) when underlying data changes
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        setFields(dataSnapshot.getValue(Customer.class));
      }
      @Override public void onCancelled(DatabaseError databaseError) {}
    };
    mCustomerRef = mDatabaseManager.getCustomerRef(mCustomerId);
  }

  @Override protected void onStart() {
    super.onStart();
    mCustomerRef.addValueEventListener(mListener);
  }

  @Override protected void onStop() {
    super.onStop();
    mCustomerRef.removeEventListener(mListener);
  }

  private void setFields(Customer customer) {
    b.setCustomer(customer);
  }
}
