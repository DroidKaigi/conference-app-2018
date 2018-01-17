# Support Design library
# We use reflection to change these, so we need to keep them.

-keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
    private boolean mShiftingMode;
}
-keepclassmembers class android.support.design.internal.BottomNavigationItemView {
    private int mDefaultMargin;
}
