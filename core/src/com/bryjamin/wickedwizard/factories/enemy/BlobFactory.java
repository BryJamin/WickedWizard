package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by Home on 26/03/2017.
 */

public class BlobFactory extends EnemyFactory {

    //TODO should just make it ten
    private final float width = com.bryjamin.wickedwizard.utils.Measure.units(9);
    private final float height = com.bryjamin.wickedwizard.utils.Measure.units(9);

    private final float textureWidth = com.bryjamin.wickedwizard.utils.Measure.units(11);
    private final float textureHeight = com.bryjamin.wickedwizard.utils.Measure.units(11);

    private final float textureOffsetX = -com.bryjamin.wickedwizard.utils.Measure.units(1f);
    private final float textureOffsetY = 0;

    private float speed = com.bryjamin.wickedwizard.utils.Measure.units(15f);


    public BlobFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag blob(float x , float y, float scale, float speed, float health, boolean startsRight, Color color){

        float width = this.width * scale;
        float height = this.height * scale;

        x = x - width / 2;
        y = y - height / 2;


        com.bryjamin.wickedwizard.utils.ComponentBag bag = this.defaultEnemyBag(new com.bryjamin.wickedwizard.utils.ComponentBag(), x, y, health);

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(startsRight ? speed : -speed, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BLOB_STANDING), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOB_STANDING),
                textureOffsetX * scale, textureOffsetY * scale, textureWidth * scale, textureHeight * scale,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE, color));

        com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent onCollisionActionComponent = blobOCAC(speed);
        bag.add(onCollisionActionComponent);

        return bag;



    }



    public com.bryjamin.wickedwizard.utils.ComponentBag blobBag(float x, float y, boolean startsRight){
        return blob(x,y,1, com.bryjamin.wickedwizard.utils.Measure.units(15f), 10, startsRight, ColorResource.BLOB_GREEN);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag angryBlobBag(float x, float y, boolean startsRight){
        return blob(x,y,1, com.bryjamin.wickedwizard.utils.Measure.units(30f), 15, startsRight, ColorResource.BLOB_RED);
    }

    public Bag<Component> smallblobBag(float x, float y,  boolean startsRight){
        com.bryjamin.wickedwizard.utils.ComponentBag bag = blob(x,y,0.5f, com.bryjamin.wickedwizard.utils.Measure.units(30f), 2, startsRight, ColorResource.BLOB_GREEN);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(LootComponent.class, bag);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.ExploderComponent());
        return bag;
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag angrySmallBag(float x, float y, boolean startsRight){
        com.bryjamin.wickedwizard.utils.ComponentBag bag = blob(x,y,0.5f, com.bryjamin.wickedwizard.utils.Measure.units(45f), 3, startsRight, ColorResource.BLOB_RED);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(LootComponent.class, bag);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.ExploderComponent());
        return bag;
    }


    private com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent blobOCAC(final float speed){
        com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent onCollisionActionComponent = new com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent();
        onCollisionActionComponent.left = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = speed;
            }
        };

        onCollisionActionComponent.right = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = -speed;
            }
        };

        return onCollisionActionComponent;
    }

    public Bag<Component> dummyBlob(float x, float y){


        x = x - width / 2;
        y = y - height / 2;

        Bag<Component> bag = new Bag<Component>();
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x,y));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(1000));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BLOB_STANDING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOB_STANDING),
                textureOffsetX, textureOffsetY, textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        return bag;
    }

}
