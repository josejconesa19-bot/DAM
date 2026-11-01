import java.util.Scanner;

public class SkullKingCreditos {
    // Las cantidades se guardan en decimas: 10 equivale a un credito.
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("1: partida de un equipo / 2: calcular una ronda / 3: cierre");
        int opcion = leerEntero(entrada, "Opcion", 1, 3);
        if (opcion == 1) {
            int saldo = 0;
            for (int ronda = 1; ronda <= 10; ronda++) {
                saldo = registrarRonda(entrada, ronda, saldo);
            }
            cerrarPartida(entrada, saldo);
        } else if (opcion == 2) {
            int ronda = leerEntero(entrada, "Ronda", 1, 10);
            int saldo = leerEntero(entrada, "Saldo anterior en decimas", 0, 200);
            registrarRonda(entrada, ronda, saldo);
        } else {
            int saldo = leerEntero(entrada, "Saldo tras la ronda 10, en decimas", 0, 200);
            cerrarPartida(entrada, saldo);
        }
        entrada.close();
    }

    public static int registrarRonda(Scanner entrada, int ronda, int saldo) {
        System.out.println("\nRONDA " + ronda);
        int apuesta = leerEntero(entrada, "Apuesta del equipo", 0, ronda);
        int ganadas = leerEntero(entrada, "Bazas ganadas", 0, ronda);
        int colores = leerEntero(entrada,
                "Bazas ganadas con colores (no negras ni personajes)", 0, ganadas);
        boolean pirata = false;
        boolean skull = false;
        boolean sirena = false;
        boolean capturaSkull = false;
        if (ganadas > colores) {
            pirata = leerSiNo(entrada, "Gano una baza con pirata o Tigresa como pirata");
            skull = leerSiNo(entrada, "Gano con Skull King capturando un pirata rival");
            sirena = leerSiNo(entrada, "Gano una baza con sirena");
            if (sirena) {
                capturaSkull = leerSiNo(entrada, "Capturo Skull King con sirena");
            }
        }
        boolean kraken = leerSiNo(entrada, "Jugo Kraken y su efecto destruyo la baza");
        boolean ballena = leerSiNo(entrada,
                "Jugo ballena y su efecto anulo un pirata, sirena o Skull King rival");
        boolean huida = leerSiNo(entrada,
                "Uso Tigresa como huida y NO gano esa baza");

        int base = calcularBase(ronda, apuesta, ganadas);
        int logro = calcularLogro(ronda, apuesta, ganadas, colores,
                pirata, skull, sirena, capturaSkull, kraken, ballena, huida);
        int cambio = base + logro;
        int nuevoSaldo = actualizarSaldo(saldo, cambio);
        System.out.println("Base: " + base / 10.0);
        System.out.println("Mejor logro: " + logro / 10.0);
        System.out.println("Cambio calculado: " + cambio / 10.0);
        System.out.println("Cambio efectivo: " + (nuevoSaldo - saldo) / 10.0);
        System.out.println("Saldo: " + nuevoSaldo / 10.0
                + " creditos (" + nuevoSaldo + " decimas)");
        return nuevoSaldo;
    }

    public static int diferencia(int apuesta, int ganadas) {
        int resultado = ganadas - apuesta;
        if (resultado < 0) {
            resultado = -resultado;
        }
        return resultado;
    }

    public static int calcularBase(int ronda, int apuesta, int ganadas) {
        int distancia = diferencia(apuesta, ganadas);
        if (distancia == 1) {
            return -2;
        }
        if (distancia >= 2) {
            return -4;
        }
        if (apuesta == 0) {
            if (ronda <= 3) {
                return 0;
            }
            return 2;
        }
        if (ronda <= 3) {
            return 2;
        } else if (ronda <= 7) {
            return 4;
        } else {
            return 6;
        }
    }

    public static int calcularLogro(int ronda, int apuesta, int ganadas,
            int colores, boolean pirata, boolean skull, boolean sirena,
            boolean capturaSkull, boolean kraken, boolean ballena, boolean huida) {
        if (diferencia(apuesta, ganadas) > 1) {
            return 0;
        }
        int premio = 0;
        if (pirata || sirena || kraken || ballena || huida) {
            premio = 2;
        }
        if (skull) {
            premio = 3;
        }
        if ((ganadas > 0 && colores == ganadas) || capturaSkull) {
            premio = 4;
        }
        if (apuesta == 0 && ganadas == 0) {
            int premioCero = 0;
            if (ronda >= 8) {
                premioCero = 4;
            } else if (ronda >= 4) {
                premioCero = 2;
            }
            if (premioCero > premio) {
                premio = premioCero;
            }
        }
        return premio;
    }

    public static int actualizarSaldo(int saldo, int cambio) {
        saldo = saldo + cambio;
        if (saldo < 0) {
            saldo = 0;
        }
        return saldo;
    }

    public static int premioPosicion(int posicion) {
        if (posicion == 1) {
            return 6;
        } else if (posicion == 2) {
            return 4;
        } else if (posicion == 3) {
            return 2;
        }
        return 0;
    }

    public static int calcularFinal(int saldo, int posicion, boolean participa) {
        saldo = saldo + premioPosicion(posicion);
        if (participa) {
            saldo = saldo + 4;
        }
        if (saldo > 50) {
            saldo = 50;
        }
        return saldo;
    }

    public static void cerrarPartida(Scanner entrada, int saldo) {
        int posicion = leerEntero(entrada, "Posicion segun puntos del juego", 1, 5);
        boolean participa = leerSiNo(entrada,
                "Registro apuestas y calculos durante toda la partida");
        int total = calcularFinal(saldo, posicion, participa);
        System.out.println("Creditos finales: " + total / 10.0);
        System.out.println("Respuestas por integrante: " + total / 10);
    }

    public static boolean leerSiNo(Scanner entrada, String mensaje) {
        return leerEntero(entrada, mensaje + " (1=si, 0=no)", 0, 1) == 1;
    }

    public static int leerEntero(Scanner entrada, String mensaje, int minimo, int maximo) {
        System.out.print(mensaje + ": ");
        while (true) {
            if (entrada.hasNextInt()) {
                int numero = entrada.nextInt();
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }
            } else {
                entrada.next();
            }
            System.out.print("Introduce un entero entre " + minimo + " y " + maximo + ": ");
        }
    }
}
