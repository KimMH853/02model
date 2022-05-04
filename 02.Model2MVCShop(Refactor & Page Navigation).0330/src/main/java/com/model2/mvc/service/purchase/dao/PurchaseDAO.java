package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public class PurchaseDAO {

	public PurchaseDAO() {
	}

	public void insertPurchase(Purchase purchase) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.NEXTVAL,?,?,?,?,?,?,?,?,to_date(sysdate,'YYYY-MM-DD HH24:MI:SS'),?)";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		stmt.setString(2, purchase.getBuyer().getUserId());
		stmt.setString(3, purchase.getPaymentOption());
		stmt.setString(4, purchase.getReceiverName());
		stmt.setString(5, purchase.getReceiverPhone());
		stmt.setString(6, purchase.getDivyAddr());
		stmt.setString(7, purchase.getDivyRequest());
		stmt.setString(8, purchase.getTranCode());
		stmt.setString(9, purchase.getDivyDate());

		int i = stmt.executeUpdate();
		System.out.println("1번 insert 유뮤 : " + i + " 개 행이 만들어졌습니다.");

		con.close();
		
	}

	
	
	public Purchase getPurchase(int tranNo) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction WHERE TRAN_NO=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();

		Purchase purchase = null;
		while (rs.next()) {

			purchase = new Purchase();

			Product product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));

			User user = new User();
			user.setUserId(rs.getString("buyer_id"));

			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setPurchaseProd(product);
			purchase.setBuyer(user);
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			purchase.setDivyDate(rs.getString("DLVY_DATE"));

		}

		con.close();

		return purchase;
	}
	  
	
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();

		System.out.println("search.getSearchCondition() :"+search.getSearchCondition());
		
		String sql = "SELECT * FROM transaction WHERE BUYER_ID ='"+userId+"'";
		
		System.out.println("PurchaseDAO::Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
	
		System.out.println(search);

		List<Purchase> list = new ArrayList<Purchase>();
		
		
		
		/*
		 * PreparedStatement stmt = con.prepareStatement(sql,
		 * ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		 * 
		 * 
		 * ResultSet rs = stmt.executeQuery();
		 * 
		 * rs.last(); int total = rs.getRow(); System.out.println("로우의 수:" + total);
		 * 
		 * HashMap<String, Object> map = new HashMap<String, Object>(); map.put("count",
		 * new Integer(total));
		 * 
		 * rs.absolute(search.getPage() * search.getPageUnit() - search.getPageUnit() +
		 * 1); System.out.println("search.getPage():" + search.getPage());
		 * System.out.println("search.getPageUnit():" + search.getPageUnit());
		 * 
		 * ArrayList<Purchase> list = new ArrayList<Purchase>();
		 */
		

		while(rs.next()){
			Purchase purchase = new Purchase();
			Product product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			
			User user = new User();
			user.setUserId(rs.getString("buyer_id"));
			
			purchase.setBuyer(user);
			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setPurchaseProd(product);
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
			purchase.setOrderDate(rs.getDate("ORDER_DATA"));
			purchase.setDivyDate(rs.getString("DLVY_DATE"));
			list.add(purchase);
		}
		
		/*
		 * if (total > 0) { for (int i = 0; i < search.getPageUnit(); i++) { Purchase
		 * purchase = new Purchase(); User user = new User();
		 * user.setUserId(rs.getString("buyer_id")); Product product = new Product();
		 * product.setProdNo(rs.getInt("PROD_NO")); vo.setBuyer(user);
		 * vo.setTranNo(rs.getInt("TRAN_NO")); vo.setPurchaseProd(product);
		 * vo.setPaymentOption(rs.getString("PAYMENT_OPTION"));
		 * vo.setReceiverName(rs.getString("RECEIVER_NAME"));
		 * vo.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
		 * vo.setDivyRequest(rs.getString("DLVY_REQUEST"));
		 * vo.setTranCode(rs.getString("TRAN_STATUS_CODE"));
		 * vo.setOrderDate(rs.getDate("ORDER_DATA"));
		 * vo.setDivyDate(rs.getString("DLVY_DATE"));
		 * 
		 * list.add(vo); if (!rs.next()) break; } } System.out.println("list.size() : "
		 * + list.size()); map.put("list", list); System.out.println("map().size() : " +
		 * map.size());
		 */
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}
	
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("PurchaseDAO :: make SQL :: "+ sql);	
		
		return sql;
	}

	public void updatePurchase(Purchase purchase) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET payment_option=?, receiver_name=?, receiver_phone=?, demailaddr=?, dlvy_request=?, dlvy_date=? WHERE tran_no=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, purchase.getPaymentOption());
		stmt.setString(2, purchase.getReceiverName());
		stmt.setString(3, purchase.getReceiverPhone());
		stmt.setString(4, purchase.getDivyAddr());
		stmt.setString(5, purchase.getDivyRequest());
		stmt.setString(6, purchase.getDivyDate());
		stmt.setInt(7, purchase.getTranNo());
		
		int i = stmt.executeUpdate();
		System.out.println("1번 insert 유뮤 : " + i + " 개 행이 수정되었습니다.");

		con.close();
	}
	
	public void updateTranCode(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET tran_status_code = ? WHERE tran_no = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, purchase.getTranCode());
		stmt.setInt(2, purchase.getTranNo());
		
		int i = stmt.executeUpdate();
		System.out.println("TranCode 1번 insert 유뮤 : " + i + " 개 행이 수정되었습니다.");
		
		con.close();

	}
	 
	  
	
}
