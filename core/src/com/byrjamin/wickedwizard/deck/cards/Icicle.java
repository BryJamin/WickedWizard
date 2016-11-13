package com.byrjamin.wickedwizard.deck.cards;

import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 13/11/2016.
 */
public class Icicle extends Card {

    public Icicle(int posX, int posY) {
        super(posX, posY, CardType.ICE);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("card_frost"));
        this.setBaseDamage(3);
        this.setProjectileType(ProjectileType.PROJECTILE);
        this.setFireRate(0.1f);
        this.setProjectileSpriteName("frost");
    }

}
