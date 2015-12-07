package cmpe277.project.skibuddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

/**
 * Created by Robin on 12/1/2015.
 */
public class UserListAdapter extends ArrayAdapter<User> {
    private final Context context;
    //private final String[] values;
    List<User> users;
    String[ ] names;
    UUID eventID;

    public UserListAdapter(Context context,int resource,  List<User> result, UUID eventID) {
        super(context, resource,result);
        this.context = context;
        this.users =  result;
        this.eventID = eventID;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Log.i("Adapter", "@@@@ getView");

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        if(rowView == null)
        {
            LayoutInflater vi ;
            vi = LayoutInflater.from(getContext());
            rowView= vi.inflate(R.layout.user_list, parent, false);
        }

       //  rowView = inflater.inflate(R.layout.user_list, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.tvUserName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ivProfile);
        Button ib = (Button) rowView.findViewById(R.id.ibTick);
        User show = users.get(position);
        ib.setTag(show);

        textView.setText(show.getName());
        imageView.setImageResource(R.drawable.invite);
        final Server s = new ServerSingleton().getServerInstance(getContext());

        ib.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    ((Button) v).setEnabled(false);
                    ((Button) v).setText("Invited");
                    v.setSelected(true);

                    final User toInvite = (User)v.getTag();
                    s.getEvent(eventID, new ServerCallback<Event>() {
                        @Override
                        public void handleResult(Event result) {
                            s.inviteUser(toInvite, result);
                            Toast t = Toast.makeText(getContext(), "Invited user", Toast.LENGTH_LONG);
                            t.show();
                        }
                    });
                }
            }
        });
        return rowView;
    }
}
