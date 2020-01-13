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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class viewOrders extends AppCompatActivity {

    private ChildEventListener mChildEventListener;
    private OrderAdapter mOrderAdapter;
    private ListView mOrderListView;
    private ArrayList<String> orders;
    private DatabaseReference fireDBref;
    private FirebaseDatabase mFireDB;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders);
        // gets a Fire db instance and reference
        mFireDB = FirebaseDatabase.getInstance();
        fireDBref = mFireDB.getReference().child("pending");
        // builds an array of orders
        final List<Order> orders = new ArrayList<>();
        mOrderListView = (ListView) findViewById(R.id.order_ListView);

        mOrderAdapter = new OrderAdapter(this, R.layout.order_item,orders);

        mOrderListView.setAdapter(mOrderAdapter);
        mFirebaseAuth = FirebaseAuth.getInstance();





        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order order = dataSnapshot.getValue(Order.class);
                orders.add(order);
                mOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);



                for (int i = 0; i < orders.size(); i++){
                    if(orders.get(i).getName() == order.getName()){
                        orders.remove(i);

                    }
                }
                mOrderListView.setAdapter(mOrderAdapter);

                mOrderAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        Log.w("message", "firing authstate listener in view order");
        fireDBref.addChildEventListener(mChildEventListener);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Log.w("message user", user.toString());
                if (user == null) {
                    finish();
                }
            }
        };









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
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
            return true;
        } else{
            return super.onOptionsItemSelected(item);
        }
    }

}
