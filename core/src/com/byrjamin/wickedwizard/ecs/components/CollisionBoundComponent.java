package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 04/03/2017.
 */
public class CollisionBoundComponent extends Component{

    public Rectangle bound;
    public Array<HitBox> hitBoxes = new Array<HitBox>();

    public boolean hitBoxDisabled = false;

    public Array<Collider.Collision> recentCollisions = new Array<Collider.Collision>();

    public CollisionBoundComponent(Rectangle bound){
        this.bound = bound;
    }

    public CollisionBoundComponent(Rectangle bound, boolean useBoundAsHitbox){
        this.bound = bound;
        if(useBoundAsHitbox) {
            hitBoxes.add(new HitBox(bound));
        }
    }


    public CollisionBoundComponent(Rectangle bound, HitBox... hitBoxes){
        this.bound = bound;
        for(HitBox hb : hitBoxes) {
            this.hitBoxes.add(hb);
        }
    }

    public CollisionBoundComponent(){
        this(new Rectangle(0,0, Measure.units(5), Measure.units(5)));
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

    public Array<Collider.Collision> getRecentCollisions() {
        return recentCollisions;
    }

    public void setCenter(float x, float y) {
        setCenterX(x);
        setCenterY(y);
    }
}
