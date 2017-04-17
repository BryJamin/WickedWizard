package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.AltarComponent;
import com.byrjamin.wickedwizard.ecs.components.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.HighlightComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory {


    public static ComponentBag createItemBag(float x, float y, Item item){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new VelocityComponent(random.nextInt((int) Measure.units(60f)) -Measure.units(30f), Measure.units(30f)));
        bag.add(new GravityComponent());
        bag.add(new PickUpComponent(item));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5))));
        bag.add(new TextureRegionComponent(item.getRegion(), Measure.units(5), Measure.units(5),
                TextureRegionComponent.PLAYER_LAYER_FAR));

        return bag;
    }


    public static ComponentBag createFloatingItemBag(float x, float y, Item item){

        float width = Measure.units(8);
        float height = Measure.units(8);

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new PickUpComponent(item));
        bag.add(new HighlightComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new TextureRegionComponent(item.getRegion(), width, height,
                TextureRegionComponent.PLAYER_LAYER_FAR));
        return bag;
    }


    public static Array<ComponentBag> createItemAltarBag(float x, float y, Item item){

        float width = Measure.units(15);
        float height = Measure.units(15);

        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        PositionComponent positionComponent = new PositionComponent(x,y);

        ComponentBag altarItemTexture = new ComponentBag();
        altarItemTexture.add(new PositionComponent());
        altarItemTexture.add(new TextureRegionComponent(item.getRegion(), Measure.units(5), Measure.units(5), TextureRegionComponent.FOREGROUND_LAYER_FAR));
        altarItemTexture.add(new FollowPositionComponent(positionComponent.position, width / 2 - Measure.units(2.5f), Measure.units(5)));

        ChildComponent c = new ChildComponent();
        altarItemTexture.add(c);

        ComponentBag bag = new ComponentBag();
        bag.add(new ParentComponent(c));
        bag.add(positionComponent);
        //bag.add(new PickUpComponent(item));
        bag.add(new AltarComponent(item));
        // bag.add(new HighlightComponent());
        bag.add(new VelocityComponent());
        bag.add(new GravityComponent());

        Rectangle bound = new Rectangle(new Rectangle(x,y, width, height));

        bag.add(new CollisionBoundComponent(bound));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion("altar"), width, height,
                TextureRegionComponent.PLAYER_LAYER_FAR));
        bag.add(new ProximityTriggerAIComponent(bound, activeAltar()));

        bags.add(altarItemTexture);
        bags.add(bag);





        return bags;
    }



    private static Action activeAltar (){

        return new Action() {
            @Override
            public void performAction(World w, Entity e) {
                e.edit().add(new HighlightComponent());

                Entity child = w.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first());
                if(child != null){
                    child.edit().add(new HighlightComponent());
                }
               // e.getComponent(ParentComponent.class).children.get(0)
            }

            @Override
            public void cleanUpAction(World w, Entity e) {
                e.edit().remove(HighlightComponent.class);

                Entity child = w.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first());
                if(child != null){
                    child.edit().remove(HighlightComponent.class);
                }
            }
        };


    }



}
