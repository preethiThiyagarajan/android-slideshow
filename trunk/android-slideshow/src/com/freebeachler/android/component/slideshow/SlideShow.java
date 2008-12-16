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

package com.freebeachler.android.component.slideshow;

import java.util.LinkedList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.freebeachler.android.component.slideshow.R;

/**
 * Cutom SlideShow component.
 * This component supports loading {@link com.freebeachler.android.component.slideshow.DrawableSlide}s which can be either
 * {@link android.drawable.Drawable}s or {@link java.net.URL}s.  Known supported {@link android.drawable.Drawable}s 
 * are Drawable and BitmapDrawable.
 * 
 * @author freebeachler
 *
 */
public class SlideShow extends View {

    public static final int SLIDESHOW_DEFAULT_WIDTH = 281;
    public static final int SLIDESHOW_DEFAULT_HEIGHT = 150;
    public static final int DEFAULT_CURRENT_SLIDE_INDEX_START = 0;
    
    /**
     * Use this listener interface to catch slide show events.
     * 
     * @author freebeachler
     *
     */
    public static interface SlideShowListener {
        /**
         * Called when slides begin loading.
         */
        public void onLoadSlidesStart(SlideShow slideshow);
        
        /**
         * Called when slides are finished loading.
         */
        public void onLoadSlidesEnd(SlideShow slideshow);
    }

    /**
     * Use this listener interface to catch slide show events.
     * 
     * @author freebeachler
     *
     */
    public static interface SlideShowAnimationListener {
        /**
         * Called when slide animation starts.
         */
        public void onSlideAnimationStart(Animation animation);

        /**
         * Called when slide animation ends.
         */
        public void onSlideAnimationEnd(Animation animation);

        /**
         * Called when slide animation repeats.
         */
        public void onSlideAnimationRepeat(Animation animation);
    }

    /**
     * 
     * @author freebeachler
     *
     */
    private class SlideAnimationListener implements Animation.AnimationListener {
        
        public SlideAnimationListener() { }

        /* (non-Javadoc)
         * @see android.view.animation.Animation.AnimationListener#onAnimationEnd(android.view.animation.Animation)
         */
        public void onAnimationEnd(Animation animation) {
            if (slideShowAnimationListener != null) {
                slideShowAnimationListener.onSlideAnimationEnd(animation);
            }
        }

        /* (non-Javadoc)
         * @see android.view.animation.Animation.AnimationListener#onAnimationRepeat(android.view.animation.Animation)
         */
        public void onAnimationRepeat(Animation animation) {
            if (slideShowAnimationListener != null) {
                slideShowAnimationListener.onSlideAnimationRepeat(animation);
            }
        }

        /* (non-Javadoc)
         * @see android.view.animation.Animation.AnimationListener#onAnimationStart(android.view.animation.Animation)
         */
        public void onAnimationStart(Animation animation) {
            if (slideShowAnimationListener != null) {
                slideShowAnimationListener.onSlideAnimationStart(animation);
            }
        }
    }
    
    /**
     * image display height
     */
    private int mDispHeight;
    
    /**
     * cache current slide
     */
    private DrawableSlide mCurrentSlide;
    
    /**
     * linked list of slides
     */
    private LinkedList<DrawableSlide> mSlides;
    
    /**
     * index of current slide being displayed
     */
    private int mCurrentSlideIndex;
    
    /**
     * first animation to apply to slide drawable
     * when hiding current slide in
     * slide list
     */
    private AlphaAnimation mHideCurrentAnimation;

    /**
     * animation set to apply to slide drawable
     * when hiding current slide in
     * slide list
     */
    private AnimationSet mHideCurrentAnimationSet;
    
    /**
     * first animation to apply to slide drawable
     * when traversing 'forward' through
     * slide list
     */
    private TranslateAnimation mNextAnimation;

    /**
     * animation set to apply to slide drawable
     * when traversing 'forward' through
     * slide list
     */
    private AnimationSet mNextAnimationSet;

    /**
     * animation to apply to slide drawable
     * when traversing 'backward' through
     * slide list
     */
    private TranslateAnimation mPrevAnimation;

    /**
     * animation set to apply to slide drawable
     * when traversing 'backward' through
     * slide list
     */
    private AnimationSet mPrevAnimationSet;

    /**
     * Default callback for slideshow.
     */
    private SlideShowListener slideShowListener;
    
