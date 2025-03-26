package br.com.nubank.capitalgain.service;

import java.util.List;

import br.com.nubank.capitalgain.domain.model.Operation;
import br.com.nubank.capitalgain.domain.model.OperationType;

public class OperationService {

	void taxCalculate(List<Operation> operations) {
		Double newAverage = 0.0;
		Double[] average = new Double[2];

		average = calculateWeightedAverage(operations);
		for (Operation operation : operations) {
			newAverage = ((average[1] * average[0]) + (quantidade - de - acoes - compradas * valor - de - compra))
					/ (quantidade - de - acoes - atual + quantidade - de - acoes - compradas);

		}

	}

	private Double calculateNewWeightedAverage(Double average, Double quantity) {

		return null;
	}

	private static Double[] calculateWeightedAverage(List<Operation> operations) {
		Double totalCost = 0.0;
		Double average[] = new Double[2];
		int totalQuantity = 0;

		for (Operation operation : operations) {
			if (operation.equals(OperationType.BUY)) {
				totalCost += operation.getUnitCost();
				totalQuantity += operation.getQuantity();
			} else {
				totalQuantity -= operation.getQuantity();
			}

		}
		average[0] = totalCost / totalQuantity;
		average[1] = (double) totalQuantity;
		return average;
	}

}
