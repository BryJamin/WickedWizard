package com.bryjamin.wickedwizard.factories.weapons;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.assets.Mix;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 23/07/2017.
 */

public class Giblets extends AbstractFactory implements Action {


    private final int numberOfGibletPairs;
    private final int layer;

    private final float expiryTime;

    //Chancefinal  to fade (ROll beteween 1 and 100
    private final float fadeChance;

    private final float minSpeed;
    private final float maxSpeed;
    private final float size;
    private final Color[] colors;
    private final Mix[] mixes;
    private final boolean intangible;

    private final Random random = MathUtils.random;


    public Giblets(GibletBuilder gb) {
        super(gb.assetManager);

        this.numberOfGibletPairs = gb.numberOfGibletPairs;
        this.layer = gb.layer;
        this.expiryTime = gb.expiryTime;
        this.fadeChance = gb.fadeChance;
        this.minSpeed = gb.minSpeed;
        this.maxSpeed = gb.maxSpeed;
        this.size = gb.size;
        this.colors = gb.colors;
        this.mixes = gb.mixes;
        this.intangible = gb.intangible;
    }

    @Override
    public void performAction(World world, Entity e) {
        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
        if(cbc == null) return;
        BagToEntity.bagsToEntities(world, giblets(cbc.getCenterX(), cbc.getCenterY()));

        if(mixes.length > 0) {
            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.class).playRandomSound(mixes);
        }


    }





    public static class GibletBuilder{

        //Mandatory
        private final AssetManager assetManager;


        //Optional
        private int numberOfGibletPairs = 3;
        private int layer = TextureRegionComponent.PLAYER_LAYER_NEAR; // TextureRegionComponent.ENEMY_LAYER_MIDDLE

        private float expiryTime = 0.2f;

        //Chance to fade between 0 and 1.0f
        private float fadeChance = 0;

        private float minSpeed = 0;
        private float maxSpeed = Measure.units(20f);
        private float size = Measure.units(0.5f);

        private Color[] colors = new Color[] {new Color(Color.WHITE)};
        private Mix[] mixes = new Mix[]{};
        private boolean intangible = true;

        public GibletBuilder(AssetManager assetManager){
            this.assetManager = assetManager;
        }


        public GibletBuilder numberOfGibletPairs(int val)
        { numberOfGibletPairs = val; return this; }

        public GibletBuilder layer(int val)
        { layer = val; return this; }

        public GibletBuilder expiryTime(float val)
        { expiryTime = val; return this; }

        public GibletBuilder fadeChance(float val)
        { fadeChance = val; return this; }

        public GibletBuilder minSpeed(float val)
        { minSpeed = val; return this; }

        public GibletBuilder maxSpeed(float val)
        { maxSpeed = val; return this; }

        public GibletBuilder size(float val)
        { size = val; return this; }

        public GibletBuilder colors(Color... val)
        { colors = val; return this; }

        public GibletBuilder mixes(Mix... val)
        { mixes = val; return this; }

        public GibletBuilder intangible(boolean val)
        { intangible = val; return this; }

        public Giblets build()
        { return new Giblets(this); }

    }

    public com.bryjamin.wickedwizard.utils.ComponentBag createGiblet(float x, float y, com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc, float expiryTime, float size, Color color){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();


        float cx = x - size / 2;
        float cy = y - size / 2;

        bag.add(new PositionComponent(cx, cy));
        bag.add(vc);

        if(!intangible) {
            bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent());
            bag.add(new CollisionBoundComponent(new Rectangle(cx, cy, size, size)));
        }

        bag.add(new ExpireComponent(expiryTime));

        if(random.nextFloat() * 1 <= fadeChance) {
            float fadeLife = random.nextFloat() * (expiryTime * 2 - expiryTime / 2) + expiryTime / 2;
            FadeComponent fc = new FadeComponent(false, fadeLife, false);
            bag.add(fc);
        }



        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), size, size,
                layer, color));

        return bag;
    }


    public Bag<com.bryjamin.wickedwizard.utils.ComponentBag> giblets(float x, float y){

        Bag<com.bryjamin.wickedwizard.utils.ComponentBag> bags = new Bag<com.bryjamin.wickedwizard.utils.ComponentBag>();

        for(int i = 0; i < numberOfGibletPairs; i++) {

            float speed = random.nextFloat() * (maxSpeed - minSpeed) + minSpeed;
            float angle = random.nextFloat() * 360;
            float vx = BulletMath.velocityX(speed, Math.toRadians(angle));
            float vy = BulletMath.velocityY(speed, Math.toRadians(angle));
            bags.add(createGiblet(x, y, new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(vx, vy), expiryTime, size, colors[random.nextInt(colors.length)]));
            bags.add(createGiblet(x, y, new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(-vx, -vy), expiryTime, size, colors[random.nextInt(colors.length)]));
        }

        return bags;

    }




}
