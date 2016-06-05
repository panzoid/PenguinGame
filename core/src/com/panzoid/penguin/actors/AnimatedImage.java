package com.panzoid.penguin.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by weipa on 5/22/16.
 */
public class AnimatedImage extends Image
{
    protected Animation[] animations = null;
    private float stateTime;
    private int state;

    public AnimatedImage(Animation[] animations) {
        super(animations[0].getKeyFrame(0));
        setState(0);
        this.animations = animations;
    }

    public void setState(int state) {
        this.state = state;
        stateTime = 0;
    }

    @Override
    public void act(float delta)
    {
        ((TextureRegionDrawable)getDrawable()).setRegion(animations[state].getKeyFrame(stateTime+=delta, true));
        super.act(delta);
    }
}
