package com.web.webdomaci4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "startServlet", value = "/")
public class StartServlet extends HttpServlet {

    private String message;

    private List<String> mondayMenu;
    private List<String> tuesdayMenu;
    private List<String> wednesdayMenu;
    private List<String> thursdayMenu;
    private List<String> fridayMenu;

    private StringBuilder optionMenuMonday;
    private StringBuilder optionMenuTuesday;
    private StringBuilder optionMenuWednesday;
    private StringBuilder optionMenuThursday;
    private StringBuilder optionMenuFriday;

    private final String PATH_PREFIX = "C:\\Users\\HP\\IdeaProjects\\demo\\WebDomaci4\\";

    public void init() {
        this.message = "Start servlet init!";

        this.mondayMenu = readMenuFromFile(this.PATH_PREFIX + "src\\main\\resources\\ponedeljak.txt");
        this.tuesdayMenu = readMenuFromFile(this.PATH_PREFIX + "src\\main\\resources\\utorak.txt");
        this.wednesdayMenu = readMenuFromFile(this.PATH_PREFIX + "src\\main\\resources\\sreda.txt");
        this.thursdayMenu = readMenuFromFile(this.PATH_PREFIX + "src\\main\\resources\\cetvrtak.txt");
        this.fridayMenu = readMenuFromFile(this.PATH_PREFIX + "src\\main\\resources\\petak.txt");

        this.optionMenuMonday = new StringBuilder();
        this.optionMenuTuesday = new StringBuilder();
        this.optionMenuWednesday = new StringBuilder();
        this.optionMenuThursday = new StringBuilder();
        this.optionMenuFriday = new StringBuilder();

        this.loadOptionsMenu();
    }

    private void loadOptionsMenu(){
        for(int i = 0; i < mondayMenu.size(); i++){

            optionMenuMonday.append("<option>");
            optionMenuMonday.append(mondayMenu.get(i));
            optionMenuMonday.append("</option>\n");

            optionMenuTuesday.append("<option>");
            optionMenuTuesday.append(tuesdayMenu.get(i));
            optionMenuTuesday.append("</option>\n");

            optionMenuWednesday.append("<option>");
            optionMenuWednesday.append(wednesdayMenu.get(i));
            optionMenuWednesday.append("</option>\n");

            optionMenuThursday.append("<option>");
            optionMenuThursday.append(thursdayMenu.get(i));
            optionMenuThursday.append("</option>\n");

            optionMenuFriday.append("<option>");
            optionMenuFriday.append(fridayMenu.get(i));
            optionMenuFriday.append("</option>\n");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("doGet in StartServlet");
        response.setContentType("text/html");

        String html = generateHtml();

        PrintWriter out = response.getWriter();
        out.println(html);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost in StartServlet");

        request.getSession().setAttribute("ponedeljak", null);
        request.getSession().setAttribute("utorak", null);
        request.getSession().setAttribute("sreda", null);
        request.getSession().setAttribute("cetvrtak", null);
        request.getSession().setAttribute("petak", null);

        request.getServletContext().setAttribute("ponedeljak", null);
        request.getServletContext().setAttribute("utorak", null);
        request.getServletContext().setAttribute("sreda", null);
        request.getServletContext().setAttribute("cetvrtak", null);
        request.getServletContext().setAttribute("petak", null);

        request.getServletContext().setAttribute("restart", "true");

        response.sendRedirect("/");
    }

    private List<String> readMenuFromFile(String fileName){

        List<String> menu = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                menu.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return menu;

    }

    private String generateHtml(){

        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "<style>" +
                        "h1 {\n" +
                        "    color: #aa0441;\n" +
                        "    font-family: American Typewriter, serif;\n" +
                        "    font-style: normal;\n" +
                        "    font-weight: normal;\n" +
                        "    font-size: xxx-large;\n" +
                        "}\n" +
                        "\n" +
                        "label {\n" +
                        "    color: #04AA6D;\n" +
                        "    font-family: American Typewriter, serif;\n" +
                        "    font-style: normal;\n" +
                        "    font-weight: bold;\n" +
                        "    font-size: xx-large;\n" +
                        "}\n" +
                        "\n" +
                        "select{\n" +
                        "    color: #132f38;\n" +
                        "    font-family: American Typewriter, serif;\n" +
                        "    font-style: normal;\n" +
                        "    font-weight: normal;\n" +
                        "    font-size: x-large;\n" +
                        "}\n" +
                        "\n" +
                        "#submit{\n" +
                        "    background-color: #04AA6D; /* Green */\n" +
                        "    border: none;\n" +
                        "    color: white;\n" +
                        "    padding: 15px 32px;\n" +
                        "    text-align: center;\n" +
                        "    text-decoration: none;\n" +
                        "    display: inline-block;\n" +
                        "    font-size: xx-large;\n" +
                        "}"+
                        "</style>"+
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Odabir jela</title>\n" +
                        "</head>\n" +
                        "\n" +
                        "<body>\n" +
                        "<h1>Odaberite vas rucak:</h1>\n" +
                        "<br>\n" +
                        "<form method=\"POST\" action=\"/potvrda\">\n" +
                        "    <label for=\"ponedeljak\">Ponedeljak</label><br>\n" +
                        "    <select id=\"ponedeljak\" name=\"ponedeljak\" size=\"1\">\n" +
                        this.optionMenuMonday +
                        "    </select><br><br>\n" +
                        "\n" +
                        "    <label for=\"utorak\">Utorak</label><br>\n" +
                        "    <select id=\"utorak\" name=\"utorak\" size=\"1\">\n" +
                        this.optionMenuTuesday +
                        "    </select><br><br>\n" +
                        "\n" +
                        "    <label for=\"sreda\">Sreda</label><br>\n" +
                        "    <select id=\"sreda\" name=\"sreda\" size=\"1\">\n" +
                        this.optionMenuWednesday +
                        "    </select><br><br>\n" +
                        "\n" +
                        "    <label for=\"cetvrtak\">Cetvrtak</label><br>\n" +
                        "    <select id=\"cetvrtak\" name=\"cetvrtak\" size=\"1\">\n" +
                        this.optionMenuThursday +
                        "    </select><br><br>\n" +
                        "\n" +
                        "    <label for=\"petak\">Petak</label><br>\n" +
                        "    <select id=\"petak\" name=\"petak\" size=\"1\">\n" +
                        this.optionMenuFriday +
                        "    </select><br><br>\n" +
                        "    <input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Potvrdite unos\"/>\n" +
                        "</form>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>"
        );
        return htmlBuilder.toString();
    }
}
