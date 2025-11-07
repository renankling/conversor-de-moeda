package principal;

import com.google.gson.*;
import modelos.Moeda;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HttpClient client = HttpClient.newHttpClient();

        int opcao = 0;

        while (opcao != 7) {
            try {
                System.out.println("\n=== Conversor de Moedas ===");
                System.out.println("1. ARS - Peso argentino -> BRL - Real brasileiro");
                System.out.println("2. BOB - Boliviano boliviano - > COP - Peso colombiano");
                System.out.println("3. BRL - Real brasileiro -> CLP - Peso chileno");
                System.out.println("4. CLP - Peso chileno -> USD - Dólar americano");
                System.out.println("5. COP - Peso colombiano -> BRL - Real brasileiro");
                System.out.println("6. USD - Dólar americano -> BRL - Real brasileiro");
                System.out.println("7. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();

                if (opcao == 7) {
                    System.out.println("Encerrando aplicação...");
                    break;
                }

                String moedaOrigem;
                String moedaDestino;

                switch (opcao) {
                    case 1 -> { moedaOrigem = "ARS"; moedaDestino = "BRL"; }
                    case 2 -> { moedaOrigem = "BOB"; moedaDestino = "COP"; }
                    case 3 -> { moedaOrigem = "BRL"; moedaDestino = "CLP"; }
                    case 4 -> { moedaOrigem = "CLP"; moedaDestino = "USD"; }
                    case 5 -> { moedaOrigem = "COP"; moedaDestino = "BRL"; }
                    case 6 -> { moedaOrigem = "USD"; moedaDestino = "BRL"; }
                    default -> {
                        System.out.println("Opção inválida! Tente novamente.");
                        continue;
                    }
                }

                System.out.print("Digite o valor em " + moedaOrigem + ": ");
                double valor = scanner.nextDouble();

                String url = "https://v6.exchangerate-api.com/v6/f9ef5957d19b5691cfccb93d/pair/"
                        + moedaOrigem + "/" + moedaDestino + "/" + valor;

                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    Moeda moeda = gson.fromJson(response.body(), Moeda.class);

                    System.out.printf("Taxa: %.4f | Resultado: %.2f %s\n",
                            moeda.conversion_rate(), moeda.conversion_result(), moeda.target_code());

                } catch (IOException | InterruptedException e) {
                    System.out.println("Erro ao se comunicar com a API: " + e.getMessage());
                } catch (JsonSyntaxException e) {
                    System.out.println("Erro ao interpretar resposta da API.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Programa finalizado.");
    }
}
