package com.tasks.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.tasks.self.Model.Journal;
import com.tasks.self.util.JournalAPI;

import java.util.Objects;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button createAcctButton;
    private AutoCompleteTextView email;
    private EditText password;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        firebaseAuth=FirebaseAuth.getInstance();
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        loginButton=findViewById(R.id.loginButton);
        createAcctButton=findViewById(R.id.createAccButton);
        progressBar=findViewById(R.id.progress_login);

        createAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is where we go to the create account page
                startActivity(new Intent(LoginActivity.this,CreateAccount.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmailPasswordUser(email.getText().toString().trim(),password.getText().toString().trim());
            }
        });
    }

    private void loginEmailPasswordUser(String emailid,String password) {
        progressBar.setVisibility(View.VISIBLE);
    if (!TextUtils.isEmpty(emailid) && !TextUtils.isEmpty(password)){
        firebaseAuth.signInWithEmailAndPassword(emailid,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        assert user != null;
                        final String currentuserid=user.getUid();
                        progressBar.setVisibility(View.INVISIBLE);
                        collectionReference.whereEqualTo("userId",currentuserid)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e!=null){
//                                    some error has occured
                                        }
                                        assert queryDocumentSnapshots != null;
                                        if (!queryDocumentSnapshots.isEmpty()){
                                            for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
//                                        it is customary to put everything within a loop so we are doing it , could have done it directly also
                                                JournalAPI journalAPI=JournalAPI.getInstance();
                                                journalAPI.setUsername(snapshot.getString("username"));
                                                journalAPI.setUserId(snapshot.getString("userId"));
                                                startActivity(new Intent(LoginActivity.this,PostJournalActivity.class));
                                            }
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }
    else{
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(LoginActivity.this,"please enter stuff",Toast.LENGTH_LONG).show();
    }
    }

}
