package cornez.com.smallys;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CheckOut extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user;

    private ListView mOrderListView;
    private ArrayList<Item> items;
    private CheckOutAdapter mcheckOutAdapter;



    protected void onCreate(Bundle savedInstanceState) {
        // super and setting the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Log.w("message user", user.toString());
                if (user == null) {
                    finish();
                }
            }
        };

        final List<Item> items= new ArrayList<>();
        mOrderListView = (ListView) findViewById(R.id.checkoutListView);
        mcheckOutAdapter = new CheckOutAdapter(this, R.layout.orderlayout, items);
        mOrderListView.setAdapter(mcheckOutAdapter);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("shoppingCart");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Order.class)!= null) {
                    Log.w("message", dataSnapshot.getKey());
                    if (dataSnapshot.getKey() == "shoppingCart") {
                        Log.w("message", "datasnapshot of shopping cart");
                        Order order = dataSnapshot.getValue(Order.class);
                        items.clear();
                        if(order.getItems() !=null) {
                            for (int i = 0; i < order.getItems().size(); i++) {
                                items.add(order.getItems().get(i));

                            }
                        }
                        mcheckOutAdapter.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button goback = (Button) findViewById(R.id.back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submit = (Button) findViewById(R.id.placeOrder);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getKey().contentEquals("shoppingCart")){

                            Order order = dataSnapshot.getValue(Order.class);
                            if( order!= null ) {
                                if (order.getItems() !=  null) {
                                    ArrayList<Item> items = new ArrayList<>();
                                    Order ordered = new Order(user.getDisplayName(), items, user.getUid());
                                    mDatabaseReference.setValue(ordered);
                                    mFirebaseDatabase.getReference().child("pending").push().setValue(order);

                                    Log.w("message", order.toString());
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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


