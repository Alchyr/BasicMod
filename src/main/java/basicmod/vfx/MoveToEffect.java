package basicmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class MoveToEffect extends AbstractGameEffect {
    private AbstractCreature creature;
    private float startX;
    private float startY;
    private float targetX;
    private float targetY;
    private boolean moveBack;

    public MoveToEffect(AbstractCreature creature, float targetX, float targetY, boolean moveBack, float duration) {
        this.creature = creature;
        this.moveBack = moveBack;
        this.startingDuration = this.duration = duration;
        this.startX = creature.animX;
        this.startY = creature.animY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public MoveToEffect(AbstractCreature creature) {
        this(creature, (float) Settings.WIDTH * 0.75F - creature.animX, AbstractDungeon.floorY - creature.animY, true, 0.75F);
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F) {
            this.isDone = true;
            this.creature.animY = this.startY;
            this.creature.animX = this.startX;
        } else {
            float tmp;
            if (this.moveBack) {
                tmp = 1.0F - Math.abs(duration / startingDuration * 2.0F - 1.0F);
            } else {
                tmp = 1.0F - this.duration / this.startingDuration;
            }

            this.creature.animX = Interpolation.pow2In.apply(this.startX, this.targetX, tmp);
            this.creature.animY = Interpolation.pow2In.apply(this.startY, this.targetY, tmp);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    @Override
    public void dispose() {
    }
}
