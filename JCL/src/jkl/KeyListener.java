/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Henry
 */
public class KeyListener implements NativeKeyListener {

    static final byte KEY_PRESSED_CODE = 0x02;
    static final byte KEY_RELEASED_CODE = 0x03;
    static final byte KEY_TYPED_CODE = 0x04;
    OutputStream stream;

    public KeyListener(OutputStream os) {
        stream = os;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        byte[] b = {KEY_PRESSED_CODE};

        try {
            stream.write(b);
            stream.write(ByteBuffer.allocate(4).putInt(nke.getKeyCode()).array());

        } catch (IOException ex) {
            Logger.getLogger(KeyListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        byte[] b = {KEY_RELEASED_CODE};

        try {
            stream.write(b);
            stream.write(ByteBuffer.allocate(4).putInt(nke.getKeyCode()).array());

        } catch (IOException ex) {
            Logger.getLogger(KeyListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
        byte[] b = {KEY_TYPED_CODE};

        try {
            stream.write(b);
            stream.write(ByteBuffer.allocate(4).putInt((int) nke.getKeyChar()).array());

        } catch (IOException ex) {
            Logger.getLogger(KeyListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
