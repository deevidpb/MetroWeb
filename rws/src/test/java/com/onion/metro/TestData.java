package com.onion.metro;

import com.onion.metro.model.Arrival;
import com.onion.metro.model.Station;

import java.util.List;
import java.util.Map;

public class TestData {
    public static List<Arrival> getMockArrivals(){
        Arrival arrival = new Arrival("linea", "dest", "time", "status");
        return List.of(arrival);
    }

    public static List<Station> getMockStations(){
        Station station = new Station("id", "name", List.of("linea1", "linea2"), false);
        return List.of(station);
    }

    public static Map<String, List<String>> getMockLines(){
        return Map.of("L1", List.of("581", "582"));
    }

    public static String getMockHtml(){
        return """
                <div class="next-trains-last-update"><span>Última actualización: </span>17:53:00</div>
                
                <div class="row">
                    <div class="small-12 columns">
                        <div class="row box__actualizar">
                            <div class="small-9 medium-6 large-6 columns">
                                <p class="text__info-estacion"><strong>Próximos trenes</strong></p>
                            </div>
                
                            <div class="small-3 medium-6 large-6 columns text-right">
                                <p class="text__info-estacion text__info-estacion--actualizar spin">
                                    <a
                                        href="/es/metro_next_trains/modal/617"
                                        class="use-ajax button button--small button--proxTren button--proxTren-act"
                                        data-dialog-type="modal"
                                        data-dialog-options='{"width":400}'
                                        title="Recargar próximos trenes"
                                    >
                                        <span>Actualizar</span>
                                    </a>
                                </p>
                            </div>
                        </div>
                
                        <div class="row">
                            <div class="small-12 columns">
                                <div class="box__info-linea--estaciones">
                                    <div class="row">
                                        <div class="small-12 medium-12 large-6 columns">
                                            <!-- Fake arrival: no icon, destination or wait -->
                                        </div>
                
                                        <div class="small-12 medium-12 large-6 columns">
                                            <div class="box__icon">
                                                <img
                                                    src="/themes/custom/buson8/images/icons-line/linea-7.svg"
                                                    alt="icono linea-7"
                                                    class="linea-7"
                                                />
                                            </div>
                                            <div class="text__info-estacion text__info-estacion--tit-icon">
                                                <p>
                                                    <span class="tiempo-espera__destino color__linea-7"></span>
                                                    <span class="arrow-destino color__linea-7"></span>
                                                    <span class="tiempo-espera__destino color__linea-7">Dirección Pitis</span>
                                                </p>
                                                <p>
                                                    <span class="tiempo-espera__minutos"> <strong>1º tren: </strong>2 min </span>
                                                </p>
                                            </div>
                                        </div>
                                        <div class="small-12 medium-12 large-6 columns">
                                            <div class="box__icon">
                                                <img
                                                    src="/themes/custom/buson8/images/icons-line/linea-7.svg"
                                                    alt="icono linea-7"
                                                    class="linea-7"
                                                />
                                            </div>
                                            <div class="text__info-estacion text__info-estacion--tit-icon">
                                                <p>
                                                    <span class="tiempo-espera__destino color__linea-7"></span>
                                                    <span class="arrow-destino color__linea-7"></span>
                                                    <span class="tiempo-espera__destino color__linea-7"
                                                        >Dirección Hospital del Henares</span
                                                    >
                                                </p>
                                                <p>
                                                    <span class="tiempo-espera__minutos">
                                                        <strong>1º tren: </strong>A la espera de previsión
                                                    </span>
                                                </p>
                                            </div>
                                        </div>
                                        <div class="small-12 medium-12 large-6 columns">
                                                <div class="box__icon">
                                                    <img
                                                        src="/themes/custom/buson8/images/icons-line/linea-7.svg"
                                                        alt="icono linea-7"
                                                        class="linea-7">
                                                </div>
                
                                                <div class="text__info-estacion text__info-estacion--tit-icon">
                                                    <p>
                                                        <span class="tiempo-espera__destino color__linea-7"></span>
                                                        <span class="arrow-destino color__linea-7"></span>
                                                        <span class="tiempo-espera__destino color__linea-7">
                                                            Dirección Pitis
                                                        </span>
                                                    </p>
                                                    <!-- NO ponemos tiempo-espera__minutos -->
                                                </div>
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                """;
    }

