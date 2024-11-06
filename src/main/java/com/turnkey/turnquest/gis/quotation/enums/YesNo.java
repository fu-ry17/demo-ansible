/**
 *
 */
package com.turnkey.turnquest.gis.quotation.enums;

/**
 * @author Paul Gichure
 */
public enum YesNo {

    Y("Yes", true, "YES", "Y"),
    N("No", false, "NO", "N");

    private String name;
    private boolean isTrue;
    private String shortName;
    private String literal;

    /**
     *
     */
    private YesNo(String name, boolean isTrue, String shortName, String literal) {
        this.name = name;
        this.isTrue = isTrue;
        this.literal = literal;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isTrue
     */
    public boolean isTrue() {
        return isTrue;
    }

    /**
     * @param isTrue the isTrue to set
     */
    public void setTrue(boolean isTrue) {
        this.isTrue = isTrue;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }
}
