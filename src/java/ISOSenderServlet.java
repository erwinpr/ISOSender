/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.XMLPackager;

/**
 *
 * @author Erwin Prasetyo
 */
public class ISOSenderServlet extends HttpServlet {
    /* disconnects connection, do not use */
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
        try {
            response.setContentType("text/html;charset=UTF-8");
            
            PrintWriter out = response.getWriter();
            //out.println("aaa");
            ISOPackager packager = new XMLPackager();
            ISOChannel channel = new XMLChannel("10.11.17.10", 23228, packager);
            channel.connect();
            ISOMsg ISOrequest = new ISOMsg();

            ISOrequest.setMTI("0200");

            ISOrequest.set(2, "16");

            //ISOrequest.set(2, "5421287475388412");
            

            //ISOrequest.set(3, "000000");
            
            //ISOrequest.set(4, "400.0");

            //ISOrequest.set(7, "0716070815");

            //ISOrequest.set(11, "844515");
            
            ISOrequest.set(2, "11689655782936215552");
            ISOrequest.set(3, "996501");
            ISOrequest.set(7, "1115105044");
            ISOrequest.set(11, "123122");
            ISOrequest.set(12, "105044");
            ISOrequest.set(13, "1115");
            ISOrequest.set(15, "1115");
            ISOrequest.set(37, "941115123122");
            ISOrequest.set(41, "10.11.33.175");
            ISOrequest.set(48, "E040588");
            
            channel.send(ISOrequest);

            ISOMsg ISOresponse = channel.receive();
            
            out.println(ISOresponse);
            //ISOresponse.dump(out, "response:");
        } catch (ISOException ex) {
            Logger.getLogger(ISOSenderServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
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
