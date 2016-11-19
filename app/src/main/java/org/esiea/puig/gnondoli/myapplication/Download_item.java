package org.esiea.puig.gnondoli.myapplication;



public class Download_item {


    private int image_dwn;
    private String Title;
    private String Description;


    public Download_item(int image_dwn, String Title, String Description){


        this.image_dwn = image_dwn;
        this.Title = Title;
        this.Description = Description;
    }


    public int getImage_dwn(){return this.image_dwn;}
    public String getTitle(){return this.Title;}
    public String getDescription(){return this.Description;}


    public void setImage_dwn(int image_dwn){
        this.image_dwn = image_dwn;
    }


    public void setTitle(String Title){
        this.Title = Title;
    }


    public void setDescription(String Description){
        this.Description = Description;
    }
}




