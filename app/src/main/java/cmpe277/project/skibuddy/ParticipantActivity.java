package cmpe277.project.skibuddy;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


/**
 * Created by rnagpal on 12/1/15.
 */



public class ParticipantActivity extends ListActivity {

    static final String[] PARTICIPANT_LIST =
            new String[] { "Bon Jovi", "Toothiya", "Weideryu", "Dulheraja"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ParticipantAdapter(this, PARTICIPANT_LIST));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

    }
}
