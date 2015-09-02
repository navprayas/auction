<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/js" var="js_url" />
<script src="${js_url}/jquery.min.js"></script>
<script>
	var timeSpans = new Array();
	var bidItemIds = new Array();
	function setTimeLefts(remain, bidItemId) {
		timeSpans.push(remain);
		bidItemIds.push(bidItemId);
	}
	function openUrl(url) {
		window.location.href = url;
	}

	var marketList = function() {
		alert("marketlist");
		$
				.ajax({
					url : 'activemarketlistajaxcall',
					method : 'get',
					success : function(data) {
						$('#marketlist-pagination').empty();
						var marketlist = jQuery.parseJSON(data);
						var l = marketlist.length;
						var tableHeader = "<tr><th>Sr. No.</th><th>Name</th><th>Location</th><th>City</th><th>Zone</th><th>Min Bid Price</th><th>Min Bid Increment</th><th>Min Bid Increment</th><th>Time Left</th><th>Created Time</th></tr>";
						$('#marketlist-pagination').append(tableHeader);
						var tableData = "";
						if (l > 0) {
							for (var i = 0; i < l; i++) {
								tableData += "<tr><td>"
										+ marketlist[i].seqId
										+ "</td><td>"
										+ marketlist[i].name
										+ "</td><td>"
										+ marketlist[i].location
										+ "</td><td>"
										+ marketlist[i].city
										+ "</td><td>"
										+ marketlist[i].zone
										+ "</td><td>"
										+ marketlist[i].minBidPrice
										+ "</td><td>"
										+ marketlist[i].minBidIncrement
										+ "</td><td>  <input type='hidden' name='currentPriceHidden' id='itemPrice"+marketlist[i].bidItemId+" value="+marketlist[i].currentMarketPrice+"> <div id='currentPriceDiv'>"
										+ marketlist[i].currentMarketPrice
										+ " </div></td><td>"
										+ getConvertedDate(marketlist[i].createdTime)
										+ "</td><td><div id='countdown"+marketlist[i].bidItemId+">"
										+ marketlist[i].timeleft
										+ "</div> <script>setTimeLefts(parseInt('"
										+ marketlist[i].timeLeft
										+ "'),'"
										+ marketlist[i].bidItemId
										+ "')<script></td><td><input type='text' value='Next Bid'/></td></tr>";
							}
						} else {
							tableData += "<tr><td colspan='10'>No Data Found</td></tr>";
						}
						$('#marketlist-pagination').append(tableData);
					},
					error : function(err) {
						alert(err);
					}
				});
	};
	setInterval(marketList, 1000 * 60 * 2); // you could choose not to continue on failure...
