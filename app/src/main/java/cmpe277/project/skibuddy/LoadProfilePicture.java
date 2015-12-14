package cmpe277.project.skibuddy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Set;

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
        if (uriStr != null && !uriStr.isEmpty()) {
            String uriWithSize = UriWithSize(uriStr, imageView.getWidth());
            if(!uriWithSize.isEmpty())
                new LoadProfileImage(imageView).execute(uriWithSize);
        }
        else imageView.setImageResource(R.drawable.profilepic);
    }

    private static String UriWithSize(String URI, int size){
        Uri uri = Uri.parse(URI);
        final String SIZE_QUERY_PARAM = "sz";
        final Set<String> params = uri.getQueryParameterNames();
        final Uri.Builder newUri = uri.buildUpon().clearQuery();

        if(params.contains(SIZE_QUERY_PARAM)) {
            for (String param : params) {
                String value;
                if (param.equals(SIZE_QUERY_PARAM)) {
                    value = String.valueOf(size);
                } else {
                    value = uri.getQueryParameter(param);
                }

                newUri.appendQueryParameter(param, value);
            }
        } else {
            newUri.appendQueryParameter(SIZE_QUERY_PARAM, String.valueOf(size));
        }

        return newUri.toString();
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
