package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by BB on 16/09/2017.
 *
 * Sets the highest stat as the lowest and lowest stat as the highest
 *
 *
 */

public class ItemEquivalentExchange implements Item {


    @Override
    public boolean applyEffect(World world, Entity player) {

        StatComponent statComponent = player.getComponent(StatComponent.class);

        float[] array = new float[]{
                statComponent.damage,
                statComponent.fireRate,
                statComponent.shotSpeed,
                statComponent.range,
                statComponent.accuracy,
                statComponent.luck};

        float lowest = array[0];
        int lowestPosition = 0;

        float highest = array[0];
        int highestPosition = 0;

        for(int i = 0; i < array.length; i++){
            if(array[i] < lowest) {
                lowest = array[i];
                lowestPosition = i;
            }

            if(array[i] > highest){
                highest = array[i];
                highestPosition = i;
            }
        }

        if(highestPosition == 0){
            statComponent.damage = lowest;
        } else if(highestPosition == 1){
            statComponent.fireRate = lowest;
        } else if(highestPosition == 2){
            statComponent.shotSpeed = lowest;
        } else if(highestPosition == 3){
            statComponent.range = lowest;
        } else if(highestPosition == 4){
            statComponent.accuracy = lowest;
        } else if(highestPosition == 5){
            statComponent.luck = lowest;
        }


        if(lowestPosition == 0){
            statComponent.damage = highest;
        } else if(lowestPosition == 1){
            statComponent.fireRate = highest;
        } else if(lowestPosition == 2){
            statComponent.shotSpeed = highest;
        } else if(lowestPosition == 3){
            statComponent.range = highest;
        } else if(lowestPosition == 4){
            statComponent.accuracy = highest;
        } else if(lowestPosition == 5){
            statComponent.luck = highest;
        }

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Luck.equivalentExchange;
    }


}
