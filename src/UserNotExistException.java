/*
  eccezione che viene lanciata per segnalare che un determinato
  utente non esiste nel sistema
*/
public class UserNotExistException extends Exception{
  public UserNotExistException(){
    super();
  }
  public UserNotExistException(String msg){
    super(msg);
  }
}
