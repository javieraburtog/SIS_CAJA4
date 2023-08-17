/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.casapellas.socketpos;

import com.sun.jna.Library;
/**
 * @author Ferdy Rodriguez
 * Fecha: 18/11/2009
 *
 * @update Roger Aguero (ragueroz@baccredomatic.com)
 * Fecha: 20/04/2010
 *
 */

/**
 * Se declara el public interface donde se pondran las funciones que estan en la DLL.
 * Las Funciones tienen que ser llamadas igual que como se llaman en la DLL y los
 * parametros de tipo Char ser cambiados a tipo String.
 *
**/
public interface sockpos extends Library {

  public String dllpurchase(String termid, int autmedia, int auttype, String cardnumber, String amount, String expdate, String track, String tax, String tip, String cash, int seccode,int secresp, String secdata, int avs, String postalcode, String address, String invoice);
  public String dllcanceltrans(String termid, String reference, String authid, String systraceno);
  public String dlltest();
  public String dllsettlement(String termid);
}
