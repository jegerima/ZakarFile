package com.example.jegerima.SIDWeb.adapters;

import android.app.ActionBar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Cesar on 07/02/2015.
 */
public class SWAnimation {
    public static void expand(final View v) {
        v.measure(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        v.setPadding(30,0,10,0);//paddig para crear un tab para que se vea aspecto de submenu
        final int targetHeight = 100;//tama~no maximo que tendran cada una de las materias
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                if(interpolatedTime==1)
                    ;//v.getLayoutParams().height =ActionBar.LayoutParams.WRAP_CONTENT;
                else
                    v.getLayoutParams().height=(int)(targetHeight * interpolatedTime);

                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration(500);//duracion de la transicion
        v.startAnimation(a);
    }


    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(500);
        v.startAnimation(a);
    }



}
