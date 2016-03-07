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
package hu.petabyte.redflags.engine.tedintf;

/**
 * @author Zsolt Jurányi
 */
public class TedInterfaceConf {

	private long addSleepBeforeRetry = 0;
	private int maxBodySize = 10 * 1000000;
	private double mulSleepBeforeRetry = 2.0;
	private String mustHaveContent = "docContent";
	private int retryCount = 2;
	private long sleepBeforeRequest = 2000;
	private int timeout = 120 * 1000;

	public long getAddSleepBeforeRetry() {
		return addSleepBeforeRetry;
	}

	public int getMaxBodySize() {
		return maxBodySize;
	}

	public double getMulSleepBeforeRetry() {
		return mulSleepBeforeRetry;
	}

	public String getMustHaveContent() {
		return mustHaveContent;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public long getSleepBeforeRequest() {
		return sleepBeforeRequest;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setAddSleepBeforeRetry(long addSleepBeforeRetry) {
		this.addSleepBeforeRetry = addSleepBeforeRetry;
	}

	public void setMaxBodySize(int maxBodySize) {
		this.maxBodySize = maxBodySize;
	}

	public void setMulSleepBeforeRetry(double mulSleepBeforeRetry) {
		this.mulSleepBeforeRetry = mulSleepBeforeRetry;
	}

	public void setMustHaveContent(String mustHaveContent) {
		this.mustHaveContent = mustHaveContent;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public void setSleepBeforeRequest(long sleepBeforeRequest) {
		this.sleepBeforeRequest = sleepBeforeRequest;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "TedInterfaceConf [addSleepBeforeRetry=" + addSleepBeforeRetry + ", maxBodySize=" + maxBodySize
				+ ", mulSleepBeforeRetry=" + mulSleepBeforeRetry + ", mustHaveContent=" + mustHaveContent
				+ ", retryCount=" + retryCount + ", sleepBeforeRequest=" + sleepBeforeRequest + ", timeout=" + timeout
				+ "]";
	}

}
