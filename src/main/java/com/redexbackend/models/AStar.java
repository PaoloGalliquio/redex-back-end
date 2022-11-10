package com.redexbackend.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {

    public static int obtenerTiempo(Vuelo vuelo) {
        int duracion = 0;

        int UTCSalida = vuelo.getAeropuertoPartida().getHusoHorario();
        int UTCLlegada = vuelo.getAeropuertoDestino().getHusoHorario();

        // Odio las fechas

        Calendar fechaPartida = GregorianCalendar.getInstance();
        Calendar fechaLlegada = GregorianCalendar.getInstance();
        fechaPartida.setTime(vuelo.getFechaPartida());
        fechaLlegada.setTime(vuelo.getFechaDestino());

        int hSalida = fechaPartida.get(Calendar.HOUR_OF_DAY) - UTCSalida;
        int mSalida = fechaPartida.get(Calendar.MINUTE);

        int hLlegada = fechaLlegada.get(Calendar.HOUR_OF_DAY) - UTCLlegada;
        int mLlegada = fechaLlegada.get(Calendar.MINUTE);

        if (UTCLlegada < UTCSalida)
            duracion = (24 + hLlegada - hSalida) * 60 + (mLlegada - mSalida);
        else
            duracion = (hLlegada - hSalida) * 60 + (mLlegada - mSalida);

        if (duracion < 0)
            duracion += 24 * 60;

        return duracion;
    }

    public static int seCruzan(Vuelo vueloEnLista, Vuelo nuevoVuelo) {
        Calendar hSalida = Calendar.getInstance();
        Calendar hLlegada = Calendar.getInstance();
        int UTCSalida = vueloEnLista.getAeropuertoDestino().getHusoHorario();
        int UTCLlegada = nuevoVuelo.getAeropuertoPartida().getHusoHorario();
        long difFechas, difHoras, difMin, difDias;

        hSalida.setTime(vueloEnLista.getFechaDestino());
        hSalida.add(Calendar.HOUR_OF_DAY, 1); // agregar el tiempo de espera de 1 hora entre escalas
        hSalida.add(Calendar.HOUR_OF_DAY, -(UTCSalida)); // mover a un mismo formato de fecha

        hLlegada.setTime(nuevoVuelo.getFechaPartida());
        hLlegada.add(Calendar.HOUR_OF_DAY, -(UTCLlegada)); // mover a un mismo formato de fecha

        if (hSalida.getTime().compareTo(hLlegada.getTime()) <= 0) {
            // No hay cruce entre los vuelos
            hSalida.add(Calendar.HOUR_OF_DAY, -1); // quitar la hora extra entre escalas, eso forma parte del tiempo
                                                   // intermedio
            difFechas = hLlegada.getTime().getTime() - hSalida.getTime().getTime();
            difMin = (difFechas / (1000 * 60)) % 60;
            difHoras = (difFechas / (1000 * 60 * 60)) % 24;
            difDias = (difFechas / (1000 * 60 * 60 * 24)) % 365; // el alcance máximo entre diferencia de vuelos llega
                                                                 // hasta días
            // System.out.println(hSalida.getTime() + " ---- " + hLlegada.getTime());

            return (int) difDias * 24 * 60 + (int) difHoras * 60 + (int) difMin;
        } else {
            // Si hay cruce entre vuelos

            return -1;
        }
    }

    public static boolean sobrepasaCapacidad(Vuelo nuevoVuelo, Integer nroPaquetes) {
        if(nuevoVuelo.getCapacidadActual() < nroPaquetes || nuevoVuelo.getAeropuertoDestino().getCapacidad() < nroPaquetes){
            return true;
        }else{
            return false;
        }
    }

    public static boolean compararFechas(Vuelo vuelo, Date fechaEnvio, Aeropuerto origen){
        
        Calendar hVuelo = Calendar.getInstance();
        Calendar hIngresoEnvio = Calendar.getInstance();

        //Obtenemos las zonas horarias

        int UTCSalida = vuelo.getAeropuertoDestino().getHusoHorario();
        int UTCEnvio = origen.getHusoHorario();
        
        //Igualamos las fechas para volver la comparación más práctica

        hVuelo.setTime(vuelo.getFechaPartida());
        hVuelo.set(2016, 6, 3);
        hVuelo.add(Calendar.HOUR_OF_DAY, -(UTCSalida)); // mover a un mismo formato de fecha

        hIngresoEnvio.setTime(fechaEnvio);
        hIngresoEnvio.set(2016, 6, 3);
        hIngresoEnvio.add(Calendar.HOUR_OF_DAY, -(UTCEnvio));

        boolean resultado = hVuelo.getTime().before(hIngresoEnvio.getTime());

        return resultado;
    }

    public static boolean mismoContinente (Aeropuerto start, Aeropuerto target){
        char continenteStart, continenteTarget;

        continenteStart = start.getCiudad().getPais().getContinente().getCodigo().charAt(0);
        continenteTarget = target.getCiudad().getPais().getContinente().getCodigo().charAt(0);

        if(continenteStart == continenteTarget)
            return true;
        
        return false;
    }

    public static Aeropuerto aStar(Aeropuerto start, Aeropuerto target, HashMap<String, Integer> timeZones, Date fechaEnvio, Integer nroPaquetes) {
        // Tiempo de prueba

        /*Date fechaPrueba = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 15, 0).getTime();*/

        PriorityQueue<Aeropuerto> closedList = new PriorityQueue<>();
        PriorityQueue<Aeropuerto> openList = new PriorityQueue<>();

        start.f = start.g + start.calculateHeuristic(start, target);
        openList.add(start);

        System.out.println("Solucion A*");
        System.out.println("==============================================");
        System.out.println("Origen: " + start.getCiudad().getNombre() + " " + start.getCodigo() + " (UTC: " + start.getUTC()+ ")");
        System.out.println("Destino: " + target.getCiudad().getNombre() + " " + target.getCodigo() + " (UTC: " + target.getUTC()+ ")");
        System.out.println("Cantidad de paquetes a enviar: " + nroPaquetes);
        System.out.print("Duración: ");

        //Colapso logístico
        if(start.getCapacidad() < nroPaquetes) return null;

        while (!openList.isEmpty()) {
            Aeropuerto n = openList.peek();
            if (n == target) {
                return n;
            }

            for (Vuelo vuelo : n.getVuelos()) {

                //Bloqueamos vuelos que no son locales si el origen y el destino están en el mismo continente
                /*if(local){
                    if(start.getCiudad().getPais().getContinente().getCodigo() != 
                    vuelo.getAeropuertoDestino().getCiudad().getPais().getContinente().getCodigo())continue; 
                }*/

                //Bloqueamos vuelos que ya no tengan capacidad
                if(!vuelo.getDisponible())continue;
                //Bloquemos vuelos que estén al límite de su capacidad
                if(vuelo.getCapacidad() - nroPaquetes < 0)continue;

                //Ignoramos los vuelos que ya hayan salido
                /*int resultadoComp = compararFechas(timeZones, vuelo, fechaEnvio, start);
                if (resultadoComp < 0){
                    continue;
                }*/

                int tiempoIntermedio = 0;
                for (Aeropuerto aero : openList) {
                    if (aero.comoLlegar == null)
                        continue;
                    // comollegar.aerodestino es el hace referencia al aeropuerto actual
                    if (aero.comoLlegar.getAeropuertoDestino().getCodigo()
                            .equals(vuelo.getAeropuertoPartida().getCodigo())) {
                        tiempoIntermedio = seCruzan(aero.comoLlegar, vuelo);
                        break;
                    }
                }
                if (tiempoIntermedio < 0 || sobrepasaCapacidad(vuelo, nroPaquetes)) {
                    continue; // el vuelo actual se cruza con los vuelos ya enlistados o no puede trasladar los paquetes
                }

                int totalWeight = n.g + tiempoIntermedio + obtenerTiempo(vuelo);

                if (!openList.contains(vuelo.getAeropuertoDestino())
                        && !closedList.contains(vuelo.getAeropuertoDestino())) {
                    // Prueba
                    vuelo.getAeropuertoDestino().comoLlegar = vuelo;
                    // Prueba
                    vuelo.getAeropuertoDestino().parent = n;
                    vuelo.getAeropuertoDestino().g = totalWeight;
                    vuelo.getAeropuertoDestino().f = vuelo.getAeropuertoDestino().g
                            + vuelo.getAeropuertoDestino().calculateHeuristic(vuelo.getAeropuertoDestino(), target);
                    openList.add(vuelo.getAeropuertoDestino());
                } else {
                    if (totalWeight < vuelo.getAeropuertoDestino().g) {
                        // Prueba
                        vuelo.getAeropuertoDestino().comoLlegar = vuelo;
                        // Prueba
                        vuelo.getAeropuertoDestino().parent = n;
                        vuelo.getAeropuertoDestino().g = totalWeight;
                        vuelo.getAeropuertoDestino().f = 
                            vuelo.getAeropuertoDestino().g + 
                            vuelo.getAeropuertoDestino().calculateHeuristic(vuelo.getAeropuertoDestino(), target);
                        vuelo.setCapacidadActual(vuelo.getCapacidadActual() - nroPaquetes);

                        if (closedList.contains(vuelo.getAeropuertoDestino())) {
                            closedList.remove(vuelo.getAeropuertoDestino());
                            openList.add(vuelo.getAeropuertoDestino());
                        }
                    }
                }
            }

            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }

    public static void cambiarCapacidades(List<Vuelo> vuelos, Integer nroPaquetes){
        for(int i = 0; i <vuelos.size(); i++){
            vuelos.get(i).setCapacidadActual(vuelos.get(i).getCapacidadActual() - nroPaquetes);
        }
    }

    public static void printPath(Aeropuerto target, Aeropuerto origen, Aeropuerto destino, HashMap<String, Integer> timeZones, Date fechaEnvio, int nroPaquetes) {

        Calendar hEnvio = Calendar.getInstance();
        hEnvio.setTime(fechaEnvio);

        hEnvio.setTime(fechaEnvio);

        Aeropuerto n = target;
        if (n == null)
            return;

        List<String> caminoAeropuertos = new ArrayList<>();
        List<String> caminoVuelos = new ArrayList<>();
        List<Integer> capacidadVuelos = new ArrayList<>();

        //Lista de vuelos
        List<Vuelo> listaVuelos = new ArrayList<>();

        Date primeraSalida = new Date(), ultimaLlegada = new Date();
        boolean primeraVez = true;
        Calendar hPSalida = Calendar.getInstance(), hULlegada = Calendar.getInstance();
        int UTCPSalida=0, UTCULlegada=0;

        while (n.parent != null) {
            if(primeraVez){
                ultimaLlegada = n.comoLlegar.getFechaDestino();
                UTCULlegada = timeZones.get(n.comoLlegar.getAeropuertoDestino().getCodigo());
                primeraVez = false;
                //Se agregan los vuelos a una lista
                listaVuelos.add(n.comoLlegar);
                //Prueba
                capacidadVuelos.add(n.comoLlegar.getCapacidadActual());
            }

            //Se agregan los vuelos a una lista
            listaVuelos.add(n.comoLlegar);

            //Si se llena el vuelo, lo bloqueamos
            if((n.comoLlegar.getCapacidadActual() - nroPaquetes )== 0)n.comoLlegar.setDisponible(false);

            //Prueba
            capacidadVuelos.add(n.comoLlegar.getCapacidadActual());

            caminoAeropuertos.add(n.getCiudad().getCodigo());
            caminoVuelos.add(n.comoLlegar.getCodigo() +
                    ": " + n.comoLlegar.getFechaPartida() + " - " + n.comoLlegar.getFechaDestino());
            primeraSalida = n.comoLlegar.getFechaPartida();
            UTCPSalida = timeZones.get(n.comoLlegar.getAeropuertoPartida().getCodigo());
            
            // if(n.parent != null)minutos += n.parent.getAdjacentNodes().get(n);
            n = n.parent;
        }
        caminoAeropuertos.add(n.getCiudad().getCodigo());
        Collections.reverse(caminoAeropuertos);
        Collections.reverse(caminoVuelos);

        //Prueba
        Collections.reverse(capacidadVuelos);
        Collections.reverse(listaVuelos);

        hPSalida.setTime(primeraSalida);
        hPSalida.add(Calendar.HOUR_OF_DAY, -(UTCPSalida));
        hULlegada.setTime(ultimaLlegada);
        hULlegada.add(Calendar.HOUR_OF_DAY, -(UTCULlegada)+1); //agregar la hora extra del destino final

        if(esMayor(hPSalida.getTime(), hULlegada.getTime(), origen, destino)){
            System.out.println("Supera la ventana de tiempo");
            return;
        }else{//Cambiar las capacidades de los vuelos
            cambiarCapacidades(listaVuelos, nroPaquetes);
        }

        minAHora(fechaEnvio, hULlegada.getTime());
        System.out.println("Inicio del Plan de Vuelo: "+fechaEnvio);
        System.out.println("Fin del Plan de Vuelo:    "+ultimaLlegada);
        System.out.println("==============================================");
        System.out.println("Ruta:");

        for (String id : caminoAeropuertos) {
            System.out.println("    " + id);
        }

        System.out.println("==============================================");
        System.out.println("Ruta vuelos:");
        for (int i = 0; i < caminoVuelos.size(); i++){
            System.out.print("    " + caminoVuelos.get(i));
            System.out.println(" - " + capacidadVuelos.get(i));
        }
        System.out.println("Capacidad aeropuerto final: " + (n.getCapacidad() - nroPaquetes));
        System.out.println("==============================================");
    }

    //Versión que se usa en el Service
    
    public static Aeropuerto aStar(Envio envio) {
        envio.getAeropuertoPartida().g = 0;
        Aeropuerto start = envio.getAeropuertoPartida();
        Aeropuerto target = envio.getAeropuertoDestino();
        int nroPaquetes = envio.getNumeroPaquetes();
        PriorityQueue<Aeropuerto> closedList = new PriorityQueue<>();
        PriorityQueue<Aeropuerto> openList = new PriorityQueue<>();

        start.f = start.g + start.calculateHeuristic(start, target);
        openList.add(start);
        
        if(start.getCapacidad() < nroPaquetes){
            return null;
        }
        
        while (!openList.isEmpty()) {
            Aeropuerto n = openList.peek();
            if(n.getVuelos() == null){
                continue;
            }

            if (n.getCodigo() == target.getCodigo()) {
                return n;
            }

            for (Vuelo vuelo : n.getVuelos()) {
                if(!vuelo.getDisponible())
                    continue;
                if(vuelo.getCapacidad() < nroPaquetes)
                    continue;
                int tiempoIntermedio = 0;
                for (Aeropuerto aero : openList) {
                    if (aero.comoLlegar == null)
                        continue;
                    if (aero.comoLlegar.getAeropuertoDestino().getCodigo().equals(vuelo.getAeropuertoPartida().getCodigo())) {
                        tiempoIntermedio = seCruzan(aero.comoLlegar, vuelo);
                        break;
                    }
                }
                if (tiempoIntermedio < 0 || sobrepasaCapacidad(vuelo, nroPaquetes))
                    continue;

                int totalWeight = n.g + tiempoIntermedio + obtenerTiempo(vuelo);

                if (!openList.contains(vuelo.getAeropuertoDestino()) && !closedList.contains(vuelo.getAeropuertoDestino())) {
                    vuelo.getAeropuertoDestino().comoLlegar = vuelo;
                    vuelo.getAeropuertoDestino().parent = n;
                    vuelo.getAeropuertoDestino().g = totalWeight;
                    vuelo.getAeropuertoDestino().f = vuelo.getAeropuertoDestino().g + vuelo.getAeropuertoDestino().calculateHeuristic(vuelo.getAeropuertoDestino(), target);
                    openList.add(vuelo.getAeropuertoDestino());
                } else {
                    if (totalWeight < vuelo.getAeropuertoDestino().g) {
                        vuelo.getAeropuertoDestino().comoLlegar = vuelo;
                        vuelo.getAeropuertoDestino().parent = n;
                        vuelo.getAeropuertoDestino().g = totalWeight;
                        vuelo.getAeropuertoDestino().f = 
                            vuelo.getAeropuertoDestino().g + 
                            vuelo.getAeropuertoDestino().calculateHeuristic(vuelo.getAeropuertoDestino(), target);
                        //vuelo.setCapacidadActual(vuelo.getCapacidadActual() - nroPaquetes);

                        if (closedList.contains(vuelo.getAeropuertoDestino())) {
                            closedList.remove(vuelo.getAeropuertoDestino());
                            openList.add(vuelo.getAeropuertoDestino());
                        }
                    }
                }
            }
            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }

    public static void obtenerPlanesDeVuelo(Aeropuerto target, Envio envio, Calendar fechaSimu) {
        var origen = envio.getAeropuertoPartida();
        var destino = envio.getAeropuertoDestino();
        var fechaEnvio = envio.getFechaEnvio();
        var nroPaquetes = envio.getNumeroPaquetes();

        Aeropuerto n = target;
        if (n == null){
            return;
        }

        List<Integer> capacidadVuelos = new ArrayList<>();

        List<Vuelo> listaVuelos = new ArrayList<>();

        Date primeraSalida = new Date(), ultimaLlegada = new Date();
        boolean primeraVez = true;
        Calendar hPSalida = Calendar.getInstance(), hULlegada = Calendar.getInstance(), hEnvio = Calendar.getInstance(), hVuelo = Calendar.getInstance();
        int UTCPSalida=0, UTCULlegada=0;

        int diaSimu = fechaSimu.get(Calendar.DAY_OF_MONTH), mesSimu = fechaSimu.get(Calendar.MONTH), aaSimu = fechaSimu.get(Calendar.YEAR);

        while (n.parent != null) {
            if(primeraVez){
                ultimaLlegada = n.comoLlegar.getFechaDestino();
                UTCULlegada = n.comoLlegar.getAeropuertoDestino().getHusoHorario();
                primeraVez = false;
                capacidadVuelos.add(n.comoLlegar.getCapacidadActual());
            }
            listaVuelos.add(n.comoLlegar);
            
            if((n.comoLlegar.getCapacidadActual() - nroPaquetes )== 0)
                n.comoLlegar.setDisponible(false);

            capacidadVuelos.add(n.comoLlegar.getCapacidadActual());

            primeraSalida = n.comoLlegar.getFechaPartida();
            UTCPSalida = n.comoLlegar.getAeropuertoPartida().getHusoHorario();
            n = n.parent;
        }
        Collections.reverse(capacidadVuelos);
        Collections.reverse(listaVuelos);

        hPSalida.setTime(primeraSalida);
        hPSalida.add(Calendar.HOUR_OF_DAY, -(UTCPSalida));
        hULlegada.setTime(ultimaLlegada);
        hULlegada.add(Calendar.HOUR_OF_DAY, -(UTCULlegada)+1);
        hEnvio.setTime(fechaEnvio);
        hEnvio.add(Calendar.HOUR_OF_DAY, -(UTCPSalida));
        hEnvio.set(Calendar.DAY_OF_MONTH, hPSalida.get(Calendar.DAY_OF_MONTH));
        hEnvio.set(Calendar.MONTH, hPSalida.get(Calendar.MONTH));

        if(!esMayor(hEnvio.getTime(), hPSalida.getTime())){
            hEnvio.add(Calendar.HOUR_OF_DAY, 1);
        }

        if(esMayor(hEnvio.getTime(), hULlegada.getTime(), origen, destino)){
            System.out.println(envio.getCodigo() + " se cae");
            return;
        }
        else
            cambiarCapacidades(listaVuelos, envio.getNumeroPaquetes());

        List<PlanDeVuelo> planDeVuelos = new ArrayList<>();
        List<VueloPorPlanDeVuelo> vueloPorPlanDeVuelos = new ArrayList<>();
        PlanDeVuelo planDeVuelo = new PlanDeVuelo();
        String codigo = "";
        int duracion = 0;
        planDeVuelo.setFechaPlan(new Date());
        planDeVuelo.setNumeroPaquetes(nroPaquetes);
        planDeVuelo.setEnvio(envio);

        for (Vuelo v : listaVuelos){
            hVuelo.setTime(v.getFechaPartida());
            hVuelo.set(aaSimu, mesSimu, diaSimu);
            v.setFechaPartida(hVuelo.getTime());
            hVuelo.add(Calendar.MINUTE, v.getDuracion());
            v.setFechaDestino(hVuelo.getTime());
            hVuelo.setTime(v.getFechaPartida());
            hVuelo.add(Calendar.HOUR, -(v.getAeropuertoPartida().getHusoHorario()));
            v.setFechaPartidaUTC0(hVuelo.getTime());
            hVuelo.setTime(v.getFechaDestino());
            hVuelo.add(Calendar.HOUR, -(v.getAeropuertoDestino().getHusoHorario()));
            v.setFechaDestinoUTC0(hVuelo.getTime());
            
            VueloPorPlanDeVuelo vueloPorPlanDeVuelo = new VueloPorPlanDeVuelo();
            codigo += v.getCodigo();
            duracion += v.getDuracion();
            vueloPorPlanDeVuelo.setPlanDeVuelo(planDeVuelo);
            vueloPorPlanDeVuelo.setVuelo(v);
            vueloPorPlanDeVuelo.setFechaVuelo(v.getFechaPartida());
            vueloPorPlanDeVuelos.add(vueloPorPlanDeVuelo);
        }
        planDeVuelo.setVuelosPorPlanDeVuelo(vueloPorPlanDeVuelos);
        planDeVuelo.setCodigo(codigo);
        planDeVuelo.setDuracionTotal(duracion);
        planDeVuelos.add(planDeVuelo);
        envio.setPlanesDeVuelo(planDeVuelos);
    }

    public static boolean esMayor(Date primeraSalida, Date ultimaLlegada, Aeropuerto origen, Aeropuerto destino){
        long min = (Math.abs(ultimaLlegada.getTime() - primeraSalida.getTime()))/60000;
        System.out.println(min);
        if(origen.getCiudad().getPais().getContinente().getCodigo().charAt(0) == destino.getCiudad().getPais().getContinente().getCodigo().charAt(0)){
            if(min > 24*60)
                return true;
            else 
                return false;
        }else{
            if(min > 48*60)
                return true;
            else 
                return false;
        }
    }

    public static boolean esMayor(Date horaEnvio, Date primeraSalida){
        if(horaEnvio.before(primeraSalida)){
            return true;
        }else{
            return false;
        }
        
    }

    public static void minAHora(Date fechaEnvio, Date ultimaLlegada) {

        long min = (Math.abs(ultimaLlegada.getTime() - fechaEnvio.getTime()))/60000;

        int horas = (int)min / 60;
        int minutos = (int)min % 60;

        String minutosString;

        if (minutos < 10) {
            minutosString = "0" + String.valueOf(minutos);
        } else {
            minutosString = String.valueOf(minutos);
        }

        System.out.println(horas + ":" + minutosString + " hrs.");
    }

}