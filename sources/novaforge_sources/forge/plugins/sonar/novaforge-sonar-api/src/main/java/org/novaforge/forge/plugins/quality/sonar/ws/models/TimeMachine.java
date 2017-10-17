package org.novaforge.forge.plugins.quality.sonar.ws.models;

import java.util.List;

/**
 * Json binding of the TimeMachine result
 * 
 * <br/>
 * <pre>
 * JSon flux result example:
 *  
 *  "cols": [
 *     {
 *       "metric": "complexity"
 *     },
 *     {
 *       "metric": "lines"
 *     }
 *  ],
 *  "cells": [
 *     {
 *       "d": "2011-10-02T00:01:00+0200",
 *       "v": [
 *         11879,
 *         94500
 *       ]
 *     },
 *     {
 *      "d": "2011-10-25T12:27:41+0200",
 *      "v": [
 *         13528,
 *         102508
 *       ]
 *     }
 *   ]
 * }
 * </pre>
 * @author s241664
 *
 */
public class TimeMachine {

	private List<TimeMachineColumn> cols;

	private List<TimeMachineCell> cells;

	public TimeMachine(List<TimeMachineColumn> cols, List<TimeMachineCell> cells) {
		super();
		this.cols = cols;
		this.cells = cells;
	}

	public List<TimeMachineColumn> getCols() {
		return cols;
	}

	public List<TimeMachineCell> getCells() {
		return cells;
	}

	public static class TimeMachineColumn {

		private String metric;

		public TimeMachineColumn(String metric) {
			super();
			this.metric = metric;
		}

		public String getMetric() {
			return metric;
		}

	}

	public static class TimeMachineCell {

		private String d;

		private long[] v;

		public TimeMachineCell(String d, long[] v) {
			super();
			this.d = d;
			this.v = v;
		}

		public String getD() {
			return d;
		}

		public long[] getV() {
			return v;
		}

	}
}
