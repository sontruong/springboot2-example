/**
 * 
 */
package com.oones.cdn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.util.StringUtils;

/**
 * @author son.truong Jul 12, 2018
 */
public class DateUtils {
	public static Date getStartTimeOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 1);
		date = calendar.getTime();
		return date;
	}

	public static Date getStartTimeOfDate(LocalDate date) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.set(Calendar.YEAR, date.getYear());
		calendar.set(Calendar.MONTH, date.getMonthValue() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 1);
		return calendar.getTime();
	}

	public static Date getEndTimeOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		date = calendar.getTime();

		return date;
	}

	public static Date getDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 1);
		return calendar.getTime();
	}

	public static Date getEndTimeOfDate(LocalDate date) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.set(Calendar.YEAR, date.getYear());
		calendar.set(Calendar.MONTH, date.getMonthValue() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getDateNoneTime(Date date) {
		if (null == date) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		date = calendar.getTime();
		return date;
	}

	public static Date getFistDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.DATE, 1);
		date = calendar.getTime();
		return date;
	}

	public static LocalDate getFistDayOfMonth(LocalDate date) {
		return date.withDayOfMonth(1);
	}

	public static String getNowWithFormat(String format) {
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
		return dt1.format(new Date());
	}

	public static String getDateFormat(Date date, String format) {
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
		return dt1.format(date);
	}

	public static String getDateFormat(LocalDate date, String format) {
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
		Date tm = getStartTimeOfDate(date);
		return dt1.format(tm);
	}

	public static Date parseDate(String date, String format) throws ParseException {
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
		return dt1.parse(date);
	}

	public static LocalDate parseLDate(String date, String format) throws ParseException {
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
		return from(dt1.parse(date));
	}

	public static Boolean equalWithoutTime(Date date, Date date2) throws ParseException {
		date = getDateNoneTime(date);
		date2 = getDateNoneTime(date2);
		return date.equals(date2);
	}

	public static Boolean equalMonthDate(Date date, Date date2) throws ParseException {
		date = getDateNoneTime(date);
		date2 = getDateNoneTime(date2);
		return get(Calendar.MONTH, date) == get(Calendar.MONTH, date2)
				&& get(Calendar.DAY_OF_MONTH, date) == get(Calendar.DAY_OF_MONTH, date2);
	}

	public static Date subtractDate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1 * day);
		return calendar.getTime();
	}

	public static Date addDate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	
	public static Date add(Date date, int type, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, num);
		return calendar.getTime();
	}

	public static LocalDate from(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
	}

	public static int get(int type, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(type);
	}

	public static Date set(int type, int value, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(type, value);
		return calendar.getTime();
	}

	public static Integer getNumDayOfMOnth(Date date) {
		YearMonth yearMonthObject = YearMonth.of(get(Calendar.YEAR, date), get(Calendar.MONTH, date));
		int daysInMonth = yearMonthObject.lengthOfMonth();
		return daysInMonth;
	}

	public static boolean equalDayOfWeek(Date date, String day) {
		Integer integer = getDayOfWeekAsInt(day);
		return equalDayOfWeek(date, integer);
	}

	public static boolean equalDayOfMonth(Date date, int day) {
		return DateUtils.get(Calendar.DAY_OF_MONTH, date) == day;
	}

	public static boolean equalDayOfWeek(Date date, int day) {
		return DateUtils.get(Calendar.DAY_OF_WEEK, date) == day;
	}

	public static Integer getDayOfWeekAsInt(String day) {
		if (StringUtils.isEmpty(day)) {
			return null;
		}
		Integer i = null;
		switch (day.toLowerCase()) {
		case "sunday":
		case "sun":
			i = 1;
			break;
		case "monday":
		case "mon":
			i = 2;
			break;
		case "tuesday":
		case "tue":
			i = 3;
			break;
		case "wednesday":
		case "wed":
			i = 4;
			break;
		case "thursday":
		case "thu":
			i = 5;
			break;
		case "friday":
		case "fri":
			i = 6;
			break;
		case "saturday":
		case "sat":
			i = 7;
			break;
		default:
			break;
		}
		return i;
	}

	public static String getDayOfWeek(int value) {
		String day = "";
		switch (value) {
		case 1:
			day = "Sunday";
			break;
		case 2:
			day = "Monday";
			break;
		case 3:
			day = "Tuesday";
			break;
		case 4:
			day = "Wednesday";
			break;
		case 5:
			day = "Thursday";
			break;
		case 6:
			day = "Friday";
			break;
		case 7:
			day = "Saturday";
			break;
		}
		return day;
	}

	public static boolean betweenDate(Date date, Date startDate, Date endDate) {
		date = getDateNoneTime(date);
		startDate = getDateNoneTime(startDate);
		endDate = getDateNoneTime(endDate);
		return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
	}

	public static Collection<Date> getDatesBetween(Date startDate, Date endDate) {
		Collection<Date> dates = new ArrayList<>();
		while (!startDate.after(endDate)) {
			dates.add(startDate);
			startDate = addDate(startDate, 1);
		}
		return dates;
	}

	public static Collection<Date> getDatesBetween(Date startDate, Date endDate, Date fromDate, Date toDate) {
		Collection<Date> dates = new ArrayList<>();
		startDate = getDateNoneTime(startDate);
		endDate = getDateNoneTime(endDate);
		fromDate = getDateNoneTime(fromDate);
		toDate = getDateNoneTime(toDate);
		if (startDate.after(endDate) || fromDate.after(toDate) || startDate.after(toDate)) {
			return dates;
		}

		if (startDate.equals(endDate) && betweenDate(startDate, fromDate, toDate)) {
			dates.add(startDate);
			return dates;
		}

		while (!startDate.after(endDate)) {
			if (betweenDate(startDate, fromDate, toDate)) {
				dates.add(startDate);
				System.out.println(startDate);
			}
			startDate = addDate(startDate, 1);
		}

		return dates;
	}

	public static boolean checkMonth(Date date, int... months) {
		if (null == months || 0 == months.length) {
			return false;
		}
		for (int i : months) {
			if (get(Calendar.MONTH, date) == i) {
				return true;
			}
		}
		return false;
	}

	public static Date from(LocalDate lDate) {
		return java.util.Date.from(lDate.atStartOfDay()
			      .atZone(ZoneId.systemDefault())
			      .toInstant());
	}

	public static boolean equalDateOnly(int type, Date date1, Date date2) {
		if (get(type, date1) == get(type, date2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param date
	 * @return true if this date is Sunday
	 */
	public static boolean isFirstDayOfWeek(Date date) {
		if (get(Calendar.DAY_OF_WEEK, date) == 1) {
			return true;
		}
		return false;
	}
	
	public static boolean isFirstDayOfQuarter(Date date) {
		int day = get(Calendar.DAY_OF_MONTH, date);
		int month = get(Calendar.MONTH, date) + 1;
		System.out.println("---" + month + " - " + Month.of(month));
		boolean firstMofQ = false;
		switch (Month.of(month)) {
			case JANUARY:
				System.out.println("--- 1: " + month + " - " + Month.of(month));
				firstMofQ = true;
				break;
			case APRIL:
				System.out.println("--- 2: " + month + " - " + Month.of(month));
				firstMofQ = true;
				break;
			case JULY:
				System.out.println("--- 3: " + month + " - " + Month.of(month));
				firstMofQ = true;
				break;
			case OCTOBER:
				System.out.println("--- 4: " + month + " - " + Month.of(month));
				firstMofQ = true;
				break;
			default:
				break;
		}
		return (day == 1) && firstMofQ;
	}
}
