package ramroservices.com.np.chatdemo.ChatDemo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.Notification.Token;
import ramroservices.com.np.chatdemo.R;


public class UserFragment extends Fragment {
    RecyclerView rvuserlist;
    UserAdapter userAdapter;
    List<Userlist> userlistList;
    FirebaseUser firebaseUser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        rvuserlist = view.findViewById(R.id.rvusers);
        userlistList = new ArrayList<>();
        rvuserlist.setHasFixedSize(true);
        rvuserlist.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();

        readusers();
        updatetoken(FirebaseInstanceId.getInstance().getToken());



        return view;
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
                userAdapter = new UserAdapter(getContext(),userlistList,true);
                rvuserlist.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    //for notification after msg sent
    private void updatetoken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);


    }



}
