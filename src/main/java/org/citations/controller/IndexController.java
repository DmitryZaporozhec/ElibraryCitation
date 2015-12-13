package org.citations.controller;

import com.sun.net.httpserver.HttpServer;
import org.citations.agent.Agent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by zaporozhec on 12/12/15.
 */
@Controller
@RequestMapping(value = "/citations")
public class IndexController {
    @RequestMapping(value = "")
    public String getIndex(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "inputURL", required = false) String inputURL) throws Exception {
        if (inputURL != null && !inputURL.isEmpty()) {
            Agent agent = new Agent();
            String filename = null;
            try {
                filename = agent.getAuthorArticles(inputURL);
            } catch (Exception e) {
                request.setAttribute("customError", e);
            }
            if (filename != null) {

                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Type", "text/xml; utf-8");
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(filename), "UTF-8"));

                StringBuilder str = new StringBuilder();
                try {

                    String c;
                    while ((c = in.readLine()) != null) {
                        str.append(c);
                        str.append("\n");
                    }
                } finally {
                    if (in != null)
                        in.close();
                    response.getWriter().write(str.toString());
                    response.getWriter().close();
                    response.flushBuffer();
                    return null;
                }
            }
        }
        return "index";
    }
}
