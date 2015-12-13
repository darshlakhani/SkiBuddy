package cmpe277.project.skibuddy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.UUID;

import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class UserProfileActivity extends Activity {
    TextView name;
    TextView tagline;
    ImageView imageView;

    ArrayList<String> arrStr = new ArrayList<String>();
    User user = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final Server s = new ServerSingleton().getServerInstance(this);
            String userUUID = bundle.getString(BundleKeys.UUID_KEY);

            s.getUser(UUID.fromString(userUUID), new ServerCallback<User>() {
                @Override
                public void handleResult(User result) {
                    if (result == null) {
                        try {
                            user = s.getAuthenticatedUser();
                            arrStr.add(user.getName());                 //index 0
                            arrStr.add(user.getTagline());              //index 1
                            arrStr.add(user.getProfilePictureURL());    //index 2
                        } catch (NotAuthenticatedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            imageView = (ImageView) findViewById(R.id.imageView);
            name = (TextView) findViewById(R.id.name);
            tagline = (TextView) findViewById(R.id.tagline);

            if (arrStr != null && arrStr.size() != 0) {
                setUserProfile();
            }
        }//if
    }

    public void setUserProfile () {
        imageView.setImageBitmap(null);
        //User name
        name.setText(getString(R.string.signed_in_fmt, arrStr.get(0)));

        //Tagline
        String taglineStr = arrStr.get(1);

        if (taglineStr != null) {
            tagline.setText(getString(R.string.tagline, taglineStr));
        } else {
            tagline.setText(getString(R.string.tagline_na));
        }

        //Load Profile picture
        LoadProfilePicture loadProfilePicture = new LoadProfilePicture(imageView);
        loadProfilePicture.loadPicture(user);

    }
}