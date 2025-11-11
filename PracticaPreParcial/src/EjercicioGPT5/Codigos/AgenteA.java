package EjercicioGPT5.Codigos;

import jade.core.Agent;
import jade.core.ContainerID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AgenteA extends Agent {
    private List<String> listaContenedores;
    private String origen;
    private int index=0;
    String path;
    private List<Integer> listaCantLineas;
    private List<Integer> listaCantPalabras;


    @Override
    protected void setup() {
        this.listaCantLineas = new ArrayList<>();
        this.listaCantPalabras = new ArrayList<>();
        //List<String> lista = (List<String>) getArguments()[0];
        //path = getArguments()[1].toString();
        System.out.println("Hola, estoy vivo");
        path = "archivo.txt";
        listaContenedores = List.of("Container-1","Container-2","Container-3");
        this.origen = here().getName();
        System.out.println("Estoy en el contenedor "+origen);
        doMove(new ContainerID(this.listaContenedores.get(index),null));
    }

    @Override
    protected void afterMove() {
        if(!here().getName().equals(origen)){
            try {

                System.out.println("Estoy en el contenedor "+here().getName());
                int cant =0;
                int cantPalabras =0;

                File file = new File(path);
                if(file.exists()){
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String linea;

                    while((linea = br.readLine()) != null){
                        cant+=1;
                        cantPalabras+= linea.length();
                    }
                }
                this.listaCantPalabras.add(cantPalabras);
                this.listaCantLineas.add(cant);

                index+=1;
                if(index < this.listaContenedores.size()){
                    doMove(new ContainerID(this.listaContenedores.get(index),null));
                }else {
                    doMove(new ContainerID(this.origen,null));
                }
            } catch (Exception e) {
                System.out.println("error en el archivo");
            }
        }else{
            System.out.println("Estoy en el origen, los resultados son: ");
            System.out.println("Cant lineas total "+this.listaCantLineas.stream()
                    .mapToInt(Integer::intValue)
                            .sum());
            System.out.println("Cant palabras total "+this.listaCantPalabras.stream()
                    .mapToInt(a -> a.intValue())
                    .sum()
            );;
        }
    }
}
//preguntas teoricas:
/*
¿Qué métodos de JADE permiten implementar la movilidad de un agente?
El metodo es doMove() permite implementar la movilidad.

¿Por qué decimos que JADE implementa movilidad de código y no simplemente comunicación por mensajes?
Porque se transfiere a todo el proceso, esto permite mover el estado del proceso actual, moviendo el valor de las variables consigo


¿Qué diferencia hay entre setup() y afterMove()?
El setUp() se ejecuta solo al principio, en la creaciuon del agente, el aftermove se ejecuta luego de todo do move()
 */