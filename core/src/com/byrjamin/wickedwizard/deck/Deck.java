package com.byrjamin.wickedwizard.deck;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.deck.cards.Card;
import com.byrjamin.wickedwizard.deck.cards.Spell;
import com.byrjamin.wickedwizard.deck.cards.Sword;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 10/11/2016.
 */
public class Deck {

    private Vector2[] deckPositions;

    private Card selectedCard;

    private Sprite selectedCardSprite;

    private Array<Card> playerCards;



    public Deck(){

        deckPositions = new Vector2[5];

        deckPositions[0] = new Vector2(300, 200);
        deckPositions[1] = new Vector2(600, 200);
        deckPositions[2] = new Vector2(900, 200);
        deckPositions[3] = new Vector2(1200, 200);
        deckPositions[4] = new Vector2(1500, 200);

        playerCards = new Array<Card>();

        playerCards.add(new Sword(300, 200));
        playerCards.add(new Spell(600, 200));
        playerCards.add(new Sword(900, 200));
        playerCards.add(new Spell(1200, 200));
        playerCards.add(new Sword(1500, 200));

        selectedCard = playerCards.get(0);

        selectedCardSprite = PlayScreen.atlas.createSprite("card_border");
        selectedCardSprite.setSize(MainGame.GAME_UNITS * 11, MainGame.GAME_UNITS * 16);

        setSelectedCardTexturePositions(0);
    }


    public Deck(Array<Card> playerCards){

        deckPositions = new Vector2[5];

        deckPositions[0] = new Vector2(300,50);
        deckPositions[1] = new Vector2(600,50);
        deckPositions[2] = new Vector2(900,50);
        deckPositions[3] = new Vector2(1200,50);
        deckPositions[4] = new Vector2(1500,50);

        playerCards.add(new Sword(300, 200));
        playerCards.add(new Spell(600, 200));
        playerCards.add(new Sword(900, 50));
        playerCards.add(new Spell(1200, 50));
        playerCards.add(new Sword(1500, 50));

    }



    public void update(float dt){

        if(selectedCard != null) {
            System.out.println("Selectedcard is " + selectedCard.getCardType().name());
        }
    }


    public void cardSelect(float posX, float posY){

        int counter = 0;
        for(Card c : playerCards){
            if(c.getSprite().getBoundingRectangle().contains(posX, posY)){
                setSelectedCard(c);
                setSelectedCardTexturePositions(counter);
                System.out.println("Selectedcard is " + selectedCard.getCardType().name());
            }
            counter++;
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

        selectedCardSprite.draw(batch);

    }


    public void setSelectedCardTexturePositions(int selectedCardTexturePositions) {
       // this.selectedCardTexturePositions = selectedCardTexturePositions;

        switch (selectedCardTexturePositions){

            case 0: selectedCardSprite.setCenter(deckPositions[0].x, deckPositions[0].y);
                break;
            case 1: selectedCardSprite.setCenter(deckPositions[1].x, deckPositions[1].y);
                break;
            case 2: selectedCardSprite.setCenter(deckPositions[2].x, deckPositions[2].y);
                break;
            case 3: selectedCardSprite.setCenter(deckPositions[3].x, deckPositions[3].y);
                break;
            case 4: selectedCardSprite.setCenter(deckPositions[4].x, deckPositions[4].y);
                break;

        }

    }
}
