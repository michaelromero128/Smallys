package cornez.com.smallys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    private final static int RC_SIGN_IN = 1;
    private FirebaseDatabase fireDB;
    private DatabaseReference fireDBref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInIntialize(user);
                } else{
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                                            new AuthUI.IdpConfig.AnonymousBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                    //user is signed out
                }


            }
        };


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toasty.info(this, "Signed in!", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED){
                Toasty.error(this ,"Signed in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    protected void onPause(){
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    private void onSignedInIntialize(FirebaseUser user ){
        String uID = user.getUid();
        if(uID.contentEquals("KWh8zLG4AKW6f4XEeTpUhbsUElt1")){
            Intent intent = new Intent (MainActivity.this, viewOrders.class );
            startActivity(intent);
            // access with user email kitchen@kitchen.kitchen password kitchenadmin
        } else if(uID.contentEquals("YNEEy0GgTlXZkQxmC9o7WRlbDQT2")){
            Intent intent = new Intent(MainActivity.this, Bonus.class);
            startActivity(intent);    // access with email Jim@jim.jim password jimjim
            // this is where I dumped all the project requirements since I didn't need to use fragments or dialog boxes
        } else {
            Intent intent = new Intent (MainActivity.this, Welcome.class );
            startActivity(intent);
        }




    }




}
