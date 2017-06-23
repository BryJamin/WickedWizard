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
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.HazardComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 16/06/2017.
 */
public class LaserOrbitalTask implements Task {


    private int[] angles = new int[] {0,80,170, 260};

    private float size;
    private float speedInDegrees;
    private int numberOfOrbitals;
    private float chargeTime;

    private TextureAtlas atlas;

    public LaserOrbitalTask(AssetManager assetManager, float size, float speedInDegrees, int numberOfOrbitals, float chargeTime, int[] angles){
        this.size = size;
        this.speedInDegrees = speedInDegrees;
        this.numberOfOrbitals = numberOfOrbitals;
        this.angles = angles;
        this.chargeTime = chargeTime;
        atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
    }

    public void setAngles(int[] angles) {
        this.angles = angles;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setSpeedInDegrees(float speedInDegrees) {
        this.speedInDegrees = speedInDegrees;
    }

    public void setNumberOfOrbitals(int numberOfOrbitals) {
        this.numberOfOrbitals = numberOfOrbitals;
    }

    public void setChargeTime(float chargeTime) {
        this.chargeTime = chargeTime;
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
                orbital.edit().add(new CollisionBoundComponent(new Rectangle(0, 0, size,size), true));
                orbital.edit().add(new OrbitComponent(
                        new Vector3(current.getX(), current.getY(), 0f), i * size,
                        angleSpeed, angle, cbc.bound.width / 2, cbc.bound.height / 2
                ));

                orbital.edit().add(new TextureRegionComponent(atlas.findRegion("block"),
                        size,size, TextureRegionComponent.ENEMY_LAYER_FAR, new Color(Color.RED)));

                orbital.edit().add(new FadeComponent(true, chargeTime, false));
                orbital.edit().add(new IntangibleComponent());

                ChildComponent c = new ChildComponent();
                pc.children.add(c);
                orbital.edit().add(c);


                orbital.edit().add(new ActionAfterTimeComponent(new Task() {
                    @Override
                    public void performAction(World world, Entity e) {
                        e.edit().add(new HazardComponent());
                    }

                    @Override
                    public void cleanUpAction(World world, Entity e) {

                    }
                }, chargeTime));


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
                child.edit().remove(EnemyComponent.class);
                child.edit().add(new FadeComponent(false, 0.5f, false));
                child.edit().add(new ExpireComponent(0.6f));
            }
        };

        e.edit().remove(ParentComponent.class);
    }
}
