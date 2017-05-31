package com.sdf.manager.weixin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sdf.manager.weixin.dao.WeixinDao;



/**
 * JdbcTemplate的执行dao层类
 * @author Administrator
 *
 */
@Repository("weixinDao")
public class WeixinDaoImpl implements WeixinDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	/**
	 * JdbcTemplate测试方法
	 * 
	 */
	public Map<String,Object> findALL(int page,int rows,String tableName) {
		
		Map<String,Object> returnData = new HashMap<String, Object>();
		
		
		
		StringBuffer sql = new StringBuffer("SELECT * FROM ");
		//连接表名
		sql.append(" "+tableName + "   ORDER BY CREATE_TIME DESC ");// WHERE ORIGIN = '9'是补录数据
		
       /*
        * //1.测试查询sql
        *  String sql = "select * from T_SDF_STATIONS";
        return jdbcTemplate.queryForList(sql); */   
        
        //2.测试插入数据sql
       /* jdbcTemplate.update("insert into STUDENT(id,name,number) values(?,?,?)",   
                new PreparedStatementSetter(){  
              
                    public void setValues(PreparedStatement ps) throws SQLException {  
                        ps.setInt(1, 2);  
                        ps.setString(2, "test");  
                        ps.setInt(3, 123);  
                    }  
        });   */
		
		int pageAll = this.getAllPageNum(sql.toString());//获取数据总数
		
		sql.append(" LIMIT "+(page-1)*rows+","+rows);//放置分页参数，limit m,n：n是指从第m+1条开始，取n条；其中m是指记录开始的index，从0开始，表示第一条记录
		
		returnData.put("totalRecord", pageAll);//放置数据总数
		returnData.put("resultList", jdbcTemplate.queryForList(sql.toString()));//放置数据总数
        
		return returnData;
    }
	
	/**
	 * 获取当前符合条件的所有数据量总数
	 * @param sql
	 * @return
	 */
	private int getAllPageNum(String sql)
	{
		int pageAll = 0;
		
		List allData = jdbcTemplate.queryForList(sql);
		
		pageAll = allData.size();
		
		
		return pageAll;
	}
	
	public boolean deleteById(String tableName,String id)
	{
		boolean flag = false;
		
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(" "+tableName+" where id="+id+"");
		
		int deleteNum = jdbcTemplate.update(sql.toString());
		
		if(deleteNum>0)
		{
			flag = true;
		}
		
		return flag;
	}
	
	
	
}
