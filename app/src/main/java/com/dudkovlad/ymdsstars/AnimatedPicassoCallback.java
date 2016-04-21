package com.dudkovlad.ymdsstars;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;


/**
 * Created by vlad on 18.04.16. todo make description
 */
public class AnimatedPicassoCallback implements Callback {

    private ImageView imageViewTempBackground;
    private ImageView imageView;

    public AnimatedPicassoCallback (
            ImageView imageView
            , ImageView imageViewTempBackground ) {

        this.imageView = imageView;
        this.imageViewTempBackground = imageViewTempBackground;
    }

    @Override
    public void onSuccess () {

        if ( (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
              && imageView.isAttachedToWindow ())
             || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ) {

            imageViewTempBackground.setBackground ( imageView.getBackground () );
            imageViewTempBackground.setVisibility ( View.VISIBLE );

            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {


                int cx          = (int)imageView.getX () + imageView.getMeasuredWidth ()/2;
                int cy          = (int)imageView.getY ();// + imageView.getMeasuredHeight() / 2;
                int finalRadius = imageView.getWidth ();

                Animator anim = ViewAnimationUtils
                        .createCircularReveal ( imageView, cx, cy, 0, finalRadius );
                anim.setDuration ( 500 );
                anim.addListener (
                        new AnimatorListenerAdapter () {

                            @Override
                            public void onAnimationEnd ( Animator animation ) {

                                super.onAnimationEnd ( animation );
                                imageViewTempBackground.setVisibility ( View.GONE );
                            }
                        } );
                anim.start ();


            } else {

                imageView.setAlpha ( 0f );
                imageView.animate ()
                         .alpha ( 1f )
                         .setDuration ( 500 )
                         .setListener (
                                 new AnimatorListenerAdapter () {

                                     @Override
                                     public void onAnimationEnd ( Animator animation ) {

                                         super.onAnimationEnd ( animation );
                                         imageView.setAlpha ( 1f );
                                         imageViewTempBackground.setVisibility ( View.GONE );
                                     }
                                 } )
                         .start ();

            }
        }
    }

    @Override
    public void onError () {

    }
}
