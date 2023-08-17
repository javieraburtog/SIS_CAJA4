// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

if (ig) {
	if (!ig.input) {
		function IgWebCalendarPackage() {
			this.TYPE_DATE_CHOOSER = "DateChooser";
			this.TYPE_CALENDAR = "Calendar";
			this.TYPE_CALENDAR_DAY = "CalendarDay";
			this.TYPE_CALENDAR_POPUP = "CalendarPopup";
			this.TYPE_INPUT = "Input";
			this.TYPE_INPUT_LIST = "InputList";	
			this.ID_POPUP_CALENDAR = "igCalendarPopup";
			this.CSS_DAY_HEADER = "ocssdh";
			this.CSS_HEADER = "ocssh";
			this.CSS_DEFAULT_CALENDAR = "odcCalendarPopup";
			this.CSS_DEFAULT_DATE = "odcCalendarDay";
			this.CSS_HOVER_DATE = "ohcCalendarDay";
			this.CSS_DISABLED_DATE = "odscCalendarDay";
			this.CSS_SELECTED_DATE = "oscCalendarDay";
			this.CSS_CALENDAR_DAY_NOT_CURRENT_MONTH = "odcncm";
			this.DAY_LIST = "odl";
			this.DISPLAYED_DATE = "od";
			this.EDIT_MASKS = "odem";
			this.EDIT_MASKS_SEPARATOR = "odems";
			this.FIRST_DAY_OF_WEEK = "ofdow";
			this.LONG_DAY_LIST = "oldl";
			this.MAX_DATE = "omxd";
			this.MIN_DATE = "omnd";
			this.MONTH_LIST = "oml";
			this.PARENT_DATE_CHOOSER = "oInputCaller";
			this.SELECTED_DATE = "osd";
			this.SHORT_DAY_LIST = "osdl";
			this.SHORT_MONTH_LIST = "osml";
			this.SHOW_DAY_HEADER = "osdh";
			this.SHOW_HEADER = "osh";
			this.createPopupCalendar = function() {
				var ppup = document.createElement("div");
				ppup.id = ig.input.ID_POPUP_CALENDAR;
				ppup.style.position = "absolute";
				ppup.style.visibility = "hidden";
				ppup.setAttribute("otype", ig.input.TYPE_CALENDAR_POPUP);
				ppup.setAttribute("ocontrol", "true");
				var cc = "<div id=\"" + ig.input.ID_POPUP_CALENDAR
						+ "_header\">";
				cc += "<table id=\"" + ig.input.ID_POPUP_CALENDAR
						+ "_tableheader\"><tr>";
				cc += "<td><button type=\"button\" class=\"owidcbcy\" "
						+ ig.PROP_FLAG + "=\"bpy\">&lt;</button></td>";
				cc += "<td><select id=\""
						+ ig.input.ID_POPUP_CALENDAR
						+ "_month\" onchange=\"ig.input.onMonthYearChange(event)\"></select></td>";
				cc += "<td>&nbsp;</td>";
				cc += "<td><input id=\"" + ig.input.ID_POPUP_CALENDAR
						+ "_year\" type=\"text\" maxlenght=\"4\" size=\"4\"";
				cc += " "
						+ ig.PROP_FLAG
						+ "=\"tbcy\" onchange=\"ig.input.onMonthYearChange(event)\"/></td>";
				cc += "<td><button type=\"button\" class=\"owidcbcy\" "
						+ ig.PROP_FLAG + "=\"bny\">&gt;</button></td>";				
				cc += "</tr></table></div>";
				cc += "<div id=\"" + ig.input.ID_POPUP_CALENDAR
						+ "_body\" align='center'></div>";
				ppup.innerHTML = cc;
				document.body.appendChild(ppup);
				return ig.getUIElementById(ppup);
			};
			this.getPopupCalendar = function() {
				var ppup = ig.getUIElementById(ig.input.ID_POPUP_CALENDAR);
				if (ig.isNull(ppup)) {
					ppup = this.createPopupCalendar();
				}
				return ppup;
			};
			this.init = function() {
				ig.factory.addClass(ig.input.TYPE_DATE_CHOOSER, IgDateChooser);
				ig.factory.addClass(ig.input.TYPE_CALENDAR, IgCalendar);
				ig.factory.addClass(ig.input.TYPE_CALENDAR_DAY, IgCalendarDay);
				ig.factory.addClass(ig.input.TYPE_CALENDAR_POPUP,
						IgCalendarPopup);
				ig.factory.addClass(ig.input.TYPE_INPUT, IgInput);
				ig.factory.addClass(ig.input.TYPE_INPUT_LIST, IgInputList);
			};
			this.addClickListenerForCalendar = function() {
				ig.addEventListener(document, ig.event.EVENT_CLICK,
						ig.input.onClickOutside, false);
			};
			this.onClickOutside = function(evt) {
				var xEvt = ig.toEvent(evt);
				if (xEvt === null) {
					return;
				}
				var oe = ig.getTargetUIElement(xEvt.target,
						ig.input.TYPE_CALENDAR_DAY);
				if (oe === null) {
					oe = ig.getTargetUIElement(xEvt.target,
							ig.input.TYPE_CALENDAR_POPUP);
					if (oe === null) {
						var flagValue = ig.getAttribute(xEvt.target,
								ig.PROP_FLAG);
						if (flagValue != "owidcb") {
							var cal = ig
									.getUIElementById(ig.input.ID_POPUP_CALENDAR);
							if (!ig.isNull(cal)) {
								if (cal.isVisible()) {
									cal.hide(false);
								}
							}
						}
					}
				}
			};
			this.onMonthYearChange = function(evt) {
				var xEvt = ig.toEvent(evt);
				if (!ig.isNull(xEvt)) {
					var cal = ig.getTargetUIElement(xEvt.target,
							ig.input.TYPE_CALENDAR_POPUP);
					if (!ig.isNull(cal)) {
						var oMonth = cal.getMonthCombo();
						var oYear = cal.getYearCombo();
						if ((!ig.isNull(oMonth)) && (!ig.isNull(oYear))) {
							cal.showDate(oMonth.selectedIndex, oYear.value);
						}
					}
				}
			};
			this.getNextMonthOfDate = function(pDate) {
				var nextMonthDate = new Date(pDate.getYear(),
						pDate.getMonth() + 1, "01");
				return nextMonthDate.getMonth();
			};
			function IgCalendar(e) {
				this.IgUIComponent(e);
			}
			;
			this.initDateChooser = function(nodeId, clientSideListeners) {
				var igElm = ig.getUIElementById(nodeId);
				if (igElm.init) {
					igElm.init(clientSideListeners);
				} else {
					alert("IgDateChooser without 'init' method found");
				}
			};
			this.initWebInput = function(nodeId, clientSideListeners) {
				var igElm = ig.getUIElementById(nodeId);
				if (igElm.init) {
					igElm.init(clientSideListeners);
				} else {
					alert("IgInput without 'init' method found");
				}
			};
			this.initWebInputList = function(nodeId, clientSideListeners) {
				var igElm = ig.getUIElementById(nodeId);
				if (igElm.init) {
					igElm.init(clientSideListeners);
				} else {
					alert("IgInputList without 'init' method found");
				}
			};
			this.raiseClientEvent = function(nodeId, eventType, browserEvt,
					smartSubmitURL) {
				var jsNode = ig.getUIElementById(nodeId, ig.input.TYPE_INPUT);
				var htmlElm = jsNode.elm;
				var theValue = null;
				if (htmlElm.nodeName == "SPAN") {
					var kid = htmlElm.firstChild;
					if (kid.nodeName == "INPUT") {
						theValue = kid.value;
					}
				} else if (htmlElm.nodeName == "SELECT") {
					theValue = htmlElm.value;
				}
				jsNode._raiseClientEvent(eventType, "Input", browserEvt, null,
						null, theValue);
				if (smartSubmitURL) {
					ig.executeScript(smartSubmitURL);
				}
			};
			this.restoreState = function(id, state) {
				var jsNode = ig.getUIElementById(id);
				jsNode.setSelectionState(state);
			}
			this.raiseListClientEvent = function(nodeId, eventType, browserEvt,
					smartSubmitURL) {
				var jsNodeParentHolder = ig.getUIElementById(nodeId,
						ig.input.TYPE_INPUT_LIST);
				var state = null;
				if (jsNodeParentHolder.getSelectionState) {
					state = jsNodeParentHolder.getSelectionState(null);
				}
				var igEvent = new IgEvent(browserEvt);
				if (eventType == "onFocus") {
					jsNodeParentHolder._raiseClientEvent(eventType,
							"InputList", browserEvt, null, null,
							igEvent.target.value);
				} else if (eventType == "onBlur") {
					jsNodeParentHolder._raiseClientEvent(eventType,
							"InputList", browserEvt, null, null,
							igEvent.target.value);
				} else if (eventType == "onCheckBoxClick") {
					var currentState = null;
					if (jsNodeParentHolder.getSelectionState) {
						currentState = jsNodeParentHolder
								.getSelectionState(igEvent.target.value);
					}
					var args = jsNodeParentHolder._raiseClientEvent(
							"selectionChanging", "InputList", browserEvt,
							currentState, state, igEvent.target.value);
					if (args != null && args.get_cancel())
						return false;
					jsNodeParentHolder._raiseClientEvent("selectionChanged",
							"InputList", browserEvt, state, null,
							igEvent.target.value);
					if (smartSubmitURL) {
						ig.executeScript(smartSubmitURL);
					}
				} else if (eventType == "onRadioButtonClick") {
					var oldState = jsNodeParentHolder.getSavedState();
					if (ig.isNull(oldState)) {
						oldState = jsNodeParentHolder.getSelectionState(null);
					}
					var selectedIndex = jsNodeParentHolder
							.getIndexOfNodeValue(igEvent.target.value);
					var newState = [];
					for ( var i = 0; i < oldState.length; i++) {
						newState
								.push(selectedIndex != -1 && selectedIndex == i);
					}
					var args = jsNodeParentHolder._raiseClientEvent(
							"selectedItemChanging", "InputList", browserEvt,
							oldState, newState, igEvent.target.value);
					if (args != null && args.get_cancel()) {
						exeStr = "ig.input.restoreState(\"" + nodeId + "\",["
								+ oldState + "]);";
						setTimeout(exeStr, 50);
						return false;
					}
					jsNodeParentHolder.setSavedState(newState);
					jsNodeParentHolder._raiseClientEvent("selectedItemChanged",
							"InputList", browserEvt, newState, null,
							igEvent.target.value);
					if (smartSubmitURL) {
						ig.executeScript(smartSubmitURL);
					}
				}
				return true;
			};
			this.raiseCalendarClientEvent = function(nodeId, eventType,
					browserEvt) {
				var jsNode = ig.getUIElementById(nodeId,
						ig.input.TYPE_DATE_CHOOSER);
				var htmlElm = jsNode.elm;
				var theValue = null;
				if (htmlElm.nodeName == "DIV") {
					var kid = htmlElm.firstChild;
					if (kid.nodeName == "INPUT") {
						theValue = kid.value;
					}
				}
				jsNode._raiseClientEvent(eventType, "Input", browserEvt, null,
						null, theValue);
			};
		}
		;
		ig.input = new IgWebCalendarPackage();
		function IgCalendar(e) {
			this.IgUIComponent(e);
		}
		;
		IgCalendar.prototype.getMonthCombo = function() {
			return ig.getElementById(this.elm.id + "_month");
		};
		IgCalendar.prototype.getYearCombo = function() {
			return ig.getElementById(this.elm.id + "_year");
		};
		IgCalendar.prototype.getHeader = function() {
			return ig.getUIElementById(this.elm.id + "_header");
		};
		IgCalendar.prototype.getBody = function() {
			return ig.getElementById(this.elm.id + "_body");
		};
		IgCalendar.prototype.getDayHeaderClass = function() {
			return this.getAttribute(ig.input.CSS_DAY_HEADER);
		};
		IgCalendar.prototype.getHeaderClass = function() {
			return this.getAttribute(ig.input.CSS_HEADER);
		};
		IgCalendar.prototype.getSelectedDate = function() {
			var result = null;
			var sd = this.elm.getAttribute(ig.input.SELECTED_DATE);
			if (ig.NaES(sd)) {
				result = new Date();
				result.igParse("yyyyMMdd", sd);
			}
			return result;
		};
		IgCalendar.prototype.getShowDayHeader = function(b) {
			return this.getAttribute(ig.input.SHOW_DAY_HEADER) != "false";
		};
		IgCalendar.prototype.getShowHeader = function(b) {
			return this.getAttribute(ig.input.SHOW_HEADER) != "false";
		};
		IgCalendar.prototype.getDayList = function() {
			return this.elm.getAttribute(ig.input.DAY_LIST);
		};
		IgCalendar.prototype.getDisplayedDate = function() {
			var odate_str = this.elm.getAttribute(ig.input.DISPLAYED_DATE);
			var odate;
			if (ig.NaES(odate_str)) {
				odate = new Date();
				odate.setDate("01");
				odate.igParse("yyyyMMdd", odate_str);
				odate.setDate("01");
				odate.setHours(0);
				odate.setSeconds(0);
				odate.setMinutes(0);
				odate.setMilliseconds(0);
			} else {
				odate = this.getSelectedDate();
				if (odate !== null) {
					odate = new Date(odate.getFullYear(), odate.getMonth(),
							'01');
				} else {
					odate = new Date();
					odate.setDate('01');
				}
				this.setDisplayedDate(odate);
			}
			return odate;
		};
		IgCalendar.prototype.getMonthList = function() {
			return this.elm.getAttribute(ig.input.MONTH_LIST);
		};
		IgCalendar.prototype.getFirstDayOfWeek = function() {
			return (this.elm.getAttribute(ig.input.FIRST_DAY_OF_WEEK));
		};
		IgCalendar.prototype.getMinDate = function() {
			var dateMin = this.elm.getAttribute(ig.input.MIN_DATE);
			if (dateMin == "null") {
				dateMin = null;
			}
			if (ig.NaES(dateMin)) {
				var minDate = new Date();
				minDate.igParse("yyyyMMdd", dateMin);
				minDate.setHours(0);
				minDate.setSeconds(0);
				minDate.setMinutes(0);
				minDate.setMilliseconds(0);
				return minDate;
			} else {
				return null;
			}
		};
		IgCalendar.prototype.getMaxDate = function() {
			var dateMax = this.elm.getAttribute(ig.input.MAX_DATE);
			if (dateMax == "null") {
				dateMax = null;
			}
			if (ig.NaES(dateMax)) {
				var maxDate = new Date();
				maxDate.igParse("yyyyMMdd", dateMax);
				maxDate.setHours(0);
				maxDate.setSeconds(0);
				maxDate.setMinutes(0);
				maxDate.setMilliseconds(0);
				return maxDate;
			} else {
				return null;
			}
		};
		IgCalendar.prototype.setMinDate = function(d) {
			this.elm.setAttribute(ig.input.MIN_DATE, d + "");
		};
		IgCalendar.prototype.setMaxDate = function(d) {
			this.elm.setAttribute(ig.input.MAX_DATE, d + "");
		};
		IgCalendar.prototype.setFirstDayOfWeek = function(d) {
			this.elm.setAttribute(ig.input.FIRST_DAY_OF_WEEK, d + "");
		};
		IgCalendar.prototype.setDayList = function(d) {
			this.elm.setAttribute(ig.input.DAY_LIST, d + "");
		};
		IgCalendar.prototype.setDisplayedDate = function(d) {
			var tmp = "";
			if (d !== null) {
				tmp = d.igFormat("yyyyMMdd");
			}
			this.elm.setAttribute(ig.input.DISPLAYED_DATE, tmp + "");
		};
		IgCalendar.prototype.setLongDayList = function(d) {
			this.elm.setAttribute(ig.input.LONG_DAY_LIST, d + "");
		};
		IgCalendar.prototype.setMonthList = function(d) {
			this.elm.setAttribute(ig.input.MONTH_LIST, d + "");
		};
		IgCalendar.prototype.setShortDayList = function(d) {
			this.elm.setAttribute(ig.input.SHORT_DAY_LIST, d + "");
		};
		IgCalendar.prototype.setShortMonthList = function(d) {
			this.elm.setAttribute(ig.input.SHORT_MONTH_LIST, d + "");
		};
		IgCalendar.prototype.showNextMonth = function() {
			var dd = this.getDisplayedDate();
			this.showDate(dd.getMonth() + 1, dd.getFullYear());
		};
		IgCalendar.prototype.showNextYear = function() {
			var dd = this.getDisplayedDate();
			this.showDate(dd.getMonth(), dd.getFullYear() + 1);
		};
		IgCalendar.prototype.showPreviousMonth = function() {
			var dd = this.getDisplayedDate();
			this.showDate(dd.getMonth() - 1, dd.getFullYear());
		};
		IgCalendar.prototype.showPreviousYear = function() {
			var dd = this.getDisplayedDate();
			this.showDate(dd.getMonth(), dd.getFullYear() - 1);
		};
		IgCalendar.prototype.setSelectedDate = function(d) {
			this.elm.setAttribute(ig.input.DISPLAYED_DATE, "");
			if (!ig.isNull(d)) {
				var tmp = d.igFormat("yyyyMMdd");
				this.elm.setAttribute(ig.input.SELECTED_DATE, tmp + "");
			} else {
				this.elm.setAttribute(ig.input.SELECTED_DATE, "");
			}
		};
		IgCalendar.prototype.setShowDayHeader = function(b) {
			this.elm.setAttribute(ig.input.SHOW_DAY_HEADER, b + "");
		};
		IgCalendar.prototype.setShowHeader = function(b) {
			this.elm.setAttribute(ig.input.SHOW_HEADER, b + "");
		};
		IgCalendar.prototype.synchroniseMonthYear = function(month, year) {
			if (this.getShowHeader()) {
				var oMonth = this.getMonthCombo();
				if (!ig.isNull(oMonth)) {
					if (oMonth.selectedIndex != parseInt(month)) {
						oMonth.selectedIndex = parseInt(month);
					}
				}
				var oYear = this.getYearCombo();
				if (!ig.isNull(oYear)) {
					if (oYear.value != year) {
						oYear.value = year;
					}
				}
			} else {
				var header = this.getHeader();
				if (!ig.isNull(header)) {
					header.hide(true);
				}
			}
		};
		ig.augment(IgCalendar, IgUIComponent);
		function IgDateChooser(e) {
			this.IgUIComponent(e);
		}
		;
		IgDateChooser.prototype.init = function(clientSideListeners) {
			this._initClientEventsForObject(this.elm, clientSideListeners);
			this._raiseClientEvent("Initialize", null, null, null);
		};
		IgDateChooser.prototype.getDayList = function() {
			return this.elm.getAttribute(ig.input.DAY_LIST);
		};
		IgDateChooser.prototype.getEditMasks = function() {
			return this.elm.getAttribute(ig.input.EDIT_MASKS);
		};
		IgDateChooser.prototype.getDate = function() {
			var result = null;
			var it = ig.findDescendant(this.elm, null, null, "input");
			if (!ig.isNull(it)) {
				var masks = this.getEditMasks();
				if (!ig.isNull(masks)) {
					var array = masks.split(this.elm
							.getAttribute(ig.input.EDIT_MASKS_SEPARATOR));
					if (!ig.isNull(array) && array.length > 0) {
						for ( var i = 0; i < array.length; i++) {
							result = new Date();
							result.igSetLocale(this.getShortMonthList(), this
									.getMonthList(), this.getShortDayList(),
									this.getLongDayList(), null);
							result = result.igParse(array[i], it.value);
							if (!ig.isNull(result)) {
								return result;
							}
						}
					}
				}
			}
			return result;
		};
		IgDateChooser.prototype.getDisplayMask = function() {
			var masks = this.getEditMasks();
			if (!ig.isNull(masks)) {
				var array = masks.split(this.elm
						.getAttribute(ig.input.EDIT_MASKS_SEPARATOR));
				if (!ig.isNull(array) && array.length > 0) {
					return array[0];
				}
			}
			return masks;
		};
		IgDateChooser.prototype.getId = function() {
			var id = null;
			var it = ig.findDescendant(this.elm, null, null, "input");
			if (!ig.isNull(it)) {
				id = it.id;
			}
			return id;
		};
		IgDateChooser.prototype.getLongDayList = function() {
			return this.elm.getAttribute(ig.input.LONG_DAY_LIST);
		};
		IgDateChooser.prototype.getMonthList = function() {
			return this.elm.getAttribute(ig.input.MONTH_LIST);
		};
		IgDateChooser.prototype.getShortDayList = function() {
			return this.elm.getAttribute(ig.input.SHORT_DAY_LIST);
		};
		IgDateChooser.prototype.getShortMonthList = function() {
			return this.elm.getAttribute(ig.input.SHORT_MONTH_LIST);
		};
		IgDateChooser.prototype.getShortName = function(longNamesString) {
			if (!ig.isNull(longNamesString)) {
				var longNamesArray = longNamesString.split(",");
				if (!ig.isNull(longNamesArray) && longNamesArray.length > 0) {
					var result = "";
					for ( var i = 0; i < longNamesArray.length; i++) {
						if (i > 0) {
							result += ",";
						}
						result += longNamesArray[i].substring(0, 3);
					}
				}
				return result;
			}
			return null;
		};
		IgDateChooser.prototype.onKeyDown = function(evt) {
			switch (evt.keyCode) {
			case evt.KEY_UP:
				this.showNextDay();
				break;
			case evt.KEY_DOWN:
				this.showPreviousDay();
				break;
			}
		};
		IgDateChooser.prototype.onClick = function(evt) {
			if (this.isEnabled()) {
				var flagValue = ig.getAttribute(evt.target, ig.PROP_FLAG);
				if (flagValue == "owidcb") {
					if (!evt.target.readOnly) {
						var cal = ig.input.getPopupCalendar();
						var dateChooser = cal.getDateChooser();
						if ((!cal.isVisible()) || (dateChooser != this.elm)) {
							var args = this._raiseClientEvent("popupOpening",
									"Input", null, null, null, null);
							if (args != null && args.get_cancel())
								return;
							cal.show(this);
							if (ig.isNetscape) {
								igDoc = ig
										.getUIElementById(ig.input.ID_POPUP_CALENDAR
												+ "_tableheader");
								igPpup = ig
										.getUIElementById(ig.input.ID_POPUP_CALENDAR);
								igPpup.setSize(igDoc.getWidth(), -1);
							}
							this._raiseClientEvent("popupOpened", "Input",
									null, null, null, null);
						} else {
							var oCaller = ig.getUIElementById(dateChooser);
							oCaller.setDate(cal.getSelectedDate());
							cal.hide(false);
						}
					}
				}
			}
		};
		IgDateChooser.prototype.setDate = function(d) {
			if (!ig.isNull(d)) {
				var previousDate = this.getDate();
				if (!ig.isNull(previousDate)) {
					d.setHours(previousDate.getHours());
					d.setMinutes(previousDate.getMinutes());
					d.setSeconds(previousDate.getSeconds());
					d.setMilliseconds(previousDate.getMilliseconds());
				}
				d.igSetLocale(this.getShortMonthList(), this
						.getMonthList(), this.getShortDayList(), this
						.getLongDayList(), null);
				var displayedDate = d.igFormat(this.getDisplayMask());
				if (displayedDate !== null) {
					var it = ig.findDescendant(this.elm, null, null, "input");
					if (!ig.isNull(it)) {
						it.value = displayedDate;
					}
				}
			}
		};
		IgDateChooser.prototype.showNextDay = function() {
			var d = this.getDate();
			if (!ig.isNull(d)) {
				d.setDate(d.getDate() + 1);
				this.setDate(d);
			}
		};
		IgDateChooser.prototype.showPreviousDay = function() {
			var d = this.getDate();
			if (!ig.isNull(d)) {
				d.setDate(d.getDate() - 1);
				this.setDate(d);
			}
		};
		IgDateChooser.prototype.setButtonText = function(bText) {
			if ((ig.isNull(bText)) || (bText == "")) {
				bText = "...";
			}
			var firstChildObj = this.elm.firstChild;
			if (!ig.isNull(firstChildObj)) {
				buttonObj = firstChildObj.nextSibling;
				if (!ig.isNull(buttonObj))
					buttonObj.value = bText;
			}
		};
		ig.augment(IgDateChooser, IgUIComponent);
		function IgCalendarDay(e) {
			this.IgUIElement(e);
		}
		;
		IgCalendarDay.prototype.getClass = function() {
			var css = this.getAttribute("ocbh");
			if (ig.NaES(css)) {
				this.setAttribute("ocbh", "");
			} else {
				if (!this.isEnabled()) {
					css = this.getDisabledClass();
				} else {
					if (this.isSelected()) {
						css = this.getSelectedClass();
					}
					if (this.isHovered()) {
						if (!ig.isNull(css)) {
							css = css + " " + this.getHoverClass();
						} else {
							css = this.getHoverClass();
						}
						this.setAttribute("ocbh", this.elm.className);
					}
					if (!css) {
						css = this.getDefaultClass();
					}
				}
				if (!css) {
					css = "";
				}
			}
			return css;
		};
		IgCalendarDay.prototype.getNextSibling = function(oe) {
			var tmp = this.callSuper("IgUIElement", "getNextSibling", oe);
			if (ig.isNull(tmp)) {
				var cellIndex = this.elm.cellIndex;
				var tr = this.elm.parentNode;
				var rowIndex = tr.sectionRowIndex;
				if (rowIndex < 5) {
					var nextTr = tr.nextSibling;
					if (!ig.isNull(nextTr)) {
						var firstTd = nextTr.firstChild;
						tmp = ig.getUIElementById(firstTd);
					}
				}
			}
			return tmp;
		};
		IgCalendarDay.prototype.getPreviousSibling = function(oe) {
			var tmp = this.callSuper("IgUIElement", "getPreviousSibling", oe);
			if (ig.isNull(tmp)) {
				var cellIndex = this.elm.cellIndex;
				var tr = this.elm.parentNode;
				var rowIndex = tr.sectionRowIndex;
				if (rowIndex > 0) {
					var prevTr = tr.previousSibling;
					var lastTd = prevTr.lastChild;
					tmp = new IgCalendarDay(lastTd);
				}
			}
			return tmp;
		};
		IgCalendarDay.prototype.getUpSibling = function(oe) {
			var cellIndex = this.elm.cellIndex;
			var tr = this.elm.parentNode;
			var rowIndex = tr.sectionRowIndex;
			if (rowIndex > 0) {
				var prevTr = tr.previousSibling;
				var Td = prevTr.childNodes[cellIndex];
				return new IgCalendarDay(Td);
			} else {
			}
		};
		IgCalendarDay.prototype.getDownSibling = function(oe) {
			var result = null;
			var cellIndex = this.elm.cellIndex;
			var tr = this.elm.parentNode;
			var rowIndex = tr.sectionRowIndex;
			if (rowIndex < 5) {
				var nextTr = tr.nextSibling;
				if (!ig.isNull(nextTr)) {
					var td = nextTr.childNodes[cellIndex];
					result = ig.getUIElementById(td);
				}
			}
			return result;
		};
		IgCalendarDay.prototype.onKeyDown = function(evt) {
			switch (evt.keyCode) {
			case evt.KEY_LEFT:
				var ps = this.getPreviousSibling();
				if (!ig.isNull(ps) && ps.focus) {
					ps.focus();
				}
				break;
			case evt.KEY_RIGHT:
				var ns = this.getNextSibling();
				if (!ig.isNull(ns) && ns.focus) {
					ns.focus();
				}
				evt.preventDefault();
				break;
			case evt.KEY_UP:
				var us = this.getUpSibling();
				if (!ig.isNull(us) && us.focus) {
					us.focus();
				}
				evt.preventDefault();
				break;
			case evt.KEY_DOWN:
				var ds = this.getDownSibling();
				if (!ig.isNull(ds) && ds.focus) {
					ds.focus();
				}
				evt.preventDefault();
				break;
			case evt.KEY_ESCAPE:
				var cal = this.getParentComponent();
				if (!ig.isNull(cal)) {
					cal.hide(false);
				}
				break;
			case evt.KEY_RETURN:
				this.onDoubleClick();
				break;
			}
		};
		IgCalendarDay.prototype.onClick = function() {
			this.select();
			var cal = this.getParentComponent();
			if (!ig.isNull(cal)) {
				cal.setSelectedDate(this.getDate());
				var oInput = cal.getDateChooser();
				oInput = ig.getUIElementById(oInput);
				oInput.setDate(cal.getSelectedDate());
				document.getElementById(oInput.getId()).focus();
				if (cal.isVisible()) {
					cal.hide(true);
				}
			}
		};
		IgCalendarDay.prototype.onDoubleClick = function() {
		};
		IgCalendarDay.prototype.getDate = function() {
			var tmp = this.getAttribute("oDate");
			if (ig.NaES(tmp)) {
				return new Date(parseInt(tmp));
			}
			return null;
		};
		ig.augment(IgCalendarDay, IgUIElement);
		function IgCalendarPopup(e) {
			this.IgCalendar(e);
		}
		;
		IgCalendarPopup.prototype.hide = function(rs) {
			var divElm = this.getDateChooser();
			var jsNode = ig.getUIElementById(divElm.id);
			var args = jsNode._raiseClientEvent("popupClosing", "Input", null,
					null, null, null);
			if (args != null && args.get_cancel())
				return;
			this.callSuper("IgPopup", "hide", rs);
			jsNode._raiseClientEvent("popupClosed", "Input", null, null, null,
					null);
		}
		IgCalendarPopup.prototype.getDateChooser = function() {
			return this.elm._dateChooser;
		};
		IgCalendarPopup.prototype.init = function(dateChooser) {
			this.elm.setAttribute(ig.PROP_DEFAULT_CLASS, dateChooser.elm
					.getAttribute(ig.input.CSS_DEFAULT_CALENDAR)
					+ "");
			this.elm.setAttribute(ig.input.CSS_DEFAULT_DATE, dateChooser.elm
					.getAttribute(ig.input.CSS_DEFAULT_DATE)
					+ "");
			this.elm.setAttribute(ig.input.CSS_DISABLED_DATE, dateChooser.elm
					.getAttribute(ig.input.CSS_DISABLED_DATE)
					+ "");
			this.elm.setAttribute(ig.input.CSS_HOVER_DATE, dateChooser.elm
					.getAttribute(ig.input.CSS_HOVER_DATE)
					+ "");
			this.elm.setAttribute(ig.input.CSS_SELECTED_DATE, dateChooser.elm
					.getAttribute(ig.input.CSS_SELECTED_DATE)
					+ "");
			this.elm.setAttribute(ig.input.CSS_HEADER, dateChooser.elm
					.getAttribute(ig.input.CSS_HEADER)
					+ "");
			this.elm.setAttribute(ig.input.CSS_DAY_HEADER, dateChooser.elm
					.getAttribute(ig.input.CSS_DAY_HEADER)
					+ "");
			this.elm
					.setAttribute(
							ig.input.CSS_CALENDAR_DAY_NOT_CURRENT_MONTH,
							dateChooser.elm
									.getAttribute(ig.input.CSS_CALENDAR_DAY_NOT_CURRENT_MONTH)
									+ "");
			this.setMinDate(dateChooser.elm.getAttribute(ig.input.MIN_DATE));
			this.setMaxDate(dateChooser.elm.getAttribute(ig.input.MAX_DATE));
			this.setFirstDayOfWeek(dateChooser.elm
					.getAttribute(ig.input.FIRST_DAY_OF_WEEK));
			this.setDayList(dateChooser.elm.getAttribute(ig.input.DAY_LIST));
			this.setLongDayList(dateChooser.elm
					.getAttribute(ig.input.LONG_DAY_LIST));
			this
					.setMonthList(dateChooser.elm
							.getAttribute(ig.input.MONTH_LIST));
			this.setShortDayList(dateChooser.elm
					.getAttribute(ig.input.SHORT_DAY_LIST));
			this.setShortMonthList(dateChooser.elm
					.getAttribute(ig.input.SHORT_MONTH_LIST));
			this.setShowDayHeader(dateChooser.elm
					.getAttribute(ig.input.SHOW_DAY_HEADER));
			this.setShowHeader(dateChooser.elm
					.getAttribute(ig.input.SHOW_HEADER));
			this.populateMonthYear();
			this.setDisplayedDate(null);
			this.setSelectedDate(dateChooser.getDate());
		};
		IgCalendarPopup.prototype.repaint = function() {
		}
		IgCalendarPopup.prototype.rebuild = function() {
			this.callSuper("IgCalendar", "repaint");
			var curDate = this.getDisplayedDate();
			var year = curDate.getFullYear();
			var month = curDate.getMonth();
			var selectedDate = this.getSelectedDate();
			var firstDayInMonth = curDate.getDay();
			var firstDayOfWeek = this.getFirstDayOfWeek();
			var offset = firstDayInMonth - firstDayOfWeek + 1;
			if (offset < 0) {
				offset = 7 + offset;
			}
			curDate.setDate(curDate.getDate() - offset);
			this.synchroniseMonthYear(month, year);
			var dateMin = this.getMinDate();
			var dateMax = this.getMaxDate();
			var dayClass = this.getAttribute("odcCalendarDay");
			var dayDisabledClass = "igDisabledCalendarDay";
			var daySelectedClass = this.getAttribute("oscCalendarDay");
			var dayHeaderClass = this.getAttribute("ocssdh");
			var dayNotCurrentMonthClass = this
					.getAttribute(ig.input.CSS_CALENDAR_DAY_NOT_CURRENT_MONTH);
			var body = "<table>";
			if (this.getShowDayHeader()) {
				body += "<thead class='" + dayHeaderClass + "'>";
				body += "<tr>";
				var weekDays = this.getDayList().split(",");
				var dayShow = parseInt(firstDayOfWeek) - 1;
				for ( var h = 0; h < 7; h++) {
					body += "<td>" + weekDays[dayShow++] + "</td>";
					if (dayShow >= 7) {
						dayShow = 0;
					}
				}
				body += "</tr></thead>";
			}
			this.setAttribute(ig.PROP_SELECTED_ELEMENT, "");
			body += "<tbody>";
			for ( var i = 0; i < 6
					&& curDate.getMonth() != ig.input.getNextMonthOfDate(this
							.getDisplayedDate()); i++) {
				body += "<tr>";
				for (j = 0; j < 7; j++) {
					var calendarDayId = this.elm.id + "_" + (i * 7 + j);
					body += "<td otype='CalendarDay' id='" + calendarDayId
							+ "' oDate='" + curDate.getTime() + "'";
					if (((dateMin !== null) && (curDate < dateMin))
							|| ((dateMax !== null) && (curDate > dateMax))) {
						body += " odsbld='true' class='" + dayClass + " "
								+ dayDisabledClass + "'>" + curDate.getDate()
								+ "</td>";
					} else {
						if (ig.isSameDay(selectedDate, curDate)) {
							this.setAttribute(ig.PROP_SELECTED_ELEMENT,
									calendarDayId);
							body += " class='" + daySelectedClass
									+ "'><a href='#' onclick='return false;'>"
									+ curDate.getDate() + "</a></td>";
						} else {
							if (curDate.getMonth() == month) {
								body += " class='"
										+ dayClass
										+ "'><a href='#' onclick='return false;'>"
										+ curDate.getDate() + "</a></td>";
							} else {
								body += " class='"
										+ dayNotCurrentMonthClass
										+ "'><a href='#' onclick='return false;'>"
										+ curDate.getDate() + "</a></td>";
							}
						}
					}
					curDate.setDate(curDate.getDate() + 1);
				}
				body += "</tr>";
			}
			body += "<tr>";
			body += "<td colspan='7'></td>";
			body += "</tr>";
			body += "</tbody>";
			body += "</table>";
			var calBody = this.getBody();
			if (!ig.isNull(calBody)) {
				calBody.innerHTML = body;
			}
		};
		IgCalendarPopup.prototype.populateMonthYear = function(month, year) {
			var oMonth = this.getMonthCombo();
			if (!ig.isNull(oMonth)) {
				oMonth.length = 0;
				var months = this.getMonthList().split(",");
				if (!ig.isNull(months)) {
					for ( var i = 0; i < months.length; i++) {
						oMonth.options[i] = new Option(months[i]);
					}
				}
			}
		};
		IgCalendarPopup.prototype.onClick = function(evt) {
			var flagValue = ig.getAttribute(evt.target, ig.PROP_FLAG);
			if (flagValue == "bpy") {
				this.showPreviousMonth();
			} else if (flagValue == "bny") {
				this.showNextMonth();
			}
		};
		IgCalendarPopup.prototype.onKeyDown = function(evt) {
			var flagValue = ig.getAttribute(evt.target, ig.PROP_FLAG);
			if (flagValue == "tbcy") {
				switch (evt.keyCode) {
				case evt.KEY_UP:
					this.showNextYear();
					break;
				case evt.KEY_DOWN:
					this.showPreviousYear();
					break;
				case evt.KEY_RETURN:
					var dd = this.getDisplayedDate();
					var newYear = evt.target.value;
					this.showDate(dd.getMonth(), newYear);
					return false;
					break;
				}
			}
		};
		IgCalendarPopup.prototype.setDateChooser = function(parent) {
			this.elm._dateChooser = parent;
		};
		IgCalendarPopup.prototype.show = function(parent) {
			var p = ig.getUIElementById(parent);
			this.init(p);
			var pos = p.getPagePosition();
			this.setDateChooser(p.elm);
			this.rebuild();
			var clientHeight = document.documentElement.clientHeight;
			var popup = ig.getUIElementById("igCalendarPopup");
			var popupY;
			if ((pos.y + p.getHeight() + popup.getHeight()) > clientHeight) {
				popupY = pos.y - popup.getHeight();
			} else {
				popupY = pos.y + p.getHeight();
			}
			this.callSuper("IgPopup", "show", pos.x, popupY);
			var sd = this.getSelectedElement();
			if (ig.isNull(sd)) {
				var ahrefs = this.elm.getElementsByTagName("a");
				if (ig.isArray(ahrefs)) {
					sd = ahrefs[0];
				}
			}
			if (!ig.isNull(sd)) {
				sd.focus();
			}
		};
		IgCalendarPopup.prototype.showDate = function(month, year) {
			if (!isNaN(month) && !isNaN(year)) {
				var oDate = new Date(year, month, "01");
				if (year.length <= 2) {
					if (oDate.getFullYear() <= 1950) {
						oDate.setYear(oDate.getFullYear() + 100);
					}
				}
				this.setDisplayedDate(oDate);
				this.rebuild();
			}
		};
		ig.augment(IgCalendarPopup, IgPopup);
		ig.augment(IgCalendarPopup, IgCalendar);
		function IgInput(e) {
			this.IgUIComponent(e);
		}
		;
		IgInput.prototype.init = function(clientSideListeners) {
			this._initClientEventsForObject(this.elm, clientSideListeners);
			this._raiseClientEvent("Initialize", null, null, null);
		};
		ig.augment(IgInput, IgUIComponent);
		function IgInputList(e) {
			this.IgInput(e);
		}
		;
		IgInputList.prototype.init = function(clientSideListeners) {
			this.callSuper("IgInput", "init", clientSideListeners);
			this.elm._listValues = this.getSelectionState(null);
		};
		IgInputList.prototype.getSavedState = function() {
			return this.elm._listValues;
		};
		IgInputList.prototype.setSavedState = function(newState) {
			this.elm._listValues = newState;
		};
		IgInputList.prototype.getIndexOfNodeValue = function(nodeValue) {
			var found = 0;
			var parentSpan = this.elm;
			if (parentSpan.nodeName == "SPAN") {
				var kid = parentSpan.firstChild;
				var subChild = null;
				while (kid != null) {
					if (kid.nodeName == "SPAN") {
						subChild = kid.firstChild;
						if (subChild.nodeName == "INPUT") {
							if (subChild.value == nodeValue) {
								return found;
							}
							found++;
						}
					}
					kid = kid.nextSibling;
				}
			}
			return -1;
		};
		IgInputList.prototype.getSelectionState = function(
				valuOfNodeToBeChanged) {
			var parentSpan = this.elm;
			if (parentSpan.nodeName != "SPAN")
				return null;
			var result = [];
			var kid = parentSpan.firstChild;
			var subChild = null;
			while (kid != null) {
				if (kid.nodeName == "SPAN") {
					subChild = kid.firstChild;
					if (subChild.nodeName == "INPUT") {
						if (valuOfNodeToBeChanged != null
								&& subChild.value == valuOfNodeToBeChanged) {
							result.push(!subChild.checked);
						} else {
							result.push(subChild.checked);
						}
					}
				}
				kid = kid.nextSibling;
			}
			return result;
		};
		IgInputList.prototype.setSelectionState = function(state) {
			var parentSpan = this.elm;
			if (parentSpan.nodeName != "SPAN")
				return;
			var counter = 0;
			var kid = parentSpan.firstChild;
			var subChild = null;
			while (kid != null) {
				if (kid.nodeName == "SPAN") {
					subChild = kid.firstChild;
					if (subChild.nodeName == "INPUT") {
						subChild.checked = state[counter];
						counter++;
					}
				}
				kid = kid.nextSibling;
			}
		};
		ig.augment(IgInputList, IgInput);
		ig.input.init();
		ig.addEventListener(window, "load",
				ig.input.addClickListenerForCalendar, false);
	}
	$IG.InputEventArgs = function() {
		$IG.InputEventArgs.initializeBase(this);
	}
	$IG.InputEventArgs.prototype = {
		get_browserEvent : function() {
			return this._props[0];
		},
		get_value : function() {
			return this._props[3];
		}
	}
	$IG.InputEventArgs.registerClass("Infragistics.Web.UI.InputEventArgs",
			$IG.CancelEventArgs);
	$IG.InputListEventArgs = function() {
		$IG.InputListEventArgs.initializeBase(this);
	}
	$IG.InputListEventArgs.prototype = {
		get_browserEvent : function() {
			return this._props[0];
		},
		get_currentSelection : function() {
			return this._props[1];
		},
		get_newSelection : function() {
			return this._props[2];
		},
		get_value : function() {
			return this._props[3];
		}
	}
	$IG.InputListEventArgs.registerClass(
			"Infragistics.Web.UI.InputListEventArgs", $IG.CancelEventArgs);
}