package ramroservices.com.np.chatdemo.ChatDemo;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ramroservices.com.np.chatdemo.R;

public class LoginActivity extends AppCompatActivity {

    EditText etusername,etpassword;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressBar progressBar;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnlogin = findViewById(R.id.btnsubmit);

        etusername = findViewById(R.id.etusername);
        etpassword = findViewById(R.id.etpassword);
        progressBar = new ProgressBar(LoginActivity.this,null,android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null){
            Intent intent = new Intent(LoginActivity.this,LandingPage.class);
            startActivity(intent);

        }


        Button btnregister = findViewById(R.id.btnregister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLogin();
            }
        });
    }

    public void myLogin(){


        String username = etusername.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        if (!username.equals("") && !password.equals("")) {

            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(LoginActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }


}
