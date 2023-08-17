// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

$IG.GridAuxRows=new function()
{this.Top=0;this.Bottom=1;};$IG.GridFiltering=function(obj,objProps,control,parentCollection)
{$IG.GridFiltering.initializeBase(this,[obj,objProps,control,parentCollection]);this._auxRowsBottom=[];this._auxRowsTop=[];this._grid=obj;this._gridElement=obj.elm.firstChild;this._container=obj.getGrid().elm;this._clientColumnFilters=[];this._editCellCssClass="igGridCellEdit";this._onMousewheelHandler=Function.createDelegate(this,this._onMousewheel);$addHandler(document,'mousewheel',this._onMousewheelHandler);$addHandler(document,'click',this._onMousewheelHandler);$addHandler(document,'dblclick',this._onMousewheelHandler);if($util.IsFireFox)
$addHandler(window,"DOMMouseScroll",this._onMousewheelHandler);this._onSelectRuleHandler=Function.createDelegate(this,this._onSelectRule);this._onMouseOverRuleHandler=Function.createDelegate(this,this._onMouseOverRule);this._onMouseOutRuleHandler=Function.createDelegate(this,this._onMouseOutRule);this._onKeyDownRuleHandler=Function.createDelegate(this,this._onKeyDownRule);this.__defaultDate=new Date();this._onGridResizeHandler=Function.createDelegate(this,this._onGridResize);this._grid._gridUtil._registerEventListener(this._grid,"Resize",this._onGridResizeHandler);this._gridElementSelectStartHandler=Function.createDelegate(this,this._onSelectstartHandler);this._grid._gridUtil._registerEventListener(this._grid,"SelectStartContainer",this._gridElementSelectStartHandler);var filterRow=document.getElementById(this._grid.getId()+"_fheaders_sr");if(!filterRow){filterRow=document.getElementById(this._grid.getId()+"_fheaders");}
if(!filterRow){return;}
this._row=new $IG.FilterRow(-1,filterRow,[],this._grid,null,this);this._row._element.setAttribute("adr",-1);this._row._container=this._row._element.parentNode.parentNode.parentNode;this._onClickHandler=Function.createDelegate(this,this._onClickHandler);$addHandler(this._row._element,"click",this._onClickHandler);this._onDblClickHandler=Function.createDelegate(this,this._onDblclickHandler);$addHandler(this._row._element,"dblclick",this._onDblClickHandler);this._parentCollection=parentCollection;this._createObjects(objProps);this._initializeComplete();this._onFilterKeyDownHandler=Function.createDelegate(this,this._onFilterKeyDown);$addHandler(this._row._element,'keydown',this._onFilterKeyDownHandler);this._dropDownBehaviorsCount=0;this._cellInEditMode=null;}
$IG.GridFiltering.prototype={_visibleDropdownCell:null,__showEditorForCell:function(cell,editor){var cellValue=cell.get_text();cell.elm.setAttribute("val",cellValue);editor.cellHeight=cell.elm.offsetHeight;editor.cellWidth=cell.elm.offsetWidth-25;var leftCellBorder=cell.getBorder("left");var rightCellBorder=cell.getBorder("right");if(leftCellBorder>rightCellBorder){editor.cellWidth=editor.cellWidth-leftCellBorder;}else{editor.cellWidth=editor.cellWidth-rightCellBorder;}
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
editor.cellWidth=editor.cellWidth-25;if(editor.cellInnerHTML==null){editor.cellInnerHTML=cell.elm.innerHTML;editor.cellPadding=cell.getStyle("padding");cell.elm.childNodes[1].innerHTML="";cell.setStyle("padding","0px");cell.setSize(editor.cellWidth,editor.cellHeight);}
cell.elm.appendChild(editor.elm);editor.showEditor(0,0,editor.cellWidth,editor.cellHeight,this._editCellCssClass,cell.elm);},get_columnFromKey:function(key){var columns=[];var fEl=document.getElementById(this._grid.elm.id+"_fheaders_sr");if(!fEl){fEl=document.getElementById(this._grid.elm.id+"_fheaders");}
var childCount=fEl.childNodes.length;for(i=0;i<childCount;i++){var child=fEl.childNodes[i];if(child.getAttribute("key")==key)
return new IgGridFilterColumn(this._grid,i,child.getAttribute("key"),child.getAttribute("ftype"));}
return null;},enterEditMode:function(cell)
{if(this._cellInEditMode!=null)
this.exitEditMode(true);if(this._grid._cellInEditMode)
return;var child=cell.get_element();setTimeout($util.createDelegate(this,this.__internalEnterEditMode,[cell]),1);},_onGridStopEditing:function()
{this.exitEditMode(true);},_onGridSelectStart:function(){},__internalEnterEditMode:function(cell)
{if(this._grid._cellInEditMode||this._cellInEditMode!=null||this._grid._isAjaxCallInProgress)
return;var editor=this.__resolveEditor(cell);if(editor==null)
return;var customDisplay={cancel:null,top:null,left:null,height:null,width:null,text:null};var key=cell.get_column()._key;var columnFilter=this.get_columnFilterFromKey(key);columnFilter=columnFilter?columnFilter:this._get_clientColumnFilterFromKey(key);var rule=columnFilter?columnFilter.get_condition().get_rule():0;var displayText=columnFilter?columnFilter.get_condition().get_value():"";var args=this.__raiseClientEvent("FilterEnteringEditMode",$IG.CancelFilterEventArgs,[cell,null,rule]);if(args==null||!args.get_cancel())
{if(args)
customDisplay.text=args.get_filterText();var container=cell.get_row()._container;if(!container)
container=this._container;this._cellInEditMode=new IgGridFilterCell(cell.elm);this._cellInEditMode.set_row(this._row);this._currentEditor=editor;this._grid._cellInEditMode=true;var elem=cell.elm;var top=customDisplay.top;var left=customDisplay.left;var height=customDisplay.height;var width=customDisplay.width;if(width==null)
width=elem.offsetWidth;if(left==null)
left=elem.offsetLeft;if(height==null)
height=elem.offsetHeight;if(top==null)
{if(elem.nodeName=='TD'||elem.nodeName=='TH')
elem=elem.parentNode;top=elem.offsetTop;}
editor.set_value((customDisplay.text==null)?displayText:customDisplay.text,displayText);if($util.IsOpera&&this._horizontalScrollBar)
left-=this._horizontalScrollBar.scrollLeft;if($util.IsOpera&&this._verticalScrollBar)
top-=this._verticalScrollBar.scrollTop;this.__showEditorForCell(cell,editor);this.__raiseClientEvent("FilterEnteredEditMode",$IG.FilterEventArgs,[cell,null,rule]);if(!this._onGridResizeHandler)
{this._onGridResizeHandler=Function.createDelegate(this,this._onGridStopEditing);this._onGridMouseWheelHandler=Function.createDelegate(this,this._onGridStopEditing);this._onGridScrollLeftChangeHandler=Function.createDelegate(this,this._onGridStopEditing);this._onGridScrollTopChangeHandler=Function.createDelegate(this,this._onGridStopEditing);this._onGridSelectStartHandler=Function.createDelegate(this,this._onGridSelectStart);this._grid._gridUtil._registerEventListener(this._grid,"Resize",this._onGridResizeHandler);this._grid._gridUtil._registerEventListener(this._grid,"MouseWheel",this._onGridMouseWheelHandler);this._grid._gridUtil._registerEventListener(this._grid,"ScrollLeftChange",this._onGridScrollLeftChangeHandler);this._grid._gridUtil._registerEventListener(this._grid,"ScrollTopChange",this._onGridScrollTopChangeHandler);this._grid._gridUtil._registerEventListener(this._grid,"SelectStartContainer",this._onGridSelectStartHandler);var i=0,div=this._grid.elm;while(div=div.parentNode)
{var tag=div.nodeName;if(tag=='BODY'||tag=='FORM'||tag=='HTML')
break;if(tag!='DIV')
continue;var scroll=$util.getStyleValue(null,'overflow',div);if(scroll!='auto'&&scroll!='scroll')
continue;$addHandler(div,'scroll',this._onGridResizeHandler);if(i==0)
this._scrollDIVs=new Array();this._scrollDIVs[i++]=div;}}}},exitEditMode:function(update)
{var cell=this._cellInEditMode,editor=this._currentEditor;if(!cell||!editor||!editor.get_value)
return;var vals=[];if(vals&&update)
{var input=editor.input,val=editor.get_value();this._gridElement.value=(val==null)?'':''+val;}
if(editor._originalNotifyLostFocus!=null)
{editor.notifyLostFocus=editor._originalNotifyLostFocus;editor._originalNotifyLostFocus=null;}
var key=cell.get_column()._key;var columnFilter=this.get_columnFilterFromKey(key);columnFilter=columnFilter?columnFilter:this._get_clientColumnFilterFromKey(key);var rule=columnFilter?columnFilter.get_condition().get_rule():0;var args=this.__raiseClientEvent("FilterExitingEditMode",$IG.CancelFilterEventArgs,[cell,editor.get_value(),rule]);if(args==null||!args.get_cancel())
{if(this._onGridResizeHandler)
{var i=this._scrollDIVs?this._scrollDIVs.length:0;while(i-->0)
$removeHandler(this._scrollDIVs[i],'scroll',this._onGridResizeHandler);delete this._scrollDIVs;this._grid._gridUtil._unregisterEventListener(this._grid,"Resize",this._onGridResizeHandler);delete this._onGridResizeHandler;this._grid._gridUtil._unregisterEventListener(this._grid,"MouseWheel",this._onGridMouseWheelHandler);delete this._onGridMouseWheelHandler;this._grid._gridUtil._unregisterEventListener(this._grid,"ScrollLeftChange",this._onGridScrollLeftChangeHandler);delete this._onGridScrollLeftChangeHandler;this._grid._gridUtil._unregisterEventListener(this._grid,"ScrollTopChange",this._onGridScrollTopChangeHandler);delete this._onGridScrollTopChangeHandler;this._grid._gridUtil._unregisterEventListener(this._grid,"SelectStartContainer",this._onGridSelectStartHandler);delete this._onGridSelectStartHandler;}
this._container.style.position="";var valueChanged=false;var inputValue;if(update)
{var val=editor.get_value();inputValue=val;if(val!=cell.get_value())
{valueChanged=true;cell.set_value(val,val);}}else{var currentValue=cell.get_value()=="undefuned"?"":cell.get_value();cell.set_text(currentValue,currentValue);}
editor.hideEditor();this._grid._gridUtil._fireEvent(this._grid,"ExitedEdit",{cell:cell,update:update,valueChanged:valueChanged,inputValue:inputValue});this.__raiseClientEvent("FilterExitedEditMode",$IG.FilterEventArgs,[cell,inputValue,rule]);this._exitedEditMode({cell:cell,update:update,valueChanged:valueChanged,inputValue:inputValue});this._cellInEditMode=null;this._currentEditor=null;this._grid._cellInEditMode=false;}},__resolveEditor:function(cell)
{var editor=null;if(editor==null){editor=new $IG.GridFilterEditor(cell);}
var validators=this._gridElement._validators_;this.Validators=null;return editor;},get_cellInEditMode:function()
{return this._cellInEditMode;},create_columnFilter:function(columnKey)
{var column=this.get_columnFromKey(columnKey);if(!column)
return null;return new $IG.ClientColumnFilter(columnKey,column.get_type(),this);},add_columnFilter:function(columnFilter)
{var clientFilter=this.get_columnFilterFromKey(columnFilter.get_columnKey());if(!clientFilter)
{var eventArgs=this.__raiseClientEvent("FilterAddingRemoving",$IG.ColumnFilterAddingRemovingEventArgs,this);if(eventArgs==null||eventArgs.get_cancel()){this._clientColumnFilters[this._clientColumnFilters.length]=columnFilter;}}else{clientFilter.get_condition().set_rule(columnFilter.get_condition().get_rule());}},add_columnFilterRange:function(columnFilters)
{if(columnFilters)
{for(i=0;i<columnFilters.length;i++){this.add_columnFilter(columnFilters[i]);}}},get_columnFilters:function()
{return this._columnFilters;},get_columnFiltersCount:function()
{return this._columnFilters.length;},get_columnFilter:function(index)
{if(index<0||index>=this._columnFilters.length)
return null;else
return this._columnFilters[index];},get_columnFilterFromKey:function(columnKey)
{for(var i=0;i<this._columnFilters.length;i++)
{if(this._columnFilters[i].get_columnKey()===columnKey)
return this._columnFilters[i];}
return null;},get_columnFilterIndexOf:function(columnFilter)
{for(var i=0;i<this._columnFilters.length;i++)
{if(this._columnFilters[i]===columnFilter)
return i;}
return-1;},containsColumnFilter:function(columnKey)
{for(var i=0;i<this._columnFilters.length;i++)
{if(this._columnFilters[i].get_columnKey()===columnKey)
return true;}
return false;},removeColumnFilter:function(columnFilter)
{this.removeColumnFilterByKey(columnFilter.get_columnKey());},removeColumnFilterByKey:function(columnKey)
{if(!this.containsColumnFilter(columnKey))
return;var eventArgs=new $IG.ColumnFilterAddingRemovingEventArgs(this);if(eventArgs==null||!eventArgs.get_cancel()){for(var i=0;i<this._columnFilters.length;i++)
{if(this._columnFilters[i].get_columnKey()===columnKey){for(var j=i;j<this._columnFilters.length-1;j++){this._columnFilters[j]=this._columnFilters[j+1];}
this._columnFilters.length=this._columnFilters.length-1;var r=this.create_columnFilter(columnKey);r.get_condition().set_rule(0);r.get_condition().set_value("");this.add_columnFilter(r);break;}}}},clearColumnFilters:function()
{var eventArgs=new $IG.ColumnFilterAddingRemovingEventArgs(this);if(eventArgs==null||!eventArgs.get_cancel()){for(var i=0;i<this._clientColumnFilters.length;i++){this._clientColumnFilters[i].get_condition().set_rule(0);this._clientColumnFilters[i].get_condition().set_value("");}
if(this._columnFilters&&this._columnFilters.length>0){for(var i=this._columnFilters.length-1;i>=0;i--){this.removeColumnFilterByKey(this._columnFilters[i].get_columnKey());}}}},get_columnSettings:function()
{return this._columnSettings;},get_columnSetting:function(index)
{if(index>=0&&index<this._columnSettings._items.length)
return this._columnSettings._items[index];else
return null;},get_columnSettingFromKey:function(columnKey)
{for(var i=0;i<this._columnSettings._items.length;i++)
{if(this._columnSettings._items[i].get_columnKey()===columnKey)
return this._columnSettings._items[i];}
return null;},applyFilters:function()
{this._apply_filters(true);},get_alignment:function()
{return this._get_clientOnlyValue("fra");},get_filtered:function()
{return this._get_clientOnlyValue("fa");},get_visibility:function()
{return this._get_value($IG.GridFilteringProps.Visibility);},set_visibility:function(value)
{this._set_value($IG.GridFilteringProps.Visibility,value);var scrollIntersection=this._grid._elements["filterInter"];if(value==$IG.FilteringVisibility.Visible)
{this._row._element.style.display="";if(scrollIntersection)
scrollIntersection.style.display="";if($util.IsFireFox2&&this.get_alignment()==$IG.FilteringAlignment.Bottom)
{var width=this._row._element.childNodes[0].style.width;elm=this._row._element.childNodes[0];elm.style.width="0px";setTimeout($util.createDelegate(this,this._adjFootVisibility,[elm,width]),1);}
else
{this._row._element.style.visibility="visible";if(scrollIntersection)
{scrollIntersection.style.visibility="visible";this._grid._onResize({"clientHeight":this._grid._element.clientHeight},false);}}}
else
{this._row._element.style.display="none";this._row._element.style.visibility="hidden";if(scrollIntersection)
{scrollIntersection.style.display="none";scrollIntersection.style.visibility="hidden";this._grid._onResize({"clientHeight":this._grid._element.clientHeight},false);}}},get_filterRuleDropdownZIndex:function()
{return this._get_value($IG.GridFilteringProps.RuleDropdownZIndex);},set_filterRuleDropdownZIndex:function(value)
{if(value==null||value==undefined)
return;this._set_value($IG.GridFilteringProps.RuleDropdownZIndex,value);for(var i=0;i<this._dropDownBehaviorsCount;i++)
{if(this._dropDownBehaviors[i])
this._dropDownBehaviors[i].set_zIndex(value);}},get_animationEnabled:function()
{return this._get_clientOnlyValue("fae");},get_animationType:function()
{return this._get_clientOnlyValue("fat");},get_animationDurationMs:function()
{return this._get_clientOnlyValue("fad");},dispose:function()
{if(!this._grid)
return;if(this._visibleDropdownCell&&this._visibleDropdownCell._dropDownBehaviour&&this._visibleDropdownCell._dropDownBehaviour.get_visible())
{this._closeRuleDropdown(this._visibleDropdownCell);}
var targetsToDelete=[];var animationContainerToDelete=[];for(var i=0;i<this._dropDownBehaviorsCount;i++)
{if(this._dropDownBehaviors[i])
{targetsToDelete[targetsToDelete.length]=this._dropDownBehaviors[i].get_targetContainer();animationContainerToDelete[animationContainerToDelete.length]=this._dropDownBehaviors[i].get_animationsContainer();this._dropDownBehaviors[i].dispose();}
this._dropDownBehaviors[i]=null;}
this._dropDownBehaviors=null;this._dropDownBehaviorsCount=0;if(this._onMouseDownHandler)
{$removeHandler(document,'mousedown',this._onMouseDownHandler);delete this._onMouseDownHandler;}
if(this._onMouseUpHandler)
{$removeHandler(document,'mouseup',this._onMouseUpHandler);delete this._onMouseUpHandler;}
if(this._onMousewheelHandler)
{$removeHandler(document,'mousewheel',this._onMousewheelHandler);if($util.IsFireFox)
$removeHandler(window,"DOMMouseScroll",this._onMousewheelHandler);delete this._onMousewheelHandler;}
if(this._onKeyDownHandler)
{$removeHandler(document,'keydown',this._onKeyDownHandler);delete this._onKeyDownHandler;}
if(this._onFilterKeyDownHandler)
{$removeHandler(this._row._element,'keydown',this._onFilterKeyDownHandler);delete this._onFilterKeyDownHandler;}
if(this._onSelectRuleHandler)
{try{$removeHandler($get(this._grid.elm.id+"_NumericRuleDropDown_UL"),'mousedown',this._onSelectRuleHandler);$removeHandler($get(this._grid.elm.id+"_TextRuleDropDown_UL"),'mousedown',this._onSelectRuleHandler);$removeHandler($get(this._grid.elm.id+"_DateTimeRuleDropDown_UL"),'mousedown',this._onSelectRuleHandler);$removeHandler($get(this._grid.elm.id+"_BooleanRuleDropDown_UL"),'mousedown',this._onSelectRuleHandler);}catch(e){}
for(var i=0;i<targetsToDelete.length;i++)
{try
{$removeHandler(targetsToDelete[i].firstChild,'mousedown',this._onSelectRuleHandler);}catch(e){}}
delete this._onSelectRuleHandler;}
if(this._onMouseOverRuleHandler)
{try{$removeHandler($get(this._grid.elm.id+"_NumericRuleDropDown_UL"),'mouseover',this._onMouseOverRuleHandler);$removeHandler($get(this._grid.elm.id+"_TextRuleDropDown_UL"),'mouseover',this._onMouseOverRuleHandler);$removeHandler($get(this._grid.elm.id+"_DateTimeRuleDropDown_UL"),'mouseover',this._onMouseOverRuleHandler);$removeHandler($get(this._grid.elm.id+"_BooleanRuleDropDown_UL"),'mouseover',this._onMouseOverRuleHandler);}catch(e){}
for(var i=0;i<targetsToDelete.length;i++)
{try
{$removeHandler(targetsToDelete[i].firstChild,'mouseover',this._onMouseOverRuleHandler);}catch(e){}}
delete this._onMouseOverRuleHandler;}
if(this._onMouseOutRuleHandler)
{try{$removeHandler($get(this._grid.elm.id+"_NumericRuleDropDown_UL"),'mouseout',this._onMouseOutRuleHandler);$removeHandler($get(this._grid.elm.id+"_TextRuleDropDown_UL"),'mouseout',this._onMouseOutRuleHandler);$removeHandler($get(this._grid.elm.id+"_DateTimeRuleDropDown_UL"),'mouseout',this._onMouseOutRuleHandler);$removeHandler($get(this._grid.elm.id+"_BooleanRuleDropDown_UL"),'mouseout',this._onMouseOutRuleHandler);}catch(e){}
for(var i=0;i<targetsToDelete.length;i++)
{try
{$removeHandler(targetsToDelete[i].firstChild,'mouseout',this._onMouseOutRuleHandler);}catch(e){}}
delete this._onMouseOutRuleHandler;}
if(this._onKeyDownRuleHandler)
{try{$removeHandler($get(this._grid.elm.id+"_NumericRuleDropDown"),'keydown',this._onKeyDownRuleHandler);$removeHandler($get(this._grid.elm.id+"_TextRuleDropDown"),'keydown',this._onKeyDownRuleHandler);$removeHandler($get(this._grid.elm.id+"_DateTimeRuleDropDown"),'keydown',this._onKeyDownRuleHandler);$removeHandler($get(this._grid.elm.id+"_BooleanRuleDropDown"),'keydown',this._onKeyDownRuleHandler);}catch(e){}
for(var i=0;i<targetsToDelete.length;i++)
{try
{$removeHandler(targetsToDelete[i].firstChild,'keydown',this._onKeyDownRuleHandler);}catch(e){}}
delete this._onKeyDownRuleHandler;}
if(this._onKeyPressRuleHandler)
{$removeHandler(document,'keypress',this._onKeyPressRuleHandler);delete this._onKeyPressRuleHandler;}
if(this._onGridResizeHandler)
{this._grid._gridUtil._unregisterEventListener(this._grid,"Resize",this._onGridResizeHandler);delete this._onGridResizeHandler;}
if(!Sys.Application._disposing)
{if(this._columnFilters&&this._columnFilters.length)
{for(var i=0;i<this._columnFilters.length;i++)
{if(this._columnFilters[i])
this._columnFilters[i].dispose();this._columnFilters[i]=null;}}}
this._columnFilters=null;if(this._clientColumnFilters&&this._clientColumnFilters.length)
{for(var i=0;i<this._clientColumnFilters.length;i++)
{if(this._clientColumnFilters[i])
this._clientColumnFilters[i].dispose();this._clientColumnFilters[i]=null;}}
this._clientColumnFilters=null;for(var i=0;i<targetsToDelete.length;i++)
{if(targetsToDelete[i]&&targetsToDelete[i].parentNode)
{targetsToDelete[i].parentNode.removeChild(targetsToDelete[i]);targetsToDelete[i]=null;}}
targetsToDelete=null;for(var i=0;i<animationContainerToDelete.length;i++)
{if(animationContainerToDelete[i]&&animationContainerToDelete[i].parentNode)
{animationContainerToDelete[i].parentNode.removeChild(animationContainerToDelete[i]);animationContainerToDelete[i]=null;}}
animationContainerToDelete=null;this._objectManager=null;this._activation=null;this._columnResizing=null;this._rowSelectors=null;this.editSpan=null;this.editFilterButton=null;this._visibleDropdownCell=null;this.__defaultDate=null;$IG.GridFiltering.callBaseMethod(this,"dispose");this._grid=null;this._gridElement=null;this._gridContainer=null;this._container=null;},destroy:function(){},_addEnteringEditEventListener:function(handler)
{this._grid._gridUtil._registerEventListener(this,"EnteringEditMode",handler);},_addExitedEditEventListener:function(handler)
{this._grid._gridUtil._registerEventListener(this,"ExitedEditMode",handler);},_get_auxRows:function(alignment)
{if(alignment==$IG.GridAuxRows.Top)
return this._auxRowsTop;if(alignment==$IG.GridAuxRows.Bottom)
return this._auxRowsBottom;return this._auxRowsTop.concat(this._auxRowsBottom);},_isAuxRow:function(row,alignment)
{if(typeof(alignment)=="undefined")
return this._isAuxRow(row,$IG.GridAuxRows.Top)||this._isAuxRow(row,$IG.GridAuxRows.Bottom);var auxRows=this._get_auxRows(alignment);for(var i=0;i<auxRows.length;i++)
if(auxRows[i]==row)
return true;return false;},_get_auxRowIndex:function(row,alignment)
{if(typeof(alignment)=="undefined")
throw"Must indicate alignment of the aux rows";var auxRows=this._get_auxRows(alignment);for(var i=0;i<auxRows.length;i++)
if(auxRows[i]==row)
return i;return-1;},_registerAuxRow:function(row,alignment)
{if(typeof(alignment)=="undefined")
throw"Must indicate alignment of the aux rows";var auxRows=this._get_auxRows(alignment);auxRows[auxRows.length]=row;row._element.setAttribute("auxRow",alignment);row._element.setAttribute("adr",auxRows.length-1);},_adjFootVisibility:function(elm,width)
{elm.style.width=width;this._row._element.style.visibility="visible";var scrollIntersection=this._grid._elements["filterInter"];if(scrollIntersection)
{scrollIntersection.style.visibility="visible";this._grid._onResize({"clientHeight":this._grid._element.clientHeight},false);}},_createObjects:function(props)
{this._columnFilters=[];var filtersProps=props[0][2];var columnFiltersCount=parseInt(this._get_clientOnlyValue("cfc"));for(var i=0;i<columnFiltersCount;i++)
{var obj=new $IG.ColumnFilter("ColumnFilter",null,filtersProps[i],this);this._columnFilters[i]=obj;obj=null;}},_createCollections:function(collectionsManager)
{this._columnSettings=collectionsManager.register_collection(0,$IG.ObjectCollection);var collectionItems=collectionsManager._collections[0];for(var columnKey in collectionItems)
this._columnSettings._addObject($IG.ColumnFilterSettings,null,columnKey);},_enteringEditMode:function(eventArgs)
{if(eventArgs.cell.get_row()===this._row)
{if(this._grid._isAjaxCallInProgress)
{eventArgs.customDisplay.cancel=true;return;}
var filterSettings=this.get_columnSettingFromKey(eventArgs.cell._column._key);if((filterSettings!=null&&!filterSettings.get_enabled())||!this._allowEdit(eventArgs.cell)||eventArgs.cell._column.get_isTemplated())
{eventArgs.customDisplay.cancel=true;return;}
var left=eventArgs.cell._element.childNodes[0].offsetLeft+eventArgs.cell._element.childNodes[0].offsetWidth;eventArgs.customDisplay.width=eventArgs.cell.elm.offsetWidth-left;eventArgs.customDisplay.width=(eventArgs.customDisplay.width<0)?0:eventArgs.customDisplay.width;eventArgs.customDisplay.height=eventArgs.cell.elm.offsetHeight;eventArgs.customDisplay.left=(eventArgs.cell.elm.offsetLeft+left);eventArgs.customDisplay.top=eventArgs.cell.elm.offsetTop;this.editFilterButton=eventArgs.cell.elm.childNodes[0].cloneNode(true);this.editSpan=eventArgs.cell.elm.childNodes[1].cloneNode(true);eventArgs.customDisplay.text=(eventArgs.cell.elm.getAttribute("val")!=null&&eventArgs.cell.elm.getAttribute("val")!="null")?eventArgs.cell.get_value():$util.htmlUnescapeCharacters(eventArgs.cell.elm.childNodes[1].innerHTML);this._grid._gridUtil._fireEvent(this._grid,"FilterCellEnteringEdit",eventArgs);}},_allowEdit:function(cell)
{var column=cell._column;var type=column.get_type();var key=column._key;var columnFilter=this.get_columnFilterFromKey(key);columnFilter=columnFilter?columnFilter:this._get_clientColumnFilterFromKey(key);if(type==="boolean")
return false;else if(type==="date"&&columnFilter&&!(columnFilter.get_condition().get_rule()===$IG.DateTimeFilterRules.All||columnFilter.get_condition().get_rule()===$IG.DateTimeFilterRules.Equals||columnFilter.get_condition().get_rule()===$IG.DateTimeFilterRules.Before||columnFilter.get_condition().get_rule()===$IG.DateTimeFilterRules.After))
{return false;}
return true;},_exitedEditMode:function(eventArgs)
{if(eventArgs.cell.get_row()===this._row&&eventArgs.update)
{var value=eventArgs.inputValue;var text=eventArgs.cell.elm.innerHTML;var colKey=eventArgs.cell._column._key;var index=eventArgs.cell.getIndex();var columnFilter=this.get_columnFilterFromKey(colKey);var saveFilterNeeded=columnFilter?false:true;columnFilter=columnFilter?columnFilter:this._get_clientColumnFilterFromKey(colKey);if(!columnFilter)
{columnFilter=this.create_columnFilter(colKey);this._clientColumnFilters[this._clientColumnFilters.length]=columnFilter;}
columnFilter.set_columnIndex(index);var oldVal=columnFilter.get_condition().get_value();if(eventArgs.valueChanged)
columnFilter.get_condition().set_value(value);var newVal=columnFilter.get_condition().get_value();if(newVal==""){columnFilter.get_condition().set_rule(0);}
var type=eventArgs.cell._column.get_type();var valueChanged=(oldVal!=newVal);valueChanged=(type!="date")?valueChanged:valueChanged&&!(oldVal.valueOf()==this.__defaultDate.valueOf()&&newVal=="");this.editSpan=null;this.editFilterButton=null;if((saveFilterNeeded&&valueChanged)||((newVal!=""||valueChanged))){this._clientColumnFilters[this._clientColumnFilters.length]=columnFilter;this._apply_filters(true);}}},_generate_args:function(columnFilters){var fkeys=new Array();var ftypes=new Array();var fvalues=new Array();var frules=new Array();var findexes=new Array();if(columnFilters){for(i=0;i<columnFilters.length;i++){var columnFilterI=columnFilters[i];fkeys[i]=columnFilterI.get_columnKey();ftypes[i]=columnFilterI.get_columnType();fvalues[i]=columnFilterI.get_condition().get_value();frules[i]=columnFilterI.get_condition().get_rule();findexes[i]=columnFilterI.get_columnIndex();}}
return"fb:"+fkeys.toString()+";ft:"+ftypes.toString()+";fv:"+fvalues.toString()+";fr:"+frules.toString()+";fci:"+findexes.toString();},_apply_filters:function(addClientFilters)
{var columnFilterToAdd=null;if(addClientFilters&&this._clientColumnFilters&&this._clientColumnFilters.length>0)
columnFilterToAdd=this._clientColumnFilters;var eventArgs=this.__raiseClientEvent("DataFiltering",$IG.CancelApplyFiltersEventArgs,[this,this._columnFilters,columnFilterToAdd]);if(eventArgs==null||!eventArgs.get_cancel()){if(columnFilterToAdd){ig.smartSubmit(this._grid.getId(),"filter",this._generate_args(columnFilterToAdd),this._grid.getId(),null);}else{ig.smartSubmit(this._grid.getId(),"filter","fb:"+this.columnFilter.get_columnKey()+";ft:"+this.columnFilter.get_columnType()+";fv:"+columnFilter.get_condition().get_value()+";fr:"+this.columnFilter.get_condition().get_rule()+";fci:"+this.columnFilter.get_columnIndex(),this._grid.getId(),null);}}},getCellFromElem:function(elem)
{var obj=$util.resolveMarkedElement(elem);if(obj!=null)
{elem=obj[0];var type=elem.getAttribute("type");if(type=="cell")
{var row=this.getRowFromCellElem(elem);return row.get_cell(obj[1]);}}
return null;},getRowFromCellElem:function(elem)
{var parentRow=elem.parentNode;while(parentRow&&(parentRow.tagName!="TR"||!parentRow.id))
parentRow=parentRow.parentNode;var obj=null;if(parentRow)
obj=$util.resolveMarkedElement(parentRow);if(obj)
{var elem=obj[0];var type=elem.tagName;if(type=="TR")
{var index=parseInt(obj[1]);if(!isNaN(index))
{var auxRow=elem.getAttribute("auxRow");if(auxRow!==null)
return this._grid._get_auxRows(auxRow)[index];else
return this._grid.get_rows().get_row(index);}
else
{if(elem._object!=null)
{return elem._object;}}}}
return null;},_onClickHandler:function(evnt)
{if(this.get_cellInEditMode()&&this.get_cellInEditMode().get_row()===this._row)
{this.exitEditMode(true);return;}
var element;var td;if($util.IsSafari&&evnt.target.getAttribute("mkr")=="befapp"||evnt.target.getAttribute("mkr")=="aftapp")
{element=evnt.target;td=evnt.target.parentNode.parentNode;}
else
{element=evnt.target.firstChild;td=evnt.target.parentNode;}
if(element&&element.nodeName=="IMG"&&(element.getAttribute("mkr")=="befapp"||element.getAttribute("mkr")=="aftapp"))
{var cell=new IgGridFilterCell(td);cell.set_row(this._row);if(!cell)
{var cellIndex=this._grid._gridUtil.getCellIndexFromElem(td);var filterRow=this._row;cell=filterRow.get_cell(cellIndex);}
if(cell!=null)
{if($util.IsSafari&&this._visibleDropdownCell)
{if(cell.elm!=this._visibleDropdownCell.elm)
this._closeRuleDropdown(this._visibleDropdownCell);}
if(cell._dropDownBehaviour.get_visible())
{this._closeRuleDropdown(cell);}
else
{if(this._visibleDropdownCell&&!this._visibleDropdownCell._dropDownBehaviour.get_visible())
this._visibleDropdownCell=null;if(!this._visibleDropdownCell)
{var args=this.__raiseClientEvent("FilterDropdownDisplaying",$IG.CancelFilterEventArgs,[cell]);if(args==null||!args.get_cancel()){cell._dropDownBehaviour.set_visible(true);this.__raiseClientEvent("FilterDropdownDisplayed",$IG.FilterEventArgs,[cell]);}
var cancelledOrHidden=cell._dropDownBehaviour.get_enableAnimations()?!cell._dropDownBehaviour.get_isAnimating():!cell._dropDownBehaviour.get_visible();if(!cancelledOrHidden)
{this._currentRuleItem=null;if($util.IsFireFox)
cell._dropDownBehaviour.get_targetContainer().firstChild.firstChild.focus();else
cell._dropDownBehaviour.get_targetContainer().firstChild.focus();this._visibleDropdownCell=cell;}}}}}},_onDblclickHandler:function(evnt)
{if(evnt.target.firstChild&&evnt.target.nodeName=="SPAN"&&(evnt.target.parentNode.firstChild.getAttribute("mkr")=="befapp"||evnt.target.parentNode.firstChild.getAttribute("mkr")=="aftapp")){var cell=new IgGridFilterCell(evnt.target.parentNode);cell.set_row(this);this.enterEditMode(cell);}},_closeRuleDropdown:function(cell,newRule)
{var args=this.__raiseClientEvent("FilterDropdownHiding",$IG.CancelFilterEventArgs,[cell,null,newRule]);if(args==null||!args.get_cancel()){cell._dropDownBehaviour.set_visible(false);this.__raiseClientEvent("FilterDropdownHidden",$IG.FilterEventArgs,[cell,null,newRule]);}
var cancelledOrHidden=cell._dropDownBehaviour.get_enableAnimations()?!cell._dropDownBehaviour.get_isAnimating():cell._dropDownBehaviour.get_visible();if(!cancelledOrHidden)
{var item=this._getSelectedRuleElement(cell._dropDownBehaviour.get_targetContainer());if(item!=null)
item.className=this._ruleDDItemCss;this._visibleDropdownCell=null;if(this._currentRuleItem&&this._currentRuleItem.className)
this._currentRuleItem.className=this._currentRuleItem.className.replace(" "+this._ruleDDHoverItemCss,"");}
return cancelledOrHidden;},_getRuleElement:function(rule,dropdownDiv)
{var lu=dropdownDiv.firstChild;if(lu&&lu.childNodes&&lu.childNodes.length>0)
{for(var i=0;i<lu.childNodes.length;i++)
{if(lu.childNodes[i].getAttribute("val")==rule)
{return lu.childNodes[i];}}}
return null;},_getSelectedRuleElement:function(dropdownDiv)
{var lu=dropdownDiv.firstChild;if(lu&&lu.childNodes&&lu.childNodes.length>0)
{for(var i=0;i<lu.childNodes.length;i++)
{if(lu.childNodes[i].className==this._ruleDDSelectedItemCss)
{return lu.childNodes[i];}}}
return null;},_onSelectRule:function(evnt)
{if(evnt.button!=0&&evnt.keyCode!=Sys.UI.Key.enter)
return;var rule=parseInt(evnt.target.getAttribute("val"));var index=this._visibleDropdownCell.elm.getAttribute("adr");var column=this._visibleDropdownCell._column;var key=column._key;var type=column.get_type();var columnFilter=this.get_columnFilterFromKey(key);columnFilter=columnFilter?columnFilter:this._get_clientColumnFilterFromKey(key);if(!columnFilter)
{columnFilter=this.create_columnFilter(key);this._clientColumnFilters[this._clientColumnFilters.length]=columnFilter;}
var oldRule=columnFilter.get_condition().get_rule();columnFilter.get_condition().set_rule(rule);this._clientColumnFilters[this._clientColumnFilters.length]=columnFilter;if(type!="boolean"&&rule==0)
columnFilter.get_condition().set_value("");var allowEdit=this._allowEdit(this._visibleDropdownCell);if((type=="boolean"&&oldRule!=rule)||(type=="date"&&!allowEdit&&oldRule!=rule)||(rule==0&&oldRule!=rule))
{if(!this._closeRuleDropdown(this._visibleDropdownCell,rule))
{this._apply_filters(true);}}
else if(allowEdit)
{var cell=this._visibleDropdownCell;if(!this._closeRuleDropdown(this._visibleDropdownCell,rule)&&rule!=0)
{this.enterEditMode(cell);}}},_get_clientColumnFilterFromKey:function(columnKey)
{for(var i=0;i<this._clientColumnFilters.length;i++)
{if(this._clientColumnFilters[i].get_columnKey()===columnKey)
return this._clientColumnFilters[i];}
return null;},_onMouseOverRule:function(evnt)
{if(evnt.target.tagName==="LI")
{if(this._currentRuleItem&&this._currentRuleItem.className)
this._currentRuleItem.className=this._currentRuleItem.className.replace(" "+this._ruleDDHoverItemCss,"");evnt.target.className=evnt.target.className+" "+this._ruleDDHoverItemCss;this._currentRuleItem=evnt.target;}},_onMouseOutRule:function(evnt)
{if(evnt.target.tagName==="LI")
{evnt.target.className=evnt.target.className.replace(" "+this._ruleDDHoverItemCss,"");if(this._currentRuleItem&&this._currentRuleItem!=evnt.target)
this._currentRuleItem.className=this._currentRuleItem.className.replace(" "+this._ruleDDHoverItemCss,"");this._currentRuleItem=null;}},_onMouseDown:function(evnt)
{this._closeDropdownOnscroll(evnt);},_onMouseUp:function(evnt)
{this._closeDropdownOnscroll(evnt);},_onGridResize:function(evnt)
{if(this._visibleDropdownCell!=null)
this._closeRuleDropdown(this._visibleDropdownCell);},_onKeyDown2:function(evnt)
{if((this._visibleDropdownCell!=null)&&(evnt.keyCode==Sys.UI.Key.left||evnt.keyCode==Sys.UI.Key.right||evnt.keyCode==Sys.UI.Key.up||evnt.keyCode==Sys.UI.Key.down||evnt.keyCode==Sys.UI.Key.pageUp||evnt.keyCode==Sys.UI.Key.pageDown))
{this._closeRuleDropdown(this._visibleDropdownCell);}},_onFilterKeyDown:function(evnt)
{if(evnt.keyCode==Sys.UI.Key.enter)
{this._onClickHandler(evnt);$util.cancelEvent(evnt);if($util.IsOpera)
this._OperaCancelEvent=true;}
if(evnt.target.nodeName=="BUTTON")
{if(evnt.keyCode==Sys.UI.Key.enter)
{this._onClickHandler(evnt);$util.cancelEvent(evnt);if($util.IsOpera)
this._OperaCancelEvent=true;}
else if(evnt.keyCode==Sys.UI.Key.tab&&evnt.shiftKey)
{if($util.IsIE||$util.IsOpera)
this._grid._gridUtil.scrollCellIntoViewIE(activeCell);else
activeCell._element.focus();$util.cancelEvent(evnt);if($util.IsOpera)
this._OperaCancelEvent=true;}}else if(evnt.target.nodeName=="INPUT"&&evnt.keyCode==Sys.UI.Key.esc){this.exitEditMode(false);}},_onKeyDownRule:function(evnt)
{var items=evnt.target.tagName=="LI"?evnt.target.parentNode:evnt.target;if(evnt.keyCode==Sys.UI.Key.up)
{var nextItem=(!this._currentRuleItem)?items.firstChild:this._currentRuleItem.previousSibling;if(!nextItem)
nextItem=items.lastChild;if(this._currentRuleItem&&this._currentRuleItem.className)
this._currentRuleItem.className=this._currentRuleItem.className.replace(" "+this._ruleDDHoverItemCss,"");nextItem.focus();nextItem.className=nextItem.className+" "+this._ruleDDHoverItemCss;this._currentRuleItem=nextItem;$util.cancelEvent(evnt);if($util.IsOpera)
this._OperaCancelEvent=true;}
else if(evnt.keyCode==Sys.UI.Key.down)
{var nextItem=(!this._currentRuleItem)?items.firstChild:this._currentRuleItem.nextSibling;if(!nextItem)
nextItem=items.firstChild;if(this._currentRuleItem&&this._currentRuleItem.className)
this._currentRuleItem.className=this._currentRuleItem.className.replace(" "+this._ruleDDHoverItemCss,"");nextItem.focus();nextItem.className=nextItem.className+" "+this._ruleDDHoverItemCss;this._currentRuleItem=nextItem;$util.cancelEvent(evnt);if($util.IsOpera)
this._OperaCancelEvent=true;}
else if(evnt.keyCode==Sys.UI.Key.enter)
{$util.cancelEvent(evnt);if($util.IsOpera)
this._OperaCancelEvent=true;if(this._currentRuleItem)
{evnt.target=this._currentRuleItem;this._onSelectRule(evnt);}
else
{var button=this._visibleDropdownCell._element.firstChild;this._closeRuleDropdown(this._visibleDropdownCell);button.focus();}}
else if(evnt.keyCode==Sys.UI.Key.esc)
{$util.cancelEvent(evnt);if($util.IsOpera)
this._OperaCancelEvent=true;var button=this._visibleDropdownCell._element.firstChild;this._closeRuleDropdown(this._visibleDropdownCell);button.focus();}},_onKeyPressRule:function(evnt)
{if(this._OperaCancelEvent)
{$util.cancelEvent(evnt);this._OperaCancelEvent=false;}},_onMousewheel:function(evnt)
{this._closeDropdownOnscroll(evnt);},_onColumnResize:function(evnt)
{if(this._visibleDropdownCell)
{this._closeRuleDropdown(this._visibleDropdownCell);}},_closeDropdownOnscroll:function(evnt)
{if(this._visibleDropdownCell!=null)
{if(evnt.target!=this._visibleDropdownCell._dropDownBehaviour.get_targetContainer()&&evnt.target!=this._visibleDropdownCell._dropDownBehaviour.get_sourceElement())
if(this._visibleDropdownCell._dropDownBehaviour.get_visible())
{if(!($util.IsSafari&&evnt.target.nodeName=="BUTTON"&&(evnt.target.getAttribute("mkr")=="befapp"||evnt.target.getAttribute("mkr")=="aftapp")))
this._closeRuleDropdown(this._visibleDropdownCell);return false;}}},_onActiveCellChanging:function(evnt)
{if(evnt.activeCell&&evnt.cell)
{var newRow=evnt.cell.get_row();if(newRow===this._row&&evnt.keyCode==Sys.UI.Key.tab&&this._row!==evnt.activeCell.get_row()&&evnt.cell===this._grid._gridUtil.getPrevCell(evnt.activeCell))
this.focusOnBtn=true;}},_onActiveCellChanged:function(evnt)
{if(this._visibleDropdownCell!=null)
this._closeDropdownOnscroll(evnt);else if(this.focusOnBtn)
{this.focusOnBtn=false;evnt.cell._element.childNodes[0].focus();}},_onSelectstartHandler:function(evnt)
{var currentElement=evnt.target;while(currentElement!=null&&currentElement.mkr!="filterRow")
currentElement=currentElement.parentNode;if(currentElement!=null&&currentElement.mkr=="filterRow")
$util.cancelEvent(evnt);},_initializeComplete:function()
{this._container=this._row._element;$IG.GridFiltering.callBaseMethod(this,"_initializeComplete");this._addEnteringEditEventListener(Function.createDelegate(this,this._enteringEditMode));this._addExitedEditEventListener(Function.createDelegate(this,this._exitedEditMode));this._registerAuxRow(this._row,(this.get_alignment()==$IG.FilteringAlignment.Top?$IG.GridAuxRows.Top:$IG.GridAuxRows.Bottom))
this._activation=this._parentCollection.getBehaviorFromInterface($IG.IActivationBehavior);var addedFilterKeys=this._get_clientOnlyValue("ack");if(addedFilterKeys&&this._clientEvents["ColumnFilterAdded"])
{var columnKeys=Sys.Serialization.JavaScriptSerializer.deserialize(addedFilterKeys);for(var i=0;i<columnKeys.length;i++)
{var columnFilter=this.get_columnFilterFromKey(columnKeys[i]);this.__raiseClientEvent('ColumnFilterAdded',new $IG.ColumnFilterAddedArgs,columnFilter);}}
if(this.get_filtered()&&this._clientEvents["DataFiltered"])
{this.__raiseClientEvent('DataFiltered',$IG.DataFilteredArgs,this.get_columnFilters());}},_get_editContainer:function()
{return this._row._element;},_onMouseOver:function(e)
{var target=e.target;target=this._owner._gridUtil._getGridCellFromElement(target);if(target&&(target.tagName=="TD"||target.tagName=="TH"))
{var row=target.parentNode;var adr=row.getAttribute("adr");if(adr===null&&row.id.indexOf("adr")>=0)
{$util._initAttr(row);adr=row.getAttribute("adr");}
if(adr!==null&&row.getAttribute("type")==null)
{var index=parseInt(adr,10);if(!this._rows[index])
this._rows[index]=this._create_item(index);}
if(!target.getAttribute("adr")&&target.tagName=="TD")
{var idx=this._owner._gridUtil.getCellIndexFromElem(target);target.setAttribute("idx",idx);target.setAttribute("adr",this._owner._gridUtil._getColumnAdrFromVisibleIndex(idx));target.setAttribute("type","cell");}}}}
$IG.GridFiltering.registerClass('Infragistics.Web.UI.GridFiltering',$IG.GridBehavior,$IG.IFilteringBehavior);$IG.FilterRow=function(adr,element,props,owner,csm,filteringBehavior)
{$IG.FilterRow.initializeBase(this,[adr,element,props,owner,csm]);this._filteringBehavior=filteringBehavior;this._owner=owner;var row=document.getElementById(owner.elm.id+"_fheaders_sr");if(!row){row=document.getElementById(owner.elm.id+"_fheaders");}
this._filterCells=[];if(!ig.isNull(row)){var childrenCount=row.childNodes.length;for(var i=0;i<childrenCount;i++)
{var child=row.childNodes[i];this._filterCells[i]=this._init_item(child);}}}
$IG.FilterRow.prototype={_init_item:function(td)
{var cell=new IgGridFilterCell(td);cell.set_row(this);if(!cell.elm.childNodes[0]){return;}
cell.elm._dropDownBehaviour=new $IG.DropDownBehavior(cell.elm.childNodes[0],false);var columnType=cell.elm.getAttribute("ftype");cell.elm._column=new IgGridFilterColumn(this._filteringBehavior._grid,cell.elm.getAttribute("index"),cell.elm.getAttribute("key"),columnType);var elmClone;if(columnType=="number")
elmClone=$get(this._owner.elm.id+"_NumericRuleDropDown").cloneNode(true);else if(columnType=="date")
elmClone=document.getElementById(this._owner.elm.id+"_DateTimeRuleDropDown").cloneNode(true);else if(columnType=="boolean")
elmClone=document.getElementById(this._owner.elm.id+"_BooleanRuleDropDown").cloneNode(true);else if(columnType=="string")
elmClone=document.getElementById(this._owner.elm.id+"_TextRuleDropDown").cloneNode(true);else
elmClone=document.getElementById(this._owner.elm.id+"_"+columnType+"RuleDropDown").cloneNode(true);$addHandler(elmClone.firstChild,'mousedown',this._filteringBehavior._onSelectRuleHandler);$addHandler(elmClone.firstChild,'mouseover',this._filteringBehavior._onMouseOverRuleHandler);$addHandler(elmClone.firstChild,'mouseout',this._filteringBehavior._onMouseOutRuleHandler);$addHandler(elmClone.firstChild,'keydown',this._filteringBehavior._onKeyDownRuleHandler);cell.elm._dropDownBehaviour.set_targetContainer(elmClone);cell.elm._dropDownBehaviour.set_zIndex(this._filteringBehavior.get_filterRuleDropdownZIndex());cell.elm._dropDownBehaviour.set_animationDurationMs(this._filteringBehavior.get_animationDurationMs());cell.elm._dropDownBehaviour.set_enableAnimations(this._filteringBehavior.get_animationEnabled());cell.elm._dropDownBehaviour.set_animationType(this._filteringBehavior.get_animationType());cell.elm._dropDownBehaviour.set_visibleOnBlur(false);if(this._filteringBehavior.get_alignment()==$IG.FilteringAlignment.Top)
cell.elm._dropDownBehaviour.set_position($IG.DropDownPopupPosition.Default);else
cell.elm._dropDownBehaviour.set_position($IG.DropDownPopupPosition.TopLeft);elmClone=null;cell.elm._dropDownBehaviour.init();return cell;},_create_item:function(adr,index)
{var cell=new IgGridFilterCell(td);var filterSettings=this._filteringBehavior.get_columnSettingFromKey(cell._column._key);if((filterSettings==null||filterSettings.get_enabled())&&!cell._column.get_isTemplated())
{cell.__set_overrideCellUpdate(true);cell._dropDownBehaviour=new $IG.DropDownBehavior(cell.get_element().childNodes[0],false);this._filteringBehavior._dropDownBehaviors[this._filteringBehavior._dropDownBehaviorsCount++]=cell._dropDownBehaviour;var columnType=cell._column.get_type();var elmClone;if(columnType=="number")
elmClone=$get(cell._owner._id+"_NumericRuleDropDown").cloneNode(true);else if(columnType=="date")
elmClone=$get(cell._owner._id+"_DateTimeRuleDropDown").cloneNode(true);else if(columnType=="boolean")
elmClone=$get(cell._owner._id+"_BooleanRuleDropDown").cloneNode(true);else
elmClone=document.getElementById(cell._owner._id+"_TextRuleDropDown").cloneNode(true);if(!$util.IsIE)
{$addHandler(elmClone.firstChild,'mousedown',this._filteringBehavior._onSelectRuleHandler);$addHandler(elmClone.firstChild,'mouseover',this._filteringBehavior._onMouseOverRuleHandler);$addHandler(elmClone.firstChild,'mouseout',this._filteringBehavior._onMouseOutRuleHandler);$addHandler(elmClone,'keydown',this._filteringBehavior._onKeyDownRuleHandler);}
cell._dropDownBehaviour.set_targetContainer(elmClone);cell._dropDownBehaviour.set_zIndex(this._filteringBehavior.get_filterRuleDropdownZIndex());cell._dropDownBehaviour.set_animationDurationMs(this._filteringBehavior.get_animationDurationMs());cell._dropDownBehaviour.set_enableAnimations(this._filteringBehavior.get_animationEnabled());cell._dropDownBehaviour.set_animationType(this._filteringBehavior.get_animationType());cell._dropDownBehaviour.set_visibleOnBlur(false);if(this._filteringBehavior.get_alignment()==$IG.FilteringAlignment.Top)
cell._dropDownBehaviour.set_position($IG.DropDownPopupPosition.Default);else
cell._dropDownBehaviour.set_position($IG.DropDownPopupPosition.TopLeft);elmClone=null;cell._dropDownBehaviour.init();}
return cell;},get_cellByColumn:function(column){return this._filterCells[column.index];},dispose:function()
{this._filteringBehavior=null;$IG.FilterRow.callBaseMethod(this,"dispose");}}
$IG.FilterRow.registerClass('Infragistics.Web.UI.FilterRow',$IG.ObjectBase);$IG.ColumnFilterSettings=function(adr,element,props,owner,csm)
{$IG.ColumnFilterSettings.initializeBase(this,[adr,element,props,owner,csm]);}
$IG.ColumnFilterSettings.prototype={get_enabled:function()
{return this._get_clientOnlyValue("fse");}}
$IG.ColumnFilterSettings.registerClass('Infragistics.Web.UI.ColumnFilterSettings',$IG.ObjectBase);$IG.ColumnFilter=function(obj,element,props,control)
{var csm=new $IG.ObjectClientStateManager(props[0]);$IG.ColumnFilter.initializeBase(this,[obj,element,props,control,csm]);this._gridFiltering=this._owner;this._index=0;}
$IG.ColumnFilter.prototype={get_columnIndex:function(){return this._index;},set_columnIndex:function(index){this._index=index;},get_columnKey:function()
{return this._props[3];},get_columnType:function()
{return this._props[0];},get_condition:function()
{if(this._condition==null)
{if(this._props&&this._props[1]){this._condition=new $IG.ClientCondition(this._props[1],this._props[2],this._columnType,this);}else{this._condition=new $IG.ClientCondition($IG.FilteringNodeObjectProps.Rule[1],"",this._columnType,this);}}
return this._condition;},_createObjects:function(objectManager)
{this._objectManager=objectManager;},dispose:function()
{this._objectManager=null;$IG.ColumnFilter.callBaseMethod(this,"dispose");}}
$IG.ColumnFilter.registerClass('Infragistics.Web.UI.ColumnFilter',$IG.ObjectBase);$IG.FilteringNodeObject=function(obj,element,props,control)
{var csm=new $IG.ObjectClientStateManager(props[0]);$IG.FilteringNodeObject.initializeBase(this,[obj,element,props,control,csm]);this._columnFilter=this._owner;this._gridFiltering=this._columnFilter._owner;this._grid=this._gridFiltering._owner;}
$IG.FilteringNodeObject.prototype={get_rule:function()
{return this._get_value($IG.FilteringNodeObjectProps.Rule);},set_rule:function(rule)
{if(this._gridFiltering._grid&&this._gridFiltering){this._set_value($IG.FilteringNodeObjectProps.Rule,rule);var cell=this._get_filterCell();var ruleElm=this._gridFiltering._getRuleElement(rule,cell.elm._dropDownBehaviour._targetContainer);if(ruleElm!=null)
{cell.get_element().childNodes[0].childNodes[0].alt=ruleElm.innerHTML;cell.get_element().childNodes[0].childNodes[0].title=ruleElm.innerHTML;cell.get_element().childNodes[0].title=ruleElm.innerHTML;}}},get_value:function()
{return this._get_value($IG.FilteringNodeObjectProps.Value);},set_value:function(value)
{var columnType=this._columnFilter.get_columnType();if(columnType=="boolean")
return;var column=this._gridFiltering.get_columnFromKey(this._columnFilter.get_columnKey());var cell=this._get_filterCell();var valueToSave=cell.__parseValue(value);var text=column._formatValue(valueToSave);if(columnType=="number")
{var parsedValue=parseFloat(value);if(isNaN(parsedValue))
{valueToSave="";text="";}}
else if(columnType=="date")
{if(value==null)
{valueToSave="";text="";}
else if(typeof(value)!="object"||typeof(value.getMonth)=="undefined")
{var parsedValue=Date.parseLocale(value);if(isNaN(parsedValue)||parsedValue==null||parsedValue==undefined)
{valueToSave="";text="";}}}
this._set_value($IG.FilteringNodeObjectProps.Value,valueToSave);cell.get_element().childNodes[1].innerHTML=$util.htmlEscapeCharacters(text);},_get_filterCell:function()
{var column=this._gridFiltering.get_columnFromKey(this._columnFilter.get_columnKey());var cell=this._gridFiltering._row.get_cellByColumn(column);return cell;},dispose:function()
{this._columnFilter=null;this._gridFiltering=null;this._grid=null;$IG.FilteringNodeObject.callBaseMethod(this,"dispose");}}
$IG.FilteringNodeObject.registerClass('Infragistics.Web.UI.FilteringNodeObject',$IG.ObjectBase);$IG.ClientColumnFilter=function(columnKey,columnType,gridFiltering)
{this._columnKey=columnKey;this._columnType=columnType;this._gridFiltering=gridFiltering;this._index=null;this._condition=new $IG.ClientCondition($IG.FilteringNodeObjectProps.Rule[1],"",this._columnType,this);}
$IG.ClientColumnFilter.prototype={get_columnKey:function()
{return this._columnKey;},get_condition:function()
{return this._condition;},get_columnType:function()
{return this._columnType;},get_columnIndex:function(){return this._index;},set_columnIndex:function(index){this._index=index;},dispose:function()
{if(this._condition)
this._condition.dispose();this._condition=null;this._columnKey=null;this._columnType=null;this._gridFiltering=null;this._index=null;}}
$IG.ClientColumnFilter.registerClass('Infragistics.Web.UI.ClientColumnFilter');$IG.ClientCondition=function(rule,value,type,columnFilter)
{this._type=type;this._rule=rule;this._value=value;this._colKey=columnFilter.get_columnKey();this._gridFiltering=columnFilter._gridFiltering;}
$IG.ClientCondition.prototype={get_rule:function()
{return this._rule;},set_rule:function(rule)
{this._rule=rule;var grid=this._grid;if(this._gridFiltering._grid&&this._gridFiltering)
{var cell=this._get_filterCell();var ruleElm=this._gridFiltering._getRuleElement(rule,cell.elm._dropDownBehaviour._targetContainer);if(ruleElm!=null)
{var elm=cell.get_element().childNodes[0].childNodes[0];elm.alt=ruleElm.innerHTML;elm.title=ruleElm.innerHTML;cell.get_element().childNodes[0].title=ruleElm.innerHTML;}}
cell=null;grid=null;ruleElm=null;},get_value:function()
{return this._value;},set_value:function(value)
{if(this._type=="boolean")
return;var column=this._gridFiltering.get_columnFromKey(this._colKey);var cell=this._get_filterCell();var valueToSave=cell.__parseValue(value);var text=column._formatValue(valueToSave);if(this._type=="number")
{var parsedValue=parseFloat(value);if(isNaN(parsedValue))
{valueToSave="";text="";}}
else if(this._type=="date")
{if(value==null)
{valueToSave="";text="";}
else if(typeof(value)!="object"||typeof(value.getMonth)=="undefined")
{var parsedValue=Date.parseLocale(value);if(isNaN(parsedValue)||parsedValue==null||parsedValue==undefined)
{valueToSave="";text="";}}}
this._value=valueToSave;cell.get_element().childNodes[1].innerHTML=$util.htmlEscapeCharacters(text);cell=null;column=null;},dispose:function()
{},_get_filterCell:function()
{var column=this._gridFiltering.get_columnFromKey(this._colKey);var cell=this._gridFiltering._row.get_cellByColumn(column);column=null;return cell;}}
$IG.ClientCondition.registerClass('Infragistics.Web.UI.ClientCondition');$IG.CancelBehaviorEventArgs=function(behavior)
{$IG.CancelBehaviorEventArgs.initializeBase(this);}
$IG.CancelBehaviorEventArgs.prototype={_context:{}}
$IG.CancelBehaviorEventArgs.registerClass('Infragistics.Web.UI.CancelBehaviorEventArgs',$IG.CancelEventArgs);$IG.FilteringAction=function(type,ownerName,object,value,tag)
{$IG.FilteringAction.initializeBase(this,[type,ownerName,object,value,tag]);}
$IG.FilteringAction.prototype={}
$IG.FilteringAction.registerClass('Infragistics.Web.UI.FilteringAction',$IG.ObjectBase);$IG.ColumnFilterAddedArgs=function(columnFilter)
{this._columnFilter=columnFilter;$IG.ColumnFilterAddedArgs.initializeBase(this);}
$IG.ColumnFilterAddedArgs.prototype={get_columnFilter:function()
{return this._columnFilter;}}
$IG.ColumnFilterAddedArgs.registerClass('Infragistics.Web.UI.ColumnFilterAddedArgs',$IG.EventArgs);$IG.DataFilteredArgs=function(columnFilters)
{this._columnFilters=columnFilters;$IG.DataFilteredArgs.initializeBase(this);}
$IG.DataFilteredArgs.prototype={get_columnFilters:function()
{return this._columnFilters;}}
$IG.DataFilteredArgs.registerClass('Infragistics.Web.UI.DataFilteredArgs',$IG.EventArgs);$IG.CancelApplyFiltersEventArgs=function(args)
{var behavior=args[0];var columnFilters=args[1];var columnFilterToAdd=args[2];$IG.CancelApplyFiltersEventArgs.initializeBase(this,[behavior]);this._columnFilters=[];if(columnFilters)
{for(var i=0;i<columnFilters.length;i++)
this._columnFilters[i]=columnFilters[i];}
if(columnFilterToAdd)
{for(var i=0;i<columnFilterToAdd.length;i++)
this._columnFilters[this._columnFilters.length]=columnFilterToAdd[i];}}
$IG.CancelApplyFiltersEventArgs.prototype={get_columnFilters:function()
{return this._columnFilters;},_dispose:function()
{if(this._columnFilters&&this._columnFilters.length)
{for(var i=0;i<this._columnFilters.length;i++)
{if(this._columnFilters[i])
this._columnFilters[i].dispose();this._columnFilters[i]=null;}}
this._columnFilters=null;}}
$IG.CancelApplyFiltersEventArgs.registerClass('Infragistics.Web.UI.CancelApplyFiltersEventArgs',$IG.CancelBehaviorEventArgs);$IG.ColumnFilterAddingRemovingEventArgs=function(behavior)
{$IG.ColumnFilterAddingRemovingEventArgs.initializeBase(this,[behavior]);this._props[1]=2;}
$IG.ColumnFilterAddingRemovingEventArgs.prototype={}
$IG.ColumnFilterAddingRemovingEventArgs.registerClass('Infragistics.Web.UI.ColumnFilterAddingRemovingEventArgs',$IG.CancelBehaviorEventArgs);$IG.FilteringAlignment=function()
{}
$IG.FilteringAlignment.prototype={Top:0,Bottom:1};$IG.FilteringAlignment.registerEnum("Infragistics.Web.UI.FilteringAlignment");$IG.FilteringVisibility=function()
{}
$IG.FilteringVisibility.prototype={Visible:0,Hidden:1};$IG.FilteringVisibility.registerEnum("Infragistics.Web.UI.FilteringVisibility");$IG.TextFilterRules=function()
{}
$IG.TextFilterRules.prototype={All:0,Equals:1,DoesNotEqual:2,BeginsWith:3,EndsWith:4,Contains:5,DoesNotContain:6};$IG.TextFilterRules.registerEnum("Infragistics.Web.UI.TextFilterRules");$IG.NumericFilterRules=function()
{}
$IG.NumericFilterRules.prototype={All:0,Equals:1,DoesNotEqual:2,GreaterThan:3,GreaterThanOrEqualTo:4,LessThan:5,LessThanOrEqualTo:6};$IG.NumericFilterRules.registerEnum("Infragistics.Web.UI.NumericFilterRules");$IG.DateTimeFilterRules=function()
{}
$IG.DateTimeFilterRules.prototype={All:0,Equals:1,Before:2,After:3,Tomorrow:4,Today:5,Yesterday:6,NextWeek:7,ThisWeek:8,LastWeek:9,NextMonth:10,ThisMonth:11,LastMonth:12,NextQuarter:13,ThisQuarter:14,LastQuarter:15,NextYear:16,ThisYear:17,LastYear:18,YearToDate:19};$IG.DateTimeFilterRules.registerEnum("Infragistics.Web.UI.DateTimeFilterRules");$IG.BooleanFilterRules=function()
{}
$IG.BooleanFilterRules.prototype={All:0,True:1,False:2};$IG.BooleanFilterRules.registerEnum("Infragistics.Web.UI.BooleanFilterRules");$IG.GridFilteringProps=new function()
{this.Visibility=[$IG.GridBehaviorProps.Count+0,$IG.FilteringVisibility.Visible];this.RuleDropdownZIndex=[$IG.GridBehaviorProps.Count+1,100100];this.Count=$IG.GridBehaviorProps.Count+2;};$IG.ColumnFilterSettingsProps=new function()
{this.ColumnKey=[$IG.ObjectBaseProps.Count+0,""];this.Count=$IG.ObjectBaseProps.Count+1;};$IG.ColumnFilterProps=new function()
{this.ColumnKey=[$IG.ObjectBaseProps.Count+0,""];this.ColumnType=[$IG.ObjectBaseProps.Count+1,""];this.Count=$IG.ObjectBaseProps.Count+2;};$IG.FilteringNodeObjectProps=new function()
{this.Rule=[$IG.ObjectBaseProps.Count+0,0];this.Value=[$IG.ObjectBaseProps.Count+1];this.Count=$IG.ObjectBaseProps.Count+2;};$IG.GridFilterEditor=function(parentElement)
{$IG.GridFilterEditor.initializeBase(this,[parentElement]);}
$IG.GridFilterEditor.prototype={hideEditor:function()
{if(!this._shown)return;$removeHandler(this.input.elm,"blur",this._inputOnBlurHandler);$removeHandler(this.input.elm,"focus",this._inputOnFocusHandler);var cell=ig.getTargetUIElement(this.elm,this.TYPE_GRID_COLUMN_FILTER);if(!ig.isNull(cell)){cell.setStyle("visibility","visible");cell.setStyle("padding",this.cellPadding);this.elm.style.display="none";this.elm.parentNode.removeChild(this.elm);}
this._shown=false;},showEditor:function(top,left,width,height,cssClass,parent){var parent=new IgUIElement(parent);var elm=new IgUIElement(this.elm);var leftPadding=parent.getPadding("left");if(leftPadding>0){this.setStyle("margin-left",""+(-leftPadding)+"px");}
var topPadding=parent.getPadding("top");if(topPadding>0){this.setStyle("margin-top",""+(-topPadding)+"px");}
this.input.setStyle("left","0px");this.input.setStyle("right","0px");if(ig.isIE){this.input.setStyle("margin-top","-1px");}
elm.setStyle("visibility","visible");this.input.elm.className=cssClass;this.elm.style.display="inline";this.elm.style.whiteSpace="nowrap";elm.setSize(width,height);this.input.setSize(width,height);this.input.focus();$addHandler(this.input.elm,"blur",this._inputOnBlurHandler);$addHandler(this.input.elm,"focus",this._inputOnFocusHandler);this._shown=true;}}
$IG.GridFilterEditor.registerClass('Infragistics.Web.UI.GridFilterEditor',Infragistics.Web.UI.GridInternalEditor);$IG.FilterEventArgs=function(params){$IG.FilterEventArgs.initializeBase(this);this._cell=params[0];this._new_text=params[1];this._new_rule=params[2];this.filtering=this._cell.getGrid().get_behaviors().getBehaviorFromInterface($IG.IFilteringBehavior);}
$IG.FilterEventArgs.prototype={get_cell:function(){return this._cell;},get_filterType:function(){return this._cell.get_column()._type;},get_filterKey:function(){return this._cell.get_column()._key;},get_filterRule:function(){var filter=this.filtering.get_columnFilterFromKey(this.get_filterKey());return(filter?filter.get_condition().get_rule():0);},get_filterText:function(){var filter=this.filtering.get_columnFilterFromKey(this.get_filterKey());return(filter?filter.get_condition().get_value():"");},get_newFilterRule:function(){return this._new_rule;},get_newFilterText:function(){return this._new_text;}}
$IG.FilterEventArgs.registerClass('Infragistics.Web.UI.FilterEventArgs',$IG.EventArgs);$IG.CancelFilterEventArgs=function(params){$IG.CancelFilterEventArgs.initializeBase(this);this._cell=params[0];this._new_text=params[1];this._new_rule=params[2];this.filtering=this._cell.getGrid().get_behaviors().getBehaviorFromInterface($IG.IFilteringBehavior);}
$IG.CancelFilterEventArgs.prototype={get_cell:function(){return this._cell;},get_filterType:function(){return this._cell.get_column()._type;},get_filterKey:function(){return this._cell.get_column()._key;},get_filterRule:function(){var filter=this.filtering.get_columnFilterFromKey(this.get_filterKey());return(filter?filter.get_condition().get_rule():0);},get_filterText:function(){var filter=this.filtering.get_columnFilterFromKey(this.get_filterKey());return(filter?filter.get_condition().get_value():"");},get_newFilterRule:function(){return this._new_rule;},get_newFilterText:function(){return this._new_text;}}
$IG.CancelFilterEventArgs.registerClass('Infragistics.Web.UI.CancelFilterEventArgs',$IG.CancelEventArgs);