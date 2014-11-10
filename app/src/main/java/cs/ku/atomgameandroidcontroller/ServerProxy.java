package cs.ku.atomgameandroidcontroller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.util.Log;

public class ServerProxy extends Service {
    //members associated with establishing a connection with the server
    private String InIP;
    private String InPort;
    private String OutIP;
    private String OutPort;
    private Integer seqNum;

    private final IBinder binder = new ServerBinder();

    public ServerProxy(){
        this.InIP = getLocalIpAddress();
        this.InPort = "5000";
        this.seqNum = 1;
        this.OutIP = "";
        this.OutPort = "";
    }

    public void setProxyCredentials(String IP, String Port){
        this.OutIP = IP;
        this.OutPort = Port;
    }

    /**
     * Send a message to the server. Will return true if successful and false otherwise.
     *
     * @param message message to send to the server via OSC
     * @return true if message was successfully send, false otherwise. Note this does not
     * guarantee the message was received by the server.
     */
    public boolean sendMessage(String message){
        try{
            Object [] oscargs = new Object [4];

            //Bundle up the incoming IP address, incoming port number, sequence number, and command
            oscargs[0] = InIP;
            oscargs[1] = Integer.parseInt(InPort);
            oscargs[2] = seqNum;
            oscargs[3] = message;

            //Initialize the OSC object that will actually send the OSC packet to HexAtom
            OSCPortOut sender = null;

            //Set the IP address and port of the server to which packets will be sent
            if (OutIP != "" && OutPort != "") {
                InetAddress otherIP = InetAddress.getByName(OutIP);
                try {
                    sender = new OSCPortOut(otherIP, Integer.parseInt(OutPort));
                }catch(SocketException e){
                    e.printStackTrace();
                    Log.e("ServerProxy", "Failed to create new OSCPortOut");
                    return false;
                }
            }else{
                //out ip and port were not yet set!
                Log.e("ServerProxy", "Null OutIP/Port");
                return false;
            }
            //Send the bundled information to HexAtom
            try {
                sender.send(new OSCMessage("/interpret", oscargs));
            }catch(final IOException e){
                e.printStackTrace();
                Log.e("ServerProxy", "Interpret failed for OSCMessage");
                return false;
            }

        }catch (final UnknownHostException ux) {
            ux.printStackTrace();
            Log.e("ServerProxy", "UnknownHostException was thrown.");
            return false;
        }
        this.seqNum++;
        return true;
    }

    public class ServerBinder extends Binder{
        ServerProxy getService(){
            //return an instance of server proxy so clients can
            //call the public methods
            return ServerProxy.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    /** This function gets the IP address of the Android device running this app, using
    /* WIFI services, and then returns the IP address it found. This function code used courtesy
    /* of Krishnaraj Varma (http://www.devlper.com/2010/07/getting-ip-address-of-the-device-in-android/)
    /* and Kevin McDonagh (http://www.androidsnippets.com/obtain-ip-address-of-current-device). */
    public String getLocalIpAddress()
    {
        try {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        } catch (Exception ex) {
            /* problem getting IP address */
        }
        return null;
    }
    public String intToIp(int i) {
        return (( i & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 24 ) & 0xFF ));
    }

}
