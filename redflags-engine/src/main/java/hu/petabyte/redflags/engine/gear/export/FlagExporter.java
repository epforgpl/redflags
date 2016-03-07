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
package hu.petabyte.redflags.engine.gear.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.boot.GearLoader;
import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.IndicatorResult.IndicatorResultType;

/**
 * @author Zsolt Jurányi
 */
@Component
@Scope("prototype")
public class FlagExporter extends AbstractExporter {

	private static final Logger LOG = LoggerFactory.getLogger(FlagExporter.class);
	private static final String MISSING_DATA_CELL = "@ Missing data";
	private static final String IRRELEVANT_DATA_CELL = "@ Irrelevant data";

	private @Autowired GearLoader gearLoader;
	private List<AbstractIndicator> indicators = new ArrayList<AbstractIndicator>();
	private String filename;
	private final ArrayList<ArrayList<String>> grid = new ArrayList<ArrayList<String>>();

	private void addHeader() {

		int all = grid.size();

		// 2nd line
		ArrayList<String> row = new ArrayList<String>();
		grid.add(0, row);
		row.add("Notice ID");
		row.add("Notice URL");
		row.add("Flag count");
		for (AbstractIndicator indicator : indicators) {
			row.add(indicator.toString());
		}

		// 1st line
		ArrayList<String> sumRow = new ArrayList<String>();
		grid.add(0, sumRow);

		sumRow.add("REDFLAGS ON " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		sumRow.add("Count of notices: " + all);
		for (int i = 0; i < indicators.size() + 1; i++) { // +1: flag count col
			int flagged = 0, relevant = all;
			for (int n = 0; n < all; n++) {
				String cell = grid.get(n + 2).get(i + 2);
				if (!"0".equals(cell) && !"".equals(cell) && !MISSING_DATA_CELL.equals(cell)
						&& !IRRELEVANT_DATA_CELL.equals(cell)) {
					flagged++;
				} else if (MISSING_DATA_CELL.equals(cell) || IRRELEVANT_DATA_CELL.equals(cell)) {
					relevant--;
				}
			}

			StringBuilder sb = new StringBuilder();

			// flagged count
			sb.append(0 == flagged ? "NO FLAG, " : String.format("F=%d, ", flagged));

			// relevant count
			if (0 == relevant) {
				sb.append("NO RELEVANT, ");
			} else if (relevant < all) {
				sb.append(String.format("R=%d, ", relevant));
			}

			// notices count
			sb.append(String.format("A=%d, ", all));

			// flagged / relevant rate
			if (0 < flagged && relevant < all) {
				double r = (double) flagged / (double) relevant * 100;
				sb.append(String.format("F/R=%.2f%%, ", r));
			}

			// flagged / all rate
			if (0 < flagged) {
				double r = (double) flagged / (double) all * 100;
				sb.append(String.format("F/A=%.2f%%, ", r));
			}

			// relevant / all rate
			if (0 < relevant && relevant < all) {
				double r = (double) relevant / (double) all * 100;
				sb.append(String.format("R/A=%.2f%%, ", r));
			}

			sumRow.add(sb.toString().trim().replaceAll(",$", ""));
		}
	}

	@Override
	public void afterSession() throws Exception {
		if (grid.isEmpty()) {
			LOG.info("Nothing to export");
		} else {
			LOG.debug("Sorting grid rows");
			sortRows();
			LOG.debug("Adding header");
			addHeader();
			LOG.debug("Writing to disk ({})", filename);
			writeFile();
			LOG.info("Flags exported to file: {}", filename);
		}
	}

	@Override
	public void beforeSession() throws Exception {
		filename = filename();

		for (AbstractGear g : gearLoader.loadGears()) {
			if (g instanceof AbstractIndicator) {
				AbstractIndicator i = (AbstractIndicator) g;
				if (shouldInclude(i)) {
					indicators.add(i);
				}
			}
		}

		LOG.debug("We have {} indicators", indicators.size());
	}

	@Override
	protected void export(Notice notice) {
		if (notice.isCancelled() || !shouldInclude(notice)) {
			return;
		}

		ArrayList<String> row = new ArrayList<String>();
		grid.add(row);
		row.add(notice.getId().get() + " :: " + notice.getId().toString());
		row.add(notice.getUrl());

		int count = 0;
		for (AbstractIndicator indicator : indicators) {
			IndicatorResult r = notice.getIndicatorResults().get(indicator.getClass().getSimpleName());
			String s = "";
			if (null != r) {
				if (IndicatorResultType.MISSING_DATA == r.getType()) {
					s = MISSING_DATA_CELL;
				} else if (IndicatorResultType.IRRELEVANT_DATA == r.getType()) {
					s = IRRELEVANT_DATA_CELL;
				} else if (IndicatorResultType.FLAG == r.getType()) {
					String c = r.getFlagCategory();
					s = String.format("(%s %s) %s", null == c ? "" : c, r.getWeight(), r.getDescription());
					count++;
				}
			}
			row.add(s);
		}
		row.add(2, Integer.toString(count));
	}

	protected String filename() {
		return String.format("redflags-%s.csv", new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()));
	}

	protected boolean shouldInclude(AbstractIndicator i) {
		return true;
	}

	protected boolean shouldInclude(Notice notice) {
		return true;
	}

	private void sortRows() {
		Collections.sort(grid, new Comparator<ArrayList<String>>() {

			@Override
			public int compare(ArrayList<String> row1, ArrayList<String> row2) {
				String id1 = row1.get(0);
				String id2 = row2.get(0);
				return id2.compareTo(id1);
			}
		});
	}

	private void writeFile() throws IOException {
		File csvFile = new File(filename);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"));
		for (ArrayList<String> row : grid) {
			for (String col : row) {
				if (null != col) {
					out.write(col);
				}
				out.write("\t");
			}
			out.write("\n");
		}
		out.flush();
		out.close();
	}

}
