import Properties.Read;
import Properties.Write;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class Insert {
    static CSVRead add = new CSVRead();

    public static void main(String[] args) {
        Write prefwrite = new Write();
        Read prefread = new Read();
        prefread.readFile();
        if (prefread.readFile()) {
            System.out.println("DB Location: " + prefread.getDir() + " DB Username: " + prefread.getUser() + " DB Password: " + prefread.getPW());
            Data.DB().Login(prefread.getUser(), prefread.getPW().toCharArray());
            add.chooseStore();
            add.printWarning();
            while (!add.getFile()) {}
            add.readCSV();
        }
        else {
            prefwrite.generateFile();
            System.out.println("db.properties file created in default directory.");
            String message = "Please configure .db.properties file in ";
            message += prefread.getRootPath();
            message += "\nUsername cannot be 'username' and password cannot be 'password'.";
            JOptionPane.showMessageDialog(null, message);
        }
    }




}
