package com.byrjamin.wickedwizard.deck.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.byrjamin.wickedwizard.MainGame;

/**
 * Created by Home on 05/11/2016.
 */
public class SpellManager {

    public int maxStorage;

    public int STORAGE_WIDTH = MainGame.GAME_UNITS;
    public int STORAGE_HEIGHT = MainGame.GAME_UNITS;

    public int STORAGE_Y_POSITIONS = MainGame.GAME_HEIGHT - (MainGame.GAME_UNITS * 2);

    public Card[] cardStored;

    public int[] x_positions;

    public SpellManager(){
        maxStorage = 2;
        cardStored =  new Card[maxStorage];
        genX();
    }

    /**
     *
     * @param maxStorage - How many cards a player can store at once.
     */
    public SpellManager(int maxStorage){
        this.maxStorage = maxStorage;
        cardStored =  new Card[maxStorage];
        genX();
    }

    public void add(Card c){

        for(int i = 0; i < cardStored.length; i++){
            if(cardStored[i] == null){
                cardStored[i] = c;
                break;
            }
        }

    }


    /**
     * Calculates where to draw the squares on the screen.
     */
    public void genX(){

        int inital_x = MainGame.GAME_UNITS * 2;

        x_positions = new int[maxStorage];

        for(int i = 0; i < maxStorage; i++){
            x_positions[i] = inital_x * i;
        }

    }

    /**
     * Draws how many spells are currently stored
     * @param sr
     */
    public void drawSpellSlots(ShapeRenderer sr){

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for(int i = 0; i < maxStorage; i++) {

            if(cardStored[i] != null) {
                drawStoredSpell(sr,i, cardStored[i].getCardType());
            } else {
                drawEmptySlot(sr,i);
            }

        }

        sr.end();


    }

    public void drawStoredSpell(ShapeRenderer sr, int position, Card.CardType type){

        switch(type){
            case ARCANE:
                sr.setColor(Color.MAGENTA);
                break;
            case FIRE:
                sr.setColor(Color.ORANGE);
                break;
            case LIGHTNING:
                sr.setColor(Color.BLUE);
                break;
            case ICE:
                sr.setColor(Color.CYAN);
                break;
            default:
                sr.setColor(Color.BLACK);
                break;
        }
        sr.rect(x_positions[position], STORAGE_Y_POSITIONS, STORAGE_WIDTH, STORAGE_HEIGHT);
    }

    public void drawEmptySlot(ShapeRenderer sr, int position) {

        sr.setColor(Color.WHITE);
        sr.rect(x_positions[position], STORAGE_Y_POSITIONS, STORAGE_WIDTH, STORAGE_HEIGHT);

    }


    public int castSpells(){

        int spelldmg = 0;

        for (Card aCardStored : cardStored) {
            if (aCardStored != null) {
                spelldmg+=aCardStored.getBaseDamage();
            }
        }

        return spelldmg;


    }

    public void resetSpell(){
        cardStored = new Card[maxStorage];
    }
























}
