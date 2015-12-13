package cmpe277.project.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private final Server s = new ServerSingleton().getServerInstance(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        imageView = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.name);
        tagline = (TextView) findViewById(R.id.tagline);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String userUUID = bundle.getString(BundleKeys.UUID_KEY);

            if (userUUID == null) {
                Log.e(UserProfileActivity.class.getName(),
                        "UserProfileActivity launched with null User UUID");
                Toast t = Toast.makeText(getBaseContext(), "An error occurred", Toast.LENGTH_LONG);
                t.show();
                return;
            }

            s.getUser(UUID.fromString(userUUID), new ServerCallback<User>() {
                @Override
                public void handleResult(User result) {
                    if (result == null) {
                        Toast t = Toast.makeText(getBaseContext(), "Couldn't find user", Toast.LENGTH_LONG);
                        t.show();
                    } else {
                        setUserProfile(result);
                    }
                }
            });

        } else {
            try {
                User user = s.getAuthenticatedUser();
                setUserProfile(user);
            } catch (NotAuthenticatedException e) {
                // User not authenticated, return to sign in screen
                Intent i = new Intent(UserProfileActivity.this, SignInActivity.class);
                startActivity(i);
            }
        }

    }

    public void setUserProfile (User user) {
        if(user == null) {
            // This should never happen
            return;
        }

        imageView.setImageBitmap(null);

        //Tagline
        String taglineStr = user.getTagline();

        if (taglineStr != null && !taglineStr.isEmpty()) {
            tagline.setText(getString(R.string.tagline, taglineStr));
        } else {
            tagline.setText(getString(R.string.tagline_na));
        }

        //Load Profile picture
        LoadProfilePicture loadProfilePicture = new LoadProfilePicture(imageView);
        loadProfilePicture.loadPicture(user);

    }
}