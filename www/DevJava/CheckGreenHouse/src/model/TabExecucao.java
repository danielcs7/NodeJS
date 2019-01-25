public enum TabExecucao {
	
    //PRODUCAO
     TAB1  ("check_greenhouse");
 
     
     //*************************************8
     //DEV
   
     private final String tabExecucao;
 
     TabExecucao(String tabExecucao) {
         this.tabExecucao = tabExecucao;
     }
     
     public String getTabExecucao() {
         return this.tabExecucao;
     }
     
     
     
 
 }