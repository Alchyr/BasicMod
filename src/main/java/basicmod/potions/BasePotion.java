package basicmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.lang.reflect.Field;
import java.util.List;

public abstract class BasePotion extends AbstractPotion {
    private static final Field liquidImg, hybridImg, spotsImg;
    static {
        try {
            liquidImg = AbstractPotion.class.getDeclaredField("liquidImg");
            hybridImg = AbstractPotion.class.getDeclaredField("hybridImg");
            spotsImg = AbstractPotion.class.getDeclaredField("spotsImg");

            liquidImg.setAccessible(true);
            hybridImg.setAccessible(true);
            spotsImg.setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to access potion image fields.", e);
        }
    }

    public PotionStrings potionStrings;
    public int basePotency;
    public AbstractPlayer.PlayerClass playerClass = null;

    public BasePotion(String id, int potency, PotionRarity rarity, PotionSize shape, Color liquidColor, Color hybridColor, Color spotsColor) {
        super("", id, rarity, shape, PotionEffect.NONE, liquidColor, hybridColor, spotsColor);
        basePotency = potency;
        checkColors();
        initializeData();
    }

    public BasePotion(String id, int potency, PotionRarity rarity, PotionSize size, PotionColor color) {
        super("", id, rarity, size, color);
        basePotency = potency;
        checkColors();
        initializeData();
    }

    protected void checkColors() {
        if (hybridColor != null && getHybridImg() == null) {
            throw new RuntimeException("Potion " + ID + " has hybridColor but no hybridImg; if this is intentional, override checkColors. Otherwise, set hybridColor to null or provide a Texture with setHybridImg.");
        }
        if (spotsColor != null && getSpotsImg() == null) {
            throw new RuntimeException("Potion " + ID + " has spotsColor but no spotsImg; if this is intentional, override checkColors. Otherwise, set spotsColor to null or provide a Texture with setSpotsImg.");
        }
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();

        potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
        this.name = potionStrings.NAME;
        this.description = getDescription();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        List<PowerTip> extraTips = getAdditionalTips();
        if (extraTips != null) {
            this.tips.addAll(extraTips);
        }
    }

    @Override
    public int getPotency(int ascension) {
        return basePotency;
    }

    public abstract String getDescription();
    public List<PowerTip> getAdditionalTips() {
        return null;
    }

    public Texture getLiquidImg() {
        try {
            return (Texture) liquidImg.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public Texture getHybridImg() {
        try {
            return (Texture) hybridImg.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public Texture getSpotsImg() {
        try {
            return (Texture) spotsImg.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLiquidImg(Texture t) {
        try {
            liquidImg.set(this, t);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void setHybridImg(Texture t) {
        try {
            hybridImg.set(this, t);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void setSpotsImg(Texture t) {
        try {
            spotsImg.set(this, t);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        try {
            return this.getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to auto-generate makeCopy for potion: " + this.ID);
        }
    }
}
