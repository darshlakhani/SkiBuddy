package cmpe277.project.skibuddy;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

/**
 * Created by rnagpal on 11/30/15.
 */
public class EventManagement extends AppCompatActivity {

    UUID eventID;
    final Server s = new ServerSingleton().getServerInstance(this);

    TextView tvDesc,tvStartDate,tvEndDate;
    ImageButton invite,recordRun;

    final private String DATETIME_FORMAT = "MMM d, YY h:mm a";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_management);
        Bundle b = getIntent().getExtras();
        eventID = UUID.fromString(b.getString(BundleKeys.EVENTID_KEY));

        tvDesc = (TextView)findViewById(R.id.tvEventDesc);
        tvStartDate = (TextView)findViewById(R.id.tvStartDate);
        tvEndDate = (TextView)findViewById(R.id.tvEndDate);
        invite = (ImageButton) findViewById(R.id.imageButton1);
        recordRun = (ImageButton) findViewById(R.id.recordRun);

        s.getEvent(eventID, new ServerCallback<Event>() {
            @Override
            public void handleResult(Event result) {

                setTitle(result.getName());
                tvDesc.setText(result.getDescription());
                tvStartDate.setText(result.getStart()
                        .toString(DateTimeFormat.forPattern(DATETIME_FORMAT)));
                tvEndDate.setText(result.getEnd()
                        .toString(DateTimeFormat.forPattern(DATETIME_FORMAT)));

                // If the user hosts the event, they should be able to invite others
                try {
                    User currentUser = s.getAuthenticatedUser();
                    if(result.getHostId() == currentUser.getId())
                        invite.setVisibility(View.VISIBLE);
                } catch (NotAuthenticatedException e) {
                    Log.w(EventManagement.class.getName(), "User not logged in!");
                }

                // If the event is happening now, we want to be able to record runs in it
                DateTime now = DateTime.now();
                if(result.getStart().isBefore(now) && result.getEnd().isAfter(now))
                    recordRun.setVisibility(View.VISIBLE);
            }
        });

        //Code for Tab Implementation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Participants"));
        tabLayout.addTab(tabLayout.newTab().setText("Runs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final EventManagerAdapter adapter = new EventManagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), eventID.toString());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        final Context context = this;



        invite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, InviteUser.class);
                intent.putExtra(BundleKeys.EVENTID_KEY, eventID);
                startActivity(intent);

            }

        });

        recordRun.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, RunActivity.class);
                intent.putExtra(BundleKeys.EVENTID_KEY, eventID);
                startActivity(intent);

            }

        });

    }
}
