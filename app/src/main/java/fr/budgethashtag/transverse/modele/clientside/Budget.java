package fr.budgethashtag.transverse.modele.clientside;


public class Budget {
    private String libelle;
    private String color;
    private Float previsionnel;

    private Portefeuille portefeuille;


    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getPrevisionnel() {
        return previsionnel;
    }

    public void setPrevisionnel(Float previsionnel) {
        this.previsionnel = previsionnel;
    }

    public Portefeuille getPortefeuille() {
        return portefeuille;
    }

    public void setPortefeuille(Portefeuille portefeuille) {
        this.portefeuille = portefeuille;
    }
}
