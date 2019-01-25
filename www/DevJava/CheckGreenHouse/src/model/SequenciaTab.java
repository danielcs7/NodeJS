package model;

public enum SequenciaTab {


    //PRODUCAO
    SEQ1  ("check_greenhouses_id_seq"); 
   
	//**********************************************************************************8
	//DEV
     /*SEQ1  ("mvhf_arduino.check_greenhouses_id_seq");
	 */
	    
    
    
    
    private final String seqTabela;

    SequenciaTab(String seqTabela) {
        this.seqTabela = seqTabela;
    }
    
    public String getSeqTabela() {
        return this.seqTabela;
    }
    
    
    
}
