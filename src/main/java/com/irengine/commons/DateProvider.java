package com.irengine.commons;

import java.util.Date;
/**得到系统时间*/
public interface DateProvider {

	Date getDate();

	static final DateProvider DEFAULT = new CurrentDateProvider();

	public static class CurrentDateProvider implements DateProvider {

		@Override
		public Date getDate() {
			return new Date();
		}
	}

	public static class ConfigurableDateProvider implements DateProvider {

		private final Date date;

		public ConfigurableDateProvider(Date date) {
			this.date = date;
		}

		@Override
		public Date getDate() {
			return date;
		}
	}

}
