package basicmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardStats {
    public final int baseCost;
    public final AbstractCard.CardType cardType;
    public final AbstractCard.CardTarget cardTarget;
    public final AbstractCard.CardRarity cardRarity;
    public final AbstractCard.CardColor cardColor;

    public CardStats(AbstractCard.CardColor cardColor, AbstractCard.CardType cardType, AbstractCard.CardRarity cardRarity, AbstractCard.CardTarget cardTarget, int baseCost)
    {
        this.baseCost = baseCost;
        this.cardType = cardType;
        this.cardTarget = cardTarget;
        this.cardRarity = cardRarity;
        this.cardColor = cardColor;
    }
}