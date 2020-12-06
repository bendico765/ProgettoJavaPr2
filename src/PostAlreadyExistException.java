/*
  eccezione che viene lanciata per segnalare che un determinato
  post esiste già nel sistema
*/
public class PostAlreadyExistException extends Exception{
  public PostAlreadyExistException(){
    super();
  }
  public PostAlreadyExistException(String msg){
    super(msg);
  }
}
