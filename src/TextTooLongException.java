/*
  eccezione usata per segnalare che il testo di un post è più lungo
  della lunghezza massima consentita
*/
public class TextTooLongException extends Exception{
  public TextTooLongException(){
    super();
  }
  public TextTooLongException(String msg){
    super(msg);
  }
}
