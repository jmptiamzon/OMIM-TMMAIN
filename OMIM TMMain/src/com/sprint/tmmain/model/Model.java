package com.sprint.tmmain.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class Model {
	
	public Map<String, Double> runQuery(String parameters, String orgId, String subinvCode) {
		Map<String, Double> record = new HashMap<>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = 
					DriverManager.getConnection("", "", "");
			
			PreparedStatement statement = conn.prepareStatement(
					"SELECT msi.segment1, msi.organization_id,moq.subinventory_code, SUM (moq.transaction_quantity) total " + 
					"FROM apps.mtl_system_items_b msi, apps.mtl_onhand_quantities moq " + 
					"WHERE moq.organization_id = msi.organization_id " + 
					"AND moq.inventory_item_id = msi.inventory_item_id " + 
					"and moq.ORGANIZATION_ID = '" + orgId + "' " + 
					"and moq.subinventory_code='" + subinvCode + "' " +
					"and msi.SEGMENT1 in (" + 
					parameters + ") " +
					"group by msi.segment1, msi.organization_id,moq.subinventory_code"
			);
			
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				record.put(rs.getString(1), rs.getDouble(4));
			}
			
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Driver error: " + e.getMessage());
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
			
		}
		
		return record;
	}
}
