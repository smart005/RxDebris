package com.cloud.dialogs.plugs;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AbsListView;

import com.cloud.dialogs.R;

final class Utils {

    private static final int INVALID = -1;

    private Utils() {
        // no instance
    }

    static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    static void animateContent(final View view, int to, Animation.AnimationListener listener) {
        HeightAnimation animation = new HeightAnimation(view, view.getHeight(), to);
        animation.setAnimationListener(listener);
        animation.setDuration(200);
        view.startAnimation(animation);
    }

    static boolean listIsAtTop(AbsListView listView) {
        return listView.getChildCount() == 0 || listView.getChildAt(0).getTop() == listView.getPaddingTop();
    }

    /**
     * This will be called in order to create view, if the given view is not null,
     * it will be used directly, otherwise it will check the resourceId
     *
     * @return null if both resourceId and view is not set
     */
    static View getView(Context context, int resourceId, View view) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (view != null) {
            return view;
        }
        if (resourceId != INVALID) {
            view = inflater.inflate(resourceId, null);
        }
        return view;
    }

    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the gravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    static int getAnimationResource(int gravity, boolean isInAnimation) {
        if ((gravity & Gravity.TOP) == Gravity.TOP) {
            return isInAnimation ? R.anim.dialog_plugs_slide_in_top : R.anim.dialog_plugs_slide_out_top;
        }
        if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            return isInAnimation ? R.anim.dialog_plugs_slide_in_bottom : R.anim.dialog_plugs_slide_out_bottom;
        }
        if ((gravity & Gravity.CENTER) == Gravity.CENTER) {
            return isInAnimation ? R.anim.dialog_plugs_fade_in_center : R.anim.dialog_plugs_fade_out_center;
        }
        return INVALID;
    }
}
