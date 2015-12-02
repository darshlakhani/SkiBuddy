package cmpe277.project.skibuddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;
import java.util.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartcipantFragment extends ListFragment {

    //static final String[] PARTICIPANT_LIST =
            //new String[] { "Bon Jovi", "Toothiya", "Weideryu", "Dulheraja"};

    static final String[] PARTICIPANT_LIST = new String[100];


    public PartcipantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partcipant, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final Server s = new ServerSingleton().getServerInstance(getActivity());
        UUID eventID = UUID.randomUUID();
        s.getEventParticipants(eventID, new ServerCallback<List<EventParticipant>>() {
            int index = 0;
            @Override
            public void handleResult(List<EventParticipant> result) {
                Log.d("Participant name", result.get(0).getName());
                for (int i = 0; i < result.size(); i++) {
                    PARTICIPANT_LIST[index] = result.get(i).getName();
                    index++;
                }
                setListAdapter(new ParticipantAdapter(getActivity(), PARTICIPANT_LIST));
            }
        });

        /*setListAdapter(new ParticipantAdapter(getActivity(), PARTICIPANT_LIST));

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });*/


    }


}
