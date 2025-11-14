# Java Programming Hackathon

This repository contains solutions to multiple Java programming challenges, structured as independent console programs. Each problem is implemented as a separate Java file with its own supporting inner or nested classes.

All code uses only the standard Java Development Kit (JDK) with no external dependencies.

## Repository Contents

At the root of the repository:

- `BrickWall.java` (+ compiled classes)
- `OptimalArrangement.java` (+ compiled classes)
- `ReduceExp.java` (+ compiled classes)

Each `.java` file corresponds to one hackathon problem. The `.class` files are the compiled bytecode generated from these sources and can be removed and regenerated at any time.

---

## 1. BrickWall

**Files:**

- `BrickWall.java`
- `BrickWall.class`
- `BrickWall$Cell.class`
- `BrickWall$GridInfo.class`
- `BrickWall$Position.class`

### Overview

`BrickWall` models a wall as a 2D grid of cells. From the helper classes:

- `Cell` represents a single location in the grid.
- `Position` represents coordinates in the wall.
- `GridInfo` stores metadata such as grid size and/or configuration.

The exact behavior and input/output formats are defined in `BrickWall.java`, particularly in its `main` method.

### How to Run

```bash
javac BrickWall.java
java BrickWall < input_brickwall.txt > output_brickwall.txt
```

- Input is read from standard input.
- Output is written to standard output.
- See `BrickWall.java` for the precise input specification and example usages.

---

## 2. OptimalArrangement

**Files:**

- `OptimalArrangement.java`
- `OptimalArrangement.class`
- `OptimalArrangement$ArrangementResult.class`
- `OptimalArrangement$Goodie.class`
- `OptimalArrangement$Item.class`
- `OptimalArrangement$Result.class`

### Overview

`OptimalArrangement` solves an arrangement/optimization problem over a collection of items (“goodies”). From the helper classes:

- `Item` and `Goodie` represent elements with certain attributes (such as value, weight, or other properties).
- `Result` / `ArrangementResult` represent a computed optimal (or near-optimal) solution.

The specific algorithm and problem statement are implemented and documented in `OptimalArrangement.java`.

### How to Run

```bash
javac OptimalArrangement.java
java OptimalArrangement < input_optimal.txt > output_optimal.txt
```

- Input is read from standard input and describes items/goodies and constraints.
- Output describes the chosen arrangement and/or its score.
- Refer to the `main` method in `OptimalArrangement.java` for the exact input format.

---

## 3. ReduceExp

**Files:**

- `ReduceExp.java`
- `ReduceExp.class`
- `ReduceExp$1.class` (anonymous inner class)
- `ReduceExp$Coeffs.class`
- `ReduceExp$Parser.class`

### Overview

`ReduceExp` parses and simplifies expressions, likely algebraic or polynomial expressions. Its helpers indicate:

- `Parser` handles tokenization and parsing of the expression string.
- `Coeffs` stores coefficients for terms.
- `ReduceExp$1` is an anonymous inner class, probably used for a callback, comparator, or helper logic.

The program’s typical workflow:

1. Read an expression from input.
2. Parse it into an internal representation.
3. Apply reduction/simplification rules (e.g., combining like terms).
4. Print a simplified expression to output.

The supported syntax, operators, and reduction rules are defined in `ReduceExp.java`, especially inside `Parser`.

### How to Run

```bash
javac ReduceExp.java
java ReduceExp < input_reduceexp.txt > output_reduceexp.txt
```

- Input is read from standard input, usually one expression per line.
- Output contains simplified or transformed versions of the given expressions.

---

## Build & Run All Problems

### Prerequisites

- Java Development Kit (JDK) 8 or later.

Check your Java installation:

```bash
java -version
javac -version
```

### Compile all sources

From the repository root:

```bash
javac BrickWall.java OptimalArrangement.java ReduceExp.java
```

This regenerates all `.class` files in the current directory.

### Run each program

Examples using redirected input/output:

```bash
# BrickWall challenge
java BrickWall < input_brickwall.txt > output_brickwall.txt

# OptimalArrangement challenge
java OptimalArrangement < input_optimal.txt > output_optimal.txt

# ReduceExp challenge
java ReduceExp < input_reduceexp.txt > output_reduceexp.txt
```

You can also run them interactively by typing input into the console.

---

## Repository Structure

All files currently reside at the repository root:

- Source code:
  - `BrickWall.java`
  - `OptimalArrangement.java`
  - `ReduceExp.java`
- Compiled artifacts:
  - `BrickWall.class`, `BrickWall$Cell.class`, `BrickWall$GridInfo.class`, `BrickWall$Position.class`
  - `OptimalArrangement.class`, `OptimalArrangement$ArrangementResult.class`, `OptimalArrangement$Goodie.class`, `OptimalArrangement$Item.class`, `OptimalArrangement$Result.class`
  - `ReduceExp.class`, `ReduceExp$1.class`, `ReduceExp$Coeffs.class`, `ReduceExp$Parser.class`

The `.class` files are generated build artifacts and can be removed:

```bash
rm *.class
```

They will be recreated when you run `javac` again.
