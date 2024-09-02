package com.teste.selaz.enums;

public enum Role {
    ADMIN("admin"),
    USER("admin");

    private String level;

    Role(String level){
        this.level = level;
    }
    public String getLevel(){
        return level;
    }
}
