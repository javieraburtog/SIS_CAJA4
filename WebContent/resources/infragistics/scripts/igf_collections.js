// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");$IG.ObjectCollection=function(control,clientStateManager,index,manager)
{this._control=control;this._csm=clientStateManager;this._index=index;this._manager=manager;this._items=[];this._collectionType=$IG.ObjectCollection;}
$IG.ObjectCollection.prototype={_addObject:function(objectType,element,adr)
{var object=new objectType(adr,element,null,this._control,this._csm);this._items.push(object);this._manager.addObject(this._index,adr,object);return object;},_addExistingObject:function(object,adr,props)
{this._items.push(object);this._manager.addObject(this._index,adr,object);object._csm=this._csm;object._address=adr;var currentClientState=this._csm.get_clientState();currentClientState[adr]=props;this._csm._items=currentClientState;return object;},_createObject:function(adr,element)
{},_getObjectByAdr:function(adr)
{return this._manager.getObject(this._index,adr);},_getObjectByIndex:function(index)
{return this._items[index];},_getUIBehaviorsObj:function()
{return this._manager.getUIBehaviorsObj(this._index);},get_length:function()
{return this._manager.getItemCount(this._index);},get_indexOf:function(item)
{for(var i=0;i<this._items.length;i++)
if(this._items[i]==item)
return i;return-1;},dispose:function()
{this._control=null;this._manager=null;this._items=null;this._csm=null;}};$IG.ObjectCollection.registerClass('Infragistics.Web.UI.ObjectCollection');$IG.NavItemCollection=function(control,clientStateManager,index,manager)
{$IG.NavItemCollection.initializeBase(this,[control,clientStateManager,index,manager]);}
$IG.NavItemCollection.prototype={_createNewCollection:function()
{return new $IG.NavItemCollection(this._control,this._csm,this._index,this._manager);},_addObject:function(navItemType,element,adr)
{var object=null;var newCollection=this._createNewCollection();var indexes=adr.split('.');if(indexes.length==1)
{var val=parseInt(adr);if(val.toString()!="NaN")
object=this._items[val]=new navItemType(adr,element,null,this._control,this._csm,newCollection,null);}
else
{var parent=this._items[indexes[0]];for(var i=1;i<indexes.length-1;i++)
{if(parent!=null)
parent=parent.getItems()._getObjectByIndex(indexes[i]);}
if(parent!=null)
object=parent.getItems()._items[indexes[indexes.length-1]]=new navItemType(adr,element,null,this._control,this._csm,newCollection,parent);}
this._manager.addObject(this._index,adr,object);return object;}};$IG.NavItemCollection.registerClass('Infragistics.Web.UI.NavItemCollection',$IG.ObjectCollection);