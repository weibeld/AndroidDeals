package org.latefire.deals.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.latefire.deals.R;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityDealDetailsBinding;
import org.latefire.deals.utils.MiscUtils;

public class DealDetailsActivity extends BaseActivity {

  public static final String ARG_DEAL = "deal";
  private ActivityDealDetailsBinding b;
  private DealDetailsActivity mActivity;
  private Deal mDeal;

  public static void start(Activity activity, Deal deal, ImageView imgDeal) {
    Intent intent = new Intent(activity, DealDetailsActivity.class);
    intent.putExtra(ARG_DEAL, deal);
    ActivityOptionsCompat options = ActivityOptionsCompat.
        makeSceneTransitionAnimation(activity, imgDeal, activity.getString(R.string.deal));
    activity.startActivity(intent, options.toBundle());
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_deal_details);
    ButterKnife.bind(this);
    supportPostponeEnterTransition();
    setSupportActionBar(b.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    postponeEnterTransition();
    mActivity = this;
    mDeal = (Deal) getIntent().getSerializableExtra(ARG_DEAL);
    b.setDeal(mDeal);

    b.btnGetDeal.setOnClickListener(v -> CurrentUserManager.getInstance()
        .getCurrentUser(user -> acquireDeal(user.getId(), mDeal.getId())));
  }

  private void acquireDeal(String userId, String dealId) {
    DatabaseManager.getInstance().acquireDeal(userId, dealId);
    MiscUtils.toastS(mActivity, "Deal grabbed");
    startActivity(new Intent(mActivity, HomeActivityCustomer.class));
  }

  @OnClick(R.id.fab_share) public void share(View view) {
    screenShot(b.dealDetailsCardview);
  }

  private void screenShot(View view) {
    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);
    String path =
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image Description",
            null);

    Uri uri = Uri.parse(path);
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
    shareIntent.setType("image/jpeg");
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share this deal");
    startActivity(shareIntent);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        supportFinishAfterTransition();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
