package recording;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;


public class RecordMain {
    private static long time = 0;
    ///public static String root = System.getProperty("user.dir") + "\\";
    public static String file = "C:\\Users\\Pancake\\IdeaProjects\\MainProg\\src\\Records\\tempScript.txt";

    public static String getFilePath() {
        return file;
    }
    public static void setTime(long time) {

        RecordMain.time = time;
    }

    public static long getTime() {

        return RecordMain.time;
    }

    //TODO Fill this out, hook and unhook global listeners as well. FINISH KEYBOARD RECORD FIRST
    public void record() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException nhx) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(nhx.getMessage());

            System.exit(1);
        }

        time = System.currentTimeMillis();
        KeyboardRecord keyListener = new KeyboardRecord(file);
        MouseRecord mouseListener = new MouseRecord(file);
        MouseRecord mouseMotionListener = new MouseRecord(file);
        GlobalScreen.addNativeKeyListener(keyListener);
        GlobalScreen.addNativeMouseListener(mouseListener);
        GlobalScreen.addNativeMouseMotionListener(mouseMotionListener);

        while (GlobalScreen.isNativeHookRegistered()) {

        }

        // Need to remove the listeners that were added...
        GlobalScreen.removeNativeKeyListener(keyListener);
        GlobalScreen.removeNativeMouseListener(mouseListener);
        GlobalScreen.removeNativeMouseMotionListener(mouseMotionListener);


    }

    public void stopRecord() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();
        }
    }
}