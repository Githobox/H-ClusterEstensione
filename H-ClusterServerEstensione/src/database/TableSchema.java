package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe TableSchema che si occupa di gestire lo schema di una tabella.
 * La classe TableSchema contiene una lista di oggetti di tipo Column.
 * Ogni oggetto Column contiene il nome e il tipo di un attributo della tabella.
 */
public class TableSchema {
	/**
	 * Attributo db di tipo DbAccess.
	 */
	private DbAccess db;
	/**
	 * Inner class Column che rappresenta un attributo della tabella.
	 * Ogni oggetto Column contiene il nome e il tipo dell'attributo.
	 */
	public class Column{
		/**
		 * Attributo name di tipo String.
		 */
		private String name;
		/**
		 * Attributo type di tipo String.
		 */
		private String type;
		/**
		 * Costruttore della classe Column.
		 * Inizializza gli attributi name e type con i valori passati come parametri.
		 * @param name nome dell'attributo
		 * @param type tipo dell'attributo
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}
		/**
		 * Metodo getColumnName che restituisce il nome dell'attributo.
		 * @return name nome dell'attributo
		 */
		String getColumnName(){
			return name;
		}
		/**
		 * Metodo isNumber che restituisce true se il tipo dell'attributo è "number", false altrimenti.
		 * @return true se il tipo dell'attributo è "number", false altrimenti
		 */
		boolean isNumber(){
			return type.equals("number");
		}
		/**
		 * Metodo toString che restituisce una stringa contenente il nome e il tipo dell'attributo.
		 * @return stringa contenente il nome e il tipo dell'attributo
		 */
		public String toString(){
			return name+":"+type;
		}
	}
	/**
	 * Lista di oggetti di tipo Column.
	 */
	private List<Column> tableSchema=new ArrayList<Column>();
	/**
	 * Costruttore della classe TableSchema.
	 * Inizializza l'attributo db con il database passato come parametro.
	 * Interroga il database per ottenere lo schema della tabella con nome tableName.
	 * In base al tipo di attributo, aggiunge un oggetto Column alla lista tableSchema.
	 * @param db database
	 * @param tableName nome della tabella
	 * @throws SQLException Eccezione lanciata in caso di errore nella interrogazione
	 * @throws DatabaseConnectionException Eccezione lanciata in caso di errore nella connessione al database
	 */
	TableSchema(DbAccess db, String tableName) throws SQLException, DatabaseConnectionException{
		this.db=db;
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");

		 Connection con=db.getConnection();
		 DatabaseMetaData meta = con.getMetaData();
	     ResultSet res = meta.getColumns(null, null, tableName, null);

	     while (res.next()) {
	         if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	        		 tableSchema.add(new Column(
	        				 res.getString("COLUMN_NAME"),
	        				 mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
	        				 );
	      }
	      res.close();
	    }
		/**
		 * Metodo getNumberOfAttributes che restituisce il numero di attributi della tabella.
		 * @return tableSchema.size() numero di attributi della tabella
		 */
		int getNumberOfAttributes(){
			return tableSchema.size();
		}
		/**
		 * Metodo getColumn che restituisce l'attributo della tabella in posizione index.
		 * @param index indice dell'attributo
		 * @return tableSchema.get(index) attributo della tabella in posizione index
		 */
		Column getColumn(int index){
			return tableSchema.get(index);
		}
}




