package cornez.com.smallys;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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

public class messageAdapter extends ArrayAdapter<String> {

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private ChildEventListener mChildEvenListener;
    public messageAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.messageiota, parent, false);

        }
        final int posit = position;
        final String string = getItem(position);
        TextView text = (TextView) convertView.findViewById(R.id.textView12);
        Button button = (Button) convertView.findViewById(R.id.button6);
        final String theString = getItem(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFireBaseDatabase = FirebaseDatabase.getInstance();
                mFirebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                mDatabaseReference = mFireBaseDatabase.getReference().child("users").child(user.getUid()).child("messages");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for( DataSnapshot ds : dataSnapshot.getChildren()){
                            String string = ds.getValue(String.class);
                            if(ds.getValue() != null&& string.contentEquals(theString)){
                                mDatabaseReference.child(ds.getKey()).removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });














            }
        });
        text.setText(theString);


        return convertView;
    }

}
