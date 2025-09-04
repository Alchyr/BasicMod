package basicmod.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ModHelper {
    public static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static void addToBotAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public static void addToTopAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }
    
    public static void addEffectAbstract(Lambda func) {
        addEffectAbstract(func, true);
    }

    public static void addEffectAbstract(Lambda func, boolean topLevel) {
        AbstractGameEffect effect = new AbstractGameEffect() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };
        
        if (topLevel) {
            AbstractDungeon.topLevelEffectsQueue.add(effect);
        } else {
            AbstractDungeon.effectsQueue.add(effect);
        }
    }

    public interface Lambda extends Runnable {}

    public static List<FindResult> findCardsInGroup(Predicate<AbstractCard> predicate, CardGroup group) {
        List<FindResult> result = new ArrayList<>();
        for (AbstractCard card : group.group) {
            if (predicate.test(card)) {
                FindResult findResult = new FindResult();
                findResult.card = card;
                findResult.group = group;
                result.add(findResult);
            }
        }
        return result;
    }

    public static List<FindResult> findCards(Predicate<AbstractCard> predicate, boolean hand, boolean discard, boolean draw, boolean exhaust, boolean limbo) {
        List<FindResult> result = new ArrayList<>();
        if (hand) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.hand));
        if (discard) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.discardPile));
        if (draw) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.drawPile));
        if (exhaust) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.exhaustPile));
        if (limbo) result.addAll(findCardsInGroup(predicate, AbstractDungeon.player.limbo));
        return result;
    }

    public static List<FindResult> findCards(Predicate<AbstractCard> predicate, boolean shuffle) {
        List<FindResult> result = findCards(predicate);
        if (shuffle) {
            Collections.shuffle(result, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
        }
        return result;
    }

    public static List<FindResult> findCards(Predicate<AbstractCard> predicate) {
        return findCards(predicate, true, true, true, true, false);
    }

    public static class FindResult {
        public AbstractCard card;
        public CardGroup group;
    }

    public static AbstractMonster betterGetRandomMonster() {
        return getRandomMonster(m -> !(m.isDying || m.isEscaping || m.halfDead || m.currentHealth <= 0), true);
    }
    
    public static boolean check(AbstractCreature m) {
        return !(m == null || m.isDying || m.isEscaping || m.halfDead || m.currentHealth <= 0);
    }

    public static AbstractMonster getRandomMonster(Predicate<AbstractMonster> predicate, boolean aliveOnly) {
        MonsterGroup group = AbstractDungeon.getCurrRoom().monsters;
        Random rng = AbstractDungeon.cardRandomRng;

        if (group.areMonstersBasicallyDead()) {
            return null;
        }

        if (group.monsters.size() == 1) {
            AbstractMonster m = group.monsters.get(0);
            if (predicate != null && !predicate.test(m)) {
                return null;
            }
            if (aliveOnly && !check(m)) {
                return null;
            }
            return m;
        }

        List<AbstractMonster> filtered = group.monsters.stream()
            .filter(m -> (predicate == null || predicate.test(m)))
            .filter(m -> !aliveOnly || (!m.halfDead && !m.isDying && !m.isEscaping && check(m)))
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return null;
        }

        return filtered.get(rng.random(0, filtered.size() - 1));
    }

    
    public static AbstractMonster getMonsterWithMaxHealth() {
        if (AbstractDungeon.currMapNode == null
                || AbstractDungeon.getMonsters() == null
                || AbstractDungeon.getMonsters().monsters == null) {
            return null;
        }
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
                .filter(ModHelper::check)
                .max(Comparator.comparingInt(m -> m.maxHealth))
                .orElse(null);
    }

    public static boolean hasRelic(String relicID) {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(relicID);
    }

    public static boolean hasBuff(AbstractCreature creature, AbstractPower.PowerType type) {
        return creature.powers.stream().anyMatch(power -> power.type == type);
    }
    
    public static int getPowerCount(AbstractCreature creature, String powerID) {
        return creature.hasPower(powerID) ? creature.getPower(powerID).amount : 0;
    }

}
