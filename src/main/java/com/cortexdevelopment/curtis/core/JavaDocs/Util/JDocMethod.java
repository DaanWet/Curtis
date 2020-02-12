package com.cortexdevelopment.curtis.core.JavaDocs.Util;

import java.util.HashMap;

public class JDocMethod extends JDocLink {

    private String modifier;
    private JDocLink returnType;
    private HashMap<String, JDocLink> arguments;


    public JDocMethod(String name){
        super(name);
        arguments = new HashMap<>();
        returnType = new JDocLink("void");
        modifier = null;
    }

    public JDocMethod(String name, String URL){
        super(name, URL);
        arguments = new HashMap<>();
        returnType = new JDocLink("void");
        modifier = null;
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

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setReturnType(JDocLink returnType) {
        this.returnType = returnType;
    }

    public void setArguments(HashMap<String, JDocLink> arguments) {
        this.arguments = arguments;
    }

    public String getModifier() {
        return modifier;
    }

    public JDocLink getReturnType() {
        return returnType;
    }

    public HashMap<String, JDocLink> getArguments() {
        return arguments;
    }
}
