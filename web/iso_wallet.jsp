<%-- 
    Document   : iso_wallet.jsp
    Created on : Des 23, 2020, 9:33:19 PM
    Author     : Andi Eko Saputro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'>
        <title>ISO Sender E-Wallet</title>
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
        <h1>ISO Message Testing(E-Wallet)</h1>
        
        <form id="frmTestServlet" name="frmTestServlet" method="POST" action="WalletServlet">
            <label class="labela" for="hostValue">Remote Host</label> <input id="hostValue" name="hostValue" type="text" value="10.110.1.100"><br/>
            <label class="labela" for="portValue">Remote Port</label> <input id="portValue" name="portValue" type="text" value="23222"><br/>
            <label class="labela" for="reqType">Request Type</label> <select id="reqType" name="reqType">
                <!--<option value="">--</option>-->
                <option value="0200">0200 - Request</option>
                <!--<option value="0400">0400 - Reversal</option>-->
            </select>
            <label class="labela" for="bit2Value">Bit 2</label> <input id="bit2Value" name="bit2Value" type="text" value="82208229301"> <span>Primary Account No.</span><br/>
            <label class="labela" for="bit3Value">Bit 3</label> <input id="bit3Value" name="bit3Value" type="text" value="996001"> <span>Transaction Code</span><br/>
            <label class="labela" for="bit4Value">Bit 4</label> <input id="bit4Value" name="bit4Value" type="text" value="000000000000"> <span>Amount Trn.</span><br/>
            <label class="labela" for="bit4Value">Bit 7</label> <input id="bit7Value" name="bit7Value" type="text" value="0111114345"> <span>Trn.Date/Time</span><br/>
            <label class="labela" for="bit11Value">Bit 11</label> <input id="bit11Value" name="bit11Value" type="text" value="841861"> <span>Trn.Trace No.</span><br/>
            <label class="labela" for="bit11Value">Bit 12</label> <input id="bit12Value" name="bit12Value" type="text" value="182632"> <span>Time<HHMMSS></span><br/>
            <label class="labela" for="bit11Value">Bit 13</label> <input id="bit13Value" name="bit13Value" type="text" value="0111"> <span>Date<MMDD></span><br/>
            <label class="labela" for="bit37Value">Bit 37</label> <input id="bit37Value" name="bit37Value" type="text" value="251733841861"> <span>Transaction Ref No.</span><br/>
            <label class="labela" for="bit41Value">Bit 41</label> <input id="bit41Value" name="bit41Value" type="text" size="110"><br/>
            <label class="labela" for="bit43Value">Bit 43</label> <input id="bit43Value" name="bit43Value" type="text" size="110"><br/>
            <label class="labela" for="bit48Value">Bit 48</label> <input id="bit48Value" name="bit48Value" type="text" size="110"><br/>
            <label class="labela" for="bit102Value">Bit 102</label> <input id="bit102Value" name="bit102Value" type="text" size="110"><br/>
            <label class="labela" for="bit102Value">Bit 103</label> <input id="bit103Value" name="bit103Value" type="text" size="110"><br/>

            <input id="btnSubmit" name="btnSubmit" type="Submit" value="Post Data">
        </form>
    </body>
</html>

<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
        init_disp = function() {
            /* do absolutely nothing */
        };
        
        init_disp();
</script>