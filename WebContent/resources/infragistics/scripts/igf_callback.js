// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");$IG.CallbackRequestHandler=function(manager,callbackObject,async)
{var me=this;this._callbackObject=callbackObject;this._manager=manager;this._async=async;this._responseComplete=function()
{if(me._request.readyState===4&&me._request.status=="200")
{window.clearTimeout(me._timerId);var response=me._request.responseText;if(response!=null&&response.length>0)
{var responseObject=[];responseObject.context=new Array();var xmlDoc=me._request.responseXML;if(!ig.isNull(xmlDoc)){var results=xmlDoc.documentElement;var components=results.getElementsByTagName("component");var component=components.item(0);var content=component.getElementsByTagName("content").item(0);var contentAsHtml=(!ig.isNull(content.firstChild))?content.firstChild.nodeValue:"";var parsedContent=ig.parseHtml(contentAsHtml,false);parsedContent.style.display='none';var elements=parsedContent.getElementsByTagName("div");if(elements.length>0){var nodeCount=elements.length;for(var i=0;i<nodeCount;i++)
{if(elements[i].id=="JSON")
responseObject.context[i]=Sys.Serialization.JavaScriptSerializer.deserialize(elements[i].innerHTML);else
responseObject.context[i]=elements[i].innerHTML;}}
var clientState=results.getElementsByTagName("state").item(0);if(!ig.isNull(clientState)){var clientStateContent=clientState.getElementsByTagName("content").item(0);if(!ig.isNull(clientStateContent)){var clientStateAsHtml=(!ig.isNull(clientStateContent.firstChild))?clientStateContent.firstChild.nodeValue:"";var parsedClientState=ig.parseHtml(clientStateAsHtml,false);if(!ig.isNull(parsedClientState)){var nodes=parsedClientState.childNodes;for(var k=nodes.length-1;k>=0;k--){var aNode=nodes.item(k);if(aNode.nodeName=="INPUT"){var id=aNode.id;var currentNode=null;if(ig.NaES(id)){currentNode=document.getElementById(id);}
else{var name=aNode.name;var elements=document.getElementsByName(name);if(elements.length==1){currentNode=elements[0];}}
if(!ig.isNull(currentNode)){ig.replaceNode(currentNode,aNode);}}}
ig.deleteNode(parsedClientState);}}}}
me._manager._requestCompleted(me,me._callbackObject,responseObject);}
else
{me._timedOut();}
me._callbackObject=null;me._manager=null;me._request=null;}
else if(me._request.readyState===4)
me._manager._requestFailed(me,me._callbackObject);}}
$IG.CallbackRequestHandler.prototype={execute:function()
{this._request=null;if(typeof XMLHttpRequest!="undefined")
this._request=new XMLHttpRequest();else if(typeof ActiveXObject!="undefined")
{try{this._request=ig_createActiveXFromProgIDs(["MSXML2.XMLHTTP","Microsoft.XMLHTTP"]);}catch(e){}}
if(this._request)
{this._request.open(this._manager.getHttpVerb(),this._manager.getUrl(),this._async);this._request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");this._request.onreadystatechange=this._responseComplete;this._timerId=window.setTimeout(Function.createDelegate(this,this._timedOut),this._manager.getTimeout());this._request.send(this._getArgs());}},_getArgs:function()
{var form=this._manager._getForm();if(!form)return;if(typeof ig_controls=='object')
for(var id in ig_controls)
ig_controls[id]._onIgSubmit();var count=form.elements.length;var element;for(var i=0;i<count;i++)
{element=form.elements[i];if(element.tagName.toLowerCase()=="input"&&(element.type=="hidden"||element.type=='password'||element.type=='text'||((element.type=="checkbox"||element.type=='radio')&&element.checked)))
this._addCallbackField(element.name,element.value);else if(element.tagName.toLowerCase()=="textarea")
this._addCallbackField(element.name,element.value);else if(element.tagName.toLowerCase()=="select")
{var o=element.options.length;while(o-->0)
{if(element.options[o].selected)
this._addCallbackField(element.name,element.options[o].value);}}}
var args=this._postdata+"__EVENTTARGET=&__EVENTARGUMENT=&__IGCallback_"+this._manager._control._id+"=";args+=Sys.Serialization.JavaScriptSerializer.serialize(this._callbackObject._getServerData());args+="&com.infragistics.faces.SMART_REFRESH_COMPONENTS="+this._manager._control._id;args+="&com.sun.faces.FORM_CLIENT_ID_ATTR="+form.id;return args;},_addCallbackField:function(name,value)
{if(!this._postdata)
this._postdata="";this._postdata+=name+"="+this._encodeValue(value)+"&";},_encodeValue:function(uri)
{if(encodeURIComponent!=null)
return encodeURIComponent(uri);else
return escape(parameter);},_timedOut:function()
{this._manager._requestFailed(this,this._callbackObject,true);}};$IG.CallbackRequestHandler.registerClass("Infragistics.Web.UI.CallbackRequestHandler");$IG.ControlCallbackManager=function(control)
{this._control=control;this._httpVerb="POST"
this._async=true;this._timeout=20000;this._url=this._getForm().action;this._currentRequests=0;this._callbackQueue=[];}
$IG.ControlCallbackManager.prototype={createCallbackObject:function(control)
{if(!control)
control=this._control;return new $IG.CallbackObject(control);},execute:function(callback,queue,async)
{if(callback)
{if(async==null)
async=this.getAsync();var requestHandler=new $IG.CallbackRequestHandler(this,callback,async);if(queue&&this._currentRequests>0)
this._pushCallback(requestHandler)
else
{this._currentRequests++;requestHandler.execute();}}},_pushCallback:function(callback)
{this._callbackQueue.push(callback);},_popCallback:function()
{for(var i=0;i<this._callbackQueue.length;i++)
{var requestHandler=this._callbackQueue[i];if(requestHandler!=null)
{delete this._callbackQueue[i];this._currentRequests++;requestHandler.execute();}}},getAsync:function()
{return this._async;},setAsync:function(val){return this._async;},getHttpVerb:function()
{return this._httpVerb;},setHttpVerb:function(verb){this._httpVerb=verb;},getUrl:function()
{return this._url;},setUrl:function(url){this._url=url;},getTimeout:function()
{return this._timeout;},setTimeout:function(val){this._timeout=val;},_getForm:function()
{if(!this._form)
{if(document.forms.length>1)
{for(var i=0;i<document.forms.length;i++)
{if(document.forms[i].method=="post"&&document.forms[i].action!="")
{this._form=document.forms[i];break;}}
if(!this._form)
this._form=document.forms[0];}
else
this._form=document.forms[0];if(!this._form)
this._form=document.form1;}
return this._form},_endRequest:function()
{this._currentRequests--;if(this._callbackQueue.length>0)
this._popCallback();},setResponseComplete:function(func,context)
{this._responseCompleteFunction=func;if(!context)
context=this._control;this._responseCompleteContext=context;},_requestFailed:function(requestHandler,callbackObject,timedOut)
{window.clearTimeout(requestHandler._timerId);if(requestHandler._request.readyState==4)
callbackObject._responseCompleteError(requestHandler._request,timedOut);this._endRequest();requestHandler._request.abort();requestHandler._request=null;},_requestCompleted:function(requestHandler,callbackObject,responseObject)
{this._endRequest();this._recursiveResponseCompleted(callbackObject,responseObject,requestHandler._request);},_recursiveResponseCompleted:function(callbackObject,responseObject,browserResponseObject)
{this._responseComplete(callbackObject,responseObject,browserResponseObject);for(var i=0;i<callbackObject._childCallbacks.length;i++)
this._recursiveResponseCompleted(callbackObject._childCallbacks[i],responseObject.children[i],browserResponseObject);},_responseComplete:function(callbackObject,responseObject,browserResponseObject)
{if(!callbackObject._responseComplete(responseObject,browserResponseObject))
{if(this._responseCompleteFunction)
this._responseCompleteFunction.apply(this._responseCompleteContext,[callbackObject,responseObject,browserResponseObject]);}
callbackObject.dispose();},dispose:function()
{this._control=null;this._form=null;this._responseCompleteContext=null;}};$IG.ControlCallbackManager.registerClass("Infragistics.Web.UI.ControlCallbackManager");$IG.CallbackObject=function(control)
{this._control=control;this.serverContext={};this.clientContext={};this._childCallbacks=[];}
$IG.CallbackObject.prototype={createCallbackObject:function(control)
{if(!control)
control=this._control;var callbackObject=new $IG.CallbackObject(control);this._childCallbacks.push(callbackObject);return callbackObject;},getId:function()
{return this._control._id;},getServerContext:function()
{return this.serverContext;},getClientContext:function()
{return this.clientContext;},setResponseComplete:function(func,context,funcError)
{this._responseCompleteFunction=func;this._responseCompleteErrorFunction=funcError;if(!context)
context=this._control;this._responseCompleteContext=context;},_responseComplete:function(responseObj,browserResponseObject)
{if(this._responseCompleteFunction)
{this._responseCompleteFunction.apply(this._responseCompleteContext,[this,responseObj,browserResponseObject]);return true;}
return false;},_responseCompleteError:function(responseObj,timedOut)
{if(this._responseCompleteErrorFunction)
{this._responseCompleteErrorFunction.apply(this._responseCompleteContext,[this,responseObj,timedOut]);return true;}
else if(typeof(this._control._responseCompleteError)!="undefined")
{this._control._responseCompleteError(this,responseObj,timedOut);return true;}
return false;},_getServerData:function()
{var data={id:this._control.get_uniqueID(),context:this.serverContext,children:[]};for(var i=0;i<this._childCallbacks.length;i++)
data.children[i]=this._childCallbacks[i]._getServerData();return data;},dispose:function()
{this._control=null;this.serverContext=null;this.clientContext=null;}};$IG.CallbackObject.registerClass("Infragistics.Web.UI.CallbackObject");