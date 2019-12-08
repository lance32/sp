package com.sp.bbs;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sp.common.MyUtil;

@Controller("bbs.boardController")
public class BoardController {
	@Autowired
	private BoardService service;
	
	@Autowired
	private MyUtil myUtil;
	
	@RequestMapping(value="/bbs/list")
	public String list(
			@RequestParam(value="page", defaultValue="1") int current_page,
			@RequestParam(value="searchKey", defaultValue="subject") String searchKey,
			@RequestParam(value="searchValue", defaultValue="") String searchValue,
			@RequestParam(value="spec", defaultValue="") String spec,
			HttpServletRequest req,
			Model model
			) throws Exception {

   	    String cp = req.getContextPath();
   	    
		int rows = 10;
		int total_page = 0;
		int dataCount = 0;
   	    
		if(req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
			spec = URLDecoder.decode(spec, "utf-8");
		}

		List<String> listSpec=null;
		if(spec.length()!=0)
			listSpec=Arrays.asList(spec.split(","));

        // 전체 페이지 수
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("listSpec", listSpec);
        map.put("searchKey", searchKey);
        map.put("searchValue", searchValue);

        dataCount = service.dataCount(map);
        if(dataCount != 0)
            total_page = myUtil.pageCount(rows,  dataCount) ;

        // 다른 사람이 자료를 삭제하여 전체 페이지수가 변화 된 경우
        if(total_page < current_page) 
            current_page = total_page;

        // 리스트에 출력할 데이터를 가져오기
        int start = (current_page - 1) * rows + 1;
        int end = current_page * rows;
        map.put("start", start);
        map.put("end", end);

        List<Board> list = service.listBoard(map);

        // 리스트의 번호
        int listNum, n = 0;
        Iterator<Board> it=list.iterator();
        while(it.hasNext()) {
            Board data = it.next();
            listNum = dataCount - (start + n - 1);
            data.setListNum(listNum);
            n++;
        }

        String query = "";
        String listUrl;
        String articleUrl;
        
        if(spec.length()!=0) {
        	query = "spec="+URLEncoder.encode(spec, "utf-8");
        }
        
        if(searchValue.length()!=0) {
        	if(query.length()!=0) query+="&";
        	
        	query += "searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "utf-8");	
        }
        
    	listUrl = cp+"/bbs/list";
        articleUrl = cp+"/bbs/article?page=" + current_page;
        if(query.length()!=0) {
        	listUrl = listUrl + "?" + query;
            articleUrl = articleUrl + "&"+ query;
        }
        
        String paging = myUtil.paging(current_page, total_page, listUrl);

        model.addAttribute("list", list);
        model.addAttribute("articleUrl", articleUrl);
        model.addAttribute("page", current_page);
        model.addAttribute("total_page", total_page);
        model.addAttribute("dataCount", dataCount);
        model.addAttribute("paging", paging);
		
        return "bbs/list";
	}
	
	@RequestMapping(value="/bbs/created", method=RequestMethod.GET)
	public String createdForm(Model model) throws Exception {
		model.addAttribute("mode", "created");
		return "bbs/created";
	}

	@RequestMapping(value="/bbs/created", method=RequestMethod.POST)
	public String createdSubmit(Board dto) throws Exception {
		try {
			service.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/bbs/list";
	}
	
	@RequestMapping(value="/bbs/article", method=RequestMethod.GET)
	public String readAticle(
			@RequestParam(value="num") int num, 
			@RequestParam(value="page") String page, 
			HttpServletRequest req, 
			@RequestParam(value="searchKey", defaultValue="subject") String searchKey,
			@RequestParam(value="searchValue", defaultValue="") String searchValue,
			@RequestParam(value="spec", defaultValue="") String spec,
			Model model) throws Exception {
		
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
			spec = URLDecoder.decode(spec, "utf-8");
		}
		
		String query = "page=" + page;
		if (spec.length() != 0) {
			query += "&spec=" + URLEncoder.encode(spec, "UTF-8");
		}
		if (searchValue.length() != 0) {
			query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		service.updateHitCount(num);
        
		Board dto = service.readBoard(num);
		if (dto == null)
			return "redirect:/bbs/list?" + query;
		dto.setContent(myUtil.htmlSymbols(dto.getContent()));
		
		// 이전글, 다음 글
		List<String> listSpec = null;
		if (spec.length() != 0)
			listSpec = Arrays.asList(spec.split(","));
		
		Map<String, Object> map = new HashMap<>();
		map.put("listSpec", listSpec);
		map.put("searchKey", searchKey);
		map.put("searchValue", searchValue);
		map.put("num", num);
		
        Board prevDto = service.preReadBoard(map);
        Board nextDto = service.nextReadBoard(map);
        
		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		model.addAttribute("page", page);
		model.addAttribute("query", query);
				
		return "bbs/article";
	}
	
	@RequestMapping(value="/bbs/update", method=RequestMethod.GET)
	public String updateForm(@RequestParam int num, Model model) throws Exception {
		Board dto = service.readBoard(num);
		model.addAttribute("dto", dto);
		model.addAttribute("mode", "update");
		
		return "bbs/created";
	}
	
	@RequestMapping(value="/bbs/update", method=RequestMethod.POST)
	public String updateSubmit(
			Board dto,
			@RequestParam(value="num") int num, 
			@RequestParam(value="page") String page, 
			HttpServletRequest req, 
			@RequestParam(value="searchKey", defaultValue="subject") String searchKey,
			@RequestParam(value="searchValue", defaultValue="") String searchValue,
			@RequestParam(value="spec", defaultValue="") String spec,
			Model model
			) throws Exception {
		
			service.updateBoard(dto);
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				searchValue = URLDecoder.decode(searchValue, "utf-8");
				spec = URLDecoder.decode(spec, "utf-8");
			}

			String query = "";
	        if(spec.length()!=0) {
	        	query = "spec="+URLEncoder.encode(spec, "utf-8");
	        }
	        
	        if(searchValue.length()!=0) {
	        	if(query.length()!=0) query+="&";
	        	query += "searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "utf-8");	
	        }
	       
	        
		return "redirect:/bbs/article?page=" + page + "&num=" + num + "&" + query;
	}
	
	@RequestMapping(value="/bbs/delete", method=RequestMethod.GET)
	public String deleteArticle(
			@RequestParam(value="num") int num, 
			@RequestParam(value="page") String page, 
			HttpServletRequest req, 
			@RequestParam(value="searchKey", defaultValue="subject") String searchKey,
			@RequestParam(value="searchValue", defaultValue="") String searchValue,
			@RequestParam(value="spec", defaultValue="") String spec			
			) throws Exception {
		
		service.deleteBoard(num);
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "utf-8");
			spec = URLDecoder.decode(spec, "utf-8");
		}
		
		String query = "page=" + page;
		if (spec.length() != 0) {
			query += "&spec=" + URLEncoder.encode(spec, "UTF-8");
		}
		if (searchValue.length() != 0) {
			query += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		return "redirect:/bbs/list?"  + query;
	}
}
