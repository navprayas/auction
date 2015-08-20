<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!-- 

{ middle } -->
<section class="main">
	<div class="container">
		<div class="form-container form-container-cr">
			<h1>
				<i class="fa fa-user"></i> E-Create Auction
			</h1>
			<form role="form" action="createauction" method="post">


				<c:if test="${not empty auctionDetails.auctionId}">

					<input type="hidden" name="auctionId" id="auctionId"
						value="${auctionDetails.auctionId}" />


				</c:if>



				<div class="form-group">
					<label for="exampleAuctionName">Auction Name</label> <input
						type="text" class="form-control" id="name" name="name"
						placeholder="Auction Name" value="${auctionDetails.name}">
				</div>
				<div class="form-group">
					<label for="exampleInputstatus">Status</label> <select
						name="status" id="status" class="form-control">
						<option
							<c:if test="${auctionDetails.status=='0'}">selected='selected'</c:if>
							value="0">select</option>
						<option
							<c:if test="${auctionDetails.status=='Start'}">selected='selected'</c:if>
							value="Start">Start</option>
						<option
							<c:if test="${auctionDetails.status=='Running'}">selected='selected'</c:if>
							value="Running">Running</option>
					</select>
				</div>
				<div class="form-group">
					<label for="exampleIsapproved">Is Approved</label> <select
						name="isApproved" id="isApproved" class="form-control">
						<option
							<c:if test="${auctionDetails.isApproved=='-1'}">selected='selected'</c:if>
							value="-1">select</option>
						<option
							<c:if test="${auctionDetails.isApproved=='1'}">selected='selected'</c:if>
							value="1">Yes</option>
						<option
							<c:if test="${auctionDetails.isApproved=='0'}">selected='selected'</c:if>
							value="0">No</option>
					</select>
				</div>
				<div class="form-group">
					<label for="exampleCreatedTime">Created Time</label> <input
						type="date" class="form-control" name="createdTimeFormat"
						id="createdTimeFormat" placeholder="Created Time"
						value="${auctionDetails.createdTime}">
				</div>
				<div class="form-group form-submit">
					<button class="btn btn-primary pull-right" type="submit">create
						auction</button>
					<div class="clearfix"></div>
				</div>
			</form>
		</div>
	</div>
	<!-- /container -->
</section>
