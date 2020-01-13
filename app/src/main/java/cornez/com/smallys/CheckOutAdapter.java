package cornez.com.smallys;


import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

public class CheckOutAdapter extends ArrayAdapter<Item> {

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private ChildEventListener mChildEvenListener;
    public CheckOutAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.orderlayout, parent, false);

        }
        final int posit = position;
        final Item item = getItem(position);
        TextView count = (TextView) convertView.findViewById(R.id.count);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        count.setText(Integer.toString(item.getCount()) +"x");
        description.setText(item.getDescription());
        Button button = (Button) convertView.findViewById(R.id.removeButton);
        final List<Item> items = new ArrayList<>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFireBaseDatabase = FirebaseDatabase.getInstance();
                mFirebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                mDatabaseReference = mFireBaseDatabase.getReference().child("users").child(user.getUid());
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for( DataSnapshot ds : dataSnapshot.getChildren()){
                            if(ds.getKey().contentEquals("shoppingCart")){
                                Order order = ds.getValue(Order.class);
                                if(order.getItems() != null){
                                    order.getItems().remove(posit);
                                    mDatabaseReference.child(ds.getKey()).setValue(order);
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


        return convertView;
    }


}