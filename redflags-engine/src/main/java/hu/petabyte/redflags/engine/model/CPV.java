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
public class CPV {

	protected static final List<CPV> POOL = new ArrayList<CPV>();

	public synchronized static CPV findOrCreate(int id, String name) {
		CPV cpv = new CPV(id, name);
		if (POOL.contains(cpv)) {
			cpv = POOL.get(POOL.indexOf(cpv));
			if (null != name) {
				cpv.name = name;
			}
		} else {
			POOL.add(cpv);
		}
		return cpv;
	}

	public static List<CPV> getPool() {
		return POOL;
	}

	protected int id;
	protected String name;

	protected CPV(int id, String name) {
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
		CPV other = (CPV) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%d (%s)", id, name);
	}

}
