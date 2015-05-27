
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.modelsb.entity.Currency;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import flexjson.JSONDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/currency")
@Authentication(type={UserType.BO})
@Authorization(hasAnyPermission={Permissions.ListCurrencyRate, Permissions.EditCurrencyRate})
public class CurrencyService {

    @Autowired
    com.pr7.sb.service.CurrencyService currencyService;
    
    @RequestMapping("/listAll.sv")
    @ResponseBody
    public Object listAllHandler() {
        return currencyService.getCurrencyInfoList();
    } 
    
    @RequestMapping("/get.sv")
    @ResponseBody
    public Object getHandler(String currencyCode) {
        return currencyService.getCurrencyByCode(currencyCode);
    } 
    
    @RequestMapping("/update.sv")
    @ResponseBody
    @Authorization(hasAllPermission={ Permissions.EditCurrencyRate})
    public Object updateHandler(String currency) {
        Currency currencyEntity = new JSONDeserializer<Currency>().deserialize(currency.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.Currency\" }"));
        return currencyService.update(currencyEntity);
    } 
    
    
}
