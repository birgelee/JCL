/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jcl.JCL.encryptionKey;

/**
 *
 * @author Henry
 */
public class JCLServer {
    
    public static Socket clientSocket;
    public static void main(String[] args) throws Exception {
        int portnumber = 80;
        ServerSocket serverSocket = new ServerSocket(portnumber);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JCLServer.waitForExit();
            }
        }).start();
        System.out.println("waiting for client to accept");
        clientSocket = serverSocket.accept();
        InputStream in = clientSocket.getInputStream();
        int type = in.read();
        //System.out.println("type read");
        byte[] br = new byte[4];
        StringBuilder clipboard;
        while (type != -1) {
            
            br[0] = (byte) in.read();//reasde size into a byte array
            br[1] = (byte) in.read();
            br[2] = (byte) in.read();
            br[3] = (byte) in.read();
            
            int length = getInt(br);
            
            clipboard = new StringBuilder();
            for (int i = 0; i < length; i++) {
                clipboard.append((char) decrypt((byte) in.read()));
            }
            switch ((byte) type) {
                case JCL.CLIPBOARD_DATA:
                    System.out.println("The clipboard sent was: " + clipboard.toString());
                    break;

            }
            type = in.read();
            //System.out.println("next type processed and was: " + type);
        }

    }

    public static void waitForExit() {
        Scanner usrin = new Scanner(System.in);
        usrin.next();
        try {
            System.out.println("terminating client and server");
            clientSocket.getOutputStream().write(JCL.TERMINATE_SESSION);
            Thread.sleep(100);
        } catch (IOException ex) {
            Logger.getLogger(JCLServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JCLServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }
    
    static int getInt(byte[] rno) {
        int i = (rno[0] << 24) & 0xff000000
                | (rno[1] << 16) & 0x00ff0000
                | (rno[2] << 8) & 0x0000ff00
                | (rno[3] << 0) & 0x000000ff;
        return i;
    }
    
    private static int encryptionIndex = 0;
    public static byte decrypt(byte b) {
        encryptionIndex = (encryptionIndex + 1) % encryptionKey.length;
        return (byte) (b ^ encryptionKey[encryptionIndex]);
    }
    
    public static byte[] decrypt(byte[] barr) {
        byte[] result = new byte[barr.length];
         for (int i = 0; i < barr.length; i++) {
             result[i] = decrypt(barr[i]);
         }
         return result;
    }
}
