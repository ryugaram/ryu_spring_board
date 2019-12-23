package com.javalec.spring_project_board.dao;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.javalec.spring_project_board.dto.BDto;
import com.javalec.spring_project_board.util.Constant;

public class BDao {
	
	DataSource dataSource;
	JdbcTemplate template=null;
	
	public BDao() {
		  try {
			Context context= new InitialContext();
			dataSource=(DataSource) context.lookup("java:comp/env/ryu1");
//			System.out.println("test1");
			
		} catch (NamingException e) {
//			System.out.println("test실패1");
			e.printStackTrace();
		}
		  template =Constant.template;
		}
	
	public BDto contentView(String strID) {
		
		upHit(strID);
		
		BDto dto =null;
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		ResultSet resultSet=null;
		
		try {
			connection=dataSource.getConnection();
					
			String query="select * from spring_board where bid=?";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(strID));
			resultSet=preparedStatement.executeQuery();
			
			
			if(resultSet.next()) {
				int bid=resultSet.getInt("bid");
				String bname=resultSet.getString("bname");
				String btitle=resultSet.getString("btitle");
				String bcontent=resultSet.getString("bcontent");
				Timestamp bdate=resultSet.getTimestamp("bdate");
				int bhit=resultSet.getInt("bhit");
				int bgroup=resultSet.getInt("bgroup");
				int bstep=resultSet.getInt("bstep");
				int bindent=resultSet.getInt("bindent");
				
				dto=new BDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(resultSet!=null)resultSet.close();
				if(preparedStatement!=null)preparedStatement.close();
				if(connection !=null)connection.close();
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		
		return dto;
		
	}
	public void write(String bname,String btitle,String bcontent) {
		
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
		try {
			connection=dataSource.getConnection();
			String query="insert into spring_board(bname,btitle,bcontent,bdate,bhit,bgroup,bstep,bindent) values(?,?,?,now(),0,(SELECT MAX(bid) FROM spring_board S) +1,0,0)";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, bname);
			preparedStatement.setString(2, btitle);
			preparedStatement.setString(3, bcontent);
			
			preparedStatement.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		finally {
			try {
				if(preparedStatement!=null)preparedStatement.close();
				if(connection!=null)connection.close();
				
			}catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		}
	

	public ArrayList<BDto> list(){
	
		//String query="select * from spring_board order by bgroup desc, bstep asc";
		//return(ArrayList<BDto>) template.query(query, new BeanPropertyRowMapper<BDto>(BDto.class));
		
		
		ArrayList<BDto> dtos=new ArrayList<BDto>();
		Connection connection =null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		
		try {
			System.out.println("datasorce 테스트");
			
			connection=dataSource.getConnection();
			
			System.out.println("datasorce 테스트2");
			String query="select * from spring_board order by bgroup desc, bstep asc";
			preparedStatement =connection.prepareStatement(query);
			resultSet=preparedStatement.executeQuery();
			System.out.println("test2");
			
			while(resultSet.next()) {
				int bid=resultSet.getInt("bid");
				String bname=resultSet.getString("bname");
				String btitle=resultSet.getString("btitle");
				String bcontent=resultSet.getString("bcontent");
				Timestamp bdate=resultSet.getTimestamp("bdate");		
				int bhit=resultSet.getInt("bhit");
				int bgroup=resultSet.getInt("bgroup");
				int bstep=resultSet.getInt("bstep");
				int bindent=resultSet.getInt("bindent");
				
				BDto dto=new BDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);
				dtos.add(dto);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
//			System.out.println("test실패2");
		}
		finally {
			try {
				if(resultSet!=null)resultSet.close();
				if(preparedStatement !=null)preparedStatement.close();
				if(connection != null)connection.close();
			}
			catch (Exception e2) {
			}
		}
		return dtos;
		
}

	public void modify(String bid,String bname,String btitle,String bcontent){
		
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		
		try {
			connection=dataSource.getConnection();
			//System.out.println("ㅇㅇ");
			String query="update spring_board set bname = ?,btitle = ?,bcontent = ? where bid = ?";
			//System.out.println("ㄴㄴ");
			preparedStatement =connection.prepareStatement(query);
			preparedStatement.setString(1, bname);
			preparedStatement.setString(2, btitle);
			preparedStatement.setString(3, bcontent);
			preparedStatement.setInt(4,Integer.parseInt(bid));

			preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("오류검출");
		}finally {
			try {
				if(preparedStatement!=null) preparedStatement.close();
				if(connection!=null) connection.close();
				
			}catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
	}
	public void delete(String strID) {
		
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
		try {
			connection =dataSource.getConnection();
			String query="delete from spring_board where bid=?";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(strID));
			preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(preparedStatement!=null)preparedStatement.close();
				if(connection!=null)connection.close();
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		
	}
	public BDto reply_view(String strID) {
		
	BDto dto=null;
	Connection connection=null;
	PreparedStatement preparedStatement =null;
	ResultSet resultSet=null;
	
	try {
		connection=dataSource.getConnection();
		String query="select * from spring_board where bid=?";
		preparedStatement=connection.prepareStatement(query);
		preparedStatement.setInt(1,Integer.parseInt(strID));
		resultSet=preparedStatement.executeQuery();
		
		if(resultSet.next()) {
			int bid=resultSet.getInt("bid");
			String bname=resultSet.getString("bname");
			String btitle=resultSet.getString("btitle");
			String bcontent=resultSet.getString("bcontent");
			Timestamp bdate=resultSet.getTimestamp("bdate");
			int bhit=resultSet.getInt("bhit");
			int bgroup=resultSet.getInt("bgroup");
			int bstep=resultSet.getInt("bstep");
			int bindent=resultSet.getInt("bindent");
			
			dto= new BDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);
			
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally {
		try {
			
		if(	resultSet!=null)resultSet.close();
		if(preparedStatement!=null)preparedStatement.close();
		if(connection!=null)connection.close();
		
		}catch(Exception e2) {
			e2.printStackTrace();
		}
	}
	return dto;
		
	}
	public void reply(String bid,String bname,String btitle,String bcontent,String bgroup,String bstep,String bindent) {
		replyShape(bgroup,bstep);
		
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
		try {
			connection=dataSource.getConnection();
			String query="insert into spring_board(bname,btitle,bcontent,bgroup,bstep,bindent) values(?,?,?,?,?,?)";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, bname);
			preparedStatement.setString(2, btitle);
			preparedStatement.setString(3, bcontent);
			preparedStatement.setInt(4, Integer.parseInt(bgroup));
			preparedStatement.setInt(5, Integer.parseInt(bstep)+1);
			preparedStatement.setInt(6, Integer.parseInt(bindent)+1);
			
			preparedStatement.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(preparedStatement!=null)preparedStatement.close();
				if(connection!=null)connection.close();
				
			}catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
	}
	
	private void upHit(String bid) {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
		try {
			connection=dataSource.getConnection();
			String query="update spring_board set bhit=bhit+1 where bid=?";
			preparedStatement =connection.prepareStatement(query);
			preparedStatement.setString(1, bid);
			
			int rn=preparedStatement.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();		
		}
		finally {
			try {
				if(preparedStatement!=null)preparedStatement.close();
				if(connection !=null)connection.close();
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}
	private void replyShape( String strgroup, String strstep) {
		 // TODO Auto-generated method stub
		 Connection connection = null;
		 PreparedStatement preparedStatement = null;
		  
		 try {
		  connection=dataSource.getConnection();
		  String query = "update spring_board set bstep = bstep + 1 where bgroup = ? and bstep > ?";
		  preparedStatement = connection.prepareStatement(query);
		  preparedStatement.setInt(1, Integer.parseInt(strgroup));
		  preparedStatement.setInt(2, Integer.parseInt(strstep));
		   
		preparedStatement.executeUpdate();
		 } catch (Exception e) {
		  // TODO: handle exception
		  e.printStackTrace();
		 } finally {
		  try {
		   if(preparedStatement != null) preparedStatement.close();
		   if(connection != null) connection.close();
		  } catch (Exception e2) {
		   // TODO: handle exception
		   e2.printStackTrace();
		  }
		 }
	}
}

