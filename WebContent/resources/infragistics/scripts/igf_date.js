// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

if(ig){if(!ig.date){function Tokenizer(str){this.value="";this.pos=0;this.string=str;this.len=str.length;this.fmts=["yyyy","yy","y","MMMM","MMM","NNN","MM","M","dd","d","EEEE","EE","E","hh","h","HH","H","KK","K","kk","k","mm","m","ss","s","a"];this.parse_string=function(strs,cs){for(var i=0;i<strs.length;++i){var tkn_len=strs[i].length;var tkn_str=cs?strs[i]:strs[i].toLowerCase();if(this.len>=this.pos+tkn_len){var value=cs?this.string.substr(this.pos,tkn_len):this.string.substr(this.pos,tkn_len).toLowerCase();if(value==tkn_str){this.value=this.string.substr(this.pos,tkn_len);this.pos=this.pos+tkn_len;return i;}}}
return-1;};this.parse_char=function(){if(this.len>=this.pos+1){this.value=this.string.substr(this.pos,1);this.pos=this.pos+1;return 0;}
return-1;};this.parse_space=function(){while((this.string.charAt(this.pos)==" ")||(this.string.charAt(this.pos)=="\t")){++this.pos;}
return 0;};this.parse_number=function(n,min,max){if(this.len>=this.pos+n){for(var i=0;i<n;++i){if(parseInt(this.string.charAt(this.pos+i),10)!=this.string.charAt(this.pos+i)){return-1;}}
this.value=this.string.substr(this.pos,n);var value=parseInt(this.value,10);if((value>=min)&&(value<=max)){this.pos+=n;return value;}}
return-1;};this.parse_format=function(){if(this.parse_string(this.fmts,true)==-1){return this.parse_char();}
return 0;};}
Date.prototype.igSetLocale=function(mnths,months,dys,days,ampms){this.mnths=mnths===null?this.en_mnths:mnths.split(",");this.months=months===null?this.en_months:months.split(",");this.dys=dys===null?this.en_dys:dys.split(",");this.days=days===null?this.en_days:days.split(",");this.ampms=ampms===null?this.en_ampms:ampms.split(",");};Date.prototype.itoa=function(i,d){var str=""+i;var len=str.length;for(var j=0;j<(d-len);++j){str="0"+str;}
if(len>d){str=str.substr(len-d);}
return str;};Date.prototype.igFormat=function(fmt){var year=this.getFullYear();var month=this.getMonth();var date=this.getDate();var day=this.getDay();var hour=this.getHours();var minute=this.getMinutes();var second=this.getSeconds();var tkn=new Tokenizer(fmt);var str="";while(tkn.parse_format()!=-1)
{switch(tkn.value){case"yyyy":str+=this.itoa(year,4);break;case"yy":str+=this.itoa(year,2);break;case"y":str+=this.itoa(year,4);break;case"MMMM":str+=this.months[month];break;case"MMM":str+=this.mnths[month];break;case"NNN":str+=this.mnths[month];break;case"MM":str+=this.itoa(1+month,2);break;case"M":str+=(1+month);break;case"dd":str+=this.itoa(date,2);break;case"d":str+=date;break;case"EEEE":case"EE":str+=this.days[day];break;case"E":str+=this.dys[day];break;case"hh":str+=this.itoa((hour%12)===0?12:(hour%12),2);break;case"h":str+=((hour%12)===0?12:(hour%12));break;case"HH":str+=this.itoa(hour,2);break;case"H":str+=hour;break;case"KK":str+=this.itoa(hour%12,2);break;case"K":str+=(hour%12);break;case"kk":str+=this.itoa(hour===0?24:hour,2);break;case"k":str+=(hour===0?24:hour);break;case"mm":str+=this.itoa(minute,2);break;case"m":str+=minute;break;case"ss":str+=this.itoa(second,2);break;case"s":str+=second;break;case"a":str+=hour<12?this.ampms[0]:this.ampms[1];break;case"W":str+=this.getWeekInMonth(date);break;case"w":str+=this.getWeekInYear(this);break;default:str=str+tkn.value;}}
return str;};Date.prototype.getWeekInMonth=function(date){return Math.ceil(date/7);};Date.prototype.getWeekInYear=function(timeInstance){var januaryFirst=new Date(timeInstance.getFullYear(),0,1);return Math.ceil((((timeInstance-januaryFirst)/86400000)+januaryFirst.getDay())/7);};Date.prototype.igParse=function(fmt,str){var fmt_tkn=new Tokenizer(fmt);var str_tkn=new Tokenizer(str);var year=-1;var month=-1;var date=-1;var day=-1;var hours=-1;var minutes=-1;var seconds=-1;var ampm=0;while(fmt_tkn.parse_format()!=-1){switch(fmt_tkn.value){case"yyyy":year=str_tkn.parse_number(4,0,9999);if(year<0){return null;}
break;case"yy":year=str_tkn.parse_number(2,0,99);if(year<0){return null;}
if(year>50)
year+=1900;else
year+=2000;break;case"y":year=str_tkn.parse_number(4,0,9999);if(year<0){year=str_tkn.parse_number(2,0,99);if(year>=0){year+=2000;}}
if(year<0){return null;}
break;case"MMM":case"MMMM":case"NNN":month=str_tkn.parse_string(this.mnths,false);if(month<0){month=str_tkn.parse_string(this.months,false);}
if(month<0){return null;}
break;case"MM":month=str_tkn.parse_number(2,1,12);if(month<0){return null;}
--month;break;case"M":month=str_tkn.parse_number(2,1,12);if(month<0){month=str_tkn.parse_number(1,1,12);}
if(month<0){return null;}
--month;break;case"dd":date=str_tkn.parse_number(2,1,31);if(date<0){return null;}
break;case"d":date=str_tkn.parse_number(2,1,31);if(date<0){date=str_tkn.parse_number(1,1,31);}
if(date<0){return null;}
break;case"EEEE":case"EE":day=str_tkn.parse_string(this.days,false);if(day<0){return null;}
break;case"E":day=str_tkn.parse_string(this.dys,false);if(day<0){return null;}
break;case"hh":hours=str_tkn.parse_number(2,1,12);if(hours<0){return null;}
if(hours==12){hours=0;}
break;case"h":hours=str_tkn.parse_number(2,1,12);if(hours<0){hours=str_tkn.parse_number(1,1,12);}
if(hours<0){return null;}
if(hours==12){hours=0;}
break;case"HH":hours=str_tkn.parse_number(2,0,23);if(hours<0){return null;}
break;case"H":hours=str_tkn.parse_number(2,0,23);if(hours<0){hours=str_tkn.parse_number(1,0,23);}
if(hours<0){return null;}
break;case"KK":hours=str_tkn.parse_number(2,0,11);if(hours<0){return null;}
break;case"K":hours=str_tkn.parse_number(2,0,11);if(hours<0){hours=str_tkn.parse_number(1,0,11);}
if(hours<0){return null;}
break;case"kk":hours=str_tkn.parse_number(2,1,24);if(hours<0){return null;}
if(hours==24){hours=0;}
break;case"k":hours=str_tkn.parse_number(2,1,24);if(hours<0){hours=str_tkn.parse_number(1,1,24);}
if(hours<0){return null;}
if(hours==24){hours=0;}
break;case"mm":minutes=str_tkn.parse_number(2,0,59);if(minutes<0){return null;}
break;case"m":minutes=str_tkn.parse_number(2,0,59);if(minutes<0){minutes=str_tkn.parse_number(1,0,59);}
if(minutes<0){return null;}
break;case"ss":seconds=str_tkn.parse_number(2,0,59);if(seconds<0){return null;}
break;case"s":seconds=str_tkn.parse_number(2,0,59);if(seconds<0){seconds=str_tkn.parse_number(1,0,59);}
if(seconds<0){return null;}
break;case"a":ampm=str_tkn.parse_string(this.ampms,false);if(ampm<0){return null;}
break;case" ":str_tkn.parse_space();break;case"w":break;case"W":break;default:if(str_tkn.parse_char()<0){return null;}
if(fmt_tkn.value!=str_tkn.value){return null;}
break;}}
if(year!=-1){this.setFullYear(year);}
if(month!=-1){if(date==-1)
previousDate=this.getDate();this.setDate(1)
this.setMonth(month);if(date!=-1){this.setDate(date);}else{this.setDate(previousDate);}}
else
if(date!=-1){this.setDate(date);}
if(hours!=-1){this.setHours(ampm===0?hours:hours+12);}
if(minutes!=-1){this.setMinutes(minutes);}
if(seconds!=-1){this.setSeconds(seconds);}
if((day!=-1)&&(this.getDay()!=day)){return null;}
return this;};Date.prototype.en_mnths=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];Date.prototype.en_months=["January","February","March","April","May","June","July","August","September","October","November","December"];Date.prototype.en_dys=["Mon","Tue","Wed","Thu","Fri","Sat","Sun"];Date.prototype.en_days=["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];Date.prototype.en_ampms=["am","pm"];Date.prototype.mnths=this.en_mnths;Date.prototype.months=this.en_months;Date.prototype.dys=this.en_dys;Date.prototype.days=this.en_days;Date.prototype.ampms=this.en_ampms;ig.date={};}}