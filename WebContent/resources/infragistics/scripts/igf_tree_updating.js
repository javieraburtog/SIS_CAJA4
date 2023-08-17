// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.1.20091.1038

$IG.TreeUpdating=function(obj,objProps,control,parentCollection)
{$IG.TreeUpdating.initializeBase(this,[obj,objProps,control,parentCollection]);this._tree=this._owner;this._treeElement=this._tree._element;this._container=control._element;this._editNodeCssClass=this._get_clientOnlyValue("enc");this._containerClickHandler=Function.createDelegate(this,this._onClickHandler);this._containerDoubleClickHandler=Function.createDelegate(this,this._onDblclickHandler);this._treeElementKeyDownHandler=Function.createDelegate(this,this._onKeydownHandler);this._treeElementKeyPress=Function.createDelegate(this,this._onKeypressHandler);this._parentCollection._addElementEventHandler(this._container,"click",this._containerClickHandler);this._parentCollection._addElementEventHandler(this._container,"dblclick",this._containerDoubleClickHandler);this._parentCollection._addElementEventHandler(this._tree._element,"keydown",this._treeElementKeyDownHandler);this._parentCollection._addElementEventHandler(this._tree._element,"keypress",this._treeElementKeyPress);this._defaultEditor=new $IG.TreeInternalEditor(this._container);this._nodeInEditMode=null;this._currentEditor=null;if(this._defaultEditor._originalNotifyLostFocus==null)
{this._defaultEditor._originalNotifyLostFocus=this._defaultEditor.notifyLostFocus;this._defaultEditor.notifyLostFocus=Function.createDelegate(this,this.__editorLostFocus);}}
$IG.TreeUpdating.prototype={get_editModeActions:function()
{return this._editModeActions;},enterEditMode:function(node)
{if(this._nodeInEditMode!=null)
this.exitEditMode(true);setTimeout(this.__createDelegate(this,this.__internalEnterEditMode,[node]),1);},exitEditMode:function(update)
{var node=this._nodeInEditMode,editor=this._currentEditor;if(!node||!editor||!editor.get_value)
return;var args=this.__raiseClientEvent("ExitingEditMode",$IG.CancelEditModeEventArgs,node);if(args==null||!args.get_cancel())
{this._container.style.position="";if(update)
{var val=editor.get_value();node.set_value(val,editor.get_displayText(val));}
editor.hideEditor();this._tree._treeUtil._fireEvent(this,"ExitedEdit",{node:node});this.__raiseClientEvent("ExitedEditMode",$IG.EditModeEventArgs,node);this._tree.set_activeNode(node);this._nodeInEditMode=null;this._currentEditor=null;}},get_isInEditMode:function(node)
{return(this._nodeInEditMode==node);},addExitKeyHandledEventListener:function(handler)
{this._tree._treeUtil._registerEventListener(this,"ExitKey",handler);},_addEnteringEditEventListener:function(handler)
{this._tree._treeUtil._registerEventListener(this,"EnteringEdit",handler);},_addExitedEditEventListener:function(handler)
{this._tree._treeUtil._registerEventListener(this,"ExitedEdit",handler);},_onClickHandler:function(evnt)
{if(this._currentEditor!=null&&evnt.target==this._currentEditor._element)
return;if(this._editModeActions.get_mouseClick()==$IG.EditMouseClickAction.Single)
this.__enterEditModeEvntHandler(evnt);},_onDblclickHandler:function(evnt)
{if(this._currentEditor!=null&&evnt.target==this._currentEditor._element)
return;if(this._editModeActions.get_mouseClick()==$IG.EditMouseClickAction.Double)
this.__enterEditModeEvntHandler(evnt);},_onKeypressHandler:function(evnt)
{if(this._editModeActions.get_enableOnKeyPress())
{if(this._nodeInEditMode==null)
{var key=evnt.charCode;if(key==Sys.UI.Key.esc||key==Sys.UI.Key.left||key==Sys.UI.Key.tab||key==Sys.UI.Key.up||key==Sys.UI.Key.right||key==Sys.UI.Key.down)
return;var activeNode=this._tree.get_activeNode();if(activeNode!=null)
{this.enterEditMode(activeNode);}}}},_onKeydownHandler:function(evnt)
{var key=evnt.keyCode;if(key==113)
{if(this._editModeActions.get_enableF2())
{var activeNode=this._tree.get_activeNode();if(activeNode!=null)
{this.enterEditMode(activeNode);}}}},_activeCellChanged:function(args)
{if(this._editModeActions.get_enableOnActive())
this.enterEditMode(args.node);},__internalEnterEditMode:function(node)
{if(this._nodeInEditMode!=null)
return;var editor=this.__resolveEditor(node);if(editor==null)
return;var customDisplay={cancel:null,top:null,left:null,height:null,width:null,text:null};this._tree._treeUtil._fireEvent(this,"EnteringEdit",{node:node,customDisplay:customDisplay});if(customDisplay.cancel)
return;var args=this.__raiseClientEvent("EnteringEditMode",$IG.CancelEditModeEventArgs,node);if(args==null||!args.get_cancel())
{var container=node._container;if(!container)
container=this._container;container.style.position="relative";this._nodeInEditMode=node;this._currentEditor=editor;var offsetImg=0;var elm=node.get_textElement();if(elm.firstChild.tagName=="IMG"){var elmImg=elm.firstChild;var offsetImg=elmImg.offsetWidth;if(Sys.Browser.agent===Sys.Browser.Firefox){if(offsetImg>0)
offsetImg+=new IgDomNode(elmImg).getMargin("width");}}
var top=elm.offsetTop;if(customDisplay.top!=null)
top=customDisplay.top;var left=elm.offsetLeft+offsetImg;if(customDisplay.left!=null)
left=customDisplay.left;var height=elm.offsetHeight;if(customDisplay.height!=null)
height=customDisplay.height;var width=elm.offsetWidth-offsetImg;if(customDisplay.width!=null)
width=customDisplay.width;editor.set_value(node.get_value());if(customDisplay.text!=null)
editor.set_value(customDisplay.text);editor.showEditor(top,left,width,height,this._editCellCssClass,container);this.__raiseClientEvent("EnteredEditMode",$IG.EditModeEventArgs,node);}},__createDelegate:function(instance,method,args)
{return function(){return method.apply(instance,args);}},__enterEditModeEvntHandler:function(evnt)
{if(evnt.button==0)
{var node=this._tree._treeUtil.getCellFromElem(evnt.target);if(node!=null)
this.enterEditMode(node);}},__editorLostFocus:function(evnt)
{if(this._currentEditor!=null)
{this._currentEditor._originalNotifyLostFocus(evnt);if(evnt!=null&&evnt.type=="keydown")
{var key=evnt.keyCode;var util=this._tree._treeUtil;var node=this._nodeInEditMode;if(key==Sys.UI.Key.tab)
{var nextCell=null;if(!evnt.shiftKey)
nextCell=util.getNextCell(node);else
nextCell=util.getPrevCell(node);this.exitEditMode(true);util._fireEvent(this,"ExitKey",{Cell:node,Evnt:evnt});if(nextCell!=null)
{if(this._activation)
this._activation.set_activeCell(nextCell,true);$util.cancelEvent(evnt);}}
else
{if(key==Sys.UI.Key.esc)
{this.exitEditMode(false);util._fireEvent(this,"ExitKey",{Cell:node,Evnt:evnt});}
else if(key==Sys.UI.Key.enter)
{this.exitEditMode(true);util._fireEvent(this,"ExitKey",{Cell:node,Evnt:evnt});$util.cancelEvent(evnt);}
if(this._activation)
{var activeCell=this._activation.get_activeCell();if(activeCell)
activeCell.get_element().focus();}}}
else
this.exitEditMode(true);}},__resolveEditor:function(node)
{return this._defaultEditor;},_registerEditableRow:function(rowElement)
{this._parentCollection._addElementEventHandler(rowElement,"click",this._containerClickHandler);this._parentCollection._addElementEventHandler(rowElement,"dblclick",this._containerDoubleClickHandler);this._parentCollection._addElementEventHandler(rowElement,"keydown",this._treeElementKeyDownHandler);this._parentCollection._addElementEventHandler(rowElement,"keypress",this._treeElementKeyPress);},_addExitKeyHandledEventListener:function(handler)
{this._tree._treeUtil._registerEventListener(this,"ExitKey",handler);},_createObjects:function(objectManager)
{$IG.TreeUpdating.callBaseMethod(this,"_createObjects",[objectManager]);this._editModeActions=new $IG.EditModeActions("EditModeActions",null,objectManager.get_objectProps(0),this);objectManager.register_object(0,this._editModeActions);},_createCollections:function(collectionsManager)
{},_initializeComplete:function()
{this._activation=this._tree.get_behaviors().getBehaviorFromInterface($IG.IActivationBehavior);if(this._activation)
this._activation._addActiveCellChangedEventHandler(Function.createDelegate(this,this._activeCellChanged));},dispose:function()
{this._parentCollection._removeElementEventHandler(this._container,"click",this._containerClickHandler);this._parentCollection._removeElementEventHandler(this._container,"dblclick",this._containerDoubleClickHandler);this._parentCollection._removeElementEventHandler(this._tree._element,"keydown",this._treeElementKeyDownHandler);this._parentCollection._removeElementEventHandler(this._tree._element,"keypress",this._treeElementKeyPress);}}
$IG.TreeUpdating.registerClass('Infragistics.Web.UI.TreeUpdating',$IG.TreeBehavior,$IG.IUpdatingBehavior);$IG.TreeEditingProps=new function()
{this.Count=$IG.TreeBehaviorProps.Count;};$IG.TreeInternalEditor=function(parentElement)
{var input=document.createElement('INPUT');input.style.position='absolute';parentElement.appendChild(input);$IG.TreeInternalEditor.initializeBase(this,[input]);this.hideEditor();}
$IG.TreeInternalEditor.prototype={get_value:function()
{return this._element.value;},set_value:function(val)
{this._element.value=val;},showEditor:function(top,left,width,height,cssClass,parent)
{var input=this._element;if(parent&&input.parentNode!=parent)
{input.parentNode.removeChild(input);parent.appendChild(input);}
style=input.style;style.top=top+'px';style.left=left+'px';style.display='';style.visibility='visible';if(cssClass)
input.className=cssClass;style.width=width+'px';var diff=input.offsetWidth-width;style.width=(width-diff)+'px';style.height=height+'px';diff=input.offsetHeight-height;style.height=(height-diff)+'px';try
{input.select();input.focus();}catch(e){}
if(!this._hasLsnr)
{if(!this._onBlurFn)
{this._onBlurFn=Function.createDelegate(this,this._onBlurHandler);this._onKeyFn=Function.createDelegate(this,this._onKeyDownHandler);}
this._hasLsnr=true;$addHandler(input,'blur',this._onBlurFn);$addHandler(input,'keydown',this._onKeyFn);}},notifyLostFocus:function(evnt)
{},get_displayText:function(val)
{return this._element.value;},hideEditor:function()
{var input=this._element;input.style.display='none';input.style.visibility='hidden';if(this._hasLsnr)
{$removeHandler(input,'blur',this._onBlurFn);$removeHandler(input,'keydown',this._onKeyFn);}
this._hasLsnr=false;},_onKeyDownHandler:function(evnt)
{var key=evnt.keyCode;if(key==Sys.UI.Key.tab||key==Sys.UI.Key.esc||key==Sys.UI.Key.enter)
this.notifyLostFocus(evnt);},_onBlurHandler:function(evnt)
{this.notifyLostFocus(evnt);}}
$IG.TreeInternalEditor.registerClass('Infragistics.Web.UI.TreeInternalEditor',Sys.UI.Control);