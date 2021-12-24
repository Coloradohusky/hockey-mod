package net.fabricmc.example;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static net.minidev.json.parser.JSONParser.*;

public class JerseyJSONParser {
    public static ArrayList getData() throws FileNotFoundException, ParseException {
        JSONParser parser = new JSONParser(DEFAULT_PERMISSIVE_MODE);
        Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + "\\..\\src\\main\\resources\\jerseysTest.json"));
        //Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + "\\src\\main\\resources\\jerseysTest.json"));
        JSONArray leagueNames = JsonPath.read(obj, "$..leagueName");
        ArrayList fullNames = new ArrayList<>();
        for (Object league : leagueNames) {
            JSONArray teamNames = JsonPath.read(obj, "$..[?(@.leagueName=='" + league + "')]..name");
            for (Object teamName : teamNames) {
                JSONArray tempJerseys = (JsonPath.read(obj, "$..[?(@.name=='" + teamName + "')]..jersey")); //goodness, I hate java
                JSONArray jerseys = (JSONArray) tempJerseys.get(0);
                for (Object jersey : jerseys) {
                    fullNames.add((String) (league + "/" + teamName + "/" + jersey));
                }
            }
        }
        return fullNames;
    }
    public static void main(String[] args) throws FileNotFoundException, ParseException {
        System.out.println(getData());
    }
}