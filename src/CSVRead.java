import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class CSVRead {
    String name = null;
    String fileLocation = null;
    int length = 0;
    String directory = null;
    String extensionType = null;
    int lastSlash;
    int extension;
    File csvFile;
    BufferedReader reader = null;
    String store;

    public CSVRead() {}

    private static Scanner input = new Scanner(System.in);

    public void readCSV() {
        String line = null;

        try {
            reader = new BufferedReader(new FileReader(fileLocation));

            while ((line = reader.readLine()) != null) {
                length++;

                if (length > 1) {
                    String[] fullLine = line.split(",");
                    String firstname = fullLine[4];
                    String lastname = fullLine[3];
                    String phone = fullLine[9];
                    String phone2 = fullLine[12];
                    String email = null;
                    String fullName;
                    if (firstname.length() > 1) {
                        fullName = firstname + " " + lastname;
                    }
                    else {
                        fullName = lastname;
                    }
                    String phoneNumbers;
                    //if (phone2.length() > 2) {
                    //    phoneNumbers = phone + ", " + phone2;
                    //} else {
                        phoneNumbers = phone;
                    //}
                    System.out.println(fullName + " " + phoneNumbers);
                    Data.DB().AddCustomer(store, fullName, email, phoneNumbers);
                }
            }

            System.out.printf("Length of file: %d\n", length - 1);
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

    public void printWarning() {
        System.out.println("Please note, there is no validity checking. All customers, including lines with incorrect "
                + "information, repeat customers, or other errors, will be added to the database.");
    }

    public void chooseStore() {
        int selection;
        Vector<String> stores = Data.DB().GetStores();
        System.out.println("Select a store to add the customer information to");
        for (int i = 0; i < stores.size(); i++) {
            System.out.printf("%d: %s\n", i, stores.get(i));
        }
        System.out.print("Selection: ");
        selection = input.nextInt();
        while (selection < 0 || selection > stores.size() - 1) {
            System.out.println("Please make a valid selection");
            System.out.print("Selection: ");
            selection = input.nextInt();
        }

        System.out.printf("Selected store: %s\n", stores.get(selection));
        store = stores.get(selection);
        input.nextLine();
    }


    /**
     * Get all information for opening a file. Only useful from command line.
     */
    public boolean getFile() {
        System.out.print("Customer file to open: ");
        fileLocation = input.nextLine();
        lastSlash = fileLocation.lastIndexOf("/");
        directory = fileLocation.substring(0, lastSlash);
        extension = fileLocation.lastIndexOf(".");
        extensionType = fileLocation.substring(extension + 1);
        name = fileLocation.substring(lastSlash + 1, extension);

        if (extensionType.compareTo("csv") != 0) {
            System.out.print("Incorrect file type, please select a different file.\n");
            fileReset();
            return false;
        }

        return true;
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
}
