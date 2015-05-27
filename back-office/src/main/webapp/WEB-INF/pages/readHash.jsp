<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--        <script type="text/javascript" src="js/jquery-1.6.2.js" />-->
        <script type="text/javascript">
            var action = '<%= request.getParameter("action") %>';
            function readHash(fArray, hashArray) {
                //alert(parent.document.documentElement.innerHTML);
                parent.readHash(action, fArray, hashArray);
            }            
        </script>
        <script type='text/javascript' src='sv/system/com.leo.agency.managersite.ManagerSite.nocache.js'></script>
    </head>
    <body>
        <div id="msg" style="display: none"><label style="color: red;font-size:20px;">Still loading. Please wait ... </label></div>
        <div></div>
        <div id="debug"></div>
        <form id="form1" action="updateHash" method="POST">
            <input id="hashService" name="hashService" type="hidden" />
            <input id="hashValue" name="hashValue" type="hidden" />
        </form>
    </body>
</html>