    /**
     * Default callback for slideshow.
     */
    private SlideShowAnimationListener slideShowAnimationListener;
    
    /**
     * Handler for thread that handles loading slides in slideshow.
     */
    private Handler slideShowFinishedLoadingHandler;
    
    /**
     * True if all slides are loaded.
     */
    private boolean slideShowLoaded;

    public SlideShow(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SlideShow);

        mCurrentSlideIndex = a.getInt(R.styleable.SlideShow_startIndex, DEFAULT_CURRENT_SLIDE_INDEX_START);

        mSlides = new LinkedList<DrawableSlide>();
        setFocusable(true);
        setFocusableInTouchMode(true);
        slideShowFinishedLoadingHandler = new Handler();
        slideShowListener = null;
        slideShowAnimationListener = null;
        mCurrentSlide = null;
        mDispHeight = SLIDESHOW_DEFAULT_HEIGHT;
        initAnimationSets();
    }

    /**
     * @return the Slides to display
     */
    @SuppressWarnings("unused")
    private LinkedList<DrawableSlide> getSlides() {
	return mSlides;
    }

    /**
     * Set the {@link com.freebeachler.android.component.slideshow.DrawableSlide}s used by this {@link com.freebeachler.android.component.slideshow.SlideShow}.
     * Also sets/resets the state of the SlideShow, such as whether slides are
     * loaded.
     * 
     * @param set Slides to display
     */
    public void setSlides(LinkedList<DrawableSlide> slides) {
	this.mSlides = slides;
	checkSlidesLoaded();
    }

    /**
     * @return the currentSlideIndex
     */
    public int getCurrentSlideIndex() {
        return mCurrentSlideIndex;
    }

    /**
     * @param currentSlideIndex the currentSlideIndex to set
     */
    public void setCurrentSlideIndex(int currentSlideIndex) {
        this.mCurrentSlideIndex = currentSlideIndex;
    }

    /**
     * @return the mHideCurrentAnimation
     */
    public Animation getHideCurrentAnimation() {
        return mHideCurrentAnimation;
    }

    /**
     * @param hideCurrentAnimation the mHideCurrentAnimation to set
     */
    public void setHideCurrentAnimation(AlphaAnimation hideCurrentAnimation) {
        this.mHideCurrentAnimation = hideCurrentAnimation;
    }

    /**
     * @return the mNextAnimation
     */
    public Animation getNextAnimation() {
        return mNextAnimation;
    }

    /**
     * @param nextAnimation the mNextAnimation to set
     */
    public void setNextAnimation(TranslateAnimation nextAnimation) {
        this.mNextAnimation = nextAnimation;
    }

    /**
     * @return the mPrevAnimation
     */
    public Animation getPrevAnimation() {
        return mPrevAnimation;
    }

    /**
     * @param prevAnimation the mPrevAnimation to set
     */
    public void setPrevAnimation(TranslateAnimation prevAnimation) {
        this.mPrevAnimation = prevAnimation;
    }
    
    /**
     * @param slideShowAnimationListener the slideShowAnimationListener to set
     */
    public void setSlideShowAnimationListener(
            SlideShowAnimationListener slideShowAnimationListener) {
        this.slideShowAnimationListener = slideShowAnimationListener;
    }

    /**
     * @param slideShowListenerCallback the slideShowListenerCallback to set
     */
    public void setSlideShowListener(
            SlideShowListener slideShowListener) {
        this.slideShowListener = slideShowListener;
    }

    /**
     * @see android.view.View#measure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }
    
    /**
     * render the slide
     * 
     * @param Canvas canvas to render slideshow on
     * @todo {@see ISlideAsset.getSlideAsset}
     */
    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect slideBounds = null;

        if (this.getBackground() == null) {
            canvas.drawColor(android.R.color.background_light);
        }
        if (mCurrentSlide != null && mCurrentSlide.isSlideLoaded()) {
            slideBounds = new Rect(0, 0, getCurrentSlideScaledImageWidth(), mDispHeight);
            //size it and preserve aspect
            mCurrentSlide.getSlideAsset().setBounds(slideBounds);
            mCurrentSlide.draw(canvas);
        }
        invalidate();
    }

    /**
     * Initialize default animation sets.
     */
    private void initAnimationSets() {
        mHideCurrentAnimation = new AlphaAnimation(0.0f, 1.0f);
        mHideCurrentAnimation.setRepeatCount(Animation.INFINITE);
        mHideCurrentAnimation.setRepeatMode(Animation.RESTART);
        mHideCurrentAnimation.initialize(SLIDESHOW_DEFAULT_WIDTH, 
                SLIDESHOW_DEFAULT_HEIGHT, 
                SLIDESHOW_DEFAULT_WIDTH, 
                SLIDESHOW_DEFAULT_HEIGHT);
        mHideCurrentAnimation.setDuration(280);

        mPrevAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f);
        mPrevAnimation.setDuration(400);
        mPrevAnimation.initialize(SLIDESHOW_DEFAULT_WIDTH, 
                SLIDESHOW_DEFAULT_HEIGHT, 
                SLIDESHOW_DEFAULT_WIDTH, 
                SLIDESHOW_DEFAULT_HEIGHT);

        mNextAnimation = new TranslateAnimation(281, 0, 0, 0);
        mNextAnimation.setDuration(400);
        mNextAnimation.setRepeatCount(0);
        mNextAnimation.initialize(SLIDESHOW_DEFAULT_WIDTH, 
                SLIDESHOW_DEFAULT_HEIGHT, 
                SLIDESHOW_DEFAULT_WIDTH, 
                SLIDESHOW_DEFAULT_HEIGHT);
        mNextAnimation.setAnimationListener(new SlideShow.SlideAnimationListener());

        mHideCurrentAnimationSet = new AnimationSet(true);
        mHideCurrentAnimationSet.addAnimation(mHideCurrentAnimation);

        mNextAnimationSet = new AnimationSet(true);
        mNextAnimationSet.addAnimation(mNextAnimation);

        mPrevAnimationSet = new AnimationSet(true);
        mPrevAnimationSet.addAnimation(mPrevAnimation);

        //thread safe to set listeners here?
        mNextAnimationSet.setAnimationListener(new SlideShow.SlideAnimationListener());
        mPrevAnimationSet.setAnimationListener(new SlideShow.SlideAnimationListener());
    }
    
    /**
     * Load or re-load any slides that are not yet loaded.
     */
    public void loadSlides() {
        this.slideShowLoaded = false;
        this.onLoadSlidesStart();
    }

    /**
     * If all slides loaded then slideshow flag to loaded.
     */
    private boolean checkSlidesLoaded() {
        //assume not loaded
        this.slideShowLoaded = false;
        for (DrawableSlide slide : this.mSlides) {
            if (!slide.isSlideLoaded()) {
                return false;
            }
        }
        this.slideShowLoaded = true;
        return this.slideShowLoaded;
    }

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //we were told how big to be
            result = specSize;
        } else {
            if (mCurrentSlide != null) {
                //measure the slide
                result = getCurrentSlideScaledImageWidth();
            } else {
                result = SLIDESHOW_DEFAULT_WIDTH;
            }
        }

        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //we were told how big to be
            result = specSize;
        } else {
            result = SLIDESHOW_DEFAULT_HEIGHT;
        }
        return result;
    }

    /**
     * get scaled width of current slide based on height set for class
     * @todo {@see ISlideAsset.getSlideAsset}
     * @return
     */
    private int getCurrentSlideScaledImageWidth() {
        //load image from list
        DrawableSlide dr = mSlides.get(mCurrentSlideIndex);
        //size it and preserve aspect
        int dispWidth = dr.getSlideAsset().getIntrinsicWidth();
        if (dr.getSlideAsset().getIntrinsicHeight() > SLIDESHOW_DEFAULT_HEIGHT) {
            //preserve aspect ratio 1:1
            dispWidth = (int) ((float) dr.getSlideAsset().getIntrinsicWidth() * ((float) SLIDESHOW_DEFAULT_HEIGHT / (float) dr.getSlideAsset().getIntrinsicHeight()));
        }

	return dispWidth;
    }

    /**
     * @param slideIndex the currentSlideIndex to set for drawing
     */
    public DrawableSlide getSlideAtIndex(int slideIndex) {
	//load image from list
        DrawableSlide dr = mSlides.get(slideIndex);
        return dr;
    }

    public DrawableSlide getCurrentSlide() {
	mCurrentSlide = getSlideAtIndex(mCurrentSlideIndex);
	return mCurrentSlide;
    }

    /**
     * garbage collection for current slide
     */
    public void clearCurrentSlide()  {
	if(mCurrentSlide == null) {
	    return;
	}
	
	mCurrentSlide.clearSlide();
        mCurrentSlide = null;
    }

    /**
     * draws the current slide using animation defined by one of
     * @param direction {@link com.freebeachler.android.component.slideshow.Slide#SLIDE_DIRECTION_FORWARD} draws slide with next animation {@link com.freebeachler.android.component.slideshow.Slide#SLIDE_DIRECTION_BACKWARD} draws slide with previous animation
     * @throws Exception 
     */
    public void drawCurrentSlide(int direction) throws Exception  {
	if (mSlides == null) {
	    StringBuffer msg = new StringBuffer();
	    msg.append("Cannot initialize an empty slideshow.  Try calling setSlides() first.");
	    throw new Exception(msg.toString());
	}
        if (!this.checkSlidesLoaded()) {
            StringBuffer msg = new StringBuffer();
            msg.append("Slideshow still loading.  Defer loading the current slide and use a SlideShowListener to avoid this error.");
            throw new Exception(msg.toString());
        }
	if (mCurrentSlideIndex >= mSlides.size()) {
	    StringBuffer msg = new StringBuffer();
	    msg.append("Cannot initialize slideshow at index " + Integer.toString(mCurrentSlideIndex) + ".  SlideShow size is " + Integer.toString(mSlides.size()) + ".");
	    throw new Exception(msg.toString());
	}
	getCurrentSlide();
        //get animation
        AnimationSet an;
        switch(direction) {
            case DrawableSlide.SLIDE_DIRECTION_BACKWARD:
                an = mPrevAnimationSet;
                break;
            default:
                //default to next animation
                an = mNextAnimationSet;
        }
        mCurrentSlide.setShowAnimation(an);
        mCurrentSlide.getShowAnimation().start();
    }

    /**
     * increment current slide index
     * loop to beginning of list if at end
     * 
     * @param direction
     * @throws Exception 
     */
    public void drawNextSlide() throws Exception {
	if (!this.checkSlidesLoaded()) {
	    return;
	}
        //increment slide index, get next slide, set animation
        mCurrentSlideIndex++;
        if(mCurrentSlideIndex >= mSlides.size()) {
            mCurrentSlideIndex = 0;
        }
        drawCurrentSlide(DrawableSlide.SLIDE_DIRECTION_FORWARD);
    }

    /**
     * increment current slide index
     * loop to beginning of list if at end
     * 
     * @param direction
     * @throws Exception 
     */
    public void drawPrevSlide() throws Exception  {
        if (!this.checkSlidesLoaded()) {
            return;
        }
	mCurrentSlideIndex--;
	if(mCurrentSlideIndex < 0) {
	    mCurrentSlideIndex = mSlides.size() - 1;
	}
        drawCurrentSlide(DrawableSlide.SLIDE_DIRECTION_BACKWARD);
    }

    /**
     * Calls {@link com.freebeachler.android.component.Slide.loadSlide()} for each {@link com.freebeachler.android.component.slideshow.DrawableSlide} 
     * in slideshow in a seperate thread.
     */
    private void onLoadSlidesStart() {
        //do nothing if slides already loaded or no slides set
        if (this.mSlides.size() < 1) {
            return;
        }
        if (checkSlidesLoaded()) {
            //slides are already loaded - display and trigger event
            onLoadSlidesEnd(null, null);
        }
        new Thread(new Runnable() {

            public void run() {
                Looper.prepare();
                Object result = null;
                Exception error = null;
                try {
                    for (DrawableSlide slide : mSlides) {
                        //load slide form uri or do nothing
                        slide.load();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                    error = e;
                }

                final Object finalResult = result;
                final Exception finalError = error;
                slideShowFinishedLoadingHandler.post(new Runnable() {

                    public void run() {
                        onLoadSlidesEnd(finalResult, finalError);
                    }
                });
            }
        }).start();
        if (slideShowListener != null) {
            slideShowListener.onLoadSlidesStart(this);
        }
    }

    private void onLoadSlidesEnd(Object finalResult, Exception finalError) {
        this.checkSlidesLoaded();
        if (finalError != null && finalError.getStackTrace() != null) {
            finalError.printStackTrace();
            return;
        }
        try {
            drawCurrentSlide(DrawableSlide.SLIDE_DIRECTION_FORWARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (slideShowListener != null) {
            slideShowListener.onLoadSlidesEnd(this);
        }
    }
}