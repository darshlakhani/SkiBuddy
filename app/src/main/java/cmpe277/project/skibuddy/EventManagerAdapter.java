package cmpe277.project.skibuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.UUID;

/**
 * Created by akankshanagpal on 12/2/15.
 */

public class EventManagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String eventID;

    public EventManagerAdapter(FragmentManager fm, int NumOfTabs, String eventID) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.eventID = eventID;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString(BundleKeys.EVENTID_KEY, eventID);
        switch (position) {
            case 0:
                ParticipantFragment tab1 = new ParticipantFragment();
                tab1.setArguments(fragmentBundle);
                return tab1;
            case 1:
                RunFragment tab2 = new RunFragment();
                tab2.setArguments(fragmentBundle);
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