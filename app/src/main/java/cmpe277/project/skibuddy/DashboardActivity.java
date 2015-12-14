package cmpe277.project.skibuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    ImageButton runButton,createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Dashboard");

        //Code for Image Button
        addListenerOnButton();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_create_event:
                startCreateEvent();
                break;
            case R.id.action_personal_run:
                startRunActivity();
                break;
            case R.id.action_sign_out:
                doSignOut();
                break;
        }

        return true;
    }

    public void addListenerOnButton() {

        runButton = (ImageButton) findViewById(R.id.dashboard_record_run_btn);

        runButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startRunActivity();

            }

        });

        createButton = (ImageButton) findViewById(R.id.imageButton2);

        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startCreateEvent();

            }
        });

    }

    private void startCreateEvent() {
        Intent i = new Intent(DashboardActivity.this, CreateEvent.class);
        startActivity(i);
    }

    private void startRunActivity() {
        Intent i = new Intent(DashboardActivity.this, RunActivity.class);
        startActivity(i);
    }

    private void doSignOut() {
        Intent i = new Intent(DashboardActivity.this, SignInActivity.class);
        Bundle b = new Bundle();
        b.putString(BundleKeys.SIGNOUT, "YES");
        i.putExtras(b);
        startActivity(i);
    }


}
