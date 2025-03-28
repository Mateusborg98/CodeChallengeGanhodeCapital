package br.com.nubank.capitalgain.service;

import br.com.nubank.capitalgain.domain.model.Operation;
import br.com.nubank.capitalgain.domain.model.OperationType;

public class OperationService {

	public static Double[] calculateWeightedAverage(Operation operation) {
		Double totalCost = 0.0;
		Double totalSales = 0.0;
		Double average[] = new Double[3];
		int totalQuantity = 0;

		if (operation.equals(OperationType.BUY)) {
			totalCost += operation.getUnitCost();
			totalQuantity += operation.getQuantity();
		} else {
			totalQuantity -= operation.getQuantity();
			totalSales += operation.getUnitCost();
		}

		average[0] = totalCost / totalQuantity;
		average[1] = (double) totalQuantity;
		average[2] = (double) totalSales;
		return average;
	}

}
