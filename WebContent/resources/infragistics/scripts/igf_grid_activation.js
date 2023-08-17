// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

$IG.GridActivation=function(obj,objProps,control,parentCollection)
{$IG.GridActivation.initializeBase(this,[obj,objProps,control,parentCollection]);this._parentCollection=parentCollection;this._container=obj.getGrid().elm;this._activeCellCssClass=this._get_clientOnlyValue("acc");this._activeColCssClass=this._get_clientOnlyValue("ahc");this._activeRowCssClass=this._get_clientOnlyValue("arc");this._activeRowSelectorImgCssClass=this._get_clientOnlyValue("arsc");this._grid=obj;this._gridElement=obj.elm.firstChild;this._gridElementMouseDownHandler=Function.createDelegate(this,this._onMousedownHandler);this._gridElementKeyDownHandler=Function.createDelegate(this,this._onKeydownHandler);this._gridElementBeforeSubmitHandler=Function.createDelegate(this,this._onBeforeSubmitHandler);this._gridElementLODPartitioned=Function.createDelegate(this,this._gridElementLODPartitioned);this._parentCollection._addElementEventHandler(this._container,"click",this._gridElementMouseDownHandler);this._parentCollection._addElementEventHandler(this._grid.elm,"keydown",this._gridElementKeyDownHandler);this._parentCollection._addElementEventHandler(obj.elm,"beforesubmit",this._gridElementBeforeSubmitHandler);this._parentCollection._addElementEventHandler(obj.elm,"submit",this._gridElementBeforeSubmitHandler);this._parentCollection._addElementEventHandler(obj.elm,"LODPartitioned",this._gridElementLODPartitioned);this._initializeComplete();}
$IG.GridActivation.prototype={__activeCell:null,__realActiveCell:null,get_activeCell:function()
{if(this.__activeCell)
return this.__activeCell;var activeCell=null;var activeCellString=this._get_value($IG.GridActivationProps.ActiveCell);if(activeCellString!=null)
{var activeCellPosition=null;var key=activeCellString.split(':');if(key.length==2)
{activeCellPosition=new IgGridPosition(ig.toFloat(key[0]),ig.toFloat(key[1]));if(activeCellPosition.col>=0){activeCell=this._grid.get_cellFromPosition(activeCellPosition);if(activeCell!=null){var rowSelector=ig.findDescendant(activeCell.elm,ig.PROP_FLAG,ig.grid.TYPE_GRID_ROW_SELECTOR);if(rowSelector!=null){activeCellPosition=new IgGridPosition(ig.toFloat(key[0]),ig.toFloat(key[1])+1);activeCell=this._grid.get_cellFromPosition(activeCellPosition);}}
this.set_activeCell(activeCell);}else{activeCellPosition.col=0;activeCellPosition.row=this._grid.get_PartitionedLODOffset();activeCell=this._grid.get_cellFromPosition(activeCellPosition);if(activeCell!=null){var rowSelector=ig.findDescendant(activeCell.elm,ig.PROP_FLAG,ig.grid.TYPE_GRID_ROW_SELECTOR);if(rowSelector!=null){activeCellPosition.col=1;activeCell=this._grid.get_cellFromPosition(activeCellPosition);}}}}}
return activeCell;},set_activeCell:function(cell,fireEvent)
{var activeCell=this.__activeCell;if(cell==null&&activeCell==null)
return;if(cell!=null&&this.__realActiveCell!=null&&cell.elm==this.__realActiveCell.elm)
return;if(fireEvent)
{var args=this.__raiseClientEvent("ActiveCellChanging",$IG.ActiveCellChangingEventArgs,[activeCell,cell]);if(args!=null&&args.get_cancel())
return;args=this._grid._gridUtil._fireEvent(this,"ActiveCellChanging",new $IG.ActiveCellChangingEventArgs([activeCell,cell]));if(args!=null&&args.get_cancel())
return;}
this.__realActiveCell=cell
if(cell!=null)
{var rowSelector=ig.findDescendant(cell.elm,ig.PROP_FLAG,ig.grid.TYPE_GRID_ROW_SELECTOR);if(!ig.isNull(rowSelector)){return}}
this.__activeCell=cell;if(activeCell!=null)
{var elem=activeCell.get_element();this.__removeCssClass(activeCell);elem.tabIndex=-1;}
var key=null;if(cell!=null)
{key=cell.getPosition();var elem=cell.get_element();this.__addCssClass(cell);if(this._grid.tabIndex!=-1)
elem.tabIndex=this._grid.tabIndex;else
elem.tabIndex=0;if($util.IsIE){var curElm;if(this._grid.isScrolling())
curElm=this._grid.getBody().getScrollingDiv().elm.parentNode;else
curElm=this._grid.getBody().elm.parentNode;var scrollingElements=new Array();while(curElm&&curElm.nodeName!="BODY"&&curElm.nodeName!="HTML"){if(curElm.style["position"]=="fixed"){break;}
if(curElm.scrollLeft!=0||curElm.scrollTop!=0){var pos=scrollingElements.length;scrollingElements[pos]=new Object();scrollingElements[pos].elm=curElm
scrollingElements[pos].scrollLeft=curElm.scrollLeft
scrollingElements[pos].scrollTop=curElm.scrollTop}
curElm=curElm.parentNode;}
var docScrollLeft=document.documentElement.scrollLeft;var docScrollTop=document.documentElement.scrollTop;}
if(elem.style.visibility!="hidden"){try{elem.focus();if($util.IsIE){for(var pos=0;pos<scrollingElements.length;pos++){scrollingElement=scrollingElements[pos];var curElm=scrollingElement.elm;if(scrollingElement.scrollLeft>0)
curElm.scrollLeft=scrollingElement.scrollLeft;if(scrollingElement.scrollTop>0)
curElm.scrollTop=scrollingElement.scrollTop;}
if(docScrollLeft>0)
document.documentElement.scrollLeft=docScrollLeft;if(docScrollTop>0)
document.documentElement.scrollTop=docScrollTop;}
cell.scrollToView();}
catch(e){}}}
this._set_value($IG.GridActivationProps.ActiveCell,key);if(fireEvent){var eventArguments="col:"+cell.getIndex()+";row:"+cell.getRowIndex();this._grid.removeEvent(this._grid.getId(),"ActiveCellChanged");this._grid.queueEvent(this._grid.getId(),"ActiveCellChanged",eventArguments);this.__raiseClientEvent("ActiveCellChanged",$IG.ActiveCellChangedEventArgs,[cell]);this._grid._gridUtil._fireEvent(this,"ActiveCellChanged",cell);}},destroy:function(){this.reset();this.set_activeCell(null,false);this._parentCollection._removeElementEventHandler(this._container,"click",this._gridElementMouseDownHandler);this._parentCollection._removeElementEventHandler(this._grid.elm,"keydown",this._gridElementKeyDownHandler);this._parentCollection._removeElementEventHandler(this._grid.elm,"beforesubmit",this._gridElementBeforeSubmitHandler);this._parentCollection._removeElementEventHandler(this._grid.elm,"submit",this._gridElementBeforeSubmitHandler);this._parentCollection._removeElementEventHandler(this._grid.elm,"LODPartitioned",this._gridElementLODPartitioned);},reset:function()
{},_onMousedownHandler:function(evnt)
{if(evnt.event.button==0)
{cell=ig.getTargetUIElement(evnt.target);if(cell!=null)
this.set_activeCell(cell,true);}},_get_realActiveCell:function(evnt)
{if(this.__realActiveCell)
return this.__realActiveCell;else
return this.get_activeCell();},_onKeydownHandler:function(evnt)
{if(evnt.event.cancelBubble)
return;var key=evnt.keyCode;var cell;if(key==Sys.UI.Key.tab||key==Sys.UI.Key.left||key==Sys.UI.Key.right)
cell=this._get_realActiveCell()
else
cell=this.get_activeCell();if(cell!=null)
{var nextCell=null;if(key==Sys.UI.Key.left||(key==Sys.UI.Key.tab&&evnt.isShiftKeyPressed()))
nextCell=this._grid.getPrevCell(cell);else if(key==Sys.UI.Key.up)
nextCell=this._grid.getPrevCellVert(cell);else if(key==Sys.UI.Key.right||(key==Sys.UI.Key.tab&&!evnt.isShiftKeyPressed()))
nextCell=this._grid.getNextCell(cell);else if(key==Sys.UI.Key.down){nextCell=this._grid.getNextCellVert(cell);}
if(nextCell!=null)
{var rowSelector=ig.findDescendant(nextCell.elm,ig.PROP_FLAG,ig.grid.TYPE_GRID_ROW_SELECTOR);if(ig.isNull(rowSelector)){this.set_activeCell(nextCell,true);$util.cancelEvent(evnt);}else{this.__realActiveCell=nextCell;if(key!=Sys.UI.Key.tab)
this._onKeydownHandler(evnt);else{$util.cancelEvent(evnt);rowSelector.focus();}}}else{this.__realActiveCell=this.__activeCell;}}},_rowSelectorClicked:function(args)
{this.set_activeCell(args.row.get_cell(0),true);},_onBeforeSubmitHandler:function(evt)
{ig.grid.inSubmit=true;this.reset();ig.grid.inSubmit=false;},_gridElementLODPartitioned:function(evt)
{},__addCssClass:function(cell)
{$util.addCompoundClass(cell.get_element(),this._activeCellCssClass);var colIndex=cell.getIndex();var hdr=this._grid.getColumnsHeader();if(hdr!=null){var colElem=hdr.getCell(hdr.getRowCount()-1,colIndex);if(colElem)
$util.addCompoundClass(colElem.get_element(),this._activeColCssClass);}
var rowSelector=cell.getParentRow().getRowSelector();if(rowSelector!=null){var rowSelectorCell=ig.findAncestor(rowSelector,ig.PROP_TYPE,ig.grid.TYPE_GRID_CELL);$util.addCompoundClass(rowSelectorCell,this._activeRowCssClass);}},__removeCssClass:function(cell)
{$util.removeCompoundClass(cell.get_element(),this._activeCellCssClass);var colIndex=cell.getIndex();var hdr=this._grid.getColumnsHeader();if(hdr!=null){var colElem=hdr.getCell(hdr.getRowCount()-1,colIndex);if(colElem)
$util.removeCompoundClass(colElem.get_element(),this._activeColCssClass);}
var rowSelector=cell.getParentRow().getRowSelector();if(rowSelector!=null){var rowSelectorCell=ig.findAncestor(rowSelector,ig.PROP_TYPE,ig.grid.TYPE_GRID_CELL);$util.removeCompoundClass(rowSelectorCell,this._activeRowCssClass);}},addActiveCellChangedEventHandler:function(handler)
{this._grid._gridUtil._registerEventListener(this,"ActiveCellChanged",handler);},addActiveCellChangingEventHandler:function(handler)
{this._grid._gridUtil._registerEventListener(this,"ActiveCellChanging",handler);},removeActiveCellChangedEventHandler:function(handler)
{this._grid._gridUtil._unregisterEventListener(this,"ActiveCellChanged",handler);},removeActiveCellChangingEventHandler:function(handler)
{this._grid._gridUtil._unregisterEventListener(this,"ActiveCellChanging",handler);},_initializeComplete:function()
{this._rowSelectors=this._parentCollection.getBehaviorFromInterface($IG.IRowSelectorsBehavior);if(this._rowSelectors)
this._rowSelectors.addRowSelectorClickedEventHandler(Function.createDelegate(this,this._rowSelectorClicked));var activeCell=this.get_activeCell();if(activeCell!=null)
{var elem=activeCell.get_element();if(this._grid.tabIndex!=-1)
elem.tabIndex=this._grid.tabIndex;else
elem.tabIndex=0;}},dispose:function()
{this._parentCollection._removeElementEventHandler(this._container,"click",this._gridElementMouseDownHandler);this._parentCollection._removeElementEventHandler(this._container,"keydown",this._gridElementKeyDownHandler);}}
$IG.GridActivation.registerClass('Infragistics.Web.UI.GridActivation',$IG.GridBehavior,$IG.IActivationBehavior);$IG.GridActivationProps=new function()
{this.ActiveCell=[$IG.GridBehaviorProps.Count,null];this.Count=$IG.GridBehaviorProps.Count+1;};$IG.ActiveCellChangingEventArgs=function(params)
{$IG.ActiveCellChangingEventArgs.initializeBase(this);this._currentActiveCell=params[0];this._newActiveCell=params[1];}
$IG.ActiveCellChangingEventArgs.prototype={getCurrentActiveCell:function()
{return this._currentActiveCell;},getNewActiveCell:function()
{return this._newActiveCell;}}
$IG.ActiveCellChangingEventArgs.registerClass('Infragistics.Web.UI.ActiveCellChangingEventArgs',$IG.CancelEventArgs);$IG.ActiveCellChangedEventArgs=function(params)
{$IG.ActiveCellChangedEventArgs.initializeBase(this);this._activeCell=params[0];}
$IG.ActiveCellChangedEventArgs.prototype={getActiveCell:function()
{return this._activeCell;}}
$IG.ActiveCellChangedEventArgs.registerClass('Infragistics.Web.UI.ActiveCellChangedEventArgs',$IG.EventArgs);