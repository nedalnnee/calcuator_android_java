package com.msnit.nedalcalculater.util;

import android.widget.Button;

public class OpBtn {

    public final static int PLUS = 0;
    public final static int MINUS = 1;
    public final static int MULTIPLY = 2;
    public final static int DIVIDE = 3;

    Button btn;
    int opValue;
    int id;

    public OpBtn(Button btn, int opValue, int id) {
        this.btn = btn;
        this.opValue = opValue;
        this.id = id;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public int getOpValue() {
        return opValue;
    }

    public void setOpValue(int opValue) {
        this.opValue = opValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperationString() {
        switch (this.opValue) {
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTIPLY:
                return "×";
            case DIVIDE:
                return "/";
            default:
                return "?";

        }


    }
}
