package com.pr7.constant;

import org.apache.commons.lang.StringUtils;

public class Constants {
	public static final String DELIMITER_COMMA = ",";
	
	public static final int SOURCE_7M = 1;
	public static final int SOURCE_FLASHSCORE = 2;
	
	public static final int TYPE_UNDEFINED = -1;
	public static final int TYPE_TOURNAMENT = 1;
	public static final int TYPE_LEAGUE = 2;
	
	public static final int SPORT_FOOTBALL = 1;
	public static final int SPORT_BASKETBALL = 2;
	
	public static final String UPDATE_BY_SCRAPER = "SCRAPER";
	
	public static final String STAT_BALL_POSSES = "Ball Possesion";
	public static final String STAT_GOAL_ATTEMPT = "Goal Attempts";
	public static final String STAT_SHOT_ON = "Shots on Goal";
	public static final String STAT_SHOT_OFF = "Shots off Goal";
	public static final String STAT_FREE_KICK = "Free Kicks";
	public static final String STAT_CORNER = "Corner Kicks";
	public static final String STAT_OFFSIDE = "Offsides";
	public static final String STAT_THROWIN = "Throw-in";
	public static final String STAT_SAVES = "Goalkeeper Saves";
	public static final String STAT_GOAL_KICK = "Goal Kicks";
	public static final String STAT_FOUL = "Fouls";
	public static final String STAT_YELLOW = "Yellow Cards";
	public static final String STAT_RED = "Red Cards";
	
	public static final String STAT_FINISHED = "Finished";
	public static final String STAT_AFTERPEN = "After Penalties";
	public static final String STAT_AFTEREXTRA = "After Extra Time";
	
	
	// Following constant store the value of EventStatistic.Teamtype as per saved into database
	public static final int TEAM_HOME = 0;
	public static final int TEAM_AWAY = 1;
	
	// Following constant store the value of MatchStatistic.Period as per saved into database
	public static final int PERIOD_1STHALF = 1;
	public static final int PERIOD_2NDHALF = 2;
	public static final int PERIOD_EXTRA = 3;
	public static final int PERIOD_PENALTY = 4;
	
	
	// Following constant store the value of event.inplaystatus as per saved into database
	public static final int INPLAYSTAT_FUTURE = 0;	
	public static final int INPLAYSTAT_FINISHED = 1;
	public static final int INPLAYSTAT_INPLAY = 2;	
	public static final int INPLAYSTAT_CANCEL = 3;	
	public static final int INPLAYSTAT_POSTPONED = 4;
	
	public static final int PERIOD_STATISTIC_DONE = 5;
	public static final int PERIOD_LAST = 10;	
	public static final int PERIOD_TODAY = 11;

	public static final int STATUS_PENDING = 0;
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_INACTIVE = 2;	
	
	public static final String TIMEZONE_DEFAULT = "GMT";
	public static final String TIMEZONE_FLASHSCORE = !StringUtils.isEmpty(System.getProperty("timezone.flashscore")) ? System.getProperty("timezone.flashscore") : "GMT+02:00";
	public static final String TIMEZONE_7M = !StringUtils.isEmpty(System.getProperty("timezone.7m")) ? System.getProperty("timezone.7m") : "GMT+08:00";
	
	// Following constant store the label/description of Match Period as per Flashscore Statistic Page
	public static final String STAT_1STHALF = "1st Half";
	public static final String STAT_2NDHALF = "2nd Half";
	public static final String STAT_HALFTIME = "Half Time";
	public static final String STAT_EXTRATIME = "Extra Time";
	public static final String STAT_PENALTY = "Penalties";
	
	public static final int EVENTTYPE_GOAL = 1;
	public static final int EVENTTYPE_REDCARD = 2;
	public static final int EVENTTYPE_YELLOWCARD = 3;
	
	// Use to calculate Match Rating Value
	public static final double HOME_MULTIPLIER = 0.0156;
	public static final double HOME_ADDITIER = 0.4647;
	public static final double AWAY_MULTIPLIER1 = 0.0003;
	public static final double AWAY_MULTIPLIER2 = 0.0127;
	public static final double AWAY_ADDITIER = 0.2365;
	public static final double DRAW_MULTIPLIER1 = 0.0003;
	public static final double DRAW_MULTIPLIER2 = 0.0029;
	public static final double DRAW_ADDITIER = 0.2948;
	
	public static final String FILTER_COUNTRY_FS = System.getProperty("filter.country.fs");//can be null
	public static final String FILTER_LEAGUE_7M = System.getProperty("filter.league.7m");	
	public static final String TIMING_EOD = System.getProperty("eod.cron.timing");
	public static final String TIMING_INPLAYMGMT = System.getProperty("inplaymgmt.cron.timing");
	public static final int TIMING_INPLAYMGMT_DELAYSEC = !StringUtils.isEmpty(System.getProperty("inplaymgmt.delaysec")) ? Integer.valueOf(System.getProperty("inplaymgmt.delaysec")): -1;
	public static final String TIMING_INPLAYMGMT_BACKWARD = System.getProperty("inplaymgmt.backward");
	public static final String TIMING_INPLAYMGMT_FORWARD = System.getProperty("inplaymgmt.forward");
	public static final String TIMING_INPLAYSTATISTIC = System.getProperty("inplaystatistic.cron.timing");	
	public static final String EOD_ENABLED_LIST = System.getProperty("eod.enable.list");
	public static final String FS_INPLAYURL_SUFFIX = !StringUtils.isEmpty(System.getProperty("fs.inplayurl.suffix")) ? System.getProperty("fs.inplayurl.suffix") : "/#statistics";
	public static final String FIREFOX_BIN = !StringUtils.isEmpty(System.getProperty("webdriver.firefox.bin")) ? System.getProperty("webdriver.firefox.bin") : System.getenv("webdriver.firefox.bin");
	public static final int PAGE_LOADING_TIMEOUT = !StringUtils.isEmpty(System.getProperty("page.loading.timeout")) ? Integer.valueOf(System.getProperty("page.loading.timeout")) : 15;
	
	public static final int BROWSER_POOL_SIZE = !StringUtils.isEmpty(System.getProperty("browser.pool.size")) ? Integer.valueOf(System.getProperty("browser.pool.size")) : 2;
	public static final int BROWSER_DISPLAY_STARTFROM = !StringUtils.isEmpty(System.getProperty("browser.display.startfrom")) ? Integer.valueOf(System.getProperty("browser.display.startfrom")) : 90;
	
	public static final int TASK_PENDING = 0;
	public static final int TASK_RUNNING = 1;
	public static final int TASK_STOPPED = 2;
        
	public static final String SCHD_LOG_PENDING = "PENDING";
	public static final String SCHD_LOG_SUCCESS = "SUCCESS";
	public static final String SCHD_LOG_FAIL = "FAIL";
	
	public static final int SORTBY_LEAGUEPRIORITY = 1;
	public static final int SORTBY_DATEMATCH = 2;
	
	public static final int HALFDAY_MINUTES = 720;
	
	public static final String COUNTRY_MAPPING_IGNORE = ",International,Europe,America,Asia,Africa,Oceania,Africa,Asia,Australia & Oceania,Europe,North & Central America,South America,World,".toLowerCase();
        
        public static final String CRYPTO_SHA1 = "SHA-1";
        public static final String CRYPTO_MD5 = "MD5";

    public static final String NSC_COOKIE_PRO = "NSC_N2_NFNCFS_WT";
}
