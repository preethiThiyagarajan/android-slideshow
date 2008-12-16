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

package com.freebeachler.android.test.compoment.slideshow;

import java.net.MalformedURLException;
import java.net.URL;

import android.test.AndroidTestCase;

import com.freebeachler.android.component.slideshow.DrawableSlide;

/**
 * @author freebeachler
 *
 */
public class SlideTest extends AndroidTestCase {
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#Slide(java.net.URL, boolean)}.
     */
    public void testSlideURLBoolean() {
        DrawableSlide slide = null;
        assertEquals(true, true);
        //test Uri Slide with defer set to true does not load immediately
        try {
            slide = new DrawableSlide(new URL("http://google.com"), true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("could not call Slide(URL, false)");
        }
        assertEquals(slide.isDrawableLoaded(), false);

        //test Uri Slide with defer set to false loads immediately
        try {
            slide = new DrawableSlide(new URL("http://google.com"), false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("could not call Slide(URL, false)");
        }
        assertEquals(slide.isDrawableLoaded(), true);
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#Slide(android.graphics.drawable.Drawable)}.
     */
    public void testSlideDrawable() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#Slide(android.graphics.drawable.Drawable, android.view.animation.Animation)}.
     */
    public void testSlideDrawableAnimation() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#Slide(android.graphics.drawable.Drawable, android.view.animation.AnimationSet)}.
     */
    public void testSlideDrawableAnimationSet() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#loadSlide()}.
     */
    public void testLoadSlide() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#setSlideUri(java.net.URL)}.
     */
    public void testSetSlideUriURL() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#setSlideUri(java.net.URL, boolean)}.
     */
    public void testSetSlideUriURLBoolean() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.freebeachler.android.component.slideshow.DrawableSlide#isDrawableLoaded()}.
     */
    public void testIsDrawableLoaded() {
        fail("Not yet implemented");
    }

}
