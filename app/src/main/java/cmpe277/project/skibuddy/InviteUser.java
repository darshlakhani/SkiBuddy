package cmpe277.project.skibuddy;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

/**
 * Created by Robin on 12/1/2015.
 */
public class InviteUser extends ListActivity {
    private EditText search;
    private ListView user;
    private UUID eventID;

    private Handler handler;
    //private List<UserListAdapter> invUsers;
    UserListAdapter invUsers;
    Context self = null;
    Server ss = null;


    @Override
    protected void onCreate(Bundle savedInstance) {


        super.onCreate(savedInstance);
        setContentView(R.layout.invite_user);

        Intent i = getIntent();

        Bundle bundle = i.getExtras();
        eventID = UUID.fromString(bundle.getString(BundleKeys.EVENTID_KEY));

        ss = new ServerSingleton().getServerInstance(self);

        Log.i("Invite User", "@@@@ action");
        self = getApplicationContext();
        //invUsers = new UserListAdapter(self,R.layout.user_list,);
        search = (EditText) findViewById(R.id.etSearch);
        //user = (ListView) findViewById(R.android.list);

        handler = new Handler();

        Log.i("Invite User", "@@@@ action 1");
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("Invite User", "@@@@ editaction 3");
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Toast.makeText(getApplicationContext(), "@@@ abc ", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //dataAdapter.getFilter().filter(s.toString());
                Log.i("Invite User", "@@@@ editaction 3");
                Toast.makeText(getApplicationContext(), "@@@ abc2 ", Toast.LENGTH_SHORT).show();
                searchUser(s.toString());
            }
        });
    }



    private void searchUser(final String q) {
        Log.i("Invite User", "@@@@ searchUser");

        ss.getUsersByName(q, new ServerCallback<List<User>>() {
            @Override
            public void handleResult(List<User> result) {
                Log.i("Invite User", q);
                Log.i("Invite User", "@@@@ handle result");




                ArrayAdapter<User> adapter = new UserListAdapter(getApplicationContext(),R.layout.user_list,result, eventID);

                setListAdapter(adapter);


            }
        });
    }




}


