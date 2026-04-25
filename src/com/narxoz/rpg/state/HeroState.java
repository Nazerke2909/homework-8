package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * Represents a state that a hero can be in, affecting its combat behavior and actions.
 *
 * States manage their own lifecycle — they may transition themselves based on internal logic
 * (e.g., a poison state might wear off after N turns) or remain active until externally changed.
 */
public interface HeroState {

    /**
     * Returns the name of this state (e.g., "Normal", "Poisoned", "Stunned", "Berserk").
     */
    String getName();

    /**
     * Modifies the hero's outgoing damage based on this state.
     * For example, a berserk state might multiply by 1.5; a weakened state might multiply by 0.7.
     *
     * @param basePower the hero's base attack power
     * @return the modified damage value
     */
    int modifyOutgoingDamage(int basePower);

    /**
     * Modifies damage incoming to the hero based on this state.
     * For example, a defensive state might reduce damage; a weakened state might increase it.
     *
     * @param rawDamage the damage before state modification
     * @return the modified damage value
     */
    int modifyIncomingDamage(int rawDamage);

    /**
     * Called at the start of the hero's turn.
     * State-specific side effects occur here: poison damage, stun check, regeneration, etc.
     * This is where states may transition themselves or trigger effects.
     *
     * @param hero the hero in this state (may be used to transition state or trigger effects)
     */
    void onTurnStart(Hero hero);

    /**
     * Called at the end of the hero's turn.
     * State timers, countdowns, or cleanup happens here.
     * This is where a state might decrement its duration and transition.
     *
     * @param hero the hero in this state
     */
    void onTurnEnd(Hero hero);

    /**
     * Returns whether the hero can take an action this turn.
     * A stunned hero, for example, returns false; a normal or poisoned hero returns true.
     *
     * @return true if the hero can act, false if the state prevents action
     */
    boolean canAct();
}
