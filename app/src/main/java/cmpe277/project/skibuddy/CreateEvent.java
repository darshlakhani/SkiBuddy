package cmpe277.project.skibuddy;

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
import android.widget.EditText;
import android.widget.Toast;

//<<<<<<< HEAD
//<<<<<<< HEAD
import com.parse.Parse;
import com.parse.ParseObject;
//=======
//=======
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
//>>>>>>> 1b2746c41940e8edcb18e05e69c77fd3773aa087
import java.util.UUID;
//>>>>>>> f0a29f3fb4d94aafcdd313c68165917f98a1407b

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.ParseServer;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.PojoEvent;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class CreateEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

//<<<<<<< HEAD
        /*Server s = new ParseServer(this);

//<<<<<<< HEAD
      *//*  Parse.initialize(this, "QY0YiXoRaSmEDYBprKbQSgUMAPX2EgYaNF4spnLt", "c0CDe7W7J4aMeWJUpeuxMCP6vBalpS6oEnyOmWmC");

        ParseObject testObject = new ParseObject("Event");
        testObject.put("eventName", "ski1");
        testObject.saveInBackground(); *//*
//=======
        try {
            s.storeEvent(new Event());
        } catch (Exception e) {
            Toast t = Toast.makeText(this, "Couldn't save event", Toast.LENGTH_SHORT);
            t.show();
//        }*/
//>>>>>>> 626ea19ae1769d73a9b3b2b961c0f7b89c7ab494
//=======
        Button createEventButton = (Button) findViewById(R.id.btCreateEventButton);


        final Context self = this;
        final Server ss = new ServerSingleton().getServerInstance(self);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ss.authenticateUser("abc", new ServerCallback<User>() {
                    @Override
                    public void handleResult(User result) {
                        // All callbacks may return null if the operation failed!
                        String text;
                        if (result == null){
                            text = "Could not get user!";
                        } else {
                            text = String.format("got user %s", result.getName());
                        }*/
                EditText etEventName = (EditText) findViewById(R.id.etEventName);
                final String eventName = etEventName.getText().toString();

                EditText etEventDesc = (EditText) findViewById(R.id.etEventDescription);
                String eventDescription = etEventDesc.getText().toString();


                EditText etDate = (EditText) findViewById(R.id.etEventDate);
                String eventDate = etDate.getText().toString();

                EditText etStartTime = (EditText) findViewById(R.id.etEventStartTime);
                String startTime = etStartTime.getText().toString();
                    startTime = eventDate + " " + startTime;

                EditText etEndtime = (EditText) findViewById(R.id.etEventEndTime);
                String endTime = etEndtime.getText().toString();
                endTime = eventDate + " " + endTime;

                DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
                DateTime sTime = formatter.parseDateTime(startTime);

                DateTime eTime = formatter.parseDateTime(endTime);

                PojoEvent event = new PojoEvent();
                    event.setName(eventName);
                    event.setDescription(eventDescription);
                    event.setStart(sTime);
                    event.setEnd(eTime);
                    ss.storeEvent(event,new ServerCallback<UUID>() {
                        @Override
                        public void handleResult(UUID result) {
                            if (result == null) {
                                Toast t = Toast.makeText(self, "Couldn't save event", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        }
                    });

                    String tdsp = endTime +","+ startTime +","+eventDate;
                Log.i("tag1", endTime);
                Log.i("tag2", startTime);
                Log.i("tag3", eventDate);
                Toast t = Toast.makeText(self, startTime, Toast.LENGTH_LONG);
                        t.show();
                    }
                });

//<<<<<<< HEAD
           /*   ss.storeEvent(event, new ServerCallback<UUID>() {
                    @Override
                    public void handleResult(UUID result) {
                        if (result == null) {
                            Toast t = Toast.makeText(self, "Couldn't save event", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });

/*
=======
        s.getEvents(new ServerCallback<List<Event>>() {
            @Override
            public void handleResult(List<Event> result) {

            }
        });

        Event newEvent = ServerSingleton.createEvent();
        newEvent.setName("Some cool event");

        s.storeEvent(newEvent, new ServerCallback<UUID>() {
            @Override
            public void handleResult(UUID result) {
                if(result == null){
                    Toast t = Toast.makeText(self, "Couldn't save event", Toast.LENGTH_SHORT);
                    t.show();
                }
>>>>>>> 1b2746c41940e8edcb18e05e69c77fd3773aa087
            }
        });
*/


//>>>>>>> f0a29f3fb4d94aafcdd313c68165917f98a1407b
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
