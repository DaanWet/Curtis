package com.cortexdevelopment.curtis.core.JavaDocs.Util;

import java.util.HashMap;

public class JDocConstructor extends JDocLink{

    private HashMap<String, JDocLink> arguments;

    public JDocConstructor(String name){
        super(name);
        arguments = new HashMap<>();
    }

    public JDocConstructor(String name, String URL){
        super(name, URL);
        arguments = new HashMap<>();
    }

    public void addArgument(String name, JDocLink type){
        arguments.put(name, type);
    }

    public String getFullName(){
        StringBuilder sb = new StringBuilder().append(super.name).append("(");
        for (String name : arguments.keySet()){
            sb.append(arguments.get(name).getName()).append(" ").append(name).append(", ");
        }
        if (sb.charAt(sb.length() - 2) == ',') {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
        return sb.toString();
    }

    public void setArguments(HashMap<String, JDocLink> arguments) {
        this.arguments = arguments;
    }

    public HashMap<String, JDocLink> getArguments() {
        return arguments;
    }


}
