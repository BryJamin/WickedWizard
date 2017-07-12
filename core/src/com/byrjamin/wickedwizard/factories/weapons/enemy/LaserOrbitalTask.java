package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.HazardComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 16/06/2017.
 */
public class LaserOrbitalTask implements Task {


    private final int[] angles;

    private final float orbitalSize;
    private final float orbitalIntervalSize;
    private final float speedInDegrees;
    private final float chargeTime;
    private final float disperseTime;

    private final boolean expire;
    private final float expiryTime;

    private final int numberOfOrbitals;
    private final int layer;

    private final Color color;

    private final TextureAtlas atlas;


    public static class LaserBuilder{

        //Required Parameters
        private final AssetManager assetManager;

        //Optional Parameters
        private float orbitalSize = 0;
        private float orbitalIntervalSize = 0;
        private float speedInDegrees = 0;
        private float chargeTime = 0.5f;
        private float disperseTime = 0.5f;


        private boolean expire = false;
        private float expiryTime;

        private int numberOfOrbitals;

        private int layer = TextureRegionComponent.ENEMY_LAYER_FAR;

        private int[] angles = new int[]{0};

        private Color color = new Color(Color.RED);

        public LaserBuilder(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        public LaserBuilder orbitalSize(float val)
        { orbitalSize = val; return this; }

        public LaserBuilder orbitalIntervalSize(float val)
        { orbitalIntervalSize = val; return this; }

        public LaserBuilder orbitalAndIntervalSize(float val)
        { orbitalSize = val; orbitalIntervalSize = val; return this; }

        public LaserBuilder speedInDegrees(float val)
        { speedInDegrees = val; return this; }

        public LaserBuilder chargeTime(float val)
        { chargeTime = val; return this; }

        public LaserBuilder disperseTime(float val)
        { disperseTime = val; return this; }


        public LaserBuilder expiryTime(float val)
        { expiryTime = val; expire = true; return this; }

        public LaserBuilder numberOfOrbitals(int val)
        { numberOfOrbitals = val; return this; }

        public LaserBuilder layer(int val)
        { layer = val; return this; }

        public LaserBuilder angles(int... val)
        { angles = val; return this; }

        public LaserOrbitalTask build() {
            return new LaserOrbitalTask(this);
        }


    }

    public LaserOrbitalTask(LaserBuilder lb){
        this.atlas = lb.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        this.orbitalSize = lb.orbitalSize;
        this.orbitalIntervalSize = lb.orbitalIntervalSize;
        this.speedInDegrees = lb.speedInDegrees;
        this.chargeTime = lb.chargeTime;
        this.numberOfOrbitals = lb.numberOfOrbitals;
        this.layer = lb.layer;
        this.angles = lb.angles;
        this.color = lb.color;
        this.expire = lb.expire;
        this.expiryTime = lb.expiryTime;
        this.disperseTime = lb.disperseTime;
    }


    @Override
    public void performAction(World world, Entity e) {

        PositionComponent current = e.getComponent(PositionComponent.class);
        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
        ParentComponent pc = new ParentComponent();

        float angleSpeed = speedInDegrees;

        for(int i = 0; i < numberOfOrbitals; i ++) {

            for (int angle : angles) {
                Entity orbital = world.createEntity();
                orbital.edit().add(new PositionComponent());
                orbital.edit().add(new CollisionBoundComponent(new Rectangle(0, 0, orbitalSize, orbitalSize), true));
                orbital.edit().add(new OrbitComponent(
                        new Vector3(current.getX(), current.getY(), 0f), i * orbitalIntervalSize,
                        angleSpeed, angle, cbc.bound.width / 2, cbc.bound.height / 2
                ));

                //TODO was Enemy Layer Far

                orbital.edit().add(new TextureRegionComponent(atlas.findRegion("block"),
                        orbitalSize, orbitalSize, layer, color));

                orbital.edit().add(new FadeComponent(true, chargeTime, false));
                orbital.edit().add(new IntangibleComponent());

                ChildComponent c = new ChildComponent();
                orbital.edit().add(c);
                pc.children.add(c);



                orbital.edit().add(new ActionAfterTimeComponent(new Task() {
                    @Override
                    public void performAction(World world, Entity e) {
                        e.edit().add(new HazardComponent());
                    }

                    @Override
                    public void cleanUpAction(World world, Entity e) {

                    }
                }, chargeTime));


                orbital.edit().add(new OnDeathActionComponent(new Task() {
                    @Override
                    public void performAction(World world, Entity e) {
                        Entity orbital = world.createEntity();
                        orbital.edit().add(e.getComponent(PositionComponent.class));
                        orbital.edit().add(e.getComponent(CollisionBoundComponent.class));
                        orbital.edit().add(e.getComponent(OrbitComponent.class));
                        orbital.edit().add(e.getComponent(TextureRegionComponent.class));
                        orbital.edit().add(new FadeComponent(false, disperseTime, false));
                        orbital.edit().add(new ExpireComponent(disperseTime + 0.1f));
                        orbital.edit().add(new IntangibleComponent());
                    }

                    @Override
                    public void cleanUpAction(World world, Entity e) {

                    }
                }));


                if(expire){


                    orbital.edit().add(new ExpireComponent(expiryTime));
                }

            }

            e.edit().add(pc);
        }


    }

    @Override
    public void cleanUpAction(World world, Entity e) {


        if(!world.getMapper(ParentComponent.class).has(e)) return;

        Array<ChildComponent> childComponents  = new Array<ChildComponent>();
        childComponents.addAll(e.getComponent(ParentComponent.class).children);



        for(ChildComponent c : childComponents) {

            Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);

            if(child != null){
                world.getSystem(OnDeathSystem.class).kill(child);
            }
        };

        e.edit().remove(ParentComponent.class);
    }
}
