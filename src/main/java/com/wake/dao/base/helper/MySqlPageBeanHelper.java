package com.wake.dao.base.helper;

public class MySqlPageBeanHelper extends PageHelperBase{
	
    /**
     * 得到查询总数的sql
     */
    public static String getCountString(String querySelect) {

        querySelect = formatQueryString(querySelect);
        int orderIndex = getLastOrderInsertPoint(querySelect);

        int formIndex = getAfterFormInsertPoint(querySelect);
        String select = querySelect.substring(0, formIndex);

        // 如果SELECT 中包含 DISTINCT 只能在外层包含COUNT
        if (select.toLowerCase().indexOf("select distinct") != -1 || querySelect.toLowerCase().indexOf("group by") != -1) {
            return new StringBuffer(querySelect.length()).append("select count(1) from (").append(querySelect.substring(0, orderIndex)).append(" ) t").toString();
        } else {
            return new StringBuffer(querySelect.length()).append("select count(1) ").append(querySelect.substring(formIndex, orderIndex)).toString();
        }
    }

    /**
     * 得到分页的SQL,hibernate可通过query.setFirstResult,query.setMaxResults实现;
     * 
     * @param offset
     *            偏移量
     * @param limit
     *            位置
     * @return 分页SQL
     */
    public static String getLimitString(String querySelect, int offset, int limit) {

        querySelect = formatQueryString(querySelect);

        String sql = querySelect + " limit " + offset + " ," + limit;

        return sql;

    }

}
