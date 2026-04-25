# FAQ — Homework 8: The Haunted Tower

---

## State Pattern

**Q: What is the difference between State and Strategy?**
A: They look structurally similar but solve different problems. **State** is about an object managing its own internal behavior and transitions — the object knows when and why it changes state, and states often depend on the entity's context. **Strategy** is about making an algorithm interchangeable — the client picks which algorithm to use, and strategies are usually independent and stateless. In this assignment, a hero's state manages the hero's behavior (damage modifiers, action restrictions) and can transition based on the hero's HP or a turn counter. This is pure State. Do not confuse the two.

**Q: Can a hero have no state (null)?**
A: No. Every hero must always have an active state. Set a default state in the Hero constructor.

**Q: Must state transitions happen automatically?**
A: States can transition in two ways: (1) the hero explicitly sets a new state (e.g., `hero.setState(new PoisonedState())`), or (2) a state transitions itself based on internal logic (e.g., `onTurnEnd()` decrements a counter and replaces itself if the counter reaches zero). Approach (2) demonstrates that the State pattern gives the state *responsibility* for its own lifecycle — do not miss this. At least one state should self-transition to show you understand this aspect.

**Q: When should `onTurnStart()` and `onTurnEnd()` be called?**
A: During a hero's turn in combat. Typically: (1) at the *start* of a hero's turn, apply any state-specific effects (poison damage, stun check, regen healing); (2) at the *end*, apply any cleanup or countdown (poison timer ticks down, stun duration decreases). The exact timing is your design — just be consistent and document it.

**Q: Does the hero need to call these methods, or does the engine?**
A: Your design. Option A: the hero calls them before/after its action. Option B: the engine/tower runner calls them on each hero. Option C: states manage their own side effects internally. All are valid. Just ensure the behavior is *visible* in the output so the grader can verify states are active.

**Q: What should `canAct()` do?**
A: Return `true` if the hero can take an action this turn, `false` if stunned or otherwise prevented. If `canAct()` returns `false`, the hero's turn should be skipped (in a round-based combat). This shows the State pattern changing control flow — essential for demonstrating mastery.

**Q: Do states need to be created once and reused, or once per hero?**
A: Either is fine. If states are stateless (no per-hero data), creating one instance per state type and sharing is efficient. If states track per-hero data (e.g., poison duration), create instances per hero. Be consistent.

**Q: Can I use enums or inner classes for states?**
A: Yes, but separate `.java` files for each concrete state is cleaner and easier to grade. Avoid putting all states in one massive file.

**Q: Should state damage modifiers be multipliers or absolute values?**
A: Your choice. Multipliers are intuitive: neutral = 1.0, aggressive = 1.5 (50% more damage), defensive = 0.7 (30% less). Absolute values are also valid. Make sure the differences are *visible* in the demo — if all states return the same numbers, you have not implemented the pattern.

**Q: Can a state modify fields on the hero directly?**
A: Yes. A state can call `hero.takeDamage()`, `hero.heal()`, or other methods. This is valid — the state is responsible for its effects. Avoid modifying private fields directly; use public methods on the hero instead.

---

## Template Method Pattern

**Q: What is the difference between Template Method and plain inheritance?**
A: Template Method is a specific pattern where (1) an abstract class defines a `final` method that orchestrates a fixed sequence of steps, and (2) subclasses override abstract methods to fill in the steps. The *sequence* is fixed; the *implementation* varies. Plain inheritance is just "subclass overrides methods." Template Method is more deliberate — the template method itself is fixed, and that fixedness is the point. Ensure `explore()` is `final` to demonstrate this.

**Q: Can I override `explore()` in a subclass?**
A: No. That would defeat the entire purpose of Template Method. If you override `explore()`, you are not using the pattern. The whole point is that the skeleton is fixed and never overridden.

**Q: What is a "hook"?**
A: A hook is a non-abstract method in the template method that subclasses may override for customization. Unlike abstract methods (which subclasses must implement), hooks have a default implementation — subclasses override them only if needed. In `TowerFloor`, examples: `announce()` has a default that prints a generic message; `shouldAwardLoot()` defaults to `true`; `cleanup()` is a no-op by default. A subclass can override any of these to customize behavior. You must demonstrate this by overriding at least one hook.

**Q: Why does `shouldAwardLoot()` exist?**
A: It is a *hook* that lets subclasses decide whether to award loot for a particular floor. A rest floor might return `false` (no loot), while a combat floor returns `true`. This shows the hook mechanism in action — the abstract `awardLoot()` method is only called if the hook permits it. The template method controls the decision, not the subclass directly.

**Q: Must all 3+ floor subclasses be different?**
A: Yes. If two subclasses implement the same behavior, you are not really exploring the pattern — you are just filling in template slots. Make them meaningfully different: one combat-based, one puzzle/trap-based, one rest/event-based, etc.

