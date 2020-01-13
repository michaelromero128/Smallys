package cornez.com.smallys;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    private DatabaseReference fireDBref;
    private FirebaseDatabase mFireDB;

    public OrderAdapter(Context context, int resource, List<Order> objects){
        super(context,resource,objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.order_item, parent, false);

        }
        mFireDB = FirebaseDatabase.getInstance();
        fireDBref = mFireDB.getReference().child("pending");
        TextView orderTextView = (TextView) convertView.findViewById(R.id.orderTextView);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        Button button = (Button) convertView.findViewById(R.id.deleteb);

        TextView order = (TextView) convertView.findViewById(R.id.orderTextView);
        TextView name = (TextView) convertView.findViewById(R.id.orderTextView);
        final Order theOrder = getItem(position);
        button.setTag(theOrder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireDBref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Order order = ds.getValue(Order.class);
                            if(theOrder.toString().contentEquals(order.toString())){
                                fireDBref.child(ds.getKey()).removeValue();
                                mFireDB.getReference().child("users").child(order.getUserID()).child("messages").push().setValue("Your order of: " +order.otherToString() +" : is ready");
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


        String description = theOrder.toString();
        orderTextView.setText(description);
        nameTextView.setText(theOrder.getName());

        return convertView;
    }
}
