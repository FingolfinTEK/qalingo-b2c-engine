/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.service;

import org.hoteia.qalingo.core.domain.CustomerGroup;

public interface GroupRoleService {

    CustomerGroup getCustomerGroupById(Long customerGroupId);

	CustomerGroup getCustomerGroupById(String customerGroupId);
	
	CustomerGroup getCustomerGroupByCode(String code);
	
	void saveOrUpdateCustomerGroup(CustomerGroup customerGroup);
	
	void deleteCustomerGroup(CustomerGroup customerGroup);

}
