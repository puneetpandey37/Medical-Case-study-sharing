package com.elyx.common.util;

public class ElyxUtility {

	String rootURL = "http://52.25.160.124:8080/elyx-api/";
	String nextURL = "";

	public String getDataUrl(String resource, long id, int limit) {
		nextURL = rootURL + resource + "?limit=" + limit;
		return nextURL;
	}

	public String getUserDynamicQuery(String orderBy, String order,
			Integer limit, Integer page, long loggedInUserId) {

		int start;
		String query;

		if (orderBy != null) {
			start = (page - 1) * limit;
			query = "select c from Case c where c.user.id = " + loggedInUserId
					+ " " + " order by " + orderBy + " " + order + " "
					+ " limit " + start + "," + limit;
		} else {
			query = "select c from Case c where c.user.id = " + loggedInUserId +" " + " order by c.id desc";
		}

		return query;
	}
}
