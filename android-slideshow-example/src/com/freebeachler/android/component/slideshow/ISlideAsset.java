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

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;

/**
 * @author freebeachler
 *
 */
public interface ISlideAsset {
    
    /**
     * Draw the slide asset to a target canvas.
     * @param canvas
     */
    public void draw(Canvas canvas);

    /**
     * Get the intrinsic width of the slide asset.
     * @return
     */
    public int getIntrinsicWidth();

    /**
     * Get the intrinsic height of the slide asset.
     * @return
     */
    public int getIntrinsicHeight();
    
    /**
     * Get the opacity used on the slide asset.
     * @return
     */
    public int getOpacity();
    
    /**
     * Set the bounds to use on the slide asset.
     * @param top
     * @param left
     * @param bottom
     * @param right
     */
    public void setBounds(int top, int left, int bottom, int right);

    /**
     * Set the bounds to use on this asset.
     * @param bounds
     */
    public void setBounds(Rect bounds);

    /**
     * Set the filter to use on this asset's bitmap - if any.
     * @param filter
     */
    public void setFilterBitmap(boolean filter);

    /**
     * Set the dither to use on the slide asset.
     * @param dither
     */
    public void setDither(boolean dither);

    /**
     * Set the Color Filter to use on the slide asset.
     * @param colorFilter
     */
    public void setColorFilter(ColorFilter colorFilter);

    /**
     * Set the alpha opacity to use on the slide asset.
     * @param alpha
     */
    public void setAlpha(int alpha);

}