</script>
<!-- { middle } -->
<section class="main">
	<div class="container">
		<input id="extn" type="hidden" name="extn" value="${timeextention}" />
		<input id="lLTime" type="hidden" name="lastLoadTime" value="0" /> <input
			id="lLCount" type="hidden" name="freqCounts" value="0" />

		<div class="table-container">
			<div class="top-line">
				<div class="col-xs-12 col-sm-6">
					<ul class="nav nav-tabs" role="tablist">
						<li><a aria-controls="home" role="tab"
							data-toggle="tab" href="marketlist" onclick="openUrl(this.href)">Market
								List</a></li>
						<li class="active" role="presentation"><a aria-controls="home" role="tab" data-toggle="tab"
							href="activemarketlist" 
							onclick="openUrl(this.href)">Active Market</a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="closedmarketlist" onclick="openUrl(this.href)">Closed
								Market</a></li>
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
								<th>Name</th>
								<th>Location</th>
								<th>City</th>
								<th>Zone</th>
								<th>Min Bid Price</th>
								<th>Min Bid Increment</th>
								<th>Current Market</th>
								<th>Created Time</th>
								<th>Time Left</th>
								<th>Next Bid</th>
							</tr>

							<c:forEach var="marketlist" items="${bidItems}"
								varStatus="status">
								<tr>
									<td>${marketlist.seqId}</td>
									<td>${marketlist.name}</td>
									<td>${marketlist.location}</td>
									<td>${marketlist.city}</td>
									<td>${marketlist.zone}</td>
									<td>${marketlist.minBidPrice}</td>
									<td>${marketlist.minBidIncrement}</td>
									<td><input type='hidden' name="currentPriceHidden"
										id="itemPrice${marketlist.bidItemId}"
										value='${marketlist.currentMarketPrice}'>
										<div id="currentPriceDiv">${marketlist.currentMarketPrice}</div></td>
									<td>${marketlist.createdTime}</td>
									<td><div id="countdown${marketlist.bidItemId}">
											${marketlist.timeLeft}</div> <script>
												setTimeLefts(
														parseInt('${marketlist.timeLeft}'),
														'${marketlist.bidItemId}');
											</script></td>
									<td><c:choose>
											<c:when
												test='${bidItemWithAutoBidFlag[bidItem.bidItemId] == 2 && bidItemWithRanks[bidItem.bidItemId] == 1} '>
												<input type="submit" name="button3"
													id="NextBid${marketlist.bidItemId}" value="Next Bid"
													onclick="doNextBid('${marketlist.bidItemId}', '${marketlist.minBidIncrement}'); return false;"
													disabled="disabled" />
											</c:when>
											<c:otherwise>
												<input type="submit" name="button3"
													id="NextBid${marketlist.bidItemId}" value="Next Bid"
													onclick="doNextBid('${marketlist.bidItemId}', '${marketlist.minBidIncrement}'); return false;" />
											</c:otherwise>
										</c:choose></td>

								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /container -->

	<script type="text/javascript">
		function doNextBid(bidItemId, minIncrementAmount) {
			var curPriceObject = $('#itemPrice' + bidItemId).val();
			var nextBidPrice = parseFloat(curPriceObject)
					+ parseFloat(minIncrementAmount);
			if (isNaN(nextBidPrice)) {
				return false;
			}

			var flag;
			flag = confirm("Are you sure you want to put next bid for amount - "
					+ nextBidPrice + " , then Press OK");
			if (flag == true) {

				$.ajax({
					url : 'bid',
					method : 'get',
					data : {
						bidItemId : bidItemId,
						bidType : 1,
						bidAmount : nextBidPrice,
						comments : "Next Bid"
					},
					success : function(data, status) {
						if (status == 'success') {
							$('#itemPrice' + bidItemId).val(nextBidPrice);
							$('#currentPriceDiv' + bidItemId).append(
									nextBidPrice);
							setNextBidDisabled('NextBid' + bidItemId);
						}

					}
				});

			}
			window.location.reload(true);
		}

		function setNextBidDisabled(nextBidButton) {
			if (document.getElementById(nextBidButton)) {
				document.getElementById(nextBidButton).disabled = true;
			}
		}

		function toHourAndMinuteAndSecond(x) {
			return Math.floor(x / 3600) + ":" + Math.floor((x % 3600) / 60)
					+ ":" + x % 60;
		}

		function displayTimes() {
			var extendTime = document.getElementById("extn").value;
			var count = document.getElementById("lLCount").value;
			if (count > 5) {
				return false;
			}
			for (var i = 0; i < bidItemIds.length; i++) {
				ts = timeSpans[i];
				ex = parseInt(extendTime);
				diff = 0;
				if (i == 0 && ex > 0) {
					diff = ex - ts;
					if (diff < 0)
						diff = 0;
					timeSpans[0] = ex;
				} else {
					timeSpans[i] += diff;
				}

				if (timeSpans[i] <= 0) {
					refreshPage();
				} else {
					$('#countdown' + bidItemIds[i]).empty();
					$('#countdown' + bidItemIds[i]).append(
							"<p>" + toHourAndMinuteAndSecond(timeSpans[i])
									+ "</p>");
					timeSpans[i] -= 1;
				}
			}
			document.getElementById("extn").value = 0;
			setTimeout("displayTimes()", 1000);
		}

		$(document).ready(function() {
			displayTimes();
		});

		function refreshPage() {
			var count = document.getElementById("lLCount").value;
			var timee = document.getElementById("lLTime").value;
			var currentTime = new Date();
			diff = currentTime.getTime() - parseInt(timee);
			if (diff < 15000) {
				document.getElementById("lLCount").value = parseInt(count) + 1;
				document.getElementById("lLTime").value = currentTime.getTime();
				return false;
			}
			document.getElementById("lLTime").value = currentTime.getTime();
			window.location.reload(true);
		}

		function getConvertedDate(time) {
			var date = new Date(time);
			var dd = date.getDate();
			if (dd < 10)
				dd = '0' + dd;

			var mm = date.getMonth() + 1;

			if (mm < 10)
				mm = '0' + mm;

			var yy = date.getFullYear() % 100

			if (yy < 10)
				yy = '0' + yy;

			var hh = date.getHours();
			if (hh < 10)
				hh = '0' + hh;

			var min = date.getMinutes();
			if (min < 10)
				min = '0' + min;

			var sec = date.getSeconds();
			if (sec < 10)
				sec = '0' + sec;
			return dd + '-' + mm + '-' + yy + ' ' + hh + ':' + mm + ':' + sec;

		}
	</script>



</section>

