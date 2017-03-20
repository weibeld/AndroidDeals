package org.latefire.deals.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.latefire.deals.R;
import org.latefire.deals.databinding.ActivityCreateDealBinding;
import org.latefire.deals.managers.DatabaseManager;
import org.latefire.deals.models.Deal;

public class CreateDealActivity extends AppCompatActivity {

  ActivityCreateDealBinding b;
  DatabaseManager mDatabaseManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_create_deal);
    mDatabaseManager = DatabaseManager.getInstance();
    getSupportActionBar().setTitle("Create Deal");

    b.btnSave.setOnClickListener(v -> {
      Deal deal = new Deal();
      deal.setTitle(b.etTitle.getText().toString());
      mDatabaseManager.createDeal(deal);
      startActivity(new Intent(this, HomeActivity.class));
    });
  }
}
