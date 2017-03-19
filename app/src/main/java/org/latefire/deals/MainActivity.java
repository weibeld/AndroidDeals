package org.latefire.deals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.latefire.deals.managers.DatabaseManager;
import org.latefire.deals.models.Customer;

public class MainActivity extends AppCompatActivity {

    DatabaseManager mDatabaseMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseMgr = DatabaseManager.getInstance();

        Customer customer = new Customer("danielmweibel@gmail.com", "+41798310140", "Daniel", "Weibel");

        String id = mDatabaseMgr.saveCustomer(customer);

        ((TextView) findViewById(R.id.tvHello)).setText(id);
    }
}
