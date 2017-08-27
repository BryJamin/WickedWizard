package com.byrjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 27/08/2017.
 */

public class CompanionFactory extends AbstractFactory {

    private static final float crownWidth = Measure.units(5);
    private static final float crownHeight = Measure.units(5f);


    public CompanionFactory(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag dangerSensorCompanion(ParentComponent parentc, PositionComponent positionc, CollisionBoundComponent cbc) {

        ComponentBag bag = new ComponentBag();

        bag.add(new ChildComponent(parentc));

        bag.add(new PositionComponent(cbc.getCenterX(), cbc.getCenterY()));
        bag.add(new FiringAIComponent(0));
        bag.add(new FriendlyComponent());
        bag.add(new IntangibleComponent());
        bag.add(new FollowPositionComponent(positionc.position, cbc.bound.width / 2,  cbc.bound.height / 2));

        bag.add(new InCombatActionComponent(new Task() {

            WeaponComponent weaponComponent;

            @Override
            public void performAction(World world, Entity e) {

                if(weaponComponent == null){
                    weaponComponent = new WeaponComponent(new MultiPistol.PistolBuilder(world.getSystem(RenderingSystem.class).assetManager)
                            .damage(1f)
                            .color(ColorResource.COMPANION_BULLET_COLOR)
                            .shotScale(1.5f)
                            .shotSpeed(Measure.units(75f))
                            .enemy(false)
                            .friendly(true)
                            .fireRate(1.0f)
                            .angles(0, 90, 180)
                            .build());
                }

                e.edit().add(weaponComponent);

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

                e.edit().remove(WeaponComponent.class);

            }
        }));


        return bag;



    }



    public ComponentBag crownOfBiggaBlobba(ParentComponent parentc, PositionComponent positionc, CollisionBoundComponent cbc) {

        ComponentBag bag = new ComponentBag();
        bag.add(new ChildComponent(parentc));

        float x = cbc.getCenterX();
        float y = cbc.getCenterY();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, crownWidth, crownHeight)));
        bag.add(new TextureRegionComponent(atlas.findRegion(ItemResource.Companion.crownOfBiggaBlobba.region.getLeft()), crownWidth, crownHeight, TextureRegionComponent.PLAYER_LAYER_NEAR));
        bag.add(new FiringAIComponent(FiringAIComponent.AI.TARGET_ENEMY, 0 , 0));
        bag.add(new FriendlyComponent());
        bag.add(new IntangibleComponent());
        bag.add(new FollowPositionComponent(positionc.position, CenterMath.offsetX(cbc.bound.getWidth(), crownWidth),  cbc.bound.height * 0.70f));

        bag.add(new InCombatActionComponent(new Task() {

            WeaponComponent weaponComponent;

            @Override
            public void performAction(World world, Entity e) {

                if(weaponComponent == null){
                    weaponComponent = new WeaponComponent(new MultiPistol.PistolBuilder(world.getSystem(RenderingSystem.class).assetManager)
                            .damage(1f)
                            .expireRange(Measure.units(85f))
                            .color(ColorResource.COMPANION_BULLET_COLOR)
                            .shotScale(1.5f)
                            .shotSpeed(Measure.units(75f))
                            .enemy(false)
                            .friendly(true)
                            .fireRate(0.75f)
                            .angles(0)
                            .build());
                }

                e.edit().add(weaponComponent);

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

                e.edit().remove(WeaponComponent.class);

            }
        }));

        return bag;



    }










}
