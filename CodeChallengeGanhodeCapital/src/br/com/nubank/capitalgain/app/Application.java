package br.com.nubank.capitalgain.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.nubank.capitalgain.domain.model.Operation;
import br.com.nubank.capitalgain.service.OperationService;

public class Application {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		StringBuilder jsonInput = new StringBuilder();
		OperationService service = new OperationService();

		System.out.println("Digite o JSON e Ctrl+Z (Windows) para finalizar:");

		while (scanner.hasNext()) {
			jsonInput.append(scanner.nextLine().trim());
		}
		scanner.close();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<Operation> allOperations = new ArrayList<>();
			String input = jsonInput.toString();

			while (!input.isEmpty()) {
				int endIndex = input.indexOf("]") + 1;
				if (endIndex == 0) {
					break;
				} else {

					String jsonArray = input.substring(0, endIndex);
					input = input.substring(endIndex).trim();

					List<Operation> operations = objectMapper.readValue(jsonArray,
							objectMapper.getTypeFactory().constructCollectionType(List.class, Operation.class));

					allOperations.addAll(service.taxCalculate(operations));
				}
			}

			for (Operation op : allOperations) {
				System.out.println("Operation: " + op.operation);
				System.out.println("Unit Cost: " + op.unitCost);
				System.out.println("Quantity: " + op.quantity);
				System.out.println("--------------------");
			}
		} catch (IOException e) {
			System.err.println("Erro ao processar JSON: " + e.getMessage());
		}
	}
}
