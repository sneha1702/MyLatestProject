package indexUtility;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
   
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("in servlet");
		int page = 1;
        int recordsPerPage = 5;
        
        try {
        
			if(request.getParameter("page") != null)
			{
	            page = Integer.parseInt(request.getParameter("page"));
	            System.out.println("page num = "+page);
			}
			String query= request.getParameter("q");
			System.out.println("query is " + query);
			
			XMLSearcher searcher = new XMLSearcher();
			searcher.search(query);
	        
			List<IndexDocument> list = searcher.retrievePageResults((page-1)*recordsPerPage,
                    recordsPerPage*page);
			int noOfRecords = searcher.getTotalResultNum();
			searcher.closeIndexDirectory();
			
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
	        request.setAttribute("itemList", list);
	        request.setAttribute("noOfPages", noOfPages);
	        request.setAttribute("currentPage", page);
	        request.setAttribute("q", query);
	        
	        RequestDispatcher view = request.getRequestDispatcher("DisplaySearchResults.jsp");
	        view.forward(request, response);
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
