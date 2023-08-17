// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");var $IG=Infragistics.Web.UI;$IG.DragDropEffects=function()
{}
$IG.DragDropEffects.prototype={None:0,Move:1,Copy:2,Default:3};$IG.DragDropEffects.registerEnum("Infragistics.Web.UI.DragDropEffects");$IG.DragDropAction=function()
{}
$IG.DragDropAction.prototype={None:0,Append:1,Insert:2};$IG.DragDropAction.registerEnum("Infragistics.Web.UI.DragDropAction");$IG._DragDropManager=function()
{this._dragging=false;this._currentTarget=null;this._currentSource=null;this._zIndex=99999;this._ctrl=false;this._shift=false;this._alt=false;this._targetElements=[];this._dragDropEffect=$IG.DragDropEffects.Default;this._supportsElemFromPoint=document.elementFromPoint!=null;this._mouseUpDelegate=Function.createDelegate(this,this._onMouseUp);this._mouseOutDelegate=Function.createDelegate(this,this._onMouseOut);this._mouseMoveShellDelegate=Function.createDelegate(this,this._onMouseMoveShell);if(window.navigator.userAgent.indexOf("MSIE 8.0")>=0)
this._mouseMoveEFPDelegate=Function.createDelegate(this,this._onMouseMoveElemFromPoint);this._mouseMoveDelegate=Function.createDelegate(this,this._onMouseMove);this._selectDelegate=Function.createDelegate(this,this._onSelectStart);this._keyDelegate=Function.createDelegate(this,this._onKey);}
$IG._DragDropManager.prototype={get_isDragging:function()
{return this._dragging;},isCtrlKey:function()
{return this._ctrl;},isShiftKey:function()
{return this._shift;},isAltKey:function()
{return this._alt;},get_source:function()
{return this._currentSource;},get_sourceElement:function()
{if(this._sourceBehavior)
return this._sourceBehavior.__startElem;return null;},get_target:function(behavior)
{if(this._currentTarget)
{if(behavior)
{var targets=this._currentTarget.target.targets;for(var i=0;i<targets.length;i++)
{var target=targets[i];if(target.behavior==behavior)
return target;}}
else
{return this._currentTarget.target.targets[0];}}
return null;},get_targetElement:function()
{if(this._currentTarget)
return this._currentTarget.elemAtPoint;return null;},get_dragDropEffect:function()
{if(this._sourceBehavior&&this._sourceBehavior._dragDropMode!=$IG.DragDropEffects.Default)
return this._sourceBehavior._dragDropMode;return this._dragDropEffect;},_set_dragDropEffect:function(effect)
{this._dragDropEffect=effect;this._updateCursor(effect);},get_dataObject:function()
{return this._dataObject;},set_dataObject:function(data)
{this._dataObject=data;},get_dataText:function()
{return this._dataText;},set_dataText:function(text)
{this._dataText=text;},get_dragMarkupElement:function()
{return this._draggingMarkup;},get_defaultActionDropElement:function()
{return this._defDropElem;},endDrag:function(cancel)
{var beh=this._sourceBehavior;if(!this._dragging||!beh)
return;if(this._currentTarget&&!cancel)
{if(this.__fireTargetEvent("Drop"))
this._defaultDrop(beh);}
else
beh._events._fireEvent("DragCancel");beh._events._fireEvent("DragEnd");this._dragging=this._shift=this._alt=this._ctrl=false;if(this._draggingMarkup)
document.body.removeChild(this._draggingMarkup);this.__setCursor('',true);if(this._dragShell)
this._dragShell.style.display="none";this._dragDropEffect=$IG.DragDropEffects.Default;this._dataObject=this._dataText=this._draggingMarkup=this._dragShell=this._copyTarget=this._currentTarget=this._currentSource=this._sourceBehavior=this._defDropElem=null;if(this._dragTime>0)
{$removeHandler(document,'selectstart',this._selectDelegate);$removeHandler(document,'keydown',this._keyDelegate);$removeHandler(document,'keyup',this._keyDelegate);$removeHandler(document,'mouseout',this._mouseOutDelegate);$removeHandler(document,'mouseup',this._mouseUpDelegate);if(!this._supportsElemFromPoint)
{this.__handleWindowedElems(false);$removeHandler(document,'mousemove',this._mouseMoveDelegate);}
else if(this._docMove)
{$removeHandler(document,'mousemove',this._mouseMoveShellDelegate);$removeHandler(document,'scroll',this._mouseUpDelegate);}}
this._dragTime=0;},elementFromPoint:function(x,y)
{if(!this._supportsElemFromPoint)
return null;var efp=document.elementFromPoint(x,y);if(efp==null)
efp=this._elementFromPoint;return efp;},dispose:function()
{this.endDrag(true);var shell=this._dragShell;if(!shell)
return;if(this._shellLsnr)
{var doc=shell.contentWindow.document;$util.removeHandler(doc,'mousemove',this._mouseMoveShellDelegate);if($util.IsIE8)
$util.removeHandler(doc,'mousemove',this._mouseMoveEFPDelegate);$util.removeHandler(doc,'mouseup',this._mouseUpDelegate);}
if(this._shellLoadDelegate)
$removeHandler(shell,'load',this._shellLoadDelegate);document.body.removeChild(shell);this._dragShell=null;},_fixCtrl:function(e,cursor)
{if(!e)
return;this._shift=e.shiftKey;this._alt=e.altKey;var ctrl=e.ctrlKey;if(ctrl==this._ctrl)
return;this._ctrl=ctrl;if(cursor)
this._validateCursor(e);},_onSelectStart:function(e)
{$util.cancelEvent(e);},_onMouseOut:function(e)
{if((new Date()).getTime()<this._dragTime+200)
return;var elem=this._supportsElemFromPoint?null:e.target;if(!elem||elem.nodeName=='HTML')
this.endDrag();},_onMouseUp:function(e)
{this._fixCtrl(e);this.endDrag();},_onKey:function(e)
{if(e.keyCode==Sys.UI.Key.esc)
this.endDrag(true);this._fixCtrl(e,1);},_onMouseMoveShell:function(e)
{var elem=this._draggingMarkup,scroll=($util.IsMac&&$util.IsSafari)?document.body:document.documentElement;if(!elem||!this._dragging)
return;elem.style.top=(elem._originalTop-(elem._originalY-e.clientY))+'px';elem.style.left=(elem._originalLeft-(elem._originalX-e.clientX))+'px';var x=scroll.scrollLeft,y=scroll.scrollTop;if(this._docMove)
{x=-x;y=-y;}
var targetObj=this._getTargetFromPoint(e.clientX-x,e.clientY-y);this._validateTarget(targetObj,e);},_elementFromPoint:null,_deferredEFPDelegate:null,_efpX:-1,_efpY:-1,_onMouseMoveElemFromPoint:function(e)
{if(!this._deferredEFPDelegate)
this._deferredEFPDelegate=Function.createDelegate(this,this._checkElementFromPoint);this._efpX=e.clientX;this._efpY=e.clientY;if(this._draggingMarkup)
this._draggingMarkup.style.display="none";if(this._dragShell)
this._dragShell.style.display="none";window.setTimeout(this._deferredEFPDelegate);},_checkElementFromPoint:function()
{this._elementFromPoint=document.elementFromPoint(this._efpX,this._efpY);if(this._draggingMarkup)
this._draggingMarkup.style.display="";if(this._dragShell)
this._dragShell.style.display="";},_onMouseMove:function(e)
{var elem=e.target;if(!elem||!this._dragging)
return;if(this._currentElement!=elem)
{this._previousElement=this._currentElement;this._currentElement=elem;}
else
this._previousElem=null;var targetObj=this._validateElem(elem);elem=this._draggingMarkup;if(!elem)
return;elem.style.top=(e.clientY+document.documentElement.scrollTop+1)+'px';elem.style.left=(e.clientX+document.documentElement.scrollLeft+1)+'px';this._validateTarget(targetObj,e);},_onLoadFrame:function()
{var win=this._dragShell;if(!win||this._cursorElem)
return;win=win.contentWindow;var doc=win.document;var body=doc.body,div=doc.createElement('DIV');var style=div.style;body.appendChild(div);body.leftMargin=body.rightMargin=body.topMargin=body.bottomMargin='0';body.style.height=style.height=style.width='100%';style.position='absolute';style.top=style.left='0px';this._cursorElem=div;if(!this._docMove)
{this._shellLsnr=true;$util.addHandler(doc,'mousemove',this._mouseMoveShellDelegate);if($util.IsIE8)
$util.addHandler(doc,'mousemove',this._mouseMoveEFPDelegate);$util.addHandler(doc,'mouseup',this._mouseUpDelegate);}},_startDrag:function(dragBehavior,source,e)
{this.endDrag();if(!dragBehavior)
return;var x=e.clientX,y=e.clientY;this._currentSource=source;this._sourceBehavior=dragBehavior;if(dragBehavior._events._fireEvent("DragStart",{x:x,y:y}))
{this._dragging=true;this._docMove=$util.IsOpera||$util.IsMac;if(this._supportsElemFromPoint)
this._showShell();this.__setupVisibleDragMakup(x,y,dragBehavior._underMouseElem?dragBehavior.__startElem:source.element);this._dragTime=(new Date()).getTime();$addHandler(document,'selectstart',this._selectDelegate);$addHandler(document,'keydown',this._keyDelegate);$addHandler(document,'keyup',this._keyDelegate);$addHandler(document,'mouseout',this._mouseOutDelegate);$addHandler(document,'mouseup',this._mouseUpDelegate);if(!this._supportsElemFromPoint)
{this.__handleWindowedElems(true);$addHandler(document,'mousemove',this._mouseMoveDelegate);}
else if(this._docMove)
{$addHandler(document,'mousemove',this._mouseMoveShellDelegate);$addHandler(document,'scroll',this._mouseUpDelegate);}
this._fixCtrl(e);}
else
this.endDrag();},_validateTarget:function(targetObj,e)
{if(targetObj)
{var targets=targetObj.target.targets;var fire=false;for(var i=0;i<targets.length;i++)
{var target=targets[i];var elem=target._includeChildren?targetObj.elemAtPoint:targetObj.element;var copy=this._copyTarget;if(!copy)
{fire=true;break;}
var iTargets=copy.target.targets;for(var j=0;j<iTargets.length;j++)
{var jElem=iTargets[j]._includeChildren?copy.elemAtPoint:copy.element;if(elem!=jElem)
{this.__fireTargetEvent("DragLeave");fire=true;}}}
if(fire)
{this._copyTarget=this._currentTarget=targetObj;if(!this.__fireTargetEvent("DragEnter"))
{this._set_dragDropEffect($IG.DragDropEffects.None);this._currentTarget=null;return;}}
this.__fireTargetEvent("DragMove",{x:e.clientX,y:e.clientY});this._validateCursor(e);}
else
{if(this._currentTarget)
this.__fireTargetEvent("DragLeave");this._set_dragDropEffect($IG.DragDropEffects.None);this._copyTarget=this._currentTarget=null;}},_validateCursor:function(e)
{var src=this._sourceBehavior;if(!src)
return;var mode=src._dragDropMode;if(mode==$IG.DragDropEffects.Default)
this._set_dragDropEffect(e.ctrlKey?$IG.DragDropEffects.Copy:$IG.DragDropEffects.Move);else
this._updateCursor(mode)},_updateCursor:function(effect)
{if(!this._dragging)
return;if(effect==$IG.DragDropEffects.None)
this.__setCursor(this._sourceBehavior._noneCursor);else if(effect==$IG.DragDropEffects.Move)
this.__setCursor(this._sourceBehavior._moveCursor);else if(effect==$IG.DragDropEffects.Copy)
this.__setCursor(this._sourceBehavior._copyCursor);},_getTargetFromPoint:function(x,y)
{this._draggingMarkup.style.display=this._dragShell.style.display="none";var elemAtPoint=this.elementFromPoint(x,y);this._draggingMarkup.style.display=this._dragShell.style.display="";return this._validateElem(elemAtPoint);},_validateElem:function(elemAtPoint)
{if(!elemAtPoint)
return null;var targets=this._targetElements;var i=targets?targets.length:0;while(i-->0)
{var target=targets[i];var elem=target.element;if(elem!=null&&(elem==elemAtPoint||((elem.contains&&elem.contains(elemAtPoint))||(!elem.contains&&$util.isChild(elem,elemAtPoint)))))
return{target:target,element:elem,elemAtPoint:elemAtPoint};}
return null;},_registerTarget:function(behavior,target)
{var index=-1;var targets=this._targetElements;for(var i=0;i<targets.length;i++)
{if(targets[i].element==target.element)
{index=i;break;}}
if(index==-1)
{index=targets.length;targets.push({element:target.element,targets:[]});}
target.behavior=behavior;targets[index].targets.push(target);},_unRegisterTarget:function(behavior,target)
{var targets=this._targetElements;var i=targets?targets.length:0;while(i-->0)
{var iTarget=targets[i];if(iTarget.element==target.element)
{var count=iTarget.targets.length;for(var j=0;j<count;j++)
{if(iTarget.targets[j]==target)
{Array.removeAt(iTarget.targets,j);break;}}
if(count==1)
Array.removeAt(targets,i);break;}}},_defaultDrop:function(beh)
{var act=beh.get_defaultDropAction();var effect=this.get_dragDropEffect();if(act<=0||effect<=0)
return;var src=this.get_source().element;var target=this.get_target().element;if(!target||!src)
return;var elem=src,parent=src.parentNode;var move=effect==1;if(!move)
{var div=document.createElement('DIV');div.appendChild(elem.cloneNode(true));div.innerHTML=div.innerHTML;elem=div.firstChild;}
else if(parent==elem)
return;this._defDropElem=elem;if(move)
parent.removeChild(src);if((act&1)==0)
target.insertBefore(elem,target.firstChild);else
target.appendChild(elem);},__setCursor:function(cursor,clear)
{if(this._cursorElem)
{this._cursorElem.style.cursor=cursor;return;}
var curElem=this._currentElement,prevElem=this._previousElement;if(prevElem)
{if(prevElem.__originalCursor!=null)
{prevElem.style.cursor=prevElem.__originalCursor;prevElem.__originalCursor=null;}}
if(curElem)
{if(clear)
{if(curElem.__originalCursor!=null)
{document.body.style.cursor=document.body.__originalCursor;curElem.style.cursor=curElem.__originalCursor;document.body.__originalCursor=curElem.style.cursor=null;}}
else
{if(document.body.__originalCursor==null)
document.body.__originalCursor=document.body.style.cursor;if(curElem.__originalCursor==null)
curElem.__originalCursor=curElem.style.cursor;document.body.style.cursor=curElem.style.cursor=cursor;}}},__handleWindowedElems:function(hide)
{var tags=['IFRAME','OBJECT'];for(var j=0;j<2;j++)
{var elems=document.getElementsByTagName(tags[j]);for(var i=0;i<elems.length;i++)
{var elem=elems[i];if(hide)
{var height=elem?elem.offsetHeight:0,width=elem?elem.offsetWidth:0;if(!height||!width||width==0)
continue;var div=elem.__dragDropDiv=document.createElement('DIV');var style=div.style;style.height=(height+4)+'px';style.width=(width+4)+'px';var pos=Sys.UI.DomElement.getLocation(elem);style.top=(pos.y-2)+'px';style.left=(pos.x-2)+'px';style.position='absolute';style.background='white';style.zIndex=this._zIndex;document.body.appendChild(div);$util.setOpacity(div,1);continue;}
if(!elem||!elem.__dragDropDiv)
continue;document.body.removeChild(elem.__dragDropDiv);elem.__dragDropDiv=null;}}},__fireTargetEvent:function(evntName,props)
{var returnVal=false,targets=this._currentTarget;if(!targets)
return false;targets=targets.target.targets;var i=targets?targets.length:0;while(i-->0)
{var target=targets[i];var behavior=target.behavior;var dropChannels=behavior._dropChannels;if(dropChannels==null||behavior._dropChannels.length==0)
{returnVal=true;if(!behavior._events._fireEvent(evntName,props))
return false;}
else
{var dragChannels=this._sourceBehavior._dragChannels;var j=dragChannels?dragChannels.length:0;while(j-->0)
{if(Array.contains(dropChannels,dragChannels[j]))
{returnVal=true;if(!behavior._events._fireEvent(evntName,props))
return false;}}}}
return returnVal;},__setupVisibleDragMakup:function(x,y,elem)
{var style,markup=document.createElement("DIV");var dragMarkup=this._sourceBehavior.get_dragMarkup();if(dragMarkup)
markup.appendChild(dragMarkup);else
{var clone=elem.cloneNode(true);style=clone.style;style.position='static';style.left=style.top='0px';style.display='';style.visibility='visible';style.height=elem.offsetHeight+'px';style.width=elem.offsetWidth+'px';markup.appendChild(clone);}
style=markup.style;style.position="absolute";style.zIndex=this._zIndex;var updateDisplay=elem.style.display=="none";if(updateDisplay)
elem.style.display="";var scrollElem=$util.IsSafari?document.body:document.documentElement;var pos=Sys.UI.DomElement.getLocation(elem);if(updateDisplay)
elem.style.display="none";markup._originalX=x+(this._docMove?0:scrollElem.scrollLeft);markup._originalY=y+(this._docMove?0:scrollElem.scrollTop);style.top=(markup._originalTop=pos.y)+"px";style.left=(markup._originalLeft=pos.x)+"px";document.body.appendChild(markup);$util.setOpacity(markup,this._sourceBehavior.get_dragMarkupOpacity());this._draggingMarkup=markup;},_showShell:function()
{var style,id='_ig_DragDropShellFrame';var shell=this._dragShell;if(!shell)
this._dragShell=shell=$get(id);if(!shell)
{this._dragShell=shell=document.createElement("IFRAME");shell.src='javascript:new String("<html></html>")';shell.frameBorder=0;shell.scrolling='no';shell.allowtransparency='true';shell.id=id;style=shell.style;style.zIndex=this._zIndex+1;style.position='absolute';style.left=style.top='0px';document.body.appendChild(shell);$util.setOpacity(shell,0);if($util.IsSafari)
window.setTimeout(Function.createDelegate(this,this._onLoadFrame),5);else
$addHandler(shell,'load',this._shellLoadDelegate=Function.createDelegate(this,this._onLoadFrame));}
style=shell.style;var rect=$util.getWinRect();if(!$util.IsIE)
{var elem=$util.IsSafari?document.body:document.documentElement;var sb=$util.IsSafari?16:18;if(rect.height<elem.scrollHeight)
rect.maxWidth-=sb;if(rect.width<elem.scrollWidth)
rect.maxHeight-=sb;}
style.height=(rect.maxHeight-2)+'px';style.width=(rect.maxWidth-1)+'px';style.display='';}}
$IG._DragDropManager.registerClass("Infragistics.Web.UI._DragDropManager");$IG.DragDropManager=new $IG._DragDropManager();$IG.DragDropBehavior=function()
{this.__sources=[];this.__targets=[];this._events=new $IG.DragDropEvents(this);this._dragMarkupOpacity=40;this._moveCursor="move";this._copyCursor=$util.IsIE?"default":"copy";this._noneCursor="not-allowed";this._dragDropMode=$IG.DragDropEffects.Default;this._defAction=$IG.DragDropAction.None;this._mouseDownDelegate=Function.createDelegate(this,this._onMouseDown);this._clickDelegate=Function.createDelegate(this,this._onClick);this._mouseMoveDelegate=Function.createDelegate(this,this._onMouseMove);this._dragStartDelegate=Function.createDelegate(this,this._onDragStart);}
$IG.DragDropBehavior.prototype={_addTarget:function(elem,object,includeChildren)
{var target={element:elem,object:object,_includeChildren:includeChildren};this.__targets.push(target);$IG.DragDropManager._registerTarget(this,target);},_addSource:function(elem,obj)
{var source={element:elem,object:obj};this.__sources.push(source);elem.__source=source;$addHandler(elem,'mousedown',this._mouseDownDelegate);$addHandler(elem,'mousemove',this._mouseMoveDelegate);$addHandler(elem,'dragstart',this._dragStartDelegate);$addHandler(elem,'click',this._clickDelegate);},_removeSrc:function(source)
{var elem=source.element;if(!elem||!elem.__source)
return;elem.__source=source.element=null;try
{$removeHandler(elem,'mousedown',this._mouseDownDelegate);$removeHandler(elem,'mousemove',this._mouseMoveDelegate);$removeHandler(elem,'dragstart',this._dragStartDelegate);$removeHandler(elem,'click',this._clickDelegate);}catch(ex){};},dispose:function()
{var list=this.__sources;var i=list?list.length:0;while(i-->0)
this._removeSrc(list[i]);this.get_events().clearHandlers();list=this.__targets;i=list?list.length:0;while(i-->0)
{var item=list[i];$IG.DragDropManager._unRegisterTarget(this,item);item.element=item.behavior=null;}},_onMouseDown:function(e)
{if(e.button==0)
{this._mouseDown=true;if(!$IG.DragDropManager._supportsElemFromPoint||$util.IsFireFox)
$util.cancelEvent(e);}},_onClick:function(e)
{this._mouseDown=false;if($IG.DragDropManager.get_isDragging())
$IG.DragDropManager.endDrag(true);},_onDragStart:function(e)
{$util.cancelEvent(e);},_onMouseMove:function(e)
{if(!$IG.DragDropManager.get_isDragging())
{if(this._mouseDown)
{var elem=e.target;this.__startElem=elem;while(elem&&!elem.__source)
elem=elem.parentNode;if(elem)
$IG.DragDropManager._startDrag(this,elem.__source,e);}}
this._mouseDown=false;},set_currentElementDragMarkup:function(enable)
{this._underMouseElem=enable;},get_currentElementDragMarkup:function()
{return this._underMouseElem==true;},set_dragMarkup:function(markup)
{this._dragMarkup=markup;},get_dragMarkup:function()
{return this._dragMarkup?this._dragMarkup:null;},set_dragMarkupOpacity:function(opacity)
{this._dragMarkupOpacity=opacity;},get_dragMarkupOpacity:function()
{return this._dragMarkupOpacity;},set_moveCursor:function(cursor,isUrl)
{this._moveCursor=isUrl?("url("+cursor+"), move"):cursor;},set_copyCursor:function(cursor,isUrl)
{this._copyCursor=isUrl?("url("+cursor+"), copy"):cursor;},set_noneCursor:function(cursor,isUrl)
{this._noneCursor=isUrl?("url("+cursor+"), not-allowed"):cursor;},set_dragDropMode:function(effect)
{this._dragDropMode=effect;},set_defaultDropAction:function(action)
{this._defAction=action;},get_defaultDropAction:function()
{return this._defAction;},addTargetElement:function(element,includeChildren)
{this._addTarget(element,null,includeChildren);},addTargetObject:function(obj,includeChildren)
{this._addTarget(obj.get_element(),obj,includeChildren);},removeTarget:function(obj)
{var elem=obj;if(!obj)
return;if(typeof obj.get_element=='function')
{elem=obj.get_element();if(!elem)
return;}
else
obj=null;var list=this.__targets;var i=list?list.length:0;while(i-->0)
{var item=list[i];if(!item||item.element!=elem||item.object!=obj)
continue;$IG.DragDropManager._unRegisterTarget(this,item);item.element=item.behavior=null;Array.removeAt(list,i);}},addSourceElement:function(elem)
{this._addSource(elem,null);},addSourceObject:function(obj)
{this._addSource(obj.get_element(),obj);},removeSource:function(obj)
{var elem=obj;if(!obj)
return;if(typeof obj.get_element=='function')
{elem=obj.get_element();if(!elem)
return;}
else
obj=null;var list=this.__sources;var i=list?list.length:0;while(i-->0)
{var item=list[i];if(!item||item.element!=elem||item.object!=obj)
continue;this._removeSrc(item);Array.removeAt(list,i);}},addDragChannels:function(channels)
{if(!this._dragChannels)
this._dragChannels=[];Array.addRange(this._dragChannels,channels);},addDropChannels:function(channels)
{if(!this._dropChannels)
this._dropChannels=[];Array.addRange(this._dropChannels,channels);},get_events:function()
{return this._events;}};$IG.DragDropBehavior.registerClass("Infragistics.Web.UI.DragDropBehavior");$IG.DragDropEvents=function(behavior)
{this._handlers={};this._behavior=behavior;}
$IG.DragDropEvents.prototype={addDragStartHandler:function(handler)
{this.__addHandler("DragStart",handler,$IG.DragDropCancelableMoveEventArgs);},addDropHandler:function(handler)
{this.__addHandler("Drop",handler,$IG.DragDropEventArgs);},addDragEnterHandler:function(handler)
{this.__addHandler("DragEnter",handler,$IG.DragDropCancelEventArgs);},addDragLeaveHandler:function(handler)
{this.__addHandler("DragLeave",handler,$IG.DragDropEventArgs);},addDragCancelHandler:function(handler)
{this.__addHandler("DragCancel",handler,$IG.DragDropEventArgs);},addDragMoveHandler:function(handler)
{this.__addHandler("DragMove",handler,$IG.DragDropMoveEventArgs);},addDragEndHandler:function(handler)
{this.__addHandler("DragEnd",handler,$IG.DragDropEventArgs);},removeDragStartHandler:function(handler)
{this.__removeHandler("DragStart",handler);},removeDropHandler:function(handler)
{this.__removeHandler("Drop",handler);},removeDragEnterHandler:function(handler)
{this.__removeHandler("DragEnter",handler);},removeDragLeaveHandler:function(handler)
{this.__removeHandler("DragLeave",handler);},removeDragCancelHandler:function(handler)
{this.__removeHandler("DragCancel",handler);},removeDragMoveHandler:function(handler)
{this.__removeHandler("DragMove",handler);},removeDragEndHandler:function(handler)
{this.__removeHandler("DragEnd",handler);},clearHandlers:function()
{this._handlers={};},_removeHandler:function(name,handler)
{var handlers=this._handlers[name];var i=handlers?handlers.length:0;while(i-->0)
{if(handlers[i][0]==handler)
{Array.removeAt(handlers,i);break;}}},__addHandler:function(name,handler,args)
{if(!this._handlers[name])
this._handlers[name]=[];this._handlers[name].push([handler,args]);},_fireEvent:function(name,evntArgs)
{var handlers=this._handlers[name];var i=handlers?handlers.length:0;while(i-->0)
{var handler=handlers[i];var evnt=handler[0];var args=new handler[1](evntArgs);evnt(this._behavior,args);if(args._cancel)
return false;}
return true;}}
$IG.DragDropEvents.registerClass("Infragistics.Web.UI.DragDropEvents");$IG.DragDropEventArgs=function()
{}
$IG.DragDropEventArgs.prototype={get_manager:function()
{return $IG.DragDropManager;}}
$IG.DragDropEventArgs.registerClass("Infragistics.Web.UI.DragDropEventArgs");$IG.DragDropMoveEventArgs=function(point)
{this._point=point;$IG.DragDropMoveEventArgs.initializeBase(this);}
$IG.DragDropMoveEventArgs.prototype={get_x:function()
{return this._point.x;},get_y:function()
{return this._point.y;}}
$IG.DragDropMoveEventArgs.registerClass("Infragistics.Web.UI.DragDropMoveEventArgs",$IG.DragDropEventArgs);$IG.DragDropCancelEventArgs=function()
{this._cancel=false;$IG.DragDropCancelEventArgs.initializeBase(this);}
$IG.DragDropCancelEventArgs.prototype={get_cancel:function()
{return this._cancel;},set_cancel:function(cancel)
{this._cancel=cancel;}}
$IG.DragDropCancelEventArgs.registerClass("Infragistics.Web.UI.DragDropCancelEventArgs",$IG.DragDropEventArgs);$IG.DragDropCancelableMoveEventArgs=function(point)
{this._cancel=false;$IG.DragDropCancelableMoveEventArgs.initializeBase(this,[point]);}
$IG.DragDropCancelableMoveEventArgs.prototype={get_cancel:function()
{return this._cancel;},set_cancel:function(cancel)
{this._cancel=cancel;}}
$IG.DragDropCancelableMoveEventArgs.registerClass("Infragistics.Web.UI.DragDropCancelableMoveEventArgs",$IG.DragDropMoveEventArgs);