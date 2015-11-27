# Details #
Unfortunately, due to [the component's usage of XML attributes and limitation of the 1.0 SDK](http://groups.google.com/group/android-developers/browse_thread/thread/3b252322deabc9d7?hl=en) it isn't currently possible to distribute this component as a JAR.  _The XML attributes are to be removed soon until the SDK support is enhanced_.

For now you must follow these manual steps to get it installed:
  1. Checkout the SlideShow component anonymously from the [0.6 stable release of this project](http://android-slideshow.googlecode.com/svn/branches/releases/stable/0.6/android-slideshow).  See http://code.google.com/p/android-slideshow/source/checkout for more about checking out files from this project.
    1. Either check out the code directly into your project - or -
    1. Copy the src/ directory from the files you checked out to your project.
  1. Satisfy dependency on custom attribute in R.java (Android resources package).  Typically this means:
    1. Add the "startIndex" attribute to your attrs.xml.
    1. Build R.java (via Eclipse or Maven)
    1. Re-import R.java into SlideShow.java.

# Demo #
For details on running the example(s) in this project see [RunSlideShowExample](RunSlideShowExample.md).