package com.sdf.manager.user.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.bean.TreeBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.user.MenuBean;
import com.sdf.manager.user.bean.AccountBean;
import com.sdf.manager.user.bean.AuthorityBean;
import com.sdf.manager.user.dto.AuthorityDTO;
import com.sdf.manager.user.entity.Authority;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.service.AuthService;
import com.sdf.manager.user.service.UserService;

/** 
  * @ClassName: MenuController 
  * @Description: 目录相关控制层
  * @author songj@sdfcp.com
  * @date 2015年9月23日 下午5:21:54 
  *  
  */
@Controller
@RequestMapping("/menu")
public class MenuController extends GlobalExceptionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	
    @Autowired
	private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    
    /**
	 * 
	* @Description: TODO(根据静态变量名称获取变量名称值，方便对静态变量值的统一管理) 
	* @author bann@sdfcp.com
	* @date 2015年10月30日 下午1:36:41
	 */
	@RequestMapping(value = "/getConstant", method = RequestMethod.GET)
	public @ResponseBody ResultBean getConstant(HttpSession httpSession,
			@RequestParam(value="constantName",required=true) String constantName,
			ModelMap model) throws Exception {
		ResultBean resultBean = new ResultBean ();
		
		if("ORIGIN_AUTH_ID".equals(constantName))//获取权限表的虚拟根节点值
		{
			resultBean.setMessage(Constants.ORIGIN_AUTH_ID);
		}
		
		if("PROVINCE_ALL".equals(constantName))//获取省份全部的code
		{
			resultBean.setMessage(Constants.PROVINCE_ALL);
		}
		
		return resultBean;
		
	}
	
    /**
	 * demo登录提交后跳转方法
	 * @param userName
	 * @param password
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getNewPage", method = RequestMethod.POST)
	public String getNewPage(HttpSession httpSession,
			@RequestParam(value="code",required=false) String code,//登录名是user表中的code
			@RequestParam(value="password",required=false) String password,
			ModelMap model) throws Exception {
		

		String message ="success";
		
		User user = userService.getUserByCode(code);
		if(null == user)
		{
			message = "0";//当前登录名不存在，请确认后登录!
		}
		else if("".equals(password))
		{
			message = "1";//登录密码不可以为空!
		}
		else if(null != user && !user.getPassword().equals(password))
		{
			message = "2";//登录密码不正确，请确认后登录!
		}
		else
		{
			//向session中写入登录信息
			LoginUtils.setLoginUserMessage(httpSession, code, password, user.getName(),user.getId());
		}
		
		 //日志输出
		   logger.info("用户登录：登录信息用户名："+code+"密码："+password+"登录状态："+message);
		   
		
		
		model.addAttribute("message", message);
		return "user/test";//"user/test"
	}
	
	/**
	 * 通行证组管理菜单
	 * @return
	 */
	@RequestMapping(value = "/logout.action", method = RequestMethod.GET)
	public String logout()
	{
		String indexPage = "index";
		
		
		return indexPage;
	}
	
	/**
	 * 通行证组管理菜单
	 * @return
	 */
	@RequestMapping(value = "/userGroupManage.action", method = RequestMethod.GET)
	public String userGroupManage()
	{
		String indexPage = "userGroup/userGroupManage";
		
		
		return indexPage;
	}
	
	/**
	 * 角色管理菜单
	 * @return
	 */
	@RequestMapping(value = "/roleManage.action", method = RequestMethod.GET)
	public String roleManage()
	{
		String indexPage = "index";
		
		
		return indexPage;
	}
	
	/**
	 * 账号管理菜单
	 * @return
	 */
	@RequestMapping(value = "/useraccount.action", method = RequestMethod.GET)
	public String useraccount()
	{
		String indexPage = "user/useraccount";
		
		
		return indexPage;
	}
	
	/**
	 * 权限管理菜单
	 * @return
	 */
	@RequestMapping(value = "/authority.action", method = RequestMethod.GET)
	public String authority()
	{
		String indexPage = "user/authority";
		
		
		return indexPage;
	}
	
	
	/**
	 * 应用管理菜单
	 * @return
	 */
	@RequestMapping(value = "/appManage.action", method = RequestMethod.GET)
	public String appManage()
	{
		String indexPage = "app/appManage";
		
		
		return indexPage;
	}
	
	/**
	 * 应用区域单价管理菜单
	 * @return
	 */
	@RequestMapping(value = "/appUpriceManage.action", method = RequestMethod.GET)
	public String appUpriceManage()
	{
		String indexPage = "appUnitPrice/appUpriceManage";
		
		
		return indexPage;
	}
	
	/**
	 * 应用版本管理菜单
	 * @return
	 */
	@RequestMapping(value = "/appVersionManage.action", method = RequestMethod.GET)
	public String appVersionManage()
	{
		String indexPage = "appversion/appVersionManage";
		
		
		return indexPage;
	}
	
	/**
	 * 应用公告管理菜单
	 * @return
	 */
	@RequestMapping(value = "/noticeManage.action", method = RequestMethod.GET)
	public String noticeManage()
	{
		String indexPage = "notice/noticeManage";
		
		
		return indexPage;
	}
	
	/**
	 * 开奖公告补录管理菜单
	 * @return
	 */
	@RequestMapping(value = "/kaijiangNoticeManage.action", method = RequestMethod.GET)
	public String kaijiangNoticeManage()
	{
		String indexPage = "notice/kaijiangNoticeManage";
		
		
		return indexPage;
	}
	
	/**
	 * 预测信息管理菜单
	 * @return
	 */
	@RequestMapping(value = "/forecastManage.action", method = RequestMethod.GET)
	public String forecastManage()
	{
		String indexPage = "forecast/forecastManage";
		
		
		return indexPage;
	}
	
	/**
	 * 订单管理菜单
	 * @return
	 */
	@RequestMapping(value = "/orderManage.action", method = RequestMethod.GET)
	public String orderManage()
	{
		String indexPage = "orderFGoods/orderManage";
		
		
		return indexPage;
	}
	
	/**
	 * 公司公告管理菜单
	 * @return
	 */
	@RequestMapping(value = "/companynoticeManage.action", method = RequestMethod.GET)
	public String companynoticeManage()
	{
		String indexPage = "companyNotice/companynoticeManage";
		
		
		return indexPage;
	}
    
	
	/**
	 * 文章管理菜单
	 * @return
	 */
	@RequestMapping(value = "/articleManage.action", method = RequestMethod.GET)
	public String articleManage()
	{
		String indexPage = "comWebProxyApply/articleManage";
		
		
		return indexPage;
	}
	
	/**
	 * 代理申请管理菜单
	 * @return
	 */
	@RequestMapping(value = "/proxyApplyFCwebManage.action", method = RequestMethod.GET)
	public String proxyApplyFCwebManage()
	{
		String indexPage = "comWebProxyApply/proxyApplyFCwebManage";
		
		
		return indexPage;
	}
	
	/**
	 * 通告管理菜单
	 * @return
	 */
	@RequestMapping(value = "/announcementManage.action", method = RequestMethod.GET)
	public String announcementManage()
	{
		String indexPage = "announcement/announcementManage";
		
		
		return indexPage;
	}
	
	/**
	 * 常见问题管理菜单
	 * @return
	 */
	@RequestMapping(value = "/wxCommonProblemManage.action", method = RequestMethod.GET)
	public String wxCommonProblemManage()
	{
		String indexPage = "weixin/wxCommonProblemManage";
		
		
		return indexPage;
	}
    
	/**
	 * 补录信息管理菜单
	 * @return
	 */
	@RequestMapping(value = "/lotteryPlayManage.action", method = RequestMethod.GET)
	public String lotteryPlayManage()
	{
		String indexPage = "weixin/lotteryPlayManage";
		
		
		return indexPage;
	}
	
	/**
	 * 补录数据管理菜单
	 * @return
	 */
	@RequestMapping(value = "/numOfMakeupFromWeixinManage.action", method = RequestMethod.GET)
	public String numOfMakeupFromWeixinManage()
	{
		String indexPage = "weixin/numOfMakeupFromWeixinManage";
		
		
		return indexPage;
	}
	
	
	/**
	 * 补录方案管理菜单
	 * @return
	 */
	@RequestMapping(value = "/lotteryPlayBuluPlanManage.action", method = RequestMethod.GET)
	public String lotteryPlayBuluPlanManage()
	{
		String indexPage = "weixin/lotteryPlayBuluPlanManage";
		
		
		return indexPage;
	}
	
	/**
	 * 应用广告管理菜单
	 * @return
	 */
	@RequestMapping(value = "/adManage.action", method = RequestMethod.GET)
	public String adManage()
	{
		String indexPage = "ad/adManage";
		
		
		return indexPage;
	}
	
	/**
	 * 应用广告管理菜单
	 * @return
	 */
	@RequestMapping(value = "/stationAppAdManage.action", method = RequestMethod.GET)
	public String stationAppAdManage()
	{
		String indexPage = "stationAppAdManage/stationAppAdManage";
		
		
		return indexPage;
	}
	
	/**
	 * 跳转到错误页
	 * @return
	 */
	@RequestMapping(value = "/error.action", method = RequestMethod.GET)
	public String error()
	{
		String indexPage = "error";
		
		
		return indexPage;
	}
	
	
    
	
	
    
	/**
	 * 
	* @Title: save 
	* @author banna    
	* @date 2015年9月22日 上午10:11:56  
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/getMenuData", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getMenuData(ModelMap model,HttpSession httpSession) throws Exception {
		
		/**
		 * 动态获取菜单实现
		 * 1.从session中获取当前登录人员的code，根据code获取当前用户对应的角色
		 * 2.便利角色，根据角色获取当前角色对应的权限数据
		 * 3.整理权限数据，将权限数据去重展示
		 * 4.%“baisic”根不动，前台获取也是根据这边name来获取的%
		 */
		//获取session中的登录数据
		String code = LoginUtils.getAuthenticatedUserCode(httpSession);
		User user = userService.getUserByCode(code);
		//获取当前登录人员的角色list
		List<Role> roles = user.getRoles();
		
		Set<Authority> authorities = new HashSet<Authority> ();
		
		for (Role role : roles) {
			
			List<Authority> authin = new ArrayList<Authority> ();
			authin = role.getAuthorities();
			for (Authority authority : authin) {
				if(Constants.IS_NOT_DELETED.equals(authority.getIsDeleted()))//Constants.IS_NOT_DELETED:未删除的权限数据
				{
					authorities.add(authority);
				}
				
			}
			
		}
		
		Map<String,Object> child = new HashMap<String,Object> ();
		List<MenuBean> menubeans = new ArrayList<MenuBean> ();
		child.put("basic", menubeans);
		
		Iterator<Authority> it = authorities.iterator();
		String parentId ;
		while(it.hasNext())
		{
			Authority authin = it.next();
			//如果父节点是根节点的则是菜单的一级菜单
			if(Constants.ORIGIN_AUTH_ID.equals(authin.getParentAuth()))//Constants.ORIGIN_AUTH_ID:权限树的树根节点写死我“1”
			{
				parentId = authin.getId();
				
				List<MenuBean> menus = new ArrayList<MenuBean> ();//下级菜单
				MenuBean mb = new MenuBean();
				mb.setMenuid(authin.getId());//权限id
				mb.setIcon(authin.getAuthImg());//权限图片
				mb.setMenuname(authin.getAuthName());//权限名称
				mb.setMenus(menus);
				menubeans.add(mb);
				
				menus = getChildMenu(parentId, authorities, menus);//获取下级菜单
				
				
				
			}
		}
		
		
		
		/*MenuBean mb = new MenuBean();
		mb.setMenuid("10");
		mb.setIcon("icon-sys");
		mb.setMenuname("用户");
		mb.setMenus(menus);
		menubeans.add(mb);
		
		MenuBean mb5 = new MenuBean();
		mb5.setMenuid("112");
		mb5.setIcon("icon-nav");
		mb5.setMenuname("帐号管理");
		mb5.setUrl("/user/useraccount.jsp");
		menus.add(mb5);
		
		MenuBean mb1 = new MenuBean();
		mb1.setMenuid("111");
		mb1.setIcon("icon-nav");
		mb1.setMenuname("站点管理");
		mb1.setUrl("/station/stationmanager.jsp");
		menus.add(mb1);
		
		MenuBean mb2 = new MenuBean();
		mb2.setMenuid("113");
		mb2.setIcon("icon-nav");
		mb2.setMenuname("添加站点");
		mb2.setUrl("#");
		menus.add(mb2);
		
		MenuBean mb3 = new MenuBean();
		mb3.setMenuid("114");
		mb3.setIcon("icon-nav");
		mb3.setMenuname("权限管理");
		mb3.setUrl("/user/authority.jsp");
		menus.add(mb3);
		
		MenuBean mb4 = new MenuBean();
		mb4.setMenuid("115");
		mb4.setIcon("icon-nav");
		mb4.setMenuname("角色管理");
		mb4.setUrl("/user/roleManage.jsp");
		menus.add(mb4);
		
		MenuBean m2b = new MenuBean();
		m2b.setMenuid("20");
		m2b.setIcon("icon-sys");
		m2b.setMenuname("商品");
		m2b.setMenus(menus2);
		menubeans.add(m2b);
		
		MenuBean mb21 = new MenuBean();
		mb21.setMenuid("211");
		mb21.setIcon("icon-nav");
		mb21.setMenuname("商品管理");
		mb21.setUrl("/user/basic.jsp");
		menus2.add(mb21);
		
		MenuBean mb22 = new MenuBean();
		mb22.setMenuid("113");
		mb22.setIcon("icon-nav");
		mb22.setMenuname("购买商品");
		mb22.setUrl("#");
		menus2.add(mb22);*/
		
		return child;
	}
	
	/**
	 * 
	* @Description: TODO(获取下级菜单) 
	* @author bann@sdfcp.com
	* @date 2015年10月29日 下午2:09:16
	 */
	private List<MenuBean> getChildMenu(String parentAuthId,Set<Authority> authorities,List<MenuBean> menus)
	{
		
		Iterator<Authority> itin = authorities.iterator();
		
		while(itin.hasNext())
		{
			Authority authchild = itin.next();
			if(parentAuthId.equals(authchild.getParentAuth()))//Constants.ORIGIN_AUTH_ID:权限树的树根节点写死我“1”
			{
				
				MenuBean mb5 = new MenuBean();
				mb5.setMenuid(authchild.getId());
				mb5.setIcon(authchild.getAuthImg());
				mb5.setMenuname(authchild.getAuthName());
				mb5.setUrl(authchild.getUrl());
				
				List<MenuBean> menuChilds = new ArrayList<MenuBean> ();
				menuChilds = getChildMenu(authchild.getId(), authorities, menuChilds);
				if(null != menuChilds &&menuChilds.size()>0)
				{
					mb5.setMenus(menuChilds);
				}
				
				menus.add(mb5);
			}
		}
		
		return menus;
	}
	
	/**
	 * 
	* @Description: TODO(保存或者修改权限) 
	* @author bann@sdfcp.com
	* @date 2015年10月9日 下午2:38:35
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
	public @ResponseBody ResultBean saveOrUpdate(
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="code",required=false) String code,
			@RequestParam(value="authName",required=false) String authName,
			@RequestParam(value="parentAuth",required=false) String parentAuth,
			@RequestParam(value="url",required=false) String url,
			@RequestParam(value="authImg",required=false) String authImg,
			@RequestParam(value="status",required=false) String status,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		ResultBean returnMap = new ResultBean();
		
		Authority authority;
		
		authority = authService.getAuthorityByCode(id);//判断当前code所属的auth是否已存在，若存在则进行修改操作
		
		if(null != authority)//已存在，进行权限数据的修改操作
		{
			authority.setId(id);
			authority.setCode(code);
			authority.setAuthName(authName);
			authority.setParentAuth(parentAuth);
			authority.setUrl(url);
			authority.setAuthImg(authImg);
			String originStatus = authority.getStatus();//记录修改前的权限状态
			authority.setStatus(status);
			authority.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			authority.setModifyTime(new Timestamp(System.currentTimeMillis()));
			//修改权限数据
			authService.save(authority);
			
			if(!originStatus.equals(status))//若待修改数据的启用状态发生变化，则要判断是否有子级权限，若有则要批量修改启用状态
			{
				List<Authority> authList = new ArrayList<Authority> ();
				authList = getChildauthByRecursive(authList, id, model, httpSession);
				
				if(authList.size()>0)//拥有子级权限，对子级权限的启用状态进行修改
				{
					for (Authority authority2 : authList) 
					{
						authority2.setStatus(status);
						authority2.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
						authority2.setModifyTime(new Timestamp(System.currentTimeMillis()));
						authService.save(authority2);
					}
				}
			}
			
			
			returnMap.setMessage("修改权限成功!");
			returnMap.setStatus("success");
			
		  //日志输出
		   logger.info("修改权限--权限id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		}
		else
		{
			authority = new Authority();
			authority.setCode(code);
			authority.setAuthName(authName);
			authority.setParentAuth(parentAuth);
			authority.setUrl(url);
			authority.setAuthImg(authImg);
			authority.setStatus(status);
			authority.setIsSystem("0");//页面中操作添加的权限都是非系统数据
			authority.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			authority.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			authority.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			authority.setModifyTime(new Timestamp(System.currentTimeMillis()));
			authority.setIsDeleted("1");
			//保存权限数据
			authService.save(authority);
			
			returnMap.setMessage("保存权限成功!");
			returnMap.setStatus("success");
			
			 //日志输出
			logger.info("保存权限--权限code="+code+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				 
		}
		
		
		
		return returnMap;
	}
	
	/**
	 * 
	* @Description: TODO(使用递归方法获取所有子级权限数据) 
	* @author bann@sdfcp.com
	* @date 2015年10月16日 下午2:19:41
	 */
	private List<Authority> getChildauthByRecursive(List<Authority> authList,String parentAuth,ModelMap model,HttpSession httpSession)
	{
		List<Authority> authListget = getChildAuthList(parentAuth, model, httpSession);
		
		if(authListget.size()>0)
		{
			for (Authority authority : authListget) {
				
				authList.add(authority);
				
				authListget = getChildauthByRecursive(authList, authority.getId(), model, httpSession);
			}
		}
		
		return authList;
	}
	
	
	/**
	 * 
	* @Description: TODO(根据code获取权限的详细信息（根据唯一条件获取数据）) 
	* @author bann@sdfcp.com
	* @date 2015年10月10日 上午10:16:35
	 */
	@RequestMapping(value = "/getDetailAuth", method = RequestMethod.GET)
	public @ResponseBody AuthorityDTO getDetailAuth(
			@RequestParam(value="code",required=false) String code,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		Authority authority = new Authority();
		
		authority = authService.getAuthorityByCode(code);
		
		AuthorityDTO authorityDTO = authService.toDTO(authority);
		
		return authorityDTO;
	}
	
	
	/**
	 * 
	* @Description: TODO(获取权限列表数据) 
	* @author bann@sdfcp.com
	* @date 2015年10月10日 下午3:14:46
	 */
	@RequestMapping(value = "/getParentAuth", method = RequestMethod.POST)
	public @ResponseBody List<AuthorityBean> getParentAuth(
			@RequestParam(value="status",required=false) String status,
			@RequestParam(value="code",required=false) String code,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		//放置分页参数
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
		
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add("1");//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		
		if(null != code && !"".equals(code))
		{
			params.add(code);//不查询自身数据，前台标记唯一一条数据使用的是id
			buffer.append(" and  id != ?").append(params.size());
		}
		
		if(null != status && !"".equals(status))
		{
			params.add(status);//只查询有效的数据
			buffer.append(" and  status = ?").append(params.size());
		}
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("id", "asc");
		
		QueryResult<Authority> authlist = authService.getAuthList(Authority.class, buffer.toString(), params.toArray(),
				orderBy, pageable);
		
		List<Authority> authes = authlist.getResultList();
		
		List<AuthorityBean> authBeanlist = new ArrayList<AuthorityBean>();
		
		
		for (Authority authority : authes) 
		{
			AuthorityBean authbeanin = new AuthorityBean();
			
			authbeanin.setCode(authority.getId());
			authbeanin.setAuthName(authority.getAuthName());
			
			authBeanlist.add(authbeanin);
		}
		
		
		return authBeanlist;
	}
	
	/**
	 * 
	* @Description: TODO(查询权限数据带分页) 
	* @author bann@sdfcp.com
	* @date 2015年10月14日 上午8:58:45
	 */
	@RequestMapping(value = "/getAuthList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAuthList(
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="status",required=false) String status,
			@RequestParam(value="parentCode",required=false) String parentCode,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		Map<String,Object> returnData = new HashMap<String,Object> ();
		
		//放置分页参数
		Pageable pageable = new PageRequest(page-1,rows);
		
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add("1");//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		
		if(null != status && !"".equals(status))
		{
			params.add(status);//只查询有效的数据
			buffer.append(" and status = ?").append(params.size());
		}
		
		if(null != parentCode && !"".equals(parentCode))
		{
			params.add(parentCode);//查询父级code为当前值的数据
			buffer.append(" and parentAuth = ?").append(params.size());
		}
		
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("code", "desc");
		
		QueryResult<Authority> authlist = authService.getAuthList(Authority.class, buffer.toString(), params.toArray(),
				orderBy, pageable);
		
		//处理返回数据
		List<Authority> authorities = authlist.getResultList();
		Long totalrow = authlist.getTotalRecord();
		
		List<AuthorityDTO> authorityDTOs = authService.toDTOS(authorities);
		
		returnData.put("rows", authorityDTOs);
		returnData.put("total", totalrow);
		
		return returnData;
	}
	
	/**
	 * 
	* @Description: TODO(删除权限数据) 
	* @author bann@sdfcp.com
	* @date 2015年10月12日 下午4:02:56
	 */
	@RequestMapping(value = "/deleteAuth", method = RequestMethod.POST)
	public @ResponseBody ResultBean deleteAuth(
			@RequestParam(value="codes",required=false) String[] codes,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		
		Authority authority ;
		for (String code : codes) 
		{
			authority = new Authority();
			authority =  authService.getAuthorityByCode(code);//code传递进去的参数实际是id
			authority.setIsDeleted("0");;//设置当前数据为已删除状态
			authority.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			authority.setModifyTime(new Timestamp(System.currentTimeMillis()));
			authService.save(authority);//保存更改状态的权限实体
			
			 //日志输出
			logger.info("删除权限--权限id="+code+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				
		}
		
		
		resultBean.setStatus("success");
		resultBean.setMessage("删除成功!");
		
		return resultBean;
	}
	
	
	/**
	 * 
	* @Description: 获取登陆人信息
	* @author bann@sdfcp.com
	* @date 2015年12月3日 上午9:34:47
	 */
	@RequestMapping(value = "/getLoginmsg", method = RequestMethod.POST)
	public @ResponseBody ResultBean getLoginmsg(
			ModelMap model,HttpSession httpSession) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		
		String name = LoginUtils.getAuthenticatedUserName(httpSession);
		
		resultBean.setMessage(name);
		
		return resultBean;
	}
	
	
	/**
	 * 
	* @Description: TODO(获取权限页面的权限树) 
	* @author bann@sdfcp.com
	* @date 2015年10月14日 下午2:59:13
	 */
	@RequestMapping(value = "/getTreedata", method = RequestMethod.POST)
	public @ResponseBody List<TreeBean> getTreedata(ModelMap model,HttpSession httpSession) throws Exception {
		
		
		
		//放置分页参数
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
		
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add("1");//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		
		params.add("1");//树种只显示启用状态的权限数据
		buffer.append(" and  status = ?").append(params.size());
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("code", "desc");
		
		QueryResult<Authority> authlist = authService.getAuthList(Authority.class, buffer.toString(), params.toArray(),
				orderBy, pageable);
		
		List<Authority> authes = authlist.getResultList();
		
		List<TreeBean> treeBeanList = new ArrayList<TreeBean> ();
		
		for (Authority authority : authes) {
			
			TreeBean treeBeanIn = new TreeBean();
			treeBeanIn.setId(authority.getId());
//			treeBeanIn.setParent(true);
			treeBeanIn.setName(authority.getAuthName());
			treeBeanIn.setOpen(true);
			treeBeanIn.setpId(authority.getParentAuth());
			treeBeanList.add(treeBeanIn);
		}
		
		return treeBeanList;
	}
	
	/**
	 * 
	* @Description: TODO(获取子级权限列表) 
	* @author bann@sdfcp.com
	* @date 2015年10月16日 下午2:14:55
	 */
	@RequestMapping(value = "/getChildAuthList", method = RequestMethod.POST)
	public @ResponseBody List<Authority> getChildAuthList(@RequestParam(value="parentAuth",required=false) String parentAuth,
			ModelMap model,HttpSession httpSession)
	{
		List<Authority> authList = new ArrayList<Authority> ();
		
		//放置分页参数
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
		
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add("1");//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		//连接父级权限id查询条件，用来查询当前id下是否有子级权限
		if(null != parentAuth && !"".equals(parentAuth))
		{
			params.add(parentAuth);
			buffer.append(" and parentAuth = ?").append(params.size());
		}
		
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		
		QueryResult<Authority> authlist = authService.getAuthList(Authority.class, buffer.toString(), params.toArray(),
				orderBy, pageable);
		
		authList = authlist.getResultList();
		
		return authList;
	}
	
	/**
	 * 
	* @Description: TODO(权限输入值校验，用来校验code唯一性和authname唯一性) 
	* @author bann@sdfcp.com
	* @date 2015年10月15日 上午10:55:07
	 */
	@RequestMapping(value = "/checkValue", method = RequestMethod.POST)
	public @ResponseBody ResultBean  checkValue(
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="code",required=false) String code,
			@RequestParam(value="authname",required=false) String authname,
			@RequestParam(value="parentAuth",required=false) String parentAuth,
			@RequestParam(value="status",required=false) String status,
			ModelMap model,HttpSession httpSession) throws Exception {
		
		ResultBean resultBean = new ResultBean ();
		
		//放置分页参数
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
		
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add("1");//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		
		if(null != code && !"".equals(code))
		{
			params.add(code);
			buffer.append(" and code = ?").append(params.size());
		}
		
		if(null != authname && !"".equals(authname))
		{
			params.add(authname);
			buffer.append(" and authName = ?").append(params.size());
		}
		
		if(null != id && !"".equals(id))
		{//校验修改中的值的唯一性
			params.add(id);
			buffer.append(" and id != ?").append(params.size());
		}
		
		//连接父级权限id查询条件，用来查询当前id下是否有子级权限
		if(null != parentAuth && !"".equals(parentAuth))
		{
			params.add(parentAuth);
			buffer.append(" and parentAuth = ?").append(params.size());
		}
		
		//连接状态条件
		if(null != status && !"".equals(status))
		{
			params.add(status);
			buffer.append(" and status = ?").append(params.size());
		}
		
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		
		QueryResult<Authority> authlist = authService.getAuthList(Authority.class, buffer.toString(), params.toArray(),
				orderBy, pageable);
		
		if(authlist.getResultList().size()>0)
		{
			resultBean.setExist(true);//若查询的数据条数大于0，则当前输入值已存在，不符合唯一性校验
		}
		else
		{
			resultBean.setExist(false);
		}
		
		return resultBean;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
