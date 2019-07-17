package gui;
import recording.RecordMain;
import playback.ReadMain;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.nio.file.*;
import java.io.IOException;
import java.beans.*;
import java.util.Random;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;
// Get the logger for "org.jnativehook" and set the level to warning.

//TODO Add Progress Bar at the bottom of the window.
public class Gui extends JFrame implements WindowListener, ActionListener, PropertyChangeListener {

    private JMenuItem menuItemNew, menuItemQuit, menuItemSave, menuItemSaveAs, menuItemOpen, menuItemInfo;
    private JButton Start;
    private JButton Stop;
    private JButton Play;
    public JProgressBar progressBar;
    JFileChooser fc;
    public Timer t;
    public final static int interval = 1000;
    private Task task;
    private static final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());


    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += 10;
                setProgress(progress);
            }
            return null;
        }
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            setCursor(null); //turn off the wait cursor
            System.out.println("Done");
            progressBar.setValue(0);
        }
    }

    public Gui() {
        super("Dat Bot");
        setLayout(new FlowLayout());
        setSize(275, 160);
        addWindowListener(this);
        logger.setLevel(Level.WARNING);

// Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
        setJMenuBar(menuBar);
        menuItemNew = new JMenuItem("New");
        menuFile.add(menuItemNew);
        menuItemNew.addActionListener(this);
        menuItemOpen = new JMenuItem("Open");
        menuFile.add(menuItemOpen);
        menuItemOpen.addActionListener(this);
        menuItemSave = new JMenuItem("Save");
        menuFile.add(menuItemSave);
        menuItemSaveAs = new JMenuItem("Save As..");
        menuFile.add(menuItemSaveAs);
        menuItemSaveAs.addActionListener(this);
        menuFile.addSeparator();
        menuItemQuit = new JMenuItem("Exit");
        menuFile.add(menuItemQuit);
        menuItemQuit.addActionListener(this);

        JMenu menuExtra = new JMenu("Extra");
        menuBar.add(menuExtra);
        menuItemInfo = new JMenuItem("Info");
        menuExtra.add(menuItemInfo);


        Start = new JButton("Start Recording");
        add(Start);
        Stop = new JButton("Stop Recording");
        add(Stop);
        Play = new JButton("Play Recording");
        add(Play);
        Start.addActionListener(this);
        Stop.addActionListener(this);
        Play.addActionListener(this);
        Play.setEnabled(false);

        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        JPanel p = new JPanel(new BorderLayout());
        p.add(progressBar);
        add(p,BorderLayout.CENTER);

    }

    //Icon st = new ImageIcon(getClass().getResource("st.png"));
    //Icon sp = new ImageIcon(getClass().getResource("sp.png"));

    /**
     * @param event This method is used to determine an action when a menu option is selected.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == menuItemQuit) {
            this.dispose();
        }
        if(event.getSource() == menuItemNew){
            try{
                Files.deleteIfExists(Paths.get("C:\\Users\\Pancake\\IdeaProjects\\MainProg\\src\\Records\\tempScript.txt"));
            }
            catch(NoSuchFileException a) {
                System.out.println("No such file/directory exists");
            }
            catch(DirectoryNotEmptyException a) {
                System.out.println("Directory is not empty.");
            }
            catch(IOException a) {
                System.out.println("Invalid permissions.");
            }

            System.out.println("Successfully deleted temp file");
            Play.setEnabled(false);
        }
        if(event.getSource() == menuItemSaveAs){
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.showSaveDialog(this);
        }
        if(event.getSource() == menuItemOpen){
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.showOpenDialog(this);
        }
        if (event.getSource() == Start) {
            System.out.println("Start Recording!");
            FileTest g = new FileTest();
            g.filemake();
            RecordMain rec = new RecordMain();
            rec.record();
            Play.setEnabled(true);
        }
        if (event.getSource() == Stop) {
            System.out.println("Stop Recording!");
            RecordMain finish = new RecordMain();
            finish.stopRecord();
            stopTime();
        }
        if (event.getSource() == Play) {
            System.out.println("Play Recording!");
            Play.setEnabled(false);
            ReadMain present = new ReadMain();
            task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
            present.play();
            Play.setEnabled(true);
        }
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        System.out.println(e.getX());
        System.out.println(e.getY());
        System.out.println("ZOINKS");
        //System.out.println(e.paramString());
    }

    private void recordTime() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }

    }

    private void stopTime() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();
        }

    }

    public void nativeKeyPressed(NativeKeyEvent e) {
    }

    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public void nativeMouseClicked(NativeMouseEvent e) {

    }

    public void nativeMousePressed(NativeMouseEvent e) {
    }

    public void nativeMouseReleased(NativeMouseEvent e) {

    }

    public void nativeMouseDragged(NativeMouseEvent e) {
    }

    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
    }

    //YIKES
    public void windowActivated(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        try{
        Files.deleteIfExists(Paths.get("C:\\Users\\Pancake\\IdeaProjects\\MainProg\\src\\Records\\tempScript.txt"));
    }
        catch(NoSuchFileException a) {
        System.out.println("No such file/directory exists");
    }
        catch(DirectoryNotEmptyException a) {
        System.out.println("Directory is not empty.");
    }
        catch(IOException a) {
        System.out.println("Invalid permissions.");
    }
        System.out.println("Successfully deleted temp file");

}

public void windowDeactivated(WindowEvent e) { }

public void windowDeiconified(WindowEvent e) { }

public void windowIconified(WindowEvent e) { }

public void windowOpened(WindowEvent e) {

}
public void windowClosed(WindowEvent e) {
        try {
            GlobalScreen.unregisterNativeHook();
        }
        catch(NativeHookException ex) {
        ex.printStackTrace();
        }
        System.runFinalization();
        System.exit(0);
        }

}
