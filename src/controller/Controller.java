package controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Controller implements Initializable {


    @FXML
    private TextField TF1,TF2,TF3,TF4;
    @FXML
    private ImageView ImgBtn;
    @FXML
    private Label TimerLb,LbSave,LbExit;


    private static final SystemTray tray = SystemTray.getSystemTray( );
    private static final PopupMenu popup = new PopupMenu( );
    private static TrayIcon trayIcon;
    private boolean alt = false;





    protected static Image createImage(String path, String description) {
        URL imageURL = Controller.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage( );
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LbExit.setOnMouseClicked(new EventHandler<MouseEvent>( ) {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });

        if (!SystemTray.isSupported( )) {
            // SystemTray is not supported
        }
        try {
            trayIcon = new TrayIcon(createImage("../resources/QuickTypeIcon.png", "tray icon"));
            trayIcon.setImageAutoSize(true);
        }catch (Exception e){
            System.out.println(getClass( ).getResource("exec"));
        }

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener( ) {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        MenuItem menuItem = new MenuItem("Github");

        menuItem.addActionListener(new ActionListener( ) {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "https://github.com/cadinz/QuickType";
                Desktop desktop = Desktop.getDesktop( );
                try {
                    desktop.browse(new URI(url));
                } catch (IOException e1) {
                    e1.printStackTrace( );
                } catch (URISyntaxException e1) {
                    e1.printStackTrace( );
                }
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


        // Obtain the image URL


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
}


