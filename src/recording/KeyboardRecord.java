package recording;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.awt.event.KeyEvent;

public class KeyboardRecord implements NativeKeyListener {
    private String filepath;
    private int Conversion = 100000;
    private boolean skip = false;
    private boolean stop = false;

    public KeyboardRecord(String filepath) {
        this.filepath = filepath;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        try {
            System.out.println(e.paramString());
            long time = System.currentTimeMillis();
            long previousTime = RecordMain.getTime();
            RecordMain.setTime(time);
            time = Math.abs(time - previousTime);
            int timeMs = (int) time;
            boolean exitloop = false;
            BufferedWriter w = new BufferedWriter(new FileWriter(filepath, true));
            int keycode = e.getKeyCode();
            String key = NativeKeyEvent.getKeyText(keycode);

            if (key.equals("Alt")) {
                keycode = 18;
            } else if (key.equals("Escape")) {
                skip = true;
            } else if (key.equals("Shift")) {
                keycode = 16;
            }

            if (!skip) {
                for (int i = 0; i < 16 * 16 * 16 * 16 && !exitloop; i++) {
                    if (KeyEvent.getKeyText(i).equals(key)) {
                        keycode = i;
                        exitloop = true;
                    }
                }

                w.write("KeyPress " + keycode + " (" + key + ")");
                w.newLine();
                w.write("Wait " + timeMs);
                w.newLine();
            } else {
                skip = false;
            }

            w.close();
        } catch (IOException iox) {
            System.err.println("Error writing.");
            System.err.println(iox.getMessage());

            System.exit(1);
        }
    }
        public void nativeKeyReleased (NativeKeyEvent e){

            try {
                System.out.println(e.paramString());
                long time = System.currentTimeMillis();
                long prevTime = RecordMain.getTime();
                RecordMain.setTime(time);
                time = Math.abs(time - prevTime);
                int timeMs = (int) (time / Conversion);
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath, true));

            int keycode = e.getKeyCode();
            String key = NativeKeyEvent.getKeyText(keycode);
            boolean leave = false;

            if (key.equals("Shift")) {
                keycode = 16;
            } else if (key.equals("Alt")) {
                keycode = 18;
            } else if (key.equals("Escape")) {
                out.write("Exit");
                out.newLine();
                stop = true;
            }

            if (!stop) {
                for (int i = 0 ; i < 16*16*16*16 && !leave ; i++) {
                    if (KeyEvent.getKeyText(i).equals(key)) {
                        keycode = i;
                        leave = true;
                    }
                }

                out.write("KeyRelease " + keycode + " (" + key + ")");
                out.newLine();
                out.write("Wait " + timeMs);
                out.newLine();
            } else {
                stop = false;
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    System.out.println("Error");
                }
            }

            out.close();
        }
	    catch(IOException iox) {
        System.err.println("Error writing.");
        System.err.println(iox.getMessage());

        System.exit(1);
    }

        }

        @Override
        public void nativeKeyTyped (NativeKeyEvent e){

        }

    }

