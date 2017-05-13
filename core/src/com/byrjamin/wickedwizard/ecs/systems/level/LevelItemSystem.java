package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.ChangeColor;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemSlimeCoat;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemSwiftShot;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemKeenEye;
import com.byrjamin.wickedwizard.factories.items.passives.damage.Anger;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemHyperTrophy;
import com.byrjamin.wickedwizard.factories.items.passives.health.Medicine;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemScope;

/**
 * Created by Home on 13/05/2017.
 */

public class LevelItemSystem extends BaseSystem {


    private Array<Item> avaliableItemList = new Array<Item>();


    public LevelItemSystem(){

        //Accuracy
        avaliableItemList.add(new ItemKeenEye());

        //Armor
        avaliableItemList.addAll(new ItemSlimeCoat());

        //Damage
        avaliableItemList.add(new Anger());

        //Firerate
        avaliableItemList.addAll(new ItemSwiftShot());

        //Health
        avaliableItemList.addAll(new ItemHyperTrophy(), new Medicine());

        //Range
        avaliableItemList.addAll(new ItemScope());

        //Other
        avaliableItemList.add(new ChangeColor());
        avaliableItemList.add(new ItemSwiftShot());

    }

    @Override
    protected void processSystem() {

    }

    public Array<Item> getAvaliableItemList() {
        return avaliableItemList;
    }
}
