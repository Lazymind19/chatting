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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import ramroservices.com.np.chatdemo.R;


public class FragmentPhoneVerification extends Fragment {

    EditText etphonenum, etverificationcode;
    Button btngetverification,btnsubmitverification;
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    String codesent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment_phone_verification, container, false);

        etphonenum = view.findViewById(R.id.etphohnenumber);
        etverificationcode =view.findViewById(R.id.etverificationcode);

        btngetverification = view.findViewById(R.id.btnverificationcode);
        btnsubmitverification=view.findViewById(R.id.btnsubmit);

        btngetverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String num= etphonenum.getText().toString().trim();
                sendverificationcode();

            }
        });
        btnsubmitverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitverificationcode();
            }
        });



        return view;
    }

    public void sendverificationcode(){
        String num= "+"+etphonenum.getText().toString().trim();


        PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codesent = s;



            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,60, TimeUnit.SECONDS,getActivity(),mcallBacks

        );




    }

    public  void submitverificationcode(){
        String code = etverificationcode.getText().toString().trim();
        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(codesent,code);
        signinwithphoneauthcredential(credential);
    }
    private  void signinwithphoneauthcredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            FirebaseUser user=task.getResult().getUser();
                            String userid = user.getUid();
                            String username = user.getPhoneNumber();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("members").child(userid);
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getContext(), LandingPage.class));

                                    } else {
                                        Toast.makeText(getContext(), "Error occur : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }



                        else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getActivity(), "Invalid code", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });
    }
}
