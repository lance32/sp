package com.sp.bbs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.common.dao.CommonDAO;

@Service("bbs.boardService")
public class BoardServiceImpl implements BoardService {
	@Autowired
	private CommonDAO dao;
	
	@Override
	public void insertBoard(Board dto) throws Exception {
		try {
			dao.insertData("bbs.insertBoard", dto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public int dataCount(Map<String, Object> map) throws Exception {
		int result = 0;
		try {
			result = dao.selectOne("bbs.dataCount", map);
		} catch (Exception e) {
			throw e;
		}
		
		return result;
	}

	@Override
	public List<Board> listBoard(Map<String, Object> map) throws Exception {
		List<Board> list = null;
		try {
			list = dao.selectList("bbs.boardList", map);
		} catch(Exception e) {
			throw e; 
		}
		return list;
	}

	@Override
	public Board readBoard(int num) throws Exception {
		Board dto = null;
		
		try {
			dto = dao.selectOne("bbs.readBoard", num);
		} catch (Exception e) {
			throw e;
		}
		return dto;
	}

	@Override
	public Board preReadBoard(Map<String, Object> map) throws Exception {
		Board dto = null;
		
		try {
			dto = dao.selectOne("bbs.preReadBoard", map);
		} catch (Exception e) {
			throw e;
		}
		return dto;
	}

	@Override
	public Board nextReadBoard(Map<String, Object> map) throws Exception {
		Board dto = null;
		
		try {
			dto = dao.selectOne("bbs.nextReadBoard", map);
		} catch (Exception e) {
			throw e;
		}
		return dto;
	}

	@Override
	public void updateHitCount(int num) throws Exception {
		try {
			dao.updateData("bbs.updateHitCount", num);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void updateBoard(Board dto) throws Exception {
		try {
			System.out.println(dto);
			dao.updateData("bbs.updateBoard", dto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void deleteBoard(int num) throws Exception {
		try {
			dao.updateData("bbs.deleteBoard", num);
		} catch (Exception e) {
			throw e;
		}
	}

}
