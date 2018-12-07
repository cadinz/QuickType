package controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Controller implements Initializable{

    private boolean alt = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LogManager.getLogManager().reset();
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace( );
        }
        GlobalScreen.addNativeKeyListener(new NativeKeyListener( ) {
            @Override
            public void nativeKeyTyped(NativeKeyEvent e) {
            }

            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if(e.getKeyCode() == NativeKeyEvent.VC_META){
                    alt = true;
                }
                if(e.getKeyCode() == NativeKeyEvent.VC_1 && alt){
                    System.out.println("알트 1");
                }
                if(e.getKeyCode() == NativeKeyEvent.VC_2 && alt){
                    System.out.println("알트 2");
                }
                if(e.getKeyCode() == NativeKeyEvent.VC_3 && alt){
                    Platform.exit();
                    System.exit(0);
                }

                System.out.println(NativeKeyEvent.getKeyText(e.getKeyCode( )));
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_META) {
                    alt = false;
                }
            }
        });

    }
}
