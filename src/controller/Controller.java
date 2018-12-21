package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Controller implements Initializable {


    private SystemTray tray = SystemTray.getSystemTray( );
    private PopupMenu popup = new PopupMenu( );


    @FXML
    private TextField TF1, TF2, TF3, TF4;

    @FXML
    private ImageView Img;

    @FXML
    private Label TimerLb, LbSave, LbExit;
    private boolean alt = false;


    private void pressString(String str) {
        String text = str;
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit( ).getSystemClipboard( );
        clipboard.setContents(stringSelection, stringSelection);


        Robot rb = null;
        try {
            rb = new Robot( );
        } catch (AWTException e) {
            e.printStackTrace( );
        }
        rb.keyPress(KeyEvent.VK_META);

        NativeKeyEvent keyEvent = new NativeKeyEvent(NativeKeyEvent.NATIVE_KEY_PRESSED, // Modifiers
                0x00, // Raw Code
                179, NativeKeyEvent.VC_V, NativeKeyEvent.CHAR_UNDEFINED);
        GlobalScreen.postNativeEvent(keyEvent);

        rb.keyRelease(KeyEvent.VK_META);


        NativeKeyEvent keyEvent4 = new NativeKeyEvent(NativeKeyEvent.NATIVE_KEY_RELEASED, // Modifiers
                0x00, // Raw Code
                179, NativeKeyEvent.VC_V, NativeKeyEvent.CHAR_UNDEFINED);
        GlobalScreen.postNativeEvent(keyEvent4);

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace( );
        }
    }

    private Image createImage(String path) {
        URL imageURL = Controller.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, "tray icon")).getImage( );
        }

    }

    private void getTFText() {
        try {

            File file = new File("command.txt");
            if (!file.exists( )) {
                new FileWriter(file);
            }
            FileReader FR = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(FR);
            String line;
            TextField[] TFtp = new TextField[4];
            TFtp[0] = TF1;
            TFtp[1] = TF2;
            TFtp[2] = TF3;
            TFtp[3] = TF4;

            for (int i = 0; (line = bufferedReader.readLine( )) != null && i < TFtp.length; i++) {
                TFtp[i].setText(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    private void initGlobalHook() {
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
                if (e.getKeyCode( ) == NativeKeyEvent.VC_CONTROL) {
                    alt = true;
                }

                if (e.getModifiers( ) == 40962) {
                    switch (e.getKeyCode( )) {
                        case NativeKeyEvent.VC_1:
                            pressString(TF1.getText( ) + "");
                            break;
                        case NativeKeyEvent.VC_2:
                            pressString(TF2.getText( ) + "");
                            break;
                        case NativeKeyEvent.VC_3:
                            pressString(TF3.getText( ) + "");
                            break;
                        case NativeKeyEvent.VC_4:
                            pressString(TF4.getText( ) + "");
                            break;
                    }
                }

                System.out.println(NativeKeyEvent.getKeyText(e.getKeyCode( )));
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (e.getKeyCode( ) == NativeKeyEvent.VC_CONTROL) {
                    alt = false;
                }
            }
        });
    }

    private void initSystemTray() {

        if (SystemTray.isSupported( )) {
            try {
                TrayIcon trayIcon = new TrayIcon(createImage("../resources/QuickTypeIcon.png"));
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(e -> {
                    System.exit(0);
                    Platform.exit( );
                });
                MenuItem menuItem = new MenuItem("Github");
                menuItem.addActionListener(e -> {
                    String url = "https://github.com/cadinz/QuickType";
                    Desktop desktop = Desktop.getDesktop( );
                    try {
                        desktop.browse(new URI(url));
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace( );
                    }
                });

                popup.add(menuItem);
                popup.add(exitItem);
                trayIcon.setPopupMenu(popup);
            } catch (Exception e) {
                System.out.println(getClass( ).getResource("exec"));
            }
        } else {
            //Is Not Supported
        }
    }

    private void initTimer() {
        TimerLb.setAlignment(Pos.CENTER);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000), ae -> {

            String now1 = new SimpleDateFormat("aa hh:mm")
                    .format(Calendar.getInstance( ).getTime( ));

            TimerLb.setText(now1);

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play( );
    }

    private void clickLbExit() {
        LbExit.setOnMousePressed(event -> LbExit.setEffect(new Bloom( )));
        LbExit.setOnMouseReleased(event -> {
            LbExit.setEffect(null);
            new Thread(() -> {
                sleep(100);
                Platform.exit( );
                System.exit(0);
            }).start( );
        });
    }

    private void clickImgBtn() {
        Img.setOnMousePressed(event -> {
            Img.setX(Img.getX( ) + 2);
            Img.setY(Img.getY( ) + 2);
        });
        Img.setOnMouseReleased(event -> {
            Img.setX(Img.getX( ) - 2);
            Img.setY(Img.getY( ) - 2);
            Stage stage = (Stage) Img.getScene( ).getWindow( );
            stage.setIconified(true);
        });
    }

    private void clickLbSave() {

        LbSave.setOnMousePressed(event -> LbSave.setEffect(new Bloom( )));
        LbSave.setOnMouseReleased(event -> {
            LbSave.setEffect(null);
            new Thread(() -> {
                sleep(100);
                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter("command.txt"));
                    out.write(TF1.getText( ) + "\n");
                    out.write(TF2.getText( ) + "\n");
                    out.write(TF3.getText( ) + "\n");
                    out.write(TF4.getText( ) + "\n");
                    out.flush( );
                    out.close( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }

            }).start( );
        });


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getTFText( );
        initGlobalHook( );
        initTimer( );
        clickLbExit( );
        clickLbSave( );
        clickImgBtn( );
        initSystemTray( );


    }

}


