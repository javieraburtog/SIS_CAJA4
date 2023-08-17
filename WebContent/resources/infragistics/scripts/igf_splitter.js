// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace('Infragistics.Web.UI');$IG.SplitterProps=new function()
{var count=$IG.LayoutControlProps.Count;this.DynamicResize=[count++,0];this.ClientStates=[count++,''];this.Count=count;};$IG.WebSplitter=function(elem)
{$IG.WebSplitter.initializeBase(this,[elem]);}
$IG.WebSplitter.prototype={_thisType:'split',_responseComplete:function(cb,response)
{var cont=response?response.context:null;if(!cont||cont.length<2||!this._element)
return;var div=document.createElement('DIV'),panes=this._asyncUpdate?null:this._panes;div.innerHTML=cont[1];var id=this.get_id(),name=this.get_name(),i=div.childNodes.length;while(i-->0)
{var node=div.childNodes[i];if(node.id==id)
{var j=panes?panes.length:0;while(j-->0)
{var body=this._findBody(node,':mkr:c'+j);if(body)
{var elem,old=null,elems=body.childNodes;i=elems?elems.length:0;if(i!=1||elems[0].nodeName!='IFRAME')
{var bodyP=panes[j]._DIV;while(i-->0)
body.removeChild(elems[i]);elems=bodyP.childNodes;i=elems.length;while(i-->0)
{bodyP.removeChild(elem=elems[i]);body.insertBefore(elem,old);old=elem;}}}}
this._element.swapNode(node);break;}}
this.dispose();$create($IG.WebSplitter,{'id':id,'name':name,'props':eval(cont[0])},null,null,$get(id));},_findBody:function(elem,mkr)
{if(elem.id&&elem.id.indexOf(mkr)>0)
return elem;var elems=elem.childNodes;var i=elems?elems.length:0;while(i-->0)
if((elem=this._findBody(elems[i],mkr))!=null)
return elem;return null;},initialize:function()
{$IG.WebSplitter.callBaseMethod(this,'initialize');var i,val,pane,elem=this._element;var prop=this._get_clientOnlyValue('ow');this._vert=prop.charAt(0)=='1';this._withBrowser=prop.charAt(1)=='1';this._enabled=prop.charAt(2)!='0';this._asyncUpdate=prop.charAt(3)=='1';this._vis0=prop.charAt(4)=='1';this._ie=Sys.Browser.agent==Sys.Browser.InternetExplorer;this._css=prop.split('|');prop=this._get_clientOnlyValue('imgs');if(prop)
prop=prop.split('|');if(prop&&prop.length>3)
{this._cImgs=new Array();for(i=0;i<prop.length+1;i+=2)
{var id=parseInt(prop[i]);val=prop[i+1];if(id>7)
val=$util.replace(val,['&quot;','"','&pipe;','|']);this._cImgs[id]=val;}}
this._0=-1;if(this._onTimer(true))
delete this._onTimer;else
ig_ui_timer(this);},_createItem:function(elem,adr)
{this._itemCollection._addObject($IG.SplitterPane,elem,adr);},_onSubmitOtherHandler:function(e)
{if(!this._panes)
return;var i=this._panes.length;while(i-->0)
this._panes[i]._onSubmit();$IG.WebSplitter.callBaseMethod(this,'_onSubmitOtherHandler',[e]);},dispose:function()
{$clearHandlers(this.get_element());$IG.WebSplitter.callBaseMethod(this,'dispose');},get_orientation:function()
{return this._vert?0:1;},get_dynamicResize:function()
{return this._get_value($IG.SplitterProps.DynamicResize)==1;},set_dynamicResize:function(value)
{this._set_value($IG.SplitterProps.DynamicResize,value?1:0);},get_resizeWithBrowser:function()
{return this._withBrowser;},get_splitterBarCss:function()
{return this._css[0];},get_splitterBarHoverCss:function()
{return this._css[1];},set_splitterBarHoverCss:function(value)
{this._css[1]=value;},get_splitterBarPressedCss:function()
{return this._css[2];},set_splitterBarPressedCss:function(value)
{this._css[2]=value;},get_splitterBarShadowCss:function()
{return this._css[3];},set_splitterBarShadowCss:function(value)
{this._css[3]=value;},get_splitterBarShadowLimitCss:function()
{return this._css[4];},set_splitterBarShadowLimitCss:function(value)
{this._css[4]=value;},get_splitterBarThicknessCss:function()
{return this._css[5];},get_panes:function()
{if(!this._panes)
this._panes=this._itemCollection._items;return this._panes;},getPaneAt:function(index)
{return this.get_panes()[index];},getPaneFromElement:function(elem)
{return elem?elem._pane:null;},getPaneFromEvent:function(e)
{return(e&&e.get_browserEvent)?this.getPaneFromElement(e.get_browserEvent().target):null;},getLayoutManager:function(index)
{return this._panes?this._panes[index]:null;},_initCButs:function(elem)
{var j=elem._id;if(j==null)
return;j=parseInt(j);if(!this._cImgs)
this._cImgs=new Array();this._cImgs[j]=elem.src;this._cImgs[j+8]=elem.alt;if(this._vert&&!this._imgHeight)
this._imgHeight=elem.offsetHeight;},_toPercent:function(elem,width)
{var val=elem.style;if(!val)return 100;val=width?val.width:val.height;if(val&&val.indexOf('%')<1)
return-1;return $util.toInt(val,100);},_getGap:function(style,swap)
{return $util.getOffset(style,swap?!this._vert:this._vert);},_toSize:function(elem,swap)
{if(!elem)return 0;var width=this._vert;if(swap)width=!width;return width?elem.offsetWidth:elem.offsetHeight;},_setPaneSize:function(pane,val,skip)
{val=pane._validSize(val);var v=Math.max(val-pane._gap-pane._marg,0);pane._size=val;if(this._vert)
pane._width=v;else
pane._height=v;pane._set_value($IG.SplitterPaneProps.Size,val+'px');if(skip==2)
return val;this._setSizeDIV(pane._DIV,val,pane._gap,pane._marg);this._setSizeTD(pane._element,val);if(!skip)
$util.raiseLayoutEvent(pane);return val;},_setSizeTD:function(elem,val,swap)
{if(!elem)return;var width=this._vert;if(swap)width=!width;if(val<0)val=0;if(width)elem.style.width=val+'px';else elem.style.height=val+'px';},_setSizeDIV:function(div,val,gap,marg,swap)
{if(!div)return;var width=this._vert;if(this._0==0)
gap=0;if(marg)
val-=marg;val=Math.max(val-gap,0);if(swap)width=!width;if(width)div.style.width=val+'px';else div.style.height=val+'px';},_validPane:function(pane,i,skip)
{while(pane)
{if(!skip&&!pane._lock&&!pane.get_collapsed())
return pane;skip=null;var pi=pane._i+i;if(pi<0||pi>=this._panes.length)
return null;pane=this._panes[pi];}
return pane;},_onTimer:function(init)
{if(this._size)
return true;var td,elem,element=this._element;if(!element)
return;var v,w=element.offsetWidth;var width=this._toPercent(element,true),height=this._toPercent(element);if(init&&(width>0||height>0))
if($util.checkLayoutManager(this))
return(this._slave=true);if(!w||w==0)
return false;this._timeOut=2000;var onResize=false;v=this._vert?width:height;if(v>0)
this._noSize=onResize=true;v=this._vert?height:width;if(v>0)
this._noSizeX=onResize=true;var style=$util.getRuntimeStyle(element);var fixBar=null,bar=0;this._gap=this._getGap(style);this._gapX=this._getGap(style,true);this._shiftBars=0;var panes=this.get_panes();var j,i=-1,iLen=panes.length,elems=this._elements;for(var id in elems)
{j=id?id.indexOf('|'):0;if(j>0)
{elem=elems[id];elems[id]=null;elems[id.substring(0,j)]=elem;elem._id=id.substring(j+1);}}
if(iLen<1)return true;var tbl=this._tbl=elems['tbl'];if(!onResize&&(tbl.offsetWidth==w||$util.toIntPX(style,'width')==w)&&((this._vert&&this._gap>0)||(!this._vert&&this._gapX>0)))
this._0=0;if(tbl.offsetHeight>element.offsetHeight)
this._0=1;var pane,barGap=0;var imgPrev=false;this._barGapX=0;while(++i<iLen)
{pane=panes[i];j=pane._get_clientOnlyValue('size');if(j&&j.length>0)
{pane._sizePerc=j.indexOf('%')>0;pane._sizeInit=j=parseInt(j);if(pane.get_collapsed())
pane._sizePref=j;}
if(imgPrev)
pane._marginImgs=1;imgPrev=false;td=pane._element;pane._i=i;var div=elems['c'+i];pane._DIV=div;style=$util.getRuntimeStyle(div);pane._gap=this._getGap(style);pane._gapX=this._getGap(style,true);pane._marg=$util.getMargin(style,this._vert);pane._margX=$util.getMargin(style,!this._vert);if(this._0<0&&div.offsetHeight>td.offsetHeight)
this._0=1;if(!onResize&&this._0<0&&td.offsetWidth==div.offsetWidth&&((this._vert&&pane._gap>0)||(!this._vert&&pane._gapX>0)))
this._0=0;td=elems['b'+i];if(td)
{div=elems['d'+i];if(bar==0)
{bar=$util.toIntPX(null,this._vert?'width':'height',0,td);j=this._vert?div.offsetWidth:div.offsetHeight;if(j>2&&(j<bar&&(this._vert||bar>10)))
bar=j;if(bar<=0)
fixBar=(bar=6)+'px';}
if(fixBar)
{if(this._vert)
td.style.width=fixBar;else
td.style.height=fixBar;}
this._shiftBars+=bar;pane._barTD=td;td._pane=pane;j=pane._get_clientOnlyValue('margin');if(j==1||j==2)
pane._marginImgs=j;imgPrev=j==3;div._pane=pane;pane._barDIV=div;if(this._enabled)
$addHandlers(div,{'mousedown':this._onMouseDown,'mouseover':this._onMouseOver,'mouseout':this._onMouseOut},this);if(i==0)
{this._css[0]=div.className;this._css[5]=td.className;style=$util.getRuntimeStyle(div);barGap=this._getGap(style);if(bar<barGap)
bar=barGap;this._barGapX=this._getGap(style,true);if(this._0<0&&div.offsetHeight>td.offsetHeight)
this._0=1;if(!onResize&&this._0<0&&td.offsetWidth==div.offsetWidth&&((this._vert&&barGap>0)||(!this._vert&&pane._barGapX>0)))
this._0=0;}
elem=elems['p'+i];if(elem)
{elem._pane=pane;elem._dir=-1;pane._collapseImg=elem;this._initCButs(elem);}
elem=elems['n'+i];if(elem)
{var pane2=panes[i+1];elem._pane=pane2;elem._dir=1;pane2._collapseImg=elem;this._initCButs(elem);}}
if(this._0<0)
this._0=(this._ie&&document.compatMode!='CSS1Compat')?0:1;if(td)
{this._setSizeDIV(pane._barDIV,bar,barGap);this._setSizeTD(td,bar);}
pane._onInit();}
var widthInit=element._width,heightInit=element._height;if(onResize)if($util.addLayoutTarget(this))
{onResize=false;var man=this._layoutManager;elem=this._element;td=man?(man.getBody?man.getBody():man._element):null;if(!widthInit&&man&&man.getClientWidth)
widthInit=man.getClientWidth(this);if(!heightInit&&man&&man.getClientHeight)
heightInit=man.getClientHeight(this);if(element.parentNode!=td||!this._withBrowser)
td=null;var offset=0;if(td)
{if(man.getBody)
{i=td.childNodes.length;while(i-->0)
if((w=td.childNodes[i])!=element)
if((j=w.offsetHeight)!=null)if(w.style.position!='absolute')
offset+=j;}
else
width=height=100;}
else
width=height=-1;this._layoutWidth=width/100;this._layoutHeight=height/100;this._layoutOffset=offset;}
this.layout(widthInit,heightInit);tbl.style.height=tbl.style.width='0px';for(i=0;i<iLen;i++)
this._initChildSize(panes[i]);$util.raiseLayoutEvent(this);if(this._vis0)
element.style.visibility='visible';if(this._thisType=='split')
this._raiseClientEvent('Initialize');if(onResize)if(onResize==this._withBrowser)
$addHandlers(this._ie?element:window,{'resize':this._onResize},this);else
this._fixDIV=true;this._once=true;return true;},_initChildSize:function(pane)
{var elem=null,nodes=pane._DIV.childNodes;var i=nodes.length;while(i-->0)
{var node=nodes[i];if(node.getAttribute&&node.getAttribute('CtlMain')=='layout')
{if(elem)
return;elem=node;}}
if(elem)
{elem._width=pane._width;elem._height=pane._height;}},layout:function(width,height)
{if(this._slave)
{var elem=this._element;if(height&&!elem._height)
{elem._height=height;elem._width=width;}
this._slave=null;return!this._onTimer();}
if(!this._timeOut)
if(!this._onTimer())
return true;if(this._drag)
return;if(this._layout(width,height))
return;var i=this._panes.length;while(i-->0)
{var pane=this._panes[i];if(!pane.get_collapsed())
$util.raiseLayoutEvent(pane);}},_layout:function(width,height)
{this._lock=(new Date()).getTime();var elem=this._element,v=this._layoutWidth;if(v&&v>0&&width!=null&&width!=this._oldWidth)
{this._oldWidth=width;v=Math.floor(width*v);if(this._0>0)
v=Math.max(v-(this._vert?this._gap:this._gapX),0);elem.style.width=v+'px';}
v=this._layoutHeight;if(v&&v>0&&height!=null&&height!=this._oldHeight)
{this._oldHeight=height;v=Math.floor(height*v)-this._layoutOffset;if(this._0>0)
v=Math.max(v-(this._vert?this._gapX:this._gap),0);elem.style.height=v+'px';}
var size=this._toSize(elem),sizeX=this._toSize(elem,true);var fix=size!=this._size,fixX=sizeX!=this._sizeX;if(!fix&&!fixX)
return true;var delta=this._size?(size-this._size):0;if(this._fixDIV)
{if(this._noSize)
this._setSizeDIV(elem,size,this._gap);if(this._noSizeX)
this._setSizeDIV(elem,sizeX,this._gapX);this._noSize=this._noSizeX=this._fixDIV=false;}
this._size=size;this._sizeX=sizeX;var panes=new Array(),panesMin=new Array(),panesMax=new Array(),iLenMax=0,iLenMin=0;size-=this._gap+this._shiftBars;sizeX-=this._gapX;if(size<0)size=0;var sizeAvail=size;var sizeTry=0;var avail,pane,i=-1,iLen=this._panes.length;if(this._noSize&&!this._x&&fix)
{while(++i<iLen)
{pane=this._panes[i];v=pane._size;if(!v)
{v=pane._sizeInit;if(v&&pane._sizePerc)
v=Math.floor(size*v/100);}
if(v)
sizeAvail-=v;else
panes[panes.length]=pane;}
i=panes.length;if(i>0)
{v=Math.max(sizeAvail/i,0);while(i-->0)
this._setSizeTD(panes[i]._element,v);panes=new Array();}
sizeAvail=size;i=-1;}
while(++i<iLen)
{pane=this._panes[i];var sizeTD=pane._sizePref;if(sizeTD==null)
sizeTD=this._toSize(pane._element);var min=pane._gap;if(fix||pane._max==null)
{var ms=pane.get_maxSize();v=10000;if(ms)
{v=parseInt(ms);if(ms.indexOf('%')>0)
v=Math.floor(size*v/100);}
pane._max=(v>min)?v:min;ms=pane.get_minSize();v=0;if(ms)
{v=parseInt(ms);if(ms.indexOf('%')>0)
v=Math.floor(size*v/100);}
pane._min=(v>min)?v:min;if(pane.get_locked())
{v=pane._sizeInit;if(v)
{if(pane._sizePerc)
v=Math.floor(size*v/100);}
else
{pane._sizeInit=v=sizeTD;if(!this._x&&this._0==1)
v+=this._vert?2:-min;}
v=pane._validSize(v);if(pane._lock!=v)
this._setPaneSize(pane,pane._lock=v,1);}}
if(pane.get_collapsed())
{if(!this._once)
{this._setPaneSize(pane,sizeTD,2);pane._show(false);}}
else if(pane._lock)
sizeAvail-=pane._lock;else if(fix)
{if(!this._x)
{v=pane._size;if(!v)
{v=pane._sizeInit;if(v)
{if(pane._sizePerc)
v=Math.floor(size*v/100);sizeTD=v;}
else if(this._0==1)
sizeTD+=this._vert?2:-min;}}
pane._sizeTry=sizeTD;if((sizeTD>=pane._max&&delta>=0)||(sizeTD<=pane._min&&delta<=0))
{sizeAvail-=this._setPaneSize(pane,sizeTD,1);if(sizeTD>=pane._max)
panesMax[iLenMax++]=pane;else if(sizeTD<=pane._min)
panesMin[iLenMin++]=pane;}
else
{sizeTry+=sizeTD;panes[panes.length]=pane;}}
if(fixX)
{if(this._vert)
{pane._height=Math.max(sizeX-pane._gapX-pane._margX,0);var img=pane._collapseImg;v=pane._marginImgs;if(v)
{v=sizeX-this._barGapX-this._imgHeight*v;img.style.marginTop=Math.floor((v<1)?0:v/2)+'px';}}
else
pane._width=Math.max(sizeX-pane._gapX-pane._margX,0);this._setSizeDIV(pane._DIV,sizeX,pane._gapX,pane._margX,true);this._setSizeTD(pane._element,sizeX,true);this._setSizeDIV(pane._barDIV,sizeX,this._barGapX,0,true);this._setSizeTD(pane._barTD,sizeX,true);}}
if((iLen=panes.length)<1)
return;sizeTry-=sizeAvail;if(sizeTry>0)
{while(iLenMax-->0)
panes[iLen++]=panesMax[iLenMax];i=iLen;while(i-->0&&sizeTry>0)
{pane=panes[i];avail=pane._sizeTry-pane._min;if(avail>sizeTry)
avail=sizeTry;pane._sizeTry-=avail;sizeTry-=avail;}}
if(sizeTry<0)
{while(iLenMin-->0)
panes[iLen++]=panesMin[iLenMin];i=iLen;while(i-->0&&sizeTry<0)
{pane=panes[i];avail=pane._max-pane._sizeTry;if(avail>-sizeTry)
avail=-sizeTry;pane._sizeTry+=avail;sizeTry+=avail;}}
i=-1;while(++i<iLen)
{pane=panes[i];if(pane._size!=pane._sizeTry)
this._setPaneSize(pane,pane._sizeTry,1);}},_checkNew:function(pane1,pane2)
{var old=pane1._oldSize+pane2._oldSize;var new1=pane1._setNew,new2=pane2._setNew;if(new1!=null)
{pane1._setNew=old-pane2._setNewSize(old-new1);return true;}
if(new2!=null)
{pane2._setNew=old-pane1._setNewSize(old-new2);return true;}
return false;},_split:function(e,up)
{var pane1=this._pane1,pane2=this._pane2;var args,new1=null,new2=null,td=null,div=this._dragDIV,dyn=this.get_dynamicResize();if(up)
{if(!pane1)return;pane1._setNew=pane2._setNew=null;args=this._raiseClientEvent('SplitterBarPositionChanging','SplitterBarPositionCancel',e,null,pane1,pane2);if(args)
{if(args.get_cancel())
{new1=pane1._oldSize;new2=pane2._oldSize;}
else if(this._checkNew(pane1,pane2))
{new1=pane1._setNew;new2=pane2._setNew;}}
if(new1==null&&!dyn)
{new1=pane1._newSize;new2=pane2._newSize;}
if(new1!=null)
{this._setPaneSize(pane1,new1);this._setPaneSize(pane2,new2);}
if(div)
{div.style.display='none';div.style.visibility='hidden';}
this._set_value($IG.SplitterProps.ClientStates,pane1._i+':'+pane2._i);this._raiseClientEvent('SplitterBarPositionChanged','SplitterBarPosition',e,null,pane1,pane2);if(!this._posted)
this._set_value($IG.SplitterProps.ClientStates,'');return;}
var x=this._vert?e.clientX:e.clientY;if(!pane1)
{pane1=this._mousePane;if(!pane1)return;if(!dyn)
td=pane1._barTD;pane2=this._panes[pane1._i+1];pane1=this._validPane(pane1,-1);pane2=this._pane2=this._validPane(pane2,1);if(!pane1||!pane2)
return;this._pane1=pane1;var elem=pane1._element;pane1._oldSize=this._toSize(elem);elem=pane2._element;pane2._oldSize=this._toSize(elem);pane1._setNew=pane2._setNew=null;this._xOld=x;}
x-=this._xOld;var end=null,o1=pane1._oldSize,o2=pane2._oldSize;if(o1+x<=(v=pane1._min))end=x=v-o1;if(o1+x>=(v=pane1._max))end=x=v-o1;if(o2-x<=(v=pane2._min))end=x=o2-v;if(o2-x>=(v=pane2._max))end=x=o2-v;if(x===this._x&&!td)
return;this._x=x;new1=pane1._newSize;new2=pane2._newSize;pane1._newSize=o1+x;pane2._newSize=o2-x;pane1._setNew=pane2._setNew=null;args=this._raiseClientEvent('SplitterBarMoving','SplitterBarPositionCancel',e,null,pane1,pane2);if(args&&args.get_cancel())
{pane1._newSize=new1;pane2._newSize=new2;return;}
if(args)if(this._checkNew(pane1,pane2))
{pane1._newSize=pane1._setNew;pane2._newSize=pane2._setNew;}
if(dyn)
{this._setPaneSize(pane1,pane1._newSize);this._setPaneSize(pane2,pane2._newSize);return;}
var style;if(!div)
{this._dragDIV=div=document.createElement('DIV');style=div.style;style.position='absolute';style.fontSize='1px';style.display='none';style.zIndex=99999;this._element.insertBefore(div,this._element.firstChild);}
style=div.style;if(td)
{style.width=td.offsetWidth+'px';style.height=td.offsetHeight+'px';o1=td.offsetLeft;o2=td.offsetTop;div._x=this._vert?o1:o2;div.className=this._css[3];style.marginLeft=o1+'px';style.marginTop=o2+'px';style.display='';style.visibility='visible';}
x=(div._x+x)+'px';if(this._vert)
style.marginLeft=x;else
style.marginTop=x;if(end!=null||div._end)
{div.className=this._css[3]+((end==null)?'':' '+this._css[4]);div._end=end!=null;}},toggle:function(pane,e,noSet)
{if(!pane||!this._size)return;var elem=pane._collapseImg;var show=pane.get_collapsed(),dir=elem?elem._dir:-1;if(noSet)
show=!show;var i=pane._i-dir,iLen=this._panes.length;var pane2=this._validPane(this._panes[i],-dir);if(!pane2)
pane2=this._validPane(this._panes[pane._i+dir],dir);var args=e?this._raiseClientEvent(show?'Expanding':'Collapsing','SplitterCollapsedStateCancel',e,null,pane,pane2):null;if(args&&args.get_cancel())
return;pane._sizePref=i=pane._size;if(pane2)
{var size=pane2._size+(show?-i:i);pane2._sizePref=Math.max(size,0);}
if(!noSet)
pane.set_collapsed(!show,null,true);pane._show(show);if(elem)
{i=dir+1+(show?0:1);elem.src=this._cImgs[i];elem.alt=this._cImgs[i+8];}
this._size=-1;this.layout();pane._sizePref=null;if(pane2)
pane2._sizePref=null;if(e)
this._raiseClientEvent(show?'Expanded':'Collapsed','SplitterCollapsedState',e,null,pane,pane2);},_onMouseOver:function(e)
{var me=$util._splitObj;if(me&&me!=this)
return;var elem=e?e.target:null;if(!elem||!elem._pane||this._drag)
return;var dir=elem._dir;if(dir)
this._mouseInCBut=true;else
this._mouseIn=true;this._mouseElem=elem;if(dir)
elem.src=this._cImgs[dir+5+(elem._pane.get_collapsed()?1:0)];else
{if(!elem._cssOld)
elem._cssOld=elem.className;elem.className=elem._cssOld+' '+this._css[1];}
this._raiseClientEvent((dir?'CollapseButton':'SplitterBar')+'MouseOver',null,e);},_onMouseOut:function(e,noEvt)
{var me=$util._splitObj;if(me&&me!=this)
return;if(e&&noEvt!==true)
this._mouseInCBut=this._mouseIn=false;if(this._drag)
return;var elem=this._mouseElem;if(!elem)
return;var dir=elem._dir;if(dir)
elem.src=this._cImgs[dir+1+(elem._pane.get_collapsed()?1:0)];else
elem.className=elem._cssOld;this._raiseClientEvent((dir?'CollapseButton':'SplitterBar')+'MouseOut',null,e);},_onMouseDown:function(e)
{var me=$util._splitObj;if(me&&me._onMouseUp)
me._onMouseUp(e);this._pane1=null;if(!e)if((e=window.event)==null)
return;var elem=this._mouseElem;if(!elem||!e||e.button!=0)
return;if(this._mouseInCBut)
{this.toggle(elem._pane,e);return;}
elem.className=elem._cssOld+' '+this._css[2];this._raiseClientEvent('SplitterBarMouseDown',null,e);this._drag=true;$util.cancelEvent(e);if(!this._onSelectFn)
{this._onMoveDocFn=Function.createDelegate(this,this._onMoveDoc);this._onMouseMoveFn=Function.createDelegate(this,this._onMouseMove);this._onMouseUpFn=Function.createDelegate(this,this._onMouseUp);this._onSelectFn=Function.createDelegate(this,this._onSelectStart);}
$addHandler(document,'mouseup',this._onMouseUpFn);$addHandler(document,'mousemove',this._onMoveDocFn);if(Sys.Browser.agent===Sys.Browser.Safari)
document.onselectstart=this._onSelectFn;else
$addHandler(document,'selectstart',this._onSelectFn);this._mousePane=elem._pane;elem=this._getShell(true);$util._splitObj=this;if(this._ie)
{elem=elem.contentWindow.document;elem.attachEvent("onmousemove",this._onMouseMove);elem.attachEvent("onmouseup",this._onMouseUp);}
else
$addHandler(this._element,'mousemove',this._onMouseMoveFn);},_getShell:function(show,e)
{var v,style,elem=this._ie?$util._splitElem:this._splitElem,me=$util._splitObj,div=this._element;if(!elem)
{elem=document.createElement(this._ie?'IFRAME':'DIV');style=elem.style;style.zIndex=100000;style.filter="progid:DXImageTransform.Microsoft.Alpha(Opacity=0);";style.opacity=0;style.position='absolute';if(this._ie)
{$util._splitElem=elem;elem.frameBorder='no';elem.scrolling='no';elem.src='javascript:""';}
else
{elem.innerHTML='&nbsp;';this._splitElem=elem;div.insertBefore(elem,div.firstChild);}}
style=elem.style;if(show)
{if(me&&me!=this&&me._onMouseUp)
me._onMouseUp(e);if(this._ie)
div.insertBefore(elem,div.firstChild);if((v=div.offsetWidth-2)<1)
v=1;style.width=v+'px';if((v=div.offsetHeight-2)<1)
v=1;style.height=v+'px';style.visibility='visible';style.display='';}
else
{style.visibility='hidden';style.display='none';if(this._ie)
div.removeChild(elem);}
return elem;},_isInside:function(e)
{var op,elem=e.target,re=e.rawEvent,pe=null,div=this._element;if(!re)re=e;var x=re.layerX,y=re.layerY;if(x==null)
{x=re.offsetX;y=re.offsetY;}
if(x==null)
{x=e.offsetX;y=e.offsetY;}
if(!elem||x==null)
return true;while(elem&&elem!=this._splitElem&&elem!=div&&elem!=this._tbl)
{if((op=elem.offsetParent)!=pe)
{x+=elem.offsetLeft;y+=elem.offsetTop;pe=op;}
elem=elem.parentNode;}
return elem&&x>-4&&y>-4&&x<div.offsetWidth+4&&y<div.offsetHeight+4;},_onMouseMove:function(e)
{var me=this;if(!me._tbl)
me=$util._splitObj;if(!me||!me._drag||!e)
return;if(!me._isInside(e))
return;me._lastMove=(new Date()).getTime();me._split(e);},_onMouseUp:function(e)
{var me=this;if(!me._tbl)
me=$util._splitObj;if(!me)return;if(me._drag)
{me._raiseClientEvent('SplitterBarMouseUp',null,e);me._split(e,true);}
me._pane1=null;me._mousePane=null;if(!me._drag)
return;me._drag=false;me.layout();$removeHandler(document,'mouseup',me._onMouseUpFn);$removeHandler(document,'mousemove',me._onMoveDocFn);if(Sys.Browser.agent===Sys.Browser.Safari)
document.onselectstart=null;else
$removeHandler(document,'selectstart',me._onSelectFn);if(!me._mouseIn)
me._onMouseOut(e,true);var elem=me._getShell(false);$util._splitObj=null;if(me._ie)
{elem.detachEvent('onmousemove',me._onMouseMove);elem.detachEvent('onmouseup',me._onMouseUp);}
else
$removeHandler(me._element,'mousemove',me._onMouseMoveFn);},_onMoveDoc:function(e)
{var me=$util._splitObj;if(!me||!e)return;var b=e.button,t=me._lastMove;if(b==0&&e.rawEvent)
b=e.rawEvent.button;if(b==1)
me._but=1;else if(me._but==1)
t=1;if(t&&(new Date()).getTime()-t>me._timeOut)
me._onMouseUp(e);},_onResize:function(e)
{if(!this._lock)
return;if(this._lock!=(new Date()).getTime())
if((this._noSize&&Math.abs(this._toSize(this._element)-this._size)>1)||(this._noSizeX&&Math.abs(this._toSize(this._element,true)-this._sizeX)>1))
this.layout();},_onSelectStart:function(e)
{return this._drag?$util.cancelEvent(e):true;}}
$IG.WebSplitter.registerClass('Infragistics.Web.UI.WebSplitter',$IG.ControlMain);$IG.SplitterBarPositionCancelEventArgs=function()
{$IG.SplitterBarPositionCancelEventArgs.initializeBase(this);}
$IG.SplitterBarPositionCancelEventArgs.prototype={get_prevPane:function()
{return this._props[2];},get_nextPane:function()
{return this._props[3];},get_prevPaneOldSize:function()
{return this.get_prevPane()._oldSize;},get_prevPaneNewSize:function()
{return this.get_prevPane()._newSize;},get_nextPaneOldSize:function()
{return this.get_nextPane()._oldSize;},get_nextPaneNewSize:function()
{return this.get_nextPane()._newSize;},set_prevPaneNewSize:function(value)
{this.get_prevPane()._setNewSize(value);},set_nextPaneNewSize:function(value)
{this.get_nextPane()._setNewSize(value);}}
$IG.SplitterBarPositionCancelEventArgs.registerClass('Infragistics.Web.UI.SplitterBarPositionCancelEventArgs',$IG.CancelEventArgs);$IG.SplitterBarPositionEventArgs=function()
{$IG.SplitterBarPositionEventArgs.initializeBase(this);}
$IG.SplitterBarPositionEventArgs.prototype={get_prevPane:function()
{return this._props[2];},get_nextPane:function()
{return this._props[3];},get_prevPaneOldSize:function()
{return this.get_prevPane()._oldSize;},get_prevPaneNewSize:function()
{return this.get_prevPane()._newSize;},get_nextPaneOldSize:function()
{return this.get_nextPane()._oldSize;},get_nextPaneNewSize:function()
{return this.get_nextPane()._newSize;}}
$IG.SplitterBarPositionEventArgs.registerClass('Infragistics.Web.UI.SplitterBarPositionEventArgs',$IG.PostBackEventArgs);$IG.SplitterCollapsedStateCancelEventArgs=function()
{$IG.SplitterCollapsedStateCancelEventArgs.initializeBase(this);}
$IG.SplitterCollapsedStateCancelEventArgs.prototype={get_pane:function()
{return this._props[2];},get_affectedPane:function()
{return this._props[3];}}
$IG.SplitterCollapsedStateCancelEventArgs.registerClass('Infragistics.Web.UI.SplitterCollapsedStateCancelEventArgs',$IG.CancelEventArgs);$IG.SplitterCollapsedStateEventArgs=function()
{$IG.SplitterCollapsedStateEventArgs.initializeBase(this);}
$IG.SplitterCollapsedStateEventArgs.prototype={_getPostArgs:function()
{return':'+this.get_pane()._i;},get_pane:function()
{return this._props[2];},get_affectedPane:function()
{return this._props[3];}}
$IG.SplitterCollapsedStateEventArgs.registerClass('Infragistics.Web.UI.SplitterCollapsedStateEventArgs',$IG.PostBackEventArgs);$IG.SplitterPaneProps=new function()
{var count=$IG.ContentPaneProps.Count;this.Collapsed=[count++,0];this.CollapseDirection=[count++,1];this.Locked=[count++,0];this.MaxSize=[count++,''];this.MinSize=[count++,'10px'];this.Size=[count++,''];this.Count=count;};$IG.SplitterPane=function(adr,element,props,control,csm,collection,parent)
{$IG.SplitterPane.initializeBase(this,[adr,element,props,control,csm,collection,parent]);}
$IG.SplitterPane.prototype={get_collapsed:function()
{return this._get_value($IG.SplitterPaneProps.Collapsed)==1;},set_collapsed:function(value,fire,ctl)
{if(value==this.get_collapsed())return;this._set_value($IG.SplitterPaneProps.Collapsed,value?1:0);if(!ctl&&this._owner)
this._owner.toggle(this,fire,true);},_show:function(vis)
{this._element.style.display=vis?'':'none';this._element.style.visibility=vis?'visible':'hidden';},get_collapsedDirection:function()
{return this._get_value($IG.SplitterPaneProps.CollapseDirection);},get_locked:function()
{return this._get_value($IG.SplitterPaneProps.Locked)==1;},set_locked:function(value)
{this._set_value($IG.SplitterPaneProps.Locked,value?1:0);var owner=this._owner;if(!owner)
return;if(this._size)
this._sizeInit=this._size;this._lock=null;owner._size=-1;owner.layout();},get_maxSize:function()
{return this._get_value($IG.SplitterPaneProps.MaxSize);},set_maxSize:function(value)
{this._set_value($IG.SplitterPaneProps.MaxSize,value);this._max=null;var ctl=this._owner;if(ctl&&ctl._size)
ctl._size=-1;},get_minSize:function()
{return this._get_value($IG.SplitterPaneProps.MinSize);},set_minSize:function(value)
{this._set_value($IG.SplitterPaneProps.MinSize,value);this._max=null;var ctl=this._owner;if(ctl&&ctl._size)
ctl._size=-1;},_validSize:function(val)
{if(val<this._min)val=this._min;if(val>this._max&&this._max)val=this._max;return val;},get_index:function()
{return this._i;},get_size:function()
{return this._size},set_size:function(value)
{var ctl=this._owner;var pane=ctl._validPane(this,1,1);if(!pane)
pane=ctr._validPane(this,-1,1);if(!pane||this.get_collapsed()||!ctl._once)
return false;var both=this._size+pane._size;if(value>both)value=both;value=this._validSize(value);var val2=pane._validSize(both-value);value=both-val2;ctl._setPaneSize(this,value);ctl._setPaneSize(pane,val2);return true;},_setNewSize:function(val)
{return this._setNew=this._validSize(val);},dispose:function()
{if(this._split)
$clearHandlers(this._split);$IG.SplitterPane.callBaseMethod(this,'dispose');}}
$IG.SplitterPane.registerClass('Infragistics.Web.UI.SplitterPane',$IG.LayoutPane);