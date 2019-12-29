package ramroservices.com.np.chatdemo.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.R;

public class UserlistAdapter extends ArrayAdapter<Userlist> {
    private Activity context;
    private List<Userlist> userlists;

    public UserlistAdapter( Activity context, List<Userlist> userlists) {
        super(context, R.layout.listview_userlist,userlists);
        this.context = context;
        this.userlists = userlists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater =context.getLayoutInflater();

        View listitemview = layoutInflater.inflate(R.layout.listview_userlist,null,true);
        TextView textView = listitemview.findViewById(R.id.tvusername);
        Userlist userlist = userlists.get(position);
        textView.setText(userlist.getUsername());
        Log.d("TAG", "getView: "+userlist.getUsername());

        // code to check list click event


        return listitemview;

    }
}
