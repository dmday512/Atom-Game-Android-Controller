package cs.ku.atomgameandroidcontroller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Intent;
import android.content.ComponentName;
import android.os.IBinder;
import android.content.ServiceConnection;
import android.util.Log;
import android.os.StrictMode;

public class MainCommandActivity extends FragmentActivity
                                implements ConnectToServerDialog.ConnectDialogListener{

    private static ServerProxy serverProxy;
    private Intent spIntent;
    private static boolean serverBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /******* DUMMY PROCEDURES ***********/
        //set button listeners
        Button connectButton = (Button) findViewById(R.id.button_OpenConnectView);
        connectButton.setOnClickListener(openConnectViewHandler);

        Button sendCommandButton = (Button) findViewById(R.id.button_SendCommand);
        sendCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mT = (EditText) findViewById(R.id.editText_EnterCommand);
                String message = (mT.getText()).toString();
                boolean send_status = MainCommandActivity.serverProxy.sendMessage(message);
            }
        });
        /******* END DUMMY PROCEDURES *********/

        //on activity create, show connection dialog
        showConnectDialog();
    }

    @Override
    protected void onStart(){
        //bind ServerProxy bound service.
        spIntent = new Intent(this,ServerProxy.class);
        bindService(spIntent,serverConnection,Context.BIND_AUTO_CREATE);
        super.onStart();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        //unbind ServerProxy bound service.
        if (this.serverBound) {
            unbindService(serverConnection);
            serverBound = false;
        }

        super.onStop();
    }
    @Override
    protected void onDestroy(){
        closeConnectionToServer();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
    }

    public void connectToServer(String IP, String Port){
        if(this.serverBound){
            this.serverProxy.setProxyCredentials(IP,Port);
        }else{
            //display toast to let client know the connect was not sucessfull
            CharSequence text = "Oops. Something went wrong. Err: ServerProxy bind unsuccessful.";
            (Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)).show();
        }
    }

    public void closeConnectionToServer(){}

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
        return true;
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the ConnectToServer.ConnectDialogListener interface
    @Override
    public void onDialogPositiveClick(ConnectToServerDialog dialog, String IP, String Port) {

        //validate input
        if(validateIP(IP)&&validatePort(Port)){
            //input is valid
            //display success toast
            Context context = getApplicationContext();
            CharSequence text = "Cool. Your Connected! :)";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0,40);
            toast.show();
            //dismiss connection dialog box
            dialog.dismiss();
            //start service for connecting to the server, serverProxy
            connectToServer(IP,Port);
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

    /** Defines callbacks for server binding, passed to bindService() */
    private ServiceConnection serverConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to ServerBinder, cast the IBinder and get ServerBinder instance
            ServerProxy.ServerBinder binder = (ServerProxy.ServerBinder) service;
            MainCommandActivity.serverProxy = binder.getService();
            MainCommandActivity.serverBound = true;

            if(MainCommandActivity.serverProxy == null) {
                Log.i("ServiceConnection", "Bind to service ServerProxy was unsuccessful.");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            MainCommandActivity.serverBound = false;
        }
    };

}
