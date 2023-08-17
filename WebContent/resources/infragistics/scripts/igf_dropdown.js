// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace('Infragistics.Web.UI');$IG.WebDropDown=function(element)
{$IG.WebDropDown.initializeBase(this,[element]);}
$IG.WebDropDown.prototype={_thisType:'dropDown',initialize:function()
{$IG.WebDropDown.callBaseMethod(this,'initialize');this.behavior=new $IG.DropDownBehavior(this._elements["Target"],true);this.behavior.set_targetContainer(this._elements["DropDown"]);this.behavior.set_position(this.get_dropDownOrientation());this.behavior.set_animationDurationMs(this.get_dropDownAnimationDuration());var dropDownList=this._elements["List"];var selectDelegate=Function.createDelegate(this,this._select);$addHandler(dropDownList,'mousedown',selectDelegate);var blurDelegate=Function.createDelegate(this,this._onBlurHandler);$addHandler(this._elements["Input"],'blur',blurDelegate);var focusDelegate=Function.createDelegate(this,this._onFocusHandler);$addHandler(this._elements["Input"],'focus',focusDelegate);var mouseOver=Function.createDelegate(this,this._mouseOverForBlur);$addHandler(this._elements["DropDown"],'mouseover',mouseOver);$addHandler(this._elements["Button"],'mouseover',mouseOver);$addHandler(this._elements["ButtonImage"],'mouseover',mouseOver);var mouseOut=Function.createDelegate(this,this._mouseOutForBlur);$addHandler(this._elements["DropDown"],'mouseout',mouseOut);$addHandler(this._elements["Button"],'mouseout',mouseOut);$addHandler(this._elements["ButtonImage"],'mouseout',mouseOut);if(this.get_enableLoadOnDemand())
{var loadOnDemandDelegate=Function.createDelegate(this,this._scrollingLoadOnDemand);$addHandler(this._elements["DropDownContents"],'scroll',loadOnDemandDelegate);}
if(this.get_enablePaging())
{var pagerPrevDelegate=Function.createDelegate(this,this._onPagerPrevResults);$addHandler(this._elements["PagerPrevLink"],'mousedown',pagerPrevDelegate);var pagerNextDelegate=Function.createDelegate(this,this._onPagerNextResults);$addHandler(this._elements["PagerNextLink"],'mousedown',pagerNextDelegate);}
var navDelegate=Function.createDelegate(this,this._navigateItems);$addHandler(dropDownList,'keydown',navDelegate);var mouseOverDelegate=Function.createDelegate(this,this._onMouseoverHandler);$addHandler(dropDownList,'mouseover',mouseOverDelegate);this.behavior._setAnimationEndListener(this);this.behavior.init();this._elements["Input"].value=this.get_currentValue();this.set_hoverItemIndex(-1);this._adjustMaxHeight();},_addHandlers:function()
{$IG.WebDropDown.callBaseMethod(this,'_addHandlers');this._registerHandlers(["selectstart","keydown","keyup","mousedown","change","mouseover","keypress"]);},dispose:function()
{$clearHandlers(this.get_element());$IG.WebDropDown.callBaseMethod(this,'dispose');},_adjustMaxHeight:function()
{if(this.get_valueListMaxHeight()>0)
{if(this._elements["List"].offsetHeight>this.get_valueListMaxHeight())
{this._elements["DropDownContents"].style.height=this.get_valueListMaxHeight()+'px';}else{this._elements["DropDownContents"].style.height='';}}},loadItems:function(text)
{this._setLoadItemsText(text);var cbo=this._callbackManager.createCallbackObject();cbo.serverContext.type="itemsRequested";cbo.serverContext.props=Sys.Serialization.JavaScriptSerializer.serialize(this._clientStateManager.get_transactionList());this._callbackManager.execute(cbo,true);},openDropDown:function()
{if(this.get_displayMode()!=$IG.DropDownDisplayMode.ReadOnly)
{var args=this._raiseClientEvent('DropDownOpening','DropDownContainer',null,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(true);this._adjustMaxHeight();this._raiseClientEvent('DropDownOpened','DropDownContainer',null,null);}}},closeDropDown:function()
{var args=this._raiseClientEvent('DropDownClosing','DropDownContainer',null,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(false);this._raiseClientEvent('DropDownClosed','DropDownContainer',null,null);}},_createItem:function(element,adr)
{this._itemCollection._addObject($IG.DropDownItem,element,adr);},_setLoadItemsText:function(text)
{this._set_value($IG.DropDownProps.LoadItemsText,text);},_setupCollections:function()
{this._itemCollection=this._collectionsManager.register_collection(0,$IG.DropDownItemCollection);this._collectionsManager.registerUIBehaviors(this._itemCollection);},get_items:function()
{return this._itemCollection;},get_selectedItem:function()
{if(this.get_selectedItemIndex()<0)
return null;else
return this.get_items()._items[selectedItemIndex];},get_selectedItems:function()
{if(this.get_enableMultipleSelection())
{var results=[];var count=0;for(i=0;i<this.get_items()._items.length;i++)
{if(this.get_items()._items[i].get_selected())
{results[count]=this.get_items()._items[i];count++;}}
return results;}else
{return this.get_selectedItem();}},set_valueBeforeFilter:function(val)
{this.__valueBeforeFilter=val;},get_valueBeforeFilter:function()
{return this.__valueBeforeFilter;},get_closeDropDownOnSelect:function()
{return this._get_value($IG.DropDownProps.CloseDropDownOnSelect);},set_closeDropDownOnSelect:function(closeDropDownOnSelect)
{return this._set_value($IG.DropDownProps.CloseDropDownOnSelect,closeDropDownOnSelect);},get_dropDownAnimationDuration:function()
{return this._get_value($IG.DropDownProps.DropDownAnimationDuration);},get_dropDownOrientation:function()
{return this._get_value($IG.DropDownProps.DropDownOrientation);},get_enableAutoCompleteFirstMatch:function()
{return this._get_value($IG.DropDownProps.EnableAutoCompleteFirstMatch);},set_enableAutoCompleteFirstMatch:function(autoComplete)
{return this._set_value($IG.DropDownProps.EnableAutoCompleteFirstMatch,autoComplete);},get_displayMode:function()
{return this._get_value($IG.DropDownProps.DisplayMode);},get_valueListMaxHeight:function()
{return this._get_value($IG.DropDownProps.ValueListMaxHeight);},get_offsetX:function()
{return this._get_value($IG.DropDownProps.OffsetX);},get_offsetY:function()
{return this._get_value($IG.DropDownProps.OffsetY);},get_enablePaging:function()
{return this._get_value($IG.DropDownProps.EnablePaging);},get_pageSize:function()
{return this._get_value($IG.DropDownProps.PageSize);},get_selectedItemIndex:function()
{return this._get_value($IG.DropDownProps.SelectedItemIndex);},set_selectedItemIndex:function(index)
{return this._set_value($IG.DropDownProps.SelectedItemIndex,index);},get_selectedItemIndices:function()
{return this._get_value($IG.DropDownProps.SelectedItemIndices);},set_selectedItemIndices:function(indices)
{return this._set_value($IG.DropDownProps.SelectedItemIndices,indices);},get_multiSelectValueDelimiter:function()
{return this._get_value($IG.DropDownProps.MultiSelectValueDelimiter);},get_enableCustomValueSelection:function()
{return this._get_value($IG.DropDownProps.EnableCustomValueSelection);},get_enableMultipleSelection:function()
{return this._get_value($IG.DropDownProps.EnableMultipleSelection);},get_persistCustomValues:function()
{return this._get_value($IG.DropDownProps.PersistCustomValues);},get_enableAutoFiltering:function()
{return this._get_value($IG.DropDownProps.EnableAutoFiltering);},get_autoFilterQueryType:function()
{return this._get_value($IG.DropDownProps.AutoFilterQueryType);},get_autoFilterResultSize:function()
{return this._get_value($IG.DropDownProps.AutoFilterResultSize);},get_autoFilterSortOrder:function()
{return this._get_value($IG.DropDownProps.AutoFilterSortOrder);},get_enableLoadOnDemand:function()
{return this._get_value($IG.DropDownProps.EnableLoadOnDemand);},get_enableClientFilteringOnly:function()
{return this._get_value($IG.DropDownProps.EnableClientFilteringOnly);},get_dropDownContainerWidth:function()
{return this._get_value($IG.DropDownProps.DropDownContainerWidth);},get_dropDownContainerHeight:function()
{return this._get_value($IG.DropDownProps.DropDownContainerHeight);},get_enableCaseSensitivity:function()
{return this._get_value($IG.DropDownProps.EnableCaseSensitivity);},get_showDropDownButton:function()
{return this._get_value($IG.DropDownProps.ShowDropDownButton);},get_dropDownValueDisplayType:function()
{return this._get_value($IG.DropDownProps.DropDownValueDisplayType);},get_inputFocusCssClass:function()
{return this._get_clientOnlyValue("dropDownInputFocusClass");},get_inputHoverCssClass:function()
{return this._get_clientOnlyValue("dropDownInputHoverClass");},get_inputCssClass:function()
{return this._get_clientOnlyValue("dropDownInputClass");},get_dropDownFocusCssClass:function()
{return this._get_clientOnlyValue("dropDownFocusClass");},get_dropDownHoverCssClass:function()
{return this._get_clientOnlyValue("dropDownHoverClass");},get_controlAreaHoverCssClass:function()
{return this._get_clientOnlyValue("controlAreaHoverClass");},get_buttonFocusCssClass:function()
{return this._get_clientOnlyValue("dropDownButtonFocusClass");},get_buttonCssClass:function()
{return this._get_clientOnlyValue("dropDownButtonClass");},get_controlAreaFocusCssClass:function()
{return this._get_clientOnlyValue("controlAreaFocusClass");},get_controlAreaCssClass:function()
{return this._get_clientOnlyValue("controlAreaClass");},get_currentValue:function()
{return this._get_value($IG.DropDownProps.CurrentValue);},set_currentValue:function(val)
{this._elements["Input"].value=val;this._set_value($IG.DropDownProps.CurrentValue,val);},set_previousValue:function(val)
{this._previousValue=val;},get_previousValue:function()
{return this._previousValue;},get_hoverItemIndex:function()
{return this.__hoverItemIndex;},set_hoverItemIndex:function(index)
{this.__hoverItemIndex=index;},_selectItem:function(item,val)
{this.__selectItem(item,val,true);},__selectItem:function(item,val,fireEvent)
{if(val)
{if(fireEvent&&!this.__initializing)
this._raiseClientEvent('ItemSelected','DropDownControl',null,null,item);}},__getNearestItem:function(elem)
{if(elem==null){return null;}
var item=null;var adr=null;if(elem.getAttribute){adr=elem.getAttribute("adr");if(adr!=null)
{item=this.get_items()._getObjectByAdr(adr);if(item!=null)
return item;}}
if(elem.parentNode==null||elem.parentNode.id==this._elements["DropDown"].id)
return null;else
return this.__getNearestItem(elem.parentNode);},_select:function(event)
{this.__isDropDownEvent=true;if(this.get_displayMode()==$IG.DropDownDisplayMode.ReadOnlyList)
return;var item=this.__getNearestItem(event.target);if(item!=null&&!item.get_disabled())
{if(!this.get_enableMultipleSelection())
{if(!item.get_selected()){var args=this._raiseClientEvent('SelectedIndexChanging','DropDownSelection',event,null,this.get_items().get_indexOf(item),this.get_selectedItemIndex());var cancel=args?args.get_cancel():false;if(!cancel)
{this.__unselectAllItems();item.select();var oldIndex=this.get_selectedItemIndex();this.set_selectedItemIndex(this.get_items().get_indexOf(item));args=this._raiseClientEvent('ValueChanging','DropDownEdit',event,null,this._elements["Input"].value,this.get_currentValue());var cancel=args?args.get_cancel():false;if(!cancel)
{var previousValue=this.get_currentValue();this.set_currentValue(item.get_text());this._raiseClientEvent('ValueChanged','DropDownEdit',event,null,this.get_currentValue(),previousValue);}
var args=this._raiseClientEvent('SelectedIndexChanged','DropDownSelection',event,null,this.get_selectedItemIndex(),oldIndex);}}else{var args=this._raiseClientEvent('SelectedIndexChanging','DropDownSelection',event,null,this.get_selectedItemIndex(),oldIndex);var cancel=args?args.get_cancel():false;if(!cancel)
{item.unselect();var args=this._raiseClientEvent('SelectedIndexChanged','DropDownSelection',event,null,this.get_selectedItemIndex(),oldIndex);var test;}}}
if(this.get_enableMultipleSelection())
{var checked=item._element.childNodes[0].checked;var isEventOnCheckbox=false;var eventSource=event.target;if(eventSource.getAttribute&&eventSource.getAttribute("type")=="checkbox")
{isEventOnCheckbox=true;}
if(!isEventOnCheckbox)
return;if(checked){item.unselect();}else{item.select();}
var currentVal='';var delim=this.get_multiSelectValueDelimiter();var isFirst=true;for(var i=0;i<this.get_items()._items.length;i++)
{if(this.get_items()._items[i].get_selected())
{if(!isFirst)
{currentVal+=delim+this.get_items()._items[i].get_text();}
else
{currentVal+=this.get_items()._items[i].get_text();isFirst=false;}}}
args=this._raiseClientEvent('ValueChanging','DropDownEdit',event,null,this._elements["Input"].value,this.get_currentValue());var cancel=args?args.get_cancel():false;if(!cancel)
{var previousValue=this.get_currentValue();this.set_currentValue(currentVal);this._raiseClientEvent('ValueChanged','DropDownEdit',event,null,this.get_currentValue(),previousValue);}}
if(this.get_closeDropDownOnSelect())
{var args=this._raiseClientEvent('DropDownClosing','DropDownContainer',event,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(false);this._adjustMaxHeight();this._raiseClientEvent('DropDownClosed','DropDownContainer',event,null);}}
if(!this._elements["Input"].disabled)
{this._elements["Input"].focus();}else{this._element.focus();}}},__unselectAllItems:function()
{var items=this.get_items()._items;for(i=0;i<items.length;i++)
{if(!items[i].get_disabled())
{items[i].unselect();}}},_navigateItems:function(event)
{},_onPagerMoreResults:function(event)
{if(!this._noMoreResults)
{var cbo=this._callbackManager.createCallbackObject();cbo.serverContext.type="pagerMoreResults";this._callbackManager.execute(cbo,true);}},_onPagerPrevResults:function(event)
{var cbo=this._callbackManager.createCallbackObject();cbo.serverContext.type="prevPage";this._callbackManager.execute(cbo,true);},_onPagerNextResults:function(event)
{var cbo=this._callbackManager.createCallbackObject();cbo.serverContext.type="nextPage";this._callbackManager.execute(cbo,true);},_scrollingLoadOnDemand:function(event)
{var container=this._elements["DropDownContents"];if(container.scrollHeight==container.clientHeight+container.scrollTop)
{this._dropDownScrollTop=container.scrollTop;this._onPagerMoreResults(event);}},_mouseOverForBlur:function(evnt)
{this.__mouseOver=true;},_mouseOutForBlur:function(evnt)
{this.__mouseOver=false;},_onFocusHandler:function(evnt)
{if(evnt.target.id==this._elements["Input"].id)
{$util.addCompoundClass(this._elements["Input"],this.get_inputFocusCssClass());this._elements["TargetTable"].className=this.get_controlAreaFocusCssClass();this._raiseClientEvent('Focus','DropDownControl',evnt,null);}},_onAnimationEnd:function()
{try{if(this.__isDropDownEvent){this._elements["Input"].focus();this.__isDropDownEvent=false;}}catch(err)
{}},_onBlurHandler:function(evnt)
{if(evnt.target.id==this._elements["Input"].id&&!this.__mouseOver)
{$util.removeCompoundClass(this._elements["Input"],this.get_inputFocusCssClass());this._elements["ButtonImage"].className=this.get_buttonCssClass();this._elements["TargetTable"].className=this.get_controlAreaCssClass();this.behavior.set_visible(false);this._adjustMaxHeight();this._raiseClientEvent('Blur','DropDownControl',evnt,null);}else{var item=this.__getNearestItem(evnt.target);if(item)
{this._raiseClientEvent('BlurItem','DropDownItem',evnt.target,null,item);}}},_onSelectstartHandler:function(elem,adr,evnt)
{},_onMouseoverHandler:function(evnt)
{var item=this.__getNearestItem(evnt.target);if(item!=null&&!item.get_disabled())
{if(this.__getHoveredItem())
{this.__getHoveredItem().unhover();}
this.__setHoveredItem(item);item.hover();}},__setHoveredItem:function(item)
{this.__hoveredItem=item;},__getHoveredItem:function()
{return this.__hoveredItem;},__unhoverItems:function()
{for(i=0;i<this.get_items()._items.length;i++)
{if(!this.get_items()._items[i].get_disabled())
{this.get_items()._items[i].unhover();}}},__loadInitial:function(elem,adr,evnt)
{if(this.get_items()._items.length==0)
{if(this.get_displayMode()!=$IG.DropDownDisplayMode.ReadOnly)
{var args=this._raiseClientEvent('DropDownOpening','DropDownContainer',evnt,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(true);this._adjustMaxHeight();this._raiseClientEvent('DropDownOpened','DropDownContainer',evnt,null);}}
this.__autoFilterOnServer(elem,adr,evnt);}},_onMousedownHandler:function(elem,adr,evnt)
{this.__isDropDownEvent=true;this._elements["Input"].focus();if(this._elements["Input"].id==elem.id){this.__loadInitial(elem,adr,evnt);}else if(this._elements["Button"].id==elem.id||this._elements["ButtonImage"].id==elem.id){this.__loadInitial(elem,adr,evnt);if(this.behavior.get_visible())
{var args=this._raiseClientEvent('DropDownClosing','DropDownContainer',evnt,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(false);this._adjustMaxHeight();this._raiseClientEvent('DropDownClosed','DropDownContainer',evnt,null);}}else{if(this.get_displayMode()!=$IG.DropDownDisplayMode.ReadOnly)
{var args=this._raiseClientEvent('DropDownOpening','DropDownContainer',evnt,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(true);this._adjustMaxHeight();this._raiseClientEvent('DropDownOpened','DropDownContainer',evnt,null);}}}
this._elements["Input"].focus();}else{}},_onChangeHandler:function(elem,adr,evnt)
{},_onKeydownHandler:function(elem,adr,evnt)
{this._raiseClientEvent('KeyDown','DropDownControl',evnt,null);if(evnt.keyCode==40||evnt.keyCode==38||evnt.keyCode==13)
{this.__handleKbNavigation(evnt);}
if(this._elements["Input"].id==elem.id)
{this.set_previousValue(this._elements["Input"].value);if(evnt.keyCode==13){evnt.preventDefault();$util.cancelEvent(evnt);}}
if(evnt.keyCode==27)
{if(this.behavior.get_visible())
{var args=this._raiseClientEvent('DropDownClosing','DropDownContainer',evnt,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(false);this._adjustMaxHeight();this._raiseClientEvent('DropDownClosed','DropDownContainer',evnt,null);}}}},_onKeyupHandler:function(elem,adr,evnt)
{if(evnt.event)
evnt=evnt.event;if(evnt.keyCode==27||evnt.keyCode==9)
return;if(evnt.altKey&&(evnt.keyCode==40||evnt.keyCode==38))
{if(evnt.keyCode==38&&this.behavior.get_visible())
{var args=this._raiseClientEvent('DropDownClosing','DropDownContainer',evnt,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(false);this._raiseClientEvent('DropDownClosed','DropDownContainer',evnt,null);}}else if(evnt.keyCode==40&&!this.behavior.get_visible()){if(this.get_displayMode()!=$IG.DropDownDisplayMode.ReadOnly)
{var args=this._raiseClientEvent('DropDownOpening','DropDownContainer',evnt,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(true);this._adjustMaxHeight();this._raiseClientEvent('DropDownOpened','DropDownContainer',evnt,null);}}}}
if(evnt.ctrlKey||evnt.altKey||(evnt.shiftKey&&evnt.keyCode==45))
return;if(evnt.keyCode!=40&&evnt.keyCode!=38&&evnt.keyCode!=13)
{var args;if(this._elements["Input"].id==elem.id||this._element==elem.id)
{if(this.get_enableCustomValueSelection()&&this.get_enableClientFilteringOnly()&&evnt.keyCode!=27&&evnt.keyCode!=13&&evnt.keyCode!=40&&evnt.keyCode!=38)
{var hasMatch=false;var currentText=this._elements["Input"].value;var items=this.get_items()._items;for(i=0;i<items.length;i++)
{if(items[i].get_text().toLowerCase().startsWith(currentText))
{hasMatch=true;break;}}
if(!hasMatch)
{this._elements["Input"].value=this.get_currentValue();return;}}else if(this.get_enableCustomValueSelection()&&!this.get_enableClientFilteringOnly()&&evnt.keyCode!=27&&evnt.keyCode!=13&&evnt.keyCode!=40&&evnt.keyCode!=38&&this.get_enableAutoFiltering())
{this._currentEvent=evnt;clearTimeout(this._timeoutID);this._timeoutID=setTimeout(Function.createDelegate(this,this.__autoFilterOnServerCustom),200);return;}
args=this._raiseClientEvent('ValueChanging','DropDownEdit',evnt,null,this._elements["Input"].value,this.get_currentValue());}
var cancel=args?args.get_cancel():false;if(!cancel)
{if(this._elements["Input"].id==elem.id){this.set_currentValue(this._elements["Input"].value);this._raiseClientEvent('ValueChanged','DropDownEdit',evnt,null,this.get_currentValue(),this.get_previousValue());if(this.get_enableClientFilteringOnly())
{if(this.get_enableAutoCompleteFirstMatch())
{this.__autoCompleteFirstMatch(elem,adr,evnt);}
if(this.get_enableAutoFiltering())
{if(!(evnt.keyCode==40||evnt.keyCode==38))
{var args=this._raiseClientEvent('AutoFilterStarting','DropDownEdit',evnt,null,this.get_currentValue(),this.get_previousValue());var cancel=args?args.get_cancel():false;if(!cancel)
{this.__autoFilter(elem,adr,evnt);this._raiseClientEvent('AutoFilterStarted','DropDownEdit',evnt,null,this.get_currentValue(),this.get_previousValue());}}}}else{this._currentEvent=evnt;clearTimeout(this._timeoutID);this._timeoutID=setTimeout(Function.createDelegate(this,this.__autoFilterOnServer),200);}}}}},__getNextVisibleItem:function(selectedIndex)
{if(selectedIndex>=0&&selectedIndex<this.get_items()._items.length)
{var index=selectedIndex+1;while(index<this.get_items()._items.length)
{var nextItem=this.get_items()._getObjectByIndex(index);if(nextItem._get_visible()&&!nextItem.get_disabled())
return nextItem;index++;}}
return null;},__getPreviousVisibleItem:function(selectedIndex)
{if(selectedIndex>=0&&selectedIndex<this.get_items()._items.length)
{var index=selectedIndex-1;while(index>=0)
{var prevItem=this.get_items()._getObjectByIndex(index);if(prevItem._get_visible()&&!prevItem.get_disabled())
return prevItem;index--;}}
return null;},__handleKbNavigation:function(e)
{this.__isDropDownEvent=true;if(this.get_displayMode()==$IG.DropDownDisplayMode.ReadOnlyList)
return;var selectedIndex=this.get_selectedItemIndex();var currentItem=this.get_items()._getObjectByIndex(selectedIndex);if(currentItem&&currentItem._get_visible()&&!currentItem.get_disabled()){if(!currentItem.get_selected())
{currentItem.select();return;}}else{currentItem=this.__getNextVisibleItem(selectedIndex);if(!currentItem)
currentItem=this.__getPreviousVisibleItem(selectedIndex);if(currentItem&&!currentItem.get_selected())
{currentItem.select();return;}}
var container=this._elements["DropDownContents"];var container_pos=$util.getPosition(container);if(e.keyCode==13)
{if(currentItem!=null)
{this._elements["Input"].value=currentItem.get_text();}
if(this.get_closeDropDownOnSelect())
{var args=this._raiseClientEvent('DropDownClosing','DropDownContainer',e,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(false);this._raiseClientEvent('DropDownClosed','DropDownContainer',e,null);}}}
if(e.keyCode==40)
{var nextItem=this.get_items()._getObjectByIndex(++selectedIndex);if(nextItem&&(!nextItem._get_visible()||nextItem.get_disabled()))
nextItem=this.__getNextVisibleItem(selectedIndex);if(nextItem)
{var oldIndex=this.get_selectedItemIndex();var args=this._raiseClientEvent('SelectedIndexChanging','DropDownSelection',e,null,selectedIndex,oldIndex);var cancel=args?args.get_cancel():false;if(!cancel)
{if(currentItem)
{currentItem.unselect();}
nextItem.select();this.set_selectedItemIndex(nextItem.get_index());if(this.get_enableMultipleSelection())
{nextItem._element.childNodes[0].checked=true;if(currentItem)
currentItem._element.childNodes[0].checked=false;}
args=this._raiseClientEvent('ValueChanging','DropDownEdit',e,null,this._elements["Input"].value,this.get_currentValue());var cancel=args?args.get_cancel():false;if(!cancel)
{var previousValue=this.get_currentValue();this.set_currentValue(nextItem.get_text());this._raiseClientEvent('ValueChanged','DropDownEdit',e,null,this.get_currentValue(),previousValue);}
this._raiseClientEvent('SelectedIndexChanged','DropDownSelection',e,null,selectedIndex,oldIndex);}}else{}
if(nextItem)
{var pos=$util.getPosition(nextItem._element);if(pos.y+Sys.UI.DomElement.getBounds(nextItem._element).height>container_pos.y+container.offsetHeight)
{container.scrollTop=container.scrollTop+Sys.UI.DomElement.getBounds(nextItem._element).height;}}}else if(e.keyCode==38)
{var prevItem=this.get_items()._getObjectByIndex(--selectedIndex);if(prevItem&&(!prevItem._get_visible()||prevItem.get_disabled()))
prevItem=this.__getPreviousVisibleItem(selectedIndex);if(prevItem)
{var oldIndex=this.get_selectedItemIndex();var args=this._raiseClientEvent('SelectedIndexChanging','DropDownSelection',e,null,selectedIndex,oldIndex);var cancel=args?args.get_cancel():false;if(!cancel)
{if(currentItem)
{currentItem.unselect();}
prevItem.select();this.set_selectedItemIndex(prevItem.get_index());if(this.get_enableMultipleSelection())
{prevItem._element.childNodes[0].checked=true;if(currentItem)
currentItem._element.childNodes[0].checked=false;}
args=this._raiseClientEvent('ValueChanging','DropDownEdit',e,null,this._elements["Input"].value,this.get_currentValue());var cancel=args?args.get_cancel():false;if(!cancel)
{var previousValue=this.get_currentValue();this.set_currentValue(prevItem.get_text());this._raiseClientEvent('ValueChanged','DropDownEdit',e,null,this.get_currentValue(),previousValue);}
this._raiseClientEvent('SelectedIndexChanged','DropDownSelection',e,null,selectedIndex,oldIndex);}}else{}
if(prevItem)
{var pos=$util.getPosition(prevItem._element);if(pos.y<container_pos.y+container.scrollTop)
{if(container.scrollTop-Sys.UI.DomElement.getBounds(prevItem._element).height>=0)
{container.scrollTop=container.scrollTop-Sys.UI.DomElement.getBounds(prevItem._element).height;}
else{container.scrollTop=0;}}}}},_responseComplete:function(callbackObject,responseObject,browserResponseObject)
{var currentControl=this;var props=eval(responseObject.context[responseObject.context.length-1]);var html=responseObject.context[0];var pagerHtml=responseObject.context[1];var type=callbackObject.serverContext.type;var eventName=callbackObject.serverContext.eventName;var list=this._elements["List"];var val=this.get_currentValue();if(this.get_enableCustomValueSelection())
{val=this._elements["Input"].value;}
if((type=="itemsRequested"||type=="itemsRequestedCustom")&&this.get_valueBeforeFilter()!=val)
{if(this.get_enableCustomValueSelection())
this.__autoFilterOnServerCustom();else
this.__autoFilterOnServer();return;}
if(type=="itemsRequested"||type=="itemsRequestedCustom"||type=="remove"||type=="nextPage"||type=="prevPage"||type=="insert"||eventName=="SelectedIndexChanged"||eventName=="ValueChanged")
{if(type=="itemsRequestedCustom"&&!html.toLowerCase().startsWith("<li"))
{this._elements["Input"].value=this.get_currentValue();return;}
while(list.childNodes[0]){list.removeChild(list.childNodes[0]);}
list.innerHTML=html;}else if(type=="add")
{list.innerHTML+=html;}else if(type=="pagerMoreResults")
{if(!html.toLowerCase().startsWith("<li"))
{this._noMoreResults=true;}
else
{list.innerHTML+=html;}}
if(this.get_enablePaging())
{var pager=this._elements["PagerArea"];pager.innerHTML=pagerHtml;}
this._elements=[];$clearHandlers(this._element);this.__clearOtherEvents();this._dataStore=props;this._props=props[0];this._clientStateManager=new $IG.ObjectClientStateManager(this._props);this._objectsManager=new $IG.ObjectsManager(this,props[1]);this._collectionsManager=new $IG.CollectionsManager(this,props[2]);this.behavior._attach();$IG.WebDropDown.callBaseMethod(this,'initialize');if(this.get_enablePaging())
{var pagerPrevDelegate=Function.createDelegate(this,this._onPagerPrevResults);$addHandler(this._elements["PagerPrevLink"],'mousedown',pagerPrevDelegate);var pagerNextDelegate=Function.createDelegate(this,this._onPagerNextResults);$addHandler(this._elements["PagerNextLink"],'mousedown',pagerNextDelegate);}
this.behavior._detach();if(type=="itemsRequestedCustom")
{args=this._raiseClientEvent('ValueChanging','DropDownEdit',this._currentEvent,null,this._elements["Input"].value,this.get_currentValue());var cancel=args?args.get_cancel():false;if(!cancel)
{var previousValue=this.get_currentValue();this.set_currentValue(this._elements["Input"].value);this._raiseClientEvent('ValueChanged','DropDownEdit',this._currentEvent,null,this.get_currentValue(),previousValue);}}
if(this._currentEvent)
{this.__autoCompleteFirstMatch(this._elements["Input"],0,this._currentEvent);}
if(type=="itemsRequested")
this._raiseClientEvent('ItemsRequested','DropDownControl',this._currentEvent,null);if(this.get_displayMode()!=$IG.DropDownDisplayMode.ReadOnly)
{if(this._currentEvent){var args=this._raiseClientEvent('DropDownOpening','DropDownContainer',this._currentEvent,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(true);this._adjustMaxHeight();this._raiseClientEvent('DropDownOpened','DropDownContainer',this._currentEvent,null);}}else{if(this.get_items()._items.length>0)
this.set_currentValue(this.get_items()._items[0].get_text());var args=this._raiseClientEvent('DropDownOpening','DropDownContainer',null,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(true);this._adjustMaxHeight();this._raiseClientEvent('DropDownOpened','DropDownContainer',null,null);}
this._elements["Input"].focus();}}
this.__unhoverItems();if(this._dropDownScrollTop!=null&&this._dropDownScrollTop>0)
{this._elements["DropDownContents"].scrollTop=this._dropDownScrollTop;}
this._adjustMaxHeight();},__autoFilterOnServer:function(elem,adr,evnt)
{this._raiseClientEvent('ItemsRequesting','DropDownEdit',evnt,null,this.get_currentValue(),this.get_previousValue());var args=this._raiseClientEvent('AutoFilterStarting','DropDownEdit',evnt,null,this.get_currentValue(),this.get_previousValue());var cancel=args?args.get_cancel():false;if(!cancel)
{var cbo=this._callbackManager.createCallbackObject();cbo.serverContext.type="itemsRequested";cbo.serverContext.props=Sys.Serialization.JavaScriptSerializer.serialize(this._clientStateManager.get_transactionList());this.set_valueBeforeFilter(this.get_currentValue());this._callbackManager.execute(cbo,true);this._raiseClientEvent('AutoFilterStarted','DropDownEdit',evnt,null,this.get_currentValue(),this.get_previousValue());}},__autoFilterOnServerCustom:function(elem,adr,evnt)
{this._raiseClientEvent('ItemsRequesting','DropDownEdit',evnt,null,this._elements["Input"].value,this.get_currentValue());var args=this._raiseClientEvent('AutoFilterStarting','DropDownEdit',evnt,null,this._elements["Input"].value,this.get_currentValue());var cancel=args?args.get_cancel():false;if(!cancel)
{this._setLoadItemsText(this._elements["Input"].value);var cbo=this._callbackManager.createCallbackObject();cbo.serverContext.type="itemsRequestedCustom";cbo.serverContext.props=Sys.Serialization.JavaScriptSerializer.serialize(this._clientStateManager.get_transactionList());this.set_valueBeforeFilter(this._elements["Input"].value);this._callbackManager.execute(cbo,true);this._raiseClientEvent('AutoFilterStarted','DropDownEdit',evnt,null,this._elements["Input"].value,this.get_currentValue());}},__autoFilter:function(elem,adr,evnt)
{var text=this.get_currentValue();var items=this.get_items()._items;var list=this._elements["List"];while(list.childNodes[0]){list.removeChild(list.childNodes[0]);}
var tempResults=new Array();for(i=0;i<items.length;i++)
{items[i]._set_visible(false);var item_text=items[i].get_text();if(this.get_autoFilterQueryType().toLowerCase()=="startsWith".toLowerCase())
{if(item_text.toLowerCase().startsWith(text.toLowerCase())||text=="")
{tempResults.push(items[i]);}}else if(this.get_autoFilterQueryType().toLowerCase()=="endsWith".toLowerCase())
{if(item_text.toLowerCase().endsWith(text.toLowerCase())||text=="")
{tempResults.push(items[i]);}}else{if(item_text.toLowerCase().indexOf(text.toLowerCase())!=-1||text=="")
{tempResults.push(items[i]);}}}
if(this.get_autoFilterResultSize()>0){for(i=0;i<this.get_autoFilterResultSize()&&i<tempResults.length;i++)
{list.appendChild(tempResults[i]._element);tempResults[i]._set_visible(true);}}else{for(i=0;i<tempResults.length;i++)
{list.appendChild(tempResults[i]._element);tempResults[i]._set_visible(true);}}
this.behavior.set_targetContainerHeight(Sys.UI.DomElement.getBounds(this._elements["DropDown"]).height);if(list.childNodes.length>0)
{if(this.get_displayMode()!=$IG.DropDownDisplayMode.ReadOnly)
{var args=this._raiseClientEvent('DropDownOpening','DropDownContainer',evnt,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(true);this._adjustMaxHeight();this._raiseClientEvent('DropDownOpened','DropDownContainer',evnt,null);}}}},__autoCompleteFirstMatch:function(elem,adr,evnt)
{var items=this.get_items()._items;var source=elem;var value=source.value.toLowerCase();if(evnt.keyCode==40||evnt.keyCode==38)
return;if(evnt.keyCode==16)
return;if(evnt.keyCode==13)
{var args=this._raiseClientEvent('DropDownClosing','DropDownContainer',event,null);var cancel=args?args.get_cancel():false;if(!cancel)
{this.behavior.set_visible(false);this._raiseClientEvent('DropDownClosed','DropDownContainer',event,null);}}
if(evnt.keyCode!=8){for(i=0;i<items.length;i++)
{var item_text=items[i].get_text();if(item_text.toLowerCase().startsWith(value)&&value.length<item_text.length)
{source.value=item_text;if(source.createTextRange&&!$util.IsOpera){range=source.createTextRange();range.findText(item_text.substr(value.length));range.select();}else{source.setSelectionRange(value.length,item_text.length);}
break;}}}}}
$IG.WebDropDown.registerClass('Infragistics.Web.UI.WebDropDown',$IG.ControlMain);$IG.DropDownValueDisplayType=function()
{}
$IG.DropDownValueDisplayType.prototype={Simple:0,WebTextEditor:1};$IG.DropDownValueDisplayType.registerEnum("Infragistics.Web.UI.DropDownValueDisplayType");$IG.DropDownAutoFilterSortOrder=function()
{}
$IG.DropDownAutoFilterSortOrder.prototype={Ascending:0,Descending:1};$IG.DropDownAutoFilterSortOrder.registerEnum("Infragistics.Web.UI.DropDownAutoFilterSortOrder");$IG.DropDownDisplayMode=function()
{}
$IG.DropDownDisplayMode.prototype={DropDownList:0,DropDown:1,ReadOnly:2,ReadOnlyList:3};$IG.DropDownDisplayMode.registerEnum("Infragistics.Web.UI.DropDownDisplayMode");$IG.DropDownProps=new function()
{this.DisplayMode=[$IG.ControlMainProps.Count+0,$IG.DropDownDisplayMode.DropDown];this.ValueListMaxHeight=[$IG.ControlMainProps.Count+1,0];this.OffsetX=[$IG.ControlMainProps.Count+2,0];this.OffsetY=[$IG.ControlMainProps.Count+3,0];this.EnablePaging=[$IG.ControlMainProps.Count+4,false];this.PageSize=[$IG.ControlMainProps.Count+5,0];this.SelectedItemIndex=[$IG.ControlMainProps.Count+6,-1];this.SelectedItemIndices=[$IG.ControlMainProps.Count+7,[]];this.MultiSelectValueDelimiter=[$IG.ControlMainProps.Count+8,","];this.EnableCustomValueSelection=[$IG.ControlMainProps.Count+9,false];this.EnableMultipleSelection=[$IG.ControlMainProps.Count+10,false];this.CloseDropDownOnSelect=[$IG.ControlMainProps.Count+11,true];this.PersistCustomValues=[$IG.ControlMainProps.Count+12,false];this.EnableAutoFiltering=[$IG.ControlMainProps.Count+13,true];this.AutoFilterQueryType=[$IG.ControlMainProps.Count+14,"startsWith"];this.AutoFilterResultSize=[$IG.ControlMainProps.Count+15,0];this.AutoFilterSortOrder=[$IG.ControlMainProps.Count+16,$IG.DropDownAutoFilterSortOrder.Ascending];this.EnableAutoCompleteFirstMatch=[$IG.ControlMainProps.Count+17,true];this.EnableLoadOnDemand=[$IG.ControlMainProps.Count+18,false];this.DropDownContainerWidth=[$IG.ControlMainProps.Count+19,0];this.DropDownContainerHeight=[$IG.ControlMainProps.Count+20,0];this.EnableCaseSensitivity=[$IG.ControlMainProps.Count+21,false];this.CurrentValue=[$IG.ControlMainProps.Count+22,null];this.ShowDropDownButton=[$IG.ControlMainProps.Count+23,true];this.DropDownValueDisplayType=[$IG.ControlMainProps.Count+24,$IG.DropDownValueDisplayType.Simple];this.EnableClientFilteringOnly=[$IG.ControlMainProps.Count+25,false];this.DropDownAnimationDuration=[$IG.ControlMainProps.Count+26,500];this.DropDownOrientation=[$IG.ControlMainProps.Count+27,0];this.LoadItemsText=[$IG.ControlMainProps.Count+28,null];this.Count=$IG.ControlMainProps.Count+29;}
$IG.DropDownItemProps=new function()
{this.Text=[$IG.ControlMainProps.Count+0,""];this.Value=[$IG.ControlMainProps.Count+1,""];this.Selected=[$IG.ControlMainProps.Count+2,""];this.Disabled=[$IG.ControlMainProps.Count+3,""];this.SelectedCssClass=[$IG.ControlMainProps.Count+4,""];this.Count=$IG.ControlMainProps.Count+5;};$IG.DropDownItem=function(adr,element,props,control,csm)
{this._control=control;$IG.DropDownItem.initializeBase(this,[adr,element,props,control,csm]);this.__visible=true;}
$IG.DropDownItem.prototype={select:function()
{this._element.className=this.get_selectedCssClass();this.set_selected(true);},unselect:function()
{this._element.className=this.get_cssClass();this.set_selected(false);},hover:function()
{this._element.className=this.get_hoverCssClass();},unhover:function()
{this._element.className=this.get_cssClass();},_ensureFlags:function()
{$IG.DropDownItem.callBaseMethod(this,"_ensureFlag");this._ensureFlag($IG.ClientUIFlags.Selectable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Visible,$IG.DefaultableBoolean.True);},get_text:function()
{return this._get_value($IG.DropDownItemProps.Text);},set_text:function(text)
{this._set_value($IG.DropDownItemProps.Text,text);},get_value:function()
{return this._get_value($IG.DropDownItemProps.Value);},set_value:function(val)
{this._set_value($IG.DropDownItemProps.Value,val);},get_index:function()
{return parseInt(this._get_address());},get_selected:function()
{return this._get_value($IG.DropDownItemProps.Selected);},get_disabled:function()
{return this._get_value($IG.DropDownItemProps.Disabled);},set_disabled:function()
{this._set_value($IG.DropDownItemProps.Disabled);this._element.className=this.get_disabledCssClass();},_get_visible:function()
{return this.__visible;},_set_visible:function(visible)
{this.__visible=visible;},set_selected:function(selected)
{this._set_value($IG.DropDownItemProps.Selected,selected);},isSelected:function()
{return this._getFlags().getSelected();},get_cssClass:function()
{return this._control._get_clientOnlyValue("dropDownItemClass");},get_selectedCssClass:function()
{return this._control._get_clientOnlyValue("dropDownItemSelected");},get_disabledCssClass:function()
{return this._control._get_clientOnlyValue("dropDownItemDisabled");},get_hoverCssClass:function()
{return this._control._get_clientOnlyValue("dropDownItemHover");}}
$IG.DropDownItem.registerClass('Infragistics.Web.UI.DropDownItem',$IG.ListItem);$IG.DropDownItemCollection=function(control,clientStateManager,index,manager)
{$IG.DropDownItemCollection.initializeBase(this,[control,clientStateManager,index,manager]);}
$IG.DropDownItemCollection.prototype={add:function(item)
{if(item==null)
return;var cbo=this._control._callbackManager.createCallbackObject();cbo.serverContext.type="add";cbo.serverContext.props=Sys.Serialization.JavaScriptSerializer.serialize(item._csm.get_transactionList())
this._control._callbackManager.execute(cbo,true);},remove:function(item)
{if(item!=null)
{var cbo=this._control._callbackManager.createCallbackObject();cbo.serverContext.type="remove";cbo.clientContext.item=item;cbo.serverContext.index=item.get_index();this._control._callbackManager.execute(cbo,true);}},insert:function(index,item)
{if(item==null)
return;var cbo=this._control._callbackManager.createCallbackObject();if(index>=0&&index<this.get_length())
cbo.serverContext.type="insert";else
cbo.serverContext.type="add";cbo.serverContext.index=index;cbo.serverContext.props=Sys.Serialization.JavaScriptSerializer.serialize(item._csm.get_transactionList())
this._control._callbackManager.execute(cbo,true);},createItem:function()
{var props=new Array();var clientProps=new Array();var length=$IG.DropDownProps.Count;for(var i=0;i<length;i++)
clientProps.push(null);props.push(clientProps);var elem=document.createElement("li");var csm=new $IG.ObjectClientStateManager(props);var item=new $IG.DropDownItem("-1",elem,props,this._control,csm);return item;},getItem:function(index)
{if(index>=0&&index<this.get_length())
return this._items[index];return null;}}
$IG.DropDownItemCollection.registerClass('Infragistics.Web.UI.DropDownItemCollection',$IG.ObjectCollection);$IG.DropDownControlEventArgs=function()
{$IG.DropDownControlEventArgs.initializeBase(this);}
$IG.DropDownControlEventArgs.prototype={}
$IG.DropDownControlEventArgs.registerClass('Infragistics.Web.UI.DropDownControlEventArgs',$IG.CancelEventArgs);$IG.DropDownSelectionEventArgs=function()
{$IG.DropDownSelectionEventArgs.initializeBase(this);}
$IG.DropDownSelectionEventArgs.prototype={getNewIndex:function()
{return this._props[2];},getOldIndex:function()
{return this._props[3];}}
$IG.DropDownSelectionEventArgs.registerClass('Infragistics.Web.UI.DropDownSelectionEventArgs',$IG.DropDownControlEventArgs);$IG.DropDownEditEventArgs=function()
{$IG.DropDownEditEventArgs.initializeBase(this);}
$IG.DropDownEditEventArgs.prototype={getNewValue:function()
{return this._props[2];},getOldValue:function()
{return this._props[3];}}
$IG.DropDownEditEventArgs.registerClass('Infragistics.Web.UI.DropDownEditEventArgs',$IG.DropDownControlEventArgs);$IG.DropDownContainerEventArgs=function()
{$IG.DropDownContainerEventArgs.initializeBase(this);}
$IG.DropDownContainerEventArgs.prototype={getVisible:function()
{return this._props[2];}}
$IG.DropDownContainerEventArgs.registerClass('Infragistics.Web.UI.DropDownContainerEventArgs',$IG.DropDownControlEventArgs);$IG.DropDownItemEventArgs=function()
{$IG.DropDownItemEventArgs.initializeBase(this);}
$IG.DropDownItemEventArgs.prototype={getItem:function()
{return this._props[1];}}
$IG.DropDownItemEventArgs.registerClass('Infragistics.Web.UI.DropDownItemEventArgs',$IG.DropDownControlEventArgs);