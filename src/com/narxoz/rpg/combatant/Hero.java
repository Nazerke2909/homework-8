package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState currentState;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.currentState = new NormalState();
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }
    public HeroState getState()    { return currentState; }

    public void setState(HeroState newState) {
        System.out.println("  " + name + " transitions from " + currentState.getName() + " to " + newState.getName());
        this.currentState = newState;
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void applyDamage(int rawDamage) {
        int modified = currentState.modifyIncomingDamage(rawDamage);
        System.out.println("  " + name + " takes " + modified + " damage (raw " + rawDamage + " modified by state " + currentState.getName() + ")");
        takeDamage(modified);
    }

    public int getModifiedAttackPower() {
        return currentState.modifyOutgoingDamage(attackPower);
    }

    public void attack(Monster monster) {
        if (!currentState.canAct()) {
            System.out.println("  " + name + " is " + currentState.getName() + " and cannot act!");
            return;
        }
        int damage = getModifiedAttackPower();
        System.out.println("  " + name + " attacks " + monster.getName() + " for " + damage + " damage (state: " + currentState.getName() + ")");
        monster.takeDamage(damage);
    }

    public void onTurnStart() {
        currentState.onTurnStart(this);
    }

    public void onTurnEnd() {
        currentState.onTurnEnd(this);
    }
}