package com.sdf.manager.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sdf.manager.common.exception.BizException;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.user.bean.AccountBean;
import com.sdf.manager.user.bean.UserRelaRoleBean;
import com.sdf.manager.user.dto.AddAgentForm;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.entity.UserRelaRole;
import com.sdf.manager.user.repository.RoleRepository;
import com.sdf.manager.user.repository.UserRelaRoleRepository;
import com.sdf.manager.user.repository.UserRepository;
import com.sdf.manager.user.service.UserService;
/** 
  * @ClassName: AuthServiceImpl 
  * @Description: 
  * @author songj@sdfcp.com
  * @date 2015年9月25日 上午8:51:54 
  */
@Service("userService")
@Transactional(propagation=Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRelaRoleRepository userRelaRoleRepository;
	@Autowired
	private RoleRepository roleRepository;
	/**
	 * @return
	 */
	public List<User> findAll() {
		return userRepository.findAll();
	}

	/**
	 * 
	* @Description: 业务上保存更新方法
	* @author songj@sdfcp.com
	 * @throws BizException 
	* @date 2015年10月15日 下午1:33:45
	 */
	public void saveOrUpdate(AccountBean accountBean,String userId) throws BizException {
		if(StringUtils.isEmpty(accountBean.getId())){
			//如果是新增判断帐号是否表内重复
			User userCode = this.getUserByCode(accountBean.getCode());
			if(null == userCode){
				User user = new User();
				user.setCode(accountBean.getCode());
				user.setName(accountBean.getName());
				user.setPassword(accountBean.getPassword());
				user.setTelephone(accountBean.getTelephone());
				user.setStatus(accountBean.getStatus());
				
				//date:2016-01-29添加账户中维护区域信息
				user.setProvinceCode(accountBean.getProvince());
				user.setCityCode(accountBean.getCity());
				
				//  暂时去掉md5加密   user.setPassword(MD5Util.MD5(accountBean.getPassword()));
				user.setPassword(accountBean.getPassword());
				user.setIsDeleted(Constants.IS_NOT_DELETED);
				user.setCreater(userId);
				user.setCreaterTime(new Date());
				user.setModify(userId);
				user.setModifyTime(new Date());
				userRepository.save(user);
			}else{
				throw new BizException(0101);
			}
		}else{
			// user.setCode(accountBean.getCode());   修改时登录帐号不允许修改
			User user = this.getUserById(accountBean.getId());
			user.setName(accountBean.getName());
			user.setPassword(accountBean.getPassword());
			user.setTelephone(accountBean.getTelephone());
			user.setStatus(accountBean.getStatus());
			user.setPassword(accountBean.getPassword());
			
			//date:2016-01-29添加账户中维护区域信息
			user.setProvinceCode(accountBean.getProvince());
			user.setCityCode(accountBean.getCity());
			
			user.setModify("admin");
			user.setModifyTime(new Date());
			userRepository.save(user);
		}
	}

	
	/* (非 Javadoc) 
	 * <p>Title: getUserByCode</p> 
	 * <p>Description: </p> 
	 * @param code
	 * @return 
	 * @see com.sdf.manager.user.service.UserService#getUserByCode(java.lang.String) 
	 */
	public User getUserById(String id) throws BizException
	{
		User user =  userRepository.findOne(id);
		if(user == null){
			throw new BizException(0103);
		}
		return user;
	}
	

	public Map<String,Object> getScrollDataByJpql(Class<User> entityClass,String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable) {
		Map<String,Object> returnData = new HashMap<String,Object>();
		List<AccountBean> accountList = new ArrayList<AccountBean>();
		QueryResult<User> userObj = userRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,orderby, pageable);
		List<User> userlist = userObj.getResultList();
		Long totalrow = userObj.getTotalRecord();
		for(User user : userlist){
			AccountBean accountBean = new AccountBean();
			accountBean.setId(user.getId());
			accountBean.setCode(user.getCode());
			accountBean.setName(user.getName());
			accountBean.setStatus(user.getStatus());
			accountBean.setTelephone(user.getTelephone());
			accountBean.setCreater(user.getCreater());
			accountBean.setCreaterTime(DateUtil.formatDate(user.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			List<UserRelaRoleBean> roleBeanList = new ArrayList<UserRelaRoleBean>();
			for(Role role : user.getRoles()){
				UserRelaRoleBean userRelaRoleBean = new UserRelaRoleBean();
				userRelaRoleBean.setRoleId(role.getId());
				userRelaRoleBean.setRoleCode(role.getCode());
				userRelaRoleBean.setRoleName(role.getName());
				roleBeanList.add(userRelaRoleBean);
			}
			accountBean.setRoles(roleBeanList);
			accountList.add(accountBean);
		}
		returnData.put("rows", accountList);
		returnData.put("total", totalrow);
		return returnData;
	}

	public User getUserByCode(String code) {
		return userRepository.getUserByCode(code);
	}
	
	
	/* (非 Javadoc) 
	 * <p>Title: deleteAccountByIds</p> 
	 * <p>Description: </p> 
	 * @param ids
	 * @throws BizException 
	 * @see com.sdf.manager.user.service.UserService#deleteAccountByIds(java.lang.String[]) 
	 */
	public void deleteAccountByIds(String[] ids,String userId) throws BizException{
		if(ids.length > 0){
			for(String id : ids){
				User user = this.getUserById(id);
				user.setIsDeleted(Constants.IS_DELETED);
				user.setModify(userId);
				user.setModifyTime(new Date());
				userRepository.save(user);
			}
		}else{
			throw new BizException(0102);
		}
	}
	
	
	/* (非 Javadoc) 
	 * <p>Title: saveUserRelaRole</p> 
	 * <p>Description: </p> 
	 * @param userId
	 * @param roles
	 * @throws BizException 
	 * @see com.sdf.manager.user.service.UserService#saveUserRelaRole(java.lang.String, java.util.List) 
	 */
	public void saveUserRelaRole(String userId,UserRelaRoleBean roleBean)throws BizException{
		//删除以前的关系
		List<UserRelaRole> userRelaRoleList = userRelaRoleRepository.findUserRelaRoleByUserId(userId);
		for(UserRelaRole userRelaRole : userRelaRoleList){
			userRelaRoleRepository.delete(userRelaRole);
		}
		//加入新关系
		UserRelaRole userRelaRole = new UserRelaRole();
		userRelaRole.setRoleId(roleBean.getRoleId());
		userRelaRole.setUserId(userId);
		userRelaRoleRepository.save(userRelaRole);
		//向user表中加入parentUid
		User user = this.getUserById(userId);
		user.setParentUid(roleBean.getParentUid());
		userRepository.save(user);
	}
	
	/** 
	  * @Description: TODO(这里用一句话描述这个类的作用)
	  * @author songj@sdfcp.com
	  * @date 2015年10月23日 上午10:35:09 
	  * @param userId
	  * @return 
	  */
	public List<UserRelaRole> findUserRelaRoleByUserId(String userId){
		return userRelaRoleRepository.findUserRelaRoleByUserId(userId);
	}
	
	/* (非 Javadoc) 
	 * <p>Title: savePassword</p> 
	 * <p>Description: </p> 
	 * @param newPassword
	 * @param userId 
	 * @see com.sdf.manager.user.service.UserService#savePassword(java.lang.String, java.lang.String) 
	 */
	public void savePassword(String newPassword,String userCode){
		User user = this.getUserByCode(userCode);
		user.setPassword(newPassword);
		userRepository.save(user);
	}

	/* (非 Javadoc) 
	 * <p>Title: findAccountsByRoleCode</p> 
	 * <p>Description:根据rolecode获取具有该角色的用户列表 </p> 
	 * @param roleCode
	 * @return 
	 * @see com.sdf.manager.user.service.UserService#findAccountsByRoleCode(java.lang.String) 
	 */
	public List<AccountBean> findAccountsByRoleCode(String roleCode) {
		Role role = roleRepository.getRoleByCode(roleCode);
		List<User> users = role.getUsers();
		if(users!=null && users.size()>0){
			List<AccountBean> accountList = new ArrayList<AccountBean>();
			for(User user : users){
				AccountBean accountBean = new AccountBean();
				try {
					BeanUtil.copyBeanProperties(accountBean, user);
				} catch (Exception e) {
					e.printStackTrace();
				}
				accountList.add(accountBean);
			}
			return accountList;
		}
		return null;
	}
	

	/* (非 Javadoc) 
	 * <p>Title: findAccountsByRoleCode</p> 
	 * <p>Description: </p> 
	 * @param roleCode
	 * @return 
	 * @see com.sdf.manager.user.service.UserService#findAccountsByRoleCode(java.lang.String) 
	 */
	public List<AccountBean> findAccountsByRoleId(String roleId) {
		Role role = roleRepository.getRoleById(roleId);
		List<User> users = role.getUsers();
		if(users!=null && users.size()>0){
			List<AccountBean> accountList = new ArrayList<AccountBean>();
			for(User user : users){
				AccountBean accountBean = new AccountBean();
				try {
					BeanUtil.copyBeanProperties(accountBean, user);
				} catch (Exception e) {
					e.printStackTrace();
				}
				accountList.add(accountBean);
			}
			return accountList;
		}
		return null;
	}
	/* (非 Javadoc) 
	 * <p>Title: findRolesByUserId</p> 
	 * <p>Description: </p> 
	 * @param userId
	 * @return 
	 * @see com.sdf.manager.user.service.UserService#findRolesByUserId(java.lang.String) 
	 */
	public List<Role> findRolesByUserId(String userId){
		User user = null;
		try {
			user = this.getUserById(userId);
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user.getRoles();
	}
	/* (非 Javadoc) 
	 * <p>Title: getUserList</p> 
	 * <p>Description: </p> 
	 * @param entityClass
	 * @param whereJpql
	 * @param queryParams
	 * @param orderby
	 * @param pageable
	 * @return 
	 * @see com.sdf.manager.user.service.UserService#getUserList(java.lang.Class, java.lang.String, java.lang.Object[], java.util.LinkedHashMap, org.springframework.data.domain.Pageable) 
	 */
	public QueryResult<User> getUserList(Class<User> entityClass, String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable) {
			QueryResult<User> stationObj = userRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,orderby, pageable);
			return stationObj;
	}
	
	
	public QueryResult<User> getAgentList(Class<User> entityClass, String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable) {
			String sql = "SELECT a.* FROM T_SDF_USERS a,RELA_SDF_USER_ROLE b,T_SDF_ROLES  c "
				+ "where a.id = b.user_id "
				+ "and   b.role_id = c.id "
				+ "and   c.code='"+Constants.ROLE_SCDL_CODE+"' and " + whereJpql;
			QueryResult<User> userObj = userRepository.getScrollDataBySql(entityClass,sql, queryParams, pageable);
			return userObj;
	}
	/* (非 Javadoc) 
	 * <p>Title: saveOrUpdate</p> 
	 * <p>Description: </p> 
	 * @param addAgentForm
	 * @param userId
	 * @throws BizException 
	 * @see com.sdf.manager.user.service.UserService#saveOrUpdate(com.sdf.manager.user.dto.AddAgentForm, java.lang.String) 
	 */
	public void saveOrUpdate(AddAgentForm addAgentForm,String userId) throws BizException{
		if(StringUtils.isEmpty(addAgentForm.getId())){
			//如果是新增判断帐号是否表内重复
			User userCode = this.getUserByCode(addAgentForm.getAddFormAgentCode());
			if(null == userCode){
				User user = new User();
				user.setCode(addAgentForm.getAddFormAgentCode());
				user.setName(addAgentForm.getAddFormName());
				user.setPassword(addAgentForm.getPassword());
				user.setTelephone(addAgentForm.getAddFormTelephone());
				user.setStatus(addAgentForm.getStatus());
				user.setParentUid(addAgentForm.getAddFormParentId());
				user.setAddress(addAgentForm.getAddFormAddress());
				user.setProvinceCode(addAgentForm.getAddFormProvince());
				user.setCityCode(addAgentForm.getAddFormCity());
				user.setRegionCode(addAgentForm.getAddFormRegion());
				user.setPassword(addAgentForm.getPassword());
				user.setIsDeleted(Constants.IS_NOT_DELETED);
				user.setCreater(userId);
				user.setCreaterTime(new Date());
				user.setModify(userId);
				user.setModifyTime(new Date());
				userRepository.save(user);
				//保存代理成功后保存角色代理对应关系
				//获取代理role实体
				UserRelaRole userRelaRole = new UserRelaRole();
				userRelaRole.setUserId(user.getId());
				//初始化的角色是系统初始化数据
				userRelaRole.setRoleId("2");
				userRelaRoleRepository.save(userRelaRole);
			}else{
				throw new BizException(0101);
			}
		}else{
			// user.setCode(accountBean.getCode());   修改时登录帐号不允许修改
			User user = this.getUserById(addAgentForm.getId());
			user.setName(addAgentForm.getAddFormName());
			user.setPassword(addAgentForm.getPassword());
			user.setTelephone(addAgentForm.getAddFormTelephone());
			user.setParentUid(addAgentForm.getAddFormParentId());
			user.setAddress(addAgentForm.getAddFormAddress());
			user.setProvinceCode(addAgentForm.getAddFormProvince());
			user.setCityCode(addAgentForm.getAddFormCity());
			user.setRegionCode(addAgentForm.getAddFormRegion());
			user.setStatus(addAgentForm.getStatus());
			user.setPassword(addAgentForm.getPassword());
			user.setModify(userId);
			user.setModifyTime(new Date());
			userRepository.save(user);
		}
	}
}
