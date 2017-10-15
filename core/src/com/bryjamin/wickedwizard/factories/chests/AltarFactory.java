package com.bryjamin.wickedwizard.factories.chests;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemInteractionTasks;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.collider.HitBox;
import com.bryjamin.wickedwizard.utils.enums.ItemType;

/**
 * Created by BB on 28/09/2017.
 */

public class AltarFactory extends AbstractFactory {

    private static final float altarWidth = Measure.units(15f);
    private static final float altarHeight = Measure.units(15f);

    private static final float altarItemWidth = Measure.units(5f);
    private static final float altarItemHeight = Measure.units(5f);

    private static final float altarItemOffsetY = Measure.units(5.5f);

    public AltarFactory(AssetManager assetManager) {
        super(assetManager);
    }


    private ComponentBag altarItemTexture(Item item, ParentComponent pc, PositionComponent altarPosition){

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent());
        bag.add(new TextureRegionComponent(atlas.findRegion(item.getValues().getRegion().getLeft(), item.getValues().getRegion().getRight()),
                altarItemWidth, altarItemHeight,
                TextureRegionComponent.ENEMY_LAYER_FAR,
                item.getValues().getTextureColor()));
        bag.add(new FollowPositionComponent(altarPosition.position, CenterMath.offsetX(altarWidth, altarItemWidth), altarItemOffsetY));
        ChildComponent c = new ChildComponent(pc);
        bag.add(c);

        return bag;


    }

    public ComponentBag createCenteredItemAltarBag(float x, float y, Color color, ItemType... itemTypes){

        x = x - altarWidth / 2;
        y = y - altarHeight / 2;

        return createItemAltarBag(x, y, color, itemTypes);
    }


    private ComponentBag emptyAltar(float x, float y, Color color){


        ComponentBag altarBag = new ComponentBag();

        altarBag.add(new ParentComponent());
        altarBag.add(new PositionComponent(x,y));
        altarBag.add(new AltarComponent());
        altarBag.add(new VelocityComponent());
        altarBag.add(new GravityComponent());

        Rectangle bound = new Rectangle(new Rectangle(x,y, altarWidth, altarHeight / 3));
        altarBag.add(new CollisionBoundComponent(bound));
        altarBag.add(new ProximityTriggerAIComponent(ItemInteractionTasks.pickUpItem(), new HitBox(bound)));
        altarBag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ALTAR), altarWidth, altarHeight,
                TextureRegionComponent.PLAYER_LAYER_FAR, new Color(color)));

        return altarBag;

    }




    public ComponentBag createItemAltarBag(float x, float y, Color color, final ItemType... itemTypes){

        ComponentBag altarBag = emptyAltar(x, y, color);

        altarBag.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {


                Item altarItem = world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateRoomItem(itemTypes);

                e.getComponent(AltarComponent.class).pickUp = altarItem;

                BagToEntity.bagToEntity(world.createEntity(), altarItemTexture(altarItem, e.getComponent(ParentComponent.class),
                        e.getComponent(PositionComponent.class)));

                e.edit().remove(DuringRoomLoadActionComponent.class);

            }
        }));

        return  altarBag;
    }



    public ComponentBag createCenteredPresetItemAltarBag(float x, float y, Color color, Item item){
        x = x - altarWidth / 2;
        y = y - altarHeight / 2;
        return createPresetItemAltarBag(x, y, color, item);
    }


    public ComponentBag createPresetItemAltarBag(float x, float y, Color color, final Item item){

        ComponentBag altarBag = emptyAltar(x, y, color);

        altarBag.add(new DuringRoomLoadActionComponent(new Action() {

            @Override
            public void performAction(World world, Entity e) {

                e.getComponent(AltarComponent.class).pickUp = item;

                BagToEntity.bagToEntity(world.createEntity(), altarItemTexture(item, e.getComponent(ParentComponent.class),
                        e.getComponent(PositionComponent.class)));

                e.edit().remove(DuringRoomLoadActionComponent.class);

            }
        }));

        return altarBag;
    }


}
