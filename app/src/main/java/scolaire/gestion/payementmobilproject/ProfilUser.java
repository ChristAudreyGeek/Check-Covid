package scolaire.gestion.payementmobilproject;

public class ProfilUser {


    String id , uid , name,img_url , centre , ville , timer , nembre , adress , numero_phone , url_img , age , genre;



    public ProfilUser(String id, String uid, String img_url) {
        this.id = id;
        this.uid = uid;
        this.img_url = img_url;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public ProfilUser() {
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getNembre() {
        return nembre;
    }

    public void setNembre(String nembre) {
        this.nembre = nembre;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNumero_phone() {
        return numero_phone;
    }

    public void setNumero_phone(String numero_phone) {
        this.numero_phone = numero_phone;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getName() {
        return name;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
