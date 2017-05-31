package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 14/05/2017.
 */

public class GibletFactory extends AbstractFactory {

    private final static int aniNumber = 1;
    private final Random random = new Random();

    public GibletFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public Giblets defaultGiblets(Color color){
        return new Giblets(5, 0.2f, (int) Measure.units(10f), (int) Measure.units(20f),Measure.units(0.5f), color);
    }

    public Giblets giblets(int numberOfGiblets, float life,float minSpeed,float maxSpeed, float size, Color color){
        return new Giblets(numberOfGiblets, life, minSpeed, maxSpeed, size, color);
    }

    public Giblets bombGiblets(int numberOfGiblets, float life,float minSpeed,float maxSpeed, float size, Color color){
        Giblets g = new Giblets(numberOfGiblets, life, minSpeed, maxSpeed, size, color);
        g.isBombGiblets = true;
        return g;
    }

    public class Giblets implements Action {

        private int numberOfGiblets;
        private final float life;
        private final float minSpeed;
        private final float maxSpeed;
        private final float size;
        private final Color color;

        public boolean isBombGiblets;

        public Giblets(int numberOfGiblets, float life,float minSpeed,float maxSpeed, float size, Color color){
            this.numberOfGiblets = numberOfGiblets;
            this.life = life;
            this.minSpeed = minSpeed;
            this.maxSpeed = maxSpeed;
            this.size = size;
            this.color = color;
        }

        @Override
        public void performAction(World world, Entity e) {
            CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
            if(isBombGiblets) {
                BagToEntity.bagsToEntities(world, bombGiblets(cbc.getCenterX(), cbc.getCenterY(), numberOfGiblets, life, minSpeed, maxSpeed, size, color));
            } else {
                BagToEntity.bagsToEntities(world, giblets(cbc.getCenterX(), cbc.getCenterY(), numberOfGiblets, life, minSpeed, maxSpeed, size, color));
            }
        }

        @Override
        public void cleanUpAction(World world, Entity e) {

        }
    }


    public Bag<ComponentBag> giblets(float x, float y, int numberOfGiblets,float life,float minSpeed,float maxSpeed, float size, Color color){

        Bag<ComponentBag> bags = new Bag<ComponentBag>();

        for(int i = 0; i < numberOfGiblets; i++) {

            float cx = x - size / 2;
            float cy = y - size / 2;

            float vx = random.nextFloat() * (maxSpeed - minSpeed) + minSpeed;
            float vy = random.nextFloat() * (maxSpeed - minSpeed) + minSpeed;
            vx = random.nextBoolean() ? vx : -vx;
            vy = random.nextBoolean() ? vy : -vy;

            ComponentBag bag = new ComponentBag();
            bag.add(new PositionComponent(cx, cy));
            bag.add(new VelocityComponent(vx, vy));
            bag.add(new BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(cx, cy, size, size)));
            bag.add(new ExpireComponent(life));

            //FadeComponent fc = new FadeComponent(false, life, false);
            //bag.add(fc);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size,
                    TextureRegionComponent.PLAYER_LAYER_NEAR);
            trc.DEFAULT = color;
            trc.color = color;

            bag.add(trc);

            bags.add(bag);

        }

        return bags;

    }


/*    public OnDeathComponent defaultGiblets(OnDeathComponent fillodc, Color color){
        return  giblets(fillodc, 5, 0.2f, (int) Measure.units(10f), (int) Measure.units(20f),Measure.units(0.5f), color);
    }

    public OnDeathComponent giblets(OnDeathComponent fillodc, int numberOfGiblets,float life,float minSpeed,float maxSpeed, float size, Color color){

        ComponentBag bag;

        for(int i = 0; i < numberOfGiblets; i++) {

            float vx = random.nextFloat() * (maxSpeed - minSpeed) + minSpeed;
            float vy = random.nextFloat() * (maxSpeed - minSpeed) + minSpeed;
            vx = random.nextBoolean() ? vx : -vx;
            vy = random.nextBoolean() ? vy : -vy;

            bag = new ComponentBag();
            bag.add(new PositionComponent());
            bag.add(new VelocityComponent(vx, vy));
            bag.add(new BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(0, 0, size, size)));
            bag.add(new ExpireComponent(life));

            //FadeComponent fc = new FadeComponent(false, life, false);
            //bag.add(fc);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size,
                    TextureRegionComponent.PLAYER_LAYER_NEAR);
            trc.DEFAULT = color;
            trc.color = color;

            bag.add(trc);

            fillodc.getComponentBags().add(bag);

        }

        return fillodc;

    }*/


    public Bag<ComponentBag> bombGiblets(float x, float y, int numberOfGiblets,float life,float minSpeed,float maxSpeed, float size, Color color){

        Bag<ComponentBag> bags = new Bag<ComponentBag>();

        for(int i = 0; i < numberOfGiblets; i++) {

            float cx = x - size / 2;
            float cy = y - size / 2;

            float vx = random.nextInt((int) maxSpeed - (int) minSpeed) + minSpeed;
            float vy = random.nextInt((int) maxSpeed - (int) minSpeed) + minSpeed;

            float fadeLife = random.nextFloat() * (life * 2 - life / 2) + life / 2;

            vx = random.nextBoolean() ? vx : -vx;
            vy = random.nextBoolean() ? vy : -vy;

            ComponentBag bag = new ComponentBag();

            bag.add(new PositionComponent(cx, cy));
            bag.add(new VelocityComponent(vx, vy));
            // bag.add(new BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(cx, cy, size, size)));
            bag.add(new ExpireComponent(life));
            bag.add(new IntangibleComponent());
            // bag.add(new ExpiryRangeComponent(new Vector3()maxSpeed))


            if(random.nextBoolean() && random.nextBoolean()) {

            } else {
                FadeComponent fc = new FadeComponent(false, fadeLife, false);
                bag.add(fc);
            }

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size,
                    TextureRegionComponent.ENEMY_LAYER_MIDDLE);
            trc.DEFAULT = color;
            trc.color = color;

            bag.add(trc);
            bags.add(bag);

        }

        return bags;

    }

/*
    public OnDeathComponent bombGiblets(OnDeathComponent fillodc, int numberOfGiblets,float life,float minSpeed,float maxSpeed, float size, Color color){

        ComponentBag bag;

        Random random = new Random();

        for(int i = 0; i < numberOfGiblets; i++) {

            float vx = random.nextInt((int) maxSpeed - (int) minSpeed) + minSpeed;
            float vy = random.nextInt((int) maxSpeed - (int) minSpeed) + minSpeed;

            float fadeLife = random.nextFloat() * (life * 2 - life / 2) + life / 2;

            vx = random.nextBoolean() ? vx : -vx;
            vy = random.nextBoolean() ? vy : -vy;

            bag = new ComponentBag();
            bag.add(new PositionComponent());
            bag.add(new VelocityComponent(vx, vy));
           // bag.add(new BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(0, 0, size, size)));
            bag.add(new ExpireComponent(life));
            bag.add(new IntangibleComponent());
            // bag.add(new ExpiryRangeComponent(new Vector3()maxSpeed))


            if(random.nextBoolean() && random.nextBoolean()) {

            } else {
                FadeComponent fc = new FadeComponent(false, fadeLife, false);
                bag.add(fc);
            }

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size,
                    TextureRegionComponent.ENEMY_LAYER_MIDDLE);
            trc.DEFAULT = color;
            trc.color = color;

            bag.add(trc);

            fillodc.getComponentBags().add(bag);

        }

        return fillodc;



    }
*/



}
