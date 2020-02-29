package com.tasks.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tasks.self.Model.Journal;
import com.tasks.self.util.JournalAPI;

import java.util.Date;
import java.util.Objects;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 1;
    private Button saveButton;
    private ProgressBar postProgress;
    private EditText title;
    private EditText thought;
    private TextView date;
    private TextView post_username;
    private ImageView addImage;
    private ImageView back;

    private String currentuserId;
    private String username;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference=db.collection("Journal");
    private Uri imageURI;//this is the path on the device where the image lives

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        postProgress=findViewById(R.id.post_progress);
        title=findViewById(R.id.post_title);
        thought=findViewById(R.id.post_thought);
        date=findViewById(R.id.post_date);
        post_username=findViewById(R.id.post_user);
        addImage=findViewById(R.id.add_image);
        back=findViewById(R.id.post_image_back);
        saveButton=findViewById(R.id.post_save_button);


        saveButton.setOnClickListener(this);
        addImage.setOnClickListener(this);

        if (JournalAPI.getInstance()!=null){
            currentuserId=JournalAPI.getInstance().getUserId();
            username=JournalAPI.getInstance().getUsername();

            post_username.setText(username);

        }
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
                if (user!=null){
                }
                else{
                }

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.post_save_button:
//                saves all the data in the journal with the image
                saveJournal();
                break;
            case R.id.add_image:
//                just lets us choose an image
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);// this is an implicit intent ,used to get image from gallery
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
                break;
        }

    }

    private void saveJournal() {
        final String titlepost=title.getText().toString().trim();
        final String thoughtpost=thought.getText().toString().trim();
        postProgress.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(titlepost) && !TextUtils.isEmpty(thoughtpost) && imageURI!=null){

//            Todo:create a journal object
//            Todo: invoke the collection reference
//            Todo: save the journal instance


            final StorageReference filepath=storageReference.child("journal_images")
                    .child("images_"+ Timestamp.now().getSeconds());// creates a folder for the images

            filepath.putFile(imageURI)//putting images in the location
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            postProgress.setVisibility(View.INVISIBLE);

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl=uri.toString();
                            Journal journal=new Journal();
                            journal.setTitle(titlepost);
                            journal.setThoughts(thoughtpost);
                            journal.setUserid(currentuserId);
                            journal.setUsername(username);
                            journal.setTimeadded(new Timestamp(new Date()));// adding data using firebase timestamp
                            journal.setImageURL(imageurl);

                            collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    postProgress.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(PostJournalActivity.this,JournalListActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(PostJournalActivity.this,"Fill everything",Toast.LENGTH_LONG).show();
        }

           }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            if (data!=null){
                imageURI = data.getData();
                back.setImageURI(imageURI);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        it is always a good idea to remove authentication Listener after the work is done cause you don't want to be listening after the application is done with
        if (firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}


