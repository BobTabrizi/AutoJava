package gui;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class FileTest {
    private static long time = 0;
    //public String fileloc = "";
    //TODO  When adding file locations, use this getter to relay the location to main class
    //public String getfileloc(){
    //    return fileloc;
   // }
    public void filemake(){
        try {
            File file = new File("C:\\Users\\Pancake\\IdeaProjects\\MainProg\\src\\Records\\tempScript.txt");
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("New File Created");

            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

}

