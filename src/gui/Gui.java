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
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public int percentage;
    public ReadMain present;
    public int taskTime;
    private String filepath = null;
    private static boolean exit = false;




    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            /*
            int looptime = taskSize();
            int currTime = (int) System.currentTimeMillis();
            int counts = 100;
           // System.out.println(currTime);
            int totalTime = currTime + looptime;
            //System.out.println("tot" + totalTime);
            //System.out.println("curr" + currTime);
            for( int i = 0;i < counts; i++){
                setProgress(i);
                System.out.println(i);
            }
            //int totalTime = looptime - currTime;
            System.out.println("loop time is: " + looptime);
            */
            Random random = new Random();
            int progress = 0;
            // Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                // Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {
                }
                // Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
    }


    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        Toolkit.getDefaultToolkit().beep();
        setCursor(null); //turn off the wait cursor
        System.out.println("Done");
        progressBar.setValue(100);
    }

}
    public int taskSize() {

        try {
            filepath = RecordMain.getFilePath();
            System.out.println(filepath);
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String input = in.readLine();
            int time;
            String[] split;

            //player.setAutoWaitForIdle(true);
            while (input != null && !exit) {
                split = input.split(" ");
                //System.err.println("[" + input + "]");
                //System.out.println(split[1]);
                if (split[0].equals("Wait")) {
                    time = Integer.parseInt(split[1]);
                    this.taskTime += time;
                } else if (split[0].equals("Exit")) {
                    exit = true;
                }
                input = in.readLine();
                //System.out.println(input);
            }
            //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            exit = false;
            in.close();
        } catch (IOException iox) {
            System.err.println("Cannot read from " + filepath + ".");
            System.err.println(iox.getMessage());

            System.exit(1);
        }
        System.out.println(taskTime);
        return taskTime;
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
     * @param percentage  This method is used to increment the loading bar during playback.
     */
    public void setPercentage(int percentage) {
        //this.progressBar.setValue(percentage);
        //this.percentage = percentage;
        this.percentage = percentage;
    }



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
            FileManager g = new FileManager();
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
            task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
            ReadMain present = new ReadMain();
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
            System.out.println("progress is: " + progress);
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
