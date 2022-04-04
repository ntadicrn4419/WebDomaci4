package com.web.webdomaci4;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "confirmationServlet", value = "/potvrda")
public class ConfirmationServlet extends HttpServlet {

    private String message;
    private Map<String, Map<String, Integer>> food; // key je dan, value je mapa gde je key jelo, a value broj porcija

    public void init() {
        this.message = "Uspesno ste odabrali meni za narednu nedelju!";
        this.food = new HashMap<>();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("doGet in ConfirmationServlet");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
        this.message = "Uspesno ste odabrali meni za narednu nedelju!";//ovde moramo da restartujemo poruku, zato sto smo je menjali u doPost-u.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPost in ConfirmationServlet");

        if(request.getSession().getAttribute("ponedeljak") != null &&
            request.getServletContext().getAttribute("restart") != "true"){

            this.message = this.generatePickedMenuHtml(request);
            response.sendRedirect("/potvrda");
            return;
        }

        this.fillFoodMap(request);
        this.updateServletContextDaysAttributes(request);
        this.food.clear();
        request.getServletContext().setAttribute("restart", "false");
        response.sendRedirect("/potvrda");
    }

    private void updateServletContextDaysAttributes(HttpServletRequest request) {

        String[] days = {"ponedeljak", "utorak", "sreda", "cetvrtak", "petak"};
        for(String day: days){
            request.getSession().setAttribute(day, request.getParameter(day));

            if(request.getServletContext().getAttribute(day) == null){
                request.getServletContext().setAttribute(day, this.food.get(day));
            }else{
                Map<String, Integer> map = (Map<String, Integer>) request.getServletContext().getAttribute(day);
                for(String k: this.food.get(day).keySet()){
                    if(map.containsKey(k)){
                        int val = map.get(k);
                        map.put(k, val+1);
                    }else{
                        map.put(k, 1);
                    }
                }
                request.getServletContext().setAttribute(day, map);
            }
        }
    }

    private void fillFoodMap(HttpServletRequest request) {

        String[] days = {"ponedeljak", "utorak", "sreda", "cetvrtak", "petak"};
        for (String day: days) {
            Map<String, Integer> dailyMenu = new HashMap<>();
            dailyMenu.put(request.getParameter(day), 1);
            this.food.put(day, dailyMenu);
        }
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
