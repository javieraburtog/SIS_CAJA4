// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");$IG.ObjectBaseProps=new function()
{this.Count=0;};$IG.ObjectBase=function(adr,element,props,owner,csm)
{this._props=props;this._element=element;this._owner=owner;this._address=adr;if(element)
element._object=this;this._csm=csm;$IG.ObjectBase.initializeBase(this);}
$IG.ObjectBase.prototype={get_element:function()
{return this._element;},set_element:function(val)
{this._element=val;},_get_owner:function(){return this._owner;},_set_owner:function(value){this._owner=value;},_get_address:function(){return this._address;},_set_address:function(val){this._address=val;},_createObjects:function(objectManager)
{},_createCollections:function(collectionsManager)
{},_set_value:function(index,value)
{if(this._csm)
this._csm.set_value(index,value,this._address);},_get_value:function(index,isBool)
{return this._csm?this._csm.get_value(index,isBool,this._address):null;},_get_clientOnlyValue:function(propName)
{return this._csm?this._csm.get_clientOnlyValue(propName,this._address):null;},_get_occasionalProperty:function(propName)
{return this._csm?this._csm.get_occasionalProperty(propName,this._address):null;},_set_occasionalProperty:function(propName,val)
{return this._csm?this._csm.set_occasionalProperty(propName,val,this._address):null;},_saveAdditionalClientState:function()
{},dispose:function()
{if(this._element)
this._element._object=null;this._element=null;this._owner=null;if(this._props)
{if(this._props.objectsManager)
this._props.objectsManager.dispose();if(this._props.collectionsManager)
this._props.collectionsManager.dispose();this._props=null;}
this._csm=null;$IG.ObjectBase.callBaseMethod(this,"dispose");}}
$IG.ObjectBase.registerClass('Infragistics.Web.UI.ObjectBase',Sys.Component);$IG.ControlObjectProps=new function()
{this.Flags=[$IG.ObjectBaseProps.Count+0,0];this.Count=$IG.ObjectBaseProps.Count+1;};$IG.UIObject=function(adr,element,props,owner,csm)
{this._flags=null;$IG.UIObject.initializeBase(this,[adr,element,props,owner,csm]);}
$IG.UIObject.prototype={_getFlags:function()
{if(this._flags==null)
{this.__flagHelper=new $IG.FlagsHelper();var key=[$IG.ObjectBaseProps.Count+0,this.__getDefaultFlags()]
this._flags=new $IG.FlagsObject(this._get_value(key),this);}
return this._flags;},__getDefaultFlags:function()
{if(this.__defaultFlags==null)
{this._ensureFlags();this.__defaultFlags=this.__flagHelper.calculateFlags();}
return this.__defaultFlags;},_updateFlags:function(flags)
{var key=[$IG.ObjectBaseProps.Count+0,this.__getDefaultFlags()]
this._set_value(key,flags)},_ensureFlags:function()
{},_ensureFlag:function(flag,val)
{this.__flagHelper.updateFlag(flag,val);},dispose:function()
{if(this._flags!=null)
this._flags.dispose();$IG.UIObject.callBaseMethod(this,"dispose");}}
$IG.UIObject.registerClass('Infragistics.Web.UI.UIObject',$IG.ObjectBase);$IG.ListItemProps=new function()
{this.KeyTag=[$IG.ControlObjectProps.Count+0,""];this.NavigateUrl=[$IG.ControlObjectProps.Count+1,""];this.Target=[$IG.ControlObjectProps.Count+2,""];this.Tooltip=[$IG.ControlObjectProps.Count+3,""];this.Count=$IG.ControlObjectProps.Count+4;};$IG.ListItem=function(adr,element,props,owner,csm,collection,parent)
{$IG.ListItem.initializeBase(this,[adr,element,props,owner,csm]);this._parent=parent;this._itemCollection=collection}
$IG.ListItem.prototype={_ensureFlags:function()
{$IG.ListItem.callBaseMethod(this,"_ensureFlag");this._ensureFlag($IG.ClientUIFlags.Hoverable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Selectable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Draggable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Droppable,$IG.DefaultableBoolean.True);},set_key:function(value)
{this._set_value($IG.ListItemProps.KeyTag,value);},get_key:function()
{return this._get_value($IG.ListItemProps.KeyTag);},set_navigateUrl:function(value)
{this._set_value($IG.ListItemProps.NavigateUrl,value);},get_navigateUrl:function()
{return this._get_value($IG.ListItemProps.NavigateUrl);},set_target:function(value)
{this._set_value($IG.ListItemProps.Target,value);},get_target:function()
{return this._get_value($IG.ListItemProps.Target);},set_tooltip:function(value)
{this._set_value($IG.ListItemProps.Tooltip,value);},get_tooltip:function()
{return this._get_value($IG.ListItemProps.Tooltip);},dispose:function()
{$IG.ListItem.callBaseMethod(this,"dispose");this._parent=null;this._itemCollection=null}}
$IG.ListItem.registerClass('Infragistics.Web.UI.ListItem',$IG.UIObject);$IG.DataItemProps=new function()
{this.DataPath=[$IG.ControlObjectProps.Count+0,null];this.Populated=[$IG.ControlObjectProps.Count+1,false];this.IsEmptyParent=[$IG.ControlObjectProps.Count+2,false];this.Count=$IG.ControlObjectProps.Count+3;};$IG.NavItemProps=new function()
{this.Text=[$IG.DataItemProps.Count+0,""];this.Value=[$IG.DataItemProps.Count+1,""];this.Key=[$IG.DataItemProps.Count+2,""];this.Count=$IG.DataItemProps.Count+3;};$IG.NavItem=function(adr,element,props,owner,csm,collection,parent)
{$IG.NavItem.initializeBase(this,[adr,element,props,owner,csm]);this._parent=parent;this._itemCollection=collection}
$IG.NavItem.prototype={_ensureFlags:function()
{$IG.NavItem.callBaseMethod(this,"_ensureFlag");this._ensureFlag($IG.ClientUIFlags.Hoverable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Selectable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Draggable,$IG.DefaultableBoolean.True);this._ensureFlag($IG.ClientUIFlags.Droppable,$IG.DefaultableBoolean.True);},set_dataPath:function(value)
{this._set_value($IG.DataItemProps.DataPath,value);},get_dataPath:function()
{return this._get_value($IG.DataItemProps.DataPath);},set_populated:function(value)
{this._set_value($IG.DataItemProps.Populated,value);},get_populated:function()
{return this._get_value($IG.DataItemProps.Populated,true);},set_isEmptyParent:function(value)
{this._set_value($IG.DataItemProps.IsEmptyParent,value);},get_isEmptyParent:function()
{return this._get_value($IG.DataItemProps.IsEmptyParent,true);},set_text:function(value)
{this._set_value($IG.NavItemProps.Text,value);},get_text:function()
{return this._get_value($IG.NavItemProps.Text);},set_valueString:function(value)
{this._set_value($IG.NavItemProps.Value,value);},get_valueString:function()
{return this._get_value($IG.NavItemProps.Value);},set_key:function(value)
{this._set_value($IG.NavItemProps.Key,value);},get_key:function()
{return this._get_value($IG.NavItemProps.Key);},getItems:function()
{return this._itemCollection;},get_selected:function()
{return this._getFlags().getSelected(this._owner);},set_selected:function(value)
{this._getFlags().setSelected(value);},get_enabled:function()
{return this._getFlags().getEnabled(this._owner);},set_enabled:function(value)
{this._getFlags().setEnabled(value);}}
$IG.NavItem.registerClass('Infragistics.Web.UI.NavItem',$IG.UIObject);$IG.FlagsHelper=function()
{this._flagsHT=[];};$IG.FlagsHelper.prototype={updateFlag:function(flag,val)
{this._flagsHT[flag]=val;},getBoolFlag:function(flag)
{var obj=this._flagsHT[flag];if(obj==null)
return false;else
return obj;},getDBFlag:function(flag)
{var obj=this._flagsHT[flag];if(obj==null)
return $IG.DefaultableBoolean.NotSet;else
return obj;},calcBoolFlag:function(flag)
{var val=this.getBoolFlag(flag);return(val)?flag:0;},calcDBFlag:function(flag)
{var val=this.getDBFlag(flag);return parseInt(flag*.5*val);},calculateFlags:function()
{var flags=0;flags+=this.calcDBFlag($IG.ClientUIFlags.Visible);flags+=this.calcDBFlag($IG.ClientUIFlags.Enabled);flags+=this.calcDBFlag($IG.ClientUIFlags.Selectable);flags+=this.calcBoolFlag($IG.ClientUIFlags.Selected);flags+=this.calcDBFlag($IG.ClientUIFlags.Hoverable);flags+=this.calcBoolFlag($IG.ClientUIFlags.Hovered);flags+=this.calcDBFlag($IG.ClientUIFlags.Editable);flags+=this.calcDBFlag($IG.ClientUIFlags.Focusable);flags+=this.calcBoolFlag($IG.ClientUIFlags.Focused);flags+=this.calcDBFlag($IG.ClientUIFlags.Draggable);flags+=this.calcDBFlag($IG.ClientUIFlags.Droppable);flags+=this.calcDBFlag($IG.ClientUIFlags.KBNavigable);return flags;}}
$IG.FlagsHelper.registerClass('Infragistics.Web.UI.FlagsHelper');$IG.FlagsObject=function(flags,object)
{this._flags=flags;this._object=object};$IG.FlagsObject.prototype={dispose:function()
{this._flags=null;this._object=null;},getVisible:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Visible,parent);},setVisible:function(val)
{this._setFlagValue($IG.ClientUIFlags.Visible,val);},getEnabled:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Enabled,parent);},setEnabled:function(val)
{this._setFlagValue($IG.ClientUIFlags.Enabled,val);},getSelectable:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Selectable,parent);},setSelectable:function(val)
{this._setFlagValue($IG.ClientUIFlags.Selectable,val);},getSelected:function()
{return this._getFlagValue($IG.ClientUIFlags.Selected,null,true);},setSelected:function(val)
{this._setFlagValue2($IG.ClientUIFlags.Selected,val);},getHoverable:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Hoverable,parent);},setHoverable:function(val)
{this._setFlagValue($IG.ClientUIFlags.Hoverable,val);},getHovered:function()
{return this._getFlagValue($IG.ClientUIFlags.Hovered,null,true);},setHovered:function(val)
{this._setFlagValue2($IG.ClientUIFlags.Hovered,val);},getEditable:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Editable,parent);},setEditable:function(val)
{this._setFlagValue($IG.ClientUIFlags.Editable,val);},getFocusable:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Focusable,parent);},setFocusable:function(val)
{this._setFlagValue($IG.ClientUIFlags.Focusable,val);},getFocused:function()
{return this._getFlagValue($IG.ClientUIFlags.Focused,null,true);},setFocused:function(val)
{this._setFlagValue2($IG.ClientUIFlags.Focused,val);},getDraggable:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Draggable,parent);},setDraggable:function(val)
{this._setFlagValue($IG.ClientUIFlags.Draggable,val);},getDroppable:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.Droppable,parent);},setDroppable:function(val)
{this._setFlagValue($IG.ClientUIFlags.Droppable,val);},getKBNavigable:function(parent)
{return this._getFlagValue($IG.ClientUIFlags.KBNavigable,parent);},setKBNavigable:function(val)
{this._setFlagValue($IG.ClientUIFlags.KBNavigable,val);},_getFlagValue:function(flag,parent,isBoolFlag)
{var returnDb=$IG.DefaultableBoolean.NotSet;var trueFlag=this._flags&(flag*.5);var falseFlag=this._flags&flag;if(trueFlag!=0&&falseFlag==0)
returnDb=$IG.DefaultableBoolean.True;else if(falseFlag!=0)
returnDb=$IG.DefaultableBoolean.False;if(parent!=null&&returnDb==$IG.DefaultableBoolean.NotSet&&parent._getFlags)
returnDb=parent._getFlags()._getFlagValue(flag);if(isBoolFlag)
return(returnDb==2)
else if(returnDb==$IG.DefaultableBoolean.True)
return true;else
return false;},_setFlagValue:function(flag,value)
{if(typeof(value)=="boolean")
value=(value)?1:2;var trueFlag=this._flags&(flag*.5);this._flags-=trueFlag;var falseFlag=this._flags&flag;this._flags-=falseFlag;this._flags+=flag*(.5)*value;this._object._updateFlags(this._flags);},_setFlagValue2:function(flag,value)
{if(typeof(val)=="boolean")
val=(val)?1:0;this._flags-=this._flags&flag;this._flags+=(value)?flag:0;this._object._updateFlags(this._flags);},_getFlags:function()
{return this._flags;}}
$IG.FlagsObject.registerClass('Infragistics.Web.UI.FlagsObject');$IG.ImageObjectProps=new function()
{this.Count=$IG.ObjectBaseProps.Count+0;};$IG.ImageObject=function(obj,element,props,owner,csm)
{if(!csm)
csm=new $IG.ObjectClientStateManager(props[0]);$IG.ImageObject.initializeBase(this,[obj,element,props,owner,csm]);this._currentState=this._get_clientOnlyValue("s");}
$IG.ImageObject.prototype={setState:function(state)
{if(this._element==null)
return;var url=this._get_clientOnlyValue(state);if(url==null||url.length==0)
url=this._get_clientOnlyValue($IG.ImageState.Normal);this._element.src=url;this._currentState=state;},getState:function()
{return this._currentState;}}
$IG.ImageObject.registerClass('Infragistics.Web.UI.ImageObject',$IG.ObjectBase);$IG.ImageState=new function()
{this.Normal='i';this.Hover='h';this.Pressed='p';this.Disabled='d';};$IG.CheckBoxMode=new function()
{this.Off=0;this.BiState=1;this.TriState=2;};$IG.CheckBoxState=new function()
{this.Unchecked=0;this.Checked=1;this.Partial=2;};$IG.ImageCheckBoxProps=new function()
{this.State=[$IG.ImageObjectProps.Count+0,$IG.CheckBoxState.Unchecked];this.Count=$IG.ImageObjectProps.Count+1;};$IG.ImageCheckBox=function(obj,element,props,owner,csm)
{$IG.ImageCheckBox.initializeBase(this,[obj,element,props,owner,csm]);}
$IG.ImageCheckBox.prototype={set_uncheckedImageURL:function(value)
{this._uncheckedImageURL=value;},set_checkedImageURL:function(value)
{this._checkedImageURL=value;},set_partialImageURL:function(value)
{this._partialImageURL=value;},set_state:function(value)
{this._set_value($IG.ImageCheckBoxProps.State,value);if(this._element==null)
return;switch(value)
{case $IG.CheckBoxState.Unchecked:this._element.src=this._uncheckedImageURL;break;case $IG.CheckBoxState.Checked:this._element.src=this._checkedImageURL;break;case $IG.CheckBoxState.Partial:this._element.src=this._partialImageURL;break;}},get_state:function()
{return this._get_value($IG.ImageCheckBoxProps.State);}}
$IG.ImageCheckBox.registerClass('Infragistics.Web.UI.ImageCheckBox',$IG.ImageObject);Infragistics._Utility=function(){};Infragistics._Utility.prototype={addCompoundClass:function(element,className)
{if(element)
Sys.UI.DomElement.addCssClass(element,className);},containsCompoundClass:function(element,className)
{return(element&&element.className.indexOf(className)>=0);},removeCompoundClass:function(element,className)
{if(!element)
return;element.className=element.className.replace(className,"");element.className=element.className.replace("  "," ");},toggleCompoundClass:function(element,className,apply)
{if(apply)
{if(!this.containsCompoundClass(element,className))
this.addCompoundClass(element,className);}
else
this.removeCompoundClass(element,className);},addClientEvent:function(obj,evtName,val)
{var fnc=this.toFunction(val);if(fnc){if(obj.get_events)
obj.get_events().addHandler(evtName,fnc);else{if(!obj._events){obj._events=new Sys.EventHandlerList();}
obj._events.addHandler(evtName,fnc);}}else
throw'The "'+val+'" for "'+evtName+'" should be a function, function name, or function text';},removeClientEvent:function(obj,evtName,fnc)
{obj.get_events().removeHandler(evtName,fnc);},getPosition:function(elem)
{var htm,name,style,elem0=elem;var first=true,noTD=true,ieRect=false,end=false;var o={x:0,y:0,scrollX:0,scrollY:0};var ie=document.all&&elem.getBoundingClientRect;var body2=!ie;while(elem)
{name=elem.nodeName;style=this.getRuntimeStyle(elem);htm=name=='HTML';if(end)
{if(htm)break;elem=elem.parentNode;continue;}
var body=name=='BODY';var bdr=false;var pos=this.getStyleValue(style,'position');var abs=pos=='absolute',rel=pos=='relative';if(ie&&rel)
ieRect=abs=true;end=body&&!ie;if((abs&&body)||name=='FORM')
break;var v=elem.offsetTop;if(v)
{if(elem.nodeName=='TD'&&elem.offsetParent!=elem.parentNode)
v=elem.parentNode.offsetTop;o.y+=v;}
v=elem.offsetLeft;if(v)o.x+=v;if(!first&&!htm)
{var td=name=='TD',tbl=name=='TABLE';if(ie)
{if(!tbl||(noTD&&abs))
{if(name!='DIV'||!rel)
bdr=true;if(td)
noTD=false;}
if(tbl||(!td&&!tbl))
noTD=true;}
else if((!tbl&&!td)||(td&&abs))
bdr=true;}
if(bdr)
{v=body2&&body;if(!ie&&!v&&(abs||rel))
v=this._isScroll(style,name);this._addBorder(style,o,false,v);}
if(elem!=elem0)
this._addScroll(elem,o);if(abs)
body2=false;first=false;var pe=elem.parentNode;elem=elem.offsetParent;if(!elem&&end)
{elem=pe;continue;}
if(!ie&&!abs&&elem)while(pe&&pe!=elem)
{if(this._isScroll(style=this.getRuntimeStyle(pe),pe.nodeName))
{this._addScroll(pe,o);this._addBorder(style,o);}
pe=pe.parentNode;}}
if(body2&&htm)
this._addBorder(style,o,true);if(ieRect)
{v=elem0.getBoundingClientRect();o.x=v.left+o.scrollX;o.y=v.top+o.scrollY;if(htm&&style)
this._addBorder(style,o,true);}
o.absX=o.x-o.scrollX;o.absY=o.y-o.scrollY;return o;},_addScroll:function(elem,o)
{var v=elem.scrollLeft;if(v)o.scrollX+=v;v=elem.scrollTop;if(v)o.scrollY+=v;},_addBorder:function(style,o,neg,twice)
{var v=this.toIntPX(style,'borderLeftWidth',0);if(twice)v+=v;o.x+=neg?-v:v;v=this.toIntPX(style,'borderTopWidth',0);if(twice)v+=v;o.y+=neg?-v:v;},_isScroll:function(style,name)
{var v=name=='DIV'?this.getStyleValue(style,'overflow'):'';return v=='auto'||v=='scroll';},cancelEvent:function(e,type,raw)
{if(!e&&!raw)e=window.event;if(!e)return true;if(type&&type.substring&&e.type!=type)
return true;if(e.stopPropagation)
e.stopPropagation();if(e.preventDefault)
e.preventDefault();e.cancelBubble=true;e.returnValue=false;if(raw)
return false;return this.cancelEvent(e.rawEvent,null,true);},getRuntimeStyle:function(elem)
{if(!elem)
return null;var s=elem.currentStyle;if(s)
return s;var win=document.defaultView;if(!win)
win=window;if(win.getComputedStyle)
s=win.getComputedStyle(elem,'');return s?s:elem.style;},getStyleValue:function(style,prop,elem)
{if(!style)
style=this.getRuntimeStyle(elem);if(!style)
return null;var val=style[prop];if(!this.isEmpty(val)||!style.getPropertyValue)
return val;return style.getPropertyValue(prop);},getPropFromCss:function(elem,prop)
{var i,v=null;try{v=elem.style[prop];}catch(i){}
if(v&&v.length&&v.length>0)
return v;var len=-1,cn=elem.className;if(!cn||cn.length<1)
return null;cn=cn.split(' ');while(++len<cn.length)
cn[len]='.'+cn[len];var sheets=document.styleSheets;i=sheets?sheets.length:0;while(i-->0)
{var rules=sheets[i].cssRules;if(!rules)
rules=sheets[i].rules;var r=rules.length;while(r-->0)
{var text=null,rule=rules[r],n=len;try{text=rule.selectorText;}catch(elem){}
while(n-->0)if(text==cn[n])
{try{v=rule.style[prop];}catch(elem){}
if(v&&v.length&&v.length>0)
return v;}}}
return null;},toInt:function(val,def)
{var ok=false;var i=-1,len=val?val.length:0;while(++i<len)
{var ch=val.charCodeAt(i);if(ch==45&&i==0)
continue;if(ch<48||ch>57)
{val=val.substring(0,i);break;}
ok=true;}
return ok?parseInt(val):def;},toIntPX:function(style,prop,def,elem)
{var px=(elem&&(prop=='width'||prop=='height'))?this.getPropFromCss(elem,prop):null;if(!px)
px=this.getStyleValue(style,prop,elem);return(px&&px.indexOf('px')>0)?this.toInt(px,0):(def?def:0);},toFunction:function(val)
{if(val instanceof Function)
return val;if(!val||!val.length||!val.charCodeAt)
return null;var fnc=window[val];if(fnc instanceof Function)
return fnc;try{fnc=eval(val);}catch(val){}
return(fnc instanceof Function)?fnc:null;},isEmpty:function(val)
{if(!val)
return true;val=val.length;return!val||val.length<1;},getOpacity:function(elem)
{var op=this.getStyleValue(null,'opacity',elem);if(op)
{op=parseFloat(op);if(op)
{op=Math.floor(op*100);return(op<100&&op>=0)?op:100;}}
op=this.getStyleValue(null,'filter',elem);if(!op)
return 100;op=this.replace(op.toLowerCase(),' ','');var i=op.indexOf('opacity=');return(i<0)?100:this.toInt(op.substring(i+8),100);},findControl:function(id,prefix)
{for(var ig in ig_controls)
{var ctl=ig_controls[ig];if(!ctl.get_id||(prefix&&ig.indexOf(prefix)!=0))
continue;var i=ig.lastIndexOf(id);if(i==0||(i>0&&i+id.length==ig.length&&ig.charAt(i-1)=='_'))
return ctl;}},findChild:function(elem,id)
{var id0=elem.id;var i=id0?id0.lastIndexOf(id):-1;if(i==0||(i>0&&i+id.length==id0.length&&id0.charAt(i-1)=='_'))
return elem;var elems=elem.childNodes;i=elems?elems.length:0;while(i-->0)
{elem=this.findChild(elems[i],id);if(elem)
return elem;}},addLayoutTarget:function(target)
{var index=-1,elem=target._element;while((elem=elem.parentNode)!=null)
{if(!elem.getAttribute)
continue;var ctl=null,id=elem.getAttribute('mkr');if(id&&id.length>1&&id.substring(0,1)=='c')
index=this.toInt(id.substring(1),-1);id=elem.getAttribute('CtlMain');if(!id)
continue;if(id=='layout')
{id=elem.id;if(id)
ctl=ig_controls[id];}
if(!ctl||!ctl.getLayoutManager)
{index=-1;continue;}
ctl=ctl.getLayoutManager(index);if(!ctl)continue;var i=-1,ids=ctl._layoutListeners,id=target._id;if(!ids)
ctl._layoutListeners=ids=new Array();while(++i<ids.length)
if(ids[i]==id)
break;ids[i]=id;target._layoutManager=ctl;return true;}
return false;},raiseLayoutEvent:function(man)
{var ctl,elem=man._element;var lsnrs=elem?elem._ctlsForLayout:null;var i=lsnrs?lsnrs.length:0;while(i-->0)
{ctl=lsnrs[i];if(ctl&&ctl.layout)
if(ctl.layout(man.getClientWidth?man.getClientWidth(ctl):null,man.getClientHeight?man.getClientHeight(ctl):null))
if(!ctl._layoutManager)
this.addLayoutTarget(ctl);lsnrs[i]=null;}
if(lsnrs)
{elem._ctlsForLayout=null;return;}
lsnrs=man._layoutListeners;i=lsnrs?lsnrs.length:0;while(i-->0)
{var ctl=ig_controls[lsnrs[i]];if(ctl&&ctl.layout)
{var width=man.getClientWidth?man.getClientWidth(ctl):null,height=man.getClientHeight?man.getClientHeight(ctl):null;ctl.layout(width,height);}}},checkLayoutManager:function(ctl)
{var i=0,elem=ctl._element;while(i++<10&&elem&&(elem=elem.parentNode)!=null)
{var css=elem.id?elem.className:null;if(css&&css.indexOf(':=CtlMain:layout')==css.length-16)
{if((i=elem._ctlsForLayout)==null)
i=elem._ctlsForLayout=new Array();i[i.length]=ctl;return true;}}
return false;},getOffset:function(style,width,noTrail,noLead)
{var val=0;if(style)while(!noLead||!noTrail)
{var prop=noLead?(width?'Right':'Bottom'):(width?'Left':'Top');if(noLead)
noTrail=true;noLead=true;val+=this.toIntPX(style,'border'+prop+'Width')+this.toIntPX(style,'padding'+prop);}
return val;},getMargin:function(style,horiz)
{return this.toIntPX(style,'margin'+(horiz?'Left':'Top'))+this.toIntPX(style,'margin'+(horiz?'Right':'Bottom'));},display:function(elem,hide)
{var style=elem?elem.style:null;if(!style)return;style.display=hide?'none':'';style.visibility=hide?'hidden':'visible';},isOut:function(e,elem)
{var to=e.toElement;if(!to)to=e.relatedTarget;e=e.rawEvent;if(!to&&e)if((to=e.toElement)==null)
to=e.relatedTarget;while(to)
{if(to==elem)
return false;to=to.parentNode;}
return true;},replace:function(str,oldVal,newVal)
{if(newVal==null)
for(var i=0;i<oldVal.length;i+=2)
str=this.replace(str,oldVal[i],oldVal[i+1]);else while(str.indexOf(oldVal)>=0)
str=str.replace(oldVal,newVal);return str;},htmlEscapeCharacters:function(str)
{return(typeof(str)==="string")?str.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;"):str;},htmlUnescapeCharacters:function(str)
{return(typeof(str)==="string")?str.replace(/&amp;/g,"&").replace(/&lt;/g,"<").replace(/&gt;/g,">"):str;},getHTML:function(win)
{if(!win)
win=window;var doc=win.document;var htm=doc.body;while(htm&&htm.nodeName!='HTML')
htm=htm.parentNode;return htm?htm:doc.body;},getWinRect:function(win)
{if(!win)
win=window;var doc=win.document;var body=doc.body,htm=this.getHTML(win),de=doc.documentElement;if(!de)
de=htm;var x=de.scrollLeft,y=de.scrollTop,wi=win.innerWidth,hi=win.innerHeight,wd=de.clientWidth,hd=de.clientHeight,w=htm.clientWidth,h=htm.clientHeight;var maxWidth=w?w:0,maxHeight=h?h:0,w2=htm.scrollWidth,h2=htm.scrollHeight;if(wd)
{maxWidth=Math.max(maxWidth,wd);maxHeight=Math.max(maxHeight,hd);}
if(wi)
{maxWidth=Math.max(maxWidth,wi);maxHeight=Math.max(maxHeight,hi);}
if(w2&&h2)
{maxWidth=Math.max(maxWidth,w2);maxHeight=Math.max(maxHeight,h2);}
maxWidth=Math.max(maxWidth,body.scrollWidth);maxHeight=Math.max(maxHeight,body.scrollHeight);w2=body.offsetWidth;h2=body.offsetHeight
maxWidth=Math.max(maxWidth,w2);maxHeight=Math.max(maxHeight,h2);var noClientSize=false;if(!x)
x=htm.scrollLeft;if(!x)
x=body.scrollLeft;if(!y)
y=htm.scrollTop;if(!y)
y=body.scrollTop;if(!wi||wi<50)
wi=99999;if(!wd||wd<50)
wd=99999;if(!w||w<50)
w=99999;if(w>wd)
w=wd;if(w>wi)
w=wi;if(w==99999)
{w=w2;noClientSize=true;}
if(!hi||hi<50)
hi=99999;if(!hd||hd<50)
hd=99999;if(!h||h<50)
h=99999;if(h>hd)
h=hd;if(h>hi)
h=hi;if(h==99999)
{h=h2;noClientSize=true;}
return{x:x,y:y,width:w,height:h,maxWidth:maxWidth,maxHeight:maxHeight,noClientSize:noClientSize};},_getDropPoint:function(edit,elem)
{var size,rect=$util.getWinRect();var height=edit.offsetHeight,width0=elem.offsetWidth,height0=elem.offsetHeight;var p=Sys.UI.DomElement.getLocation(edit),pe=Sys.UI.DomElement.getLocation(elem);p.x-=pe.x;p.y-=pe.y;if((size=rect.height)+18<rect.maxHeight)
size-=15;size+=rect.y-p.y-height;p.y+=(height0>size&&size<p.y-rect.y)?-height0:height;if((size=rect.width)+18<rect.maxWidth)
size-=15;if(p.x+width0>(size+=rect.x))
p.x=size-width0;return p;},_zIndexTop:function(elem,z0)
{while(elem)
{if(elem.nodeName=='BODY'||elem.nodeName=='FORM')
break;var z=this.getStyleValue(null,'zIndex',elem);if(z&&z.substring)
z=(z.length>4&&z.charCodeAt(0)<58)?parseInt(z):0;if(z&&z>=zIndex)
z0=z+1;elem=elem.parentNode;}
return z0;},setOpacity:function(element,opacity)
{element.style.opacity=opacity/100;if(element.filters)
{if(!element.filters["alpha"]||element.style.filter.indexOf("alpha")==-1)
element.style.filter+=" alpha(opacity="+opacity+")";else
element.filters["alpha"].opacity=opacity;}},_initAttr:function(elem)
{var attr=elem.id;var separator=attr.indexOf(":");if(attr){attr=':'+attr.substring(0,separator)+'.'+attr.substring(separator+1,attr.length);}
var j=99,i=attr?attr.length:0;if(i<1)
return false;if(attr.charAt(0)==':')
{attr=attr.split(':');i=attr.length;if((i>=4||(i%2==0))&&attr[1].indexOf('.')>0)
j=1;}
if(j>2)
{var css=elem.className;j=(css&&css.length>5)?css.indexOf(' :='):-1;if(j<0)
return true;attr=css.substring(j+3);if(attr.indexOf(' ')>=0)
return true;attr=attr.split(':');i=attr.length;if(i<2||(i&1)!=0)
return true;elem.className=css.substring(0,j);j=-1;}
while((i-=2)>j)
elem.setAttribute(attr[i],attr[i+1]);return j<0;},resolveMarkedElement:function(elem,checkControl)
{var adr=null;var control=null;var secondWalkthrough=false;while(elem)
{if(elem.getAttribute)
{adr=elem.getAttribute("adr");if(adr==null)
adr=elem.getAttribute("mkr");if(adr==null)
adr=elem.getAttribute("obj");if(adr==null&&!secondWalkthrough)
{adr=elem.getAttribute("id");if(adr)
{secondWalkthrough=true;if(!$util._initAttr(elem))
continue;adr=null;}}
else
secondWalkthrough=false;}
if(typeof(adr)=="string")
{if(adr.length>0)
break;}
else
if(typeof(adr)!="undefined"&&adr!==null)
break;elem=elem.parentNode;}
if(elem==null)
return null;else if(checkControl)
{var parent=elem.parentNode;while(parent)
{if(parent.control!=null)
{control=parent.control;break;}
parent=parent.parentNode;}}
return[elem,adr,control];},compare:function(val1,val2)
{if(val1==val2)
return true;else if(val1!=null&&val2!=null)
{var type1=Object.getType(val1).__typeName;var type2=Object.getType(val2).__typeName;if(type1!=type2||type1=="String"||type1=="Number"||type1=="Boolean")
return false;if(type1=="Array")
{if(val1.length!=val2.length)
return false;for(var i in val1)
{if(!$util.compare(val1[i],val2[i]))
return false;}
return true;}
else if(type1=="Date")
{if(val1.getTime()==val2.getTime())
return true;}
else
{for(var i in val1)
{if(!$util.compare(val1[i],val2[i]))
return false;}
return true;}}
return false;},ensureBrowserInfo:function()
{try{this.AgentName=navigator.userAgent.toLowerCase();}catch(e){this.AgentName="";}
this.MajorVersionNumber=parseInt(navigator.appVersion);this.IsWebKit=this.AgentName.indexOf("webkit")>=0;this.IsSafari=this.IsWebKit||this.AgentName.indexOf("safari")>=0;this.IsFireFox=this.AgentName.indexOf("firefox")>=0;if(this.IsFireFox)
{this.IsFireFox2=this.AgentName.indexOf("firefox/2")>=0;this.IsFireFox3=this.AgentName.indexOf("firefox/3")>=0;}
this.IsOpera=this.AgentName.indexOf("opera")>=0;this.IsMac=this.AgentName.indexOf("mac")>=0;this.IsIE=document.all!=null&&!this.IsOpera&&!this.IsSafari;if(this.IsIE)
{this.IsIE8=this.AgentName.indexOf("msie 8.0")>=0;this.IsIEStandards=(this.IsIE8);this.IsIE6=this.AgentName.indexOf("msie 6.0")>=0;}},_getWidthMargin:function(element)
{var style=this.getRuntimeStyle(element);var borderLeftWidth=0;if(style.borderLeftStyle!="none")
{if(style.borderLeftWidth=="thin")
borderLeftWidth=1;else if(style.borderLeftWidth=="medium")
borderLeftWidth=3;else if(style.borderLeftWidth=="thick")
borderLeftWidth=5;else
{var w=parseInt(style.borderLeftWidth,10);if(isNaN(w))
w=0;borderLeftWidth=w;}}
var borderRightWidth=0;if(style.borderRightStyle!="none")
{if(style.borderRightWidth=="thin")
borderRightWidth=1;else if(style.borderRightWidth=="medium")
borderRightWidth=3;else if(style.borderRightWidth=="thick")
borderRightWidth=5;else
{var w=parseInt(style.borderRightWidth,10);if(isNaN(w))
w=0;borderRightWidth=w;}}
var paddingLeft=parseInt(style.paddingLeft,10);if(isNaN(paddingLeft))
paddingLeft=0;var paddingRight=parseInt(style.paddingRight,10);if(isNaN(paddingRight))
paddingRight=0;return borderLeftWidth+borderRightWidth+paddingLeft+paddingRight;},_getHeightMargin:function(element)
{var style=this.getRuntimeStyle(element);var borderTopWidth=0;if(style.borderTopStyle!="none")
{if(style.borderTopWidth=="thin")
borderTopWidth=1;else if(style.borderTopWidth=="medium")
borderTopWidth=3;else if(style.borderTopWidth=="thick")
borderTopWidth=5;else
{var w=parseInt(style.borderTopWidth,10);if(isNaN(w))
w=0;borderTopWidth=w;}}
var borderBottomWidth=0;if(style.borderBottomStyle!="none")
{if(style.borderBottomWidth=="thin")
borderBottomWidth=1;else if(style.borderBottomWidth=="medium")
borderBottomWidth=3;else if(style.borderBottomWidth=="thick")
borderBottomWidth=5;else
{var w=parseInt(style.borderBottomWidth,10);if(isNaN(w))
w=0;borderBottomWidth=w;}}
var paddingTop=parseInt(style.paddingTop,10);if(isNaN(paddingTop))
paddingTop=0;var paddingBottom=parseInt(style.paddingBottom,10);if(isNaN(paddingBottom))
paddingBottom=0;return borderTopWidth+borderBottomWidth+paddingTop+paddingBottom;},setAbsoluteWidth:function(element,width)
{width-=this._getWidthMargin(element);if(width<0)
width=0;element.style.width=width+"px";if(element.offsetWidth!=0)
{var adjustment=width+(width-element.offsetWidth+this._getWidthMargin(element));if(adjustment>0)
element.style.width=adjustment+"px";}},getAbsoluteWidth:function(element)
{var width=element.offsetWidth+this._getWidthMargin(element);if(width<0)
width=0;return width;},setAbsoluteHeight:function(element,height)
{height-=this._getHeightMargin(element);if(height<0)
height=0;element.style.height=height+"px";},getAbsoluteHeight:function(element)
{var height=element.offsetHeight-this._getHeightMargin(element);if(height<0)
height=0;return height;},addHandler:function(element,eventName,handler)
{if(!handler)
return;var browserHandler;if(element.addEventListener)
{browserHandler=function(e)
{return handler.call(element,new Sys.UI.DomEvent(e));}
element.addEventListener(eventName,browserHandler,false);}
else if(element.attachEvent)
{browserHandler=function(e)
{return handler.call(element,new Sys.UI.DomEvent(e));}
element.attachEvent('on'+eventName,browserHandler);}},removeHandler:function(element,eventName,handler)
{if(!handler)
return;if(element.removeEventListener)
element.removeEventListener(eventName,handler,false);else if(element.detachEvent)
element.detachEvent('on'+eventName,handler);},isChild:function(parent,child)
{var p=child.parentNode;while(p!=parent&&p!=document.body&&p!=null)
p=p.parentNode;return(p==parent)},getRows:function(tbl)
{if(!tbl)
return null;try
{if(typeof tbl.rows=='object')
return tbl.rows;}catch(e){}
if(tbl.nodeName=='TABLE')
tbl=tbl.firstChild;return(tbl.nodeName=='TBODY')?tbl.childNodes:null;},createDelegate:function(instance,method,args)
{return function()
{return method.apply(instance,args);}},_setMouseBlock:function(targetDIV)
{var mouseBlock=this._mouseBlock;if(!targetDIV&&!mouseBlock)
return;if(!mouseBlock)
{this._mouseBlock=mouseBlock=document.createElement('DIV');var style=mouseBlock.style;style.zIndex=100000;style.position='absolute';style.background='white';style.filter='alpha(opacity:0)';style.opacity=0.0;}
if(targetDIV)
{if(mouseBlock._targetDIV!=targetDIV)
{this._setMouseBlock();mouseBlock._targetDIV=targetDIV;targetDIV.insertBefore(mouseBlock,targetDIV.firstChild);}
if(mouseBlock._w!=targetDIV.offsetWidth)
mouseBlock.style.width=(mouseBlock._w=targetDIV.offsetWidth)+'px';if(mouseBlock._h!=targetDIV.offsetHeight)
mouseBlock.style.height=(mouseBlock._h=targetDIV.offsetHeight)+'px';return;}
if(!mouseBlock._targetDIV)
return;mouseBlock._targetDIV=null;mouseBlock.parentNode.removeChild(mouseBlock);}};Infragistics._Utility.registerClass("Infragistics._Utility");Infragistics.Utility=new Infragistics._Utility();var $util=Infragistics.Utility;$util.ensureBrowserInfo();if($util.IsIE8)
{Sys.UI.DomElement.getLocation=function Sys$UI$DomElement$getLocation(element)
{var e=Function._validateParams(arguments,[{name:"element",domElement:true}]);if(e)throw e;if((element.window&&(element.window===element))||element.nodeType===9)return new Sys.UI.Point(0,0);var offsetX=0;var offsetY=0;var previous=null;var previousStyle=null;var currentStyle=null;for(var parent=element;parent;previous=parent,previousStyle=currentStyle,parent=parent.offsetParent)
{var tagName=parent.tagName;currentStyle=Sys.UI.DomElement._getCurrentStyle(parent);if((parent.offsetLeft||parent.offsetTop)&&!((tagName==="BODY")&&(!previousStyle||previousStyle.position!=="absolute")))
{offsetX+=parent.offsetLeft;offsetY+=parent.offsetTop;}
if(previous!==null&&currentStyle)
{if((tagName!=="TABLE")&&(tagName!=="TD")&&(tagName!=="HTML"))
{offsetX+=parseInt(currentStyle.borderLeftWidth)||0;offsetY+=parseInt(currentStyle.borderTopWidth)||0;}
if(tagName==="TABLE"&&(currentStyle.position==="relative"||currentStyle.position==="absolute"))
{offsetX+=parseInt(currentStyle.marginLeft)||0;offsetY+=parseInt(currentStyle.marginTop)||0;}}}
currentStyle=Sys.UI.DomElement._getCurrentStyle(element);var elementPosition=currentStyle?currentStyle.position:null;var elementPositioned=elementPosition&&(elementPosition!=="static");if(!elementPosition||(elementPosition!=="absolute"))
{for(var parent=element.parentNode;parent;parent=parent.parentNode)
{tagName=parent.tagName;if((tagName!=="BODY")&&(tagName!=="HTML")&&(parent.scrollLeft||parent.scrollTop))
{offsetX-=(parent.scrollLeft||0);offsetY-=(parent.scrollTop||0);currentStyle=Sys.UI.DomElement._getCurrentStyle(parent);offsetX+=parseInt(currentStyle.borderLeftWidth)||0;offsetY+=parseInt(currentStyle.borderTopWidth)||0;}}}
return new Sys.UI.Point(offsetX,offsetY);}}
var ig_ui_all=null;function ig_ui_timer(o,del)
{var all=ig_ui_all;var i,fn=all?all._timerFn:null;if(typeof o!='object')
o=null;if(o)
{if(!o._onTimer)return;if(!all)ig_ui_all=all=new Array();i=all.length;while(i-->0)if(all[i]==o)break;if(del)
{if(i<0)return;delete o._onTimer;delete all[i];o=null;i=all.length;while(i-->0)if(all[i])o=true;}
else
{if(i<0)
{while(all[++i]);all[i]=o;}
if(!fn)all._timerFn=fn=window.setInterval(ig_ui_timer,200);}}
if(o)return;if(!del&&fn)for(i=0;i<all.length;i++)
{o=all[i];if(o&&o._onTimer)
{if(!o._onTimer())
{fn=null;continue;}
delete o._onTimer;delete all[i];}}
if(!fn)return;window.clearInterval(fn);delete all._timerFn;ig_ui_all=null;}
$IG.EventArgs=function()
{$IG.EventArgs.initializeBase(this);this._props=[null,0];}
$IG.EventArgs.prototype={get_browserEvent:function()
{return this._props[0];},dispose:function()
{this._props[0]=null;}}
$IG.EventArgs.registerClass('Infragistics.Web.UI.EventArgs',Sys.EventArgs);$IG.PostBackEventArgs=function()
{$IG.PostBackEventArgs.initializeBase(this);}
$IG.PostBackEventArgs.prototype={get_postBack:function()
{return this._props[1];},set_postBack:function(val)
{this._props[1]=val;}}
$IG.PostBackEventArgs.registerClass('Infragistics.Web.UI.PostBackEventArgs',$IG.EventArgs);$IG.CancelEventArgs=function()
{$IG.CancelEventArgs.initializeBase(this);this._cancel=false;}
$IG.CancelEventArgs.prototype={get_cancel:function()
{return this._cancel;},set_cancel:function(val)
{this._cancel=val;}}
$IG.CancelEventArgs.registerClass('Infragistics.Web.UI.CancelEventArgs',$IG.EventArgs);$IG.AjaxEventArgs=function(){$IG.AjaxEventArgs.initializeBase(this);}
$IG.AjaxEventArgs.prototype={}
$IG.AjaxEventArgs.registerClass('Infragistics.Web.UI.AjaxEventArgs',$IG.CancelEventArgs);$IG.MoveEventArgs=function()
{$IG.MoveEventArgs.initializeBase(this);}
$IG.MoveEventArgs.prototype={get_postBack:function(){return this._props[1];},set_postBack:function(val){this._props[1]=val;},get_x:function()
{return this._props[2];},get_y:function()
{return this._props[3];},get_oldX:function()
{return this._props[4];},get_oldY:function()
{return this._props[5];},set_x:function(val)
{this._props[2]=this._x=val;},set_y:function(val)
{this._props[3]=this._y=val;}}
$IG.MoveEventArgs.registerClass('Infragistics.Web.UI.MoveEventArgs',$IG.CancelEventArgs);$IG.ResizeEventArgs=function()
{$IG.ResizeEventArgs.initializeBase(this);}
$IG.ResizeEventArgs.prototype={get_postBack:function(){return this._props[1];},set_postBack:function(val){this._props[1]=val;},get_width:function(){return this._props[2];},get_height:function(){return this._props[3];},get_oldWidth:function(){return this._props[4];},get_oldHeight:function(){return this._props[5];},set_width:function(val){this._props[2]=this._width=val;},set_height:function(val){this._props[3]=this._height=val;}}
$IG.ResizeEventArgs.registerClass('Infragistics.Web.UI.ResizeEventArgs',$IG.CancelEventArgs);$IG.PageChangeEventArgs=function(){$IG.PageChangeEventArgs.initializeBase(this);}
$IG.PageChangeEventArgs.prototype={get_currentPage:function(){return this._props[2];},get_newPage:function(){return this._props[3];}}
$IG.PageChangeEventArgs.registerClass("Infragistics.Web.UI.PageChangeEventArgs",$IG.CancelEventArgs);var _bugE=null;function _bug4(v){_bug3(v);_bugE.style.background='yellow';}
function _bug3(v){_bug("<br />"+v,true,"400px");}
function _bug2(v){_bug(v,true,"400px");}
function _bug1(v){_bug(v,false,"400px");}
function _bug(v,a,l,t)
{if(!_bugE)
{_bugE=document.createElement('DIV');document.body.insertBefore(_bugE,document.body.firstChild);var s=_bugE.style;s.position='absolute';s.zIndex=10000;s.left=s.top='0px';s.border='1px dotted red';s.fontSize='12px';s.fontFamily='courier';}
if(l)_bugE.style.left=l;if(t)_bugE.style.top=t;_bugE.innerHTML=(a?_bugE.innerHTML:'')+v;}
$IG.TransparentFrame=function(parent,mouseMoveHandler,mouseUpHandler)
{this._parent=parent;this._mmh=mouseMoveHandler;this._muh=mouseUpHandler;this.frame=document.createElement("IFRAME");this.frame.src='javascript:new String("<html></html>")';this.frame.style.zIndex=1000;this.frame.style.position="absolute";this.frame.style.top=0;this.frame.style.left=0;this._originalPos=parent.style.position
parent.appendChild(this.frame);setTimeout(Function.createDelegate(this,this._setupFrame),0);}
$IG.TransparentFrame.prototype={_setupFrame:function()
{var doc=this.frame.contentWindow.document;$util.setOpacity(this.frame,0);$util.addHandler(doc,'mousemove',this._mmh);$util.addHandler(doc,'mouseup',this._muh);},showFrame:function(show,height,width)
{var parent=this._parent;if(parent!=null&&parent.tagName=="DIV")
{if(show)
{if(parent.scrollHeight<=parent.offsetHeight&&this._originalOverflowY==null)
{this._originalOverflowY=parent.style.overflowY;parent.style.overflowY="hidden";}
if(parent.scrollWidth<=parent.offsetWidth&&this._originalOverflowX==null)
{this._originalOverflowX=parent.style.overflowX;parent.style.overflowX="hidden";}}
else
{if(this._originalOverflowX!=null)
parent.style.overflowX=this._originalOverflowX;if(this._originalOverflowY!=null)
parent.style.overflowY=this._originalOverflowY;this._originalOverflowX=null;this._originalOverflowY=null;}}
parent.style.position=(show)?"relative":this._originalPos;this.frame.style.height=height+"px";this.frame.style.width=width+"px";this.frame.style.display=show?"":"none";}}
$IG.TransparentFrame.registerClass('Infragistics.Web.UI.TransparentFrame');