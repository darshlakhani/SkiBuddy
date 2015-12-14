package cmpe277.project.skibuddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cmpe277.project.skibuddy.common.Run;

import java.util.ArrayList;
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


    private UUID eventID;
    private List<Run> runList = new ArrayList();
    final Server s = new ServerSingleton().getServerInstance(getActivity());


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

        // Get bundle, read either eventID or userID, depending on which one is populated,
        // get the user's runs or the event's runs.

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               UUID runID = runList.get(position).getRunId();
                Intent i = new Intent(getActivity(),RunActivity.class);
                Bundle b = new Bundle();
                b.putString(BundleKeys.RUNID_KEY, runID.toString());
                i.putExtras(b);
                startActivity(i);
            }
        });

    }

    private void loadRuns() {
        Bundle bundle = getArguments();
        if(bundle.getString(BundleKeys.EVENTID_KEY) != null){
            // get event runs
            UUID eventID = UUID.fromString(bundle.getString(BundleKeys.EVENTID_KEY));
            s.getRuns(eventID, new RunServerCallback());

        } else if (bundle.getString(BundleKeys.UUID_KEY) != null) {
            UUID userId = UUID.fromString(bundle.getString(BundleKeys.UUID_KEY));
            s.getUserRuns(userId, new RunServerCallback());
        } else {
            Log.w(RunFragment.class.getName(), "RunFragment needs either an eventID or a userID");
        }
    }

    class RunServerCallback extends ServerCallback<List<Run>>{

        @Override
        public void handleResult(List<Run> result) {

            if (result == null || result.size() < 1) {
                return;
            }

            setListAdapter(new RunAdapter(getActivity(), result));
            runList = result;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loadRuns();
    }
}
