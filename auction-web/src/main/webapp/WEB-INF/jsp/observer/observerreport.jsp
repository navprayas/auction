
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<spring:url value="/css" var="css_url" />
<spring:url value="/js" var="js_url" />
<script src="${js_url}/jquery.min.js"></script>
<!-- { middle } -->
<section class="main">
	<div class="container">
		<div class="table-container">
			<div class="top-line">
				<div class="col-xs-12 col-sm-6">
					<ul class="nav nav-tabs" role="tablist">
						<!-- <li role="presentation" class="active"><a
							aria-controls="home" role="tab" data-toggle="tab"
							href="marketlist" onclick="openUrl(this.href)">Market List</a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="activemarketlist" onclick="openUrl(this.href)">Active
								Market</a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="closedmarketlist" onclick="openUrl(this.href)">Closed
								Market</a></li> -->
					</ul>
				</div>
				<div class="col-xs-12 col-sm-6">
					<ul class="list-inline pull-right">
						<li><a href="#excel">Export to Excel</a></li>
						<li><a href="#pdf">Download PDF</a></li>
						<li><a href="#print">Print</a></li>
					</ul>
				</div>
				<div class="clearfix"></div>
			</div>
			<!-- { tab-Effects } -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="summary">
					<div class="mark-check1">
						<div class="col-sx-12 col-sm-12 select-date">
							<form role="form">
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2 tetrt">
									Select Date:&nbsp; <span>Form</span>
								</div>
								<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
									<input type="date" placeholder="Date" id="inputdate31"
										class="form-control">
								</div>
								<div class="col-xs-12 col-sm-1 tetrt">To</div>
								<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
									<input type="date" placeholder="Date" id="inputdate41"
										class="form-control">
								</div>
								<div class="col-xs-12 col-sm-1 col-md-2 col-lg-2">
									<input type="button" class="btn btn-primary" Value="Go">
								</div>
							</form>
						</div>
					</div>
					<div class="table-responsive user-map">
						<table id="marketlist-pagination"
							class="table table-bordered table-striped text-center">
							<tr>
								<th>Sr. No.</th>
								<th>Description</th>
								<th>Lot Number</th>
								<th>Category Name</th>
								<th>Material Name</th>
								<th>Remark</th>
								<th>Length Range</th>
								<th>Actual Length</th>
								<th>Quantity</th>
							</tr>

							<c:forEach items="${wonList}" var="bidItem" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>
										<div class="modal fade" id="dialog_desc${status.index+1}"
											tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
											<div class="modal-dialog" role="document">
												<div class="modal-content">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal"
															aria-label="Close">
															<span aria-hidden="true">&times;</span>
														</button>
														<h4 class="modal-title" id="myModalLabel">Description</h4>
													</div>
													<div class="modal-body">

														<div id="dialog_desc${status.index+1}"
															class="table-responsive user-map">
															<table
																class="table table-bordered table-striped text-center">
																<tr>
																	<td>
																		<table
																			class="table table-bordered table-striped text-center">
																			<tr></tr>
																			<tr>
																				<td>Sr. No.</td>
																				<td>Category</td>
																				<td>Lot No.</td>
																				<td>Materials Name</td>
																				<td>Remark</td>
																				<td>Length Range</td>
																				<td>Actual Length <br> (Approx)
																				</td>
																				<td>Qty</td>
																			</tr>
																			<tr>
																				<td>${status.index+1}.</td>
																				<td>${bidItem.category.categoryName}</td>
																				<td>${bidItem.bidItemId}</td>
																				<td>${bidItem.name}</td>
																				<c:if test="${fn:length(bidItem.itemLots) == 1}">
																					<c:forEach items="${bidItem.itemLots}"
																						var="itemLotUnique" varStatus="status2">
																						<td>${itemLotUnique.remark}</td>
																						<td>${itemLotUnique.lengthRange}</td>
																						<td>${itemLotUnique.actualLengh}</td>
																					</c:forEach>
																				</c:if>
																				<c:if test="${fn:length(bidItem.itemLots) > 1}">
																					<td>&nbsp;</td>
																					<td>&nbsp;</td>
																					<td>&nbsp;</td>
																				</c:if>
																				<td>${bidItem.totalQuantity}${bidItem.unit}</td>
																			</tr>


																			<c:if test="${fn:length(bidItem.itemLots) > 1}">
																				<c:forEach items="${bidItem.itemLots}" var="itemLot"
																					varStatus="status1">
																					<tr>
																						<td>${status1.index+1}</td>
																						<td>${bidItem.category.categoryName}</td>
																						<td>${itemLot.lotId}</td>
																						<td>${itemLot.name}</td>
																						<td>${itemLot.remark}</td>
																						<td>${itemLot.lengthRange}</td>
																						<td>${itemLot.actualLengh}</td>
																						<td>${itemLot.quantity}${itemLot.unit}</td>
																					</tr>
																				</c:forEach>
																			</c:if>
																		</table>
																	</td>
																</tr>
															</table>
														</div>
													</div>
												</div>
											</div>
										</div> <input type="submit" name="button3"
										id="desc${status.index+1}" value="Desc"
										data-toggle="modal" data-target="#dialog_desc${status.index+1}" />
									</td>
									<td>${bidItem.bidItemId}</td>
									<td>${bidItem.category.categoryName}</td>
									<td>${bidItem.name}</td>
									<c:if test="${fn:length(bidItem.itemLots) == 1}">
										<c:forEach items="${bidItem.itemLots}" var="itemLotUnique"
											varStatus="status2">
											<td>${itemLotUnique.remark}</td>
											<td>${itemLotUnique.lengthRange}</td>
											<td>${itemLotUnique.actualLengh}</td>
										</c:forEach>
									</c:if>
									<c:if test="${fn:length(bidItem.itemLots) > 1}">
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</c:if>
									<td>${bidItem.totalQuantity}${bidItem.unit}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /container -->

</section>

