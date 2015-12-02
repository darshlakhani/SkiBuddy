package cmpe277.project.skibuddy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by akankshanagpal on 12/2/15.
 */

public class EventManagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public EventManagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PartcipantFragment tab1 = new PartcipantFragment();
                return tab1;
            case 1:
                RunFragment tab2 = new RunFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}