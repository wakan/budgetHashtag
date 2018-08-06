package fr.budgethashtag.transverse.event.portefeuille;

public class GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent {
    private long idPortefeuille;

    public GetOrCreateDefaultPortefeuilleIfNotExistResponseEvent(long idPortefeuille) {
        this.idPortefeuille = idPortefeuille;
    }

    public long getIdPortefeuille() {
        return idPortefeuille;
    }

    public void setIdPortefeuille(long idPortefeuille) {
        this.idPortefeuille = idPortefeuille;
    }
}
