/**
 * Comic class is responsible for maintaining information about a single comic.
 */
public class Comic {

    private String title;
    private String diamondCode;
    private String issue = "";
    private String graphicNovel = "";
    private String nonBook = "";
    private String csvDate = "";

    /**
     * Constructor, sets initial values.
     * @param t Title of the comic.
     * @param dc Diamond Code of the comic.
     */
    public Comic(String t, String dc){
        setTitle(t);
        setDiamondCode(dc);
    }

    /**
     * Gets title of the comic.
     * @return Title.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Sets the title of the comic.
     * @param newTitle New title to set for the comic.
     */
    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    /**
     * Get the diamond code of the comic.
     * @return Comics diamond code.
     */
    public String getDiamondCode(){
        return diamondCode;
    }

    /**
     * Sets the comics diamond code.
     * @param newDiamondCode New diamond code for the comic.
     */
    public void setDiamondCode(String newDiamondCode){
        this.diamondCode = newDiamondCode;
    }

    /**
     * Gets the issue number of the comic.
     * @return Comic issue number.
     */
    public String getIssue(){
        return issue;
    }

    /**
     * Sets the issue number of the comic.
     * @param newIssue Issue number of the comic.
     */
    public void setIssue(String newIssue) { this.issue = newIssue; }

    /**
     * Gets graphic novel status.
     * @return Graphic novel string.
     */
    public String getGraphicNovel() { return graphicNovel; }

    /**
     * Sets the graphic novel status.
     * @param newGraphicNovel String to set if graphic novel. Should only be 0 or 1.
     */
    public void setGraphicNovel(String newGraphicNovel) { this.graphicNovel = newGraphicNovel; }

    /**
     * Get if the item is a non book.
     * @return Value of nonbook. 1 if nonbook.
     */
    public String getNonBook() { return nonBook; }

    /**
     * Sets nonbook status of item.
     * @param newNonBook Value for nonbook. 1 if nonbook, 0 otherwise.
     */
    public void setNonBook(String newNonBook) { this.nonBook = newNonBook; }

    /**
     * Gets the csv date of the item.
     * @return CSV Date of the item.
     */
    public String getCsvDate() { return csvDate; }

    /**
     * Sets the csv date of the item.
     * @param newCsvDate Corresponding csvdate for this item.
     */
    public void setCsvDate(String newCsvDate) { this.csvDate = newCsvDate; }

    /**
     * To string for this object. Prints to console.
     */
    public void ToString(){
        System.out.printf("Title: %s\nDiamond Code: %s\nIssue: %s\nGraphical Novel: %s\nNon Book: %s\nCsv Date: %s\n",
                this.title, this.diamondCode, this.issue, this.graphicNovel, this.nonBook, this.csvDate);
    }
}
