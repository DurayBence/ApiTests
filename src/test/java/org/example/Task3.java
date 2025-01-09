package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

//Task
/*
There is a REST API for getting information by country: https://restcountries.com/

- Write a method that will find all language codes obtained from https://restcountries.com/v3/all
- Write a method that will find the number of people for whom the specified language is the official language.
*/

public class Task3 {
    private static final HashMap<String, String> headers = new HashMap<>();

    static Set<String> getAllLanguageCodes() {
        //Get json https://restcountries.com/v3/all
        //For every country get list of languages
        //Return list of unique languages

        String url = "https://restcountries.com/v3/all";
        Set<String> languages = new HashSet<>();
        Response response = RestAssured.given().headers(headers).get(url);
        JsonPath jsonPath = response.jsonPath();

        //split removes all the long language names (checking for spaces and non-ascii chars)
        jsonPath.getList("languages").stream()
                .map(String::valueOf)
                .distinct()
                .map(s -> s.replace("{", ""))
                .map(s -> s.split("=[\\w\\s]+, |=[\\w\\s]+[^[:ascii:]][\\w\\s]+}"))
                .map(Arrays::asList)
                .forEach(languages::addAll);
        return languages;
    }

    static HashMap<String, Long> getSpeakerCount(List<String> languages) {
        //The method takes as a parameter a list of language codes
        //For each language code get json from https://restcountries.com/v3/lang/[language code]
        //In the received json, summarize the population values
        //Return a HashMap with pairs: language code, sum of population for language

        String url = "https://restcountries.com/v3/lang/";
        HashMap<String, Long> result = new HashMap<>();
        for (String lang : languages) {
            Response response = RestAssured.given().headers(headers).get(url + lang);
            JsonPath jsonPath = response.jsonPath();

            AtomicLong speakers = new AtomicLong(0L);
            jsonPath.getList("population").stream()
                    .mapToLong(o -> Integer.parseInt(String.valueOf(o)))
                    .forEach(speakers::addAndGet);
            result.put(lang, speakers.get());
        }
        return result;
    }

    public static void main(String[] args) {
        headers.put("User-Agent", "Learning Automation");
        //Task 1
        Set<String> languageCodes = getAllLanguageCodes();
        System.out.println("Found language codes: " + languageCodes);
        System.out.println("No. of codes: " + languageCodes.size());
        System.out.println("Handles multi-word and non-ascii: " + languageCodes.contains("rar")); //"Cook Islands Māori" was used as a splitting point

        //Task 2
        List<String> languageInput = List.of("eng", "niu");
        HashMap<String, Long> speakerOfLanguages = getSpeakerCount(languageInput);
        for (String s : speakerOfLanguages.keySet()) {
            System.out.println(s + "\t" + speakerOfLanguages.get(s));
        }
    }
}

