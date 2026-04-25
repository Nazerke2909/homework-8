package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {
    private int turnsLeft;

    public PoisonedState(int duration) {
        this.turnsLeft = duration;
    }

    @Override
    public String getName() { return "Poisoned(" + turnsLeft + ")"; }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int)(basePower * 0.7);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return rawDamage;
    }

    @Override
    public void onTurnStart(Hero hero) {
        int poisonDamage = 4;
        System.out.println("  Poison deals " + poisonDamage + " damage to " + hero.getName());
        hero.applyDamage(poisonDamage);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsLeft--;
        if (turnsLeft <= 0) {
            System.out.println("  Poison wears off for " + hero.getName());
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() { return true; }
}