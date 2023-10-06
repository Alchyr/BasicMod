package basicmod.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basicmod.util.GeneralUtils;
import basicmod.util.TextureLoader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import static basicmod.BasicMod.relicPath;

public abstract class BaseRelic extends CustomRelic {
    public AbstractCard.CardColor pool = null;
    public RelicType relicType = RelicType.SHARED;
    protected String imageName;

    //for character specific relics
    public BaseRelic(String id, String imageName, AbstractCard.CardColor pool, RelicTier tier, LandingSound sfx) {
        this(id, imageName, tier, sfx);

        setPool(pool);
    }

    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        this(id, GeneralUtils.removePrefix(id), tier, sfx);
    }
    public BaseRelic(String id, String imageName, RelicTier tier, LandingSound sfx) {
        super(id, "", tier, sfx);

        this.imageName = imageName;
        loadTexture();
    }

    protected void loadTexture() {
        this.img = TextureLoader.getTextureNull(relicPath(imageName + ".png"), true);
        if (img != null) {
            outlineImg = TextureLoader.getTextureNull(relicPath(imageName + "Outline.png"), true);
            if (outlineImg == null)
                outlineImg = img;
        }
        else {
            ImageMaster.loadRelicImg("Derp Rock", "derpRock.png");
            this.img = ImageMaster.getRelicImg("Derp Rock");
            this.outlineImg = ImageMaster.getRelicOutlineImg("Derp Rock");
        }
    }

    @Override
    public void loadLargeImg() {
        if (largeImg == null) {
            this.largeImg = ImageMaster.loadImage(relicPath("large/" + imageName + ".png"));
        }
    }

    private void setPool(AbstractCard.CardColor pool) {
        switch (pool) { //Basegame pools are handled differently
            case RED:
                relicType = RelicType.RED;
                break;
            case GREEN:
                relicType = RelicType.GREEN;
                break;
            case BLUE:
                relicType = RelicType.BLUE;
                break;
            case PURPLE:
                relicType = RelicType.PURPLE;
                break;
            default:
                this.pool = pool;
                break;
        }
    }
}