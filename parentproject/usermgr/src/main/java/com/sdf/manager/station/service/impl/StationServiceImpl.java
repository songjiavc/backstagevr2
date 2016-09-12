package com.sdf.manager.station.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sdf.manager.common.exception.BizException;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.station.application.dto.StationFormDto;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.repository.StationRepository;
import com.sdf.manager.station.service.StationService;
/** 
  * @ClassName: AuthServiceImpl 
  * @Description: 
  * @author songj@sdfcp.com
  * @date 2015年9月25日 上午8:51:54 
  *  
  */
@Service("stationService")
@Transactional(propagation=Propagation.REQUIRED)
public class StationServiceImpl implements StationService {
	@Autowired
	private StationRepository stationRepository;

	/**
	 * 
	* @Description: 业务上保存更新方法
	* @author songj@sdfcp.com
	 * @throws BizException 
	* @date 2015年10月15日 下午1:33:45
	 */
	public void saveOrUpdate(StationFormDto stationFormDto,String userId) throws BizException {
		if(StringUtils.isEmpty(stationFormDto.getId())){
			//如果是新增判断帐号是否表内重复
			Station stationCode = this.getStationByCode(stationFormDto.getAddFormStationCode());
			if(null == stationCode){
				Station station = new Station();
				station.setAgentId(stationFormDto.getAddFormAgent());
				station.setCode(stationFormDto.getAddFormStationCode());
				station.setOwner(stationFormDto.getAddFormName());
				station.setStationNumber(stationFormDto.getAddFormStationNumber());
				station.setAddress(stationFormDto.getAddFormAddress());
				station.setProvinceCode(stationFormDto.getAddFormProvince());
				station.setCityCode(stationFormDto.getAddFormCity());
				station.setRegionCode(stationFormDto.getAddFormRegion());
				station.setOwnerTelephone(stationFormDto.getAddFormTelephone());
				station.setStationType(stationFormDto.getAddFormStationStyle());
				station.setPassword(stationFormDto.getPassword());
				station.setIsDeleted(Constants.IS_NOT_DELETED);
				station.setCreater(userId);
				station.setCreaterTime(new Date());
				station.setModify(userId);
				station.setModifyTime(new Date());
				stationRepository.save(station);
			}else{
				throw new BizException(0201);
			}
		}else{
			Station station = this.getStationById(stationFormDto.getId());
			station.setAgentId(stationFormDto.getAddFormAgent());
			station.setCode(stationFormDto.getAddFormStationCode());//update by banna in 2016-09-12:修改通行证信息时登录账号可能重新生成，所以在修改时也要重新保存
			station.setOwner(stationFormDto.getAddFormName());
			station.setStationNumber(stationFormDto.getAddFormStationNumber());
			station.setStationType(stationFormDto.getAddFormStationStyle());
			station.setAddress(stationFormDto.getAddFormAddress());
			station.setProvinceCode(stationFormDto.getAddFormProvince());
			station.setCityCode(stationFormDto.getAddFormCity());
			station.setRegionCode(stationFormDto.getAddFormRegion());
			station.setOwnerTelephone(stationFormDto.getAddFormTelephone());
			station.setPassword(stationFormDto.getPassword());
			station.setModify(userId);
			station.setModifyTime(new Date());
			stationRepository.save(station);
		}
	}

	
	/* (非 Javadoc) 
	 * <p>Title: getUserByCode</p> 
	 * <p>Description: </p> 
	 * @param code
	 * @return 
	 * @see com.sdf.manager.user.service.UserService#getUserByCode(java.lang.String) 
	 */
	public Station getStationById(String id)
	{
		Station station =  stationRepository.getStationById(id);
		return station;
	}
	

	public QueryResult<Station>  getStationList(Class<Station> entityClass,String whereJpql, Object[] queryParams,LinkedHashMap<String, String> orderby, Pageable pageable)
	{
		QueryResult<Station> stationObj = stationRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,orderby, pageable);
		return stationObj;
	}

	public Station getStationByCode(String code) {
		return stationRepository.getStationByCode(code);
	}
	
	
	/* (非 Javadoc) 
	 * <p>Title: deleteStationByIds</p> 
	 * <p>Description: </p> 
	 * @param ids
	 * @throws BizException 
	 * @see com.sdf.manager.user.service.UserService#deleteStationByIds(java.lang.String[]) 
	 */
	public void deleteStationByIds(String[] ids,String userId) throws BizException{
		if(ids.length > 0){
			for(String id : ids){
				Station station = this.getStationById(id);
				station.setIsDeleted(Constants.IS_DELETED);
				station.setModify(userId);
				station.setModifyTime(new Date());
				stationRepository.save(station);
			}
		}else{
			throw new BizException(0202);
		}
	}

	/* (非 Javadoc) 
	 * <p>Title: getSationById</p> 
	 * <p>Description: </p> 
	 * @param id
	 * @return 
	 * @see com.sdf.manager.station.service.StationService#getSationById(java.lang.String) 
	 */
	public Station getSationById(String id) {
		return stationRepository.getStationById(id);
	}


	/**
	 * 根据代理id获取其下属的站点列表
	 */
	public List<Station> getStationByAgentId(String agentId) {
		return stationRepository.getStationByAgentId(agentId);
	}


	public List<Station> getStationByStationTypeAndOwnerAndOwnertelephone(
			String stationType, String owner, String ownerTelephone) {
		return stationRepository.getStationByStationTypeAndOwnerAndOwnertelephone
				(stationType, owner, ownerTelephone);
	}
	
	public void update(Station station)
	{
		stationRepository.save(station);
	}
}
