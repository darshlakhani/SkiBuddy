package cmpe277.project.skibuddy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.ListFragment;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.content.*;

import java.util.*;
import android.util.Log;
import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;
import cmpe277.project.skibuddy.server.Server;




/**
 * A simple {@link Fragment} subclass.
 */
public class PastEventFragment extends ListFragment {

    static final String[] PAST_EVENT_LIST = new String[100];
    static final ArrayList<EventRelation> erList = new ArrayList<>();



    public PastEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_past_event, container, false);
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
                Log.d("Event name", result.get(0).getName().toString());
                for (int i = 0; i < result.size(); i++) {
                    PAST_EVENT_LIST[i] = result.get(i).getName();
                    erList.add(result.get(i));
                }
                setListAdapter(new ParticipantAdapter(getActivity(), PAST_EVENT_LIST));

            }
        });

        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT)
                        .show();
                Intent i = new Intent(getActivity(), EventManagement.class);
                String eventName = PAST_EVENT_LIST[position];
                EventRelation erObj = erList.get(position);
                HashMap<String,String> mp = new HashMap();
                mp.put("name",erObj.getName());
                mp.put("desc",erObj.getDescription());
                mp.put("startDate",erObj.getStart().toString());
                mp.put("endDate",erObj.getEnd().toString());
                Log.d("end date",erObj.getEnd().toString() );

                i.putExtra("eventMap",mp);
                startActivity(i);
            }
        });
    }


}











