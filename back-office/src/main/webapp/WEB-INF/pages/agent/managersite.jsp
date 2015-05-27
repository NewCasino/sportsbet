<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>    
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />  
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
        <title>Manager Site</title>
        <script type='text/javascript' src='http://code.jquery.com/jquery-1.7.2.min.js'></script>
        <link rel="stylesheet" type="text/css" href="resources/css/gxt-all.css" />
        <link rel="stylesheet" type="text/css" href="css/ManagerSite.css" />
        <style type="text/css">
            .auto-login .login-panel-background, .auto-login .login-panel-background-IE {display: none!important;}
        </style>
    </head>

    <body>
        <div id="loading" style="display: none">
            Loading... Please wait.<BR/>
            <img src="resources/images/gxt/icons/loading.gif" />
        </div>

        <iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"></iframe>
        <noscript>
            <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
                Your web browser must have JavaScript enabled
                in order for this application to display correctly.
            </div>
        </noscript>    
        <script type="text/javascript" src="manager/com.leo.agency.managersite.ManagerSite/com.leo.agency.managersite.ManagerSite.nocache.js"></script>
        <script type="text/javascript">
            $(function(){
                if(document.location.hash && document.location.hash != "" && document.location.hash !="#"){
                    $("body").addClass("auto-login");
                    var checkLoadedInterval = setInterval(function(){
                        var btnLogin = $("#buttonLogin button");
                        if(btnLogin.length > 0 && checkLoadedInterval){
                            clearInterval(checkLoadedInterval);
                            checkLoadedInterval = false;
                            $("form[action*='FormHandler']").submit(function(e){
                                e.preventDefault();
                                return false; 
                            }).attr("action","#");
                            $("input[name='username']").val("<%= request.getAttribute("agentCode")%>"+ document.location.hash.replace("#", ""));
                            $("input[name='password']").val("<%= request.getAttribute("password")%>");
                            setTimeout(function(){
                                try{
                                    $("#buttonLogin button").click() ;
                                }catch(e){
                                    //IE Sucks
                                    $("#buttonLogin button")[0].click() ;
                                };
                            }, 500);
                            document.location.hash = "";                            
                        }
                    }, 100);
                    
                    
                }
            });
        </script>
    </body>
</html>