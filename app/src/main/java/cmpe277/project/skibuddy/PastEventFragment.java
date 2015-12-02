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
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.MockServer;



/**
 * A simple {@link Fragment} subclass.
 */
public class PastEventFragment extends ListFragment {

    static final String[] PAST_EVENT_LIST = new String[100];

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

        s.getEvents(new ServerCallback<List<Event>>() {
            int index = 0;
            @Override
            public void handleResult(List<Event> result) {
                Log.d("Event name", result.get(0).getName().toString());
                for (int i = 0; i < result.size(); i++) {
                    PAST_EVENT_LIST[index] = result.get(i).getName();
                    index++;
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
                startActivity(i);
            }
        });
    }


}











