package cs.ku.atomgameandroidcontroller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;
import android.content.Context;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainCommandActivity extends FragmentActivity
                                implements ConnectToServerDialog.ConnectDialogListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_main);

        //set button listeners
        Button connectButton = (Button) findViewById(R.id.button_OpenConnectView);
        connectButton.setOnClickListener(openConnectViewHandler);

        //on activity create, show connection dialog
        showConnectDialog();
    }

    /**
     * Open ConnectToServerView for entering a new IP and port.
     */
    View.OnClickListener openConnectViewHandler = new View.OnClickListener() {
        public void onClick(View v) {
            showConnectDialog();

        }
    };

    public void showConnectDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ConnectToServerDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the ConnectToServer.ConnectDialogListener interface
    @Override
    public void onDialogPositiveClick(ConnectToServerDialog dialog, String IP, String Port) {

        //validate input
        if(validateIP(IP)){
            //input is valid
            //display success toast
            Context context = getApplicationContext();
            CharSequence text = "Hey cool. Your Connected!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0,40);
            toast.show();
            //dismiss connection dialog box
            dialog.dismiss();
        }else{
            //error, input not valid
            Context context = getApplicationContext();
            CharSequence text = "Sorry! invalid IP or Port :(";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0,0);
            toast.show();

            showConnectDialog();
        }
    }

    @Override
    public void onDialogNegativeClick(ConnectToServerDialog dialog) {
        // User touched the dialog's negative button--cancel

    }

    /**
     * Validate ip address.
     * @param ip ip address for validation
     * @return true valid ip address, false invalid ip address
     */
    public boolean validateIP(final String ip){
        Pattern pattern = Pattern.compile(
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * Validate port number.
     * @param port port number for validation
     * @return true valid port number, false invalid port number
     * @throws UnsupportedOperationException protocol for connecting is currently
     * unspecified. Thus, port number cannot be validated to be in range.
     */
    public boolean validatePort(final String port) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }

}
