/*
  eccezione che viene lanciata quando un utente prova a mettere
  like ad un suo post
*/
public class AuthorCantLikeHimselfException extends Exception{
  public AuthorCantLikeHimselfException(){
    super();
  }
  public AuthorCantLikeHimselfException(String msg){
    super(msg);
  }
}
