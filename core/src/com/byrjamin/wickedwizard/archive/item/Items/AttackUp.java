package com.byrjamin.wickedwizard.archive.item.Items;

import com.byrjamin.wickedwizard.archive.gameobject.player.Wizard;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 07/01/2017.
 */
public class AttackUp extends com.byrjamin.wickedwizard.archive.item.Item {


    public AttackUp() {
        super(PlayScreen.atlas.findRegion("fire"));
    }

    @Override
    public void use(Wizard wizard) {
        wizard.increaseDamage(1);
        destroy();
    }
}
