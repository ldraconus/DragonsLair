public class Comic {

    private String title;
    private String diamondCode;
    private String issue = "";
    private String graphicNovel = "";
    private String nonBook = "";
    private String csvDate = "";

    public Comic(String t, String dc){
        setTitle(t);
        setDiamondCode(dc);
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public String getDiamondCode(){
        return diamondCode;
    }

    public void setDiamondCode(String newDiamondCode){
        this.diamondCode = newDiamondCode;
    }

    public String getIssue(){
        return issue;
    }

    public void setIssue(String newIssue) { this.issue = newIssue; }

    public String getGraphicNovel() { return graphicNovel; }

    public void setGraphicNovel(String newGraphicNovel) { this.graphicNovel = newGraphicNovel; }

    public String getNonBook() { return nonBook; }

    public void setNonBook(String newNonBook) { this.nonBook = newNonBook; }

    public String getCsvDate() { return csvDate; }

    public void setCsvDate(String newCsvDate) { this.csvDate = newCsvDate; }

    public void ToString(){
        System.out.printf("Title: %s\nDiamond Code: %s\nIssue: %s\nGraphical Novel: %s\nNon Book: %s\nCsv Date: %s\n",
                this.title, this.diamondCode, this.issue, this.graphicNovel, this.nonBook, this.csvDate);
    }
}
