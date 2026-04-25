# Homework 8 — The Haunted Tower: Ascending the Floors

## Learning Objectives

By completing this assignment, you will:

- Apply the **State** pattern to manage per-entity behavior changes that can self-transition
- Apply the **Template Method** pattern to define algorithm skeletons that subclasses specialize
- Discover how two behavioral patterns can operate *independently* within a shared system
- Practice building extensible class hierarchies using inheritance and polymorphism

---

## What Is Provided

The following files are given to you. **Do not modify them.**

| File | Purpose |
|------|---------|
| `state/HeroState.java` | State interface |
| `floor/TowerFloor.java` | Abstract class with template method |
| `floor/FloorResult.java` | Floor exploration result data class |
| `combatant/Hero.java` | Hero skeleton |
| `combatant/Monster.java` | Monster concrete class |
| `tower/TowerRunResult.java` | Tower run result data class |
| `Main.java` | Entry point skeleton |

---

## What You Must Build

You are building **The Haunted Tower: Ascending the Floors** — a sequence of increasingly challenging floors where each hero manages its own status effects (poison, stun, regeneration, berserk) that modify combat and can self-transition. Each floor presents a distinct challenge structured by the Template Method pattern.

---

## Part 1: State Pattern

### The Interface

`HeroState` defines how a hero behaves while in a particular state:

```java
String getName();
int modifyOutgoingDamage(int basePower);      // scale the hero's attack
int modifyIncomingDamage(int rawDamage);      // scale damage received
void onTurnStart(Hero hero);                  // side effects at turn start
void onTurnEnd(Hero hero);                    // side effects at turn end
boolean canAct();                             // false if state prevents acting (e.g. stunned)
```

### Hero States

Implement at least **3 concrete `HeroState` implementations**:

- **A baseline neutral state** that leaves damage and action unmodified
- **At least one self-transitioning state** that either wears off after N turns or flips based on a condition (e.g. HP threshold). Demonstrate that states can manage their own lifecycle without external intervention.
- **At least one buff or debuff state** that modifies *both outgoing and incoming damage*. Examples: a poison state that reduces outgoing damage and applies passive damage; a berserk state that boosts outgoing damage but reduces defense; a regeneration state that triggers healing on turn start.

**Critical requirement:** Heroes must transition between states **at runtime** based on game events or state-internal logic. States are not set once and forgotten — they must be *actively swappable*. At least one state must have `canAct()` return `false`, demonstrating that the State pattern changes the control flow of the hero's turn.

---

## Part 2: Template Method Pattern

### The Abstract Class

`TowerFloor` defines the skeleton of exploring a floor:

```java
public final FloorResult explore(List<Hero> party) {
    announce();                          // step 1: concrete default
    setup(party);                        // step 2: abstract
    FloorResult r = resolveChallenge(party);  // step 3: abstract
    if (shouldAwardLoot(r)) {            // hook with default=true
        awardLoot(party, r);             // step 4: abstract
    }
    cleanup(party);                      // hook: optional cleanup
    return r;
}
```

Key structural points:
- `explore()` is `final` and **never overridden**
- Abstract steps (`setup`, `resolveChallenge`, `awardLoot`) are implemented by subclasses
- Hooks (`announce`, `shouldAwardLoot`, `cleanup`) are non-abstract and may be overridden by subclasses to customize behavior
- The step order is fixed and visible in the code — this is the defining feature of Template Method

### TowerFloor Subclasses

Implement at least **3 distinct floor types**, each as a concrete `TowerFloor` subclass:

- All subclasses must use the inherited `explore()` template method; **none may override `explore()`**
- Each subclass must implement all abstract steps
- At least one subclass must **override an optional hook** (e.g. a rest floor overrides `shouldAwardLoot()` to return `false`; a boss floor overrides `announce()` to print an intimidating message). This demonstrates the hook mechanism and shows subclasses customizing behavior within the fixed skeleton.
- At least one subclass must produce a non-trivial `resolveChallenge()` that interacts meaningfully with the party — monsters fight, puzzles are solved, traps trigger — and may cause state transitions in heroes.

---

## Part 3: Tower Runner and Demo (Main.java)

### Tower Runner Logic

Design and implement a tower runner that:
- Executes a sequence of floors in order
- Tracks which floors have been cleared
- Identifies when heroes fall or the tower is defeated
- Returns a `TowerRunResult` with summary statistics

### Demo Requirements (Main.java)

Your `Main.java` must demonstrate all of the following:

1. At least **2 heroes** starting with different initial states
2. A sequence of **≥ 4 floors** of at least **3 distinct subclass types**
3. At least one hero **transitioning through ≥ 2 states visibly** during the tower climb (visible in the output)
4. At least one floor subclass that **overrides a hook** and produces different behavior than the default
5. Output demonstrating that the Template Method skeleton is followed (announce → setup → challenge → loot → cleanup steps visible)
6. A final `TowerRunResult` printed showing floors cleared, heroes surviving, and tower status

---

## Deliverables

Submit a ZIP file containing:

- All Java source files (`.java`) — scaffold files + your implementations
- `Main.java` producing the demo output listed above
- **UML Diagram 1:** State pattern — interface, all implementations, and Hero with its state field
- **UML Diagram 2:** Template Method pattern — abstract class, all subclasses, showing the template method and inheritance relationships

Do **not** include compiled `.class` files.

---

## Grading (15 points)

| Area | Points | What the grader checks |
|------|--------|----------------------|
| **State** | 6 | Interface used correctly; ≥ 3 state implementations with measurably different damage modifiers; heroes transition between states at runtime (not set-once); at least one state makes `canAct()` false and affects control flow; state transitions driven by internal logic or game events, not external calls |
| **Template Method** | 6 | `explore()` is `final` and never overridden; ≥ 3 subclasses fill in abstract steps; at least one subclass overrides an optional hook; step order fixed and visible in output; no subclass duplicates the skeleton |
| **Runner + Demo** | 3 | Main.java builds a non-trivial tower climb; visible state transitions in output; `TowerRunResult` printed; output demonstrates all required behaviors |
