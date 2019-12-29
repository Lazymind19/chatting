package ramroservices.com.np.chatdemo.Adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import ramroservices.com.np.chatdemo.Model.Chat;
import ramroservices.com.np.chatdemo.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context context;
    List<Chat> chatlists;
    FirebaseUser firebaseUser;
    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT =1;

    public MessageAdapter(Context context, List<Chat> chatlists) {
        this.context = context;
        this.chatlists = chatlists;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i ==MSG_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder viewHolder, int i) {
        Chat chat = chatlists.get(i);
        String message = chat.getMessage();
        if (isNumeric(message)==false) {


            viewHolder.tvshowmessage.setText(message);
            viewHolder.ivpic.setVisibility(View.GONE);
        }
        else {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Image Files");
          storageReference.child(message).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
              @Override
              public void onSuccess(Uri uri) {
                  Log.d("TAG", "onSuccess: ");
                //  String filepath = uri.getPath();
                  viewHolder.ivpic.setVisibility(View.VISIBLE);
                  Picasso.get().load(uri).fit().centerCrop().into(viewHolder.ivpic);

              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

              }
          });

           // viewHolder.tvshowmessage.setText("Picture is here");
        }

    }

    @Override
    public int getItemCount() {
        return chatlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvshowmessage;
        ImageView ivpic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvshowmessage = itemView.findViewById(R.id.showmessage);
           ivpic = itemView.findViewById(R.id.ivpic);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatlists.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return MSG_RIGHT;
        }
        else {
            return MSG_LEFT;
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
