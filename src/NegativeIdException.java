/*
  eccezione che viene lanciata quando si prova ad effettuare una qualsiasi
  operazione riguardando un post usando un id negativo
*/
public class NegativeIdException extends Exception{
  public NegativeIdException(){
    super();
  }
  public NegativeIdException(String msg){
    super(msg);
  }
}
