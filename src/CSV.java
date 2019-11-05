import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

/**
 * This class is responsible for opening a .csv file and adding to the database.
 */
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


    private static Scanner input = new Scanner(System.in);

    /**
     * getInfo prints the file name, number of lines in the file, and file extension to the console.
     */
    public void getInfo() {
        System.out.println("\tFile name: " + name);
        System.out.printf("\tFile length: %s lines\n", length);
        System.out.println("\tFile extension: " + extensionType);
    }


    /**
     * Used to get the location of the file.
     * @return file path name.
     */
    public String getLocation() { return fileLocation; }


    /**
     * setLocation - Set the location of the file to read.
     * @param location location to set as file location. Does not do validity checking.
     */
    public void setLocation(String location)
    {
        fileLocation = location;
    }


    /**
     * getLength of the file.
     * @return total file length in number of lines.
     */
    public int getLength() {return length;}


    /**
     * Get all information for opening a file. Only useful from command line.
     */
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

    /**
     * Reset all file information to default.
     */
    private void fileReset() {
        fileLocation = null;
        lastSlash = 0;
        directory = null;
        extension = 0;
        extensionType = null;
        name = null;
    }

    /**
     * Set the file directory to read from. Does not do validity checking.
     * @param directory directory file is located in.
     */
    public void setFileLocation(String directory) {
        this.directory = directory;
    }

    /**
     * Set the file name to read from. Does not do validity checking.
     * @param fileName Name of the file to read.
     */
    public void setName(String fileName) {
        name = fileName;
    }

    /**
     * Opens a file and add items to the database. Prints information to the command line.
     * @param location File location to be read.
     */
    public void openFile(String location) {
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(location));

            while ((line = reader.readLine()) != null) {
                length++;

                String[] fullLine = line.split(",");

                if (length > 4) {
                    System.out.printf("Diamond Number: '%s'\tName: %-60.60s", fullLine[1], fullLine[2]);
                    System.out.println();
                    Data.DB().insertItemTable(fullLine[2], fullLine[1], Data.Store());
                }


            }
            System.out.printf("Length of file: %d\n", length - 4);
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