**Q: Can `resolveChallenge()` call other methods on the hero?**
A: Yes. Call `hero.takeDamage()`, check `hero.isAlive()`, transition hero states, etc. The more interaction, the better the demo.

**Q: Should I create a Monster for every floor?**
A: No. Floors can have different types of challenges: some have monsters (combat), some have traps (automatic damage), some have puzzles (no combat). Design floors to be distinct.

**Q: Can I add helper methods to `TowerFloor` beyond the required abstract and hook methods?**
A: Yes. Helper methods like `damageAllHeroes(int amount)` or `log(String message)` are fine. They support the abstract steps without breaking the pattern.

**Q: What should `FloorResult` contain?**
A: It is provided with `isCleared()`, `getDamageTaken()`, `getSummary()`. These are sufficient. Do not modify it.

---

## Independence of the Two Patterns

**Q: How do State and Template Method relate in this assignment?**
A: They do not relate structurally. State governs intra-entity behavior (how a hero acts based on its internal state). Template Method governs inter-activity structure (how a floor exploration is orchestrated). A hero with multiple states can explore any floor — neither pattern depends on the other. This independence is a core lesson: patterns solve different problems.

**Q: Can a floor's `resolveChallenge()` transition a hero's state?**
A: Yes! Absolutely. Combat can apply poison, stun, or berserk. This is how the two patterns *interact* at runtime, even though they are structurally independent. The hero's state is responsible for its effects; the floor is responsible for triggering transitions.

**Q: What if a hero is stunned during a floor's challenge?**
A: Depends on your design. If `canAct()` returns `false` for stun, the hero skips its action. If the floor's combat loop checks this, the hero is helpless. This is a valid and interesting scenario — the State pattern affects the Template Method's execution.

---

## Tower Runner

**Q: How should the tower runner work?**
A: Create a class (e.g., `TowerRunner`) that (1) holds a list of floors, (2) iterates through them in order, (3) calls `explore()` on each floor with the hero party, (4) tracks results (floors cleared, heroes alive/dead), and (5) returns a `TowerRunResult`. Simple and clean.

**Q: What if all heroes die mid-tower?**
A: The tower run ends immediately. Return a `TowerRunResult` reflecting the number of floors cleared before the wipe. This is a valid outcome.

**Q: Should floors or the runner track party state?**
A: Floors should work with the party they receive (via `explore(List<Hero> party)`). The runner tracks which floors were cleared and returns the final state. Floors do not need to know about the overall tower.

---

## Demo (Main.java)

**Q: How much output should Main.java produce?**
A: Enough to visibly demonstrate: (1) State transitions, (2) Template Method structure (announce/setup/challenge/loot/cleanup steps), (3) Hook customization, (4) Final results. Aim for 50–200 lines of console output. Too much is noise; too little does not show the patterns.

**Q: Should heroes have meaningful starting stats?**
A: Yes. Give them enough HP to survive at least one floor. Give them enough attack power to deal damage. Balancing makes the demo more interesting.

**Q: What if all heroes die in the first floor?**
A: That is a valid demo, but make sure the output shows state transitions before they die. Adjust stats to make the run last at least 2–3 floors.

**Q: Can I print state names and floor names for clarity?**
A: Yes, absolutely. Print when a hero transitions states: `"Aragorn is now Poisoned!"`. Print floor names in the announce step. This helps the grader verify the pattern is working.

---

## General

**Q: Do I need Maven or Gradle?**
A: No. Plain `javac` is sufficient. See `QUICKSTART.md`.

**Q: Can I add more classes beyond the required ones?**
A: Yes. Helper classes, utility methods, even a `Tower` class to hold floors — all are fine as long as core pattern requirements are met.

**Q: Should I write unit tests?**
A: Not required for this assignment. A working, well-demonstrated `Main.java` is sufficient.

**Q: Can I use Java generics, streams, or lambdas?**
A: Yes. `List<HeroState>` and streams over floor lists are natural. Do not force them in where they complicate the code.

**Q: Is it okay if the tower run fails (heroes wipe)?**
A: Yes, but make the run meaningful. Tune stats and state transitions so all required behaviors are visible before the wipe. A one-floor demo is not acceptable.

**Q: Can I modify the provided classes?**
A: No. You may only add concrete implementations (new state classes, new floor classes). Do not change `HeroState.java`, `TowerFloor.java`, `Hero.java`, `Monster.java`, `FloorResult.java`, `TowerRunResult.java`, or `Main.java`'s structure. You may fill in the TODOs in `Main.java`.

**Q: Can I reorganize the package structure?**
A: You may add new sub-packages (e.g., `com.narxoz.rpg.states`, `com.narxoz.rpg.floors`, `com.narxoz.rpg.runner`). Do not move or rename the provided scaffold files.

**Q: What if my states or floors have bugs in the provided methods?**
A: The provided files are clean. If you see an error, check that you have read the file correctly and imported packages properly. If you believe the provided code has a bug, ask the instructor before modifying it.
