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
package hu.petabyte.redflags.engine.boot;

import hu.petabyte.redflags.engine.tedintf.TedInterfaceConf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "redflags.engine")
public class RedflagsEngineConfig {

	protected String scope;
	protected String cacheDirectory;
	protected int threadCount;
	protected TedInterfaceConf tedinterface = new TedInterfaceConf();
	protected List<String> gears = new ArrayList<String>();

	public String getCacheDirectory() {
		return cacheDirectory;
	}

	public List<String> getGears() {
		return gears;
	}

	public String getScope() {
		return scope;
	}

	public TedInterfaceConf getTedinterface() {
		return tedinterface;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setCacheDirectory(String cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
	}

	public void setGears(List<String> gears) {
		this.gears = gears;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setTedinterface(TedInterfaceConf tedinterface) {
		this.tedinterface = tedinterface;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	@Override
	public String toString() {
		return "RedflagsEngineConfig [scope=" + scope + ", cacheDirectory="
				+ cacheDirectory + ", threadCount=" + threadCount
				+ ", tedinterface=" + tedinterface + ", gears=" + gears + "]";
	}

}
