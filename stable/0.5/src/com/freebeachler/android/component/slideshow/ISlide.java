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

import java.net.URL;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author freebeachler
 *
 */
public interface ISlide {

    /**
     * @return the slideUri
     */
    public URL getSlideUri();
    
    /**
     * Set the slide Uri - by default the slide is not loaded immediately.
     * Beware of usage on UI thread.
     * @param slideUri the slideUri to set
     */
    public void setSlideUri(URL uri);

    /**
     * Set the slide Uri.  Any existing Drawable will be unloaded.
     * If defer is set to false the slide will be loaded immediately.
     * @param slideUri the slideUri to set
     * @param defer defer loading this slide to another time
     */
    public void setSlideUri(URL slideUri, boolean defer);
    
    /**
     * Manually set the slide loaded flag - be careful!
     */
    public void setSlideLoaded(boolean slideLoaded);
    
    /**
     * Determine if this slide's asset(s) has/have been loaded.
     * 
     * @return true if slide is loaded, false otherwise
     */
    public boolean isSlideLoaded();

    /**
     * @return the transformation used when playing animations
     */
    public Animation getHideAnimation();

    /**
     * @param transformation the transformation to set when playing animations
     */
    public void setHideAnimation(Animation anim);

    /**
     * Get animation to show for slide.
     * @return the animation
     */
    public Animation getShowAnimation();
    
    /**
     * @param anim the animation to set
     */
    public void setShowAnimation(Animation anim);

    /**
     * @return the transformation used when playing animations
     */
    public Transformation getTransformation();

    /**
     * @param transformation the transformation to set when playing animations
     */
    public void setTransformation(Transformation transformation);
    
    /**
     * Reset everything about the slide - set to null or blank.
     */
    public void clearSlide();

    /**
     * Get the slide's visual asset(s).
     */
    public Drawable getSlideAsset();

    /**
     * Load this slide's drawable from a URL.  If the Drawable is already 
     * loaded do nothing.
     * @param slideUri
     */
    public void load();

    /**
     * Draw the slide to the specified canvas.
     * @param canvas
     */
    public void draw(Canvas canvas);

    /**
     * Helper for determining if slides animation(s) has(have) begun
     * @return true when show animation has begun, false otherwise
     */
    public boolean hasStarted();

    /**
     * Helper for determining if slides animation(s) has(have) begun
     * @return true when hide animation has ended, false otherwise
     */
    public boolean hasEnded();

    /**
     * clear all assign animations and transformations
     */
    public void clearAllAnimations();

}