package com.apps.meirovichomer.triviagame;

import com.apps.meirovichomer.triviagame.retroClasses.QuestionsClass;
import com.apps.meirovichomer.triviagame.retroClasses.SessionsClass;
import com.apps.meirovichomer.triviagame.retroClasses.TopSessionClass;
import com.apps.meirovichomer.triviagame.retroClasses.createSessionClass;
import com.apps.meirovichomer.triviagame.retroClasses.createUserClass;
import com.apps.meirovichomer.triviagame.retroClasses.setNewNameClass;
import com.apps.meirovichomer.triviagame.retroClasses.suggestQuestionClass;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Retrofit Trivia requests, all using a @QueryMap (Map <String,String>).
 */

public interface RetrofitTriviaInterface {

    @GET("Trivia/")
    Call<createUserClass> createUser(@QueryMap(encoded = true) Map<String, String> options);

    @GET("Trivia/")
    Call<createSessionClass> createSession(@QueryMap(encoded = true) Map<String, String> options);

    @GET("Trivia/")
    Call<QuestionsClass> getRandomTen(@QueryMap(encoded = true) Map<String, String> options);

    @GET("Trivia/")
    Call<SessionsClass> getSessionInfo(@QueryMap(encoded = true) Map<String, String> options);

    @GET("Trivia/")
    Call<TopSessionClass> getTopFive(@QueryMap(encoded = true) Map<String, String> options);

    @GET("Trivia/")
    Call<setNewNameClass> setNewName(@QueryMap(encoded = true) Map<String, String> options);

    @GET("TriviaSuggest/")
    Call<suggestQuestionClass> suggestQuestion(@QueryMap(encoded = true) Map<String, String> options);


}
