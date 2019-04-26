package com.levez.expandablecard;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * AnimationUtils - This class is used to perform animations.
 *
 * This class is tailored to the needs of the ExpandableView class.
 * The original version can be found at https://stackoverflow.com/a/13381228.
 *
 * @author  Felipe Levez
 * @version 0.1.0
 */

class AnimationUtils {

    /**
     * Perform the expansion.
     * @param v A View-like variable that represents the layout
     *          element that will not move but will serve as the
     *          starting point of the animation of your child.
     * @param child A View-like variable that represents the layout
     *              element that will move.
     */
    static void expand(final View v, final View child) {
        child.measure(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        child.getLayoutParams().height = 1;
        child.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                child.getLayoutParams().height = interpolatedTime == 1
                        ? FrameLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                child.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(targetHeight / child.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    /**
     * Perform contraction.
     * @param v A View-like variable that represents the layout
     *          element that will not move but will serve as the
     *          starting point of the animation of your child.
     * @param child A View-like variable that represents the layout
     *              element that will move.
     */
    static void collapse(final View v, final View child) {
        final int initialHeight = child.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    child.setVisibility(View.GONE);
                }else{
                    child.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    child.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
