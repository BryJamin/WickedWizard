package com.byrjamin.wickedwizard.factories.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 11/06/2017.
 */

public class JigFactory extends EnemyFactory{

    private float width = Measure.units(10f);
    private float height = Measure.units(10f);

    private float health = 15;

    public JigFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag jig(float x, float y){

        x = width / 2;
        y = height / 2;

        ComponentBag bag = new ComponentBag();
        this.defaultEnemyBag(bag, x,y,width,height,15);





    }








}
