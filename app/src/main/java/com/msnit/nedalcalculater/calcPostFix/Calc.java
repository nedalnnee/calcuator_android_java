package com.msnit.nedalcalculater.calcPostFix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Calc {
    public final static Stack<Double> stack = new Stack<>();
    public final static ArrayList<String> values = new ArrayList<>();
    public final static ArrayList<String> valuesParsed = new ArrayList<>();
    public static final String SQUARE_ROOT_SYMBOL = "√(";
    public static final String SIN = "sin(";
    public static final String COS = "cos(";
    public static final String TAN = "tan(";
    public static final String FACTOR = "^";

    public static boolean isInt(Double d) {
        return (d % 1) == 0;
    }

    public static void parseValues() {
        Stack<String> ops = new Stack<>();

        for (String value : values) {
            if (isNumber(value)) {
                valuesParsed.add(value);
            } else {
                if (checkBrackets(ops, value)) {
                    continue;
                }
                checkPower(ops, value);
            }
        }

        while (!ops.isEmpty()) {
            valuesParsed.add(ops.pop());
        }

    }

    private static boolean checkBrackets(Stack<String> ops, String value) {
        if (value.equals(")")) {
            String pop;

            do {
                pop = ops.pop();
                if (pop.contains("(")) {
                    if (isSingleValueOp(pop))
                        valuesParsed.add(pop);

                    break;
                }
                valuesParsed.add(pop);

            } while (!ops.isEmpty());
            return true;
        }
        return false;
    }


    public static void checkPower(Stack<String> ops, String value) {
        if (ops.isEmpty()) {
            ops.add(value);
            return;
        }
        if (getOpPower(ops.peek()) < getOpPower(value) || value.contains("(")) {
            ops.push(value);
        } else {
            valuesParsed.add(ops.pop());
            checkPower(ops, value);
        }
    }

    public static double calcAll() {
        for (String item : valuesParsed) {
            if (isOp(item)) {
                if (isSingleValueOp(item)) {
                    stack.push(roundTo6Decimal(calc(item, stack.pop())));
                } else {
                    double v2 = stack.pop();
                    double v1 = stack.pop();
                    double value = roundTo6Decimal(calc(item, v1, v2));
                    stack.push(value);
                }
            } else {
                stack.push(Double.parseDouble(item.trim()));
            }
        }
        if (stack.isEmpty()) return 0;
        return (stack.pop());
    }

    private static double roundTo6Decimal(double v) {
        BigDecimal bd = new BigDecimal(Double.toString(v));
        bd = bd.setScale(6, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }

    public static double calc(String op, double v1, double v2) {
        switch (op) {
            case "+":
                return (v1 + v2);
            case "-":
                return (v1 - v2);
            case "×":
                return (v1 * v2);
            case "/":
                return (v1 / v2);
            case "^":
                return (int) Math.pow(v1, v2);
        }
        return 0;
    }

    public static double calc(String op, double v) {
        switch (op) {
            case SIN:
                return Math.sin(Math.toRadians(v));
            case COS:
                return Math.cos(Math.toRadians(v));
            case TAN:
                return Math.tan(Math.toRadians(v));
            case SQUARE_ROOT_SYMBOL:
                return Math.sqrt(v);
        }
        return 0;
    }

    public static Integer getOpPower(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "×":
            case "/":
                return 2;
            case "^":
                return 3;
//            case SIN:
//            case COS:
//            case TAN:
//            case SQUARE_ROOT_SYMBOL:
//                return 4;


        }
        return 0;
    }

    private static boolean isTMathOp(String op) {
        switch (op.toLowerCase()) {
            case SIN:
            case COS:
            case TAN:
                return true;
        }
        return false;

    }

    public static boolean isOp(String text) {
        switch (text) {
            case "+":
            case "-":
            case "×":
            case "/":
            case "^":
            case "(":
            case ")":
            case SIN:
            case COS:
            case TAN:
            case SQUARE_ROOT_SYMBOL:
                return true;
        }
        return false;


    }

    public static boolean isBasicOp(String text) {
        switch (text) {
            case "+":
            case "-":
            case "×":
            case "/":
            case "^":
                return true;
        }
        return false;


    }

    public static boolean isSingleValueOp(String text) {
        switch (text) {
            case SIN:
            case COS:
            case TAN:
            case SQUARE_ROOT_SYMBOL:
                return true;
        }
        return false;


    }

    public static boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;

    }
}