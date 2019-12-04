<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/css/taotao.css" rel="stylesheet" />
<title>我的皮卡--我的订单</title>
</head>
<body>
	<!-- header start -->
	<jsp:include page="commons/header.jsp" />
	<!-- header end -->

	<!-- 删除时弹出提示框 -->
	<div id="ui-dialog-delete" class="ui-dialog">
		<div class="ui-dialog-title" style="width: 480px">
			<span style="margin-right: 450px;">提示</span>
		</div>
		<div class="ui-dialog-content"
			style="height: 148px; width: 480px; overflow: hidden;">
			<div class="tip-box icon-box">
				<span class="qm-icon m-icon"></span>
				<div class="item-fore">
					<h3>您确定要删除该订单吗？</h3>
					<div class="op-btns" style="margin-right: 220px;">
						<a  id="btn-1 remove-order" class="btn-1 remove-order" href="javascript:void(0)" >确定</a> <a
							class="btn-9 ml10 remove-order-cancel" href="#none"
							onclick="closeConfim()">取消</a>
					</div>
				</div>
			</div>
		</div>
		<a class="ui-dialog-close" title="关闭" onclick="closeConfim()"> <span
			class="ui-icon ui-icon-delete"></span>
		</a>
	</div>
	<div id="ui-mask-delete" class="ui-mask"></div>
	<!-- 删除时弹出提示框  end-->
	<!-- 确认收货时弹出提示框 -->
	<div id="ui-dialog-harvest" class="ui-dialog">
		<div class="ui-dialog-title" style="width: 480px">
			<span style="margin-right: 450px;">提示</span>
		</div>
		<div class="ui-dialog-content"
			style="height: 148px; width: 480px; overflow: hidden;">
			<div class="tip-box icon-box">
				<span class="qm-icon m-icon"></span>
				<div class="item-fore">
					<h3>您确定要收货吗？</h3>
					<div class="op-btns" style="margin-right: 220px;">
						<a  id="btn-1 harvest-order" class="btn-1 remove-order" href="javascript:void(0)" >确定</a> <a
							class="btn-9 ml10 remove-order-cancel" href="#none"
							onclick="closeHarvest()">取消</a>
					</div>
				</div>
			</div>
		</div>
		<a class="ui-dialog-close" title="关闭" onclick="closeHarvest()"> <span
			class="ui-icon ui-icon-delete"></span>
		</a>
	</div>
	<div id="ui-mask-harvest" class="ui-mask"></div>
	<!-- 确认收货时弹出提示框  end-->
	
	<div id="main">
		<div id="order02" class="mod-main mod-comm lefta-box">
			<div class="mt">
				<ul class="extra-l">
					<li ><a id="allOrders" class="txt curr"
						href="http://localhost:8091/order/allOrders/${userId }.action" >全部订单</a></li>
					<li><a id="ordertoPay" class="txt" href="http://localhost:8091/order/showOrdersByStatus/${userId }/1.action">待付款</a></li>
					<li><a id="ordertoSend" class="txt"  href="http://localhost:8091/order/showOrdersByStatus/${userId }/2.action" >待发货</a></li>
					<li><a id="ordertoReceive" class="txt"  href="http://localhost:8091/order/showOrdersByStatus/${userId }/4.action" >待收货</a></li>
				</ul>
				<div class="extra-r">
					<div class="search">
					<form action="/order/searchOrder.action" id="searchOrderForm" method="post">
						<input type="hidden" name="userId" value="${userId }"/>
						<input id="ip_keyword" class="itxt" name="key" placeholder="商品名称/商品编号/订单号"
							style="color: rgb(51, 51, 51);" type="text"/> <a
							class="search-btn" href="javascript:void(0)" onclick="searchOrderForm.submit()">搜索<b></b></a>
					</form>
					</div>
				</div>
			</div>
			<table class="order-tb">
				<colgroup>
					<col class="number-col">
					<col class="consignee-col">
					<col class="amount-col">
					<col class="status-col">
					<col class="operate-col">
				</colgroup>
				<thead>
					<tr>
						<th>订单详情</th>
						<th>收货人</th>
						<th>金额</th>
						<th>订单状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<c:forEach items="${allOrders }" var="orderInfo">
					<!-- 订单展示开始 -->
					<tbody>
						<tr class="sep-row">
							<td colspan="5"></td>
						</tr>
						<tr class="tr-th">
							<td colspan="5"><span class="gap"></span> <span
								class="dealtime" title=""> <fmt:formatDate
										value="${orderInfo.createTime }" type="date"
										pattern="yyyy-MM-dd HH:mm:ss" /></span> <span class="number">订单号：
									${orderInfo.orderId }</span>
								<div class="tr-operate">
									<a class="order-del" href="#none" title="删除"
										onclick="popConfim(${orderInfo.orderId })">删除</a>
								</div></td>
						</tr>

						<!-- 订单商品展示 -->
						<c:forEach items="${orderInfo.orderItems }" var="orderItem"
							varStatus="status">
							<tr class="tr-bd">
								<td>
									<div class="goods-item ">
										<div class="p-img">
											<a href="http://localhost:8086/item/${orderItem.itemId}.html"
												target="_blank"> <img alt="皮卡"
												src="${orderItem.picPath }" title="${orderItem.title}"
												data-lazy-img="done" width="60" height="60">
											</a>
										</div>
										<div class="p-msg">
											<div class="p-name">
												<a class="a-link"
													href="http://localhost:8086/item/${orderItem.itemId }.html"
													target="_blank" title="${orderItem.title}">${orderItem.title}</a>
											</div>
										</div>
									</div>
									<div class="goods-number">x${orderItem.num}</div>
									<div class="clr"></div>
								</td>
								<c:if test="${status.count == 1 }">
									<td rowspan="${orderInfo.itemTpyeNum }">
										<div class="consignee tooltip">
											<span class="txt" onmouseover="show(${orderInfo.orderId })"
												onmouseout="hide(${orderInfo.orderId })">${orderInfo.orderShipping.receiverName}</span>
											<b onmouseover="show(${orderInfo.orderId })"
												onmouseout="hide(${orderInfo.orderId })"></b>
											<div id="${orderInfo.orderId }" class="prompt-01 prompt-02"
												style="display: none">
												<div class="pc">
													<strong>${orderInfo.orderShipping.receiverName}</strong>
													<p>${orderInfo.orderShipping.receiverState}${orderInfo.orderShipping.receiverCity}
														${orderInfo.orderShipping.receiverDistrict}${orderInfo.orderShipping.receiverAddress}</p>
													<p>${orderInfo.orderShipping.receiverMobile}</p>
												</div>
												<div clss="p-arrow p-arrow-left"></div>
											</div>
										</div>
									</td>
									<td rowspan="${orderInfo.itemTpyeNum }">
										<div class="amount">
											<span>总额 ￥${orderInfo.payment }</span> <br> <span
												class="ftx-13"> <c:if
													test="${orderInfo.paymentType == 1 }">在线支付</c:if> <c:if
													test="${orderInfo.paymentType == 2 }">货到付款</c:if>
											</span>
										</div>
									</td>
									<td rowspan="${orderInfo.itemTpyeNum }">
										<div class="status">
											<span class="order-status ftx-03"> <c:if
													test="${orderInfo.status == 1 }">未付款</c:if> <c:if
													test="${orderInfo.status == 2 }">已付款</c:if> <c:if
													test="${orderInfo.status == 3 }">未发货</c:if> <c:if
													test="${orderInfo.status == 4 }">已发货</c:if> <c:if
													test="${orderInfo.status == 5 }">交易成功</c:if> <c:if
													test="${orderInfo.status == 6 }">交易关闭</c:if>
											</span> <br>
										</div>
									</td>
									<td rowspan="${orderInfo.itemTpyeNum }">
										<div class="operate">
											<c:if test="${orderInfo.status == 1 }">
												<form id="payForm" action="/goAlipay.action" method="post">
													<input type="hidden" name="orderId"
														value="${orderInfo.orderId }" />
													<!-- <input type="submit" value="立即付款"> -->
													<br />
													<div>
														<a class="btn-again btn-again-show"
															href="javascript:void(0)" onclick="payForm.submit()"><b></b>立即付款</a><br>
													</div>
												</form>
											</c:if>
											<c:if test="${orderInfo.status == 2 }">
												<br />
												<div>
													<div
														style="color: #aaa; line-height: 22px; text-align: center;">等待卖家发货</div>
												</div>
											</c:if>
											<c:if test="${orderInfo.status == 4 }">
												<br />
												<div>
													<a class="btn-again btn-again-show"
														href="javascript:void(0)" onclick="popHarvest(${orderInfo.orderId })"><b></b>确认收货</a><br>
												</div>
											</c:if>
										</div>
									</td>
								</c:if>
							</tr>
						</c:forEach>
						<!-- 订单商品展示结束 -->
					</tbody>
				</c:forEach>
				<!-- 订单展示结束 -->
			</table>
		</div>
	</div>
	<!-- 分页 -->
	<%-- <div class="m clearfix" id="bottom_pager">
