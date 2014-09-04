/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Henry
 */
public class JKLTestServer {

    public static void main(String[] args) throws Exception {
        int portnumber = 99;
        ServerSocket serverSocket = new ServerSocket(portnumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out
                = new PrintWriter(clientSocket.getOutputStream(), true);
        InputStream in = clientSocket.getInputStream();
        int type = in.read();
        byte[] br = new byte[4];
        while (type != -1) {
            br[0] = (byte) in.read();
            br[1] = (byte) in.read();
            br[2] = (byte) in.read();
            br[3] = (byte) in.read();
            int keycode = getInt(br);
            switch ((byte) type) {
                case KeyListener.KEY_PRESSED_CODE:
                    System.out.println("The user pressed the key: " + ((char) keycode));
                    break;
                case KeyListener.KEY_RELEASED_CODE:
                    System.out.println("The user released the key: " + ((char) keycode));
                    break;
                case KeyListener.KEY_TYPED_CODE:
                    System.out.println("The user typed the key: " + ((char) keycode));
                    break;

            }
            type = in.read();
        }

    }

    static int getInt(byte[] rno) {
        int i = (rno[0] << 24) & 0xff000000
                | (rno[1] << 16) & 0x00ff0000
                | (rno[2] << 8) & 0x0000ff00
                | (rno[3] << 0) & 0x000000ff;
        return i;
    }
}
