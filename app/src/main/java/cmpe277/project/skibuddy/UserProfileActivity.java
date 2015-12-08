package cmpe277.project.skibuddy;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;

public class UserProfileActivity extends Activity {
    TextView name;
    TextView tagline;
    ImageView imageView;

    ArrayList<String> arrStr = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        arrStr = getIntent().getStringArrayListExtra("UserProfileActivity");

        imageView = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.name);
        tagline = (TextView) findViewById(R.id.tagline);

        if (arrStr != null && arrStr.size() != 0) {
            setUserProfile();
        }
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

        //Profile picture
        final int PROFILE_PIC_SIZE = 500;
        String uriStr = arrStr.get(2);
        if (uriStr != null || uriStr != "") {
            String shorter_url =
                    uriStr.substring(0,
                            uriStr.length() - 2)
                            + PROFILE_PIC_SIZE;
            new LoadProfileImage(imageView).execute(shorter_url);
        }
        else imageView.setImageBitmap(null);
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}