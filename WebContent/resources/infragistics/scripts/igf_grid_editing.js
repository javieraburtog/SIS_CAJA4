// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

$IG.GridEditing=function(obj,objProps,control,parentCollection)
{$IG.GridEditing.initializeBase(this,[obj,objProps,control,parentCollection]);this._parentCollection=parentCollection;this._createObjects(objProps[0]);this._initializeComplete();this._grid=obj;this._gridElement=obj.elm.firstChild;this._container=obj.getGrid().elm;this._editCellCssClass="igGridCellEdit";this._containerMouseDownHandler=Function.createDelegate(this,this._onMousedownHandler);this._containerDoubleClickHandler=Function.createDelegate(this,this._onDblclickHandler);this._gridElementKeyDownHandler=Function.createDelegate(this,this._onKeydownHandler);this._gridElementKeyPress=Function.createDelegate(this,this._onKeypressHandler);this._gridElementBeforeSubmitHandler=Function.createDelegate(this,this._onBeforeSubmitHandler);this._parentCollection._addElementEventHandler(this._container,"click",this._containerMouseDownHandler);this._parentCollection._addElementEventHandler(this._container,"dblclick",this._containerDoubleClickHandler);this._parentCollection._addElementEventHandler(obj.elm,"keydown",this._gridElementKeyDownHandler);this._parentCollection._addElementEventHandler(obj.elm,"keypress",this._gridElementKeyPress)
this._parentCollection._addElementEventHandler(obj.elm,"beforesubmit",this._gridElementBeforeSubmitHandler)
this._parentCollection._addElementEventHandler(obj.elm,"submit",this._gridElementBeforeSubmitHandler)
this._cellInEditMode=null;this._currentEditor=null;this._lastEditedRow=-1;this.editors=new Object();}
$IG.GridEditing.prototype={get_editModeActions:function()
{return this._editModeActions;},destroy:function(){this.resetEditors();this._parentCollection._removeElementEventHandler(this._container,"click",this._containerMouseDownHandler);this._parentCollection._removeElementEventHandler(this._container,"dblclick",this._containerDoubleClickHandler);this._parentCollection._removeElementEventHandler(this._grid.elm,"keydown",this._gridElementKeyDownHandler);this._parentCollection._removeElementEventHandler(this._grid.elm,"keypress",this._gridElementKeyPress)
this._parentCollection._removeElementEventHandler(this._grid.elm,"beforesubmit",this._gridElementBeforeSubmitHandler)
this._parentCollection._removeElementEventHandler(this._grid.elm,"submit",this._gridElementBeforeSubmitHandler)
this.dipose();},enterEditMode:function(cell)
{if(cell==null)
return;if(!this._activation&&this._lastEditedRow>=0&&this._lastEditedRow!=cell.getRowIndex()){this.exitEditMode();this._grid.onRowChange();}
if(this._cellInEditMode!=null){if(this._cellInEditMode.elm==cell.elm)
return;if(this._grid.getEditMode()=="row"){this.exitEditMode(true);}else{this.exitEditMode();}}
if(this._cellInEditMode!=null)
return;if(this._grid.getEditMode()=="row"&&this._lastEditedRow!=cell.getRowIndex()){this.__hideEditors(cell,null);}
var editor=this.__resolveEditor(cell);if(editor==null)
return;if(this.enteringEditMode)
return;this.enteringEditMode=true;var args=this.__raiseClientEvent("EnteringEditMode",$IG.CancelEditModeEventArgs,cell);if(args==null||!args.get_cancel())
{this.__showEditors(cell,editor);editor.enableEditor();this._cellInEditMode=cell;this._currentEditor=editor;this._lastEditedRow=cell.getRowIndex();this.enteringEditMode=false;this.__raiseClientEvent("EnteredEditMode",$IG.EditModeEventArgs,cell);}else{this.enteringEditMode=false;}},exitEditMode:function(doNotHideEditor)
{if(this._cellInEditMode!=null)
{var args=this.__raiseClientEvent("ExitingEditMode",$IG.CancelEditModeEventArgs,this._cellInEditMode);if(args==null||!args.get_cancel())
{var editor=this._currentEditor;this._container.style.position="";var val=editor.get_value();if(!doNotHideEditor){this.__hideEditors(this._cellInEditMode,editor);}
if(this._grid.getEditMode()!="row"){this._cellInEditMode.set_value(val,editor.get_displayText(val));}
this.__raiseClientEvent("ExitedEditMode",$IG.EditModeEventArgs,this._cellInEditMode);this._cellInEditMode=null;this._currentEditor=null;}}else{if(this._grid.getEditMode()=="row"){this.__hideEditors(null,null);}}},get_isInEditMode:function(cell)
{return(this._cellInEditMode==cell);},resetEditors:function()
{this.hideEditors();for(var i in this.editors){var editor=this.editors[i];if($IG.GridInternalWrapperEditor.isInstanceOfType(editor)){var columnEditor=document.getElementById(this._grid.getId()+":"+i);if(columnEditor!=null){if(columnEditor.firstChild==null)
columnEditor.appendChild(editor.elm);}}
editor._removeHandlers();}
this.editors=new Object();this._lastEditedRow=-1;},hideEditors:function()
{this.exitEditMode();if(this._grid.getEditMode()=="row"){this.__hideEditors(null,null);;}},_onMousedownHandler:function(evnt)
{if(this._currentEditor!=null&&evnt.target==this._currentEditor._element)
return;if(this._editModeActions.get_mouseClick()==$IG.EditMouseClickAction.Single)
this.__enterEditModeEvntHandler(evnt);},_onDblclickHandler:function(evnt)
{if(this._currentEditor!=null&&evnt.target==this._currentEditor._element)
return;if(this._editModeActions.get_mouseClick()==$IG.EditMouseClickAction.Double)
this.__enterEditModeEvntHandler(evnt);},_onKeypressHandler:function(evnt)
{var key=evnt.keyCode;if(this._editModeActions.get_enableOnKeyPress())
{if(this._activation&&this._cellInEditMode==null)
{if(key==Sys.UI.Key.esc||key==Sys.UI.Key.right||key==Sys.UI.Key.tab||key==Sys.UI.Key.left||key==Sys.UI.Key.up||key==Sys.UI.Key.down)
return;var activeCell=this._activation.get_activeCell();if(activeCell!=null&&activeCell==this._activation._get_realActiveCell())
{this.enterEditMode(activeCell);if(this._cellInEditMode!=null&&this._currentEditor!=null){if(key!=Sys.UI.Key.enter){this._currentEditor.set_value(String.fromCharCode(key));this._currentEditor.__setCursorPosition(1);}
evnt.preventDefault();}}}}},_onKeydownHandler:function(evnt)
{var key=evnt.keyCode;if(key==Sys.UI.Key.esc){this.__editorLostFocus(evnt);}
if(this._activation)
{if(key==113)
{if(this._editModeActions.get_enableF2())
{var activeCell=this._activation.get_activeCell();if(activeCell!=null&&activeCell==this._activation._get_realActiveCell())
this.enterEditMode(activeCell);}}}},_activeCellChanging:function(event)
{var activeCell=event.getCurrentActiveCell();var newActiveCell=event.getNewActiveCell();if(newActiveCell==null||activeCell!=null&&activeCell.getRowIndex()!=newActiveCell.getRowIndex()){this.exitEditMode();activeCell.getGrid().onRowChange();this._lastEditedRow=-1;}},_activeCellChanged:function(cell)
{if(this._editModeActions.get_enableOnActive()){this.__activating=true;this.enterEditMode(cell);this.__activating=false;}else{if(this._grid.getEditMode()=="row"&&this._lastEditedRow>=0){this.enterEditMode(cell);}}},_onBeforeSubmitHandler:function(evt)
{ig.grid.inSubmit=true;this.resetEditors();ig.grid.inSubmit=false;},__showEditors:function(cell,editor){if(this._grid.getEditMode()=="row"){if(this._lastEditedRow!=cell.getRowIndex()){var colCount=this._grid.getColCount();var colIndex=cell.getIndex();var rowIndex=cell.getRowIndex();var position=new IgGridPosition(rowIndex,0);for(;position.col<colCount;position.col++){if(position.col!=colIndex){var aCell=this._grid.get_cellFromPosition(position);var aEditor=this.__resolveEditor(aCell);if(aEditor!=null){this.__showEditorForCell(aCell,aEditor);aEditor.saveState();}}}
this.__showEditorForCell(cell,editor);editor.saveState();}}else{this.__showEditorForCell(cell,editor);editor.saveState();}},__hideEditors:function(cell,editor){if(this._grid.getEditMode()=="row"){if(this._lastEditedRow<0)
return;var colCount=this._grid.getColCount();var position=new IgGridPosition(this._lastEditedRow,0);for(;position.col<colCount;position.col++){{var aCell=this._grid.get_cellFromPosition(position);var aEditor=this.__resolveEditor(aCell);if(aEditor!=null){var val=aEditor.get_value();var displayText=aEditor.get_displayText(val);this.__hideEditorForCell(aCell,aEditor);aCell.set_value(val,displayText);}}}
this._lastEditedRow=-1;}else{this.__hideEditorForCell(cell,editor);}},__restoreState:function(){if(this._grid.getEditMode()=="row"){if(this._lastEditedRow<0)
return;var colCount=this._grid.getColCount();var position=new IgGridPosition(this._lastEditedRow,0);for(;position.col<colCount;position.col++){var aCell=this._grid.get_cellFromPosition(position);var aEditor=this.__resolveEditor(aCell);if(aEditor!=null){aEditor.restoreState();}}}else{if(this._currentEditor!=null){this._currentEditor.restoreState();}}},__showEditorForCell:function(cell,editor){var cellValue=cell.get_value();cell.elm.setAttribute("val",cellValue);editor.cellHeight=cell.elm.offsetHeight;editor.cellWidth=cell.elm.offsetWidth
var leftCellBorder=cell.getBorder("left");var rightCellBorder=cell.getBorder("right");if(leftCellBorder>rightCellBorder){editor.cellWidth=editor.cellWidth-leftCellBorder;}else{editor.cellWidth=editor.cellWidth-rightCellBorder;}
var topCellBorder=cell.getBorder("top");var bottomCellBorder=cell.getBorder("bottom");if(topCellBorder>bottomCellBorder){editor.cellHeight=editor.cellHeight-topCellBorder;}else{editor.cellHeight=editor.cellHeight-bottomCellBorder;}
var isFirstRow=cell.elm.offsetParent.rows[0]==cell.elm.parentNode;var isLastRow=cell.elm.offsetParent.rows[cell.elm.offsetParent.rows.length-1]==cell.elm.parentNode;if(ig.isIE){if(cell.elm.previousSibling==null&&cell.elm.offsetParent!=null){var table=new IgUIElement(cell.elm.offsetParent);var leftBorder=table.getBorder("left");if(leftCellBorder<rightCellBorder){editor.cellWidth=editor.cellWidth+rightCellBorder/2;}else{editor.cellWidth=editor.cellWidth+leftCellBorder/2;}
if(leftBorder==0){editor.cellWidth=editor.cellWidth-leftCellBorder;}}
if(cell.elm.nextSibling==null&&cell.elm.offsetParent!=null){var table=new IgUIElement(cell.elm.offsetParent);var rightBorder=table.getBorder("right");if(leftCellBorder<rightCellBorder){editor.cellWidth=editor.cellWidth+rightCellBorder/2;}else{editor.cellWidth=editor.cellWidth+leftCellBorder/2;}
if(rightBorder==0){editor.cellWidth=editor.cellWidth-rightCellBorder;}}
if(cell.elm.offsetParent!=null&&(isFirstRow||cell.elm.parentNode.previousSibling==null)){var table=new IgUIElement(cell.elm.offsetParent);var topBorder=table.getBorder("top");if(!isFirstRow){topBorder=0;}
if(topCellBorder<bottomCellBorder){editor.cellHeight=editor.cellHeight+bottomCellBorder/2;}else{editor.cellHeight=editor.cellHeight+topCellBorder/2;}
if(topBorder==0){if(isFirstRow){editor.cellHeight=editor.cellHeight-topCellBorder;}else{editor.cellHeight=editor.cellHeight-topCellBorder/2;}}}
if(cell.elm.offsetParent!=null&&(isLastRow||cell.elm.parentNode.nextSibling==null)){var table=new IgUIElement(cell.elm.offsetParent);var bottomBorder=table.getBorder("bottom");if(!isLastRow){bottomBorder=0;}
if(topCellBorder<bottomCellBorder){editor.cellHeight=editor.cellHeight+bottomCellBorder/2;}else{editor.cellHeight=editor.cellHeight+topCellBorder/2;}
if(bottomBorder==0){if(isLastRow){editor.cellHeight=editor.cellHeight-bottomCellBorder;}else{editor.cellHeight=editor.cellHeight-bottomCellBorder/2;}}}}
else
if(ig.isFirefox){if(cell.elm.previousSibling==null&&cell.elm.offsetParent!=null){var table=new IgUIElement(cell.elm.offsetParent);var leftBorder=table.getBorder("left");if(leftCellBorder<rightCellBorder){if(leftBorder>leftCellBorder){editor.cellWidth=editor.cellWidth+Math.floor(rightCellBorder/2)-Math.floor(leftBorder/2);}else{editor.cellWidth=editor.cellWidth+Math.floor(rightCellBorder/2)-Math.floor(leftCellBorder/2);}}else{if(leftBorder>leftCellBorder){editor.cellWidth=editor.cellWidth+Math.floor(leftCellBorder/2)-Math.floor(leftBorder/2);}}}
if(cell.elm.nextSibling==null&&cell.elm.offsetParent!=null){var table=new IgUIElement(cell.elm.offsetParent);var rightBorder=table.getBorder("right");if(leftCellBorder<rightCellBorder){if(rightBorder>rightCellBorder){editor.cellWidth=editor.cellWidth+Math.floor(rightCellBorder/2)-Math.floor((rightBorder+1)/2);}}else{if(rightBorder>rightCellBorder){editor.cellWidth=editor.cellWidth+Math.floor(leftCellBorder/2)-Math.floor((rightBorder+1)/2);}else{editor.cellWidth=editor.cellWidth+Math.floor(leftCellBorder/2)-Math.floor((rightCellBorder+1)/2);}}}
if(cell.elm.offsetParent!=null&&(isFirstRow||cell.elm.parentNode.previousSibling==null)){var table=new IgUIElement(cell.elm.offsetParent);var topBorder=table.getBorder("top");if(topCellBorder<bottomCellBorder){if(topBorder>topCellBorder){if(isFirstRow)
editor.cellHeight=editor.cellHeight+Math.floor(bottomCellBorder/2)-Math.floor(topBorder/2);else{editor.cellHeight=editor.cellHeight+Math.floor(bottomCellBorder/2)-Math.floor(topCellBorder/2);}}else{editor.cellHeight=editor.cellHeight+Math.floor(bottomCellBorder/2)-Math.floor(topCellBorder/2);}}else{if(topBorder>topCellBorder){if(isFirstRow){editor.cellHeight=editor.cellHeight+Math.floor(topCellBorder/2)-Math.floor(topBorder/2);}}}}
if(cell.elm.offsetParent!=null&&(isLastRow||cell.elm.parentNode.nextSibling==null)){var table=new IgUIElement(cell.elm.offsetParent);var bottomBorder=table.getBorder("bottom");if(topCellBorder<bottomCellBorder){if(bottomBorder>bottomCellBorder){if(isLastRow){editor.cellHeight=editor.cellHeight+Math.floor(bottomCellBorder/2)-Math.floor(bottomBorder/2);}}}else{if(bottomBorder>bottomCellBorder){if(isLastRow)
editor.cellHeight=editor.cellHeight+Math.floor(topCellBorder/2)-Math.floor(bottomBorder/2);else{editor.cellHeight=editor.cellHeight+Math.floor(topCellBorder/2)-Math.floor(bottomCellBorder/2);}}else{editor.cellHeight=editor.cellHeight+Math.floor(topCellBorder/2)-Math.floor(bottomCellBorder/2);}}}}
if(editor.cellInnerHTML==null){editor.cellInnerHTML=cell.elm.innerHTML;editor.cellPadding=cell.getStyle("padding");cell.elm.innerHTML="";cell.setStyle("padding","0px");cell.setSize(editor.cellWidth,editor.cellHeight);}
if(cell.elm.firstChild!=null)
cell.elm.insertBefore(editor.elm,cell.elm.firstChild);else
cell.elm.appendChild(editor.elm);editor.set_value(cellValue);editor.showEditor(0,0,editor.cellWidth,editor.cellHeight,this._editCellCssClass,cell.elm);},__hideEditorForCell:function(cell,editor){if(!editor._shown)
return;editor.hideEditor();cell.elm.innerHTML=editor.cellInnerHTML;cell.setStyle("padding",editor.cellPadding);cell.setSize(editor.cellWidth,editor.cellHeight);editor.cellInnerHTML=null;},__enterEditModeEvntHandler:function(evnt)
{if(evnt.event.button==0)
{var cell=ig.grid.getTargetCell(evnt.target);if(cell!=null)
this.enterEditMode(cell);}},__editorFocus:function(evnt)
{if(this._grid.getEditMode()!="row")
return;if(this._currentEditor!=null)
{this._currentEditor._originalNotifyFocus(evnt);}
this.enterEditMode(ig.grid.getTargetCell(evnt.target));},__editorLostFocus:function(evnt)
{if(this._currentEditor!=null)
{this._currentEditor._originalNotifyLostFocus(evnt);if(evnt!=null&&(evnt.type=="keydown"||evnt.type=="keypress"))
{var key=evnt.keyCode;if(key==Sys.UI.Key.tab||key==Sys.UI.Key.enter)
{if(key==Sys.UI.Key.tab&&this._editModeActions.get_enableOnActive())
return;var cell=this._cellInEditMode;var nextCell=cell;do{if(!evnt.shiftKey){nextCell=this._grid.getNextCell(nextCell);}else
nextCell=this._grid.getPrevCell(nextCell);editor=this.__resolveEditor(nextCell);}while(editor==null&&nextCell!=null);if(nextCell!=null)
{if(this._activation)
this._activation.set_activeCell(nextCell,true);}
if(this._grid.getEditMode()=="row"&&nextCell!=null&&cell!=null&&nextCell.getRowIndex()==cell.getRowIndex()){if(key==Sys.UI.Key.enter)
{evnt.stopPropagation();evnt.preventDefault();this.exitEditMode();this._grid.onRowChange(evnt);}
return;}else{if(!this._activation){this._grid.onRowChange(evnt);}
this.exitEditMode();}
if(nextCell!=null)
{evnt.stopPropagation();evnt.preventDefault();this._lastEditedRow=-1;}}
else
{if(key==Sys.UI.Key.esc){this.__restoreState();this.exitEditMode();}else if(key==Sys.UI.Key.enter)
{this.exitEditMode();evnt.stopPropagation();evnt.preventDefault();}
if(this._activation)
{var activeCell=this._activation.get_activeCell();if(activeCell)
activeCell.get_element().focus();}}}else if(evnt!=null&&evnt.type=="blur"){if(this._grid.getEditMode()=="row"){this.exitEditMode(true);}else{if(this.__activating)
this.exitEditMode();}}
else
this.exitEditMode();}},__resolveEditor:function(cell)
{if(cell==null)
return null;var editor=null;var editorName="colEd"+cell.getIndex();editor=this.editors[editorName];if(editor==null){var columnEditor=document.getElementById(this._grid.getId()+":colEd"+cell.getIndex());if(columnEditor!=null){var readonly=columnEditor.getAttribute("readonly");if(readonly!=null&&readonly=="true")
return null;}
var editorDiv=document.getElementById(this._grid.getId()+":colEdDiv"+cell.getIndex());if(editorDiv!=null){editor=new $IG.GridInternalWrapperEditor(editorDiv);this.editors[editorName]=editor;}}
if(editor==null){editor=new $IG.GridInternalEditor(cell.elm);this.editors[editorName]=editor;}
if(editor._originalNotifyLostFocus==null)
{editor._originalNotifyLostFocus=editor.notifyLostFocus;editor.notifyLostFocus=Function.createDelegate(this,this.__editorLostFocus);}
if(editor._originalNotifyFocus==null)
{editor._originalNotifyFocus=editor.notifyFocus;editor.notifyFocus=Function.createDelegate(this,this.__editorFocus);}
return editor;},_createObjects:function(props)
{this._editModeActions=new $IG.EditModeActions("EditModeActions",null,[[props]],this);},_createCollections:function(collectionsManager)
{this._editors=collectionsManager.register_collection(0,$IG.ObjectCollection);var collectionItems=collectionsManager._collections[0];for(var columnKey in collectionItems)
this._editors._addObject($IG.ColumnEditor,null,columnKey);},_initializeComplete:function()
{this._activation=this._parentCollection.getBehaviorFromInterface($IG.IActivationBehavior);if(this._activation){this._activeCellChangingHandler=Function.createDelegate(this,this._activeCellChanging);this._activeCellChangedHandler=Function.createDelegate(this,this._activeCellChanged);this._activation.addActiveCellChangingEventHandler(this._activeCellChangingHandler);this._activation.addActiveCellChangedEventHandler(this._activeCellChangedHandler);}},dipose:function()
{this._activation=this._parentCollection.getBehaviorFromInterface($IG.IActivationBehavior);if(this._activation){this._activation.removeActiveCellChangingEventHandler(this._activeCellChangingHandler);this._activation.removeActiveCellChangedEventHandler(this._activeCellChangedHandler);}}}
$IG.GridEditing.registerClass('Infragistics.Web.UI.GridEditing',$IG.GridBehavior,$IG.IEditingBehavior);$IG.GridEditingProps=new function()
{this.Count=$IG.GridBehaviorProps.Count;};$IG.EditMouseClickAction=function(){}
$IG.EditMouseClickAction.prototype={None:0,Single:1,Double:2};$IG.EditMouseClickAction.registerEnum("Infragistics.Web.UI.EditMouseClickAction");$IG.EditModeActionsProps=new function()
{this.MouseClick=[$IG.ObjectBaseProps.Count+0,$IG.EditMouseClickAction.Double];this.EnableF2=[$IG.ObjectBaseProps.Count+1,true];this.EnableOnActive=[$IG.ObjectBaseProps.Count+2,false];this.EnableOnKeyPress=[$IG.ObjectBaseProps.Count+3,true];this.Count=$IG.ObjectBaseProps.Count+4;};$IG.ColumnEditorProps=new function()
{this.ColumnKey=[$IG.ObjectBaseProps.Count+0,""];this.EditorID=[$IG.ObjectBaseProps.Count+1,""];this.ReadOnly=[$IG.ObjectBaseProps.Count+2,false];this.Count=$IG.ObjectBaseProps.Count+3;};$IG.EditModeActions=function(obj,element,props,control)
{var csm=obj?new $IG.ObjectClientStateManager(props[0]):null;$IG.EditModeActions.initializeBase(this,[obj,element,props,control,csm]);}
$IG.EditModeActions.prototype={get_mouseClick:function()
{return this._get_value($IG.EditModeActionsProps.MouseClick);},set_mouseClick:function(value)
{this._set_value($IG.EditModeActionsProps.MouseClick,value);},get_enableF2:function()
{return this._get_value($IG.EditModeActionsProps.EnableF2,true);},set_enableF2:function(value)
{this._set_value($IG.EditModeActionsProps.EnableF2,value);},get_enableOnActive:function()
{return this._get_value($IG.EditModeActionsProps.EnableOnActive,true);},set_enableOnActive:function(value)
{this._set_value($IG.EditModeActionsProps.EnableOnActive,value);},get_enableOnKeyPress:function()
{return this._get_value($IG.EditModeActionsProps.EnableOnKeyPress,true);},set_enableOnKeyPress:function(value)
{this._set_value($IG.EditModeActionsProps.EnableOnKeyPress,value);}}
$IG.EditModeActions.registerClass('Infragistics.Web.UI.EditModeActions',$IG.ObjectBase);$IG.GridInternalEditor=function(parentElement)
{var editorDiv=document.createElement("div");editorDiv.style.border="0px";editorDiv.style.padding="0px";editorDiv.style.margin="0px";this.input=new IgUIElement(document.createElement("input"));editorDiv.appendChild(this.input.elm);editorDiv.style.display="none";$IG.GridInternalEditor.initializeBase(this,[editorDiv]);this.elm=editorDiv;this._inputOnBlurHandler=Function.createDelegate(this,this._onBlurHandler);this._inputOnFocusHandler=Function.createDelegate(this,this._onFocusHandler);this._inputOnKeyDownHandler=Function.createDelegate(this,this._onKeyDownHandler);this._inputOnClickHandler=Function.createDelegate(this,this._onClickHandler);$addHandler(this.input.elm,"keydown",this._inputOnKeyDownHandler);$addHandler(this.input.elm,"click",this._inputOnClickHandler);this._shown=false;}
$IG.GridInternalEditor.prototype={get_value:function()
{return this.input.elm.value;},set_value:function(val)
{this.input.elm.value=val;},saveState:function()
{this.prevValue=this.get_value();},restoreState:function()
{this.set_value(this.prevValue);},showEditor:function(top,left,width,height,cssClass,parent)
{var parent=new IgUIElement(parent);var elm=new IgUIElement(this.elm);var leftPadding=parent.getPadding("left");if(leftPadding>0){this.setStyle("margin-left",""+(-leftPadding)+"px");}
var topPadding=parent.getPadding("top");if(topPadding>0){this.setStyle("margin-top",""+(-topPadding)+"px");}
this.input.setStyle("left","0px");this.input.setStyle("right","0px");if(ig.isIE){this.input.setStyle("margin-top","-1px");}
parent.setStyle("visibility","hidden");elm.setStyle("visibility","visible");this.elm.style.display="";this.input.elm.className=cssClass;elm.setSize(width,height);this.input.setSize(width,height);$addHandler(this.input.elm,"blur",this._inputOnBlurHandler);$addHandler(this.input.elm,"focus",this._inputOnFocusHandler);this._shown=true;},enableEditor:function(){if(this.input.elm.select)
this.input.elm.select();this.input.elm.focus();},notifyFocus:function(evnt)
{},notifyLostFocus:function(evnt)
{},get_displayText:function(val)
{return this.input.elm.value;},hideEditor:function()
{if(!this._shown)return;$removeHandler(this.input.elm,"blur",this._inputOnBlurHandler);$removeHandler(this.input.elm,"focus",this._inputOnFocusHandler);var cell=ig.grid.getTargetCell(this.elm);if(!ig.isNull(cell)){cell.setStyle("visibility","visible");this.elm.style.display="none";this.elm.parentNode.removeChild(this.elm);}
this._shown=false;},_removeHandlers:function(){$removeHandler(this.input.elm,"keydown",this._inputOnKeyDownHandler);$removeHandler(this.input.elm,"click",this._inputOnClickHandler);},_onKeyDownHandler:function(evnt)
{var key=evnt.keyCode;if(key==Sys.UI.Key.left){$util.cancelEvent(evnt);}else if(key==Sys.UI.Key.right){$util.cancelEvent(evnt);}else if(key==Sys.UI.Key.esc){this.notifyLostFocus(evnt);}else if(key==Sys.UI.Key.tab||key==Sys.UI.Key.enter)
this.notifyLostFocus(evnt);},_onClickHandler:function(evnt)
{$util.cancelEvent(evnt);this.notifyFocus(evnt);},_onFocusHandler:function(evnt)
{this.notifyFocus(evnt);evnt.stopPropagation();evnt.preventDefault();},_onBlurHandler:function(evnt)
{this.notifyLostFocus(evnt);},__setCursorPosition:function(pos){if(this.input.elm&&this.input.elm.createTextRange){var range=this.input.elm.createTextRange();range.collapse(true);range.moveEnd('character',pos);range.moveStart('character',pos);range.select();}else if(this.input.elm.selectionEnd){this.input.elm.selectionEnd=pos;this.input.elm.selectionStart=pos;}},__getCursorPosition:function(){var field=this.input.elm;var cursorPos=-1;if(document.selection&&document.selection.createRange){var range=document.selection.createRange().duplicate();if(range.parentElement()==field){range.moveStart('textedit',-1);cursorPos=range.text.length;}}else if(field.selectionEnd){cursorPos=field.selectionEnd;}
return cursorPos;}}
$IG.GridInternalEditor.registerClass('Infragistics.Web.UI.GridInternalEditor',Sys.UI.Control);$IG.GridInternalWrapperEditor=function(editorDiv)
{this.input=ig.findDescendant(editorDiv,null,null,"input");if(this.input==null)
this.input=ig.findDescendant(editorDiv,null,null,"select");if(this.input==null)
this.input=ig.findDescendant(editorDiv,null,null,"textarea");if(this.input.type=="checkbox"){editorDiv.style.textAlign="center";}
this._inputOnBlurHandler=Function.createDelegate(this,this._onBlurHandler);this._inputOnFocusHandler=Function.createDelegate(this,this._onFocusHandler);$IG.GridInternalWrapperEditor.initializeBase(this,[editorDiv]);this.elm=editorDiv;this._inputOnKeyDownHandler=Function.createDelegate(this,this._onKeyDownHandler);this._inputOnClickHandler=Function.createDelegate(this,this._onClickHandler);$addHandler(this.input,"keydown",this._inputOnKeyDownHandler);$addHandler(this.input,"click",this._inputOnClickHandler);}
$IG.GridInternalWrapperEditor.prototype={get_value:function()
{if(this.input.type=="checkbox"){return this.input.checked;}else if(this.input.type=="radio"){if(this.input.form){var radiobuttons=this.input.form[this.input.name];for(var i=radiobuttons.length-1;i>-1;i--){if(radiobuttons[i].checked){return radiobuttons[i].value;}}}}
else
return this.input.value;},set_value:function(val)
{if(this.input.type=="checkbox"){this.input.checked=(val==true||val=="true");}else if(this.input.type=="radio"){var radiobuttons=this.input.form[this.input.name];for(var i=radiobuttons.length-1;i>-1;i--){if(radiobuttons[i].value==val){radiobuttons[i].checked=true;}}}
else{this.input.value=val;}},saveState:function()
{this.prevValue=this.get_value();},restoreState:function()
{this.set_value(this.prevValue);},showEditor:function(top,left,width,height,cssClass,parent)
{var parent=new IgUIElement(parent);var elm=new IgUIElement(this.elm);var leftPadding=parent.getPadding("left");if(leftPadding>0){elm.setStyle("margin-left",""+(-leftPadding)+"px");}
var topPadding=parent.getPadding("top");if(topPadding>0){elm.setStyle("margin-top",""+(-topPadding)+"px");}
var input=new IgUIElement(this.input);if(this.input.type!="radio"){input.setStyle("left","0px");input.setStyle("right","0px");if(ig.isIE){input.setStyle("margin-top","-1px");}}
parent.setStyle("visibility","hidden");elm.setStyle("visibility","visible");this.elm.style.display="";this.elm.className=cssClass;elm.setSize(width,height);if(this.input.type=="text"||this.input.type=="textarea"){if(this.input.nextSibling==null&&this.input.nextSibling==null){input.setSize(width,height);$addHandler(this.input,"blur",this._inputOnBlurHandler);}else{input.setSize(null,height);}}else if(this.input.type=="select"){input.setSize(null,height);}
$addHandler(this.input,"focus",this._inputOnFocusHandler);this._shown=true;},enableEditor:function(){if(this.input.select)
this.input.select();this.input.focus();},notifyFocus:function(evnt)
{},notifyLostFocus:function(evnt)
{},get_displayText:function(val)
{return val;},hideEditor:function()
{$removeHandler(this.input,"focus",this._inputOnFocusHandler);if(this.input.type=="text"||this.input.type=="textarea"){if(this.input.nextSibling==null&&this.input.nextSibling==null){$removeHandler(this.input,"blur",this._inputOnBlurHandler);}}
var cell=ig.grid.getTargetCell(this.elm);if(!ig.isNull(cell)){cell.setStyle("visibility","visible");this.elm.style.display="none";this.elm.parentNode.removeChild(this.elm);}},_removeHandlers:function(){$removeHandler(this.input,"keydown",this._inputOnKeyDownHandler);$removeHandler(this.input,"click",this._inputOnClickHandler);},_onKeyDownHandler:function(evnt)
{var key=evnt.keyCode;if(key==Sys.UI.Key.left||key==Sys.UI.Key.right||key==Sys.UI.Key.down||key==Sys.UI.Key.up){$util.cancelEvent(evnt);}else if(key==Sys.UI.Key.esc){this.notifyLostFocus(evnt);}else if(key==Sys.UI.Key.tab||(key==Sys.UI.Key.enter&&this.input.type!="textarea")){this.notifyLostFocus(evnt);}},_onClickHandler:function(evnt)
{if(this.input.type!="checkbox"&&this.input.type!="radio"){ig.ui.cancelClick=true;}
this.notifyFocus(evnt);},_onFocusHandler:function(evnt)
{this.notifyFocus(evnt);evnt.stopPropagation();evnt.preventDefault();},_onBlurHandler:function(evnt)
{this.notifyLostFocus(evnt);},__setCursorPosition:function(pos){if(this.input.type=="text"){var field=this.input;if(field!=null&&field.createTextRange){var range=field.createTextRange();range.collapse(true);range.moveEnd('character',pos);range.moveStart('character',pos);range.select();}else if(field.selectionEnd){field.selectionEnd=pos;field.selectionStart=pos;}}},__getCursorPosition:function(){if(this.input.type=="text"){var field=this.input;var cursorPos=-1;if(document.selection&&document.selection.createRange){var range=document.selection.createRange().duplicate();if(range.parentElement()==field){range.moveStart('textedit',-1);cursorPos=range.text.length;}}else if(field.selectionEnd){cursorPos=field.selectionEnd;}
return cursorPos;}else{return-1;}}}
$IG.GridInternalWrapperEditor.registerClass('Infragistics.Web.UI.GridInternalWrapperEditor',Sys.UI.Control);$IG.ColumnEditor=function(adr,element,props,owner,csm)
{$IG.ColumnEditor.initializeBase(this,[adr,element,props,owner,csm]);}
$IG.ColumnEditor.prototype={get_columnKey:function()
{return this._get_value($IG.ColumnEditorProps.ColumnKey);},get_editorID:function()
{return this._get_value($IG.ColumnEditorProps.EditorID,true);},get_readOnly:function()
{return this._get_value($IG.ColumnEditorProps.ReadOnly,true);}}
$IG.ColumnEditor.registerClass('Infragistics.Web.UI.ColumnEditor',$IG.ObjectBase);$IG.CancelEditModeEventArgs=function(cell)
{$IG.CancelEditModeEventArgs.initializeBase(this);this._cell=cell;}
$IG.CancelEditModeEventArgs.prototype={getCell:function()
{return this._cell;}}
$IG.CancelEditModeEventArgs.registerClass('Infragistics.Web.UI.CancelEditModeEventArgs',$IG.CancelEventArgs);$IG.CancelCellUpdateEventArgs=function(args)
{$IG.CancelCellUpdateEventArgs.initializeBase(this);this._cell=args[0];this._newValue=args[1];}
$IG.CancelCellUpdateEventArgs.prototype={getCell:function()
{return this._cell;},getNewValue:function()
{return this._newValue;}}
$IG.CancelCellUpdateEventArgs.registerClass('Infragistics.Web.UI.CancelCellUpdateEventArgs',$IG.CancelEventArgs);$IG.CellUpdateEventArgs=function(cell)
{$IG.CellUpdateEventArgs.initializeBase(this);this._cell=cell;}
$IG.CellUpdateEventArgs.prototype={getCell:function()
{return this._cell;}}
$IG.CellUpdateEventArgs.registerClass('Infragistics.Web.UI.CellUpdateEventArgs',$IG.EventArgs);$IG.EditModeEventArgs=function(cell)
{$IG.EditModeEventArgs.initializeBase(this);this._cell=cell;}
$IG.EditModeEventArgs.prototype={getCell:function()
{return this._cell;}}
$IG.EditModeEventArgs.registerClass('Infragistics.Web.UI.EditModeEventArgs',$IG.EventArgs);