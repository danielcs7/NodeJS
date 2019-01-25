package model;

public enum TabRelatorioExec {

	// PRODUCAO
	TAB1("report.tb_relatorio_gerencial_ilhas"), 
	TAB2("report.tb_relatorio_pedagogico_ilhas"),
	TAB3("report.tb_relatorio_ilhas_ltr"), 
	TAB4("report.tb_relatorio_ilhas_fonema"),
	TAB5("report.tb_relatorio_ilhas_caligrf"), 
	TAB6("report.tb_check_log_ilhas");

	private final String tabExecucao;

	TabRelatorioExec(String tabExecucao) {
		this.tabExecucao = tabExecucao;
	}

	public String getTabExecucao() {
		return this.tabExecucao;
	}

}
