package com.byrjamin.wickedwizard.deck.cards.fire;

import com.byrjamin.wickedwizard.deck.cards.Card;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 30/10/2016.
 */
public class Fireball extends Card {

    public Fireball(int posX, int posY) {
        super(posX, posY, CardType.FIRE);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("card_spell"));
        this.setBaseDamage(3);
        this.setProjectileType(ProjectileType.PROJECTILE);
        this.setProjectileSpriteName("fire");
    }
}
