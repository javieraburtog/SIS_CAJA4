/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ni.com.casapellas.client.config;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Francisco Landeros
 */
public class Parameter {

    private List<String> parameters = new ArrayList<String>();

    public Parameter() {
    }

    public Parameter(List<String> parameters) {
        this.parameters = parameters;
    }
    
    public void addValue(String value){
        this.parameters.add(value);
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
