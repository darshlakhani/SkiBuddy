package cmpe277.project.skibuddy;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.Server;
import cmpe277.project.skibuddy.server.ParseServer;

public class CreateEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Server s = new ParseServer(this);

        try {
            s.storeEvent(new Event());
        } catch (NotAuthenticatedException e) {
            Toast t = Toast.makeText(this, "Couldn't save event", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // this.menu = menu;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            SearchView search = (SearchView) menu.findItem(R.id.action_search1).getActionView();

            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String query) {


                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String keywords) {

                    //loadHistory(query);

                    Log.d("Search", "Inside querychange");



                    //  ad = new ArrayAdapter<VideoItem>(MainActivity.this, android.R.layout.simple_list_item_1, searchResults);

                    //  videoListV.setAdapter(ad);
                    return true;
                }

            });

        }

        return true;
    }


}
