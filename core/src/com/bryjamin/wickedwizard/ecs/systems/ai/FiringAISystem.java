package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.ecs.components.HealthComponent;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.bryjamin.wickedwizard.utils.BulletMath;

/**
 * Created by BB on 11/03/2017.
 *
 * System used for enemies who use a Weapon
 *
 * Calculates the angle to fire a weapon from based on the Firing AI of the Enemy
 *
 * Calculates when to fire the weapon by ticking down it's timer
 *
 */
public class FiringAISystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.WeaponComponent> wm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent> fm;

    private Aspect.Builder enemyTargets;

    private Vector2 start = new Vector2();
    private Vector2 end = new Vector2();


    private Viewport gameport;

    @SuppressWarnings("unchecked")
    public FiringAISystem(Viewport gameport) {
        super(Aspect.all(PositionComponent.class, WeaponComponent.class, FiringAIComponent.class));
        enemyTargets = Aspect.all(EnemyComponent.class, HealthComponent.class, CollisionBoundComponent.class);
        this.gameport = gameport;
    }

    @Override
    protected void process(Entity e) {


        WeaponComponent wc = wm.get(e);


        FiringAIComponent fc = fm.get(e);

        wc.timer.update(world.delta);

        float x;
        float y;

        if(cbm.has(e)){
            CollisionBoundComponent cbc = cbm.get(e);
            x = cbc.getCenterX() + fc.offsetX;
            y = cbc.getCenterY() + fc.offsetY;
        } else {
            x = pm.get(e).getX();
            y = pm.get(e).getY();
        }


        switch(fc.ai){
            case TARGET_PLAYER:

                if(wc.timer.isFinishedAndReset()){

                    wc.weapon.fire(world, e, x, y, firingAngleToPlayerInRadians(x, y));
                    if(world.getMapper(AnimationStateComponent.class).has(e))
                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);
                }

                break;

            case TARGET_ENEMY:

                if(wc.timer.isFinished() && enemiesExist()){

                    wc.weapon.fire(world, e, x, y, firingAngleToNearestEnemyInRadians(x, y));
                    if(world.getMapper(AnimationStateComponent.class).has(e))
                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);
                    wc.timer.reset();
                }

                break;


            case UNTARGETED:

                if(wc.timer.isFinishedAndReset()){

                    wc.weapon.fire(world,e, x, y, fc.firingAngleInRadians);
                    if(world.getMapper(AnimationStateComponent.class).has(e))
                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);

                }

                break;
        }

    }


    /**
     *
     * @param x - Starting x position of the shot fired
     * @param y - Starting y position of the shot fired
     * @return - Angle in radians the player is from the starting position
     */
    private double firingAngleToPlayerInRadians(float x, float y){
        CollisionBoundComponent playercbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        return BulletMath.angleOfTravel(x, y, playercbc.getCenterX(), playercbc.getCenterY());
    }


    public boolean enemiesExist(){
        EntitySubscription subscription = world.getAspectSubscriptionManager().get(enemyTargets);
        IntBag entityIds = subscription.getEntities();

        for(int i = 0; i < entityIds.size(); i++){
            if(!CameraSystem.isOnCamera(cbm.get(entityIds.get(i)).bound, gameport.getCamera())){
                entityIds.remove(i);
            };
        }

        return entityIds.size() > 0;
    }



    public double firingAngleToNearestEnemyInRadians(float x, float y){

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(enemyTargets);
        IntBag entityIds = subscription.getEntities();

        if(entityIds.size() <= 0) return 0;

        start.set(x, y);

        float nearestDistance = start.dst(cbm.get(entityIds.get(0)).getCenterX(), cbm.get(entityIds.get(0)).getCenterY());
        end.set(cbm.get(entityIds.get(0)).getCenterX(), cbm.get(entityIds.get(0)).getCenterY());


        for(int i = 0; i < entityIds.size(); i++){

            CollisionBoundComponent cbc = cbm.get(entityIds.get(i));
            float distance = start.dst(cbc.getCenterX(), cbc.getCenterY());



            if(distance < nearestDistance){
                nearestDistance = distance;
                end.set(cbc.getCenterX(), cbc.getCenterY());

            }
        }

        return BulletMath.angleOfTravel(x, y, end.x, end.y);
    }



}