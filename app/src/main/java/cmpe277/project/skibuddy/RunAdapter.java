package cmpe277.project.skibuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

/**
 * Created by akankshanagpal on 12/2/15.
 */
public class RunAdapter extends BaseAdapter{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Run> values;

    private final Server s;

    public RunAdapter(Context context, List<Run> values) {
        inflater = LayoutInflater.from(context);
        s = new ServerSingleton().getServerInstance(context);
        this.context = context;
        this.values = values;
    }

    private class ViewHolder {
        public Run run;
        public TextView label, tvStatus;
        public ImageView logo;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder holder;

        if(convertView == null){
            view = inflater.inflate(R.layout.list_participant, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) view.findViewById(R.id.label);
            holder.logo = (ImageView) view.findViewById(R.id.logo);
            holder.tvStatus = (TextView)view.findViewById(R.id.tvStatus);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Run run = values.get(position);
        holder.run = run;

        // TODO: get objects from the user objects

        holder.tvStatus.setText(String.format("%,.0f m", run.getDistance()));
        s.getUser(run.getUserId(), new ServerCallback<User>() {
            @Override
            public void handleResult(User result) {
                holder.label.setText(result.getName());
            }
        });

        holder.logo.setImageResource(R.drawable.map);

        return view;
    }
}
