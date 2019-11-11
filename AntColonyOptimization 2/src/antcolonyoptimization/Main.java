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
//        double trails = sc.nextDouble();
//        double alpha = sc.nextDouble();
//        double beta = sc.nextDouble();
//        double evaporationFactor = sc.nextDouble();
//        double q = sc.nextDouble();
//        double antFactor = sc.nextDouble();
//        double randomFactor = sc.nextDouble();
//        int numberOfIterations = sc.nextInt();
        double trails = 1.0;
        double alpha = 1;
        double beta = 5;
        double evaporationFactor = 0.5;
        double q = 500;
        double antFactor = 0.8;
        double randomFactor = 0.01;
        int numberOfIterations = 1000;
        System.out.println("num of city : /n");
        int numberOfCities = sc.nextInt();
        List<City> cities = new ArrayList<>();
        
        String id = sc.next();
        while(!id.equals("EOF")){
            cities.add(new City(id,sc.nextInt(),sc.nextInt()));
            id = sc.next();
        }
        AntColonyOptimization aco = new AntColonyOptimization(trails,alpha,beta,evaporationFactor,q,antFactor,randomFactor,numberOfIterations,numberOfCities,cities);
        System.out.println(aco.printCities());
        aco.generateDistanceBetweenCities(numberOfCities);
        System.out.println("lala");
        aco.startAntOptimization();
    }
}
