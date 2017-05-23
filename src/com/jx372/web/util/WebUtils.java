package com.jx372.web.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtils {

	public static String checkParameter( String value, String defaultValue ) {
		if( value == null ) {
			return defaultValue;
		}
		
		return value;
	}
	
	public static Long checkParameter( String value, Long defaultValue ) {
		if( value == null || value.matches("\\d+") == false ) {
			return defaultValue;
		}
		
		return Long.parseLong( value );
	}

	public static int checkParameter( String value, int defaultValue ) {
		if( value == null || value.matches("\\d+") == false ) {
			return defaultValue;
		}
		
		return Integer.parseInt( value );
	}
	
	public static void redirect(
		String url,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
		response.sendRedirect( url );
	}
	
	public static void forward(
			String path, 
			HttpServletRequest request,
			HttpServletResponse response ) 
			throws IOException, ServletException {
		
		RequestDispatcher rd = request.getRequestDispatcher( path );
		rd.forward( request, response );		
	}
}
