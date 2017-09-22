package com.bryjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.object.BlockEnemyBulletComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.ColorChangeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.explosives.ExplosionFactory;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 27/08/2017.
 */

public class CompanionFactory extends AbstractFactory {

    private static final float crownWidth = Measure.units(5);
    private static final float crownHeight = Measure.units(5f);


    private static final float orbitRadius = Measure.units(7.5f);
    private static final float orbitalSpeedInDegrees = 1.5f;
    private static final float orbitalSize = Measure.units(5f);

    private ExplosionFactory explosionFactory;

    public CompanionFactory(AssetManager assetManager) {
        super(assetManager);
        this.explosionFactory = new ExplosionFactory(assetManager);
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


    public ComponentBag hiddenFiringCompanions(ComponentBag fillBag, ParentComponent parentComponent, PositionComponent positionComponent, CollisionBoundComponent cbc,
                                               FiringAIComponent companionAi){

        fillBag.add(new ChildComponent(parentComponent));
        fillBag.add(new PositionComponent(cbc.getCenterX(), cbc.getCenterY()));
        fillBag.add(companionAi);
        fillBag.add(new FriendlyComponent());
        fillBag.add(new IntangibleComponent());
        fillBag.add(new FollowPositionComponent(positionComponent.position, cbc.bound.width / 2,  cbc.bound.height / 2));

        return fillBag;

    }


    public ComponentBag autoRocket(final Entity player, PositionComponent positionc, final CollisionBoundComponent cbc){

        ComponentBag bag = hiddenFiringCompanions(new ComponentBag(), player.getComponent(ParentComponent.class), positionc, cbc,
                new FiringAIComponent(FiringAIComponent.AI.TARGET_ENEMY));

        final WeaponComponent weaponComponent = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .damage(1f)
                .color(ColorResource.COMPANION_ROCKET_COLOR_ONE)
                .colorChangeComponent(new ColorChangeComponent(
                        new Color(ColorResource.COMPANION_ROCKET_COLOR_ONE),
                        new Color(ColorResource.COMPANION_ROCKET_COLOR_TWO),
                        0.05f,
                        true))
                .shotScale(3f)
                .shotSpeed(Measure.units(150f))
                .enemy(false)
                .friendly(true)
                .fireRate(5.0f)
                .angles(0)
                .customOnDeathAction(new OnDeathActionComponent(explosionFactory.friendlyExplosionTask(5)))
                .build());

        bag.add(weaponComponent);


        return bag;


    }



    public ComponentBag sideCannonCompanion(final Entity player, PositionComponent positionc, final CollisionBoundComponent cbc) {

        ComponentBag bag = hiddenFiringCompanions(new ComponentBag(), player.getComponent(ParentComponent.class), positionc, cbc,
                new FiringAIComponent(0));

        final WeaponComponent weaponComponent = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .damage(1f)
                .color(ColorResource.COMPANION_BULLET_COLOR)
                .shotScale(1.5f)
                .shotSpeed(Measure.units(75f))
                .enemy(false)
                .friendly(true)
                .fireRate(1.0f)
                .angles(0, 180)
                .build());;


