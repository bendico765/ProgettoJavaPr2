/*
  eccezione che viene lanciata per segnalare che un determinato
  post non esiste nel sistema
*/
public class PostDoesNotExist extends Exception{
  public PostDoesNotExist(){
    super();
  }
  public PostDoesNotExist(String msg){
    super(msg);
  }
}
