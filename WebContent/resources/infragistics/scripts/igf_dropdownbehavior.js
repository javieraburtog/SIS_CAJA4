// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");var $IG=Infragistics.Web.UI;$IG.DropDownPopupPosition=function(){}
$IG.DropDownPopupPosition.prototype={Default:0,Center:1,Left:2,Right:3,TopLeft:4,TopRight:5,BottomLeft:6,BottomRight:7};$IG.DropDownPopupPosition.registerEnum("Infragistics.Web.UI.DropDownPopupPosition");$IG.DropDownBehavior=function(sourceElement,dropDownIsChild,outerContainer){if(!sourceElement){throw Error.argumentNull('sourceElement');}
this._sourceElement=sourceElement;this._targetContainer=null;this._targetContent=null;this._position=$IG.DropDownPopupPosition.Default;this._outerContainer=outerContainer;this._offsetX=0;this._offsetY=0;this._containerHeight=0;this._offScreen=false;this._enableAutomaticPositioning=true;this._enableMovingTargetWithSource=true;this._enableAnimations=true;this._animationType=$IG.AnimationEquationType.EaseInOut;this._visible=false;this._visibleOnFocus=false;this._visibleOnBlur=false;this._visibleOnMouseOver=false;this._visibleOnClick=false;this._visibleOnKeyDown=false;this._dropDownAnimation=null;this._animationDurationMs=300;this._zIndex=10000;this._animationsContainer=null;this._targetBounds=null;this._dropDownIsChild=dropDownIsChild;this._events=new $IG.DropDownEvents(this);this._animationEndListener=null;this._mouseUpDelegate=Function.createDelegate(this,this._mouseUpHandler);this._mouseClickDelegate=Function.createDelegate(this,this._mouseClickHandler);this._mouseOverDelegate=Function.createDelegate(this,this._mouseOverHandler);this._mouseDownDelegate=Function.createDelegate(this,this._mouseDownHandler);this._keyDownDelegate=Function.createDelegate(this,this._keyDownHandler);this._keyUpDelegate=Function.createDelegate(this,this._keyUpHandler);this._focusDelegate=Function.createDelegate(this,this._focusHandler);this._blurDelegate=Function.createDelegate(this,this._blurHandler);this._moveDelegate=Function.createDelegate(this,this._onMove);if(this._enableMovingTargetWithSource){this._checkDelegate=Function.createDelegate(this,this._onCheckPosition);this._sourceLocation=Sys.UI.DomElement.getLocation(this._sourceElement);this._sourceBounds=Sys.UI.DomElement.getBounds(this._sourceElement);this._closeCheckID=setInterval(this._checkDelegate,500);}}
$IG.DropDownBehavior.prototype={get_sourceElement:function()
{return this._sourceElement;},get_targetContainer:function()
{return this._targetContainer;},get_animationsContainer:function()
{return this._animationsContainer;},set_targetContainer:function(container)
{if(!container)
{throw Error.argumentNull('container');}
this._targetContainer=container;},set_targetContainerHeight:function(height)
{this._targetBounds.height=height;this._containerHeight=height;if(this.get_enableAnimations())
$util.setAbsoluteHeight(this._animationsContainer,height);},get_targetContent:function()
{return this._targetContent;},set_targetContent:function(content)
{this._targetContent=content;},get_zIndex:function()
{return this._zIndex;},set_zIndex:function(zindex)
{this._zIndex=zindex;},get_position:function()
{return this._position;},set_position:function(position)
{this._position=position;},get_enableAutomaticPositioning:function()
{return this._enableAutomaticPositioning;},set_enableAutomaticPositioning:function(enabled)
{this._enableAutomaticPositioning=enabled;},get_enableMovingTargetWithSource:function()
{return this._enableMovingTargetWithSource;},set_enableMovingTargetWithSource:function(enabled)
{this._enableMovingTargetWithSource=enabled;if(this._enableMovingTargetWithSource)
{clearInterval(this._closeCheckID);this._checkDelegate=Function.createDelegate(this,this._onCheckPosition);this._sourceLocation=Sys.UI.DomElement.getLocation(this._sourceElement);this._sourceBounds=Sys.UI.DomElement.getBounds(this._sourceElement);this._closeCheckID=setInterval(this._checkDelegate,200);}else
{clearInterval(this._closeCheckID);}},get_enableAnimations:function()
{return this._enableAnimations;},set_enableAnimations:function(enableAnimations)
{this._enableAnimations=enableAnimations;},get_animationType:function()
{return this._animationType;},set_animationType:function(animationType)
{this._animationType=animationType;},get_animationDurationMs:function()
{return this._animationDurationMs;},set_animationDurationMs:function(duration)
{this._animationDurationMs=duration;},get_offsetX:function()
{return this._offsetX;},set_offsetX:function(offsetX)
{this._offsetX=offsetX;},get_offsetY:function()
{return this._offsetY;},set_offsetY:function(offsetY)
{this._offsetY=offsetY;},get_offScreen:function()
{return this._offScreen;},set_offScreen:function(offScreen)
{this._offScreen=offScreen;},get_visibleOnFocus:function()
{return this._visibleOnFocus;},set_visibleOnFocus:function(visibleOnFocus)
{this._visibleOnFocus=visibleOnFocus;},get_visibleOnBlur:function()
{return this._visibleOnBlur;},set_visibleOnBlur:function(visibleOnBlur)
{this._visibleOnBlur=visibleOnBlur;},get_isAnimating:function()
{if(this._dropDownAnimation)
return this._dropDownAnimation.get_isAnimating();else
return false;},get_visibleOnMouseOver:function()
{return this._visibleOnMouseOver;},set_visibleOnClick:function(visibleOnClick)
{this._visibleOnClick=visibleOnClick;},get_visibleOnClick:function()
{return this._visibleOnClick;},set_visibleOnMouseOver:function(visibleOnMouseOver)
{this._visibleOnMouseOver=visibleOnMouseOver;},get_visibleOnKeyDown:function()
{return this._visibleOnKeyDown;},set_visibleOnKeyDown:function(visibleOnKeyDown)
{this._visibleOnKeyDown=visibleOnKeyDown;},get_dropDownIsChild:function()
{return this._dropDownIsChild;},get_visible:function()
{return this._visible;},set_visible:function(visible)
{var cancelEvent=false;this._sourceLocation=Sys.UI.DomElement.getLocation(this._sourceElement);if(this._dropDownAnimation&&this._dropDownAnimation.get_isAnimating())
{return;}
if(visible)
{cancelEvent=this.get_Events()._fireEvent("SettingVisible",this);}else
{cancelEvent=this.get_Events()._fireEvent("SettingHidden",this);}
if(!cancelEvent)
{if(visible&&!this._visible)
{this.__showDropDown();}else if(!visible&&this._visible)
{this.__hideDropDown();}
if(visible)
{this.get_Events()._fireEvent("SetVisible",this);}else
{this.get_Events()._fireEvent("SetHidden",this);}
if(!this._enableAnimations)
{this._visible=visible;}}},get_Events:function()
{return this._events;},getBounds:function()
{return this._bounds;},triggerVisibility:function()
{if(this._visible)
{this.set_visible(false);}else
{this.set_visible(true);}},set_containerMaxHeight:function(maxHeight,containerContent,container)
{var realContainer=this.get_enableAnimations()?this._animationsContainer:this._targetContainer;realContainer.style.left=-10000;realContainer.style.top=-10000;realContainer.style.display='block';realContainer.style.visibility='visible';if(maxHeight<=containerContent.offsetHeight)
{container.style.height=maxHeight+'px';}
realContainer.style.left=0;realContainer.style.top=0;realContainer.style.display='none';realContainer.style.visibility='hidden';},init:function()
{$addHandler(this._sourceElement,'mouseup',this._mouseUpDelegate);$addHandler(this._sourceElement,'click',this._mouseClickDelegate);$addHandler(this._sourceElement,'mouseover',this._mouseOverDelegate);$addHandler(this._sourceElement,'mousedown',this._mouseDownDelegate);$addHandler(this._sourceElement,'keydown',this._keyDownDelegate);$addHandler(this._sourceElement,'keyup',this._keyUpDelegate);$addHandler(this._sourceElement,'focus',this._focusDelegate);$addHandler(this._sourceElement,'blur',this._blurDelegate);if(this._dropDownIsChild)
{this._sourceElement.appendChild(this._targetContainer);}else
{document.forms[0].appendChild(this._targetContainer);}
this._targetContainer.style.display='block';this._targetContainer.style.visibility='visible';this._targetContainer.style.position='';this._targetBounds=Sys.UI.DomElement.getBounds(this._targetContainer);this._containerHeight=this._targetBounds.height;this._dropDownAnimation=new $IG.DropDownAnimation(this);this._dropDownAnimation.set_duration(this._animationDurationMs);if(this._enableAnimations)
{this._animationsContainer=document.createElement("div");this._animationsContainer.id=this._targetContainer.id+"_animations";this._animationsContainer.style.display='none';this._animationsContainer.style.visibility='hidden';this._animationsContainer.style.overflow='hidden';this._animationsContainer.style.position='absolute';}
var container=(this._enableAnimations)?this._animationsContainer:this._targetContainer;if(this._dropDownIsChild)
{this._sourceElement.insertBefore(container,this._sourceElement.firstChild);}else
{document.forms[0].appendChild(container);}
if(this._enableAnimations)
{$util.setAbsoluteHeight(this._animationsContainer,0);this._containerHeight=this._targetBounds.height;$util.setAbsoluteWidth(this._animationsContainer,this._targetBounds.width);$addHandler(this._animationsContainer,'move',this._moveDelegate);this._animationsContainer.appendChild(this._targetContainer);}else
{this._targetContainer.style.display='none';this._targetContainer.style.visibility='hidden';this._targetContainer.style.position='absolute';$addHandler(this._targetContainer,'move',this._moveDelegate);}},dispose:function()
{this._dropDownAnimation.stop();this._dropDownAnimation.onEnd();try
{$removeHandler(this._sourceElement,'mouseup',this._mouseUpDelegate);}catch(e){}
try
{$removeHandler(this._sourceElement,'click',this._mouseClickDelegate);}catch(e){}
try
{$removeHandler(this._sourceElement,'mouseover',this._mouseOverDelegate);}catch(e){}
try
{$removeHandler(this._sourceElement,'mousedown',this._mouseDownDelegate);}catch(e){}
try
{$removeHandler(this._sourceElement,'keydown',this._keyDownDelegate);}catch(e){}
try
{$removeHandler(this._sourceElement,'keyup',this._keyUpDelegate);}catch(e){}
try
{$removeHandler(this._sourceElement,'focus',this._focusDelegate);}catch(e){}
try
{$removeHandler(this._sourceElement,'blur',this._blurDelegate);}catch(e){}
try
{$removeHandler(this._targetContainer,'move',this._moveDelegate);}catch(e){}
delete this._mouseUpDelegate;delete this._mouseClickDelegate;delete this._mouseOverDelegate;delete this._mouseDownDelegate;delete this._keyDownDelegate;delete this._keyUpDelegate;delete this._focusDelegate;delete this._blurDelegate;delete this._moveDelegate;delete this._events;delete this._containerHeight;delete this._dropDownAnimation;delete this._animationsContainer;clearInterval(this._closeCheckID);delete this._closeCheckID;delete this._checkDelegate;this._sourceElement=null;this._targetContainer=null;delete this._position;this._outerContainer=null;delete this._offsetX;delete this._offsetY;delete this._containerHeight;delete this._offScreen;delete this._enableAutomaticPositioning;delete this._enableMovingTargetWithSource;delete this._enableAnimations;delete this._animationType;delete this._visible;delete this._visibleOnFocus;delete this._visibleOnBlur;delete this._visibleOnMouseOver;delete this._visibleOnClick;delete this._visibleOnKeyDown;delete this._dropDownAnimation;delete this._animationDurationMs;delete this._zIndex;delete this._targetBounds;delete this._dropDownIsChild;},_onCheckPosition:function()
{var oldPos=this._sourceLocation;var oldBounds=this._sourceBounds;var shouldHideFromContainer=false;try
{var currentPos=Sys.UI.DomElement.getLocation(this._sourceElement);var currentBounds=Sys.UI.DomElement.getBounds(this._sourceElement);if(this._outerContainer)
{var outerContainerBounds=Sys.UI.DomElement.getBounds(this._outerContainer);var outerContainerPos=Sys.UI.DomElement.getLocation(this._outerContainer);if(currentPos.y+currentBounds.height>outerContainerBounds.height+outerContainerPos.y||currentPos.y<outerContainerPos.y)
{shouldHideFromContainer=true;}}}
catch(e)
{return;}
if(oldPos.x!=currentPos.x||oldPos.y!=currentPos.y||currentBounds.width!=oldBounds.width||currentBounds.height!=oldBounds.height)
{if((currentBounds.width<=0&&currentBounds.height<=0)||shouldHideFromContainer)
{this._targetContainer.style.visibility='hidden';this._targetContainer.style.display='none';}else
{if(this.get_visible())
{this.set_visible(false);this.set_visible(true);}}
this._sourceLocation=currentPos;this._sourceBounds=currentBounds;}},_onMove:function()
{var container=(this._enableAnimations)?this._animationsContainer:this._targetContainer;if(this._childFrame)
{container.parentNode.insertBefore(this._childFrame,container);this._childFrame.style.top=container.style.top;this._childFrame.style.left=container.style.left;}},_mouseUpHandler:function(e)
{},_mouseClickHandler:function(e)
{},_mouseOverHandler:function(e)
{if(this._visibleOnMouseOver)
{this.__handleVisible(e);}},_mouseDownHandler:function(e)
{if(this._visibleOnMouseDown)
{this.__handleTrigger(e);}},_keyDownHandler:function(e)
{if(this._visibleOnKeyDown)
{this.__handleVisible(e);}},_keyUpHandler:function(e)
{if(this._visibleOnKeyDown)
{this.__handleHidden(e);}},_focusHandler:function(e)
{if(this._visibleOnFocus)
{this.__handleVisible(e);}},_blurHandler:function(e)
{if(this._visibleOnBlur)
{this.__handleHidden(e);}},_addBackgroundIFrame:function()
{var container=(this._enableAnimations)?this._animationsContainer:this._targetContainer;if((Sys.Browser.agent===Sys.Browser.InternetExplorer)&&(Sys.Browser.version<7))
{if(!this._childFrame)
{this._childFrame=document.createElement("iframe");this._childFrame.src="javascript:'<html></html>';";this._childFrame.style.position="absolute";this._childFrame.style.display="none";this._childFrame.scrolling="no";this._childFrame.frameBorder="0";this._childFrame.tabIndex="-1";this._childFrame.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)";container.parentNode.insertBefore(this._childFrame,container);}
this._setBounds(this._childFrame,this._getBounds(container));this._childFrame.style.display=container.style.display;if(container.currentStyle&&container.currentStyle.zIndex)
{this._childFrame.style.zIndex=container.currentStyle.zIndex;}else if(container.style.zIndex)
{this._childFrame.style.zIndex=container.style.zIndex;}}},_attach:function()
{if(!this._dropDownIsChild)
{this._sourceElement.appendChild(this._targetContainer);}},_detach:function()
{if(!this._dropDownIsChild)
{if(this.get_enableAnimations())
{this._animationsContainer.appendChild(this._targetContainer);}
else
{document.forms[0].appendChild(this._targetContainer);}}},_setAnimationEndListener:function(element)
{this._animationEndListener=element;},_getBounds:function(element)
{var offset=$util.getPosition(element);return new Sys.UI.Bounds(offset.x,offset.y,element.offsetWidth||0,element.offsetHeight||0);},_setBounds:function(element,bounds)
{if(!element)
{throw Error.argumentNull('element');}
if(!bounds)
{throw Error.argumentNull('bounds');}
$util.setAbsoluteHeight(element,bounds.height);$util.setAbsoluteWidth(element,bounds.width);this.__setLocation(element,bounds);},_adjustDropDownPosition:function(targetBounds,sourceBounds,currentX,currentY)
{this._offScreen=false;var x=currentX,y=currentY;var container=(this._enableAnimations)?this._animationsContainer:this._targetContainer;var targetContainerPosition=Sys.UI.DomElement.getLocation(container);var sourceElementPosition=Sys.UI.DomElement.getLocation(this._sourceElement);var reset=false;var windowHeight=0;var windowScrollTop=0;if(document.compatMode=="BackCompat")
{windowHeight=document.body.clientHeight;windowScrollTop=document.body.scrollTop;}else
{windowHeight=window.innerHeight;windowScrollTop=window.pageYOffset;if($util.IsIE)
{windowHeight=document.documentElement.clientHeight;windowScrollTop=document.documentElement.scrollTop;}}
if(this._outerContainer)
{var outerContainerPos=Sys.UI.DomElement.getLocation(this._outerContainer);var outerContainerBounds=Sys.UI.DomElement.getBounds(this._outerContainer);if(targetBounds.height<outerContainerBounds.height)
{if(targetContainerPosition.y+targetBounds.height>outerContainerPos.y+outerContainerBounds.height)
{if(targetContainerPosition.y-targetBounds.height>outerContainerPos.y)
{y-=targetBounds.height;if(this.get_position()!=$IG.DropDownPopupPosition.Left&&this.get_position()!=$IG.DropDownPopupPosition.Right)
{y-=sourceBounds.height;}else
{y+=sourceBounds.height;}
reset=true;this._offScreen=true;y-=this._offsetY*2;}}}}else if(targetBounds.height>windowHeight-sourceElementPosition.y+windowScrollTop)
{y-=targetBounds.height;if(this.get_position()!=$IG.DropDownPopupPosition.Left&&this.get_position()!=$IG.DropDownPopupPosition.Right)
{y-=sourceBounds.height;}else
{y+=sourceBounds.height;}
reset=true;this._offScreen=true;y-=this._offsetY*2;}
if(reset)
{container.style.marginTop=y+'px';}},__handleHidden:function(e)
{if(this._visible)
{this.set_visible(false);}},__handleVisible:function(e)
{if(!this._visible)
{this.set_visible(true);}},__handleTrigger:function(e)
{if(this._visible)
{this.set_visible(false);}else
{this.set_visible(true);}},__showDropDown:function()
{var container=(this._enableAnimations)?this._animationsContainer:this._targetContainer;container.style.position='absolute';container.style.display='';container.style.visibility='visible';container.style.zIndex=this._zIndex;var x=0,y=0;y=Sys.UI.DomElement.getBounds(this._sourceElement).height;var targetBounds=this._targetBounds;var sourceBounds=Sys.UI.DomElement.getBounds(this._sourceElement);var isTop=this._position==$IG.DropDownPopupPosition.TopLeft||this._position==$IG.DropDownPopupPosition.TopRight;switch(this._position)
{case $IG.DropDownPopupPosition.Default:break;case $IG.DropDownPopupPosition.Center:x+=sourceBounds.width/2;x-=targetBounds.width/2;break;case $IG.DropDownPopupPosition.Left:x-=targetBounds.width;y-=sourceBounds.height;break;case $IG.DropDownPopupPosition.Right:x+=sourceBounds.width;y-=sourceBounds.height;break;case $IG.DropDownPopupPosition.BottomLeft:break;case $IG.DropDownPopupPosition.BottomRight:x+=sourceBounds.width;x-=targetBounds.width;break;case $IG.DropDownPopupPosition.TopLeft:y-=targetBounds.height;y-=sourceBounds.height;break;case $IG.DropDownPopupPosition.TopRight:x+=sourceBounds.width;x-=targetBounds.width;y-=targetBounds.height;y-=sourceBounds.height;break;default:break;}
container.style.marginLeft='';container.style.marginTop='';var p0=Sys.UI.DomElement.getLocation(this._sourceElement),p1=Sys.UI.DomElement.getLocation(container);if(Sys.Browser.agent==Sys.Browser.Safari&&this._sourceElement.nodeName=='TD')
{y-=this._sourceElement.offsetTop;y+=this._sourceElement.parentNode.offsetTop;}
if(this._outerContainer&&Sys.Browser.agent==Sys.Browser.Opera)
{y-=this._outerContainer.scrollTop;}
x+=p0.x-p1.x;y+=p0.y-p1.y;x+=this._offsetX;if(isTop)
{y-=this._offsetY;}else
{y+=this._offsetY;}
container.style.marginLeft=x+'px';container.style.marginTop=y+'px';if(!isTop&&this._enableAutomaticPositioning)
{this._adjustDropDownPosition(targetBounds,sourceBounds,x,y);}
this._targetContainer.style.display="";this._targetContainer.style.visibility="visible";var currentHeight=Sys.UI.DomElement.getBounds(this._targetContainer).height;this._containerHeight=currentHeight;this._addBackgroundIFrame();this._dropDownAnimation.set_maxHeight(this._containerHeight);if(this._enableAnimations)
{this._dropDownAnimation.play();}},__hideDropDown:function()
{if(this._enableAnimations)
{this._dropDownAnimation.play();}else
{this._targetContainer.style.display='none';this._targetContainer.style.visibility='hidden';if(this._childFrame)
{this._childFrame.style.display="none";}}},__notifyAnimationEnd:function()
{if(this._animationEndListener!=null&&this._animationEndListener._onAnimationEnd)
this._animationEndListener._onAnimationEnd();},__setLocation:function(element,point)
{Sys.UI.DomElement.setLocation(element,point.x,point.y);}};$IG.DropDownBehavior.registerClass("Infragistics.Web.UI.DropDownBehavior");$IG.UIObjectDropDownBehavior=function()
{}
$IG.UIObjectDropDownBehavior.prototype={};$IG.UIObjectDropDownBehavior.registerClass("Infragistics.Web.UI.UIObjectDropDownBehavior");$IG.DropDownEvents=function(behavior)
{this._handlers={};this._behavior=behavior;}
$IG.DropDownEvents.prototype={addSetVisibleHandler:function(handler)
{this.__addHandler("SetVisible",handler,$IG.DropDownEventArgs);},addSetHiddenHandler:function(handler)
{this.__addHandler("SetHidden",handler,$IG.DropDownEventArgs);},addSettingHiddenHandler:function(handler)
{this.__addHandler("SettingHidden",handler,$IG.CancelDropDownEventArgs);},addSettingVisibleHandler:function(handler)
{this.__addHandler("SettingVisible",handler,$IG.CancelDropDownEventArgs);},removeSetVisibleHandler:function()
{this.__removeHandler("SetVisible");},removeSettingVisibleHandler:function()
{this.__removeHandler("SettingVisible");},removeSetHiddenHandler:function()
{this.__removeHandler("SetHidden");},removeSettingHiddenHandler:function()
{this.__removeHandler("SettingHidden");},__addHandler:function(name,handler,args)
{var handlers=this._handlers[name];if(!handlers)
this._handlers[name]=handlers=[];var i=-1;while(++i<handlers.length)
if(handlers[i])
break;handlers[i]=[handler,args];},__removeHandler:function(name,handler,args)
{var handlers=this._handlers[name];if(!handlers)
return;var i=-1;while(++i<handlers.length)
{var obj=handlers[i];if(obj&&obj[0]==handler)
handlers[i]=null;}},_fireEvent:function(name,evntArgs)
{var handlers=this._handlers[name];var count=handlers?handlers.length:0;for(var i=0;i<count;i++)
{var handler=handlers[i];if(!handler)
continue;var evnt=handler[0];var args=new handler[1](evntArgs);evnt(this._behavior,args);if(args._cancel)
return true;}
return false;}}
$IG.DropDownEvents.registerClass("Infragistics.Web.UI.DropDownEvents");$IG.DropDownEventArgs=function(behavior)
{this._behavior=behavior;}
$IG.DropDownEventArgs.prototype={get_source:function()
{return this._behavior.get_sourceElement();},get_targetContainer:function()
{return this._behavior.get_targetContainer();},get_targetContent:function()
{return this._behavior.get_targetContent();},get_dropDownBehavior:function()
{return this._behavior;}}
$IG.DropDownEventArgs.registerClass("Infragistics.Web.UI.DropDownEventArgs");$IG.CancelDropDownEventArgs=function()
{this._cancel=false;$IG.CancelDropDownEventArgs.initializeBase(this);}
$IG.CancelDropDownEventArgs.prototype={get_cancel:function()
{return this._cancel;},set_cancel:function(cancel)
{this._cancel=cancel;}}
$IG.CancelDropDownEventArgs.registerClass("Infragistics.Web.UI.CancelDropDownEventArgs",$IG.DropDownEventArgs);$IG.DropDownAnimation=function(behavior)
{this._dropDownBehavior=behavior;this._increaseDelta=0;this._marginDelta=0;this._accumulatedHeight=0;this._maxHeight=0;this._minHeight=0;this._maxMargin=0;this._container=null;this._isTop=false;this._accumulatedMarginTop=0;$IG.DropDownAnimation.initializeBase(this);}
$IG.DropDownAnimation.prototype={set_maxHeight:function(maxHeight){this._maxHeight=maxHeight;},onBegin:function(){this._container=this._dropDownBehavior.get_animationsContainer();var pos=this._dropDownBehavior.get_position();if(this._dropDownBehavior.get_visible())
{this._accumulatedHeight=this._maxHeight;this._increaseDelta=-1*this._increaseDelta;}
if(this._dropDownBehavior.get_offScreen()||pos==$IG.DropDownPopupPosition.TopLeft||pos==$IG.DropDownPopupPosition.TopRight)
{this._isTop=true;this._marginDelta=this._increaseDelta;this._maxMargin=parseInt(this._container.style.marginTop);this._accumulatedMarginTop=this._maxMargin+this._maxHeight;if(this._dropDownBehavior.get_visible()){this._accumulatedMarginTop=this._maxMargin;}
this._container.style.marginTop=this._accumulatedMarginTop+'px';}},onNext:function(){this._increaseDelta=this._calc(this._dropDownBehavior.get_animationType(),this._time,0,this._maxHeight,this.get_duration());if(this._dropDownBehavior.get_visible()){this._increaseDelta=-1*this._increaseDelta;}
this._accumulatedHeight+=this._increaseDelta;if(this._accumulatedHeight>=this._maxHeight||this._accumulatedHeight<0){if(this._accumulatedHeight<0)
this._accumulatedHeight=0;else if(this._accumulatedHeight>this._maxHeight)
this._accumulatedHeight=this._maxHeight;this.stop();}
if(this._isTop)
{this._accumulatedMarginTop-=this._increaseDelta;this._container.style.marginTop=parseInt(this._accumulatedMarginTop)+'px';}
$util.setAbsoluteHeight(this._container,parseInt(this._accumulatedHeight));if(this._dropDownBehavior._childFrame){$util.setAbsoluteHeight(this._dropDownBehavior._childFrame,parseInt(this._accumulatedHeight));}},onEnd:function(){if(this._isTop){if(this._accumulatedMarginTop!=this._maxMargin)
{this._container.style.marginTop=this._maxMargin+'px';}}
this._accumulatedHeight=0;this._accumulatedMarginTop=0;this._increaseDelta=0;this._marginDelta=0;this._isTop=false;if(this._dropDownBehavior.get_visible()){this._container.style.display='none';this._container.style.visibility='hidden';if(this._dropDownBehavior._childFrame){this._dropDownBehavior._childFrame.style.display="none";}
this._dropDownBehavior._visible=false;}else{this._dropDownBehavior._visible=true;}
this._dropDownBehavior.__notifyAnimationEnd();}}
$IG.DropDownAnimation.registerClass("Infragistics.Web.UI.DropDownAnimation",$IG.AnimationBase);