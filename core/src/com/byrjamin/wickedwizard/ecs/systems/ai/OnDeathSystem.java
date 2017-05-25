package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.MinionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 01/04/2017.
 */

public class OnDeathSystem  extends BaseSystem {

    ComponentMapper<OnDeathComponent> odm;
    ComponentMapper<LootComponent> lm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ParentComponent> parentm;
    ComponentMapper<ChildComponent> cm;

    ComponentMapper<MinionComponent> mm;

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public void kill(Entity deadEntity) {

        PositionComponent pc = pm.get(deadEntity);

        if(odm.has(deadEntity)){

            for (ComponentBag bag : odm.get(deadEntity).getComponentBags()) {
                Entity newEntity = world.createEntity();
                for (Component c : bag) {

                    newEntity.edit().add(c);
                }


                if(cbm.has(deadEntity)){
                    CollisionBoundComponent cbc = cbm.get(deadEntity);

                    if(cbm.has (newEntity)) {
                        CollisionBoundComponent odcbc = cbm.get(newEntity);

                        newEntity.getComponent(PositionComponent.class).position = new Vector3(cbc.getCenterX(), cbc.getCenterY(), 0);

                        if(cbm.has(newEntity)){



                            Rectangle r = newEntity.getComponent(CollisionBoundComponent.class).bound;
                            r.x = cbc.getCenterX() - r.getWidth() / 2;
                            r.y = cbc.getCenterY() - r.getHeight() / 2;


                            PositionComponent newEntityPc = newEntity.getComponent(PositionComponent.class);

                            newEntityPc.position.x = r.x;
                            newEntityPc.position.y = r.y;

                            System.out.println(r.x + "X");
                            System.out.println(r.y + "y");

                        }


                    } else {

                        newEntity.getComponent(PositionComponent.class).position = new Vector3(pc.position);
                    }

                } else {

                    newEntity.getComponent(PositionComponent.class).position = new Vector3(pc.position);
                }
            }
        }

        if(lm.has(deadEntity) && cbm.has(deadEntity) && !mm.has(deadEntity)) {
            CollisionBoundComponent cbc = cbm.get(deadEntity);
            world.getSystem(LuckSystem.class).spawnPickUp(cbc.getCenterX(), cbc.getCenterY());
        }

        if(cm.has(deadEntity)){
            Entity parent = world.getSystem(FindChildSystem.class).findParentEntity(cm.get(deadEntity));
            parent.getComponent(ParentComponent.class).children.removeValue(cm.get(deadEntity), true);
        }

        if(parentm.has(deadEntity)){
            Array<ChildComponent> children = new Array<ChildComponent>();
            children.addAll(parentm.get(deadEntity).children);

            for(ChildComponent c : children){
                Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);
                if(child != null) {
                    kill(child);
                }
                parentm.get(deadEntity).children.removeValue(c, true);
            }

        }

        deadEntity.deleteFromWorld();


        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all());
        IntBag entityIds = subscription.getEntities();
     //   System.out.println(entityIds.size());
    }


}
