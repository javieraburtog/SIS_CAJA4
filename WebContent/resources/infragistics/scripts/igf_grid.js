// (c) 2008 Infragistics - Do NOT modify the content of this file
// Version 9.2.20092.1000

if (ig) {
	if (!ig.grid) {
		function IgWebGridPackage() {
			this.TYPE_GRID = "Grid";
			this.TYPE_GRID_CELL = "GridCell";
			this.TYPE_GRID_ROW = "GridRow";
			this.TYPE_GRID_ROW_SCROLL = "GridRowScroll";
			this.TYPE_GRID_SECTION = "GridSection";
			this.TYPE_GRID_ROW_SELECTOR = "GridRowSelector";
			this.TYPE_GRID_COLUMN_HEADER = "GridColumnHeader";
			this.TYPE_GRID_AG_FUNCTION = "GridAgFunction";
			this.TYPE_GRID_COLUMN_FILTER = "GridColumnFilter";
			this.PROP_JUNCTION_EXPANDED_ICON = "ojei";
			this.PROP_JUNCTION_COLLAPSED_ICON = "ojci";
			this.PROP_IS_SCROLLING = "igscrlg";
			this.PROP_IS_LOAD_ON_DEMAND = "igLoadOnDemand";
			this.PARTITIONED_ROW_COUNT = "igRowCount";
			this.PROP_SINGLE_ROW_HEIGHT = "singleRowHeight";
			this.PROP_PREVIOUS_SCROLL_TOP = "previousScrollTop";
			this.PROP_CURRENT_VIRTUAL_PAGE = "currentVirtualPage";
			this.PROP_LOD_MARKER_HEIGHT = "lodMarkerHeight";
			this.PROP_IG_ROW_FETCH_SIZE = "igRowFetchSize";
			this.PROP_IG_SCROLL_TOP_STATE = "igScrollTopState";
			this.LOAD_ON_DEMAND_DEFAULT = "default";
			this.LOAD_ON_DEMAND_PARTITIONED = "partitioned";
			this.PROP_IG_LOADING_IMAGE = "igLoadingImage";
			this.PROP_IG_CURRENT_ROW_COUNT = "igCurrentRowCount";
			this.PROP_IG_NEEDS_SCROLLING = "igNeedsScrolling";
			this.PROP_IG_VIRTUAL_PAGE_COUNT = "igVirtualPageCount";
			this.PROP_IG_LOAD_ON_DEMAND_TIMER_DEFAULT = 200;
			this.PROP_IG_LOAD_ON_DEMAND_TIMER_PARTITIONED = 400;
			this.PROP_IG_LOAD_ON_DEMAND_THRESHOLD = 0.25;
			this.PROP_IG_LOAD_ON_DEMAND_TOOLTIP_OFFSET = 50;
			this.PROP_REMAINDER_COUNT = "igRemainderCount";
			this.PROP_FORCE_VERTICAL_SCROLL = "igScrollVerticalForced";
			this.init = function() {
				ig.factory.addClass(ig.grid.TYPE_GRID, IgGrid);
				ig.factory.addClass(ig.grid.TYPE_GRID_CELL, IgGridCell);
				ig.factory.addClass(ig.grid.TYPE_GRID_ROW, IgGridRow);
				ig.factory.addClass(ig.grid.TYPE_GRID_ROW_SCROLL,
						IgGridScrollingRow);
				ig.factory.addClass(ig.grid.TYPE_GRID_SECTION, IgGridSection);
				ig.factory.addClass(ig.grid.TYPE_GRID_COLUMN_HEADER,
						IgGridColumnHeader);
				ig.factory.addClass(ig.grid.TYPE_GRID_AG_FUNCTION,
						IgGridAgFunction);
				ig.factory.addClass(ig.grid.TYPE_GRID_COLUMN_FILTER,
						IgGridFilterCell);
			};
			this.initGrid = function(gridId, clientSideListeners) {
				var e = ig.grid.getGrid(gridId);
				if (ig.isNull(e)) {
					clearInterval(ig.grid.initTimeoutManagers[gridId]);
					return;
				}
				var scroll = e.getAttribute(ig.grid.PROP_FORCE_VERTICAL_SCROLL);
				e.isForcedVerticalScroll = !ig.isNull(scroll)
						&& scroll == "true";
				if (!ig.isNull(e) && e.elm.offsetHeight == 0
						&& ig.isNull(ig.grid.initTimeoutManagers[e.elm.id])) {
					ig.grid.initTimeoutManagers[e.elm.id] = setInterval(
							'ig.grid.initGrid(\"' + e.elm.id + '\", '
									+ clientSideListeners + ');', 500);
				} else {
					if (e.elm.offsetHeight != 0) {
						clearInterval(ig.grid.initTimeoutManagers[e.elm.id]);
						if (!ig.isNull(e)) {
							e.init(clientSideListeners);
							var form = e.getForm(e.id);
							if (ig.grid.formsWithGrid[form.id] == null) {
								ig.addEventListener(form, "submit",
										ig.grid.onBeforeSubmit, false);
								ig.grid.formsWithGrid[form.id] = form;
							}
						}
					}
				}
			};
			this.__finishInitScrollingGrid = function(gridId) {
				var e = ig.grid.getGrid(gridId);
				if (!ig.isNull(e)
						&& !ig.isNull(ig.grid.initTimeoutManagers[e.elm.id])) {
					clearInterval(ig.grid.initTimeoutManagers[e.elm.id]);
					e.__finishInitScrollingGrid();
				}
			};
			this.onExpandedOnDemand = function(httpReq) {
				ig.onPartialRefreshDefault(httpReq);
				var node = ig.getUIElementById(httpReq.getSourceOfRequest());
				if (!ig.isNull(node)) {
					node.focus();
				}
			};
			this.onExpandRow = function(row) {
				if (!ig.isNull(row)) {
					if (row.hasChild()) {
						icon = (row.isExpanded()) ? row.getAttribute(
								ig.grid.PROP_JUNCTION_EXPANDED_ICON, true)
								: row.getAttribute(
										ig.grid.PROP_JUNCTION_COLLAPSED_ICON,
										true);
						if (ig.isNull(row) || row.elm.childNodes.length == 0) {
							var scrollingRow = ig.getUIElementById(row.elm.id
									+ "_sr");
							scrollingRow.updateJunctionIcon(icon);
						} else {
							row.updateJunctionIcon(icon);
						}
					}
					row.repaint();
				}
			};
			this.selectAllRows = function(domNode, select) {
				var grid = this.getGrid(domNode);
				if (!ig.isNull(grid)) {
					grid.selectAllRows(select);
					if (grid.isImmediateRowsChangeEvent()) {
						ig.smartSubmit(grid.elm.id, "selectAllRows", null);
					}
				}
			};
			this.selectRow = function(domNode, select) {
				var row = this.getTargetRow(domNode);
				if (row != null) {
					var grid = this.getGrid(domNode);
					if (select) {
						var args = grid._raiseClientEvent(
								"RowSelectionChanging", "GridSelectionChange",
								row, false);
						if (args != null && args.get_cancel())
							return false;
						row.select();
						grid._raiseClientEvent("RowSelectionChanged",
								"GridSelectionChange", row, true);
					} else {
						var args = grid._raiseClientEvent(
								"RowSelectionChanging", "GridSelectionChange",
								row, true);
						if (args != null && args.get_cancel())
							return false;
						row.unselect();
						grid._raiseClientEvent("RowSelectionChanged",
								"GridSelectionChange", row, false);
					}
					if (!ig.isNull(grid) && grid.isImmediateRowsChangeEvent()) {
						ig.smartSubmit(grid.elm.id, null, null);
					}
				}
				return true;
			};
			this.toggleRow = function(rowId) {
				var row = ig.getUIElementById(rowId);
				if (ig.isNull(row)) {
					row = ig.getUIElementById(rowId + "_fr");
				}
				if (!ig.isNull(row)) {
					if (row.isEnabled()) {
						row.toggle();
					}
				}
				return false;
			};
			this.getGrid = function(domNode) {
				return ig.getTargetUIElement(domNode, this.TYPE_GRID);
			};
			this.initLoadOnDemandPartitioned = function(grid) {
				var virtualLoadOnDemand = grid
						.getAttribute(ig.grid.PROP_IS_LOAD_ON_DEMAND);
				if (!ig.isNull(virtualLoadOnDemand)) {
					if (virtualLoadOnDemand == ig.grid.LOAD_ON_DEMAND_PARTITIONED) {
						var partitionedRowCount = grid
								.getAttribute(ig.grid.PARTITIONED_ROW_COUNT);
						var singleRowHeight = grid.getSingleRowHeight();
						var currentVirtualPage = grid.getCurrentVirtualPage();
						var virtualPageCount = grid
								.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT);
						grid.setAttribute(ig.grid.PROP_LOD_MARKER_HEIGHT, 0);
						var lodMarker2 = document.getElementById("lodMarker");
						if (lodMarker2) {
							ig.deleteNode(lodMarker2);
						}
						var lodMarker1 = document.getElementById("lodMarker");
						if (lodMarker1) {
							ig.deleteNode(lodMarker1);
						}
						grid.getBody().getScrollingDivContent().setSize(null,
								partitionedRowCount * singleRowHeight);
						grid.getBody().getFixedDivContent().setSize(null,
								partitionedRowCount * singleRowHeight);
					}
				}
			};
			this.onGridSortingUpdate = function(httpReq) {
				var xmlDoc = httpReq.getResponseXml();
				if (!ig.isNull(xmlDoc)) {
					var results = xmlDoc.documentElement;
					if (!ig.isNull(results)) {
						var components = results
								.getElementsByTagName("component");
						for ( var i = 0; i < components.length; i++) {
							var component = components.item(i);
							var componentId = component.getAttribute("id");
							if (ig.getType(componentId) != ig.grid.TYPE_GRID)
								return;
							if (ig.NaES(componentId)) {
								var currentComponent = ig
										.getElementById(componentId);
								if (!ig.isNull(currentComponent)) {
									var content = component
											.getElementsByTagName("content")
											.item(0);
									var existingGrid = ig.grid.getGrid(document
											.getElementById(componentId));
									var contentAsHtml = (!ig
											.isNull(content.firstChild)) ? content.firstChild.nodeValue
											: "";
									var existingRowsParent = null;
									if (existingGrid.isScrolling()) {
										existingRowsParent = existingGrid
												.getBody()
												.getScrollingSection().elm.childNodes[0];
									} else {
										existingRowsParent = existingGrid
												.getBody().elm;
									}
									if (existingRowsParent
											&& existingRowsParent.hasChildNodes
											&& existingRowsParent.removeChild) {
										while (existingRowsParent
												.hasChildNodes()) {
											existingRowsParent
													.removeChild(existingRowsParent.firstChild);
										}
									}
									var fixedColumnsCount = existingGrid
											.getFrozenColCount();
									var fixedRowsParent = null;
									if (fixedColumnsCount > 0) {
										fixedRowsParent = existingGrid
												.getBody().getFixedSection().elm.childNodes[0];
										if (fixedRowsParent
												&& fixedRowsParent.hasChildNodes
												&& fixedRowsParent.removeChild) {
											while (fixedRowsParent
													.hasChildNodes()) {
												fixedRowsParent
														.removeChild(fixedRowsParent.firstChild);
											}
										}
									}
									var aDiv = document.createElement("div");
									document.body.appendChild(aDiv);
									aDiv.innerHTML = contentAsHtml;
									var newGrid = ig.findDescendant(aDiv, "id",
											componentId);
									var colWidths = ig.ui.getMaxColWidths(ig
											.findDescendant(newGrid, "id",
													componentId + "_g"));
									var newGridBody = ig.findDescendant(aDiv,
											"id", componentId + "_body");
									var newRows = newGridBody.rows;
									var rowSize = newRows.length;
									for ( var ridx = 0; ridx < rowSize; ridx++) {
										var tmpRow = newRows[ridx]
												.cloneNode(true);
										if (fixedColumnsCount > 0) {
											var fixedTr = ig
													.getUIElementById(document
															.createElement("TR"));
											var rheight = existingGrid
													.getSingleRowHeight();
											var tmpId = tmpRow.id;
											tmpRow.id = tmpId + "_sr";
											tmpRow.setAttribute("otype",
													"GridRowScroll");
											fixedTr.elm.id = tmpId;
											fixedTr.elm.className = tmpRow.className;
											fixedTr.elm.setAttribute("otype",
													"GridRow");
											ig.getUIElementById(tmpRow)
													.setSize(null, rheight);
											fixedTr.setSize(null, rheight);
											for ( var fi = 0; fi < fixedColumnsCount; fi++) {
												fixedTr.elm
														.appendChild(tmpRow.childNodes[0]
																.cloneNode(true));
												ig
														.deleteNode(tmpRow.childNodes[0]);
											}
											fixedRowsParent
													.appendChild(fixedTr.elm);
											existingRowsParent
													.appendChild(tmpRow);
										} else {
											existingRowsParent
													.appendChild(tmpRow);
										}
									}
									for ( var attr = 0; attr < newGrid.attributes.length; attr++) {
										if (newGrid.attributes[attr].nodeName != "style") {
											existingGrid.elm
													.setAttribute(
															newGrid.attributes[attr].nodeName,
															newGrid.attributes[attr].nodeValue);
										}
									}
									var columnHeaderId = componentId
											+ "_cheaders";
									var old_columnHeaderId = columnHeaderId;
									if (existingGrid.isScrolling()) {
										old_columnHeaderId = componentId
												+ "_cheaders_sr";
									}
									var oldColumnsHeader = ig.findDescendant(
											existingGrid.elm, "id",
											old_columnHeaderId);
									var newColumnsHeader = ig.findDescendant(
											newGrid, "id", columnHeaderId);
									if (fixedColumnsCount > 0) {
										var oldColumnsHeaderFixed = ig
												.findDescendant(
														existingGrid.elm, "id",
														columnHeaderId);
										while (oldColumnsHeaderFixed
												.hasChildNodes()) {
											oldColumnsHeaderFixed
													.removeChild(oldColumnsHeaderFixed.firstChild);
										}
										while (oldColumnsHeader.hasChildNodes()) {
											oldColumnsHeader
													.removeChild(oldColumnsHeader.firstChild);
										}
										for ( var i = 0; i < fixedColumnsCount; i++) {
											oldColumnsHeaderFixed
													.appendChild(newColumnsHeader.childNodes[0]
															.cloneNode(true));
											ig
													.deleteNode(newColumnsHeader.childNodes[0]);
										}
										ig.replaceNode(oldColumnsHeader,
												newColumnsHeader);
										newColumnsHeader.id = old_columnHeaderId;
										(new IgUIElement(oldColumnsHeaderFixed))
												.setSize(
														null,
														oldColumnsHeader.offsetHeight);
									} else {
										ig.replaceNode(oldColumnsHeader,
												newColumnsHeader);
										newColumnsHeader.id = old_columnHeaderId;
									}
									ig.deleteNode(aDiv);
									for ( var c = 0; c < colWidths.length; c++) {
										var w = colWidths[c];
										var aCol = existingGrid.getColumn(c);
										aCol.setWidth(w, true);
									}
									if (existingGrid.isScrolling()) {
										var scrollTopState = existingGrid
												.getAttribute(ig.grid.PROP_IG_SCROLL_TOP_STATE);
										if (!ig.isNull(scrollTopState)) {
											existingGrid.isScrollingGrid = true;
										}
										if (!ig.isNull(scrollTopState))
											existingGrid
													.setScrollTop(scrollTopState);
									}
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
										var id = aNode.id;
										var currentNode = null;
										if (ig.NaES(id)) {
											currentNode = document
													.getElementById(id);
										} else {
											var name = aNode.name;
											var elements = document
													.getElementsByName(name);
											if (elements.length == 1) {
												currentNode = elements[0];
											}
										}
										if (!ig.isNull(currentNode)) {
											ig.replaceNode(currentNode, aNode);
										}
									}
								}
								ig.deleteNode(parsedClientState);
							}
						}
					}
				}
			};
			this.onGridExpansionUpdate = function(httpReq, sourceOfRequest) {
				var xmlDoc = httpReq.getResponseXml();
				if (!ig.isNull(xmlDoc)) {
					var results = xmlDoc.documentElement;
					if (!ig.isNull(results)) {
						var components = results
								.getElementsByTagName("component");
						for ( var i = 0; i < components.length; i++) {
							var component = components.item(i);
							var componentId = component.getAttribute("id");
							if (ig.getType(componentId) != ig.grid.TYPE_GRID)
								return;
							if (ig.NaES(componentId)) {
								var currentComponent = ig
										.getElementById(componentId);
								if (!ig.isNull(currentComponent)) {
									var content = component
											.getElementsByTagName("content")
											.item(0);
									var existingGrid = ig.grid.getGrid(document
											.getElementById(componentId));
									var contentAsHtml = (!ig
											.isNull(content.firstChild)) ? content.firstChild.nodeValue
											: "";
									var existingRowsParent = null;
									if (existingGrid.isScrolling()) {
										existingRowsParent = existingGrid
												.getBody()
												.getScrollingSection().elm.childNodes[0];
									} else {
										existingRowsParent = existingGrid
												.getBody().elm;
									}
									var aDiv = document.createElement("div");
									document.body.appendChild(aDiv);
									aDiv.innerHTML = contentAsHtml;
									var newGrid = ig.findDescendant(aDiv, "id",
											componentId);
									var colWidths = ig.ui.getMaxColWidths(ig
											.findDescendant(newGrid, "id",
													componentId + "_g"));
									var newGridBody = ig.findDescendant(aDiv,
											"id", componentId + "_body");
									var newRows = newGridBody.rows;
									var existingRow;
									var newRow;
									if (existingGrid.isScrolling()
											&& existingGrid.getFrozenColCount() <= 0) {
										existingRow = ig.findDescendant(
												existingGrid.elm, "id",
												sourceOfRequest + "_sr");
										newRow = ig.findDescendant(newGrid,
												"id", sourceOfRequest);
										newRow.id = newRow.id + "_sr";
									} else {
										existingRow = ig.findDescendant(
												existingGrid.elm, "id",
												sourceOfRequest);
										newRow = ig.findDescendant(newGrid,
												"id", sourceOfRequest);
									}
									ig.replaceNode(existingRow, newRow);
									var nestedRow = ig.findDescendant(newGrid,
											"id", sourceOfRequest + "_cc");
									if (existingGrid.getFrozenColCount() > 0) {
										nestedRow.id = nestedRow.id + "_sr";
										var scrollingRow = ig.findDescendant(
												existingGrid.elm, "id",
												sourceOfRequest + "_sr");
										scrollingRow.parentNode.insertBefore(
												nestedRow,
												scrollingRow.nextSibling);
										var ncdcf = document
												.createElement("TD");
										ncdcf.colSpan = existingGrid
												.getFrozenColCount();
										var containerRow = document
												.createElement("TR");
										containerRow.id = newRow.id + "_cc";
										containerRow.appendChild(ncdcf);
										new IgUIElement(containerRow).setSize(
												null, nestedRow.offsetHeight);
										newRow.parentNode.insertBefore(
												containerRow,
												newRow.nextSibling);
									} else {
										newRow.parentNode.insertBefore(
												nestedRow, newRow.nextSibling);
									}
									ig.deleteNode(aDiv);
									for ( var c = 0; c < colWidths.length; c++) {
										var w = colWidths[c];
										var aCol = existingGrid.getColumn(c);
										aCol.setWidth(w);
									}
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
										var id = aNode.id;
										var currentNode = null;
										if (ig.NaES(id)) {
											currentNode = document
													.getElementById(id);
										} else {
											var name = aNode.name;
											var elements = document
													.getElementsByName(name);
											if (elements.length == 1) {
												currentNode = elements[0];
											}
										}
										if (!ig.isNull(currentNode)) {
											ig.replaceNode(currentNode, aNode);
										}
									}
								}
								ig.deleteNode(parsedClientState);
							}
						}
					}
				}
			};
			this.onGridPagingUpdate = function(httpReq) {
				var xmlDoc = httpReq.getResponseXml();
				if (!ig.isNull(xmlDoc)) {
					var results = xmlDoc.documentElement;
					if (!ig.isNull(results)) {
						var components = results
								.getElementsByTagName("component");
						for ( var i = 0; i < components.length; i++) {
							var component = components.item(i);
							var componentId = component.getAttribute("id");
							if (ig.getType(componentId) != ig.grid.TYPE_GRID)
								return;
							if (ig.NaES(componentId)) {
								var currentComponent = ig
										.getElementById(componentId);
								if (!ig.isNull(currentComponent)) {
									var content = component
											.getElementsByTagName("content")
											.item(0);
									var existingGrid = ig.grid.getGrid(document
											.getElementById(componentId));
									var contentAsHtml = (!ig
											.isNull(content.firstChild)) ? content.firstChild.nodeValue
											: "";
									var existingRowsParent = null;
									if (existingGrid.isScrolling()) {
										existingRowsParent = existingGrid
												.getBody()
												.getScrollingSection().elm.childNodes[0];
									} else {
										existingRowsParent = existingGrid
												.getBody().elm;
									}
									if (existingRowsParent
											&& existingRowsParent.hasChildNodes
											&& existingRowsParent.removeChild) {
										while (existingRowsParent
												.hasChildNodes()) {
											existingRowsParent
													.removeChild(existingRowsParent.firstChild);
										}
									}
									var fixedColumnsCount = existingGrid
											.getFrozenColCount();
									var isScrolling = existingGrid
											.isScrolling();
									var fixedRowsParent = null;
									if (isScrolling) {
										fixedRowsParent = existingGrid
												.getBody().getFixedSection().elm.childNodes[0];
										if (fixedRowsParent
												&& fixedRowsParent.hasChildNodes
												&& fixedRowsParent.removeChild) {
											while (fixedRowsParent
													.hasChildNodes()) {
												fixedRowsParent
														.removeChild(fixedRowsParent.firstChild);
											}
										}
									}
									var aDiv = document.createElement("div");
									document.body.appendChild(aDiv);
									aDiv.innerHTML = contentAsHtml;
									var newGrid = ig.findDescendant(aDiv, "id",
											componentId);
									var colWidths = ig.ui.getMaxColWidths(ig
											.findDescendant(newGrid, "id",
													componentId + "_g"));
									var newGridBody = ig.findDescendant(aDiv,
											"id", componentId + "_body");
									var newRows = newGridBody.rows;
									var pagerId = componentId + ":topPager";
									var newPager = ig.findDescendant(aDiv,
											"id", pagerId);
									if (newPager) {
										var oldPager = ig
												.findDescendant(
														existingGrid.elm, "id",
														pagerId);
										ig.replaceNode(oldPager, newPager);
									}
									var bottomPagerId = componentId
											+ ":bottomPager";
									var newBottomPager = ig.findDescendant(
											aDiv, "id", bottomPagerId);
									if (newBottomPager) {
										var oldBottomPager = ig.findDescendant(
												existingGrid.elm, "id",
												bottomPagerId);
										ig.replaceNode(oldBottomPager,
												newBottomPager);
									}
									var rowSize = newRows.length;
									var rheight = existingGrid
											.getSingleRowHeight();
									for ( var ridx = 0; ridx < rowSize; ridx++) {
										var tmpRow = newRows[ridx]
												.cloneNode(true);
										if (isScrolling) {
											var fixedTr = ig
													.getUIElementById(document
															.createElement("TR"));
											var tmpId = tmpRow.id;
											tmpRow.id = tmpId + "_sr";
											tmpRow.setAttribute("otype",
													"GridRowScroll");
											fixedTr.elm.id = tmpId;
											fixedTr.elm.className = tmpRow.className;
											fixedTr.elm.setAttribute("otype",
													"GridRow");
											for ( var fi = 0; fi < fixedColumnsCount; fi++) {
												fixedTr.elm
														.appendChild(tmpRow.childNodes[0]
																.cloneNode(true));
												ig
														.deleteNode(tmpRow.childNodes[0]);
											}
											fixedRowsParent
													.appendChild(fixedTr.elm);
											existingRowsParent
													.appendChild(tmpRow);
										} else {
											existingRowsParent
													.appendChild(tmpRow);
										}
									}
									for ( var attr = 0; attr < newGrid.attributes.length; attr++) {
										if (newGrid.attributes[attr].nodeName != "style") {
											existingGrid.elm
													.setAttribute(
															newGrid.attributes[attr].nodeName,
															newGrid.attributes[attr].nodeValue);
										}
									}
									ig.deleteNode(aDiv);
									var existingColWidths = existingGrid
											.getColumnWidthsAsArray();
									for ( var c = 0; c < colWidths.length; c++) {
										var w = colWidths[c];
										var aCol = existingGrid.getColumn(c);
										if (!aCol.isResizable()) {
											aCol.setWidth(w);
										} else {
											aCol.setWidth(existingColWidths[c]);
										}
									}
									ig.grid
											.initLoadOnDemandPartitioned(existingGrid);
									if (existingGrid.isScrolling()) {
										var scrollTopState = existingGrid
												.getAttribute(ig.grid.PROP_IG_SCROLL_TOP_STATE);
										if (!ig.isNull(scrollTopState)) {
											existingGrid.isScrollingGrid = true;
										}
										if (!ig.isNull(scrollTopState))
											existingGrid
													.setScrollTop(scrollTopState);
									}
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
										var id = aNode.id;
										var currentNode = null;
										if (ig.NaES(id)) {
											currentNode = document
													.getElementById(id);
										} else {
											var name = aNode.name;
											var elements = document
													.getElementsByName(name);
											if (elements.length == 1) {
												currentNode = elements[0];
											}
										}
										if (!ig.isNull(currentNode)) {
											ig.replaceNode(currentNode, aNode);
										}
									}
								}
								ig.deleteNode(parsedClientState);
							}
						}
					}
				}
			};
			this.getTargetRow = function(domNode) {
				while (true) {
					var jsNode = ig.getUIElementById(domNode);
					if (jsNode != null) {
						var typeAttrValue = jsNode.getAttribute(ig.PROP_TYPE);
						if (typeAttrValue == this.TYPE_GRID_ROW
								|| typeAttrValue == this.TYPE_GRID_ROW_SCROLL) {
							return jsNode;
						}
					}
					domNode = domNode.parentNode;
					if (!domNode)
						return null;
				}
			};
			this.getTargetCell = function(domNode) {
				return ig.getTargetUIElement(domNode, this.TYPE_GRID_CELL);
			};
			this.getCell = function(domNode) {
				return ig.getTargetUIElement(domNode, this.TYPE_GRID_CELL);
			};
			this._registerEventListener = function(obj, evntName, listener) {
				if (obj._internalEventListeners == null)
					obj._internalEventListeners = {};
				if (obj._internalEventListeners[evntName] == null)
					obj._internalEventListeners[evntName] = [];
				obj._internalEventListeners[evntName].push(listener);
			};
			this._fireEvent = function(obj, evntName, args) {
				if (obj._internalEventListeners == null)
					return;
				var listeners = obj._internalEventListeners[evntName];
				if (listeners != null && listeners.length > 0) {
					for ( var i = 0; i < listeners.length; i++)
						listeners[i](args);
				}
			};
			this._unregisterEventListener = function(obj, evntName, listener) {
				if (obj._internalEventListeners == null)
					return;
				var listeners = obj._internalEventListeners[evntName];
				if (listeners != null && listeners.length > 0) {
					for ( var i = 0; i < listeners.length; i++) {
						if (listeners[i] == listener) {
							delete obj._internalEventListeners[evntName][i];
							return;
						}
					}
				}
			};
			this.onBeforeSubmit = function(evt) {
				for ( var i in ig.grid.instances) {
					var grid = ig.grid.instances[i];
					if (grid.onBeforeSubmit != null) {
						grid.onBeforeSubmit(evt);
					}
				}
				return true;
			};
			this.lastGridIngEvent = null;
			this.lastGridView = null;
			this.lastArg0Value = null;
			this.lastArg1Value = null;
			this.raiseGridIngClientEvent = function(gridId, eventType, arg0,
					arg1, toEvaluate) {
				var theGrid = ig.getUIElementById(gridId);
				ig.grid.lastGridView = theGrid;
				ig.grid.lastGridIngEvent = eventType;
				if (eventType == "pageChange") {
					ig.grid.lastArg0Value = arg0;
					ig.grid.lastArg1Value = arg1;
					var args = theGrid._raiseClientEvent("PageChanging",
							"PageChange", null, null, arg0, arg1);
					if (args != null && args.get_cancel())
						return;
					eval(toEvaluate);
				} else if (eventType == "ColumnSort") {
					ig.grid.lastArg0Value = arg0;
					var args = theGrid._raiseClientEvent("ColumnSorting",
							"ColumnSort", arg0);
					if (args != null && args.get_cancel())
						return;
					eval(toEvaluate);
				} else if (eventType == "columnFix") {
					ig.grid.lastArg0Value = arg0;
					ig.grid.lastArg1Value = arg1;
					var args = theGrid._raiseClientEvent("ColumnFixing",
							"GridColumnFix", arg0, arg1);
					if (args != null && args.get_cancel())
						return;
					eval(toEvaluate);
				}
			};
			this.raiseGridEdClientEvent = function(httpReq) {
				ig.onPartialRefreshDefault(httpReq);
				if (ig.grid.lastGridIngEvent == "pageChange") {
					ig.grid.lastGridView._raiseClientEvent("PageChanged",
							"PageChange", null, null, ig.grid.lastArg0Value,
							ig.grid.lastArg1Value);
				} else if (ig.grid.lastGridIngEvent == "ColumnSort") {
					ig.grid.lastGridView._raiseClientEvent("ColumnSorted",
							"ColumnSort", ig.grid.lastArg0Value);
				} else if (ig.grid.lastGridIngEvent == "columnFix") {
					ig.grid.lastGridView._raiseClientEvent("ColumnFixed",
							"GridColumnFix", ig.grid.lastArg0Value,
							ig.grid.lastArg1Value);
				}
				ig.grid.lastGridIngEvent = null;
				ig.grid.lastGrid = null;
				ig.grid.lastArg0Value = null;
				ig.grid.lastArg1Value = null;
			};
		}
		ig.grid = new IgWebGridPackage();
		ig.grid.instances = [];
		ig.grid.instancesIds = [];
		ig.grid.formsWithGrid = [];
		ig.grid.initTimeoutManagers = [];
		ig.grid.lastLodGrid = null;
		ig.grid.lastLodGridStartRow = 0;
		ig.grid.lastLodGridFetchSize = 0;
		function IgGrid(e) {
			var id = e.id;
			id = id.replace(/:/, "_");
			if (!ig.isNull(ig.grid.instances[id])) {
				this._behaviors = ig.grid.instances[id].get_behaviors();
			}
			ig.grid.instances[id] = this;
			this.IgUIComponent(e);
			this._gridUtil = ig.grid;
		}
		;
		IgGrid.prototype.get_behaviors = function() {
			if (this._behaviors == null)
				this._behaviors = new $IG.GridBehaviorCollection(this);
			return this._behaviors;
		}
		IgGrid.prototype.createEmptyTable = function(id, css) {
			var t = document.createElement("table");
			if (ig.NaES(id)) {
				t.id = id;
			}
			if (ig.NaES(css)) {
				t.style.cssText = css;
			}
			t.border = "0";
			t.cellPadding = "0";
			t.cellSpacing = "0";
			return t;
		};
		IgGrid.prototype.getColCount = function() {
			return ig.toFloat(this.getAttribute("igColCount"));
		};
		IgGrid.prototype.getColumn = function(i) {
			return new IgGridColumn(this, i);
		};
		IgGrid.prototype.getColWidths = function() {
			return ig.ui.getMaxColWidths(this.getBody());
		};
		IgGrid.prototype.getFrozenColCount = function() {
			return ig.toFloat(this.getAttribute("igFrozenColCount"));
		};
		IgGrid.prototype.hasRowSelection = function() {
			if (ig.isNull(this.iHasRowSelection)) {
				var rowSelector = ig.findDescendant(this.elm, ig.PROP_FLAG,
						ig.grid.TYPE_GRID_ROW_SELECTOR);
				this.iHasRowSelection = !ig.isNull(rowSelector);
			}
			return this.iHasRowSelection;
		};
		IgGrid.prototype.getFrozenColumnsWidth = function() {
			var cc = this.getFrozenColCount();
			var w = 0;
			for ( var c = 0; c < cc; c++) {
				var col = this.getColumn(c);
				w += col.getWidth();
			}
			return w;
		};
		IgGrid.prototype.getGrid = function() {
			return ig.getUIElementById(this.elm.id + "_g");
		};
		IgGrid.prototype.getGridContainer = function() {
			return ig.getUIElementById(this.elm.id);
		};
		IgGrid.prototype.getGridTopPager = function() {
			return ig.getUIElementById(this.elm.id + ":topPager");
		};
		IgGrid.prototype.getBody = function() {
			return this.getSection(this.elm.id + "_body", this);
		};
		IgGrid.prototype.getColumnsWidth = function() {
			var fcw = this.getFrozenColumnsWidth();
			var scw = this.getScrollingColumnsWidth();
			return fcw + scw;
		};
		IgGrid.prototype.getColumnWidthsAsArray = function() {
			var r = this.elm["igArrColWidths"];
			if (!ig.isArray(r)) {
				r = new Array();
				this.elm["igArrColWidths"] = r;
			}
			return r;
		};
		IgGrid.prototype.getColumnsFooter = function() {
			return this.getSection(this.elm.id + "_footer", this);
		};
		IgGrid.prototype.getColumnsHeader = function() {
			return this.getSection(this.elm.id + "_header", this);
		};
		IgGrid.prototype.getGridFooter = function() {
			return this.getSection(this.elm.id + "_gfooter", this);
		};
		IgGrid.prototype.getGridHeader = function() {
			return this.getSection(this.elm.id + "_gheader", this);
		};
		IgGrid.prototype.getScrollingGridRowCount = function() {
			return this.scrollingGridRowCount;
		};
		IgGrid.prototype.setScrollingGridRowCount = function(count) {
			this.scrollingGridRowCount = count;
		};
		IgGrid.prototype.getRowCount = function() {
			var rc = 0;
			var sec = this.getColumnsHeader();
			if (!ig.isNull(sec)) {
				rc += sec.getRowCount();
			}
			sec = this.getBody();
			if (!ig.isNull(sec)) {
				rc += sec.getRowCount();
			}
			sec = this.getColumnsFooter();
			if (!ig.isNull(sec)) {
				rc += sec.getRowCount();
			}
			return rc;
		};
		IgGrid.prototype.getRows = function() {
			var b = this.getBody();
			if (!ig.isNull(b)) {
				if (this.isScrolling()) {
					var f = b.getFixedSection();
					return f.elm.rows;
				} else {
					return b.elm.rows;
				}
			} else {
				return null;
			}
		};
		IgGrid.prototype.getSingleRowHeight = function() {
			return this.getAttribute(ig.grid.PROP_SINGLE_ROW_HEIGHT);
		};
		IgGrid.prototype.setSingleRowHeight = function(height) {
			this.setAttribute(ig.grid.PROP_SINGLE_ROW_HEIGHT, height);
		};
		IgGrid.prototype.getScrollingColumnsWidth = function() {
			var cc = this.getColCount();
			var fcc = this.getFrozenColCount();
			var w = 0;
			for ( var c = fcc; c < cc; c++) {
				var col = this.getColumn(c);
				w += col.getWidth();
			}
			return w;
		};
		IgGrid.prototype.getVisibleScrollingColumnsWidth = function() {
			var gs = this.getGrid().getSize();
			var result = gs.width - this.getGrid().getBorder("width")
					- this.getGrid().getPadding("width")
					- this.getFrozenColumnsWidth();
			return result;
		};
		IgGrid.prototype.getSection = function(id, grid) {
			var r = null;
			if (!ig.isNull(id)) {
				id = ig.getElementById(id);
				if (!ig.isNull(id)) {
					r = new IgGridSection(id, grid);
				}
			}
			return r;
		};
		IgGrid.prototype.hasResizableColumn = function() {
			var r = false;
			var nbCols = this.getColCount();
			for ( var ic = 0; ic < nbCols; ic++) {
				var aCol = this.getColumn(ic);
				if (!ig.isNull(aCol)) {
					if (aCol.isResizable()) {
						r = true;
						break;
					}
				}
			}
			return r;
		};
		IgGrid.prototype.onScrollEndDefault = function() {
			var grid = ig.grid.getGrid(this);
			var body = grid.getBody();
			if (!ig.isNull(body)) {
				var bodyScrollDiv = body.getScrollingDiv();
				var scrollTop = bodyScrollDiv.elm.scrollTop;
				if (scrollTop + bodyScrollDiv.elm.clientHeight > bodyScrollDiv.elm.scrollHeight
						- ig.grid.PROP_IG_LOAD_ON_DEMAND_THRESHOLD
						* bodyScrollDiv.elm.clientHeight) {
					var id = this.elm.id;
					id = id.replace(/:/, "_");
					clearInterval(ig.grid.instancesIds[id]);
					this.setAttribute("onScrollMonitor", false);
					var needsScrolling = this
							.getAttribute(ig.grid.PROP_IG_NEEDS_SCROLLING);
					if (!ig.isNull(needsScrolling)
							&& parseInt(needsScrolling) < 0)
						return;
					var pageRowCount = parseInt(this
							.getAttribute(ig.grid.PARTITIONED_ROW_COUNT));
					var currentRowCount = parseInt(this
							.getAttribute(ig.grid.PROP_IG_CURRENT_ROW_COUNT));
					var rowFetchSize = parseInt(this
							.getAttribute(ig.grid.PROP_IG_ROW_FETCH_SIZE));
					if (currentRowCount == pageRowCount)
						return;
					var startRowIndex = currentRowCount;
					var endRowIndex = 0;
					if ((pageRowCount - currentRowCount) < rowFetchSize)
						endRowIndex = pageRowCount - 1;
					else
						endRowIndex = currentRowCount + rowFetchSize - 1;
					var args = this._raiseClientEvent("MoreRowsRequesting",
							"CancelMoreRowsRequesting", null, null,
							startRowIndex, rowFetchSize);
					if (args != null && args.get_cancel())
						return;
					ig.grid.lastLodGrid = this;
					ig.grid.lastLodGridStartRow = startRowIndex;
					ig.grid.lastLodGridFetchSize = rowFetchSize;
					var loadingDiv = document.getElementById(this.elm.id
							+ "_loadingDiv");
					var loadingImage = loadingDiv.childNodes[0];
					loadingDiv.style.left = this.getBody().getSize().width / 2
							- parseInt(loadingImage.width / 2) + "px";
					loadingDiv.style.top = -this.getBody().getSize().height / 2
							- parseInt(loadingImage.height / 2) + "px";
					loadingDiv.style.visibility = "visible";
					this.scrollTopState = bodyScrollDiv.elm.scrollTop;
					ig.smartSubmit(this.getId(), "scrollingLoadOnDemand",
							"rowRange:" + startRowIndex + " " + endRowIndex
									+ " " + this.scrollTopState, this.getId(),
							this.onLoadOnDemand);
				}
			}
		};
		IgGrid.prototype.onScrollEnd = function() {
			var grid = ig.grid.getGrid(this);
			var doFetch = false;
			if (!ig.isNull(grid) && !ig.isNull(grid.getBody())) {
				var currentScrollTop = grid.getBody().getScrollingDiv().elm.scrollTop;
				var previousScrollTop = this
						.getAttribute(ig.grid.PROP_PREVIOUS_SCROLL_TOP);
				if (!ig.isNull(previousScrollTop)
						&& !ig.isNull(currentScrollTop)
						&& currentScrollTop == previousScrollTop) {
					doFetch = true;
				}
				previousScrollTop = currentScrollTop;
				this.setAttribute(ig.grid.PROP_PREVIOUS_SCROLL_TOP,
						previousScrollTop);
			}
			var virtualPageIndex = document.getElementById(this.elm.id
					+ "_virtualPageIndex");
			if (!ig.isNull(virtualPageIndex))
				virtualPageIndex.style.visibility = "hidden";
			if (doFetch) {
				var id = this.elm.id;
				id = id.replace(/:/, "_");
				clearInterval(ig.grid.instancesIds[id]);
				this.setAttribute("onScrollMonitor", false);
				var body = grid.getBody();
				if (!ig.isNull(body)) {
					var bodyScrollDiv = body.getScrollingDiv();
					var oldVirtualPage = parseInt(grid
							.getAttribute(ig.grid.PROP_CURRENT_VIRTUAL_PAGE));
					var singleRowHeight = grid.getSingleRowHeight();
					var scrollTop = parseInt(bodyScrollDiv.elm.scrollTop);
					var realContentHeight = 0;
					var lodMarkerHeight = parseInt(this
							.getAttribute(ig.grid.PROP_LOD_MARKER_HEIGHT));
					var fetchSize = parseInt(this
							.getAttribute(ig.grid.PROP_IG_ROW_FETCH_SIZE));
					var allHeight = fetchSize * singleRowHeight;
					var currentlyAt = scrollTop / singleRowHeight;
					grid.scrollTopState = bodyScrollDiv.elm.scrollTop;
					var currentVirtualPage = 0;
					var remainderCount = null;
					if (grid.getAttribute(ig.grid.PROP_REMAINDER_COUNT) != null)
						remainderCount = parseInt(grid
								.getAttribute(ig.grid.PROP_REMAINDER_COUNT));
					currentVirtualPage = parseInt(currentlyAt / fetchSize);
					if (!ig.isNull(remainderCount)) {
						if (parseInt((currentlyAt - remainderCount) / fetchSize) != oldVirtualPage) {
							currentVirtualPage = parseInt((currentlyAt - remainderCount)
									/ fetchSize);
						}
					}
					if (currentVirtualPage + 1 > grid
							.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT))
						return;
					if ((currentVirtualPage - oldVirtualPage == 1 || currentVirtualPage
							- oldVirtualPage == -1)
							&& oldVirtualPage != null) {
						currentVirtualPage = oldVirtualPage;
					}
					currentVirtualPage = oldVirtualPage;
					var finishedDown = false;
					var finishedUp = false;
					if (ig.isNull(oldVirtualPage))
						oldVirtualPage = 0;
					if (scrollTop > lodMarkerHeight) {
						var contentHeight = 0;
						if (this.getBody().getScrollingDivContent().elm.childNodes.length == 1)
							contentHeight = this.getBody()
									.getScrollingDivContent().elm.firstChild.offsetHeight;
						else
							contentHeight = this.getBody()
									.getScrollingDivContent().elm.childNodes[1].offsetHeight;
						var lodPlusBody = lodMarkerHeight + contentHeight;
						if (lodPlusBody - scrollTop < this.getBody().getSize().height / 2
								&& lodPlusBody - scrollTop > 0) {
							currentVirtualPage = oldVirtualPage + 1;
							var finishedDown = true;
							grid.scrollTopState = currentVirtualPage
									* fetchSize * singleRowHeight;
						} else if (scrollTop > lodPlusBody) {
							currentVirtualPage = parseInt(currentlyAt
									/ fetchSize);
							if (!ig.isNull(remainderCount)) {
								if (parseInt((currentlyAt - remainderCount)
										/ fetchSize) != oldVirtualPage) {
									currentVirtualPage = parseInt((currentlyAt - remainderCount)
											/ fetchSize);
								}
							}
						}
					} else if (!finishedDown
							&& lodMarkerHeight - scrollTop > this.getBody()
									.getSize().height / 2
							&& lodMarkerHeight - scrollTop < this.getBody()
									.getSize().height
							&& scrollTop != lodMarkerHeight) {
						var bodyHeight = this.getBody().getSize().height;
						currentVirtualPage = oldVirtualPage - 1;
						grid.scrollTopState = currentVirtualPage * fetchSize
								* singleRowHeight;
						finishedUp = true;
					} else if (scrollTop < lodMarkerHeight) {
						currentVirtualPage = parseInt(currentlyAt / fetchSize);
						if (currentVirtualPage == oldVirtualPage
								&& lodMarkerHeight - scrollTop > this.getBody()
										.getSize().height)
							currentVirtualPage--;
						grid.scrollTopState = currentVirtualPage * fetchSize
								* singleRowHeight;
					} else if (scrollTop == 0) {
						currentVirtualPage = 0;
					}
					if (currentVirtualPage != oldVirtualPage
							&& currentVirtualPage >= 0) {
						var loadingDiv = document.getElementById(this.elm.id
								+ "_loadingDiv");
						loadingDiv.style.visibility = "visible";
						var startRowIndex = parseInt(currentVirtualPage
								* fetchSize);
						var args = grid._raiseClientEvent("MoreRowsRequesting",
								"CancelMoreRowsRequesting", null, null,
								startRowIndex, fetchSize);
						if (args != null && args.get_cancel())
							return;
						ig.grid.lastLodGrid = grid;
						ig.grid.lastLodGridStartRow = startRowIndex;
						ig.grid.lastLodGridFetchSize = fetchSize;
						var loadingImage = loadingDiv.childNodes[0];
						loadingDiv.style.left = this.getBody().getSize().width
								/ 2 - parseInt(loadingImage.width / 2) + "px";
						loadingDiv.style.top = -this.getBody().getSize().height
								/ 2 - parseInt(loadingImage.height / 2) + "px";
						var endRowIndex = startRowIndex + fetchSize - 1;
						ig.smartSubmit(grid.getId(), "scrollingLoadOnDemand",
								"rowRange:" + startRowIndex + " " + endRowIndex
										+ " " + grid.scrollTopState + " "
										+ currentVirtualPage, grid.getId(),
								null);
						grid.setAttribute(ig.grid.PROP_CURRENT_VIRTUAL_PAGE, ""
								+ currentVirtualPage);
					}
				}
			}
		};
		IgGrid.prototype.init = function(clientSideListeners) {
			var scrollTopState = this
					.getAttribute(ig.grid.PROP_IG_SCROLL_TOP_STATE);
			if (!ig.isNull(scrollTopState) && scrollTopState > 0) {
				this.isScrollingGrid = true;
			}
			this.initScrollingGrid();
			this._initializeObjects();
			if (!ig.isNull(scrollTopState) && scrollTopState > 0)
				this.setScrollTop(scrollTopState);
			this._initClientEventsForObject(this.elm, clientSideListeners);
		};
		IgGrid.prototype.initScrollingGrid = function() {
			if (this.needScrollingGrid() || this.isScrollingGrid
					|| this.isForcedVerticalScroll) {
				var fc = this.getFrozenColCount();
				var cc = this.getColCount();
				var fixedGrid = this.getGrid();
				var cws = ig.ui.getMaxColWidths(fixedGrid.elm);
				if (ig.isArray(cws)) {
					this.elm.__gcw = 0;
					for ( var i = 0; i < cws.length; i++) {
						this.elm.__gcw += cws[i];
					}
					var fixedGridSize = fixedGrid.getSize();
					fixedGridSize.width = this.elm.__gcw;
					var originalUserWidth = this.getStyleInline("width");
					var originalUserHeight = this.getStyleInline("height");
					this.elm.__widthUsesPercentages = false;
					this.elm.__heightUsesPercentages = false;
					if (originalUserWidth.indexOf("%", 0) != -1) {
						this.elm.__widthUsesPercentages = true;
					}
					if (originalUserHeight.indexOf("%", 0) != -1) {
						this.elm.__heightUsesPercentages = true;
					}
					var udw = ig.toFloat(this.getStyleInline("width"));
					this.elm.__userWidth = udw;
					this.elm.__fixedWidth = fixedGridSize.width;
					var udh = ig.toFloat(this.getStyleInline("height"));
					this.elm.__userHeight = udh;
					var scrollableGrid = this.toScrollingGrid(fixedGrid.elm,
							cc, fc, cws, this.elm.__gcw);
					var parentNode = fixedGrid.elm.parentNode;
					var ns = fixedGrid.elm.nextSibling;
					parentNode.removeChild(fixedGrid.elm);
					parentNode.insertBefore(scrollableGrid.elm, ns);
					this.setColumnWidthsAsArray(cws);
					for ( var c = 0; c < cws.length; c++) {
						var w = cws[c];
						var aCol = this.getColumn(c);
						aCol.setWidth(w, true);
					}
					var gw = fixedGridSize.width;
					var gh = fixedGridSize.height;
					this.elm.__gh = gh;
					if (ig.isBrowser('msie', '6')) {
						ig.grid.initTimeoutManagers[this.elm.id] = setInterval(
								'ig.grid.__finishInitScrollingGrid(\"'
										+ this.elm.id + '\");', 1000);
					} else {
						this.__finishInitScrollingGrid();
					}
				}
			}
		};
		IgGrid.prototype.__finishInitScrollingGrid = function() {
			var gw = this.elm.__fixedWidth;
			var udw = this.elm.__userWidth;
			var udh = this.elm.__userHeight;
			var gh = this.elm.__gh;
			var needsMods = false;
			if (ig.isIE && (this.getCompatMode() != ig.ui.COMPAT_BACK)) {
				var accumulatedHeight = 0;
				var singleRowHeight = this.getSingleRowHeight();
				var scrollingRowsParent = this.getBody().getScrollingSection().elm.childNodes[0];
				var fixedColumnsCount = this.getFrozenColCount();
				var fixedRowsParent = null;
				var paddingTop = 0;
				var paddingBottom = 0;
				if (scrollingRowsParent.childNodes.length > 0) {
					paddingTop = scrollingRowsParent.childNodes[0].childNodes[0].currentStyle['paddingTop'];
					paddingBottom = scrollingRowsParent.childNodes[0].childNodes[0].currentStyle['paddingBottom'];
				}
				singleRowHeight -= parseInt(paddingTop)
						+ parseInt(paddingBottom);
				var cheader = this.getColumnsHeader();
				if (cheader) {
					var rows = cheader.elm.rows;
					ig.getUIElementById(rows[0]).setSize(null, singleRowHeight);
				}
				if (fixedColumnsCount > 0) {
					fixedRowsParent = this.getBody().getFixedSection().elm.childNodes[0];
					for ( var i = 0; i < fixedRowsParent.childNodes.length; i++) {
						if (fixedRowsParent.childNodes[i].id.indexOf("_cc") == -1) {
							fixedRowsParent.childNodes[i].style.height = singleRowHeight;
						} else {
							fixedRowsParent.childNodes[i].style.height = fixedRowsParent.childNodes[i].offsetHeight;
						}
					}
				}
				for ( var i = 0; i < scrollingRowsParent.childNodes.length; i++) {
					scrollingRowsParent.childNodes[i].style.height = singleRowHeight;
				}
				if (this.getFrozenColCount() > 0) {
				} else {
					var grid = this.getGrid();
					var height = grid.getHeight();
					gh = height;
				}
			}
			var addScrollWidthToBody = false;
			if ((udw == 0 && (udh > 0 && udh <= gh))
					|| this.getGrid().isForcedVerticalScroll) {
				if (!ig.isIE) {
					this.getGridContainer().setStyle("width",
							gw + this.getScrollBarWidth() + 2 + "px");
				} else {
					this.getGridContainer().setStyle("width",
							gw + this.getScrollBarWidth() + "px");
				}
			} else if (udw == 0 && (udh > 0 && udh > gh)) {
				this.getGridContainer().setStyle("width", gw + "px");
			}
			if (udh == 0 && (udw > 0 && udw < gw)) {
				if (!ig.isIE) {
					var header = document.getElementById(this.elm.id
							+ "_cheaders");
					if (header != null)
						this.getGridContainer().setStyle(
								"height",
								gh + this.getScrollBarWidth()
										+ header.offsetHeight - 5 + "px");
					else
						this.getGridContainer().setStyle("height",
								gh + this.getScrollBarWidth() + "px");
					addScrollWidthToBody = true;
				} else {
					addScrollWidthToBody = true;
				}
			} else if (udh == 0 && (udw > 0 && udw > gw)) {
				this.getGridContainer().setStyle("height", gh + "px");
				this.setStyle("height", gh + "px");
			}
			if (udw > 0 && !this.elm.__widthUsesPercentages) {
				gw = udw;
			}
			if (udh > 0 && !this.elm.__heightUsesPercentages) {
				gh = udh;
			}
			if (udh > 0 && udh - this.getScrollBarWidth() > 0) {
				var topPager = document.getElementById(this.elm.id
						+ ":topPager");
				if (topPager) {
					var pagerHeight = topPager.offsetHeight;
					if (ig.isIE) {
						if (parseInt(topPager.currentStyle.marginBottom)) {
							pagerHeight += parseInt(topPager.currentStyle.marginBottom);
						}
						if (parseInt(topPager.currentStyle.marginTop)) {
							pagerHeight += parseInt(topPager.currentStyle.marginTop);
						}
					}
					var height = gh + pagerHeight;
					this.getGridContainer().setStyle('height', height + "px");
				} else {
					this.getGridContainer().setSize(null, gh);
				}
				var bottomPager = document.getElementById(this.elm.id
						+ ":bottomPager");
				if (bottomPager) {
					var pagerHeight = bottomPager.offsetHeight;
					if (ig.isIE) {
						if (parseInt(bottomPager.currentStyle.marginBottom)) {
							pagerHeight += parseInt(bottomPager.currentStyle.marginBottom);
						}
						if (parseInt(bottomPager.currentStyle.marginTop)) {
							pagerHeight += parseInt(bottomPager.currentStyle.marginTop);
						}
					}
					var height = gh + pagerHeight;
					this.getGridContainer().setStyle('height', height + "px");
				} else {
					this.getGridContainer().setSize(null, gh);
				}
			}
			if (this.isForcedVerticalScroll) {
				var brCorrect = ig.isIE ? 0 : 3;
				gw = gw + this.getScrollBarWidth() + brCorrect;
			}
			if (udh == 0 && udw == 0) {
				this.setStyle("width", gw + "px");
			}
			var newWidth = this.elm.__gcw;
			var newHeight = gh;
			if (udh == 0) {
				newHeight = null;
			}
			if (udw == 0) {
				newWidth = null;
			}
			this.setSize(newWidth, newHeight, addScrollWidthToBody);
			this.__fixRowSizes();
			var virtualLoadOnDemand = this
					.getAttribute(ig.grid.PROP_IS_LOAD_ON_DEMAND);
			if (!ig.isNull(virtualLoadOnDemand)) {
				function findPos(obj) {
					var curleft = curtop = 0;
					if (obj.offsetParent) {
						curleft = obj.offsetLeft;
						curtop = obj.offsetTop;
						while (obj = obj.offsetParent) {
							curleft += obj.offsetLeft;
							curtop += obj.offsetTop;
						}
					}
					return [ curleft, curtop ];
				}
				;
				var pos = findPos(this.getBody().elm);
				this.setAttribute("onScrollMonitor", false);
				if (virtualLoadOnDemand == ig.grid.LOAD_ON_DEMAND_PARTITIONED) {
					var partitionedRowCount = this
							.getAttribute(ig.grid.PARTITIONED_ROW_COUNT);
					var singleRowHeight = this.getSingleRowHeight();
					var currentVirtualPage = this.getCurrentVirtualPage();
					var virtualPageCount = this
							.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT);
					this.getBody().getScrollingDivContent().setSize(null,
							partitionedRowCount * singleRowHeight);
					this.getBody().getFixedDivContent().setSize(null,
							partitionedRowCount * singleRowHeight);
					var needLodMarker = false;
					var scrollingContent = this.getBody()
							.getScrollingDivContent();
					var fixedContent = this.getBody().getFixedDivContent();
					var fetchSize = this
							.getAttribute(ig.grid.PROP_IG_ROW_FETCH_SIZE);
					if (!ig.isNull(currentVirtualPage)
							&& currentVirtualPage != 0) {
						needLodMarker = true;
					}
					this.setAttribute(ig.grid.PROP_LOD_MARKER_HEIGHT, 0);
					if (needLodMarker) {
						lodMarker = document.createElement("div");
						lodMarker.id = "lodMarker";
						var lodMarkerHeight = currentVirtualPage * fetchSize
								* singleRowHeight;
						lodMarkerHeight = this
								.getAttribute(ig.grid.PROP_IG_SCROLL_TOP_STATE);
						this.setAttribute(ig.grid.PROP_LOD_MARKER_HEIGHT,
								lodMarkerHeight);
						if (ig.isIE || ig.isOpera)
							lodMarker.style.height = lodMarkerHeight;
						else {
							var igLodMarker = new IgDomNode(lodMarker);
							igLodMarker.setStyle('height', lodMarkerHeight
									+ 'px;', '!important');
						}
						scrollingContent.elm.insertBefore(lodMarker,
								scrollingContent.elm.firstChild);
						if (ig.isFirefox) {
							var scrollingContentFirstChild = new IgDomNode(
									scrollingContent.elm.firstChild);
							scrollingContentFirstChild.setStyle("height",
									lodMarkerHeight + "px;", '!important');
						}
						fixedContent.elm.insertBefore(
								lodMarker.cloneNode(true),
								fixedContent.elm.firstChild);
						if (ig.isFirefox) {
							var fixedContentFirstChild = new IgDomNode(
									fixedContent.elm.firstChild);
							fixedContentFirstChild.setStyle("height",
									lodMarkerHeight + "px;", '!important');
						}
					}
					var virtualPageIndex = document.createElement("div");
					virtualPageIndex.id = this.elm.id + "_virtualPageIndex";
					virtualPageIndex.style.position = "relative";
					virtualPageIndex.style.border = "solid 1px";
					virtualPageIndex.style.display = "inline";
					virtualPageIndex.style.visibility = "hidden";
					var virtualPageCount = this
							.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT);
					var virtualPageText = document.createTextNode("0 / "
							+ virtualPageCount);
					virtualPageIndex.appendChild(virtualPageText);
					this.elm.appendChild(virtualPageIndex);
					virtualPageIndex.style.left = this.getBody().getSize().width
							- ig.grid.PROP_IG_LOAD_ON_DEMAND_TOOLTIP_OFFSET
							+ "px";
					virtualPageIndex.style.top = -this.getBody().getSize().height
							/ 2 + "px";
				}
				var loadingImage = document.createElement("img");
				loadingImage.setAttribute("src", this
						.getAttribute(ig.grid.PROP_IG_LOADING_IMAGE));
				var loadingDiv = document.createElement("div");
				loadingDiv.id = this.elm.id + "_loadingDiv";
				loadingDiv.appendChild(loadingImage);
				this.elm.appendChild(loadingDiv);
				loadingDiv.style.position = "relative";
				loadingDiv.style.visibility = "hidden";
				if (ig.grid.lastLodGrid != null) {
					ig.grid.lastLodGrid._raiseClientEvent("MoreRowsReceived",
							"MoreRowsReceived", null, null,
							ig.grid.lastLodGridStartRow,
							ig.grid.lastLodGridFetchSize);
					ig.grid.lastLodGrid = null;
					ig.grid.lastLodGridStartRow = 0;
					ig.grid.lastLodGridFetchSize = 0;
				}
			}
			if (ig.isOpera)
				this.scrollTo(0, 0);
			this.getBody().adjustSizeFixedSection();
			this.getBody().adjustSizeScrollingSection();
		};
		IgGrid.prototype.__fixRowSizes = function() {
			var fcc = this.getFrozenColCount();
			if (fcc > 0) {
				var sec = this.getColumnsHeader();
				if (!ig.isNull(sec)) {
					this.__fixRowSizesForSection(sec);
				}
				sec = this.getBody();
				if (!ig.isNull(sec)) {
					this.__fixRowSizesForSection(sec);
				}
				sec = this.getColumnsFooter();
				if (!ig.isNull(sec)) {
					this.__fixRowSizesForSection(sec);
				}
			}
		}
		IgGrid.prototype.__fixRowSizesForSection = function(section) {
			var fcc = this.getFrozenColCount();
			if (fcc > 0) {
				var sSec = section.getScrollingSection();
				var fSec = section.getFixedSection();
				for ( var i = 0; i < sSec.getRowCount(); i++) {
					var fRow = fSec.getRow(i);
					var sRow = sSec.getRow(i);
					sRow.elm.style['height'] = null;
					var fHeight = 0;
					if (!ig.isNull(fRow)) {
						fRow.elm.style['height'] = null;
						fHeight = fRow.getHeight();
					}
					var sHeight = sRow.getHeight();
					var maxHeight = Math.max(sHeight, fHeight);
					if (!ig.isNull(fRow))
						fRow.setSize(null, maxHeight);
					sRow.setSize(null, maxHeight);
				}
			}
		}
		IgGrid.prototype.isScrolling = function() {
			return ig.NaES(this.getAttribute(ig.grid.PROP_IS_SCROLLING));
		};
		IgGrid.prototype.isImmediateRowsChangeEvent = function() {
			return ig.NaES(this.getAttribute("igirce"));
		};
		IgGrid.prototype.moveColumn = function(fci, tci) {
			fci = ig.toFloat(fci);
			tci = ig.toFloat(tci);
			if (!isNaN(fci) && !isNaN(tci)) {
				if (fci >= 0 && tci >= 0 && fci != tci) {
					this.resetEditors();
					ig.smartSubmit(this.elm.id, "moveColumn", "fci:" + fci
							+ ";tci:" + tci, this.elm.id);
					args = this._raiseClientEvent("ColumnMoved", "ColumnMove",
							this, fci, tci);
				}
			}
		};
		IgGrid.prototype.resetEditors = function() {
			var editing = this.get_behaviors().getBehaviorFromInterface(
					$IG.IEditingBehavior);
			if (editing != null) {
				editing.resetEditors();
			}
		}
		IgGrid.prototype.hideEditors = function() {
			var editing = this.get_behaviors().getBehaviorFromInterface(
					$IG.IEditingBehavior);
			if (editing != null) {
				editing.hideEditors();
			}
		}
		IgGrid.prototype.onBeforeSubmit = function(evt) {
			this.get_behaviors()._fireEvent(this.elm, evt);
		}
		IgGrid.prototype.needScrollingGrid = function() {
			var w = ig.toFloat(this.getStyleInline("width"));
			var h = ig.toFloat(this.getStyleInline("height"));
			return (this.hasResizableColumn() || w > 0 || h > 0)
					&& !this.isScrolling();
		};
		IgGrid.prototype.onResize = function() {
		};
		IgGrid.prototype.onLoadOnDemand = function(httpReq) {
			var xmlDoc = httpReq.getResponseXml();
			if (!ig.isNull(xmlDoc)) {
				var results = xmlDoc.documentElement;
				if (!ig.isNull(results)) {
					var components = results.getElementsByTagName("component");
					for ( var i = 0; i < components.length; i++) {
						var component = components.item(i);
						var componentId = component.getAttribute("id");
						if (ig.NaES(componentId)) {
							var currentComponent = ig
									.getElementById(componentId);
							if (!ig.isNull(currentComponent)) {
								var content = component.getElementsByTagName(
										"content").item(0);
								var existingGrid = ig.grid.getGrid(document
										.getElementById(componentId));
								var contentAsHtml = (!ig
										.isNull(content.firstChild)) ? content.firstChild.nodeValue
										: "";
								var existingRowsParent = existingGrid.getBody()
										.getScrollingSection().elm.childNodes[0];
								var fixedColumnsCount = existingGrid
										.getFrozenColCount();
								var fixedRowsParent = null;
								fixedRowsParent = existingGrid.getBody()
										.getFixedSection().elm.childNodes[0];
								var aDiv = document.createElement("div");
								document.body.appendChild(aDiv);
								aDiv.innerHTML = contentAsHtml;
								var newGrid = ig.findDescendant(aDiv, "id",
										componentId);
								var newGridBody = ig.findDescendant(aDiv, "id",
										componentId + "_body");
								var newRows = newGridBody.rows;
								var rowSize = newRows.length;
								for ( var ridx = 0; ridx < rowSize; ridx++) {
									var tmpRow = newRows[ridx].cloneNode(true);
									var fixedTr = ig.getUIElementById(document
											.createElement("TR"));
									var rheight = existingGrid
											.getSingleRowHeight();
									var tmpId = tmpRow.id;
									tmpRow.id = tmpId + "_sr";
									tmpRow.setAttribute("otype",
											"GridRowScroll");
									fixedTr.elm.id = tmpId;
									fixedTr.elm.className = tmpRow.className;
									fixedTr.elm
											.setAttribute("otype", "GridRow");
									ig.getUIElementById(tmpRow).setSize(null,
											rheight);
									fixedTr.setSize(null, rheight);
									for ( var fi = 0; fi < fixedColumnsCount; fi++) {
										fixedTr.elm
												.appendChild(tmpRow.childNodes[0]
														.cloneNode(true));
										ig.deleteNode(tmpRow.childNodes[0]);
									}
									fixedRowsParent.appendChild(fixedTr.elm);
									existingRowsParent.appendChild(tmpRow);
								}
								for ( var attr = 0; attr < newGrid.attributes.length; attr++) {
									if (newGrid.attributes[attr].nodeName != "style") {
										existingGrid.elm
												.setAttribute(
														newGrid.attributes[attr].nodeName,
														newGrid.attributes[attr].nodeValue);
									}
								}
								ig.deleteNode(aDiv);
								var scrollTopState = existingGrid
										.getAttribute(ig.grid.PROP_IG_SCROLL_TOP_STATE);
								if (!ig.isNull(scrollTopState)) {
									existingGrid.isScrollingGrid = true;
								}
								if (!ig.isNull(scrollTopState))
									existingGrid.setScrollTop(scrollTopState);
								var loadingDiv = document
										.getElementById(existingGrid.elm.id
												+ "_loadingDiv");
								loadingDiv.style.visibility = "hidden";
								var activation = existingGrid.get_behaviors()
										.getBehaviorFromInterface(
												$IG.IActivationBehavior);
								if (activation) {
									activation.get_activeCell();
								}
								existingGrid.__fixRowSizes();
								existingGrid.getBody().adjustSizeFixedSection();
								existingGrid.getBody()
										.adjustSizeScrollingSection();
								if (ig.grid.lastLodGrid != null) {
									ig.grid.lastLodGrid._raiseClientEvent(
											"MoreRowsReceived",
											"MoreRowsReceived", null, null,
											ig.grid.lastLodGridStartRow,
											ig.grid.lastLodGridFetchSize);
									ig.grid.lastLodGrid = null;
									ig.grid.lastLodGridStartRow = 0;
									ig.grid.lastLodGridFetchSize = 0;
								}
							}
						}
					}
				}
				var clientState = results.getElementsByTagName("state").item(0);
				if (!ig.isNull(clientState)) {
					var clientStateContent = clientState.getElementsByTagName(
							"content").item(0);
					if (!ig.isNull(clientStateContent)) {
						var clientStateAsHtml = (!ig
								.isNull(clientStateContent.firstChild)) ? clientStateContent.firstChild.nodeValue
								: "";
						var parsedClientState = ig.parseHtml(clientStateAsHtml,
								false);
						if (!ig.isNull(parsedClientState)) {
							var nodes = parsedClientState.childNodes;
							for ( var k = nodes.length - 1; k >= 0; k--) {
								var aNode = nodes.item(k);
								if (aNode.nodeName == "INPUT") {
									var id = aNode.id;
									var currentNode = null;
									if (ig.NaES(id)) {
										currentNode = document
												.getElementById(id);
									} else {
										var name = aNode.name;
										var elements = document
												.getElementsByName(name);
										if (elements.length == 1) {
											currentNode = elements[0];
										}
									}
									if (!ig.isNull(currentNode)) {
										ig.replaceNode(currentNode, aNode);
									}
								}
							}
							ig.deleteNode(parsedClientState);
						}
					}
				}
			}
		};
		IgGrid.prototype.onScroll = function() {
			var loadOnDemand = this
					.getAttribute(ig.grid.PROP_IS_LOAD_ON_DEMAND);
			if (!ig.isNull(this.getAttribute("onScrollMonitor"))
					&& this.getAttribute("onScrollMonitor") == "false") {
				this.setAttribute("onScrollMonitor", true);
				var id = this.elm.id;
				id = id.replace(/:/, "_");
				if (loadOnDemand == ig.grid.LOAD_ON_DEMAND_DEFAULT) {
					if (!ig.isNull(ig.grid.instancesIds[id])) {
						clearInterval(ig.grid.instancesIds[id]);
					}
					ig.grid.instancesIds[id] = setInterval(
							'ig.grid.instances[\'' + id
									+ '\'].onScrollEndDefault()',
							ig.grid.PROP_IG_LOAD_ON_DEMAND_TIMER_DEFAULT);
				} else if (loadOnDemand == ig.grid.LOAD_ON_DEMAND_PARTITIONED) {
					if (!ig.isNull(ig.grid.instancesIds[id])) {
						clearInterval(ig.grid.instancesIds[id]);
					}
					ig.grid.instancesIds[id] = setInterval(
							'ig.grid.instances[\'' + id + '\'].onScrollEnd()',
							ig.grid.PROP_IG_LOAD_ON_DEMAND_TIMER_PARTITIONED);
				}
			}
			var body = this.getBody();
			if (!ig.isNull(body)) {
				var bodyScrollDiv = body.getScrollingDiv();
				if (!ig.isNull(bodyScrollDiv)) {
					this.scrollTo(bodyScrollDiv.elm.scrollLeft,
							bodyScrollDiv.elm.scrollTop);
				}
				if (ig.isNull(loadOnDemand))
					return;
				else if (loadOnDemand == ig.grid.LOAD_ON_DEMAND_PARTITIONED) {
					var virtualPageIndex = document.getElementById(this.elm.id
							+ "_virtualPageIndex");
					if (virtualPageIndex == null)
						return;
					virtualPageIndex.style.visibility = "visible";
					var grid = ig.grid.getGrid(this);
					var body = grid.getBody();
					if (!ig.isNull(body)) {
						var bodyScrollDiv = body.getScrollingDiv();
						var scrollTop = parseInt(bodyScrollDiv.elm.scrollTop);
						var singleRowHeight = parseInt(grid
								.getSingleRowHeight());
						var fetchSize = parseInt(this
								.getAttribute(ig.grid.PROP_IG_ROW_FETCH_SIZE));
						var excludedHeight = 0;
						var expandedRows = document.getElementById(this.elm.id
								+ "_expandedRows");
						if (expandedRows != null) {
							expandedRows = expandedRows.value.split(" ");
							if (expandedRows != "") {
								for ( var i = 0; i < expandedRows.length; i++) {
									if (expandedRows[i] != ""
											&& !ig.isNull(expandedRows[i])) {
										var row = document
												.getElementById(expandedRows[i]
														+ "_cc_sr");
										if (row != null) {
											excludedHeight += row.offsetHeight;
										}
									}
								}
							}
						}
						var currentlyAt = parseInt((scrollTop - excludedHeight)
								/ singleRowHeight);
						var currentVirtualPage = 0;
						currentVirtualPage = parseInt(currentlyAt / fetchSize);
						currentVirtualPage++;
						if (currentVirtualPage > grid
								.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT))
							currentVirtualPage = grid
									.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT);
						else if (currentVirtualPage == 0)
							currentVirtualPage++;
						if (virtualPageIndex != null)
							virtualPageIndex.childNodes[0].nodeValue = " "
									+ currentVirtualPage
									+ " / "
									+ grid
											.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT)
									+ " ";
					}
				}
			}
		};
		IgGrid.prototype.getCurrentVirtualPage = function() {
			return this.getAttribute("currentVirtualPage");
		};
		IgGrid.prototype.setCurrentVirtualPage = function(page) {
			this.setAttribute("currentVirtualPage", page);
		};
		IgGrid.prototype.setScrollTop = function(scrollTop) {
			var body = this.getBody();
			var bodyScrollDiv = body.getScrollingDiv();
			bodyScrollDiv.elm.scrollTop = scrollTop;
		};
		IgGrid.prototype.selectAllRows = function(select) {
			var rows = this.getRows();
			if (ig.isArray(rows)) {
				for ( var i = 0; i < rows.length; i++) {
					var aRow = ig.getUIElementById(rows[i]);
					type = ig.getType(aRow);
					if (type == "GridRow") {
						if (select === true) {
							aRow.select();
						} else {
							aRow.unselect();
						}
					}
				}
			}
		};
		IgGrid.prototype.setColumnWidthsAsArray = function(w) {
			if (ig.isArray(w)) {
				this.elm["igArrColWidths"] = w;
			}
		};
		IgGrid.prototype.setSize = function(w, h, addScrollWidthToBody) {
			var bodyHeight = null;
			var chdr = this.getColumnsHeader();
			var cftr = this.getColumnsFooter();
			if (ig.isNumber(h)) {
				bodyHeight = h;
				var ghdr = this.getGridHeader();
				if (!ig.isNull(ghdr)) {
					bodyHeight = bodyHeight - ghdr.getHeight();
				}
				if (!ig.isNull(chdr)) {
					bodyHeight = bodyHeight - chdr.getHeight();
				}
				if (!ig.isNull(cftr)) {
					bodyHeight = bodyHeight - cftr.getHeight();
				}
				var gftr = this.getGridFooter();
				if (!ig.isNull(gftr)) {
					bodyHeight = bodyHeight - gftr.getHeight();
				}
			}
			if (!ig.isNull(chdr)) {
				chdr.setSize(w, null);
			}
			var bdy = this.getBody();
			if (!ig.isNull(bdy)) {
				if (ig.isNumber(bodyHeight)) {
					var bsd = bdy.getScrollingDiv();
					if (!ig.isNull(bsd)) {
						if (addScrollWidthToBody) {
							if (ig.isIE) {
								bsd.setStyle('height', bodyHeight
										+ this.getScrollBarWidth()
										+ document.getElementById(this.elm.id
												+ "_cheaders").offsetHeight
										- 10 + "px");
							} else {
								bsd.setStyle('height', bodyHeight
										+ this.getScrollBarWidth() + "px");
							}
						} else {
							bsd.setSize(null, bodyHeight);
						}
					}
				}
				bdy.setSize(w, null);
			}
			if (!ig.isNull(cftr)) {
				cftr.setSize(w, null);
			}
		};
		IgGrid.prototype.getScrollBarWidth = function() {
			if (ig.grid.scrollbarWidth == null) {
				document.body.style.overflow = 'hidden';
				var width = document.body.clientWidth;
				document.body.style.overflow = 'scroll';
				width -= document.body.clientWidth;
				if (!width)
					width = document.body.offsetWidth
							- document.body.clientWidth;
				document.body.style.overflow = '';
				ig.grid.scrollbarWidth = width;
			}
			return ig.grid.scrollbarWidth;
		};
		IgGrid.prototype.scrollTo = function(x, y) {
			if (ig.isNumber(x) || ig.isNumber(y)) {
				var SCROLL_IN_PROGRESS = "igstip";
				if (!ig.NaES(this.getAttribute(SCROLL_IN_PROGRESS))) {
					this.setAttribute(SCROLL_IN_PROGRESS, "t");
					var hdr = this.getColumnsHeader();
					if (!ig.isNull(hdr)) {
						hdr.scrollTo(x);
					}
					var bdy = this.getBody();
					if (!ig.isNull(bdy)) {
						bdy.scrollTo(x, y);
					}
					var ftr = this.getColumnsFooter();
					if (!ig.isNull(ftr)) {
						ftr.scrollTo(x);
					}
					this.setAttribute(SCROLL_IN_PROGRESS, "");
				}
			}
		};
		IgGrid.prototype.toScrollingGrid = function(table, cc, fc) {
			var grd = null;
			if (!ig.isNull(table)) {
				grd = document.createElement("div");
				grd.id = table.id;
				grd.className = table.className;
				igGrd = new IgDomNode(grd);
				igGrd.setStyle("overflow", "hidden");
				igTable = new IgDomNode(table);
				igTable.setStyle("width", "");
				var gw = this.elm.__fixedWidth;
				var udw = this.elm.__userWidth;
							
				
				if (!ig.isNull(udw) && udw != 0) {
					if (this.elm.__widthUsesPercentages) {
						udw = this.elm.__gcw;
					} else {
						udw = udw - this.getGrid().getBorder("width");
					}
					igGrd.setStyle("width", udw + "px");

				}
				var gHeader = this.toGridHeaderFooterSection(this
						.getGridHeader());
				if (!ig.isNull(gHeader)) {
					grd.appendChild(gHeader);
				}
				var overflow;
				
				if (udw >= gw && this.isForcedVerticalScroll) {
					overflow = "overflow-x: hidden ; overflow-y: scroll;";
				} else if (udw >= gw) {
					overflow = "overflow-x: hidden ; overflow-y: auto;";
				} else if (this.isForcedVerticalScroll) {
					overflow = "overflow-x: auto ; overflow-y: scroll;";
				} else {
					
					overflow = ( Math.abs(udw-gw) < 5 )?
							"overflow-x: hidden ; overflow-y: auto; ":
							"overflow: auto;";

				}			
//				alert("udw: "+udw +", gw: "+gw +", this.isForcedVerticalScroll: "
//						+this.isForcedVerticalScroll
//						+", Diferencia: "+Math.abs(udw-gw) 
//						+", overflow: "+overflow);
//				
				
				var body = this.toScrollingSection(table.tBodies[0], fc, cc,
						overflow, true);
				
				var cHeader = this.toScrollingSection(table.tHead, fc, cc);
				if (!ig.isNull(cHeader)) {
					grd.appendChild(cHeader);
				}
				if (!ig.isNull(body)) {
					grd.appendChild(body);
				}
				var gFooter = this.toGridHeaderFooterSection(this
						.getGridFooter());
				var cFooter = this.toScrollingSection(table.tFoot, fc, cc);
				if (!ig.isNull(cFooter)) {
					grd.appendChild(cFooter);
				}
				if (!ig.isNull(gFooter)) {
					grd.appendChild(gFooter);
				}
				grd = ig.getUIElementById(grd);
				this.setAttribute(ig.grid.PROP_IS_SCROLLING, "true");
			}
			return grd;
		};
		IgGrid.prototype.toGridHeaderFooterSection = function(id) {
			var result = null;
			if (!ig.isNull(id)) {
				var tmp = ig.getUIElementById(id);
				if (!ig.isNull(tmp)) {
					result = document.createElement("div");
					result.style.cssText = "overflow: hidden;";
					var tmpId = tmp.elm.id;
					tmp.elm.id = "";
					var table = this
							.createEmptyTable(
									tmpId,
									"border: 0px; margin: 0px; padding: 0px; width: 100%; empty-cells: show; border-collapse: collapse;");
					result.appendChild(table);
					var tableTBody = document.createElement("tbody");
					table.appendChild(tableTBody);
					tableTBody.appendChild(tmp.elm);
				}
			}
			return result;
		};
		IgGrid.prototype.toScrollingSection = function(sec, fc, cc, css, rsl) {
			var scrollingSection = null;
			if (!ig.isNull(sec)) {
				var sc = cc - fc;
				scrollingSection = document.createElement("div");
				scrollingSection.style.cssText = "overflow-x: hidden;";
				var twoColumnsTable = this.createEmptyTable(sec.id,
						"border: 0px; margin: 0px; padding: 0px;");
				scrollingSection.appendChild(twoColumnsTable);
				var tctblTBody = document.createElement("tbody");
				twoColumnsTable.appendChild(tctblTBody);
				var tctblTr = document.createElement("tr");
				tctblTr.vAlign = "top";
				tctblTBody.appendChild(tctblTr);
				var fixedTd = document.createElement("td");
				tctblTr.appendChild(fixedTd);
				var fixedDiv = document.createElement("div");
				fixedDiv.style.cssText = "overflow: hidden;";
				fixedTd.appendChild(fixedDiv);
				var fixedDivContent = document.createElement("div");
				fixedDiv.appendChild(fixedDivContent);
				var scrollingTd = document.createElement("td");
				tctblTr.appendChild(scrollingTd);
				var scrollingDiv = document.createElement("div");
				if (ig.NaES(css)) {
					scrollingDiv.style.cssText = css;
				} else {
					scrollingDiv.style.cssText = "overflow: hidden;padding-right:50px";
				}
				if (rsl == true) {
					fixedDiv.onscroll = ig.ui.onScroll;
					scrollingDiv.onscroll = ig.ui.onScroll;
				}
				scrollingTd.appendChild(scrollingDiv);
				var scrollingDivContent = document.createElement("div");
				scrollingDiv.appendChild(scrollingDivContent);
				var fixedTable = this
						.createEmptyTable(
								null,
								"border: 0px; margin: 0px; padding: 0px; table-layout: fixed; width: 0px; empty-cells: show; border-collapse: collapse;");
				fixedDivContent.appendChild(fixedTable);
				var ftb = document.createElement("tbody");
				fixedTable.appendChild(ftb);
				if (rsl == true) {
					var scrollingTable = this
							.createEmptyTable(
									null,
									"border: 0px; margin: 0px; padding: 0px; overflow: hidden; table-layout: fixed; width: 0px; empty-cells: show; border-collapse: collapse");
				} else {
					var scrollingTable = this
							.createEmptyTable(
									null,
									"border: 0px; margin: 0px; padding: 0px; overflow: hidden; table-layout: fixed; width: 0px; empty-cells: show; border-collapse: collapse;float:left");
					var tableWithScroller = this.createEmptyTable(null,
							"display:none;border: 0px; margin: 0px; padding: 0px;width:"
									+ this.getScrollBarWidth() + "px;");
					var tblWScrTBody = document.createElement("tbody");
					tableWithScroller.appendChild(tblWScrTBody);
					var tblWScrTr = document.createElement("tr");
					tblWScrTBody.appendChild(tblWScrTr);
					var td1 = document.createElement("td");
					tblWScrTr.appendChild(td1);
					td1.style["width"] = this.getScrollBarWidth();
					td1.innerHTML = "&nbsp;";
				}
				scrollingDivContent.appendChild(scrollingTable);
				var stb = document.createElement("tbody");
				scrollingTable.appendChild(stb);
				if (!ig.isNull(sec.rows)) {
					this.setScrollingGridRowCount(sec.rows.length);
					if (sec.rows.length > 0) {
						if (sec.rows[0].id.indexOf("cheaders") == -1
								&& sec.rows[0].id.indexOf("cfooters") == -1) {
							this.setSingleRowHeight(sec.rows[0].offsetHeight);
						}
					} else {
						var emptyRow = document.createElement("tr");
						var emptyTD = document.createElement("td");
						emptyTD.innerHTML = "&nbsp;";
						emptyRow.appendChild(emptyTD);
						stb.appendChild(emptyRow);
					}
					while (sec.rows.length > 0) {
						var r = ig.getUIElementById(sec.rows[0]);
						var rId = r.elm.id;
						var rHeight = r.getHeight();
						var rsize = r.getSize();
						stb.appendChild(r.elm);
						if (rHeight != 0)
							r.setSize(null, rsize.height);
						r.elm.id = rId + "_sr";
						var rType = ig.getAttribute(r.elm, ig.PROP_TYPE);
						if (ig.NaES(rType)) {
							ig.setAttribute(r.elm, ig.PROP_TYPE,
									ig.grid.TYPE_GRID_ROW_SCROLL);
						}
						var ftr = ig.getUIElementById(document
								.createElement("TR"));
						ftr.elm.id = rId;
						ftr.elm.className = r.elm.className;
						ftr.elm.style.cssText = r.elm.style.cssText;
						if (rHeight != ftr.getHeight())
							ftr.setSize(null, rHeight);
						ftb.appendChild(ftr.elm);
						if (ig.NaES(rType)) {
							ig.setAttribute(ftr.elm, ig.PROP_TYPE,
									ig.grid.TYPE_GRID_ROW);
						}
						var cl = r.elm.cells.length;
						if (cl == 0) {
							var dcf = document.createElement("TD");
							if (fc > 0)
								dcf.colSpan = fc;
							ftr.elm.appendChild(dcf);
						}
						var oflg = r.getAttribute(ig.PROP_FLAG);
						if (oflg == "igNested") {
							var ncdcf = document.createElement("TD");
							if (fc != 0)
								ncdcf.colSpan = fc;
							ig.setAttribute(ftr.elm, ig.PROP_FLAG, oflg);
							ftr.elm.appendChild(ncdcf);
						} else {
							for ( var ci = 0; ci < fc; ci++) {
								if (ci < cl) {
									var aci = r.elm.cells[0];
									aci.style.cssText = aci.style.cssText
											+ "; overflow: hidden;";
									var colSpan = aci.colSpan;
									if (colSpan > 1 && ci < fc) {
										aci.colSpan = fc - ci;
									}
									ftr.elm.appendChild(aci);
								} else {
								}
							}
						}
						var cl = r.elm.cells.length;
						if (cl == 0) {
							var dcs = document.createElement("TD");
							if (sc > 0) {
								dcs.colSpan = sc;
							}
							r.elm.appendChild(dcs);
						}
						for ( var ci = 0; ci < sc; ci++) {
							if (ci < cl) {
								var aci = r.elm.cells[ci];
								aci.style.cssText = aci.style.cssText
										+ "; overflow: hidden;";
								var colSpan = aci.colSpan;
								if (colSpan > 1 && ci < sc) {
									aci.colSpan = sc - ci;
								}
								ig.setAttribute(aci, ig.grid.PROP_IS_SCROLLING,
										"true");
							} else {
							}
						}
					}
				}
			}
			return scrollingSection;
		};
		IgGrid.prototype.onClick = function(evt) {
		};
		IgGrid.prototype.onDoubleClick = function(evt) {
			this.get_behaviors()._fireEvent(this.elm, evt);
			ig.ui.setActiveComponent(this);
		};
		IgGrid.prototype.onRowChange = function(evt) {
			if (this.getUpdateMode() == "row"
					&& this.findEvent(this.getId(), "celledit") >= 0) {
				ig.smartSubmit(this.getId(), "rowedit", null, "messages");
				this.clearEventQueue();
			}
		}
		IgGrid.prototype.get_cellFromPosition = function(position) {
			if (ig.isNull(position)) {
				return null;
			}
			var virtualLoadOnDemand = this
					.getAttribute(ig.grid.PROP_IS_LOAD_ON_DEMAND);
			if (!ig.isNull(virtualLoadOnDemand)) {
				if (virtualLoadOnDemand == ig.grid.LOAD_ON_DEMAND_PARTITIONED) {
					var currentVirtualPage = +this.getCurrentVirtualPage();
					var fetchSize = +this
							.getAttribute(ig.grid.PROP_IG_ROW_FETCH_SIZE);
					var offset = currentVirtualPage * fetchSize;
					position.row = position.row - offset;
					if (position.row < 0 || position.row >= fetchSize) {
						var newVirtualPage = currentVirtualPage
								+ Math.floor(position.row / fetchSize);
						var virtualPageCount = grid
								.getAttribute(ig.grid.PROP_IG_VIRTUAL_PAGE_COUNT);
						if (newVirtualPage >= 0
								&& newVirtualPage < virtualPageCount) {
							var startRowIndex = newVirtualPage * fetchSize;
							var endRowIndex = startRowIndex + fetchSize - 1;
							var eventArguments = "col:" + position.col
									+ ";row:" + (position.row + offset);
							this.removeEvent(this.getId(), "ActiveCellChanged");
							this.queueEvent(this.getId(), "ActiveCellChanged",
									eventArguments);
							var body = this.getBody();
							var bodyScrollDiv = body.getScrollingDiv();
							grid.scrollTopState = grid.getSingleRowHeight()
									* (newVirtualPage * fetchSize);
							ig.smartSubmit(this.getId(),
									"scrollingLoadOnDemand", "rowRange:"
											+ startRowIndex + " " + endRowIndex
											+ " " + grid.scrollTopState + " "
											+ newVirtualPage, this.getId(),
									null);
							this.setCurrentVirtualPage("" + newVirtualPage);
							return null;
						}
					}
				}
			}
			var cell = this.getBody().getCell(position.row, position.col);
			if (!ig.isNull(cell)) {
				return ig.grid.getTargetCell(cell.elm);
			}
			return null;
		}
		IgGrid.prototype.get_PartitionedLODOffset = function() {
			var virtualLoadOnDemand = this
					.getAttribute(ig.grid.PROP_IS_LOAD_ON_DEMAND);
			if (!ig.isNull(virtualLoadOnDemand)) {
				if (virtualLoadOnDemand == ig.grid.LOAD_ON_DEMAND_PARTITIONED) {
					var currentVirtualPage = this.getCurrentVirtualPage();
					var fetchSize = this
							.getAttribute(ig.grid.PROP_IG_ROW_FETCH_SIZE);
					return currentVirtualPage * fetchSize;
				}
			}
			return 0;
		}
		IgGrid.prototype.selectCell = function(row, col) {
			var newCell = this.getBody().getCell(row, col);
			if (!ig.isNull(newCell)) {
				newCell = new IgGridCell(newCell.elm);
				if (!ig.isNull(newCell)) {
					newCell.select();
					if (this.isScrolling()) {
						var cc = this.getColCount();
						var fcc = this.getFrozenColCount()
						if (col >= fcc) {
							var b = this.getBody().getScrollingDiv();
							if ((newCell.elm.offsetLeft) < (b.elm.scrollLeft)) {
								this.getBody().scrollTo(newCell.elm.offsetLeft);
							} else if ((newCell.elm.offsetLeft + newCell.elm.offsetWidth) > (b.elm.scrollLeft + b
									.getWidth())) {
								this.getBody().scrollTo(
										newCell.elm.offsetLeft
												+ newCell.elm.offsetWidth);
							}
						}
					}
				}
			}
		}
		IgGrid.prototype.onKeyDown = function(evt) {
			this.get_behaviors()._fireEvent(this.elm, evt);
			ig.ui.setActiveComponent(this);
		}
		IgGrid.prototype.onKeyPress = function(evt) {
			this.get_behaviors()._fireEvent(this.elm, evt);
			ig.ui.setActiveComponent(this);
		};
		IgGrid.prototype.getPrevCell = function(currentCell) {
			var selectedCell = currentCell.getPosition();
			if (selectedCell.col == 0 && selectedCell.row == 0)
				return null;
			else if (selectedCell.col == 0) {
				selectedCell.row = selectedCell.row - 1;
				selectedCell.col = this.getColCount() - 1;
			} else
				selectedCell.col = selectedCell.col - 1;
			return this.get_cellFromPosition(selectedCell);
		}
		IgGrid.prototype.getNextCell = function(currentCell) {
			var selectedCell = currentCell.getPosition();
			if (selectedCell.col >= this.getColCount() - 1) {
				selectedCell.row = selectedCell.row + 1;
				selectedCell.col = 0;
			} else {
				selectedCell.col = selectedCell.col + 1;
			}
			return this.get_cellFromPosition(selectedCell);
		}
		IgGrid.prototype.getNextCellVert = function(currentCell) {
			var selectedCell = currentCell.getPosition();
			selectedCell.row = selectedCell.row + 1;
			return this.get_cellFromPosition(selectedCell);
		}
		IgGrid.prototype.getPrevCellVert = function(currentCell) {
			var selectedCell = currentCell.getPosition();
			if (selectedCell.row == 0)
				return null;
			selectedCell.row = selectedCell.row - 1;
			return this.get_cellFromPosition(selectedCell);
		}
		IgGrid.prototype.hasActivation = function() {
			return (!ig.isNull(this.getAttribute("igactg")));
		}
		IgGrid.prototype.hasEditing = function() {
			return (!ig.isNull(this.getAttribute("igeditg")));
		}
		IgGrid.prototype.getEditMode = function() {
			return this.getAttribute("igeditm");
		}
		IgGrid.prototype.getUpdateMode = function() {
			return this.getAttribute("igupdatem");
		}
		IgGrid.prototype._initializeObjects = function() {
		}
		ig.augment(IgGrid, IgUIComponent);
		function IgGridSection(e, grid) {
			this.IgHtmlTable(e);
			this.grid = grid;
		}
		;
		IgGridSection.prototype.adjustSizeFixedSection = function() {
			if (ig.isMozilla) {
				var fcw = this.grid.getFrozenColumnsWidth();
				var fSec = this.getFixedSection();
				fSec.setSize(fcw);
			}
			var body = this.grid.getBody();
			if (!ig.isNull(body)) {
				var bsd = body.getScrollingDiv();
				if (!ig.isNull(bsd)) {
					var bsdc = body.getScrollingDivContent();
					if (!ig.isNull(bsdc)) {
						var bfd = body.getFixedDiv();
						if (!ig.isNull(bfd)) {
							var bsdHeight = bsd.getHeight();
							bfd.setSize(null, bsdHeight);
						}
						var bfdc = body.getFixedDivContent();
						if (!ig.isNull(bfdc)) {
							var bsdcHeight = bsdc.getHeight();
							bfdc.setSize(null, bsdcHeight + 50);
						}
					}
				}
			}
		};
		IgGridSection.prototype.adjustSizeScrollingSection = function() {
			if (ig.isMozilla) {
				var sSec = this.getScrollingSection();
				var scw = this.grid.getScrollingColumnsWidth();
				sSec.setSize(scw);
			}
			var sDiv = this.getScrollingDiv();
			if (!ig.isNull(sDiv)) {
				var vscw = this.grid.getVisibleScrollingColumnsWidth();
				sDiv.setSize(vscw);
			}
		};
		IgGridSection.prototype.getCell = function(row, col) {
			var result = null;
			if (ig.isNumber(row) && ig.isNumber(col)) {
				var fcc = this.grid.getFrozenColCount();
				if (this.grid.isScrolling() && ig.isNumber(fcc)) {
					if (col >= fcc) {
						var scol = col - fcc;
						var sSec = this.getScrollingSection();
						row = this.fixRowIndex(row, sSec);
						result = sSec.getCell(row, scol);
					} else {
						var fSec = this.getFixedSection();
						row = this.fixRowIndex(row, fSec);
						result = fSec.getCell(row, col);
					}
				} else {
					row = this.fixRowIndex(row, this);
					result = this.callSuper("IgHtmlTable", "getCell", row, col);
				}
			}
			return result;
		};
		IgGridSection.prototype.fixRowIndex = function(rowIndex, table) {
			var rows = table.elm.rows;
			var nestedRows = 0;
			if (ig.isArray(rows)) {
				for ( var i = 0; i <= rowIndex && i < rows.length; i++) {
					var oflg = rows[i].getAttribute(ig.PROP_FLAG);
					if (oflg == "igNested") {
						rowIndex++;
					}
				}
			}
			return rowIndex;
		};
		IgGridSection.prototype.getFixedSection = function() {
			return this.getSubsection(0);
		};
		IgGridSection.prototype.getScrollingSection = function() {
			return this.getSubsection(1);
		};
		IgGridSection.prototype.getFixedDiv = function() {
			var result = null;
			var tmp = this.getFixedDivContent();
			if (!ig.isNull(tmp)) {
				result = ig.getUIElementById(tmp.elm.parentNode);
			}
			return result;
		};
		IgGridSection.prototype.getFixedDivContent = function() {
			var result = null;
			var sec = this.getFixedSection();
			if (!ig.isNull(sec)) {
				result = ig.getUIElementById(sec.elm.parentNode);
			}
			return result;
		};
		IgGridSection.prototype.getScrollingDiv = function() {
			var result = null;
			var tmp = this.getScrollingDivContent();
			if (!ig.isNull(tmp)) {
				result = ig.getUIElementById(tmp.elm.parentNode);
			}
			return result;
		};
		IgGridSection.prototype.getScrollingDivContent = function() {
			var result = null;
			var sec = this.getScrollingSection();
			if (!ig.isNull(sec)) {
				result = ig.getUIElementById(sec.elm.parentNode);
			}
			return result;
		};
		IgGridSection.prototype.getSubsection = function(col) {
			var result = null;
			var rCount = this.getRowCount();
			if (rCount > 0) {
				var tr = this.getRow(rCount - 1);
				if (!ig.isNull(tr)) {
					var td = tr.elm.cells[col];
					if (!ig.isNull(td)) {
						var tbls = td.getElementsByTagName("table");
						if (ig.isArray(tbls) && tbls.length > 0) {
							result = new IgHtmlTable(tbls[0]);
						}
					}
				}
			}
			return result;
		};
		IgGridSection.prototype.setColumnWidth = function(col, w, dnaac) {
			if (ig.isNumber(col) && ig.isNumber(w)) {
				if (this.grid.isScrolling()) {
					var fcc = this.grid.getFrozenColCount();
					var sSec = this.getScrollingSection();
					var fSec = this.getFixedSection();
					if (col >= fcc) {
						var scol = col - fcc;
						sSec.setColumnWidth(scol, w);
						if (dnaac != true) {
							this.adjustSizeFixedSection();
							this.adjustSizeScrollingSection();
						}
					} else {
						fSec.setColumnWidth(col, w);
						if (dnaac != true) {
							this.adjustSizeFixedSection();
							this.adjustSizeScrollingSection();
						}
					}
				}
			}
		};
		IgGridSection.prototype.scrollTo = function(x, y) {
			var fixedDiv = this.getFixedDiv();
			var scrollDiv = this.getScrollingDiv();
			if (ig.isNumber(x)) {
				if (!ig.isNull(scrollDiv)) {
					if (scrollDiv.elm.scrollLeft != x) {
						scrollDiv.elm.scrollLeft = x;
					}
				}
			}
			if (ig.isNumber(y)) {
				if (!ig.isNull(scrollDiv)) {
					if (scrollDiv.elm.scrollTop != y) {
						scrollDiv.elm.scrollTop = y;
					}
				}
				if (!ig.isNull(fixedDiv)) {
					if (fixedDiv.elm.scrollTop != y) {
						fixedDiv.elm.scrollTop = y;
					}
				}
			}
		};
		IgGridSection.prototype.setSize = function(w, h) {
			this.adjustSizeFixedSection();
			this.adjustSizeScrollingSection();
		};
		ig.augment(IgGridSection, IgHtmlTable);
		function IgGridRow(e) {
			this.IgUIElement(e);
		}
		;
		IgGridRow.prototype.applyClass = function() {
			var css = this.getClass();
			if (ig.isString(css)) {
				if (this.elm.className != css) {
					this.elm.className = css;
				}
				var br = this.getSlaveRow();
				if (!ig.isNull(br)) {
					if (br.elm.className != css) {
						br.elm.className = css;
					}
				}
			}
		};
		IgGridRow.prototype.collapse = function() {
			var args = this.getGrid()._raiseClientEvent("RowCollapsing",
					"GridRow", this);
			if (args != null && args.get_cancel())
				return false;
			var cc = this.getChildContainer();
			if (!ig.isNull(cc)) {
				cc.hide(true);
				ig.grid.onExpandRow(this);
				this.queueEvent(this.getId(), "expand", "false");
				var expandedRows = document
						.getElementById(this.getGrid().elm.id + "_expandedRows");
				expandedRows.value = expandedRows.value
						.replace(this.elm.id, "");
				expandedRows.value = expandedRows.value.replace("  ", " ");
				var sr = this.getSlaveRow();
				if (!ig.isNull(sr)) {
					sr.collapse();
				}
				this.getGrid().onScroll();
			}
			this.getGrid()._raiseClientEvent("RowCollapsed", "GridRow", this);
		};
		IgGridRow.prototype.expand = function() {
			var args = this.getGrid()._raiseClientEvent("RowExpanding",
					"GridRow", this);
			if (args != null && args.get_cancel())
				return false;
			if (this.hasChild()) {
				this.callSuper("IgUIElement", "expand");
				ig.grid.onExpandRow(this);
				this.queueEvent(this.getId(), "expand", "true");
				var expandedRows = document
						.getElementById(this.getGrid().elm.id + "_expandedRows");
				expandedRows.value = expandedRows.value + " " + this.elm.id;
				expandedRows.value = expandedRows.value.replace("  ", " ");
				var sr = this.getSlaveRow();
				if (!ig.isNull(sr)) {
					sr.expand();
				}
			} else {
				var grid = this.getGrid();
				if (!ig.isNull(grid.getCurrentVirtualPage())) {
					if (!ig.isNull(grid.getBody().getScrollingDiv()))
						this
								.fireEvent(
										"expand",
										"scrollState:"
												+ grid.getBody()
														.getScrollingDiv().elm.scrollTop
												+ " "
												+ grid.getCurrentVirtualPage(),
										ig.grid.onExpandedOnDemand, grid
												.getId());
					else
						this
								.fireEvent(
										"expand",
										"scrollState:"
												+ grid
														.getAttribute(ig.grid.PROP_LOD_MARKER_HEIGHT)
												+ " "
												+ grid.getCurrentVirtualPage(),
										ig.grid.onExpandedOnDemand, grid
												.getId());
				} else {
					if (!ig.isNull(grid.getBody().getScrollingDiv()))
						this
								.fireEvent(
										"expand",
										"scrollState:"
												+ grid.getBody()
														.getScrollingDiv().elm.scrollTop,
										ig.grid.onExpandedOnDemand, grid
												.getId());
					else
						this.fireEvent("expand", null,
								ig.grid.onExpandedOnDemand, grid.getId());
				}
			}
			this.getGrid()._raiseClientEvent("RowExpanded", "GridRow", this);
		};
		IgGridRow.prototype.getSlaveRow = function(suffix) {
			if (ig.isNull(suffix)) {
				suffix = "_sr";
			}
			var result = ig.getUIElementById(this.getId() + suffix);
			return result;
		};
		IgGridRow.prototype.inScrollingGrid = function() {
			return false;
		}
		IgGridRow.prototype.getDefaultClass = function() {
			var result = this.getAttribute(ig.PROP_DEFAULT_CLASS, false);
			if (ig.isNull(result)) {
				if (this.elm.nodeName == "TR") {
					if ((this.elm.sectionRowIndex % 2) === 0) {
						result = this.getAttribute(ig.PROP_DEFAULT_CLASS, true);
					} else {
						result = this.getAttribute(ig.PROP_ALTERNATE_CLASS,
								true);
					}
				}
			}
			return result;
		};
		IgGridRow.prototype.getGrid = function() {
			return ig.grid.getGrid(this.elm);
		};
		IgGridRow.prototype.select = function() {
			var rowSelector = this.getRowSelector();
			if (!ig.isNull(rowSelector)) {
				rowSelector.checked = true;
				this.repaint();
			}
		};
		IgGridRow.prototype.getRowSelector = function() {
			var rowSelector = ig.findDescendant(this.elm, ig.PROP_FLAG,
					ig.grid.TYPE_GRID_ROW_SELECTOR);
			if (ig.isNull(rowSelector)) {
				var slaveRow = this.getSlaveRow();
				if (!ig.isNull(slaveRow)) {
					var rowSelector = ig.findDescendant(slaveRow.elm,
							ig.PROP_FLAG, ig.grid.TYPE_GRID_ROW_SELECTOR);
				}
			}
			return rowSelector;
		};
		IgGridRow.prototype.unselect = function() {
			var rowSelector = this.getRowSelector();
			if (!ig.isNull(rowSelector)) {
				rowSelector.checked = false;
				this.repaint();
			}
		};
		IgGridRow.prototype.updateJunctionIcon = function(anIcon) {
			if (ig.NaES(anIcon)) {
				var icon = ig.findDescendant(this.elm, null, null, "img");
				if (!ig.isNull(icon) && icon.src != anIcon) {
					icon.src = anIcon;
				}
			}
		};
		IgGridRow.prototype.isSelected = function() {
			var result = false;
			var rowSelector = this.getRowSelector();
			if (ig.isNull(rowSelector)) {
				var slaveRow = this.getSlaveRow();
				if (!ig.isNull(slaveRow)) {
					rowSelector = slaveRow.getRowSelector();
				}
			}
			if (!ig.isNull(rowSelector)) {
				result = rowSelector.checked;
			}
			return result;
		};
		IgGridRow.prototype.onClick = function(e) {
			this.getGrid().onClick(e);
		};
		IgGridRow.prototype.onDoubleClick = function(e) {
			this.getGrid().onDoubleClick(e);
		};
		ig.augment(IgGridRow, IgUIElement);
		function IgGridScrollingRow(e) {
			this.IgGridRow(e);
		}
		;
		IgGridScrollingRow.prototype.getChildContainer = function() {
			var id = this.getId();
			var sf = id.substr(id.length - 3, 3);
			id = id.substr(0, id.length - 3);
			return ig.getUIElementById(id + "_cc" + sf);
		};
		IgGridScrollingRow.prototype.updateJunctionIcon = function(anIcon) {
			if (ig.NaES(anIcon)) {
				var icon = ig.findDescendant(this.elm, null, null, "img");
				if (!ig.isNull(icon) && icon.src != anIcon) {
					icon.src = anIcon;
				}
			}
		};
		IgGridScrollingRow.prototype.onMouseEnter = function() {
			var master = this.getMaster();
			if (!ig.isNull(master)) {
				ig.ui.setHoveredElement(master);
			}
		};
		IgGridScrollingRow.prototype.onMouseLeave = function() {
			ig.ui.setHoveredElement(null);
		};
		IgGridScrollingRow.prototype.getMaster = function() {
			var result = null;
			var mId = this.getId();
			if (ig.NaES(mId)) {
				mId = mId.substr(0, mId.length - 3);
				result = ig.getUIElementById(mId);
			}
			return result;
		};
		IgGridScrollingRow.prototype.inScrollingGrid = function() {
			return true;
		}
		IgGridScrollingRow.prototype.getGrid = function() {
			return ig.grid.getGrid(this.elm);
		};
		IgGridScrollingRow.prototype.onClick = function(e) {
			this.getGrid().onClick(e);
		};
		IgGridScrollingRow.prototype.onDoubleClick = function(e) {
			this.getGrid().onDoubleClick(e);
		};
		IgGridScrollingRow.prototype.getRowSelector = function() {
			var rowSelector = ig.findDescendant(this.elm, ig.PROP_FLAG,
					ig.grid.TYPE_GRID_ROW_SELECTOR);
			if (ig.isNull(rowSelector)) {
				var masterRow = this.getMaster();
				if (!ig.isNull(masterRow)) {
					var rowSelector = ig.findDescendant(masterRow.elm,
							ig.PROP_FLAG, ig.grid.TYPE_GRID_ROW_SELECTOR);
				}
			}
			return rowSelector;
		};
		ig.augment(IgGridScrollingRow, IgGridRow);
		function IgGridColumnHeader(e) {
			this.IgUIElement(e);
		}
		;
		IgGridColumnHeader.prototype.getResizeMode = function(left, bottom,
				right, top) {
			return this.isResizable() && right ? 4 : 0;
		};
		IgGridColumnHeader.prototype.doResizeBegin = function() {
			this.orszwidh = 0;
			var grid = ig.grid.getGrid(this.elm);
			var index = this.getIndex();
			if (!ig.isNull(grid)) {
				var column = grid.getColumn(index);
				this.orszwidh = column.getWidth();
			}
			grid.hideEditors();
			return null;
		};
		IgGridColumnHeader.prototype.doResizeMove = function(evt) {
			var dx = ig.ui.x - ig.ui.mouseDownX;
			if (this.orszwidh + dx > 0) {
				var grid = ig.grid.getGrid(this.elm);
				var index = this.getIndex();
				var column = grid.getColumn(index);
				column.setWidth(this.orszwidh + dx, false);
				var b = grid.getBody().getScrollingDiv();
				if (!ig.isNull(b)) {
					b.setStyle("overflow-x", "auto");
				}
				grid.onScroll();
			}
			return null;
		};
		IgGridColumnHeader.prototype.doResizeEnd = function() {
			var grid = ig.grid.getGrid(this.elm);
			if (!ig.isNull(grid)) {
				grid.__fixRowSizes();
				grid.getBody().adjustSizeFixedSection();
				grid.getBody().adjustSizeScrollingSection();
				grid.onScroll();
			}
		};
		IgGridColumnHeader.prototype.doResizeStop = function() {
			var grid = ig.grid.getGrid(this.elm);
			if (!ig.isNull(grid)) {
				var column = grid.getColumn(this.getIndex());
				column.setWidth(this.orszwidh, false);
				grid.__fixRowSizes();
				grid.getBody().adjustSizeFixedSection();
				grid.getBody().adjustSizeScrollingSection();
				grid.onScroll();
			}
		};
		IgGridColumnHeader.prototype.getIndex = function() {
			var r = this.elm.cellIndex;
			if (this.isScrolling()) {
				var g = ig.grid.getGrid(this.elm);
				if (!ig.isNull(g)) {
					r += g.getFrozenColCount();
				}
			}
			return r;
		};
		IgGridColumnHeader.prototype.onDragOver = function(dea) {
			this.showDropCaret(dea);
		};
		IgGridColumnHeader.prototype.onDrop = function(dea, dragSources) {
			if (!ig.isNull(dragSources)) {
				var grid = ig.grid.getGrid(this.elm);
				if (!ig.isNull(grid)) {
					for ( var i = 0; i < dragSources.size(); i++) {
						var node = ig.getUIElementById(dragSources.get(i));
						if (!ig.isNull(node)) {
							if (!this.equals(node)) {
								var gridSrc = ig.grid.getGrid(node.elm);
								if (grid.equals(gridSrc)) {
									var i = node.getIndex();
									var ni = this.getIndex();
									var ib = ((dea.x - this.getPagePosition().x) < (this
											.getWidth() / 2));
									if (!ib) {
										ni = ni + 1;
									}
									if (ni > i) {
										ni = ni - 1;
									}
									grid.moveColumn(i, ni);
								}
							}
						}
					}
				}
			}
		};
		IgGridColumnHeader.prototype.hideDropCaret = function() {
			this.callSuper("IgUIElement", "hideDropCaret");
			var tmp = ig.getUIElementById("odrgfddbck");
			if (!ig.isNull(tmp)) {
				tmp.removeNode();
			}
		};
		IgGridColumnHeader.prototype.isDragSourceAccepted = function(
				dragSources) {
			var result = false;
			if (!ig.isNull(dragSources)) {
				var table = this.elm.parentNode;
				if (!ig.isNull(table)) {
					var t = this.getType();
					for ( var i = 0; i < dragSources.size(); i++) {
						var n = ig.getUIElementById(dragSources.get(i));
						if (n.getAttribute(ig.PROP_TYPE) === t) {
							var table2 = n.elm.parentNode;
							if (!(table === table2) || !this.isDraggable()) {
								result = false;
								break;
							}
						} else {
							result = false;
							break;
						}
						result = true;
					}
				}
			}
			return result;
		};
		IgGridColumnHeader.prototype.isScrolling = function() {
			return ig.NaES(this.getAttribute(ig.grid.PROP_IS_SCROLLING));
		};
		IgGridColumnHeader.prototype.getDropCaret = function() {
			var r = ig.getUIElementById("odrgfddbck");
			if (ig.isNull(r)) {
				r = new IgDomNode(document.createElement("div"));
				r.elm.id = "odrgfddbck";
				r.elm.className = "igGridColumnCaret";
				r.setSize(null, this.getHeight());
				document.body.appendChild(r.elm);
			}
			return r;
		};
		IgGridColumnHeader.prototype.showDropCaret = function(dea) {
			this.callSuper("IgUIElement", "showDropCaret", dea);
			var df = this.getDropCaret();
			if (!ig.isNull(df)) {
				var pos = this.getPagePosition();
				var left = ((dea.x - pos.x) < (this.getWidth() / 2));
				if (left) {
					df.moveTo(pos.x - 1, pos.y);
				} else {
					df.moveTo(pos.x + this.getWidth() - 1, pos.y);
				}
			}
		};
		IgGridColumnHeader.prototype.dragDrop = function(dea) {
			var grid = ig.grid.getGrid(this.elm);
			args = grid._raiseClientEvent("ColumnMoving", "ColumnMove", this);
			if (args == null || !args.get_cancel())
				this.callSuper("IgUIElement", "dragDrop");
		}
		IgGridColumnHeader.prototype.isSortable = function() {
			return (this.getAttribute(ig.ui.PROP_SORTABLE, false, "") == "true");
		};
		ig.augment(IgGridColumnHeader, IgUIElement);
		function IgGridColumn(grid, index) {
			this.grid = grid;
			this.index = index;
		}
		IgGridColumn.prototype.getWidth = function() {
			var acws = this.grid.getColumnWidthsAsArray();
			return acws[this.index];
		};
		IgGridColumn.prototype.isResizable = function() {
			var r = false;
			var hdr = this.grid.getColumnsHeader();
			if (!ig.isNull(hdr)) {
				var rc = hdr.getRowCount();
				for ( var ri = 0; ri < rc; ri++) {
					var aCell = hdr.getCell(ri, this.index);
					if (!ig.isNull(aCell)) {
						if (aCell.isResizable()) {
							r = true;
							break;
						}
					}
				}
			}
			return r;
		};
		IgGridColumn.prototype.setWidth = function(w, dnaac) {
			if (ig.isNumber(w)) {
				var acws = this.grid.getColumnWidthsAsArray();
				acws[this.index] = w;
				var hdr = this.grid.getColumnsHeader();
				if (!ig.isNull(hdr)) {
					hdr.setColumnWidth(this.index, w, dnaac);
				}
				var bdy = this.grid.getBody();
				if (!ig.isNull(bdy)) {
					bdy.setColumnWidth(this.index, w, dnaac);
				}
				var ftr = this.grid.getColumnsFooter();
				if (!ig.isNull(ftr)) {
					ftr.setColumnWidth(this.index, w, dnaac);
				}
			}
		};
		IgGridColumn.prototype.inScrollingGrid = function() {
			return false;
		};
		function IgGridCell(e, cellIndex) {
			this.IgUIElement(e);
			this.cellIndex = cellIndex;
		}
		IgGridCell.prototype.getIndex = function() {
			var row = this.getParentRow();
			var grid = this.getGrid()
			var i = this.elm.cellIndex;
			if (grid.isScrolling() && row.inScrollingGrid()) {
				if (!ig.isNull(grid)) {
					i += grid.getFrozenColCount();
				}
			}
			return i;
		};
		IgGridCell.prototype.getRowIndex = function() {
			var row = this.getParentRow();
			if (row.inScrollingGrid())
				row = row.getMaster();
			var grid = this.getGrid();
			var rowIndex = 0;
			var nestedRows = 0;
			var rows = grid.getRows();
			if (ig.isArray(rows)) {
				for ( var i = 0; i < rows.length; i++) {
					var oflg = rows[i].getAttribute(ig.PROP_FLAG);
					if (oflg == "igNested") {
						nestedRows++;
					}
					if (row.elm == rows[i]) {
						rowIndex = i;
						break;
					}
				}
			}
			return rowIndex - nestedRows + grid.get_PartitionedLODOffset();
		};
		IgGridCell.prototype.getColGroupCol = function() {
			var result = null;
			var tbl = ig.findAncestor(this.elm, null, null, "table");
			if (!ig.isNull(tbl)) {
				var colGroups = tbl.getElementsByTagName("colgroup");
				if (ig.isArray(colGroups)) {
					var cols = colGroups[0].getElementsByTagName("col");
					if (ig.isArray(cols)) {
						if (cols.length > this.cellIndex) {
							result = new IgDomNode(cols[this.cellIndex]);
						}
					}
				}
			}
			return result;
		};
		IgGridCell.prototype.setWidth = function(w, dnaac) {
			var r = new IgRect(0, 0, w, 0);
			this.convertBoxCoordinates(r, ig.ui.BOX_BORDER, this
					.getBoxingModel());
			var cgc = this.getColGroupCol();
			if (!ig.isNull(cgc)) {
				if (ig.isIE) {
					if (r.width > 0) {
						cgc.elm.width = r.width;
					}
				} else {
					if (w > 0) {
						cgc.elm.width = w;
					}
				}
			}
		};
		IgGridCell.prototype.onClick = function(e) {
			var grid = this.getGrid();
			grid._raiseClientEvent("CellClicked", null, e);
			grid.get_behaviors()._fireEvent(grid.getGrid().elm, e);
			ig.ui.setActiveComponent(grid);
		};
		IgGridCell.prototype.onDoubleClick = function(e) {
			var grid = this.getGrid();
			grid.get_behaviors()._fireEvent(grid.getGrid().elm, e);
			ig.ui.setActiveComponent(grid);
		};
		IgGridCell.prototype.getGrid = function() {
			return ig.grid.getGrid(this.elm);
		};
		IgGridCell.prototype.getParentRow = function() {
			return ig.getUIElementById(this.elm.parentNode);
			;
		};
		IgGridCell.prototype.onMouseEnter = function() {
			this.getParentRow().onMouseEnter();
		};
		IgGridCell.prototype.get_value = function() {
			var value = this.elm.getAttribute("val");
			if (!ig.isNull(value))
				return value;
			return this.get_text();
		}
		IgGridCell.prototype.set_value = function(value, text) {
			var grid = this.getGrid();
			var editing = grid.get_behaviors().getBehaviorFromInterface(
					$IG.IEditingBehavior);
			var args = null;
			if (editing != null) {
				args = editing.__raiseClientEvent("CellUpdating",
						$IG.CancelCellUpdateEventArgs, [ this, value ]);
			}
			if (args == null || !args.get_cancel()) {
				var oldValue = this.get_value();
				var val = this.elm.getAttribute("val");
				if (!ig.isNull(val)) {
					val = value;
					this.elm.setAttribute("val", val);
				}
				if (typeof (text) == "undefined")
					text = value.toString();
				if (text != oldValue) {
					this.set_text(text);
					var eventArguments = "col:" + this.getIndex() + ";row:"
							+ this.getRowIndex() + ";value:" + text;
					if (grid.getUpdateMode() == "cell"
							&& ig.grid.inSubmit != true) {
						grid.fireEvent("celledit", eventArguments, null, null);
					} else {
						grid.queueEvent(grid.getId(), "celledit",
								eventArguments);
					}
					if (editing != null) {
						editing.__raiseClientEvent("CellUpdated",
								$IG.CellUpdateEventArgs, this);
					}
				}
			}
		}
		IgGridCell.prototype.get_text = function() {
			var leaf = this.getFirstLeaf();
			var s = leaf.innerHTML;
			if (ig.isEmpty(s))
				this.set_text("&nbsp;");
			s = s.replace('&nbsp;', '');
			return s;
		}
		IgGridCell.prototype.set_text = function(value) {
			if (typeof (value) != "string")
				value = "" + value;
			if (ig.isEmpty(value))
				value = "&nbsp;";
			var leaf = this.getFirstLeaf();
			leaf.innerHTML = "" + value;
		}
		IgGridCell.prototype.getPosition = function(value) {
			return new IgGridPosition(this.getRowIndex(), this.getIndex());
		}
		IgGridCell.prototype.scrollToView = function() {
			var grid = this.getGrid();
			if (grid.isScrolling()) {
				var cc = grid.getColCount();
				var fcc = grid.getFrozenColCount()
				var col = this.getIndex();
				if (col >= fcc) {
					var b = grid.getBody().getScrollingDiv();
					if ((this.elm.offsetLeft) < (b.elm.scrollLeft)) {
						grid.getBody().scrollTo(this.elm.offsetLeft);
					} else if ((this.elm.offsetLeft) > (b.elm.scrollLeft + b
							.getWidth())) {
						grid.getBody().scrollTo(this.elm.offsetLeft);
					}
				} else {
					var fixedDiv = grid.getBody().getFixedDiv();
					if ((this.elm.offsetTop) < (fixedDiv.elm.scrollTop)) {
						grid.getBody().scrollTo(null, this.elm.offsetTop);
					} else if ((this.elm.offsetTop + this.elm.offsetHeight) > (fixedDiv.elm.scrollTop + fixedDiv
							.getHeight())) {
						grid.getBody().scrollTo(null,
								fixedDiv.elm.scrollTop + this.elm.offsetHeight);
						grid.scrollTo(fixedDiv.elm.scrollLeft,
								fixedDiv.elm.scrollTop);
					} else {
						grid.scrollTo(fixedDiv.elm.scrollLeft,
								fixedDiv.elm.scrollTop);
					}
				}
			}
		}
		ig.augment(IgGridCell, IgUIElement);
		function IgGridFilterCell(e) {
			this.IgGridCell(e);
			this._dropDownBehaviour = e._dropDownBehaviour;
			this._column = e._column;
			this._row = null;
			this._defaultDateFormat = "{0:d}";
		}
		IgGridFilterCell.prototype.onMouseEnter = function() {
		}
		IgGridFilterCell.prototype.get_column = function() {
			return this.elm._column;
		}
		IgGridFilterCell.prototype.get_row = function() {
			return this._row;
		}
		IgGridFilterCell.prototype.set_row = function(row) {
			this._row = row;
		}
		IgGridFilterCell.prototype.set_value = function(value, text) {
			var grid = this.getGrid();
			var filtering = grid.get_behaviors().getBehaviorFromInterface(
					$IG.IFilteringBehavior);
			var args = null;
			if (filtering != null) {
				args = null;
			}
			if (args == null || !args.get_cancel()) {
				var oldValue = this.get_value();
				var val = this.elm.getAttribute("val");
				if (!ig.isNull(val)) {
					val = value;
					this.elm.setAttribute("val", val);
				}
				if (typeof (text) == "undefined")
					text = value.toString();
				if (text != oldValue) {
					this.set_text(text);
				}
			}
		}
		IgGridFilterCell.prototype.get_text = function() {
			var leaf = this.elm.childNodes[1];
			if (leaf.innerHTML) {
				return leaf.innerHTML;
			}
		}
		IgGridFilterCell.prototype.set_text = function(value) {
			if (typeof (value) != "string")
				value = "" + value;
			if (ig.isEmpty(value) || value == "undefined")
				value = "&nbsp;";
			var leaf = this.elm.childNodes[1];
			leaf.innerHTML = "" + value;
		}
				IgGridFilterCell.prototype.__parseValue = function(value) {
					var column = this.get_column();
					switch (column.get_type()) {
					case "number":
						if (typeof (value) != "number") {
							var val = Number.parseLocale(value);
							if (isNaN(val))
								val = parseFloat(value);
							if (isNaN(value = val))
								value = 0;
						}
						break;
					case "boolean":
						if (typeof (value) != "boolean") {
							if (value
									&& value.toString().toLowerCase() == "true")
								value = true;
							else
								value = false;
						}
						break;
					case "date":
						if (typeof (value) != "object"
								|| (value != null && typeof (value.getMonth) == "undefined"))
							value = value;
						break;
					}
					return value;
				}, IgGridFilterCell.prototype.get_key = function() {
					return this.get_column()._key;
				}, IgGridFilterCell.prototype.get_type = function() {
					return this.get_column()._type;
				}, ig.augment(IgGridFilterCell, IgGridCell);
		function IgGridFilterColumn(grid, index, key, type) {
			this.IgGridColumn(grid, index);
			this._key = key;
			this._type = type;
		}
		IgGridFilterColumn.prototype.get_type = function() {
			return this._type;
		}
		IgGridFilterColumn.prototype._get_dataFormatString = function() {
			var format = null;
			if (!format && this.get_type() == "date")
				format = this._defaultDateFormat;
			return format;
		}
		IgGridFilterColumn.prototype._formatValue = function(value) {
			if (value == null)
				return "";
			var format = this._get_dataFormatString();
			if (format) {
				if (this.get_type() === "date" && value) {
					if (value == null)
						return "";
					if (typeof (value) == "string")
						return value;
					else if (typeof (value) == "object"
							&& typeof (value.getMonth) != "undefined")
						return value;
					else
						return value;
				} else
					return String.localeFormat(format, value);
			} else if (typeof value == 'number') {
				return String.localeFormat('{0:d}', value);
			}
			if (value == null)
				value = "";
			return value.toString();
		}
		ig.augment(IgGridFilterColumn, IgGridColumn);
		function IgGridAgFunction(e) {
			this.IgUIElement(e);
		}
		;
		ig.augment(IgGridAgFunction, IgUIElement);
		ig.grid.init();
	}
	function IgGridPosition(row, col) {
		this.col = col;
		this.row = row;
	}
	$IG.GridSelectionChangeEventArgs = function() {
		$IG.GridSelectionChangeEventArgs.initializeBase(this);
	}
	$IG.GridSelectionChangeEventArgs.prototype = {
		get_row : function() {
			return this._props[0];
		},
		get_currentState : function() {
			return this._props[1];
		}
	}
	$IG.GridSelectionChangeEventArgs.registerClass(
			"Infragistics.Web.UI.GridSelectionChangeEventArgs",
			$IG.CancelEventArgs);
	$IG.ColumnMoveEventArgs = function() {
		$IG.ColumnMoveEventArgs.initializeBase(this);
	}
	$IG.ColumnMoveEventArgs.prototype = {
		get_header : function() {
			return this._props[0];
		},
		get_oldIndex : function() {
			return this._props[1];
		},
		get_newIndex : function() {
			return this._props[2];
		}
	}
	$IG.ColumnMoveEventArgs.registerClass(
			"Infragistics.Web.UI.ColumnMoveEventArgs", $IG.CancelEventArgs);
	$IG.ColumnSortEventArgs = function() {
		$IG.ColumnSortEventArgs.initializeBase(this);
	}
	$IG.ColumnSortEventArgs.prototype = {
		get_arguments : function() {
			return this._props[0];
		}
	}
	$IG.ColumnSortEventArgs.registerClass(
			"Infragistics.Web.UI.ColumnSortEventArgs", $IG.CancelEventArgs);
	$IG.GridColumnFixEventArgs = function() {
		$IG.GridColumnFixEventArgs.initializeBase(this);
	}
	$IG.GridColumnFixEventArgs.prototype = {
		get_currentIndex : function() {
			return this._props[0];
		},
		get_isFixed : function() {
			return this._props[1];
		}
	}
	$IG.GridColumnFixEventArgs.registerClass(
			"Infragistics.Web.UI.GridColumnFixEventArgs", $IG.CancelEventArgs);
	$IG.GridRowEventArgs = function() {
		$IG.GridRowEventArgs.initializeBase(this);
	}
	$IG.GridRowEventArgs.prototype = {
		get_row : function() {
			return this._props[0];
		}
	}
	$IG.GridRowEventArgs.registerClass("Infragistics.Web.UI.GridRowEventArgs",
			$IG.CancelEventArgs);
	$IG.CancelMoreRowsRequestingEventArgs = function() {
		$IG.CancelMoreRowsRequestingEventArgs.initializeBase(this);
	}
	$IG.CancelMoreRowsRequestingEventArgs.prototype = {
		get_topRowIndex : function() {
			return this._props[2];
		},
		get_rowsRange : function() {
			return this._props[3];
		}
	}
	$IG.CancelMoreRowsRequestingEventArgs.registerClass(
			'Infragistics.Web.UI.CancelMoreRowsRequestingEventArgs',
			$IG.CancelEventArgs);
	$IG.MoreRowsReceivedEventArgs = function() {
		$IG.MoreRowsReceivedEventArgs.initializeBase(this);
	}
	$IG.MoreRowsReceivedEventArgs.prototype = {
		get_topRowIndex : function() {
			return this._props[2];
		},
		get_rowsRange : function() {
			return this._props[3];
		}
	}
	$IG.MoreRowsReceivedEventArgs.registerClass(
			'Infragistics.Web.UI.MoreRowsReceivedEventArgs', $IG.EventArgs);
}