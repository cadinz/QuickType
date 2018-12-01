package QuickType;

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
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

            }

            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
                System.out.println(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode( )));
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

            }
        });

    }
}
