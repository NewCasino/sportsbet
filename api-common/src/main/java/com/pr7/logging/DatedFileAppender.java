package com.pr7.logging;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import com.pr7.util.DateUtil;
import com.pr7.util.PropertyHelper;
 
/**
 * Loosing logfiles?
 * There is a known issue with DailyRollingFileAppender and 1.2.x.<br>
 * It is fixed in the discontinued 1.3 and in the experimental 2.x, but 
in production system
 * you really don't want to loose your log-files, and you are most 
likely using the stable 1.2.x.
 * 
 * </p>
 * The defects causing the need for this additional DatedFileAppender:<br>
 * log4j defects<br>
 * <ul>
 * <li>https://issues.apache.org/bugzilla/show_bug.cgi?id=29726</li>
 * <li>https://issues.apache.org/bugzilla/show_bug.cgi?id=39023</li>
 * <li>https://issues.apache.org/bugzilla/show_bug.cgi?id=41735</li>
 * <li>https://issues.apache.org/bugzilla/show_bug.cgi?id=43374</li>
 * </ul>
 * caused by java sun defect<br>
 * <ul>
 * <li>http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4167147</li>
 * </ul>
 * 
 * <p>
 * The main reason for this is the defect in java.io.File.renameTo() 
causing log-files to be lost occasionally.<br>
 * The RollingFileAppender tries to rename the old files during roll over.
 * However, it does not check if the rename succeeded and as a result, 
it may end up deleting information.<br>
 * There is no exception thrown just a boolean return for success or 
failure.<br>
 * In particular, on Windows, the file rename can fail if
 * another process has the file open.
 * To avoid the whole renaming-problem the log-file is created with date 
suffix, and not postponed until rolling occurs.
 * 
 * </p>
 * 
 * DatedFileAppender appends Log4j logging events to a different log 
file every
 * day. Today's date (as yyyy-MM-dd) is appended to the end of the 
file.<br>
 * DatedFileAppender creates a new log file with the first message it 
logs on a
 * given day and continues to use that same file throughout the day. 
<br>Thus avoiding the whole rename-problem.<br><br>
 * 
 * If you want hourly/minutely etc,modify the datepattern and 
resolveNextRollOverTime method accordingly.
 * 
 * The following sample log4j.xml appender block
 * 
 * <pre>
 *  &lt;appender name=&quot;file&quot; 
class=&quot;no.gwr.log4j.DatedFileAppender&quot;&gt;
 *  &lt;param name=&quot;File&quot; 
value=&quot;application-log.log&quot; /&gt;  
 *  &lt;layout class=&quot;org.apache.log4j.PatternLayout&quot;&gt;
 *  &lt;param name=&quot;ConversionPattern&quot; 
value=&quot;[%d{dd.MM.yyyy HH:mm:ss}, %-5p, %c{1}] - %m%n&quot;/&gt;
 *  &lt;/layout&gt;
 *  &lt;/appender&gt;
 * </pre>
 * 
 * 
 * @author geirwr
 * 
 */
public class DatedFileAppender extends FileAppender {
 
    private Calendar calendar = Calendar.getInstance();
    private String datePattern = "'.'yyyy-MM-dd";//change this if you want hourly/minutely, i.e. different rolling than daily
    private long rotationTime = 0L;
    private String file;
    private String fileNamePrefix;
 
    /**
     * Called by AppenderSkeleton.doAppend() to write a log message 
formatted
     * according to the layout defined for this appender.
     */
    public void append(LoggingEvent event) {
        if (this.layout == null) {
            errorHandler.error("No layout set for the appender named [" 
+ name+ "].");
            return;
        }
        long n = System.currentTimeMillis();
        if (n >= rotationTime) {
            rollOver(n);
            try {
            	customCleanUp();
            } catch(Exception e) {
            	LogLog.error("ERROR when call customCleanUp", e);
            }
            //this.fileName = C:\logs\pj7\sb-test.log.2012-07-10-09-41
            //this.fileName = C:\logs\pj7\sb-test.log.2012-07-10-09-42
            //this.fileName = C:\logs\pj7\sb-test.log.2012-07-10-09-43
            //LogLog.debug("this.fileName = " + this.fileName + ", this.file = " + this.file);
        }
        if (this.qw == null) { // should never happen
            errorHandler.error("No output stream or file set for the appender named ["+ name + "].");
            return;
        }
        subAppend(event);
    }
 
    /**
     * rolling over to a new file. 
     * No File.renameTo or File.copy necessary, as the appropriate 
filename is logged to directly
     * not as in default DailyRollingFileAppender, which can cause 
problems in Windows-env
     * 
     * @param currentTimeInMillis
     */
    private void rollOver(long currentTimeInMillis) {
        calendar.setTime(new Date(currentTimeInMillis));
        File newFile = new File(getFileName());
        this.fileName = newFile.getAbsolutePath();
        rotationTime=resolveNextRollOverTime(calendar);
        super.activateOptions(); // close current file and open new file
    }
    /**
     * @return a filename containing the timestamp part
     */
    private String getFileName() {
        StringBuffer sb = new StringBuffer();
        sb.append(file);
        //LogLog.debug("datePattern = " + datePattern);
        SimpleDateFormat fmt = new SimpleDateFormat(datePattern);
        sb.append(fmt.format(calendar.getTime()));
        return sb.toString();
    }
 
