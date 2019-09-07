package Main;
import gui.Gui;

import javax.swing.JFrame;
public class LaunchProgram{
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                Gui skeet = new Gui();
                skeet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                skeet.setVisible(true);
        }

    });
}
}
