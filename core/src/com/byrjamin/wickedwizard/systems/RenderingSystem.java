package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.TextureRegionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Home on 04/03/2017.
 */
public class RenderingSystem extends EntitySystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<TextureRegionComponent> trm;

    private ArrayList<Entity> orderedEntities;

    public SpriteBatch batch;
    public OrthographicCamera gamecam;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch, OrthographicCamera gamecam) {
        super(Aspect.all(PositionComponent.class, TextureRegionComponent.class));
        this.batch = batch;
        this.gamecam = gamecam;
        orderedEntities = new ArrayList<Entity>();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(gamecam.combined);
        if(!batch.isDrawing()) {
            batch.begin();
        }
    }

    @Override
    protected void processSystem() {
        for (int i = 0; orderedEntities.size() > i; i++) {
            process(orderedEntities.get(i));
        }
    }

    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        TextureRegionComponent trc = trm.get(e);

        float originX = trc.width * 0.5f;
        float originY = trc.height * 0.5f;


        batch.setColor(trc.color);
        batch.draw(trc.region,
                pc.getX() + trc.offsetX , pc.getY() + trc.offsetY,
                originX, originY,
                trc.width, trc.height,
                trc.scaleX, trc.scaleY,
                trc.rotation);
        batch.setColor(Color.WHITE);

    }

    @Override
    protected void end() {
        batch.end();
    }


    @Override
    public void inserted(Entity e) {
        orderedEntities.add(e);
        Collections.sort(orderedEntities, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                TextureRegionComponent t1 = trm.get(e1);
                TextureRegionComponent t2 = trm.get(e2);

                return ((Integer)t1.layer).compareTo(t2.layer);
            }
        });
    }

    @Override
    public void removed(Entity e) {
        orderedEntities.remove(e);
    }
    
}
