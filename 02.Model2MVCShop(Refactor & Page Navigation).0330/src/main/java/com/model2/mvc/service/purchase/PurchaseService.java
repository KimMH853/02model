package com.model2.mvc.service.purchase;

import java.util.HashMap;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;


public interface PurchaseService {
	
	public void addPurchase(Purchase purchaseVO) throws Exception;
		
	public Purchase getPurchase(int tranNo) throws Exception;
		
	public HashMap<String, Object> getPurchaseList(Search search, String userId) throws Exception;
		
	public void updatePurchase(Purchase purchase) throws Exception;
	
	public void updateTranCode(Purchase purchase) throws Exception;

}
