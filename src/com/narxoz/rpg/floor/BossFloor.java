package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import java.util.List;
import java.util.Random;

public class BossFloor extends TowerFloor {
    private final String floorName;
    private Monster boss;
    private final Random rand = new Random();

    public BossFloor(String name, Monster boss) {
        this.floorName = name;
        this.boss = boss;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void announce() {
        System.out.println("\n----- " + getFloorName() + " - BOSS FLOOR -----");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  The dreaded " + boss.getName() + " stands before you!");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamageTaken = 0;
        boolean heroesAlive = true;
        System.out.println("  Epic boss battle begins!");

        while (boss.isAlive() && heroesAlive) {
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();
                if (hero.getState().canAct()) {
                    hero.attack(boss);
                    if (!boss.isAlive()) break;
                }
                hero.onTurnEnd();
            }
            if (!boss.isAlive()) break;

            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                int damage = boss.getAttackPower();
                hero.applyDamage(damage);
                totalDamageTaken += Math.min(damage, hero.getHp());
                if (hero.getHp() < hero.getMaxHp() * 0.3 && rand.nextInt(100) < 50 && hero.isAlive()) {
                    System.out.println("  " + hero.getName() + " goes berserk from the boss's roar!");
                    hero.setState(new BerserkState());
                }
                if (!hero.isAlive()) {
                    System.out.println("  " + hero.getName() + " has fallen!");
                }
            }
            heroesAlive = party.stream().anyMatch(Hero::isAlive);
        }

        boolean cleared = boss.isAlive() ? false : heroesAlive;
        String summary = cleared ? "Defeated the boss!" : "Failed to defeat the boss";
        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("  Legendary loot! Full heal.");
            for (Hero hero : party) {
                if (hero.isAlive()) hero.heal(999);
            }
        }
    }
}