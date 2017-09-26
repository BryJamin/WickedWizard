package com.bryjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.BulletFactory;
import com.bryjamin.wickedwizard.utils.BagSearch;

/**
 * Created by Home on 11/06/2017.
 */

public class CircleBlast implements Weapon {


    private int[] angles = new int[] {0,45,90,135,180,225,270,315};
    private boolean left;
    private float firerate;
    private BulletFactory bf;
    private float changePerFireInAngles;

    private float speed;
    private float size;

    public CircleBlast(AssetManager assetManager, int[] angles, float firerate, float changePerFireInAngles, boolean left){
        this.angles = angles;
        this.firerate = firerate;
        this.changePerFireInAngles = changePerFireInAngles;
        this.left = left;

        bf = new BulletFactory(assetManager);

    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {
        for(int i : angles){
            double angleOfTravel = angleInRadians + Math.toRadians(i);

            Bag<Component> bag = bf.basicEnemyBulletBag(x, y, size);
            bag.add(new VelocityComponent((float) (speed * Math.cos(angleOfTravel)), (float) (speed * Math.sin(angleOfTravel))));
            BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, bag).layer = TextureRegionComponent.ENEMY_LAYER_FAR;

            Entity bullet = world.createEntity();
            for(Component c : bag){
                bullet.edit().add(c);
            }
        }

        e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class).firingAngleInRadians += Math.toRadians((left) ? changePerFireInAngles : -changePerFireInAngles);

    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getBaseFireRate() {
        return firerate;
    }

    public void setLeft(boolean left){
        this.left = left;
    }
}
