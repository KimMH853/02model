package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;


public class UpdateTranCodeByProdAction extends Action{

	public UpdateTranCodeByProdAction() {
		
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Search search = new Search();
		int page=1;
		if(request.getParameter("page") != null)
			page=Integer.parseInt(request.getParameter("page"));
		
		search.setPage(page);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		
		String pageUnit=getServletContext().getInitParameter("pageSize");
		search.setPageUnit(Integer.parseInt(pageUnit));
		
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		
		Purchase purchase = new Purchase();
		purchase.setTranNo(prodNo);
		purchase.setTranCode(request.getParameter("tranCode"));
		System.out.println("UpdateTranCodeByProdAction prodNo : "+prodNo);
		System.out.println("UpdateTranCodeByProdAction tranCode : "+purchase.getTranCode());
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updateTranCode(purchase);
		
		ProductService service2 = new ProductServiceImpl();
		HashMap<String,Object> map = service2.getProductList(search);
		
		request.setAttribute("map", search);
		
		return "forward : /listProduct.do?menu=manage";
	}
	
	

}
