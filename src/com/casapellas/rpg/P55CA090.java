/* 
 * @(#)P55CA090.java       09:39:09 PM 2011/08/23
 *
 * Class generated by iSeries tools.  Such as the Web Interaction, 
 * the Web Service, or the Program Call Wizards.
 */ 
package com.casapellas.rpg;
import com.ibm.as400.access.*;
import com.ibm.connector.as400.ProgramCallBean;

public class P55CA090 extends ProgramCallBean
{

  public P55CA090()
  {
    super();
    setJavaAppMode(true);
    setRuntimeConfigFile("defaultPCWPOS");
    setPcmlName("com.casapellas.rpg.P55CA090");
    setProgramName("P55CA090");
    initInput();
  }

  public P55CA090( String jndiName )
  {
    super(jndiName);
    setJavaAppMode(true);
    setRuntimeConfigFile("defaultPCWPOS");
    setPcmlName("com.casapellas.rpg.P55CA090");
    setProgramName("P55CA090");
    initInput();
  }

	/*
	 * Initialize input paramters to default values.
	 */
	public void initInput() {
		setCIA("");

		setTERMINAL("");

		setDIGITO("");

		setREFERENC("");

		setAUTORIZ("");

		setFECHA("");

		setVARIOS("");

	}
	public String getCIA() {
		return (String) getValue(_strParentField + ".CIA");
	}

	public void setCIA(String CIA) {
		setValue(_strParentField + ".CIA", CIA);
	}


	public String getTERMINAL() {
		return (String) getValue(_strParentField + ".TERMINAL");
	}

	public void setTERMINAL(String TERMINAL) {
		setValue(_strParentField + ".TERMINAL", TERMINAL);
	}


	public String getDIGITO() {
		return (String) getValue(_strParentField + ".DIGITO");
	}

	public void setDIGITO(String DIGITO) {
		setValue(_strParentField + ".DIGITO", DIGITO);
	}


	public String getREFERENC() {
		return (String) getValue(_strParentField + ".REFERENC");
	}

	public void setREFERENC(String REFERENC) {
		setValue(_strParentField + ".REFERENC", REFERENC);
	}


	public String getAUTORIZ() {
		return (String) getValue(_strParentField + ".AUTORIZ");
	}

	public void setAUTORIZ(String AUTORIZ) {
		setValue(_strParentField + ".AUTORIZ", AUTORIZ);
	}


	public String getFECHA() {
		return (String) getValue(_strParentField + ".FECHA");
	}

	public void setFECHA(String FECHA) {
		setValue(_strParentField + ".FECHA", FECHA);
	}


	public String getVARIOS() {
		return (String) getValue(_strParentField + ".VARIOS");
	}

	public void setVARIOS(String VARIOS) {
		setValue(_strParentField + ".VARIOS", VARIOS);
	}




    /**
     * Invoke the host program.
     *
     * The connection data, i.e. host information, must be set.
     * All input parameter(s) should have been set.
     */
    public void invoke()
    {
      super.invoke();
    }

    /**
     * Set connection object.
     * To display signon prompt, pass an AS400 with 
     * partial authentication, such as:
     *        new AS400("myiSeries")
     *        new AS400("myiSeries","myID")
     *
     * @param as400Obj AS400 A toolbox AS400 object
     */
    public void setConnectionData( AS400 as400Obj )
    {
      super.setConnectionData( as400Obj );
    }

    /**
     * Set connection authentication parameters.
     * These values will be used to authenticate quietly
     * to the iSeries.  Invalid authentication parameters
     * will result in Exception, not the signon prompt.
     *
     * @param hostname String iSeries hostname
     * @param userid   String user id
     * @param passwd   String password
     */
    public void setConnectionData( String hostname, String userid, String passwd )
    {
      super.setConnectionData(hostname, userid, passwd );
    }

    /**
     * Set program path.
     * Allows for dynamically specifying the program path.
     * e.g. /QSYS.LIB/MYDEV.LIB/TEST1.PGM
     *
     * @param path A String containing the path to the program object to be run on the server.
     */
    public void setPath( String path )
    {
      super.setPath(path);
    }

    /**
     * Get the AS400 object
     *
     * @return AS400 AS400 Toolbox connection object
     */
    public AS400 getAS400Object( )
    {
      return super.getAS400Object();
    }

    /**
     * Initialize the user portion of library list for the server job.
     *
     * @param liblist  String[] library list
     */
    public void setLibraryList( String[] liblist )
    {
      super.setLibraryList(liblist);
    }

    /**
     * Add a list of library positions
     * e.g *FIRST,*LAST
     *
     * @param liblistPos Sting[] library list
     */
    public void setLibraryListPos( String[] liblistPos)
    {
      super.setLibraryListPos(liblistPos);
    }

    /**
     * Set the current library to be a named library, *CRTDFT, or *USRPRF.
     * If *CRTDFT is set, any objects that are created into the current library
     * using *CURLIB on the create command, use library QGPL as the default current library. 
     * If *USRPRF is set, then the setting in the user profile is used.
     *
     * @param curLib String current library
     */
     public void setCurrentLibrary( String curLib )
     {
       super.setCurrentLibrary(curLib);
     }

    /**
     * Specify the host command to execute after signing on to the host system
     * in the Initial command field.
     * For example, you can specify a CL setup program to set the environment
     * before invoking applications.
     *
     * @param initCmd String initial command
     */
     public void setInitialCommand( String initCmd )
     {
       super.setInitialCommand(initCmd);
     }
  
    /**
     * Disconnect the iSeries connection
     *
     */
    public void disconnect( )
    {
      super.disconnect();
    }

    /**
     * Enable Pcml trace
     *
     */
    public void setTraceEnabled( boolean bEnabled )
    {
      super.setTraceEnabled(bEnabled);
    }



	private String _strParentField = "P55CA090";
	private static final long serialVersionUID = -2264968535713574473L;}
