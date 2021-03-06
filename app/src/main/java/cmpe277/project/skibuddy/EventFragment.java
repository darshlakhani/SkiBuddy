package cmpe277.project.skibuddy;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.ListFragment;
import android.widget.AdapterView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.common.ParticipationStatus;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;


public class EventFragment extends ListFragment {

    private final String[] CURRENT_EVENT_LIST = new String[100];
    private final EventRelation[] CURRENT_EVENT_LIST_FINAL = new EventRelation[100];
    private final ArrayList<EventRelation> erList = new ArrayList<>();

    // If true show past, if false show current events
    private boolean showPastEvents = false;

    public EventFragment() {
        // Required empty public constructor
    }

    public void setIsPastEventFragment(boolean value){
        showPastEvents = value;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        erList.clear();
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final Server s = new ServerSingleton().getServerInstance(getActivity());

        s.getEvents(new ServerCallback<List<EventRelation>>() {

            @Override
            public void handleResult(List<EventRelation> result) {

                if (result == null){
                    // we didn't get a result, something went wrong, or whatever
                    return;
                }
                //Get Current Date Time
                DateTime currentValue = new DateTime();
                int index = 0;

                for (int i = 0; i < result.size(); i++) {

                    //Get DateTime from Event Object
                    DateTime dateValue = result.get(i).getEnd();

                    //Compare datetime from event with current datetime value
                    int difference = DateTimeComparator.getInstance().compare(currentValue, dateValue);
                    Log.d("Difference Value",String.valueOf(difference));

                    //If Current date is greater, event should be in past and should be added to list
                    if((difference > 0 && showPastEvents) || (difference <= 0 && !showPastEvents)){
                        CURRENT_EVENT_LIST_FINAL[index] = result.get(i);
                        CURRENT_EVENT_LIST[index] = result.get(i).getName();
                        if( ! erList.contains(result.get(i))) {
                            erList.add(result.get(i));
                            index++;
                        }
                    }
                }
                setListAdapter(new EventListAdapter(getActivity(), erList));

            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final EventRelation erObj = erList.get(position);

                Object status = erObj.getParticipationStatus();

                if (status == ParticipationStatus.INVITEE) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());

                    // set title
                    alertDialogBuilder.setTitle(erObj.getName());
                    alertDialogBuilder
                            .setMessage("Do you want to accept the invitation?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    Log.d("Reply", "Accept Invite");
                                    launchEventManager(erObj);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.d("Reply", "reject Invite");
                                    s.rejectInvitation(erObj);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                } else {

                    launchEventManager(erObj);
                }
            }
        });
    }

    private void launchEventManager(Event event) {
        Intent i = new Intent(getActivity(), EventManagement.class);
        Bundle b = new Bundle();
        b.putString(BundleKeys.EVENTID_KEY, event.getEventID().toString());
        i.putExtras(b);
        startActivity(i);
    }


}
