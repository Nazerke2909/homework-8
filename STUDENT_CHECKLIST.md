# Student Checklist — Homework 8

Use this checklist to track your progress. Work through the phases in order — each phase builds on the previous one.

---

## Phase 1: Understand the Patterns

- [ ] Read `ASSIGNMENT.md` from start to finish
- [ ] In your own words, write down what State does and what Template Method does
- [ ] Identify why the two patterns are *independent* in this assignment
- [ ] Read `FAQ.md` sections on State and Template Method
- [ ] Review the provided source files: `HeroState.java`, `TowerFloor.java`, `FloorResult.java`, `TowerRunResult.java`

---

## Phase 2: Design on Paper First

- [ ] Sketch a class diagram showing all State implementations and how Hero uses them
- [ ] Sketch a class diagram showing all `TowerFloor` subclasses and the template method structure
- [ ] Identify: which state methods are called during hero combat? When?
- [ ] Identify: how will states transition (by what triggers)?
- [ ] Decide: what are the ≥ 3 floor types you will implement?
- [ ] Decide: which floor will override a hook, and why?

---

## Phase 3: Implement the State Pattern

- [ ] Create ≥ 3 concrete `HeroState` implementations (in separate `.java` files)
- [ ] Verify each state's `modifyOutgoingDamage` and `modifyIncomingDamage` return visibly different values
- [ ] Verify at least one state has `canAct()` returning `false`
- [ ] Design state transition logic: when/why does a state change?
- [ ] Add a `HeroState` field to `Hero` (extend the provided skeleton)
- [ ] Implement a method on `Hero` to transition states at runtime
- [ ] Implement `onTurnStart()` and `onTurnEnd()` logic (e.g., poison tick, stun countdown)

---

## Phase 4: Implement the Template Method Pattern

- [ ] Create ≥ 3 concrete `TowerFloor` subclasses (in separate `.java` files)
- [ ] Implement all abstract methods for each subclass: `getFloorName()`, `setup()`, `resolveChallenge()`, `awardLoot()`
- [ ] Verify that **no subclass overrides `explore()`** — the template method is inherited
- [ ] For at least one subclass, override an optional hook: `announce()`, `shouldAwardLoot()`, or `cleanup()`
- [ ] For at least one subclass, make `resolveChallenge()` create combat or a challenge that interacts with the party

---

## Phase 5: Create Monster and Floor Support Classes

- [ ] Verify `Monster.java` compiles and has `takeDamage()`, `isAlive()`, `attack(Hero)` methods
- [ ] Verify `FloorResult.java` has getters for `isCleared()`, `getDamageTaken()`, `getSummary()`
- [ ] Create a `TowerRunner` class (or equivalent) that executes floors in sequence and tracks state

---

## Phase 6: Wire Everything in Main.java

- [ ] Create at least 2 heroes with different starting states
- [ ] Create a sequence of ≥ 4 floors using your ≥ 3 floor subclasses
- [ ] Instantiate the tower runner
- [ ] Run the tower climb
- [ ] Print the final `TowerRunResult`

---

## Phase 7: Verify Output

- [ ] At least one hero transitions through ≥ 2 states visibly in the output
- [ ] Each floor's `explore()` method produces output matching the template: announce → setup → resolve → loot → cleanup
- [ ] The floor that overrides a hook shows its customized behavior in the output
- [ ] Combat or challenges in at least one floor cause visible damage or state changes
- [ ] Final `TowerRunResult` is printed with correct values (floors cleared, heroes surviving, tower cleared status)

---

## Phase 8: Draw UML Diagrams

- [ ] **Diagram 1 — State:** Interface + all implementations + Hero (showing the state field and method)
- [ ] **Diagram 2 — Template Method:** Abstract class + all subclasses (showing inheritance and which methods are overridden)
- [ ] Both diagrams are legible and show all relationships clearly

---

## Phase 9: Final Review and Submission

- [ ] All Java files compile without errors or warnings
- [ ] `Main.java` demonstrates all required behaviors listed in `ASSIGNMENT.md` Part 3
- [ ] You have not modified any provided scaffold files (see `ASSIGNMENT.md` "What Is Provided")
- [ ] Both UML diagrams are included and clearly labeled
- [ ] ZIP your submission — source files + diagrams, no `.class` files
