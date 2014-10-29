package cs.ku.atomgameandroidcontroller;

/**
 * author info...
 */

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.SocketException;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import android.os.AsyncTask;
import android.widget.EditText;

public class SendCommand extends AsyncTask<String, Void, String>{
    //Initialize a variable to hold the Command Activity instance and
    //a variable to hold a command string (if the calling activity is sending a string)
    //private CommandActivity CmdActParent;
    private String command;

    /*
    public SendCommand(CommandActivity parent, String cmdstring){
        CmdActParent = parent;
        command = cmdstring;
    }
    */

    @Override
    protected String doInBackground(String...info) {
        try{
            //Get the port numbers and IP addresses from the calling method.
            String InIP = info[0];
            String InPort = info[1];
            String OutIP = info[2];
            String OutPort = info[3];
            String seqNum = info[4];

            //Set up the Object array that will be used
            //to bundle up the information into a packet to be sent to HexAtom.
            Object [] oscargs = new Object [4];

            //If the calling Command Activity did not send a command (called via the gamespace Click
            //function), then get the command the user entered in the EditText.
            if(command == ""){
                //Get the command entered by the user and send it to the sendCommand function.
                //EditText editText_command = (EditText)CmdActParent.findViewById(R.id.editText_command);
                //command = editText_command.getText().toString();
            }

            //Bundle up the incoming IP address, incoming port number, sequence number, and command
            oscargs[0] = InIP;
            oscargs[1] = Integer.parseInt(InPort);
            oscargs[2] = Integer.parseInt(seqNum);
            oscargs[3] = command;

            //Initialize the OSC object that will actually send the OSC packet to HexAtom
            OSCPortOut sender = null;

            //Set the IP address and port of the server to which packets will be sent
            if (OutIP != "" && OutPort != "") {
                InetAddress otherIP = InetAddress.getByName(OutIP);
                try {
                    sender = new OSCPortOut(otherIP, Integer.parseInt(OutPort));
                }catch(SocketException e){
                    e.printStackTrace();
                }
            }

            //Send the bundled information to HexAtom
            /*
            try {
                sender.send(new OSCMessage("/interpret", oscargs));
            }catch(final IOException e){
                e.printStackTrace();
            }
            */

        }catch (final UnknownHostException ux) {
            //print err
        }
        return "done";
    }

}
