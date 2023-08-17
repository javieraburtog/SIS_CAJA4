// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");var $IG=Infragistics.Web.UI;var theForm=document.forms[0];function __doPostBack(eventTarget,eventArgument){ig.submit(eventTarget,eventArgument,eventArgument);}
__igOldDoPostBack=window.__doPostBack;window.__doPostBack=__igdoPostBack;try
{Sys.Application.add_load(__igAppLoaded)
__igOldFormSubmit=theForm.submit;theForm.submit=__igFormSubmit;}
catch(ex){};function __igAppLoaded()
{if(Sys.WebForms.PageRequestManager)
{var prm=Sys.WebForms.PageRequestManager.getInstance()
if(!prm.__igSet)
{__igOldWebFormsFormSubmit=prm._onFormSubmit;prm._onFormSubmit=__igdoPostBack2;prm.__igSet=true;}
Sys.WebForms.PageRequestManager._initialize('scriptmanager1',document.forms[0]);Sys.WebForms.PageRequestManager.getInstance()._updateControls([],[],[],90);}}
if(typeof ig_controls!="object")
var ig_controls=new Object();function __igSubmit()
{if(ig_controls)
for(var id in ig_controls)
ig_controls[id]._onSubmitOtherHandler();}
function __igdoPostBack2(evt)
{__igSubmit();this._onFormSubmit=__igOldWebFormsFormSubmit;this._onFormSubmit(evt);this._onFormSubmit=__igdoPostBack2;}
function __igdoPostBack(eventTarget,eventArgument)
{__igSubmit();__igOldDoPostBack(eventTarget,eventArgument);}
function __igFormSubmit()
{if(ig_controls)
for(var id in ig_controls)
ig_controls[id]._onSubmitOtherHandler();if(ig.isNull(theForm))
{var theForm=document.forms[0];theForm.submit=__igOldFormSubmit;theForm.submit();}}
$IG.ControlMainProps=new function()
{this.Flags=[0,0];this.Count=1;};$IG.ControlMain=function(elem)
{$IG.ControlMain.initializeBase(this,[elem]);this._elements={};this._callbackManager=new $IG.ControlCallbackManager(this);this._callbackManager.setResponseComplete(this._responseComplete,this);}
$IG.ControlMain.prototype={initialize:function()
{this._setupCollections();this.__walkThrough(this._element,true);this._setupMarkerElements();$IG.ControlMain.callBaseMethod(this,'initialize');this.__attachEvents();this.__attachOtherEvents();this._uniqueID=this._get_clientOnlyValue("uid");ig_controls[this._id]=this;var rm=null;try
{rm=Sys.WebForms.PageRequestManager.getInstance();}catch(e){}
if(rm&&!rm._ig_onsubmit)
{rm._ig_onsubmit=rm._onsubmit;if(!rm._ig_onsubmit)
rm._ig_onsubmit=2;var form=rm._form;if(form&&typeof theForm=='object')
form=theForm;if(form&&!form._ig_submit)
{form._ig_submit=form.submit;form.submit=function()
{try
{if(typeof ig_controls=='object')
for(var id in ig_controls)
ig_controls[id]._onIgSubmit();}catch(e){}
if(this._ig_submit)
this._ig_submit();}}
rm._onsubmit=function()
{if(typeof ig_controls=='object')
for(var id in ig_controls)
ig_controls[id]._onIgSubmit();if(typeof this._ig_onsubmit=='function')try
{if(this._ig_onsubmit()===false)
return false;}catch(id){}
return true;}}},dispose:function()
{if(this._objectsManager)
this._objectsManager.dispose();if(this._collectionsManager)
this._collectionsManager.dispose();if(this._callbackManager)
this._callbackManager.dispose();if(this.get_element())
$clearHandlers(this.get_element());this.__clearOtherEvents();if(this._flags!=null)
this._flags.dispose();this._dataStore=null;for(var p in this._elements)
delete this._elements[p];$IG.ControlMain.callBaseMethod(this,'dispose');},__attachEvents:function()
{this._addHandlers();var handlers=this._handlers;var i=handlers?handlers.length:0;if(i>0)
{var evnts={};while(i-->0)
{var evnt=handlers[i];evnts[evnt]=this._onEventHandler;}
$addHandlers(this.get_element(),evnts,this);}},__clearOtherEvents:function()
{var handlers=this._otherHandlers;var i=handlers?handlers.length:0;while(i-->0)
{for(var evnt in handlers[i])
{var element=handlers[i][evnt];if(element._events&&element._events[evnt]&&element._events[evnt].length>0)
{try
{$removeHandler(element,evnt,this.__otherHandlerDelegate);}
catch(exc){}}}}
this._otherHandlers=null;},__attachOtherEvents:function()
{this._addOtherHandlers();this.__otherHandlerDelegate=Function.createDelegate(this,this._onOtherEventHandler);var handlers=this._otherHandlers;var i=handlers?handlers.length:0;while(i-->0)
for(var evnt in handlers[i])
$addHandler(handlers[i][evnt],evnt,this.__otherHandlerDelegate);},_handleEvent:function(elem,adrElement,adr,e)
{var func=this["_on"+e.type.substring(0,1).toUpperCase()+e.type.substring(1)+"Handler"];if(func)
func.apply(this,[e.target,adr,e]);},__walkThrough:function(elem,topItem)
{$util._initAttr(elem);var adr=elem.getAttribute("adr");var mkr=elem.getAttribute("mkr");var obj=elem.getAttribute("obj");if(adr)
this._createItem(elem,adr);else if(obj)
this._createObject(elem,obj);else if(mkr)
{var mkrAr=mkr.split('.');for(var i=0;i<mkrAr.length;i++)
{mkr=mkrAr[i];if(typeof(this._elements[mkr])!="undefined")
{var mkrElem=this._elements[mkr];if(typeof(mkrElem.length)=="undefined")
mkrElem=this._elements[mkr]=[this._elements[mkr]];mkrElem[mkrElem.length]=elem;}
else
this._elements[mkr]=elem;}}
var ctl=elem.getAttribute("nw");if(ctl)
return;var children=elem.childNodes;for(var i=0;i<children.length;i++)
{var element=children[i];if(element.getAttribute)
this.__walkThrough(element,false);}},__getViewStateEnabled:function()
{var vse=this._get_clientOnlyValue("vse");if(vse==null)
return true;else if(vse==0)
return false;else if(vse==1)
return true;},_onEventHandler:function(e)
{var obj=$util.resolveMarkedElement(e.target,true);if(obj!=null)
{if(obj[2]==this)
this._handleEvent(e.target,obj[0],obj[1],e);}},_onIgSubmit:function()
{var oldT=this._ig_submit_time,newT=(new Date()).getTime();if(oldT&&newT<oldT+99)
return;this._ig_submit_time=newT;this._onSubmitOtherHandler();},_onOtherEventHandler:function(e)
{if(!e)
return;if(e.type=='submit')
{this._onIgSubmit();return;}
if(e.type!=null)
{var func=this["_on"+e.type.substring(0,1).toUpperCase()+e.type.substring(1)+"OtherHandler"];if(func)
func.apply(this,[e.target,e])}},_get_CS:function(){return $get(this._id+'_clientState');},_onSubmitOtherHandler:function(e)
{var clientState=this._get_CS();if(clientState)
{var vse=this.__getViewStateEnabled();var state=[[this._clientStateManager.get_serverProps(vse),this._objectsManager.getServerObjects(vse),this._collectionsManager.getServerCollection(vse)]];state[1]=[this._clientStateManager.get_transactionList(),this._collectionsManager.get_allTransactionLists()];state[2]=this._saveAdditionalClientState();clientState.value=Sys.Serialization.JavaScriptSerializer.serialize(state);}},__bs:['[[[[]],[],[]],[{},[]],"','"]'],_setBackState:function(key,val)
{var cs=this._ig_submit_time?null:this._get_CS();if(!cs)
return;key=key?''+key:'0';if(key.indexOf('|')>=0)
throw Error.invalidOperation('_setBackState: key can not contain | character');key='|'+key+'|';val=''+val;val=val.replace(/\|/g,'&tilda;').replace(/\"/g,'&qout;')+'|';var old=cs.value,len0=this.__bs[0].length;var i=old.indexOf(key),empty=old.length<len0+3;if(empty||i<len0)
{cs.value=this.__bs[0]+key+val+(empty?this.__bs[1]:old.substring(len0+1));return;}
var str=old.substring(i+=key.length);var end=str.indexOf('|');if(end<0)
return;cs.value=old.substring(0,i)+val+str.substring(end+1);},_getBackState:function(key)
{var i=-1,cs=this._get_CS();if(cs)
cs=cs.value;if(!cs||cs.indexOf(this.__bs[0])!=0)
return null;key=key?''+key:'0';cs=cs.replace(this.__bs[0],'').split('|');while((i+=2)+2<cs.length)
if(cs[i]==key)
return cs[i+1].replace(/&tilda;/g,'|').replace(/&qout;/g,'"');return null;},_onBeforeunloadOtherHandler:function(e)
{},_setupMarkerElements:function()
{},_addHandlers:function()
{},_addOtherHandlers:function()
{this._registerOtherHandlers([{"submit":theForm,"beforeunload":window}]);},_createItem:function(element,adr)
{},_createObject:function(element,obj)
{},__responseCompleteInternal:function(callbackObject,responseObject,browserResponseObject)
{var cssClasses=responseObject.context.shift();if(cssClasses)
{var igStyles;if($util.IsIE)
{for(var i=0;i<document.styleSheets.length;i++)
{var ss=document.styleSheets[i];if(ss.id=="igStyles")
{igStyles=ss;break;}}
if(igStyles)
igStyles.cssText+=cssClasses;}
else
{igStyles=document.styleSheets[document.styleSheets.length-1];var rules=cssClasses.split("}");for(var i=0;i<rules.length-1;i++)
{igStyles.insertRule(rules[i]+"}",igStyles.cssRules.length);}}}
this._responseComplete(callbackObject,responseObject,browserResponseObject);},_responseComplete:function(callbackObject,responseObject,browserResponseObject)
{},_responseCompleteError:function(callbackObject,responseObject)
{},_setupCollections:function()
{this._itemCollection=this._collectionsManager.register_collection(0,$IG.ObjectCollection);},_saveAdditionalClientState:function()
{return null;},_set_value:function(index,value)
{this._clientStateManager.set_value(index,value);},_get_value:function(index,isBool)
{return this._clientStateManager.get_value(index,isBool);},_get_clientOnlyValue:function(propName)
{return this._clientStateManager.get_clientOnlyValue(propName);},_get_occasionalProperty:function(propName)
{return this._clientStateManager.get_occasionalProperty(propName);},_set_occasionalProperty:function(propName,val)
{this._clientStateManager.set_occasionalProperty(propName,val);},_cancelEvent:function(e)
{e.stopPropagation();e.preventDefault();},_registerHandlers:function(handlers)
{if(!this._handlers)
this._handlers=[];this._handlers=this._handlers.concat(handlers);},_registerOtherHandlers:function(handlers)
{if(!this._otherHandlers)
this._otherHandlers=[];this._otherHandlers=this._otherHandlers.concat(handlers);},_add_item:function(adr,item)
{this._items[adr]=item;this.__itemCount++;},_remove_item:function(adr)
{if(adr in this._items)
{delete this._items[adr];this.__itemCount--;}},_initClientEvents:function(vals)
{this._initClientEventsForObject(this,vals);},_initClientEventsForObject:function(owner,vals)
{owner._clientEvents=new Object();var i=vals?vals.length:0;while(i-->0)
{var evt=vals[i].split(':');this.setClientEvent(owner,evt[0],evt[1],evt[2]);}},_postAction:function(args,evtName)
{var act=args._props?args._props[1]:args;if(act==1)
{if(this._causeValidation&&typeof WebForm_DoPostBackWithOptions=='function')
{WebForm_DoPostBackWithOptions({validation:true,validationGroup:this._validationGroup});if(!Page_IsValid)
return;}
__doPostBack(this._id,evtName+(args._getPostArgs?args._getPostArgs():''));this._posted=true;}
if(act==2)
{var cb=this._callbackManager.createCallbackObject();cb.serverContext.eventName=evtName;var i=args._props?args._props.length:0;while(--i>1)
eval('cb.serverContext.props'+(i-2)+'="'+args._props[i]+'"');if(args._context)
{for(var contextProp in args._context)
cb.serverContext[contextProp]=args._context[contextProp];}
if(this._filterAsyncPostBack)
this._filterAsyncPostBack(cb.serverContext,evtName,args);this._callbackManager.execute(cb);this._posted=true;}},_raiseClientEventStart:function(param)
{var params=param;if(params.substring)
params=arguments;var post=this.getClientEventPostBack(params[0]);if(!post)
post=params[3];return this._raiseCE_0(this,params[0],post,params[1],params);},_raiseClientEvent:function(param)
{var args=this._raiseClientEventStart(param.substring?arguments:param);return args?this._raiseClientEventEnd(args,args._name):null;},_raiseClientEventEnd:function(args)
{if(args&&args._props&&!(args.get_cancel&&args.get_cancel()))
this._postAction(args,args._name);return args;},_raiseSenderClientEvent:function(sender,clientEvent,eventArgs)
{eventArgs=this._raiseSenderClientEventStart(sender,clientEvent,eventArgs);return this._raiseClientEventEnd(eventArgs);},_raiseSenderClientEventStart:function(sender,clientEvent,eventArgs)
{return this._raiseCE_0(sender,clientEvent.name,clientEvent.postBack,eventArgs);},_raiseCE_0:function(me,evtName,post,args,params)
{var fnc=me.get_events().getHandler(evtName);var str=args&&args.substring;if(!fnc&&post==null)
return str?null:args;if(str)
eval('try{args = new Infragistics.Web.UI.'+args+'EventArgs();}catch(ex){args = null;}');var i=1,len=params?params.length:0;if(!args)
args=(len<3)?new Sys.EventArgs():new $IG.EventArgs();if(args._props)
while(++i<len)if(params[i]!=null)
args._props[i-2]=params[i];if(post)
{if(!args._props)
args._props=new Array();if(!args._props[1]||args._props[1]==0)
args._props[1]=post;}
if(fnc)
fnc(this,args);if(args._props)
delete args._props[0];args._name=evtName;return args;},_getFlags:function()
{if(this._flags==null)
{this.__flagHelper=new $IG.FlagsHelper();var key=[$IG.ObjectBaseProps.Count+0,this.__getDefaultFlags()]
this._flags=new $IG.FlagsObject(this._get_value(key),this);}
return this._flags;},_updateFlags:function(flags)
{var key=[$IG.ObjectBaseProps.Count+0,this.__getDefaultFlags()]
this._set_value(key,flags)},_ensureFlags:function()
{this._ensureFlag($IG.ClientUIFlags.Visible,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Enabled,$IG.DefaultableBoolean.True);},__getDefaultFlags:function()
{if(this.__defaultFlags==null)
{this._ensureFlags();this.__defaultFlags=this.__flagHelper.calculateFlags();}
return this.__defaultFlags;},_ensureFlag:function(flag,val)
{this.__flagHelper.updateFlag(flag,val);},_get_clientStateManager:function(){return this._clientStateManager;},_get_item:function(adr)
{return this._itemCollection._getObjectByAdr(adr);},set_id:function(id)
{this._id=id;},get_name:function(name)
{return this.get_element().name;},set_name:function(value)
{this.get_element().name=value;},get_uniqueID:function()
{return this._uniqueID},addClientEventHandler:function(owner,evtName,fnc)
{$util.addClientEvent(owner,evtName,fnc);},removeClientEventHandler:function(owner,evtName,fnc)
{$util.removeClientEvent(owner,evtName,fnc);},getClientEventPostBack:function(name)
{return this.getClientEventPostBackForObject(this,name);},getClientEventPostBackForObject:function(owner,name)
{var ce=owner._clientEvents[name];return ce?ce.postBack:null;},setClientEvent:function(owner,evtName,fnc,postBack)
{if(postBack)
postBack=parseInt(postBack,10);else
postBack=0;owner._clientEvents[evtName]={name:evtName,fnc:fnc,postBack:postBack};if(evtName&&fnc)
this.addClientEventHandler(owner,evtName,fnc);},get_props:function()
{return this._props;},set_props:function(value)
{this._dataStore=value;this._props=value[0];this._clientStateManager=new $IG.ObjectClientStateManager(this._props);this._objectsManager=new $IG.ObjectsManager(this,value[1]);this._collectionsManager=new $IG.CollectionsManager(this,value[2]);this._initClientEvents(value[3]);}}
$IG.ControlMain.registerClass('Infragistics.Web.UI.ControlMain',Sys.UI.Control);$IG.NavControlProps=new function()
{this.Count=$IG.ControlMainProps.Count+0;};$IG.NavControl=function(elem)
{$IG.NavControl.initializeBase(this,[elem]);}
$IG.NavControl.prototype={initialize:function()
{$IG.NavControl.callBaseMethod(this,'initialize');},_setupCollections:function()
{this._itemCollection=this._collectionsManager.register_collection(0,$IG.NavItemCollection);this._collectionsManager.registerUIBehaviors(this._itemCollection);},resolveItem:function(address)
{return this._itemCollection._getObjectByAdr(address);}}
$IG.NavControl.registerClass('Infragistics.Web.UI.NavControl',$IG.ControlMain);