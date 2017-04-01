package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WallComponent;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;

/**
 * Created by Home on 22/03/2017.
 */

public class MoveToSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;

    @SuppressWarnings("unchecked")
    public MoveToSystem() {
        super(Aspect.all(PositionComponent.class, MoveToComponent.class, VelocityComponent.class, AccelerantComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        MoveToComponent mtc = mtm.get(e);
        AccelerantComponent ac = am.get(e);

        Float targetX = mtc.targetX;

        if(targetX != null){

            CollisionBoundComponent cbc = cbm.get(e);

            float currentPosition = (cbm.has(e)) ? cbm.get(e).getCenterX() : pc.getX();

            if (cbc.bound.contains(mtc.targetX, cbc.getCenterY())) {
                vc.velocity.x = mtc.endSpeedX; //For grappling
                mtc.targetX = null;
            } else if (currentPosition >= targetX) {
                vc.velocity.x = mtc.speedX;
                //vc.velocity.add(mtc.speedX, 0);//(vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
            } else {
                vc.velocity.x = mtc.speedX;
                //vc.velocity.add(mtc.speedX, 0); //(vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
            }
        }

        Float targetY = mtc.targetY;

        if(targetY != null){

            CollisionBoundComponent cbc = cbm.get(e);

            float currentPosition = (cbm.has(e)) ? cbm.get(e).getCenterY() : pc.getY();

            if (cbc.bound.contains(cbc.getCenterX(), mtc.targetY)) {
                vc.velocity.y = mtc.endSpeedY; //For grappling
                mtc.targetY = null;
            } else if (currentPosition >= targetY) {
                vc.velocity.y = mtc.speedY;
                //vc.velocity.add(0, mtc.speedY); //(vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
            } else {
                vc.velocity.y = mtc.speedY;
                //vc.velocity.add(0, mtc.speedY); //(vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
            }
        }




    }


    public void flyTo(double angleOfTravel, float distanceOfTravel, float speedOfTravel, MoveToComponent mtc, CollisionBoundComponent cbc){

        float cosine = (float) Math.cos(angleOfTravel);
        float sine = (float) Math.sin(angleOfTravel);

        Vector2 flyPath = flyPathCheck(angleOfTravel, distanceOfTravel, cbc.bound);

        mtc.targetX = flyPath.x;
        mtc.targetY = flyPath.y;

        mtc.speedX = cosine * speedOfTravel;
        mtc.speedY = sine * speedOfTravel;

/*        mtc.endSpeedX = 0;
        mtc.endSpeedY = 0;*/

    }



    public Vector2 flyPathCheck(double angleOfTravel, float distanceOfTravel, Rectangle startingBound){

        float cosine = (float) Math.cos(angleOfTravel);
        float sine = (float) Math.sin(angleOfTravel);

        float xDistance = (cosine * distanceOfTravel);
        float yDistance = (sine * distanceOfTravel);;

        float targetX = (startingBound.x + startingBound.getWidth() / 2) + xDistance; //input.x; //gradientX * GRAPPLE_MOVEMENT;
        float targetY = (startingBound.y + startingBound.getHeight() / 2) + yDistance; //input.y; //gradientY * GRAPPLE_MOVEMENT;


        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(WallComponent.class));
        IntBag wallComponentIds = subscription.getEntities();

        boolean useX = xDistance > yDistance;
        float mockTravelPathDistance = (useX) ? startingBound.getWidth() : startingBound.getHeight();

        Array<Rectangle> mockPath = new Array<Rectangle>();
        mockPath.add(startingBound);

        boolean pathNotFound = true;

        do {

            //TODO is it possible to endlessly loop?

            Rectangle r = mockPath.peek();
            float x = r.x + (cosine * mockTravelPathDistance);
            float y = r.y + (sine * mockTravelPathDistance);

            Rectangle newR = new Rectangle(x,y,r.width, r.height);

            for(int i = 0; i < wallComponentIds.size(); i++) {
                Rectangle wall = wm.get(wallComponentIds.get(i)).bound;
                if(newR.overlaps(wall)){
                    Collider.collision(r, newR, wall);
                    targetX = r.x + r.getWidth() / 2;
                    targetY = r.y + r.getHeight() / 2;
                    pathNotFound = false;
                    break;
                }

                if((Math.abs(startingBound.x - newR.x)) > xDistance &&
                        Math.abs(startingBound.y - newR.y) > yDistance)
                {
                    pathNotFound = false;
                    break;
                }
            }

            if(pathNotFound) {
                mockPath.add(newR);
            }



        } while(pathNotFound);


        return new Vector2(targetX, targetY);

    }


    public static void moveTo(float target, float currentPosition, AccelerantComponent ac, VelocityComponent vc){
        if (target - 20 <= currentPosition && currentPosition < target + 20) {
            vc.velocity.x = 0;
        } else if (currentPosition >= target) {
            vc.velocity.x  = (vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
        } else {
            vc.velocity.x  = (vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
        }
    }

    public static void decelerate(AccelerantComponent ac, VelocityComponent vc){
        if (vc.velocity.x >= -50 && vc.velocity.x < 50) {
            vc.velocity.x = 0;
        }else if(vc.velocity.x >= 0) {
            vc.velocity.x = (vc.velocity.x - ac.accelX <= 0) ? 0 : vc.velocity.x - ac.accelX;
        } else if(vc.velocity.x <= 0){
            vc.velocity.x = (vc.velocity.x + ac.accelX >= 0) ? 0 : vc.velocity.x + ac.accelX;
        }
    }


}

