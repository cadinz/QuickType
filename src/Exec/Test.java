package Exec;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Test implements NativeKeyListener{

    public static void main(String[] args) {

        LogManager.getLogManager().reset();
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace( );
        }
        GlobalScreen.addNativeKeyListener(new Test());

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        System.out.println(nativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
