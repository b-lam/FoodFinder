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
                    case ("Personality")://Big5
                        Log.d("PERSONALITY","Personality branch is iterating");
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

private void setBig5 (String id, JSONObject dataObj2) throws JSONException {
    switch (id) {
        case ("Agreeableness"):
            Personality.agreeableness = dataObj2.getDouble("percentage");
            Log.d("BIG5",Double.toString(Personality.agreeableness));
            break;
        case ("Conscientiousness"):
            Personality.conscientiousness = dataObj2.getDouble("percentage");
            Log.d("BIG5",Double.toString(Personality.conscientiousness));
            break;
        case ("Extraversion"):
            Personality.extraversion = dataObj2.getDouble("percentage");
            Log.d("BIG5",Double.toString(Personality.extraversion));
            break;
        case ("Openness"):
            Personality.openness = dataObj2.getDouble("percentage");
            Log.d("BIG5",Double.toString(Personality.openness));
            break;
        case ("Neuroticism"):
            Personality.emotionalRange = dataObj2.getDouble("percentage");
            Log.d("BIG5",Double.toString(Personality.emotionalRange));
            break;
        default:
            Log.d("PERSONALITY","UNKNOWN VALUE FOUND");
            break;
    }
}

    private void setValues (String id, JSONObject dataObj3) throws JSONException {
        switch (id){
            case ("Conservation"):
                Personality.tradition = dataObj3.getDouble("percentage");
                Log.d("VALUES",Double.toString(Personality.tradition));
                break;
            case ("Openness to change"):
                Personality.opennessToChange = dataObj3.getDouble("percentage");
                break;
            case ("Hedonism"):
                Personality.hedonism = dataObj3.getDouble("percentage");
                break;
            case ("Self-enhancement"):
                Personality.achievingSuccess = dataObj3.getDouble("percentage");
                break;
            case ("Self-transcendence"):
                Personality.helpingOthers = dataObj3.getDouble("percentage");
                break;
        }
    }

    private void setNeeds (String id, JSONObject dataObj3) throws JSONException {
        switch (id) {
            case "Challenge":
                Personality.challenge = dataObj3.getDouble("percentage");
                Log.d("NEEDS",Double.toString(Personality.challenge));
                break;
            case "Closeness":
                Personality.closeness = dataObj3.getDouble("percentage");
                break;
            case "Curiosity":
                Personality.curiosity = dataObj3.getDouble("percentage");
                break;
            case "Excitement":
                Personality.excitement = dataObj3.getDouble("percentage");
                break;
            case "Harmony":
                Personality.harmony = dataObj3.getDouble("percentage");
                break;
            case "Ideal":
                Personality.ideal = dataObj3.getDouble("percentage");
                break;
            case "Liberty":
                Personality.liberty = dataObj3.getDouble("percentage");
                break;
            case "Love":
                Personality.love = dataObj3.getDouble("percentage");
                break;
            case "Practicality":
                Personality.practicality = dataObj3.getDouble("percentage");
                break;
            case "Self-expression":
                Personality.selfExpression = dataObj3.getDouble("percentage");
                break;
            case "Stability":
                Personality.stability = dataObj3.getDouble("percentage");
                break;
            case "Structure":
                Personality.structure = dataObj3.getDouble("percentage");
                break;
        }
    }
}
