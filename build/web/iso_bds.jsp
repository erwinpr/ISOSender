<%-- 
    Document   : index
    Created on : Jun 15, 2019, 9:33:19 PM
    Author     : Erwin Prasetyo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'>
        <title>ISO Sender</title>
        <style>
            body { 
                font-family: 'Roboto';
                font-size: 1em;
                padding: 1em;
                margin: 1em;
            }
            
            input, select {
                padding: 0.25em;
                margin: 0.25em;
            }
            
            .labela{
                text-align:right;
                width:136px;
                display:inline-block;
            }
        </style>
    </head>
    <body>
        <h1>ISO Message Testing</h1>
        
        <form id="frmTestServlet" name="frmTestServlet" method="POST" action="SenderServlet">
            <input id="testValue" name="testValue" type="hidden">
            <label class="labela" for="hostValue">Remote Host</label> <input id="hostValue" name="hostValue" type="text" value="10.110.1.10"><br/>
            <label class="labela" for="portValue">Remote Port</label> <input id="portValue" name="portValue" type="text" value="23242"><br/>
            <label class="labela" for="reqType">Request Type</label> <select id="reqType" name="reqType">
                <option value="">--</option>
                <option value="0200">0200 - Request</option>
                <option value="0400">0400 - Reversal</option>
            </select>
            <label class="labela" for="bit3Value">Caller Id</label> <input id="callerId" name="callerId" type="text" value="BDS_TELLER"><br/>
            <label class="labela" for="bit3Value">Bit 3</label> <input id="bit3Value" name="bit3Value" type="text" value="185003"><br/>
            <label class="labela" for="bit11Value">Bit 11</label> <input id="bit11Value" name="bit11Value" type="text" value="841861"><br/>
            <label class="labela" for="bit18Value">Bit 18</label> <input id="bit18Value" name="bit18Value" type="text" value="4444" maxlength="4"><br/>
            <label class="labela" for="bit35Value">Bit 35</label> <input id="bit35Value" name="bit35Value" type="text" value="cMQJYkM27eGrjER1AAA"><br/>
            <label class="labela" for="bit37Value">Bit 37</label> <input id="bit37Value" name="bit37Value" type="text" value="251733841861"><br/>
            <label class="labela" for="oprType">Operation Type</label> <select id="oprType" name="oprType">
                <option value="">--</option>
                <option value="inq_bds">Free Format Bit 48</option>
                <option value="inq_bds_a">Free Format Bit 48 & 60</option>
                <option value="ovb_bds">Free Format Bit 48 w/ Bit(s) 102,103</option>
                <option value="ovb_bds_a">Free Format Bit 48 & 62 w/ Bit(s) 90,102,103</option>
            </select><br/>
            <div id="divref1">
                <label id="lblBit48" class="labela" for="bit48Value">Bit 48</label> <input id="bit48Value" name="bit48Value" type="text" size="110"><br/>
            </div>
            <div id="divref5">
                <label id="lblBit60" class="labela" for="bit60Value">Bit 60</label> <input id="bit60Value" name="bit60Value" type="text" size="110"><br/>
            </div>
            <div id="divref4">
                <label id="lblBit62" class="labela" for="bit62Value">Bit 62</label> <input id="bit90Value" name="bit62Value" type="text" size="110" /><br/>
                <label id="lblBit90" class="labela" for="bit90Value">Bit 90</label> <input id="bit90Value" name="bit90Value" type="text" size="42" maxlength="42"/><br/>
            </div>
            <div id="divref2">
                <label id="lblBit102" class="labela" for="bit102Value">Bit 102</label> <input id="bit102Value" name="bit102Value" type="text" size="110"><br/>
            </div>
            <div id="divref3">
                <label id="lblBit103" class="labela" for="bit102Value">Bit 103</label> <input id="bit103Value" name="bit103Value" type="text" size="110"><br/>
            </div>
            <input id="btnSubmit" name="btnSubmit" type="Submit" value="Post Data To Servlet">
        </form>
    </body>
</html>

<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
        init_disp = function() {
            $("#divref1").hide();
            $("#divref2").hide();
            $("#divref3").hide();
            $("#divref4").hide();
            
            check_opr();
        };
    
        check_opr = function() {
            console.log($('#oprType').val());
            var operationType = $('#oprType').val();
            if(operationType == "") {
                $("#divref1").hide();
                $("#divref2").hide();
                $("#divref3").hide();
                $("#divref4").hide();
            }

            if(operationType === "inq_bds") {
                $("#divref1").show();
                $("#divref2").hide();
                $("#divref3").hide();
                $("#divref4").hide();
            }
            
            if(operationType === "inq_bds_a") {
                $("#divref1").show();
                $("#divref5").show();
                $("#divref2").hide();
                $("#divref3").hide();
                $("#divref4").hide();
            }

            if(operationType === "ovb_bds") {
                $("#divref1").show();
                $("#divref2").show();
                $("#divref3").show();
                $("#divref4").hide();
            }
            
            if(operationType === "ovb_bds_a") {
                $("#divref1").show();
                $("#divref2").show();
                $("#divref3").show();
                $("#divref4").show();
            }
        };
        
        init_opr = function() {
            $('#oprType').on('change',function() {
                check_opr();
            });
        };

        init_opr();
        init_disp();
</script>