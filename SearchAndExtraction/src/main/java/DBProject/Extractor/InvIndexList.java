package DBProject.Extractor;

import java.util.ArrayList;

public class InvIndexList {
	private ArrayList<InvertedIndexEntry> entries;
	DatabaseConnector dbc;

	public InvIndexList(DatabaseConnector dbc) {
		entries = new ArrayList<InvertedIndexEntry>();
		this.dbc = dbc;
	}

	public void addToList(InvertedIndexEntry entry) {
		entries.add(entry);
		if (entries.size() > 499) {
			dbc.addToIITable(entries);
			entries = new ArrayList<InvertedIndexEntry>();
		}
	}

	public void addRemainingRecords() {
		dbc.addToIITable(entries);
		entries = new ArrayList<InvertedIndexEntry>();
	}

	public void addToList(String word, int nodeID) {
		this.addToList(new InvertedIndexEntry(word, nodeID));
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (InvertedIndexEntry e : entries){
			sb.append(e.toString() + "\n");
		}
		return sb.toString();
	}

}
