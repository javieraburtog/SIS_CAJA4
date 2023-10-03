/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ni.com.casapellas.client.config;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Francisco Landeros
 */
public class OperationProperties extends ConnectionSqlite {

    public OperationProperties(String path) {
        super(path);
    }

    public boolean insert(Properties properties) {
        boolean insert = false;
        if (properties != null) {
            open();
            try {
                String sSql = "insert into Properties (code, name, value, state) "
                        + "values('" + properties.getCode()
                        + "','" + properties.getName() + "',"
                        + "'" + properties.getValue()+ "'," 
                        + properties.getState() + ")";
                getStatement().executeUpdate(sSql);
                insert = true;
            } catch (SQLException e) {
                insert = false;
                e.printStackTrace();
            } finally {
                try {
                    getStatement().close();
                    getConnection().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return insert;
    }

    public List<Properties> executeSelect(String sql) {
        List<Properties> propertieses = new ArrayList<Properties>();
        open();
        try {
            ResultSet result = getStatement().executeQuery(sql);
            int count = result.getMetaData().getColumnCount();
            while (result.next()) {
                Properties properties = new Properties();
                for (int i = 1; i <= count; i++) {
                    try {
                        setPropertyValues(properties, result.getMetaData().getColumnName(i), result.getObject(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                propertieses.add(properties);
            }
        } catch (SQLException e) {
        	e.printStackTrace();

        }
        close();
        return propertieses;
    }

    private void setPropertyValues(Properties prop, String fieldName, Object value) throws IllegalAccessException {
        Field[] fields = prop.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getName().compareToIgnoreCase(fieldName) == 0) {
                field.setAccessible(true);
                field.set(prop, value);
            }
        }
    }
}
