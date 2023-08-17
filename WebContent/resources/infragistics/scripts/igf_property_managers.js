// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");$IG.CollectionsManager=function(control,collections)
{this._control=control;this._collections=collections;this._count=collections?collections.length:0;this._itemCollections=[];this._clientStateManagers=[];this._items=[];this._itemCount=[];this._uiBehaviors=[];for(var i=0;i<this._count;i++)
{this._itemCount[i]=0;this._items[i]={};this._clientStateManagers[i]=new $IG.CollectionClientStateManager(collections[i]);}}
$IG.CollectionsManager.prototype={get_collection:function(index)
{return this._collections?this._collections[index]:null;},get_count:function()
{return this._count;},get_allTransactionLists:function()
{var state=[];for(var i=0;i<this._count;i++)
state[i]=this.get_transactionList(i);return state;},get_transactionList:function(index)
{return this._clientStateManagers[index].get_transactionList()},register_collection:function(index,collectionType,noBehavior)
{var collection=this._itemCollections[index]=new collectionType(this._control,this._clientStateManagers[index],index,this);return collection;},registerUIBehaviors:function(collection)
{var index=collection._index;this._uiBehaviors[index]=new $IG.UIBehaviorsObject(this._control,collection)},getItemCount:function(index)
{return this._itemCount[index];},getUIBehaviorsObj:function(index)
{return this._uiBehaviors[index];},addObject:function(index,adr,object)
{this._items[index][adr]=object;var uiBehaviorObj=this._uiBehaviors[index];if(uiBehaviorObj)
{if(object._getFlags().getSelected())
uiBehaviorObj.select(object);}
this._itemCount[index]++;},getObject:function(index,adr)
{return this._items[index][adr];},getServerCollection:function(vse)
{if(vse)
{var collections=[];var index=this._collections?this._collections.length:0;while(index-->0)
{collections[index]={};var csm=this._clientStateManagers[index];for(var adr in this._collections[index])
collections[index][adr]=csm.get_serverProps(adr);}
return collections;}
return null;},dispose:function()
{if(!this._itemCollections)
return;var count=this._itemCollections.length;for(var i=0;i<count;i++)
{if(this._uiBehaviors[i])
this._uiBehaviors[i].dispose();this._itemCollections[i].dispose();var item=this._items[i]
for(var adr in item)
{var obj=item[adr];if(obj&&obj.dispose)
obj.dispose();}}
this._control=null;this._collections=null;this._itemCollections=null;this._clientStateManagers=null;this._items=null;this._itemCount=null;this._uiBehaviors=null;}};$IG.CollectionsManager.registerClass('Infragistics.Web.UI.CollectionsManager');$IG.ObjectsManager=function(control,objects)
{this._objects=objects;this._control=control;this._clientStateManagers=[];this._objectCollection=[];this._count=objects?objects.length:0;}
$IG.ObjectsManager.prototype={get_objectProps:function(index)
{return this._objects?this._objects[index]:null;},get_count:function()
{return this._count;},register_object:function(index,object)
{var objects=this._objects;if(!objects||!object)
return;this._clientStateManagers[index]=object._csm;this._objectCollection[index]=(object);var objectProps=objects[index];objectProps.objectsManager=new $IG.ObjectsManager(object,objectProps[1]);objectProps.collectionsManager=new $IG.CollectionsManager(object,objectProps[2]);objectProps.registered=true;object._createObjects(objectProps.objectsManager);object._createCollections(objectProps.collectionsManager);},get_object:function(index)
{return this._objectCollection[index];},get_allTransactionLists:function()
{var state=[];for(var i=0;i<this._count;i++)
state[i]=this.get_transactionList(i);return state;},get_csm:function(index)
{return this._clientStateManagers[index];},getServerObjects:function(vse)
{var objects=[],index=this._objects?this._objects.length:0;while(index-->0)
{var object=this._objects[index];if(object.registered)
{var csm=this._clientStateManagers[index];var state=[[csm.get_serverProps(vse),object.objectsManager.getServerObjects(vse),object.collectionsManager.getServerCollection(vse)]];state[1]=[csm.get_transactionList(),object.collectionsManager.get_allTransactionLists()];state[2]=this._objectCollection[index]._saveAdditionalClientState();objects[index]=state;}
else
{objects[index]=this._getUnRegisteredServerObjects(object);}}
return objects;},_getUnRegisteredServerObjects:function(obj)
{var returnObjects=[];var objs=obj[1];for(var i=0;objs&&i<objs.length;i++)
{returnObjects.push(this._getUnRegisteredServerObjects(objs[i]));}
return[[obj[0],returnObjects,obj[2]],[null,null],[null]];},get_transactionList:function(index)
{var csm=this._clientStateManagers[index];if(csm)
return csm.get_transactionList();return null;},dispose:function()
{var items=this._objectCollection;if(!items)
return;var i=items.length;while(i-->0)
{if(items[i]&&(!Sys.Component.isInstanceOfType(items[i])||!Sys.Application._disposing))
items[i].dispose();}
this._control=null;this._objects=null;this._clientStateManagers=null;this._objectCollection=null;}};$IG.ObjectsManager.registerClass('Infragistics.Web.UI.ObjectsManager');