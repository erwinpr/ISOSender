/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.*;
import java.util.*;
import org.jpos.iso.ISOUtil;
//import org.jpos;

/**
 *
 * @author Erwin Prasetyo
 */
public class PagingServlet extends HttpServlet {

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
        String oprType = request.getParameter("oprType");
        String refValue = request.getParameter("refValue");
        String refValue2 = request.getParameter("refValue2");
        
        String callerId = request.getParameter("callerId");
        
        String bit3 = request.getParameter("bit3Value");
        String bit11 = request.getParameter("bit11Value");
        String bit37 = request.getParameter("bit37Value");
        String bit48 = request.getParameter("bit48Value");
        String bit60 = request.getParameter("bit60Value");
        String bit62 = request.getParameter("bit62Value");
        String bit102 = request.getParameter("bit102Value");
        String bit103 = request.getParameter("bit103Value");
        
        String hostValue = request.getParameter("hostValue");
        String portValue = request.getParameter("portValue");
        
        //String repeatVal = request.getParameter("repeatVal");
        
        String sample = "";
        
        int bit48Length;
        String str48Len;
        String bit48Prefix;
        
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
        
        byte[] ISObits;
        byte[] msgToSend;
        String msg = "";
        String serverResp = "";

        PrintWriter out = response.getWriter();
        try {        
            
            Map<String, String> outputmap = new HashMap<String, String>();
            outputmap.put("servletInfo", servletInfo);
            //outputmap.put("sentValue", testValue);
            //outputmap.put("sentMessage", msg);
            // etc
            
            Map<String, String> responseMap = new HashMap<String, String>();
            
            int tempInt,k=0;
            long tempLong;
            String flgNext = "Y";
            String tmpStr = "";
            //int repeater = Integer.parseInt(repeatVal);
            
            //for(int k = 0;k < repeater; k++) {
            while(flgNext.equals("Y") && k < 10) {    
                tempInt = Integer.parseInt(bit11.trim()) + 1;
                bit11 = Integer.toString(tempInt);

                tempLong = Long.parseLong(bit37.trim()) + 1;
                bit37 = Long.toString(tempLong);
                
                bit48Length = bit48.length();
                str48Len = "000" + String.valueOf(bit48Length);
                bit48Prefix = str48Len.substring(str48Len.length() - 3);

                sample = "0200223A400008810000"+bit3+"0225024043"+ bit11 +"024043022502254444" +bit37 + callerId + "      " + bit48Prefix + bit48;
                
                ISObits = ISOUtil.asciiToEbcdic(sample);
            
                for(byte b : ISObits){
                    msg = msg + b;
                }
                
                msgToSend = buildHeader(ISObits);
                serverResp = sendMessage(msgToSend,host,port);            
                responseMap.put(Integer.toString(k), serverResp);
                if(serverResp.length() > 0){
                    tmpStr = serverResp.substring(134,135);
                    flgNext = tmpStr;
                    if(flgNext.equals("Y")) {
                        outputmap.put("masukSini", "true");
                        bit48 = bit48.replace("*FIRS", "*NEXT");
                    }else{
                        flgNext = "N";
                    }
                }else{
                    flgNext = "N";
                }
                k++;
            }
            responseMap.put("Paging",tmpStr);
            
            String json = "";
            json = new Gson().toJson(responseMap);
            
            outputmap.put("sentValue", bit48);
            outputmap.put("Response", json);
            
            json = new Gson().toJson(outputmap); // anyObject = List<Bean>, Map<K, Bean>, Bean, String, etc..
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
            clientSocket.setSoTimeout(10*1000);
            dOut = new DataOutputStream(clientSocket.getOutputStream());

            dOut.write(message); 

            /* this currently  works, do not tamper*/

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            byte[] buffer = new byte[4096];
            in.read(buffer, 0, buffer.length);
            String respMsg = "";

            for(byte b : buffer){
                    respMsg = respMsg + b;
            }

            String serverResp = ISOUtil.ebcdicToAscii(buffer);

            dOut.flush();
            dOut.close();

            //return respMsg;
            //\\u0003 -> EOF 
            response =  serverResp.replaceAll("\\u0000", "");
        }catch(Exception ex) {
            if(dOut == null) {
                /* do absolutely nothing */
            }else{
                dOut.flush();
                dOut.close();
            }
        }
        return response;
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
