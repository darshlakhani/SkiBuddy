package cmpe277.project.skibuddy;


import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Robin on 12/1/2015.
 */
public class InviteUser extends ListActivity {
    private EditText search;
    private ListView user;

    private Handler handler;
    private List<UserList> invUsers;

    @Override
    protected  void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
       setContentView(R.layout.invite_user);

        search = (EditText) findViewById(R.id.etSearch);
        user = (ListView) findViewById(R.id.lvUserList);

        handler = new Handler();

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    searchUser(v.getText().toString());
                    return false;
                }
                return true;
            }
        });

    }
        private void searchUser(final String q)
        {
            new Thread(){
                public void run(){
                    invUsers = invitedusers(q);
                }
            }.start();
        }

    private List<UserList> invitedusers(String key)
    {


        return null;
    }

}
