package ramroservices.com.np.chatdemo.ChatDemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ramroservices.com.np.chatdemo.Adapter.UserlistAdapter;
import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.R;

public class SendingActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    ArrayList<String> usernamelist = new ArrayList<>();

    ArrayAdapter arrayAdapter;
    List<Userlist> userlists;

    FirebaseUser iuser;
    FirebaseAuth firebaseAuth;

    ListView userlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("members");
        userlist = findViewById(R.id.messagelist);
        userlists = new ArrayList<>();
        onStart();

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                userlists.clear();
                for(DataSnapshot usersnapshot :dataSnapshot.getChildren()){
                    Userlist userlist = usersnapshot.getValue(Userlist.class);
                    userlists.add(userlist);

                }

             //   iuser = firebaseAuth.getCurrentUser();

                UserlistAdapter adapter  = new UserlistAdapter(SendingActivity.this,userlists);
                //for check item click in list view
//                userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String username = (String) parent.getItemAtPosition(position);
//                      //  String currentusername = iuser.toString();
//                        Toast.makeText(SendingActivity.this, "I clicked"+username, Toast.LENGTH_SHORT).show();
//                      //  Toast.makeText(SendingActivity.this, "I am "+currentusername, Toast.LENGTH_SHORT).show();
//                    }
//                });
                userlist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





//        ValueEventListener valueEventListener  = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//               /* try {
//                    JSONObject jsonObject = new JSONObject(String.valueOf(dataSnapshot));
//                    Log.d("TAG", "onDataChange: ");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }*/
//
//
///*GenericTypeIndicator<Map<String,List<Userlist>>> genericTypeIndicator = new GenericTypeIndicator<Map<String, List<Userlist>>>() {
//};
//HashMap<String,List<Userlist>> hashmap = (HashMap<String, List<Userlist>>) dataSnapshot.getValue(genericTypeIndicator);
//for (Map.Entry<String,List<Userlist>> entry :hashmap.entrySet()){
//    List<Userlist> userlists = entry.getValue();
//    for (Userlist userlist :userlists){
//        Log.i("TAG", "onDataChange: "+userlist.user);
//    }
//}*/
//               /* usernamelist = new ArrayList<>((ArrayList) dataSnapshot.getValue());
//               arrayAdapter = new ArrayAdapter(SendingActivity.this,android.R.layout.simple_list_item_1,usernamelist);
//               userlist.setAdapter(arrayAdapter);
//*/
//
//                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    Userlist post = postSnapshot.getValue(Userlist.class);
//                    Log.e("Get Data", post.getUser());
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(SendingActivity.this, "failed to load"+databaseError.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        };
//        databaseReference.addValueEventListener(valueEventListener);

    }
}
