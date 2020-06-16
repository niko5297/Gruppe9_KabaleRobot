package com.example.gruppe9_kabalerobot.Framework.controller;

import com.example.gruppe9_kabalerobot.CardPlacement.CardObj;
import com.example.gruppe9_kabalerobot.CardPlacement.CardPlacement;
import com.example.gruppe9_kabalerobot.Framework.controller.SolitarieLogic;
import com.example.gruppe9_kabalerobot.Framework.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardTranslator {
    private CardPlacement placement;

    public CardTranslator(CardPlacement placement) {
        this.placement = placement;
    }

    public void insertCards(SolitarieLogic game) {
        //Waste
        game.getWaste().addListToKnown(translateCardList(placement.getWaste()));
        //Foundation
        game.setFoundations(translateCardList(placement.getFoundations()));
        //Tableau
        List<List<Card>> transTableaus = new ArrayList<>();
        for(List<CardObj> cardObj : placement.getTableaus()) {
            transTableaus.add(translateCardList(cardObj));
        }
        game.setTableaus(placement.getHiddenCards(), transTableaus);
    }

    private List<Card> translateCardList(List<CardObj> cardObjs) {
        List<Card> cards = new ArrayList<>();
        for(CardObj cardObj : cardObjs) {
            Card card = new Card(cardObj.getSuit(), cardObj.getValue());
            cards.add(card);
        }
        return cards;
    }
}