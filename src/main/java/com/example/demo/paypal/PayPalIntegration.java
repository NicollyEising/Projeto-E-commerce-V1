	package com.example.demo.paypal;
	
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.OutputStreamWriter;
	import java.net.HttpURLConnection;
	import java.net.URL;
	import java.util.Scanner;
	
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.core.annotation.Order;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.DataOutputStream;
	
	import com.example.demo.dao.UsuarioRepository;
	import com.example.demo.service.ShoppingCartService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
	import com.google.gson.JsonObject;
	import com.google.gson.JsonParser;
	
	import java.util.Base64;
	
	
	
	
	import org.springframework.web.bind.annotation.RestController;
	
	import com.example.demo.paypal.PayPalIntegration;
	import com.example.demo.service.ApprovalUrlService;
	
	@RestController
	@Order(0)
	@RequestMapping("/payment")
	public class PayPalIntegration {
	
		@Autowired
		private static ApprovalUrlService approvalUrlService;
	
		@Autowired
		private ShoppingCartService shoppingCartService;
	
		@Autowired
		private UsuarioRepository usuariorepository;
	
	
	    @Autowired
	    public PayPalIntegration(ApprovalUrlService approvalUrlService) {
	        this.approvalUrlService = approvalUrlService;
	    }
	    static String url;
	    
	
	    private static final String API_BASE_URL = "https://api-m.sandbox.paypal.com/v2";
	    private static final String CLIENT_ID = "AYSzYk9CP5qKe-caEANhrpZLD0rDDiRMvvUM1I4QNC90_p-xIXUxyyim9cWjBjIzttYdQzXkM4ZE-c1_";
	    private static final String CLIENT_SECRET = "EEBcYDretxPPsmGIbsJDRGZp3QCndKGAUnneMJeg1NiL97MVh230RN5RfOcEI2bQhPFXIoo-yQUf7Oly";
	
	    // Método para capturar pagamento de um pedido específico
	    public static void captureOrderPayment(String orderId) throws IOException {
	        // URL de captura de pagamento da API do PayPal
	        String url = API_BASE_URL + "/checkout/orders/" + orderId + "/capture";
	
	        // Configuração da conexão HTTP
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("POST");
	
	        // Headers necessários
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        // Corpo da requisição para captura de pagamento
	        httpConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
	        writer.write("{ \"final_capture\": true }"); // Final_capture indica que é uma captura final
	        writer.flush();
	        writer.close();
	
	        // Executa a requisição e obtém a resposta
	        printResponse(httpConn);
	
	        // Fecha a conexão
	        httpConn.disconnect();
	    }
	
	    // Método para obter o token de acesso OAuth2
	    private static String getAccessToken() throws IOException {
	        String authUrl = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
	        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
	        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
	
	        HttpURLConnection authConn = (HttpURLConnection) new URL(authUrl).openConnection();
	        authConn.setRequestMethod("POST");
	        authConn.setRequestProperty("Authorization", "Basic " + encodedCredentials);
	        authConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        authConn.setDoOutput(true);
	
	        OutputStreamWriter writer = new OutputStreamWriter(authConn.getOutputStream());
	        writer.write("grant_type=client_credentials");
	        writer.flush();
	        writer.close();
	
	        String response = getResponse(authConn);
	
	        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
	        return json.get("access_token").getAsString();
	    }
	
	    // Método para mostrar detalhes de um pedido
	    public static void showOrderDetails(String orderId) throws IOException {
	        // URL para obter detalhes de um pedido na API do PayPal
	        String url = API_BASE_URL + "/checkout/orders/" + orderId;
	
	        // Configuração da conexão HTTP
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("GET");
	
	        // Headers necessários
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        // Executa a requisição e obtém a resposta
	        printResponse(httpConn);
	
	        // Fecha a conexão
	        httpConn.disconnect();
	    }
	
	    // Método para confirmar um pedido
	    public static void confirmOrder(String orderId) throws IOException {
	        // URL para confirmar um pedido na API do PayPal
	        String url = API_BASE_URL + "/checkout/orders/" + orderId + "/confirm";
	
	        // Configuração da conexão HTTP
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("POST");
	
	        // Headers necessários
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        // Corpo da requisição para confirmação do pedido
	        httpConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
	        writer.write("{ }"); // Corpo vazio, pois a confirmação pode não exigir payload específico
	        writer.flush();
	        writer.close();
	
	        // Executa a requisição e obtém a resposta
	        printResponse(httpConn);
	
	        // Fecha a conexão
	        httpConn.disconnect();
	    }
	    
	    // Método para autorizar o pagamento de um pedido
	    public static void authorizePaymentForOrder(String orderId) throws IOException {
	        // URL para autorizar pagamento de um pedido na API do PayPal
	        String url = API_BASE_URL + "/checkout/orders/" + orderId + "/authorize";
	
	        // Configuração da conexão HTTP
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("POST");
	
	        // Headers necessários
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        // Corpo da requisição para autorização de pagamento
	        httpConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
	        writer.write("{ }"); // Corpo vazio, pois a autorização pode não exigir payload específico
	        writer.flush();
	        writer.close();
	
	        // Executa a requisição e obtém a resposta
	        printResponse(httpConn);
	
	        // Fecha a conexão
	        httpConn.disconnect();
	    }
	    
	    // Método para capturar pagamento de um pedido
	    public static void capturePaymentForOrder(String orderId) throws IOException {
	        // URL para capturar pagamento de um pedido na API do PayPal
	        String url = API_BASE_URL + "/checkout/orders/" + orderId + "/capture";
	
	        // Configuração da conexão HTTP
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("POST");
	
	        // Headers necessários
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        // Corpo da requisição para captura de pagamento
	        httpConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
	        writer.write("{ \"final_capture\": true }"); // Final_capture indica que é uma captura final
	        writer.flush();
	        writer.close();
	
	        // Executa a requisição e obtém a resposta
	        printResponse(httpConn);
	
	        // Fecha a conexão
	        httpConn.disconnect();
	    }
	    
	    // Método para adicionar informações de rastreamento para um pedido
	    public static void addTrackingInfoForOrder(String orderId, String carrierName, String trackingNumber) throws IOException {
	        // URL para adicionar informações de rastreamento para um pedido na API do PayPal
	        String url = API_BASE_URL + "/checkout/orders/" + orderId + "/tracking";
	
	        // Configuração da conexão HTTP
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("POST");
	
	        // Headers necessários
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        // Corpo da requisição para adicionar informações de rastreamento
	        JsonObject requestBody = new JsonObject();
	        requestBody.addProperty("carrier_name", carrierName);
	        requestBody.addProperty("tracking_number", trackingNumber);
	
	        httpConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
	        writer.write(requestBody.toString());
	        writer.flush();
	        writer.close();
	
	        // Executa a requisição e obtém a resposta
	        printResponse(httpConn);
	
	        // Fecha a conexão
	        httpConn.disconnect();
	    }
	    
	    // Método para atualizar ou cancelar informações de rastreamento para um pedido
	    public static void updateOrCancelTrackingInfoForOrder(String orderId, String carrierName, String trackingNumber, boolean cancel) throws IOException {
	        String url = API_BASE_URL + "/checkout/orders/" + orderId + "/tracking";
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("PATCH");
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        JsonObject requestBody = new JsonObject();
	        requestBody.addProperty("carrier_name", carrierName);
	        requestBody.addProperty("tracking_number", trackingNumber);
	        requestBody.addProperty("action", cancel ? "CANCEL" : "UPDATE");
	
	        httpConn.setDoOutput(true);
	        try (OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream())) {
	            writer.write(requestBody.toString());
	            writer.flush();
	        }
	
	        // Executa a requisição e obtém a resposta
	        printResponse(httpConn);
	
	        // Fecha a conexão
	        httpConn.disconnect();
	    }
	    
	
	
	    // Método utilitário para obter a resposta da requisição HTTP
	    private static String getResponse(HttpURLConnection httpConn) throws IOException {
	        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
	                ? httpConn.getInputStream()
	                : httpConn.getErrorStream();
	        Scanner scanner = new Scanner(responseStream);
	        String responseBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
	        scanner.close();
	        return responseBody;
	    }
	    
	 // Método para criar uma ordem de checkout
	    public static String createOrder(String currencyCode, String value) throws IOException {
	        // URL para criar uma ordem de checkout na API do PayPal
	        String url = API_BASE_URL + "/checkout/orders";
	
	        // Configuração da conexão HTTP
	        HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
	        httpConn.setRequestMethod("POST");
	
	        // Headers necessários
	        httpConn.setRequestProperty("Content-Type", "application/json");
	        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken());
	
	        // Corpo da requisição para criar uma ordem de checkout
	        httpConn.setDoOutput(true);
	        DataOutputStream writer = new DataOutputStream(httpConn.getOutputStream());
	        String body = "{\n" +
	                "  \"intent\": \"CAPTURE\",\n" +
	                "  \"purchase_units\": [\n" +
	                "    {\n" +
	                "      \"reference_id\": \"PUHF\",\n" +
	                "      \"amount\": {\n" +
	                "        \"currency_code\": \"" + currencyCode + "\",\n" +
	                "        \"value\": \"" + value + "\"\n" +
	                "      }\n" +
	                "    }\n" +
	                "  ],\n" +
	                "  \"application_context\": {\n" +
	                "    \"return_url\": \"\",\n" +
	                "    \"cancel_url\": \"\"\n" +
	                "  }\n" +
	                "}";
	        writer.writeBytes(body);
	        writer.flush();
	        writer.close();
	
	        // Executa a requisição e obtém a resposta
	        String responseBody = printResponse(httpConn);
	        System.out.println("ok");
	        // Fecha a conexão
	        httpConn.disconnect();
	
	        // Parseia a resposta para obter o orderId
	        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
	        String orderId = jsonResponse.get("id").getAsString();
	       
	        
	
	        
	        String approvalUrl = getOrderLink(jsonResponse);
	
	       
	
	        return orderId;
	    }
	    
	    public static String getOrderLink(@RequestParam JsonObject jsonResponse) {
	        String approvalUrl = null;

	        // Verifica se o elemento "links" existe e é um array
	        if (jsonResponse.has("links") && jsonResponse.get("links").isJsonArray()) {
	            JsonArray linksArray = jsonResponse.getAsJsonArray("links");

	            // Itera pelo array para encontrar a primeira URL de aprovação
	            for (JsonElement element : linksArray) {
	                JsonObject linkObject = element.getAsJsonObject();
	                String linkRel = linkObject.get("rel").getAsString();

	                // Verifica se é a URL de aprovação
	                if ("approve".equals(linkRel)) {
	                    approvalUrl = linkObject.get("href").getAsString();
	                }
	            }
	        }

	        // Se não encontrou nenhuma URL de aprovação
	        if (approvalUrl != null) {
	        	 url = approvalUrl;
	        	String focp = getUrl();
	            System.out.println("Approval URL: " + url); // Para debug
	            return url;
	        } else {
	            String error = "erro";
	            System.out.println("Approval URL not found"); // Para debug
	            return error; // Retorna "erro" se não encontrar a URL de aprovação
	        }
	    }
	  
	    
	    
	    @GetMapping("/getLinks")
		static String getUrl() {
		return url;
	}
	    	
	    

	    
	    // Método utilitário para imprimir a resposta da requisição HTTP e retornar como string
	    private static String printResponse(HttpURLConnection httpConn) throws IOException {
	        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
	                ? httpConn.getInputStream()
	                : httpConn.getErrorStream();
	        Scanner scanner = new Scanner(responseStream);
	        String responseBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
	        scanner.close();
	        System.out.println(responseBody); // Opcional: imprimir a resposta
	        return responseBody;
	    }
	    
	    
	
	    }
