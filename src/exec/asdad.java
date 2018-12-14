package exec;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class asdad {
    private static final SystemTray tray = SystemTray.getSystemTray();
    private static final PopupMenu popup = new PopupMenu();
    private static TrayIcon trayIcon;

    public static void main(String[] args) {

        if (!SystemTray.isSupported()) {
            // SystemTray is not supported
        }

        trayIcon = new TrayIcon(createImage("aaa.jpg", "tray icon"));
        trayIcon.setImageAutoSize(true);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem) e.getSource();

                String s = (String) JOptionPane.showInputDialog(null, "Report "
                                + item.getLabel(), "Create Report",
                        JOptionPane.PLAIN_MESSAGE, null, null, "");

                // Do something with the string...
            }
        };
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });


        Menu reportMenu = new Menu("Report");
        MenuItem menuItem = new MenuItem("Item");
        reportMenu.add(menuItem);
        menuItem.addActionListener(listener);
        popup.add(reportMenu);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            // TrayIcon could not be added
        }

    }

    // Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = asdad.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}