package cmpe277.project.skibuddy;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class CreateEvent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button createEventButton = (Button) findViewById(R.id.createEventButton);
        final Context self = this;
        final Server s = new ServerSingleton().getServerInstance(self);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.authenticateUser("abc", new ServerCallback<User>() {
                    @Override
                    public void handleResult(User result) {
                        // All callbacks may return null if the operation failed!
                        String text;
                        if (result == null){
                            text = "Could not get user!";
                        } else {
                            text = String.format("got user %s", result.getName());
                        }
                        Toast t = Toast.makeText(self, text, Toast.LENGTH_LONG);
                        t.show();
                    }
                });
            }
        });

        s.storeEvent(new Event(), new ServerCallback<UUID>() {
            @Override
            public void handleResult(UUID result) {
                if(result == null){
                    Toast t = Toast.makeText(self, "Couldn't save event", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
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
