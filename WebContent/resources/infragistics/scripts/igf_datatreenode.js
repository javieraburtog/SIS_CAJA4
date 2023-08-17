// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace('Infragistics.Web.UI');$IG.DataTreeProps=new function()
{this.CollapsedIndicatorImageUrl=[$IG.NavControlProps.Count+0,""];this.DataLoadingMessage=[$IG.NavControlProps.Count+1,""];this.EnableAjax=[$IG.NavControlProps.Count+2,""];this.EnableConnectorLines=[$IG.NavControlProps.Count+3,0];this.Enabled=[$IG.NavControlProps.Count+4,0];this.EnableExpandImages=[$IG.NavControlProps.Count+5,true];this.EnableExpandOnClick=[$IG.NavControlProps.Count+6,false];this.ExpandedIndicatorImageUrl=[$IG.NavControlProps.Count+7,""];this.NodeDataMessage=[$IG.NavControlProps.Count+8,""];this.EnableDropInsertion=[$IG.NavControlProps.Count+9,false];this.DragMode=[$IG.NavControlProps.Count+10,2];this.NodeSelectionType=[$IG.NavControlProps.Count+11,0];this.EnableWordWrapping=[$IG.NavControlProps.Count+12,false];this.ActiveNodeAddress=[$IG.NavControlProps.Count+13,""];this.DataLoadingMessageCssClass=[$IG.NavControlProps.Count+14,""];this.EnableHotTracking=[$IG.NavControlProps.Count+15,false];this.AnimationEquationType=[$IG.NavControlProps.Count+16,0];this.AnimationDuration=[$IG.NavControlProps.Count+17,1000];this.ControlFocused=[$IG.NavControlProps.Count+18,false];this.Count=$IG.NavControlProps.Count+19;};$IG.NodeSettingsProps=new function()
{this.CssClass=[0,""];this.DisabledCssClass=[1,""];this.ActiveCssClass=[2,""];this.HoverCssClass=[3,""];this.ImageUrl=[4,""];this.LeafNodeImageUrl=[5,""];this.Target=[6,""];this.NavigateUrl=[7,""];this.ParentNodeImageUrl=[8,""];this.SelectedCssClass=[9,""];this.SelectedImageUrl=[10,""];this.SelectedLeafNodeImageUrl=[11,""];this.SelectedParentNodeImageUrl=[12,""];this.ImageCssClass=[13,""];this.AnchorCssClass=[14,""];this.HolderCssClass=[15,""];this.GroupCssClass=[16,""];this.ImageToolTip=[17,""];this.LeafNodeImageToolTip=[18,""];this.ParentNodeImageToolTip=[19,""];this.Count=20;};$IG.NodeEditingProps=new function()
{this.Enabled=[0,false];this.EnableOnF2=[1,false];this.EnableOnDoubleClick=[2,false];this.InternalEditorCssClass=[3,""];this.Count=4;};$IG.WebDataTree=function(element)
{$IG.WebDataTree.initializeBase(this,[element]);element.style.visibility="visible";}
$IG.WebDataTree.prototype={_thisType:'tree',initialize:function()
{this._nodeSettings=new $IG.NodeSettings("nodeSettings",null,this._objectsManager.get_objectProps(0),this,"nodeSettings");this._objectsManager.register_object(0,this._nodeSettings);this._nodeEditing=new $IG.NodeEditing("nodeEditing",null,this._objectsManager.get_objectProps(1),this,"nodeEditing");this._objectsManager.register_object(1,this._nodeEditing);this._controlActiveCssClass=this._get_clientOnlyValue("cssControlActive");this._nodeCssClass=this._get_clientOnlyValue("cssNode");this._nodeHoverCssClass=this._get_clientOnlyValue("cssNodeHover");this._nodeActiveCssClass=this._get_clientOnlyValue("cssNodeActive");this._nodeSelectedCssClass=this._get_clientOnlyValue("cssNodeSelected");this._nodeDisabledCssClass=this._get_clientOnlyValue("cssNodeDisabled");this._internalNodeEditorCssClass=this._get_clientOnlyValue("cssInternalNodeEditor");this._collapseImageToolTip=this._get_clientOnlyValue("collapseImageToolTip");this._expandImageToolTip=this._get_clientOnlyValue("expandImageToolTip");this._uncheckedImageURL=this._get_clientOnlyValue("uncheckedImageURL");this._checkedImageURL=this._get_clientOnlyValue("checkedImageURL");this._partialImageURL=this._get_clientOnlyValue("partialImageURL");this._selectedNodes=[];this._newSelectedNodes=[];this._contSelStartNode=null;this._contSelDir=0;this._selectionType=this._get_value($IG.DataTreeProps.NodeSelectionType);this._checkedNodes=[];this._checkBoxMode=this._get_clientOnlyValue("checkBoxMode");this._initialSelection=true;$IG.WebDataTree.callBaseMethod(this,'initialize');this._initialSelection=false;this._itemCollection._getUIBehaviorsObj().setDragDropNotification(true);this._loadImages(this.get_element());this._element._address="main";this._activeNode=null;var activeNodeAddr=this._get_value($IG.DataTreeProps.ActiveNodeAddress);if(activeNodeAddr!=null&&activeNodeAddr.length>0)
{var actNode=this._ensureNode(null,activeNodeAddr);if(actNode!=null)
{this._activeNode=actNode;}}
this._nodeEditor=new $IG.TreeInternalEditor(this.get_element());if(this._nodeEditor._originalNotifyLostFocus==null)
{this._nodeEditor._originalNotifyLostFocus=this._nodeEditor.notifyLostFocus;this._nodeEditor.notifyLostFocus=Function.createDelegate(this,this.__editorLostFocus);}
this._currentEditor=null;this._nodeInEditMode=null;this._onKeypressFn=Function.createDelegate(this,this._onKeypressHandler);$addHandler(this.get_element(),'keypress',this._onKeypressFn);this._onKeydownFn=Function.createDelegate(this,this._onKeydownHandler);$addHandler(this.get_element(),'keydown',this._onKeydownFn);this._onKeyupFn=Function.createDelegate(this,this._onKeyupHandler);$addHandler(this.get_element(),'keyup',this._onKeyupFn);this._onMouseDownFn=Function.createDelegate(this,this._onMouseDownHandler);$addHandler(this.get_element(),'mousedown',this._onMouseDownFn);var focusElement=this.get_focusElement();if(focusElement!=null)
{this._onFocusFn=Function.createDelegate(this,this._onFocusHandler);$addHandler(focusElement,'focus',this._onFocusFn);this._onBlurFn=Function.createDelegate(this,this._onBlurHandler);$addHandler(focusElement,'blur',this._onBlurFn);var wasFocused=this._get_value($IG.DataTreeProps.ControlFocused,true);if(wasFocused)
{focusElement.blur();focusElement.focus();}}
this.__cancelBlur=false;this.__controlActive=false;this.__scrollbarWidth=null;if(this._thisType=='tree'&&!this.preventInitEvent)
this._raiseClientEvent('Initialize');},getNodeEditing:function()
{return this._nodeEditing;},_uiBehaviorsObject_select:function(item,e)
{var clickedOnDTN=false;if(e!=null)
{if(this._control._nodeInEditMode!=null)
{this._control._exitNodeEditing(true);}
var mainEl=this._control.get_element();var parent=e.target;while(parent&&parent!=mainEl)
{if(parent.getAttribute("mkr")=="dtnIcon"||parent.getAttribute("mkr")=="dtnContent")
{clickedOnDTN=true;this._control.__cancelBlur=true;break;}
parent=parent.parentNode;}}
if(e==null)
{this._control._nodeClick(item,e);}
else
if(e.button==0)
{if(parseInt(e.target.getAttribute("idx"))>0)
{var node=this._control._ensureNode(e.target.parentNode,e.target.parentNode.getAttribute("adr"));item.toggle(true);}
else if(e.target.getAttribute("mkr")=="check")
{var node=this._control._ensureNode(e.target.parentNode,e.target.parentNode.getAttribute("adr"));if(node.get_enabled())
{var args=this._control._raiseClientEvent('NodeChecking','DataTreeNode',null,null,node);if(args&&args.get_cancel())return;var oldSelection=this._control.get_checkedNodes();this._control._toggleNodeCheckState(node);var newSelection=this._control.get_checkedNodes();this._control._raiseClientEvent('CheckBoxSelectionChanged','DataTreeCheckBoxSelection',null,null,oldSelection,newSelection);}}
else if(clickedOnDTN)
{if(this._control.get_enableExpandOnClick())item.toggle(true);this._control.set_activeNode(item,true);this._control._nodeClick(item,e);}}
if(e)
{this._mouseDown=true;$util.cancelEvent(e);}},_checkNode:function(node,state)
{if(state==$IG.CheckBoxState.Checked)
{this._checkedNodes.push(node);}
else
{for(i=0;i<this._checkedNodes.length;i++)
{if(this._checkedNodes[i]==node){this._checkedNodes.splice(i,1);break;}}}},_toggleNodeCheckState:function(node)
{if(node.get_checkState()==$IG.CheckBoxState.Unchecked)
{node.set_checkState($IG.CheckBoxState.Checked);}
else
{node.set_checkState($IG.CheckBoxState.Unchecked);}
this._applyCheckStateToChildren(node);this._applyCheckStateToParent(node);},_applyCheckStateToChildren:function(node)
{var state=node.get_checkState();for(var i=0;i<node.get_childrenCount();i++)
{var childNode=node.get_childNode(i);childNode.set_checkState(state);this._applyCheckStateToChildren(childNode);}},_applyCheckStateToParent:function(node)
{var parentNode=node.get_parentNode();if(parentNode==null)
return;var childrenCount=parentNode.get_childrenCount();var checkedCount=0;var partialCount=0;for(var i=0;i<childrenCount;i++)
{var childState=parentNode.get_childNode(i).get_checkState();if(childState==$IG.CheckBoxState.Checked)
checkedCount++;else if(childState==$IG.CheckBoxState.Partial)
partialCount++;}
if(partialCount>0)
{parentNode.set_checkState($IG.CheckBoxState.Partial);}
else if(checkedCount==0)
{parentNode.set_checkState($IG.CheckBoxState.Unchecked);}
else if(checkedCount==childrenCount)
{parentNode.set_checkState($IG.CheckBoxState.Checked);}
else if(this._checkBoxMode==$IG.CheckBoxMode.TriState)
{parentNode.set_checkState($IG.CheckBoxState.Partial);}
else
{parentNode.set_checkState($IG.CheckBoxState.Unchecked);}
this._applyCheckStateToParent(parentNode);},_nodeClick:function(node,browserEvent)
{if(this._initialSelection)
{this._selectedNodes.push(node);}
else
{if(this._selectionType==1||this._selectionType==2)
{this._selectNode(node,true,browserEvent);}}},_selectNode:function(node,value,browserEvent)
{if(!node)return;if(!node.get_enabled())
{this._newSelectedNodes=[];this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);return;}
if(browserEvent==null)
{if(value!=node.get_selected())
{this._newSelectedNodes=Array.clone(this._selectedNodes);if(value)
{this._newSelectedNodes.push(node);this._setNodeSelectionState(node,true);}
else
{for(i=0;i<this._newSelectedNodes.length;i++)
{if(this._newSelectedNodes[i]._get_address()==node._get_address())
{this._newSelectedNodes.splice(i,1);break;}}
this._setNodeSelectionState(node,false);}
this._selectedNodes=this._newSelectedNodes;}
else
{var foundAtIndex=-1;for(i=0;i<this._selectedNodes.length;i++)
{if(this._selectedNodes[i]._get_address()==node._get_address())
{foundAtIndex=i;break;}}
if(foundAtIndex==-1)this._selectedNodes.push(node);else this._selectedNodes[foundAtIndex]=node;}}
else
{if(this._selectionType==1)
{if(!node.get_selected())
{this._newSelectedNodes=[];if(value)this._newSelectedNodes.push(node);this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}
else
if(node.get_selected()&&browserEvent.ctrlKey)
{this._newSelectedNodes=[];this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}
this._contSelStartNode==null}
else
if(this._selectionType==2)
{if(browserEvent.shiftKey)
{if(this._selectedNodes.length==0||this.get_activeNode()==null)
{this._newSelectedNodes=Array.clone(this._selectedNodes);this._newSelectedNodes.push(node);this._addRemoveSelection(this._selectedNodes,this._newSelectedNodes,node,true,true);}
else
{if(browserEvent.ctrlKey)
{this._newSelectedNodes=Array.clone(this._selectedNodes);}
else
{this._newSelectedNodes=[];}
var selectionBeginNode=null;if(this._selectedNodes.length>0)
{selectionBeginNode=this._selectedNodes[this._selectedNodes.length-1];}
else
{selectionBeginNode=this.get_activeNode();}
if(selectionBeginNode!=node)
{if(selectionBeginNode&&!browserEvent.ctrlKey)
{this._newSelectedNodes.push(selectionBeginNode);}
if(node.isAfter(selectionBeginNode))
{var nextNode=selectionBeginNode._get_navigationDownNode();while(nextNode!=null)
{if(nextNode.get_enabled())this._newSelectedNodes.push(nextNode);if(nextNode==node)break;nextNode=nextNode._get_navigationDownNode();}}
else
{var nextNode=selectionBeginNode._get_navigationUpNode();while(nextNode!=null)
{if(nextNode.get_enabled())this._newSelectedNodes.push(nextNode);if(nextNode==node)break;nextNode=nextNode._get_navigationUpNode();}}
this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}}}else
if(browserEvent.ctrlKey)
{this._newSelectedNodes=Array.clone(this._selectedNodes);if(node.get_selected())
{for(i=0;i<this._newSelectedNodes.length;i++)
{if(this._newSelectedNodes[i]._get_address()==node._get_address())
{this._newSelectedNodes.splice(i,1);break;}}}
else
{this._newSelectedNodes.push(node);}
this._addRemoveSelection(this._selectedNodes,this._newSelectedNodes,node,!node.get_selected(),true);}
else
{if(!(this._selectedNodes.length==1&&this._selectedNodes[0]==node))
{this._newSelectedNodes=[];this._newSelectedNodes.push(node);this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}
this._contSelStartNode==null;}}}},_addRemoveSelection:function(oldSelection,newSelection,aNode,select,fireEvents)
{if(fireEvents)
{var args=this._raiseClientEvent('SelectionChanging','DataTreeSelection',null,null,oldSelection,newSelection);if(args&&args.get_cancel())return;}
this._setNodeSelectionState(aNode,select);this._selectedNodes=newSelection;if(fireEvents)
{this._raiseClientEvent('SelectionChanged','DataTreeSelection',null,null,oldSelection,newSelection);}},_changeSelection:function(oldSelection,newSelection,fireEvents)
{if(fireEvents)
{var args=this._raiseClientEvent('SelectionChanging','DataTreeSelection',null,null,oldSelection,newSelection);if(args&&args.get_cancel())return;}
var i;for(i=0;i<oldSelection.length;i++)
{this._setNodeSelectionState(oldSelection[i],false);}
for(i=0;i<newSelection.length;i++)
{this._setNodeSelectionState(newSelection[i],true);}
this._selectedNodes=newSelection;if(fireEvents)
{this._raiseClientEvent('SelectionChanged','DataTreeSelection',null,null,oldSelection,newSelection);}},_setNodeSelectionState:function(node,value)
{if(node==null)return;var nodeFlags=node._getFlags();if(nodeFlags.getSelected()==value)return;nodeFlags.setSelected(value);styleElement=node.get_styleElement();if(styleElement)
{var cssClass=this._selectedCssClassResolved(node);Infragistics.Utility.toggleCompoundClass(styleElement,cssClass,value);}},get_focusElement:function()
{var child=this.get_element().firstChild;while(child!=null)
{if(child.tagName=="INPUT")return child;child=child.nextSibling;}
return null;},get_activeNode:function()
{return this._activeNode;},set_activeNode:function(node,fireEvent)
{if(node!=null&&!node.get_visible())return;if(fireEvent)
{var args=this._raiseClientEvent('ActivationChanging','DataTreeActivation',null,null,this._activeNode,node);if(args&&args.get_cancel())return;}
if(this._activeNode!=null)
{$util.removeCompoundClass(this._activeNode.get_styleElement(),this._activeCssClassResolved(this._activeNode));}
this._activeNode=node;if(this._activeNode!=null)
{this._set_value($IG.DataTreeProps.ActiveNodeAddress,this._activeNode._get_address());if(this.__controlActive)
{var styleEl=this._activeNode.get_styleElement();$util.addCompoundClass(styleEl,this._activeCssClassResolved(this._activeNode));this._scrollToNode(this._activeNode,this.get_element());}}
else
{this._set_value($IG.DataTreeProps.ActiveNodeAddress,"");}
if(fireEvent)
{this._raiseClientEvent('ActivationChanged','DataTreeActivation',null,null,this._activeNode,this._activeNode);}},_scrollToNode:function(node,scrollElement)
{var styleEl=node.get_styleElement();if(styleEl==null)return;var bounds=Sys.UI.DomElement.getBounds(styleEl);var el_x1=bounds.x;var el_y1=bounds.y;var el_x2=el_x1+bounds.width;var el_y2=el_y1+bounds.height;var va_x1=scrollElement.scrollLeft;var va_y1=scrollElement.scrollTop;var va_x2=va_x1+scrollElement.offsetWidth-1;if(scrollElement.scrollWidth>scrollElement.offsetWidth)
va_x2=va_x2-this.__get_scrollbarWidth();var va_y2=va_y1+scrollElement.offsetHeight-1;if(scrollElement.scrollHeight>scrollElement.offsetHeight)
va_y2=va_y2-this.__get_scrollbarWidth();var dx=0;if(el_x1<va_x1)dx=el_x1-va_x1;else if(el_x2>va_x2)dx=el_x2-va_x2;var dy=0;if(el_y1<va_y1)dy=el_y1-va_y1;else if(el_y2>va_y2)dy=el_y2-va_y2;scrollElement.scrollLeft+=dx;scrollElement.scrollTop+=dy;},_updateNarratorInfo:function()
{if(this._activeNode!=null)
{var activeNodeAnchor=this._activeNode.get_anchorElement();var focusElement=this.get_focusElement();if(activeNodeAnchor!=null&&focusElement!=null)
{focusElement.title=activeNodeAnchor.innerHTML;this.__cancelBlur=true;focusElement.blur();focusElement.focus();}}},__get_scrollbarWidth:function()
{if(this.__scrollbarWidth==null)
{var oldScroll=document.body.style.overflow;document.body.style.overflow='hidden';this.__scrollbarWidth=document.body.clientWidth;document.body.style.overflow='scroll';this.__scrollbarWidth-=document.body.clientWidth;if(!this.__scrollbarWidth)
this.__scrollbarWidth=document.body.offsetWidth-document.body.clientWidth;document.body.style.overflow=oldScroll;}
return this.__scrollbarWidth;},get_collapseImageToolTip:function()
{return this._collapseImageToolTip;},get_expandImageToolTip:function()
{return this._expandImageToolTip;},_addHandlers:function()
{$IG.WebDataTree.callBaseMethod(this,'_addHandlers');this._registerHandlers(["mouseup","dblclick"]);},_onMouseupHandler:function(elem,adr,browserEvent)
{if(this._nodeInEditMode==null)
{var node=this._itemCollection._getUIBehaviorsObj().getItemFromElem(elem);if(node&&browserEvent!=null)
{var clickedOnDTN=false;var mainEl=this.get_element();var parent=browserEvent.target;while(parent&&parent!=mainEl)
{if(parent.getAttribute("mkr")=="dtnIcon"||parent.getAttribute("mkr")=="dtnContent")
{clickedOnDTN=true;break;}
parent=parent.parentNode;}
if(clickedOnDTN)
{node._toggleClicked();this._raiseClientEvent('NodeClick','DataTreeNode',browserEvent,null,node,node._get_address(),browserEvent.button);}}
var fa=this.get_focusElement();if(fa&&fa.focus)fa.focus();this._updateNarratorInfo();}},_onDblclickHandler:function(elem,adr,browserEvent)
{if(!this._nodeEditing.get_enableOnDoubleClick())return;if(this._currentEditor!=null&&e.target==this._currentEditor._element)return;if(browserEvent.target.tagName=="A"&&browserEvent.button==0)
{var activeNode=this.get_activeNode();if(activeNode!=null)
{var nodeEditable=activeNode.get_editable();if(nodeEditable==2||(nodeEditable==0&&this._nodeEditing.get_enabled()))
{this._enterNodeEditing(activeNode);}}}},_createItem:function(element,adr)
{var node=this._itemCollection._addObject($IG.Node,element,adr);if(node.get_checkState()==$IG.CheckBoxState.Checked)
this._checkedNodes.push(node);},_setupCollections:function()
{this._itemCollection=this._collectionsManager.register_collection(0,$IG.NodeCollection);this._collectionsManager.registerUIBehaviors(this._itemCollection);this._itemCollection._getUIBehaviorsObj().select=this._uiBehaviorsObject_select;},_loadImages:function(element)
{var imageContainer=$get(this.get_id()+"_Images");this._imageList=imageContainer.getElementsByTagName("IMG");var images=element.getElementsByTagName("IMG");for(var i=0;i<images.length;i++)
{var image=images[i];this._resolveImage(image);}},_ensureNode:function(e,adr)
{var node=this._itemCollection._getObjectByAdr(adr);if(node==null&&e!=null)
node=this._itemCollection._addObject($IG.Node,e,adr);return node;},_resolveImage:function(image)
{var index=image.className;if(!$util.isEmpty(index)&&!isNaN(index))
{image.className="";image.parentNode._expImage=image;if(this._imageList.length<4)
{if(index=="1"||index=="2")
{image.setAttribute("idx",index);}}
else
{if(index=="3"||index=="4"||index=="6"||index=="7"||index=="9"||index=="10"||index=="14"||index=="15")
{image.setAttribute("idx",index);}}}},_populateItem:function(node)
{var args=this._raiseClientEvent('NodePopulating','DataTreeNode',null,null,node);if(args&&args.get_cancel())
return;this.__showDataLoadingMessage(node);var adr=node._get_address();var cbo=this._callbackManager.createCallbackObject();var nodeDisplayChain=this._getDisplayChain(node,adr);cbo.serverContext.type=cbo.clientContext.type="populate";cbo.serverContext.parent=adr;cbo.serverContext.dataPath=node.get_dataPath();cbo.serverContext.displayChain=nodeDisplayChain;cbo.clientContext.currentLoadingNodeText=node.get_text();cbo.clientContext.parentNode=node;this._callbackManager.execute(cbo);},__showDataLoadingMessage:function(node)
{var msgBox=this._elements["DataLoadingMessage"];if(!msgBox||msgBox==null)
return;var e=node._element;e.appendChild(msgBox);msgBox.style.display="";msgBox.style.visibility="visible";msgBox.style.position="relative";},__hideDataLoadingMessage:function()
{var msgBox=this._elements["DataLoadingMessage"];if(!msgBox||msgBox==null)
return;msgBox.parentNode.removeChild(msgBox);msgBox.style.display="none";msgBox.style.visibility="hidden";msgBox.style.position="absolute";},_getDisplayChain:function(node,adr)
{var level=adr.split(".").length;var nodeDisplayChain="";if(this.get_enableConnectorLines())
{var parent=node;while(parent!=null)
{var next;next=parent.get_nextNode();nodeDisplayChain+=(next!=null)?"1":"0";parent=parent.get_parentNode();}}
return nodeDisplayChain;},_responseComplete:function(callbackObject,responseObject)
{var type=callbackObject.serverContext.type;if(type=="populate")
{this.__hideDataLoadingMessage();var html=responseObject.context[0];var parentNode=callbackObject.clientContext.parentNode;var e=parentNode._element;e.innerHTML+=html;var img=parentNode._get_expandCollapseElement();var nodeCount=responseObject.context.length;for(var i=1;i<nodeCount;i++)
{var response=eval(responseObject.context[i]);var adr=response[0];var props=response[1];this._itemCollection._csm.set_itemProps(adr,props);}
var childNodesElement=parentNode._get_subgroup();this.__walkThrough(childNodesElement,false);var children=e.getElementsByTagName("LI");for(i=0;i<children.length;i++)
{var child=children[i];this._ensureNode(child,child.getAttribute("adr"));}
this._loadImages(e);parentNode.set_populated(true);this._raiseClientEvent('NodePopulated','DataTreeNode',null,null,parentNode);parentNode.set_expanded(true,true);}
else
if(type=="add")
{}
else
if(type=="remove")
{var html=responseObject.context[0];var parentNode=callbackObject.clientContext.parentNode;var e=parentNode.get_element();var helperEl=document.createElement("DIV");helperEl.innerHTML=html;e.id=helperEl.firstChild.id;e.className=helperEl.firstChild.className;e.innerHTML=helperEl.firstChild.innerHTML;$util._initAttr(e);this._loadImages(e);var img=parentNode._get_expandCollapseElement();var nodeCount=responseObject.context.length;for(var i=1;i<nodeCount;i++)
{var response=eval(responseObject.context[i]);var adr=response[0];var props=response[1];this._itemCollection._csm.set_itemProps(adr,props);}
this._ensureNode(e,e.getAttribute("adr"));var children=e.getElementsByTagName("LI");for(i=0;i<children.length;i++)
{var child=children[i];$util._initAttr(child);this._ensureNode(child,child.getAttribute("adr"));}}
else
{var save1=null;if(this._contSelStartNode!=null)save1=this._contSelStartNode._get_address();var save2=this._contSelDir;var save3=this._selectionType;var save4=null;if(this._activeNode!=null)save4=this._activeNode._get_address();var save5=this._currentEditor;var save7=this.__cancelBlur;var save8=this.__controlActive;var save9=this.__scrollbarWidth;this._elements=[];this._events._list=[];$clearHandlers(this._element);var focusElement=this.get_focusElement();if(focusElement!=null)$clearHandlers(focusElement);this._nodeEditor.dispose();var props=eval(responseObject.context[0]);this.set_props(props);this.preventInitEvent=true;this.initialize();this._contSelStartNode=save1!=null?this.resolveItem(save1):null;this._contSelDir=save2;this._selectionType=save3;this._activeNode=save4!=null?this.resolveItem(save4):null;this._currentEditor=save5;this.__cancelBlur=save7;this.__controlActive=save8;this.__scrollbarWidth=save9;}},_onKeypressHandler:function(browserEvent)
{var aNode=this.get_activeNode();var args=this._raiseClientEvent('KeyPress','DataTreeNode',window.event,null,aNode);if(args&&args.get_cancel())
{$util.cancelEvent(window.event);return;}
if(this._nodeInEditMode==null)
{if($util.IsOpera)
{this.__internalKeyHandler(window.event);}}},_onKeyupHandler:function(browserEvent)
{var aNode=this.get_activeNode();var args=this._raiseClientEvent('KeyUp','DataTreeNode',browserEvent,null,aNode);if(args&&args.get_cancel())
{$util.cancelEvent(browserEvent);return;}
if(this._nodeInEditMode==null)
{this._updateNarratorInfo();}},_onKeydownHandler:function(browserEvent)
{var aNode=this.get_activeNode();var args=this._raiseClientEvent('KeyDown','DataTreeNode',browserEvent,null,aNode);if(args&&args.get_cancel())
{$util.cancelEvent(browserEvent);return;}
if(this._nodeInEditMode==null)
{if(!$util.IsOpera)
{this.__internalKeyHandler(browserEvent);}}},__internalKeyHandler:function(browserEvent)
{var key=browserEvent.keyCode;switch(key)
{case Sys.UI.Key.up:case Sys.UI.Key.down:this._newSelectedNodes=[];var oldNode=this.get_activeNode();if(oldNode==null)
{oldNode=this.getNode(0);this.set_activeNode(oldNode,true);$util.cancelEvent(browserEvent);return;}
else
if(this._selectionType==1||this._selectionType==2)
{var beginOfContSel=this._contSelStartNode==null;if(browserEvent.shiftKey)
{if(oldNode.get_enabled())
{if(this._contSelStartNode==null)
{this._contSelStartNode=oldNode;}}
if((this._contSelDir==1&&key==Sys.UI.Key.down)||(this._contSelDir==2&&key==Sys.UI.Key.up))
{if(oldNode==this._contSelStartNode)
{this._contSelDir=0;}
else
{if(oldNode.get_enabled())
{this._newSelectedNodes=Array.clone(this._selectedNodes);var aNode=this._newSelectedNodes.pop();this._addRemoveSelection(this._selectedNodes,this._newSelectedNodes,aNode,false,true);}}}}}
var newNode=null;if(key==Sys.UI.Key.up)
{if(browserEvent.shiftKey&&this._contSelDir==0)this._contSelDir=1;newNode=oldNode._get_navigationUpNode();}
if(key==Sys.UI.Key.down)
{if(browserEvent.shiftKey&&this._contSelDir==0)this._contSelDir=2;newNode=oldNode._get_navigationDownNode();}
if(newNode!=null)
{this.set_activeNode(newNode,true);if(this._selectionType==1||this._selectionType==2)
{if(browserEvent.shiftKey&&this._selectionType==2)
{if((this._contSelDir==1&&key==Sys.UI.Key.up)||(this._contSelDir==2&&key==Sys.UI.Key.down))
{if(newNode.get_enabled())
{if(beginOfContSel)
{this._newSelectedNodes=[];this._newSelectedNodes.push(oldNode);this._newSelectedNodes.push(newNode);this._changeSelection(this._selectedNodes,this._newSelectedNodes,newNode,true);}
else
{this._newSelectedNodes=Array.clone(this._selectedNodes);this._newSelectedNodes.push(newNode);this._addRemoveSelection(this._selectedNodes,this._newSelectedNodes,newNode,true,true);}}}}
else
if(!browserEvent.ctrlKey)
{this._contSelDir=0;this._contSelStartNode=null;this._newSelectedNodes=[];if(newNode.get_enabled())
{this._newSelectedNodes.push(newNode);}
this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}}}
$util.cancelEvent(browserEvent);break;case Sys.UI.Key.space:var aNode=this.get_activeNode();if(aNode!=null&&aNode.get_enabled())
{if(this._selectionType==1||this._selectionType==2)
{this._selectNode(aNode,true,browserEvent);}}
$util.cancelEvent(browserEvent);break;case Sys.UI.Key.left:var aNode=this.get_activeNode();if(aNode!=null)
{if(aNode.get_expanded())
{aNode.set_expanded(false,true);}
else
{var parentNode=aNode.get_parentNode();if(parentNode)
{this.set_activeNode(parentNode,true);if(this._selectionType==1||this._selectionType==2)
{if(!browserEvent.ctrlKey)
{this._newSelectedNodes=[];this._newSelectedNodes.push(parentNode);this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}}}}}
else
{this.set_activeNode(this.getNode(0),true);}
$util.cancelEvent(browserEvent);break;case Sys.UI.Key.right:var aNode=this.get_activeNode();if(aNode!=null&&(aNode.get_isEmptyParent()||aNode.hasChildren()))
{if(aNode.get_expanded())
{var firstChild=aNode.get_childNode(0);if(firstChild)
{this.set_activeNode(firstChild,true);if(this._selectionType==1||this._selectionType==2)
{if(!browserEvent.ctrlKey)
{this._newSelectedNodes=[];this._newSelectedNodes.push(firstChild);this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}}}}
else
{aNode.set_expanded(true,true);}}
$util.cancelEvent(browserEvent);break;case Sys.UI.Key.enter:var aNode=this.get_activeNode();if(aNode!=null)
{aNode._toggleClicked();this._raiseClientEvent('NodeClick','DataTreeNode',browserEvent,null,aNode,aNode._get_address(),-1);var anchor=aNode.get_anchorElement();if(anchor!=null)
{var aForm=document.createElement('FORM');aForm.action=anchor.href;aForm.target=anchor.target;aForm.style.position="absolute";aForm.style.left="-50000px";aForm.style.top="-50000px";var aFormSubmit=document.createElement('INPUT');aFormSubmit.type="submit";aForm.appendChild(aFormSubmit);document.body.appendChild(aForm);aFormSubmit.focus();aForm.submit();document.body.removeChild(aForm);delete aForm;}
var fe=this.get_focusElement();if(fe!=null)fe.focus();}
$util.cancelEvent(browserEvent);break;case 113:if(this._nodeEditing.get_enableOnF2())
{var activeNode=this.get_activeNode();if(activeNode!=null)
{var nodeEditable=activeNode.get_editable();if(nodeEditable==2||(nodeEditable==0&&this._nodeEditing.get_enabled()))
{this._enterNodeEditing(activeNode);}}}
$util.cancelEvent(browserEvent);break;case Sys.UI.Key.home:var firstNode=this.getNode(0);if(firstNode!=null&&firstNode!=this.get_activeNode())
{this.set_activeNode(firstNode,true);if(this._selectionType==1||this._selectionType==2)
{this._newSelectedNodes=[];this._newSelectedNodes.push(firstNode);this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}}
$util.cancelEvent(browserEvent);break;case Sys.UI.Key.end:var aNode=this.getNode(0);if(aNode!=null)
{var nextNode=null;while(true)
{nextNode=aNode.get_nextNode();if(nextNode==null)break;aNode=nextNode;}
nextNode=null;while(true)
{nextNode=aNode._get_navigationDownNode();if(nextNode==null)break;aNode=nextNode;}
this.set_activeNode(aNode,true);if(this._selectionType==1||this._selectionType==2)
{this._newSelectedNodes=[];this._newSelectedNodes.push(aNode);this._changeSelection(this._selectedNodes,this._newSelectedNodes,true);}}
$util.cancelEvent(browserEvent);break;}},_onMouseDownHandler:function(browserEvent)
{this.__cancelBlur=true;},_onFocusHandler:function(browserEvent)
{var el=this.get_element();Infragistics.Utility.toggleCompoundClass(el,this._controlActiveCssClass,true);this.__controlActive=true;var aNode=this.get_activeNode();if(aNode==null)
{aNode=this.getNode(0);this.set_activeNode(aNode,true);}
else
{Infragistics.Utility.toggleCompoundClass(aNode.get_styleElement(),this._activeCssClassResolved(aNode),true);}
this._set_value($IG.DataTreeProps.ControlFocused,true);},_onBlurHandler:function(browserEvent)
{setTimeout(this.__createDelegate(this,this._onBlurHandlerFinal,[browserEvent]),10);},_onBlurHandlerFinal:function(browserEvent)
{if(this.__cancelBlur)
{this.__cancelBlur=false;}
else
{var el=this.get_element();Infragistics.Utility.toggleCompoundClass(el,this._controlActiveCssClass,false);this.__controlActive=false;var aNode=this.get_activeNode();if(aNode!=null)
{Infragistics.Utility.toggleCompoundClass(aNode.get_styleElement(),this._activeCssClassResolved(aNode),false);}
this._set_value($IG.DataTreeProps.ControlFocused,false);}},_hoverItem:function(node,val)
{if(this.get_enableHotTracking())
{var elem=node.get_styleElement();if(elem)
Infragistics.Utility.toggleCompoundClass(elem,this._hoverCssClassResolved(node),val);if(val)
this._raiseClientEvent('NodeHovered','DataTreeNode',null,null,node);else
this._raiseClientEvent('NodeUnhovered','DataTreeNode',null,null,node);}},_shouldSelect:function(item,e)
{return true;},_shouldHover:function(item,e)
{if(e!=null)
{var mainEl=this.get_element();var parent=e.target;while(parent&&parent!=mainEl)
{if(parent.getAttribute("mkr")=="dtnIcon"||parent.getAttribute("mkr")=="dtnContent")
{return true;}
parent=parent.parentNode;}}
return false;},_selectedCssClassResolved:function(node)
{var className=node.get_selectedCssClass();if(!className||className=="")
{className=this._nodeSelectedCssClass;}
return className;},_activeCssClassResolved:function(node)
{var className=node.get_activeCssClass();if(!className||className=="")
{className=this._nodeActiveCssClass;}
return className;},_hoverCssClassResolved:function(node)
{var className=node.get_hoverCssClass();if(!className||className=="")
{className=this._nodeHoverCssClass;}
return className;},_disabledCssClassResolved:function(node)
{var className=node.get_disabledCssClass();if(!className||className=="")
{className=this._nodeDisabledCssClass;}
return className;},_keyPressed:function(uiBehaviorsObj,key,currentElem,currentADR,item,evnt)
{if(!item)
return;var nextItem=null;switch(key)
{case Sys.UI.Key.up:nextItem=this._itemCollection._getPreviousItem(item,true,true,true);break;case Sys.UI.Key.down:nextItem=this._itemCollection._getNextItem(item,true,true,true);break;}
if(nextItem)
{uiBehaviorsObj.focus(nextItem,nextItem.get_anchorElement(),nextItem.get_element().getAttribute("adr"));this._cancelEvent(evnt);}},_ensureActiveVisible:function(parentNode)
{var aNode=this.get_activeNode();if(aNode!=null&&!aNode.get_visible())
{this.set_activeNode(parentNode,true);}},_removeNode:function(node)
{var cbo=this._callbackManager.createCallbackObject();cbo.serverContext.type="remove";cbo.serverContext.address=node._get_address();var parentNode=node.get_parentNode();if(parentNode==null)
{cbo.clientContext.parentNode=null;cbo.serverContext.displayChain="";}
else
if(parentNode!=null)
{cbo.clientContext.parentNode=parentNode;var nodeDisplayChain="";var parent2=parentNode.get_parentNode();if(parent2!=null)
{parentAddr=parent2._get_address();nodeDisplayChain=this._getDisplayChain(parent2,parentAddr);}
cbo.serverContext.displayChain=nodeDisplayChain;}
this._callbackManager.execute(cbo,true);},get_checkedNodes:function()
{return Array.clone(this._checkedNodes);},get_checkBoxMode:function()
{return this._checkBoxMode;},get_selectedNodes:function()
{return Array.clone(this._selectedNodes);},get_selectionType:function()
{return this._selectionType;},set_selectionType:function(newType)
{if(newType<0||newType>2)return;this._selectionType=newType;this._set_value($IG.DataTreeProps.NodeSelectionType,this._selectionType);},getNode:function(index)
{return this._itemCollection._getObjectByIndex(index);},getNodes:function()
{return this._itemCollection;},get_enableAjax:function()
{return this._get_value($IG.DataTreeProps.EnableAjax);},get_enableConnectorLines:function()
{return this._get_value($IG.DataTreeProps.EnableConnectorLines);},set_enableConnectorLines:function()
{return this._set_value($IG.DataTreeProps.EnableConnectorLines);},get_enableExpandImages:function()
{return this._get_value($IG.DataTreeProps.EnableExpandImages);},set_enableExpandImages:function()
{return this._set_value($IG.DataTreeProps.EnableExpandImages);},get_enableExpandOnClick:function()
{return this._get_value($IG.DataTreeProps.EnableExpandOnClick);},set_enableExpandOnClick:function()
{return this._set_value($IG.DataTreeProps.EnableExpandOnClick);},get_enableDropInsertion:function()
{return this._get_value($IG.DataTreeProps.EnableDropInsertion);},set_enableDropInsertion:function()
{return this._set_value($IG.DataTreeProps.EnableDropInsertion);},get_dragDropMode:function()
{return this._get_value($IG.DataTreeProps.DragMode);},set_dragDropMode:function()
{return this._set_value($IG.DataTreeProps.DragMode);},get_dataLoadingMessage:function()
{return this._get_value($IG.DataTreeProps.DataLoadingMessage);},get_enableHotTracking:function()
{return this._get_value($IG.DataTreeProps.EnableHotTracking,true);},set_enableHotTracking:function(value)
{this._set_value($IG.DataTreeProps.EnableHotTracking,value);},get_animationEquationType:function()
{return this._get_value($IG.DataTreeProps.AnimationEquationType,false);},set_animationEquationType:function(value)
{this._set_value($IG.DataTreeProps.AnimationEquationType,value);},get_animationDuration:function()
{return this._get_value($IG.DataTreeProps.AnimationDuration,false);},set_animationDuration:function(value)
{this._set_value($IG.DataTreeProps.AnimationDuration,value);},__dragStart:function(uiBehavior,item,ddb,evntArgs)
{var ddm=evntArgs.get_manager();var src=ddm.get_sourceElement();if(src.tagName!="SPAN")
{while(true)
{if(!src||src.tagName=="LI")
{evntArgs.set_cancel(true);return;}
if(src.tagName=="SPAN")
break;src=src.parentNode;}}
this._raiseClientEvent("DragStart","DataTreeDragDrop",null,item,evntArgs.get_manager());var elem=item.get_element();var clone=elem.cloneNode(true);clone.className=this._element.className+clone.className+" igt_group";clone.style.height=elem.offsetHeight+"px";clone.style.width=elem.offsetWidth+"px";ddb.set_dragMarkup(clone);ddb.set_moveCursor("default",false);ddb.set_copyCursor("help",false);ddb.set_dragDropMode(this.get_dragDropMode());ddm.set_dataText(item.get_text());this.__dropInsertBefore=false;},__dragEnter:function(uiBehavior,item,ddb,evntArgs)
{if(item==null)
return;if(!this.__insertDiv&&this.get_enableDropInsertion())
{this.__insertDiv=document.createElement("div");this.__insertDiv.style.height="1px";this.__insertDiv.style.backgroundColor="black";this.__insertDiv.style.position="absolute";this.__insertDiv.style.width=this._element.clientWidth+"px";this.__insertDiv.style.display="none";this._element.appendChild(this.__insertDiv);}
this._raiseClientEvent("DragEnter","DataTreeDragDrop",null,item,evntArgs.get_manager());var elem=item.get_textElement();if(item&&!item.get_selected()&&elem)
Infragistics.Utility.toggleCompoundClass(elem,this._selectedCssClassResolved(item),true);},__dragMove:function(uiBehavior,item,ddb,evntArgs)
{this._raiseClientEvent("DragMove","DataTreeDragDrop",null,item,evntArgs.get_manager());if(!item)
return;var elem=item.get_element();var pos=Sys.UI.DomElement.getLocation(elem);if(this.get_enableDropInsertion())
{if(evntArgs.get_y()<pos.y+8)
{this.__dropInsertBefore="Before";this.__insertDiv.style.top=pos.y+"px";this.__insertDiv.style.display="";}
else
{this.__insertDiv.style.display="none";this.__dropInsertBefore="On";}}},__dragLeave:function(uiBehavior,item,ddb,evntArgs)
{if(item==null)
return;this._raiseClientEvent("DragLeave","DataTreeDragDrop",null,item,evntArgs.get_manager());var elem=item.get_textElement();if(item&&!item.get_selected()&&elem)
Infragistics.Utility.toggleCompoundClass(elem,this._selectedCssClassResolved(item),false);if(this.__insertDiv)
this.__insertDiv.style.display="none";},__dragEnd:function(uiBehavior,item,ddb,evntArgs)
{this._raiseClientEvent("DragEnd","DataTreeDragDrop",null,item,evntArgs.get_manager());if(this.__insertDiv)
this.__insertDiv.style.display="none";},__dragCancel:function(uiBehavior,item,ddb,evntArgs)
{if(evntArgs!=null)
this._raiseClientEvent("DragCancel","DataTreeDragDrop",null,item,evntArgs.get_manager());if(this.__insertDiv)
this.__insertDiv.style.display="none";},__drop:function(uiBehavior,item,ddb,evntArgs)
{var ddm=evntArgs.get_manager();var elem=ddm.get_sourceElement();var sourceElement=ddm.get_source().element;var sourceNode=ddm.get_dataObject();var destNode=item;var dataText="";var sourceId;if(sourceNode._address)
sourceId=sourceNode._address;else
if(sourceElement.id)
sourceId=sourceElement.id;dataText=ddm.get_dataText();var destId;if(item)
destId=item._address;else
{destId="Main";this.__dropInsertBefore="0";}
var args=this._raiseClientEvent('Drop','DataTreeDragDrop',null,null,destNode,evntArgs.get_manager());if(args&&args.get_cancel())
return;var dragDropEffect=ddm.get_dragDropEffect();var sourceTreeId=ddm._currentSource.element.id;if(ddm._currentSource.object!=null)
sourceTreeId=ddm._currentSource.object._uniqueID
var sourceString="NodeDrop|"+sourceTreeId+"|"+sourceId+"|"+dataText+"|";var destString=this._uniqueID+"|"+destId+"|"+this.__dropInsertBefore+"|"+dragDropEffect;var dropString=sourceString+destString;this._raiseClientEvent("Dropped",'DataTreeDragDrop',null,destNode,evntArgs.get_manager());__igdoPostBack(this._uniqueID,dropString);},_ensureFlags:function()
{$IG.WebDataTree.callBaseMethod(this,"_ensureFlag");this._ensureFlag($IG.ClientUIFlags.Enabled,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Hoverable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Draggable,$IG.DefaultableBoolean.False);this._ensureFlag($IG.ClientUIFlags.Droppable,$IG.DefaultableBoolean.False);this._ensureFlag($IG.ClientUIFlags.Selectable,$IG.DefaultableBoolean.True);},_get_maingroup:function()
{return this._element.firstChild;},_enterNodeEditing:function(node)
{if(!node)return;if(!node.get_enabled())return;if(!node.get_visible())return;if(this._nodeInEditMode!=null)
this._exitNodeEditing(true);setTimeout(this.__createDelegate(this,this.__internalEnterEditMode,[node]),1);},_exitNodeEditing:function(update)
{var node=this._nodeInEditMode,editor=this._nodeEditor;if(!node||!editor||!editor.get_value)
return;var args=this._raiseClientEvent("NodeEditingExiting","CancelNodeEditing",null,null,node);if(args==null||!args.get_cancel())
{if(update)
{var newValue=editor.get_value();var args=this._raiseClientEvent("NodeEditingTextChanging","TextChange",null,null,node,newValue);if(args==null||!args.get_cancel())
{node.set_text(newValue);this._raiseClientEvent("NodeEditingTextChanged","TextChange",null,null,node,newValue);}}
editor.hideEditor();this._raiseClientEvent("NodeEditingExited","NodeEditing",null,null,node);this._nodeInEditMode=null;this._currentEditor=null;var fa=this.get_focusElement();if(fa!=null&&fa.focus)fa.focus();}},_get_isInNodeEditing:function(node)
{if(node==null||!node||!node._get_address)return false;if(this._nodeInEditMode==null)return false;return(this._nodeInEditMode._get_address()==node._get_address());},__internalEnterEditMode:function(node)
{if(this._nodeInEditMode!=null)return;var editor=this._nodeEditor;if(editor==null)return;var elm=node.get_textElement();if(elm==null||elm.tagName!="A")return;var args=this._raiseClientEvent("NodeEditingEntering","CancelNodeEditing",null,null,node);if(args==null||!args.get_cancel())
{var container=this.get_element();this._nodeInEditMode=node;this._currentEditor=editor;var bounds=Sys.UI.DomElement.getBounds(elm);editor.set_value(node.get_text());var iEditorCssClass=this._internalNodeEditorCssClass;if(this._nodeEditing.get_internalEditorCssClass())
iEditorCssClass=this._nodeEditing.get_internalEditorCssClass();editor.showEditor(bounds.x,bounds.y,bounds.width,bounds.height,iEditorCssClass,container);this._raiseClientEvent("NodeEditingEntered","NodeEditing",null,null,node);}},__createDelegate:function(instance,method,args)
{return function()
{return method.apply(instance,args);}},__editorLostFocus:function(evnt)
{if(this._currentEditor!=null)
{this._currentEditor._originalNotifyLostFocus(evnt);if(evnt!=null&&evnt.type=="keydown")
{var key=evnt.keyCode;var node=this._nodeInEditMode;if(key==Sys.UI.Key.tab)
{this._exitNodeEditing(true);}
else if(key==Sys.UI.Key.esc)
{this._exitNodeEditing(false);}
else if(key==Sys.UI.Key.enter)
{this._exitNodeEditing(true);$util.cancelEvent(evnt);}
if(this._activation)
{var activeCell=this._activation.get_activeCell();if(activeCell)
activeCell.get_element().focus();}}
else
this._exitNodeEditing(true);}},dispose:function()
{$clearHandlers(this.get_element());$IG.WebDataTree.callBaseMethod(this,'dispose');}}
$IG.WebDataTree.registerClass('Infragistics.Web.UI.WebDataTree',$IG.NavControl);$IG.NodeSettings=function(obj,element,props,control,mkrAttribute)
{var csm=obj?new $IG.ObjectClientStateManager(props[0]):null;$IG.NodeSettings.initializeBase(this,[obj,element,props,control,csm]);this._mkrAttribute=mkrAttribute;}
$IG.NodeSettings.prototype={get_selectedCssClass:function()
{return this._get_value($IG.NodeSettingsProps.SelectedCssClass);},set_selectedCssClass:function(value)
{this._set_value($IG.NodeSettingsProps.SelectedCssClass,value);},get_hoverCssClass:function()
{return this._get_value($IG.NodeSettingsProps.HoverCssClass);},set_hoverCssClass:function(value)
{this._set_value($IG.NodeSettingsProps.HoverCssClass,value);}}
$IG.NodeSettings.registerClass('Infragistics.Web.UI.NodeSettings',$IG.ObjectBase);$IG.NodeEditing=function(obj,element,props,control,mkrAttribute)
{var csm=obj?new $IG.ObjectClientStateManager(props[0]):null;$IG.NodeEditing.initializeBase(this,[obj,element,props,control,csm]);this._mkrAttribute=mkrAttribute;}
$IG.NodeEditing.prototype={enterNodeEditing:function(node)
{this._owner._enterNodeEditing(node);},exitNodeEditing:function(update)
{this._owner._exitNodeEditing(update);},get_isInNodeEditing:function(node)
{return this._owner._get_isInNodeEditing(node);},get_enabled:function()
{return this._get_value($IG.NodeEditingProps.Enabled,true);},set_enabled:function(value)
{return this._set_value($IG.NodeEditingProps.Enabled,value);},get_enableOnF2:function()
{return this._get_value($IG.NodeEditingProps.EnableOnF2,true);},set_enableOnF2:function(value)
{return this._set_value($IG.NodeEditingProps.EnableOnF2,value);},get_enableOnDoubleClick:function()
{return this._get_value($IG.NodeEditingProps.EnableOnDoubleClick,true);},set_enableOnDoubleClick:function(value)
{return this._set_value($IG.NodeEditingProps.EnableOnDoubleClick,value);},get_internalEditorCssClass:function()
{return this._get_value($IG.NodeEditingProps.InternalEditorCssClass);},set_internalEditorCssClass:function(value)
{return this._set_value($IG.NodeEditingProps.InternalEditorCssClass,value);}}
$IG.NodeEditing.registerClass('Infragistics.Web.UI.NodeEditing',$IG.ObjectBase);$IG.NodeCollection=function(control,clientStateManager,index,manager)
{$IG.NodeCollection.initializeBase(this,[control,clientStateManager,index,manager]);}
$IG.NodeCollection.prototype={_createNewCollection:function()
{var nodes=new $IG.NodeCollection(this._control,this._csm,this._index,this._manager);nodes._ownerNode=this;return nodes;},getNode:function(index)
{if(index>=0&&index<this.get_length())
return this._nodes[index];return null;}}
$IG.NodeCollection.registerClass('Infragistics.Web.UI.NodeCollection',$IG.NavItemCollection);$IG.TreeInternalEditor=function(parentElement)
{var input=document.createElement('INPUT');input.style.position='absolute';parentElement.appendChild(input);$IG.TreeInternalEditor.initializeBase(this,[input]);this.hideEditor();}
$IG.TreeInternalEditor.prototype={get_value:function()
{return this._element.value;},set_value:function(val)
{this._element.value=val;},showEditor:function(left,top,width,height,cssClass,parent)
{var input=this._element;if(parent&&input.parentNode!=parent)
{input.parentNode.removeChild(input);parent.appendChild(input);}
style=input.style;style.top=top+'px';style.left=left+'px';style.display='';style.visibility='visible';if(cssClass)
input.className=cssClass;style.width=width+'px';style.height=height+'px';if(!this._hasLsnr)
{if(!this._onBlurFn)
{this._onBlurFn=Function.createDelegate(this,this._onBlurHandler);this._onKeyFn=Function.createDelegate(this,this._onKeyDownHandler);}
this._hasLsnr=true;$addHandler(input,'blur',this._onBlurFn);$addHandler(input,'keydown',this._onKeyFn);}
try
{input.focus();input.select();}catch(e){}},notifyLostFocus:function(evnt)
{},get_displayText:function(val)
{return this._element.value;},hideEditor:function()
{var input=this._element;input.style.display='none';input.style.visibility='hidden';if(this._hasLsnr)
{$removeHandler(input,'blur',this._onBlurFn);$removeHandler(input,'keydown',this._onKeyFn);}
this._hasLsnr=false;},_onKeyDownHandler:function(evnt)
{var key=evnt.keyCode;if(key==Sys.UI.Key.tab||key==Sys.UI.Key.esc||key==Sys.UI.Key.enter)
this.notifyLostFocus(evnt);},_onBlurHandler:function(evnt)
{this.notifyLostFocus(evnt);},dispose:function()
{$clearHandlers(this.get_element());var parentEl=this.get_element().parentNode;parentEl.removeChild(this.get_element());$IG.TreeInternalEditor.callBaseMethod(this,'dispose');}}
$IG.TreeInternalEditor.registerClass('Infragistics.Web.UI.TreeInternalEditor',Sys.UI.Control);$IG.DataTreeActivationEventArgs=function()
{$IG.DataTreeActivationEventArgs.initializeBase(this);}
$IG.DataTreeActivationEventArgs.prototype={getOldActiveNode:function()
{return this._props[2];},getNewActiveNode:function()
{return this._props[3];}}
$IG.DataTreeActivationEventArgs.registerClass('Infragistics.Web.UI.DataTreeActivationEventArgs',$IG.CancelEventArgs);$IG.DataTreeSelectionEventArgs=function()
{$IG.DataTreeSelectionEventArgs.initializeBase(this);}
$IG.DataTreeSelectionEventArgs.prototype={getOldSelectedNodes:function()
{return this._props[2];},getNewSelectedNodes:function()
{return this._props[3];}}
$IG.DataTreeSelectionEventArgs.registerClass('Infragistics.Web.UI.DataTreeSelectionEventArgs',$IG.CancelEventArgs);$IG.DataTreeCheckBoxSelectionEventArgs=function()
{$IG.DataTreeCheckBoxSelectionEventArgs.initializeBase(this);}
$IG.DataTreeCheckBoxSelectionEventArgs.prototype={getOldCheckedNodes:function()
{return this._props[2];},getNewCheckedNodes:function()
{return this._props[3];}}
$IG.DataTreeCheckBoxSelectionEventArgs.registerClass('Infragistics.Web.UI.DataTreeCheckBoxSelectionEventArgs',$IG.EventArgs);$IG.DataTreeNodeEventArgs=function()
{$IG.DataTreeNodeEventArgs.initializeBase(this);}
$IG.DataTreeNodeEventArgs.prototype={getNode:function()
{return this._props[2];},_getPostArgs:function()
{var _nodeAddr=this.getNode()._get_address();var _mouseButton=this._props[4];return':'+_nodeAddr+":"+_mouseButton;}}
$IG.DataTreeNodeEventArgs.registerClass('Infragistics.Web.UI.DataTreeNodeEventArgs',$IG.CancelEventArgs);$IG.DataTreeDragDropEventArgs=function()
{$IG.DataTreeDragDropEventArgs.initializeBase(this);}
$IG.DataTreeDragDropEventArgs.prototype={get_node:function()
{return this._props[2];},get_dragDropManager:function()
{return this._props[3];}}
$IG.DataTreeDragDropEventArgs.registerClass('Infragistics.Web.UI.DataTreeDragDropEventArgs',$IG.CancelEventArgs);$IG.CancelNodeEditingEventArgs=function()
{$IG.CancelNodeEditingEventArgs.initializeBase(this);}
$IG.CancelNodeEditingEventArgs.prototype={getNode:function()
{return this._props[2];}}
$IG.CancelNodeEditingEventArgs.registerClass('Infragistics.Web.UI.CancelNodeEditingEventArgs',$IG.CancelEventArgs);$IG.NodeEditingEventArgs=function()
{$IG.NodeEditingEventArgs.initializeBase(this);}
$IG.NodeEditingEventArgs.prototype={getNode:function()
{return this._props[2];}}
$IG.NodeEditingEventArgs.registerClass('Infragistics.Web.UI.NodeEditingEventArgs',$IG.EventArgs);$IG.TextChangeEventArgs=function()
{$IG.TextChangeEventArgs.initializeBase(this);}
$IG.TextChangeEventArgs.prototype={getNode:function()
{return this._props[2];},getNewText:function()
{return this._props[3];}}
$IG.TextChangeEventArgs.registerClass('Infragistics.Web.UI.TextChangeEventArgs',$IG.CancelEventArgs);