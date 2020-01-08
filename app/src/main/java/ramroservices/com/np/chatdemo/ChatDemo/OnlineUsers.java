package ramroservices.com.np.chatdemo.ChatDemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import ramroservices.com.np.chatdemo.Adapter.UserAdapter;
import ramroservices.com.np.chatdemo.Model.Isseen;
import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.Notification.Token;
import ramroservices.com.np.chatdemo.R;

public class OnlineUsers extends AppCompatActivity {
    RecyclerView rvuserlist;
    UserAdapter userAdapter;
    List<Userlist> userlistList;
    FirebaseUser firebaseUser;
    List<Isseen> isseens;
    Context context;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_users);

        rvuserlist = findViewById(R.id.rvusers);
        userlistList = new ArrayList<>();
        isseens = new ArrayList<>();
        rvuserlist.setHasFixedSize(true);
        rvuserlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        context = this;
        readusers();
        readisseen();
        updatetoken(FirebaseInstanceId.getInstance().getToken());
// handler = new Handler();
// runnable =new Runnable() {
//    @Override
//    public void run() {
//         rf();
//         handler.postDelayed(runnable,10000);
//    }
//};
// handler.post(runnable);


        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);



    }


    public void readisseen(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("isseen").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isseens.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Isseen isseen = snapshot.getValue(Isseen.class);
                    assert isseen!=null;
                    isseens.add(isseen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readusers(){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("members");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlistList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Userlist userlist = snapshot.getValue(Userlist.class);

                    assert userlist!=null;
                    assert  firebaseUser!=null;

                    if (!userlist.getId().equals(firebaseUser.getUid())){
                        userlistList.add(userlist);
                    }
                }
                // userAdapter = new UserAdapter(getContext(),userlistList,true);
                userAdapter = new UserAdapter(context,userlistList,true,isseens);
                rvuserlist.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void rf(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    //for notification after msg sent
    private void updatetoken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);


    }
}
