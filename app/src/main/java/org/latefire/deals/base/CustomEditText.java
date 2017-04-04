package org.latefire.deals.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextPaint;
import android.util.AttributeSet;
import org.latefire.deals.R;

/**
 * Created by Ken on 10/4/16.
 */
public class CustomEditText extends AppCompatEditText {
  String smallText;
  Paint smallTextPaint;

  public CustomEditText(Context context) {
    super(context);
    init(null);
  }

  public CustomEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  void init(AttributeSet attrs) {
    setSingleLine(true);
    TextPaint paint = getPaint();

    smallTextPaint = new Paint(paint);
    smallTextPaint.setTextSize(getTextSize());
    smallTextPaint.setColor(getCurrentTextColor());

    smallText = "";
    if (attrs == null) {
      return;
    }
    TypedArray a =
        getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0);

    try {
      smallText = a.getString(R.styleable.CustomEditText_smallText);
    } finally {
      a.recycle();
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    String currentText = getText().toString();
    smallTextPaint.setTypeface(getTypeface());
    Rect bounds = new Rect();
    TextPaint paint = getPaint();
    paint.getTextBounds(currentText, 0, currentText.length(), bounds);
    Rect smallBounds = new Rect();
    smallTextPaint.getTextBounds(smallText, 0, smallText.length(), smallBounds);
    canvas.drawText(smallText, getWidth() - smallBounds.width() - 20,
        getHeight() / 2 + getTextSize() / 3, smallTextPaint);
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    super.onTextChanged(text, start, lengthBefore, lengthAfter);
    invalidate();
  }
}
