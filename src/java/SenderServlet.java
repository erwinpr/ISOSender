/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.*;
import org.jpos.iso.ISOUtil;
//import org.jpos;
import java.io.PrintWriter;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 *
 * @author Erwin Prasetyo
 */
public class SenderServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String servletInfo = getServletInfo();
        String testValue = request.getParameter("testValue");
        
        String reqType = request.getParameter("reqType");
        String oprType = request.getParameter("oprType");
        String refValue = request.getParameter("refValue");
        String refValue2 = request.getParameter("refValue2");
        
        String callerId = request.getParameter("callerId");
        String bitmap;
        
        String bit3 = request.getParameter("bit3Value");
        String bit7 = "0225024043";
        String bit11 = request.getParameter("bit11Value");
        String bit12 = "024043";
        String bit13 = "0225";
        String bit15 = "0225";
        String bit18 = "4444";
        String bit35;
        String bit37 = request.getParameter("bit37Value");
        //String bit41 = callerId + "      ";
        String bit41 = padRight(callerId, 16);
        String bit48 = request.getParameter("bit48Value");
        String bit60 = request.getParameter("bit60Value");
        String bit62 = request.getParameter("bit62Value");
        String bit90 = request.getParameter("bit90Value");
        String bit102 = request.getParameter("bit102Value");
        String bit103 = request.getParameter("bit103Value");
        
        String hostValue = request.getParameter("hostValue");
        String portValue = request.getParameter("portValue");
        
        String sample = "";
        String cifno;
        String lastAccNmbrAndType;
        
        int bit35Length;
        String str35Len;
        String bit35Prefix;
        
        int bit48Length;
        String str48Len;
        String bit48Prefix;
        
        int bit60Length;
        String str60Len;
        String bit60Prefix;
        
        int bit62Length;
        String str62Len;
        String bit62Prefix;
        
        int bit102Length;
        String str102Len;
        String bit102Prefix;
        
        int bit103Length;
        String str103Len;
        String bit103Prefix;
        
        int port;
        try {
           port = Integer.parseInt(portValue);
        }catch (NumberFormatException e){
           port = 23237;
        }
        
        String host; 
        if(hostValue == null || hostValue.isEmpty()){        
            host = "10.110.1.100";
        }else{
            host = hostValue;
        }
        
        if(reqType == null || reqType.isEmpty()) {
            reqType = "0200";
        }
        
        //ganti sequence setiap akan send
        //String seq = "122415";
        String seq = testValue;
        
        String channel = request.getParameter("bit18Value");
        if(channel != null && channel.length() == 4) {
            bit18 = channel;
        }
        
        switch(oprType) {
        
            case "inquiry_cif" :
            //inquiry cif
            cifno = refValue;
            sample = reqType + "A23A40000881001000000000000000001820061115105044"+ seq +"105044071607168801940716"+ seq +"DEV4            007" + cifno + "0190000000000000000000";
            //invalid message
            //String sample = "0200F23A40000881001000000000000000019965011115105044"+ seq +"105044070407048801940704"+ seq +"WMS01           007S0083400190000000000000000000";

            break;
            
            case "inquiry_cif_next" :
            cifno = refValue;
            lastAccNmbrAndType = refValue2;
            sample = reqType + "A23A40000881001000000000000000001820061115105044"+ seq +"105044071607168801940716"+ seq +"DEV4            007" + cifno + "020" + lastAccNmbrAndType;
                
            break;

            case "hold_amount" :
            
            //hold amount

            //String acchold = "0000010210020091199";
            String acchold = refValue;
            //String acchold = "0000000000000000000";
            //String amthold = "000000056500035";
            String amthold = refValue2;
            String chghold = "000000000000000";
            
            sample = reqType + "A23A40000881000000000000000000002820150704105044"+ seq +"105044070407048801940704"+ seq +"DEV4            079"+ acchold + amthold + chghold +"Hold Amount Request OVB       ";

            break;
            
            case "release" :
            
            //release amount
            
            String accrls = "0000010210020091199";
            String acctyp = "S";
            //String seqrls = "00001";
            String seqrls = refValue;
            //String sample = "0200A23A00000881000000000000000000002820161115105044"+ seq +"10504411151115941115"+ seq +"WMS01           025"+ accrls + acctyp + seqrls;
            //with bit 18
            //sample = "0200A23A40000881000000000000000000002820160704105044"+ seq +"105044070407048801940704"+ seq +"WMS01           025"+ accrls + acctyp + seqrls;
            sample = reqType + "A23A40000881000000000000000000002820160704105044"+ seq +"105044070407048801940704"+ seq +"DEV4            025"+ accrls + acctyp + seqrls;
            
        
            break;    
            
            case "bit_48" :
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);

            sample = reqType + "223A400008810000"+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + callerId + "      " + bit48Prefix + bit48;
                
            break;
            
            case "bit_48_neg" :
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);

            sample = reqType + "A23A511008810000"+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + callerId + "      " + bit48Prefix + bit48;
                
            break;
            
            case "bit_60" :
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);
            
            bit60Length = bit60.length();
            str60Len = "000" + String.valueOf(bit60Length);
            bit60Prefix = str48Len.substring(str60Len.length() - 3);
            
            sample = reqType + "223A400008810000"+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + callerId + "      " + bit48Prefix + bit48 + bit60Prefix + bit60;
                
            break;
            
            case "iso_ovb" :
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);
            
            bit102Length = bit102.length();
            str102Len = "00" + String.valueOf(bit102Length);
            bit102Prefix = str102Len.substring(str102Len.length() - 2);
            
            bit103Length = bit103.length();
            str103Len = "00" + String.valueOf(bit103Length);
            bit103Prefix = str103Len.substring(str103Len.length() - 2);
                        
            sample = reqType + "A23A4000088100000000000006000000"+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + "1234567891234567" + bit48Prefix + bit48 +bit102Prefix+ bit102 +bit103Prefix+ bit103; // andi //
    
            break;
            
            case "iso_62" :
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);
            
            bit62Length = bit62.length();
            str62Len = "000" + String.valueOf(bit62Length);
            bit62Prefix = str62Len.substring(str62Len.length() - 3);
            
            bit102Length = bit102.length();
            str102Len = "00" + String.valueOf(bit102Length);
            bit102Prefix = str102Len.substring(str102Len.length() - 2);
            
            bit103Length = bit103.length();
            str103Len = "00" + String.valueOf(bit103Length);
            bit103Prefix = str103Len.substring(str103Len.length() - 2);
                        
            sample = reqType + "A23A4000088100040000000006000000"+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + "1234567891234567" + bit48Prefix + bit48 + bit62Prefix + bit62 +bit102Prefix+ bit102 +bit103Prefix+ bit103; // andi //
    
            break;
            
            case "iso_abc" :
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);
                        
            sample = reqType + "223A400008810000"+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + "1234567891234567" + bit48Prefix + bit48; // andi //
            //port = 23238;
            break;
            
            case "inq_bds" :
                
            bit35 = request.getParameter("bit35Value");
                
            bitmap = "223A400028810000";
            
            //llvar
            bit35Length = bit35.length();
            str35Len = "00" + String.valueOf(bit35Length);
            bit35Prefix = str35Len.substring(str35Len.length() - 2);
            
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);

            sample = reqType +bitmap+bit3+bit7+bit11+bit12+bit13+bit15+bit18+bit35Prefix+bit35+bit37+bit41+bit48Prefix+bit48;

            break;
            
            case "inq_bds_a" :
                
            bit35 = request.getParameter("bit35Value");
                
            bitmap = "223A400028810010";
            
            //llvar
            bit35Length = bit35.length();
            str35Len = "00" + String.valueOf(bit35Length);
            bit35Prefix = str35Len.substring(str35Len.length() - 2);
            
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);
            
            bit60Length = bit60.length();
            str60Len = "000" + String.valueOf(bit60Length);
            bit60Prefix = str48Len.substring(str60Len.length() - 3);

            sample = reqType+bitmap+bit3+bit7+bit11+bit12+bit13+bit15+bit18+bit35Prefix+bit35+bit37+bit41+bit48Prefix+bit48+bit60Prefix+bit60;

            break;

            case "ovb_bds_a" :

            bit35 = request.getParameter("bit35Value");

            //bitmap = "A23A4000088100000000000006000000";
            bitmap = "A23A4000288100000000000006000000";
            
            bit35Length = bit35.length();
            str35Len = "00" + String.valueOf(bit35Length);
            bit35Prefix = str35Len.substring(str35Len.length() - 2);
            
            //pad 10 space(s)
            //bit41 = padRight(callerId, 10);
            
            bit48Length = bit48.length();
            str48Len = "000" + String.valueOf(bit48Length);
            bit48Prefix = str48Len.substring(str48Len.length() - 3);
            
            bit102Length = bit102.length();
            str102Len = "00" + String.valueOf(bit102Length);
            bit102Prefix = str102Len.substring(str102Len.length() - 2);
            
            bit103Length = bit103.length();
            str103Len = "00" + String.valueOf(bit103Length);
            bit103Prefix = str103Len.substring(str103Len.length() - 2);
                        
            //sample = "0200"+bitmap+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + "1234567891234567" + bit48Prefix + bit48 +bit102Prefix+ bit102 +bit103Prefix+ bit103; // ae01 //
            sample = reqType+bitmap+bit3+bit7+bit11+bit12+bit13+bit15+bit18+bit35Prefix+bit35+bit37+bit41+bit48Prefix+bit48+bit102Prefix+bit102+bit103Prefix+bit103;
            
            break;
        }        
        
        //response.getWriter().write(oprType+sample);
        
        byte[] ISObits;
        byte[] msgToSend;
        String msg = "";
        String serverResp;
        //byte[] serverResp;
        //String resp = "";

        PrintWriter out = response.getWriter();
        try {        
            
            ISObits = ISOUtil.asciiToEbcdic(sample);
            
            for(byte b : ISObits){
                //System.out.println(b);
                msg = msg + b;
            }
            
            //System.out.println(Arrays.toString(ISObits));
            
            Map<String, String> outputmap = new HashMap<String, String>();
            outputmap.put("servletInfo", servletInfo);
            outputmap.put("oprType", oprType);
            outputmap.put("sentValue", sample);
            outputmap.put("sentMessage", msg);
            // etc

            //map.get("name"); // returns "demo"
            msgToSend = buildHeader(ISObits);
            //sendMessage(msgToSend);
            
            //log sent message to file
            //FileWriter myWriter = new FileWriter("C:\\Temp\\isosender\\sendlog.txt");
            //myWriter.write(Arrays.toString(ISObits));
            //byte[] expected = sample.getBytes("Cp1047");
            //myWriter.write(expected.toString());
            //myWriter.close();
            
            serverResp = sendMessage(msgToSend,host,port);
            //zzzz
            /*
            Socket clientSocket = new Socket(host, port);
            OutputStream outstream = clientSocket.getOutputStream(); 
            PrintWriter outs = new PrintWriter(outstream);

            String toSend = Convert(sample,"ISO-8859-1","CP1047");
            
            outs.print(toSend);
            
            InputStream instream = clientSocket.getInputStream();
            
            
            outs.close();
            */
            //zzz
            
            outputmap.put("serverResponse", serverResp);
            //outputmap.put("Message sent, please check");
            
            String json = new Gson().toJson(outputmap); // anyObject = List<Bean>, Map<K, Bean>, Bean, String, etc..
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            
        }catch(Exception ex) {
            //e.printStackTrace();
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            out.println(errors.toString());
        }finally{
            out.close();
        }
    }
    
    
    private String sendMessage(byte[] message, String hostname, int port) throws IOException {
        String response = "";
        //hostname = "10.110.1.100";
        //port = 23237;
        
        DataOutputStream dOut = null;
        
        try{
            //Socket socket = ...; // Create and connect the socket
            Socket clientSocket = new Socket(hostname, port);
            clientSocket.setSoTimeout(5*1000);
            dOut = new DataOutputStream(clientSocket.getOutputStream());

            dOut.write(message); 

            //BufferedReader buff = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //PrintStream os = new PrintStream(clientSocket.getOutputStream());
            //DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

            /* this currently  works, do not tamper*/

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            byte[] buffer = new byte[4096];
            //int loopnum = 5;
            //int z = 0;
            String serverResp = "";
            
            /* alasan jalan 2x //
            The problem you have is related to TCP streaming nature.

            The fact that you sent 100 Bytes (for example) from the server doesn't mean 
            you will read 100 Bytes in the client the first time you read. Maybe the bytes sent 
            from the server arrive in several TCP segments to the client.
            */
            in.read(buffer, 0, buffer.length);
            serverResp += ISOUtil.ebcdicToAscii(buffer);

            //byte[] AsciiBits = ISOUtil.ebcdicToAsciiBytes(buffer);
            //for(byte b : buffer){
            //        respMsg = respMsg + b;
            //}
            
            in.read(buffer, 0, buffer.length);
            serverResp += ISOUtil.ebcdicToAscii(buffer);
            /*
            while(in.read(buffer, 0, buffer.length) != -1){
                serverResp += ISOUtil.ebcdicToAscii(buffer);
            }
            */

            dOut.flush();
            dOut.close();

            
            //return respMsg;
            //\\u0003 -> EOF 
            response =  serverResp.replaceAll("\\u0000", "");
            //response = Integer.toString(buffer.length) + serverResp;
        }catch(Exception ex) {
            //ex.printStackTrace();
            
            if(dOut == null) {
                /* do absolutely nothing */
            }else{
                dOut.flush();
                dOut.close();
            }
        }
        return response;
    }
    
    public String Convert (String strToConvert,String in, String out){
       try {

        Charset charset_in = Charset.forName(out);
        Charset charset_out = Charset.forName(in);

        CharsetDecoder decoder = charset_out.newDecoder();

        CharsetEncoder encoder = charset_in.newEncoder();

        CharBuffer uCharBuffer = CharBuffer.wrap(strToConvert);

        ByteBuffer bbuf = encoder.encode(uCharBuffer);

        CharBuffer cbuf = decoder.decode(bbuf);

        String s = cbuf.toString();

        //System.out.println("Original String is: " + s);
        return s;

        } catch (CharacterCodingException e) {

            System.out.println("Character Coding Error: " + e.getMessage());
            //return "";
            return null;

        }
    }
    
    public byte[] buildHeader(byte[] msg){
	    //header
	    //String value = eventContext.getMessage().getProperty("header",PropertyScope.SESSION);
	 	byte[] header = "00".getBytes();
		ByteBuffer byteBuff = ByteBuffer.allocate(msg.length + 2);
		header[0] = (byte)(msg.length / 256);
		header[1] = (byte)(msg.length % 256);
		byteBuff.put(header);
		byteBuff.put(msg);
		byteBuff.compact();
		return byteBuff.array();
    }

    
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);  
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);  
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
