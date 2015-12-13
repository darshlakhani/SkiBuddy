package cmpe277.project.skibuddy.server;

import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.User;

/**
 * Created by eh on 12/11/2015.
 */
public interface AuthenticatedUserProvider {

    /**
     * Returns the currently logged in user.
     * @throws cmpe277.project.skibuddy.common.NotAuthenticatedException If no user currently logged in
     */
    User getAuthenticatedUser() throws NotAuthenticatedException;
}
