package com.example.johnteng.foodfinder;


import android.util.Log;

import java.util.HashMap;

/**
 * Created by Brandon on 9/18/2016.
 */
public class ComputationalMatrix {

    static public HashMap<String, Double> priceMap = new HashMap<>();
    static public HashMap<String, Double> ratingMap = new HashMap<>();
    static public HashMap<String, Double> reviewsMap = new HashMap<>();
    static public HashMap<String, Double> radiusMap = new HashMap<>();

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

    public double returnCalculationFactor(HashMap<String, Double> hashmap){
        return   hashmap.get("Agreeableness")* PersonalityInsights.agreeableness+
                hashmap.get("Conscientiousness")* PersonalityInsights.conscientiousness+
                hashmap.get("Extraversion")* PersonalityInsights.extraversion+
                hashmap.get("Emotional Range")* PersonalityInsights.emotionalRange+
                hashmap.get("Openness")* PersonalityInsights.openness+
                hashmap.get("Excitement")* PersonalityInsights.excitement+
                hashmap.get("Harmony")* PersonalityInsights.harmony+
                hashmap.get("Curiosity")* PersonalityInsights.curiosity+
                hashmap.get("Ideal")* PersonalityInsights.ideal+
                hashmap.get("Closeness")* PersonalityInsights.closeness+
                hashmap.get("Self-expression")* PersonalityInsights.selfExpression+
                hashmap.get("Liberty")* PersonalityInsights.liberty+
                hashmap.get("Love")* PersonalityInsights.love+
                hashmap.get("Practicality")* PersonalityInsights.practicality+
                hashmap.get("Stability")* PersonalityInsights.stability+
                hashmap.get("Challenge")* PersonalityInsights.challenge+
                hashmap.get("Structure")* PersonalityInsights.structure+
                hashmap.get("Helping others")* PersonalityInsights.helpingOthers+
                hashmap.get("Tradition")* PersonalityInsights.tradition+
                hashmap.get("Hedonism")* PersonalityInsights.hedonism+
                hashmap.get("Achieving success")* PersonalityInsights.achievingSuccess+
                hashmap.get("Open to change")* PersonalityInsights.opennessToChange;

    }

    public double maxGenerator(HashMap<String, Double> hashmap){
        return   hashmap.get("Agreeableness")+
                hashmap.get("Conscientiousness")+
                hashmap.get("Extraversion")+
                hashmap.get("Emotional Range")+
                hashmap.get("Openness")+
                hashmap.get("Excitement")+
                hashmap.get("Harmony")+
                hashmap.get("Curiosity")+
                hashmap.get("Ideal")+
                hashmap.get("Closeness")+
                hashmap.get("Self-expression")+
                hashmap.get("Liberty")+
                hashmap.get("Love")+
                hashmap.get("Practicality")+
                hashmap.get("Stability")+
                hashmap.get("Challenge")+
                hashmap.get("Structure")+
                hashmap.get("Helping others")+
                hashmap.get("Tradition")+
                hashmap.get("Hedonism")+
                hashmap.get("Achieving success")+
                hashmap.get("Open to change");
    }

    public void calculateStandardForPrice(HashMap<String, Double> bb){
        double standard = (returnCalculationFactor(bb)/maxGenerator(bb))*100;
        Log.d("Matrix Price", String.valueOf(standard));
        if(standard>=0 && standard<25){
            BusinessSearch.price = "1,2";
        }

        else if(standard>=25 && standard<50){
            BusinessSearch.price = "1,2,3";
        }

        else if(standard>=50 && standard<100) {
            BusinessSearch.price = "1,2,3,4";
        }
    }

    public void calculateStandardForRadius(HashMap<String, Double> bb){
        double standard = (returnCalculationFactor(bb)/maxGenerator(bb))*100;
        Log.d("Matrix Radius", String.valueOf(standard));
        if(standard>=0 && standard<25){
            BusinessSearch.radius = 1000;
        }

        else if(standard>=25 && standard<50){
            BusinessSearch.radius = 5000;
        }

        else if(standard>=50 && standard<100) {
            BusinessSearch.radius = 10000;
        }
    }

    public double calculateStandardForRatings(HashMap<String, Double> bb){
        return (returnCalculationFactor(bb)/maxGenerator(bb))*100;
    }

    public double calculateStandardForReviews(HashMap<String, Double> bb){
        return (returnCalculationFactor(bb)/maxGenerator(bb))*100;
    }
}
