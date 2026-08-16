package com.onion.metro.utils;

import com.onion.metro.model.Arrival;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class TimesParse {

    private TimesParse() {
    }

    public static List<Arrival> parseArrivals(String html) {
        Document document = Jsoup.parse(html);
        List<Arrival> arrivals = new ArrayList<>();

        for (Element station : document.select(
                ".box__info-linea--estaciones .row > div")) {

            Element icon = station.selectFirst(".box__icon img");
            Element destination = station.selectFirst(".tiempo-espera__destino:last-of-type");
            Element wait = station.selectFirst(".tiempo-espera__minutos");

            if (icon == null || destination == null || wait == null) {
                if(icon != null){
                    String line = changeLine(icon.attr("alt")
                            .replace("icono ", "")
                            .trim());
                    arrivals.add(new Arrival(line, "", "A la espera de previsión", "unavailable"));
                }
                continue;
            }

            String line = changeLine(icon.attr("alt")
                    .replace("icono ", "")
                    .trim());

            String destinationText = destination.text()
                    .replace("Dirección ", "")
                    .trim();

            String waitText = wait.text()
                    .replace("1º tren:", "")
                    .trim();

            String status = switch (waitText) {
                case "Tren va a entrar en estación" -> "entering";
                case "A la espera de previsión" -> "unavailable";
                default -> "normal";
            };

            arrivals.add(new Arrival(
                    line,
                    destinationText,
                    waitText,
                    status
            ));
        }
        return arrivals;
    }

    public static String changeLine(String line) {
        return switch (line) {
            case "linea-1" -> "Línea 1";
            case "linea-2" -> "Línea 2";
            case "linea-3" -> "Línea 3";
            case "linea-4" -> "Línea 4";
            case "linea-5" -> "Línea 5";
            case "linea-6-circular" -> "Línea 6";
            case "linea-7" -> "Línea 7";
            case "linea-8" -> "Línea 8";
            case "linea-9" -> "Línea 9";
            case "linea-10" -> "Línea 10";
            case "linea-11" -> "Línea 11";
            case "linea-12-metrosur" -> "Línea 12";
            case "ramal" -> "Ramal";
            case "ml1" -> "ML1";
            default -> line;
        };
    }
}
