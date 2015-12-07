package cmpe277.project.skibuddy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.UUID;

//import cmpe277.project.skibuddy.server.PojoEvent;
import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class CreateEvent extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "cmpe277.project.skibuddy";

    EditText etEventName,etEventDesc, etDate, etStartTime, etEndtime;
    Event e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button createEventButton = (Button) findViewById(R.id.btCreateEventButton);


        final Context self = this;
        final Server ss = new ServerSingleton().getServerInstance(self);
        e = ServerSingleton.createEvent();
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
                etEventName = (EditText) findViewById(R.id.etEventName);
                // final String eventName = etEventName.getText().toString();

                etEventDesc = (EditText) findViewById(R.id.etEventDescription);
                //String eventDescription = etEventDesc.getText().toString();


                etDate = (EditText) findViewById(R.id.etEventDate);
                etStartTime = (EditText) findViewById(R.id.etEventStartTime);
                etEndtime = (EditText) findViewById(R.id.etEventEndTime);
                if(!checkEditText())
                {
                    Toast.makeText(getApplicationContext(),"Enter All Values", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.i("Create Event", "@@@@ in else");
                    String eventDate = etDate.getText().toString();


                    String startTime = etStartTime.getText().toString();
                    startTime = eventDate + " " + startTime;


                    String endTime = etEndtime.getText().toString();
                    endTime = eventDate + " " + endTime;

                DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
//                DateTime sTime = formatter.parseDateTime(startTime);
//
//                DateTime eTime = formatter.parseDateTime(endTime);

                /*PojoEvent event = new PojoEvent();
                    event.setName(eventName);
                    event.setDescription(eventDescription);
                    event.setStart(sTime);
                    event.setEnd(eTime);*/
                   /* ss.storeEvent(event, new ServerCallback<UUID>() {
                        @Override
                        public void handleResult(UUID result) {
                            if (result == null) {
                                Toast t = Toast.makeText(self, "Couldn't save event", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        }
                    });*/

                   /* String tdsp = endTime + "," + startTime + "," + eventDate;
                    Log.i("tag1", endTime);
                    Log.i("tag2", startTime);
                    Log.i("tag3", eventDate);
                   */ //Toast t = Toast.makeText(self, eventName, Toast.LENGTH_LONG);
                    //t.show();

                    e.setName(etEventName.getText().toString());
                    e.setDescription(etEventDesc.getText().toString());
                    e.setStart(DateTime.now());
                    e.setEnd(DateTime.now().plusHours(4));
                    ss.storeEvent(e, new ServerCallback<UUID>() {
                        @Override
                        public void handleResult(UUID result) {
                            Log.i("Create Event", "@@@@ stored event");

                        }
                    });

                    Intent intent = new Intent(getApplicationContext(), InviteUser.class);
                    Bundle b = new Bundle();

                    b.putString(BundleKeys.EVENTID_KEY, e.getEventID().toString());
                    intent.putExtras(b);

                    startActivity(intent);
                }

            }



                });



    }

    private boolean checkEditText() {

//        if(etDate.getText().toString().trim().length()==0) {
//            return false;
//        }
//        if(etEndtime.getText().toString().trim().length()==0)
//        {
//            return false;
//        }
//        if(etEventDesc.getText().toString().trim().length()==0)
//        {
//            return  false;
//        }
//        if(etEventName.getText().toString().trim().length()==0)
//        {
//            return false;
//        }
//        if(etStartTime.getText().toString().trim().length()==0)
//        {
//            return false;
//        }
        return true;
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
