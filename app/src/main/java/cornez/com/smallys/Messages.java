package cornez.com.smallys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user;

    private ListView mOrderListView;
    private ArrayList<String> strings;
    private messageAdapter mMessageAdapter;



    protected void onCreate(Bundle savedInstanceState) {
        // super and setting the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Log.w("message user", user.toString());
                if (user == null) {
                    finish();
                }
            }
        };

        final List<String> strings= new ArrayList<>();
        mOrderListView = (ListView) findViewById(R.id.messagesListView);
        mMessageAdapter = new messageAdapter(this, R.layout.messageiota, strings);
        mOrderListView.setAdapter(mMessageAdapter);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("messages");

       mDatabaseReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               String theString =dataSnapshot.getValue(String.class);
               strings.add(theString);
               mMessageAdapter.notifyDataSetChanged();
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String theString = dataSnapshot.getValue(String.class);
                for(int i = 0; i <strings.size(); i++){
                    if(theString.contentEquals(strings.get(i))){
                        strings.remove(i);
                        break;
                    }
                }
               mMessageAdapter.notifyDataSetChanged();

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.signOut){
            AuthUI.getInstance().signOut(this);
            return true;
        } else{
            return super.onOptionsItemSelected(item);
        }
    }


}