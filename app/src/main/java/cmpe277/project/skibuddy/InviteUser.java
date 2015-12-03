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
        Log.i("Invite User", "@@@@ action");
        self = getApplicationContext();
        //invUsers = new UserListAdapter(self,R.layout.user_list,);
        ss = new ServerSingleton().getServerInstance(self);
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

               /* while(it.hasNext())
                {
                    String u = it.next().getName();
                    Log.i("Invite User", "@@@@"+u);
                }
                Log.i("Invite User", "@@@@ before update");
*/
                /*UserListAdapter custom = new UserListAdapter(self, R.layout.user_list, result);
                user.setAdapter(custom);*/

                User resUsers[] = new User[result.size()];
                result.toArray(resUsers);

                ArrayAdapter<User> adapter = new UserListAdapter(getApplicationContext(),R.layout.user_list,result);

                setListAdapter(adapter);


            }
        });
    }



    private void updateUsers(List<User> result) {

        Log.i("Invite User", "@@@@ Inside updateUser");
        /*ArrayAdapter<UserListAdapter> adapter = new ArrayAdapter<UserListAdapter>(getApplicationContext(), R.layout.user_list, invUsers) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.i("Invite User", "@@@@ Inside getView");
                if (convertView == null) {
                    Log.i("Invite User", "@@@@ No view");
                    convertView = getLayoutInflater().inflate(R.layout.user_list, parent, false);
                }

                Log.i("Invite User", "@@@@ view");
                TextView name = (TextView) convertView.findViewById(R.id.tvUserName);
                name.setText("Raj");
                return convertView;
            }
        };*/
    }

}


