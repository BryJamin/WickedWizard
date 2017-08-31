package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.GrappleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.BlockEnemyBulletComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.object.EnemyOnlyWallComponent;
import com.byrjamin.wickedwizard.ecs.components.object.GrappleableComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PlatformComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 04/03/2017.
 */
public class CollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlayerComponent> playerm;
    ComponentMapper<EnemyComponent> enemym;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;
    ComponentMapper<BulletComponent> bulletm;
    ComponentMapper<BounceComponent> boucem;
    ComponentMapper<GrappleComponent> grapplem;
    ComponentMapper<MoveToComponent> mtm;


    private Rectangle futureRectangle = new Rectangle();
    private Rectangle nearByCheckRectangle = new Rectangle();
    private Array<Rectangle> collidableobjects = new Array<Rectangle>();
    private Array<Rectangle> temporarayCollidableObjects = new Array<Rectangle>();

    private Aspect.Builder wall;
    private Aspect.Builder destructibleWalls;
    private Aspect.Builder playerWalls;
    private Aspect.Builder platforms;
    private Aspect.Builder enemyOnlyWalls;
    private Aspect.Builder enemyBulletBlockWalls;


    PerformanceCounter performanceCounter = new PerformanceCounter("Collision System Counter");

    @SuppressWarnings("unchecked")
    public  CollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class).exclude(IntangibleComponent.class));
        wall = Aspect.all(WallComponent.class).exclude(HealthComponent.class);
        destructibleWalls = Aspect.all(WallComponent.class, HealthComponent.class);
        enemyOnlyWalls = Aspect.all(CollisionBoundComponent.class, EnemyOnlyWallComponent.class);

        enemyBulletBlockWalls = Aspect.all(CollisionBoundComponent.class, BlockEnemyBulletComponent.class);
        playerWalls = Aspect.all(DoorComponent.class, CollisionBoundComponent.class).exclude(GrappleableComponent.class);
        platforms = Aspect.all(PlatformComponent.class, CollisionBoundComponent.class);



    }


    @Override
    protected void begin() {
        super.begin();

        performanceCounter.start();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        cbm.get(e).getRecentCollisions().clear();

        collidableobjects.clear();

        addNearByCollidableObjects(cbc.bound, collidableobjects, wall);

        if(!playerm.has(e)) {
            addNearByCollidableObjects(cbc.bound, collidableobjects, playerWalls);
        }

        if(!bulletm.has(e)){
            addNearByCollidableObjects(cbc.bound, collidableobjects, destructibleWalls);
        }

        //Enemies respect platforms players and bullets don't
        if(!playerm.has(e) && !bulletm.has(e) && !grapplem.has(e)) {
            addNearByCollidableObjects(cbc.bound, collidableobjects, platforms);
        }

        if(enemym.has(e)) {
            addNearByCollidableObjects(cbc.bound, collidableobjects, enemyOnlyWalls);

            if(bulletm.has(e)){
                addNearByCollidableObjects(cbc.bound, collidableobjects, enemyBulletBlockWalls);
            }

        }


        if(collidableobjects.size >= 2){

        }



        futureRectangle.set(cbc.bound.x, cbc.bound.y, cbc.bound.getWidth(), cbc.bound.getHeight());

        if(!bulletm.has(e)) {
            futureRectangle.x += (vc.velocity.x * world.delta);
            //System.out.println(futureRectangle.getX());
            futureRectangle.y += (vc.velocity.y * world.delta);
        } else {
            futureRectangle.x += (vc.velocity.x / 5 * world.delta);
            futureRectangle.y += (vc.velocity.y / 5 * world.delta);
        }

        //BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, futureRectangle);

        if(bulletm.has(e)) {
            System.out.println(collidableobjects.size);
        }

        for(Rectangle r : collidableobjects) {

            if(bulletm.has(e)) {
                System.out.println("Not even going to do anything");
            }

            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);

            if(c != Collider.Collision.NONE){

                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();

                if(bulletm.has(e) || grapplem.has(e)){


                    world.getSystem(OnDeathSystem.class).kill(e);
                }

            }

            cbc.getRecentCollisions().add(c);

        }



    }


    public void addNearByCollidableObjects(Rectangle r, Array<Rectangle> collidableObjects, Aspect.Builder aspect){
        EntitySubscription subscription = world.getAspectSubscriptionManager().get(aspect);
        IntBag entityIds = subscription.getEntities();

        for(int i = 0; i < entityIds.size(); i++){
            if(wm.has(entityIds.get(i))) {
                if(isNearBy(r, wm.get(entityIds.get(i)).bound)) collidableObjects.add(wm.get(entityIds.get(i)).bound);
            } else if(cbm.has(entityIds.get(i))) {
                if(isNearBy(r, cbm.get(entityIds.get(i)).bound)) collidableObjects.add(cbm.get(entityIds.get(i)).bound);
            }
        }
    }

    /**
     * Checks if two Rectangles are nearby each other. 'Nearby' is if the dynamic rectangles width/height mulitplied by 3
     * overlap the stationary rectangle
     * @param dynamic - In context the dynamic rectangle are characters in the game (players/enemies/items)
     * @param stationary - In context stationary are the walls they collide with
     * @return - Returns true if the two rectangles are nearby each other
     */
    public boolean isNearBy(Rectangle dynamic, Rectangle stationary){

        float minX = dynamic.getX() - (dynamic.getWidth() * 3 / 2 - dynamic.getWidth() / 2);
        float maxX = minX + dynamic.getWidth() * 3;

        float minY = dynamic.getY() - (dynamic.getHeight() * 3 / 2 - dynamic.getHeight() / 2);
        float maxY = minX + dynamic.getHeight() * 3;

        nearByCheckRectangle.set(minX, minY, dynamic.getWidth() * 3, dynamic.getHeight() * 3);

        return stationary.overlaps(nearByCheckRectangle);

    }


    @Override
    protected void end() {
        super.end();

        performanceCounter.stop();
        performanceCounter.tick();

/*        System.out.println("\n BLOCK");
        System.out.println(performanceCounter.name + " BLOCK");
        System.out.println("MAX " + performanceCounter.time.max);
        System.out.println("AVERAGE " + performanceCounter.time.average);
        System.out.println("LATEST " + performanceCounter.time.latest);*/
       // System.out.println(performanceCounter.time.max);

    }
}
