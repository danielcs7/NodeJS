package model;

public enum SeqTabelasRelatExec {

	// PRODUCAO
	SEQ1("report.seq_tb_relat_ger_ilhas"), 
	SEQ2("report.seq_tb_relat_ped_ilhas"), 
	SEQ3("report.seq_tb_relat_ilhas_ltr"),
	SEQ4("report.seq_tb_relat_ilhas_fon"), 
	SEQ5("report.seq_tb_relat_ilhas_caligrf"),
	SEQ6("report.seq_tb_check_log_ilhas");

	private final String seqTabela;

	SeqTabelasRelatExec(String seqTabela) {
		this.seqTabela = seqTabela;
	}

	public String getSeqTabela() {
		return this.seqTabela;
	}

}
