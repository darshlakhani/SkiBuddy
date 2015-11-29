package cmpe277.project.skibuddy.server;

/**
 * Created by eh on 11/28/2015.
 */
public abstract class ServerCallback<T> implements Runnable {
    private T result;

    public ServerCallback(){
    }

    void postResult(T result){
        this.result = result;
    }

    @Override
    public void run() {
        handleResult(result);
    }

    public abstract void handleResult(T result);
}
