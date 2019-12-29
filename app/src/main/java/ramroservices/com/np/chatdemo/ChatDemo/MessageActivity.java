package ramroservices.com.np.chatdemo.ChatDemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ramroservices.com.np.chatdemo.Adapter.MessageAdapter;
import ramroservices.com.np.chatdemo.Model.Chat;
import ramroservices.com.np.chatdemo.Model.Userlist;
import ramroservices.com.np.chatdemo.Notification.Client;
import ramroservices.com.np.chatdemo.Notification.Data;
import ramroservices.com.np.chatdemo.Notification.MyResponse;
import ramroservices.com.np.chatdemo.Notification.Sender;
import ramroservices.com.np.chatdemo.Notification.Token;
import ramroservices.com.np.chatdemo.R;

public class MessageActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databaseReferenceimage;
    String titlename,myurl="";
    private Uri fileuri;
    TextView tvusername;
    EditText etmessage;
    ImageButton ibsendbtn,ibfilesend;
    Intent intent;
    MessageAdapter messageAdapter;
    List<Chat> chatList;
    RecyclerView rvmessagelist;
    String userid;
    boolean notify = false;
    StorageTask uploadtask;
    StorageReference storageReference, filepath;


    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar= findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        etmessage = findViewById(R.id.etmessage);
        ibsendbtn = findViewById(R.id.ibmessagesend);
        rvmessagelist = findViewById(R.id.rvmesssagelist);
        ibfilesend = findViewById(R.id.ibfilesend);
        chatList = new ArrayList<>();


        rvmessagelist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvmessagelist.setLayoutManager(linearLayoutManager);

        tvusername = findViewById(R.id.tvusername);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceimage = FirebaseDatabase.getInstance().getReference().child("Image Files");
        databaseReference = FirebaseDatabase.getInstance().getReference("members").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Userlist userlist = dataSnapshot.getValue(Userlist.class);
                tvusername.setText(userlist.getUsername().toUpperCase());

                readMessage(firebaseUser.getUid(),userid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ibfilesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,"Select image"),438);
            }
        });

        ibsendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = etmessage.getText().toString();
                if(!message.equals("")){
                    sendmessage(firebaseUser.getUid(),userid,message);

                }
                else {
                    Toast.makeText(MessageActivity.this, "Cannot sent empty message", Toast.LENGTH_SHORT).show();
                }
                etmessage.setText("");
            }
        });



        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


    }

    private void sendmessage(String sender, final String receiver, String message){
        databaseReference  = FirebaseDatabase.getInstance().getReference();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        databaseReference.child("messages").push().setValue(hashMap);

        //for chatlist of frag

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chatlist").child(firebaseUser.getUid()).child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    reference.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = message;
        databaseReference = FirebaseDatabase.getInstance().getReference("members").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Userlist userlist = dataSnapshot.getValue(Userlist.class);
                if (notify) {


                    sendNotification(receiver, userlist.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private  void readMessage(final String myid, final String userid){
        databaseReference = FirebaseDatabase.getInstance().getReference("messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(userid) && chat.getReceiver().equals(myid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        chatList.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,chatList);
                    rvmessagelist.setAdapter(messageAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendNotification(String receiver, final String username, final String message){

        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = databaseReference.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,username+" : "+message,"New Message",userid);

                    Sender sender  = new Sender(data,token.getToken());
                    apiService.sendnotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code()==200){
                                        if (response.body().success ==1){
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 438 && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            fileuri = data.getData();

            String currentmils = String.valueOf(System.currentTimeMillis());

           HashMap<String,String> hashMap = new HashMap<>();
           hashMap.put("currentusser",firebaseUser.getUid());
           hashMap.put("receiver",userid);
           hashMap.put("filename",currentmils);
           databaseReferenceimage.child("Image Files").push().setValue(hashMap);

           sendmessage(firebaseUser.getUid(),userid,currentmils);


            storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");


              filepath = storageReference.child((currentmils));


            UploadTask uploadTask = filepath.putFile(fileuri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        System.out.println("Upload " + downloadUri);
                        Toast.makeText(MessageActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        if (downloadUri != null) {

                            String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            System.out.println("Upload " + photoStringLink);

                        }

                    } else {
                        // Handle failures
                        // ...
                        Toast.makeText(MessageActivity.this,task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });


//            filepath.putFile(fileuri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Get a URL to the uploaded content
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                            // ...
//                        }
//                    });



        }
    }

    public static boolean isNumeric(String strnum){
        if (strnum==null){
            return false;
        }
        try{
            Long strnm = Long.parseLong(strnum);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
