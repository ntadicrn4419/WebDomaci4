package com.web.webdomaci4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "alreadyChoosenMenuServlet", value = "/vec-odabrani-meni")
public class AlreadyChoosenMenuServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet in AlreadyChoosenMenuServlet");

        response.setContentType("text/html");

        String html = generatePickedMenuHtml(request);

        PrintWriter out = response.getWriter();
        out.println(html);
    }

    private String generatePickedMenuHtml(HttpServletRequest request){

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Vec ste izabrali meni za narednu nedelju!</h1>");

        sb.append("<h2>Ponedeljak:</h2>");
        sb.append("<h3>");
        sb.append(request.getSession().getAttribute("ponedeljak"));
        sb.append("</h3><br>");

        sb.append("<h2>Utorak:</h2>");
        sb.append("<h3>");
        sb.append(request.getSession().getAttribute("utorak"));
        sb.append("</h3><br>");

        sb.append("<h2>Sreda:</h2>");
        sb.append("<h3>");
        sb.append(request.getSession().getAttribute("sreda"));
        sb.append("</h3><br>");

        sb.append("<h2>Cetvrtak:</h2>");
        sb.append("<h3>");
        sb.append(request.getSession().getAttribute("cetvrtak"));
        sb.append("</h3><br>");

        sb.append("<h2>Petak:</h2>");
        sb.append("<h3>");
        sb.append(request.getSession().getAttribute("petak"));
        sb.append("</h3><br>");

        return sb.toString();
    }
}
