/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

/**
 * 
 * @author Kathryn Andrew
 */
public class SimConsts {
    
    private static int MAX_LAYERS = 10;
    private static int MAX_NODES_PER_LAYER = 10;
    
    private static int ENV_MIN_SIZE = 100;
    private static int ENV_MAX_SIZE = 3000;
    private static int ENV_MIN_DENSITY = 2;
    private static int ENV_MAX_DENSITY = 10;
    
    private static int START_ENERGY = 600;
    private static int MAX_ENERGY = 600;
    
    private static int FITNESS_WEIGHT_MIN = -10;
    private static int FITNESS_WEIGHT_MAX = 10;    
    
    private static int GENETIC_BOTTLENECK_POPULATION = 10; 

    public static int getMAX_LAYERS() {
        return MAX_LAYERS;
    }

    public static int getMAX_NODES_PER_LAYER() {
        return MAX_NODES_PER_LAYER;
    }

    static void setMAX_LAYERS(int MAX_LAYERS) {
        SimConsts.MAX_LAYERS = MAX_LAYERS;
    }

    static void setMAX_NODES_PER_LAYER(int MAX_NODES_PER_LAYER) {
        SimConsts.MAX_NODES_PER_LAYER = MAX_NODES_PER_LAYER;
    }

    public static int getENV_MIN_SIZE() {
        return ENV_MIN_SIZE;
    }

    static void setENV_MIN_SIZE(int ENV_MIN_SIZE) {
        SimConsts.ENV_MIN_SIZE = ENV_MIN_SIZE;
    }

    public static int getENV_MAX_SIZE() {
        return ENV_MAX_SIZE;
    }

    static void setENV_MAX_SIZE(int ENV_MAX_SIZE) {
        SimConsts.ENV_MAX_SIZE = ENV_MAX_SIZE;
    }

    public static int getENV_MIN_DENSITY() {
        return ENV_MIN_DENSITY;
    }

    static void setENV_MIN_DENSITY(int ENV_MIN_DENSITY) {
        SimConsts.ENV_MIN_DENSITY = ENV_MIN_DENSITY;
    }

    public static int getENV_MAX_DENSITY() {
        return ENV_MAX_DENSITY;
    }

    static void setENV_MAX_DENSITY(int ENV_MAX_DENSITY) {
        SimConsts.ENV_MAX_DENSITY = ENV_MAX_DENSITY;
    }

    public static int getSTART_ENERGY() {
        return START_ENERGY;
    }

    static void setSTART_ENERGY(int START_ENERGY) {
        SimConsts.START_ENERGY = START_ENERGY;
    }

    public static int getMAX_ENERGY() {
        return MAX_ENERGY;
    }

    public static int getFITNESS_WEIGHT_MIN() {
        return FITNESS_WEIGHT_MIN;
    }

    public static int getFITNESS_WEIGHT_MAX() {
        return FITNESS_WEIGHT_MAX;
    }

    public static int getGENETIC_BOTTLENECK_POPULATION() {
        return GENETIC_BOTTLENECK_POPULATION;
    }

    public static void setGENETIC_BOTTLENECK_POPULATION(int GENETIC_BOTTLENECK_POPULATION) {
        SimConsts.GENETIC_BOTTLENECK_POPULATION = GENETIC_BOTTLENECK_POPULATION;
    }
    
    
    
}
