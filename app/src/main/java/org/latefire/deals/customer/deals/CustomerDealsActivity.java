package org.latefire.deals.customer.deals;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import org.latefire.deals.R;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.databinding.ActivityCustomerDealsBinding;

public class CustomerDealsActivity extends AppCompatActivity {

  ActivityCustomerDealsBinding b;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_customer_deals);

    // Toolbar
    setSupportActionBar(b.toolbarInclude.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Your Deals");

    CurrentUserManager.getInstance().getCurrentUser(user -> {
      b.viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), user.getId()));
      b.tabLayout.setupWithViewPager(b.viewPager);
    });
  }





  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.customer_deals, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
