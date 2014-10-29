package cs.ku.atomgameandroidcontroller;

/**
 * Author info and such
 */

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.UnsupportedOperationException;
import android.widget.Button;
import android.view.View;
import android.graphics.Color;

/**
 * Activity for entering and validating IP address and port number to connect to
 * the server for HexAtomGame.
 */
public class ConnectToServerView extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_server_view);

        Button connectButton = (Button) findViewById(R.id.button_Connect);
        connectButton.setOnClickListener(connectHandler);
    }

    /**
     * Event handler for R.id.button_Connect. Take's IP address returns it's validated
     * status [true/false].
     */
    View.OnClickListener connectHandler = new View.OnClickListener(){
        public void onClick(View v) {
            //make and display toast, without holding a ref to toast
            Context context = getApplicationContext();
            TextView errorText = (TextView)findViewById(R.id.textView_Error);
            errorText.setText("");  //reset error message if one exists
            String IPAddr = ((EditText)findViewById(R.id.textField_IPAddress)).getText().toString();
            boolean IPValidated = validateIP(IPAddr);

            //for now display toast if successful validation, else display err on view
            if(IPValidated){
                String text = "Cool, those credentials look good!";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, text, duration).show();
            }else{
                errorText.setTextColor(Color.RED);
                errorText.setText("Sorry, invalid IP.");
            }
        }
    };

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
