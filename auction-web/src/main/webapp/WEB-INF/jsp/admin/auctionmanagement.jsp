<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/admin/initcache" var="admin_initcache_url" />
<spring:url value="admin/closeAuction" var="close_auction_url" />
<script>
	function initializeAuction(time) {
		var auctionId = document.getElementById("auctionId").value;
		var location1 = "${admin_initcache_url}?auctionId=" + auctionId
				+ "&auctionTimeExt=" + time;
		window.location.href = location1;
	}
</script>
<!-- { middle } -->
<section class="main">
	<div class="container">
		<div class="table-container">
			<div class="top-line">
				<div class="col-xs-12 col-sm-6">
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active"><a
							aria-controls="home" role="tab" data-toggle="tab"
							href="marketlist" onclick="openUrl(this.href)">Auction List</a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="activemarketlist" onclick="openUrl(this.href)"></a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="closedmarketlist" onclick="openUrl(this.href)"></a></li>
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
						<table class="table table-bordered table-striped text-center">
							<tr>




								<th>Sr. No.</th>
								<th>Auction Id</th>
								<th>Auction Name</th>
								<th>Status</th>
								<th>Auction Start Time</th>
								<th>Auction End Time</th>
								<th>Action</th>
							</tr>

							<c:forEach var="auction" items="${auctionlist}"
								varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${auction.id}</td>
									<td>${auction.name}</td>
									<td>${auction.status}</td>
									<td>${auction.auctionStartTime}</td>
									<td>${auction.auctionEndTime}</td>
									<td><c:if test="${auction.status == 'Start'}">
											<c:if test="${aunctionRunningOrClosedPresent == 0}">
												<a
													href="${admin_initcache_url}?auctionId=${auction.id}">Start</a>
											</c:if>
											<c:if test="${aunctionRunningOrClosedPresent == 1}">
     					Close Running or Ended Auction First
     				</c:if>
										</c:if> <c:if test="${auction.status == 'End'}">
											<a href="${close_auction_url}?auctionId=${auction.id}">Close</a>
										</c:if> <c:if test="${auction.status == 'Running'}">
											<a href="${close_auction_url}?auctionId=${auction.id}">Close</a>
										</c:if></td>

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

