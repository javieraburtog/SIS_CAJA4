// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

Type.registerNamespace("Infragistics.Web.UI");
var $IG = Infragistics.Web.UI;
function ig_EventObject() {
	this.event = null;
	this.cancel = false;
	this.cancelPostBack = false;
	this.needPostBack = false;
	this.reset = function() {
		this.event = null;
		this.needPostBack = false;
		this.cancel = false;
		this.cancelPostBack = false;
		this.needAsyncPostBack = false;
	}
}
function ig_fireEvent(oControl, eventName) {
	var i, fn = eventName;
	if (!fn || !oControl)
		return false;
	if (ig.isName(fn)) {
		fn += "(oControl";
		for (i = 2; i < ig_fireEvent.arguments.length; i++)
			fn += ", ig_fireEvent.arguments[" + i + "]";
		fn += ");";
	}
	try {
		eval(fn);
	} catch (i) {
		window.status = "Can't eval " + fn;
		return false;
	}
	return true;
}
function ig_dispose(obj) {
	if (ig.isIE && ig.isWin)
		for ( var item in obj) {
			var t = typeof obj[item];
			if (obj[item] && t != 'undefined' && !obj[item].tagName
					&& !obj[item].disposing && t != 'boolean' && t != 'number'
					&& t != 'string' && t != 'function') {
				try {
					obj[item].disposing = true;
					ig_dispose(obj[item]);
				} catch (e1) {
					;
				}
			}
			try {
				delete obj[item];
			} catch (e2) {
				;
			}
		}
}
function ig_cancelEvent(e, type) {
	if (e == null)
		if ((e = window.event) == null)
			return;
	if (type && e.type != type)
		return;
	if (e.stopPropagation != null)
		e.stopPropagation();
	if (e.preventDefault != null)
		e.preventDefault();
	e.cancelBubble = true;
	e.returnValue = false;
}
if (!ig) {
	function Ig() {
		this.components = [];
		this.PROP_ALLOW_MULTI_SELECT = "oams";
		this.PROP_TYPE = "otype";
		this.PROP_COMPONENT = "ocontrol";
		this.PROP_DATA_ON_DEMAND = "odod";
		this.PROP_FLAG = "oflg";
		this.PROP_PARENT = "oParent";
		this.PROP_PARENT_COMPONENT = "oParentComponent";
		this.PROP_DISABLED = "odsbld";
		this.PROP_EXPANDED = "oex";
		this.PROP_DEFAULT_CLASS = "odc";
		this.PROP_ALTERNATE_CLASS = "odac";
		this.PROP_DISABLED_CLASS = "odsc";
		this.PROP_HOVER_CLASS = "ohc";
		this.PROP_SELECTED_CLASS = "osc";
		this.PROP_FIRE_EVENT = "ofe";
		this.PROP_HREF = "ohref";
		this.PROP_SELECTED_ELEMENT = "oseid";
		this.PROP_TARGET = "otarget";
		this.PROP_FOR_COMPONENT_ID = "ofcid";
		this.PROP_FOR_COMPONENT = "igfcid";
		var agt = navigator.userAgent.toLowerCase();
		this.isMozilla = (agt.indexOf('mozilla') != -1);
		this.isIE = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
		this.isOpera = (agt.indexOf("opera") != -1);
		this.isNetscape = (agt.indexOf("netscape") != -1);
		this.isWin = (agt.indexOf("win") >= 0);
		this.isFirefox = (agt.indexOf(' firefox/') > -1);
		this.uniqueId = 0;
		this.loadedScripts = [];
		this.onLoadCallbacks = [];
		this.STRICT_MODE = 1;
		this.QUIRKS_MODE = 2;
		this.DOC_MODE = (document.compatMode == 'CSS1Compat') ? this.STRICT_MODE
				: this.QUIRKS_MODE;
		this.augment = function(c, sc, scName) {
			if (c !== sc) {
				var cp = c.prototype;
				var scp = sc.prototype;
				var n = ig.getFunctionName(sc);
				if (!this.isEmpty(n)) {
					cp[n] = sc;
				} else if (!this.isEmpty(scName)) {
					cp[scName] = sc;
				}
				for ( var p in scp) {
					if (!(cp[p])) {
						cp[p] = scp[p];
					}
				}
				cp["callSuper"] = function(sclazz, method) {
					if (ig.NaES(sclazz) && ig.NaES(method)) {
						var m = this[sclazz].prototype[method];
						var params = [];
						for ( var i = 2; i < arguments.length; i++) {
							params[i - 2] = arguments[i];
						}
						return m.apply(this, params);
					}
				}
			}
		};
		/*this.hasInterface = function(instance, interface) {
			var cp = instance;
			var scp = interface.prototype;
			for ( var p in scp) {
				if (!(cp[p])) {
					return false;
				}
			}
			return true;
		}*/
		this.bind = function(method, object, args) {
			return function() {
				method.apply(object, args);
			};
		};
		this.camelize = function(p) {
			if (ig.NaES(p)) {
				p = p.replace(/(-|_)[a-z]/g, function(m) {
					return m.substr(1).toUpperCase()
				});
			}
			return p;
		}
		this.delegates = function(obj, method) {
			if (!ig.isNull(obj) && !ig.isNull(method)) {
				var params = [];
				for ( var i = 2; i < arguments.length; i++) {
					params[i - 2] = arguments[i];
				}
				return method.apply(obj, params);
			}
		};
		this.debug = function(tmp, id) {
			if (!ig.NaES(id)) {
				id = "infragisticsDebug";
			}
			var out = ig.getElementById(id);
			if (!out) {
				return;
			}
			var s = tmp + "\n" + out.value;
			s = s.substring(0, 500);
			out.value = s;
		};
		this.deleteNode = function(node) {
			if (!ig.isNull(node)) {
				var parent = node.parentNode;
				if (!ig.isNull(parent)) {
					parent.removeChild(node);
				}
				node = null;
			}
		};
		this.addEventListener = function(e, anEvent, aListener, useCapture) {
			if (e.addEventListener) {
				e.addEventListener(anEvent, aListener, useCapture);
			} else if (e.attachEvent) {
				e.attachEvent("on" + anEvent, aListener);
			}
		};
		this.getAttribute = function(n, attrn) {
			var node = ig.getElementById(n);
			return (node && node.getAttribute) ? node.getAttribute(attrn)
					: null;
		};
		this.getClientHeight = function() {
			var tmp = 0;
			if ((ig.DOC_MODE == ig.STRICT_MODE)
					&& (document.documentElement && document.documentElement.clientHeight)) {
				tmp = document.documentElement.clientHeight;
			} else if ((ig.DOC_MODE == ig.QUIRKS_MODE)
					&& (document.body && document.body.clientHeight)) {
				tmp = document.body.clientHeight;
			} else if (self.innerHeight) {
				tmp = self.innerHeight;
			}
			return tmp;
		};
		this.getClientWidth = function() {
			var tmp = 0;
			if (document.documentElement
					&& document.documentElement.clientWidth) {
				tmp = document.documentElement.clientWidth;
			} else if (document.body && document.body.clientWidth) {
				tmp = document.body.clientWidth;
			} else if (self.innerWidth) {
				tmp = self.innerWidth;
			}
			return tmp;
		};
		this.getComponentsByType = function(type) {
			return this.components[type];
		};
		this.getElementById = function(id) {
			var r = null;
			if (typeof (id) != "string") {
				r = id;
			} else if (ig.NaES(id) && document.getElementById) {
				r = document.getElementById(id);
			}
			return r;
		};
		this.getForm = function(id) {
			return ig.findAncestor(id, null, null, "FORM");
		};
		this.getFunctionName = function(f) {
			var n = null;
			if (!ig.isNull(f)) {
				n = f.toString();
				n = n.substring(n.indexOf(" ") + 1, n.indexOf("("));
			}
			return n;
		};
		this.getScrollTop = function() {
			var tmp = 0;
			if (self.pageYOffset) {
				tmp = self.pageYOffset;
			} else if (document.documentElement
					&& document.documentElement.scrollTop) {
				tmp = document.documentElement.scrollTop;
			} else if (document.body && document.body.scrollTop) {
				tmp = document.body.scrollTop;
			}
			return tmp;
		};
		this.getScrollLeft = function() {
			var tmp = 0;
			if (self.pageXOffset) {
				tmp = self.pageXOffset;
			} else if (document.documentElement
					&& document.documentElement.scrollLeft) {
				tmp = document.documentElement.scrollLeft;
			} else if (document.body && document.body.scrollLeft) {
				tmp = document.body.scrollLeft;
			}
			return tmp;
		};
		this.getType = function(id) {
			return this.getAttribute(id, ig.PROP_TYPE);
		};
		this.executeScript = function(code) {
			if (ig.NaES(code)) {
				if (window.execScript) {
					window.execScript(code);
				} else {
					window.eval(code);
				}
			}
		};
		this.executeMethod = function(obj, fname, arg) {
			var res = true;
			if (ig.NaES(fname)) {
				try {
					res = obj[fname](arg);
				} catch (e) {
				}
			}
			return res;
		};
		this.findAncestor = function(e, attrn, attrv, tag, fctn, fctv) {
			var node = ig.getElementById(e);
			while (node) {
				var dn = ig.getUIElementById(node);
				if (ig.isNull(tag)
						|| (!ig.isNull(tag) && (dn.elm.nodeName.toLowerCase() == tag
								.toLowerCase()))) {
					if (ig.isNull(attrn)
							|| (!ig.isNull(attrn) && ig.isNull(attrv) && ig
									.NaES(dn.getAttribute(attrn)))
							|| (!ig.isNull(attrn) && !ig.isNull(attrv) && (dn
									.getAttribute(attrn) == attrv))) {
						if (ig.isNull(fctn)
								|| (!ig.isNull(fctn) && (ig.executeMethod(dn,
										fctn, null) == fctv))) {
							break;
						}
					}
				}
				node = node.parentNode;
			}
			return node;
		};
		this.findDescendant = function(e, attrn, attrv, tag) {
			var nodes = [];
			var result = null;
			e = ig.getElementById(e);
			if (!ig.isNull(e)) {
				if (ig.isNull(tag)) {
					nodes = e.childNodes;
				} else {
					nodes = e.getElementsByTagName(tag);
				}
			}
			if (ig.isArray(nodes)) {
				for ( var i = 0; i < nodes.length; i++) {
					var node = nodes[i];
					if (ig.isNull(tag)
							|| (!ig.isNull(tag) && (node.nodeName.toLowerCase() == tag
									.toLowerCase()))) {
						if (ig.isNull(attrn)) {
							result = node;
							break;
						}
						if (!ig.isNull(attrn) && ig.isNull(attrv)
								&& this.NaES(this.getAttribute(node, attrn))) {
							result = node;
							break;
						}
						if (!ig.isNull(attrn) && !ig.isNull(attrv)
								&& (this.getAttribute(node, attrn) == attrv)) {
							result = node;
							break;
						}
						var tmp = ig.findDescendant(node, attrn, attrv, tag);
						if (!ig.isNull(tmp)) {
							result = tmp;
							break;
						}
					}
				}
			}
			return result;
		};
		this.init = function() {
		};
		this.isArray = function(a) {
			return a != null && a.length != null;
		}
		this.isEmpty = function(o) {
			return !(this.isArray(o) && o.length > 0);
		}
		this.notEmpty = function(o) {
			return (this.isArray(o) && o.length > 0);
		}
		this.isName = function(n) {
			return n && n.indexOf('=') < 0 && n.indexOf(':') < 0
					&& n.indexOf('(') < 0 && n.indexOf(';') < 0
					&& n.indexOf(',') < 0 && n.indexOf('[') < 0
					&& n.indexOf('{') < 0 && n.indexOf('\"') < 0
					&& n.indexOf("'") < 0;
		}
		this.replace = function(txt, s0, s1) {
			while (txt.indexOf(s0) >= 0)
				txt = txt.replace(s0, s1);
			return txt;
		}
		this.isDate = function(d) {
			return (!ig.isNull(d) && d.getFullYear);
		};
		this.isEqualsIgnoreCase = function(s1, s2) {
			var result = false;
			if (ig.NaES(s1) && ig.NaES(s2)) {
				result = (s1.toLowerCase() == s2.toLowerCase());
			}
			return result;
		};
		this.isObject = function(arg) {
			return (arg && typeof arg == 'object')
					|| (typeof arg == 'function');
		};
		this.isSameDay = function(date1, date2) {
			var result = false;
			if (ig.isDate(date1) && ig.isDate(date2)) {
				result = (date1.getFullYear() == date2.getFullYear()
						&& date1.getMonth() == date2.getMonth() && date1
						.getDate() == date2.getDate());
			}
			return result;
		};
		this.isSmartRefreshSupported = function() {
			var isBrowserSupported = ig.isBrowser('firefox', null)
					|| ig.isBrowser('msie', '5up');
			return (!ig.isNull(smartRefreshSupport) && smartRefreshSupport)
					&& (!ig.isNull(window.XMLHttpRequest) || !ig
							.isNull(window.ActiveXObject));
		};
		this.isBrowser = function(browserName, browserVersion) {
			var agent = navigator.userAgent.toLowerCase();
			var agentVersion = navigator.appVersion.toLowerCase();
			var minor = parseFloat(agentVersion);
			var major = parseInt(minor);
			var msie = agentVersion.indexOf('msie') != -1;
			var safari = agent.indexOf('safari') != -1;
			var opera = agent.indexOf('opera') != -1;
			var firefox = (agent.indexOf('Firefox') != -1)
					|| (agent.indexOf('firefox') != -1);
			var gecko = (navigator.product)
					&& (navigator.product.toLowerCase() == "gecko") ? true
					: false;
			var mozilla = (agent.indexOf('mozilla/5') != -1)
					&& (!opera)
					&& gecko
					&& ((navigator.vendor == "") || (navigator.vendor == "Mozilla"));
			var netscape_navigator = (agent.indexOf('mozilla') != -1)
					&& (agent.indexOf('compatible') == -1) && (!opera)
					&& (!mozilla);
			if (browserName == 'msie' && msie) {
				var ieIndex = agentVersion.indexOf('msie');
				minor = parseFloat(agentVersion.substring(ieIndex + 5,
						agentVersion.indexOf(';', ieIndex)));
				major = parseInt(minor);
				if (browserVersion == major
						|| browserVersion == null
						|| (browserVersion.indexOf('up') != -1 && browserVersion
								.substring(0, 1) <= major)) {
					return true;
				}
			} else if (browserName == 'safari' && safari) {
				return true;
			} else if (browserName == 'mozilla' && mozilla) {
				if (browserVersion == null)
					return true;
				var hasMozillaVersion = (navigator.vendorSub) ? navigator.vendorSub
						: 0;
				if (!hasMozillaVersion) {
					hasMozillaVersion = agent.indexOf('rv:');
					hasMozillaVersion = agent.substring(hasMozillaVersion + 3);
					var parenthesisIndex = hasMozillaVersion.indexOf(')');
					hasMozillaVersion = hasMozillaVersion.substring(0,
							parenthesisIndex);
				}
				major = parseInt(hasMozillaVersion);
				if (browserVersion == major)
					return true;
				else if (browserVersion.indexOf('up') != -1) {
					var versionToCheck = browserVersion.substring(0, 1);
					if (versionToCheck <= major)
						return true;
				}
			} else if (browserName == 'navigator' && netscape_navigator) {
				if (browserVersion == null)
					return true;
				var hasMajorMinor = (navigator.vendor)
						&& ((navigator.vendor == "Netscape6") || (navigator.vendor == "Netscape"));
				if (hasMajorMinor) {
					major = parseInt(navigator.vendorSub);
					minor = parseFloat(navigator.vendorSub);
					if (browserVersion == major)
						return true;
					else if (browserVersion.indexOf('up') != -1) {
						var versionToCheck = browserVersion.substring(0, 1);
						if (versionToCheck <= minor) {
							return true;
						}
					}
				}
			} else if (browserName == 'opera' && opera) {
				if (browserVersion == null)
					return true;
				var originalBrowserVersion1 = "opera " + browserVersion;
				var originalBrowserVersion2 = "opera/" + browserVersion;
				if ((agent.indexOf(originalBrowserVersion1) != -1 || agent
						.indexOf(originalBrowserVersion2) != -1))
					return true;
				else if (browserVersion.indexOf('up') != -1) {
					var versionToCheck = browserVersion.substring(0, 1);
					if (versionToCheck == 5) {
						return (!(agent.indexOf("opera 2") != -1 || agent
								.indexOf("opera/2") != -1)
								&& !(agent.indexOf("opera 3") != -1 || agent
										.indexOf("opera/3") != -1) && !(agent
								.indexOf("opera 4") != -1 || agent
								.indexOf("opera/4") != -1));
					} else if (versionToCheck == 6) {
						return (!(agent.indexOf("opera 2") != -1 || agent
								.indexOf("opera/2") != -1)
								&& !(agent.indexOf("opera 3") != -1 || agent
										.indexOf("opera/3") != -1)
								&& !(agent.indexOf("opera 4") != -1 || agent
										.indexOf("opera/4") != -1) && !(agent
								.indexOf("opera 5") != -1 || agent
								.indexOf("opera/5") != -1));
					} else if (versionToCheck == 7) {
						return (!(agent.indexOf("opera 2") != -1 || agent
								.indexOf("opera/2") != -1)
								&& !(agent.indexOf("opera 3") != -1 || agent
										.indexOf("opera/3") != -1)
								&& !(agent.indexOf("opera 4") != -1 || agent
										.indexOf("opera/4") != -1)
								&& !(agent.indexOf("opera 5") != -1 || agent
										.indexOf("opera/5") != -1) && !(agent
								.indexOf("opera 6") != -1 || agent
								.indexOf("opera/6") != -1));
					}
				}
			} else if (browserName == 'firefox' && firefox) {
				if (browserVersion == null)
					return true;
				if ((browserVersion == 2 || browserVersion == '2up')
						&& agent.indexOf("firefox/2") != -1) {
					return true;
				} else if ((browserVersion == 1 || browserVersion == '1up')
						&& agent.indexOf("firefox/1") != -1) {
					return true;
				}
			}
			return false;
		};
		this.isNull = function(v) {
			return (typeof v == 'object' && !v) || (typeof v == 'undefined');
		};
		this.isNumber = function(v) {
			return typeof v == 'number' && isFinite(v);
		};
		this.isOfType = function(n, t) {
			return this.getAttribute(n, ig.PROP_TYPE) == t;
		};
		this.isString = function(v) {
			return typeof v == 'string';
		};
		this.isUIComponent = function(n) {
			return ig.NaES(ig.getAttribute(n, ig.PROP_COMPONENT));
		};
		this.isDomNode = function(n) {
			return ig.isObject(n) && n.elm;
		};
		this.isUIElement = function(n) {
			return ig.NaES(ig.getAttribute(n, ig.PROP_TYPE));
		};
		this.getTargetUIElement = function(e, t) {
			var a = ig.findAncestor(e, ig.PROP_TYPE, t);
			return ig.getUIElementById(a);
		};
		this.getUIElementById = function(id, type) {
			return ig.factory.getInstance(id, type);
		};
		this.getUniqueId = function() {
			if (ig.uniqueId == Number.MAX_VALUE) {
				ig.uniqueId = 0;
			}
			return ig.uniqueId++;
		};
		this.isScriptLoaded = function(src) {
			var result = false;
			if (ig.NaES(src)) {
				var scripts = document.getElementsByTagName("script");
				if (ig.isArray(scripts)) {
					for ( var i = 0; i < scripts.length; i++) {
						if (scripts[i].src == src) {
							result = true;
							break;
						}
					}
				}
				if (!result) {
					for ( var j = 0; j < ig.loadedScripts.length; j++) {
						if (ig.loadedScripts[j] == src) {
							result = true;
							break;
						}
					}
				}
			}
			return result;
		};
		this.loadScript = function(src) {
			if (ig.NaES(src)) {
				if (!ig.isScriptLoaded(src)) {
					var oxmlhttp = null;
					try {
						oxmlhttp = new XMLHttpRequest();
						oxmlhttp.overrideMimeType("text/xml");
					} catch (e) {
						try {
							oxmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
						} catch (e) {
							return null;
						}
					}
					if (!oxmlhttp) {
						return null;
					}
					try {
						oxmlhttp.open("GET", src, false);
						oxmlhttp.send(null);
					} catch (e) {
						return null;
					}
					var code = oxmlhttp.responseText;
					ig.loadedScripts.push(src);
					ig.executeScript(code);
				}
			}
		};
		this.onPartialRefresh = function(httpReq, callback) {
			if (!ig.isNull(callback)) {
				if (callback.charAt) {
					eval(callback)(httpReq);
				} else {
					callback(httpReq);
				}
			} else {
				ig.onPartialRefreshDefault(httpReq);
			}
			ig.processOnLoadCallbacks();
			ig.httprequestpool.release(httpReq);
		};
		this.getFormId = function(chNode) {
			if(chNode == null)
				return false;
			
			var pNode = chNode.parentNode;
			while (!ig.isNull(pNode)) {
				if (!ig.isNull(pNode.nodeName) && pNode.nodeName == 'FORM') {
					return pNode.id;
				}
				pNode = pNode.parentNode;
			}
		};
		this.onPartialRefreshDefault = function(httpReq) {
			var xmlDoc = httpReq.getResponseXml();
			if (!ig.isNull(xmlDoc)) {
				var results = xmlDoc.documentElement;
				if (!ig.isNull(results)) {
					var action = results.getElementsByTagName("action");
					if (ig.isArray(action) && action.length > 0) {
						var actionContent = (!ig.isNull(action[0].firstChild)) ? action[0].firstChild.nodeValue
								: "";
						if (actionContent == "fullPageSubmit") {
							ig.submit();
						}
					} else {
						var components = results
								.getElementsByTagName("component");
						for ( var i = 0; i < components.length; i++) {
							var component = components.item(i);
							var componentId = component.getAttribute("id");
							if (ig.NaES(componentId)) {
								var currentComponent = ig
										.getElementById(componentId);
								if (!ig.isNull(currentComponent)) {
									var content = component
											.getElementsByTagName("content")
											.item(0);
									if (!ig.isNull(content)) {
										var contentAsHtml = (!ig
												.isNull(content.firstChild)) ? content.firstChild.nodeValue
												: "";
										if (ig.NaES(contentAsHtml)) {
											var parsedContent = ig
													.parseHtml(contentAsHtml);
											if (!ig.isNull(parsedContent)) {
												var cscripts = parsedContent
														.getElementsByTagName("script");
												var scripts = [];
												var iscripts = [];
												if (ig.isArray(cscripts)) {
													for ( var j = 0; j < cscripts.length; j++) {
														var aScript = cscripts[j];
														if (ig
																.NaES(aScript.src)) {
															scripts
																	.push(aScript.src);
														} else {
															var scriptContent = aScript.innerHTML;
															if (ig
																	.NaES(scriptContent)) {
																scriptContent = scriptContent
																		.replace(
																				/<!--/g,
																				"");
																scriptContent = scriptContent
																		.replace(
																				/\/\/-->/g,
																				"");
																iscripts
																		.push(scriptContent);
															}
														}
													}
													while (cscripts.length > 0) {
														ig
																.deleteNode(cscripts[0]);
													}
												}
												var parentNode = currentComponent.parentNode;
												var insertBeforeNode = currentComponent.nextSibling;
												var newNode = ig
														.findDescendant(
																parsedContent,
																"id",
																componentId);
												while (!ig.isNull(newNode)) {
													var existingNode = null;
													if (ig.NaES(newNode.id)) {
														existingNode = ig
																.findDescendant(
																		parentNode,
																		"id",
																		newNode.id);
													}
													var nextSibling = newNode.nextSibling;
													if (!ig
															.isNull(existingNode)) {
														ig.replaceNode(
																existingNode,
																newNode);
													} else {
														if (!ig
																.isNull(parentNode)) {
															parentNode
																	.insertBefore(
																			newNode,
																			insertBeforeNode);
														}
													}
													newNode = nextSibling;
												}
												for (j = 0; j < scripts.length; j++) {
													ig.loadScript(scripts[j]);
												}
												for (j = 0; j < iscripts.length; j++) {
													ig
															.executeScript(iscripts[j]);
												}
											}
										}
										ig.deleteNode(parsedContent);
									}
								}
							}
						}
						var clientState = results.getElementsByTagName("state")
								.item(0);
						if (!ig.isNull(clientState)) {
							var clientStateContent = clientState
									.getElementsByTagName("content").item(0);
							if (!ig.isNull(clientStateContent)) {
								var clientStateAsHtml = (!ig
										.isNull(clientStateContent.firstChild)) ? clientStateContent.firstChild.nodeValue
										: "";
								var parsedClientState = ig.parseHtml(
										clientStateAsHtml, false);
								if (!ig.isNull(parsedClientState)) {
									var nodes = parsedClientState.childNodes;
									for ( var k = nodes.length - 1; k >= 0; k--) {
										var aNode = nodes.item(k);
										if (aNode.nodeName == "INPUT") {
											var currentNode = null;
											var name = aNode.name;
											var elements = document
													.getElementsByName(name);
											if (elements.length == 1) {
												currentNode = elements[0];
											} else {
												var length = elements.length;
												var pId1 = ig
														.getFormId(document
																.getElementById(httpReq.sourceOfRequest));
												for (i = length - 1; i >= 0; i--) {
													var el = elements[i];
													if (el.parentNode.id == pId1) {
														currentNode = elements[i];
													}
												}
											}
										}
										if (!ig.isNull(currentNode)) {
											ig.replaceNode(currentNode, aNode);
										}
									}
									ig.deleteNode(parsedClientState);
								}
							}
						}
					}
				}
			}
			document.body.style.cursor = "pointer";
			document.body.style.cursor = "auto";
		};
		this.pad = function(str, len, pad) {
			var result = str + "";
			var tmp = "";
			for ( var i = 0; i < (len - result.length); i++) {
				tmp += pad;
			}
			return tmp + result;
		};
		this.parseHtml = function(toParse, appendToDocument) {
			var aDiv = document.createElement("div");
			if (appendToDocument !== false) {
				document.body.appendChild(aDiv);
			}
			aDiv.innerHTML = toParse;
			return aDiv;
		};
		this.processOnLoadCallbacks = function() {
			for ( var i = 0; i < ig.onLoadCallbacks.length; i++) {
				var tmp = ig.onLoadCallbacks[i];
				ig.executeScript(tmp);
			}
			ig.onLoadCallbacks = [];
		};
		this.registerOnloadCallback = function(fct) {
			ig.onLoadCallbacks[ig.onLoadCallbacks.length] = fct;
			ig.addEventListener(window, "load", ig.processOnLoadCallbacks,
					false);
		};
		this.registerComponent = function(id) {
			var comp = ig.getUIElementById(id);
			if (ig.isNull(comp)) {
				return;
			}
			var type = comp.getType();
			var comps = this.components[type];
			if (ig.isNull(comps)) {
				this.components[type] = new Array();
				comps = this.components[type];
			}
			comps[comps.length] = comp.elm.id;
		};
		this.replaceNode = function(node1, node2) {
			var parent = node1.parentNode;
			if (!ig.isNull(parent)) {
				return parent.replaceChild(node2, node1);
			}
		};
		this.setAttribute = function(e, n, v) {
			if (e && e.setAttribute && this.getAttribute(e, n) != v) {
				e.setAttribute(n, v + "");
				return true;
			}
			return false;
		};
		this.showStatusMsg = function(msg) {
			if (ig.isNull(msg)) {
				msg = "";
			}
			window.status = msg;
		};
		this.submit = function(eventSource, eventName, eventArguments, target) {
			if (!ig.isNull(ig.grid) && eventName != "celledit"
					&& eventName != "rowedit"
					&& eventName != "scrollingLoadOnDemand") {
				evt = new IgEvent();
				evt.type = "beforesubmit";
				evt.target = eventSource;
				ig.grid.onBeforeSubmit(evt);
			}
			if (!ig.NaES(eventSource)) {
				var doc = document;
				if (!ig.isNull(doc)) {
					var frms = doc.getElementsByTagName("FORM");
					if (!ig.isNull(frms)) {
						frms[0].submit();
					}
				}
			} else {
				var frm = ig.getForm(eventSource);
				if (!ig.isNull(frm)) {
					if (!ig.NaES(eventSource)) {
						eventSource = "";
					}
					if (!ig.NaES(eventName)) {
						eventName = "";
					}
					if (!ig.NaES(eventArguments)) {
						eventArguments = "";
					}
					var cse = eventSource + "::" + eventName + "::"
							+ eventArguments;
					var savAction = frm.action;
					var action = new IgUri(savAction);
					action.addParameter(
							"com.infragistics.faces.CLIENT_SIDE_EVENT", cse);
					frm.action = action;
					if (ig.NaES(target)) {
						var oldTarget = frm.target;
						frm.target = target;
						frm.submit();
						frm.target = oldTarget;
					} else {
						frm.submit();
					}
					frm.action = savAction;
				}
			}
		};
		this.smartSubmit = function(eventSource, eventName, eventArguments,
				smartRefreshComponents, jsCallback) {
			
			if (!ig.isSmartRefreshSupported()) {
				ig.submit(eventSource, eventName, eventArguments);
			} else {
				var jsNode = ig.getUIElementById(eventSource);
				var isIgLink = (smartRefreshComponents.indexOf("lnk") != -1);
//				alert('jsNode ' + isIgLink);
				
				if (jsNode) {
					var targetElementID = ig.getAttribute(jsNode.elm,
							ig.PROP_FOR_COMPONENT);
					if (targetElementID) {
						var tmpNode = ig.getUIElementById(targetElementID);
						if (tmpNode && !tmpNode.getParentComponent) {
							tmpNode = Sys.Application
									.findComponent(targetElementID);
						}
						if (tmpNode)
							jsNode = tmpNode;
					}
					while (jsNode && !ig.isNull(jsNode._raiseClientEvent)
							&& !jsNode._raiseClientEvent) {
						jsNode = jsNode.getParentComponent();
					}
					if (jsNode && jsNode._raiseClientEvent) {
						var args = jsNode._raiseClientEvent("AjaxRequesting",
								"Ajax", null, null);
						if (args != null && args.get_cancel())
							return;
					}
				}
				if (!ig.isNull(ig.grid) && eventName != "celledit"
						&& eventName != "rowedit"
						&& eventName != "scrollingLoadOnDemand") {
					evt = new IgEvent();
					evt.type = "beforesubmit";
					evt.target = eventSource;
					ig.grid.onBeforeSubmit(evt);
				}
				var frm = ig.getForm(eventSource);
				if (!ig.isNull(frm)) {
					if (!ig.NaES(eventSource)) {
						eventSource = "";
					}
					if (!ig.NaES(eventName)) {
						eventName = "";
					}
					if (!ig.NaES(eventArguments)) {
						eventArguments = "";
					}
					if (!ig.NaES(smartRefreshComponents)) {
						smartRefreshComponents = "";
					}
					var cse = eventSource + "::" + eventName + "::"
							+ eventArguments;
					var params = ig.formToUri(frm);
					params.addParameter(
							"com.infragistics.faces.CLIENT_SIDE_EVENT", cse);
					params.addParameter(
							"com.infragistics.faces.SMART_REFRESH_COMPONENTS",
							smartRefreshComponents);
					params.addParameter("com.sun.faces.FORM_CLIENT_ID_ATTR",
							frm.id);
					
					var httpRequest = ig.httprequestpool.get();
					httpRequest.send(frm.action, (isIgLink == true ? 'lnk=lnk&' : '') + params.query  , jsCallback,
							eventSource, frm.id);
				}
			}
		};
		this.stringInsert = function(str1, str2, pos) {
			if (!pos) {
				pos = 0;
			}
			return str1.substring(0, pos) + str2
					+ str1.substring(pos, str1.length);
		};
		this.formToUri = function(frm) {
			var qs = new IgUri();
			if (!ig.isNull(frm)) {
				for ( var i = 0; i < frm.elements.length; i++) {
					var control = frm.elements[i];
					if (!ig.isNull(control)) {
						var name = control.name;
						if (ig.NaES(name) && (control.disabled !== true)) {
							var type = control.type;
							if (type == "hidden" || type == "password"
									|| type == "text" || type == "textarea") {
								qs.addParameter(name, control.value);
							} else {
								if (type == "button") {
								} else {
									if (type == "checkbox") {
										if (control.checked === true) {
											qs
													.addParameter(name,
															control.value);
										}
									} else {
										if (type == "radio") {
											if (control.checked === true) {
												qs.addParameter(name,
														control.value);
											}
										} else {
											if (type == "select-one") {
												for ( var j = 0; j < control.options.length; j++) {
													if (control.options[j].selected === true) {
														qs
																.addParameter(
																		name,
																		control.options[j].value);
														break;
													}
												}
											} else {
												if (type == "select-multiple") {
													for ( var k = 0; k < control.options.length; k++) {
														if (control.options[k].selected === true) {
															qs
																	.addParameter(
																			name,
																			control.options[k].value);
														}
													}
												} else {
													if (type == "submit") {
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return qs;
		};
		this.copyForm = function(srcFrm, destDoc, params) {
			if (!ig.isNull(srcFrm) && !ig.isNull(destDoc)) {
				var tmp = '<html><body><form ';
				tmp += ' id="' + srcFrm.id + '"';
				tmp += ' action="' + srcFrm.action + '"';
				tmp += ' method="' + srcFrm.method + '"';
				tmp += ' enctype="' + srcFrm.encoding + '" >';
				for ( var i = 0; i < srcFrm.elements.length; i++) {
					var anElm = srcFrm.elements[i];
					if (anElm.type != "submit" && anElm.type != "reset"
							&& anElm.type != "image") {
						tmp += '<input type="' + anElm.type + '"';
						tmp += ' name="' + anElm.name + '"';
						tmp += ' value="' + anElm.value + '" />';
					}
				}
				if (ig.NaES(params)) {
					var arrayParams = params.split(";");
					for ( var nbParams = 0; nbParams < arrayParams.length; nbParams++) {
						var param = arrayParams[nbParams];
						if (ig.NaES(param)) {
							var paramNameValue = param.split("=");
							if (paramNameValue.length == 2) {
								tmp += '<input type="hidden"';
								tmp += ' name="' + paramNameValue[0] + '"';
								tmp += ' value="' + paramNameValue[1] + '" />';
							}
						}
					}
				}
				tmp += "</form></body></html>";
				destDoc.open();
				destDoc.write(tmp);
				destDoc.close();
			}
		};
		this.NaES = function(str1) {
			return (ig.isString(str1) && !ig.isNull(str1.length) && str1.length > 0) ? true
					: false;
		};
		this.navigateToUrl = function(url, clientId, target) {
			if (ig.NaES(url)) {
				if (ig.NaES(clientId) && ig.NaES(target)) {
					var frm = ig.getForm(clientId);
					if (!ig.isNull(frm)) {
						var oldTarget = frm.target;
						var oldAction = frm.action;
						frm.action = url;
						frm.target = target;
						frm.submit();
						frm.target = oldTarget;
						frm.action = oldAction;
					}
				} else {
					window.location = url;
				}
			}
		};
		this.stringInsertAfter = function(str1, str2, str3) {
			var pos = 0;
			str3 += "";
			pos = str1.indexOf(str3);
			return ig.stringInsert(str1, str2, pos + 1);
		};
		this.toEvent = function(e) {
			return new IgEvent(e);
		};
		this.hashToFloat = [];
		this.toFloat = function(s) {
			var result = this.hashToFloat[s];
			if (result == null) {
				var f = parseFloat(s);
				result = isNaN(f) ? 0 : f;
				this.hashToFloat[s] = result;
			}
			return result;
		};
		this.ucase = function(p) {
			var result = null;
			if (ig.NaES(p)) {
				result = p.substr(0, 1).toUpperCase() + p.substr(1);
			}
			return result;
		};
		this.toFunction = function(funcName) {
			if (funcName instanceof Function) {
				return funcName;
			}
			if (!funcName || !funcName.length || !funcName.charCodeAt) {
				return null;
			}
			var fnc = window[funcName];
			if (fnc instanceof Function) {
				return fnc;
			}
			try {
				fnc = eval(funcName);
				if (fnc instanceof Function) {
					return fnc;
				}
			} catch (e) {
			}
			return null;
		};
		this.getStrippedId = function(id) {
			var pos = id.indexOf(':adr:');
			if (pos >= 0)
				return id.substring(0, pos);
			else
				return this.elm.id;
		};
	}
	;
	var ig = new Ig();
	ig.ArrayList = function(arr) {
		this.array = ((arr) ? arr : new Array());
		this.add = function(o) {
			if (!ig.isNull(o)) {
				this.array.push(o);
			}
		};
		this.addAt = function(i, o) {
			if (!ig.isNull(o)) {
				this.array.splice(i, 0, o);
			}
		};
		this.clear = function() {
			this.array.splice(0, this.array.length);
		};
		this.contains = function(o) {
			for ( var i = 0; i < this.array.length; i++) {
				if (this.array[i] == o) {
					return true;
				}
			}
			return false;
		};
		this.indexOf = function(o) {
			for ( var i = 0; i < this.array.length; i++) {
				if (this.array[i] == o) {
					return i;
				}
			}
			return -1;
		};
		this.get = function(i) {
			return this.array[i];
		};
		this.remove = function(o) {
			var i = this.indexOf(o);
			if (i >= 0) {
				this.array.splice(i, 1);
			}
		};
		this.removeAt = function(i) {
			this.array.splice(i, 1);
		};
		this.set = function(i, o) {
			this.array[i] = o;
		};
		this.size = function() {
			return this.array.length;
		};
		this.toString = function() {
			return this.array.join(",");
		};
	};
	ig.Hashtable = function(arr) {
		this.hashtable = ((arr) ? arr : new Array());
		this.clear = function hashtable_clear() {
			this.hashtable = new Array();
		}
		this.containsKey = function(key) {
			var exists = false;
			for ( var i in this.hashtable) {
				if (i == key && this.hashtable[i] != null) {
					exists = true;
					break;
				}
			}
			return exists;
		}
		this.containsValue = function(value) {
			var contains = false;
			if (value != null) {
				for ( var i in this.hashtable) {
					if (this.hashtable[i] == value) {
						contains = true;
						break;
					}
				}
			}
			return contains;
		}
		this.get = function(key) {
			return this.hashtable[key];
		}
		this.isEmpty = function() {
			return (parseInt(this.size()) == 0) ? true : false;
		}
		this.keys = function() {
			var keys = new Array();
			for ( var i in this.hashtable) {
				if (this.hashtable[i] != null)
					keys.push(i);
			}
			return keys;
		}
		this.put = function(key, value) {
			if (key == null || value == null) {
				throw "NullPointerException {" + key + "},{" + value + "}";
			} else {
				this.hashtable[key] = value;
			}
		}
		this.remove = function(key) {
			var rtn = this.hashtable[key];
			this.hashtable[key] = null;
			return rtn;
		}
		this.size = function() {
			var size = 0;
			for ( var i in this.hashtable) {
				if (this.hashtable[i] != null)
					size++;
			}
			return size;
		}
		this.toString = function() {
			var result = "";
			for ( var i in this.hashtable) {
				if (this.hashtable[i] != null)
					result += "{" + i + "},{" + this.hashtable[i] + "}\n";
			}
			return result;
		}
		this.values = function() {
			var values = new Array();
			for ( var i in this.hashtable) {
				if (this.hashtable[i] != null)
					values.push(this.hashtable[i]);
			}
			return values;
		}
	}
	function IgEventQueue(e) {
		this.elm = ig.getElementById(e + "_eventQueue");
		this.SEPARATOR = "::";
		this.EVENTS_SEPARATOR = ":;";
		this.queueEvent = function(evtSrc, evtName, evtValue) {
			var tmp = "";
			if (!ig.isNull(evtSrc)) {
				tmp = evtSrc;
			}
			if (!ig.isNull(evtName)) {
				tmp += (this.SEPARATOR + evtName);
			}
			if (!ig.isNull(evtValue)) {
				tmp += (this.SEPARATOR + evtValue);
			}
			tmp = this.getValue() + tmp + this.EVENTS_SEPARATOR;
			this.setValue(tmp);
		};
		this.findEvent = function(evtSrc, evtName) {
			var tmp = this.getValue();
			return (ig.isNull(tmp)) ? -1 : tmp.indexOf(evtSrc + this.SEPARATOR
					+ evtName);
		};
		this.getValue = function() {
			return ig.isNull(this.elm) ? null : this.elm.value;
		};
		this.removeEvent = function(evtSrc, evtName) {
			var idx = this.findEvent(evtSrc, evtName);
			if (idx > -1) {
				var tmp = this.getValue();
				var idxSep = tmp.indexOf(this.EVENTS_SEPARATOR, idx);
				if (idxSep > -1) {
					tmp = tmp.substring(0, idx)
							+ tmp.substring(idxSep + 2, tmp.length);
					this.setValue(tmp);
				}
			}
		};
		this.setValue = function(val) {
			if (!ig.isNull(this.elm)) {
				this.elm.value = val;
			}
		};
	}
	;
	function IgEventPackage() {
		this.EVENT_CLICK = "click";
		this.EVENT_DOUBLECLICK = "dblclick";
		this.EVENT_COLLAPSE = "collapse";
		this.EVENT_CONTEXTMENU = "contextmenu";
		this.EVENT_EXPAND = "expand";
		this.EVENT_KEYDOWN = "keydown";
		this.EVENT_KEYPRESS = "keypress";
		this.EVENT_MOUSEDOWN = "mousedown";
		this.EVENT_MOUSEMOVE = "mousemove";
		this.EVENT_MOUSEOVER = "mouseover";
		this.EVENT_MOUSEUP = "mouseup";
		this.EVENT_SCROLL = "scroll";
	}
	;
	ig.event = new IgEventPackage();
	function IgEvent(evt) {
		this.KEY_DOWN = 40;
		this.KEY_ESCAPE = 27;
		this.KEY_LEFT = 37;
		this.KEY_RETURN = 13;
		this.KEY_RIGHT = 39;
		this.KEY_TAB = 9;
		this.KEY_UP = 38;
		this.KEY_DELETE = 46;
		this.MOUSE_LEFT_BUTTON = 1;
		this.MOUSE_RIGHT_BUTTON = 2;
		this.MOUSE_MIDDLE_BUTTON = 4;
		if (evt) {
			this.event = evt;
		} else if (window.event) {
			this.event = window.event;
		}
		if (!this.event) {
			return;
		}
		this.type = (this.event.type) ? this.event.type : "";
		if (this.event.target) {
			this.target = this.event.target;
		} else if (this.event.srcElement) {
			this.target = this.event.srcElement;
		}
		if (this.event.relatedTarget) {
			this.relatedTarget = this.event.relatedTarget;
		} else if (this.event.fromElement && this.type == "mouseover") {
			this.relatedTarget = this.event.fromElement;
		} else if (this.event.toElement && this.type == "mouseout") {
			this.relatedTarget = this.event.toElement;
		}
		if (this.event.keyCode) {
			this.keyCode = this.event.keyCode;
		} else if (this.event.which) {
			this.keyCode = this.event.which;
		} else if (this.event.charCode) {
			this.keyCode = this.event.charCode;
		}
		if (this.event.pageX || this.event.pageY) {
			this.pageX = this.event.pageX;
			this.pageY = this.event.pageY;
		} else if (this.event.clientX || this.event.clientY) {
			this.pageX = this.event.clientX + document.body.scrollLeft;
			this.pageY = this.event.clientY + document.body.scrollTop;
		}
		if (this.type.indexOf("mouse", 0) >= 0) {
			if (this.event.button) {
				this.button = this.event.button;
			} else if (this.event.which) {
				this.button = this.event.which - 1;
			}
		}
		this.isShiftKeyPressed = function() {
			return this.event.shiftKey;
		};
		this.stopPropagation = function() {
			this.event.cancelBubble = true;
			if (this.event.stopPropagation) {
				this.event.stopPropagation();
			}
		};
		this.preventDefault = function() {
			this.event.returnValue = false;
			if (this.event.preventDefault) {
				this.event.preventDefault();
			}
		};
	}
	;
	function IgFactory() {
		this.addClass = function(cls, cp) {
			this[cls] = cp;
		};
		this.getInstance = function(id, type) {
			var r = ig.getElementById(id);
			if (!ig.isDomNode(r)) {
				if (!ig.isNull(r)) {
					if (!ig.NaES(type)) {
						type = ig.getType(r);
					}
					var cn = this[type];
					if (!ig.isNull(cn)) {
						r = new cn(r);
					} else {
						r = new IgDomNode(r);
					}
				}
			}
			return r;
		};
	}
	;
	ig.factory = new IgFactory();
	function IgHttpRequest() {
		this.httpRequestImpl = null;
		this.sourceOfRequest = null;
		this.getHttpRequestImpl = function() {
			if (ig.isNull(this.httpRequestImpl)) {
				if (window.XMLHttpRequest) {
					this.httpRequestImpl = new XMLHttpRequest();
				} else if (window.ActiveXObject) {
					var MSXML_VERSIONS = new Array('MSXML2.XMLHTTP.5.0',
							'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0',
							'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP');
					for ( var i = 0; i < MSXML_VERSIONS.length; i++) {
						try {
							this.httpRequestImpl = new ActiveXObject(
									MSXML_VERSIONS[i]);
							break;
						} catch (e) {
						}
					}
				}
			}
			return this.httpRequestImpl;
		};
		this.getResponseXml = function() {
			var xmlDoc;
			var xmlText;
			if (ig.isNull(this.httpRequestImpl.responseXML)
					&& !ig.isNull(this.httpRequestImpl.responseText)
					|| (!ig.isNull(this.httpRequestImpl.responseXML) && this.httpRequestImpl.responseXML.documentElement == null)) {
				var endIdx = this.httpRequestImpl.responseText
						.indexOf("</fragments>");
				xmlText = this.httpRequestImpl.responseText
						.substring(0, endIdx)
						+ "</fragments>";
			}
			if (ig.isNull(this.httpRequestImpl.responseXML)
					&& !ig.isNull(this.httpRequestImpl.responseText)) {
				xmlDoc = (new DOMParser()).parseFromString(xmlText, 'text/xml');
				return xmlDoc;
			} else if (!ig.isNull(this.httpRequestImpl.responseXML)
					&& this.httpRequestImpl.responseXML.documentElement == null) {
				if (window.ActiveXObject) {
					xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
					xmlDoc.async = "false";
					xmlDoc.loadXML(xmlText);
					return xmlDoc;
				}
			}
			return (!ig.isNull(this.httpRequestImpl)) ? this.httpRequestImpl.responseXML
					: null;
		};
		this.send = function(url, params, callback, sourceOfRequest, formId) {
			//alert((params.indexOf("lnk") != -1));
			this.sourceOfRequest = sourceOfRequest;
			var compNs;
			if (formId.indexOf("viewns") != -1) {
				compNs = formId.substring(6, formId.length);
				var formIdidx = compNs.indexOf(":");
				compNs = compNs.substring(0, formIdidx);
			}
			var singleRefreshUrl = document.getElementById("igUrlns" + compNs);
			if (!ig.isNull(singleRefreshUrl)) {
				url = singleRefreshUrl.value;
			}
			var httpReq = this.getHttpRequestImpl();
			var myself = this;
			if (!ig.isNull(httpReq)) {
				var isLink = (params.indexOf("lnk") != -1);
				//Mostralo
				if(isLink){
					$('#wraploader').show();

					var bodyHeight = $(document).height() + 'px';
					
					$(window).resize(function() {

		                var bodyWidth = $(window).width() + 'px';
						$('#wraploader').css('width',  bodyWidth);
						$('#wraploader').css('height', bodyHeight);
						
		            }).trigger("resize");
				}				
				var srcComp = ig.getUIElementById(sourceOfRequest);
				if (!ig.isNull(srcComp)) {
					srcComp.setCursor("wait");
				}
				var poundSign = url.indexOf("#");
				if (poundSign != -1) {
					url = url.substring(0, poundSign);
				}
				httpReq.open("POST", url, true);
				httpReq.setRequestHeader("Content-Type",
						"application/x-www-form-urlencoded");
				
				httpReq.onreadystatechange = function() {
					// ===================== INICIO DE LINEA 35
					try{
						
						if (httpReq.readyState == 4) {
							
							var jsNode = ig.getUIElementById(sourceOfRequest);
							if (jsNode) {
								var targetElementID = ig.getAttribute(jsNode.elm,
										ig.PROP_FOR_COMPONENT);
								if (targetElementID) {
									var tmpNode = ig
											.getUIElementById(targetElementID);
									if (tmpNode && !tmpNode.getParentComponent) {
										tmpNode = Sys.Application
												.findComponent(targetElementID);
									}
									if (tmpNode)
										jsNode = tmpNode;
								}
								while (jsNode
										&& !ig.isNull(jsNode._raiseClientEvent)
										&& !jsNode._raiseClientEvent) {
									jsNode = jsNode.getParentComponent();
								}
								if (jsNode && jsNode._raiseClientEvent) {
									jsNode._raiseClientEvent(
											"AjaxResponseCompleted", "Ajax", null,
											null);
								}
							}
							ig.onPartialRefresh(myself, callback);
							if (!ig.isNull(srcComp)) {
								srcComp.setCursor("");
							}
							if(isLink){
								$('#wraploader').hide();
							}
						}
					}catch(e){
						alert('Error' +e);
						
					}
				};
				try{ httpReq.send(params); }catch(e){alert('ups 1.1');}
			}
		};
		this.getSourceOfRequest = function() {
			return this.sourceOfRequest;
		};
	}
	;
	function IgPool(objectType) {
		this.objectType = objectType;
		this.pool = new Array();
		this.get = function() {
			var result = null;
			
			try{
			
				for (i = 0; i < this.pool.length; i++) {
					var anObject = this.pool[i];
					if (!ig.isNull(anObject)) {
						if (anObject.infragisticsPool_isBusy === false) {
							result = anObject;
							break;
						}
					}
				}
				// ===================== FIN DE LINEA 35
				
				if (ig.isNull(result)) {
					result = new this.objectType;
					this.pool[this.pool.length] = result;
				}
				if (!ig.isNull(result)) {
					result.infragisticsPool_isBusy = true;
				}
			}catch(error){
				alert('ups 2');
			}	
			return result;
		};
		this.release = function(obj) {
			for (i = 0; i < ig.httprequestpool.pool.length; i++) {
				var anObject = ig.httprequestpool.pool[i];
				if (anObject == obj) {
					anObject.infragisticsPool_isBusy = false;
					return;
				}
			}
		};
	}
	;
	ig.httprequestpool = new IgPool(IgHttpRequest);
	function IgUri(uri) {
		this.baseUri = null;
		this.query = null;
		this.addParameter = function(name, value) {
			if (ig.NaES(name)) {
				if (ig.NaES(this.query)) {
					if (this.query.charAt(this.query.length - 1) != '&') {
						this.query = this.query + "&";
					}
				} else {
					this.query = "";
				}
				this.query = this.query + encodeURIComponent(name) + "="
						+ encodeURIComponent(value);
			}
		};
		this.parseUri = function(uri) {
			if (ig.NaES(uri)) {
				var i = uri.indexOf("?");
				if (i != -1) {
					this.baseUri = uri.substring(0, i);
					this.query = uri.substr(i + 1);
				} else {
					this.baseUri = uri;
					this.query = null;
				}
			}
		};
		this.toString = function() {
			return (ig.NaES(this.query)) ? this.baseUri + "?" + this.query
					: this.baseUri;
		};
		this.parseUri(uri);
	}
	;
	ig.init();
}