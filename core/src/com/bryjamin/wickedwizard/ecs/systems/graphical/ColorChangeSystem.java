package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.ecs.components.texture.ColorChangeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by BB on 10/09/2017.
 */



public class ColorChangeSystem extends EntityProcessingSystem {

    ComponentMapper<ColorChangeComponent> cm;

    ComponentMapper<TextureFontComponent> tfm;
    ComponentMapper<TextureRegionComponent> trm;
    ComponentMapper<TextureRegionBatchComponent> trbm;


    private Color temp = new Color();

    @SuppressWarnings("unchecked")
    public ColorChangeSystem() {
        super(Aspect.all(ColorChangeComponent.class));
    }



    public void setCurrentColor(Color start, Color end, Color current, float percentage) {
        float r = start.r + (end.r - start.r) * percentage;
        float g = start.g + (end.g - start.g) * percentage;
        float b = start.b + (end.b - start.b) * percentage;
        float a = start.a + (end.a - start.a) * percentage;
        current.set(r, g, b, a);
    }


    @Override
    protected void process(Entity e) {

        ColorChangeComponent cc = cm.get(e);


        float percentage = cc.currentColorChangeTime / cc.targetColorChangeTime;

        if(percentage >= 1){
            if(cc.isEndless){
                colorFlip(cc.startColor, cc.endColor);
                cc.currentColorChangeTime = 0;
                return;
            } else {
                return;
            }
        }

        cc.currentColorChangeTime += world.delta;
        setCurrentColor(cc.startColor, cc.endColor, cc.currentColor, percentage);
        applyColor(e, cc.currentColor);

    }


    private void colorFlip(Color start, Color end){

        float startR = start.r;
        float startG = start.g;
        float startB = start.b;
        float startA = start.a;

        start.set(end);

        end.set(startR, startG, startB, startA);

        System.out.println(start);
        System.out.println(end);

    }


    private void applyColor(Entity e, Color alpha){
        if(trm.has(e)) trm.get(e).color = alpha;
        if(trbm.has(e)) trbm.get(e).color = alpha;
        if(tfm.has(e)) tfm.get(e).color = alpha;
    }

}
