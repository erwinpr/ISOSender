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
        
        <form id="frmTestServlet" name="frmTestServlet" method="POST" action="PagingServlet">
            <input id="testValue" name="testValue" type="hidden">
            <label class="labela" for="hostValue">Remote Host</label> <input id="hostValue" name="hostValue" type="text" value="10.110.1.100"><br/>
            <label class="labela" for="portValue">Remote Port</label> <input id="portValue" name="portValue" type="text" value="23237"><br/>
            <label class="labela" for="bit3Value">Caller Id</label> <input id="callerId" name="callerId" type="text" value="RDNFRNTEND"><br/>
            <label class="labela" for="bit3Value">Bit 3</label> <input id="bit3Value" name="bit3Value" type="text" value="285031"><br/>
            <label class="labela" for="bit11Value">Bit 11</label> <input id="bit11Value" name="bit11Value" type="text" value="845858"><br/>
            <label class="labela" for="bit37Value">Bit 37</label> <input id="bit37Value" name="bit37Value" type="text" value="251733845858"><br/>
            <label class="labela" for="oprType">Operation Type</label> <select id="oprType" name="oprType">
                <option value="">--</option>
                <option value="bit_48">Use Free Format Bit 48</option>
                <!--<option value="bit_48_neg">Use Free Format Bit 48(Negative Test)</option>-->
            </select><br/>
            <!--<label class="labela" for="repeatVal">Repeat Request</label> <input id="repeatVal" name="repeatVal" type="text" value="1"><br/>-->
            <div id="divref1">
                <label id="lblRef" class="labela" for="refValue">Reference Value 1</label> <input id="refValue" name="refValue" type="text"><br/>
            </div>
            <div id="divref2">
                <label id="lblRef2" class="labela" for="refValue">Reference Value 2</label> <input id="refValue2" name="refValue2" type="text"><br/>
            </div>
            <div id="divref3">
                <label id="lblBit48" class="labela" for="bit48Value">Bit 48</label> <input id="bit48Value" name="bit48Value" type="text" size="110" value="*FIRS00000107400203676480101201931122019"><br/>
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
            //$("#divref3").hide();
        }
    
        init_opr = function() {
            $('#oprType').on('change',function() {
                console.log($(this).val());
                var operationType = $(this).val();
                if(operationType == "") {
                    $("#divref1").hide();
                    $("#divref2").hide();
                }
                
                
                if(operationType == "inquiry_cif") {
                    $("#lblRef").html("CIF No.");
                    
                    $("#divref1").show();
                    $("#divref2").hide();
                    
                }
                
                if(operationType == "inquiry_cif_next") {
                    $("#lblRef").html("CIF No.");
                    $("#lblRef2").html("Last Account No. & Type");                    
                    
                    $("#divref1").show();
                    $("#divref2").show();
                    
                }
                
                if(operationType == "hold_amount") {
                    $("#lblRef").html("Account No.(19)");
                    $("#lblRef2").html("Amount Hold(15)");
                    
                    $("#divref1").show();
                    $("#divref2").show();
                }
                
                if(operationType == "release") {
                    $("#lblRef").html("Hold Id No.");
                    
                    $("#divref1").show();
                    $("#divref2").hide();
                }
                
                if(operationType == "bit_48" || operationType == "bit_48_neg") {

                    
                    $("#divref1").hide();
                    $("#divref2").hide();
                    $("#divref3").show();
                }
            });
        }
        
        init_disp();
        init_opr();
        $('#oprType').change();
</script>