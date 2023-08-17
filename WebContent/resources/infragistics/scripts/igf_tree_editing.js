// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.1.20091.1038

$IG.TreeEditing=function(obj,objProps,control)
{$IG.TreeEditing.initializeBase(this,[obj,objProps,control]);this._tree=this._owner;this._nodesToDelete=new $IG.TreeAffectedItems();this._addInNode=null;this._addNodeValue=null;control._treeUtil._registerEventListener(control,"NodeValueChanged",Function.createDelegate(this,this._nodeValueChanged));control._treeUtil._registerEventListener(control,"NodesDeleted",Function.createDelegate(this,this._onNodesDeleting));control._treeUtil._registerEventListener(control,"NodeAdded",Function.createDelegate(this,this._onNodeAdding));}
$IG.TreeEditing.prototype={commit:function()
{this._commitUpdates();this._commitDeletes();this._commitAdds();},_commitUpdates:function()
{for(var actionKey in this._actions)
{var action=this._actions[actionKey];if(!action._commited)
{var node=action._object;var eventArgs=new $IG.CancelUpdateNodeEventArgs(this,node);if(this._clientEvents["NodeUpdating"])
this._owner._raiseSenderClientEventStart(this,this._clientEvents["NodeUpdating"],eventArgs);if(!eventArgs.get_cancel())
{action._commited=true;if(this._clientEvents["NodeUpdating"])
this._get_owner()._raiseClientEventEnd(eventArgs);}
else
{var node=this.tree.getItems()._getObjectByAdr(action.adr);node._set_value_internal(action.oldValue);delete this._actions[actionKey];}}}},_commitDeletes:function()
{if(this._nodesToDelete.get_length()>0)
{var NodesDeletingEventArgs=new $IG.CancelNodesDeletingEventArgs(this,this._nodesToDelete);this._owner._raiseSenderClientEventStart(this,this._clientEvents["NodesDeleting"],NodesDeletingEventArgs);if(!NodesDeletingEventArgs.get_cancel())
{nodes=NodesDeletingEventArgs.get_nodes();var numNodes=nodes.get_length();var node;var nodeAddresses=new Array();for(var i=numNodes-1;i>=0;i--)
{node=nodes.getItem(i);nodeAddresses[i]=node._get_address();}
this._get_owner()._actionList.add_transaction(new $IG.TreeAction("DeleteNode","NodeEditing",this._get_owner(),nodeAddresses));this._get_owner()._raiseClientEventEnd(NodesDeletingEventArgs);}
this._nodesToDelete.clear();}},_commitAdds:function()
{if(this._addNodeValue)
{var eventArgs=new $IG.CancelAddNodeEventArgs(this,this._addInNode,this._addNodeValue);this._owner._raiseSenderClientEventStart(this,this._clientEvents["NodeAdding"],eventArgs);if(!eventArgs.get_cancel())
{this._get_owner()._actionList.add_transaction(new $IG.TreeAction("AddNode","TreeEditing",this._get_owner().Editing,{"adr":this._addInNode._get_address(),"value":this._addNodeValue}));this._get_owner()._raiseClientEventEnd(eventArgs);this._addInNode=null;this._addNodeValue=null;}}},_nodeValueChanged:function(args)
{var action=this._retrieveUpdateAction(args.node);action._addUpdateNode(args.node,args.oldValue);this.commit();},_actions:{},_retrieveUpdateAction:function(node)
{var nodeAddress=node._get_address();var actionKey=nodeAddress;var action=this._actions[actionKey];if(!action)
{action=new $IG.UpdateNodeAction("UpdateNode",this.get_name(),node,{"adr":node._get_address(),"oldValue":node.get_value()});this._actions[actionKey]=action;}
this._owner._actionList.add_transaction(action);return action;},_onNodesDeleting:function(args)
{this._nodesToDelete.add(args.node);if(args.commit)
this.commit();},_onNodeAdding:function(args)
{this._addInNode=args.node;this._addNodeValue=args.nodeValue;this.commit();},_initializeComplete:function()
{var deletionExemptNodeIDs=this._get_clientOnlyValue("den");if(this._clientEvents["NodesDeleted"]&&deletionExemptNodeIDs)
{var deletionExemptNodes=Sys.Serialization.JavaScriptSerializer.deserialize(deletionExemptNodeIDs);var NodesDeletedEventArgs=new $IG.NodesDeletedEventArgs(this,deletionExemptNodes);this._owner._raiseSenderClientEventStart(this,this._clientEvents["NodesDeleted"],NodesDeletedEventArgs);}
var updatedNodesIDs=this._get_clientOnlyValue("unp");if(updatedNodesIDs&&this._clientEvents["NodeUpdated"])
{var updNodes=Sys.Serialization.JavaScriptSerializer.deserialize(updatedNodesIDs);for(var i=0;i<updNodes.length;i++)
{var node=this._tree.getItems()._getObjectByAdr(updNodes[i]);var nodeUpdatedEventArgs=new $IG.NodeUpdatedEventArgs(this,node);this._owner._raiseSenderClientEvent(this,this._clientEvents["NodeUpdated"],nodeUpdatedEventArgs);}}},_responseComplete:function(callbackObject,responseOptions)
{if(responseOptions.updatedNodeAdr&&responseOptions.updatedNodeValue)
{var node=this._tree.getItems()._getObjectByAdr(responseOptions.updatedNodeAdr);node._set_value_internal(responseOptions.updatedNodeValue);if(this._clientEvents["NodeUpdated"])
{var nodeUpdatedEventArgs=new $IG.NodeUpdatedEventArgs(this,node);this._owner._raiseSenderClientEvent(this,this._clientEvents["NodeUpdated"],nodeUpdatedEventArgs);}}}}
$IG.TreeEditing.registerClass('Infragistics.Web.UI.TreeEditing',$IG.TreeBehaviorContainer,$IG.IEditingBehavior);$IG.UpdateNodeAction=function(type,ownerName,object,value,tag)
{$IG.UpdateNodeAction.initializeBase(this,[type,ownerName,object,value,tag]);}
$IG.UpdateNodeAction.prototype={get_value:function()
{return this._value;},_addUpdateNode:function(node,oldValue)
{var updNodeAction=this.get_value();if(!updNodeAction)
{updNodeAction={"adr":node._get_address()};this._value=updNodeAction;}
updNodeAction.value=node.get_value();updNodeAction.oldValue=oldValue;updNodeAction._commited=false;}}
$IG.UpdateNodeAction.registerClass('Infragistics.Web.UI.UpdateNodeAction',$IG.TreeAction);$IG.TreeAffectedItems=function()
{$IG.TreeAffectedItems.initializeBase(this);this._lsize=0;this._items=new Array();}
$IG.TreeAffectedItems.prototype={add:function(item)
{if(item==null)return;this._lsize++;this._items[(this._lsize-1)]=item;},remove:function(index)
{if(index<0||index>this._items.length-1)return;this._items[index]=null;for(var i=index;i<=this._lsize;i++)
this._items[i]=this._items[i+1];this._lsize--;},indexOf:function(item)
{for(var i=0;i<this._lsize;i++)
{if(item==this._items[i])
return i;}
return-1;},isEmpty:function()
{return this._lsize==0;},get_length:function()
{return this._lsize},getItem:function(index)
{return this._items[index];},clear:function()
{for(var i=0;i<this._lsize;i++)
this._items[i]=null;this._lsize=0;},clone:function()
{var c=new TreeAffectedItems();for(var i=0;i<this._lsize;i++)
c.add(this._items[i]);return c;}}
$IG.TreeAffectedItems.registerClass('Infragistics.Web.UI.TreeAffectedItems');$IG.CancelNodesDeletingEventArgs=function(deleting,nodes)
{$IG.CancelNodesDeletingEventArgs.initializeBase(this,[deleting]);this._nodes=nodes;}
$IG.CancelNodesDeletingEventArgs.prototype={get_nodes:function()
{return this._nodes;}}
$IG.CancelNodesDeletingEventArgs.registerClass('Infragistics.Web.UI.CancelNodesDeletingEventArgs',$IG.CancelBehaviorEventArgs);$IG.NodesDeletedEventArgs=function(deleting,deletionExemptNodeAddresses)
{$IG.NodesDeletedEventArgs.initializeBase(this,[deleting]);this._deletionExemptNodeAddresses=deletionExemptNodeAddresses;}
$IG.NodesDeletedEventArgs.prototype={get_canceled_nodeAddresses:function()
{return this._deletionExemptNodeAdresses;}}
$IG.NodesDeletedEventArgs.registerClass('Infragistics.Web.UI.NodesDeletedEventArgs',$IG.EventArgs);$IG.CancelAddNodeEventArgs=function(addNewNode,node,nodeValue)
{$IG.CancelAddNodeEventArgs.initializeBase(this,[addNewNode]);this._node=node;this._nodeValue=nodeValue;}
$IG.CancelAddNodeEventArgs.prototype={get_node:function()
{return this._node;},get_nodeValue:function()
{return this._nodeValue;}}
$IG.CancelAddNodeEventArgs.registerClass('Infragistics.Web.UI.CancelAddNodeEventArgs',$IG.CancelBehaviorEventArgs);$IG.CancelUpdateNodeEventArgs=function(updating,node)
{$IG.CancelUpdateNodeEventArgs.initializeBase(this,[updating]);this._node=node;}
$IG.CancelUpdateNodeEventArgs.prototype={get_node:function()
{return this._node;}}
$IG.CancelUpdateNodeEventArgs.registerClass('Infragistics.Web.UI.CancelUpdateNodeEventArgs',$IG.CancelBehaviorEventArgs);$IG.NodeUpdatedEventArgs=function(node)
{$IG.NodeUpdatedEventArgs.initializeBase(this);this._node=node;}
$IG.NodeUpdatedEventArgs.prototype={get_node:function()
{return this._node;}}
$IG.NodeUpdatedEventArgs.registerClass('Infragistics.Web.UI.NodeUpdatedEventArgs',$IG.EventArgs);