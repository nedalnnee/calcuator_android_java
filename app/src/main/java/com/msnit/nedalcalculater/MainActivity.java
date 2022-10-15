package com.msnit.nedalcalculater;

import static com.msnit.nedalcalculater.calcPostFix.Calc.COS;
import static com.msnit.nedalcalculater.calcPostFix.Calc.FACTOR;
import static com.msnit.nedalcalculater.calcPostFix.Calc.SIN;
import static com.msnit.nedalcalculater.calcPostFix.Calc.SQUARE_ROOT_SYMBOL;
import static com.msnit.nedalcalculater.calcPostFix.Calc.TAN;
import static com.msnit.nedalcalculater.calcPostFix.Calc.isInt;
import static com.msnit.nedalcalculater.calcPostFix.Calc.stack;
import static com.msnit.nedalcalculater.calcPostFix.Calc.valuesParsed;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.msnit.nedalcalculater.calcPostFix.Calc;
import com.msnit.nedalcalculater.util.NumberBtn;
import com.msnit.nedalcalculater.util.OpBtn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int NUMBER_DIGIT_LIMIT = 12;
    private static final NumberBtn[] number = new NumberBtn[10];
    private static final Map<Double, NumberBtn> numberMap = new HashMap<>();
    private static boolean equalShow = false;
    private static String equal = "";
    private static int orientation;
    private final Map<Integer, OpBtn> operationMap = new HashMap<>();
    private final ArrayList<String> values = Calc.values;
    private TextView calcScreen;
    private int openBracket = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calcScreen = findViewById(R.id.calc_screen);
        updateDisplay();

        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) toast("LANDSCAPE");
        else toast("Portrait");

        loadButtonsWithEvents();
    }

    private void loadButtonsWithEvents() {
        int[] ids = new int[10];
        ids[0] = R.id.zero;
        ids[1] = R.id.one;
        ids[2] = R.id.two;
        ids[3] = R.id.three;
        ids[4] = R.id.four;
        ids[5] = R.id.five;
        ids[6] = R.id.six;
        ids[7] = R.id.seven;
        ids[8] = R.id.eight;
        ids[9] = R.id.nine;
        for (int i = 0; i < number.length; i++) {
            number[i] = new NumberBtn(findViewById(ids[i]), i, ids[i]);
            number[i].getBtn().setOnClickListener(this::numberClicked);
            numberMap.put((double) ids[i], number[i]);
        }


        assignOpBtnAndEvent(R.id.plus, OpBtn.PLUS);
        assignOpBtnAndEvent(R.id.minus, OpBtn.MINUS);
        assignOpBtnAndEvent(R.id.multiply, OpBtn.MULTIPLY);
        assignOpBtnAndEvent(R.id.divide, OpBtn.DIVIDE);

        findViewById(R.id.clear).setOnClickListener((e) -> clear());
        findViewById(R.id.equals).setOnClickListener(this::equal);
        findViewById(R.id.dot).setOnClickListener(this::dot);
        findViewById(R.id.deleteOne).setOnClickListener(this::deleteLastDigit);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.left_bracket).setOnClickListener(this::leftBracket);
            findViewById(R.id.right_bracket).setOnClickListener(this::rightBracket);
            findViewById(R.id.root).setOnClickListener(this::root);
            findViewById(R.id.sin).setOnClickListener(this::TMath);
            findViewById(R.id.cos).setOnClickListener(this::TMath);
            findViewById(R.id.tan).setOnClickListener(this::TMath);
            findViewById(R.id.factor).setOnClickListener(this::factor);
        }

    }

    private void factor(View e) {
        clearIfEqualsShow(true);
        values.add(FACTOR);
        updateDisplay();
    }

    private void TMath(View view) {

        String math = "";
        if (view.getId() == R.id.sin) {
            math = SIN;
        } else if (view.getId() == R.id.cos) {
            math = COS;

        } else if (view.getId() == R.id.tan) {
            math = TAN;
        }

        openBracket++;
        if (!values.isEmpty())
            if (!lastEnteredIsOp() || values.get(values.size() - 1).equals(")")) {
                values.add("×");
            }
        values.add(math);
        updateDisplay();

    }

    private void clearIfEqualsShow(boolean addEqualAsValue) {
        if (equalShow) {
            clear();
            equalShow = false;
            if (addEqualAsValue) {
                values.add(equal);
            }
            updateDisplay();
        }
    }

    private void root(View e) {
        clearIfEqualsShow(false);

        openBracket++;
        values.add(SQUARE_ROOT_SYMBOL);
        updateDisplay();
    }

    private void rightBracket(View e) {

        if (openBracket < 1) {
            return;
        }
        if (values.get(values.size() - 1).contains("(") || lastEnteredIsOp()) {
            if (!values.get(values.size() - 1).equals(")"))
                values.add("0");
        }

        values.add(")");
        openBracket--;
        updateDisplay();
    }

    private void leftBracket(View e) {
        clearIfEqualsShow(false);
        if (openBracket > NUMBER_DIGIT_LIMIT) {
            toast("Close Brackets before you open more!");
            return;
        }
        values.add("(");
        openBracket++;
        updateDisplay();
    }

    private void dot(View e) {
        if (values.isEmpty()) {
            values.add("0");
            equalShow = false;
        }
        clearIfEqualsShow(true);
        if (!lastEnteredIsOp()) {
            String lastEnteredNum = values.get(values.size() - 1);
            if (lastEnteredNum.length() < NUMBER_DIGIT_LIMIT) {
                if (!lastEnteredNum.contains(".")) {
                    lastEnteredNum += ".";
                    values.set(values.size() - 1, lastEnteredNum);
                }

            }
        } else {
            values.add("0.");
        }
        updateDisplay();
    }

    private void numberClicked(View e) {

        NumberBtn numberBtn = numberMap.get((double) e.getId());
        if (numberBtn != null) {
            clearIfEqualsShow(false);


            if (values.isEmpty()) {
                values.add(String.valueOf(numberBtn.getNumberValue()));
            } else if (!lastEnteredIsOp()) {
                String lastEnteredNum = values.get(values.size() - 1);
                if (lastEnteredNum.length() < NUMBER_DIGIT_LIMIT) {
                    lastEnteredNum += numberBtn.getNumberValue();
                    if (!lastEnteredNum.startsWith("0."))
                        lastEnteredNum = lastEnteredNum.replaceFirst("^0+(?!$)", "");
                    values.set(values.size() - 1, lastEnteredNum);
                }
            } else {
                values.add(String.valueOf(numberBtn.getNumberValue()));
            }


            updateDisplay();

        } else toast("Error Number Value ");
    }

    private void opClicked(View e) {

        OpBtn opbtn = operationMap.get(e.getId());

        if (opbtn != null) {
            clearIfEqualsShow(true);
            if (values.isEmpty()) {
                return;
            }
            if (lastEnteredIsBasicOp()) {
                values.set(values.size() - 1, opbtn.getOperationString());
            } else if (lastEnteredIsOp()) {
                if (!values.get(values.size() - 1).equals(")"))
                    values.add("0");
                values.add(opbtn.getOperationString());
            } else {
                values.add(opbtn.getOperationString());
            }
        }

        updateDisplay();

    }

    private void deleteLastDigit(View e) {

        if (values.isEmpty()) return;

        if (equalShow) {
            equalShow = false;
            updateDisplay();
            return;
        }


        if (lastEnteredIsOp()) {
            String valueToRemove = values.get(values.size() - 1);
            if (valueToRemove.contains("(")) {
                openBracket--;
            } else if (valueToRemove.contains(")")) {
                openBracket++;
            }
            values.remove(values.size() - 1);

        } else {
            String s = values.get(values.size() - 1);
            if (s.length() == 1) values.remove(values.size() - 1);
            else {
                s = s.substring(0, s.length() - 1);
                values.set(values.size() - 1, s);
            }
        }
        updateDisplay();


    }

    private boolean lastEnteredIsOp() {
        if (values.isEmpty()) return false;
        return Calc.isOp(values.get(values.size() - 1));


    }

    private boolean lastEnteredIsBasicOp() {
        if (values.isEmpty()) return false;
        return Calc.isBasicOp(values.get(values.size() - 1));


    }

    private void assignOpBtnAndEvent(int id, int opValue) {
        Button btn = findViewById(id);
        btn.setOnClickListener(this::opClicked);
        operationMap.put(id, new OpBtn(btn, opValue, id));
    }

    private void clear() {
        values.clear();
        openBracket = 0;
        equalShow = false;
        updateDisplay();
    }

    public void removeIfLastIsOp() {
        if (lastEnteredIsOp()) {
            values.remove(values.size() - 1);
        }
    }

    private void updateDisplay() {
        StringBuilder text = new StringBuilder();
        for (String value : values) {
            text.append(value).append(" ");
        }
        if (equalShow) {
            text.append(" = ").append(equal);
        }
        calcScreen.setText(text.toString());
    }


    private void equal(View e) {
        valuesParsed.clear();
        stack.clear();
        try {
            if (lastEnteredIsOp()) {
                removeLastEntered();
            }
            for (; openBracket > 0; openBracket--) {
                values.add(")");

            }
            Calc.parseValues();
            equal = String.valueOf(Calc.calcAll());
            if (isInt(Double.parseDouble(equal))) {
                equal = String.valueOf((long) Double.parseDouble(equal));

            }
            equalShow = true;
            updateDisplay();

        } catch (Exception error) {

            toast(" ERROR: not valid!!");
        }
    }

    private void removeLastEntered() {
        if (!values.isEmpty()) {
            if (values.get(values.size() - 1).equals(")")) {
                return;
            }
            values.remove(values.size() - 1);
        }
    }


    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}