package org.latefire.deals.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import org.latefire.deals.R;

/**
 * Created by phongnguyen on 3/20/17.
 */

public class StringUtils {

  public static SpannableStringBuilder makePriceText(Context context, String priceRegular, String priceDeal) {
    ForegroundColorSpan redForegroundColorSpan =
        new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent));

    // Use a SpannableStringBuilder so that both the text and the spans are mutable
    SpannableStringBuilder ssb = new SpannableStringBuilder(priceRegular);
    ssb.append("\n");
    ssb.append(priceDeal);
    ssb.setSpan(redForegroundColorSpan,            // the span to add
        ssb.length() - priceRegular.length(),                                 // the start of the span (inclusive)
        ssb.length(),                      // the end of the span (exclusive)
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // behavior when text is later inserted into the SpannableStringBuilder
    StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
    ssb.setSpan(strikethroughSpan, 0, priceRegular.length(),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return ssb;
  }
}
