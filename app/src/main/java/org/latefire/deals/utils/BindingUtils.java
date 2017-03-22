package org.latefire.deals.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import org.latefire.deals.R;

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
}

