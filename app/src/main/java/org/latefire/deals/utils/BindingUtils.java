package org.latefire.deals.utils;

import android.databinding.BindingAdapter;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import org.latefire.deals.R;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;

/**
 * Created by Ken on 2/17/2017.
 */

public class BindingUtils {
  @BindingAdapter("imgUrlDeal")
  public static void loadImageDeal(ImageView imageView, String imgUrl) {
    if (imgUrl.isEmpty()) {
      imageView.setImageResource(R.drawable.image_not_found);
      return;
    }
    Glide.with(imageView.getContext())
        .load(imgUrl)
        .asBitmap()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.image_not_found)
        .into(imageView);
  }

  @BindingAdapter("price") public static void showPrice(TextView textView, Deal deal) {
    // Deal price and regular price
    String regularPrice = String.valueOf(deal.getRegularPrice());
    String dealPrice = String.valueOf(deal.getDealPrice());
    SpannableStringBuilder price =
        StringUtils.makePriceText(textView.getContext(), regularPrice, dealPrice);
    textView.setText(price, TextView.BufferType.EDITABLE);
  }

  @BindingAdapter("imgBusinessAvatar")
  public static void showBusinessAvatar(RoundedImageView roundedImageView, Deal deal) {
    DatabaseManager.getInstance().getBusiness(deal.getBusinessId(), model -> {
      Business business = (Business) model;
      if (business.getProfilePhoto() == null || business.getProfilePhoto().isEmpty()) {
        roundedImageView.setImageResource(R.drawable.image_not_found);
        return;
      }
      Glide.with(roundedImageView.getContext())
          .load(business.getProfilePhoto())
          .asBitmap()
          .placeholder(R.drawable.placeholder)
          .error(R.drawable.image_not_found)
          .into(roundedImageView);
    });
  }

  @BindingAdapter(("businessName"))
  public static void showBusinessName(TextView textView, Deal deal) {
    DatabaseManager.getInstance().getBusiness(deal.getBusinessId(), model -> {
      Business business = (Business) model;
      textView.setText(business.getBusinessName());
    });
  }
}

