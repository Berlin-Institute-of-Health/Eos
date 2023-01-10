/*******************************************************************************
 * Copyright (c) 2019 Georgia Tech Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *

 * Copyright (c) 2023 Berlin Institute of Health
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/

package org.bih.eos.jpabase.jpa.dao;

import org.bih.eos.jpabase.model.entity.Relationship;
import org.springframework.stereotype.Repository;

@Repository
public class RelationshipDao extends JPABaseEntityDao<Relationship> {
	
	public Relationship findById(Class<Relationship> entityClass, String id) {
		return getEntityManager().find(entityClass, id);
	}

	public String delete(Class<Relationship> entityClass, String id) {
		Relationship entity = findById(entityClass, id);
		if (entity != null) {
			getEntityManager().remove(entity);
			return id;
		} else {
			return null;
		}
	}

}
