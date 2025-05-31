package org.mat.it.tester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mat.it.tester.generator.ClassGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
//        ClassGenerator.teste(unitSourceGenerator);
        Gato fred = new Gato("fred",
                "laranja",
                List.of(new Gato("fred filho 1", "preto", null, List.of("sobrenome fred")),
                        new Gato("fred filho 2", "azula", null, List.of("sobrenome fred filho 2"))),
                List.of("fred sobrenomes"),
                List.of(1,2,3),
                23);
        Gato fredFilho = new Gato("fred",
                "rosa",
                List.of(new Gato("fred filho", "preto", null, null),
                        new Gato("fred filho 2", "azul", null, List.of("sobrenome fred filho 2"))),
                List.of("fred sobrenomes2"),
                List.of(1,10,3),
                23);

        Map<String, Object> result = new HashMap<>();
        ResultComparator.compare(fred, fredFilho, result);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        System.out.println(gson.toJson(result));
    }

    public static class Gato {

        private String nome;
        private String cor;
        private List<Gato> filhos;
        private List<String> sobrenomes;
        private List<Integer> numeros;
        private Integer idade;

        public Gato(String nome, String cor, List<Gato> filhos, List<String> sobrenomes) {
            this.nome = nome;
            this.cor = cor;
            this.filhos = filhos;
            this.sobrenomes = sobrenomes;
        }

        public Gato(String nome, String cor, List<Gato> filhos, List<String> sobrenomes, List<Integer> numeros, Integer idade) {
            this.nome = nome;
            this.cor = cor;
            this.filhos = filhos;
            this.sobrenomes = sobrenomes;
            this.numeros = numeros;
            this.idade = idade;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCor() {
            return cor;
        }

        public void setCor(String cor) {
            this.cor = cor;
        }

        public List<Gato> getFilhos() {
            return filhos;
        }

        public void setFilhos(List<Gato> filhos) {
            this.filhos = filhos;
        }

        public List<String> getSobrenomes() {
            return sobrenomes;
        }

        public void setSobrenomes(List<String> sobrenomes) {
            this.sobrenomes = sobrenomes;
        }

        public List<Integer> getNumeros() {
            return numeros;
        }

        public void setNumeros(List<Integer> numeros) {
            this.numeros = numeros;
        }

        public Integer getIdade() {
            return idade;
        }

        public void setIdade(Integer idade) {
            this.idade = idade;
        }
    }

}
