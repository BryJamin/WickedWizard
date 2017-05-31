package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.GrappleableComponent;

/**
 * Created by Home on 18/03/2017.
 */

public class GrapplePointSystem extends EntitySystem {

    private Array<Rectangle> grapples = new Array<Rectangle>();

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<GrappleableComponent> gm;


    @SuppressWarnings("unchecked")
    public GrapplePointSystem() {
        super(Aspect.all(GrappleableComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {


        grapples.clear();

        for(Entity e : this.getEntities()){
            grapples.add(e.getComponent(CollisionBoundComponent.class).bound);
        }




    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }


    public Vector2 canGrappleTo(float inputX, float inputY){
        for(Rectangle r : grapples){
            if(r.contains(inputX, inputY)){
                Vector2 destination = new Vector2();
                destination.x = r.getX() + r.getWidth() / 2;
                destination.y = r.getY() + r.getHeight() / 2;
                return destination;
            }
        }
        return null;
    }


    public Rectangle returnTouchedGrapple(float inputX, float inputY){
        for(Rectangle r : grapples){
            if(r.contains(inputX, inputY)){
                return r ;
            }
        }
        return null;
    }

    public boolean touchedGrapple(float inputX, float inputY){
        for(Rectangle r : grapples){
            if(r.contains(inputX, inputY)){
                return true;
            }
        }
        return false;
    }


}
