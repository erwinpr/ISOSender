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
import org.jpos.iso.ISOUtil;;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * WalletServlet
 * @author Andi Eko Saputro
 */

public class WalletServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String servletInfo = getServletInfo();
        
        String reqType = request.getParameter("reqType");
        String bitmap;
        
        String bit2 = request.getParameter("bit2Value");
        String bit3 = request.getParameter("bit3Value");
        String bit4 = request.getParameter("bit4Value");
        String bit7 = request.getParameter("bit7Value");
        String bit11 = request.getParameter("bit11Value");
        String bit12 = request.getParameter("bit12Value");
        String bit13 = request.getParameter("bit13Value");
        String bit15 = request.getParameter("bit15Value");
        String bit37 = request.getParameter("bit37Value");
        String bit41 = request.getParameter("bit41Value");
        String bit43 = request.getParameter("bit43Value");
        String bit48 = request.getParameter("bit48Value");
        String bit102 = request.getParameter("bit102Value");
        String bit103 = request.getParameter("bit103Value");
        
        String hostValue = request.getParameter("hostValue");
        String portValue = request.getParameter("portValue");
        
        int bit48Length;
        String str48Len;
        String bit48Prefix;
        
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
           port = 23222;
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
        
        /* begin bit processing */
        String messageToSend;
        
        //11110010001110100000000000000000000010001010000100000000000000000000000000000000000000000000000000000110000000000000000000000000
        bitmap = "F23A000008A100000000000006000000";

        bit48Length = bit48.length();
        str48Len = "000" + String.valueOf(bit48Length);
        bit48Prefix = str48Len.substring(str48Len.length() - 3);

        bit102Length = bit102.length();
        str102Len = "00" + String.valueOf(bit102Length);
        bit102Prefix = str102Len.substring(str102Len.length() - 2);

        bit103Length = bit103.length();
        str103Len = "00" + String.valueOf(bit103Length);
        bit103Prefix = str103Len.substring(str103Len.length() - 2);

        messageToSend = reqType+bitmap+bit2+bit3+bit4+bit7+bit11+bit12+bit13+bit15+bit37+bit41+bit43+bit48Prefix+bit48+bit102Prefix+bit102+bit103Prefix+bit103;

        /* end bit processing */    
        
        byte[] ISObits;
        byte[] msgToSend;
        String msg = "";
        String serverResp;

        PrintWriter out = response.getWriter();
        
        try {        
            
            ISObits = ISOUtil.asciiToEbcdic(messageToSend);
            
            for(byte b : ISObits){
                //System.out.println(b);
                msg = msg + b;
            }
            
            Map<String, String> outputmap = new HashMap<String, String>();
            outputmap.put("servletInfo", servletInfo);
            outputmap.put("oprType", bit3);
            outputmap.put("sentValue", messageToSend);
            outputmap.put("sentMessage", msg);

            msgToSend = buildHeader(ISObits);
            serverResp = sendMessage(msgToSend,host,port);
            outputmap.put("serverResponse", serverResp);
            
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
        
        DataOutputStream dOut = null;
        
        try{
            // Create and connect the socket
            Socket clientSocket = new Socket(hostname, port);
            clientSocket.setSoTimeout(5*1000);
            dOut = new DataOutputStream(clientSocket.getOutputStream());

            dOut.write(message); 

            /* this currently  works, do not tamper*/

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            byte[] buffer = new byte[4096];
            String serverResp = "";
            
            /* alasan jalan 2x //
            The problem you have is related to TCP streaming nature.

            The fact that you sent 100 Bytes (for example) from the server doesn't mean 
            you will read 100 Bytes in the client the first time you read. Maybe the bytes sent 
            from the server arrive in several TCP segments to the client.
            */
            in.read(buffer, 0, buffer.length);
            serverResp += ISOUtil.ebcdicToAscii(buffer);

            in.read(buffer, 0, buffer.length);
            serverResp += ISOUtil.ebcdicToAscii(buffer);

            dOut.flush();
            dOut.close();
            
            response =  serverResp.replaceAll("\\u0000", "");
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
    
    public byte[] buildHeader(byte[] msg){
        //header
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
