package cornez.com.smallys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

public class Welcome extends AppCompatActivity {

    private Button newOrder;
    private Button oldOrder;
    private Button extras;
    private Button checkOut;
    private Button button2;
    private Button buttonMessages;
    private FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;
    private Order order;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super and setting the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // make or access a shopping cart order;



        newOrder = (Button) findViewById(R.id.gotoPlaceOrder);
        checkOut = (Button) findViewById(R.id.gotoCheckOut);
        button2 = (Button) findViewById(R.id.gotoExtras);
        buttonMessages = (Button) findViewById(R.id.button4);
        newOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.w("message", "order a pizza ");

                Intent intent = new Intent(Welcome.this, submit.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("message", "firing onclick listener for extras");
                Intent intent = new Intent(Welcome.this, submit2.class);
                startActivity(intent);

            }
        });
        buttonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("message", "firing onclick listener for extras");
                Intent intent = new Intent(Welcome.this, Messages.class);
                startActivity(intent);

            }
        });


        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("message", "go to check out ");

                // change this soon
                Intent intent = new Intent(Welcome.this, CheckOut.class);
                startActivity(intent);
            }
        });
        mFireBaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if (firebaseAuth.getCurrentUser() == null){
                    finish();
                } else{
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    user = firebaseAuth.getCurrentUser();
                    mDatabaseReference = mFirebaseDatabase.getReference();

                    mDatabaseReference.child("users").child(user.getUid()).child("shoppingCart").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                i++;
                            }
                            if(i == 0){
                                ArrayList<Item> items = new ArrayList<>();
                                Order order = new Order(user.getDisplayName(),items, user.getUid());
                                mDatabaseReference.child("users").child(user.getUid()).child("shoppingCart").setValue(order);

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }


            }
        };
        mFireBaseAuth.addAuthStateListener(mAuthStateListener);

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
