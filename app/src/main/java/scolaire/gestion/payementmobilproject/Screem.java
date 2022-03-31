package scolaire.gestion.payementmobilproject;

public class Screem {
    String title  , id, url_image, description , entete , description_deux;

    public String getDescription_deux() {
        return description_deux;
    }

    public void setDescription_deux(String description_deux) {
        this.description_deux = description_deux;
    }

    public String getEntete() {
        return entete;
    }

    public void setEntete(String entete) {
        this.entete = entete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    int img;

    public Screem() {
    }

    public Screem(String title, String description,String entete , int img,String description_deux) {
        this.title = title;
        this.img = img;
        this.description = description;
        this.entete = entete ;
        this.description_deux = description_deux;
    }



    public Screem(String urlimage) {
        url_image = urlimage;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
