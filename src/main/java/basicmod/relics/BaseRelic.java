package basicmod.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
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
        super(id, TextureLoader.getTexture(relicPath(imageName + ".png")), tier, sfx);

        setPool(pool);

        this.imageName = imageName;
        loadOutline();
    }

    public BaseRelic(String id, String imageName, RelicTier tier, LandingSound sfx) {
        super(id, TextureLoader.getTexture(relicPath(imageName + ".png")), tier, sfx);

        this.imageName = imageName;
        loadOutline();
    }

    protected void loadOutline() {
        outlineImg = TextureLoader.getTextureNull(relicPath(imageName + "Outline.png"));
        if (outlineImg == null)
            outlineImg = img;
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