    public static String getMockHtml2(){
        return """
    <div class="next-trains-last-update">
    <span>Última actualización: </span>19:31:50
  </div>
  <div class="row">
    <div class="small-12 columns">
      <div class="row box__actualizar">
        <div class="small-9 medium-6 large-6 columns">
          <p class="text__info-estacion"><strong>Próximos trenes</strong></p>
        </div>
        <div class="small-3 medium-6 large-6 columns text-right">
          <p class="text__info-estacion text__info-estacion--actualizar spin">
              <a href="/es/metro_next_trains/modal/592" class="use-ajax button button--small button--proxTren button--proxTren-act" data-dialog-type="modal" data-dialog-options="{&quot;width&quot;:400}" title="Recargar próximos trenes">
              <span>Actualizar</span>
            </a>
          </p>
        </div>
      </div>
      <div class="row">
        <div class="small-12 columns">
          <div class="box__info-linea--estaciones">
                        <div class="row">
                                            <div class="small-12 columns">
                  <div class="box__icon">
                     <img src="/themes/custom/buson8/images/icons-line/ramal.svg" alt="icono ramal" class="ramal">
                  </div>
                  <p>Actualmente sin previsión</p>
                </div>
                          </div>
                        <div class="row">
                                            <div class="small-12 medium-12 large-6 columns">
                                                        <div class="box__icon">
                      <img src="/themes/custom/buson8/images/icons-line/linea-6-circular.svg" alt="icono linea-6-circular" class="linea-6-circular">
                    </div>
                    <div class="text__info-estacion text__info-estacion--tit-icon">
                      <p>
                        <span class="tiempo-espera__destino color__linea-6-circular"></span>
                        <span class="arrow-destino color__linea-6-circular"></span>
                        <span class="tiempo-espera__destino color__linea-6-circular">Dirección Lucero - Andén 1</span>
                      </p>
                      <p>
                        <span class="tiempo-espera__minutos">
                          <strong>1º tren: </strong>A la espera de previsión
                        </span>
                                              </p>
                    </div>
                                  </div>
                <div class="small-12 medium-12 large-6 columns">
                                                        <div class="box__icon">
                      <img src="/themes/custom/buson8/images/icons-line/linea-6-circular.svg" alt="icono linea-6-circular" class="linea-6-circular">
                    </div>
                    <div class="text__info-estacion text__info-estacion--tit-icon">
                      <p>
                        <span class="tiempo-espera__destino color__linea-6-circular"></span>
                        <span class="arrow-destino color__linea-6-circular"></span>
                        <span class="tiempo-espera__destino color__linea-6-circular">Dirección Laguna - Andén 2</span>
                      </p>
                      <p>
                        <span class="tiempo-espera__minutos">
                          <strong>1º tren: </strong>5 min
                        </span>
                                              </p>
                    </div>
                                  </div>
                          </div>
                        <div class="row">
                                            <div class="small-12 medium-12 large-6 columns">
                                                        <div class="box__icon">
                      <img src="/themes/custom/buson8/images/icons-line/linea-10.svg" alt="icono linea-10" class="linea-10">
                    </div>
                    <div class="text__info-estacion text__info-estacion--tit-icon">
                      <p>
                        <span class="tiempo-espera__destino color__linea-10"></span>
                        <span class="arrow-destino color__linea-10"></span>
                        <span class="tiempo-espera__destino color__linea-10">Dirección Puerta del Sur</span>
                      </p>
                      <p>
                        <span class="tiempo-espera__minutos">
                          <strong>1º tren: </strong>4 min
                        </span>
                                              </p>
                    </div>
                                  </div>
                <div class="small-12 medium-12 large-6 columns">
                                                        <div class="box__icon">
                      <img src="/themes/custom/buson8/images/icons-line/linea-10.svg" alt="icono linea-10" class="linea-10">
                    </div>
                    <div class="text__info-estacion text__info-estacion--tit-icon">
                      <p>
                        <span class="tiempo-espera__destino color__linea-10"></span>
                        <span class="arrow-destino color__linea-10"></span>
                        <span class="tiempo-espera__destino color__linea-10">Dirección Hospital Infanta Sofía</span>
                      </p>
                      <p>
                        <span class="tiempo-espera__minutos">
                          <strong>1º tren: </strong>Tren va a entrar en estación
                        </span>
                                              </p>
                    </div>
                                  </div>
                          </div>
                      </div>
        </div>
      </div>
    </div>
  </div>
  """;
    }

}
