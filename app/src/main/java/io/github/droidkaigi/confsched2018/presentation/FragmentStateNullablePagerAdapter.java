package io.github.droidkaigi.confsched2018.presentation;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * memo:
 * FragmentStateNullablePagerAdapter needs to be written in Kotlin.
 * cuz, setPrimaryItem signature is #setPrimaryItem(@NonNull ViewGroup, int, @NonNull Object),
 * but Object come in null, so kotlin occurs NPE crash
 */
public abstract class FragmentStateNullablePagerAdapter extends FragmentStatePagerAdapter {

    public FragmentStateNullablePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPrimaryItem(ViewGroup container, int position, @Nullable Object object) {
        super.setPrimaryItem(container, position, object);
    }
}
