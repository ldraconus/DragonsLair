public class Comic {

    private String title;
    private String diamondCode;

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
}
