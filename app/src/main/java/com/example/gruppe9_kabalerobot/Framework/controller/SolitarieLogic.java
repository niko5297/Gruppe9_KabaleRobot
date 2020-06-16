package com.example.gruppe9_kabalerobot.Framework.controller;

import com.example.gruppe9_kabalerobot.Framework.model.Card;
import com.example.gruppe9_kabalerobot.Framework.model.Foundation;
import com.example.gruppe9_kabalerobot.Framework.model.Tableau;
import com.example.gruppe9_kabalerobot.Framework.model.Waste;

import java.util.ArrayList;
import java.util.List;

public class SolitarieLogic {
    Waste waste;
    Tableau[] tableau;
    Foundation[] foundation;

    /**
     * Constructor for the controller
     */
    public SolitarieLogic() {
        waste = new Waste(false, null);
        tableau = new Tableau[7];
        for(int i = 0 ; i < 7 ; i++){
            tableau[i] = new Tableau(0, null);
        }
        foundation = new Foundation[4];
    }

    /**
     * A printet version of the current game state
     */
    public String getGameState() {
        //Waste
        String wasteNfoundation, shownWaste = "";
//        for(Card c : waste.getKnownCards()) { shownWaste += c.shortString() + "|"; }
        if(waste.getKnownCards().size() != 0) {
            shownWaste += waste.getKnownCards().get(waste.getKnownCards().size() - 1).shortString();
        } else shownWaste += "Emp ";
        //Foundation
        String foundationString = "";
        for(int i = 0 ; i < foundation.length ; i++){
            foundationString += foundation[i].countCards() > 0 ? foundation[i].peekCard().shortString()+ " " : "Emp ";
        }
        wasteNfoundation = waste.isWastePilePresent() ? "W" : "Emp";
        wasteNfoundation += "|"+ shownWaste + "     " + foundationString;
        System.out.println(wasteNfoundation);
        //Tableau
        String tableauLengths = "";
        String tableauValues = "";
        for(int i = 0 ; i < tableau.length ; i++) {
            tableauLengths += " " + tableau[i].countHiddenCards() + "  ";
            tableauValues += tableau[i].isEmpty() ? "Emp " : tableau[i].getTopCard().shortString() + " " ;
        }

        return (tableauLengths + "\n" + tableauValues);
    }

    public Tableau[] getTableau() { return tableau; }
    public void setTableaus(List<Integer> hiddenCards, List<List<Card>> transTableaus) {
        for (int i = 0 ; i < 7 ; i++) {
            if(!transTableaus.isEmpty())
                tableau[i] = new Tableau(hiddenCards.get(i), transTableaus.get(i));
        }
    }

    public Foundation[] getFoundation() { return foundation; }
    public void setFoundations(List<Card> cards) {
        for(int i = 0 ; i < 4 ; i++) {
            if(!cards.isEmpty())
                foundation[i] = new Foundation(cards.get(i));
        }
    }

    public Waste getWaste() { return waste; }
    public void setWaste(Waste waste) {
        this.waste = waste;
    }

    /**
     * Used for tests, to easily set the tableau
     * @param foundation
     */
    public void setFoundation(Foundation[] foundation){
        this.foundation = foundation;
    }
}