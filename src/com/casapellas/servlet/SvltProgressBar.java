package com.casapellas.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SvltProgressBar")
public class SvltProgressBar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SvltProgressBar() {
		super();
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;

		try {
			out = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			if (request.getSession().getAttribute("porcentajeavanceproceso") == null) {
				out.println("{ \"valueIncrement\": 0 }");
			} else {
				
				int progreso = (Integer) request.getSession().getAttribute("porcentajeavanceproceso");
				out.println("{ \"valueIncrement\": " + progreso + "}");
				
			}

		} catch (Exception e) {

		}

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse restponse) throws ServletException, IOException {
		processRequest(request, restponse);
	}

}
