package cmpe277.project.skibuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.util.Calendar;
import java.util.UUID;

//import cmpe277.project.skibuddy.server.PojoEvent;
import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class CreateEvent extends Activity {
    public final static String EXTRA_MESSAGE = "cmpe277.project.skibuddy";

    EditText etEventName,etEventDesc, etStartTime;
    static final int TIME_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID1 = 1;
    static final int DATA_DIALOG_ID = 2;




    Event e;
    Button bDate, bEndTime, bStartTime;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView,endTimeText, startTimeText;
    private int year, month, day;
    private int hour;
    private int minute;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button createEventButton = (Button) findViewById(R.id.btCreateEventButton);
        bEndTime = (Button) findViewById(R.id.bEventEndTime);
        bStartTime = (Button) findViewById(R.id.bEventStartTime);
        bDate = (Button) findViewById(R.id.bDate);
        endTimeText =  (TextView) findViewById(R.id.tvEventEndTime);
        startTimeText = (TextView) findViewById(R.id.tvEventStartTime);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        //end time of event timepicker
        bEndTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        //start time timepicker button
        bStartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID1);
            }
        });
         bDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showDialog(DATA_DIALOG_ID);
             }
         });
        final Context self = this;
        final Server ss = new ServerSingleton().getServerInstance(self);
        e = ServerSingleton.createEvent();
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                etEventName = (EditText) findViewById(R.id.etEventName);

                etEventDesc = (EditText) findViewById(R.id.etEventDescription);

                dateView = (TextView) findViewById(R.id.tvDateView);

                //bDate = (Button) findViewById(R.id.bDate);


                if(!checkEditText())
                {
                    Toast.makeText(getApplicationContext(),"Enter All Values", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.i("Create Event", "@@@@ in else");
                    String eventDate = dateView.getText().toString();


                    String startTime = startTimeText.getText().toString();
                    startTime = eventDate + " " + startTime;

                    String endTime = endTimeText.getText().toString();
                    endTime = eventDate+" "+endTime;


                    DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
                DateTime sTime = formatter.parseDateTime(startTime);

                DateTime eTime = formatter.parseDateTime(endTime);






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

                    UUID eid = e.getEventID();
                    Intent intent = new Intent(getApplicationContext(), EventManagement.class);

                    Bundle b = new Bundle();

                    b.putString(BundleKeys.EVENTID_KEY, e.getEventID().toString());
                    intent.putExtras(b);

                    startActivity(intent);
                }

            }



                });



    }


    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "date", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 2) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if(id == 0)
        {
            return new TimePickerDialog(this, timePickerListener, hour, minute,false);

        }


        if(id == 1)
        {
            return new TimePickerDialog(this, timePickerListener1, hour, minute,false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =  new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

            hour = selectedHour;

            minute = selectedMinute;



            // set current time into textview

            endTimeText.setText(new StringBuilder().append(padding_str(hour)).append(":").append(padding_str(minute)));



            // set current time into timepicker

            /*timePicker.setCurrentHour(hour);

            timePicker.setCurrentMinute(minute);*/



        }

    };
    private TimePickerDialog.OnTimeSetListener timePickerListener1 =  new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

            hour = selectedHour;

            minute = selectedMinute;



            // set current time into textview

            startTimeText.setText(new StringBuilder().append(padding_str(hour)).append(":").append(padding_str(minute)));



            // set current time into timepicker

            /*timePicker.setCurrentHour(hour);

            timePicker.setCurrentMinute(minute);*/



        }

    };

    private static String padding_str(int c) {

        if (c >= 10)

            return String.valueOf(c);

        else

            return "0" + String.valueOf(c);

    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView = (TextView) findViewById(R.id.tvDateView);
        dateView.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }


    private boolean checkEditText() {


        if(dateView.getText().toString().equals("Date Selected")) {
            return false;
        }

        if(etEventDesc.getText().toString().trim().length()==0)
        {
            return  false;
        }
        if(etEventName.getText().toString().trim().length()==0)
        {
            return false;
        }
        if(startTimeText.getText().toString().equals("Start Time"))
        {
            return false;
        }
        if(endTimeText.getText().toString().equals("End Time"))
        {
            return false;
        }
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
