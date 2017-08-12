package com.byrjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemDangerSensor implements Item {




    @Override
    public boolean applyEffect(World world, Entity player) {

        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(player.getComponent(CollisionBoundComponent.class).getCenterX(), player.getComponent(CollisionBoundComponent.class).getCenterY()));
        e.edit().add(new FiringAIComponent(0));
        e.edit().add(new CollisionBoundComponent(new Rectangle(player.getComponent(CollisionBoundComponent.class).getCenterX(), player.getComponent(CollisionBoundComponent.class).getCenterY(),1,1)));
        e.edit().add(new FollowPositionComponent(player.getComponent(PositionComponent.class).position, player.getComponent(CollisionBoundComponent.class).bound.width / 2,  player.getComponent(CollisionBoundComponent.class).bound.height / 2));
        e.edit().add(new WeaponComponent(new MultiPistol.PistolBuilder(world.getSystem(RenderingSystem.class).assetManager)
                .shotSpeed(Measure.units(50f))
                .shotScale(2)
                .color(new Color(Color.WHITE))
                .angles(0, 90, 180)
                .enemy(false)
                .damage(1f)
                .friendly(true)
                .fireRate(3f)
                .build()));

        e.edit().add(new ChildComponent(player.getComponent(ParentComponent.class)));




       // player.getComponent(StatComponent.class).accuracy += PresetStatIncrease.minor;
       // player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Accuracy.Ace;
    }

}
