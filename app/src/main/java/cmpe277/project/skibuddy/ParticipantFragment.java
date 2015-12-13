package cmpe277.project.skibuddy;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;
import java.util.*;

/*
/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantFragment extends ListFragment {

    private UUID eventID;

    public ParticipantFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.eventID = UUID.fromString(getArguments().getString(BundleKeys.EVENTID_KEY));
        return inflater.inflate(R.layout.fragment_partcipant, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final Server s = new ServerSingleton().getServerInstance(getActivity());
        s.getEventParticipants(eventID, new ServerCallback<List<EventParticipant>>() {

            @Override
            public void handleResult(List<EventParticipant> result) {

                if(result == null || result.size() < 1)
                {
                    return;
                }

                setListAdapter(new ParticipantAdapter(getActivity(), result));
            }
        });



        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }


}