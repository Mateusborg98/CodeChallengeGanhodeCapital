package br.com.nubank.capitalgain.app;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.nubank.capitalgain.domain.model.Operation;
import br.com.nubank.capitalgain.domain.model.OperationType;

public class Application {

	private static double precoMedio = 0.0; // Preço médio ponderado das ações
	private static int quantidadeAcoes = 0; // Quantidade de ações no portfólio
	private static double prejuizoAcumulado = 0.0; // Prejuízo acumulado
	private static final double TAXA_IMPOSTO = 0.20; // 20% de imposto sobre lucro

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		StringBuilder jsonInput = new StringBuilder();

		System.out.println("Digite o JSON e Ctrl+Z (Windows) para finalizar:");

		while (scanner.hasNext()) {
			jsonInput.append(scanner.nextLine().trim());
		}
		scanner.close();

		try {
			// Regex para capturar os blocos JSON
			Pattern pattern = Pattern.compile("\\[.*?\\]", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(jsonInput);

			ObjectMapper objectMapper = new ObjectMapper();

			List<List<Map<String, Double>>> taxResults = new ArrayList<>();

			while (matcher.find()) {
				String jsonBlock = matcher.group();
				Operation[] operationsArray = objectMapper.readValue(jsonBlock, Operation[].class);
				List<Operation> operations = Arrays.asList(operationsArray);

				List<Map<String, Double>> taxList = new ArrayList<>();
				for (Operation op : operations) {
					double tax = (op.getOperation().equalsIgnoreCase(OperationType.BUY.toString()))
							? realizarCompra(op.getQuantity(), op.getUnitCost())
							: realizarVenda(op);

					taxList.add(Collections.singletonMap("tax", tax));
				}
				taxResults.add(taxList);
			}

			// 🔹 Exibir a saída JSON corretamente formatada
			for (List<Map<String, Double>> taxList : taxResults) {
				System.out.println(objectMapper.writeValueAsString(taxList));
			}

		} catch (IOException e) {
			System.err.println("Erro ao processar JSON: " + e.getMessage());
		}
	}

	// ✅ Método refatorado para processar compras
	private static double realizarCompra(int quantidade, double preco) {
		precoMedio = ((quantidadeAcoes * precoMedio) + (quantidade * preco)) / (quantidadeAcoes + quantidade);
		quantidadeAcoes += quantidade;
		return 0.0; // Compra nunca gera imposto
	}

	// ✅ Método refatorado para processar vendas e calcular imposto
	private static double realizarVenda(Operation operation) {
		double valorTotalOperacao = operation.getQuantity() * operation.getUnitCost();
		double imposto = 0.0;

		if (operation.getUnitCost() > precoMedio) { // Venda com lucro
			double lucro = (operation.getUnitCost() - precoMedio) * operation.getQuantity();
			double lucroComPrejuizo = Math.max(0, lucro - prejuizoAcumulado);

			if (valorTotalOperacao > 20000) { // Apenas vende com imposto se a operação for acima de R$ 20.000
				imposto = lucroComPrejuizo * TAXA_IMPOSTO;
			}
			prejuizoAcumulado = Math.max(0, prejuizoAcumulado - lucro);
		} else if (operation.getUnitCost() < precoMedio) { // Venda com prejuízo
			double prejuizo = (precoMedio - operation.getUnitCost()) * operation.getQuantity();
			prejuizoAcumulado += prejuizo;
		}

		quantidadeAcoes -= operation.getQuantity();
		return imposto;
	}
}
