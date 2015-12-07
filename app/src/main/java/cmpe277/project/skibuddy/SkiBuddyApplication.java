package cmpe277.project.skibuddy;

import android.content.Context;

import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerSingleton;

/**
 * Created by Robin on 12/2/2015.
 */
public class SkiBuddyApplication extends android.app.Application {

        @Override
        public void onCreate()
        {
            super.onCreate();
            final Context self = this;
            final Server ss = new ServerSingleton().getServerInstance(self);
        }
}
