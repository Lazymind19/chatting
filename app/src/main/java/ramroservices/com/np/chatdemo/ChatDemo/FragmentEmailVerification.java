package ramroservices.com.np.chatdemo.ChatDemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import ramroservices.com.np.chatdemo.Model.Member;
import ramroservices.com.np.chatdemo.R;


public class FragmentEmailVerification extends Fragment {
    EditText etemail, etpassword, etrepassword, etusername;
    Button btnsubmit;
    Member member;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_email_verification, container, false);

        etemail = view.findViewById(R.id.etemail);
        etpassword = view.findViewById(R.id.etpassword);
        etrepassword = view.findViewById(R.id.etrepassword);
        btnsubmit = view.findViewById(R.id.btnsubmit);
        etusername = view.findViewById(R.id.etusername);
        member = new Member();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("members");
        firebaseAuth = FirebaseAuth.getInstance();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        return view;


    }


    public void registerUser() {
        final String email = etemail.getText().toString().trim();
        String pass = etpassword.getText().toString().trim();
        String repass = etpassword.getText().toString().trim();
        final String userame = etusername.getText().toString().trim();

        if (!email.isEmpty() && !pass.isEmpty() && !repass.isEmpty() && !userame.isEmpty()) {

            if (pass.equals(repass)) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                       /* member = new Member();
                        member.setUsername(email);
                        databaseReference.push().setValue(member);
*/
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("members").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", userame);

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(getContext(), "Cannot register with this info", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

//                            Toast.makeText(getContext(), "User registerd successfully", Toast.LENGTH_SHORT).show();
//                        etemail.setText("");
//                        etpassword.setText("");
//                        etrepassword.setText("");
//                        }
//                        else
//                        {
//                            task.addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure( Exception e) {
//
//                                    Toast.makeText(getContext(), "Some logical error"+e.toString(), Toast.LENGTH_SHORT).show();
//
//
//                                }
//                            });
//                           // Toast.makeText(RegisterActivity.this, "Some logical error", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });


                        } else {
                            Toast.makeText(getContext(), "cannot register" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }


                });
            }

        }
    }
}







