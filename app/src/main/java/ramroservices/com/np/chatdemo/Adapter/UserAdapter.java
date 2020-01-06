package ramroservices.com.np.chatdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ramroservices.com.np.chatdemo.ChatDemo.MessageActivity;
import ramroservices.com.np.chatdemo.Model.Isseen;
import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<Userlist> userlists;
    private boolean ischat;
    List<Isseen> isseens;

    public UserAdapter(Context context, List<Userlist> userlists, boolean ischat, List<Isseen> isseens) {
        this.context = context;
        this.userlists = userlists;
        this.ischat = ischat;
        this.isseens = isseens;
    }

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
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("isseen").child(firebaseUser.getUid());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Isseen isseen = snapshot.getValue(Isseen.class);
                            if (isseen!=null){

                                for(int x=0;x<isseens.size();x++){
                                    Isseen isseen1 =isseens.get(x);
                                    if (isseen1.getId().equals(userlist.getId())){
                                       // viewHolder.btnalert.setVisibility(View.VISIBLE);
                                        databaseReference.removeValue();

                                        break;
                                    }
                                    else {
                                       // viewHolder.btnalert.setVisibility(View.GONE);
                                    }

                                }


                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",userlist.getId());
                context.startActivity(intent);
            }
        });

        if(isseens!=null) {

            int a=isseens.size();
            for(int x=0;x<isseens.size();x++){
                Isseen isseen =isseens.get(x);
                if (isseen.getId().equals(userlist.getId())){
                    viewHolder.btnalert.setVisibility(View.VISIBLE);
                    break;
                }
                else {
                    viewHolder.btnalert.setVisibility(View.GONE);
                }

            }

//            boolean alert = isseens.contains(userlist.getId());
//            if (alert) {
//                viewHolder.btnalert.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.btnalert.setVisibility(View.GONE);
//            }
        }


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
        Button btnon,btnoff,btnalert;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvusername = itemView.findViewById(R.id.tvusername);
            btnon = itemView.findViewById(R.id.btnonline);
            btnoff = itemView.findViewById(R.id.btnoffline);
            btnalert =itemView.findViewById(R.id.btnreadalert);

        }
    }
}
