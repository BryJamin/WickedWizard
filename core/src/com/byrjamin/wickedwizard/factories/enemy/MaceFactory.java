package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.HazardComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 23/05/2017.
 */

public class MaceFactory extends EnemyFactory {


    public MaceFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public Bag<ComponentBag> orbitalMace(float x, float y) {

        PositionComponent pc = new PositionComponent(x,y);
        ComponentBag position = new ComponentBag();
        position.add(pc);

        Bag<ComponentBag> bags = new Bag<ComponentBag>();
        bags.add(position);
        bags.add(orbitalMaceconnecter(
                new OrbitComponent(pc.position, 0, 4, 0), Measure.units(5f), Measure.units(5f)));

        bags.add(orbitalMaceconnecter(
                new OrbitComponent(pc.position, Measure.units(6f), 4, 0), Measure.units(5f), Measure.units(5f)));

        bags.add(orbitalMacePart(
                new OrbitComponent(pc.position, Measure.units(15f), 4, 0), Measure.units(10f), Measure.units(10f)));


        return bags;



    }

    public ComponentBag orbitalMaceconnecter(OrbitComponent oc, float width, float height){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(0,0, width, height)));
        bag.add(new IntangibleComponent());
        bag.add(oc);

        bag.add(new TextureRegionComponent(atlas.findRegion("block"),
                width,
                height,
                TextureRegionComponent.ENEMY_LAYER_NEAR));

        return bag;
    }


    public ComponentBag orbitalMacePart(OrbitComponent oc, float width, float height){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(0,0, width, height), true));
        bag.add(new HazardComponent());
        bag.add(new IntangibleComponent());
        bag.add(oc);

        bag.add(new TextureRegionComponent(atlas.findRegion("mace"),
                width,
                height,
                TextureRegionComponent.ENEMY_LAYER_NEAR));

        return bag;
    }




}
