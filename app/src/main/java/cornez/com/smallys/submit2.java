package cornez.com.smallys;

import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class submit2 extends AppCompatActivity{

    private FirebaseDatabase fireDB;
    private DatabaseReference fireDBref;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private Button button;
    private int countCB;
    private int countWings;
    private int countSoda;
    private int countBrownies;
    private  Spinner spinnerCB;
    private  Spinner spinnerWings;
    private  Spinner spinnerSoda;
    private  Spinner spinnerBrownies;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extras);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Log.w("message user", user.toString());
                if (user == null) {
                    finish();
                }
            }
        };
        mFirebaseAuth =FirebaseAuth.getInstance();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        button = findViewById(R.id.extrasSubmitButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                spinnerCB = (Spinner) findViewById(R.id.spinnerCB);
                spinnerWings = (Spinner) findViewById(R.id.spinnerWings);
                spinnerSoda = (Spinner) findViewById(R.id.spinnerSoda);
                spinnerBrownies = (Spinner) findViewById(R.id.spinnerBrownies);
                countCB = Integer.parseInt(spinnerCB.getSelectedItem().toString());
                countWings = Integer.parseInt(spinnerWings.getSelectedItem().toString());
                countSoda = Integer.parseInt(spinnerSoda.getSelectedItem().toString());
                countBrownies = Integer.parseInt(spinnerBrownies.getSelectedItem().toString());

                Log.w("message", Integer.toString(countBrownies+countCB+countSoda+countWings));
                fireDB = FirebaseDatabase.getInstance();

                mFirebaseAuth = FirebaseAuth.getInstance();
                user = mFirebaseAuth.getCurrentUser();
                fireDBref = fireDB.getReference().child("users").child(user.getUid());
                Log.w("message", "onClick fired" );

                if(countCB!= 0 || countWings !=0 || countSoda !=0 || countBrownies !=0) {


                    fireDBref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if(ds.getKey().contentEquals("shoppingCart")) {
                                    Order order = ds.getValue(Order.class);
                                    if (order.getItems() == null) {
                                        ArrayList<Item> items = new ArrayList<>();
                                        order.setItems(items);
                                    }
                                    if(countCB!= 0) {
                                        order.addItems(new Item("Crazy Bread", countCB));
                                    }
                                    if(countWings != 0){
                                        order.addItems(new Item("Wings", countWings));
                                    }
                                    if(countSoda !=0){
                                        order.addItems(new Item("Soda", countSoda));
                                    }
                                    if (countBrownies != 0) {
                                        order.addItems(new Item("Brownies", countBrownies));

                                    }
                                    fireDBref.child(ds.getKey()).setValue(order);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();
                }
                else {
                    makeToast();

                }




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
    public void makeToast(){
        Toast.makeText(this, "Nothing selected", Toast.LENGTH_LONG).show();


    }
}