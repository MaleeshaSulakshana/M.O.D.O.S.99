package com.example.modos99;

public class selectedRadio {

    private String radioValue, btnCommand;

    public void setRadioValues(String value, String command) {
        this.radioValue = value;
        this.btnCommand = command;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public String getBtnCommand() {
        return btnCommand;
    }

}
