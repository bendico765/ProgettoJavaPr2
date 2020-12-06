/*
  eccezione che viene lanciata per segnalare che un determinato
  utente esiste già nel sistema
*/
public class UserAlreadyExistException extends Exception{
  public UserAlreadyExistException(){
    super();
  }
  public UserAlreadyExistException(String msg){
    super(msg);
  }
}
