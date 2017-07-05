package com.byrjamin.wickedwizard.factories.items;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.items.passives.ChangeColor;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemAce;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemKeenEye;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemIronBody;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemSlimeCoat;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemSquareBuckler;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.byrjamin.wickedwizard.factories.items.passives.damage.Anger;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemLuckyShot;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemMiniCatapult;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemSmoulderingEmber;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemStability;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemElasticity;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemMinorAccelerant;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemSwiftShot;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemTacticalKnitwear;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemHyperTrophy;
import com.byrjamin.wickedwizard.factories.items.passives.health.Medicine;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemForgottenScarab;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemGoldenScarab;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemJadeScarab;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemThreeLeafClover;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemScope;
import com.byrjamin.wickedwizard.factories.items.passives.speed.ItemQuickness;

import java.util.Random;

/**
 * Created by Home on 04/07/2017.
 */

public class ItemStore {



    //TODO the general idea of this class is to create a tagging system for all items
    //TODO by this I mean I'll create an array with Pair<WeightedObject<Item>, Array<Avaliable>>
    //TODO when you get an item you go through the pair array and see if the avaliable is relevant to
    //TODO what you're after. e.g. You need a boss item (it only grabs boss items
    //TODO you then roll for an item and return it. You then remove the

    private enum Avaliable {
        SHOP, BOSS, ITEM
    }

    private Array<Item> itemPool = new Array<Item>();
    private Random random;

    public ItemStore(Random random){
        this.random = random;

        //Accuracy
        itemPool.add(new ItemAce());
        itemPool.add(new ItemKeenEye());


        //Armor
        itemPool.addAll(
                new ItemIronBody(),
                new ItemSlimeCoat(),
                new ItemSquareBuckler(),
                new ItemVitaminC());

        //Damage
        itemPool.addAll(
                new Anger(),
                new ItemLuckyShot(),
                new ItemMiniCatapult(),
                new ItemSmoulderingEmber(),
                new ItemStability());

        //Firerate
        itemPool.addAll(
                new ItemElasticity(),
                new ItemMinorAccelerant(),
                new ItemSwiftShot(),
                new ItemTacticalKnitwear());

        //Health
        itemPool.addAll(new ItemHyperTrophy(), new Medicine());


        //Luck
        itemPool.addAll(
                new ItemForgottenScarab(),
                new ItemGoldenScarab(),
                new ItemJadeScarab(),
                new ItemThreeLeafClover()
        );


        //Range
        itemPool.addAll(new ItemScope());

        //Speed
        itemPool.addAll(new ItemQuickness());

        //Other
        itemPool.add(new ChangeColor());

    }
















}
