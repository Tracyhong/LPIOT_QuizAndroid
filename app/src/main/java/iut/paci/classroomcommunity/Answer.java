package iut.paci.classroomcommunity;

public class Answer {
    private String texte;
    private boolean isReponse;

    public Answer(String texte, boolean isReponse) {
        this.texte = texte;
        this.isReponse = isReponse;
    }

    public String getTexte() {
        return texte;
    }

    public boolean getIsReponse() {
        return isReponse;
    }
}
