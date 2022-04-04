package com.web.webdomaci4;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "choosenFoodServlet", value = "/odabrana-jela")
public class ChoosenFoodServlet extends HttpServlet {

    private final String PATH_PREFIX = "C:\\Users\\HP\\IdeaProjects\\demo\\WebDomaci4\\";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("doGet in ChoosenFoodServlet");

        String userPassword = request.getParameter("password");
        String requiredPassword = this.readPasswordFromFile(PATH_PREFIX + "src\\main\\resources\\password.txt");

        if(!checkPassword(userPassword, requiredPassword, response)){
            return;
        }
        response.setContentType("text/html");

        Map<String, String> preparedTableContent = this.prepareTable();
        String html;
        if(preparedTableContent == null){
            html = generateHtmlMessage();
        }else{
            html = generateHtml(preparedTableContent);
        }
        PrintWriter out = response.getWriter();
        out.println(html);
    }

    private Map<String, String> prepareTable(){

        if(this.getServletContext().getAttribute("ponedeljak") == null){
            return null;
        }

        Map<String, String> preparedTableContent = new HashMap<>();

        String[] days = {"ponedeljak", "utorak", "sreda", "cetvrtak", "petak"};
        Map<String, Integer> dailyMenu;
        for(String day: days){
            StringBuilder sb = new StringBuilder();
            dailyMenu = (Map<String, Integer>) this.getServletContext().getAttribute(day);
            int cnt = 1;
            for(String key: dailyMenu.keySet()){

                sb.append("<tr>\n");
                sb.append("<td>");
                sb.append(cnt);
                sb.append("</td>\n");

                sb.append("<td>");
                sb.append(key);
                sb.append("</td>\n");

                sb.append("<td>");
                sb.append(dailyMenu.get(key));
                sb.append("</td>\n");
                sb.append("<tr>\n");
                cnt++;
            }
            preparedTableContent.put(day, sb.toString());
        }
        return preparedTableContent;
    }

    private String generateHtmlMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head><body>");
        sb.append("<h1>Nema jos podataka.</h1>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private String readPasswordFromFile(String fileName){
        String password = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                password += line;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return password;
    }

    private boolean checkPassword(String userPassword, String requiredPassword, HttpServletResponse response) throws IOException {

        StringBuilder sb = new StringBuilder();

        if(userPassword == null || userPassword == ""){

            sb.append("<html><head></head><body>");
            sb.append("<h1>Morate uneti password kao query parametar u url!</h1>");
            sb.append("<h2>Na primer: http://localhost:8080/odabrana-jela?password=vasalozinka</h2>");
            sb.append("</body></html>");
            response.getOutputStream().println(sb.toString());
            response.setStatus(403);
            return false;
        }

        if(!userPassword.equals(requiredPassword)){

            sb.append("<html><head></head><body>");
            sb.append("<h1>Pogresna lozinka!</h1>");
            sb.append("</body></html>");
            response.getOutputStream().println(sb.toString());
            response.setStatus(403);
            return false;
        }
        return true;
    }

    private String generateHtml(Map<String, String> preparedTableContent) {
        StringBuilder html = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        sb.append("<tr>\n");
        sb.append("<th>#</th>\n");
        sb.append("<th>Jelo</th>\n");
        sb.append("<th>Kolicina</th>\n");
        sb.append("</tr>\n");

        String tableHeader = sb.toString();

        html.append(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Odabrana jela</title>\n" +
                        "    <style>\n" +
                        "       #submit {\n" +
                        "           background-color: #04AA6D; /* Green */\n" +
                        "           border: none;\n" +
                        "           color: white;\n" +
                        "           padding: 15px 32px;\n" +
                        "           text-align: center;\n" +
                        "           text-decoration: none;\n" +
                        "           display: inline-block;\n" +
                        "           font-size: xx-large;\n" +
                        "       }"+
                        "        table {\n" +
                        "            font-family: Arial, Helvetica, sans-serif;\n" +
                        "            border-collapse: collapse;\n" +
                        "            width: 75%;\n" +
                        "            table-layout: fixed;\n" +
                        "        }\n" +
                        "\n" +
                        "        table td, table th {\n" +
                        "            border: 1px solid #ddd;\n" +
                        "            padding: 8px;\n" +
                        "            overflow: hidden;" +
                        "        }\n" +
                        "\n" +
                        "        table tr:nth-child(even){background-color: #f2f2f2;}\n" +
                        "\n" +
                        "        table tr:hover {background-color: #ddd;}\n" +
                        "\n" +
                        "        table th {\n" +
                        "            padding-top: 12px;\n" +
                        "            padding-bottom: 12px;\n" +
                        "            text-align: left;\n" +
                        "            background-color: #04AA6D;\n" +
                        "            color: white;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1>Odabrana jela</h1>" +
                        "<form method=\"POST\" action=\"/\">\n" +
                        "    <input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Ocisti\"/>\n" +
                        "</form>" +
                        "<h2>Ponedeljak</h2>\n" +
                        "<table name=\"ponedeljak\" id=\"pondeljak\">\n" +
                        tableHeader +
                        preparedTableContent.get("ponedeljak") +
                        "</table>\n" +
                        "\n" +
                        "<h2>Utorak</h2>\n" +
                        "<table name=\"utorak\" id=\"utorak\">\n" +
                        tableHeader +
                        preparedTableContent.get("utorak") +
                        "</table>\n" +
                        "\n" +
                        "<h2>Sreda</h2>\n" +
                        "<table name=\"sreda\" id=\"sreda\">\n" +
                        tableHeader +
                        preparedTableContent.get("sreda") +
                        "</table>\n" +
                        "\n" +
                        "<h2>Cetvrtak</h2>\n" +
                        "<table name=\"cetvrtak\" id=\"cetvrtak\">\n" +
                        tableHeader +
                        preparedTableContent.get("cetvrtak") +
                        "</table>\n" +
                        "\n" +
                        "<h2>Petak</h2>\n" +
                        "<table name=\"petak\" id=\"petak\">\n" +
                        tableHeader +
                        preparedTableContent.get("petak") +
                        "</table>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>"
        );

        return html.toString();
    }
}
