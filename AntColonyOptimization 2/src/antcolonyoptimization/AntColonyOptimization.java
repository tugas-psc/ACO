package antcolonyoptimization;


import antcolonyoptimization.Ant;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

/*
 * default
 * private double c = 1.0;             //number of trails
 * private double alpha = 1;           //pheromone importance
 * private double beta = 5;            //distance priority
 * private double evaporation = 0.5;
 * private double Q = 500;             //pheromone left on trail per ant
 * private double antFactor = 0.8;     //no of ants per node
 * private double randomFactor = 0.01; //introducing randomness
 * private int maxIterations = 1000;
 */
//1.0 1 5 0.5 500 0.8 0.01 1000
public class AntColonyOptimization {

    public String s = "";
    private double c;             //number of trails
    private double alpha;           //pheromone importance
    private double beta;            //distance priority
    private double evaporation;
    private double Q;             //pheromone left on trail per ant
    private double antFactor;     //no of ants per node
    private double randomFactor; //introducing randomness

    private int maxIterations;

    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private double trails[][];
    private List<Ant> ants = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private Random random = new Random();
    private double probabilities[];

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;
    private DecimalFormat df;
    /**
     * Constructor dengan atribut yang sudah diset nilai defaultnya.
     * @param cities : List kota yang akan dicari solusinya.
     */
    public AntColonyOptimization(List<City> cities){
        c = 1.0;
        alpha = 1;
        beta = 5;
        evaporation = 0.5;
        Q = 500;
        antFactor = 0.8;
        randomFactor = 0.01;
        maxIterations = 1000;
        this.cities.addAll(cities);
        df = new DecimalFormat("#.##");
        graph = generateDistanceBetweenCities(cities.size());
        numberOfCities = cities.size();
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];

        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(numberOfCities));
        }
    }
    /**
     * Constructor dengan parameter yang menerima input sesuai keinginan user
     * @param tr : jumlah trails original
     * @param al : nilai kepentingan pheromone
     * @param be : nilai prioritas jarak
     * @param ev : koefisien penguapan pheromone
     * @param q : jumlah pheromone yang ditinggalkan semut di jalur yang dilewati
     * @param af : banyaknya semut yang digunakan per kota
     * @param rf : faktor random
     * @param iter : jumlah iterasi program maksimal
     * @param cities : List kota yang akan dicari solusinya.
     */
    public AntColonyOptimization(double tr, double al, double be, double ev,
            double q, double af, double rf, int iter,List<City> cities) {
        c = tr;
        alpha = al;
        beta = be;
        evaporation = ev;
        Q = q;
        antFactor = af;
        randomFactor = rf;
        maxIterations = iter;
        this.cities.addAll(cities);
        df = new DecimalFormat("#.##");
        graph = generateDistanceBetweenCities(cities.size());
        numberOfCities = cities.size();
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];

        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(numberOfCities));
        }
    }
    
    public String printCities(){
        String res = "";
        for(int i=0;i<numberOfCities;i++){
            res += cities.get(i).getId()+" ";
        }
        return res;
    }
    
    public void addCity(City newCity){
        this.cities.add(newCity);
    }
    /**
     * Menghitung jarak dari masing-masing kota ke semua kota lainnya.
     */
    public double[][] generateDistanceBetweenCities(int n) {
        double[][] cityMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    cityMatrix[i][j] = 0;
                } else {
                    double distance = calculateDistance(cities.get(i), cities.get(j));
                    cityMatrix[i][j] = Double.valueOf(df.format(distance));
                    cityMatrix[j][i] = cityMatrix[i][j];
                }
            }
        }

//        s += ("\t");
//        for (int i = 0; i < n; i++) {
//            s += (cities.get(i).getId() + "\t");
//        }
//        s += "\n";
//
//        for (int i = 0; i < numberOfCities; i++) {
//            s += (cities.get(i).getId() + "\t");
//            for (int j = 0; j < numberOfCities; j++) {
//                s += (cityMatrix[i][j] + "\t");
//            }
//            s += "\n";
//        }
//
//        int sum = 0;
////
//        for (int i = 0; i < numberOfCities -1; i++) {
//            sum += cityMatrix[i][i + 1];
//        }
//        sum += cityMatrix[numberOfCities -1 ][0];
//        s += ("\nNaive solution 0-1-2-...-n-0 = " + sum + "\n");
        return cityMatrix;
    }
    /**
     * Menghitung jarak garis lurus antara 2 kota
     * @param c1 : kota 1 
     * @param c2 : kota 2
     * @return jarak garis lurus antara kota c1 dengan kota c2
     */
    public double calculateDistance(City c1, City c2) {
        double x = Math.pow(c2.getX() - c1.getX(), 2);
        double y = Math.pow(c2.getY() - c1.getY(), 2);
        double res = Math.sqrt(x + y);
        return res;
    }

    /**
     * Method untuk memulai Ant Colony Optimization
     */
    public void startAntOptimization() {
        for (int i = 1; i <= 5; i++) {
            s += ("\nAttempt #" + i);
            solve();
            s += "\n";
        }
        System.out.println(s);
    }

    /**
     * Method untuk menjalankan simulasi
     */
    public int[] solve() {
        setupAnts();
        clearTrails();
        for (int i = 0; i < maxIterations; i++) {
            moveAnts();
            updateTrails();
            updateBest();
        }
        double bestTourLengthFormatted = bestTourLength - numberOfCities;
        s += ("\nBest tour length: " + (Double.valueOf(df.format(bestTourLengthFormatted))));
        s += ("\nBest tour order: " + Arrays.toString(bestTourOrder));
        return bestTourOrder.clone();
    }

    /**
     * Menyiapkan semut untuk simulasi
     */
    private void setupAnts() {
        for (int i = 0; i < numberOfAnts; i++) {
            for (Ant ant : ants) {
                ant.clear();
                ant.visitCity(-1, random.nextInt(numberOfCities));
            }
        }
        currentIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        for (int i = currentIndex; i < numberOfCities - 1; i++) {
            for (Ant ant : ants) {
                ant.visitCity(currentIndex, selectNextCity(ant));
            }
            currentIndex++;
        }
    }

    /**
     * Method untuk memilih kota selanjutnya yang akan didatangi oleh semut
     * @param ant : semut yang akan pergi ke kota selanjutnya
     * @return index kota selanjutnya yang akan dikunjungi semut
     */
    private int selectNextCity(Ant ant) {
        int t = random.nextInt(numberOfCities - currentIndex);
        if (random.nextDouble() < randomFactor) {
            int cityIndex = -999;
            for (int i = 0; i < numberOfCities; i++) {
                if (i == t && !ant.visited(i)) {
                    cityIndex = i;
                    break;
                }
            }
            if (cityIndex != -999) {
                return cityIndex;
            }
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }
        return numberOfCities-1;
    }

    /**
     * Menghitung peluang semut mengambil kota selanjutnya
     */
    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) {
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
            }
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * Memperbarui jalur yang digunakan semut
     */
    private void updateTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                trails[i][j] *= evaporation;
            }
        }
        for (Ant a : ants) {
            double contribution = Q / a.trailLength(graph);
            for (int i = 0; i < numberOfCities - 1; i++) {
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            }
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    /**
     * Memperbarui solusi terbaik saat ini
     */
    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0).trailLength(graph);
        }

        for (Ant a : ants) {
            if (a.trailLength(graph) < bestTourLength) {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail.clone();
            }
        }
    }

    /**
     * Membersihkan jalur setelah simulasi
     */
    private void clearTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                trails[i][j] = c;
            }
        }
    }
}
