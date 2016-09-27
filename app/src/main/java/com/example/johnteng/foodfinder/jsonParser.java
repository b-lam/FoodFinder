package com.example.johnteng.foodfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by johnteng on 16-09-17.
 */

public class jsonParser {
    public jsonParser () {
    }
    public void parseWatson (JSONObject j)  {

        JSONArray dataJsonArray = null;
        JSONArray dataJsonArray2 = null;
        JSONArray dataJsonArray3 = null;


        try {
            JSONObject tree = j.getJSONObject("tree");
            dataJsonArray = tree.getJSONArray("children");
            for (int i = 0; i < dataJsonArray.length(); i++) {
                //Enter first array which selects either Big5, needs, values


                JSONObject dataObj = (JSONObject)dataJsonArray.get(i);
                dataJsonArray2 = dataObj.getJSONArray("children");
                String ID = dataObj.getString("id");
                //Takes different action depending on whether Big5, needs, or values is currently selected
                switch (ID) {
                    case ("personality")://Big5
                        Log.d("PERSONALITY","PersonalityInsights branch is iterating");
                        for (int x = 0; x<dataJsonArray2.length();x++){
                            JSONObject dataObj2 = (JSONObject)dataJsonArray2.get(x);
                            dataJsonArray3 = dataObj2.getJSONArray("children");
                            for (int y = 0; y<dataJsonArray3.length();y++){
                                JSONObject dataObj3 = (JSONObject)dataJsonArray3.get(y);
                                String id = dataObj3.getString("id");
                                setBig5(id, dataObj3);
                            }
                        }
                        break;
                    case ("needs")://needs
                        for (int x = 0; x<dataJsonArray2.length();x++){
                            JSONObject dataObj2 = (JSONObject)dataJsonArray2.get(x);
                            dataJsonArray3 = dataObj2.getJSONArray("children");
                            for (int y = 0; y<dataJsonArray3.length();y++){
                                JSONObject dataObj3 = (JSONObject)dataJsonArray3.get(y);
                                String id = dataObj3.getString("id");
                                setNeeds(id, dataObj3);
                            }
                        }
                        break;
                    case ("values")://values
                        for (int x = 0; x<dataJsonArray2.length();x++){
                            JSONObject dataObj2 = (JSONObject)dataJsonArray2.get(x);
                            dataJsonArray3 = dataObj2.getJSONArray("children");
                            for (int y = 0; y<dataJsonArray3.length();y++){
                                JSONObject dataObj3 = (JSONObject)dataJsonArray3.get(y);
                                String id = dataObj3.getString("id");
                                setValues(id, dataObj3);
                            }
                        }
                        break;
                }
            }//End of outer array


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

private void setBig5 (String id, JSONObject dataObj3) throws JSONException {
    switch (id) {
        case ("Agreeableness"):
            PersonalityInsights.agreeableness = dataObj3.getDouble("percentage");
            Log.d("BIG5",Double.toString(PersonalityInsights.agreeableness));
            break;
        case ("Conscientiousness"):
            PersonalityInsights.conscientiousness = dataObj3.getDouble("percentage");
            Log.d("BIG5",Double.toString(PersonalityInsights.conscientiousness));
            break;
        case ("Extraversion"):
            PersonalityInsights.extraversion = dataObj3.getDouble("percentage");
            Log.d("BIG5",Double.toString(PersonalityInsights.extraversion));
            break;
        case ("Openness"):
            PersonalityInsights.openness = dataObj3.getDouble("percentage");
            Log.d("BIG5",Double.toString(PersonalityInsights.openness));
            break;
        case ("Neuroticism"):
            PersonalityInsights.emotionalRange = dataObj3.getDouble("percentage");
            Log.d("BIG5",Double.toString(PersonalityInsights.emotionalRange));
            break;
        default:
            Log.d("PERSONALITY","UNKNOWN VALUE FOUND");
            break;
    }
}

    private void setValues (String id, JSONObject dataObj3) throws JSONException {
        switch (id){
            case ("Conservation"):
                PersonalityInsights.tradition = dataObj3.getDouble("percentage");
                Log.d("VALUES",Double.toString(PersonalityInsights.tradition));
                break;
            case ("Openness to change"):
                PersonalityInsights.opennessToChange = dataObj3.getDouble("percentage");
                break;
            case ("Hedonism"):
                PersonalityInsights.hedonism = dataObj3.getDouble("percentage");
                break;
            case ("Self-enhancement"):
                PersonalityInsights.achievingSuccess = dataObj3.getDouble("percentage");
                break;
            case ("Self-transcendence"):
                PersonalityInsights.helpingOthers = dataObj3.getDouble("percentage");
                break;
        }
    }

    private void setNeeds (String id, JSONObject dataObj3) throws JSONException {
        switch (id) {
            case "Challenge":
                PersonalityInsights.challenge = dataObj3.getDouble("percentage");
                Log.d("NEEDS",Double.toString(PersonalityInsights.challenge));
                break;
            case "Closeness":
                PersonalityInsights.closeness = dataObj3.getDouble("percentage");
                break;
            case "Curiosity":
                PersonalityInsights.curiosity = dataObj3.getDouble("percentage");
                break;
            case "Excitement":
                PersonalityInsights.excitement = dataObj3.getDouble("percentage");
                break;
            case "Harmony":
                PersonalityInsights.harmony = dataObj3.getDouble("percentage");
                break;
            case "Ideal":
                PersonalityInsights.ideal = dataObj3.getDouble("percentage");
                break;
            case "Liberty":
                PersonalityInsights.liberty = dataObj3.getDouble("percentage");
                break;
            case "Love":
                PersonalityInsights.love = dataObj3.getDouble("percentage");
                break;
            case "Practicality":
                PersonalityInsights.practicality = dataObj3.getDouble("percentage");
                break;
            case "Self-expression":
                PersonalityInsights.selfExpression = dataObj3.getDouble("percentage");
                break;
            case "Stability":
                PersonalityInsights.stability = dataObj3.getDouble("percentage");
                break;
            case "Structure":
                PersonalityInsights.structure = dataObj3.getDouble("percentage");
                break;
        }
    }
}
