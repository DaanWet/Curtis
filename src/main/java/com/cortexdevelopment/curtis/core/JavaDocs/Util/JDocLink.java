package com.cortexdevelopment.curtis.core.JavaDocs.Util;

public class JDocLink {

    protected String name;
    protected String URL;

    protected String description;
    protected boolean deprecated;

    public JDocLink(String name, String URL){
        this.name = name;
        this.URL = URL;
        deprecated = false;
    }

    public JDocLink(String name){
        this.name = name;
        this.URL = null;
        deprecated = false;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }
}
