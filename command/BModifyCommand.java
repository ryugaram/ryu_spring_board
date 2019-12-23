package com.javalec.spring_project_board.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.javalec.spring_project_board.dao.BDao;

public class BModifyCommand implements BCommand {

	@Override
	public void execute(Model model) {
	
		Map<String, Object> map =model.asMap();
		HttpServletRequest request=(HttpServletRequest) map.get("request");
		
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("content");
		
		BDao dao=new BDao();
		dao.modify(bid,bname,btitle,bcontent);

	}

}
