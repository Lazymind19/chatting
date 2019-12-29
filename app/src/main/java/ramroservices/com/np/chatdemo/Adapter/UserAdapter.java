package ramroservices.com.np.chatdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ramroservices.com.np.chatdemo.ChatDemo.MessageActivity;
import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<Userlist> userlists;
    private boolean ischat;

    public UserAdapter(Context context, List<Userlist> userlists, boolean ischat) {
        this.context = context;
        this.userlists = userlists;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_userlist,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder viewHolder, int i) {
        final Userlist userlist = userlists.get(i);
        viewHolder.tvusername.setText(userlist.getUsername());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",userlist.getId());
                context.startActivity(intent);
            }
        });

        if (ischat){
            if (userlist.getStatus().equals("online")){
                viewHolder.btnon.setVisibility(View.VISIBLE);
                viewHolder.btnoff.setVisibility(View.GONE);
            }
            else {
                viewHolder.btnoff.setVisibility(View.VISIBLE);
                viewHolder.btnon.setVisibility(View.GONE);
            }
        }

        else {
            viewHolder.btnon.setVisibility(View.GONE);
            viewHolder.btnoff.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return userlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvusername;
        Button btnon,btnoff;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvusername = itemView.findViewById(R.id.tvusername);
            btnon = itemView.findViewById(R.id.btnonline);
            btnoff = itemView.findViewById(R.id.btnoffline);

        }
    }
}
