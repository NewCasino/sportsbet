package com.pr7.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConnectionHelperTest {
	private static final Logger _logger = LogManager.getLogger(ConnectionHelperTest.class);
	private static String NEWCORE_COOKIES = ConnectionHelper.extractCookieValues("NSC_BHFODZ_WT=ffffffff096c0acf45525d5f4f58455e445a4a425b80;expires=Thu, 14-Jun-2012 04:15:29 GMT;path=/;httponly; JSESSIONID=92c49fa4f6af57ba5d78a3776f4f; Path=/managersite; HttpOnly; ");
	
	@Before
	public void init() {
		BasicConfigurator.configure();
	}	
	
	@Test
	public void testConnectUrl() throws Exception {
		String url = "http://113.212.177.39/user_smartbets8/?p=login_action&bw=&"; 
		String params = "fr_gdcode=" + 1234 + "&fr_language=english&fr_password=qwe123&fr_submit=Enter&fr_username=dm0170";
		
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("Host", "113.212.177.39");                
		requestHeader.put("Referer", "http://113.212.177.39/user_smartbets8/login_header.php?invalid=&language=english&red=");
        requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        requestHeader.put("Accept-Language", "en-us,en;q=0.5");
        requestHeader.put("Accept-Encoding", "gzip, deflate");
        requestHeader.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        requestHeader.put("Connection", "keep-alive");
        requestHeader.put("Cookie", "WEBSESSID=" + 12345); 
        
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "POST", params, requestHeader);
		
		for(Entry<String, Object> entry: result.entrySet()) {
			_logger.debug(entry.getKey() + " = " + entry.getValue());
		}
	}
	
	@Test
	public void testConnectUrl2() throws Exception {
		String url = "http://113.212.177.39/user_smartbets8/draw_gd.php"; 
		String params = "";
		
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("Host", "113.212.177.39");                
		requestHeader.put("Referer", "http://113.212.177.39/user_smartbets8/login_header.php?invalid=&language=english&red=");
        requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        requestHeader.put("Accept-Language", "en-us,en;q=0.5");
        requestHeader.put("Accept-Encoding", "gzip, deflate");
        requestHeader.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        requestHeader.put("Connection", "keep-alive");
        requestHeader.put("Cookie", "WEBSESSID=" + 12345); 
        
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "POST", params, requestHeader);
		
		for(Entry<String, Object> entry: result.entrySet()) {
			_logger.debug(entry.getKey() + " = " + entry.getValue());
		}
		byte[] bodyarray = (byte[]) result.get("bodyarray");
		Assert.assertTrue(bodyarray.length > 0);
	}
	
	@Test
	public void testConnectUrlSboBet() throws Exception {
		String url = "http://www.sbobet.com/"; 
		String params = "";
		
		Map<String, String> requestHeader = new HashMap<String, String>();
        requestHeader.put("Cookie", "visited=yes;"); 
        
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "GET", params, requestHeader);
		
		_logger.debug("printHeader = " + ConnectionHelper.printHeader(result));
	}
	
	@Test
	public void testExtractCookieValues() {
		String src = "WEBSESSID=2dfce9641bd414872bdd3d5503253359; path=/;" +
				"user_onlinekey268454=41316c849d52e07a535471f47aa37303; expires=Wed, 21-Sep-2011 03:51:51 GMT; path=/;" +
				"user_onlineip268454=117.20.168.250; expires=Wed, 21-Sep-2011 03:51:51 GMT; path=/;" +
				"user_username268454=bwt_dm0170; expires=Wed, 21-Sep-2011 03:51:51 GMT; path=/;" +
				"cookie_user_onlinekey=41316c849d52e07a535471f47aa37303; expires=Wed, 21-Sep-2011 03:51:51 GMT; path=/;" +
				"cookie_user_onlineip=117.20.168.250; expires=Wed, 21-Sep-2011 03:51:51 GMT; path=/;" +
				"cookie_user_username=bwt_dm0170; expires=Wed, 21-Sep-2011 03:51:51 GMT; path=/;" +
				"cookie_user_language=english; expires=Wed, 21-Sep-2011 03:51:51 GMT; path=/;";
		String cookieValues = ConnectionHelper.extractCookieValues(src);
		
		_logger.debug("cookieValues = " + cookieValues);
	}
	
	@Test
	public void testConnectUrlLoginSb() throws Exception {
		String mainUrl = "https://www.sbobet.com/processlogin.aspx";
		//				 code=2954&id=hientran1985&key=351431918665440&lang=en&page=default&password=minh%401616&sv=A09&tea=9965566&tzDiff=0
		String params = "code=0837&id=hientran1985&key=287039715203204&lang=en&page=default&password=minh%401616&sv=A05&tea=8141170&tzDiff=0";
		String url = mainUrl;
		Map<String, String> requestHeader = new HashMap<String, String>();		
		requestHeader.put("Referer", "http://www.sbobet.com/hometop.aspx?ip=&p=");
		requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:7.0.1) Gecko/20100101 Firefox/7.0.1");
		
		String cookie = "lang=en; __utma=1.1895406573.1321872771.1321872771.1321927037.2; __utmz=1.1321872771.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); affiliate=www.sbobet.com; AWSUSER_ID=awsuser_id1321872773167r9580; __utmb=1.46.10.1321927037; visited=yes; AWSSESSION_ID=awssession_id1321927039124r6783; G_DN=94m4jlizit79; logged=yes; ASP.NET_SessionId=lynmo045hzbfwle5nfjlpcbf; c152i3=83951882.20480.0000; __utmc=1";		
		
		requestHeader.put("Cookie", cookie); 
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "POST", params, requestHeader);
		cookie = result.get("Set-Cookie").toString();
	
		_logger.debug("printHeader = " + ConnectionHelper.printHeader(result));
	}
	
	@Test
	public void testNewAgentLogin() throws Exception {		
//		Accept	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//		Accept-Encoding	gzip, deflate
//		Accept-Language	en-us,en;q=0.5
//		Cache-Control	no-cache
//		Connection	keep-alive
//		Content-Length	250
//		Content-Type	text/x-gwt-rpc; charset=utf-8
//		Cookie	JSESSIONID=404522aa5f34187d34c30417bbdf; NSC_BHFODZ_WT=ffffffff096c0af045525d5f4f58455e445a4a425b80
//		Host	203.90.242.132
//		Pragma	no-cache
//		Referer	http://203.90.242.132/managersite/
//		User-Agent	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0
//		X-GWT-Module-Base	http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/
//		X-GWT-Permutation	B1D2E69C159ABEC0AA19A744C9B3363B
		
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("Content-Type", "text/x-gwt-rpc; charset=utf-8");
		//requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0");
		//requestHeader.put("X-GWT-Module-Base", "http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/");
		//requestHeader.put("X-GWT-Permutation", "B1D2E69C159ABEC0AA19A744C9B3363B");
		
		//String cookie = "JSESSIONID=404522aa5f34187d34c30417bbdf; NSC_BHFODZ_WT=ffffffff096c0af045525d5f4f58455e445a4a425b80";		
		
		requestHeader.put("Cookie", "");
		String url = "http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/services";
		//12EF50A59C6A77331104E48F1DEA04B0 -> com.leo.agency.ui.service.IUserLoginService function ZZf(){TZf();_nd.call(this,$moduleBase,'12EF50A59C6A77331104E48F1DEA04B0',SZf)}
		String params = "7|0|9|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|12EF50A59C6A77331104E48F1DEA04B0|com.leo.agency.ui.service.IUserLoginService|getLoginUser|java.lang.String/2004016611|I|SAXN1111|saxon123|en_US|1|2|3|4|4|5|5|5|6|7|8|9|1|";
		
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "POST", params, requestHeader);
		_logger.debug("printHeader = " + ConnectionHelper.printHeader(result));
		
		String cookies = ConnectionHelper.extractCookieValues((String) result.get("Set-Cookie"));
		System.out.println("cookies = " + cookies);
		
		String url2 = "http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/services";
		//function GZf(){AZf();_nd.call(this,$moduleBase,'4B54C4C528C8675AE00DD40EAA35847C',zZf)}
		String params2 = "7|0|7|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|4B54C4C528C8675AE00DD40EAA35847C|com.leo.agency.ui.service.IUserListingService|searchDirectDownlineAgents|I|java.lang.String/2004016611|SAXN1111|1|2|3|4|5|5|6|5|5|5|1|7|439|0|5|";
		
		requestHeader.put("Cookie", cookies);
		result = ConnectionHelper.connectUrl(url2, "POST", params2, requestHeader);
		_logger.debug("printHeader2 = " + ConnectionHelper.printHeader(result));
	}
	
	@Test
	public void testCheckAvail() throws Exception {
		String cookies = "JSESSIONID=414915d180fba752d65f439e59a3; NSC_BHFODZ_WT=ffffffff096c0af045525d5f4f58455e445a4a425b80"; 
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("Content-Type", "text/x-gwt-rpc; charset=utf-8");
		requestHeader.put("Cookie", cookies);
		
		String url = "http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/services";
		//function nZf(){iZf();_nd.call(this,$moduleBase,'16B19CD8DAEC2C566AE037B29311FBD4',hZf)} -> com.leo.agency.ui.service.IUserCreationService
		String params = "7|0|8|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|16B19CD8DAEC2C566AE037B29311FBD4|com.leo.agency.ui.service.IUserCreationService|getInitDataFromValidateUsercode|java.lang.String/2004016611|I|Z|SAXN1111002|1|2|3|4|4|5|6|6|7|8|1|5|0|";
		
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "POST", params, requestHeader);
		_logger.debug("printHeader2 = " + ConnectionHelper.printHeader(result));
	}
	
	@Test
	public void testGetBal() throws Exception {
		String cookies = "JSESSIONID=4edee6f917d2faf3f5fa454d0de6; NSC_BHFODZ_WT=ffffffff096c0acf45525d5f4f58455e445a4a425b80"; 
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("Content-Type", "text/x-gwt-rpc; charset=utf-8");
		requestHeader.put("Cookie", cookies);
		
		String url = "http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/services";
		//function rXf(){lXf();_nd.call(this,$moduleBase,'8F802EF15CBBC35C51275E21997BEEDF',kXf)}
		String memberCode = "SAXN1111013";//"%AAA";
		String params = "7|0|15|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|8F802EF15CBBC35C51275E21997BEEDF|com.leo.agency.ui.service.ICreditLimitListingService|searchListCreditLimit|com.extjs.gxt.ui.client.data.PagingLoadConfig|com.leo.agency.ui.agency.creditlisting.CreditListingQueryVO/4018745662|com.extjs.gxt.ui.client.data.BasePagingLoadConfig/2011366567|com.extjs.gxt.ui.client.data.RpcMap/3441186752|sortField|sortDir|com.extjs.gxt.ui.client.Style$SortDir/640452531|offset|java.lang.Integer/3438268394|limit|%s|1|2|3|4|2|5|6|7|0|1|8|4|9|0|10|11|0|12|13|0|14|13|50|6|1|0|0|15|439|-1|";
		
		params = String.format(params, memberCode);
		
		System.out.println(params);
		
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "POST", params, requestHeader);
		_logger.debug("printHeader2 = " + ConnectionHelper.printHeader(result));
		
		String sbal = StringUtils.substringBetween((String) result.get("body"), "availableBalanceDouble\",\"", "\",\"");
		Double bal = Double.parseDouble(sbal);
		System.out.println("bal = " + bal + ", sbal = " + sbal);
	}
	
	@Test
	public void testString() {
		//9999 credit limit, 1111 minbet, 22222 max bet, 33333 max bet per match
		
		String s = "7|0|104|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|16B19CD8DAEC2C566AE037B29311FBD4|com.leo.agency.ui.service.IUserCreationService|createUser|com.leo.agency.ui.vo.UserCreationVO/493402625|com.leo.agency.ui.model.User/2887661713|13/06/2012 04:25:12|com.extjs.gxt.ui.client.data.RpcMap/3441186752|loginId|creditLimitDouble|java.math.BigDecimal/8151472|10000000000|levelId|java.lang.Integer/3438268394|state|baseCurrency|settlementModeId|securityQuestion|password|java.lang.String/2004016611|{sha-1}WoFGDii4wgdGw+tTG7T8K3zepNWjGgc=|totalSuspendedMember|city|isDetail|lastLoginIp|182.55.247.179|postalCode|totalActiveMember|buId|userId|parentUserId|gender|birthDate|firstName|createdDate|java.util.Date/3385151746|securityAnswer|lastLoginTime|countryId|totalClosedMember|lastName|totalInActiveMember|userCode|SAXN1111|textBuBaseCurrency|CNY|hasChangedPassword|userRole|lastActivityTime|userStatusId|languageId|oddsGroupId|phoneNo|maxMemberCreditLimitDouble|email|address|maxAgentCreditLimitDouble|0|faxNo|textBaseCurrency|selfExclusion|userCategoryId|comments|mobileNo|userCodeForAudit|userIdForAudit|java.util.ArrayList/4159755760|com.leo.agency.ui.model.Commission/445191376|com.leo.agency.ui.model.Sport/1570216373|com.leo.agency.ui.model.BetSetting/2645023239|22222|1111|com.leo.agency.ui.model.PositionTaking/1082007261|Mix Parlay|33333|HDP/Next Goal|PT1|Over/Under|1st HDP|1st OU|Odd/Even|1x2|PT2|Correct Score|Total Goal|HTFT|FGLG|Outright|Live HDP|Live AH|Live OU|Live 1st HDP|Live 1st OU|Soccer|userCode1|userCode2|userCode3|password123|First|Last|Phone|Mobile|9999|Fax"
			+ "|1|2|3|4|1|5|5|0|0|6|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|7|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|8|48|9|0|10|11|12|13|14|4|15|0|16|14|5|17|14|1|18|0|19|20|21|22|14|0|23|0|24|-9|25|20|26|27|0|28|-9|29|-7|30|14|439|31|14|438|32|-9|33|0|34|0|35|36|112|5|12|22|58|20|37|0|38|36|112|5|13|2|32|58|39|-9|40|-9|41|0|42|-9|43|20|44|45|20|46|47|-7|48|0|49|36|112|5|12|22|58|20|50|-7|51|-9|52|-7|53|0|54|11|12|55|0|56|0|57|11|58|59|0|60|-16|61|0|62|-9|63|0|64|0|65|-15|66|-11|67|0|67|3|68|3|1|0|0|1|0|0|0|0|11|58|68|0|1|0|0|0|0|0|0|0|11|58|68|0|2|0|0|0|0|0|0|0|11|58|0|0|0|67|2|69|70|0|11|71|11|58|11|72|0|0|67|1|73|1|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|74|0|0|5|5|74|1|0|0|74|69|70|0|11|71|11|75|11|72|0|0|67|15|73|2|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|76|0|0|1|1|77|2|0|73|3|0|2|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|78|0|0|1|1|77|3|0|73|4|0|3|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|79|0|0|1|1|77|4|0|73|5|0|4|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|80|0|0|1|1|77|5|0|73|6|0|5|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|81|0|0|1|1|77|6|0|73|7|0|6|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|82|0|0|2|2|83|7|0|73|8|0|7|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|84|0|0|2|2|83|8|0|73|9|0|8|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|85|0|0|2|2|83|9|0|73|10|0|9|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|86|0|0|2|2|83|10|0|73|11|0|10|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|87|0|0|2|2|83|11|0|73|12|0|11|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|88|0|0|2|2|83|12|0|73|13|0|12|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|89|0|0|3|3|90|13|0|73|14|0|13|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|91|0|0|4|4|91|14|0|73|15|0|14|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|92|0|0|3|3|90|15|0|73|16|0|15|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|93|0|0|4|4|91|16|0|1|94|0|6|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|8|20|43|-15|95|20|58|96|-59|97|-59|29|-7|31|14|439|13|-6|52|14|3|16|-6|50|-7|19|20|98|34|20|99|41|20|100|53|20|101|64|20|102|10|11|103|57|11|58|54|11|58|59|20|104|24|-9|";
		
		s = "7|0|102|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|16B19CD8DAEC2C566AE037B29311FBD4|com.leo.agency.ui.service.IUserCreationService|createUser|com.leo.agency.ui.vo.UserCreationVO/493402625|com.leo.agency.ui.model.User/2887661713|13/06/2012 04:25:12|com.extjs.gxt.ui.client.data.RpcMap/3441186752|loginId|creditLimitDouble|java.math.BigDecimal/8151472|10000000000|levelId|java.lang.Integer/3438268394|state|baseCurrency|settlementModeId|securityQuestion|password|java.lang.String/2004016611|{sha-1}WoFGDii4wgdGw+tTG7T8K3zepNWjGgc=|totalSuspendedMember|city|isDetail|lastLoginIp|182.55.247.179|postalCode|totalActiveMember|buId|userId|parentUserId|gender|birthDate|firstName|createdDate|java.util.Date/3385151746|securityAnswer|lastLoginTime|countryId|totalClosedMember|lastName|totalInActiveMember|userCode|SAXN1111|textBuBaseCurrency|CNY|hasChangedPassword|userRole|lastActivityTime|userStatusId|languageId|oddsGroupId|phoneNo|maxMemberCreditLimitDouble|email|address|maxAgentCreditLimitDouble|0|faxNo|textBaseCurrency|selfExclusion|userCategoryId|comments|mobileNo|userCodeForAudit|userIdForAudit|java.util.ArrayList/4159755760|com.leo.agency.ui.model.Commission/445191376|com.leo.agency.ui.model.Sport/1570216373|com.leo.agency.ui.model.BetSetting/2645023239|100000|1000|com.leo.agency.ui.model.PositionTaking/1082007261|Mix Parlay|1000000|HDP/Next Goal|PT1|Over/Under|1st HDP|1st OU|Odd/Even|1x2|PT2|Correct Score|Total Goal|HTFT|FGLG|Outright|Live HDP|Live AH|Live OU|Live 1st HDP|Live 1st OU|Soccer|userCode1|A|userCode2|B|userCode3|C|password123|1111|1|2|3|4|1|5|5|0|0|6|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|7|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|8|48|9|0|10|11|12|13|14|4|15|0|16|14|5|17|14|1|18|0|19|20|21|22|14|0|23|0|24|-9|25|20|26|27|0|28|-9|29|-7|30|14|439|31|14|438|32|-9|33|0|34|0|35|36|112|5|12|22|58|20|37|0|38|36|112|5|13|2|32|58|39|-9|40|-9|41|0|42|-9|43|20|44|45|20|46|47|-7|48|0|49|36|112|5|12|22|58|20|50|-7|51|-9|52|-7|53|0|54|11|12|55|0|56|0|57|11|58|59|0|60|-16|61|0|62|-9|63|0|64|0|65|-15|66|-11|67|0|67|3|68|3|1|0|0|1|0|0|0|0|11|58|68|0|1|0|0|0|0|0|0|0|11|58|68|0|2|0|0|0|0|0|0|0|11|58|0|0|0|67|2|69|70|0|11|71|11|58|11|72|0|0|67|1|73|1|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|74|0|0|5|5|74|1|0|0|74|69|70|0|11|71|11|75|11|72|0|0|67|15|73|2|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|76|0|0|1|1|77|2|0|73|3|0|2|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|78|0|0|1|1|77|3|0|73|4|0|3|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|79|0|0|1|1|77|4|0|73|5|0|4|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|80|0|0|1|1|77|5|0|73|6|0|5|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|81|0|0|1|1|77|6|0|73|7|0|6|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|82|0|0|2|2|83|7|0|73|8|0|7|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|84|0|0|2|2|83|8|0|73|9|0|8|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|85|0|0|2|2|83|9|0|73|10|0|9|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|86|0|0|2|2|83|10|0|73|11|0|10|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|87|0|0|2|2|83|11|0|73|12|0|11|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|88|0|0|2|2|83|12|0|73|13|0|12|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|89|0|0|3|3|90|13|0|73|14|0|13|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|91|0|0|4|4|91|14|0|73|15|0|14|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|92|0|0|3|3|90|15|0|73|16|0|15|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|93|0|0|4|4|91|16|0|1|94|0|6|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|8|20|43|-15|95|20|96|97|20|98|99|20|100|29|-7|31|14|439|13|-6|52|14|3|16|-6|50|-7|19|20|101|34|0|41|0|53|0|64|0|10|11|102|57|11|58|54|11|58|59|0|24|-9|";
		
		String[] array1 = s.split("\\|");
		System.out.println(array1.length);
		System.out.println(array1[7]);
		System.out.println(array1[102 + 2]);
		
		s = "//OK[0,126,0,125,-79,124,0,123,0,122,18,83,121,0,120,0,119,0,118,46,6,117,-85,116,0,115,-79,114,-77,113,-77,112,20,58,22,12,5,112,100,111,110,83,109,108,6,107,-79,106,0,105,-79,104,-79,103,57,31,5,13,5,112,100,102,0,101,20,58,22,12,5,112,100,99,0,98,0,97,0,96,-79,95,438,78,94,439,78,93,-77,92,-79,91,0,90,89,83,88,-79,87,0,86,0,78,85,84,83,82,1,78,81,5,78,80,0,79,4,78,77,76,6,75,0,74,45,73,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,72,71,4,69,70,1,69,2,3,68,1,0,16,65,4,4,0,0,67,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,15,0,16,48,0,15,64,3,3,0,0,66,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,14,0,15,48,0,14,65,4,4,0,0,65,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,13,0,14,48,0,13,64,3,3,0,0,63,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,12,0,13,48,0,12,57,2,2,0,0,62,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,11,0,12,48,0,11,57,2,2,0,0,61,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,10,0,11,48,0,10,57,2,2,0,0,60,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,9,0,10,48,0,9,57,2,2,0,0,59,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,8,0,9,48,0,8,57,2,2,0,0,58,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,7,0,8,48,0,7,57,2,2,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,6,0,7,48,0,6,52,1,1,0,0,56,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,5,0,6,48,0,5,52,1,1,0,0,55,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,4,0,5,48,0,4,52,1,1,0,0,54,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,3,0,4,48,0,3,52,1,1,0,0,53,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,2,0,3,48,0,2,52,1,1,0,0,51,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,2,48,15,3,0,0,47,6,50,6,45,6,0,44,43,49,0,0,1,49,5,5,0,0,49,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,48,1,3,0,0,47,6,46,6,45,6,0,44,43,2,3,0,3,1,1,42,4,7,6,38,1,1,41,3,7,6,38,1,1,40,2,7,6,38,1,1,39,1,7,6,38,4,3,0,12,37,36,13,0,11,35,34,13,0,10,33,32,13,0,1,31,30,13,0,9,29,28,13,0,4,27,26,13,0,2,25,24,13,0,8,23,22,13,0,3,21,20,13,0,5,19,18,13,0,7,17,16,13,0,6,15,14,13,12,3,7,6,2,0,0,12,0,0,9999,2,0,4,7,6,1,0,0,11,0,0,1,1,0,4,7,6,0,0,4,10,1,0,1,1,4,4,7,6,0,0,3,9,1,0,1,1,3,4,7,6,0,0,2,8,1,0,1,1,2,4,7,6,0,0,1,5,1,0,1,1,1,4,6,3,0,0,0,0,0,0,0,0,3,2,1,[\"com.leo.agency.ui.vo.UserCreationVO/493402625\",\"com.leo.agency.ui.vo.BLResponseVO/1542966064\",\"java.util.ArrayList/4159755760\",\"com.leo.agency.ui.model.Commission/445191376\",\"Group A\",\"java.math.BigDecimal/8151472\",\"0.00\",\"Group B\",\"Group C\",\"Group D\",\"1x2\",\"Others\",\"com.leo.agency.ui.model.Currency/595015138\",\"AUD\",\"Australian Dollar\",\"CAD\",\"Canadian Dollar\",\"CNY\",\"Chinese Yuan\",\"EUR\",\"Euro\",\"GBP\",\"British Pound\",\"HKD\",\"Hongkong Dollar\",\"IDR\",\"Indonesian Rupian\",\"MYR\",\"Malaysian Ringgit\",\"SGD\",\"Singapore Dollar\",\"THB\",\"Thai Baht\",\"TWD\",\"Taiwan New Dollar\",\"USD\",\"US Dollar\",\"com.leo.agency.ui.model.OddsGroup/3774797869\",\"A\",\"B\",\"C\",\"D\",\"com.leo.agency.ui.model.Sport/1570216373\",\"com.leo.agency.ui.model.BetSetting/2645023239\",\"100000\",\"0\",\"1000\",\"com.leo.agency.ui.model.PositionTaking/1082007261\",\"Mix Parlay\",\"1000000\",\"HDP/Next Goal\",\"PT1\",\"Over/Under\",\"1st HDP\",\"1st OU\",\"Odd/Even\",\"PT2\",\"Correct Score\",\"Total Goal\",\"HTFT\",\"FGLG\",\"Outright\",\"Live HDP\",\"Live AH\",\"Live OU\",\"Live 1st HDP\",\"Live 1st OU\",\"Soccer\",\"com.leo.agency.ui.model.UserStatus/2535589419\",\"Active\",\"Inactive\",\"com.leo.agency.ui.model.User/2887661713\",\"com.extjs.gxt.ui.client.data.RpcMap/3441186752\",\"loginId\",\"creditLimitDouble\",\"10000000000\",\"levelId\",\"java.lang.Integer/3438268394\",\"state\",\"baseCurrency\",\"settlementModeId\",\"password\",\"java.lang.String/2004016611\",\"{sha-1}WoFGDii4wgdGw+tTG7T8K3zepNWjGgc=\",\"totalSuspendedMember\",\"city\",\"isDetail\",\"lastLoginIp\",\"182.55.247.179\",\"postalCode\",\"totalActiveMember\",\"buId\",\"userId\",\"parentUserId\",\"gender\",\"birthDate\",\"maxMemberCreditLimitGuide\",\"firstName\",\"createdDate\",\"java.util.Date/3385151746\",\"securityAnswer\",\"lastLoginTime\",\"countryId\",\"totalClosedMember\",\"lastName\",\"totalInActiveMember\",\"availableBalanceDouble\",\"9999659898.000000\",\"userCode\",\"SAXN1111\",\"lastActivityTime\",\"userStatusId\",\"oddsGroupId\",\"languageId\",\"phoneNo\",\"maxMemberCreditLimitDouble\",\"maxAgentCreditLimitDouble\",\"email\",\"address\",\"faxNo\",\"textBaseCurrency\",\"maxAgentCreditLimitGuide\",\"selfExclusion\",\"userCategoryId\",\"mobileNo\",\"comments\"],0,7]";
		s = StringUtils.substringBetween(s, "com.leo.agency.ui.model.BetSetting", "\",\"com.leo");
		
		///2645023239","100000","0","1000
		String[] array = s.split("\",\"");
		if (array.length == 4) {
			String maxbet = array[1];
			String minbet = array[3];
			System.out.println(maxbet + ", " + minbet);
		} else {
			//Set default
		}
		
	}
	
	@Test
	public void testCreateMember() throws Exception {
		String maxbet = null;
		String minbet = null;
		String mixparlay = null;
		String sha1 = null;
		String code1, code2, code3, password, firstname, lastname, phone, mobile, creditlimit, fax;
		
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("Content-Type", "text/x-gwt-rpc; charset=utf-8");
		requestHeader.put("Cookie", NEWCORE_COOKIES);
		
		//STEP1 Init
		String url = "http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/services";
		String param = "7|0|5|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|16B19CD8DAEC2C566AE037B29311FBD4|com.leo.agency.ui.service.IUserCreationService|getInitData|I|1|2|3|4|2|5|5|439|1|";
				
		Map<String, Object> result = ConnectionHelper.connectUrl(url, "POST", param, requestHeader);
		_logger.debug("printHeader = " + ConnectionHelper.printHeader(result));
		
		String body = (String) result.get("body");
		sha1 = "{sha-1}" + StringUtils.substringBetween(body, "{sha-1}", "\"");
		mixparlay = StringUtils.substringBetween(body, "Mix Parlay\",\"", "\"");
		
		String s = StringUtils.substringBetween(body, "com.leo.agency.ui.model.BetSetting", "\",\"com.leo");
		String[] array = s.split("\",\"");
		if (array.length == 4) {
			maxbet = array[1];
			minbet = array[3];			
		} else {
			//Set default
		}
		
		System.out.println(maxbet + ", " + minbet + ", sha1 = " + sha1 + ", mixparlay = " + mixparlay);
		
		//STEP2 Create member
		//9999 credit limit, 1111 minbet, 22222 max bet, 33333 max bet per match, {sha-1}WoFGDii4wgdGw+tTG7T8K3zepNWjGgc=	
		String param2 = "7|0|107|http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/|16B19CD8DAEC2C566AE037B29311FBD4|com.leo.agency.ui.service.IUserCreationService|createUser|com.leo.agency.ui.vo.UserCreationVO/493402625|com.leo.agency.ui.model.User/2887661713|13/06/2012 04:25:12|com.extjs.gxt.ui.client.data.RpcMap/3441186752|loginId|creditLimitDouble|java.math.BigDecimal/8151472|10000000000|levelId|java.lang.Integer/3438268394|state|baseCurrency|settlementModeId|securityQuestion|password|java.lang.String/2004016611|" +
				":sha1|totalSuspendedMember|city|isDetail|lastLoginIp|182.55.247.179|postalCode|totalActiveMember|buId|userId|parentUserId|gender|birthDate|firstName|createdDate|java.util.Date/3385151746|securityAnswer|lastLoginTime|countryId|totalClosedMember|lastName|totalInActiveMember|" +
				"userCode|SAXN1111|textBuBaseCurrency|CNY|hasChangedPassword|userRole|lastActivityTime|userStatusId|languageId|oddsGroupId|phoneNo|maxMemberCreditLimitDouble|email|address|maxAgentCreditLimitDouble|0|faxNo|textBaseCurrency|selfExclusion|userCategoryId|comments|mobileNo|userCodeForAudit|userIdForAudit|java.util.ArrayList/4159755760|com.leo.agency.ui.model.Commission/445191376|com.leo.agency.ui.model.Sport/1570216373|com.leo.agency.ui.model.BetSetting/2645023239|" +
				":maxbet|:minbet|com.leo.agency.ui.model.PositionTaking/1082007261|Mix Parlay|:mixparlay|HDP/Next Goal|PT1|Over/Under|1st HDP|1st OU|Odd/Even|1x2|PT2|Correct Score|Total Goal|HTFT|FGLG|Outright|Live HDP|Live AH|Live OU|Live 1st HDP|Live 1st OU|Soccer|" +
				"userCode1|:code1|userCode2|:code2|userCode3|:code3|:password|:firstname|:lastname|:phone|:mobile|:creditlimit|:fax" +
				"|1|2|3|4|1|5|5|0|0|6|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|7|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|8|48|9|0|10|11|12|13|14|4|15|0|16|14|5|17|14|1|18|0|19|20|21|22|14|0|23|0|24|-9|25|20|26|27|0|28|-9|29|-7|30|14|439|31|14|438|32|-9|33|0|34|0|35|36|112|5|12|22|58|20|37|0|38|36|112|5|13|2|32|58|39|-9|40|-9|41|0|42|-9|43|20|44|45|20|46|47|-7|48|0|49|36|112|5|12|22|58|20|50|-7|51|-9|52|-7|53|0|54|11|12|55|0|56|0|57|11|58|59|0|60|-16|61|0|62|-9|63|0|64|0|65|-15|66|-11|67|0|67|3|68|3|1|0|0|1|0|0|0|0|11|58|68|0|1|0|0|0|0|0|0|0|11|58|68|0|2|0|0|0|0|0|0|0|11|58|0|0|0|67|2|69|70|0|11|71|11|58|11|72|0|0|67|1|73|1|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|74|0|0|5|5|74|1|0|0|74|69|70|0|11|71|11|75|11|72|0|0|67|15|73|2|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|76|0|0|1|1|77|2|0|73|3|0|2|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|78|0|0|1|1|77|3|0|73|4|0|3|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|79|0|0|1|1|77|4|0|73|5|0|4|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|80|0|0|1|1|77|5|0|73|6|0|5|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|81|0|0|1|1|77|6|0|73|7|0|6|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|82|0|0|2|2|83|7|0|73|8|0|7|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|84|0|0|2|2|83|8|0|73|9|0|8|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|85|0|0|2|2|83|9|0|73|10|0|9|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|86|0|0|2|2|83|10|0|73|11|0|10|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|87|0|0|2|2|83|11|0|73|12|0|11|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|88|0|0|2|2|83|12|0|73|13|0|12|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|89|0|0|3|3|90|13|0|73|14|0|13|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|91|0|0|4|4|91|14|0|73|15|0|14|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|92|0|0|3|3|90|15|0|73|16|0|15|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|93|0|0|4|4|91|16|0|1|94|0|6|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|8|20|43|-15|95|20|96|97|20|98|99|20|100|29|-7|31|14|439|13|-6|52|14|3|16|-6|50|-7|19|20|101|34|20|102|41|20|103|53|20|104|64|20|105|10|11|106|57|11|58|54|11|58|59|20|107|24|-9|";
//		String[] array1 = param2.split("\\|");
//		System.out.println(array1[107 + 2]);
		
		code1 = "0";
		code2 = "0";
		code3 = "7";
		password = "123qwe";
		firstname = "汉语/漢語";
		lastname = "ut";
		phone = "1122";
		mobile = "3344";
		creditlimit = "1234";
		fax = "5566";
		
		param2 = param2.replace(":sha1", sha1);
		param2 = param2.replace(":mixparlay", mixparlay);
		param2 = param2.replace(":maxbet", maxbet);
		param2 = param2.replace(":minbet", minbet);
		param2 = param2.replace(":code1", code1);
		param2 = param2.replace(":code2", code2);
		param2 = param2.replace(":code3", code3);
		param2 = param2.replace(":password", password);
		param2 = param2.replace(":firstname", firstname);
		param2 = param2.replace(":lastname", lastname);
		param2 = param2.replace(":phone", phone);
		param2 = param2.replace(":mobile", mobile);
		param2 = param2.replace(":creditlimit", creditlimit);
		param2 = param2.replace(":fax", fax);
		
		String url2 = "http://203.90.242.132/managersite/com.leo.agency.managersite.ManagerSite/services";

		System.out.println("param2 = " + param2);
		
		Map<String, Object> result2 = ConnectionHelper.connectUrl(url2, "POST", param2, requestHeader);
		_logger.debug("printHeader2 = " + ConnectionHelper.printHeader(result2));
	}
	
	@Test
	public void test() {
		String response = "0,0,107,0,106,-86,105,0,104,0,103,20,4,102,0,101,0,100,20,4,99,0,98,3,62,97,-86,96,2,62,95,0,5,1,15,5,112,83,94,0,93,-86,92,3,9,90,-86,89,112,9,88,-86,87,-86,86,0,85,0,84,0,5,1,15,5,112,83,82,111,9,81,0,80,-86,79,439,62,78,465,62,77,-84,76,-86,75,0,74,0,72,-86,71,0,70,-84,69,110,9,67,0,66,-84,65,-83,64,0,63,-83,61,7,4,59,0,58,44,57,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,56,0,15,0,0,0,15,0,15,0,109,0,15,108,0,0,0,0,107,0,106,-86,105,0,104,0,103,20,4,102,0,101,0,100,60,4,99,0,98,-84,97,-86,96,-84,95,57,30,0,15,5,112,83,94,0,93,-84,92,91,9,90,-86,89,0,88,-86,87,-86,86,18,32,2,15,5,112,83,85,0,84,20,58,22,12,5,112,83,82,0,81,0,80,-86,79,438,62,78,439,62,77,-84,76,-86,75,0,74,73,9,72,-86,71,0,70,0,62,69,68,9,67,0,66,1,62,65,5,62,64,0,63,4,62,61,60,4,59,0,58,44,57,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,56,0,1,1,55,4,4,1,11,4,51,1,1,54,3,3,1,11,4,51,1,1,53,2,2,1,11,4,51,1,1,52,1,1,1,11,4,51,4,15,0,50,4,46,49,3,46,48,2,46,47,1,46,4,15,45,1,0,16,42,4,0,0,0,44,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,15,0,16,24,0,15,41,3,0,0,0,43,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,14,0,15,24,0,14,42,4,0,0,0,42,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,13,0,14,24,0,13,41,3,0,0,0,40,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,12,0,13,24,0,12,34,2,0,0,0,39,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,11,0,12,24,0,11,34,2,0,0,0,38,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,10,0,11,24,0,10,34,2,0,0,0,37,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,9,0,10,24,0,9,34,2,0,0,0,36,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,8,0,9,24,0,8,34,2,0,0,0,35,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,7,0,8,24,0,7,34,2,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,6,0,7,24,0,6,29,1,0,0,0,33,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,5,0,6,24,0,5,29,1,0,0,0,32,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,4,0,5,24,0,4,29,1,0,0,0,31,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,3,0,4,24,0,3,29,1,0,0,0,30,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,2,0,3,24,0,2,29,1,0,0,0,28,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,2,24,15,15,23,4,27,4,22,4,1179,18,0,21,4,26,4,19,4,1229,18,17,25,0,0,1,25,5,0,0,0,25,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,24,1,15,23,4,20,4,22,4,1178,18,0,21,4,20,4,19,4,1228,18,17,2,15,11,4,0,0,0,12,0,0,0,2,0,16,11,4,0,0,0,10,0,0,0,1,0,16,11,4,0,0,0,13,1,1,0,3,0,16,3,15,0,0,-19,0,15,14,4,6,4,11,4,13,9,11,4,12,9,11,4,10,9,3,8,0,1,0.0,6,4,0.0,6,4,0.0,0,465,0.0,6,4,0.0,0.0,6,4,0.0,6,4,0.0,0,7,4,555.0,6,4,0.0,0,5,555.0,5,4,3,761,2,1,[\"com.leo.agency.ui.vo.UserAcctVO/2787799701\",\"com.leo.agency.ui.model.Account/3764101148\",\"SAXN111100G\",\"java.math.BigDecimal/8151472\",\"555.000000\",\"0.000000\",\"555\",\"java.util.HashMap/1797211028\",\"java.lang.String/2004016611\",\"1x2\",\"0.00\",\"Others\",\"Group C\",\"9999458332.000000\",\"java.util.ArrayList/4159755760\",\"com.leo.agency.ui.model.Commission/445191376\",\"com.leo.agency.ui.model.Sport/1570216373\",\"com.leo.agency.ui.model.BetSetting/2645023239\",\"2222\",\"0\",\"1111\",\"100000\",\"1000\",\"com.leo.agency.ui.model.PositionTaking/1082007261\",\"Mix Parlay\",\"888888\",\"1000000\",\"HDP/Next Goal\",\"PT1\",\"Over/Under\",\"1st HDP\",\"1st OU\",\"Odd/Even\",\"PT2\",\"Correct Score\",\"Total Goal\",\"HTFT\",\"FGLG\",\"Outright\",\"Live HDP\",\"Live AH\",\"Live OU\",\"Live 1st HDP\",\"Live 1st OU\",\"Soccer\",\"com.leo.agency.ui.model.UserStatus/2535589419\",\"Active\",\"Suspended\",\"Closed\",\"Inactive\",\"com.leo.agency.ui.vo.BuOddsGroupVO/2751596019\",\"A\",\"B\",\"C\",\"D\",\"com.leo.agency.ui.model.User/2887661713\",\"com.extjs.gxt.ui.client.data.RpcMap/3441186752\",\"loginId\",\"creditLimitDouble\",\"10000000000\",\"levelId\",\"java.lang.Integer/3438268394\",\"state\",\"baseCurrency\",\"settlementModeId\",\"securityQuestion\",\"password\",\"{sha-1}WoFGDii4wgdGw+tTG7T8K3zepNWjGgc=\",\"totalSuspendedMember\",\"city\",\"isDetail\",\"lastLoginIp\",\"182.55.247.179\",\"postalCode\",\"totalActiveMember\",\"buId\",\"userId\",\"parentUserId\",\"gender\",\"birthDate\",\"firstName\",\"createdDate\",\"java.util.Date/3385151746\",\"securityAnswer\",\"lastLoginTime\",\"countryId\",\"totalClosedMember\",\"lastName\",\"totalInActiveMember\",\"userCode\",\"SAXN1111\",\"hasChangedPassword\",\"userRole\",\"lastActivityTime\",\"userStatusId\",\"languageId\",\"oddsGroupId\",\"phoneNo\",\"maxMemberCreditLimitDouble\",\"email\",\"address\",\"maxAgentCreditLimitDouble\",\"faxNo\",\"selfExclusion\",\"userCategoryId\",\"comments\",\"mobileNo\",\"CNY\",\"com.leo.agency.ui.vo.UserAccountCategoryVO/4129656925\",\"{sha-1}K51Cm7ExaSuSlhZOqhEe/860nc1GVlY=\",\"F1\",\"L1\"],0,7";
		response = "0,0,107,0,106,-86,105,0,104,0,103,20,4,102,0,101,0,100,20,4,99,0,98,3,62,97,-86,96,2,62,95,0,5,1,15,5,112,83,94,0,93,-86,92,3,9,90,-86,89,112,9,88,-86,87,-86,86,0,85,0,84,0,5,1,15,5,112,83,82,111,9,81,0,80,-86,79,439,62,78,465,62,77,-84,76,-86,75,0,74,0,72,-86,71,0,70,-84,69,110,9,67,0,66,-84,65,-83,64,0,63,-83,61,7,4,59,0,58,44,57,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,56,0,15,0,0,0,15,0,15,0,109,0,15,108,0,0,0,0,107,0,106,-86,105,0,104,0,103,20,4,102,0,101,0,100,60,4,99,0,98,-84,97,-86,96,-84,95,57,30,0,15,5,112,83,94,0,93,-84,92,91,9,90,-86,89,0,88,-86,87,-86,86,18,32,2,15,5,112,83,85,0,84,20,58,22,12,5,112,83,82,0,81,0,80,-86,79,438,62,78,439,62,77,-84,76,-86,75,0,74,73,9,72,-86,71,0,70,0,62,69,68,9,67,0,66,1,62,65,5,62,64,0,63,4,62,61,60,4,59,0,58,44,57,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,56,0,1,1,55,4,4,1,11,4,51,1,1,54,3,3,1,11,4,51,1,1,53,2,2,1,11,4,51,1,1,52,1,1,1,11,4,51,4,15,0,50,4,46,49,3,46,48,2,46,47,1,46,4,15,45,1,0,16,42,4,0,0,0,44,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,15,0,16,24,0,15,41,3,0,0,0,43,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,14,0,15,24,0,14,42,4,0,0,0,42,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,13,0,14,24,0,13,41,3,0,0,0,40,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,12,0,13,24,0,12,34,2,0,0,0,39,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,11,0,12,24,0,11,34,2,0,0,0,38,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,10,0,11,24,0,10,34,2,0,0,0,37,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,9,0,10,24,0,9,34,2,0,0,0,36,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,8,0,9,24,0,8,34,2,0,0,0,35,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,7,0,8,24,0,7,34,2,0,0,0,10,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,6,0,7,24,0,6,29,1,0,0,0,33,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,5,0,6,24,0,5,29,1,0,0,0,32,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,4,0,5,24,0,4,29,1,0,0,0,31,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,3,0,4,24,0,3,29,1,0,0,0,30,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,2,0,3,24,0,2,29,1,0,0,0,28,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,2,24,15,15,23,4,27,4,22,4,1179,18,0,21,4,26,4,19,4,1229,18,17,25,0,0,1,25,5,0,0,0,25,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,24,1,15,23,4,20,4,22,4,1178,18,0,21,4,20,4,19,4,1228,18,17,2,15,11,4,0,0,0,12,0,0,0,2,0,16,11,4,0,0,0,10,0,0,0,1,0,16,11,4,0,0,0,13,1,1,0,3,0,16,3,15,0,0,-19,0,15,14,4,6,4,11,4,13,9,11,4,12,9,11,4,10,9,3,8,0,1,0.0,6,4,0.0,6,4,0.0,0,465,0.0,6,4,0.0,0.0,6,4,0.0,6,4,0.0,0,7,4,555.0,6,4,0.0,0,5,555.0,5,4,3,761,2,1,[],0,7";
		String[] array1 = response.split(",");
		System.out.println(array1.length);
		
		String s2 = "[\"com.leo.agency.ui.vo.UserAcctVO/2787799701\",\"com.leo.agency.ui.model.Account/3764101148\",\"SAXN111100G\",\"java.math.BigDecimal/8151472\",\"555.000000\",\"0.000000\",\"555\",\"java.util.HashMap/1797211028\",\"java.lang.String/2004016611\",\"1x2\",\"0.00\",\"Others\",\"Group C\",\"9999458332.000000\",\"java.util.ArrayList/4159755760\",\"com.leo.agency.ui.model.Commission/445191376\",\"com.leo.agency.ui.model.Sport/1570216373\",\"com.leo.agency.ui.model.BetSetting/2645023239\",\"2222\",\"0\",\"1111\",\"100000\",\"1000\",\"com.leo.agency.ui.model.PositionTaking/1082007261\",\"Mix Parlay\",\"888888\",\"1000000\",\"HDP/Next Goal\",\"PT1\",\"Over/Under\",\"1st HDP\",\"1st OU\",\"Odd/Even\",\"PT2\",\"Correct Score\",\"Total Goal\",\"HTFT\",\"FGLG\",\"Outright\",\"Live HDP\",\"Live AH\",\"Live OU\",\"Live 1st HDP\",\"Live 1st OU\",\"Soccer\",\"com.leo.agency.ui.model.UserStatus/2535589419\",\"Active\",\"Suspended\",\"Closed\",\"Inactive\",\"com.leo.agency.ui.vo.BuOddsGroupVO/2751596019\",\"A\",\"B\",\"C\",\"D\",\"com.leo.agency.ui.model.User/2887661713\",\"com.extjs.gxt.ui.client.data.RpcMap/3441186752\",\"loginId\",\"creditLimitDouble\",\"10000000000\",\"levelId\",\"java.lang.Integer/3438268394\",\"state\",\"baseCurrency\",\"settlementModeId\",\"securityQuestion\",\"password\",\"{sha-1}WoFGDii4wgdGw+tTG7T8K3zepNWjGgc=\",\"totalSuspendedMember\",\"city\",\"isDetail\",\"lastLoginIp\",\"182.55.247.179\",\"postalCode\",\"totalActiveMember\",\"buId\",\"userId\",\"parentUserId\",\"gender\",\"birthDate\",\"firstName\",\"createdDate\",\"java.util.Date/3385151746\",\"securityAnswer\",\"lastLoginTime\",\"countryId\",\"totalClosedMember\",\"lastName\",\"totalInActiveMember\",\"userCode\",\"SAXN1111\",\"hasChangedPassword\",\"userRole\",\"lastActivityTime\",\"userStatusId\",\"languageId\",\"oddsGroupId\",\"phoneNo\",\"maxMemberCreditLimitDouble\",\"email\",\"address\",\"maxAgentCreditLimitDouble\",\"faxNo\",\"selfExclusion\",\"userCategoryId\",\"comments\",\"mobileNo\",\"CNY\",\"com.leo.agency.ui.vo.UserAccountCategoryVO/4129656925\",\"{sha-1}K51Cm7ExaSuSlhZOqhEe/860nc1GVlY=\",\"F1\",\"L1\"]";
		String[] array = s2.split(",");
		System.out.println(array.length + ", " + array[107] + ", " + array[109]);
		
		s2 = "1|2|3|4|1|5|5|6|761|7|8|9|555|5|0|0|8|10|555|8|11|0|0|8|10|0|8|10|0|0|8|10|0|465|0|0|8|10|0|8|10|0|1|0|0|0|0|0|0|0|12|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|13|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|14|48|15|0|16|8|17|18|19|4|20|0|21|19|5|22|19|1|23|0|24|25|26|27|19|0|28|0|29|-18|30|25|31|32|0|33|-18|34|-16|35|19|439|36|19|438|37|-18|38|0|39|0|40|41|112|5|12|22|58|20|42|0|43|41|112|5|15|0|7|9|44|-18|45|-18|46|0|47|-18|48|25|49|50|25|51|52|-16|53|0|54|41|112|5|15|0|30|57|55|-16|56|-18|57|-16|58|0|59|8|17|60|0|61|0|62|8|63|64|0|65|-25|66|0|67|-18|68|0|69|0|70|-24|71|-20|72|3|73|0|3|0|1|1|0|0|0|0|8|63|73|0|1|0|0|0|0|0|0|0|8|63|73|0|2|0|0|0|0|0|0|0|8|63|72|2|74|75|1228|8|76|8|63|8|77|0|0|72|1|78|1|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|79|0|0|0|5|79|1|80|0|0|0|0|0|0|0|0|0|1|0|465|0|0|74|75|1229|8|76|8|81|8|77|0|0|72|15|78|2|0|1|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|82|0|0|0|1|83|2|80|0|0|0|0|0|0|0|0|0|2|0|465|78|3|0|2|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|84|0|0|0|1|83|3|80|0|0|0|0|0|0|0|0|0|3|0|465|78|4|0|3|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|85|0|0|0|1|83|4|80|0|0|0|0|0|0|0|0|0|4|0|465|78|5|0|4|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|86|0|0|0|1|83|5|80|0|0|0|0|0|0|0|0|0|5|0|465|78|6|0|5|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0|0|0|87|0|0|0|1|83|6|80|0|0|0|0|0|0|0|0|0|6|0|465|78|7|0|6|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|88|0|0|0|2|89|7|80|0|0|0|0|0|0|0|0|0|7|0|465|78|8|0|7|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|90|0|0|0|2|89|8|80|0|0|0|0|0|0|0|0|0|8|0|465|78|9|0|8|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|91|0|0|0|2|89|9|80|0|0|0|0|0|0|0|0|0|9|0|465|78|10|0|9|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|92|0|0|0|2|89|10|80|0|0|0|0|0|0|0|0|0|10|0|465|78|11|0|10|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|93|0|0|0|2|89|11|80|0|0|0|0|0|0|0|0|0|11|0|465|78|12|0|11|0|0|0|0|0|0|0|2|0|0|0|0|0|0|0|0|0|0|0|0|0|94|0|0|0|2|89|12|80|0|0|0|0|0|0|0|0|0|12|0|465|78|13|0|12|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|95|0|0|0|3|96|13|80|0|0|0|0|0|0|0|0|0|13|0|465|78|14|0|13|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|97|0|0|0|4|97|14|80|0|0|0|0|0|0|0|0|0|14|0|465|78|15|0|14|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|98|0|0|0|3|96|15|80|0|0|0|0|0|0|0|0|0|15|0|465|78|16|0|15|0|0|0|0|0|0|0|3|0|0|0|0|0|0|0|0|0|0|0|0|0|99|0|0|0|4|97|16|80|0|0|0|0|0|0|0|0|0|16|0|465|1|0|0|0|0|0|0|0|0|0|0|72|0|0|12|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|14|15|48|25|7|34|-16|36|-18|18|-15|21|-18|55|19|2|39|25|100|46|25|101|58|0|69|0|64|0|16|8|11|62|8|63|59|8|63|35|19|465|72|2|102|0|1|0|103|72|1|-43|74|0|0|0|-95|0|79|0|12|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|14|44|15|0|16|8|11|18|-15|20|0|21|-15|22|-16|23|0|24|25|104|27|-18|28|0|29|-18|30|0|32|0|33|-18|34|-16|35|-91|36|19|439|37|-18|38|0|39|-86|40|41|112|5|15|1|5|0|42|0|43|0|44|-18|45|-18|46|-87|47|-16|48|-84|52|-18|53|0|54|41|112|5|15|1|5|0|55|-14|56|-18|57|19|3|58|0|59|8|63|60|0|61|0|62|8|63|64|0|66|0|67|-18|68|0|69|0|1|0|102|0|1|0|103|72|15|-51|-53|-55|-57|-59|-61|-63|-65|-67|-69|-71|-73|-75|-77|-79|74|0|0|0|-109|1|105|0|-97|1|0";
		array = s2.split("\\|");
		System.out.println(array.length + ", " + array[107] + ", " + array[109]);
	}
	
	@Test
	public void testMarkettypes() throws Exception {
		String url = "http://203.90.242.131/membersite/en-US/resource/markettypes";
		//String url = "http://203.90.242.131/membersite/en-US/resource/announcement";
		
		String cssCookie = "JSESSIONID=d7d95c555149dd1df5d0cd51ba62; __utma=42882086.1416815965.1339585152.1339987693.1339992122.3; __utmz=42882086.1339585152.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); NSC_NFNCFS_WT=ffffffff096c0adc45525d5f4f58455e445a4a425b80; __utmc=42882086; TOKEN=2685275472l";
		//cssCookie =        "JSESSIONID=d7d95c555149dd1df5d0cd51ba62; __utma=42882086.1416815965.1339585152.1339987693.1339992122.3; __utmz=42882086.1339585152.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); NSC_NFNCFS_WT=ffffffff096c0adc45525d5f4f58455e445a4a425b80; __utmc=42882086; TOKEN=3795353193l";
		System.out.println("cssCookie = " + cssCookie);
		Map<String, String> requestHeader = new HashMap<String, String>();
        requestHeader.put("Cookie", cssCookie);
        requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:13.0) Gecko/20100101 Firefox/13.0");
        requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        requestHeader.put("Accept-Language", "en-us,en;q=0.5");
        requestHeader.put("Connection", "keep-alive");
        requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        
        Map<String, Object> responseHeader = ConnectionHelper.connectUrl(url, "GET", "", requestHeader);
        System.out.println(ConnectionHelper.printHeader(responseHeader));
        
        
	}
	
	@Test
	public void testMGSPlaycheck() throws Exception {
		//Flow to scrap MGS
		/*
		  	1. Login
			2. Session List
			3. Session Details
				3.1 Game Detail 1
				3.2 Game Detail 2 (ajax)
				3.3 Back to session
			4. Next Session
		 */
		
		
		String url = "https://playcheck3.gameassists.co.uk/playcheck/default.aspx";
		
		//String cookie = "ASPSESSIONIDCSACQCAQ=BABNDKODPGMHJDEFFIOMPJGG; ASP.NET_SessionId=blxpcx45vtvxb5zprm2rdn55";
		String cookie = "ASPSESSIONIDCQDBQAAR=EHOLIHMDGJJGJKNOKAIHKPPL; ASP.NET_SessionId=ssjf0q55oc0aaeebptbwod45";
		
		Map<String, String> requestHeader = new HashMap<String, String>();
        requestHeader.put("Cookie", cookie);
        requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:14.0) Gecko/20100101 Firefox/14.0.1");
        requestHeader.put("Referer", "https://playcheck3.gameassists.co.uk/playcheck/default.aspx");
        requestHeader.put("Accept-Encoding", "gzip, deflate");
        
        //Map<String, Object> responseHeader = null;
        
        //Map<String, Object> responseHeader = ConnectionHelper.connectUrl(url, "POST", "ucListSessions$ddl_From=2&ucListSessions$ddl_To=0", requestHeader);
        //System.out.println(ConnectionHelper.printHeader(responseHeader));
        
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListSessions%24gridSessions%24ctl07%24linkButtonViewGames&__EVENTVALIDATION=%2FwEWKQKnzrd9AuC2870FAuG2870FAuK2870FAuO2870FAuS2870FAuW2870FAua2870FAve2870FAvi2870FAuC2s74FAuC2v74FAuC2u74FAuC2h74FAuC2g74FAuC2j74FAuC2i74FAuC2l74FAuC2070FAuC2370FAuG2s74FAuG2v74FAuG2u74FAuG2h74FAuG2g74FAuG2j74FAuG2i74FAuG2l74FAuG2070FAuG2370FAuK2s74FApGJu%2BILAqGNt44KAtj0roIMAqvsztMMAsO2p5oFAvHt1vILAvyfs84NAv%2Bmg5gMAvrkt44LAsTb4tAGGzPZLHV1z%2Bm3LbgABF%2FQ3pzSTSw%3D&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgBBRt1Y0xpc3RTZXNzaW9ucyRncmlkU2Vzc2lvbnMPPCsACgEIAgFkT5mpTKVOxTJTyG0Ai7IbLpPEv6M%3D&hidden_appmode=&ucListSessions%24ddl_From=2&ucListSessions%24ddl_To=0";
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListSessions%24gridSessions%24ctl08%24linkButtonViewGames&__EVENTVALIDATION=%2FwEWKQKnzrd9AuC2870FAuG2870FAuK2870FAuO2870FAuS2870FAuW2870FAua2870FAve2870FAvi2870FAuC2s74FAuC2v74FAuC2u74FAuC2h74FAuC2g74FAuC2j74FAuC2i74FAuC2l74FAuC2070FAuC2370FAuG2s74FAuG2v74FAuG2u74FAuG2h74FAuG2g74FAuG2j74FAuG2i74FAuG2l74FAuG2070FAuG2370FAuK2s74FApGJu%2BILAqGNt44KAtj0roIMAqvsztMMAsO2p5oFAvHt1vILAvyfs84NAv%2Bmg5gMAvrkt44LAsTb4tAGGzPZLHV1z%2Bm3LbgABF%2FQ3pzSTSw%3D&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgBBRt1Y0xpc3RTZXNzaW9ucyRncmlkU2Vzc2lvbnMPPCsACgEIAgFkT5mpTKVOxTJTyG0Ai7IbLpPEv6M%3D&hidden_appmode=&ucListSessions%24ddl_From=2&ucListSessions%24ddl_To=0";
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListSessions%24gridSessions%24ctl07%24linkButtonViewGames&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgBBRt1Y0xpc3RTZXNzaW9ucyRncmlkU2Vzc2lvbnMPPCsACgEIAgFkT5mpTKVOxTJTyG0Ai7IbLpPEv6M%3D&hidden_appmode=&ucListSessions%24ddl_From=2&ucListSessions%24ddl_To=0";
        //String params = "__EVENTTARGET=ucListSessions$gridSessions$ctl07$linkButtonViewGames&__VIEWSTATE=/wEPDwUJNzQzMDMxMzE5ZBgBBRt1Y0xpc3RTZXNzaW9ucyRncmlkU2Vzc2lvbnMPPCsACgEIAgFkT5mpTKVOxTJTyG0Ai7IbLpPEv6M=&ucListSessions$ddl_From=2&ucListSessions$ddl_To=0";
        //String params = "__EVENTTARGET=ucListSessions$gridSessions$ctl08$linkButtonViewGames&ucListSessions$ddl_From=2&ucListSessions$ddl_To=0";

        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListGames$Grid_ListGames$ctl04$ctl01&__EVENTVALIDATION=%2FwEWEgLOnZWtDwL3h7PmAwLy%2BKmrBALgksCECAKHqbeYAgLDto6bDQLLrKXvDQLvh6hQAprQjtsDAtzgsrsPAt%2FiypUBAqr9%2BosEAtDI2sMEAoG6sekMAruepKANAuXWt6oFAt%2F2s9ICAsTb4tAGL%2FUeZ3ZeiSdc%2B06B6wDIYa5NsZ8%3D&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUhdWNMaXN0R2FtZXMkcmFkaW9idXR0b25fc2hvd0dhbWVzBSd1Y0xpc3RHYW1lcyRyYWRpb2J1dHRvbl9zaG93VHJhbnNhY3Rpb24FJ3VjTGlzdEdhbWVzJHJhZGlvYnV0dG9uX3Nob3dUcmFuc2FjdGlvbgUhdWNMaXN0R2FtZXMkR3JpZF9MaXN0VHJhbnNhY3Rpb25zD2dkBRp1Y0xpc3RHYW1lcyRHcmlkX0xpc3RHYW1lcw88KwAKAQgCAWQ7mpCWrqffyro78LpzVcLLkVF5VA%3D%3D&hidden_appmode=&ucListGames%24gameOrTransaction=radiobutton_showGames&ucListGames%24inputHidden_filterVisibility=Minimise&ucListGames%24inputHidden_visibleGames=%2C";
        
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListSessions%24gridSessions%24ctl07%24linkButtonViewGames&__EVENTVALIDATION=%2FwEWKAKnzrd9AuC2870FAuG2870FAuK2870FAuO2870FAuS2870FAuW2870FAua2870FAve2870FAvi2870FAuC2s74FAuC2v74FAuC2u74FAuC2h74FAuC2g74FAuC2j74FAuC2i74FAuC2l74FAuC2070FAuC2370FAuG2s74FAuG2v74FAuG2u74FAuG2h74FAuG2g74FAuG2j74FAuG2i74FAuG2l74FAuG2070FAuG2370FAuK2s74FApGJu%2BILAqGNt44KAtj0roIMAqvsztMMAsO2p5oFAvHt1vILAvyfs84NAv%2Bmg5gMAsTb4tAGNiQL%2FrKSjLnWL%2FweMg05n8Uj3Uw%3D&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgBBRt1Y0xpc3RTZXNzaW9ucyRncmlkU2Vzc2lvbnMPPCsACgEIAgFkT5mpTKVOxTJTyG0Ai7IbLpPEv6M%3D&hidden_appmode=&ucListSessions%24ddl_From=1&ucListSessions%24ddl_To=0";
        
        //Show session list
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListGames%24a_gotoSessionList&__EVENTVALIDATION=%2FwEWEALOnZWtDwL3h7PmAwLy%2BKmrBALgksCECAKHqbeYAgLDto6bDQLLrKXvDQLvh6hQAprQjtsDAtzgsrsPAt%2FiypUBAuSyxsMGAruepKANAuXWt6oFAt%2F2s9ICAsTb4tAG6YxpVvvZgcrSjPdLEnm%2F7m7fuv8%3D&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUhdWNMaXN0R2FtZXMkcmFkaW9idXR0b25fc2hvd0dhbWVzBSd1Y0xpc3RHYW1lcyRyYWRpb2J1dHRvbl9zaG93VHJhbnNhY3Rpb24FJ3VjTGlzdEdhbWVzJHJhZGlvYnV0dG9uX3Nob3dUcmFuc2FjdGlvbgUhdWNMaXN0R2FtZXMkR3JpZF9MaXN0VHJhbnNhY3Rpb25zD2dkBRp1Y0xpc3RHYW1lcyRHcmlkX0xpc3RHYW1lcw88KwAKAQgCAWQ7mpCWrqffyro78LpzVcLLkVF5VA%3D%3D&hidden_appmode=&ucListGames%24gameOrTransaction=radiobutton_showGames&ucListGames%24inputHidden_filterVisibility=Minimise&ucListGames%24inputHidden_visibleGames=%2C";
        
        //View sesion details
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListSessions%24gridSessions%24ctl02%24linkButtonViewGames&__EVENTVALIDATION=%2FwEWJwKnzrd9AuC2870FAuG2870FAuK2870FAuO2870FAuS2870FAuW2870FAua2870FAve2870FAvi2870FAuC2s74FAuC2v74FAuC2u74FAuC2h74FAuC2g74FAuC2j74FAuC2i74FAuC2l74FAuC2070FAuC2370FAuG2s74FAuG2v74FAuG2u74FAuG2h74FAuG2g74FAuG2j74FAuG2i74FAuG2l74FAuG2070FAuG2370FAuK2s74FApGJu%2BILAqGNt44KAtj0roIMAqvsztMMAsO2p5oFAvHt1vILAvyfs84NAsTb4tAGI%2BFl1h8XykOjmKoXgfMJA5l6wpg%3D&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgBBRt1Y0xpc3RTZXNzaW9ucyRncmlkU2Vzc2lvbnMPPCsACgEIAgFkT5mpTKVOxTJTyG0Ai7IbLpPEv6M%3D&hidden_appmode=&ucListSessions%24ddl_From=1&ucListSessions%24ddl_To=0";
        String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListSessions$gridSessions$ctl06$linkButtonViewGames&__EVENTVALIDATION=%2FwEWJwKnzrd9AuC2870FAuG2870FAuK2870FAuO2870FAuS2870FAuW2870FAua2870FAve2870FAvi2870FAuC2s74FAuC2v74FAuC2u74FAuC2h74FAuC2g74FAuC2j74FAuC2i74FAuC2l74FAuC2070FAuC2370FAuG2s74FAuG2v74FAuG2u74FAuG2h74FAuG2g74FAuG2j74FAuG2i74FAuG2l74FAuG2070FAuG2370FAuK2s74FApGJu%2BILAqGNt44KAtj0roIMAqvsztMMAsO2p5oFAvHt1vILAvyfs84NAsTb4tAGI%2BFl1h8XykOjmKoXgfMJA5l6wpg%3D&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgBBRt1Y0xpc3RTZXNzaW9ucyRncmlkU2Vzc2lvbnMPPCsACgEIAgFkT5mpTKVOxTJTyG0Ai7IbLpPEv6M%3D&hidden_appmode=&ucListSessions%24ddl_From=1&ucListSessions%24ddl_To=0";
        
        //Next session        
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListGames%24Grid_ListGames%24ctl03%24ctl02&__EVENTVALIDATION=%2FwEWCgLOnZWtDwL3h7PmAwLy%2BKmrBALgksCECALksp6aAgLkspKaAgK7nqSgDQLl1reqBQLf9rPSAgLE2%2BLQBnfkry5zI907u%2FKI8jjLOxysjq7x&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUhdWNMaXN0R2FtZXMkcmFkaW9idXR0b25fc2hvd0dhbWVzBSd1Y0xpc3RHYW1lcyRyYWRpb2J1dHRvbl9zaG93VHJhbnNhY3Rpb24FJ3VjTGlzdEdhbWVzJHJhZGlvYnV0dG9uX3Nob3dUcmFuc2FjdGlvbgUhdWNMaXN0R2FtZXMkR3JpZF9MaXN0VHJhbnNhY3Rpb25zD2dkBRp1Y0xpc3RHYW1lcyRHcmlkX0xpc3RHYW1lcw88KwAKAQgCAWQ7mpCWrqffyro78LpzVcLLkVF5VA%3D%3D&hidden_appmode=&ucListGames%24gameOrTransaction=radiobutton_showGames&ucListGames%24inputHidden_filterVisibility=Minimise&ucListGames%24inputHidden_visibleGames=%2C";
        
        //Previous session
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListGames%24Grid_ListGames%24ctl14%24ctl01&__EVENTVALIDATION=%2FwEWFQLOnZWtDwL3h7PmAwLy%2BKmrBALgksCECAKHqbeYAgLDto6bDQLLrKXvDQLvh6hQAprQjtsDAtzgsrsPAt%2FiypUBAqr9%2BosEAtDI2sMEAu%2BgnoQNArjegPMOAsbgxpwGAoG6zZoDAruepKANAuXWt6oFAt%2F2s9ICAsTb4tAGLWlYtVNBwbI%2BERDm%2FAesogzVXDk%3D&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUhdWNMaXN0R2FtZXMkcmFkaW9idXR0b25fc2hvd0dhbWVzBSd1Y0xpc3RHYW1lcyRyYWRpb2J1dHRvbl9zaG93VHJhbnNhY3Rpb24FJ3VjTGlzdEdhbWVzJHJhZGlvYnV0dG9uX3Nob3dUcmFuc2FjdGlvbgUhdWNMaXN0R2FtZXMkR3JpZF9MaXN0VHJhbnNhY3Rpb25zD2dkBRp1Y0xpc3RHYW1lcyRHcmlkX0xpc3RHYW1lcw88KwAKAQgCAWQ7mpCWrqffyro78LpzVcLLkVF5VA%3D%3D&hidden_appmode=&ucListGames%24gameOrTransaction=radiobutton_showGames&ucListGames%24inputHidden_filterVisibility=Minimise&ucListGames%24inputHidden_visibleGames=%2C";
        
        //Back to session
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucGameDetail%24a_gotoSessionList&__EVENTVALIDATION=%2FwEWBALJ6bDaCwL867ydAwLm4IPNDALE2%2BLQBkI5h1vp7lLGCrpRq9yNBjO7v%2F%2F9&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZGTiy3JvVsfsqmfDs1BHKJK8qJQqdQ%3D%3D&hidden_appmode=&ucGameDetail%24inputHidden_leavingGameDetail=1";
        
        //View details
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListGames%24Grid_ListGames%24ctl08%24linkButtonViewGameDetail&__EVENTVALIDATION=%2FwEWEALOnZWtDwL3h7PmAwLy%2BKmrBALgksCECAKHqbeYAgLDto6bDQLLrKXvDQLvh6hQAprQjtsDAtzgsrsPAt%2FiypUBAuSyxsMGAruepKANAuXWt6oFAt%2F2s9ICAsTb4tAG6YxpVvvZgcrSjPdLEnm%2F7m7fuv8%3D&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUhdWNMaXN0R2FtZXMkcmFkaW9idXR0b25fc2hvd0dhbWVzBSd1Y0xpc3RHYW1lcyRyYWRpb2J1dHRvbl9zaG93VHJhbnNhY3Rpb24FJ3VjTGlzdEdhbWVzJHJhZGlvYnV0dG9uX3Nob3dUcmFuc2FjdGlvbgUhdWNMaXN0R2FtZXMkR3JpZF9MaXN0VHJhbnNhY3Rpb25zD2dkBRp1Y0xpc3RHYW1lcyRHcmlkX0xpc3RHYW1lcw88KwAKAQgCAWQ7mpCWrqffyro78LpzVcLLkVF5VA%3D%3D&hidden_appmode=&ucListGames%24gameOrTransaction=radiobutton_showGames&ucListGames%24inputHidden_filterVisibility=Minimise&ucListGames%24inputHidden_visibleGames=%2C";
        
        //String params = "__EVENTARGUMENT=&__EVENTTARGET=ucListGames%24Grid_ListGames%24ctl04%24ctl02&__EVENTVALIDATION=%2FwEWDALOnZWtDwL3h7PmAwLy%2BKmrBALgksCECAKHqbeYAgLDto6bDQLkssLeBgLksrbeBgK7nqSgDQLl1reqBQLf9rPSAgLE2%2BLQBs99ppK8LB4IOP%2BayKkXo9z124gJ&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUJNzQzMDMxMzE5ZBgDBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwUhdWNMaXN0R2FtZXMkcmFkaW9idXR0b25fc2hvd0dhbWVzBSd1Y0xpc3RHYW1lcyRyYWRpb2J1dHRvbl9zaG93VHJhbnNhY3Rpb24FJ3VjTGlzdEdhbWVzJHJhZGlvYnV0dG9uX3Nob3dUcmFuc2FjdGlvbgUhdWNMaXN0R2FtZXMkR3JpZF9MaXN0VHJhbnNhY3Rpb25zD2dkBRp1Y0xpc3RHYW1lcyRHcmlkX0xpc3RHYW1lcw88KwAKAQgCAWQ7mpCWrqffyro78LpzVcLLkVF5VA%3D%3D&hidden_appmode=&ucListGames%24gameOrTransaction=radiobutton_showGames&ucListGames%24inputHidden_filterVisibility=Minimise&ucListGames%24inputHidden_visibleGames=%2C";
        
        ConnectionResponse connectionResponse = ConnectionHelper.connectToUrl(url, "POST", params, requestHeader);
        //ConnectionResponse connectionResponse = ConnectionHelper.connectToUrl("https://playcheck3.gameassists.co.uk/playcheck/PageRequest.ashx?Control=GasianTickets", "GET", params, requestHeader);
        System.out.println(ConnectionHelper.printHeader(connectionResponse.getHeaders()));
        System.out.println(connectionResponse.getBodyAsString());
        
        
        
