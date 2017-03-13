package com.wake.dao.base.helper;

public class PageBean {
	private int pageNo = 1;// 第几页
	private int pageSize = 20;// 每页的数量
	private boolean isrolling = false;// 是否滚动加载

	private int totalCount = 0;// 总记录数
	private int totalPages = 1;// 总页数

	/**
	 * 构造函数
	 */
	public PageBean() {
	}

	/**
	 * 构造函数
	 * 
	 * @param pageNo
	 *            当前页
	 */
	public PageBean(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 构造函数
	 * 
	 * @param pageNo
	 *            当前页
	 * @param pageSize
	 *            每页记录个数
	 */
	public PageBean(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * 
	 * @param pageNo
	 *            当前页
	 * @param pageSize
	 *            每页记录个数
	 * @param isrolling
	 *            是否是滚动翻页
	 */
	public PageBean(int pageNo, int pageSize, boolean isrolling) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.isrolling = isrolling;
	}

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 获得每页的记录数量,默认为20.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 */
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
	 */
	public int getOffset() {
		return (pageNo - 1) * pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为1.
	 */
	private void calTotalPages() {
		if (totalCount <= 0) {
			totalPages = 1;
		} else {
			int pages = totalCount / pageSize;
			totalPages = totalCount % pageSize > 0 ? ++pages : pages;
		}
	}

	/**
	 * 设置总记录数.
	 */
	public void setTotalCount(final int totalCount) {
		this.totalCount = totalCount;
		calTotalPages();
		if (pageNo > totalPages && totalPages > 0) {
			if (!isrolling) {
				pageNo = totalPages;
			}
		}
	}

	/**
	 * 获取分页（Bootstrap样式）
	 * 
	 * @param pagingform
	 * @param size
	 * @return
	 */
	public String getPagination(String pagingform, int size) {
		StringBuffer pagelink = new StringBuffer();
		if (totalCount > pageSize) {
			pagelink.append("<ul class='pagination'>");

			// 首页
			pagelink.append(buildPagelinkLI(pagingform, "首页", 1, ""));
			// 上一页
			pagelink.append(buildPagelinkLI(pagingform, "上一页", pageNo - 1, pageNo <= 1 ? "disabled" : ""));
			// 分页
			int start_p = pageNo - size / 2;
			if (start_p <= 0) {
				start_p = 1;
			}
			int end_p = start_p + size - 1;
			if (end_p > totalPages) {
				end_p = totalPages;
				//重新计算start_p
				start_p = totalPages - size + 1;
				if (start_p <= 0) {
					start_p = 1;
				}
			}
			for (int i = start_p; i <= end_p; i++) {
				pagelink.append(buildPagelinkLI(pagingform, String.valueOf(i), i, i == pageNo ? "active" : ""));
			}
			// 下一页
			pagelink.append(buildPagelinkLI(pagingform, "下一页", pageNo + 1, pageNo >= totalPages ? "disabled" : ""));
			// 尾页
			pagelink.append(buildPagelinkLI(pagingform, "尾页", totalPages, ""));

			pagelink.append("</ul>");
		}
		return pagelink.toString();
	}

	private String buildPagelinkLI(String pagingform, String asign, int cpage, String liclass) {
		return "<li class='" + liclass + "'><a href='javascript:void(0)'"
				+ ((cpage < 1 || cpage > totalPages) ? ""
						: "onclick='pagingquery(\"" + pagingform + "\",\"" + cpage + "\")'")
				+ ">" + asign + "</a></li>";
	}

	/**
	 * 获取分页link,nav形式
	 * 
	 * @param formName
	 *            查询表单id
	 * @param size
	 *            显示分页个数，一般为5
	 * @return
	 */
	public String getPageLinkWithNav(String formName, int size) {
		int psize = size; // 得到每页的分页列表数
		int pageList = 1; // 初始化为第一个分页列表
		int cp = 1; // 用于存放计算后的当前页
		int temp = 1; // 用于临时存放计算得到的当前分页列表号
		String navsize = "";
		if (pageNo > totalPages)
			pageNo = totalPages;

		pageList = (pageNo + psize - 1) / psize; // 得到当前分页列表号

		temp = pageList;

		StringBuffer sb = new StringBuffer();

		String link = "<ul class='pagination' " + navsize + "> ";
		sb.append(link);
		if (totalPages != 0) {
			sb.append("<li><a href=\"").append(buildUrl(formName, 1)).append("\">").append("首页</a></li>");
		} else {
			sb.append("<li class='disabled'><a href='javascript:void(0)'>首页</a></li> ");
		}

		int maxPsize = (totalPages + psize - 1) / psize; // 总分页列表数
		if (totalPages < psize) // 如果总页数小于每页分页列表数,则只显示当前总分页数列表
			psize = totalPages;

		if (pageNo <= 1) {
			sb.append("<li class='disabled'><a href='javascript:void(0)'>").append("上一页</a></li>");
			pageList = 1;
		} else {
			if (pageList > maxPsize) { // 大于最大的分页列表数
				pageList = maxPsize;
				// cp = (pageList - 1) * psize + 1;
				cp = pageNo - 1;
			} else {
				pageList = pageList - 1;
				// cp = (pageList - 1) * psize + 1;
				cp = pageNo - 1;
			}
			sb.append("<li><a href=\"").append(buildUrl(formName, cp)).append("\">上一页</a></li>");
		}

		pageList = temp;

		int start = 0;
		int end = 0;
		int mid = size / 2;
		if (totalPages <= size) {
			start = 1;
			end = totalPages;
		} else if (totalPages - pageNo <= mid) {
			start = totalPages - size + 1;
			end = totalPages;
		} else if (pageNo <= mid) {
			start = 1;
			end = size;
		} else {
			end = pageNo + mid;
			start = end - size + 1;
		}
		for (int i = start; i <= end; i++) {
			if (i == pageNo) { // 页码必须小于或等于总页数
				sb.append("<li class='active'><a href='javascript:void(0)'>").append(i).append("</a></li>");
			} else {
				if (i <= totalPages)
					sb.append("<li><a href=\"").append(buildUrl(formName, i)).append("\">").append(i)
							.append("</a></li> ");
			}
		}

		pageList = temp;
		if (pageList < 1) {
			sb.append("<li class='disabled'><a href='javascript:void(0)'>下一页</a></li> ");
		} else {
			if (pageList > maxPsize) {
				pageList = maxPsize;
				cp = (pageList - 1) * psize + 1;
			} else {

				cp = pageNo + 1;
			}
			if (cp > totalPages) {
				sb.append("<li class='disabled'><a href='javascript:void(0)'>下一页</a></li> ");
			} else {
				sb.append("<li><a href=\"").append(buildUrl(formName, cp)).append("\">下一页</a></li> ");
			}
		}

		if (totalPages != 0) {
			sb.append("<li><a href=\"").append(buildUrl(formName, totalPages)).append("\">尾页</a></li>  ");
		} else {
			sb.append("<li class='disabled'><a href='javascript:void(0)'>尾页</a></li> ");
		}

		sb.append("</ul>");
		return sb.toString();
	}

	public String getPageLinkWithDiv(String formName, int size) {

		int psize = size; // 得到每页的分页列表数
		int pageList = 1; // 初始化为第一个分页列表
		int cp = 1; // 用于存放计算后的当前页
		int temp = 1; // 用于临时存放计算得到的当前分页列表号

		if (pageNo > totalPages)
			pageNo = totalPages;

		pageList = (pageNo + psize - 1) / psize; // 得到当前分页列表号

		temp = pageList;

		StringBuffer sb = new StringBuffer();

		String link = "<div id=\"tnt_pagination\"> ";
		sb.append(link);
		if (totalPages != 0) {
			sb.append("<a style=\" width : 50px; height: 22px;\" href=\"").append(buildUrl(formName, 1)).append("\">")
					.append("第一页</a>");
		} else {
			sb.append("<span class=\"disabled_tnt_pagination\" style=\" width : 50px; height: 22px;\">第一页</span> ");
		}

		int maxPsize = (totalPages + psize - 1) / psize; // 总分页列表数
		if (totalPages < psize) // 如果总页数小于每页分页列表数,则只显示当前总分页数列表
			psize = totalPages;

		if (pageNo <= 1) {
			sb.append("<span style=\" width : 50px; height: 22px;\" class=\"disabled_tnt_pagination\">")
					.append("上一页</span>");
			pageList = 1;
		} else {
			if (pageList > maxPsize) { // 大于最大的分页列表数
				pageList = maxPsize;
				// cp = (pageList - 1) * psize + 1;
				cp = pageNo - 1;
			} else {
				pageList = pageList - 1;
				// cp = (pageList - 1) * psize + 1;
				cp = pageNo - 1;
			}
			sb.append("<a style=\" width : 50px; height: 22px;\" href=\"").append(buildUrl(formName, cp))
					.append("\">上一页</a>");
		}

		pageList = temp;

		for (int i = (pageList - 1) * psize + 1; i <= pageList * psize; i++) {
			if (i == pageNo) { // 页码必须小于或等于总页数
				sb.append("<span style=\" height: 22px;\" class=\"active_tnt_link\">").append(i).append("</span>");
			} else {
				if (i <= totalPages)
					sb.append("<a href=\"").append(buildUrl(formName, i)).append("\">").append(i).append("</a> ");
			}
		}

		pageList = temp;
		if (pageList < 1) {
			sb.append("<span class=\"disabled_tnt_pagination\" style=\" width : 50px; height: 22px;\">下一页</span> ");
		} else {
			if (pageList > maxPsize) {
				pageList = maxPsize;
				cp = (pageList - 1) * psize + 1;
			} else {

				cp = pageNo + 1;
			}
			if (cp > totalPages) {
				sb.append("<span class=\"disabled_tnt_pagination\" style=\" width : 50px; height: 22px;\">下一页</span> ");
			} else {
				sb.append("<a style=\" width : 50px; height: 22px;\" href=\"").append(buildUrl(formName, cp))
						.append("\">下一页</a> ");
			}
		}

		if (totalPages > (pageNo + size * 2)) {
			sb.append("&nbsp;...&nbsp;");
			sb.append("<a style=\" height: 22px;\" href=\"").append(buildUrl(formName, pageNo + size * 2 - 2))
					.append("\">").append(pageNo + size * 2 - 2).append("</a> ");
			sb.append("<a style=\" height: 22px;\" href=\"").append(buildUrl(formName, pageNo + size * 2 - 1))
					.append("\">").append(pageNo + size * 2 - 1).append("</a> ");
			sb.append("&nbsp;");
		}

		if (totalPages != 0) {
			sb.append("<a style=\" width : 50px; height: 22px;\" href=\"").append(buildUrl(formName, totalPages))
					.append("\">最后页</a>  ");
		} else {
			sb.append("<span class=\"disabled_tnt_pagination\" style=\" width : 50px; height: 22px;\">最后页</span> ");
		}

		sb.append("&nbsp;共 <span style=\" color:red;font-weight:bold; \">").append(totalCount)
				.append("</span> 条记录  当前页<span style=\" color:red;font-weight:bold; \">").append(pageNo)
				.append("</span>/").append(totalPages).append("页");
		if (formName == null) {
			sb.append(
					"&nbsp;到<input type='text' id='page' name='page' style='width:40px'>页<input type='button' name='Submit' value='go' class='rb1' onclick=\"window.location.href='")
					.append("/").append("+document.getElementById('page').value\"/></div>");
		} else {
			sb.append(
					"&nbsp;跳转到<input type='text' id='page' name='page' style='width:40px'>页<input type='button' name='Submit' value='go' class='rb1' onclick=\"submitForm('")
					.append(formName).append("','/'" + "+eval(document.getElementById('page').value))\" /></div>");
		}

		return sb.toString();
	}

	private String buildUrl(String formName, int page) {
		String returnUrl = "/" + page;
		if (formName != null) {
			return "javascript:submitForm('" + formName + "','" + returnUrl + "')";
		}
		return returnUrl;
	}

}
