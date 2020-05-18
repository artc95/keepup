package artc.artc.keepup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText editEmailAddress, editPassword, editPhoneNumber, editOTP;
    Button buttonRegister, buttonLogin, buttonSendOTP, buttonVerifyOTP;

    FirebaseAuth mAuth;
    DatabaseReference databaseShares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editEmailAddress = findViewById(R.id.editEmailAddress);
        editPassword = findViewById(R.id.editPassword);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editOTP = findViewById(R.id.editOTP);

        databaseShares = FirebaseDatabase.getInstance().getReference();

        //final String phone_number = editPhoneNumber.getText().toString();

        // Register with Email Address and Password
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_address = editEmailAddress.getText().toString();
                editEmailAddress.setText("");
                String password = editPassword.getText().toString();
                editPassword.setText("");
                registerUser(email_address, password);
            }
        });

        // Login with Email Address and Password
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_address = editEmailAddress.getText().toString();
                String password = editPassword.getText().toString();
                editPassword.setText("");
                loginUser(email_address, password);
            }
        });

        buttonSendOTP = findViewById(R.id.buttonSendOTP);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);
    }

    private void registerUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // If registration succeeds, inform user and create new database branch using unique User ID
                    Toast.makeText(LoginActivity.this, String.format("%s registered!", email), Toast.LENGTH_SHORT).show();
                } else {
                    // If registration fails, inform user to try again
                    Toast.makeText(LoginActivity.this, "Registration failed, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // If login succeeds, update UI with user's unique User ID
                    FirebaseUser user = mAuth.getCurrentUser();
                    String user_email = user.getEmail();
                    String user_id = user.getUid();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user_email", user_email);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                } else {
                    // If sign in fails, inform user to try again
                    Toast.makeText(LoginActivity.this, "Login failed, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
