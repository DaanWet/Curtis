package com.cortexdevelopment.curtis.core.JavaDocs.Util;

import java.util.ArrayList;

public class JDocClass extends JDocLink{

    private ArrayList<JDocConstructor> constructors;
    private ArrayList<JDocMethod> methods;


    public JDocClass(String name, String URL){
        super(name, URL);
    }

    public ArrayList<JDocConstructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(ArrayList<JDocConstructor> constructors) {
        this.constructors = constructors;
    }

    public ArrayList<JDocMethod> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<JDocMethod> methods) {
        this.methods = methods;
    }
}
