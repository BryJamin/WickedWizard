package com.byrjamin.wickedwizard.deck.cards;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 30/10/2016.
 */
public class Spell extends Card {


    public Spell(int posX, int posY, CardType ct) {
        super(posX, posY, ct);
        Sprite sprite = PlayScreen.atlas.createSprite("card_spell");
        sprite.setSize(MainGame.GAME_UNITS * 10, MainGame.GAME_UNITS * 15);
        sprite.setPosition(posX, posY);
        this.setSprite(sprite);
        this.setBaseDamage(3);
        this.setCardType(CardType.FIRE);

    }
}
