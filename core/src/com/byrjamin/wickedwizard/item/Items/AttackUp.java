package com.byrjamin.wickedwizard.item.Items;

import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.gameobject.player.Wizard;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 07/01/2017.
 */
public class AttackUp extends Item {


    public AttackUp() {
        super(PlayScreen.atlas.findRegion("fire"));
    }

    @Override
    public void use(Wizard wizard) {
        wizard.increaseDamage(1);
        destroy();
    }
}
