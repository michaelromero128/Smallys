package cornez.com.smallys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class dialogFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup containter, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_my_custom, containter, false);
        return view;


    }
}
