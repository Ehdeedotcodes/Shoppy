package controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HideFabOnScroll extends FloatingActionButton.Behavior {
    public HideFabOnScroll(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dyUnconsumed, int dyUnConsumed){
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dyUnconsumed, dyUnConsumed);

        //child is the floating action button
        if (dyConsumed > 0){
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_bottom_margin = layoutParams.bottomMargin;
            int position = child.getHeight() + fab_bottom_margin;
            child.animate().translationY(position * 2).setInterpolator(new LinearInterpolator()).start();
        }
        else if (dyConsumed < 0){
            child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

}
