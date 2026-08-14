import requests
from bs4 import BeautifulSoup
import json

def getlinecodes():
    num_lineas = []
    for i in range(1, 12):
        num_lineas.append(f"linea-{i}")

    num_lineas.append(f"linea-12-metrosur")
    num_lineas.append(f"ramal")
    num_lineas.append(f"ml1")

    return num_lineas

def getLineName(linea):
    match linea:
        case "linea-1":
            return "Línea 1"
        case "linea-2":
            return "Línea 2"
        case "linea-3":
            return "Línea 3"
        case "linea-4":
            return "Línea 4"
        case "linea-5":
            return "Línea 5"
        case "linea-6":
            return "Línea 6"
        case "linea-7":
            return "Línea 7"
        case "linea-8":
            return "Línea 8"
        case "linea-9":
            return "Línea 9"
        case "linea-10":
            return "Línea 10"
        case "linea-11":
            return "Línea 11"
        case "linea-12-metrosur":
            return "Línea 12"
        case "ramal":
            return "Ramal"
        case "ml1":
            return "ML1"
        case _:
            return None

def getHtml(linea):
    get_linea = requests.request("GET", f"https://www.metromadrid.es/es/linea/{linea}")
    soup = BeautifulSoup(get_linea.content, "html.parser")
    return soup

def scrapper():
    estaciones = []
    lineas = {}

    for linea_code in getlinecodes():
        linea = []
        for station in getHtml(linea_code).select("a.list-line__btn.accordion-title"):
            name = station.select_one(".list-line__btn__text").get_text(strip=True)
            station_id = station["aria-controls"].removesuffix("-label")
            linea.append(station_id)

            estacion = next(
                (estacion for estacion in estaciones
                if estacion["id"] == station_id), None
            )

            if not estacion:
                station_container = station.find_parent("li")
                accessible = station_container.select_one(
                    "img.icon-discapacitados"
                ) is not None

                station = {
                    "id": station_id,
                    "name": name,
                    "lines":[getLineName(linea_code)],
                    "accesible": accessible
                }
                estaciones.append(station)
            else:
                estacion["lines"].append(getLineName(linea_code))

        lineas[getLineName(linea_code)] = linea
    return estaciones, lineas


estaciones,lineas = scrapper()
with open("rws/src/main/resources/stations.json", "w", encoding="utf-8") as f:
    json.dump(estaciones, f, ensure_ascii=False, indent=2)
with open("rws/src/main/resources/lines.json", "w", encoding="utf-8") as f:
    json.dump(lineas, f, ensure_ascii=False, indent=2)