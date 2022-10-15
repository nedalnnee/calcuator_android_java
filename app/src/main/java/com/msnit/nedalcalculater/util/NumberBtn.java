package com.msnit.nedalcalculater.util;

import android.widget.Button;

public class NumberBtn {

    Button btn;
    int numberValue;
    int id;

    public NumberBtn(Button btn, int numberValue, int id) {
        this.btn = btn;
        this.numberValue = numberValue;
        this.id = id;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(int numberValue) {
        this.numberValue = numberValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