<div  id="pagin-btm" class="pagin fr" clstag="search|keycount|search|pre-page2">
	<span class="prev-disabled">上一页<b></b></span>
	<a href="javascript:void(0)" class="current">1</a>
	<a href="search?keyword=java&enc=utf-8&qr=&qrst=UNEXPAND&rt=1&page=2">2</a>
	<a href="search?keyword=java&enc=utf-8&qr=&qrst=UNEXPAND&rt=1&page=3">3</a>
	<a href="search?keyword=java&enc=utf-8&qr=&qrst=UNEXPAND&rt=1&page=4">4</a>
	<a href="search?keyword=java&enc=utf-8&qr=&qrst=UNEXPAND&rt=1&page=5">5</a>
	<a href="search?keyword=java&enc=utf-8&qr=&qrst=UNEXPAND&rt=1&page=6">6</a>
	<span class="text">…</span>
	<a href="search?keyword=java&enc=utf-8&qr=&qrst=UNEXPAND&rt=1&page=2" class="next">下一页<b></b></a>
	<span class="page-skip"><em>&nbsp;&nbsp;共${totalPages}页&nbsp;&nbsp;&nbsp;&nbsp;到第</em></span>
</div>
</div> --%>
	<!-- footer start -->
	<jsp:include page="commons/footer.jsp" />
	<!-- footer end -->

	<script type="text/javascript">
	function show(id){
		document.getElementById(id).style.display = "block";
	}
	function hide(id){
		document.getElementById(id).style.display = "none";
	}
	
	function popConfim(orderId) {
		var uiDialog =document.getElementById("ui-dialog-delete");
		var uiMask = document.getElementById("ui-mask-delete");
		//动态设置背景层高度和宽度
		uiMask.style.height = $(document.body)
				.height()
				+ "px";
		uiMask.style.width = $(document.body)
				.width()
				+ "px";
		uiMask.style.display = "block";
		uiDialog.style.display = "block";
		/* 给删除链接动态传递订单号 */
		var del = document.getElementById("btn-1 remove-order");
		del.setAttribute("onclick",'deleteOrder('+orderId+')');   
	};
	/*点击关闭按钮*/
	function closeConfim() {
		var uiDialog = document.getElementById("ui-dialog-delete");
		var uiMask = document.getElementById("ui-mask-delete");
		uiDialog.style.display = "none";
		uiMask.style.display = "none";
		
	};
	function popHarvest(orderId) {
		var uiDialog =document.getElementById("ui-dialog-harvest");
		var uiMask = document.getElementById("ui-mask-harvest");
		//动态设置背景层高度和宽度
		uiMask.style.height = $(document.body)
				.height()
				+ "px";
		uiMask.style.width = $(document.body)
				.width()
				+ "px";
		uiMask.style.display = "block";
		uiDialog.style.display = "block";
		/* 给删除链接动态传递订单号 */
		var harvest = document.getElementById("btn-1 harvest-order");
		harvest.setAttribute("onclick",'harvestOrder('+orderId+')');   
	};
	/*点击关闭按钮*/
	function closeHarvest() {
		var uiDialog = document.getElementById("ui-dialog-harvest");
		var uiMask = document.getElementById("ui-mask-harvest");
		uiDialog.style.display = "none";
		uiMask.style.display = "none";
		
	};
	
	 $(function(){
		 /* 切换导航栏状态 */
			$('li a').click(function(){
				var f = this;
				$('li a').each(function(){
					this.className = this == f ? 'txt curr' :'txt';
				})
			})
	})  
	
	/* 删除订单 */
	function deleteOrder(orderId){
		$.ajax({
			url:"/order/deleteOrder.action",
			type:"post",
			dataType:"json",
			data:{"orderId":orderId},
			success:function(data){
				if(data.status != 200){
					alert(data.msg);
				}else{
					location.reload();
				}
			}
		})
	}
	/* 确认收货 */
	function harvestOrder(orderId){
		$.ajax({
			url:"/order/harvest.action",
			type:"post",
			dataType:"json",
			data:{"orderId":orderId},
			success:function(data){
				if(data.status != 200){
					alert(data.msg);
				}else{
					location.reload();
				}
			}
		})
	}
	
</script>
</body>
</html>