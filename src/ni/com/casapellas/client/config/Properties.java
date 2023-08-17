/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ni.com.casapellas.client.config;

/**
 *
 * @author Francisco Landeros
 */
public class Properties {

    private int id;
    private String code;
    private String name;
    private String value;
    private int state;

    public Properties() {
    }

    public Properties(String code, String name, String value, int state) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
