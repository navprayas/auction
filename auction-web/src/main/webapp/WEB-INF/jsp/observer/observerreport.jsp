
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
							<td>Sr. No.</td>
							<td>Lot. No</td>
							<td>Description</td>
							<td>Category</td>
							<td>Material Name</td>
							<td>Remark</td>
							<td>Length Range</td>
							<td>Actual Length (Approx)
							</td>
							<td>Quantity</td>
							<td>Zone</td>
							<td colspan="2">
								<table>
									<tr>
										<td colspan="2">H1</td>
									</tr>

									<tr>
										<td>Company Name</td>
										<td>Bid Price</td>
									</tr>
								</table>
							</td>
							<td colspan="2">
								<table>
									<tr>
										<td colspan="2">H2</td>
									</tr>
									<tr>
										<td>Company Name</td>
										<td>Bid Price</td>
									</tr>
								</table>
							</td>
							<td colspan="2">H3
								<table>
									<tr>
										<td>Company Name</td>
										<td>Bid Price</td>
									</tr>
								</table></td>
							<td>Lot's Status
								</td>
							<td>Sales Price</td>
							<td>Total Sales (Qty X SalesPrice)</td>
						</tr>

							<c:forEach items="${bidsreport}" var="bidItem" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${bidItem.bidId}
										</td>
									<td><div class="modal fade" id="dialog_desc${status.index+1}"
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
														title="Item Description" class="table-responsive user-map">

														<table
															class="table table-bordered table-striped text-center">
															<tr>
																<td align="left" valign="top"><form name="form1"
																		method="post" action="">
																		<table
																			class="table table-bordered table-striped text-center">
																			<tr>
																				<td>Sr. No.</td>
																				<td>Category</td>
																				<td>Lot No.</td>
																				<td>Material Name</td>
																				<td>Remark</td>
																				<td>Length Range</td>
																				<td>Actual Length <br> (Approx) </td>
																				<td>Qty</td>
																				<td>Zone</td>
																			</tr>
																			<tr>
																				<td>${bids.bidItem.serialNo}.</td>
																				<td>${bids.bidItem.category.categoryName}</td>
																				<c:forEach items="${bids.bidItem.itemLots}"
																					var="itemLotUnique" varStatus="status2">
																					<c:if test="${status2.index+1 == 1}">
																						<td>${itemLotUnique.lotNo}</td>
																					</c:if>
																				</c:forEach>

																				<td>${bids.bidItem.name}</td>
																				<c:if
																					test="${fn:length(bids.bidItem.itemLots) == 1}">
																					<c:forEach items="${bids.bidItem.itemLots}"
																						var="itemLotUnique" varStatus="status2">
																						<td>${itemLotUnique.remark}</td>
																						<td>${itemLotUnique.lengthRange}</td>
																						<td>${itemLotUnique.actualLengh}</td>
																					</c:forEach>
																				</c:if>
																				<c:if test="${fn:length(bids.bidItem.itemLots) > 1}">
																					<td>&nbsp;</td>
																					<td>&nbsp;</td>
																					<td>&nbsp;</td>
																				</c:if>

																				<td>${bids.bidItem.totalQuantity}
																					${bids.bidItem.unit}</td>
																				<c:if
																					test="${fn:length(bids.bidItem.itemLots) == 1}">
																					<c:forEach items="${bids.bidItem.itemLots}"
																						var="itemLotUnique" varStatus="status2">
																						<td>${bids.bidItem.zone}</td>
																					</c:forEach>
																				</c:if>
																				<c:if test="${fn:length(bids.bidItem.itemLots) > 1}">
																					<td>&nbsp;</td>
																				</c:if>
																			</tr>

																			<c:if test="${fn:length(bids.bidItem.itemLots) > 1}">
																				<c:forEach items="${bids.bidItem.itemLots}"
																					var="itemLot" varStatus="status1">
																					<tr>
																						<td>${status1.index+1}.</td>
																						<td>${bids.bidItem.category.categoryName}</td>
																						<td>${itemLot.lotNo}</td>
																						<td>${itemLot.name}</td>

																						<td>${itemLot.remark}</td>
																						<td>${itemLot.lengthRange}</td>
																						<td>${itemLot.actualLengh}</td>
																						<td>${itemLot.quantity}${itemLot.unit}</td>
																						<td>${itemLot.zone}</td>
																					</tr>
																				</c:forEach>
																			</c:if>

																		</table>
																	</form></td>
															</tr>
														</table>
													</div>


												</div>
											</div>
										</div>
									</div> <input type="submit" name="button7" id="desc${status.index+1}"
									value="Desc" data-toggle="modal"
									data-target="#dialog_desc${status.index+1}" /></td>
									
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