    @Override
    public void setFile(String f) {
        this.file = f;
        
        File dummy = new File(f);
        this.fileNamePrefix = dummy.getName();        
        
        super.setFile(getFileName());
    }
 
    /**
     * resolving next rollover time-
     * improve with hourly, minutely,weekly,monthly if needed.
     * Must also have a datepattern to match it.
     * Could the rollover-frequency be resolved from datePattern, as in 
DailyRollingFileAppender?
     * sample 
     * <pre>
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (DAILY) {hour=minute=0;}//due to daily rollover
        calendar.clear(); // clear all fields
        calendar.set(year,month,day,hour,minute); // setting next rollover
     * </pre>
     * Sets a calendar to the start of tomorrow, with all time values 
reset to
     * zero.
     * This must be changed if different rolling wanted. 
     */
    public long resolveNextRollOverTime(Calendar calendar) {
        /*int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH)+1;//gives daily rolling
        //int hour = calendar.get(Calendar.HOUR_OF_DAY)+1;//gives hourly rolling
        //int minute = calendar.get(Calendar.MINUTE)+1;//gives minutely rolling
        calendar.clear(); 
        calendar.set(year,month,day); 
        return calendar.getTime().getTime();*/
    	if (this.type == TOP_OF_TROUBLE) {
    		this.type = computeCheckPeriod();
    	}
    	
    	switch (type) {
		case TOP_OF_MINUTE:
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.MINUTE, 1);
			break;
		case TOP_OF_HOUR:
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			break;
		case HALF_DAY:
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour < 12) {
				calendar.set(Calendar.HOUR_OF_DAY, 12);
			} else {
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			break;
		case TOP_OF_DAY:
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, 1);
			break;
//		case TOP_OF_WEEK:
//			calendar.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
//			calendar.set(Calendar.HOUR_OF_DAY, 0);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			calendar.set(Calendar.MILLISECOND, 0);
//			calendar.add(Calendar.WEEK_OF_YEAR, 1);
//			break;
		case TOP_OF_MONTH:
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.MONTH, 1);
			break;
		default:
			throw new IllegalStateException("Unknown periodicity type.");
		}
		return calendar.getTime().getTime();
    }
 
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }
 
    public String getDatePattern() {
        return datePattern;
    }
	
    /* COPY FROM CustomDailyRollingFileAppender */
    
    static final int TOP_OF_TROUBLE = -1;
	static final int TOP_OF_MINUTE = 0;
	static final int TOP_OF_HOUR = 1;
	static final int HALF_DAY = 2;
	static final int TOP_OF_DAY = 3;
	static final int TOP_OF_WEEK = 4;
	static final int TOP_OF_MONTH = 5;
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
    
    private String jndiName;
	private String archiveMaxDays = "30";
	private String archiveCompress = "true";	
	private String archiveTiming = "03:00:00";//11am GMT+8
	private Date lastBackup;
    
    private int type = TOP_OF_TROUBLE;
    
    public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getArchiveMaxDays() {
		return archiveMaxDays;
	}

	public void setArchiveMaxDays(String archiveMaxDays) {		
		String jndiKey = StringUtils.substringBetween(archiveMaxDays, "{", "}");
		this.archiveMaxDays = PropertyHelper.getProperty(jndiName, jndiKey);
		if (this.archiveMaxDays == null) {
			this.archiveMaxDays = "30";
		}
		LogLog.debug("archiveMaxDays = " + this.archiveMaxDays);
	}

	public String getArchiveCompress() {
		return archiveCompress;
	}

	public void setArchiveCompress(String archiveCompress) {
		String jndiKey = StringUtils.substringBetween(archiveCompress, "{", "}");
		this.archiveCompress = PropertyHelper.getProperty(jndiName, jndiKey);
		if (this.archiveCompress == null) {
			this.archiveCompress = "true";
		}
		LogLog.debug("archiveCompress = " + this.archiveCompress);
	}

	public String getArchiveTiming() {
		return archiveTiming;
	}

	public void setArchiveTiming(String archiveTiming) {
		String jndiKey = StringUtils.substringBetween(archiveTiming, "{", "}");
		this.archiveTiming = PropertyHelper.getProperty(jndiName, jndiKey);
		if (this.archiveTiming == null) {
			this.archiveTiming = "07:00:00";
		}
		LogLog.debug("archiveTiming = " + this.archiveTiming);
	}

	public Date getLastBackup() {
		return lastBackup;
	}

	public void setLastBackup(Date lastBackup) {
		this.lastBackup = lastBackup;
	}
	
    int computeCheckPeriod() {
		RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone,
				Locale.ENGLISH);
		// set sate to 1970-01-01 00:00:00 GMT
		Date epoch = new Date(0);
		if (datePattern != null) {
			for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						datePattern);
				simpleDateFormat.setTimeZone(gmtTimeZone); // do all date
				// formatting in GMT
				String r0 = simpleDateFormat.format(epoch);
				rollingCalendar.setType(i);
				Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
				String r1 = simpleDateFormat.format(next);
				//LogLog.debug("Type = "+i+", r0 = "+r0+", r1 = "+r1);
				if (r0 != null && r1 != null && !r0.equals(r1)) {
					return i;
				}
			}
		}
		return TOP_OF_TROUBLE; // Deliberately head for trouble...
	}
    
    private void customCleanUp() {
		// Check to see if there are already 5 files
		File file = new File(this.fileName);
		Calendar cal = Calendar.getInstance();
		int maxDays = 30;
		try {
			maxDays = Integer.parseInt(archiveMaxDays);
		} catch (Exception e) {
			// just leave it at 30.
		}
		cal.add(Calendar.DATE, -maxDays);
		Date cutoffDate = cal.getTime();
		
		Date timeBackup = DateUtil.parse(DateUtil.format(new Date(), "dd/MM/yyyy ") + archiveTiming, "dd/MM/yyyy HH:mm:ss");		
		
		//This make sure only backup once per day
		//LogLog.debug("cleanupAndRollOver checked:: cutoffDate = " + cutoffDate + ", timeBackup = " + timeBackup + ", lastBackup = " + lastBackup + ", file.getParentFile().exists() = " + file.getParentFile().exists());
		if ((lastBackup == null || timeBackup.after(lastBackup)) && timeBackup.compareTo(new Date()) <= 0 && file.getParentFile().exists()) {
			LogLog.debug("cleanupAndRollOver executed:: cutoffDate = " + cutoffDate + ", timeBackup = " + timeBackup + ", lastBackup = " + lastBackup);
			
			File[] files = file.getParentFile().listFiles(
					new StartsWithFileFilter(this.fileNamePrefix, false));
			
			LogLog.debug("cleanupAndRollOver executed:: files size = " + files.length + ", fileNamePrefix = " + this.fileNamePrefix + ", file.getName() = " + file.getName());
			
			int nameLength = this.fileNamePrefix.length();
			SimpleDateFormat fmt = new SimpleDateFormat(datePattern);
			
			for (int i = 0; i < files.length; i++) {
				String datePart = null;
				try {
					if (!file.getName().equals(files[i].getName())) {
						datePart = files[i].getName().substring(nameLength);
						Date date = fmt.parse(datePart);
						LogLog.debug("datePart = " + datePart + ", date = " + date + " vs cutoffDate " + cutoffDate);
						if (date.before(cutoffDate)) {						
							if (archiveCompress.equalsIgnoreCase("true")) {							
								zipAndDelete(files[i]);
							} else {
								LogLog.debug("delete file = " + files[i].getName());
								files[i].delete();
							}
						}
					}
				} catch (Exception e) {
//					e.printStackTrace();
					// This isn't a file we should touch (it isn't named
					// correctly)
				}
			}
			lastBackup = new Date();
		}			
	}
	
	/**
	 * Compresses the passed file to a .zip file, stores the .zip in the same
	 * directory as the passed file, and then deletes the original, leaving only
	 * the .zipped archive.
	 * 
	 * @param file
	 */
	private void zipAndDelete(File file) throws IOException {
		if (!file.getName().endsWith(".zip")) {
			LogLog.debug("zipAndDelete file = " + file.getName());
			String folderName = "archive";
			File folder = new File(file.getParent(), folderName);
			if (!folder.exists()) {
				if (folder.mkdir()) {
					LogLog.debug("Create '" + folderName + "' folder success.");
				} else {
					LogLog.debug("Create '" + folderName + "' folder failed.");
				}
			}
			
			File zipFile = new File(folder, file.getName() + ".zip");
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zos.putNextEntry(zipEntry);

			byte[] buffer = new byte[4096];
			while (true) {
				int bytesRead = fis.read(buffer);
				if (bytesRead == -1)
					break;
				else {
					zos.write(buffer, 0, bytesRead);
				}
			}
			zos.closeEntry();
			fis.close();
			zos.close();
			file.delete();
		}
	}
	
	class StartsWithFileFilter implements FileFilter {
		private String startsWith;
		private boolean inclDirs = false;

		/**
	     * 
	     */
		public StartsWithFileFilter(String startsWith,
				boolean includeDirectories) {
			super();
			this.startsWith = startsWith.toUpperCase();
			inclDirs = includeDirectories;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FileFilter#accept(java.io.File)
		 */
		public boolean accept(File pathname) {
			if (!inclDirs && pathname.isDirectory()) {
				return false;
			} else
				return pathname.getName().toUpperCase().startsWith(startsWith);
		}
	}
}