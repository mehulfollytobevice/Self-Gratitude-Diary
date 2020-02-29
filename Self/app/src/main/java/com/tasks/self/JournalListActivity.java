package com.tasks.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.tasks.self.Model.Journal;
import com.tasks.self.ui.JournalListAdapter;
import com.tasks.self.util.JournalAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JournalListActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();// getting the current instance of the firebase in use
//    private CollectionReference collectionReference=db.collection("Users");
    private CollectionReference journalCollection=db.collection("Journal");
    private StorageReference storageReference;
    private FirebaseUser user;

    private List<Journal> journalList;
    private JournalListAdapter journalListAdapter;
    private RecyclerView recyclerView;
    private TextView noJournalEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        noJournalEntry=findViewById(R.id.nothoughts);
        journalList=new ArrayList<>();

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
//                Todo: Add another item
                if (user!=null && firebaseAuth!=null){
                    startActivity(new Intent(JournalListActivity.this,PostJournalActivity.class));
//                    finish();
                }
                break;
            case R.id.action_signout:
//                Todo:Signout of the journal
                if (user!=null && firebaseAuth!=null){
                    firebaseAuth.signOut();//signing out of the firebase
                    startActivity(new Intent(JournalListActivity.this,MainActivity.class));
//                    finish();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        using the query we can get all the information pertaining to a particular user
        journalCollection.whereEqualTo("userid", JournalAPI.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot journals:queryDocumentSnapshots){
//                                journals is a querydocumentSnapshot object
                                Journal journal=journals.toObject(Journal.class);// mapping all the stuff we are getting to an object
                                journalList.add(journal);
                            }
                            journalListAdapter=new JournalListAdapter(journalList,JournalListActivity.this);
                            recyclerView.setAdapter(journalListAdapter);
                            journalListAdapter.notifyDataSetChanged();//notifies recyclerView adapter if anything changes
                        }
                        else{
                            noJournalEntry.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
