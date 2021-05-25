package com.thecat;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TesteApi extends MassaDeDados {

    @BeforeClass
    public static void urlBase() {
        RestAssured.baseURI = "https://api.thecatapi.com/v1/";
    }

    @Test
    public void cadastro() {
        Response response = given().contentType("application/json")
            .body(corpoCadastro)
            .when()
            .post(urlCadastro);

        validacao(response);
    }

    @Test
    public void votacao() {
        Response response = given().contentType("application/json")
            .body(corpoVotacao)
            .when()
            .post("votes/");

        validacao(response);

        String id = response.jsonPath().getString("id");
        vote_id = id;

        System.out.println("Vote id: " + vote_id);
        System.out.println("ID -> " + id);
    }

    public void deletaVoto() {
        System.out.println("Vote id: " + vote_id);
        Response response = given().contentType("application/json")
            .header("x-api-key", chave)
            .pathParam("vote_id", vote_id)
            .when()
            .delete("votes/{vote_id}");

        validacao(response);
    }

    @Test
    public void deletaVotacao() {
        votacao();
        deletaVoto();
    }

    @Test
    public void favoritaDesfavorita() {
        favorita();
        desfavorita();
    }

    public void favorita() {
        Response response = given().contentType("application/json")
                .header("x-api-key", chave)
                .body(corpoFavorita)
                .when()
                .post("favourites");

        String id = response.jsonPath().getString("id");
        vote_id = id;

        validacao(response);
    }

    private void desfavorita() {
        Response response = given().contentType("application/json")
                .header("x-api-key", chave)
                .pathParam("favourite_id", vote_id)
                .when()
                .delete("favourites/{favourite_id}");

        validacao(response);
    }

    public void validacao(Response response) {
        response.then().body("message", containsString("SUCCESS")).statusCode(200);
        System.out.println("Retorno da API -> " + response.body().asString());
        System.out.println("................................................");
    }
}