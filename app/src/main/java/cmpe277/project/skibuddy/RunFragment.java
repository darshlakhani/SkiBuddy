package cmpe277.project.skibuddy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cmpe277.project.skibuddy.common.Run;
import java.util.List;
import java.util.UUID;
import android.support.v4.app.ListFragment;
import android.widget.AdapterView;
import android.widget.Toast;

import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends ListFragment {


    public RunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final Server s = new ServerSingleton().getServerInstance(getActivity());
        UUID eventID = UUID.randomUUID();
        s.getRuns(eventID, new ServerCallback<List<Run>>() {

            @Override
            public void handleResult(List<Run> result) {

                if (result == null || result.size() < 1) {
                    return;
                }

                setListAdapter(new RunAdapter(getActivity(), result));
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
