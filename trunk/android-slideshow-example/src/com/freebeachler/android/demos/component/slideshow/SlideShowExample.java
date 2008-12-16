/**
 * SlideShow is an Android Custom View that displays a set of slides.
 *
 * Copyright (C) 2008-2009 Free Beachler
 * <http://code.google.com/p/android-slide-show/>
 *
 * SlideShow is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * SlideShow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SlideShow.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.freebeachler.android.demos.component.slideshow;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.freebeachler.android.component.slideshow.DrawableSlide;
import com.freebeachler.android.component.slideshow.R;
import com.freebeachler.android.component.slideshow.SlideShow;

/**
 * This Activity demonstrates the use of the SlideShow custom component. It
 * loads 4 slides into a simple layout - 3 from local resources and one from a
 * background loader.
 * 
 * @author freebeachler
 * 
 */
public class SlideShowExample extends Activity {
    /**
     * SlideShow view displayed in this activity from XML.
     */
    private SlideShow ss;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initLayout();
    }

    /**
     * initalize layout and set listeners
     */
    public void initLayout() {
        ss = (SlideShow) findViewById(R.id.slide_show);

        // slideshow listeners
        ss.setSlideShowListener(new SlideShow.SlideShowListener() {

            public void onLoadSlidesEnd(SlideShow slideshow) {
                Toast.makeText(SlideShowExample.this, R.string.label_ss_load_end,
                        Toast.LENGTH_SHORT).show();
            }

            public void onLoadSlidesStart(SlideShow slideshow) {
                Toast.makeText(SlideShowExample.this, R.string.label_ss_load_start,
                        Toast.LENGTH_SHORT).show();
            }
        });
        ss.setSlideShowAnimationListener(new SlideShow.SlideShowAnimationListener() {

            public void onSlideAnimationEnd(Animation animation) {
                //your implementation here
            }

            public void onSlideAnimationRepeat(Animation animation) {
                //your implementation here
            }

            public void onSlideAnimationStart(Animation animation) {
                //your implementation here
            }
        });

        // view listeners
        ImageButton buttPrev = (ImageButton) findViewById(R.id.butt_slide_main_prev);
        ImageButton buttNext = (ImageButton) findViewById(R.id.butt_slide_main_next);
        buttNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    ss.drawNextSlide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        buttPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    ss.drawPrevSlide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        
        //get slides
        ss.setSlides(getSlides());
        ss.loadSlides();
    }

    /**
     * load slides for slide show
     * 
     * @return
     */
    private LinkedList<DrawableSlide> getSlides() {
        LinkedList<DrawableSlide> slides = new LinkedList<DrawableSlide>();
        // define list of resource ids
        ArrayList<Integer> imageList = new ArrayList<Integer>();
        imageList.add(Integer.valueOf(R.drawable.new_zeal));
        imageList.add(Integer.valueOf(R.drawable.eclipse_from_moon));
        imageList.add(Integer.valueOf(R.drawable.halebopp));
        // load resource ids
        for (int i = 0; i < imageList.size(); i++) {
            DrawableSlide slide = new DrawableSlide(this.getBaseContext().getResources()
                    .getDrawable(imageList.get(i)));
            slides.add(slide);
        }
        // add url slide - defer loading for manual call
        URL slideUri1 = null, slideUri2 = null;
        try {
            slideUri1 = new URL(
                    "http://farm3.static.flickr.com/2250/2264380240_cd9bb6e704.jpg");
            slideUri2 = new URL(
                    "http://farm3.static.flickr.com/2398/2264381278_d31ab2ffc9.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        slides.add(new DrawableSlide(slideUri1, true));
        slides.add(new DrawableSlide(slideUri2, true));

        return slides;
    }
}