package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Controller implements Initializable {


    private static final SystemTray tray = SystemTray.getSystemTray( );
    private static final PopupMenu popup = new PopupMenu( );
    private static TrayIcon trayIcon;
    @FXML
    private TextField TF1, TF2, TF3, TF4;
    @FXML
    private ImageView ImgBtn;
    @FXML
    private Label TimerLb, LbSave, LbExit;
    private boolean alt = false;

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace( );
        }
    }

    private Image createImage(String path, String description) {
        URL imageURL = Controller.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage( );
        }

    }

    private void initLbExit(){
        LbExit.setOnMousePressed(event -> LbExit.setEffect(new Bloom()));
        LbExit.setOnMouseReleased(event -> {
            LbExit.setEffect(null);
            new Thread(() -> {
                sleep(100);
                System.exit(0);
            }).start();
        });
    }

    private void initMenuItem(){
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        MenuItem menuItem = new MenuItem("Github");
        menuItem.addActionListener(e -> {
            String url = "https://github.com/cadinz/QuickType";
            Desktop desktop = Desktop.getDesktop( );
            try {
                desktop.browse(new URI(url));
            } catch (IOException e1) {
                e1.printStackTrace( );
            } catch (URISyntaxException e1) {
                e1.printStackTrace( );
            }
        });
        popup.add(menuItem);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            // TrayIcon could not be added
        }
    }

    private void initGlobalHook(){
        LogManager.getLogManager( ).reset( );

        Logger.getLogger(GlobalScreen.class.getPackage( ).getName( )).setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook( );
        } catch (NativeHookException e) {
            e.printStackTrace( );
        }

        GlobalScreen.addNativeKeyListener(new NativeKeyListener( ) {
            @Override
            public void nativeKeyTyped(NativeKeyEvent e) {
            }

            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (e.getKeyCode( ) == NativeKeyEvent.VC_META) {
                    alt = true;
                }
                if (e.getKeyCode( ) == NativeKeyEvent.VC_1 && alt) {

                    String text = "Hello World";
                    StringSelection stringSelection = new StringSelection(text);
                    Clipboard clipboard = Toolkit.getDefaultToolkit( ).getSystemClipboard( );
                    clipboard.setContents(stringSelection, stringSelection);


                    Robot robot = null;
                    try {
                        robot = new Robot( );
                    } catch (AWTException e1) {
                        e1.printStackTrace( );
                    }
                    robot.keyPress(KeyEvent.VK_META);
                    robot.keyPress(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_V);
                    robot.keyRelease(KeyEvent.VK_META);
                }
                if (e.getKeyCode( ) == NativeKeyEvent.VC_2 && alt) {
                    System.out.println("알트 2");
                }
                if (e.getKeyCode( ) == NativeKeyEvent.VC_3 && alt) {
                    System.exit(0);
                }

                System.out.println(NativeKeyEvent.getKeyText(e.getKeyCode( )));
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (e.getKeyCode( ) == NativeKeyEvent.VC_META) {
                    alt = false;
                }
            }
        });
    }

    private void initTrayIcon(){

        if (SystemTray.isSupported( )) {
            try {
                trayIcon = new TrayIcon(createImage("../resources/QuickTypeIcon.png", "tray icon"));
                trayIcon.setImageAutoSize(true);
            } catch (Exception e) {
                System.out.println(getClass( ).getResource("exec"));
            }
        }else{
            //Is Not Supported
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initGlobalHook();
        initLbExit();
        initMenuItem();
        initTrayIcon();


    }
}


