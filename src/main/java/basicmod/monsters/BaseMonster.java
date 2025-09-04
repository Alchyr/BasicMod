package basicmod.monsters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import basemod.abstracts.CustomMonster;
import basicmod.BasicMod;
import basicmod.util.ModHelper;
import basicmod.vfx.MoveToEffect;

public abstract class BaseMonster extends CustomMonster {
    public MonsterStrings monsterStrings;
    public String NAME;
    public String[] MOVES;
    public String[] DIALOG;
    public int turnCount;
    public int[] damages;
    public String bgm;
    public AbstractPlayer p = AbstractDungeon.player;
    public List<MoveInfo> moveInfos = new ArrayList<>();
    public EnemyMoveInfo currMove;
    public boolean moreDamageAs, moreHPAs, specialAs;
    public float floatIndex = 0;
    public Consumer<BaseMonster> preBattleAction;

    public BaseMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float x, float y) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, x, y);
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(id);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

        dialogX = -150.0F * Settings.scale;
        dialogY = 70.0F * Settings.scale;
        
        p = AbstractDungeon.player;
        moreDamageAs = BaseMonster.moreDamageAscension(type);
        moreHPAs = BaseMonster.moreHPAscension(type);
        specialAs = BaseMonster.specialAscension(type);
    }
    
    public BaseMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float x, float y, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, x, y, ignoreBlights);
    }

    public BaseMonster(String id, String imgUrl, float hb_x, float hb_y, float hb_w, float hb_h, float x, float y) {
        this(CardCrawlGame.languagePack.getMonsterStrings(BasicMod.makeID(id)).NAME,
                id,
                1,
                hb_x, hb_y,
                hb_w, hb_h,
                imgUrl,
                x, y);
    }

    public BaseMonster(String id, float hb_x, float hb_y, float hb_w, float hb_h, float x, float y) {
        this(CardCrawlGame.languagePack.getMonsterStrings(BasicMod.makeID(id)).NAME,
                id,
                1,
                hb_x, hb_y,
                hb_w, hb_h,
                BasicMod.monsterPath(id + ".png"),
                x, y);
    }

    public BaseMonster(String id, float hb_w, float hb_h, float x, float y) {
        this(CardCrawlGame.languagePack.getMonsterStrings(BasicMod.makeID(id)).NAME,
                id,
                1,
                0, -15F,
                hb_w, hb_h,
                BasicMod.monsterPath(id + ".png"),
                x, y);
    }

    public BaseMonster(String id, float hb_w, float hb_h) {
        this(CardCrawlGame.languagePack.getMonsterStrings(BasicMod.makeID(id)).NAME,
                id,
                1,
                0, -15F,
                hb_w, hb_h,
                BasicMod.monsterPath(id + ".png"),
                0, 0);
    }

    public BaseMonster process(Consumer<BaseMonster> consumer) {
        consumer.accept(this);
        return this;
    }

    public BaseMonster modifyHpByPercent(float percent) {
        setHp((int) (maxHealth * percent));
        return this;
    }

    public BaseMonster modifyHp(int modifyAmount) {
        setHp(maxHealth + modifyAmount);
        return this;
    }

    public BaseMonster setPreBattleAction(Consumer<BaseMonster> action) {
        preBattleAction = action;
        return this;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (bgm != null) {
            AbstractDungeon.scene.fadeOutAmbiance();
            CardCrawlGame.music.silenceTempBgmInstantly();
            AbstractDungeon.getCurrRoom().playBgmInstantly(bgm);
        }
        if (preBattleAction != null) {
            preBattleAction.accept(this);
        }
    }

    @Override
    public void takeTurn() {
        if (nextMove >= 0 && nextMove < moveInfos.size()) {
            moveInfos.get(nextMove).move();
        } else {
            BasicMod.logger.info("Monster {} has no {} move!", NAME, nextMove);
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        if (floatIndex != 0)
            this.animY = floatIndex * MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
    }

    public void setDamagesWithAscension(int... damages) {
        this.damage.clear();
        this.damages = damages;
        for (int i = 0; i < damages.length; i++) {
            damages[i] = Math.round(damages[i] * (BaseMonster.moreDamageAscension(type) ? 1.1f : 1));
            this.damage.add(new DamageInfo(this, damages[i]));
        }
    }

    public void setDamages(int... damages) {
        this.damage.clear();
        this.damages = damages;
        for (int j : damages) {
            this.damage.add(new DamageInfo(this, j));
        }
    }

    public void addDamageActions(AbstractCreature target, int index, int numTimes, AbstractGameAction.AttackEffect effect) {
        for (int i = 0; i < numTimes; i++) {
            addToBot(new DamageAction(target, this.damage.get(index), effect));
        }
    }

    public void addMove(Intent intent, int dmg, Supplier<Integer> dmgTimeSupplier, Consumer<MoveInfo> takeMove) {
        int index = moveInfos.size();
        this.damage.add(new DamageInfo(this, dmg));
        moveInfos.add(new MoveInfo(index, intent, () -> damage.get(index).base, dmgTimeSupplier, takeMove));
    }

    public void addMoveA(Intent intent, int dmg, Supplier<Integer> dmgTime, Consumer<MoveInfo> takeMove) {
        if (BaseMonster.moreDamageAscension(type)) dmg = Math.round(dmg * 1.1f);
        addMove(intent, dmg, dmgTime, takeMove);
    }

    public void addMove(Intent intent, int dmg, int dmgTime, Consumer<MoveInfo> takeMove) {
        addMove(intent, dmg, () -> dmgTime, takeMove);
    }

    public void addMoveA(Intent intent, int dmg, int dmgTime, Consumer<MoveInfo> takeMove) {
        if (BaseMonster.moreDamageAscension(type)) dmg = Math.round(dmg * 1.1f);
        addMove(intent, dmg, () -> dmgTime, takeMove);
    }

    public void addMove(Intent intent, int dmg, Consumer<MoveInfo> takeMove) {
        addMove(intent, dmg, () -> 1, takeMove);
    }

    public void addMoveA(Intent intent, int dmg, Consumer<MoveInfo> takeMove) {
        if (BaseMonster.moreDamageAscension(type)) dmg = Math.round(dmg * 1.1f);
        addMove(intent, dmg, () -> 1, takeMove);
    }

    public void addMove(Intent intent, Consumer<MoveInfo> takeMove) {
        addMove(intent, 0, () -> 0, takeMove);
    }

    public void setMove(int i) {
        MoveInfo info = moveInfos.get(i);
        int dmgTime = info.damageTimeSupplier.get();
        if (dmgTime <= 0) {
            setMove(MOVES[i], (byte) i, info.intent);
        } else if (dmgTime == 1) {
            setMove(MOVES[i], (byte) i, info.intent, damage.get(i).base);
        } else {
            setMove(MOVES[i], (byte) i, info.intent, damage.get(i).base, dmgTime, true);
        }
    }

    public void attack(MoveInfo info, AbstractGameAction.AttackEffect effect, AttackAnim anim) {
        if (anim != null)
            switch (anim) {
                case FAST:
                    addToBot(new AnimateFastAttackAction(this));
                    break;
                case SLOW:
                    addToBot(new AnimateSlowAttackAction(this));
                    break;
                case HOP:
                    addToBot(new AnimateHopAction(this));
                    break;
                case JUMP:
                    addToBot(new AnimateJumpAction(this));
                    break;
                case MOVE:
                    addToBot(new VFXAction(new MoveToEffect(this, p.hb.cX + p.hb.width / 2 - hb.cX, p.hb.cY - hb.cY, true, 0.4f)));
                    break;
            }
        for (int i = 0; i < info.damageTimeSupplier.get(); i++) {
            addToBot(new DamageAction(p, this.damage.get(info.index), effect));
        }
    }

    public void attack(MoveInfo info, AbstractGameAction.AttackEffect effect) {
        attack(info, effect, null);
    }

    public String getLastMove() {
        return MOVES[MOVES.length - 1];
    }

    
    public static boolean moreDamageAscension(AbstractMonster.EnemyType type) {
        int level = 2;
        switch (type) {
            case NORMAL:
                level = 2;
                break;
            case ELITE:
                level = 3;
                break;
            case BOSS:
                level = 4;
                break;
        }
        return AbstractDungeon.ascensionLevel >= level;
    }

    public static boolean moreHPAscension(AbstractMonster.EnemyType type) {
        int level = 7;
        switch (type) {
            case NORMAL:
                level = 7;
                break;
            case ELITE:
                level = 8;
                break;
            case BOSS:
                level = 9;
                break;
        }
        return AbstractDungeon.ascensionLevel >= level;
    }

    public static boolean specialAscension(AbstractMonster.EnemyType type) {
        int level = 17;
        switch (type) {
            case NORMAL:
                level = 17;
                break;
            case ELITE:
                level = 18;
                break;
            case BOSS:
                level = 19;
                break;
        }
        return AbstractDungeon.ascensionLevel >= level;
    }
    
    public static boolean eventAscension() {
        return AbstractDungeon.ascensionLevel >= 15;
    }

    public void shout(int index, float volume) {
        if (index >= DIALOG.length) return;
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + index, volume));
        addToBot(new ShoutAction(this, DIALOG[index]));
    }

    public void shout(int index) {
        shout(index, 1.0F);
    }

    public void shout (int index, String sound) {
        shout(index, sound, 1.0F);
    }

    public void shout(int index, String sound, float volume) {
        if (index >= DIALOG.length) return;
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(sound, volume));
        addToBot(new ShoutAction(this, DIALOG[index]));
        
    }

    public void shout(int start, int end, float volume) {
        shout(AbstractDungeon.miscRng.random(start, end), volume);
    }

    public void shout(int start, int end) {
        shout(AbstractDungeon.miscRng.random(start, end), 3.0F);
    }

    public void shoutIf(int index) {
        if (AbstractDungeon.miscRng.randomBoolean()) {
            shout(index);
        }
    }

    public void shoutIf(int index, int chance) {
        if (chance < AbstractDungeon.miscRng.random(100)) {
            shout(index);
        }
    }
    
    public static class MoveInfo {
        public int index;
        public AbstractMonster.Intent intent;
        public Consumer<MoveInfo> takeMove;
        public Supplier<Integer> damageSupplier;
        public Supplier<Integer> damageTimeSupplier;

        public MoveInfo(int index, Intent intent, Supplier<Integer> damageSupplier, Supplier<Integer> damageTimeSupplier, Consumer<MoveInfo> takeMove) {
            this.index = index;
            this.intent = intent;
            this.takeMove = takeMove;
            this.damageSupplier = damageSupplier;
            this.damageTimeSupplier = damageTimeSupplier;
        }

        public void move() {
            takeMove.accept(this);
        }
    }

    public enum AttackAnim {
        FAST,
        SLOW,
        HOP,
        JUMP,
        MOVE,
    }

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {String.class, String.class, int.class, float.class, float.class, float.class, float.class, String.class, float.class, float.class, boolean.class})
    public static class MonsterMoveInfoPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster __instance, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights, EnemyMoveInfo ___move) {
            if (__instance instanceof BaseMonster) {
                ((BaseMonster) __instance).currMove = ___move;
            }
        }
    }
}
