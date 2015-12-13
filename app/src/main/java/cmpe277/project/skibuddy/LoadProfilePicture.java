package cmpe277.project.skibuddy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import cmpe277.project.skibuddy.common.User;

/**
 * Created by nissaie on 12/8/15.
 */
public class LoadProfilePicture {
    ImageView imageView;

    public LoadProfilePicture(ImageView imageView) {
        this.imageView = imageView;
    }

    public void loadPicture (User user) {
        final int PROFILE_PIC_SIZE = 500;
        String uriStr = user.getProfilePictureURL();
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
