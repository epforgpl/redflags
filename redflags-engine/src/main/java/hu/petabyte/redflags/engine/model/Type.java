/*
   Copyright 2014-2016 PetaByte Research Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package hu.petabyte.redflags.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zsolt Jurányi
 *
 */
public class Type {

	protected static final List<Type> POOL = new ArrayList<Type>();

	public synchronized static Type findOrCreate(String id, String name) {
		Type type = new Type(id, name);
		if (POOL.contains(type)) {
			type = POOL.get(POOL.indexOf(type));
			if (null != name) {
				type.name = name;
			}
		} else {
			POOL.add(type);
		}
		return type;
	}

	public static List<Type> getPool() {
		return POOL;
	}

	/**
	 * Type identifier in "fieldAbbr-typeId" format, eg. "AA-3".
	 */
	protected String id;
	protected String name;

	protected Type(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Type other = (Type) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", id, name);
	}
}
