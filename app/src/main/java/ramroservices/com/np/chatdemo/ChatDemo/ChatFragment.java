package ramroservices.com.np.chatdemo.ChatDemo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import ramroservices.com.np.chatdemo.Adapter.UserAdapter;
import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.R;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    List<Userlist> userlists;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

//recyclerView = view.findViewById(R.id.rvchats);
//recyclerView.setHasFixedSize(true);
//recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//userlists = new ArrayList<>();
//firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//databaseReference = FirebaseDatabase.getInstance().getReference("messages")
//databaseReference.addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        userlists.clear();
//        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//            Chat chat = snapshot.getValue(Chat.class);
//            if (chat.getSender().equals(firebaseUser.getUid())){
//                Userlist userlist = new Userlist();
//                userlist.setUsername(chat.getReceiver());
//                userlists.add(userlist);
//            }
//            if (chat.getSender().equals(firebaseUser.getUid())){
//                Userlist userlist = new Userlist();
//
//            }
//        }
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//    }
//})

        return view;
    }


}
