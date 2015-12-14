package cmpe277.project.skibuddy;

/**
 * Created by akankshanagpal on 12/1/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EventFragment currentEventsFragment = new EventFragment();
                currentEventsFragment.setIsPastEventFragment(false);
                return currentEventsFragment;
            case 1:
                EventFragment pastEventsFragment = new EventFragment();
                pastEventsFragment.setIsPastEventFragment(true);
                return pastEventsFragment;
            
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}