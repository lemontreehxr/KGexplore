/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.model;

import java.util.List;

public class Dropdown {
    private List<String> dropdownList;

    public Dropdown(List<String> autoCompletionList) {
        setDropdownList(autoCompletionList);
    }

    public List<String> getDropdownList() {
        return dropdownList;
    }

    public void setDropdownList(List<String> dropdownList) {
        this.dropdownList = dropdownList;
    }

    @Override
    public String toString() {
        String s = "\n\tDropdown{";
        for(String dropdown : dropdownList)
            s += "\n\t\t" + dropdown;
        s += "\n\t}";

        return s;
    }
}
