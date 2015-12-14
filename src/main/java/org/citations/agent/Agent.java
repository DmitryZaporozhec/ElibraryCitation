package org.citations.agent;

import org.apache.log4j.Logger;
import org.citations.model.Citation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zaporozhec on 12/12/15.
 */
public class Agent {

    private Logger log = Logger.getLogger(Agent.class);

    private String savetoFile(List<Citation> citationList, String userName) throws Exception {
        if (citationList == null || citationList.size() == 0)
            return null;
        String filename = null;
        if (userName == null) {
            filename = String.valueOf(new Date()) + ".txt";
        } else {
            filename = transliterateName(userName) + ".txt";
        }
        File f = new File(filename);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter fw = new FileWriter(f);
        for (int i = 0; i < citationList.size(); i++) {
            fw.append(i + ". " + citationList.get(i).toString());
        }
        fw.flush();
        fw.close();
        return filename;
    }


    private String transliterateName(String name) {
        return Transliterator.transliterate(name, true).replace(" ", "_").replace(".", "_");
    }

    public String getAuthorArticles(String urlFull) {
        URL url;
        String filename = null;
        String userShortName = null;
        try {
            url = new URL(urlFull);
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URLConnection conn = url.openConnection();
            //TODO cookie issue
            conn.setRequestProperty("Cookie", "SesCookieID=392846974; UserID=173871986; _ym_uid=1450029282156305229; _ym_isad=1; __utmt=1; __utma=216042306.751378257.1450029282.1450034930.1450035493.3; __utmb=216042306.7.10.1450035493; __utmc=216042306; __utmz=216042306.1450035493.3.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided)");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            org.jsoup.nodes.Document doc = Jsoup.parse(sb.toString());

            Elements title = doc.getElementsByTag("title");
            userShortName = this.getShortUserName(title.get(0).text().replace("eLIBRARY.RU - ", "").replace(" - Список публикаций", "").trim());

            Element table = doc.getElementById("restab");
            Elements trs = table.getElementsByTag("tr");
            List<Citation> citationList = new ArrayList<Citation>();
            if (trs != null && trs.size() > 0)
                for (int i = 1; i < trs.size(); i++) {
                    Element tr = trs.get(i);
                    if (tr.childNodes() != null && tr.childNodes().size() == 3) {
                        Citation c = new Citation();
                        try {
                            List<Node> tds = tr.childNodes();
                            Node td = tds.get(1);
                            String name = td.childNode(0).childNode(0).childNode(0).toString();
                            c.setName(name);
                            String authors = td.childNode(2).childNode(0).childNode(0).toString();
                            if (authors.split(",") != null && authors.split(",").length > 0) {
                                List<String> authorsList = new ArrayList<String>();
                                for (String a : authors.split(",")) {
                                    authorsList.add(a.trim());
                                }
                                c.setAuthors(authorsList);
                            }
                            if (td.childNode(4).childNode(0).childNodes().size() > 0) {
                                String publisher = td.childNode(4).childNode(0).childNode(0).toString();
                                c.setPublishInfo(publisher);
                            }
                            if (td.childNode(4).childNodes().size() > 1) {
                                String year = td.childNode(4).childNode(1).toString();
                                if (year != null) {
                                    year = year.replace(".", "").replace(" ", "").trim();
                                    try {
                                        c.setYear(Integer.parseInt(year));
                                    } catch (Exception e) {
                                        log.warn(e);
                                        //try to fix
                                        if (year.length() > 4) {
                                            try {
                                                c.setYear(Integer.parseInt(year.substring(0, 4)));
                                                year = year.substring(4, year.length() - 1);
                                                log.warn("NEW YEAR=" + year);
                                                if (year.contains("С") && year.split("С") != null && year.split("С").length == 2) {
                                                    c.setNumber(year.split("С")[0]);
                                                    String pages = year.split("С")[1];
                                                    if (pages.split("-") != null && pages.split("-").length == 2) {
                                                        try {
                                                            c.setStartPage(Integer.parseInt(pages.split("-")[0]));
                                                            c.setStartPage(Integer.parseInt(pages.split("-")[1]));
                                                        } catch (Exception e2) {
                                                            log.warn(e2);
                                                        }
                                                    } else {
                                                        try {
                                                            c.setStartPage(Integer.parseInt(pages));
                                                        } catch (Exception e2) {
                                                            log.warn(e2);
                                                        }
                                                    }
                                                }
                                            } catch (Exception e1) {
                                                log.warn(e1);
                                            }
                                        }
                                    }

                                }
                            }
                            if (td.childNode(4).childNodes().size() > 2) {
                                String number = td.childNode(4).childNode(2).childNode(0).toString().replace("&nbsp;", "");
                                c.setNumber(number);
                            }
                            if (td.childNode(4).childNodes().size() > 3) {
                                String pages = td.childNode(4).childNode(3).toString().replace("&nbsp;", "");
                                if (pages != null) {
                                    pages = pages.replace(".", "").replace(" ", "").replace("С", "").trim();
                                    log.warn(pages);
                                    if (pages.split("-") != null && pages.split("-").length > 0) {
                                        try {
                                            c.setStartPage(Integer.parseInt(pages.split("-")[0]));
                                        } catch (Exception e) {
                                            log.warn(e);
                                        }
                                        try {
                                            c.setEndPage(Integer.parseInt(pages.split("-")[1]));
                                        } catch (Exception e) {
                                            log.warn(e);
                                        }
                                    }
                                }
                            }
                            citationList.add(c);
                        } catch (Exception e) {
                            log.error("can't parse!");
                        }
                    }
                }
            filename = this.savetoFile(citationList, userShortName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getShortUserName(String name) {
        StringBuilder sb = new StringBuilder();
        if (name.split(" ") != null && name.split(" ").length > 0) {
            String names[] = name.split(" ");
            if (names.length > 0) {
                sb.append(names[0]);
            }
            if (names.length > 1) {
                sb.append(" ");
                sb.append(String.valueOf(names[1].charAt(0)).toUpperCase());
                sb.append(".");
            }
            if (names.length > 2) {
                sb.append(String.valueOf(names[2].charAt(0)).toUpperCase());
                sb.append(".");
            }
        }
        return sb.toString();
    }

}
