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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tasks.self.util.JournalAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccount extends AppCompatActivity {
//    //    authentication is a seperate subsection of firebase
//    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private FirebaseUser currentuser;
//    //    firebase connection
//    private FirebaseFirestore db=FirebaseFirestore.getInstance();
//    private CollectionReference collectionReference=db.collection("Users");
////    widgets
//    private EditText username;
//    private AutoCompleteTextView email;
//    private EditText password;
//    private Button createAccount;
//    private ProgressBar progressBar;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_account);
//        firebaseAuth=FirebaseAuth.getInstance();
//
//        username=findViewById(R.id.usernameacct);
//        email=findViewById(R.id.email_acct);
//        password=findViewById(R.id.password_acct);
//        progressBar=findViewById(R.id.progress_acct);
//        createAccount=findViewById(R.id.acctButton);
//
//
//        authStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                currentuser=firebaseAuth.getCurrentUser();
//                if (currentuser!=null){
////                    we are already logged in
//                }
//                else{
////                    no user yet...
//                }
//
//            }
//        };
//
//        createAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){
//                    String emailaccount=email.getText().toString().trim();
//                    String passwordtext=password.getText().toString().trim();
//                    String usernametext=username.getText().toString().trim();
//                    createEmailAccount(emailaccount,passwordtext,usernametext);
//                }
//                else{
//                    Toast.makeText(CreateAccount.this,"Empty fields please fill",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    private void createEmailAccount(String emailt, String passwordt,final String usernamet){
//        if (!TextUtils.isEmpty(emailt) && !TextUtils.isEmpty(passwordt) && !TextUtils.isEmpty(usernamet) ){
//
////            progress bar is used to show the users that soamething is happening in the back-end
//            progressBar.setVisibility(View.VISIBLE);
//            firebaseAuth.createUserWithEmailAndPassword(emailt,passwordt)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()){
////                                we take the user to the AddJournalActivity
//                                currentuser=firebaseAuth.getCurrentUser();
//                                assert currentuser != null;
//                                final String userId=currentuser.getUid();
//
////                                creating map to put username and userId into a collection , to keep of track of all the users wh logged in
//                                Map<String,String> userMap=new HashMap<>();
//                                userMap.put("userId",userId);
//                                userMap.put("username",usernamet);
//
////                                save to our firestore database
//                                collectionReference.add(userMap)
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//
//                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                        if (Objects.requireNonNull(task.getResult()).exists()){
//                                                            progressBar.setVisibility(View.INVISIBLE);
//                                                            String name=task.getResult().getString("username");
//                                                            Intent intent=new Intent(CreateAccount.this,PostJournalActivity.class);
//                                                            intent.putExtra("username",name);
//                                                            intent.putExtra("username",userId);
//                                                        }
//                                                        else {
//                                                            progressBar.setVisibility(View.INVISIBLE);
//                                                        }
//                                                    }
//                                                }).addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//
//                                                    }
//                                                });
//
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//
//                                            }
//                                        });
//                            }
//                            else{
////                                something went wrong
//                                Toast.makeText(CreateAccount.this,"Something is wrong",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(CreateAccount.this,"Failed",Toast.LENGTH_LONG).show();
//                }
//            });
//        }else{
//            Toast.makeText(CreateAccount.this,"failed",Toast.LENGTH_LONG).show();
//        }
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        currentuser=firebaseAuth.getCurrentUser();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }

//    Something wrong in the code please verify
    private Button createAcctButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");


    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private EditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        firebaseAuth = FirebaseAuth.getInstance();

        createAcctButton = findViewById(R.id.acctButton);
        progressBar = findViewById(R.id.progress_acct);
        emailEditText = findViewById(R.id.email_acct);
        passwordEditText = findViewById(R.id.password_acct);
        userNameEditText = findViewById(R.id.usernameacct);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    //user is already loggedin..
                }else {
                    //no user yet...
                }

            }
        };

        createAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailEditText.getText().toString())
                        && !TextUtils.isEmpty(passwordEditText.getText().toString())
                        && !TextUtils.isEmpty(userNameEditText.getText().toString()))  {

                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String username = userNameEditText.getText().toString().trim();

                    createUserEmailAccount(email, password, username);

                }else {
                    Toast.makeText(CreateAccount.this,
                            "Empty Fields Not Allowed ",
                            Toast.LENGTH_LONG)
                            .show();
                }



            }
        });

    }

    private void createUserEmailAccount(String email, String password, final String username) {
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(username)) {

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //we take user to AddJournalActivity
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserId = currentUser.getUid();

                                //Create a user Map so we can create a user in the User collection
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);

                                //save to our firestore database
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Objects.requireNonNull(task.getResult()).exists()) {
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String name = task.getResult()
                                                                            .getString("username");

                                                                    JournalAPI journalAPI=JournalAPI.getInstance();
                                                                    journalAPI.setUserId(currentUserId);
                                                                    journalAPI.setUsername(name);

                                                                    Intent intent = new Intent(CreateAccount.this,
                                                                            PostJournalActivity.class);
                                                                    intent.putExtra("username", name);
                                                                    intent.putExtra("userId", currentUserId);
                                                                    startActivity(intent);


                                                                }else {

                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                }

                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                            }else {
                                //something went wrong
                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

}
