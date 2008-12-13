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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

/**
 * @author freebeachler
 *
 */
public class DrawableSlide extends Slide implements Drawable.Callback {

    private static final int IO_BUFFER_SIZE = 4096;

    /**
     * proxy drawable object drives this class
     */
    private ProxyDrawable dr;
    
    /**
     * input stream
     */
    private boolean drawableLoaded;
    
    /**
     * 
     * @param target
     */    
    public DrawableSlide(Drawable target) {
        clearSlide();
        dr = new ProxyDrawable(target);
        //setup animation
        dr.setCallback(this);
        setTransformation(new Transformation());
        //call it done
        setDrawableLoaded(true);
    }

    /**
     * 
     * @param target
     * @param animation
     */    
    public DrawableSlide(Drawable target, Animation animation) {
        this(target);
        setShowAnimation(animation);
    }

    /**
     * 
     * @param target
     * @param animation
     */    
    public DrawableSlide(Drawable target, AnimationSet animation) {
        this(target);
        setShowAnimation(animation);
    }

    /**
     * Build a slide from a URL and build its drawable.  Beware
     * building several slides from a UI activity thread - in such
     * cases this constructor should be called in a seperate thread
     * or by setting defer to true.  Images can then be loaded via manual 
     * call to {@link com.freebeachler.android.component.slideshow.DrawableSlide#loadSlide()} 
     * by setting defer to false.
     * 
     * If an eror occurs when loading image from slideUri the drawable
     * will be left blank.
     * 
     * @param target
     * @param defer defer loading the slide for slideshow or activity invocation of {@link Slide.loadSlide()}
     */
    public DrawableSlide(URL slideUri, boolean defer) {
        this(null);
        //set bitmap drawable with defer option
        setSlideUri(slideUri, defer);
    }
    
    /**
     * @inheritDoc
     */
    public Drawable getSlideAsset() {
        return (Drawable) dr.getProxy();
    }

    /**
     * @inheritDoc
     */
    public void draw(Canvas canvas) {
        if (dr.getProxy() != null) {

            int sc = canvas.save();
            if (getShowAnimation() != null) {
                getShowAnimation().getTransformation(
                                    AnimationUtils.currentAnimationTimeMillis(),
                                    getTransformation());
                canvas.concat(getTransformation().getMatrix());
            }
            dr.draw(canvas);
            canvas.restoreToCount(sc);
        }
    }

    /**
     * @inheritDoc
     */
    public void load() {
        if (isDrawableLoaded()) {
            return;
        }
        //known bug that cannot use Drawable.createFromStream(is, src)
        //see http://groups.google.com/group/android-developers/browse_thread/thread/4ed17d7e48899b26/a15129024bb845bf?show_docid=a15129024bb845bf&pli=1
        BufferedInputStream in = null;
        Bitmap imageBmp = null;
        try {
            in = new BufferedInputStream(getSlideUri().openStream(), IO_BUFFER_SIZE);
        } catch (IOException e) {
            setDrawableLoaded(false);
            if (e.getStackTrace() != null) {
                e.printStackTrace();
            }
        }
        imageBmp = BitmapFactory.decodeStream(in);
        dr.setProxy(new BitmapDrawable(imageBmp));
        setDrawableLoaded(true);
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public void setSlideUri(URL slideUri) {
        this.setSlideUri(slideUri, true);
    }

    /**
     * @inheritDoc
     */
    public void setSlideUri(URL slideUri, boolean defer) {
        super.setSlideUri(slideUri);
        setDrawableLoaded(false);
        dr.setProxy(null);
        if (!defer) {
            this.load();
        }
    }

    /**
     * @return the drawableLoaded
     */
    public boolean isDrawableLoaded() {
        return drawableLoaded;
    }

    /**
     * @param drawableLoaded the drawableLoaded to set
     */
    public void setDrawableLoaded(boolean drawableLoaded) {
        this.drawableLoaded = drawableLoaded;
        setSlideLoaded(drawableLoaded);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isSlideLoaded() {
        return drawableLoaded;
    }
    
    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable.Callback#invalidateDrawable(android.graphics.drawable.Drawable)
     */
    public void invalidateDrawable(Drawable who) { }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable.Callback#scheduleDrawable(android.graphics.drawable.Drawable, java.lang.Runnable, long)
     */
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        getHandler().postAtTime(what, who, when);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable.Callback#unscheduleDrawable(android.graphics.drawable.Drawable, java.lang.Runnable)
     */
    public void unscheduleDrawable(Drawable who, Runnable what) {
        getHandler().removeCallbacks(what, who);
    }

}