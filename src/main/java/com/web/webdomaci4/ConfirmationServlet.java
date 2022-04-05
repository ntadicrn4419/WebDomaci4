package com.web.webdomaci4;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "confirmationServlet", value = "/potvrda")
public class ConfirmationServlet extends HttpServlet {

    private String message;
    private Map<String, Map<String, Integer>> food; // key je dan, value je mapa gde je key jelo, a value broj porcija

    public void init() {
        this.message = "Uspesno ste odabrali meni za narednu nedelju!";
        this.food = new ConcurrentHashMap<>();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("doGet in ConfirmationServlet");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPost in ConfirmationServlet");

        synchronized (this){
            if(request.getSession().getAttribute("ponedeljak") != null &&
                    request.getServletContext().getAttribute("restart") != "true"){
                response.sendRedirect("/vec-odabrani-meni");
                return;
            }
        }

        this.fillFoodMap(request);
        this.updateServletContextAttributes(request);
        this.food.clear();
        response.sendRedirect("/potvrda");
    }

    private synchronized void updateServletContextAttributes(HttpServletRequest request) {

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
        request.getServletContext().setAttribute("restart", "false");
    }

    private void fillFoodMap(HttpServletRequest request) {

        String[] days = {"ponedeljak", "utorak", "sreda", "cetvrtak", "petak"};
        for (String day: days) {
            Map<String, Integer> dailyMenu = new ConcurrentHashMap<>();
            dailyMenu.put(request.getParameter(day), 1);
            this.food.put(day, dailyMenu);
        }
    }
}
