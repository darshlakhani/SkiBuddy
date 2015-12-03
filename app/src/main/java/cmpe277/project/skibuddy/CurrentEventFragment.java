package cmpe277.project.skibuddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.ListFragment;
import android.widget.AdapterView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;


public class CurrentEventFragment extends ListFragment {

    static final String[] CURRENT_EVENT_LIST = new String[100];
    static final EventRelation[] CURRENT_EVENT_LIST_FINAL = new EventRelation[100];
    static final ArrayList<EventRelation> erList = new ArrayList<>();


    public CurrentEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_event, container, false);
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
                Log.d("Event name", result.get(0).getName().toString());
                int index = 0;

                for (int i = 0; i < result.size(); i++) {

                    //Get DateTime from Event Object
                    DateTime dateValue = result.get(i).getEnd();

                    //Compare datetime from event with current datetime value
                    int difference = DateTimeComparator.getInstance().compare(currentValue, dateValue);
                    Log.d("Difference Value",String.valueOf(difference));

                    //If Current date is greater, event should be in past and should be added to list
                    if(difference == -1 || difference == 0) {
                        CURRENT_EVENT_LIST_FINAL[index] = result.get(i);
                        CURRENT_EVENT_LIST[index] = result.get(i).getName();
                        erList.add(result.get(i));
                        index++;
                    }
                }
                setListAdapter(new EventListAdapter(getActivity(), erList));

            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT)
                        .show();
                Intent i = new Intent(getActivity(), EventManagement.class);
                String eventName = CURRENT_EVENT_LIST[position];
                EventRelation erObj = erList.get(position);
                HashMap<String, String> mp = new HashMap();
                mp.put("name", erObj.getName());
                mp.put("desc", erObj.getDescription());
                mp.put("startDate", erObj.getStart().toString());
                mp.put("endDate", erObj.getEnd().toString());
                Log.d("end date", erObj.getEnd().toString());

                i.putExtra("eventMap", mp);
                startActivity(i);
            }
        });
    }



}
