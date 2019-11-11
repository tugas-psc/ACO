/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antcolonyoptimization;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author DAVIP
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<City> cities = new ArrayList<>();
        System.out.println("Selamat datang di aplikasi simulasi Ant Colony Optimization untuk Euclidean Travelling Salesman Problem");
        System.out.println("Apakah anda ingin memasukan parameter ACO sendiri?");
        System.out.println("Masukkan Y jika ya atau N jika tidak.");
        String input = sc.next();
        if (input.equals("Y")) {
            System.out.println("=======================================================================");
            System.out.println("Masukkan nilai trail original pada saat program dimulai : ");
            double trails = sc.nextDouble();
            System.out.println("Untuk hasil yang baik, masukkan nilai alpha < beta.");
            System.out.println("Masukkan nilai alpha (nilai kepentingan pheromone) : ");
            double alpha = sc.nextDouble();
            System.out.println("Masukkan nilai beta (nilai prioritas jarak) : ");
            double beta = sc.nextDouble();
            System.out.println("Masukkan nilai koefisien penguapan pheromone : ");
            double evaporationFactor = sc.nextDouble();
            System.out.println("Masukkan nilai jumlah pheromone yang ditinggalkan semut di jalur : ");
            double q = sc.nextDouble();
            System.out.println("Masukkan nilai jumlah semut per kota : ");
            double antFactor = sc.nextDouble();
            System.out.println("Masukkan nilai faktor random : ");
            double randomFactor = sc.nextDouble();
            System.out.println("Masukkan jumlah iterasi maksimal program ini : ");
            int numberOfIterations = sc.nextInt();
            System.out.println("Masukkan id kota : ");
            String id = sc.next();
            while (!id.equals("EOF")) {
                System.out.println("Masukkan posisi x kota pada peta : ");
                int x = sc.nextInt();
                System.out.println("Masukkan posisi y kota pada peta : ");
                int y = sc.nextInt();
                cities.add(new City(id, x, y));
                System.out.println("Masukkan id kota selanjutnya : ");
                System.out.println("Jika tidak ada kota selanjutnya masukkan EOF.");
                id = sc.next();
            }
            AntColonyOptimization aco = new AntColonyOptimization(trails, alpha, beta, evaporationFactor, q, antFactor, randomFactor, numberOfIterations, cities);
            aco.startAntOptimization();
        } else {
            System.out.println("Masukkan id kota : ");
            String id = sc.next();
            while (!id.equals("EOF")) {
                System.out.println("Masukkan posisi x kota pada peta : ");
                int x = sc.nextInt();
                System.out.println("Masukkan posisi y kota pada peta : ");
                int y = sc.nextInt();
                cities.add(new City(id, x, y));
                System.out.println("Masukkan id kota selanjutnya : ");
                System.out.println("Jika tidak ada kota selanjutnya masukkan EOF.");
                id = sc.next();
            }
            AntColonyOptimization aco = new AntColonyOptimization(cities);
            aco.startAntOptimization();
        }
    }
}
