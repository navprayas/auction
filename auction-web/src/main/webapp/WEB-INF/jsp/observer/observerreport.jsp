
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
						<li><a href="#" onclick="filterReport('excelreport')">Export
								to Excel</a></li>
						<li><a href="#" onclick="filterReport('pdfreport')">Download
								PDF</a></li>
						<li><a href="#" onclick="printData();">Print</a></li>
					</ul>
				</div>
				<div class="clearfix"></div>
			</div>
			<!-- { tab-Effects } -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="summary">
					<div class="mark-check1">
						<div class="col-sx-12 col-sm-12 select-date">
							<form role="form" action="#" id="filteredReport">
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2 tetrt">
									Select Date:&nbsp; <span>Form</span>
								</div>
								<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
									<input type="date" placeholder="Date" id="fromDate"
										class="form-control" name="fromDate" required="required">
								</div>
								<div class="col-xs-12 col-sm-1 tetrt">To</div>
								<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
									<input type="date" placeholder="Date" id="toDate"
										class="form-control" name="toDate" required="required">
								</div>
								<div class="col-xs-12 col-sm-1 col-md-2 col-lg-2">
									<input type="button" class="btn btn-primary" Value="Go"
										onclick="filterReport('observerreport')">
								</div>
							</form>
						</div>
					</div>
					<div class="table-responsive user-map" id="printTable">
						<table id="marketlist-pagination"
							class="table table-bordered table-striped text-center">
							<tr>
								<td>Sr. No.</td>
								<td>Lot. No</td>
								<td>Category</td>
								<td>Material Name</td>
								<td>Remark</td>
								<td>Length Range</td>
								<td>Quantity</td>
								<td>Location</td>
								<td>City</td>
								<td>Zone</td>
								<td>Currency</td>
								<td>Lot's Status</td>
								<td>Sales Price</td>
							</tr>

							<c:forEach items="${bidsreport}" var="bidItem" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${bidItem.bidItemId}</td>
									<td>${bidItem.categoryName}</td>
									<td>${bidItem.bidItemName}</td>
									<td>${bidItem.comments}</td>
									<td>${bidItem.unit}</td>
									<td>${bidItem.totalQuantity}</td>
									<td>${bidItem.location}</td>
									<td>${bidItem.city}</td>
									<td>${bidItem.zone}</td>
									<td>${bidItem.currency}</td>
									<td>${bidItem.bidStatus}</td>
									<td>${bidItem.salesPrice}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /container -->


	<script>
		function filterReport(url) {
			$('#filteredReport').attr('action', url);
			$('#filteredReport').submit();
		}

		function printData() {
			var divToPrint = document.getElementById("printTable");
			newWin = window.open("");
			newWin.document.write(divToPrint.outerHTML);
			newWin.print();
			newWin.close();
		}
	</script>

</section>

