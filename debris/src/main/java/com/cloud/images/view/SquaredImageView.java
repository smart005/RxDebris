package com.cloud.images.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

class SquaredImageView extends ImageView {
  public SquaredImageView(Context context) {
    super(context);
  }

  public SquaredImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }
}
