package basicmod.cards;

import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MegaStrike extends BaseCard {
    public static final String ID = makeID(MegaStrike.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.RED,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            0
    );

    private static final int DAMAGE = 60;
    private static final int UPG_DAMAGE = 69;

    public MegaStrike() {
        super(ID, info);

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.

        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new MegaStrike();
    }
}
