package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;


public class UpdateTranCodeAction extends Action{

	public UpdateTranCodeAction() {
		
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Search search = new Search();
		
		int page = 1;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		search.setPage(page);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		
		String pageUnit = getServletContext().getInitParameter("pageSize");
		search.setPageUnit(Integer.parseInt(pageUnit));
		
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		
		Purchase purchase = new Purchase();
		purchase.setTranNo(tranNo);
		purchase.setTranCode(request.getParameter("tranCode"));
		System.out.println("tranNo : " + tranNo);
		System.out.println("updateTronNo : "+purchase.getTranNo());
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updateTranCode(purchase);
		
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String buyer = user.getUserId();
		
		PurchaseService service2 = new PurchaseServiceImpl();
		HashMap<String, Object> map = service2.getPurchaseList(search, buyer);
		
		request.setAttribute("map", map);
		request.setAttribute("search", search);
		System.out.println("UpdateTranCodeAction map : "+map);
				
		
		return "forward:/purchase/listPurchase.jsp";
	}

}
