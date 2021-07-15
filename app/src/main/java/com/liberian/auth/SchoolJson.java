package com.liberian.auth;

public class SchoolJson {

    String school;
    String meaning;

    public SchoolJson(String school, String meaning) {
        this.school = school;
        this.meaning = meaning;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
