package ni.com.casapellas.client.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConfigConnection {
	
   //Datos para Consumir Web Services de Proyecto CANALES ALTERNOS (Integración con Bancos V1).
	public static String PROTOCOL = "http";
	public static String SERVER = "172.17.17.20";
	public static String PORT = "9083";
    public static String CONTEXT_NAME_WS_CAJA = "GCPMCAJAWSREST";
    public static String CONTEXT_NAME_CAJA = "gcpmcajarest";

   //Datos para Consumir Web Services de Proyecto FINANCIAMIENTO V1.
    public static String PROTOCOL_WS_FINAN = "http";
    public static String SERVER_WS_FINAN = "172.17.17.20";
	public static String PORT_WS_FINAN = "9083";
    public static String CONTEXT_NAME_WS_FINANCIAMIENTO = "GCPFINANCIAMIENTOWSREST";
    public static String CONTEXT_NAME_FINANCIAMIENTO = "gcpfinanciamientorest";
    
    //Datos para Consumir Web Services de Proyecto CANALES ALTERNOS (Integración con Bancos V2).
    public static String PROTOCOL_WS_CAJA_N = "http";
    public static String SERVER_WS_CAJA_N = "172.17.17.20";
	public static String PORT_WS_CAJA_N = "9083";
    public static String CONTEXT_NAME_WS_CAJA_N = "GCPCAJAWSRESTV2";
    public static String CONTEXT_NAME_CAJA_N = "gcpcajarest";
    
   // Ambiente de Desarrollo/Pruebas, ESQUEMAS de Base de Datos  
    public static String SchemaCaja = "E2GCPMCAJA";								// Esquema Sistema de Caja
    public final static String SchemaENS = "E2ENS";								// Esquema de Entorno de Navegación y Seguridad (ENS)
	public final static String SchemaJDEDTA = "PRODDTA920";							// Esquema de Jd Edwards E1 
	public final static String SChemaJDECTL = "PRODCTL920";							// Esquema de Jd Edwards E1 - Tablas compartidas
	
   // Código de Usuario para Conexión a Base de Datos DB2  
    public static String USUARIO = "appcp";
	public static String PASSWRD = "appcp1810";
	public static String CLASSFORNAME  	= "com.ibm.as400.access.AS400JDBCDriver";
	public static String URLSERVER	 	= "jdbc:as400://192.168.1.3;prompt=false";

   // Datos de Servidor de eMails
	public final static String ipservermail = "192.168.1.137";
	
	/******************************************************************************************/
	
	/** Método: Obtener la conexion con el servidor
	 *	Fecha:  18/07/2011
	 *  Nombre: Carlos Manuel Hernández Morrison.
	 *  Modif: 	Luis Alberto Fonseca
	 *  Fecha:	16/07/2018
	 */
	
    public static Connection getConnection(){
    	Connection cn = null;
        try {
        	
            Class.forName(ConfigConnection.CLASSFORNAME);
            cn = DriverManager.getConnection(ConfigConnection.URLSERVER, 
            								 ConfigConnection.USUARIO, 
            								 ConfigConnection.PASSWRD); 
        }catch(Exception e){
        	cn = null;
        	e.printStackTrace();
        }
        return cn;
    }
}
