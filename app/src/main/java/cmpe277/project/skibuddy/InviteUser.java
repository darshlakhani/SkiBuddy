package cmpe277.project.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by rnagpal on 11/30/15.
 */
public class InviteUser extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);

        Intent i = getIntent();
        String myemail = i.getStringExtra("email");
    }

}
