// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.1.20091.1038

$IG.TreeBehaviorCollection=function(control)
{this._tree=control;this._eventHandlerHash=new Object();this._eventHandlerHash.length=0;this._eventHandlerHash.handlers={};this._behaviors=[];}
$IG.TreeBehaviorCollection.prototype={get_tree:function()
{return this._tree;},add:function(behavior)
{this._behaviors.push(behavior);},_fireEvent:function(element,event)
{var key=this._tree.get_behaviors().__resolveKey(element,event.type);var handler=this._tree.get_behaviors()._eventHandlerHash[key];if(handler)
for(i=0;i<handler.length;i++){handler[i](event);}},_addElementEventHandler:function(element,eventType,handler)
{var key=this.__resolveKey(element,eventType);var resolvedHashItem=this._eventHandlerHash[key];if(!resolvedHashItem)
{resolvedHashItem=this._eventHandlerHash[key]=[];this._eventHandlerHash.length++;}
for(var i=0;i<resolvedHashItem.length;i++)
{if(resolvedHashItem[i]==handler)
return false;}
if(resolvedHashItem.length==0)
{var internalHandler=this._eventHandlerHash.handlers[key]=Function.createDelegate(resolvedHashItem,this._elementEventHandler);}
resolvedHashItem[resolvedHashItem.length]=handler;return true;},_removeElementEventHandler:function(element,eventType,handler)
{var key=this.__resolveKey(element,eventType);var resolvedHashItem=this._eventHandlerHash[key];if(!resolvedHashItem)
return false;for(var i=0;i<resolvedHashItem.length;i++)
{if(resolvedHashItem[i]==handler)
{resolvedHashItem.splice(i,1);if(resolvedHashItem.length==0)
{try
{$removeHandler(element,eventType,this._eventHandlerHash.handlers[key]);}catch(e){};delete this._eventHandlerHash[key];delete this._eventHandlerHash.handlers[key];}
return true;}}
return false;},__resolveKey:function(element,eventType)
{if(!element)
return null;var ehh_id=element.id;if(element.getAttribute)
{if(!ehh_id)
ehh_id=element.getAttribute("_ehh_id");if(!ehh_id)
{ehh_id="_ehh_id_"+this._eventHandlerHash.length;element.setAttribute("_ehh_id"+eventType,ehh_id);}}
else
{ehh_id=element._ehh_id;if(!ehh_id)
ehh_id=element._ehh_id="_ehh_id_"+this._eventHandlerHash.length;}
return ehh_id+"_"+eventType;},_elementEventHandler:function(evnt)
{var resolvedHashItem=this;for(var i=0;i<resolvedHashItem.length;i++)
resolvedHashItem[i](evnt);},_initializeBehaviors:function()
{if(!this._behaviors)
return;for(var i=0;i<this._behaviors.length;i++)
{var behavior=this._behaviors[i];if(behavior)
behavior._initializeComplete();}},getBehaviorFromInterface:function(behaviorInterface)
{if(!behaviorInterface)
return null;var behavior=null;for(var i=0;i<this._behaviors.length;i++)
{if(behaviorInterface.isInstanceOfType(this._behaviors[i]))
{behavior=this._behaviors[i];break;}
if($IG.ITreeBehaviorContainer.isInstanceOfType(this._behaviors[i]))
{behavior=this._behaviors[i].get_behaviors().getBehaviorFromInterface(behaviorInterface);if(behavior!=null)
break;}}
return behavior;}}
$IG.TreeBehaviorCollection.registerClass('Infragistics.Web.UI.TreeBehaviorCollection',$IG.ObjectBase);$IG.TreeBehavior=function(obj,objProps,control,parentCollection)
{var props=objProps[0];var clientEvents=objProps[3];var csm=obj?new $IG.ObjectClientStateManager(props):null;$IG.TreeBehavior.initializeBase(this,[obj,control._element,props,control,csm]);this._parentCollection=parentCollection;this._owner._initClientEventsForObject(this,clientEvents);this.__raiseClientEvent('Initialize');}
$IG.TreeBehavior.prototype={_initializeComplete:function()
{},get_name:function()
{return this._get_value($IG.TreeBehaviorProps.Name);},dispose:function()
{},__raiseClientEvent:function(clientEventName,evntArgs,eventArgsParams)
{var args=null;var tree=this._tree;var clientEvent=this._clientEvents[clientEventName];if(clientEvent!=null)
{if(evntArgs==null)
args=new $IG.EventArgs();else
args=new evntArgs(eventArgsParams);args=tree._raiseSenderClientEvent(this,clientEvent,args);}
return args;},_responseComplete:function(callbackObject,responseObject)
{}}
$IG.TreeBehavior.registerClass('Infragistics.Web.UI.TreeBehavior',$IG.ObjectBase);$IG.TreeBehaviorProps=new function()
{var count=$IG.ObjectBaseProps.Count;this.Name=[count++,''];this.Count=count;};$IG.ITreeBehaviorContainer=function(){};$IG.ITreeBehaviorContainer.prototype={_initializeBehaviors:function(behaviorProps,control,parentCollection)
{if(!behaviorProps)
return;for(var i=0;i<behaviorProps.length;i++)
{var objProps=behaviorProps[i];var objName=objProps[0][0][0];var propName=objName;if(propName.indexOf("Tree")==0)
propName=propName.replace("Tree","");var behaviorCollection=this.get_behaviors();var behavior=this[propName]=new $IG[objName]((objName+i),objProps,control,parentCollection);behaviorCollection.add(behavior);this._objectsManager.register_object(i,behavior);}
this.get_behaviors()._initializeBehaviors();},get_behaviors:function(){throw"Not implemented: get_behaviors()!"}};$IG.ITreeBehaviorContainer.registerInterface("Infragistics.Web.UI.ITreeBehaviorContainer");$IG.TreeBehaviorContainer=function(obj,objProps,control,parentCollection)
{$IG.TreeBehaviorContainer.initializeBase(this,[obj,objProps,control,parentCollection]);}
$IG.TreeBehaviorContainer.prototype={_createObjects:function(objectsManager)
{this._objectsManager=objectsManager;this._initializeBehaviors(objectsManager._objects,this._owner,this._owner.get_behaviors());},_behaviors:null,get_behaviors:function()
{if(this._behaviors==null)
this._behaviors=new $IG.TreeBehaviorCollection(this);return this._behaviors;}}
$IG.TreeBehaviorContainer.registerClass('Infragistics.Web.UI.TreeBehaviorContainer',$IG.TreeBehavior,$IG.ITreeBehaviorContainer);$IG.TreeAction=function(type,ownerName,object,value,tag)
{this.type=type;this.ownerName=ownerName;this._object=object;if(value)
this._value=value;if(tag)
this._tag=tag;};$IG.TreeAction.prototype={type:"",ownerName:"",_object:null,_value:null,_tag:null,_transactionList:null,_index:-1,get_action:function()
{return{ownerName:this.ownerName,type:this.type,id:(this._object.get_idPair?this._object.get_idPair():null),value:this.get_value(),tag:this.get_tag()};},get_value:function()
{return this._value;},get_tag:function()
{return this._tag;}}
$IG.TreeAction.registerClass('Infragistics.Web.UI.TreeAction');$IG.TreeActionTransactionList=function()
{$IG.TreeActionTransactionList.initializeBase(this);}
$IG.TreeActionTransactionList.prototype={add_transaction:function(action,keepPrevious)
{if(typeof(keepPrevious)=="undefined")
keepPrevious=false;if(!keepPrevious)
{var prevAction=action._object["_action"+action.type];if(prevAction&&prevAction._transactionList==this)
this.remove_transaction(prevAction);}
action._transactionList=this;action._object["_action"+action.type]=action;action._index=this._count;this._orderedList[this._count]=action;this._count++;},remove_transaction:function(action)
{delete action._object["_action"+action.type];delete this._orderedList[action._index];},get_value:function(action)
{return action.get_action();},get_list:function()
{var list=[];var i=0;for(var action in this._orderedList)
list[i++]=this._orderedList[action].get_action();return list;},get_actionListForType:function(type){var list=[];var i=0;for(var action in this._orderedList){if(this._orderedList[action].type==type)
list[i++]=this._orderedList[action];}
return list;}}
$IG.TreeActionTransactionList.registerClass('Infragistics.Web.UI.TreeActionTransactionList',$IG.TransactionListBase);$IG.IUpdatingBehavior=function()
{};$IG.IUpdatingBehavior.prototype={enterEditMode:function(cell)
{},exitEditMode:function()
{},get_isInEditMode:function(cell)
{},_addExitKeyHandledEventListener:function(handler)
{},_registerEditableRow:function(rowElement)
{},_addEnteringEditEventListener:function(handler)
{},_addExitedEditEventListener:function(handler)
{}};$IG.IUpdatingBehavior.registerInterface("Infragistics.Web.UI.IUpdatingBehavior");$IG.IDPair=function(index,key)
{if(typeof(index)!="undefined")
this.index=index;if(typeof(key)!="undefined"&&key!==null)
{if(typeof(key)=="string")
this.key=key.split(",");else if(key.length)
this.key=key;else
this.key=[key];}};$IG.IDPair.prototype={index:-1,key:[]}
$IG.TreeUtility=function(treeObj)
{this._tree=treeObj;};$IG.TreeUtility.prototype={_registerEventListener:function(obj,evntName,listener)
{if(obj._internalEventListeners==null)
obj._internalEventListeners={};if(obj._internalEventListeners[evntName]==null)
obj._internalEventListeners[evntName]=[];obj._internalEventListeners[evntName].push(listener);},_fireEvent:function(obj,evntName,args)
{if(obj._internalEventListeners==null)
return;var listeners=obj._internalEventListeners[evntName];if(listeners!=null&&listeners.length>0)
{for(var i=0;i<listeners.length;i++)
if(listeners[i](args))
return true;}
return false;},_unregisterEventListener:function(obj,evntName,listener)
{if(obj._internalEventListeners==null)
return;var listeners=obj._internalEventListeners[evntName];if(listeners!=null&&listeners.length>0)
{for(var i=0;i<listeners.length;i++)
{if(listeners[i]==listener)
{delete obj._internalEventListeners[evntName][i];return;}}}}};$IG.TreeUtility.registerClass("Infragistics.Web.UI.TreeUtility");