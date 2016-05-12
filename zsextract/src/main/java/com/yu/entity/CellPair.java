package com.yu.entity;

import java.util.List;

/**
 * A successive pair of cells on a route
 * @author Kenny
 *
 */
public class CellPair {
	private Cell c1, c2;

	public CellPair(Cell c1, Cell c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public List<Cell> merge(CellPair cp){
		return null;
	}
	
}
