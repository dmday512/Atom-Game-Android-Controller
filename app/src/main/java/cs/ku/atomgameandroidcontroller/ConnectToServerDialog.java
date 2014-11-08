package cs.ku.atomgameandroidcontroller;

/**
 * Author Information.
 */

import android.support.v4.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.R;
import android.text.Editable;
import android.view.LayoutInflater;
import android.app.Activity;
import android.widget.Toast;
import android.widget.EditText;
import android.view.View;

public class ConnectToServerDialog extends DialogFragment {

        /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
        public interface ConnectDialogListener{
            public void onDialogPositiveClick(ConnectToServerDialog dialog,String IP, String Port);
            public void onDialogNegativeClick(ConnectToServerDialog dialog);
        }

        // Use this instance of the interface to deliver action events
        ConnectDialogListener mListener;

        // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
                mListener = (ConnectDialogListener) activity;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(activity.toString()
                        + " must implement ConnectDialogListener");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View v = inflater.inflate(cs.ku.atomgameandroidcontroller.R.layout.dialog_connect, null);
            builder.setView(v);

            builder.setTitle("Connect");

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Editable IP_text = ((EditText)v.findViewById(cs.ku.atomgameandroidcontroller.
                                                                R.id.editText_IPAddress)).getText();
                    String IP = IP_text.toString();
                    Editable Port_text = ((EditText)v.findViewById(cs.ku.atomgameandroidcontroller.
                                                                R.id.editText_PortNumber)).getText();
                    String Port = Port_text.toString();
                    mListener.onDialogPositiveClick(ConnectToServerDialog.this,IP,Port);
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //send message to host activity
                    mListener.onDialogNegativeClick(ConnectToServerDialog.this);
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }

}
