// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");$IG.AnimationBase=function(elem)
{this._element=elem;this._duration=35;this._tickInterval=21;this._curveDepth=2;if($util.IsFireFox)
this._tickInterval=31;};$IG.AnimationBase.prototype={play:function()
{this.onBegin();this._time=1
if(this._animating==true)
this.stop();this._animating=true;this._init();if(this._animating)
this.__tick(true);},stop:function()
{this._animating=false;clearInterval(this._timerId);this._timerId=undefined;},onBegin:function()
{},onNext:function()
{},onEnd:function()
{},get_duration:function()
{return this._duration;},set_duration:function(ms)
{this._duration=parseInt(ms/this._tickInterval);if(this._duration<1||ms<=0)
this._duration=1;},get_isAnimating:function()
{return this._animating;},__tick:function(firstTime)
{this.onNext();this._next();if(this._animating)
{this._time++;if($util.IsFireFox&&this._animating)
{clearInterval(this._timerId);this._timerId=setInterval(Function.createDelegate(this,this.__tick),this._tickInterval);}
if(firstTime&&!$util.IsFireFox)
this._timerId=setInterval(Function.createDelegate(this,this.__tick),this._tickInterval);}
else
this.onEnd();},_init:function()
{},_next:function()
{},_calc:function(type,t,s,e,d)
{var cd=this._curveDepth;if(type==$IG.AnimationEquationType.Linear)
return((e-s)/d)*t+s;else if(type==$IG.AnimationEquationType.EaseIn)
return(Math.pow(t,cd)*(e-s))/Math.pow(d,cd)+s;else if(type==$IG.AnimationEquationType.EaseOut)
return((-Math.pow(t,cd)*(e-s))/Math.pow(d,cd))+((2*t*(e-s))/d)+s;else if(type==$IG.AnimationEquationType.EaseInOut)
{if(t<(d/2))
return(Math.pow(t,cd)*((e-s)/2))/Math.pow((d/2),cd)+s;else
return((-Math.pow((t-(d/2)),cd)*((e-s)/2))/Math.pow((d/2),cd))+((2*(t-(d/2))*((e-s)/2))/(d/2))+(s+e)/2;}
else if(type==$IG.AnimationEquationType.Bounce)
{var ts=(t/=d)*t;var tc=ts*t;return(e-s)*(44.25*tc*ts+-138.25*ts*ts+156.5*tc+-76.5*ts+15*t)+s;}},dispose:function()
{this.stop();this._element=null;}};$IG.AnimationBase.registerClass("Infragistics.Web.UI.AnimationBase");$IG.OpacityAnimation=function(elem,equationType)
{$IG.OpacityAnimation.initializeBase(this,[elem]);if(equationType!=null&&typeof(equationType)!="undefined")
this._equationType=equationType;else
this._equationType=$IG.AnimationEquationType.Linear;};$IG.OpacityAnimation.prototype={_equationType:$IG.AnimationEquationType,play:function(startOpacity,endOpacity,removeOpacityAtEnd)
{this._startOpacity=startOpacity;this._endOpacity=endOpacity;this.removeOpacityAtEnd=removeOpacityAtEnd;$IG.OpacityAnimation.callBaseMethod(this,"play");},_init:function()
{this._opacity=this._startOpacity;$util.setOpacity(this._element,this._opacity);},_next:function()
{if(this._startOpacity<this._endOpacity)
{this._opacity=$IG.OpacityAnimation.callBaseMethod(this,"_calc",[this._equationType,this._time,this._startOpacity,this._endOpacity,this._duration]);$util.setOpacity(this._element,this._opacity);if(this._opacity>=this._endOpacity)
this.stop();}
else
{this._opacity=$IG.OpacityAnimation.callBaseMethod(this,"_calc",[this._equationType,this._time,this._startOpacity,this._endOpacity,this._duration]);$util.setOpacity(this._element,this._opacity);if(this._opacity<=this._endOpacity)
this.stop();}},stop:function()
{if(this.removeOpacityAtEnd)
{if(this._element!=null&&typeof(this._element)!=undefined)
{if(this._element.style.removeAttribute)
this._element.style.removeAttribute("filter")
else
this._element.style.removeProperty("opacity")}}
$IG.OpacityAnimation.callBaseMethod(this,"stop");}};$IG.OpacityAnimation.registerClass("Infragistics.Web.UI.OpacityAnimation",$IG.AnimationBase);$IG.AnimationEquationType=function()
{}
$IG.AnimationEquationType.prototype={Linear:0,EaseIn:1,EaseOut:2,EaseInOut:3,Bounce:4};$IG.AnimationEquationType.registerEnum("Infragistics.Web.UI.AnimationEquationType");