package com.byrjamin.wickedwizard.deck;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.deck.cards.fire.FireBlast;
import com.byrjamin.wickedwizard.deck.cards.fire.Molotov;
import com.byrjamin.wickedwizard.deck.cards.Card;
import com.byrjamin.wickedwizard.deck.cards.Icicle;
import com.byrjamin.wickedwizard.deck.cards.fire.Fireball;
import com.byrjamin.wickedwizard.deck.cards.Sword;
import com.byrjamin.wickedwizard.deck.cards.Teleport;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 10/11/2016.
 */
public class Deck {

    private Vector2[] deckPositions;

    private Card selectedCard;

    private Sprite selectedCardSprite;

    private Array<Card> playerCards;


    /**
     * Default Constructor for the Deck Class
     *
     * Currently, being used as a way to test different spells.
     *
     * In future the player will most likley have a loadout and the constructor will take
     * that load out.
     *
     *
     */
    public Deck(){

        deckPositions = new Vector2[5];

        deckPositions[0] = new Vector2(300, 200);
        deckPositions[1] = new Vector2(600, 200);
        deckPositions[2] = new Vector2(900, 200);
        deckPositions[3] = new Vector2(1200, 200);
        deckPositions[4] = new Vector2(1500, 200);

        playerCards = new Array<Card>();

        playerCards.add(new FireBlast(300, 200));
        playerCards.add(new Fireball(600, 200));
        playerCards.add(new Icicle(900, 200));
        playerCards.add(new Molotov(1200, 200));
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

        playerCards.add(new Teleport(300, 200));
        playerCards.add(new Fireball(600, 200));
        playerCards.add(new Icicle(900, 50));
        playerCards.add(new Fireball(1200, 50));
        playerCards.add(new Sword(1500, 50));

    }


    /**
     * Runs the update of the selected Card class, As currently the deck doesn't have any
     * animations for anything to update accross the board.
     * @param dt
     */
    public void update(float dt){
        selectedCard.update(dt);
    }


    /**
     * Uses the player input to see which card has been selected.
     * When a card has been selected it updates the selected Card and
     * the selected card Texture.
     * @param posX - Input X
     * @param posY - Input y
     */
    public void cardSelect(float posX, float posY){
        int counter = 0;
        for(Card c : playerCards){
            if(c.getSprite().getBoundingRectangle().contains(posX, posY)){
                setSelectedCard(c);
                setSelectedCardTexturePositions(counter);
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


    /**
     * //TODO Probably needs a major refactor
     * uses the Position Vectos to move the Center of the selected Card Texture.
     * @param selectedCardTexturePositions -
     */
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
