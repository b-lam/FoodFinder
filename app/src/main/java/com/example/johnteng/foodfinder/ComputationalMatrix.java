package com.example.johnteng.foodfinder;


import java.util.HashMap;

/**
 * Created by Brandon on 9/18/2016.
 */
public class ComputationalMatrix {
    private double JsonFactor = 0;

    HashMap<String, Double> priceMap = new HashMap<>();
    HashMap<String, Double> ratingMap = new HashMap<>();
    HashMap<String, Double> reviewsMap = new HashMap<>();
    HashMap<String, Double> radiusMap = new HashMap<>();

    public ComputationalMatrix(){
        populateHashmap(priceMap, 0, 0.25, 0, 0, 0, 0, 0, 0, 0.25, 0, 0, 0.25, 0, 0, 0, 0, 0, 0 ,0 ,0.25, 0, 0);
        populateHashmap(ratingMap, 0, 0, 0, 0, 0, 0, 0, 0, 0.333, 0, 0, 0, 0, 0, 0.333, 0, 0, 0 ,0 ,0.333, 0, 0);
        populateHashmap(reviewsMap, 0, 0, 0.333, 0, 0, 0.333, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0 ,0, 0, 0.333);
        populateHashmap(radiusMap, 0, 0, 0.167, 0, 0.167, 0, 0, 0.167, 0, 0, 0, 0.167, 0, 0, 0, 0.167, 0, 0 ,0 ,0, 0, 0.167);
    }

    public void populateHashmap(HashMap<String, Double> hashmap, double a, double b, double c, double d, double e, double f, double g, double h, double i, double j, double k, double l, double m, double n, double o, double p, double q, double r, double s, double t, double u, double v){
        hashmap.put("Agreeableness", a);
        hashmap.put("Conscientiousness", b);
        hashmap.put("Extraversion", c);
        hashmap.put("Emotional Range", d);
        hashmap.put("Openness", e);
        hashmap.put("Excitement", f);
        hashmap.put("Harmony", g);
        hashmap.put("Curiosity", h);
        hashmap.put("Ideal", i);
        hashmap.put("Closeness", j);
        hashmap.put("Self-expression", k);
        hashmap.put("Liberty", l);
        hashmap.put("Love", m);
        hashmap.put("Practicality", n);
        hashmap.put("Stability", o);
        hashmap.put("Challenge", p);
        hashmap.put("Structure", q);
        hashmap.put("Helping others", r);
        hashmap.put("Tradition", s);
        hashmap.put("Hedonism", t);
        hashmap.put("Achieving success", u);
        hashmap.put("Open to change", v);
    }

    public double returnCalculationFactor(HashMap<String, Double> bb){
        return   bb.get("Agreeableness")*JsonFactor+
                bb.get("Conscientiousness")*JsonFactor+
                bb.get("Extraversion")*JsonFactor+
                bb.get("Emotional Range")*JsonFactor+
                bb.get("Openness")*JsonFactor;

    }

    public double maxGenerator(HashMap<String, Double> bb){
        return   bb.get("Agreeableness")+
                bb.get("Conscientiousness")+
                bb.get("Extraversion")+
                bb.get("Emotional Range")+
                bb.get("Openness");
    }



}
