package com.rishwagnik.ehrms;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

public class TableHandler {

    private String TAG = TableHandler.class.getSimpleName();

    public TableHandler(){}

    public void revealTable(CardView card){

        int cx = card.getWidth()/2;
        int cy = card.getHeight()/2;
        float rad = (float)Math.hypot(cx,cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(card,cx,cy,0,rad);
        card.setVisibility(View.VISIBLE);

        anim.start();

    }

    public void collapseTable(final CardView card){

        int cx = card.getWidth()/2;
        int cy = card.getHeight()/2;
        float rad = (float)Math.hypot(cx,cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(card,cx,cy,rad,0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                card.setVisibility(View.GONE );
            }
        });

        anim.start();

    }



}
