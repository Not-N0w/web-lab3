package com.github.not.n0w.weblab3.model;

import java.io.Serializable;

public class XCheckbox implements Serializable {
    private String value;
    private boolean selected;

    public XCheckbox(String value, boolean selected) {
        this.value = value;
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