        bag.add(new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return player.getComponent(WeaponComponent.class).timer.isFinished();
            }
        }, new Action() {
            int count;
            @Override
            public void performAction(World world, Entity e) {
                count++;
                if(count == 3) {
                    FiringAIComponent firingAIComponent = e.getComponent(FiringAIComponent.class);
                    weaponComponent.weapon.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), firingAIComponent.firingAngleInRadians);
                    count = 0;
                }
            }
        }));

        return bag;

    }


    public ComponentBag megaSideCannonCompanion(final Entity player, PositionComponent positionc, final CollisionBoundComponent cbc) {

        ComponentBag bag = hiddenFiringCompanions(new ComponentBag(), player.getComponent(ParentComponent.class), positionc, cbc,
                new FiringAIComponent(0));

        final WeaponComponent weaponComponent = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .damage(1.25f)
                .color(ColorResource.COMPANION_BULLET_COLOR)
                .shotScale(3f)
                .shotSpeed(Measure.units(75f))
                .enemy(false)
                .friendly(true)
                .angles(0, 180)
                .build());;


        bag.add(new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return player.getComponent(WeaponComponent.class).timer.isFinished();
            }
        }, new Action() {
            int count;
            @Override
            public void performAction(World world, Entity e) {
                count++;
                if(count == 3) {
                    FiringAIComponent firingAIComponent = e.getComponent(FiringAIComponent.class);
                    weaponComponent.weapon.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), firingAIComponent.firingAngleInRadians);
                    count = 0;
                }
            }
        }));

        return bag;

    }


    public ComponentBag ghastlyWailCompanion(final Entity player, PositionComponent positionc, final CollisionBoundComponent cbc) {

        ComponentBag bag = hiddenFiringCompanions(new ComponentBag(), player.getComponent(ParentComponent.class), positionc, cbc,
                new FiringAIComponent(0));

        final WeaponComponent weaponComponent = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .damage(1f)
                .color(ColorResource.COMPANION_GHOST_BULLET_COLOR)
                .shotScale(1.5f)
                .shotSpeed(Measure.units(50f))
                .enemy(false)
                .expireRange(Measure.units(50f))
                .friendly(true)
                .angles(0,30,60,90,120,150,180,210,240,270,300,330)
                .intangible(true)
                .build());;


        bag.add(new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return player.getComponent(WeaponComponent.class).timer.isFinished();
            }
        }, new Action() {

            int count;


            @Override
            public void performAction(World world, Entity e) {
                count++;
                if(count == 10) {
                    FiringAIComponent firingAIComponent = e.getComponent(FiringAIComponent.class);
                    weaponComponent.weapon.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), firingAIComponent.firingAngleInRadians);
                    count = 0;
                }
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
        bag.add(new TextureRegionComponent(atlas.findRegion(ItemResource.Companion.crownOfBiggaBlobba.getRegion().getLeft()), crownWidth, crownHeight, TextureRegionComponent.PLAYER_LAYER_NEAR));
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
                            .fireRate(1.5f)
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



    public ComponentBag liasCrown(ParentComponent parentc, PositionComponent positionc, CollisionBoundComponent cbc) {

        ComponentBag bag = new ComponentBag();
        bag.add(new ChildComponent(parentc));

        float x = cbc.getCenterX();
        float y = cbc.getCenterY();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, crownWidth, crownHeight)));
        bag.add(new TextureRegionComponent(atlas.findRegion(ItemResource.Companion.liasCrown.getRegion().getLeft()), crownWidth, crownHeight, TextureRegionComponent.FOREGROUND_LAYER_FAR));
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
                            .color(new Color(ColorResource.COMPANION_BULLET_COLOR))
                            .shotScale(1.5f)
                            .shotSpeed(Measure.units(150f))
                            .enemy(false)
                            .friendly(true)
                            .colorChangeComponent(new ColorChangeComponent(new Color(ColorResource.COMPANION_BULLET_COLOR), new Color(Color.BLACK), 0.125f, true))
                            .fireRate(0.5f)
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








    public ComponentBag miniSpinnyThingy(ParentComponent parentc, PositionComponent positionc, CollisionBoundComponent cbc) {

        ComponentBag bag = new ComponentBag();
        bag.add(new ChildComponent(parentc));

        float x = cbc.getCenterX();
        float y = cbc.getCenterY();

        bag.add(new PositionComponent(x, y));
        bag.add(new BlockEnemyBulletComponent());

        bag.add(new OrbitComponent(positionc.position, orbitRadius, orbitalSpeedInDegrees, 0, cbc.bound.width / 2, cbc.bound.height / 2));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, orbitalSize, orbitalSize)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.KUGELDUSCHE_LASER), orbitalSize, orbitalSize, TextureRegionComponent.PLAYER_LAYER_NEAR));
        bag.add(new FriendlyComponent());
        bag.add(new IntangibleComponent());

        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.1f / 1f, atlas.findRegions(TextureStrings.KUGELDUSCHE_LASER), Animation.PlayMode.LOOP_REVERSED));


        bag.add(new AnimationComponent(animMap));

        return bag;

    }




    public ComponentBag myVeryOwnStalker(ParentComponent parentc, PositionComponent positionc, CollisionBoundComponent cbc) {

        ComponentBag bag = new ComponentBag();
        bag.add(new ChildComponent(parentc));

        float x = cbc.getCenterX();
        float y = cbc.getCenterY();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, orbitalSize, orbitalSize)));
        bag.add(new FollowPositionComponent(positionc.position, CenterMath.offsetX(cbc.bound.getWidth(), crownWidth),  cbc.bound.height * 1.8f));
        bag.add(new TextureRegionComponent(atlas.findRegion(ItemResource.Companion.myVeryOwnStalker.getRegion().getLeft()), orbitalSize, orbitalSize, TextureRegionComponent.PLAYER_LAYER_NEAR,
                new Color(1,1,1,0)));
        bag.add(new FriendlyComponent());
        bag.add(new IntangibleComponent());
        bag.add(new FiringAIComponent(FiringAIComponent.AI.TARGET_ENEMY));

        FadeComponent fc = new FadeComponent(false, 0.5f, false);
        bag.add(fc);

      //  bag.add(new FadeComponent(false, 0.5f, false));

        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(1f / 14f,
                atlas.findRegions(ItemResource.Companion.myVeryOwnStalker.getRegion().getLeft()), Animation.PlayMode.LOOP_REVERSED));


        bag.add(new AnimationComponent(animMap));






        bag.add(new InCombatActionComponent(new Task() {

            WeaponComponent weaponComponent;

            @Override
            public void performAction(World world, Entity e) {


               // e.getComponent(FadeComponent.class).isEndless = true;
                e.getComponent(FadeComponent.class).fadeIn = true;





                if(weaponComponent == null){
                    weaponComponent = new WeaponComponent(new MultiPistol.PistolBuilder(world.getSystem(RenderingSystem.class).assetManager)
                            .damage(1f)
                            .expireRange(Measure.units(85f))
                            .color(ColorResource.GHOST_BULLET_COLOR)
                            .shotScale(1.5f)
                            .shotSpeed(Measure.units(75f))
                            .intangible(true)
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
                //e.getComponent(FadeComponent.class).isEndless = false;
                e.getComponent(FadeComponent.class).fadeIn = false;

            }
        }));




        return bag;

    }







}
