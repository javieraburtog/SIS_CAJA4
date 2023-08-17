// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");var $IG=Infragistics.Web.UI;$IG.ClientStateManagerBase=function(props)
{this._items=props;this._transactionList=null;}
$IG.ClientStateManagerBase.prototype={get_value:function(id)
{},set_value:function(id,value){},get_transactionList:function()
{return this._transactionList.get_list();},get_clientState:function()
{return this._items;}}
$IG.ClientStateManagerBase.registerClass('Infragistics.Web.UI.ClientStateManagerBase');$IG.ObjectClientStateManager=function(props)
{$IG.ObjectClientStateManager.initializeBase(this,[props]);this._transactionList=new $IG.ObjectTransactionList();}
$IG.ObjectClientStateManager.prototype={get_value:function(id,isBool)
{var index=id[0];var defaultVal=id[1];var val=this._transactionList.get_value(index);if(val==null)
{val=this._items[0][index]
if(val==null)
val=defaultVal;}
if(isBool)
{if(val==0)
val=false;else if(val==1)
val=true;}
return val;},get_clientOnlyValue:function(propName)
{return this.__getExraProp(propName,"c");},get_occasionalProperty:function(propName)
{return this.__getExraProp(propName,"o");},set_occasionalProperty:function(propName,val)
{var prop=this.__getExraProp(propName,"o");if(!$util.compare(prop,val))
this._transactionList.add_transaction(val,propName);else
this._transactionList.remove_transaction(propName);},__getExraProp:function(propName,key)
{var item=this._items[1];if(item!=null)
item=item[key];return item?item[propName]:null;},set_value:function(id,val)
{var index=id[0];var defaultVal=id[1];if(typeof(val)=="boolean")
val=(val)?1:0;var item=this._items[0][index];if(item==null)
item=defaultVal;if(!$util.compare(item,val))
this._transactionList.add_transaction(val,index);else
this._transactionList.remove_transaction(index);},get_serverProps:function(vse)
{if(vse)
{var props=[];props.push(this._items[0]);if(this._items[1]!=null&&this._items[1]["o"]!=null)
props.push(this._items[1]["o"]);return props;}
else
return null;}}
$IG.ObjectClientStateManager.registerClass('Infragistics.Web.UI.ObjectClientStateManager',$IG.ClientStateManagerBase);$IG.CollectionClientStateManager=function(props)
{$IG.CollectionClientStateManager.initializeBase(this,[props]);this._transactionList=new $IG.CollectionTransactionList();}
$IG.CollectionClientStateManager.prototype={get_value:function(id,isBool,address)
{var index=id[0];var defaultVal=id[1];var val=this._transactionList.get_value(address,index);if(val==null)
{val=this._items[address][0][index]
if(val==null)
val=defaultVal;}
if(isBool)
{if(val==0)
val=false;else if(val==1)
val=true;}
return val;},get_clientOnlyValue:function(propName,adr)
{return this.__getExraProp(propName,adr,"c");},get_occasionalProperty:function(propName,adr)
{var prop=this._transactionList.get_value(adr,propName);if(prop==null)
prop=this.__getExraProp(propName,adr,"o");return prop;},set_occasionalProperty:function(propName,val,adr)
{var prop=this.__getExraProp(propName,adr,"o");if(!$util.compare(prop,val))
this._transactionList.add_transaction(adr,val,propName);else
this._transactionList.remove_transaction(adr,propName);},__getExraProp:function(propName,adr,key)
{var item=this._items[adr];if(item!=null)
{item=item[1]
if(item!=null)
{item=item[key];}
if(item!=null)
return item[propName];}
return null;},set_value:function(id,val,address)
{var index=id[0];var defaultVal=id[1];if(typeof(val)=="boolean")
val=(val)?1:0;var item=this._items[address][0][index]
if(item==null)
item=defaultVal;if(!$util.compare(item,val))
this._transactionList.add_transaction(address,val,index);else
this._transactionList.remove_transaction(address,index);},set_itemProps:function(adr,props)
{this._items[adr]=props;},get_serverProps:function(address)
{var props=[];var item=this._items[address];props.push(item[0]);if(item[1]!=null&&item[1]["o"]!=null)
props.push(item[1]["o"]);return props;}}
$IG.CollectionClientStateManager.registerClass('Infragistics.Web.UI.CollectionClientStateManager',$IG.ClientStateManagerBase);$IG.TransactionListBase=function()
{this._items={};this._orderedList={};this._count=0;}
$IG.TransactionListBase.prototype={add_transaction:function()
{},remove_transaction:function()
{},get_value:function()
{},get_list:function()
{return this._orderedList;}}
$IG.TransactionListBase.registerClass('Infragistics.Web.UI.TransactionListBase');$IG.ObjectTransactionList=function()
{$IG.ObjectTransactionList.initializeBase(this);}
$IG.ObjectTransactionList.prototype={add_transaction:function(value,propsIndex)
{if(this._items[propsIndex]!=null&&this._items[propsIndex].length>0)
delete this._orderedList[this._items[propsIndex][0]];this._items[propsIndex]=[this._count,value];this._orderedList[this._count]=[propsIndex,value];this._count++;},remove_transaction:function(propsIndex)
{var item=this._items[propsIndex];if(item)
{var index=item[0];delete this._orderedList[index];delete this._items[propsIndex];}},get_value:function(propsIndex)
{var item=this._items[propsIndex];if(item!=null)
return item[1];return null;}}
$IG.ObjectTransactionList.registerClass('Infragistics.Web.UI.ObjectTransactionList',$IG.TransactionListBase);$IG.CollectionTransactionList=function()
{$IG.CollectionTransactionList.initializeBase(this);}
$IG.CollectionTransactionList.prototype={add_transaction:function(adr,value,propsIndex)
{var item=this._items[adr];if(!item)
item=this._items[adr]=[];else
{if(item[propsIndex]!=null&&item[propsIndex].length>0)
delete this._orderedList[item[propsIndex][0]];}
item[propsIndex]=[this._count,value];this._orderedList[this._count]=[adr,propsIndex,value];this._count++;},remove_transaction:function(adr,propsIndex)
{var item=this._items[adr];if(item!=null)
{item=item[propsIndex];if(item!=null)
{var index=item[0];delete this._orderedList[index];delete this._items[adr][propsIndex];}}},get_value:function(adr,propsIndex)
{var item=this._items[adr];if(item!=null)
{item=item[propsIndex]
if(item!=null)
return item[1];}
return null;}}
$IG.CollectionTransactionList.registerClass('Infragistics.Web.UI.CollectionTransactionList',$IG.TransactionListBase);