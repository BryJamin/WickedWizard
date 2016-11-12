package com.byrjamin.wickedwizard.deck;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.deck.cards.Card;
import com.byrjamin.wickedwizard.deck.cards.Spell;
import com.byrjamin.wickedwizard.deck.cards.Sword;

/**
 * Created by Home on 10/11/2016.
 */
public class Deck {

    private Vector2[] deckPositions;

    private Card selectedCard;

    private Array<Card> playerCards;



    public Deck(){

        deckPositions = new Vector2[5];

        deckPositions[0] = new Vector2(300,0);
        deckPositions[1] = new Vector2(600,0);
        deckPositions[2] = new Vector2(900,0);
        deckPositions[3] = new Vector2(1200,0);
        deckPositions[4] = new Vector2(1500,0);

        playerCards = new Array<Card>();

        playerCards.add(new Sword(300, 0));
        playerCards.add(new Spell(600, 0));
        playerCards.add(new Sword(900, 0));
        playerCards.add(new Spell(1200, 0));
        playerCards.add(new Sword(1500, 0));
    }


    public Deck(Array<Card> playerCards){

        deckPositions = new Vector2[5];

        deckPositions[0] = new Vector2(300,0);
        deckPositions[1] = new Vector2(600,0);
        deckPositions[2] = new Vector2(900,0);
        deckPositions[3] = new Vector2(1200,0);
        deckPositions[4] = new Vector2(1500,0);

        playerCards.add(new Sword(300, 0));
        playerCards.add(new Spell(600, 0));
        playerCards.add(new Sword(900, 0));
        playerCards.add(new Spell(1200, 0));
        playerCards.add(new Sword(1500, 0));

    }



    public void update(float dt){

        if(selectedCard != null) {
            System.out.println("Selectedcard is " + selectedCard.getCardType().name());
        }
    }


    public void cardSelect(float posX, float posY){
        for(Card c : playerCards){
            if(c.getSprite().getBoundingRectangle().contains(posX, posY)){
                setSelectedCard(c);
                System.out.println("Selectedcard is " + selectedCard.getCardType().name());
            }
        }
    }


    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void draw(SpriteBatch batch){

        for(Card c : playerCards){
            c.draw(batch);
        }

    }




}
