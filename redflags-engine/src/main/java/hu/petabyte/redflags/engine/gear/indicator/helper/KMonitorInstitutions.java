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
package hu.petabyte.redflags.engine.gear.indicator.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.util.CompanyNameUtils;
import hu.petabyte.redflags.engine.util.StrUtils;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "kmonitor.inst")
public class KMonitorInstitutions {

	private static final Logger LOG = LoggerFactory.getLogger(KMonitorInstitutions.class);

	private boolean initialized = false;
	private String dbhost;
	private String dbname;
	private String dbpass;
	private String dbuser;
	private Connection conn;
	private final List<String> institutions = new ArrayList<String>();

	public String findOrgName(String rfName) {
		if (null == rfName) {
			return null;
		}

		rfName = norm(rfName);
		String rfNameNoSuffix = CompanyNameUtils.removeSuffixes(rfName);
		String rfNamePattern = rfNameNoSuffix.replaceAll(" |$", "( \\\\w+)* ").trim();

		String foundKmName = null;
		double foundKmNameDist = 0.0;
		for (String kmName : institutions) {

			String originalKmName = kmName;
			kmName = norm(kmName);
			String kmNameNoSuffix = CompanyNameUtils.removeSuffixes(kmName);
			String kmNamePattern = kmNameNoSuffix.replaceAll(" |$", "( \\\\w+)* ").trim();
			if (CompanyNameUtils.matchSuffixesNorm(rfName, kmName)) {
				if (rfNameNoSuffix.matches(kmNamePattern) || kmNameNoSuffix.matches(rfNamePattern)) {

					double dist = StringUtils.getJaroWinklerDistance(kmName, rfName);
					if (dist > foundKmNameDist) {
						foundKmNameDist = dist;
						foundKmName = originalKmName;
					}
				}
			}
		}

		// önkormányzatok javítása
		if (null != foundKmName && rfName.startsWith("budapest fovaros ")) {
			String[] rfWords = rfName.split(" ");
			String[] fWords = norm(foundKmName).split(" ");
			int commonPrefixWords = 0;
			for (int i = 0; i < 3; i++) {
				if (i < rfWords.length && i < fWords.length && rfWords[i].equals(fWords[i])) {
					commonPrefixWords++;
				}
			}
			if (commonPrefixWords < 3) {
				foundKmName = null;
			}
		}

		return foundKmName;
	}

	public String getDbhost() {
		return dbhost;
	}

	public String getDbname() {
		return dbname;
	}

	public String getDbpass() {
		return dbpass;
	}

	public String getDbuser() {
		return dbuser;
	}

	public void init() {
		if (initialized) {
			return;
		}

		if (null == dbhost || null == dbname || null == dbuser || null == dbpass) {
			LOG.warn("K-Monitor Institutions component is not initialized.");
			return;
		}

		try {
			LOG.info("Connecting to K-Monitor database...");
			conn = DriverManager.getConnection(
					String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8", dbhost, dbname), dbuser,
					dbpass);
			LOG.info("Querying institutions...");
			PreparedStatement ps = conn.prepareStatement("SELECT name FROM news_institutions");
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				institutions.add(rs.getString(1));
			}
			rs.close();
			ps.close();
			LOG.info("We have {} institutions", institutions.size());
			conn.close();
		} catch (Exception e) {
			LOG.error("Failed to connect to KMDB.", e);
		}
		initialized = true;
	}

	private String norm(String name) {
		name = name.toLowerCase();
		name = StrUtils.removeAccents(name);
		name = StrUtils.leaveOnlyAlphanumerics(name);
		return name;
	}

	public void setDbhost(String dbhost) {
		this.dbhost = dbhost;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public void setDbpass(String dbpass) {
		this.dbpass = dbpass;
	}

	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}

}
