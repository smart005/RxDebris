package com.cloud.components.shape.shapes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;

import com.cloud.components.R;
import com.cloud.components.shape.ShapeOfView;
import com.cloud.components.shape.manager.ClipPathManager;

public class ArcView extends ShapeOfView {

    public static final int POSITION_BOTTOM = 1;
    public static final int POSITION_TOP = 2;
    public static final int POSITION_LEFT = 3;
    public static final int POSITION_RIGHT = 4;
    public static final int CROP_INSIDE = 1;

    public static final int CROP_OUTSIDE = 2;
    private int arcPosition = POSITION_TOP;
    private int cropDirection = CROP_INSIDE;

    private int arcHeight = 0;

    public ArcView(Context context) {
        super(context);
        init(context, null);
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
            arcHeight = attributes.getDimensionPixelSize(R.styleable.ArcView_av_arc_height, arcHeight);
            arcPosition = attributes.getInteger(R.styleable.ArcView_av_arc_position, arcPosition);
            cropDirection = attributes.getInteger(R.styleable.ArcView_av_arc_cropDirection, cropDirection);
            attributes.recycle();
        }
        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();

                final boolean isCropInside = cropDirection == CROP_INSIDE;

                switch (arcPosition) {
                    case POSITION_BOTTOM: {
                        if (isCropInside) {
                            path.moveTo(0, 0);
                            path.lineTo(0, height);
                            path.quadTo(width / 2, height - 2 * arcHeight, width, height);
                            path.lineTo(width, 0);
                            path.close();
                        } else {
                            path.moveTo(0, 0);
                            path.lineTo(0, height - arcHeight);
                            path.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
                            path.lineTo(width, 0);
                            path.close();
                        }
                        break;
                    }
                    case POSITION_TOP:
                        if (isCropInside) {
                            path.moveTo(0, height);
                            path.lineTo(0, 0);
                            path.quadTo(width / 2, 2 * arcHeight, width, 0);
                            path.lineTo(width, height);
                            path.close();
                        } else {
                            path.moveTo(0, arcHeight);
                            path.quadTo(width / 2, -arcHeight, width, arcHeight);
                            path.lineTo(width, height);
                            path.lineTo(0, height);
                            path.close();
                        }
                        break;
                    case POSITION_LEFT:
                        if (isCropInside) {
                            path.moveTo(width, 0);
                            path.lineTo(0, 0);
                            path.quadTo(arcHeight * 2, height / 2, 0, height);
                            path.lineTo(width, height);
                            path.close();
                        } else {
                            path.moveTo(width, 0);
                            path.lineTo(arcHeight, 0);
                            path.quadTo(-arcHeight, height / 2, arcHeight, height);
                            path.lineTo(width, height);
                            path.close();
                        }
                        break;
                    case POSITION_RIGHT:
                        if (isCropInside) {
                            path.moveTo(0, 0);
                            path.lineTo(width, 0);
                            path.quadTo(width - arcHeight * 2, height / 2, width, height);
                            path.lineTo(0, height);
                            path.close();
                        } else {
                            path.moveTo(0, 0);
                            path.lineTo(width - arcHeight, 0);
                            path.quadTo(width + arcHeight, height / 2, width - arcHeight, height);
                            path.lineTo(0, height);
                            path.close();
                        }
                        break;

                }
                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }


    public int getArcPosition() {
        return arcPosition;
    }

    public void setArcPosition(int arcPosition) {
        this.arcPosition = arcPosition;
        requiresShapeUpdate();
    }

    public int getCropDirection() {
        return cropDirection;
    }

    public void setCropDirection(int cropDirection) {
        this.cropDirection = cropDirection;
        requiresShapeUpdate();
    }

    public int getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
        requiresShapeUpdate();
    }
}
