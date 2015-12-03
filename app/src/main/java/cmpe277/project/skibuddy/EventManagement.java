package cmpe277.project.skibuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by rnagpal on 11/30/15.
 */
public class EventManagement extends AppCompatActivity {

    ImageButton button;
    TextView tvEventName,tvDesc,tvStartDate,tvEndDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_management);
        Intent intent = getIntent();
        HashMap<String,String> mpResult = (HashMap<String,String>)intent.getSerializableExtra("eventMap");

        tvEventName = (TextView)findViewById(R.id.tvEventName);
        tvDesc = (TextView)findViewById(R.id.tvEventDesc);
        tvStartDate = (TextView)findViewById(R.id.tvStartDate);
        tvEndDate = (TextView)findViewById(R.id.tvEndDate);

        tvEventName.setText(mpResult.get("name"));
        tvDesc.setText(mpResult.get("desc"));
        tvStartDate.setText(mpResult.get("startDate"));
        tvEndDate.setText(mpResult.get("endDate"));


        //Code for Tab Implementation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Participants"));
        tabLayout.addTab(tabLayout.newTab().setText("Runs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final EventManagerAdapter adapter = new EventManagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
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

        button = (ImageButton) findViewById(R.id.imageButton1);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, InviteUser.class);
                intent.putExtra("email", "myemail@gmail.com");
                startActivity(intent);

            }

        });

    }
}
