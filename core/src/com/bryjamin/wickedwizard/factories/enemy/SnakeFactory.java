package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.collider.Collider;
import com.bryjamin.wickedwizard.utils.enums.Direction;

import static com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM;
import static com.bryjamin.wickedwizard.utils.collider.Collider.Collision.LEFT;
import static com.bryjamin.wickedwizard.utils.collider.Collider.Collision.RIGHT;
import static com.bryjamin.wickedwizard.utils.collider.Collider.Collision.TOP;

/**
 * Created by Home on 27/06/2017.
 */

public class SnakeFactory extends EnemyFactory {

    public SnakeFactory(AssetManager assetManager) {
        super(assetManager);
    }

    private static final float width = Measure.units(7.5f);
    private static final float height = Measure.units(7.5f);

    private static final float hitboxWidth = Measure.units(7f);
    private static final float hitboxHeight = Measure.units(7f);

    private static final float speed = Measure.units(50f);

    public enum SnakeType {
        ALWAYS_LEFT, ALWAYS_RIGHT, RANDOM
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag rightSnake(float x, float y, Direction direction){


        x = x - width / 2;
        y = y - width / 2;

        com.bryjamin.wickedwizard.utils.ComponentBag bag  = this.defaultEnemyBagNoLoot(new com.bryjamin.wickedwizard.utils.ComponentBag(), x, y, 5);

        CollisionBoundComponent cbc = new CollisionBoundComponent(new Rectangle(x,y,width,height));
        cbc.hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, hitboxWidth, hitboxHeight), (width / 2) - (hitboxWidth / 2), (height / 2) - (hitboxHeight / 2)));
        bag.add(cbc);

        VelocityComponent vc = new VelocityComponent();
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SNAKE_GREENISH),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        switch (direction){
            case LEFT:
            default: vc.velocity.x = -speed; trc.rotation = 90;
                break;
            case RIGHT: vc.velocity.x = speed; trc.rotation = -90;
                break;
            case UP: vc.velocity.y = speed; trc.rotation = 0;
                break;
            case DOWN: vc.velocity.y = -speed; trc.rotation = 180;
                break;
        }


        bag.add(vc);
        bag.add(trc);

        bag.add(new BounceComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SNAKE_GREENISH), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        OnCollisionActionComponent onCollisionActionComponent = new OnCollisionActionComponent();
        onCollisionActionComponent.left = new CollisionAction(LEFT, SnakeType.ALWAYS_RIGHT);
        onCollisionActionComponent.right = new CollisionAction(RIGHT, SnakeType.ALWAYS_RIGHT);
        onCollisionActionComponent.top = new CollisionAction(TOP, SnakeType.ALWAYS_RIGHT);
        onCollisionActionComponent.bottom = new CollisionAction(BOTTOM, SnakeType.ALWAYS_RIGHT);

        bag.add(onCollisionActionComponent);

        return bag;

    }


    public com.bryjamin.wickedwizard.utils.ComponentBag leftSnake(float x, float y, Direction direction){


        x = x - width / 2;
        y = y - width / 2;

        com.bryjamin.wickedwizard.utils.ComponentBag bag  = this.defaultEnemyBagNoLoot(new com.bryjamin.wickedwizard.utils.ComponentBag(), x, y, 5);

        CollisionBoundComponent cbc = new CollisionBoundComponent(new Rectangle(x,y,width,height));
        cbc.hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, hitboxWidth, hitboxHeight), (width / 2) - (hitboxWidth / 2), (height / 2) - (hitboxHeight / 2)));
        bag.add(cbc);

        VelocityComponent vc = new VelocityComponent();
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SNAKE_BLACK),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        switch (direction){
            case LEFT:
            default: vc.velocity.x = -speed; trc.rotation = 90;
                break;
            case RIGHT: vc.velocity.x = speed; trc.rotation = -90;
                break;
            case UP: vc.velocity.y = speed; trc.rotation = 0;
                break;
            case DOWN: vc.velocity.y = -speed; trc.rotation = 180;
                break;
        }


        bag.add(vc);
        bag.add(trc);

        bag.add(new BounceComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SNAKE_BLACK), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        OnCollisionActionComponent onCollisionActionComponent = new OnCollisionActionComponent();
        onCollisionActionComponent.left = new CollisionAction(LEFT, SnakeType.ALWAYS_LEFT);
        onCollisionActionComponent.right = new CollisionAction(RIGHT, SnakeType.ALWAYS_LEFT);
        onCollisionActionComponent.top = new CollisionAction(TOP, SnakeType.ALWAYS_LEFT);
        onCollisionActionComponent.bottom = new CollisionAction(BOTTOM, SnakeType.ALWAYS_LEFT);

        bag.add(onCollisionActionComponent);

        return bag;

    }



    private class CollisionAction implements Action {

        private Collider.Collision direction;
        private SnakeType SnakeType;

        private CollisionAction(Collider.Collision direction, SnakeType SnakeType){
            this.direction = direction;
            this.SnakeType = SnakeType;
        }

        @Override
        public void performAction(World world, Entity e) {

            VelocityComponent vc = e.getComponent(VelocityComponent.class);
            TextureRegionComponent trc = e.getComponent(TextureRegionComponent.class);

            float speed = Math.max(Math.abs(vc.velocity.x), Math.abs(vc.velocity.y));


            switch (direction){
                case LEFT: vc.velocity.y = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? -speed : speed;
                    trc.rotation = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? 180 : 0;
                    vc.velocity.x = 0;
                    break;
                case RIGHT: vc.velocity.y = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? speed : -speed;
                    trc.rotation = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? 0 : 180;
                    vc.velocity.x = 0;
                    break;
                case TOP: vc.velocity.x = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? -speed : speed;
                    trc.rotation = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? 90 : -90;
                    vc.velocity.y = 0;
                    break;
                case BOTTOM: vc.velocity.x = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? speed : -speed;
                    trc.rotation = SnakeType == SnakeFactory.SnakeType.ALWAYS_LEFT ? -90 : 90;
                    vc.velocity.y = 0;
                    break;

            }


        }
    }






}