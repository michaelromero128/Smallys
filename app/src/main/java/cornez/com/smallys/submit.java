package cornez.com.smallys;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class submit extends AppCompatActivity {

    private FirebaseDatabase fireDB;
    private DatabaseReference fireDBref;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;


    // the views
    private CheckBox ckStuffed;
    private CheckBox ckPep;
    private CheckBox ckPine;
    private CheckBox ckHam;


    private RadioGroup radioGroup;
    private RadioButton radioSmall;
    private RadioButton radioMed;
    private RadioButton radioLarge;
    private RadioButton radioXL;

    private Button button;


    //private



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit);
        final CheckBox ckStuffed = findViewById(R.id.ckStuffed);
        final CheckBox ckPep = findViewById(R.id.ckPep);
        final CheckBox ckPine = findViewById(R.id.ckPine);
        final CheckBox ckHam = findViewById(R.id.ckHam);


        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final RadioButton radioSmall = findViewById(R.id.radioSmall);
        final RadioButton radioMed = findViewById(R.id.radioMed);
        final RadioButton radioLarge = findViewById(R.id.radioLarge);
        final RadioButton radioXL = findViewById(R.id.radioXL);

        button = (Button) findViewById(R.id.submitOrderButton);


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
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               // Log.w("message", "the on click listener in submit fired");



                ArrayList<String> toppings = new ArrayList<>();
                    if (ckStuffed.isChecked()) {
                        toppings.add("Stuffed Crust");
                    }
                    if (ckPep.isChecked()) {
                        toppings.add("Pepperoni");
                    }
                    if (ckHam.isChecked()) {
                        toppings.add("Ham");
                    }
                    if (ckPine.isChecked()) {
                        toppings.add("Pineapple");
                    }
                    String topping = "";
                    if (toppings.size() == 0) {
                        topping = "Cheese Pizza";
                    } else {
                        for (int i = 0; i < toppings.size(); i++) {
                            if (i < toppings.size() - 1) {
                                topping += toppings.get(i) + ", ";
                            } else {
                                topping += toppings.get(i);
                            }
                        }
                        topping += " Pizza";
                    }
                    int selected = radioGroup.getCheckedRadioButtonId();
                    String size = "Large";
                    if (selected == R.id.radioSmall) {
                        size = "Small";
                    } else if (selected == R.id.radioMed) {
                        size = "Medium";

                    } else if (selected == R.id.radioXL) {
                        size = "Extra Large";
                    }
                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
                    int count = Integer.parseInt(spinner.getSelectedItem().toString());
                    topping+= ", " + size;
                    final Item item = new Item(topping,count);
                fireDB = FirebaseDatabase.getInstance();

                mFirebaseAuth = FirebaseAuth.getInstance();
                user = mFirebaseAuth.getCurrentUser();
                fireDBref = fireDB.getReference().child("users").child(user.getUid());
                Log.w("message", "onClick fired" );

                fireDBref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for( DataSnapshot ds : dataSnapshot.getChildren()){

                            Log.w("message", ds.getKey());
                            if(ds.getKey().contentEquals("shoppingCart")) {


                                Order order = ds.getValue(Order.class);
                                if (order.getItems() == null) {
                                    ArrayList<Item> items = new ArrayList<>();
                                    items.add(item);
                                    order.setItems(items);
                                } else {
                                    order.addItems(item);
                                }



                                fireDBref.child(ds.getKey()).setValue(order);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                    displayToast();
                    finish();
                }


        });



        }
    private void displayToast(){
        Toast.makeText(this, "Added to Shopping Cart", Toast.LENGTH_LONG).show();

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
