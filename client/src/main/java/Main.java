
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import service.core.Application;
import service.core.ClientInfo;
import service.core.Quotation;

import java.io.IOException;
import java.text.NumberFormat;


public class Main {

	public static void main(String[] args) {

		// NOTE: The way this client is being used, brokers 'GET applications/' is never used
		// Application objects are received in the response body of the POST requests
		// So to avoid redundancy, I am not bothering with the GET calls.

		// The alternative approach would be to send off the POST requests,
		// then afterward make a GET applications request.
 
		// Create the broker and run the test data
		for (ClientInfo info : clients) {
			displayProfile(info);

			Application application = new Application(info);

			try {
				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(info);
				StringEntity stringEntity = new StringEntity(json);

				HttpPost httpPost = new HttpPost("http://localhost:8083/applications");
				httpPost.setEntity(stringEntity);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpPost);

				if (response.getCode() >= 200 || response.getCode() <= 300){
					application = objectMapper.readValue(response.getEntity().getContent(), Application.class);
				} else {
					System.out.println("Warning: Did not get an application response");
				}
				httpClient.close();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			// Retrieve quotations from the broker and display them...
			for(Quotation quotation : application.quotations) {
				displayQuotation(quotation);
			}

			// Print a couple of lines between each client
			System.out.println("\n");
		}

		System.out.println("FINISHED" +
				"\nRemember. Wait 5 seconds before running this client." +
				"\nThis is to ensure that quotation services can successfully POST their services.");
	}
	
	/**
	 * Display the client info nicely.
	 * 
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) + 
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| Weight/Height: " + String.format("%1$-20s", info.weight+"kg/"+info.height+"m") + 
				" | Smoker: " + String.format("%1$-27s", info.smoker?"YES":"NO") +
				" | Medical Problems: " + String.format("%1$-17s", info.medicalIssues?"YES":"NO")+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 * 
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) + 
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	}
	
	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
	};
}
