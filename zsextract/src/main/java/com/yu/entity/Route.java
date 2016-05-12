package com.yu.entity;

import java.util.LinkedHashMap;

/**
 * A sequence of positions learned from a tracking
 * @author Kenny
 *
 */
public class Route {
	private LinkedHashMap<String, Cell> cells = new LinkedHashMap<String, Cell>();
	
	/**
	 * @param cell
	 * @return boolean. A false value indicates a reversal in the direction and the route should terminate.
	 */
	public boolean extend(Cell cell){
		if (cells.get(cell.geoHash) != null){
			return false;
		}else{
			cells.put(cell.geoHash, cell);
			return true;
		}
	}
	
	/**
	 * Identifies the starting position of the route and removes what is before it.
	 */
	public void trim(){
		
	}
}
