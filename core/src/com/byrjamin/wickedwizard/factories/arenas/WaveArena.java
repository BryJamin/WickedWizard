package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;

import java.util.Stack;

/**
 * Created by ae164 on 19/05/17.
 */

public class WaveArena extends Arena{

    private Stack<Bag<Bag<Component>>> waves = new Stack<Bag<Bag<Component>>>();


    public WaveArena(ArenaSkin arenaSkin, MapCoords... mapCoords) {
        super(RoomType.TRAP, arenaSkin, mapCoords);
    }

    public void addWave(Bag<Component>... bags){
        Bag<Bag<Component>> bagOfBags = new Bag<Bag<Component>>();

        for(Bag<Component> b : bags){
            bagOfBags.add(b);
        }
        waves.add(bagOfBags);
    }


    public void addWave(Bag<Bag<Component>> bags){
        waves.add(bags);
    }

    public Stack<Bag<Bag<Component>>> getWaves() {
        return waves;
    }
}
