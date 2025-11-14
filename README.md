# Java Programming Hackathon Repository

This repository contains solutions for three distinct programming challenges, demonstrating proficiency in data structures, algorithms, and advanced problem-solving techniques in Java.

## Directory Structure

```
└── raj17deep-java-programming-hackathon/
    ├── BrickWall.java             # Shortest path problem using 0-1 BFS
    ├── OptimalArrangement.java    # K-th lexicographical arrangement for min cost
    └── ReduceExp.java             # Algebraic simplification and operator minimization
```

---

## Project Modules and Algorithms

### 1. `BrickWall.java`

**Problem:** Find the minimum number of Green ('G') bricks that must be broken to create a path from the Source ('S') to the Destination ('D') in a grid. Red ('R') bricks are impassable.

**Algorithm:**
The problem is modeled as finding the shortest path in an unweighted graph where the edges have binary weights (0 or 1).
*   **Nodes:** Each individual brick is a node in the graph.
*   **Edges:** An edge exists between adjacent non-Red bricks.
*   **Cost:** The cost of traversing an edge is **1** if the target brick is Green ('G'), and **0** if it is 'S', 'D', or any other non-specified non-Red type (implicitly, these are assumed to be cost-free 'paths'). Red bricks ('R') are ignored during graph construction.

This structure is perfectly suited for **0-1 Breadth-First Search (0-1 BFS)**, which is implemented using a `java.util.ArrayDeque`. Zero-cost edges are pushed to the front (`offerFirst`), and one-cost edges are pushed to the back (`offerLast`), ensuring the shortest path in terms of total broken bricks is found efficiently.

### 2. `OptimalArrangement.java`

**Problem:** Given a list of items with labels and weights, find the $K$-th lexicographical arrangement that achieves the *minimum possible total cost*. The cost of an arrangement is $\sum (\text{weight} \times \text{new\_position})$.

**Algorithm:**
The solution involves a combination of a greedy optimization and advanced combinatorics:

1.  **Greedy Cost Minimization:** The minimum cost is achieved by placing items with the highest total weight (sum of all items sharing the same label) into the earliest positions. The code groups items by label, calculates the total weight for each label, and sorts the groups in **descending order** of total weight. Labels within the same weight group are sorted lexicographically. This establishes the minimum cost.

2.  **$K$-th Lexicographical Permutation:** Since all arrangements derived from the same group order (but potentially different internal label orders) yield the minimum cost, the remaining task is to find the $K$-th arrangement among these minimum-cost permutations. This is solved using the **Factoradic Number System** (sometimes called factorial number system) to map the 1-based index $K$ to the specific permutation order. The implementation uses `BigInteger` to safely handle potentially large permutation counts.

### 3. `ReduceExp.java`

**Problem:** Given a polynomial expression in $x$ and $y$ using only addition and multiplication, find the minimum number of arithmetic operators required to represent the expression, prioritizing reduced factored forms over the original expression.

**Algorithm:**

1.  **Coefficient Extraction:** The initial expression is a second-degree polynomial of the form $A x^2 + B xy + C x + D y + E$. The coefficients ($A, B, C, D, E$) are numerically extracted by evaluating the expression at five unique test points: $(0,0), (1,0), (0,1), (1,1), (2,0)$. A stack-based **Expression Evaluator** is used to handle the runtime calculation.

2.  **Brute-Force Optimization:** The extracted coefficients are checked against three common factored forms:
    *   Form 1: $(a_1x + b_1)(a_2x + b_2)$
    *   Form 2: $(a_1x + b_1)(a_2y + b_2)$
    *   Form 3: $(a_1x + b_1y)(a_2x + b_2)$

    A constrained **Brute-Force Search** iterates through possible integer coefficients $a_i, b_i$ in the range $[-99, 99]$. If a match is found, a helper function calculates the exact number of operators required for that factored form.

3.  **Result:** The minimum operator count found across all valid factored forms is compared against the original operator count, and the lowest value is reported.

---

## ⚙️ Setup and Execution

### Prerequisites

*   Java Development Kit (JDK) 8 or newer.

### Execution

All files are standard Java applications and can be compiled and run from the command line:

```bash
# Compile all classes
javac BrickWall.java OptimalArrangement.java ReduceExp.java

# --- Example 1: BrickWall.java ---
# Execution expects input from standard input (stdin)
echo -e "3\n1R1G1D\n1G1R1G\n1S1G1G" | java BrickWall

# --- Example 2: OptimalArrangement.java ---
# Execution expects input from standard input (stdin)
# Example input: 
# 3
# A 10
# B 5
# A 10
# 1
# echo -e "3\nA 10\nB 5\nA 10\n1" | java OptimalArrangement

# --- Example 3: ReduceExp.java ---
# Execution expects input from standard input (stdin)
# Example input: x*x + x*y + 2*x + 2*y
# echo "x*x + x*y + 2*x + 2*y" | java ReduceExp
```
