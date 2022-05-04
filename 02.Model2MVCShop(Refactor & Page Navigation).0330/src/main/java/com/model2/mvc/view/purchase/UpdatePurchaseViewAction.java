package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;



public class UpdatePurchaseViewAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		
		System.out.println("°¡³ª´Ù");
		System.out.println(tranNo);
		/*
		PurchaseService service=new PurchaseServiceImpl();
		Purchase purchase=service.getPurchase(tranNo);
		
		request.setAttribute("purchase", purchase);
		*/
		
		
		String userId=request.getParameter("userId");
		
		UserService userservice = new UserServiceImpl();
		User uservo = userservice.getUser(userId);
		
		Purchase purchase=new Purchase();
		purchase.setTranNo(tranNo );
		purchase.setBuyer(uservo);
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyAddr(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("receiverDate"));
		
		PurchaseService service=new PurchaseServiceImpl();
		service.updatePurchase(purchase);
//		
//		HttpSession session=request.getSession();
//		int sessionNO=((Purchase)session.getAttribute("purchase")).getProdNo();
//	
//		if(sessionNO==prodNo){
//			session.setAttribute("purchase", purchase);
//		}
		
		return "redirect:/updatePurchaseView.do?tranNo="+tranNo;
	}

}
