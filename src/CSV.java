import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

//This class is responsible for opening a .csv file.
public class CSV
{
    String name = null;
    String fileLocation = null;
    int length = 0;
    String directory = null;
    String extensionType = null;
    int lastSlash;
    int extension;
    File csvFile;
    BufferedReader reader = null;

    //DB database = new DB();


    private static Scanner input = new Scanner(System.in);

    public void getInfo() {
        System.out.println("\tFile name: " + name);
        System.out.printf("\tFile length: %s lines\n", length);
        System.out.println("\tFile extension: " + extensionType);
    }

    //Returns file path name. Use in conjunction with openFile after calling getFile.
    public String getLocation() { return fileLocation; }

    //getFile is preferred over setLocation. Only use if location is a known valid .csv file location.
    public void setLocation(String location)
    {
        fileLocation = location;
    }

    //Returns the length of the file that was read.
    public int getLength() {return length;}

    //Sets all necessary parameters of a file to be able to open it.
    public void getFile() {
        System.out.print("File to open: ");
        fileLocation = input.nextLine();
        lastSlash = fileLocation.lastIndexOf("/");
        directory = fileLocation.substring(0, lastSlash);
        extension = fileLocation.lastIndexOf(".");
        extensionType = fileLocation.substring(extension + 1);
        name = fileLocation.substring(lastSlash + 1, extension);

        if (extensionType.compareTo("csv") != 0) {
            System.out.print("Incorrect file type, please select a different file.\n");
            fileReset();
        }
    }

    //Resets file information if the attempted file was not a .csv file.
    private void fileReset() {
        fileLocation = null;
        lastSlash = 0;
        directory = null;
        extension = 0;
        extensionType = null;
        name = null;
    }

    public void setFileLocation(String directory) {
        this.directory = directory;
    }

    public void setName(String fileName) {
        name = fileName;
    }

    //Opens the provided file and prints all of the information.
    public void openFile(String location) {
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(location));

            while ((line = reader.readLine()) != null) {
                length++;

                String[] fullLine = line.split(",");

                //for (int i = 0; i < fullLine.length; i++) {
                //    System.out.printf("%-5s ", fullLine[i]);
                //}
                if (length > 4) {
                    System.out.printf("Diamond Number: %s\tName: %-60.60s", fullLine[1], fullLine[2]);
                    System.out.println();
                    Data.DB().insertItemTable(fullLine[2], fullLine[1]);
                }
                //System.out.println();

            }
        }

        catch (FileNotFoundException FNF) {
            FNF.printStackTrace();
        }

        catch (IOException IO) {
            IO.printStackTrace();
        }

        finally {
            if (reader != null) {
                try {
                    reader.close();
                }

                catch (IOException IO) {
                    IO.printStackTrace();
                }
            }
        }
    }
}
