package recording;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;


public class MouseRecord implements NativeMouseInputListener {


    private static int Conversion = 10000;
    private String filepath;

    public MouseRecord(String filepath) {
        this.filepath = filepath;

    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        try {
            //System.out.println(e.paramString());
            long time = System.currentTimeMillis();
            System.out.println(time);
            long previoustime = RecordMain.getTime();
            System.out.println(previoustime);
            RecordMain.setTime(time);
            time = Math.abs(time - previoustime);
            int timeMs = (int) time;
            System.out.println(timeMs);
            BufferedWriter w = new BufferedWriter(new FileWriter(filepath, true));

            int button = e.getButton();

            if (button == 2) {
                button = 3;
            } else if (button == 3) {
                button = 2;
            }
            w.write("MousePress : " + button);
            w.newLine();
            w.write("Delay : " + timeMs);
            w.newLine();
            w.close();
        }
            catch (IOException f){
                System.err.println("Error Occurred During Writing");
                System.err.println(f.getMessage());
                System.exit(1);
            }
        }
        @Override
        public void nativeMouseReleased (NativeMouseEvent e){
            try{
                long time = System.currentTimeMillis();
                long previoustime = RecordMain.getTime();
                RecordMain.setTime(time);
                time = Math.abs(time - previoustime);
                int timeMs = (int) time ;

                BufferedWriter w = new BufferedWriter(new FileWriter(filepath, true));

                int button = e.getButton();

                if (button == 2) {
                    button = 3;
                } else if (button == 3) {
                    button = 2;
                }
                w.write("MouseRelease : " + button);
                w.newLine();
                w.write("Delay : " + timeMs);
                w.newLine();
                w.close();
            }
            catch (IOException f){
                System.err.println("Error Occurred During Writing");
                System.err.println(f.getMessage());
                System.exit(1);
            }
        }

        @Override
        public void nativeMouseMoved (NativeMouseEvent e) {
            try {
                System.out.println(e.paramString());
                long time = System.currentTimeMillis();
                long prevTime = RecordMain.getTime();
                RecordMain.setTime(time);
                time = Math.abs(time - prevTime);
                int timeMs = (int) time;

                BufferedWriter out = new BufferedWriter(new FileWriter(filepath, true));

                int x = e.getX();
                int y = e.getY();
                out.write("Move X: " + x + " Y: " + y);
                out.newLine();
                out.write("Delay : " + timeMs);
                out.newLine();
                out.close();
            } catch (IOException iox) {
                System.err.println("Error writing.");
                System.err.println(iox.getMessage());
                System.exit(1);
            }
        }
        @Override
        public void nativeMouseDragged (NativeMouseEvent e){
            nativeMouseMoved(e);
        }

        }

