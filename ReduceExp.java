import java.util.*;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.math.BigDecimal;

public class ReduceExp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String expr = sc.nextLine();
        sc.close();

        // If only constants, evaluate and return 0
        if (isConstantOnly(expr)) {
            System.out.println(0);
            return;
        }

        // Count operators in original expression
        int origOps = countOperators(expr);

        // Get coefficients by evaluating at points
        Coeffs coeffs = getCoeffs(expr);

        int minOps = origOps;

        // Try Form 1: (a1x + b1)(a2x + b2) = a1a2 xx + (a1b2 + a2b1) x + b1b2
        if (coeffs.xy == 0 && coeffs.y == 0) {
            for (int a1 = -99; a1 <= 99; a1++) {
                for (int a2 = -99; a2 <= 99; a2++) {
                    if (a1 == 0 || a2 == 0) continue;
                    if (a1 * a2 != coeffs.xx) continue;
                    for (int b1 = -99; b1 <= 99; b1++) {
                        for (int b2 = -99; b2 <= 99; b2++) {
                            if (a1 * b2 + a2 * b1 == coeffs.x && b1 * b2 == coeffs.constTerm) {
                                int ops = countFactoredOps(a1, b1, a2, b2, 1);
                                if (ops < minOps) minOps = ops;
                            }
                        }
                    }
                }
            }
        }

        // Try Form 2: (a1x + b1)(a2y + b2) = a1a2 xy + a1b2 x + a2b1 y + b1b2
        if (coeffs.xx == 0) {
            for (int a1 = -99; a1 <= 99; a1++) {
                for (int a2 = -99; a2 <= 99; a2++) {
                    if (a1 == 0 || a2 == 0) continue;
                    if (a1 * a2 != coeffs.xy) continue;
                    for (int b1 = -99; b1 <= 99; b1++) {
                        for (int b2 = -99; b2 <= 99; b2++) {
                            if (a1 * b2 == coeffs.x && a2 * b1 == coeffs.y && b1 * b2 == coeffs.constTerm) {
                                int ops = countFactoredOps(a1, b1, a2, b2, 2);
                                if (ops < minOps) minOps = ops;
                            }
                        }
                    }
                }
            }
        }

        // Try Form 3: (a1x + b1y)(a2x + b2) = a1a2 xx + a1b2 x + a2b1 xy + b1b2 y
        if (coeffs.constTerm == 0) {
            for (int a1 = -99; a1 <= 99; a1++) {
                for (int a2 = -99; a2 <= 99; a2++) {
                    if (a1 == 0 || a2 == 0) continue;
                    if (a1 * a2 != coeffs.xx) continue;
                    for (int b1 = -99; b1 <= 99; b1++) {
                        for (int b2 = -99; b2 <= 99; b2++) {
                            if (a1 * b2 == coeffs.x && a2 * b1 == coeffs.xy && b1 * b2 == coeffs.y) {
                                int ops = countFactoredOps(a1, b1, a2, b2, 3);
                                if (ops < minOps) minOps = ops;
                            }
                        }
                    }
                }
            }
        }

        System.out.println(minOps);
    }

    static class Coeffs {
        int xx, xy, x, y, constTerm;
        Coeffs() {
            xx = xy = x = y = constTerm = 0;
        }
    }

    static Coeffs getCoeffs(String expr) {
        Coeffs c = new Coeffs();
        // Evaluate at (0,0), (1,0), (0,1), (1,1), (2,0)
        double f00 = evaluate(expr, 0, 0);
        double f10 = evaluate(expr, 1, 0);
        double f01 = evaluate(expr, 0, 1);
        double f11 = evaluate(expr, 1, 1);
        double f20 = evaluate(expr, 2, 0);

        c.constTerm = (int) Math.round(f00);
        double E = f00;
        double A_plus_C = f10 - E;
        double D = f01 - E;
        double A_plus_B_plus_C_plus_D = f11 - E;
        double _4A_plus_2C = f20 - E;

        // Solve for A, C, B
        double A = (_4A_plus_2C - 2 * A_plus_C) / 2;
        double C = A_plus_C - A;
        double B = A_plus_B_plus_C_plus_D - A - C - D;

        c.xx = (int) Math.round(A);
        c.x = (int) Math.round(C);
        c.y = (int) Math.round(D);
        c.xy = (int) Math.round(B);

        return c;
    }

    static double evaluate(String expr, int x, int y) {
        // Simple evaluation by replacing and using a stack-based evaluator
        String expr2 = expr.replace("x", Integer.toString(x)).replace("y", Integer.toString(y));
        return evaluateSimple(expr2);
    }

    static double evaluateSimple(String expr) {
        // Remove spaces
        expr = expr.replaceAll("\\s+", "");
        // Use a stack for evaluation
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == ' ') continue;
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
                    sb.append(expr.charAt(i));
                    i++;
                }
                i--;
                values.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop(); // remove '('
            } else if (c == '+' || c == '*') {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(c)) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '*': return a * b;
        }
        return 0;
    }

    static int countOperators(String expr) {
        int count = 0;
        for (char c : expr.toCharArray()) {
            if (c == '+' || c == '*') {
                count++;
            }
        }
        return count;
    }

    static boolean isConstantOnly(String expr) {
        for (char c : expr.toCharArray()) {
            if (c == 'x' || c == 'y') {
                return false;
            }
        }
        return true;
    }

    static int countFactoredOps(int a1, int b1, int a2, int b2, int form) {
        int ops = 0;
        if (form == 1) {
            // (a1x + b1)(a2x + b2)
            // First paren
            if (a1 != 0) {
                if (a1 != 1) ops++; // a1 * x
                if (b1 != 0) ops++; // + b1
            } else {
                if (b1 != 0) {
                    // just b1, no op
                }
            }
            // Second paren
            if (a2 != 0) {
                if (a2 != 1) ops++; // a2 * x
                if (b2 != 0) ops++; // + b2
            } else {
                if (b2 != 0) {
                    // just b2, no op
                }
            }
            ops++; // multiplication
        } else if (form == 2) {
            // (a1x + b1)(a2y + b2)
            // First paren
            if (a1 != 0) {
                if (a1 != 1) ops++; // a1 * x
                if (b1 != 0) ops++; // + b1
            } else {
                if (b1 != 0) {
                    // just b1, no op
                }
            }
            // Second paren
            if (a2 != 0) {
                if (a2 != 1) ops++; // a2 * y
                if (b2 != 0) ops++; // + b2
            } else {
                if (b2 != 0) {
                    // just b2, no op
                }
            }
            ops++; // multiplication
        } else if (form == 3) {
            // (a1x + b1y)(a2x + b2)
            // First paren: a1x + b1y
            if (a1 != 0) {
                if (a1 != 1) ops++; // a1 * x
            }
            if (b1 != 0) {
                if (b1 != 1) ops++; // b1 * y
            }
            if (a1 != 0 && b1 != 0) {
                ops++; // the +
            }
            // Second paren: a2x + b2
            if (a2 != 0) {
                if (a2 != 1) ops++; // a2 * x
                if (b2 != 0) ops++; // + b2
            } else {
                if (b2 != 0) {
                    // just b2, no op
                }
            }
            ops++; // multiplication
        }
        return ops - 2;
    }
}