package com.bryjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 04/03/2017.
 */
public class CollisionBoundComponent extends Component{

    public Rectangle bound;
    public Array<com.bryjamin.wickedwizard.utils.collider.HitBox> hitBoxes = new Array<com.bryjamin.wickedwizard.utils.collider.HitBox>();

    public boolean hitBoxDisabled = false;

    public Array<com.bryjamin.wickedwizard.utils.collider.Collider.Collision> recentCollisions = new Array<com.bryjamin.wickedwizard.utils.collider.Collider.Collision>();

    public CollisionBoundComponent(Rectangle bound){
        this.bound = bound;
    }

    public CollisionBoundComponent(Rectangle bound, boolean useBoundAsHitbox){
        this.bound = bound;
        if(useBoundAsHitbox) {
            hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(bound));
        }
    }


    public CollisionBoundComponent(Rectangle bound, com.bryjamin.wickedwizard.utils.collider.HitBox... hitBoxes){
        this.bound = bound;
        for(com.bryjamin.wickedwizard.utils.collider.HitBox hb : hitBoxes) {
            this.hitBoxes.add(hb);
        }
    }

    public CollisionBoundComponent(){
        this(new Rectangle(0,0, com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(5)));
    }

    public float getCenterX() {
        return bound.x + bound.width / 2;
    }

    public float getCenterY() {
        return bound.y + bound.height / 2;
    }

    public void setCenterX(float x) {
        bound.x = x - bound.getWidth() / 2;
    }

    public void setCenterY(float y) {
        bound.y = y - bound.getHeight() / 2;
    }

    public Array<com.bryjamin.wickedwizard.utils.collider.Collider.Collision> getRecentCollisions() {
        return recentCollisions;
    }

    public void setCenter(float x, float y) {
        setCenterX(x);
        setCenterY(y);
    }
}
