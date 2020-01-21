package com.talent.posdat.home;

public class Country
{
    String country_name=null;
    int country_code=00263;
    boolean selected =false;

    public Country() {
    }

    public Country(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