//        responseHeader = ConnectionHelper.connectUrl("https://playcheck3.gameassists.co.uk/playcheck/PageRequest.ashx", "GET", "Control=GasianTickets", requestHeader);
//        System.out.println(ConnectionHelper.printHeader(responseHeader));
	}
	
	@Test
	public void testPgaEncode() throws Exception {		
		String url = "http://localhost:28080/gateway/88bet/sdpay/cdc/deposit/receive.jview";
		String params = "HiddenField1=Hp8vMdPYbBKW%2BjNxrVr3TqzMlWkGnePK5Uec1jtFoxLIxXJ9zR/GNlKOlSGvjqR5Cc14WeaVLy85KIKJ6ucdelzbMqr7jeBKX5sjHwiflQdMNHG/44oQX8gzRO5r5ToKHEU5jIMGNe5MUqv2LO3lreIhb8qIxOEmkFePbjIFaBh0fKiZGAtGPvl4EI8bmeMqfmzm3ADcnO9wHYb8JsLWUT%2BARjIzQOM3DUQq66i8nKXLrbVQwcquq7Xmc89%2BcdcxuiFewo87DsXHfN7KDi0ECo25EFUuFRGCspJ0OjZ0hXzh6Oi2UtFDRoXckw3ZA9gB/AmYqcVybVN8RtintohhQ%2BIrop6FaXuRZUaeHr4znvw1NuKrQuZ3YdQ1IiTE60dY/WW8CxQXgvmw8Yjw93Gn4aQHN6rsJW/QsZL0EJIakGZmuMvQXdVK1wMRoXJ5ieLbnCXWQ0UxX3s%2BWyJk%2BWXJbvFKtrWk8dxv7tkwL1Li5FuWkrQQDGmXQ3gD1VyzZcTOA3YwLRfz3kA90Gg0dIyzfFTpvHipkPhNlNxphlxToSQVngIZQ86I9rhbwJ2FR75SuIQ4/oeQ9VSgNGsNGBKBCpIJq5pLQYUhSJEKdYhCaNwUqv8bKG5%2BbO/DM7s5bgAek8ctWYMvFmgSctb%2BQrxFr6tsqwDVcpPU9SA9OdZ8ONo6DQIUlzdKCVkoVT0y0shlNA0KL7mJB4noYwQwxb/%2BRY24EAAaAMpiZH67aBoW653csjUT0UbOx/QqMmplHzmr0qhfdThQSTVhYvPYlekS/WRZr2Qi/muskOyHjBJCGnY1RrGSIIkfg/xUuVW8ja4zClg10NSkqgn2OpEELAME3R7eMhJbZQHMD7Jk%2ByUfkyzvdWyLWqr0jjB7RasKaH8r9qwF/AiuQpvIqhriHnjMFsp5p5vj67YKcgMm%2BXcT7/ykdBFtxCGHAeqCemQfVFDER%2B1S9QKD9hskfe5DVv7u%2BDLxDKS8ZZ6lUhp8iryl16mIIZ5YAZb2i%2Bl/S05Zh7VdwK75WaTCdyADNZ7ZEDZ/RuIviC6DLskZGYlqEaNPNuDFcPWSEQCkk4eSLI21fnJChA9GWZKxCnplotyfL8bcizPorj3FTVTxzHcP6XqAC2r3Xz7jgDmEXvol7GO%2Bpk1e";
		
		Map<String, String> requestHeader = new HashMap<String, String>();
		//requestHeader.put("Content-Type", "text/xml; charset=ISO-8859-1;");
		
//		ConnectionResponse response = ConnectionHelper.connectToUrl(url, "POST", params, requestHeader);
//		_logger.debug("response = " + response.getBodyAsString());
		
		System.out.println(URLEncoder.encode("ucListSessions$gridSessions$ctl06$linkButtonViewGames", "UTF-8"));
		
		for(int i=0; i <= 36; i++) {
			System.out.println("chip" + i + "=" + i);			
		}
	}
	
	@Test
	public void testStatement() throws Exception {		
		
		Map<String, String> requestHeader = new HashMap<String, String>();
		requestHeader.put("Cookie", "__utma=15316005.340543454.1349417874.1349417874.1349417874.1; __utmb=15316005.8.10.1349417874; __utmz=15316005.1349417874.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); Hm_lvt_35145154f6132c81ca8359c0cfc9a325=1349417874875,1349417938774; __utmn=f93adf939f6cf4cddd3f45bb5d45; acode=nnqewcX; bcode=1; __utmc=15316005; Hm_lpvt_35145154f6132c81ca8359c0cfc9a325=1349418020354; JSESSIONID=f93c3f5c20a6db4a6826695e0ee3; NSC_NFNCFS_WT=ffffffff096c0adc45525d5f4f58455e445a4a425b80; TOKEN=4121214825l; lang=simplified, language=CHINESE_SIMPLIFIED, filterDate=Fri Oct 05 00:00:00 UTC 2012, sessionTimeoutSec=1800");
		String params = "lang=ENGLISH&transactionType=ALL&startDate=10%2F01%2F2012&endDate=10%2F07%2F2012+23%3A59%3A59&timeZone=&_=1349419477380";
		
		ConnectionResponse response = ConnectionHelper.connectToUrl("http://www.betmart.com/member/statement/searchAll", "POST", params, requestHeader);
		_logger.debug("response = " + response.getBodyAsString());

	}
	
	@Test
	public void testAscsetTime() throws Exception {
		
		Map<String, String> requestHeader = new HashMap<String, String>();
//		requestHeader.put("Host", "113.212.177.39");                
//		requestHeader.put("Referer", "http://113.212.177.39/user_smartbets8/login_header.php?invalid=&language=english&red=");
//        requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
//        requestHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        requestHeader.put("Accept-Language", "en-us,en;q=0.5");
//        requestHeader.put("Accept-Encoding", "gzip, deflate");
//        requestHeader.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
//        requestHeader.put("Connection", "keep-alive");
        //requestHeader.put("Cookie", "ASP.NET_SessionId=aalzqyq4c3bznu55h51m3d45");
//		requestHeader.put("Cookie", "ASP.NET_SessionId=0z1js3n0wdk11g2cjpsaheyr; JSESSIONID=4ca19b2f934b88096fcf0563085c; Hm_lvt_35145154f6132c81ca8359c0cfc9a325=1367319764,1367324708,1367393713,1367489443; Hm_lpvt_35145154f6132c81ca8359c0cfc9a325=1367490577; __utma=1.1425707771.1367490578.1367490578.1367490578.1; __utmb=1.1.10.1367490578; __utmc=1; __utmz=1.1367490578.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); CSSID=4ca578118c9450054b40eadcfc02; NSC_NFNCFS_WT=ffffffff096c0adf45525d5f4f58455e445a4a425b80");
//		requestHeader.put("Host", "58.145.202.70");
//		requestHeader.put("Referer", "http://58.145.202.70/sb2odds/Login_Header.aspx");
//		requestHeader.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:14.0) Gecko/20100101 Firefox/14.0.1");
//		requestHeader.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		requestHeader.put("Content-Length", "11");
//		requestHeader.put("Accept", "*/*");
//		requestHeader.put("Accept-Encoding", "gzip, deflate");
//		requestHeader.put("Accept-Language", "en-us,en;q=0.5");
//        
//		ConnectionResponse result = ConnectionHelper.connectToUrl("http://www.ascbet.com/Handler/Handler.ashx", "POST", "setTime=yes", requestHeader);		
//		
//		System.out.println(result.toString());
		
		
		
		requestHeader.put("Cookie", "ASP.NET_SessionId=2cjhry3elgqom2rsbevp1umi;");
        
		ConnectionResponse result = ConnectionHelper.connectToUrl("http://www.ascbet.com/Login_Index.aspx", "POST", "setTime=yes", requestHeader);
		System.out.println(result.toString());
//		for(Entry<String, Object> entry: result.entrySet()) {
//			_logger.debug(entry.getKey() + " = " + entry.getValue());
//		}
	}
}
